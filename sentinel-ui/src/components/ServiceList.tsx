import type { MonitoredServiceResponse } from "../types.ts";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";
import toast from "react-hot-toast";

interface Props {
    services: MonitoredServiceResponse[];
    totalElements: number;
    onDelete: (id: string) => Promise<void>;
}

function formatLastChecked(iso: string | null): string {
    if (!iso) return "Never";
    return new Date(iso).toLocaleString([], {
        year: "numeric",
        month: "short",
        day: "numeric",
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
    });
}

function formatResponseTime(ms: number | null): string {
    if (ms === null) return "—";
    if (ms < 1000) return `${ms} ms`;
    return `${(ms / 1000).toFixed(1)} s`;
}

export const ServiceList = ({ services, totalElements, onDelete }: Props) => {
    const handleDelete = async (id: string, name: string) => {
        const loadingToast = toast.loading(`Removing ${name}…`);
        try {
            await onDelete(id);
            toast.success(`${name} removed.`, { id: loadingToast });
        } catch {
            toast.error("Failed to remove service.", { id: loadingToast });
        }
    };

    return (
        <Card>
            <CardHeader className="flex flex-row items-center justify-between">
                <CardTitle>Monitored Services</CardTitle>
                {totalElements > 0 && (
                    <span className="text-sm text-muted-foreground">{totalElements} total</span>
                )}
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
                                <TableHead>Response Time</TableHead>
                                <TableHead>Last Checked</TableHead>
                                <TableHead>Interval</TableHead>
                                <TableHead className="text-right">Actions</TableHead>
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
                                            variant={s.status === "UP" ? "default" : "destructive"}
                                            className={
                                                s.status === "UP"
                                                    ? "bg-emerald-500/15 text-emerald-600 dark:text-emerald-400 border-emerald-500/30 hover:bg-emerald-500/20"
                                                    : ""
                                            }
                                        >
                                            ● {s.status}
                                        </Badge>
                                    </TableCell>
                                    <TableCell className="text-sm tabular-nums">
                                        {formatResponseTime(s.lastResponseTimeMs)}
                                    </TableCell>
                                    <TableCell className="text-sm text-muted-foreground tabular-nums">
                                        {formatLastChecked(s.lastChecked)}
                                    </TableCell>
                                    <TableCell className="text-sm text-muted-foreground">
                                        {s.checkIntervalSeconds}s
                                    </TableCell>
                                    <TableCell className="text-right">
                                        <Button
                                            variant="ghost"
                                            size="sm"
                                            className="text-destructive hover:text-destructive hover:bg-destructive/10"
                                            onClick={() => handleDelete(s.id, s.name)}
                                        >
                                            Remove
                                        </Button>
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
