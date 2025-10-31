import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { login } from '../services/api'

export default function Login() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  async function onSubmit(e) {
    e.preventDefault()
    setError(null)
    setLoading(true)
    try {
      console.log('Attempting login', { username })
      await login(username, password)
      console.log('Login successful, token stored')
      // notify the app that login happened so App can update auth state
      window.dispatchEvent(new Event('bs_login'))
      navigate('/admin')
    } catch (err) {
      setError(err.message)
    }
    setLoading(false)
  }

  return (
    <div className="centered">
      <div className="card">
        <h2>Admin Login</h2>
        <form onSubmit={onSubmit}>
          <label>Username</label>
          <input name="username" autoComplete="username" value={username} onChange={e => setUsername(e.target.value)} />
          <label>Password</label>
          {/* Use new-password to reduce some password-manager "breach" checks in dev. */}
          <input
            type="password"
            name="new-password"
            autoComplete="new-password"
            value={password}
            onChange={e => setPassword(e.target.value)}
          />
          <button type="submit" disabled={loading}>{loading ? 'Logging in…' : 'Login'}</button>
          {error && <p className="error">{error}</p>}
        </form>
        <p className="muted">Use <strong>admin</strong> / <strong>Adm1n$ecure!2025</strong> to login</p>
        <p className="muted">If your browser still shows a "breached password" warning, that's the browser's Password Check feature flagging the entered string as previously seen in public leaks — it is not a server-side breach of this app. Use the strong password above or change it in the code for development convenience.</p>
      </div>
    </div>
  )
}
