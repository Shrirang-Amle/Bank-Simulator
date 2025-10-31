import React from 'react'
import { Link, useNavigate } from 'react-router-dom'

export default function NavBar() {
  const navigate = useNavigate()
  function logout() {
    localStorage.removeItem('bs_token')
    // notify app about logout so it can update state without reload
    window.dispatchEvent(new Event('bs_logout'))
    navigate('/login')
  }
  return (
    <nav className="navbar">
      <div className="brand">Bank Admin</div>
      <div className="links">
        <Link to="/admin/customers">Customers</Link>
        <Link to="/admin/accounts">Accounts</Link>
        <Link to="/admin/transactions">Transactions</Link>
        <button className="link-like" onClick={logout}>Logout</button>
      </div>
    </nav>
  )
}
