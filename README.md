# Library Management System

A console-based Java application for managing a small library catalogue and book loans. The project also includes a presentation and a project report in the `project ppt` and `project report` folders.

## Features

- Add, display, remove, and search books by ID or title
- Sort the catalogue by title
- Issue books to students and queue requests when a book is already issued
- Return books and automatically assign them to the next student in the waiting queue
- Save and restore catalogue and loan data using Java serialization

## Requirements

- Java Development Kit (JDK) 14 or newer

## Build and Run

Run these commands from the directory containing `LibraryManagementSystem.java`:

```powershell
javac LibraryManagementSystem.java
java LibraryManagementSystem
```

The program saves its data in `library.txt` in the current working directory. Run the program from the same directory to load the same library data on the next launch.

## Menu

1. Add Book
2. Display Books
3. Search Book by ID
4. Search Book by Title
5. Sort Books
6. Issue Book
7. Return Book
8. Remove Book
9. Exit
