import axios from 'axios';

const api = axios.create({
    baseURL: "http://localhost:8080/api/v1"
});

api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('sentinel_token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401 || error.response?.status === 403) {
            localStorage.removeItem('sentinel_token');
            window.location.href = '/';

        }
        return Promise.reject(error);
    }
);

export default api;