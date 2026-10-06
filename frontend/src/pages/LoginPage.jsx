import { useState } from 'react'
import { useLocation, useNavigate } from 'react-router'
import { useMutation } from '@tanstack/react-query'
import { login, register } from '../api.js'
import { useAuth } from '../context/AuthContext.jsx'

export default function LoginPage() {
  const { saveToken } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()

  // One page, two modes: log in, or create an account
  const [isNew, setIsNew] = useState(false)
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')

  const sending = useMutation({
    mutationFn: () =>
      isNew ? register(name, email, password) : login(email, password),
    onSuccess: (answer) => {
      // The server answered { token }. Keep it, then go back
      saveToken(answer.token)
      navigate(location.state?.from ?? '/')
    },
  })

  function handleSubmit(event) {
    event.preventDefault()
    sending.mutate()
  }
  return (
    <div className="narrow">
      <h1>{isNew ? 'Create an account' : 'Log in'}</h1>

      <form className="form" onSubmit={handleSubmit}>
        {/* Only a new account needs a name */}
        {isNew && (
          <label>
            Name
            <input
              value={name}
              onChange={(event) => setName(event.target.value)}
              required
              maxLength={100}
            />
          </label>
        )}
        <label>
          Email
          <input
            type="email"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
            required
          />
        </label>
        <label>
          Password
          <input
            type="password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            required
            minLength={isNew ? 8 : undefined}
            maxLength={72}
          />
        </label>
        {sending.isError && <p className="error">{sending.error.message}</p>}
        <button className="button" disabled={sending.isPending}>
          {isNew ? 'Create account' : 'Log in'}
        </button>
      </form>

      {/* Switch mode; type="button" so it doesn't submit the form */}
      <button
        type="button"
        className="text-button"
        onClick={() => setIsNew(!isNew)}
      >
        {isNew ? 'I already have an account' : 'New here? Create an account'}
      </button>
    </div>
  )
}