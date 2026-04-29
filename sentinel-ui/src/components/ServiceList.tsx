import type { MonitoredServiceResponse } from "../types.ts";

interface Props {
    services: MonitoredServiceResponse[];
}

export const ServiceList = ({ services }: Props) => {
    return (
        <div style={{ marginTop: "10px" }}>
            <h2>Monitored Services</h2>
            <table border={1} style={{ width: "100%", textAlign: 'left' }} >
                <thead>
                <tr>
                    <th>Name</th>
                    <th>URL</th>
                    <th>Status</th>
                </tr>
                </thead>
                <tbody>
                {services.map((s) => (
                    <tr key={s.id}>
                        <td style={{padding: '10px'}}>{s.name}</td>
                        <td style={{padding: '10px'}}>{s.url}</td>
                        <td style={{padding: '10px'}}>
                            <span style={{color: 'green'}}>● Online</span>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    )
}