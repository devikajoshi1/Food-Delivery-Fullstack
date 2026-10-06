import { Link, useParams } from 'react-router'
import { useQuery } from '@tanstack/react-query'
import { getMenu, getRestaurant } from '../api.js'
import { rupees } from '../format.js'
import { useCart } from '../context/CartContext.jsx'

export default function MenuPage() {
  // The :id part of /restaurants/:id
  const { id } = useParams()
  const { lines, dispatch } = useCart()

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

  function addToCart(item) {
    // A dish from another restaurant would empty the cart: ask first
    const otherRestaurant =
      lines.length > 0 && lines[0].restaurantId !== restaurant.data.id
    if (otherRestaurant && !window.confirm('Start a new cart here?')) {
      return
    }

    // Keep only what the cart page needs to show
    dispatch({
      type: 'add',
      item: {
        id: item.id,
        name: item.name,
        price: item.price,
        restaurantId: restaurant.data.id,
        restaurantName: restaurant.data.name,
      },
    })
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
            <div className="row-end">
              <strong>{rupees.format(item.price)}</strong>
              <button className="button" onClick={() => addToCart(item)}>
                Add
              </button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  )
}
