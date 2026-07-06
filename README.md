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
│   │   ├── Project.java              # Abstract base class
│   │   ├── SoftwareProject.java      # Concrete project type
│   │   ├── HardwareProject.java      # Concrete project type
│   │   ├── Task.java                 # Task model
│   │   ├── User.java                 # Abstract user base
│   │   ├── RegularUser.java          # Concrete user type
│   │   ├── AdminUser.java            # Concrete user type
│   │   └── StatusReport.java         # Status report generation
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
│       └── ValidationUtils.java      # Input validation
│
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
## Feature Summary 

### Feature 1: Project Catalog Management

Following the Epic 1: Project Catalog Management, the user is able to: 
- Create new projects (e.g., `SoftwareProject`, `HardwareProject`)
- View all existing projects with details (ID, name, description, team size, budget, etc.)
- Filter projects by type (software/hardware)
- Display project-specific attributes dynamically

### Feature 2: Task Operations

Following the Epic 2: Task Operations, the user is able to:
- Add tasks to specific projects
- Assign task status (Pending, In Progress, Completed)
- View all tasks per project with progress details
- Update or delete tasks
- Validate inputs to prevent invalid task status or duplicate task names

### Feature 3: User Management

Following the Epic 3: User Management, the system is able to: 
- Create and manage system users (`RegularUser` and `AdminUser`)
- Assign users to projects or tasks
- Enforce role-based access (Admin can delete/update; Regular can view/add)
- Automatically generate unique user IDs

### Feature 4: Status Processing & Reporting

Following the Epic 4: Status Processing & Reporting, the system is able to: 
- Calculate and display completion averages per project
- Generate status reports (e.g., "Project Alpha is 75% complete")
- Display task statistics: total, completed, and pending counts
- Show per-user performance summaries (future expansion)

### Feature 5: Menu Navigation & User Experience

Following the Epic 5: Menu Navigation & Application Control, the system is able to: 
- Display a clear main menu and sub-menus for operations
- Validate all user inputs (numbers, text, IDs)
- Provide formatted outputs with clear sections and alignment
- Support graceful exit and return navigation

