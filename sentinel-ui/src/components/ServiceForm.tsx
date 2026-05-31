import React, { useState } from 'react';
import api from '../api/axiosConfig';
import toast from 'react-hot-toast';
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

interface Props {
    onServiceAdded: () => void;
}

export const ServiceForm = ({ onServiceAdded }: Props) => {
    const [formData, setFormData] = useState({ name: '', url: '' });
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setIsSubmitting(true);

        const loadingToast = toast.loading('Registering service…');
        try {
            await api.post('/service', formData);
            toast.success('Service registered!', { id: loadingToast });
            setFormData({ name: '', url: '' });
            onServiceAdded();
        } catch (error: any) {
            const apiError = error.response?.data;
            toast.dismiss(loadingToast);
            if (apiError?.errors) {
                Object.values(apiError.errors).forEach((e: any) => toast.error(String(e)));
            } else {
                toast.error("An unexpected error occurred.");
            }
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <Card>
            <CardHeader>
                <CardTitle>Register a Service</CardTitle>
            </CardHeader>
            <CardContent>
                <form onSubmit={handleSubmit} className="flex flex-col sm:flex-row gap-4 items-end">
                    <div className="space-y-2 flex-1">
                        <Label htmlFor="service-name">Service Name</Label>
                        <Input
                            id="service-name"
                            placeholder="e.g. Payment API"
                            required
                            value={formData.name}
                            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                        />
                    </div>
                    <div className="space-y-2 flex-1">
                        <Label htmlFor="service-url">Service URL</Label>
                        <Input
                            id="service-url"
                            placeholder="https://api.example.com/health"
                            required
                            value={formData.url}
                            onChange={(e) => setFormData({ ...formData, url: e.target.value })}
                        />
                    </div>
                    <Button type="submit" disabled={isSubmitting} className="shrink-0">
                        {isSubmitting ? 'Registering…' : 'Register Service'}
                    </Button>
                </form>
            </CardContent>
        </Card>
    );
};
