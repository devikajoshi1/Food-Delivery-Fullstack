import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { useState } from 'react';
import { getRestaurants } from '../api.js';
import RestaurantCard from '../components/RestaurantCard';


export default function RestaurantsPage() {
    const[search, setSearch] = useState('');

    const {data, isPending, isError, error} = useQuery({
        queryKey:['restaurants', search],
        queryFn: ()=> getRestaurants(search),
        placeholderData: keepPreviousData,
    })

  return (
    <div>
      <h1>Restaurants</h1>

      <input
        value={search}
        onChange={(event)=> setSearch(event.target.value)}
        placeholder="Search by name"
        className="search"
      />

      {isPending && <p>Loading...</p>}
      {isError && <p className="error">{error.message}</p>}
      {data?.content.length === 0 &&(
        <p className="muted">No restaurant matches.</p>
      )}

      <ul className="card-grid">
        {data?.content.map((restaurant) =>(
            <li key={restaurant.id}>
                <RestaurantCard restaurant = {restaurant}/>
            </li>
        ))}
      </ul>
    </div>
  )
}
