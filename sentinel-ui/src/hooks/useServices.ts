import { useEffect, useState, useCallback } from "react";
import type { MonitoredServiceResponse } from "../types.ts";
import api from '../api/axiosConfig'

export const useServices = () => {
    const [services, setServices] = useState<MonitoredServiceResponse[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const fetchServices = useCallback(async () => {
        setIsLoading(true);
        setError(null);
        try{
            const response = await api.get('/service');
            setServices(response.data);
        } catch(err: any){
            setError(err.message || "Failed to fetch services.");
        } finally {
            setIsLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchServices(); // Initial fetch

        const interval = setInterval(() => {
            fetchServices();
        }, 30000); // Poll every 30 seconds

        return () => clearInterval(interval); // Cleanup on unmount
    }, [fetchServices]);

    return {services, isLoading, error, refresh: fetchServices};
}