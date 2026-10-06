import { useAuth } from '../context/AuthContext'
import { Navigate, Outlet, useLocation } from 'react-router';

export default function RequireLogin() {
    const {token} = useAuth();
    const location = useLocation();

    if(!token){
        return <Navigate to="/login" state={{from: location.pathname}}/>
    }

  return (
    <Outlet/>
  )
}
