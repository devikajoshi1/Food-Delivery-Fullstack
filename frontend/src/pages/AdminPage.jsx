import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { getAllOrders, updateOrderStatus } from '../api'
import { useAuth } from '../context/AuthContext'
import { rupees, statusText } from '../format'



const STATUSES = ['PREPARING', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELED']


export default function AdminPage() {
    const {token , role} = useAuth()
    const queryClient = useQueryClient()

    const orders = useQuery({
        queryKey: ['admin-orders'],
        queryFn: ()=> getAllOrders(token),
        enabled : role === 'ADMIN',
    })

    const changing = useMutation({
        mutationFn: ({id, status}) => updateOrderStatus(id, status, token),
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ['admin-orders']})
        },
    })

    if(role !== 'ADMIN'){
        return <p className="error">Only admins can see this page.</p>
    }

    if(orders.isPending){
        return <p>Loading...</p>
    }
    if(orders.isError){
        return <p className="error">{orders.error.message}</p>
    }

  return (
    <div>
        <h1>All orders</h1>
        {changing.isError && <p className="error">{changing.error.message}</p>}

        <ul className="menu">
            {orders.data.map((order)=>(
                <li key={order.id}>
                    <div>
                        <strong>Order #{order.id}</strong>
                        <p className="muted">
                            {order.restaurantName} . {rupees.format(order.totalAmount)}
                            {' . '}
                            {statusText(order.status)}
                        </p>
                    </div>

                    <select
                     className="status"
                     value= ""
                     onChange={(event)=> changing.mutate({id: order.id, status: event.target.value})
                    }
                    >
                        <option value="" disabled >
                            Change status...
                        </option>

                        {STATUSES.map((status)=>(
                            <option key = {status} value={status}>
                                {statusText(status)}
                            </option>
                        ))}
                     </select>
                </li>
            ))}
        </ul>
    </div>
  )
}
