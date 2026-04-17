# Module 20: Port Forwarding & Host Mapping

## 1. Host vs. Container Ports
In Docker, a port mapping follows the `HOST:CONTAINER` format.
- **Container Port (5432):** The port the service listens on *inside* the isolated container.
- **Host Port (5433):** The port we use on our actual laptop to "tunnel" into that container.

## 2. Resolving Conflicts
When the default port (5432) is occupied by a system service, shifting the host port allows us to maintain the project's isolation without needing to uninstall system-level software.

## 3. Interview Talking Point
"I encountered a port conflict with a local system service. Rather than modifying the system environment, I utilized Docker's port mapping capabilities to shift the host port to 5433. This ensured a clean development environment and demonstrated my ability to manage infrastructure constraints."