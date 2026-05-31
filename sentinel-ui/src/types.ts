export type MonitoredService = {
    id: string;
    name: string;
    url: string;
}

export interface MonitoredServiceResponse {
    id: string;
    name: string;
    url: string;
    status: string;
}

export interface LoginRequest {
    username: string;
    password: string;
}

export interface AuthResponse {
    token: string;
}

export interface User {
    username: string;
    password: string;
}