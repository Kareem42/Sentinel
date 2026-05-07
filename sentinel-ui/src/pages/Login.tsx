import React, { useState } from "react";
import { useNavigate } from "react-router";
import api from '../api/axiosConfig.ts';

export const Login: React.FC = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError("");

        try {
            const response = await api.post("/auth/login", { username, password });

            localStorage.setItem("sentinel_token", response.data.token);
            navigate("/dashboard");
        } catch (err: any) {
            setError(err.response?.data?.message || 'Invalid username or password');
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="flex min-h-screen items-center justify-center bg-gray-900 p-4">
            <div className="w-full max-w-md rounded-lg bg-gray-800 p-8 shadow-xl">
                <h2 className="mb-6 text-center text-3xl font-bold text-white">Welcome to the Sentinel</h2>
                <p>Grab your APIs and study them all in one spot!!</p>
                {error && (
                    <div className="mb-4 rounded bg-red-500/10 p-3 text-sm text-red-500 border border-red-500/50">
                        {error}
                    </div>
                )}

                <form onSubmit={handleSubmit} className="space-y-6">
                    <div>
                        <label className="block text-sm font-medium text-gray-400">Username:</label>
                        <input
                            type="text"
                            required
                            className="mt-2 w-full h-11 rounded border border-gray-700 bg-gray-900 px-3 py-2 text-white text-base focus:border-blue-500 focus:outline-none"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-400">Password:</label>
                        <input
                            type="password"
                            required
                            className="mt-2 w-full h-11 rounded border border-gray-700 bg-gray-900 px-3 py-2 text-white text-base focus:border-blue-500 focus:outline-none"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>

                    <button
                        type="submit"
                        disabled={loading}
                        className="w-full rounded bg-blue-600 p-3 font-semibold text-white transition hover:bg-blue-700 disabled:opacity-50"
                    >
                        {loading ? 'Authenticating...' : 'Sign In'}
                    </button>
                </form>
            </div>
        </div>
    )
}
