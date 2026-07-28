# Design Decisions & Architecture

## Overview

The Project Management System is built using Object-Oriented Programming (OOP), Functional Programming (Streams/Lambdas), and Java Concurrency principles to create a modular, thread-safe, and persistent application. The system decouples responsibilities into models, interfaces, services, utilities, and persistence handlers.

---

# 1. Separation of Concerns & Modular Architecture

The application is structured into clear layers:

- **Models (`models.project`, `models.task`, `models.user`)**
    - Encapsulate data, domain rules, and regex constraint validation.
    - Examples: `Project`, `SoftwareProject`, `HardwareProject`, `Task`, `User`, `AdminUser`, `RegularUser`.

- **Services (`services`)**
    - Encapsulate domain logic, streaming filters, user authentication, and multi-threaded simulations.
    - Examples: `ProjectService`, `TaskService`, `UserService`, `StreamService`, `ConcurrencyService`.

- **Utilities & Persistence (`utils`)**
    - Provide validation utilities, string parsing, regular expression enforcement, and JSON file I/O operations.
    - Examples: `ValidationUtils`, `RegexValidator`, `FileUtils`.

- **Exceptions (`utils.exceptions`)**
    - Typed runtime exceptions for fail-fast error handling (`InvalidInputException`, `TaskNotFoundException`, `ProjectNotFoundException`, `EmptyProjectException`).

---

# 2. Dynamic Collections over Fixed Arrays

The system replaces fixed-size native arrays with standard Java Collections framework structures:
- **`Map<String, Project>` (`HashMap`)**: Serves as the high-performance project catalog, enabling $O(1)$ lookup time by Project ID.
- **`List<Task>` (`ArrayList`)**: Stores project tasks dynamically without arbitrary array boundaries.
- **`List<User>` (`ArrayList`)**: Maintains project team member rosters.

---

# 3. User Authentication & Role-Based Access Control (RBAC)

Rather than hardcoding standard default users, `UserService` manages interactive system access:
- **Authentication Flow**: Supports real user registration (`AdminUser` vs. `RegularUser`) and login via username/password verification.
- **Active Session Tracking**: Maintains a thread-safe reference to the logged-in `currentUser`.
- **Role Enforcement**: Critical operations (such as updating task statuses across projects) check user role privileges before executing actions.

---

# 4. Streams & Functional Programming

The application utilizes Java Streams and functional paradigms (`map`, `filter`, `flatMap`, `collect`, method references) via `StreamService`:
- Filtering projects based on task completion percentage thresholds.
- Extracting list projections (e.g., task names within projects).
- Aggregate calculations across projects using `flatMap` to evaluate global completion counts safely.

---

# 5. Regex Input Validation

Data integrity is guaranteed using pattern matching (`java.util.regex`):
- **Project IDs**: Strictly enforces `P\d{3}` (e.g., `P001`, `P004`).
- **Task IDs**: Strictly enforces `T\d{3}` (e.g., `T001`).
- **Emails**: Validates standard RFC 5322 string formats.
- Pre-validation occurs prior to instantiation, protecting project and task objects from corrupt states.

---

# 6. Persistence & File I/O (NIO)

Data is saved to and restored from disk (`data/projects_data.json`) using `java.nio.file.Files` and standard stream parsing:
- **Startup Auto-Load**: `FileUtils.loadProjects(...)` restores project catalog states and task listings.
- **Safe Parsing Safeguard**: Blocks malformed or invalid IDs using `RegexValidator` before constructor invocation, preventing startup crashes.
- **Exit Auto-Save**: Serializes catalog objects to JSON format on application exit.

---

# 7. Concurrency & Thread Safety

Multi-threaded task state modifications are managed in `ConcurrencyService`:
- Worker threads update task states in parallel using `Thread` and `Runnable`.
- Uses `synchronized` blocks on shared task instances to prevent race conditions during concurrent state updates.

---

# 8. Automated JUnit 5 Test Suite

Unit testing spans functional operations:
- Stream mapping and reduction filters (`StreamOperationsTest`).
- File persistence I/O serialization (`FilePersistenceTest`).
- Dynamic catalog management and regex input checks.