import React, { useState, useEffect } from 'react';
import api from '../services/api';
import {
  Landmark,
  TrendingUp,
  Percent,
  CheckCircle,
  ShieldAlert,
  ArrowRight,
  Search,
  Filter,
  DollarSign,
  Calendar,
  Sparkles
} from 'lucide-react';

const PRODUCT_TABS = [
  { id: 'FD', label: 'Fixed Deposits (FD)' },
  { id: 'SAVINGS', label: 'Savings Accounts' },
  { id: 'HOME_LOAN', label: 'Home Loans' },
  { id: 'PERSONAL_LOAN', label: 'Personal Loans' },
  { id: 'AUTO_LOAN', label: 'Auto Loans' },
];

export default function BankRates() {
  const [activeTab, setActiveTab] = useState('FD');
  const [products, setProducts] = useState([]);
  const [comparisonResults, setComparisonResults] = useState([]);
  const [loading, setLoading] = useState(true);

  // Comparison form parameters
  const [calcAmount, setCalcAmount] = useState('100000');
  const [calcMonths, setCalcMonths] = useState('12');
  const [isSeniorCitizen, setIsSeniorCitizen] = useState(false);
  const [comparing, setComparing] = useState(false);

  const fetchProducts = async () => {
    setLoading(true);
    try {
      const res = await api.get(`/banks/by-type?type=${activeTab}`);
      setProducts(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const runComparison = async () => {
    if (!calcAmount || !calcMonths) return;
    setComparing(true);
    try {
      const res = await api.post('/banks/compare', {
        productType: activeTab,
        amount: parseFloat(calcAmount),
        tenureMonths: parseInt(calcMonths, 10),
        isSeniorCitizen: isSeniorCitizen,
      });
      setComparisonResults(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setComparing(false);
    }
  };

  useEffect(() => {
    fetchProducts();
    // Default tenure suggestions based on product
    if (activeTab === 'FD') setCalcMonths('12');
    else if (activeTab === 'SAVINGS') setCalcMonths('12');
    else if (activeTab === 'HOME_LOAN') setCalcMonths('240');
    else if (activeTab === 'PERSONAL_LOAN') setCalcMonths('36');
    else if (activeTab === 'AUTO_LOAN') setCalcMonths('60');
  }, [activeTab]);

  useEffect(() => {
    runComparison();
  }, [activeTab, isSeniorCitizen]);

  const isDepositType = activeTab === 'FD' || activeTab === 'SAVINGS';

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-900 tracking-tight">
            Bank Interest Rate Comparison
          </h1>
          <p className="text-slate-500 text-sm mt-0.5">
            Compare live savings, fixed deposit yields, and loan EMIs across premier Indian banks
          </p>
        </div>
        <div className="inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold bg-emerald-50 text-emerald-700 border border-emerald-200">
          <CheckCircle className="w-3.5 h-3.5 mr-1 text-emerald-600" />
          <span>Rates Verified & Active</span>
        </div>
      </div>

      {/* Product Tabs */}
      <div className="flex space-x-2 border-b border-slate-200 overflow-x-auto pb-1">
        {PRODUCT_TABS.map((tab) => (
          <button
            key={tab.id}
            onClick={() => setActiveTab(tab.id)}
            className={`px-4 py-2.5 text-sm font-semibold whitespace-nowrap rounded-t-xl transition-all ${
              activeTab === tab.id
                ? 'bg-white border-t-2 border-indigo-600 text-indigo-600 shadow-xs'
                : 'text-slate-500 hover:text-slate-900 hover:bg-slate-100'
            }`}
          >
            {tab.label}
          </button>
        ))}
      </div>

      {/* Interactive Yield / EMI Comparison Widget */}
      <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-4">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-2">
          <h3 className="font-bold text-slate-900 text-base">
            {isDepositType ? 'Simulate Maturity Returns' : 'Simulate Loan Monthly EMI'}
          </h3>
          {isDepositType && (
            <label className="flex items-center space-x-2 text-xs font-medium text-slate-700 cursor-pointer">
              <input
                type="checkbox"
                checked={isSeniorCitizen}
                onChange={(e) => setIsSeniorCitizen(e.target.checked)}
                className="rounded text-indigo-600 focus:ring-indigo-500 h-4 w-4"
              />
              <span>Senior Citizen (Special Higher Rate)</span>
            </label>
          )}
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-3 gap-3">
          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-1">
              {isDepositType ? 'Deposit Principal (₹)' : 'Loan Amount (₹)'}
            </label>
            <input
              type="number"
              value={calcAmount}
              onChange={(e) => setCalcAmount(e.target.value)}
              className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
          </div>
          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-1">
              Tenure (Months)
            </label>
            <input
              type="number"
              value={calcMonths}
              onChange={(e) => setCalcMonths(e.target.value)}
              className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
          </div>
          <div className="flex items-end">
            <button
              onClick={runComparison}
              disabled={comparing}
              className="w-full py-2 bg-indigo-600 hover:bg-indigo-700 text-white font-semibold text-sm rounded-xl transition-all shadow-sm"
            >
              {comparing ? 'Calculating...' : 'Recalculate Table'}
            </button>
          </div>
        </div>
      </div>

      {/* Comparison Results Table */}
      <div className="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
        {loading ? (
          <div className="p-8 text-center text-slate-500 text-sm">
            <div className="w-8 h-8 border-4 border-indigo-600 border-t-transparent rounded-full animate-spin mx-auto mb-2" />
            Comparing bank rates...
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="bg-slate-50 border-b border-slate-200 text-xs font-semibold text-slate-500 uppercase tracking-wider">
                  <th className="py-3.5 px-4">Bank & Product</th>
                  <th className="py-3.5 px-4 text-center">Interest Rate</th>
                  {isDepositType ? (
                    <>
                      <th className="py-3.5 px-4 text-right">Interest Earned</th>
                      <th className="py-3.5 px-4 text-right">Total Maturity</th>
                    </>
                  ) : (
                    <>
                      <th className="py-3.5 px-4 text-right">Monthly EMI</th>
                      <th className="py-3.5 px-4 text-right">Total Repayment</th>
                    </>
                  )}
                  <th className="py-3.5 px-4">Key Features</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 text-sm">
                {(comparisonResults.length > 0 ? comparisonResults : products).map((p, idx) => {
                  const appliedRate = p.appliedRate || p.interestRate;
                  return (
                    <tr key={p.id || idx} className="hover:bg-slate-50/70 transition-colors">
                      <td className="py-4 px-4">
                        <div className="font-bold text-slate-900">{p.bankName}</div>
                        <div className="text-xs text-slate-500">{p.productName}</div>
                      </td>
                      <td className="py-4 px-4 text-center">
                        <span className="inline-flex items-center px-2.5 py-1 rounded-full text-xs font-bold bg-indigo-50 text-indigo-700 border border-indigo-200">
                          {appliedRate}% p.a.
                        </span>
                      </td>

                      {isDepositType ? (
                        <>
                          <td className="py-4 px-4 text-right font-semibold text-emerald-600">
                            +₹{Number(p.estimatedInterest || 0).toLocaleString()}
                          </td>
                          <td className="py-4 px-4 text-right font-bold text-slate-900">
                            ₹{Number(p.maturityAmount || 0).toLocaleString()}
                          </td>
                        </>
                      ) : (
                        <>
                          <td className="py-4 px-4 text-right font-bold text-indigo-600">
                            ₹{Number(p.monthlyEmi || 0).toLocaleString()} /mo
                          </td>
                          <td className="py-4 px-4 text-right text-slate-700">
                            ₹{Number(p.totalRepayment || 0).toLocaleString()}
                          </td>
                        </>
                      )}

                      <td className="py-4 px-4 text-xs text-slate-500 max-w-xs">
                        {p.features || 'Standard banking product terms apply.'}
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Transparency Note */}
      <div className="p-4 rounded-xl bg-slate-100 border border-slate-200 text-xs text-slate-600 flex items-start space-x-2">
        <Sparkles className="w-4 h-4 flex-shrink-0 text-indigo-500 mt-0.5" />
        <p>
          Rates are updated in accordance with published bank schedules. Fixed deposit calculations reflect quarterly compounding standard across Indian commercial banks. Loan EMIs are based on standard reducing balance amortisation.
        </p>
      </div>
    </div>
  );
}
