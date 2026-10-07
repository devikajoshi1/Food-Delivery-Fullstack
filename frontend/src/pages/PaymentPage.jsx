import { useState } from 'react'
import { useNavigate, useParams } from 'react-router'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { getOrder, payOrder } from '../api.js'
import { rupees } from '../format.js'
import { useAuth } from '../context/AuthContext.jsx'


export default function PaymentPage() {
    const {id} = useParams()
    const { token } = useAuth()
    const navigate = useNavigate()
    const queryClient = useQueryClient()

    const [method, setMethod] = useState('UPI');

    const order = useQuery({
        queryKey: ['order',id],
        queryFn: ()=> getOrder(id, token),
    })

    const paying = useMutation({
        mutationFn: ()=> payOrder(id, method, token),
        onSuccess: (paidOrder) => {
            queryClient.setQueryData(['order', id], paidOrder)
            navigate(`/orders/${id}`)
        },
    })

    function handleSubmit(event){
        event.preventDefault()
        paying.mutate()
    }

    if(order.isPending){
        return <p>Loading...</p>
    }
    if(order.isError){
        return <p className="error">{order.error.message}</p>
    }

  return (
    <div className="narrow">
      <h1>Pay {rupees.format(order.data.totalAmount)}</h1>
      <p className="muted">Order #{order.data.id} . Demo: no real money</p>

      <form action="" className="form" onSubmit={handleSubmit}>
        <label className="choice">
            <input
                type="radio"
                name="method"
                checked = {method === 'UPI'}
                onChange={()=> setMethod('UPI')}
            />
            UPI
        </label>

         <label className="choice">
            <input
                type="radio"
                name="method"
                checked = {method === 'CARD'}
                onChange={()=> setMethod('CARD')}
            />
            CARD
        </label>
    
        {paying.isError && <p className="error">{paying.error.message}</p>}
        <button className="button" disabled={paying.isPending}>{paying.isPending ? 'Paying...':'Pay Now'}</button>

      </form>

    </div>
  )
}
