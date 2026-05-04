import axios from 'axios';
import type {LoginRequest, AuthResponse} from '../types';

const API_URL = 'http://localhost:8080/api/v1/auth';

export const login = async (credentials: LoginRequest): Promise<string> => {
    const repsonse = await axios.post<AuthResponse>(`${API_URL}/login`, credentials);
    const { token } = repsonse.data;

    // Storing the passport in the browser's localStorage
    localStorage.setItem('sentinel_token', token);

    return token;
};

export const logout = () => {
    // Once the user log out, the token will be removed from the local storage and they will be redirected back to the login page
    localStorage.removeItem('sentinel_token');
    window.location.href = '/login';
}

export const getToken = () => localStorage.getItem('sentinel_token');