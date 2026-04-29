export interface MonitoredService {
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
