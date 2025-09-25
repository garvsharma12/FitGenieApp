import { useState } from 'react';
import { register } from '../api/client';
import { Link, useNavigate } from 'react-router-dom';

export default function Signup(){
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [fullName, setFullName] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const navigate = useNavigate();

  async function onSubmit(e){
    e.preventDefault();
    setError(''); setSuccess('');
    try{
  await register({ email, username, password, fullName });
      setSuccess('Account created! You can now log in.');
      setTimeout(()=> navigate('/login'), 800);
    }catch(err){
      try{
        const parsed = JSON.parse(err.message);
        setError(parsed.error || 'Signup failed');
      }catch(_){
        setError(err.message || 'Signup failed');
      }
    }
  }

  return (
    <div className="auth-container">
      <h2>FitGenie Sign up</h2>
      <form onSubmit={onSubmit} className="auth-form">
  <label>Full name</label>
  <input value={fullName} onChange={e=>setFullName(e.target.value)} required />
  <label>Email</label>
        <input value={email} onChange={e=>setEmail(e.target.value)} type="email" required />
  <label>Username</label>
  <input value={username} onChange={e=>setUsername(e.target.value)} required />
        <label>Password</label>
        <input value={password} onChange={e=>setPassword(e.target.value)} type="password" required />
        {error && <p className="error">{error}</p>}
        {success && <p className="success">{success}</p>}
        <button type="submit">Create account</button>
      </form>
      <p>Already have an account? <Link to="/login">Login</Link></p>
    </div>
  );
}
