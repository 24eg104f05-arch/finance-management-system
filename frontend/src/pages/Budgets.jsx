import React, { useState, useEffect } from 'react';
import api from '../services/api';
import {
  Plus,
  AlertTriangle,
  CheckCircle,
  TrendingUp,
  PieChart,
  Trash2,
  X,
  AlertCircle,
  DollarSign
} from 'lucide-react';

const CATEGORIES = [
  'Housing',
  'Groceries',
  'Dining Out',
  'Utilities',
  'Transportation',
  'Entertainment',
  'Healthcare',
  'Shopping',
  'Education',
  'Personal Care',
  'Other'
];

export default function Budgets() {
  const [budgets, setBudgets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const [modalOpen, setModalOpen] = useState(false);
  const [formData, setFormData] = useState({
    category: 'Groceries',
    monthlyLimit: '',
    month: new Date().getMonth() + 1,
    year: new Date().getFullYear(),
  });
  const [submitting, setSubmitting] = useState(false);
  const [formError, setFormError] = useState('');

  const fetchBudgets = async () => {
    setLoading(true);
    setError('');
    try {
      const now = new Date();
      const res = await api.get('/budgets/status', {
        params: { month: now.getMonth() + 1, year: now.getFullYear() },
      });
      setBudgets(res.data);
    } catch (err) {
      console.error(err);
      setError('Failed to fetch budgets');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBudgets();
  }, []);

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this budget?')) return;
    try {
      await api.delete(`/budgets/${id}`);
      setBudgets(budgets.filter((b) => b.budgetId !== id));
    } catch (err) {
      alert('Failed to delete budget');
    }
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    setFormError('');

    try {
      await api.post('/budgets', {
        category: formData.category,
        monthlyLimit: parseFloat(formData.monthlyLimit),
        month: parseInt(formData.month, 10),
        year: parseInt(formData.year, 10),
      });
      setModalOpen(false);
      fetchBudgets();
    } catch (err) {
      setFormError(err.response?.data?.error || 'Failed to save budget');
    } finally {
      setSubmitting(false);
    }
  };

  const totalBudgeted = budgets.reduce((acc, b) => acc + Number(b.monthlyLimit), 0);
  const totalSpent = budgets.reduce((acc, b) => acc + Number(b.spentAmount), 0);
  const exceededCount = budgets.filter((b) => b.exceeded).length;

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-900 tracking-tight">Monthly Budgets</h1>
          <p className="text-slate-500 text-sm mt-0.5">
            Set and track expense spending limits to avoid overspending
          </p>
        </div>
        <button
          onClick={() => {
            setFormData({
              category: 'Groceries',
              monthlyLimit: '',
              month: new Date().getMonth() + 1,
              year: new Date().getFullYear(),
            });
            setFormError('');
            setModalOpen(true);
          }}
          className="flex items-center space-x-2 px-4 py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl text-sm font-semibold shadow-sm shadow-indigo-500/20 transition-all self-start sm:self-auto"
        >
          <Plus className="w-4 h-4" />
          <span>Set Budget Limit</span>
        </button>
      </div>

      {/* Exceeded Alert Warning Banner */}
      {exceededCount > 0 && (
        <div className="p-4 rounded-2xl bg-red-50 border border-red-200 flex items-start space-x-3 text-red-800">
          <AlertTriangle className="w-5 h-5 flex-shrink-0 text-red-600 mt-0.5" />
          <div>
            <h4 className="font-bold text-sm">Budget Overrun Warning!</h4>
            <p className="text-xs text-red-700 mt-0.5">
              You have exceeded your monthly budget in <strong>{exceededCount}</strong> categories. Review your expenses and reduce non-essential spending.
            </p>
          </div>
        </div>
      )}

      {/* Aggregate Metrics */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm">
          <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">Total Monthly Budget</span>
          <div className="text-2xl font-bold text-slate-900 mt-1">₹{totalBudgeted.toLocaleString()}</div>
        </div>

        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm">
          <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">Total Spent Against Budget</span>
          <div className="text-2xl font-bold text-red-600 mt-1">₹{totalSpent.toLocaleString()}</div>
        </div>

        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm">
          <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">Remaining Cushion</span>
          <div className={`text-2xl font-bold mt-1 ${totalBudgeted - totalSpent >= 0 ? 'text-emerald-600' : 'text-red-600'}`}>
            ₹{(totalBudgeted - totalSpent).toLocaleString()}
          </div>
        </div>
      </div>

      {/* Budget Cards List */}
      {loading ? (
        <div className="text-center py-12 text-slate-500 text-sm">
          <div className="w-8 h-8 border-4 border-indigo-600 border-t-transparent rounded-full animate-spin mx-auto mb-2" />
          Loading budgets...
        </div>
      ) : budgets.length === 0 ? (
        <div className="bg-white p-12 rounded-2xl border border-slate-200 text-center text-slate-400">
          <PieChart className="w-12 h-12 mx-auto mb-3 opacity-30" />
          <h3 className="font-semibold text-slate-700 text-base">No budgets created yet</h3>
          <p className="text-xs text-slate-500 mt-1">Add monthly limits for categories like Groceries, Housing, or Dining.</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
          {budgets.map((b) => {
            const isExceeded = b.exceeded;
            const isNearLimit = b.statusLevel === 'WARNING';
            const progress = Math.min(b.percentageSpent || 0, 100);

            return (
              <div
                key={b.budgetId}
                className={`p-5 bg-white rounded-2xl border transition-all ${
                  isExceeded ? 'border-red-300 shadow-sm bg-red-50/20' : 'border-slate-200 shadow-sm'
                }`}
              >
                <div className="flex items-center justify-between mb-3">
                  <div className="flex items-center space-x-2">
                    <h3 className="font-bold text-slate-900 text-base">{b.category}</h3>
                    {isExceeded && (
                      <span className="px-2 py-0.5 rounded-full text-[10px] font-bold bg-red-100 text-red-700">
                        Exceeded
                      </span>
                    )}
                    {isNearLimit && (
                      <span className="px-2 py-0.5 rounded-full text-[10px] font-bold bg-amber-100 text-amber-700">
                        {Number(b.percentageSpent).toFixed(0)}% Used
                      </span>
                    )}
                  </div>
                  <button
                    onClick={() => handleDelete(b.budgetId)}
                    className="p-1.5 text-slate-400 hover:text-red-600 rounded-lg transition-colors"
                    title="Delete Budget"
                  >
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>

                {/* Progress bar */}
                <div className="space-y-1 mb-4">
                  <div className="flex justify-between text-xs text-slate-500">
                    <span>Spent: ₹{Number(b.spentAmount).toLocaleString()}</span>
                    <span>Limit: ₹{Number(b.monthlyLimit).toLocaleString()}</span>
                  </div>
                  <div className="w-full bg-slate-100 h-2.5 rounded-full overflow-hidden">
                    <div
                      className={`h-full transition-all duration-300 ${
                        isExceeded ? 'bg-red-500' : isNearLimit ? 'bg-amber-500' : 'bg-emerald-500'
                      }`}
                      style={{ width: `${progress}%` }}
                    />
                  </div>
                </div>

                <div className="pt-3 border-t border-slate-100 flex items-center justify-between text-xs">
                  <span className="text-slate-500">Remaining to spend:</span>
                  <span
                    className={`font-bold ${
                      Number(b.remainingAmount) >= 0 ? 'text-emerald-600' : 'text-red-600'
                    }`}
                  >
                    ₹{Number(b.remainingAmount).toLocaleString()}
                  </span>
                </div>
              </div>
            );
          })}
        </div>
      )}

      {/* Set Budget Modal */}
      {modalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/60 p-4 backdrop-blur-xs">
          <div className="bg-white rounded-2xl shadow-xl w-full max-w-md overflow-hidden border border-slate-100">
            <div className="flex items-center justify-between p-5 border-b border-slate-100">
              <h3 className="font-bold text-slate-900 text-lg">Set Category Budget</h3>
              <button onClick={() => setModalOpen(false)} className="text-slate-400 hover:text-slate-600">
                <X className="w-5 h-5" />
              </button>
            </div>

            {formError && (
              <div className="mx-5 mt-4 p-3 rounded-xl bg-red-50 border border-red-200 text-red-700 text-xs flex items-center space-x-2">
                <AlertCircle className="w-4 h-4 flex-shrink-0" />
                <span>{formError}</span>
              </div>
            )}

            <form onSubmit={handleFormSubmit} className="p-5 space-y-4">
              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">Category</label>
                <select
                  value={formData.category}
                  onChange={(e) => setFormData({ ...formData, category: e.target.value })}
                  className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  {CATEGORIES.map((c) => (
                    <option key={c} value={c}>{c}</option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">Monthly Spending Limit (₹)</label>
                <input
                  type="number"
                  step="0.01"
                  min="0.01"
                  required
                  value={formData.monthlyLimit}
                  onChange={(e) => setFormData({ ...formData, monthlyLimit: e.target.value })}
                  placeholder="e.g. 15000"
                  className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Month</label>
                  <input
                    type="number"
                    min="1"
                    max="12"
                    required
                    value={formData.month}
                    onChange={(e) => setFormData({ ...formData, month: e.target.value })}
                    className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Year</label>
                  <input
                    type="number"
                    min="2020"
                    required
                    value={formData.year}
                    onChange={(e) => setFormData({ ...formData, year: e.target.value })}
                    className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
              </div>

              <div className="flex justify-end space-x-2 pt-2">
                <button
                  type="button"
                  onClick={() => setModalOpen(false)}
                  className="px-4 py-2 border border-slate-200 text-slate-700 rounded-xl text-sm font-medium hover:bg-slate-50"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={submitting}
                  className="px-4 py-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl text-sm font-semibold shadow-sm disabled:opacity-60"
                >
                  {submitting ? 'Saving...' : 'Save Budget'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
