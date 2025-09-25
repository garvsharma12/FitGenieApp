import { useEffect, useMemo, useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import { useAuth } from './context/AuthContext'
import { Link } from 'react-router-dom'
import { getUserIdFromToken, postActivity } from './api/client'

function App() {
  const { token, logout } = useAuth();
  const userId = useMemo(() => getUserIdFromToken(token), [token]);

  const [type, setType] = useState('HIIT');
  const [duration, setDuration] = useState(50);
  const [caloriesBurned, setCaloriesBurned] = useState(300);
  const [startTime, setStartTime] = useState(() => new Date().toISOString().slice(0,16)); // yyyy-MM-ddTHH:mm
  const [distance, setDistance] = useState(17);
  const [averageSpeed, setAverageSpeed] = useState(16);
  const [maxHeartRate, setMaxHeartRate] = useState(180);
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');

  async function onSubmit(e){
    e.preventDefault();
    setError(''); setResult(null);
    if (!token) { setError('Please log in first.'); return; }
    if (!userId) { setError('Could not determine user id from token.'); return; }
    const payload = {
      userId,
      type,
      duration: Number(duration),
      caloriesBurned: Number(caloriesBurned),
      startTime: new Date(startTime).toISOString().slice(0,19),
      additionalMetrices: {
        distance: Number(distance),
        averageSpeed: Number(averageSpeed),
        maxHeartRate: Number(maxHeartRate)
      }
    };
    try{
      const res = await postActivity(payload, token);
      setResult(res);
    }catch(err){
      const msg = err?.details?.message || err?.message || 'Failed to create activity';
      setError(msg);
    }
  }

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
            <p>You're logged in as {userId || 'unknown user'}.</p>
            <button onClick={logout}>Logout</button>
            <hr />
            <h3>Create Activity</h3>
            <form onSubmit={onSubmit} className="auth-form" style={{textAlign:'left', maxWidth: 480}}>
              <label>Type</label>
              <input value={type} onChange={e=>setType(e.target.value)} />
              <label>Duration (minutes)</label>
              <input type="number" value={duration} onChange={e=>setDuration(e.target.value)} />
              <label>Calories Burned</label>
              <input type="number" value={caloriesBurned} onChange={e=>setCaloriesBurned(e.target.value)} />
              <label>Start Time</label>
              <input type="datetime-local" value={startTime} onChange={e=>setStartTime(e.target.value)} />
              <fieldset>
                <legend>Additional Metrices</legend>
                <label>Distance</label>
                <input type="number" value={distance} onChange={e=>setDistance(e.target.value)} />
                <label>Average Speed</label>
                <input type="number" value={averageSpeed} onChange={e=>setAverageSpeed(e.target.value)} />
                <label>Max Heart Rate</label>
                <input type="number" value={maxHeartRate} onChange={e=>setMaxHeartRate(e.target.value)} />
              </fieldset>
              {error && <p className="error">{error}</p>}
              <button type="submit">Submit Activity</button>
            </form>
            {result && (
              <pre style={{textAlign:'left', background:'#111', padding:12, borderRadius:8, overflow:'auto'}}>
                {JSON.stringify(result, null, 2)}
              </pre>
            )}
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
