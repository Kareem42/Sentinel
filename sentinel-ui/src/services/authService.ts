// Auth helpers — use AuthContext (context/AuthContext.tsx) for login/logout state.
// Use axiosConfig (api/axiosConfig.ts) for authenticated API calls.

export const getToken = () => localStorage.getItem('sentinel_token');