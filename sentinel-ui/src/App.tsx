
import './App.css'
import {ServiceForm} from "./components/ServiceForm.tsx";
import {ServiceList} from "./components/ServiceList.tsx";
import {useServices} from "./hooks/useServices.ts";
import { Toaster } from 'react-hot-toast';
import { BrowserRouter, Routes, Route } from "react-router";
import {Login} from "./pages/Login";

function App() {
    const {services, isLoading, refresh} = useServices();
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Login /> } />
                <Route path="/dashboard" element={
                    <div style={{padding: "20px"}}>
                        <Toaster position="top-right"/>
                        <h1>Sentinel Dashboard</h1>
                        <ServiceForm onServiceAdded={refresh}/>
                        <hr/>
                        {isLoading ? <p>Loading services...</p> : <ServiceList services={services}/>}
                    </div>
                } />
            </Routes>
        </BrowserRouter>

    )
}

export default App