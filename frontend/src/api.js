// Where the API lives. Vercel will set VITE_API_URL; your laptop uses 8080
const API_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'

// Every request goes through here. It adds the token if there is one,
// and turns any answer that isn't 2xx into an error with a status
async function request(path, { method = 'GET', body, token } = {}) {
  const headers = { 'Content-Type': 'application/json' }
  if (token) {
    headers.Authorization = `Bearer ${token}`
  }

  const response = await fetch(API_URL + path, {
    method,
    headers,
    body: JSON.stringify(body),
  })

  if (!response.ok) {
    // Spring's error body has a "detail"; a 401 from the filter has none
    const problem = await response.json().catch(() => ({}))
    const error = new Error(
      problem.detail ?? `The server answered ${response.status}`,
    )
    error.status = response.status
    throw error
  }
  return response.json()
}

// GET /api/restaurants?search=pi&size=20
export function getRestaurants(search) {
  const params = new URLSearchParams({ search, size: 20 })
  return request(`/api/restaurants?${params}`)
}

export function getRestaurant(id) {
  return request(`/api/restaurants/${id}`)
}

export function getMenu(id) {
  return request(`/api/restaurants/${id}/menu-items`)
}

// POST /api/auth/login answers { token: "eyJ..." }
export function login(email, password) {
  return request('/api/auth/login', {
    method: 'POST',
    body: { email, password },
  })
}

export function register(name, email, password) {
  return request('/api/auth/register', {
    method: 'POST',
    body: { name, email, password },
  })
}

// These two need the token: the server reads the user from it
export function placeOrder(order, token) {
  return request('/api/orders', { method: 'POST', body: order, token })
}

export function getOrder(id, token) {
  return request(`/api/orders/${id}`, { token })
}

export function payOrder(orderId, method, token){
  return request(`/api/orders/${orderId}/pay`,{
    method: 'POST',
    body:{method},
    token,
  })
}

export function getMyOrders(token){
  return request('/api/orders',{token})
}

//admin
export function getAllOrders(token){
  return request('/api/admin/orders',{ token })
}

export function updateOrderStatus(orderId, status, token){
  return request(`/api/admin/orders/${orderId}/status`,{
    method: 'PATCH',
    body: { status },
    token,
  })
}