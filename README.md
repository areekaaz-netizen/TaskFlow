# TaskFlow – Android Task Management Application

TaskFlow is a modern Android task management and productivity application designed to help users organize, schedule, track, and manage their daily tasks efficiently.

The application provides a centralized workspace where users can create tasks, assign priorities, set due dates and reminders, monitor progress, and analyze their productivity through statistics and calendar-based views.

## Features

- Create, edit, and delete tasks
- Add task titles and descriptions
- Set due dates and times
- Assign Low, Medium, or High priority
- Schedule task reminders
- Enable notification sounds and vibration
- Mark tasks as completed
- View active, completed, and overdue tasks
- Search tasks
- Sort tasks by due date
- Calendar-based task management
- Daily progress tracking
- Productivity statistics and progress visualization
- Customizable notification preferences
- Reminder and snooze settings
- Light/Dark/System appearance options
- Persistent offline task storage

## Technology Stack

| Technology | Purpose |
|------------|---------|
| Kotlin | Primary programming language |
| Jetpack Compose | User interface development |
| MVVM | Application architecture |
| ViewModel | UI state and business-logic coordination |
| Repository Pattern | Separation of data access from UI logic |
| Room Database | Local persistent data storage |
| Navigation Compose | Screen navigation |
| WorkManager | Background task scheduling |
| Android Notifications | Task reminders and alerts |

## Architecture

TaskFlow follows the **MVVM (Model–View–ViewModel)** architecture with a repository-based data layer.
┌─────────────────────┐
│   Jetpack Compose   │
│         UI          │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│      ViewModel      │
│   UI State / Logic  │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│     Repository      │
│    Data Access      │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│      Room DAO       │
│   Database Queries  │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│    Room Database    │
│   Local Task Data   │
└─────────────────────┘
