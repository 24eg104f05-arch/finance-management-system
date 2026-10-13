import React, { useState } from 'react';
import api from '../services/api';
import {
  ReceiptText,
  Info,
  ShieldCheck,
  CheckCircle,
  HelpCircle,
  ArrowRight,
  Sparkles,
  AlertTriangle
} from 'lucide-react';

export default function TaxEstimation() {
  const [financialYear, setFinancialYear] = useState('2024-2025');
  const [regime, setRegime] = useState('NEW');
  const [grossIncome, setGrossIncome] = useState('1200000');
  const [otherIncome, setOtherIncome] = useState('0');

  // Old Regime deductions
  const [section80C, setSection80C] = useState('150000');
  const [section80D, setSection80D] = useState('25000');
  const [section80CCD, setSection80CCD] = useState('50000');
  const [hra, setHra] = useState('0');
  const [homeLoan24b, setHomeLoan24b] = useState('0');
  const [otherDeductions, setOtherDeductions] = useState('0');

  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);

  const handleEstimate = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const payload = {
        financialYear,
        regime,
        grossAnnualIncome: parseFloat(grossIncome) || 0,
        incomeFromOtherSources: parseFloat(otherIncome) || 0,
        section80C: parseFloat(section80C) || 0,
        section80D: parseFloat(section80D) || 0,
        section80CCD1B: parseFloat(section80CCD) || 0,
        hraExemption: parseFloat(hra) || 0,
        homeLoanInterest24b: parseFloat(homeLoan24b) || 0,
        otherDeductions: parseFloat(otherDeductions) || 0,
      };

      const res = await api.post('/tax/estimate', payload);
      setResult(res.data);
    } catch (err) {
      console.error(err);
      alert('Failed to calculate tax estimation');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold text-slate-900 tracking-tight">Income Tax Estimation</h1>
        <p className="text-slate-500 text-sm mt-0.5">
          Estimate income tax under Indian New Tax Regime (Sec 115BAC) and Old Tax Regime
        </p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
        {/* Input Form */}
        <div className="lg:col-span-5 bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-4">
          <h3 className="font-bold text-slate-900 text-base">Tax Details & Deductions</h3>

          <form onSubmit={handleEstimate} className="space-y-4">
            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">Financial Year</label>
                <select
                  value={financialYear}
                  onChange={(e) => setFinancialYear(e.target.value)}
                  className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option value="2024-2025">FY 2024-25 (AY 2025-26)</option>
                  <option value="2025-2026">FY 2025-26 (AY 2026-27)</option>
                </select>
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">Tax Regime</label>
                <select
                  value={regime}
                  onChange={(e) => setRegime(e.target.value)}
                  className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option value="NEW">New Tax Regime (Default)</option>
                  <option value="OLD">Old Tax Regime</option>
                </select>
              </div>
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-700 mb-1">Gross Annual Salary (₹)</label>
              <input
                type="number"
                min="0"
                step="10000"
                required
                value={grossIncome}
                onChange={(e) => setGrossIncome(e.target.value)}
                className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-700 mb-1">
                Income from Other Sources (Interest, etc.)
              </label>
              <input
                type="number"
                min="0"
                step="5000"
                value={otherIncome}
                onChange={(e) => setOtherIncome(e.target.value)}
                className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
            </div>

            {/* Deductions - visible mainly for Old Regime */}
            <div className={`p-4 rounded-xl border transition-all ${regime === 'OLD' ? 'bg-amber-50/50 border-amber-200' : 'bg-slate-50 border-slate-200'}`}>
              <div className="flex items-center justify-between mb-2">
                <span className="text-xs font-bold text-slate-800">
                  {regime === 'OLD' ? 'Old Regime Deductions (Chapter VI-A)' : 'Deductions (Applicable only in Old Regime)'}
                </span>
                {regime === 'NEW' && (
                  <span className="text-[10px] text-slate-400">Ignored in New Regime</span>
                )}
              </div>

              <div className="space-y-3">
                <div className="grid grid-cols-2 gap-2">
                  <div>
                    <label className="block text-[11px] text-slate-600 mb-1">Sec 80C (Max 1.5L)</label>
                    <input
                      type="number"
                      disabled={regime === 'NEW'}
                      value={section80C}
                      onChange={(e) => setSection80C(e.target.value)}
                      className="w-full p-2 bg-white border border-slate-200 rounded-lg text-xs disabled:opacity-50"
                    />
                  </div>
                  <div>
                    <label className="block text-[11px] text-slate-600 mb-1">Sec 80D (Health Ins.)</label>
                    <input
                      type="number"
                      disabled={regime === 'NEW'}
                      value={section80D}
                      onChange={(e) => setSection80D(e.target.value)}
                      className="w-full p-2 bg-white border border-slate-200 rounded-lg text-xs disabled:opacity-50"
                    />
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-2">
                  <div>
                    <label className="block text-[11px] text-slate-600 mb-1">80CCD(1B) NPS (50k)</label>
                    <input
                      type="number"
                      disabled={regime === 'NEW'}
                      value={section80CCD}
                      onChange={(e) => setSection80CCD(e.target.value)}
                      className="w-full p-2 bg-white border border-slate-200 rounded-lg text-xs disabled:opacity-50"
                    />
                  </div>
                  <div>
                    <label className="block text-[11px] text-slate-600 mb-1">HRA Exemption</label>
                    <input
                      type="number"
                      disabled={regime === 'NEW'}
                      value={hra}
                      onChange={(e) => setHra(e.target.value)}
                      className="w-full p-2 bg-white border border-slate-200 rounded-lg text-xs disabled:opacity-50"
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-[11px] text-slate-600 mb-1">Home Loan Interest Sec 24(b) (Max 2L)</label>
                  <input
                    type="number"
                    disabled={regime === 'NEW'}
                    value={homeLoan24b}
                    onChange={(e) => setHomeLoan24b(e.target.value)}
                    className="w-full p-2 bg-white border border-slate-200 rounded-lg text-xs disabled:opacity-50"
                  />
                </div>
              </div>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full py-3 bg-indigo-600 hover:bg-indigo-700 text-white font-bold text-sm rounded-xl transition-all shadow-sm disabled:opacity-60"
            >
              {loading ? 'Estimating Tax...' : 'Calculate Tax Liability'}
            </button>
          </form>
        </div>

        {/* Results Card */}
        <div className="lg:col-span-7 space-y-5">
          {result ? (
            <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-6">
              {/* Regime Recommendation Banner */}
              {result.regimeComparisonNote && (
                <div className="p-4 rounded-xl bg-gradient-to-r from-emerald-500/10 to-indigo-500/10 border border-emerald-500/30 flex items-start space-x-3 text-slate-800">
                  <Sparkles className="w-5 h-5 flex-shrink-0 text-emerald-600 mt-0.5" />
                  <div>
                    <h4 className="font-bold text-sm text-slate-900">
                      Regime Comparison Recommendation
                    </h4>
                    <p className="text-xs text-slate-600 mt-0.5">{result.regimeComparisonNote}</p>
                    <div className="mt-2 text-xs font-semibold text-indigo-700 flex space-x-4">
                      <span>Selected ({result.regime}): ₹{Number(result.totalTaxPayable).toLocaleString()}</span>
                      <span>Alternate: ₹{Number(result.alternateRegimeTaxPayable).toLocaleString()}</span>
                    </div>
                  </div>
                </div>
              )}

              {/* Primary Output Numbers */}
              <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
                <div className="p-4 rounded-xl bg-slate-50 border border-slate-200">
                  <span className="text-xs font-semibold text-slate-500 uppercase">Gross Income</span>
                  <div className="text-xl font-bold text-slate-900 mt-1">
                    ₹{Number(result.grossTotalIncome).toLocaleString()}
                  </div>
                  <div className="text-[11px] text-slate-400 mt-1">
                    Std. Deduction: ₹{Number(result.standardDeduction).toLocaleString()}
                  </div>
                </div>

                <div className="p-4 rounded-xl bg-slate-50 border border-slate-200">
                  <span className="text-xs font-semibold text-slate-500 uppercase">Net Taxable Income</span>
                  <div className="text-xl font-bold text-slate-900 mt-1">
                    ₹{Number(result.netTaxableIncome).toLocaleString()}
                  </div>
                  <div className="text-[11px] text-slate-400 mt-1">
                    Deductions: ₹{Number(result.totalDeductions).toLocaleString()}
                  </div>
                </div>

                <div className="p-4 rounded-xl bg-red-50 border border-red-200">
                  <span className="text-xs font-semibold text-red-600 uppercase">Estimated Tax</span>
                  <div className="text-xl font-bold text-red-700 mt-1">
                    ₹{Number(result.totalTaxPayable).toLocaleString()}
                  </div>
                  <div className="text-[11px] text-red-500 mt-1">
                    Effective Rate: {Number(result.effectiveTaxRate).toFixed(2)}%
                  </div>
                </div>
              </div>

              {/* Detailed Breakdown */}
              <div className="space-y-3">
                <h4 className="font-bold text-slate-900 text-sm">Progressive Slab Computation</h4>
                <div className="overflow-x-auto border border-slate-200 rounded-xl">
                  <table className="w-full text-left text-xs border-collapse">
                    <thead className="bg-slate-50 font-semibold text-slate-600">
                      <tr>
                        <th className="p-2.5">Income Slab Range</th>
                        <th className="p-2.5 text-center">Tax Rate</th>
                        <th className="p-2.5 text-right">Taxable in Slab</th>
                        <th className="p-2.5 text-right">Tax Amount</th>
                      </tr>
                    </thead>
                    <tbody className="divide-y divide-slate-100">
                      {(result.slabBreakdowns || []).map((slab, i) => (
                        <tr key={i} className="hover:bg-slate-50">
                          <td className="p-2.5 font-medium">{slab.slabRange}</td>
                          <td className="p-2.5 text-center">{slab.ratePercent}%</td>
                          <td className="p-2.5 text-right">₹{Number(slab.taxableAmountInSlab).toLocaleString()}</td>
                          <td className="p-2.5 text-right font-semibold">₹{Number(slab.taxForSlab).toLocaleString()}</td>
                        </tr>
                      ))}
                      <tr className="bg-slate-50 font-semibold">
                        <td colSpan="3" className="p-2.5 text-right text-slate-700">Base Tax Before Rebate:</td>
                        <td className="p-2.5 text-right text-slate-900">₹{Number(result.taxBeforeRebate).toLocaleString()}</td>
                      </tr>
                      {Number(result.rebate87A) > 0 && (
                        <tr className="text-emerald-700 font-semibold">
                          <td colSpan="3" className="p-2.5 text-right">Less: Section 87A Tax Rebate:</td>
                          <td className="p-2.5 text-right">-₹{Number(result.rebate87A).toLocaleString()}</td>
                        </tr>
                      )}
                      <tr>
                        <td colSpan="3" className="p-2.5 text-right text-slate-600">Add: 4% Health & Education Cess:</td>
                        <td className="p-2.5 text-right text-slate-900">₹{Number(result.healthAndEducationCess).toLocaleString()}</td>
                      </tr>
                      <tr className="bg-indigo-50 text-indigo-900 font-bold text-sm">
                        <td colSpan="3" className="p-3 text-right">Total Estimated Tax Payable:</td>
                        <td className="p-3 text-right">₹{Number(result.totalTaxPayable).toLocaleString()}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>

              {/* Official Disclaimer */}
              <div className="p-4 rounded-xl bg-amber-50 border border-amber-200 text-xs text-amber-900 flex items-start space-x-2">
                <AlertTriangle className="w-4 h-4 flex-shrink-0 text-amber-600 mt-0.5" />
                <p><strong>Disclaimer: </strong>{result.disclaimer}</p>
              </div>
            </div>
          ) : (
            <div className="bg-white p-12 rounded-2xl border border-slate-200 text-center text-slate-400 h-full flex flex-col items-center justify-center">
              <ReceiptText className="w-12 h-12 mb-3 opacity-20" />
              <h3 className="font-semibold text-slate-700 text-base">Estimate will appear here</h3>
              <p className="text-xs text-slate-400 mt-1">
                Enter your annual income and select your financial year to view full tax computations.
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
