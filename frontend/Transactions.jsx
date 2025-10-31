import React, { useEffect, useState } from 'react'
import { transfer, fetchTransactions } from '../services/api'

export default function Transactions() {
  const [account, setAccount] = useState('')
  const [transactions, setTransactions] = useState([])
  const [error, setError] = useState(null)
  const [form, setForm] = useState({ fromAccount: '', toAccount: '', amount: '' })

  async function load() {
    if (!account) return
    try {
      const res = await fetchTransactions(account)
      if (Array.isArray(res)) {
        setTransactions(res)
      } else if (res == null) {
        setTransactions([])
      } else if (typeof res === 'object') {
        setTransactions([res])
      } else {
        setError(String(res))
        setTransactions([])
      }
    } catch (err) {
      setError(err.message)
    }
  }

  useEffect(() => { load() }, [account])

  async function doTransfer(e) {
    e.preventDefault()
    setError(null)
    try {
      const r = await transfer({ senderAccountNumber: form.fromAccount, receiverAccountNumber: form.toAccount, amount: parseFloat(form.amount) })
      // if backend returns created transaction or message, show it briefly
      if (r && typeof r === 'object') {
        // success
      }
      setForm({ fromAccount: '', toAccount: '', amount: '' })
      if (account === form.fromAccount || account === form.toAccount) load()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div>
      <h2>Transactions</h2>
      <div className="grid">
        <div className="card">
          <h3>Transfer</h3>
          <form onSubmit={doTransfer}>
            <label>From Account</label>
            <input value={form.fromAccount} onChange={e => setForm({ ...form, fromAccount: e.target.value })} />
            <label>To Account</label>
            <input value={form.toAccount} onChange={e => setForm({ ...form, toAccount: e.target.value })} />
            <label>Amount</label>
            <input value={form.amount} onChange={e => setForm({ ...form, amount: e.target.value })} />
            <button type="submit">Transfer</button>
            {error && <p className="error">{error}</p>}
          </form>
        </div>
        <div className="card">
          <h3>View by Account</h3>
          <label>Account Number</label>
          <input value={account} onChange={e => setAccount(e.target.value)} />
          <button onClick={load}>Load</button>

          <table className="table">
            <thead><tr><th>Id</th><th>Type</th><th>Amount</th><th>Time</th><th>Details</th></tr></thead>
            <tbody>
              {transactions.map(t => (
                <tr key={t.transactionId}>
                  <td>{t.transactionId}</td>
                  <td>{t.transactionType}</td>
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
  )
}
