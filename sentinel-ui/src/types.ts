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
    lastChecked: string | null;
    lastResponseTimeMs: number | null;
    checkIntervalSeconds: number;
}

export interface IntegrationResponse {
    id: string;
    name: string;
    purposeDescription: string;
    appUrl: string;
    apiDescription: string;
    apiWebsiteUrl: string;
    createdAt: string;
}

export interface PageResponse<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    number: number;
    size: number;
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