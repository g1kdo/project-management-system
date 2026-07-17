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
│   │   └── Completable.java          # Interface for completion logic
│   │
│   ├── services/
│   │   ├── ProjectService.java       # Project operations
│   │   ├── TaskService.java          # Task operations
│   │   └── ReportService.java        # Reporting logic
│   │
│   └── utils/
│       ├── ConsoleMenu.java          # Menu handling
│       ├── ValidationUtils.java      # Input validation
│       └── exceptions/               # Custom runtime boundaries
│           ├── InvalidInputException.java
│           ├── TaskNotFoundException.java
│           ├── EmptyProjectException.java
│           └── ProjectNotFoundException.java[cite: 2]
│
├── test/                             # Automated JUnit validation suites[cite: 2]
│   ├── ProjectTests.java
│   ├── TaskTests.java
│   └── ValidationTests.java
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
## Feature Summary

### Feature 1: Project Catalog Management

Following the Epic 1: Project Catalog Management, the user is able to:
- Create new projects (e.g., `SoftwareProject`, `HardwareProject`)
- View all existing projects with explicit, scannable data layouts
- Filter projects dynamically by specific type configurations
- Enforce positive budget allocations and validate records via standard exception traps

### Feature 2: Task Operations

Following the Epic 2: Task Operations, the user is able to:
- Add tasks to specific projects with validation preventing duplicate names
- Assign and track normalized statuses (Pending, In Progress, Completed)
- Update tasks gracefully while intercepting bad actions via `TaskNotFoundException`

### Feature 3: User Management & Dynamic Teams

Following the Epic 3: User Management, the system is able to:
- Manage system profiles (`RegularUser` and `AdminUser`) with role-based restrictions
- Dynamically add users as members to target projects (`joinProject`)
- Calculate team size properties continuously based on real member arrays, removing manual variables
- Review project rosters inside an isolated team dashboard (`viewTeam`)

### Feature 4: Status Processing & Reporting

Following the Epic 4: Status Processing & Reporting, the system is able to:
- Calculate exact completion percentages using object method boundaries
- Throw an explicit `EmptyProjectException` when generating progress calculations over unassigned project objects
- Round progress and average fields to two decimal places

### Feature 5: Menu Navigation & Error Stability

Following the Epic 5: Menu Navigation & Application Control, the system is able to:
- Present menu layers without experiencing hard runtime crashes during bad entry inputs
- Log clean warning strings to the console user, prompting immediate input correction cycles