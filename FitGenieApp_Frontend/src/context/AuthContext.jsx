import { createContext, useContext, useEffect, useState } from 'react';
import { getToken, logout } from '../api/client';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(getToken());

  useEffect(() => {
    setToken(getToken());
  }, []);

  const value = {
    token,
    setToken,
    logout: () => {
      logout();
      setToken(null);
    }
  };
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth(){
  return useContext(AuthContext);
}
