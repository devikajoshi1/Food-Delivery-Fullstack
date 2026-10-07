import {rupees, statusText} from "../format"
import { useAuth } from '../context/AuthContext'
import { useQuery } from '@tanstack/react-query';
import { getMyOrders } from '../api';
import { Link } from 'react-router';

export default function OrdersPage() {
    const { token } = useAuth();
    const orders = useQuery({
        queryKey: ['orders'],
        queryFn:() => getMyOrders(token),
    })
    if(orders.isPending){
        return <p>Loading</p>
    }
    if(orders.isError){
        return <p className="error">{orders.error.message}</p>
    }
    if(orders.data.length === 0){
        return <p className="muted">No orders yet.</p>
    }
  return (
    <div>
        <h1>My orders</h1>
        <ul className="menu">
            {orders.data.map((order)=>(
                <li key={order.id}>
                    <Link to ={`/orders/${order.id}`} className="row-link">
                        <strong>Order #{order.id}</strong>
                        <p className="muted">
                            {order.restaurantName} . {statusText(order.status)}
                        </p>
                    </Link>
                    <strong>{rupees.format(order.totalAmount)}</strong>
                </li>
            ))}
        </ul>
    </div>
  )
}
