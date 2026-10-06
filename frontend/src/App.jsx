import { Routes, Route } from 'react-router'
import Layout from './components/Layout.jsx'
import RequireLogin from './components/RequireLogin.jsx'
import RestaurantsPage from './pages/RestaurantsPage.jsx'
import MenuPage from './pages/MenuPage.jsx'
import CartPage from './pages/CartPage.jsx'
import LoginPage from './pages/LoginPage.jsx'
import OrderPage from './pages/OrderPage.jsx'

export default function App() {
  return (
    <Routes>
      {/* Every page sits inside the Layout: navbar on top */}
      <Route element={<Layout />}>
        <Route path="/" element={<RestaurantsPage />} />
        <Route path="/restaurants/:id" element={<MenuPage />} />
        <Route path="/cart" element={<CartPage />} />
        <Route path="/login" element={<LoginPage />} />

        {/* Pages inside here need a logged-in user */}
        <Route element={<RequireLogin />}>
          <Route path="/orders/:id" element={<OrderPage />} />
        </Route>
      </Route>
    </Routes>
  )
}