import React, { useEffect, useState } from 'react'
import { useLocation } from 'react-router-dom'
import ClientNav from './ClientNav'
import { fetchTransactions, transfer } from '../../services/api'

function useQuery() { return new URLSearchParams(useLocation().search) }

export default function ClientTransactions() {
  const q = useQuery()
  const initialAccount = q.get('account') || ''
  const [account, setAccount] = useState(initialAccount)
  const [transactions, setTransactions] = useState([])
  const [error, setError] = useState(null)
  const [form, setForm] = useState({ to: '', amount: '', pin: '' })

  useEffect(() => { if (account) load() }, [account])

  async function load() {
    setError(null)
    try {
      const res = await fetchTransactions(account)
      if (Array.isArray(res)) setTransactions(res)
      else if (res == null) setTransactions([])
      else if (typeof res === 'object') setTransactions([res])
      else setError(String(res))
    } catch (err) {
      setError(err.message)
    }
  }

  async function doTransfer(e) {
    e.preventDefault()
    setError(null)
    try {
      await transfer({ senderAccountNumber: account, receiverAccountNumber: form.to, amount: parseFloat(form.amount), pin: form.pin })
      setForm({ to: '', amount: '', pin: '' })
      load()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div>
      <ClientNav />
      <div className="container">
        <div className="grid">
          <div className="card">
            <h3>Make Transfer</h3>
            <label>From Account</label>
            <input value={account} onChange={e => setAccount(e.target.value)} placeholder="Your account number" />
            <label>To Account</label>
            <input value={form.to} onChange={e => setForm({ ...form, to: e.target.value })} />
            <label>Amount</label>
            <input value={form.amount} onChange={e => setForm({ ...form, amount: e.target.value })} />
            <label>PIN</label>
            <input type="password" value={form.pin} onChange={e => setForm({ ...form, pin: e.target.value })} />
            <button onClick={doTransfer}>Transfer</button>
            {error && <p className="error">{error}</p>}
          </div>
          <div className="card">
            <h3>Transactions for {account}</h3>
            <table className="table">
              <thead><tr><th>Id</th><th>Amount</th><th>Time</th><th>Details</th></tr></thead>
              <tbody>
                {transactions.map(t => (
                  <tr key={t.transactionId}>
                    <td>{t.transactionId}</td>
                    <td>{t.transactionAmount}</td>
                    <td>{t.transactionTime}</td>
                    <td>{t.description || t.receiverDetails || t.senderDetails}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  )
}
