import { createContext, useContext, useState } from 'react'
import { useQueryClient } from '@tanstack/react-query'


const AuthContext = createContext(null)


export function AuthProvider({ children }) {
  const queryClient = useQueryClient()

  // Start with the token saved last time, so a reload keeps you logged in
  const [token, setToken] = useState(() => localStorage.getItem('token'))

  function saveToken(newToken) {
    localStorage.setItem('token', newToken)
    setToken(newToken)
  }

  // Forget the token and every answer fetched with it
  function logOut() {
    localStorage.removeItem('token')
    setToken(null)
    queryClient.clear()
  }

  return (
    <AuthContext value={{ token, saveToken, logOut }}>
      {children}
    </AuthContext>
  )
}

// A hook next to a component is fine; tell ESLint so
// eslint-disable-next-line react-refresh/only-export-components
export function useAuth() {
  return useContext(AuthContext)
}
