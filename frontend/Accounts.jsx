import React, { useEffect, useState } from 'react'
import { fetchAccounts, createAccount } from '../services/api'

export default function Accounts() {
  const [list, setList] = useState([])
  const [form, setForm] = useState({ accountNumber: '', customerPhone: '', balance: '' })
  const [error, setError] = useState(null)

  useEffect(() => { load() }, [])
  async function load() {
    try {
      const res = await fetchAccounts()
      if (Array.isArray(res)) {
        setList(res)
      } else if (res == null) {
        setList([])
      } else if (typeof res === 'object') {
        setList([res])
      } else {
        setError(String(res))
        setList([])
      }
    } catch (err) {
      setError(err.message)
    }
  }

  async function onSubmit(e) {
    e.preventDefault()
    setError(null)
    try {
      // backend expects account with accountNumber, accountType(optional), phoneNumber (linked), balance
      const payload = {
        accountNumber: form.accountNumber,
        accountType: 'SAVINGS',
        phoneNumber: form.customerPhone,
        balance: parseFloat(form.balance || 0)
      }
      await createAccount(payload)
      setForm({ accountNumber: '', customerPhone: '', balance: '' })
      load()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div>
      <h2>Accounts</h2>
      <div className="grid">
        <div className="card">
          <h3>Create</h3>
          <form onSubmit={onSubmit}>
            <label>Account Number</label>
            <input value={form.accountNumber} onChange={e => setForm({ ...form, accountNumber: e.target.value })} />
            <label>Customer Phone</label>
            <input value={form.customerPhone} onChange={e => setForm({ ...form, customerPhone: e.target.value })} />
            <label>Initial Balance</label>
            <input value={form.balance} onChange={e => setForm({ ...form, balance: e.target.value })} />
            <button type="submit">Create</button>
            {error && <p className="error">{error}</p>}
          </form>
        </div>
        <div className="card">
          <h3>List</h3>
          <table className="table">
            <thead><tr><th>Id</th><th>AccountNumber</th><th>Balance</th><th>CustomerId</th></tr></thead>
            <tbody>
              {list.map(a => (
                <tr key={a.accountId}>
                  <td>{a.accountId}</td>
                  <td>{a.accountNumber}</td>
                  <td>{a.balance}</td>
                  <td>{a.customerId}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}
