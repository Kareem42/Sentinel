export type MonitoredService = {
    id: number;
    name: string;
    url: string;
}

export interface MonitoredServiceResponse {
    id: number;
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