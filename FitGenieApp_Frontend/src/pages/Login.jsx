import { useState } from 'react';
import { login } from '../api/client';
import { useAuth } from '../context/AuthContext';
import { Link, useNavigate } from 'react-router-dom';

export default function Login(){
  const [identifier, setIdentifier] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { setToken } = useAuth();
  const navigate = useNavigate();

  async function onSubmit(e){
    e.preventDefault();
    setError('');
    try{
      const token = await login(identifier, password);
      setToken(token);
  navigate('/home');
    }catch(err){
      const msg = err?.details?.message || err?.message || 'Login failed';
      setError(msg);
    }
  }

  return (
    <div className="auth-container">
      <h2>FitGenie Login</h2>
      <form onSubmit={onSubmit} className="auth-form">
        <label>Email or Username</label>
        <input value={identifier} onChange={e=>setIdentifier(e.target.value)} required />
        <label>Password</label>
        <input value={password} onChange={e=>setPassword(e.target.value)} type="password" required />
        {error && <p className="error">{error}</p>}
        <button type="submit">Login</button>
      </form>
      <p>New here? <Link to="/signup">Create an account</Link></p>
    </div>
  );
}
