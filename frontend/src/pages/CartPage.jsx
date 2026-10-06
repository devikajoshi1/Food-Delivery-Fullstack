import { useState } from 'react'
import { Link, useNavigate } from 'react-router'
import { useMutation } from '@tanstack/react-query'
import { placeOrder } from '../api.js'
import { rupees } from '../format.js'
import { useAuth } from '../context/AuthContext.jsx'
import { useCart } from '../context/CartContext.jsx'

export default function CartPage() {
  const { lines, dispatch } = useCart()
  const { token, logOut } = useAuth()
  const navigate = useNavigate()
  const [address, setAddress] = useState('')

  // What the server needs: ids and quantities. Never prices
  const order = {
    restaurantId: lines[0]?.restaurantId,
    deliveryAddress: address,
    items: lines.map((line) => ({
      menuItemId: line.id,
      quantity: line.quantity,
    })),
  }

  // A mutation is a request that changes data on the server
  const placing = useMutation({
    mutationFn: () => placeOrder(order, token),
    onSuccess: (saved) => {
      dispatch({ type: 'clear' })
      navigate(`/orders/${saved.id}`)
    },
    onError: (error) => {
      // The token expired (it lasts 1 hour): log in again, then come back
      if (error.status === 401) {
        logOut()
        navigate('/login', { state: { from: '/cart' } })
      }
    },
  })
if (lines.length === 0) {
    return (
      <div>
        <h1>Your cart</h1>
        <p className="muted">Your cart is empty.</p>
      </div>
    )
  }

  // For show only: the server works out the real total
  const total = lines.reduce(
    (sum, line) => sum + line.price * line.quantity,
    0,
  )

  function handleSubmit(event) {
    event.preventDefault()
    placing.mutate()
  }

  return (
    <div>
      <h1>Your cart</h1>
      <p className="muted">From {lines[0].restaurantName}</p>

      {/* One row per dish: − quantity + on the right */}
      <ul className="menu">
        {lines.map((line) => (
          <li key={line.id}>
            <strong>{line.name}</strong>
            <div className="row-end">
              <button
                className="qty"
                onClick={() => dispatch({ type: 'remove', id: line.id })}
              >
                −
              </button>
              <span>{line.quantity}</span>
              <button
                className="qty"
                onClick={() => dispatch({ type: 'add', item: line })}
              >
                +
              </button>
              <strong>{rupees.format(line.price * line.quantity)}</strong>
            </div>
          </li>
        ))}
      </ul>
      <p className="total">Total {rupees.format(total)}</p>
       {/* Logged in: the order form. Logged out: a link to log in */}
      {token ? (
        <form className="form" onSubmit={handleSubmit}>
          <label>
            Delivery address
            <input
              value={address}
              onChange={(event) => setAddress(event.target.value)}
              required
              maxLength={255}
            />
          </label>
          {placing.isError && (
            <p className="error">{placing.error.message}</p>
          )}
          <button className="button" disabled={placing.isPending}>
            {placing.isPending ? 'Placing…' : 'Place order'}
          </button>
        </form>
      ) : (
        <Link to="/login" state={{ from: '/cart' }} className="button">
          Log in to place your order
        </Link>
      )}
    </div>
  )
}