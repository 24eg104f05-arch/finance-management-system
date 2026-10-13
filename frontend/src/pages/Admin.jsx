import React, { useState, useEffect } from 'react';
import api from '../services/api';
import {
  ShieldCheck,
  Plus,
  Trash2,
  Users,
  Landmark,
  ReceiptText,
  X,
  AlertCircle,
  CheckCircle2
} from 'lucide-react';

export default function Admin() {
  const [stats, setStats] = useState(null);
  const [bankProducts, setBankProducts] = useState([]);
  const [taxRules, setTaxRules] = useState([]);
  const [loading, setLoading] = useState(true);

  // Bank product modal
  const [bankModalOpen, setBankModalOpen] = useState(false);
  const [bankForm, setBankForm] = useState({
    bankName: '',
    productType: 'FD',
    productName: '',
    interestRate: '',
    seniorCitizenRate: '',
    minTenureMonths: 12,
    maxTenureMonths: 120,
    minAmount: 10000,
    features: '',
    verifiedCurrent: true,
  });

  const fetchData = async () => {
    setLoading(true);
    try {
      const [statsRes, banksRes, taxRes] = await Promise.all([
        api.get('/admin/stats'),
        api.get('/banks'),
        api.get('/tax/rules?financialYear=2024-2025'),
      ]);
      setStats(statsRes.data);
      setBankProducts(banksRes.data);
      setTaxRules(taxRes.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleDeleteBankProduct = async (id) => {
    if (!window.confirm('Are you sure you want to delete this bank product?')) return;
    try {
      await api.delete(`/admin/banks/${id}`);
      setBankProducts(bankProducts.filter((p) => p.id !== id));
      setStats((prev) => ({ ...prev, totalBankProducts: prev.totalBankProducts - 1 }));
    } catch (err) {
      alert('Failed to delete product');
    }
  };

  const handleBankFormSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await api.post('/admin/banks', {
        ...bankForm,
        interestRate: parseFloat(bankForm.interestRate),
        seniorCitizenRate: bankForm.seniorCitizenRate ? parseFloat(bankForm.seniorCitizenRate) : null,
      });
      setBankProducts([...bankProducts, res.data]);
      setBankModalOpen(false);
      setStats((prev) => ({ ...prev, totalBankProducts: prev.totalBankProducts + 1 }));
    } catch (err) {
      alert('Failed to add bank product');
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <div className="flex items-center space-x-2">
            <h1 className="text-2xl font-bold text-slate-900 tracking-tight">Admin Management Panel</h1>
            <span className="px-2 py-0.5 rounded-full text-xs font-bold bg-purple-100 text-purple-700">
              Admin Only
            </span>
          </div>
          <p className="text-slate-500 text-sm mt-0.5">
            Manage system reference data, bank interest rates, and tax rules
          </p>
        </div>
        <button
          onClick={() => setBankModalOpen(true)}
          className="flex items-center space-x-2 px-4 py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl text-sm font-semibold shadow-sm transition-all self-start sm:self-auto"
        >
          <Plus className="w-4 h-4" />
          <span>Add Bank Product</span>
        </button>
      </div>

      {/* System Stats Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-5">
        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm flex items-center space-x-4">
          <div className="w-12 h-12 rounded-xl bg-blue-50 text-blue-600 flex items-center justify-center">
            <Users className="w-6 h-6" />
          </div>
          <div>
            <span className="text-xs font-semibold text-slate-500 uppercase">Registered Users</span>
            <div className="text-2xl font-bold text-slate-900">{stats?.totalUsers || 0}</div>
          </div>
        </div>

        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm flex items-center space-x-4">
          <div className="w-12 h-12 rounded-xl bg-indigo-50 text-indigo-600 flex items-center justify-center">
            <Landmark className="w-6 h-6" />
          </div>
          <div>
            <span className="text-xs font-semibold text-slate-500 uppercase">Bank Products</span>
            <div className="text-2xl font-bold text-slate-900">{stats?.totalBankProducts || 0}</div>
          </div>
        </div>

        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm flex items-center space-x-4">
          <div className="w-12 h-12 rounded-xl bg-purple-50 text-purple-600 flex items-center justify-center">
            <ReceiptText className="w-6 h-6" />
          </div>
          <div>
            <span className="text-xs font-semibold text-slate-500 uppercase">Tax Rule Slabs</span>
            <div className="text-2xl font-bold text-slate-900">{stats?.totalTaxRules || 0}</div>
          </div>
        </div>
      </div>

      {/* Bank Products Table */}
      <div className="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden space-y-3 p-5">
        <h3 className="font-bold text-slate-900 text-base">Active Bank Products & Rates</h3>

        <div className="overflow-x-auto border border-slate-200 rounded-xl">
          <table className="w-full text-left text-xs border-collapse">
            <thead className="bg-slate-50 text-slate-600 font-semibold">
              <tr>
                <th className="p-3">Bank Name</th>
                <th className="p-3">Product Name</th>
                <th className="p-3">Type</th>
                <th className="p-3 text-center">Interest Rate</th>
                <th className="p-3 text-center">Senior Rate</th>
                <th className="p-3 text-center">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {bankProducts.map((p) => (
                <tr key={p.id} className="hover:bg-slate-50">
                  <td className="p-3 font-semibold text-slate-900">{p.bankName}</td>
                  <td className="p-3 text-slate-700">{p.productName}</td>
                  <td className="p-3">
                    <span className="px-2 py-0.5 rounded-full bg-slate-100 text-slate-700 font-medium text-[11px]">
                      {p.productType}
                    </span>
                  </td>
                  <td className="p-3 text-center font-bold text-indigo-600">{p.interestRate}%</td>
                  <td className="p-3 text-center text-slate-600">
                    {p.seniorCitizenRate ? `${p.seniorCitizenRate}%` : '—'}
                  </td>
                  <td className="p-3 text-center">
                    <button
                      onClick={() => handleDeleteBankProduct(p.id)}
                      className="p-1 text-slate-400 hover:text-red-600 rounded transition-colors"
                      title="Delete Product"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Tax Rules View */}
      <div className="bg-white rounded-2xl border border-slate-200 shadow-sm p-5 space-y-3">
        <h3 className="font-bold text-slate-900 text-base">Maintained Tax Slabs (FY 2024-2025)</h3>
        <div className="overflow-x-auto border border-slate-200 rounded-xl">
          <table className="w-full text-left text-xs border-collapse">
            <thead className="bg-slate-50 text-slate-600 font-semibold">
              <tr>
                <th className="p-3">Regime</th>
                <th className="p-3">Min Range</th>
                <th className="p-3">Max Range</th>
                <th className="p-3 text-center">Rate</th>
                <th className="p-3">Description</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {taxRules.map((r) => (
                <tr key={r.id} className="hover:bg-slate-50">
                  <td className="p-3 font-bold text-indigo-600">{r.regime}</td>
                  <td className="p-3">₹{Number(r.slabMin).toLocaleString()}</td>
                  <td className="p-3">{r.slabMax ? `₹${Number(r.slabMax).toLocaleString()}` : 'No Limit'}</td>
                  <td className="p-3 text-center font-bold">{r.ratePercent}%</td>
                  <td className="p-3 text-slate-500">{r.description}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Add Bank Product Modal */}
      {bankModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/60 p-4 backdrop-blur-xs">
          <div className="bg-white rounded-2xl shadow-xl w-full max-w-md overflow-hidden border border-slate-100">
            <div className="flex items-center justify-between p-5 border-b border-slate-100">
              <h3 className="font-bold text-slate-900 text-lg">Add Bank Financial Product</h3>
              <button onClick={() => setBankModalOpen(false)} className="text-slate-400 hover:text-slate-600">
                <X className="w-5 h-5" />
              </button>
            </div>

            <form onSubmit={handleBankFormSubmit} className="p-5 space-y-3 text-xs">
              <div>
                <label className="block font-semibold text-slate-700 mb-1">Bank Name</label>
                <input
                  type="text"
                  required
                  value={bankForm.bankName}
                  onChange={(e) => setBankForm({ ...bankForm, bankName: e.target.value })}
                  placeholder="e.g. State Bank of India"
                  className="w-full p-2 bg-slate-50 border border-slate-200 rounded-xl"
                />
              </div>

              <div className="grid grid-cols-2 gap-2">
                <div>
                  <label className="block font-semibold text-slate-700 mb-1">Product Type</label>
                  <select
                    value={bankForm.productType}
                    onChange={(e) => setBankForm({ ...bankForm, productType: e.target.value })}
                    className="w-full p-2 bg-slate-50 border border-slate-200 rounded-xl"
                  >
                    <option value="FD">Fixed Deposit</option>
                    <option value="SAVINGS">Savings Account</option>
                    <option value="HOME_LOAN">Home Loan</option>
                    <option value="PERSONAL_LOAN">Personal Loan</option>
                    <option value="AUTO_LOAN">Auto Loan</option>
                  </select>
                </div>
                <div>
                  <label className="block font-semibold text-slate-700 mb-1">Product Name</label>
                  <input
                    type="text"
                    required
                    value={bankForm.productName}
                    onChange={(e) => setBankForm({ ...bankForm, productName: e.target.value })}
                    placeholder="e.g. Regular 1-Year FD"
                    className="w-full p-2 bg-slate-50 border border-slate-200 rounded-xl"
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-2">
                <div>
                  <label className="block font-semibold text-slate-700 mb-1">Interest Rate (% p.a.)</label>
                  <input
                    type="number"
                    step="0.05"
                    required
                    value={bankForm.interestRate}
                    onChange={(e) => setBankForm({ ...bankForm, interestRate: e.target.value })}
                    placeholder="e.g. 7.10"
                    className="w-full p-2 bg-slate-50 border border-slate-200 rounded-xl"
                  />
                </div>
                <div>
                  <label className="block font-semibold text-slate-700 mb-1">Senior Citizen Rate (%)</label>
                  <input
                    type="number"
                    step="0.05"
                    value={bankForm.seniorCitizenRate}
                    onChange={(e) => setBankForm({ ...bankForm, seniorCitizenRate: e.target.value })}
                    placeholder="e.g. 7.60"
                    className="w-full p-2 bg-slate-50 border border-slate-200 rounded-xl"
                  />
                </div>
              </div>

              <div>
                <label className="block font-semibold text-slate-700 mb-1">Features / Notes</label>
                <textarea
                  rows="2"
                  value={bankForm.features}
                  onChange={(e) => setBankForm({ ...bankForm, features: e.target.value })}
                  placeholder="Special conditions or features..."
                  className="w-full p-2 bg-slate-50 border border-slate-200 rounded-xl"
                />
              </div>

              <div className="flex justify-end space-x-2 pt-2">
                <button
                  type="button"
                  onClick={() => setBankModalOpen(false)}
                  className="px-4 py-2 border border-slate-200 rounded-xl hover:bg-slate-50 font-medium"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="px-4 py-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl font-bold shadow-sm"
                >
                  Save Product
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
