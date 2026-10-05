import { Link } from 'react-router'

export default function RestaurantCard({restaurant}) {
  return (
    <Link to={`/restaurants/${restaurant.id}`} className="card">
      <h2>{restaurant.name}</h2>
      <p className="cuisine">{restaurant.cuisine}</p>
      <p className="muted">{restaurant.address}</p>
    </Link>
  )
}
