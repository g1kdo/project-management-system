# Design Decisions

## Overview

The Project Management System was designed using Object-Oriented Programming (OOP) principles to create a modular, 
maintainable, and extensible console application. The system separates responsibilities into models, interfaces, services, and 
utility classes, making it easier to manage application logic and support future enhancements.

---
# 1. Separation of Concerns

The application is divided into three major layers:

- **Models**
    - Represent the application's data.
    - Examples:
        - Project
        - Task
        - User

- **Services**
    - Contain business logic.
    - Examples:
        - ProjectService
        - TaskService
        - ReportService

- **Utilities**
    - Handle reusable helper functionality.
    - Examples:
        - ValidationUtils
        - ConsoleMenu

In addition, The application uses a `Completable` interface to define behavior for objects that can report whether they are complete.
The `Task` class implements this interface by providing the `isCompleted()` method based on the task's current status.

This separation keeps the `Main` class focused on controlling program flow rather than implementing business logic.

---
# 2. Use of Inheritance

Inheritance was used to model different project and user types.

## Projects

The abstract `Project` class stores shared information such as:

- ID
- Name
- Description
- Budget
- Team Size
- Tasks

Specific project types extend this class:

- SoftwareProject
- HardwareProject

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

The current user is stored in the application and passed into service methods for permission checking.

---
# 5. Input Validation

All user input is validated before processing using the `ValidationUtils` class.

Examples include:

- integer validation
- double validation
- range checking

Centralizing validation avoids duplicated code throughout the application.

---
# 6. In-Memory Storage

Projects and users are stored in arrays during program execution.

This approach was chosen because:

- it satisfies the project requirements
- it keeps the implementation simple
- no external database is required

---
# Summary

The design emphasizes:

- Object-Oriented Programming principles
- Separation of concerns
- Inheritance and polymorphism
- Role-based access control
- Modular service classes
- Input validation
- Maintainability and future extensibility

These decisions produce a structured console application that is easier to understand, test, and extend.