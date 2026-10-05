//where api lives 
const API_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'

//every get goes through here

async function get(path){
    const response = await fetch(API_URL + path)
    
    if(!response.ok){
        throw new Error(`The server answered ${response.status}`)
    }
    return response.json()
}

//get api/restaurant?search=pi&size=20
export function getRestaurants(search){
    const params = new URLSearchParams({search, size: 20})
    return get(`/api/restaurants?${params}`)
}

export function getRestaurant(id){
    return get(`/api/restaurants/${id}`)
}

export function getMenu(id){
    return get(`/api/restaurants/${id}/menu-items`)
}