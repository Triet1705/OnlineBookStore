package system;

import model.Book;
import model.Customer;
import model.Order;
import dsa.ArrayList.ArrayListADT;
import dsa.Queue.LinkedQueueADT;
import dsa.Sorting;
import dsa.Searching;
import java.util.Scanner;

public class BookstoreSystem {
    // --- SYSTEM ATTRIBUTES ---
    private final Scanner scanner;
    private final Customer defaultCustomer;
    private final ArrayListADT<Book> availableBooks;
    private final LinkedQueueADT<Order> orderQueue;

    // --- CONSTRUCTOR ---
    public BookstoreSystem() {
        this.scanner = new Scanner(System.in);
        this.defaultCustomer = new Customer("Pham Minh Triet", "20 Thi Sach", "0823735666");
        this.availableBooks = new ArrayListADT<>();
        this.orderQueue = new LinkedQueueADT<>();
        loadDefaultBooks();
    }

    // --- MAIN APPLICATION LOOP ---
    public void run() {
        System.out.println("WELCOME TO THE ONLINE BOOKSTORE SYSTEM");
        System.out.println("Default Customer: " + defaultCustomer.getName());

        while (true) {
            System.out.println("\n========= MAIN MENU =========");
            System.out.println("1. Create New Order");
            System.out.println("2. Process Next Order");
            System.out.println("3. Exit Program");
            System.out.print("Please enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleOrderCreation();
                    break;
                case "2":
                    processNextOrder();
                    break;
                case "3":
                    System.out.println("Thank you for using the system!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // --- LOGIC HANDLING METHODS ---

    private void handleOrderCreation() {
        Order newOrder = createNewOrder();
        if (!newOrder.getBooks().isEmpty()) {
            orderQueue.offer(newOrder);
            System.out.println("\n==> Order " + newOrder.getOrderId() + " has been confirmed and placed in the processing queue.");
        } else {
            System.out.println("\n==> Order is empty and has been cancelled.");
        }
    }

    private void processNextOrder() {
        if (orderQueue.isEmpty()) {
            System.out.println("\nThere are no orders in the queue to process.");
            return;
        }

        Order orderToProcess = orderQueue.poll();
        System.out.println("\n--- PROCESSING ORDER: " + orderToProcess.getOrderId() + " ---");
        System.out.println("Customer: " + orderToProcess.getCustomer().getName());

        ArrayListADT<Book> booksInOrder = orderToProcess.getBooks();

        System.out.println("\n--- BOOKS BEFORE SORTING ---");
        printBooks(booksInOrder);

        Sorting.insertionSort(booksInOrder);

        System.out.println("\n--- BOOKS AFTER SORTING (BY TITLE) ---");
        printBooks(booksInOrder);

        System.out.println("\n--- SEARCH FOR A BOOK IN THE ORDER ---");
        System.out.print("Enter the title of the book to search for: ");
        String titleToSearch = scanner.nextLine();
        Book foundBook = Searching.binarySearchByTitle(booksInOrder, titleToSearch);

        if (foundBook != null) {
            System.out.println("=> Found '" + foundBook.getTitle() + "' in the order.");
        } else {
            System.out.println("=> Could not find '" + titleToSearch + "' in the order.");
        }
        System.out.println("--- FINISHED PROCESSING ORDER ---");
    }

    // --- HELPER METHODS ---

    private Order createNewOrder() {
        String orderId = "ORD" + (int)(Math.random() * 1000);
        Order newOrder = new Order(orderId, this.defaultCustomer);
        System.out.println("\nCreated new order (" + orderId + ") for customer " + this.defaultCustomer.getName());

        while (true) {
            System.out.println("\n--- PLEASE SELECT BOOKS ---");
            for (int i = 0; i < availableBooks.size(); i++) {
                Book book = availableBooks.get(i);
                System.out.println((i + 1) + ". " + book.getTitle() + " - by " + book.getAuthor());
            }
            System.out.println("0. Finish Order");
            System.out.print("Enter a number to add a book to the order: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice == 0) break;

                if (choice > 0 && choice <= availableBooks.size()) {
                    Book selectedBook = availableBooks.get(choice - 1);
                    newOrder.addBook(selectedBook);
                    System.out.println("-> Added '" + selectedBook.getTitle() + "' to the order.");
                } else {
                    System.out.println("Invalid number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return newOrder;
    }

    private void printBooks(ArrayListADT<Book> books) {
        for (int i = 0; i < books.size(); i++) {
            System.out.println("- " + books.get(i).getTitle());
        }
    }

    private void loadDefaultBooks() {
        availableBooks.add(new Book("Dune", "Frank Herbert"));
        availableBooks.add(new Book("A Game of Thrones", "George R.R. Martin"));
        availableBooks.add(new Book("The Lord of the Rings", "J.R.R. Tolkien"));
        availableBooks.add(new Book("To Kill a Mockingbird", "Harper Lee"));
    }

    // --- MAIN METHOD (ENTRY POINT) ---
    // Đây là hàm main đã được gộp vào
    public static void main(String[] args) {
        // Tạo một đối tượng của hệ thống nhà sách
        BookstoreSystem mySystem = new BookstoreSystem();

        // Bắt đầu chạy hệ thống
        mySystem.run();
    }
}