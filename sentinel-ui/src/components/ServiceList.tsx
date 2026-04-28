import { useEffect, useState } from "react";
import axios from "axios";
import type {MonitoredService} from "../types.ts";

export const ServiceList = () => {
    const [services, setServices] = useState<MonitoredService[]>([]);

    const fetchServices = async () => {
        try{
            const response = await axios.get('http://localhost:8000/api/services');
            setServices(response.data);
        } catch (error) {
            console.log("Failed to fetch services: ", error);
        }
    };

    useEffect(() => {
        fetchServices();
    }, []);

    return (
        <div style={{ marginTop: "10px" }}>
            <h2>Monitored Services</h2>
            <table border={1} style={{ width: "100%", textAlign: 'left' }} >
                <thead>
                <tr>
                    <th>Name</th>
                    <th>URL</th>
                    <th>Status</th>
                </tr>
                </thead>
                <tbody>
                {services.map((service, index) => (
                    <tr key={index}>
                        <td>{service.name}</td>
                        <td>{service.url}</td>
                        <td>Online</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    )
}