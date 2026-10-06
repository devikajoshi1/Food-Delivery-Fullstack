import { Link, Outlet } from 'react-router'
import { useAuth } from '../context/AuthContext.jsx'
import { useCart } from '../context/CartContext.jsx'

export default function Layout() {
  const { token, logOut } = useAuth()
  const { lines } = useCart()

  // 2 naan + 1 dal = 3 items
  const count = lines.reduce((sum, line) => sum + line.quantity, 0)

  return (
    <>
      <header className="navbar">
        <nav className="container nav">
          <Link to="/" className="logo">
            Food Delivery
          </Link>

          {/* Right side: the cart, then log in or log out */}
          <div className="nav-links">
            <Link to="/cart">Cart ({count})</Link>
            {token ? (
              <button className="text-button" onClick={logOut}>
                Log out
              </button>
            ) : (
              <Link to="/login">Log in</Link>
            )}
          </div>
        </nav>
      </header>

      {/* The current page is drawn here */}
      <main className="container">
        <Outlet />
      </main>
    </>
  )
}