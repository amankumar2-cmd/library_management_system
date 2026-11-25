import java.io.*;
import java.util.*;

class Book implements Serializable {
    String id, title, author;
    String issuedTo = "";
    Queue<String> waiting = new LinkedList<>();

    Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }
}

class Node {
    Book book;
    Node next;

    Node(Book book) {
        this.book = book;
    }
}

public class LibraryManagementSystem {

    static Node head;
    static HashMap<String, Book> bookMap = new HashMap<>();
    static int nextId = 101;

    static Scanner sc = new Scanner(System.in);
    static final String FILE = "library.txt";

    // Add book using Linked List
    static void addBook() {
        System.out.print("Enter book title: ");
        String title = sc.nextLine();

        System.out.print("Enter author name: ");
        String author = sc.nextLine();

        if (title.trim().isEmpty() || author.trim().isEmpty()) {
            System.out.println("Title and author cannot be empty.");
            return;
        }

        String id = "B" + nextId++;
        Book b = new Book(id, title, author);

        Node newNode = new Node(b);

        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != null)
                temp = temp.next;

            temp.next = newNode;
        }

        bookMap.put(id, b);

        System.out.println("Book added successfully!");
        System.out.println("Book ID: " + id);

        saveData();
    }

    // Display all books
    static void displayBooks() {
        if (head == null) {
            System.out.println("No books available.");
            return;
        }

        Node temp = head;

        while (temp != null) {
            Book b = temp.book;

            System.out.println(
                b.id + " | " + b.title + " | " + b.author
            );

            temp = temp.next;
        }
    }

    // Search book by ID
    static void searchBook() {
        System.out.print("Enter book ID: ");
        String id = sc.nextLine().trim();

        Book b = bookMap.get(id);

        if (b == null) {
            System.out.println("Book " + id + " not found.");
        } else {
            System.out.println(
                b.id + " | " + b.title + " | " + b.author
            );
        }
    }

    // Search book by title
    static void searchByTitle() {
        System.out.print("Enter title: ");
        String title = sc.nextLine().toLowerCase();

        Node temp = head;
        boolean found = false;

        while (temp != null) {
            Book b = temp.book;

            if (b.title.toLowerCase().contains(title)) {
                System.out.println(
                    b.id + " | " + b.title + " | " + b.author
                );
                found = true;
            }

            temp = temp.next;
        }

        if (!found)
            System.out.println("Book not found.");
    }

    // Sort books by title
    static void sortBooks() {
        ArrayList<Book> list = new ArrayList<>();

        Node temp = head;

        while (temp != null) {
            list.add(temp.book);
            temp = temp.next;
        }

        list.sort(
            Comparator.comparing(b -> b.title.toLowerCase())
        );

        System.out.println("Books sorted by title:");

        for (Book b : list) {
            System.out.println(
                b.id + " | " + b.title + " | " + b.author
            );
        }
    }

    // Issue book or add student to waiting queue
    static void issueBook() {
        System.out.print("Enter book ID: ");
        String id = sc.nextLine().trim();

        Book b = bookMap.get(id);

        if (b == null) {
            System.out.println("Book not found.");
            return;
        }

        System.out.print("Enter student name: ");
        String student = sc.nextLine().trim();

        if (student.isEmpty()) {
            System.out.println("Student name cannot be empty.");
            return;
        }

        if (b.issuedTo.isEmpty()) {
            b.issuedTo = student;
            System.out.println("Book issued to " + student);
        } else {
            b.waiting.offer(student);

            System.out.println(
                student + " added to waiting queue. Position: "
                + b.waiting.size()
            );
        }

        saveData();
    }

    // Return book and serve waiting queue
    static void returnBook() {
        System.out.print("Enter book ID: ");
        String id = sc.nextLine().trim();

        Book b = bookMap.get(id);

        if (b == null) {
            System.out.println("Book not found.");
            return;
        }

        if (b.issuedTo.isEmpty()) {
            System.out.println("Book is not issued.");
            return;
        }

        String previousStudent = b.issuedTo;

        System.out.println("Book returned by " + previousStudent);

        if (!b.waiting.isEmpty()) {
            b.issuedTo = b.waiting.poll();

            System.out.println(
                "Book assigned to waiting student: " + b.issuedTo
            );
        } else {
            b.issuedTo = "";
            System.out.println("Book is now available.");
        }

        saveData();
    }

    // Remove book from catalogue
    static void removeBook() {
        System.out.print("Enter book ID: ");
        String id = sc.nextLine().trim();

        Book b = bookMap.get(id);

        if (b == null) {
            System.out.println("Book not found.");
            return;
        }

        if (!b.issuedTo.isEmpty() || !b.waiting.isEmpty()) {
            System.out.println(
                "Cannot remove issued book or book with waiting requests."
            );
            return;
        }

        if (head.book.id.equals(id)) {
            head = head.next;
        } else {
            Node temp = head;

            while (temp.next != null &&
                   !temp.next.book.id.equals(id)) {
                temp = temp.next;
            }

            if (temp.next != null)
                temp.next = temp.next.next;
        }

        bookMap.remove(id);

        System.out.println("Book removed successfully.");
        saveData();
    }

    // Save data to file
    static void saveData() {
        try (ObjectOutputStream out =
             new ObjectOutputStream(new FileOutputStream(FILE))) {

            ArrayList<Book> list = new ArrayList<>();
            Node temp = head;

            while (temp != null) {
                list.add(temp.book);
                temp = temp.next;
            }

            out.writeObject(list);
            out.writeInt(nextId);

        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }

    // Load data from file
    @SuppressWarnings("unchecked")
    static void loadData() {
        File file = new File(FILE);

        if (!file.exists())
            return;

        try (ObjectInputStream in =
             new ObjectInputStream(new FileInputStream(FILE))) {

            ArrayList<Book> list =
                (ArrayList<Book>) in.readObject();

            nextId = in.readInt();

            for (Book b : list) {
                Node newNode = new Node(b);

                if (head == null) {
                    head = newNode;
                } else {
                    Node temp = head;

                    while (temp.next != null)
                        temp = temp.next;

                    temp.next = newNode;
                }

                bookMap.put(b.id, b);
            }

        } catch (Exception e) {
            System.out.println("Error loading library data.");
        }
    }

    public static void main(String[] args) {
        loadData();

        while (true) {
            System.out.println("\n===== LIBRARY MANAGEMENT =====");
            System.out.println("1. Add Book");
            System.out.println("2. Display Books");
            System.out.println("3. Search Book by ID");
            System.out.println("4. Search Book by Title");
            System.out.println("5. Sort Books");
            System.out.println("6. Issue Book");
            System.out.println("7. Return Book");
            System.out.println("8. Remove Book");
            System.out.println("9. Exit");

            System.out.print("Enter choice: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> addBook();
                case "2" -> displayBooks();
                case "3" -> searchBook();
                case "4" -> searchByTitle();
                case "5" -> sortBooks();
                case "6" -> issueBook();
                case "7" -> returnBook();
                case "8" -> removeBook();
                case "9" -> {
                    saveData();
                    System.out.println("Thank you!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}