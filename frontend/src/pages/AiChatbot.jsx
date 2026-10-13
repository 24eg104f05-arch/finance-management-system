import React, { useState, useEffect, useRef } from 'react';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import {
  Bot,
  User,
  Send,
  Sparkles,
  Trash2,
  RefreshCw,
  AlertCircle,
  HelpCircle,
  DollarSign
} from 'lucide-react';

const SUGGESTIONS = [
  'How much did I spend this month?',
  'What is my total income?',
  'Which category has the highest expenses?',
  'Am I exceeding my budget?',
  'How can I save more money?',
  'Explain my loan EMI.',
  'Explain my fixed deposit returns.',
  'How was my estimated tax calculated?'
];

export default function AiChatbot() {
  const { user } = useAuth();
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);
  const [fetchingHistory, setFetchingHistory] = useState(true);
  const [error, setError] = useState('');
  const chatEndRef = useRef(null);

  const fetchHistory = async () => {
    setFetchingHistory(true);
    try {
      const res = await api.get('/chatbot/history');
      setMessages(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setFetchingHistory(false);
    }
  };

  useEffect(() => {
    fetchHistory();
  }, []);

  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, loading]);

  const handleSend = async (question) => {
    const textToSend = question || input;
    if (!textToSend.trim() || loading) return;

    setError('');
    const userMessage = {
      role: 'user',
      content: textToSend.trim(),
      createdAt: new Date().toISOString(),
    };

    setMessages((prev) => [...prev, userMessage]);
    setInput('');
    setLoading(true);

    try {
      const res = await api.post('/chatbot/ask', { message: textToSend.trim() });
      const assistantMessage = {
        role: 'assistant',
        content: res.data.reply,
        createdAt: new Date().toISOString(),
      };
      setMessages((prev) => [...prev, assistantMessage]);
    } catch (err) {
      console.error(err);
      setError('Failed to reach AI financial assistant. Please check backend connection.');
    } finally {
      setLoading(false);
    }
  };

  const handleClearHistory = async () => {
    if (!window.confirm('Are you sure you want to clear your conversation history?')) return;
    try {
      await api.delete('/chatbot/history');
      setMessages([]);
    } catch (err) {
      console.error(err);
    }
  };

  // Helper to format basic markdown-style text safely
  const renderFormattedText = (text) => {
    if (!text) return null;
    const lines = text.split('\n');
    return lines.map((line, idx) => {
      // Headers
      if (line.startsWith('### ')) {
        return <h4 key={idx} className="font-bold text-slate-900 text-sm mt-2 mb-1">{line.replace('### ', '')}</h4>;
      }
      if (line.startsWith('## ')) {
        return <h3 key={idx} className="font-bold text-slate-900 text-base mt-2 mb-1">{line.replace('## ', '')}</h3>;
      }
      // Bullets
      if (line.startsWith('- ')) {
        return (
          <div key={idx} className="flex items-start space-x-2 my-0.5">
            <span className="text-indigo-500 font-bold">•</span>
            <span className="flex-1">{parseBold(line.replace('- ', ''))}</span>
          </div>
        );
      }
      if (line.trim() === '') {
        return <div key={idx} className="h-1.5" />;
      }
      return <p key={idx} className="my-0.5">{parseBold(line)}</p>;
    });
  };

  const parseBold = (str) => {
    const parts = str.split(/(\*\*.*?\*\*)/g);
    return parts.map((part, i) => {
      if (part.startsWith('**') && part.endsWith('**')) {
        return <strong key={i} className="font-semibold text-slate-900">{part.slice(2, -2)}</strong>;
      }
      return part;
    });
  };

  return (
    <div className="flex flex-col h-[calc(100vh-8rem)] max-w-4xl mx-auto space-y-4">
      {/* Header */}
      <div className="flex items-center justify-between bg-white px-5 py-4 rounded-2xl border border-slate-200 shadow-sm">
        <div className="flex items-center space-x-3">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-indigo-600 to-emerald-400 flex items-center justify-center text-white shadow-sm">
            <Bot className="w-6 h-6" />
          </div>
          <div>
            <div className="flex items-center space-x-2">
              <h2 className="font-bold text-slate-900 text-base">Financify AI Assistant</h2>
              <span className="px-2 py-0.5 text-[10px] font-bold bg-emerald-100 text-emerald-700 rounded-full flex items-center space-x-1">
                <span className="w-1.5 h-1.5 rounded-full bg-emerald-500 animate-pulse" />
                <span>Live Financial Context</span>
              </span>
            </div>
            <p className="text-xs text-slate-500">
              Personalized insights using your real income, transactions & budgets
            </p>
          </div>
        </div>

        <button
          onClick={handleClearHistory}
          className="p-2 text-slate-400 hover:text-red-600 hover:bg-red-50 rounded-xl transition-colors"
          title="Clear History"
        >
          <Trash2 className="w-4 h-4" />
        </button>
      </div>

      {/* Suggested Quick Prompts */}
      <div className="flex gap-2 overflow-x-auto pb-1 scrollbar-none">
        {SUGGESTIONS.map((suggestion, idx) => (
          <button
            key={idx}
            onClick={() => handleSend(suggestion)}
            className="text-xs px-3 py-1.5 whitespace-nowrap bg-white hover:bg-indigo-50 hover:text-indigo-700 border border-slate-200 hover:border-indigo-300 rounded-full font-medium text-slate-700 transition-all shadow-xs flex items-center space-x-1"
          >
            <Sparkles className="w-3 h-3 text-indigo-500" />
            <span>{suggestion}</span>
          </button>
        ))}
      </div>

      {/* Chat Messages Container */}
      <div className="flex-1 bg-white rounded-2xl border border-slate-200 shadow-sm p-4 overflow-y-auto space-y-4">
        {fetchingHistory ? (
          <div className="h-full flex items-center justify-center text-slate-400 text-sm">
            <div className="w-8 h-8 border-4 border-indigo-600 border-t-transparent rounded-full animate-spin mx-auto mb-2" />
            Loading chat history...
          </div>
        ) : messages.length === 0 ? (
          <div className="h-full flex flex-col items-center justify-center text-center p-6 text-slate-400">
            <div className="w-14 h-14 rounded-2xl bg-indigo-50 text-indigo-600 flex items-center justify-center mb-3">
              <Sparkles className="w-7 h-7" />
            </div>
            <h3 className="font-bold text-slate-800 text-base">How can I help you today?</h3>
            <p className="text-xs text-slate-500 max-w-md mt-1 mb-4">
              I have direct access to your verified financial records. Ask me anything about your monthly spending, income, budget overruns, or tax calculations.
            </p>
          </div>
        ) : (
          messages.map((m, idx) => {
            const isUser = m.role === 'user';
            return (
              <div
                key={idx}
                className={`flex items-start space-x-3 ${isUser ? 'flex-row-reverse space-x-reverse' : ''}`}
              >
                <div
                  className={`w-8 h-8 rounded-xl flex items-center justify-center text-xs flex-shrink-0 ${
                    isUser
                      ? 'bg-indigo-600 text-white'
                      : 'bg-emerald-500/10 text-emerald-600 border border-emerald-500/20'
                  }`}
                >
                  {isUser ? <User className="w-4 h-4" /> : <Bot className="w-4 h-4" />}
                </div>

                <div
                  className={`p-4 rounded-2xl max-w-[85%] text-sm leading-relaxed ${
                    isUser
                      ? 'bg-indigo-600 text-white rounded-tr-none shadow-xs'
                      : 'bg-slate-50 border border-slate-200 text-slate-800 rounded-tl-none'
                  }`}
                >
                  {isUser ? m.content : renderFormattedText(m.content)}
                </div>
              </div>
            );
          })
        )}

        {loading && (
          <div className="flex items-start space-x-3">
            <div className="w-8 h-8 rounded-xl bg-emerald-50 text-emerald-600 border border-emerald-200 flex items-center justify-center flex-shrink-0">
              <Bot className="w-4 h-4" />
            </div>
            <div className="p-4 rounded-2xl bg-slate-50 border border-slate-200 text-slate-500 text-sm rounded-tl-none flex items-center space-x-2">
              <span className="w-2 h-2 rounded-full bg-indigo-600 animate-bounce" />
              <span className="w-2 h-2 rounded-full bg-indigo-600 animate-bounce [animation-delay:0.2s]" />
              <span className="w-2 h-2 rounded-full bg-indigo-600 animate-bounce [animation-delay:0.4s]" />
              <span className="text-xs text-slate-400 pl-2">Consulting financial database...</span>
            </div>
          </div>
        )}

        <div ref={chatEndRef} />
      </div>

      {error && (
        <div className="p-3 bg-red-50 border border-red-200 rounded-xl text-red-700 text-xs flex items-center space-x-2">
          <AlertCircle className="w-4 h-4 flex-shrink-0" />
          <span>{error}</span>
        </div>
      )}

      {/* Input Form */}
      <form
        onSubmit={(e) => {
          e.preventDefault();
          handleSend();
        }}
        className="flex items-center space-x-2 bg-white p-2 rounded-2xl border border-slate-200 shadow-sm"
      >
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="Ask a question about your finances, budgets, loans, or taxes..."
          className="flex-1 px-4 py-2 text-sm bg-transparent focus:outline-none text-slate-900 placeholder-slate-400"
        />
        <button
          type="submit"
          disabled={!input.trim() || loading}
          className="p-2.5 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl transition-all shadow-sm disabled:opacity-40"
        >
          <Send className="w-4 h-4" />
        </button>
      </form>
    </div>
  );
}
