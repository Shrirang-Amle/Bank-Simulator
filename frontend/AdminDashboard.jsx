import React from 'react'
import { Routes, Route, Link, Navigate } from 'react-router-dom'
import Customers from './Customers'
import Accounts from './Accounts'
import Transactions from './Transactions'
import NavBar from '../components/NavBar'

export default function AdminDashboard() {
  return (
    <div>
      <NavBar />
      <div className="container">
        <Routes>
          <Route path="/" element={<Navigate to="customers" replace />} />
          <Route path="customers" element={<Customers />} />
          <Route path="accounts" element={<Accounts />} />
          <Route path="transactions" element={<Transactions />} />
        </Routes>
      </div>
    </div>
  )
}
