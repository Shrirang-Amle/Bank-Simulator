const BASE = (import.meta.env.VITE_API_BASE) ? import.meta.env.VITE_API_BASE : '/api'

async function handleResponse(res) {
  // Read the response as text first so we can safely handle empty bodies or non-JSON responses
  const text = await res.text()
  // When response is not ok, try to extract a useful message from JSON body or plain text
  if (!res.ok) {
    let data = null
    try {
      data = text ? JSON.parse(text) : null
    } catch (e) {
      data = text || null
    }
    // If the server returned HTML (e.g. the Vite index.html), it's a sign the request
    // routed to the frontend dev server or the backend returned an HTML error page.
    if (typeof data === 'string' && data.trim().toLowerCase().startsWith('<!doctype html')) {
      throw new Error(`Server returned HTML instead of JSON. This usually means the API URL is incorrect or the backend is down. Check VITE_API_BASE and backend status. (HTTP ${res.status})`)
    }
    const msg = (data && data.message) || data || res.statusText || `HTTP ${res.status}`
    throw new Error(msg)
  }

  if (!text) return null

  try {
    // If the response is HTML (index.html returned by dev server), surface a clearer error
    if (text.trim().toLowerCase().startsWith('<!doctype html')) {
      throw new Error('Unexpected HTML response from server (likely wrong API base or backend not running).')
    }
    return JSON.parse(text)
  } catch (e) {
    // Not JSON (e.g., HTML error page or plain text); return the raw text so callers can decide
    // If it's HTML, convert to a helpful error instead of returning raw HTML
    if (typeof text === 'string' && text.trim().toLowerCase().startsWith('<!doctype html')) {
      throw new Error('Received HTML page when expecting JSON from the API. Verify the API base URL and backend status.')
    }
    return text
  }
}

export function setToken(token) {
  localStorage.setItem('bs_token', token)
}
export function getToken() {
  return localStorage.getItem('bs_token')
}

// NOTE: backend currently has no auth endpoints. We mock login locally.
export async function login(username, password) {
  // call backend auth endpoint
  const res = await call('/auth/login', { method: 'POST', body: JSON.stringify({ username, password }) })
  // server returns { token: '...' }
  if (res && res.token) {
    setToken(res.token)
    return res
  }
  throw new Error('Invalid credentials')
}

async function call(path, options = {}) {
  const url = BASE + path
  const headers = options.headers || {}
  headers['Content-Type'] = 'application/json'
  const token = getToken()
  if (token) headers['Authorization'] = `Bearer ${token}`
  const opts = { ...options, headers }
  const res = await fetch(url, opts)
  if (res.status === 204) return null
  return handleResponse(res)
}

// Customers
export function fetchCustomers() {
  // backend: GET /customers
  return call('/customers', { method: 'GET' })
}
export function createCustomer(customer) {
  // backend: POST /customers
  return call('/customers', { method: 'POST', body: JSON.stringify(customer) })
}

// Accounts
export function fetchAccounts() {
  return call('/accounts/getAll', { method: 'GET' })
}
export function createAccount(account) {
  return call('/accounts/create', { method: 'POST', body: JSON.stringify(account) })
}

// Transactions
export function transfer(payload) {
  return call('/transactions/transfer', { method: 'POST', body: JSON.stringify(payload) })
}
export function fetchTransactions(accountNumber) {
  return call(`/transactions/get/${encodeURIComponent(accountNumber)}`, { method: 'GET' })
}
