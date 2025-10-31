import React from 'react'
import { Link, useNavigate } from 'react-router-dom'

export default function ClientNav() {
  const navigate = useNavigate()
  function logout() {
    localStorage.removeItem('client_phone')
    navigate('/client')
  }
  return (
    <nav className="navbar">
      <div className="brand">Bank Client</div>
      <div className="links">
        <Link to="/client/accounts">Accounts</Link>
        <Link to="/client/transactions">Transactions</Link>
        <button className="link-like" onClick={logout}>Logout</button>
      </div>
    </nav>
  )
}
