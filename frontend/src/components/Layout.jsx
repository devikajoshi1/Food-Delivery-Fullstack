import { Link, Outlet } from 'react-router'
const Layout = () => {
  return (

    <>
     <header className='navbar'>
        <nav className="container">
            <Link to="/" className='logo'>
                Food delivery
            </Link>
        </nav>
    </header> 

    <main className="container">
        <Outlet/>
    </main>

    </>
  )
}

export default Layout
