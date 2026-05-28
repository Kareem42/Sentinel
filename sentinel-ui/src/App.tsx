import { ServiceForm } from "./components/ServiceForm.tsx";
import { ServiceList } from "./components/ServiceList.tsx";
import { useServices } from "./hooks/useServices.ts";
import { Toaster } from 'react-hot-toast';
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { Login } from "./pages/Login";
import { AuthProvider, useAuth } from "./context/AuthContext.tsx";
import { Register } from "./pages/Register.tsx";
import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";

function Dashboard() {
    const { services, isLoading, refresh } = useServices();
    const { logout, username } = useAuth();


    return (
        <div className="min-h-screen bg-background">
            {/* Header */}
            <header className="border-b">
                <div className="max-w-6xl mx-auto px-6 h-14 flex items-center justify-between">
                    <span className="font-semibold text-lg tracking-tight">
                        ⬡ Sentinel
                    </span>
                    <Button variant="ghost" size="sm" onClick={logout}>
                        Sign out
                    </Button>
                </div>
            </header>

            {/* Main */}
            <main className="max-w-6xl mx-auto px-6 py-8 space-y-6">
                <div>
                    <h1 className="text-2xl font-bold bg-black tracking-tight">Welcome, {username}</h1>
                    <p className="text-muted-foreground text-sm mt-1">
                        Register and monitor your service endpoints.
                    </p>
                </div>

                <Separator />

                <ServiceForm onServiceAdded={refresh} />

                {isLoading ? (
                    <p className="text-muted-foreground text-sm">Loading services…</p>
                ) : (
                    <ServiceList services={services} />
                )}
            </main>
        </div>
    );
}

function AppRoutes() {
    const { isAuthenticated } = useAuth();

    return (
        <Routes>
            <Route path="/" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route
                path="/dashboard"
                element={isAuthenticated ? <Dashboard /> : <Navigate to="/" replace />}
            />
        </Routes>
    );
}

function App() {
    return (
        <AuthProvider>
            <Router>
                <Toaster position="top-right" />
                <AppRoutes />
            </Router>
        </AuthProvider>
    );
}

export default App;
