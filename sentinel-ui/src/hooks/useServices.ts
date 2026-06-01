import { useEffect, useState, useCallback } from "react";
import type { MonitoredServiceResponse, PageResponse } from "../types.ts";
import api from '../api/axiosConfig';

export const useServices = (page = 0, size = 20) => {
    const [services, setServices] = useState<MonitoredServiceResponse[]>([]);
    const [totalElements, setTotalElements] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const fetchServices = useCallback(async () => {
        setIsLoading(true);
        setError(null);
        try {
            const response = await api.get<PageResponse<MonitoredServiceResponse>>(
                `/service?page=${page}&size=${size}&sort=createdAt,desc`
            );
            setServices(response.data.content);
            setTotalElements(response.data.totalElements);
            setTotalPages(response.data.totalPages);
        } catch (err: any) {
            setError(err.message || "Failed to fetch services.");
        } finally {
            setIsLoading(false);
        }
    }, [page, size]);

    useEffect(() => {
        fetchServices(); // Initial fetch

        const interval = setInterval(() => {
            fetchServices();
        }, 30000); // Poll every 30 seconds

        return () => clearInterval(interval); // Cleanup on unmount
    }, [fetchServices]);

    const deleteService = useCallback(async (id: string) => {
        await api.delete(`/service/${id}`);
        // Optimistically remove from local state, then let the next poll sync totals
        setServices(prev => prev.filter(s => s.id !== id));
        setTotalElements(prev => prev - 1);
    }, []);

    return { services, totalElements, totalPages, isLoading, error, refresh: fetchServices, deleteService };
};
