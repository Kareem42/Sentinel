import type { MonitoredServiceResponse } from "../types.ts";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";

interface Props {
    services: MonitoredServiceResponse[];
}

export const ServiceList = ({ services }: Props) => {
    return (
        <Card>
            <CardHeader>
                <CardTitle>Monitored Services</CardTitle>
            </CardHeader>
            <CardContent>
                {services.length === 0 ? (
                    <p className="text-center text-muted-foreground py-8">
                        No services registered yet. Add one above.
                    </p>
                ) : (
                    <Table>
                        <TableHeader>
                            <TableRow>
                                <TableHead>Name</TableHead>
                                <TableHead>URL</TableHead>
                                <TableHead>Status</TableHead>
                            </TableRow>
                        </TableHeader>
                        <TableBody>
                            {services.map((s) => (
                                <TableRow key={s.id}>
                                    <TableCell className="font-medium">{s.name}</TableCell>
                                    <TableCell className="text-muted-foreground font-mono text-sm">
                                        {s.url}
                                    </TableCell>
                                    <TableCell>
                                        <Badge
                                            variant={s.status === 'UP' ? 'default' : 'destructive'}
                                            className={
                                                s.status === 'UP'
                                                    ? 'bg-emerald-500/15 text-emerald-600 dark:text-emerald-400 border-emerald-500/30 hover:bg-emerald-500/20'
                                                    : ''
                                            }
                                        >
                                            ● {s.status}
                                        </Badge>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                )}
            </CardContent>
        </Card>
    );
};
