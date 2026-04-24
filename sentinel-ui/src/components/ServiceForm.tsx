import React, { useState } from 'react';
import axios from "axios";

export const ServiceForm = () => {
    const [formData, setFormData] = useState({name: '', url: ''});
    const [status, setStatus] = useState('');

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try{
            // This hits the Dockerized Spring Boot API
            await axios.post('https://localhost:8080/api/v1/service', formData);
            setStatus('Success! Service registered.');
            setFormData({name: '', url: ''});
        } catch (error: any) {
            // This will catch the 400 error from the JUnit test
            const errorMsg = error.response?.data?.url || 'Error registering service';
            setStatus(`Failed: ${errorMsg}`);
        }
    };

    return (
        <form onSubmit={handleSubmit} style={{display: 'flex', flexDirection: 'column', gap: '10px', maxWidth: '300px'}}>
            <input
                placeholder="Service Name"
                value={formData.name}
                onChange={(e) => setFormData({...formData, name: e.target.value})}
                />
            <input
            placeholder="Service Url"
            value={formData.url}
            onChange={(e) => setFormData({...formData, url: e.target.value})}
            />
            <button type="submit">Register Service</button>
            {status && <p>{status}</p>}
        </form>
    )
}