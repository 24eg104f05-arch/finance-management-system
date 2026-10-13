package com.finance.app.service;

import com.finance.app.entity.*;
import com.finance.app.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BankProductRepository bankProductRepository;
    private final TaxRuleRepository taxRuleRepository;
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           BankProductRepository bankProductRepository,
                           TaxRuleRepository taxRuleRepository,
                           TransactionRepository transactionRepository,
                           BudgetRepository budgetRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bankProductRepository = bankProductRepository;
        this.taxRuleRepository = taxRuleRepository;
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        initUsers();
        initBankProducts();
        initTaxRules();
        initDemoFinancialData();
    }

    private void initUsers() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User(
                    "admin",
                    "admin@finance.app",
                    passwordEncoder.encode("Admin@123"),
                    "Administrator",
                    Role.ROLE_ADMIN
            );
            userRepository.save(admin);
        }

        if (!userRepository.existsByUsername("demouser")) {
            User demo = new User(
                    "demouser",
                    "demo@finance.app",
                    passwordEncoder.encode("Demo@123"),
                    "Alex Johnson",
                    Role.ROLE_USER
            );
            userRepository.save(demo);
        }
    }

    private void initBankProducts() {
        if (bankProductRepository.count() == 0) {
            List<BankProduct> products = List.of(
                    // Savings Accounts
                    new BankProduct("State Bank of India", BankProductType.SAVINGS, "SBI Basic Savings Deposit", new BigDecimal("2.70"), new BigDecimal("2.70"), 1, 120, new BigDecimal("0.00"), "Zero balance requirement, complimentary Rupay card, free online NEFT/RTGS.", true),
                    new BankProduct("HDFC Bank", BankProductType.SAVINGS, "HDFC Regular Savings Account", new BigDecimal("3.00"), new BigDecimal("3.00"), 1, 120, new BigDecimal("10000.00"), "Comprehensive net banking, InstaAlerts, rewards on debit card.", true),
                    new BankProduct("ICICI Bank", BankProductType.SAVINGS, "ICICI Advantage Woman Savings", new BigDecimal("3.00"), new BigDecimal("3.00"), 1, 120, new BigDecimal("10000.00"), "Cashback offers, accidental insurance cover up to 5 Lakhs.", true),
                    new BankProduct("Kotak Mahindra Bank", BankProductType.SAVINGS, "Kotak 811 Zero Balance", new BigDecimal("3.50"), new BigDecimal("3.50"), 1, 120, new BigDecimal("0.00"), "Instant digital account opening, virtual debit card, zero balance.", true),

                    // Fixed Deposits
                    new BankProduct("State Bank of India", BankProductType.FD, "SBI Amrit Kalash (400 Days)", new BigDecimal("7.10"), new BigDecimal("7.60"), 12, 14, new BigDecimal("10000.00"), "Special high-yield FD for 400 days tenure. Premature withdrawal permitted.", true),
                    new BankProduct("HDFC Bank", BankProductType.FD, "HDFC Regular Fixed Deposit (1 Year)", new BigDecimal("6.60"), new BigDecimal("7.10"), 12, 120, new BigDecimal("5000.00"), "Quarterly compounding interest, overdraft facility against FD up to 90%.", true),
                    new BankProduct("ICICI Bank", BankProductType.FD, "ICICI Golden Years FD", new BigDecimal("7.20"), new BigDecimal("7.75"), 15, 60, new BigDecimal("10000.00"), "Special tenure of 15 to 18 months, additional 0.55% for seniors.", true),
                    new BankProduct("Axis Bank", BankProductType.FD, "Axis Fixed Deposit Plus", new BigDecimal("7.10"), new BigDecimal("7.60"), 12, 120, new BigDecimal("10000.00"), "Flexible interest payouts: monthly, quarterly, or cumulative at maturity.", true),

                    // Home Loans
                    new BankProduct("State Bank of India", BankProductType.HOME_LOAN, "SBI Regular Home Loan", new BigDecimal("8.50"), null, 12, 360, new BigDecimal("500000.00"), "Low processing fees, zero prepayment penalties, concession for women borrowers.", true),
                    new BankProduct("HDFC Bank", BankProductType.HOME_LOAN, "HDFC Reach Home Loan", new BigDecimal("8.75"), null, 12, 360, new BigDecimal("500000.00"), "Quick digital sanctions, top-up loan facility, customized repayment options.", true),
                    new BankProduct("ICICI Bank", BankProductType.HOME_LOAN, "ICICI Express Home Loan", new BigDecimal("8.75"), null, 12, 360, new BigDecimal("500000.00"), "Instant approval for pre-approved customers, 30 years repayment flexibility.", true),

                    // Personal Loans
                    new BankProduct("HDFC Bank", BankProductType.PERSONAL_LOAN, "HDFC Insta Personal Loan", new BigDecimal("10.50"), null, 12, 60, new BigDecimal("50000.00"), "Disbursal in under 10 seconds for salary account holders, zero collateral.", true),
                    new BankProduct("ICICI Bank", BankProductType.PERSONAL_LOAN, "ICICI Personal Loan", new BigDecimal("10.80"), null, 12, 72, new BigDecimal("50000.00"), "Flexible tenure up to 6 years, competitive fixed interest rate.", true),
                    new BankProduct("Axis Bank", BankProductType.PERSONAL_LOAN, "Axis 24x7 Personal Loan", new BigDecimal("11.25"), null, 12, 60, new BigDecimal("50000.00"), "End-to-end digital processing, minimal documentation required.", true),

                    // Auto Loans
                    new BankProduct("State Bank of India", BankProductType.AUTO_LOAN, "SBI Car Loan", new BigDecimal("8.75"), null, 12, 84, new BigDecimal("100000.00"), "Finance up to 90% of on-road price, repayment tenure up to 7 years.", true),
                    new BankProduct("HDFC Bank", BankProductType.AUTO_LOAN, "HDFC Custom Car Loan", new BigDecimal("8.95"), null, 12, 84, new BigDecimal("100000.00"), "100% on-road financing available on select car models, step-up EMI scheme.", true)
            );
            bankProductRepository.saveAll(products);
        }
    }

    private void initTaxRules() {
        if (taxRuleRepository.count() == 0) {
            List<TaxRule> rules = List.of(
                    // FY 2024-2025 NEW REGIME (Section 115BAC revised)
                    new TaxRule("2024-2025", TaxRegime.NEW, new BigDecimal("0.00"), new BigDecimal("300000.00"), BigDecimal.ZERO, 1, "Up to ₹3 Lakh: Nil"),
                    new TaxRule("2024-2025", TaxRegime.NEW, new BigDecimal("300000.00"), new BigDecimal("700000.00"), new BigDecimal("5.00"), 2, "₹3 Lakh to ₹7 Lakh: 5%"),
                    new TaxRule("2024-2025", TaxRegime.NEW, new BigDecimal("700000.00"), new BigDecimal("1000000.00"), new BigDecimal("10.00"), 3, "₹7 Lakh to ₹10 Lakh: 10%"),
                    new TaxRule("2024-2025", TaxRegime.NEW, new BigDecimal("1000000.00"), new BigDecimal("1200000.00"), new BigDecimal("15.00"), 4, "₹10 Lakh to ₹12 Lakh: 15%"),
                    new TaxRule("2024-2025", TaxRegime.NEW, new BigDecimal("1200000.00"), new BigDecimal("1500000.00"), new BigDecimal("20.00"), 5, "₹12 Lakh to ₹15 Lakh: 20%"),
                    new TaxRule("2024-2025", TaxRegime.NEW, new BigDecimal("1500000.00"), null, new BigDecimal("30.00"), 6, "Above ₹15 Lakh: 30%"),

                    // FY 2024-2025 OLD REGIME
                    new TaxRule("2024-2025", TaxRegime.OLD, new BigDecimal("0.00"), new BigDecimal("250000.00"), BigDecimal.ZERO, 1, "Up to ₹2.5 Lakh: Nil"),
                    new TaxRule("2024-2025", TaxRegime.OLD, new BigDecimal("250000.00"), new BigDecimal("500000.00"), new BigDecimal("5.00"), 2, "₹2.5 Lakh to ₹5 Lakh: 5%"),
                    new TaxRule("2024-2025", TaxRegime.OLD, new BigDecimal("500000.00"), new BigDecimal("1000000.00"), new BigDecimal("20.00"), 3, "₹5 Lakh to ₹10 Lakh: 20%"),
                    new TaxRule("2024-2025", TaxRegime.OLD, new BigDecimal("1000000.00"), null, new BigDecimal("30.00"), 4, "Above ₹10 Lakh: 30%"),

                    // FY 2025-2026 NEW REGIME
                    new TaxRule("2025-2026", TaxRegime.NEW, new BigDecimal("0.00"), new BigDecimal("300000.00"), BigDecimal.ZERO, 1, "Up to ₹3 Lakh: Nil"),
                    new TaxRule("2025-2026", TaxRegime.NEW, new BigDecimal("300000.00"), new BigDecimal("700000.00"), new BigDecimal("5.00"), 2, "₹3 Lakh to ₹7 Lakh: 5%"),
                    new TaxRule("2025-2026", TaxRegime.NEW, new BigDecimal("700000.00"), new BigDecimal("1000000.00"), new BigDecimal("10.00"), 3, "₹7 Lakh to ₹10 Lakh: 10%"),
                    new TaxRule("2025-2026", TaxRegime.NEW, new BigDecimal("1000000.00"), new BigDecimal("1200000.00"), new BigDecimal("15.00"), 4, "₹10 Lakh to ₹12 Lakh: 15%"),
                    new TaxRule("2025-2026", TaxRegime.NEW, new BigDecimal("1200000.00"), new BigDecimal("1500000.00"), new BigDecimal("20.00"), 5, "₹12 Lakh to ₹15 Lakh: 20%"),
                    new TaxRule("2025-2026", TaxRegime.NEW, new BigDecimal("1500000.00"), null, new BigDecimal("30.00"), 6, "Above ₹15 Lakh: 30%"),

                    // FY 2025-2026 OLD REGIME
                    new TaxRule("2025-2026", TaxRegime.OLD, new BigDecimal("0.00"), new BigDecimal("250000.00"), BigDecimal.ZERO, 1, "Up to ₹2.5 Lakh: Nil"),
                    new TaxRule("2025-2026", TaxRegime.OLD, new BigDecimal("250000.00"), new BigDecimal("500000.00"), new BigDecimal("5.00"), 2, "₹2.5 Lakh to ₹5 Lakh: 5%"),
                    new TaxRule("2025-2026", TaxRegime.OLD, new BigDecimal("500000.00"), new BigDecimal("1000000.00"), new BigDecimal("20.00"), 3, "₹5 Lakh to ₹10 Lakh: 20%"),
                    new TaxRule("2025-2026", TaxRegime.OLD, new BigDecimal("1000000.00"), null, new BigDecimal("30.00"), 4, "Above ₹10 Lakh: 30%")
            );
            taxRuleRepository.saveAll(rules);
        }
    }

    private void initDemoFinancialData() {
        userRepository.findByUsername("demouser").ifPresent(user -> {
            LocalDate today = LocalDate.now();
            int currentMonth = today.getMonthValue();
            int currentYear = today.getYear();

            if (transactionRepository.findByUserIdOrderByTransactionDateDescCreatedAtDesc(user.getId()).isEmpty()) {
                // Seed Budgets for current month
                budgetRepository.save(new Budget(user, "Housing", new BigDecimal("30000.00"), currentMonth, currentYear));
                budgetRepository.save(new Budget(user, "Groceries", new BigDecimal("15000.00"), currentMonth, currentYear));
                budgetRepository.save(new Budget(user, "Dining Out", new BigDecimal("8000.00"), currentMonth, currentYear));
                budgetRepository.save(new Budget(user, "Utilities", new BigDecimal("6000.00"), currentMonth, currentYear));
                budgetRepository.save(new Budget(user, "Entertainment", new BigDecimal("5000.00"), currentMonth, currentYear));

                // Seed Transactions for current month and previous month
                List<Transaction> transactions = List.of(
                        new Transaction(user, "Monthly Tech Consulting Salary", new BigDecimal("95000.00"), TransactionType.INCOME, "Salary", today.withDayOfMonth(1), "Primary salary direct deposit"),
                        new Transaction(user, "Dividend Payout", new BigDecimal("4500.00"), TransactionType.INCOME, "Investments", today.withDayOfMonth(5), "Quarterly stock dividend credit"),
                        new Transaction(user, "Freelance Web Project", new BigDecimal("22000.00"), TransactionType.INCOME, "Freelancing", today.withDayOfMonth(8), "UI consulting milestone payment"),

                        // Expenses
                        new Transaction(user, "Apartment Monthly Rent", new BigDecimal("28000.00"), TransactionType.EXPENSE, "Housing", today.withDayOfMonth(2), "Rent paid via bank transfer"),
                        new Transaction(user, "Supermarket Grocery Run", new BigDecimal("6200.00"), TransactionType.EXPENSE, "Groceries", today.withDayOfMonth(3), "Weekly organic grocery staples"),
                        new Transaction(user, "Fiber Internet & Mobile Plan", new BigDecimal("2100.00"), TransactionType.EXPENSE, "Utilities", today.withDayOfMonth(4), "Monthly telecom bill"),
                        new Transaction(user, "Electricity Bill", new BigDecimal("3400.00"), TransactionType.EXPENSE, "Utilities", today.withDayOfMonth(6), "State power board payment"),
                        new Transaction(user, "Dinner at Olive Bistro", new BigDecimal("3800.00"), TransactionType.EXPENSE, "Dining Out", today.withDayOfMonth(7), "Family weekend celebration"),
                        new Transaction(user, "Organic Supermarket Restock", new BigDecimal("4900.00"), TransactionType.EXPENSE, "Groceries", today.withDayOfMonth(10), "Mid-month pantry restocking"),
                        new Transaction(user, "Streaming Services & Cinema", new BigDecimal("1850.00"), TransactionType.EXPENSE, "Entertainment", today.withDayOfMonth(11), "OTT subscriptions & IMAX tickets")
                );

                transactionRepository.saveAll(transactions);
            }
        });
    }
}
