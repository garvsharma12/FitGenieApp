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
    let msg = `Request failed: ${res.status}`;
    try {
      const data = await res.json();
      msg = data.message || data.error || msg;
      throw Object.assign(new Error(msg), { details: data, status: res.status });
    } catch (_) {
      const text = await res.text();
      if (text) msg = text;
      throw new Error(msg);
    }
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

// Decode a JWT without verifying signature (client-side use only)
function base64UrlDecode(str) {
  try {
    // Replace URL-safe chars and pad
    const base64 = str.replace(/-/g, '+').replace(/_/g, '/').padEnd(Math.ceil(str.length / 4) * 4, '=');
    const decoded = atob(base64);
    // Decode UTF-8
    const bytes = Uint8Array.from(decoded, c => c.charCodeAt(0));
    const decoder = new TextDecoder('utf-8');
    return decoder.decode(bytes);
  } catch (_) {
    return null;
  }
}

export function decodeJwt(token) {
  if (!token) return null;
  const parts = token.split('.');
  if (parts.length !== 3) return null;
  const payloadJson = base64UrlDecode(parts[1]);
  if (!payloadJson) return null;
  try {
    return JSON.parse(payloadJson);
  } catch (_) {
    return null;
  }
}

export function getUserIdFromToken(token) {
  const payload = decodeJwt(token);
  if (!payload) return null;
  // Our backend sets subject (sub) to userId
  return payload.sub || payload.userId || null;
}

export async function postActivity(activity, token) {
  return apiPost('/api/activities', activity, token);
}
