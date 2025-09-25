const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:9090';

export async function apiPost(path, body, token) {
  const res = await fetch(`${API_BASE}${path}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
    body: JSON.stringify(body),
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text || `Request failed: ${res.status}`);
  }
  return res.json();
}

export async function login(identifier, password) {
  const data = await apiPost('/api/auth/login', { identifier, password });
  const token = data.token;
  if (token) localStorage.setItem('token', token);
  return token;
}

export async function register(payload) {
  return apiPost('/api/auth/register', payload);
}

export function getToken() {
  return localStorage.getItem('token');
}

export function logout() {
  localStorage.removeItem('token');
}
