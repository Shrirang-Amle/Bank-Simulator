import React, { useEffect, useState } from 'react'
import { fetchCustomers, createCustomer } from '../services/api'

export default function Customers() {
  const [list, setList] = useState([])
  const [form, setForm] = useState({ name: '', phoneNumber: '', email: '', address: '', pin: '', aadharNumber: '', dob: '' })
  const [error, setError] = useState(null)

  useEffect(() => { load() }, [])
  async function load() {
    try {
      const res = await fetchCustomers()
      // handle non-array responses (error text or single object)
      if (Array.isArray(res)) {
        setList(res)
      } else if (res == null) {
        setList([])
      } else if (typeof res === 'object') {
        // maybe single created response or object - try to normalize
        setList([res])
      } else {
        // text or unexpected
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
      // normalize payload to match backend DTO names
      const payload = {
        name: form.name,
        phoneNumber: form.phoneNumber,
        email: form.email,
        address: form.address,
        pin: form.pin || form.customerPin,
        aadharNumber: form.aadharNumber,
        dob: form.dob || null
      }
      const created = await createCustomer(payload)
      setForm({ name: '', phoneNumber: '', email: '', address: '', pin: '', aadharNumber: '', dob: '' })
      // if server returned created id or message, refresh list
      load()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div>
      <h2>Customers</h2>
      <div className="grid">
        <div className="card">
          <h3>Create</h3>
          <form onSubmit={onSubmit}>
            <label>Name</label>
            <input value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} />
            <label>Phone</label>
            <input value={form.phoneNumber} onChange={e => setForm({ ...form, phoneNumber: e.target.value })} />
            <label>Email</label>
            <input value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} />
            <label>Address</label>
            <input value={form.address} onChange={e => setForm({ ...form, address: e.target.value })} />
            <label>PIN</label>
            <input value={form.pin} onChange={e => setForm({ ...form, pin: e.target.value })} />
            <label>Aadhar</label>
            <input value={form.aadharNumber} onChange={e => setForm({ ...form, aadharNumber: e.target.value })} />
            <label>DOB (YYYY-MM-DD)</label>
            <input value={form.dob} onChange={e => setForm({ ...form, dob: e.target.value })} />
            <button type="submit">Create</button>
            {error && <p className="error">{error}</p>}
          </form>
        </div>
        <div className="card">
          <h3>List</h3>
          <table className="table">
              <thead><tr><th>Id</th><th>Name</th><th>Phone</th><th>Email</th></tr></thead>
            <tbody>
                {list.map(c => (
                  <tr key={c.customerId}>
                    <td>{c.customerId}</td>
                    <td>{c.name}</td>
                    <td>{c.phoneNumber || c.phone}</td>
                    <td>{c.email}</td>
                  </tr>
                ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}
