import { useEffect, useState, useCallback } from "react";
import axios from "axios";
import type {MonitoredService} from "../types.ts";

export const useServices = () => {
    const [services, setServices] = useState<MonitoredService[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const fetchServices = useCallback(async () => {
        setIsLoading(true);
        setError(null);
        try{
            const response = await axios('http://localhost:8000/api/v1/services');
            setServices(response.data);
        } catch(err: any){
            setError(err.message || "Failed to fetch services.");
        } finally {
            setIsLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchServices();
    }, [fetchServices]);

    return {services, isLoading, error, refresh: fetchServices};
}
