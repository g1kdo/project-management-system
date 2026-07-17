# Design Decisions

## Overview

The Project Management System was designed using Object-Oriented Programming (OOP) principles to create a modular,
maintainable, and extensible console application. The system separates responsibilities into models, interfaces, services, and
utility classes, making it easier to manage application logic and support future enhancements.

---
# 1. Separation of Concerns

The application is divided into three major layers:

- **Models**
    - Represent the application's data and encapsulate baseline constraints.
    - Examples:
        - Project
        - Task
        - User

- **Services**
    - Contain business logic and coordinate state mutations safely.
    - Examples:
        - ProjectService
        - TaskService
        - ReportService

- **Utilities**
    - Handle reusable helper functionality, input parsing, and custom error types.
    - Examples:
        - ValidationUtils
        - ConsoleMenu
        - Exceptions (`InvalidInputException`, `TaskNotFoundException`, `EmptyProjectException`, `ProjectNotFoundException`)

In addition, the application uses a `Completable` interface to define behavior for objects that can report whether they are complete.
The `Task` class implements this interface by providing the `isCompleted()` method based on the task's current status.

This separation keeps the `Main` class focused on controlling program flow rather than implementing business logic.

---
# 2. Use of Inheritance & Dynamic Association

Inheritance was used to model different project and user types.

## Projects

The abstract `Project` class stores shared information such as:

- ID
- Name
- Description
- Budget
- Members (An array of assigned `User` objects replacing manual tracking)
- Tasks

Specific project types extend this class:

- SoftwareProject
- HardwareProject

Instead of entering team sizes manually, projects now dynamically maintain a roster of concrete `User` entities. Calling
`getTeamSize()` evaluates the runtime occupancy of the member array, enforcing a strong object association.

---
## Users

The application also models users using inheritance.

```
User
   │
   ├── AdminUser
   └── RegularUser
```

Both users share common attributes such as:

- Name
- Email

while each subclass provides its own role.

---
# 3. Polymorphism

The program stores different project types using the common `Project` type.

Example:

```
Project newProject;

    newProject = new SoftwareProject(...);

    newProject = new HardwareProject(...);
```

This allows the rest of the application to work with projects without needing to know their concrete type.

The same approach is used for users.

---
# 4. Role-Based Access Control

Administrative actions are protected using user roles.

For example:

- Only administrators may remove tasks.

- Only administrators may change task status.

The current user profile is passed into service layers to check compliance before modifying tasks, throwing an exception if unauthorized.

---
# 5. Robust Exception Handling

User validation is backed by a custom exception framework under `utils.exceptions`. Instead of traditional conditional error printing, unexpected operations trigger explicitly typed exceptions:

- `InvalidInputException`: Thrown when project metrics or status parameters fail formatting laws.

- `ProjectNotFoundException`: Dispatched when querying mismatched project identifiers.

- `TaskNotFoundException`: Handled when target tasks cannot be discovered within parent projects.

- `EmptyProjectException`: Raised when progress summaries run against projects completely devoid of active tasks.

Centralizing validation avoids duplicate code and shields the runtime container from sudden input-driven failures.

---
# 6. Automated Unit Testing

The core completion and membership mechanics are verified utilizing JUnit testing frameworks. This guarantees that formula 
changes, status adjustments, and registration constraints yield reliable outcomes under rigorous boundary constraints.

---
# 7. In-Memory Storage

Projects and users remain stored inside bounded arrays during execution, satisfying the pure in-memory constraints without 
requiring an active external database layer.

---
# Summary

The design emphasizes:

Object-Oriented Programming principles

- Separation of concerns

- Inheritance and polymorphism

- Role-based access control

- Clean, decoupled service operations (SOLID)

- Custom exception propagation and stable menu loops

- Dynamic model cross-linking

- High maintainability and testability via JUnit suites