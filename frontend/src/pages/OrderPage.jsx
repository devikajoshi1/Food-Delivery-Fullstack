import { useParams } from 'react-router'
import { useQuery } from '@tanstack/react-query'
import { getOrder } from '../api.js'
import { rupees } from '../format.js'
import { useAuth } from '../context/AuthContext.jsx'

export default function OrderPage() {
  const { id } = useParams()
  const { token } = useAuth()

  // Read the order back from the server: the database is the truth
  const order = useQuery({
    queryKey: ['order', id],
    queryFn: () => getOrder(id, token),
  })

  if (order.isPending) {
    return <p>Loading…</p>
  }
  if (order.isError) {
    return <p className="error">{order.error.message}</p>
  }

  return (
    <div>
      <h1>Order #{order.data.id}</h1>
      <p className="muted">
        {order.data.restaurantName} · {order.data.status}
      </p>

      <ul className="menu">
        {order.data.items.map((item) => (
          <li key={item.name}>
            <span>
              {item.quantity} × {item.name}
            </span>
            <strong>{rupees.format(item.unitPrice * item.quantity)}</strong>
          </li>
        ))}
      </ul>
      <p className="total">Total {rupees.format(order.data.totalAmount)}</p>
    </div>
  )
}