import React, { useEffect, useState } from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import Login from './pages/Login'
import AdminDashboard from './pages/AdminDashboard'
import ClientLogin from './pages/client/ClientLogin'
import ClientAccounts from './pages/client/ClientAccounts'
import ClientTransactions from './pages/client/ClientTransactions'

function App() {
  const [token, setToken] = useState(() => localStorage.getItem('bs_token'))

  useEffect(() => {
    function onLogin() {
      setToken(localStorage.getItem('bs_token'))
    }
    function onLogout() {
      setToken(null)
    }
    window.addEventListener('bs_login', onLogin)
    window.addEventListener('bs_logout', onLogout)
    return () => {
      window.removeEventListener('bs_login', onLogin)
      window.removeEventListener('bs_logout', onLogout)
    }
  }, [])

  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/client" element={<ClientLogin />} />
      <Route path="/client/accounts" element={<ClientAccounts />} />
      <Route path="/client/transactions" element={<ClientTransactions />} />
      <Route
        path="/admin/*"
        element={token ? <AdminDashboard /> : <Navigate to="/login" replace />}
      />
      <Route path="/" element={<Navigate to={token ? '/admin' : '/login'} replace />} />
    </Routes>
  )
}

export default App
