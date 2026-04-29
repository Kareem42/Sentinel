import React, { useState } from 'react';
import axios from "axios";
import toast from 'react-hot-toast';

interface Props {
    onServiceAdded: () => void;
}

export const ServiceForm  = ({onServiceAdded}: Props) => {
    const [formData, setFormData] = useState({name: '', url: ''});
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setIsSubmitting(true);

        const loadingToast = toast.loading('Registering service...');
        try{
            // This hits the Dockerized Spring Boot API
            await axios.post('http://localhost:8080/api/v1/service', formData);
            toast.success('Success! Service registered.', { id: loadingToast });
            setFormData({name: '', url: ''});
            onServiceAdded();
        } catch (error: any) {
            const apiError = error.response?.data;

            if (apiError?.errors) {
                Object.values(apiError.errors).forEach((error: any) => {
                    toast.error(error.message);
                })
            } else {
                toast.error("An unexpected error has occurred.");
            }
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <form onSubmit={handleSubmit}
              style={{display: 'flex', flexDirection: 'column', gap: '10px', maxWidth: '300px'}}>
            <input
                required
                placeholder="Service Name"
                value={formData.name}
                onChange={(e) => setFormData({...formData, name: e.target.value})}
            />
            <input
                required
                placeholder="Service Url"
                value={formData.url}
                onChange={(e) => setFormData({...formData, url: e.target.value})}
            />
            <button type="submit" disabled={isSubmitting}>Register Service</button>
            {isSubmitting ? 'Saving...' : 'Register Service'}
        </form>
    )
}