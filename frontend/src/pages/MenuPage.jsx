import { useQuery } from '@tanstack/react-query'
import { Link, useParams } from 'react-router'
import { getMenu, getRestaurant } from '../api.js'


const rupees = new Intl.NumberFormat('en-IN', {
  style: 'currency',
  currency: 'INR',
})

export default function MenuPage() {
  // The :id part of /restaurants/:id
  const { id } = useParams()

  // Two requests, each remembered under its own key
  const restaurant = useQuery({
    queryKey: ['restaurant', id],
    queryFn: () => getRestaurant(id),
  })
  const menu = useQuery({
    queryKey: ['menu', id],
    queryFn: () => getMenu(id),
  })

  // Wait for both; show one message if either fails
  if (restaurant.isPending || menu.isPending) {
    return <p>Loading…</p>
  }
  if (restaurant.isError || menu.isError) {
    return <p className="error">Couldn't load this restaurant.</p>
  }

  return (
    <div>
      <Link to="/" className="back-link">
        ← All restaurants
      </Link>
      <h1>{restaurant.data.name}</h1>
      <p className="muted">{restaurant.data.cuisine}</p>

      {/* One row per dish: name and description left, price right */}
      <ul className="menu">
        {menu.data.map((item) => (
          <li key={item.id}>
            <div>
              <strong>{item.name}</strong>
              <p className="muted">{item.description}</p>
            </div>
            <strong>{rupees.format(item.price)}</strong>
          </li>
        ))}
      </ul>
    </div>
  )
}