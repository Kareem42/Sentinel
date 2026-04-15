# Module 11: The Persistence Lifecycle

## 1. New/Transient State
When we call `new MonitoredService()`, the object exists only in the computer's RAM. It has no `id` yet.

## 2. Managed State
When we call `repository.save()`, the object is sent to the database. The database assigns it a unique identity (UUID).

## 3. Why return the 'saved' object?
The `repository.save()` method returns a **new instance** of the entity that represents its state in the database. We use THIS instance to build our `ServiceResponse` so we can give the user their new ID.

## 4. Interview Talking Point
"I ensure that my API responses are built from the persisted entity returned by the repository. This guarantees that the ID and system-generated timestamps are accurately reflected in the response sent to the client."