import React, { useEffect, useState } from 'react'
import ClientNav from './ClientNav'
import { fetchAccounts } from '../../services/api'

export default function ClientAccounts() {
  const [accounts, setAccounts] = useState([])
  const [error, setError] = useState(null)

  useEffect(() => { load() }, [])

  async function load() {
    setError(null)
    try {
      const res = await fetchAccounts()
      const phone = localStorage.getItem('client_phone')
      let list = []
      if (Array.isArray(res)) {
        list = res.filter(a => (a.phoneNumber || a.phoneNumberLinked || a.phone || a.phoneNumber) == phone)
      } else if (typeof res === 'object' && res != null) {
        const a = res
        if ((a.phoneNumber || a.phoneNumberLinked || a.phone) == phone) list = [a]
      }
      setAccounts(list)
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div>
      <ClientNav />
      <div className="container">
        <div className="card">
          <h3>Your Accounts</h3>
          {error && <p className="error">{error}</p>}
          <table className="table">
            <thead><tr><th>Account #</th><th>Type</th><th>Balance</th><th>Actions</th></tr></thead>
            <tbody>
              {accounts.map(a => (
                <tr key={a.accountId}>
                  <td>{a.accountNumber}</td>
                  <td>{a.accountType}</td>
                  <td>{a.balance}</td>
                  <td>
                    <a href={`/client/transactions?account=${encodeURIComponent(a.accountNumber)}`}>View</a>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          {accounts.length === 0 && <p className="muted">No accounts found for this phone number.</p>}
        </div>
      </div>
    </div>
  )
}
