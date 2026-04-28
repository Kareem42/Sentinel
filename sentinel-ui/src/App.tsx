
import './App.css'
import {ServiceForm} from "./components/ServiceForm.tsx";
import {ServiceList} from "./components/ServiceList.tsx";
import {useServices} from "./hooks/useServices.ts";
import {Toaster } from 'react-hot-toast';

function App() {
    const {services, isLoading, refresh} = useServices();
    return (
        <div style={{padding: "20px"}}>
            <Toaster position="top-right"/>
            <h1>Sentinel Dashboard</h1>
            <ServiceForm onServiceAdded={refresh}/>
            <hr/>
            {isLoading ? <p>Loading services...</p> : <ServiceList services={services}/>}
        </div>
    )
}

export default App