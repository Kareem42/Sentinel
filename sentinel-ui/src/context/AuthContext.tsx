import React, { createContext, useContext, useState } from "react";

interface AuthContextType {
    token: string | null;
    username: string | null;
    login: (token: string, username: string) => void;
    logout: () => void;
    isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{children: React.ReactNode}> = ({children}) => {
    const [token, setToken] = useState<string | null>(localStorage.getItem("sentinel_token"));
    const [username, setUsername] = useState<string | null>(localStorage.getItem("sentinel_username"));

    const login = (newToken: string, newUsername: string) => {
        localStorage.setItem("sentinel_token", newToken);
        localStorage.setItem("sentinel_username", newUsername);
        setToken(newToken);
        setUsername(newUsername);
    };

    const logout = () => {
        localStorage.removeItem("sentinel_token");
        localStorage.removeItem("sentinel_username");
        setToken(null);
        setUsername(null);
    };

    const isAuthenticated = !!token;

    return (
        <AuthContext.Provider value={{token, username, login, logout, isAuthenticated}}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) throw new Error('useAuth must be used within a AuthProvider');
    return context;
}