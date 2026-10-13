import React, { useState } from 'react';
import api from '../services/api';
import {
  Calculator,
  Percent,
  Calendar,
  Layers,
  ArrowRight,
  HelpCircle,
  FileSpreadsheet
} from 'lucide-react';

export default function Calculators() {
  const [calcType, setCalcType] = useState('LOAN_EMI'); // 'LOAN_EMI', 'FD', 'COMPOUND', 'SIMPLE'
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);

  // Simple Interest State
  const [siPrincipal, setSiPrincipal] = useState('100000');
  const [siRate, setSiRate] = useState('7.5');
  const [siYears, setSiYears] = useState('3');

  // Compound Interest State
  const [ciPrincipal, setCiPrincipal] = useState('100000');
  const [ciRate, setCiRate] = useState('8.0');
  const [ciYears, setCiYears] = useState('5');
  const [ciFrequency, setCiFrequency] = useState('4'); // 4 = quarterly

  // FD State
  const [fdDeposit, setFdDeposit] = useState('150000');
  const [fdRate, setFdRate] = useState('7.1');
  const [fdMonths, setFdMonths] = useState('24');

  // Loan EMI State
  const [loanAmount, setLoanAmount] = useState('500000');
  const [loanRate, setLoanRate] = useState('9.0');
  const [loanMonths, setLoanMonths] = useState('36');

  const handleCalculate = async (e) => {
    e.preventDefault();
    setLoading(true);
    setResult(null);

    try {
      let endpoint = '';
      let payload = {};

      if (calcType === 'SIMPLE') {
        endpoint = '/calculator/simple-interest';
        payload = {
          principal: parseFloat(siPrincipal),
          rate: parseFloat(siRate),
          timeYears: parseFloat(siYears),
        };
      } else if (calcType === 'COMPOUND') {
        endpoint = '/calculator/compound-interest';
        payload = {
          principal: parseFloat(ciPrincipal),
          rate: parseFloat(ciRate),
          timeYears: parseFloat(ciYears),
          compoundingFrequency: parseInt(ciFrequency, 10),
        };
      } else if (calcType === 'FD') {
        endpoint = '/calculator/fd';
        payload = {
          depositAmount: parseFloat(fdDeposit),
          annualRate: parseFloat(fdRate),
          tenureMonths: parseInt(fdMonths, 10),
          compoundingFrequency: 4,
        };
      } else if (calcType === 'LOAN_EMI') {
        endpoint = '/calculator/loan-emi';
        payload = {
          loanAmount: parseFloat(loanAmount),
          annualRate: parseFloat(loanRate),
          tenureMonths: parseInt(loanMonths, 10),
        };
      }

      const res = await api.post(endpoint, payload);
      setResult(res.data);
    } catch (err) {
      console.error(err);
      alert('Error calculating result. Please check inputs.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold text-slate-900 tracking-tight">Financial Calculators</h1>
        <p className="text-slate-500 text-sm mt-0.5">
          High precision calculators for loans, fixed deposits, and simple & compound interest
        </p>
      </div>

      {/* Tabs */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-2 bg-slate-200/60 p-1.5 rounded-2xl">
        <button
          onClick={() => { setCalcType('LOAN_EMI'); setResult(null); }}
          className={`py-2 px-3 text-xs sm:text-sm font-bold rounded-xl transition-all ${
            calcType === 'LOAN_EMI'
              ? 'bg-white text-indigo-600 shadow-sm'
              : 'text-slate-600 hover:text-slate-900'
          }`}
        >
          🏦 Loan EMI
        </button>
        <button
          onClick={() => { setCalcType('FD'); setResult(null); }}
          className={`py-2 px-3 text-xs sm:text-sm font-bold rounded-xl transition-all ${
            calcType === 'FD'
              ? 'bg-white text-indigo-600 shadow-sm'
              : 'text-slate-600 hover:text-slate-900'
          }`}
        >
          📈 Fixed Deposit (FD)
        </button>
        <button
          onClick={() => { setCalcType('COMPOUND'); setResult(null); }}
          className={`py-2 px-3 text-xs sm:text-sm font-bold rounded-xl transition-all ${
            calcType === 'COMPOUND'
              ? 'bg-white text-indigo-600 shadow-sm'
              : 'text-slate-600 hover:text-slate-900'
          }`}
        >
          🔄 Compound Interest
        </button>
        <button
          onClick={() => { setCalcType('SIMPLE'); setResult(null); }}
          className={`py-2 px-3 text-xs sm:text-sm font-bold rounded-xl transition-all ${
            calcType === 'SIMPLE'
              ? 'bg-white text-indigo-600 shadow-sm'
              : 'text-slate-600 hover:text-slate-900'
          }`}
        >
          ➕ Simple Interest
        </button>
      </div>

      {/* Calculator Body */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
        {/* Form Inputs */}
        <div className="lg:col-span-5 bg-white p-6 rounded-2xl border border-slate-200 shadow-sm">
          <h3 className="font-bold text-slate-900 text-base mb-4">Enter Parameters</h3>

          <form onSubmit={handleCalculate} className="space-y-4">
            {/* LOAN EMI INPUTS */}
            {calcType === 'LOAN_EMI' && (
              <>
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Loan Amount (₹)</label>
                  <input
                    type="number"
                    min="1000"
                    step="1000"
                    required
                    value={loanAmount}
                    onChange={(e) => setLoanAmount(e.target.value)}
                    className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Annual Interest Rate (% p.a.)</label>
                  <input
                    type="number"
                    step="0.05"
                    min="0.1"
                    required
                    value={loanRate}
                    onChange={(e) => setLoanRate(e.target.value)}
                    className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Loan Tenure (Months)</label>
                  <input
                    type="number"
                    min="1"
                    max="360"
                    required
                    value={loanMonths}
                    onChange={(e) => setLoanMonths(e.target.value)}
                    className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
              </>
            )}

            {/* FD INPUTS */}
            {calcType === 'FD' && (
              <>
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Deposit Amount (₹)</label>
                  <input
                    type="number"
                    min="100"
                    step="500"
                    required
                    value={fdDeposit}
                    onChange={(e) => setFdDeposit(e.target.value)}
                    className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Annual Rate (% p.a.)</label>
                  <input
                    type="number"
                    step="0.05"
                    min="0.1"
                    required
                    value={fdRate}
                    onChange={(e) => setFdRate(e.target.value)}
                    className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Tenure (Months)</label>
                  <input
                    type="number"
                    min="1"
                    max="120"
                    required
                    value={fdMonths}
                    onChange={(e) => setFdMonths(e.target.value)}
                    className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
              </>
            )}

            {/* COMPOUND INTEREST INPUTS */}
            {calcType === 'COMPOUND' && (
              <>
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Principal (₹)</label>
                  <input
                    type="number"
                    min="1"
                    step="100"
                    required
                    value={ciPrincipal}
                    onChange={(e) => setCiPrincipal(e.target.value)}
                    className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Annual Rate (%)</label>
                  <input
                    type="number"
                    step="0.1"
                    min="0.1"
                    required
                    value={ciRate}
                    onChange={(e) => setCiRate(e.target.value)}
                    className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Time Period (Years)</label>
                  <input
                    type="number"
                    step="0.5"
                    min="0.5"
                    required
                    value={ciYears}
                    onChange={(e) => setCiYears(e.target.value)}
                    className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Compounding Frequency</label>
                  <select
                    value={ciFrequency}
                    onChange={(e) => setCiFrequency(e.target.value)}
                    className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  >
                    <option value="1">Annually (1x per year)</option>
                    <option value="2">Semi-Annually (2x per year)</option>
                    <option value="4">Quarterly (4x per year - Standard)</option>
                    <option value="12">Monthly (12x per year)</option>
                  </select>
                </div>
              </>
            )}

            {/* SIMPLE INTEREST INPUTS */}
            {calcType === 'SIMPLE' && (
              <>
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Principal (₹)</label>
                  <input
                    type="number"
                    min="1"
                    step="100"
                    required
                    value={siPrincipal}
                    onChange={(e) => setSiPrincipal(e.target.value)}
                    className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Annual Rate (%)</label>
                  <input
                    type="number"
                    step="0.1"
                    min="0.1"
                    required
                    value={siRate}
                    onChange={(e) => setSiRate(e.target.value)}
                    className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Time Period (Years)</label>
                  <input
                    type="number"
                    step="0.5"
                    min="0.5"
                    required
                    value={siYears}
                    onChange={(e) => setSiYears(e.target.value)}
                    className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
              </>
            )}

            <button
              type="submit"
              disabled={loading}
              className="w-full py-3 bg-indigo-600 hover:bg-indigo-700 text-white font-bold text-sm rounded-xl transition-all shadow-sm shadow-indigo-500/20 disabled:opacity-60"
            >
              {loading ? 'Computing...' : 'Calculate Now'}
            </button>
          </form>
        </div>

        {/* Results Card */}
        <div className="lg:col-span-7 space-y-5">
          {result ? (
            <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-6">
              <h3 className="font-bold text-slate-900 text-lg border-b border-slate-100 pb-3">
                Calculation Breakdown
              </h3>

              {/* Primary Key Outputs */}
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                {result.monthlyEmi && (
                  <div className="p-4 rounded-xl bg-indigo-50 border border-indigo-100">
                    <span className="text-xs font-semibold text-indigo-700 uppercase">Monthly EMI</span>
                    <div className="text-2xl font-bold text-indigo-900 mt-1">
                      ₹{Number(result.monthlyEmi).toLocaleString()}
                    </div>
                  </div>
                )}

                <div className="p-4 rounded-xl bg-emerald-50 border border-emerald-100">
                  <span className="text-xs font-semibold text-emerald-700 uppercase">
                    {result.monthlyEmi ? 'Total Interest Payable' : 'Total Interest Earned'}
                  </span>
                  <div className="text-2xl font-bold text-emerald-900 mt-1">
                    ₹{Number(result.interestEarnedOrPaid).toLocaleString()}
                  </div>
                </div>

                <div className="p-4 rounded-xl bg-slate-50 border border-slate-200">
                  <span className="text-xs font-semibold text-slate-600 uppercase">
                    {result.monthlyEmi ? 'Total Loan Repayment' : 'Total Maturity Value'}
                  </span>
                  <div className="text-2xl font-bold text-slate-900 mt-1">
                    ₹{Number(result.totalAmount).toLocaleString()}
                  </div>
                </div>

                <div className="p-4 rounded-xl bg-slate-50 border border-slate-200">
                  <span className="text-xs font-semibold text-slate-600 uppercase">Principal Base</span>
                  <div className="text-2xl font-bold text-slate-900 mt-1">
                    ₹{Number(result.principalAmount).toLocaleString()}
                  </div>
                </div>
              </div>

              {/* Formula & Assumptions */}
              <div className="p-4 rounded-xl bg-slate-50 border border-slate-100 text-xs text-slate-600 space-y-2">
                <div>
                  <strong className="text-slate-800">Formula: </strong>
                  <code>{result.formulaUsed}</code>
                </div>
                <div>
                  <strong className="text-slate-800">Assumptions: </strong>
                  <span>{result.assumptions}</span>
                </div>
              </div>

              {/* Amortization Schedule if Loan EMI */}
              {result.amortizationSchedule && result.amortizationSchedule.length > 0 && (
                <div className="space-y-3 pt-2">
                  <div className="flex items-center justify-between">
                    <h4 className="font-bold text-slate-900 text-sm flex items-center space-x-1.5">
                      <FileSpreadsheet className="w-4 h-4 text-indigo-600" />
                      <span>Monthly Amortization Schedule (First 36 Months)</span>
                    </h4>
                  </div>
                  <div className="overflow-x-auto max-h-60 border border-slate-200 rounded-xl">
                    <table className="w-full text-left text-xs border-collapse">
                      <thead className="bg-slate-100 sticky top-0 font-semibold text-slate-600">
                        <tr>
                          <th className="p-2.5">Month</th>
                          <th className="p-2.5">Balance</th>
                          <th className="p-2.5">EMI</th>
                          <th className="p-2.5">Principal</th>
                          <th className="p-2.5">Interest</th>
                          <th className="p-2.5">Remaining</th>
                        </tr>
                      </thead>
                      <tbody className="divide-y divide-slate-100">
                        {result.amortizationSchedule.map((row) => (
                          <tr key={row.monthNumber} className="hover:bg-slate-50">
                            <td className="p-2 font-medium">{row.monthNumber}</td>
                            <td className="p-2">₹{Number(row.beginningBalance).toLocaleString()}</td>
                            <td className="p-2 font-semibold">₹{Number(row.emiAmount).toLocaleString()}</td>
                            <td className="p-2 text-indigo-600">₹{Number(row.principalComponent).toLocaleString()}</td>
                            <td className="p-2 text-red-500">₹{Number(row.interestComponent).toLocaleString()}</td>
                            <td className="p-2">₹{Number(row.endingBalance).toLocaleString()}</td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </div>
              )}
            </div>
          ) : (
            <div className="bg-white p-12 rounded-2xl border border-slate-200 text-center text-slate-400 h-full flex flex-col items-center justify-center">
              <Calculator className="w-12 h-12 mb-3 opacity-20" />
              <h3 className="font-semibold text-slate-700 text-base">Results will appear here</h3>
              <p className="text-xs text-slate-400 mt-1">
                Enter your parameters and click "Calculate Now" to see financial breakdowns and schedules.
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
