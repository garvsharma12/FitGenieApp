import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import { useAuth } from './context/AuthContext'
import { Link } from 'react-router-dom'

function App() {
  const [count, setCount] = useState(0)
  const { token, logout } = useAuth();

  return (
    <>
      <div>
        <a href="https://vite.dev" target="_blank">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
      </div>
      <h1>FitGenie Home</h1>
      <div className="card">
        {token ? (
          <>
            <p>You're logged in.</p>
            <button onClick={logout}>Logout</button>
          </>
        ) : (
          <>
            <p>You are not logged in.</p>
            <Link to="/login">Go to Login</Link>
          </>
        )}
      </div>
      <p className="read-the-docs">Welcome to FitGenieApp</p>
    </>
  )
}

export default App
