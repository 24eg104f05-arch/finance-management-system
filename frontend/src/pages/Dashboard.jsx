import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import {
  TrendingUp,
  TrendingDown,
  PiggyBank,
  AlertTriangle,
  ArrowRight,
  PlusCircle,
  Bot,
  Sparkles,
  RefreshCw,
  Calendar,
  CheckCircle2,
  Clock
} from 'lucide-react';
import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend,
  CategoryScale,
  LinearScale,
  BarElement,
  Title
} from 'chart.js';
import { Doughnut, Bar } from 'react-chartjs-2';

ChartJS.register(ArcElement, Tooltip, Legend, CategoryScale, LinearScale, BarElement, Title);

export default function Dashboard() {
  const { user } = useAuth();
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const fetchOverview = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await api.get('/dashboard/overview');
      setData(res.data);
    } catch (err) {
      console.error(err);
      setError('Failed to load dashboard metrics. Ensure backend server is reachable.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOverview();
  }, []);

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh] space-y-4">
        <div className="w-10 h-10 border-4 border-indigo-600 border-t-transparent rounded-full animate-spin"></div>
        <p className="text-slate-500 font-medium text-sm">Loading your financial dashboard...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="p-6 bg-red-50 border border-red-200 rounded-2xl text-red-700 space-y-3">
        <p className="font-semibold">{error}</p>
        <button
          onClick={fetchOverview}
          className="px-4 py-2 bg-red-600 text-white rounded-xl text-sm font-medium hover:bg-red-700"
        >
          Retry
        </button>
      </div>
    );
  }

  // Doughnut Chart Data for Categories
  const categoryLabels = (data?.categoryBreakdownCurrentMonth || []).map((c) => c.category);
  const categoryValues = (data?.categoryBreakdownCurrentMonth || []).map((c) => c.amount);

  const doughnutData = {
    labels: categoryLabels.length > 0 ? categoryLabels : ['No Data'],
    datasets: [
      {
        data: categoryValues.length > 0 ? categoryValues : [1],
        backgroundColor: [
          '#6366f1',
          '#ec4899',
          '#10b981',
          '#f59e0b',
          '#8b5cf6',
          '#06b6d4',
          '#f97316',
          '#94a3b8',
        ],
        borderWidth: 0,
      },
    ],
  };

  // Bar Chart Data for Current Month Income vs Expense
  const barData = {
    labels: ['Monthly Flow'],
    datasets: [
      {
        label: 'Income',
        data: [data?.currentMonthIncome || 0],
        backgroundColor: '#10b981',
        borderRadius: 8,
      },
      {
        label: 'Expense',
        data: [data?.currentMonthExpense || 0],
        backgroundColor: '#ef4444',
        borderRadius: 8,
      },
      {
        label: 'Net Savings',
        data: [data?.currentMonthSavings || 0],
        backgroundColor: '#6366f1',
        borderRadius: 8,
      },
    ],
  };

  return (
    <div className="space-y-8">
      {/* Top Banner / Welcome */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 bg-white p-6 rounded-2xl border border-slate-200 shadow-sm">
        <div>
          <h1 className="text-2xl font-bold text-slate-900 tracking-tight">
            Welcome back, {user?.fullName || 'User'} 👋
          </h1>
          <p className="text-slate-500 text-sm mt-1">
            Here is your financial health summary and budget tracking for this month.
          </p>
        </div>
        <div className="flex items-center space-x-3">
          <button
            onClick={fetchOverview}
            className="p-2.5 rounded-xl border border-slate-200 text-slate-600 hover:bg-slate-50 transition-colors"
            title="Refresh Dashboard"
          >
            <RefreshCw className="w-4 h-4" />
          </button>
          <Link
            to="/transactions"
            className="flex items-center space-x-2 px-4 py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl text-sm font-semibold shadow-sm shadow-indigo-500/20 transition-all"
          >
            <PlusCircle className="w-4 h-4" />
            <span>Add Transaction</span>
          </Link>
        </div>
      </div>

      {/* AI Assistant Quick Pill Banner */}
      <div className="p-4 rounded-2xl bg-gradient-to-r from-indigo-900 via-indigo-800 to-purple-900 text-white shadow-md flex flex-col sm:flex-row items-center justify-between gap-4">
        <div className="flex items-center space-x-3">
          <div className="w-10 h-10 rounded-xl bg-white/10 flex items-center justify-center text-emerald-300">
            <Sparkles className="w-6 h-6 animate-pulse" />
          </div>
          <div>
            <h3 className="font-semibold text-sm">Have financial questions about your spending?</h3>
            <p className="text-xs text-indigo-200">
              Ask our AI assistant to analyze your real transactions, budgets, or explain taxes & loans.
            </p>
          </div>
        </div>
        <Link
          to="/ai-assistant"
          className="flex items-center space-x-2 px-4 py-2 bg-emerald-500 hover:bg-emerald-600 text-slate-950 font-bold text-xs uppercase tracking-wider rounded-xl transition-all shadow-sm"
        >
          <Bot className="w-4 h-4" />
          <span>Consult AI</span>
          <ArrowRight className="w-3.5 h-3.5" />
        </Link>
      </div>

      {/* Metric Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
        {/* Income Card */}
        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm relative overflow-hidden">
          <div className="flex items-center justify-between mb-3">
            <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">Month Income</span>
            <div className="w-8 h-8 rounded-lg bg-emerald-50 text-emerald-600 flex items-center justify-center">
              <TrendingUp className="w-5 h-5" />
            </div>
          </div>
          <div className="text-2xl font-bold text-slate-900">₹{Number(data?.currentMonthIncome || 0).toLocaleString()}</div>
          <div className="text-xs text-slate-400 mt-2">
            Lifetime Income: ₹{Number(data?.totalIncomeAllTime || 0).toLocaleString()}
          </div>
        </div>

        {/* Expense Card */}
        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm relative overflow-hidden">
          <div className="flex items-center justify-between mb-3">
            <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">Month Expenses</span>
            <div className="w-8 h-8 rounded-lg bg-red-50 text-red-600 flex items-center justify-center">
              <TrendingDown className="w-5 h-5" />
            </div>
          </div>
          <div className="text-2xl font-bold text-slate-900">₹{Number(data?.currentMonthExpense || 0).toLocaleString()}</div>
          <div className="text-xs text-slate-400 mt-2">
            Lifetime Expense: ₹{Number(data?.totalExpenseAllTime || 0).toLocaleString()}
          </div>
        </div>

        {/* Savings Card */}
        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm relative overflow-hidden">
          <div className="flex items-center justify-between mb-3">
            <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">Month Savings</span>
            <div className="w-8 h-8 rounded-lg bg-indigo-50 text-indigo-600 flex items-center justify-center">
              <PiggyBank className="w-5 h-5" />
            </div>
          </div>
          <div className={`text-2xl font-bold ${Number(data?.currentMonthSavings || 0) >= 0 ? 'text-indigo-600' : 'text-red-600'}`}>
            ₹{Number(data?.currentMonthSavings || 0).toLocaleString()}
          </div>
          <div className="text-xs text-slate-400 mt-2">
            Lifetime Net Savings: ₹{Number(data?.totalSavingsAllTime || 0).toLocaleString()}
          </div>
        </div>

        {/* Savings Rate Card */}
        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm relative overflow-hidden">
          <div className="flex items-center justify-between mb-3">
            <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">Savings Rate</span>
            <div className="w-8 h-8 rounded-lg bg-purple-50 text-purple-600 flex items-center justify-center">
              <CheckCircle2 className="w-5 h-5" />
            </div>
          </div>
          <div className="text-2xl font-bold text-slate-900">
            {Number(data?.currentMonthSavingsRate || 0).toFixed(1)}%
          </div>
          <div className="text-xs text-slate-400 mt-2">
            Recommended Target: 20%+
          </div>
        </div>
      </div>

      {/* Charts Section */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Cash Flow Comparison */}
        <div className="lg:col-span-2 bg-white p-6 rounded-2xl border border-slate-200 shadow-sm">
          <h2 className="text-base font-bold text-slate-900 mb-1">Monthly Flow Comparison</h2>
          <p className="text-xs text-slate-500 mb-6">Income vs Expenses vs Net Savings for this calendar month</p>
          <div className="h-64">
            <Bar
              data={barData}
              options={{
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                  legend: { position: 'top' },
                },
                scales: {
                  y: {
                    beginAtZero: true,
                    ticks: {
                      callback: (v) => `₹${Number(v).toLocaleString()}`,
                    },
                  },
                },
              }}
            />
          </div>
        </div>

        {/* Expense Category Breakdown */}
        <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm flex flex-col">
          <h2 className="text-base font-bold text-slate-900 mb-1">Expense Breakdown</h2>
          <p className="text-xs text-slate-500 mb-4">Spending distribution by category</p>
          <div className="flex-1 flex items-center justify-center h-52">
            {categoryValues.length > 0 ? (
              <Doughnut
                data={doughnutData}
                options={{
                  responsive: true,
                  maintainAspectRatio: false,
                  plugins: {
                    legend: { position: 'bottom', labels: { boxWidth: 12, font: { size: 10 } } },
                  },
                  cutout: '65%',
                }}
              />
            ) : (
              <p className="text-slate-400 text-sm">No expenses this month</p>
            )}
          </div>
        </div>
      </div>

      {/* Budgets & Recent Transactions Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Budget Progress Tracker */}
        <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h2 className="text-base font-bold text-slate-900">Budget Progress</h2>
              <p className="text-xs text-slate-500">Track monthly category limits & alerts</p>
            </div>
            <Link to="/budgets" className="text-xs font-semibold text-indigo-600 hover:underline flex items-center space-x-1">
              <span>Manage</span>
              <ArrowRight className="w-3.5 h-3.5" />
            </Link>
          </div>

          {(!data?.budgetStatuses || data.budgetStatuses.length === 0) ? (
            <div className="text-center py-8 text-slate-400 text-sm">
              <p>No budgets created for this month yet.</p>
              <Link to="/budgets" className="mt-2 inline-block text-xs font-semibold text-indigo-600 hover:underline">
                + Create a Category Budget
              </Link>
            </div>
          ) : (
            <div className="space-y-4">
              {data.budgetStatuses.map((b) => {
                const isOver = b.exceeded;
                const isWarning = b.statusLevel === 'WARNING';
                const progressPct = Math.min(b.percentageSpent || 0, 100);

                return (
                  <div key={b.budgetId} className="p-3.5 rounded-xl border border-slate-100 bg-slate-50/50 space-y-2">
                    <div className="flex items-center justify-between text-xs">
                      <div className="font-semibold text-slate-800 flex items-center space-x-1.5">
                        <span>{b.category}</span>
                        {isOver && (
                          <span className="px-1.5 py-0.5 rounded text-[10px] font-bold bg-red-100 text-red-700 flex items-center space-x-0.5">
                            <AlertTriangle className="w-3 h-3" />
                            <span>Exceeded</span>
                          </span>
                        )}
                        {isWarning && (
                          <span className="px-1.5 py-0.5 rounded text-[10px] font-bold bg-amber-100 text-amber-700">
                            Near Limit
                          </span>
                        )}
                      </div>
                      <div className="text-slate-500">
                        ₹{Number(b.spentAmount).toLocaleString()} / ₹{Number(b.monthlyLimit).toLocaleString()}
                      </div>
                    </div>
                    {/* Progress Bar */}
                    <div className="w-full bg-slate-200 h-2 rounded-full overflow-hidden">
                      <div
                        className={`h-full transition-all duration-300 ${
                          isOver ? 'bg-red-500' : isWarning ? 'bg-amber-500' : 'bg-emerald-500'
                        }`}
                        style={{ width: `${progressPct}%` }}
                      />
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </div>

        {/* Recent Transactions */}
        <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h2 className="text-base font-bold text-slate-900">Recent Transactions</h2>
              <p className="text-xs text-slate-500">Latest financial activities</p>
            </div>
            <Link to="/transactions" className="text-xs font-semibold text-indigo-600 hover:underline flex items-center space-x-1">
              <span>View All</span>
              <ArrowRight className="w-3.5 h-3.5" />
            </Link>
          </div>

          {(!data?.recentTransactions || data.recentTransactions.length === 0) ? (
            <div className="text-center py-8 text-slate-400 text-sm">
              <p>No transactions recorded yet.</p>
              <Link to="/transactions" className="mt-2 inline-block text-xs font-semibold text-indigo-600 hover:underline">
                + Add your first transaction
              </Link>
            </div>
          ) : (
            <div className="divide-y divide-slate-100">
              {data.recentTransactions.slice(0, 5).map((t) => {
                const isIncome = t.type === 'INCOME';
                return (
                  <div key={t.id} className="py-3 flex items-center justify-between">
                    <div className="flex items-center space-x-3">
                      <div
                        className={`w-9 h-9 rounded-xl flex items-center justify-center font-bold text-xs ${
                          isIncome ? 'bg-emerald-50 text-emerald-600' : 'bg-red-50 text-red-600'
                        }`}
                      >
                        {isIncome ? <TrendingUp className="w-4 h-4" /> : <TrendingDown className="w-4 h-4" />}
                      </div>
                      <div>
                        <div className="font-semibold text-slate-900 text-sm">{t.title}</div>
                        <div className="text-xs text-slate-400 flex items-center space-x-2">
                          <span className="px-1.5 py-0.2 bg-slate-100 rounded text-slate-600 font-medium">
                            {t.category}
                          </span>
                          <span>{t.transactionDate}</span>
                        </div>
                      </div>
                    </div>
                    <div className={`font-bold text-sm ${isIncome ? 'text-emerald-600' : 'text-slate-900'}`}>
                      {isIncome ? '+' : '-'}₹{Number(t.amount).toLocaleString()}
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
