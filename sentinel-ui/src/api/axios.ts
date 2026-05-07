import axios from "axios";

const api = axios.create({
    baseURL: 'https://api.sentinel.com/api/',
});

export default api;