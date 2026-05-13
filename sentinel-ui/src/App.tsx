
import './App.css'
import { ServiceForm } from "./components/ServiceForm.tsx";
import { ServiceList } from "./components/ServiceList.tsx";
import { useServices } from "./hooks/useServices.ts";
import { Toaster } from 'react-hot-toast';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { Login } from "./pages/Login";
import { AuthProvider } from "./context/AuthContext.tsx";
import { Register } from "./pages/Register.tsx";

function App() {
    const {services, isLoading, refresh} = useServices();
    return (
        <AuthProvider>
            <Router>
                <Routes>
                    <Route path="/" element={<Login/>} />
                    <Route path="/register" element={<Register/>}/>
                    <Route path="/dashboard" element={
                        <div style={{padding: "20px"}}>
                            <Toaster position="top-right"/>
                            <h1>Sentinel Dashboard</h1>
                            <ServiceForm onServiceAdded={refresh}/>
                            <hr/>
                            {isLoading ? <p>Loading services...</p> : <ServiceList services={services}/>}
                        </div>
                    }/>
                </Routes>
            </Router>
        </AuthProvider>
    )
}

export default App