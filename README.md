# Finance Management System with Bank Interest and Tax Calculation

A complete, modern, responsive personal finance web application built with **React + Vite + Tailwind CSS** on the frontend, **Java 21 + Spring Boot 3.3** on the backend, **Neon Cloud PostgreSQL**, and an **integrated Google Gemini AI Financial Chatbot**.

---

## 🌟 Key Features

1. **User Authentication & Authorization**
   - Secure registration, login, and profile retrieval.
   - BCrypt password hashing & stateless JWT token validation.
   - User data isolation: each user accesses only their own records.
   - Role-based access control (`ROLE_USER` and `ROLE_ADMIN`).

2. **Executive Financial Dashboard**
   - Real-time income, expenses, and savings metrics.
   - Savings rate calculation (with healthy target indicators).
   - Interactive visual charts: Monthly cash flow & category spending distribution.
   - Budget progress bars with threshold alerts (Warning at 80%, Exceeded alert at 100%+).
   - Recent transactions table with categorization.

3. **Income & Expense Tracking**
   - Full CRUD: Add, edit, view, and delete transactions.
   - Categories (Salary, Housing, Groceries, Dining Out, Utilities, Investments, etc.).
   - Instant search by title/description and multi-criteria filtering by type, category, and date range.
   - Filtered view summary totals.

4. **Budget Management**
   - Set monthly spending limits per category.
   - Live tracking comparing actual month expenditures against limits.
   - Remaining cushion calculation and budget overrun warning banners.

5. **Bank Interest Rate Comparison**
   - Compare verified interest rates across leading Indian banks (SBI, HDFC, ICICI, Axis, Kotak).
   - Product categories: Fixed Deposits (FD), Savings Accounts, Home Loans, Personal Loans, Auto Loans.
   - Interactive returns simulation: maturity proceeds and loan monthly EMIs calculated in real time.
   - Senior citizen higher rate support.

6. **Financial Calculators**
   - **Loan EMI Calculator**: Exact reducing balance EMI formula with full 36-month monthly amortization schedule (Principal vs Interest breakdown).
   - **Fixed Deposit (FD) Calculator**: Bank quarterly compounding maturity yield.
   - **Compound Interest Calculator**: Customizable compounding frequency (Annual, Semi-Annual, Quarterly, Monthly).
   - **Simple Interest Calculator**: Transparent linear interest breakdown.

7. **Indian Income Tax Estimation**
   - Supports **New Tax Regime (Section 115BAC)** and **Old Tax Regime** for **FY 2024-25 and FY 2025-26**.
   - Incorporates revised ₹75,000 Standard Deduction (New Regime) and ₹50,000 (Old Regime).
   - Deductions under Chapter VI-A (80C, 80D, 80CCD NPS, HRA, Home Loan Interest 24b).
   - Section 87A rebate calculation (zero tax liability up to ₹7 Lakhs taxable income under New Regime).
   - 4% Health & Education Cess.
   - Side-by-side Regime Comparison highlighting which regime saves you more money!

8. **AI Financial Chatbot (LLM Integrated)**
   - Context-aware financial chatbot integrated directly into the Spring Boot backend.
   - Injects the user's authentic live financial snapshot (spending, income, budgets, category breakdown) to answer questions such as:
     - *"How much did I spend this month?"*
     - *"What is my total income?"*
     - *"Which category has the highest expenses?"*
     - *"Am I exceeding my budget?"*
     - *"How can I save more money?"*
     - *"Explain my loan EMI or FD returns"*
   - Chat history persistence in database.
   - Clean UI with loading indicator, quick prompt pills, and Markdown rendering.
   - Backend API key protection: API keys never exposed to client browser.

9. **Administrator Panel**
   - Enforced backend authorization (`ROLE_ADMIN`).
   - Add, edit, and delete bank financial products and interest rates.
   - Audit maintained tax slab rules.
   - System usage statistics.

---

## 🚀 Quick Start (Local Development)

### Prerequisites
- **Java 21 LTS**
- **Apache Maven 3.9+**
- **Node.js 18+ & npm**

### 1. Run the Spring Boot Backend
```bash
cd backend
mvn clean spring-boot:run
```
*The backend starts at `http://localhost:8080`. By default, it automatically initializes an in-memory PostgreSQL-compatible database pre-seeded with demo records, so you do not need to install anything locally.*

### 2. Run the React Frontend
```bash
cd frontend
npm install
npm run dev
```
*The frontend starts at `http://localhost:5173` with proxy configured to forward `/api` requests to `http://localhost:8080`.*

---

## 🔑 Pre-Seeded Demo Accounts

You can click the quick-login buttons on the login screen or use:

| Role | Username | Password |
|---|---|---|
| **Demo User** | `demouser` | `Demo@123` |
| **Administrator** | `admin` | `Admin@123` |

---

## ☁️ Deployment Guide

### A. Neon Cloud PostgreSQL Setup
1. Create a free project at [neon.tech](https://neon.tech).
2. Copy your connection string:
   ```
   postgres://[user]:[password]@[endpoint].aws.neon.tech/neondb?sslmode=require
   ```
3. Set JDBC URL in backend:
   ```properties
   SPRING_DATASOURCE_URL=jdbc:postgresql://[endpoint].aws.neon.tech/neondb?sslmode=require
   SPRING_DATASOURCE_USERNAME=[user]
   SPRING_DATASOURCE_PASSWORD=[password]
   ```

### B. Backend Deployment on Render
1. Create a new **Web Service** on [render.com](https://render.com).
2. Connect your GitHub repository.
3. Select **Docker** environment (or Maven):
   - Dockerfile path: `backend/Dockerfile`
   - Docker context: `backend`
4. Set Environment Variables:
   - `SPRING_DATASOURCE_URL` (Your Neon JDBC URL)
   - `SPRING_DATASOURCE_USERNAME`
   - `SPRING_DATASOURCE_PASSWORD`
   - `JWT_SECRET` (32+ character hex key)
   - `GEMINI_API_KEY` (Your Google Gemini API key)
   - `CORS_ALLOWED_ORIGINS` (e.g., `https://your-frontend.vercel.app`)

### C. Frontend Deployment on Vercel
1. Import your GitHub repository on [vercel.com](https://vercel.com).
2. Set Root Directory to `frontend`.
3. Set Framework to **Vite**.
4. Set Environment Variable:
   - `VITE_API_BASE_URL`: `https://your-backend.onrender.com`
5. Click **Deploy**.
