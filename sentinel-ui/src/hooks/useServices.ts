import { useEffect, useState, useCallback } from "react";
import axios from "axios";
import type { MonitoredServiceResponse } from "../types.ts";

export const useServices = () => {
    const [services, setServices] = useState<MonitoredServiceResponse[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const fetchServices = useCallback(async () => {
        setIsLoading(true);
        setError(null);
        try{
            const response = await axios('http://localhost:8080/api/v1/service');
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
