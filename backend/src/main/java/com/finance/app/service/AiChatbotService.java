package com.finance.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.app.dto.budget.BudgetStatusDto;
import com.finance.app.dto.chatbot.ChatMessageDto;
import com.finance.app.dto.chatbot.ChatResponse;
import com.finance.app.dto.dashboard.DashboardOverviewDto;
import com.finance.app.dto.transaction.CategoryExpenseDto;
import com.finance.app.dto.transaction.TransactionResponse;
import com.finance.app.entity.ChatMessage;
import com.finance.app.entity.User;
import com.finance.app.repository.ChatMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AiChatbotService {

    private static final Logger log = LoggerFactory.getLogger(AiChatbotService.class);

    private final ChatMessageRepository chatMessageRepository;
    private final AuthService authService;
    private final DashboardService dashboardService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.gemini.api-key:}")
    private String geminiApiKey;

    @Value("${app.gemini.model:gemini-1.5-flash}")
    private String geminiModel;

    public AiChatbotService(ChatMessageRepository chatMessageRepository,
                            AuthService authService,
                            DashboardService dashboardService) {
        this.chatMessageRepository = chatMessageRepository;
        this.authService = authService;
        this.dashboardService = dashboardService;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public List<ChatMessageDto> getChatHistory() {
        User user = authService.getAuthenticatedUser();
        return chatMessageRepository.findByUserIdOrderByCreatedAtAsc(user.getId())
                .stream().map(ChatMessageDto::new).collect(Collectors.toList());
    }

    @Transactional
    public void clearHistory() {
        User user = authService.getAuthenticatedUser();
        chatMessageRepository.deleteByUserId(user.getId());
    }

    @Transactional
    public ChatResponse processChat(String userQuery) {
        User user = authService.getAuthenticatedUser();

        // 1. Save user's question to chat history
        chatMessageRepository.save(new ChatMessage(user, "user", userQuery.trim()));

        // 2. Fetch live financial data context for this user
        DashboardOverviewDto snapshot = dashboardService.getDashboardOverview();
        String financialContext = buildFinancialContext(user, snapshot);

        String replyText;
        boolean fallback = false;

        // 3. Check if Gemini API Key is configured
        if (geminiApiKey != null && !geminiApiKey.isBlank() && !geminiApiKey.equals("YOUR_GEMINI_API_KEY")) {
            try {
                replyText = callGeminiApi(userQuery, financialContext);
            } catch (Exception e) {
                log.warn("Gemini API call failed: {}. Falling back to dynamic financial engine.", e.getMessage());
                replyText = generateContextualFinancialAnswer(userQuery, snapshot, user);
                fallback = true;
            }
        } else {
            // Intelligent dynamic financial analyst utilizing real records
            replyText = generateContextualFinancialAnswer(userQuery, snapshot, user);
            fallback = true;
        }

        // 4. Save assistant reply to chat history
        chatMessageRepository.save(new ChatMessage(user, "assistant", replyText));

        return new ChatResponse(replyText, fallback);
    }

    private String buildFinancialContext(User user, DashboardOverviewDto d) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("User: %s (Email: %s)\n", user.getFullName(), user.getEmail()));
        sb.append(String.format("Current Date: %s\n", LocalDate.now()));
        sb.append(String.format("Total All-Time Income: ₹%s\n", d.getTotalIncomeAllTime()));
        sb.append(String.format("Total All-Time Expenses: ₹%s\n", d.getTotalExpenseAllTime()));
        sb.append(String.format("Net All-Time Savings: ₹%s\n", d.getTotalSavingsAllTime()));
        sb.append(String.format("Current Month Income: ₹%s\n", d.getCurrentMonthIncome()));
        sb.append(String.format("Current Month Expense: ₹%s\n", d.getCurrentMonthExpense()));
        sb.append(String.format("Current Month Savings: ₹%s (Savings Rate: %.1f%%)\n",
                d.getCurrentMonthSavings(), d.getCurrentMonthSavingsRate() != null ? d.getCurrentMonthSavingsRate() : 0.0));

        sb.append("\nCurrent Month Expenses by Category:\n");
        if (d.getCategoryBreakdownCurrentMonth() != null && !d.getCategoryBreakdownCurrentMonth().isEmpty()) {
            for (CategoryExpenseDto c : d.getCategoryBreakdownCurrentMonth()) {
                sb.append(String.format("- %s: ₹%s (%.1f%% of monthly expenses)\n", c.getCategory(), c.getAmount(), c.getPercentage()));
            }
        } else {
            sb.append("- No category expenses recorded this month yet.\n");
        }

        sb.append("\nActive Budgets and Status:\n");
        if (d.getBudgetStatuses() != null && !d.getBudgetStatuses().isEmpty()) {
            for (BudgetStatusDto b : d.getBudgetStatuses()) {
                sb.append(String.format("- %s Budget: Limit ₹%s | Spent ₹%s | Remaining ₹%s | Utilization: %.1f%% | Status: %s\n",
                        b.getCategory(), b.getMonthlyLimit(), b.getSpentAmount(), b.getRemainingAmount(), b.getPercentageSpent(), b.getStatusLevel()));
            }
        } else {
            sb.append("- No monthly budgets configured yet.\n");
        }

        sb.append("\nRecent Transactions:\n");
        if (d.getRecentTransactions() != null && !d.getRecentTransactions().isEmpty()) {
            for (TransactionResponse t : d.getRecentTransactions()) {
                sb.append(String.format("- [%s] %s %s: ₹%s (%s on %s)\n",
                        t.getType(), t.getTitle(), t.getCategory(), t.getAmount(),
                        t.getDescription() != null ? t.getDescription() : "", t.getTransactionDate()));
            }
        } else {
            sb.append("- No recent transactions.\n");
        }

        return sb.toString();
    }

    private String callGeminiApi(String userPrompt, String financialContext) throws Exception {
        String url = String.format("https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s",
                geminiModel, geminiApiKey);

        String systemInstruction = "You are an intelligent, friendly, and precise personal financial assistant in the 'Finance Management System'.\n" +
                "You have access to the user's authentic, live financial records provided below:\n" +
                financialContext + "\n" +
                "GUIDELINES:\n" +
                "1. When answering personal financial questions (e.g., 'How much did I spend?', 'Am I over budget?', 'What is my total income?'), use the exact numbers from the user's records above.\n" +
                "2. Provide clear, empathetic, and actionable financial advice on savings, loan EMIs, fixed deposits, and taxes.\n" +
                "3. Format your answers clearly using markdown bullet points and bold text where helpful.\n" +
                "4. Keep explanations professional, concise, and helpful.";

        Map<String, Object> textPart = Map.of("text", userPrompt);
        Map<String, Object> content = Map.of("role", "user", "parts", List.of(textPart));

        Map<String, Object> systemInstructionPart = Map.of("text", systemInstruction);
        Map<String, Object> systemContent = Map.of("parts", List.of(systemInstructionPart));

        Map<String, Object> requestPayload = Map.of(
                "systemInstruction", systemContent,
                "contents", List.of(content)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestPayload, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode candidates = root.path("candidates");
            if (candidates.isArray() && candidates.size() > 0) {
                JsonNode parts = candidates.get(0).path("content").path("parts");
                if (parts.isArray() && parts.size() > 0) {
                    return parts.get(0).path("text").asText();
                }
            }
        }

        throw new RuntimeException("Unexpected response structure from Gemini API");
    }

    private String generateContextualFinancialAnswer(String query, DashboardOverviewDto data, User user) {
        String q = query.toLowerCase();

        // 1. Spend this month
        if (q.contains("spend") || q.contains("spent") || q.contains("expense")) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("### 💳 Monthly Spending Breakdown\n\n"));
            sb.append(String.format("You have spent **₹%s** so far this month.\n\n", data.getCurrentMonthExpense()));

            if (data.getCategoryBreakdownCurrentMonth() != null && !data.getCategoryBreakdownCurrentMonth().isEmpty()) {
                sb.append("**Breakdown by Category:**\n");
                for (CategoryExpenseDto cat : data.getCategoryBreakdownCurrentMonth()) {
                    sb.append(String.format("- **%s**: ₹%s (%.1f%%)\n", cat.getCategory(), cat.getAmount(), cat.getPercentage()));
                }
            }
            return sb.toString();
        }

        // 2. Total Income
        if (q.contains("income") || q.contains("earn") || q.contains("salary")) {
            return String.format("### 💰 Income Summary\n\n" +
                            "- **This Month's Income**: ₹%s\n" +
                            "- **Total Lifetime Income**: ₹%s\n" +
                            "- **Current Month Net Savings**: ₹%s (Savings Rate: %.1f%%)\n\n" +
                            "Keep your savings rate above 20%% for steady wealth building!",
                    data.getCurrentMonthIncome(), data.getTotalIncomeAllTime(),
                    data.getCurrentMonthSavings(), data.getCurrentMonthSavingsRate() != null ? data.getCurrentMonthSavingsRate() : 0.0);
        }

        // 3. Highest expense category
        if (q.contains("highest") || q.contains("top category") || q.contains("most")) {
            if (data.getCategoryBreakdownCurrentMonth() != null && !data.getCategoryBreakdownCurrentMonth().isEmpty()) {
                CategoryExpenseDto top = data.getCategoryBreakdownCurrentMonth().get(0);
                return String.format("### 📊 Highest Expense Category\n\n" +
                                "Your highest expense category this month is **%s** at **₹%s**, accounting for **%.1f%%** of your total monthly expenditures.",
                        top.getCategory(), top.getAmount(), top.getPercentage());
            } else {
                return "You haven't recorded any expenses for this month yet.";
            }
        }

        // 4. Budget status
        if (q.contains("budget") || q.contains("exceed") || q.contains("limit")) {
            if (data.getBudgetStatuses() == null || data.getBudgetStatuses().isEmpty()) {
                return "You haven't set any monthly budgets yet. Head over to the **Budgets** section to set monthly limits for your categories.";
            }
            StringBuilder sb = new StringBuilder("### 🎯 Budget Status Tracking\n\n");
            long exceededCount = data.getBudgetStatuses().stream().filter(b -> Boolean.TRUE.equals(b.getExceeded())).count();
            if (exceededCount > 0) {
                sb.append(String.format("⚠️ **Attention:** You have exceeded your budget in **%d category(s)**!\n\n", exceededCount));
            } else {
                sb.append("✅ Great job! None of your category budgets are exceeded this month.\n\n");
            }

            for (BudgetStatusDto b : data.getBudgetStatuses()) {
                String icon = b.getExceeded() ? "🔴 Exceeded" : (b.getStatusLevel().equals("WARNING") ? "🟡 Near Limit" : "🟢 On Track");
                sb.append(String.format("- **%s**: Spent ₹%s / Limit ₹%s (%.1f%%) — %s\n",
                        b.getCategory(), b.getSpentAmount(), b.getMonthlyLimit(), b.getPercentageSpent(), icon));
            }
            return sb.toString();
        }

        // 5. Save more money / advice
        if (q.contains("save") || q.contains("saving") || q.contains("advice") || q.contains("tips")) {
            return "### 💡 Smart Financial Tips to Boost Savings\n\n" +
                    "1. **Follow the 50/30/20 Rule**: Allocate 50% of income to Needs, 30% to Wants, and at least 20% to Savings & Investments.\n" +
                    "2. **Track Discretionary Spending**: Check categories like Dining Out and Shopping in your Transactions tab.\n" +
                    "3. **Build an Emergency Fund**: Stash 3 to 6 months of basic living expenses into a High-Yield Savings Account or Liquid FD.\n" +
                    "4. **Automate Savings**: Set up an auto-debit on salary day directly into a recurring deposit or mutual fund SIP.";
        }

        // 6. EMI / Loan explanation
        if (q.contains("emi") || q.contains("loan")) {
            return "### 🏦 Understanding Loan EMI (Equated Monthly Installment)\n\n" +
                    "Loan EMI is calculated using the standard reducing balance formula:\n\n" +
                    "$$\\text{EMI} = \\frac{P \\times r \\times (1+r)^n}{(1+r)^n - 1}$$\n\n" +
                    "Where:\n" +
                    "- **P** = Principal Loan Amount\n" +
                    "- **r** = Monthly interest rate (Annual Rate ÷ 12 ÷ 100)\n" +
                    "- **n** = Tenure in number of months\n\n" +
                    "In the initial months, a larger portion of EMI goes toward interest; as time progresses, more goes toward the principal repayment. Use our **Calculators** page to simulate any loan amount!";
        }

        // 7. Fixed Deposit / FD returns
        if (q.contains("fixed deposit") || q.contains("fd")) {
            return "### 📈 Fixed Deposit (FD) Returns Explained\n\n" +
                    "In India, banks compound Fixed Deposit interest **quarterly** using compound interest:\n\n" +
                    "$$A = P \\times \\left(1 + \\frac{r}{4}\\right)^{4 \\times t}$$\n\n" +
                    "- Senior citizens typically receive **0.50% to 0.75% higher rates**.\n" +
                    "- You can compare live FD rates from SBI, HDFC, ICICI, and Axis on our **Bank Rates** page.";
        }

        // 8. Tax calculation explanation
        if (q.contains("tax") || q.contains("slab") || q.contains("regime")) {
            return "### 📑 How Estimated Income Tax is Calculated\n\n" +
                    "Under India's tax system:\n" +
                    "- **New Tax Regime (Section 115BAC)** offers lower slab rates with a **₹75,000 Standard Deduction** and full rebate under Section 87A for taxable income up to ₹7,00,000 (meaning zero tax up to ₹7.75 Lakhs gross income).\n" +
                    "- **Old Tax Regime** provides deductions under Section 80C (up to ₹1.5L), 80D (health insurance), 80CCD (NPS), and Home Loan Interest under Section 24(b).\n" +
                    "- A 4% Health & Education Cess applies to final tax.\n\n" +
                    "Try our interactive **Tax Estimation** tool to see your exact tax breakdown and side-by-side regime comparison!";
        }

        // Default friendly financial assistant overview
        return String.format("Hello %s! I'm your AI Financial Assistant. Here is your current snapshot:\n\n" +
                        "- **Current Month Income**: ₹%s\n" +
                        "- **Current Month Expenses**: ₹%s\n" +
                        "- **Current Month Savings**: ₹%s\n\n" +
                        "Feel free to ask me:\n" +
                        "- *\"How much did I spend this month?\"*\n" +
                        "- *\"What is my total income?\"*\n" +
                        "- *\"Which category has the highest expenses?\"*\n" +
                        "- *\"Am I exceeding my budget?\"*\n" +
                        "- *\"How can I save more money?\"*\n" +
                        "- *\"Explain my loan EMI or FD returns\"*",
                user.getFullName(), data.getCurrentMonthIncome(), data.getCurrentMonthExpense(), data.getCurrentMonthSavings());
    }
}
