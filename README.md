# Project Management System

## Project Overview

Built a console-based Project/Task Management System where users can create projects, add and assign tasks, record task 
completion status, and calculate completion averages.

This lab focuses on core Java programming and object-oriented principles while storing all data in-memory using arrays 
(no databases or external dependencies).

The lab serves as the foundation for the multi-week Project Management series. Future labs will gradually enhance this 
base — replacing arrays with collections, adding file persistence, and integrating user authentication and reporting dashboards.

---
## Project Structure

```
project-management-system/
│
├── src/
│   ├── Main.java                     # Application entry point
│   ├── models/
|   |   ├── project/
|   │   │   ├── Project.java              # Abstract base class
|   │   │   ├── SoftwareProject.java      # Concrete project type
|   │   │   ├── HardwareProject.java      # Concrete project type
|   │   │   └──Type.java                 # Project Type Enum
|   |   ├── task/
|   │   │   ├── Task.java                 # Task model   
|   │   │   └── Status.java               # Task Status Enum
|   |   └── user/
|   │       ├── User.java                 # Abstract user base
|   │       ├── RegularUser.java          # Concrete user type
|   │       ├── AdminUser.java            # Concrete user type
|   │       └── Role.java                 # User Role Enum
│   │
│   ├── interfaces/
|   |   ├── TaskFilter.java           # Interface for filtering logic
│   │   └── Completable.java          # Interface for completion logic
│   │
│   ├── services/
│   │   ├── ProjectService.java       # Project operations
│   │   ├── TaskService.java          # Task operations
│   │   ├── ReportService.java        # Reporting logic
│   │   ├── StreamService.java        # Stream filtering, mapping, & reduction operations
│   │   └── ConcurrencyService.java   # Thread-safe multi-threaded update simulation
│   │
│   └── utils/
│       ├── ConsoleMenu.java          # Menu handling
│       ├── ValidationUtils.java      # Input validation
│       ├── FileUtils.java            # NIO persistence read/write routines
│       ├── RegexValidator.java       # Pattern matching validation utilities
│       └── exceptions/               # Custom runtime boundaries
│           ├── InvalidInputException.java
│           ├── TaskNotFoundException.java
│           ├── EmptyProjectException.java
│           └── ProjectNotFoundException.java
│
├── test/                             # Automated JUnit validation suites[cite: 2]
│   ├── ProjectTests.java
│   ├── TaskTests.java
│   ├── ValidationTests.java
│   ├── StreamOperationsTest.java
|   └── FilePersistenceTest.java
|
|
├── docs/
│   ├── class-diagram.png
│   └── design-decisions.md
│
└── README.md
```
---
## Setup & Run Instructions

- Installed JDK 21 and IntelliJ IDEA
- Created project folder structure
- Tested object creation with sample data

> To run application, you must go to `Main.java` and depending on the IDE you use, you should be able to locate the run 
button at the top of the window.

---
## Setup & Run Instructions

- Installed JDK 21 and IntelliJ IDEA
- Configure JUnit libraries within your environment classpath

> To run the main application workflow, target `Main.java` and click the execution arrow within your IDE dashboard.
> To verify code stability, execute the suites inside the `/test` folder via the integrated test runner window.

---

## Key Features

1. **Interactive User Authentication**
    - Register as an `AdminUser` or `RegularUser`.
    - Log in using username and password verification.
    - Session-aware role restrictions for sensitive task updates.

2. **Project Catalog (HashMap)**
    - Create and manage projects (`SoftwareProject`, `HardwareProject`).
    - Standardized catalog keys utilizing `$O(1)$` HashMap storage.

3. **Regex Pattern Enforcement**
    - Strict pattern verification for Project IDs (`P###`), Task IDs (`T###`), and user email strings.

4. **Stream Processing Engine**
    - Filter projects based on task completion percentage thresholds.
    - Map and extract list attributes across complex project lists using streams.

5. **JSON File Persistence**
    - Save and load state from `data/projects_data.json` using `java.nio.file.Files`.
    - Automatic pre-validation shields data from corrupted input blocks.

6. **Parallel Concurrency Engine**
    - Thread-safe task state update simulation using `Thread`, `Runnable`, and `synchronized` locking boundaries.
