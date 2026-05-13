import React, { createContext, useContext, useState } from "react";

interface AuthContextType {
    token: string | null;
    login: (token: string) => void;
    logout: () => void;
    isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{children: React.ReactNode}> = ({children}) => {
    const [token, setToken] = useState<string | null>(localStorage.getItem("sentinel_token"));

    const login = (newToken: string) => {
        localStorage.setItem("sentinel_token", newToken);
        setToken(newToken);
    };

    const logout = () => {
        localStorage.removeItem("sentinel_token");
        setToken(null);
    }

    const isAuthenticated = !!token;

    return (
        <AuthContext.Provider value={{token, login, logout, isAuthenticated}}>
            {children}
            </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) throw new Error('useAuth must be used within a AuthProvider');
    return context;
}