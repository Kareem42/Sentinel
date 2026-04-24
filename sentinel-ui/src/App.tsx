import { useEffect } from "react";
import axios from 'axios';
import './App.css'
import {ServiceForm} from "./components/ServiceForm.tsx";

function App() {
  useEffect(() => {
    axios.get("http://localhost:5173/api/v1/service")
    .then(res => console.log("Connection Successful:", res.data))
    .catch(err => console.log("Connection Failed:", err));
  }, []);
  return (
      <div>
        <h1>Sentinel Dashboard</h1>
          <ServiceForm/>
      </div>
  )
}

export default App
