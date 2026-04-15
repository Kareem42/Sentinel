# Module 10: Request vs. Response DTOs

## 1. Principle of Least Privilege
A `ServiceRequest` should only accept the bare minimum data needed to perform an action. This prevents "Mass Assignment" vulnerabilities where a user could potentially overwrite fields they shouldn't have access to.

## 2. Enrichment
The `ServiceResponse` is an "enriched" version of the data. It combines the user's input with system-generated data (UUIDs, Timestamps, calculated statuses).

## 3. Interview Talking Point
"I use separate DTOs for requests and responses. This allows me to keep my API inputs clean and secure while providing a rich, informative response to the frontend without exposing my internal database entities."