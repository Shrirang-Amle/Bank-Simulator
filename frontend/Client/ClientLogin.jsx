import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'

export default function ClientLogin() {
  const [phone, setPhone] = useState('')
  const [error, setError] = useState(null)
  const navigate = useNavigate()

  function onSubmit(e) {
    e.preventDefault()
    setError(null)
    if (!phone || phone.trim().length < 6) {
      setError('Enter a valid phone number')
      return
    }
    // store phone locally and go to client accounts
    localStorage.setItem('client_phone', phone.trim())
    navigate('/client/accounts')
  }

  return (
    <div className="centered">
      <div className="card" style={{maxWidth:420}}>
        <h2>Customer Login</h2>
        <form onSubmit={onSubmit}>
          <label>Phone Number</label>
          <input value={phone} onChange={e => setPhone(e.target.value)} autoComplete="tel" />
          <label>PIN (for transfers)</label>
          <input type="password" name="pin" autoComplete="one-time-code" placeholder="Enter your 4-digit PIN" />
          <button type="submit">Continue</button>
          {error && <p className="error">{error}</p>}
        </form>
        <p className="muted">Enter your registered phone number to view accounts and make transfers.</p>
      </div>
    </div>
  )
}
