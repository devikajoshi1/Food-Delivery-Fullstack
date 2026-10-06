import { createContext, useContext, useEffect, useReducer } from 'react'
const CartContext = createContext(null)

// Every change to the cart goes through here: old lines + action = new lines
function cartReducer(lines, action) {
  switch (action.type) {
    case 'add': {
      const item = action.item

      // One restaurant per order: a dish from another one starts a new cart
      if (lines.length > 0 && lines[0].restaurantId !== item.restaurantId) {
        return [{ ...item, quantity: 1 }]
      }

      // Already in the cart? One more, up to the server's limit of 20
      if (lines.some((line) => line.id === item.id)) {
        return lines.map((line) =>
          line.id === item.id
            ? { ...line, quantity: Math.min(line.quantity + 1, 20) }
            : line,
        )
      }
      return [...lines, { ...item, quantity: 1 }]
    }

    // One fewer; a line that reaches 0 leaves the cart
    case 'remove':
      return lines
        .map((line) =>
          line.id === action.id
            ? { ...line, quantity: line.quantity - 1 }
            : line,
        )
        .filter((line) => line.quantity > 0)

    case 'clear':
      return []

    default:
      throw new Error(`Unknown cart action: ${action.type}`)
  }
}

export function CartProvider({children}){
 // Start from the cart saved in the browser, or an empty one
  const [lines, dispatch] = useReducer(cartReducer, [], () =>
    JSON.parse(localStorage.getItem('cart') ?? '[]'),
  )

  // Save after every change, so a reload keeps the cart
  useEffect(() => {
    localStorage.setItem('cart', JSON.stringify(lines))
  }, [lines])

  return (
    <CartContext value={{ lines, dispatch }}>
      {children}
    </CartContext>
  )
}

// A hook next to a component is fine; tell ESLint so
// eslint-disable-next-line react-refresh/only-export-components
export function useCart() {
  return useContext(CartContext)
}