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
    private final Scanner scanner;
    private final Customer defaultCustomer;
    private final ArrayListADT<Book> availableBooks;
    private final LinkedQueueADT<Order> orderQueue;

    public BookstoreSystem() {
        this.scanner = new Scanner(System.in);
        this.defaultCustomer = new Customer("Pham Minh Triet", "20 Thi Sach", "0823735666");
        this.availableBooks = new ArrayListADT<>();
        this.orderQueue = new LinkedQueueADT<>();
        loadDefaultBooks();
    }

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
        printBookTitles(booksInOrder);

        Sorting.insertionSort(booksInOrder);

        System.out.println("\n--- BOOKS AFTER SORTING (BY TITLE) ---");
        printBookTitles(booksInOrder);
        System.out.println("\n--- SEARCH FOR A BOOK IN THE ORDER ---");

        searchLoop:
        while (true) {
            System.out.println("\n--- CHOOSE SEARCH ALGORITHM ---");
            System.out.println("1. Flexible Search (Linear Search)");
            System.out.println("2. Exact Search (Binary Search)");
            System.out.println("3. Finish Searching This Order");
            System.out.print("Your choice: ");
            String searchChoice = scanner.nextLine();

            switch (searchChoice) {
                case "1":
                    System.out.print("Enter book title for FLEXIBLE search: ");
                    String linearTerm = scanner.nextLine();
                    ArrayListADT<Book> linearResults = Searching.linearSearchFlexible(booksInOrder, linearTerm);
                    if (!linearResults.isEmpty()) {
                        System.out.println("=> Found " + linearResults.size() + " result(s):");
                        printSearchResults(linearResults);
                    } else {
                        System.out.println("=> No books found matching '" + linearTerm + "'.");
                    }
                    break;

                case "2":
                    System.out.print("Enter EXACT book title for BINARY search: ");
                    String binaryTerm = scanner.nextLine();
                    ArrayListADT<Book> binaryResults = Searching.binarySearchByTitle(booksInOrder, binaryTerm);
                    if (!binaryResults.isEmpty()) {
                        System.out.println("=> Found " + binaryResults.size() + " result(s):");
                        printSearchResults(binaryResults);
                    } else {
                        System.out.println("=> No exact match found for '" + binaryTerm + "'.");
                    }
                    break;

                case "3":
                    break searchLoop;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        System.out.println("--- FINISHED PROCESSING ORDER ---");
        System.out.println("\n--- FINISHED ORDER: " + orderToProcess.getOrderId() + " ---");
        System.out.println("Customer name: " + orderToProcess.getCustomer().getName());
        System.out.println("Shipping address: " + orderToProcess.getCustomer().getAddress());
    }


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

    private void printBookTitles(ArrayListADT<Book> books) {
        for (int i = 0; i < books.size(); i++) {
            System.out.println("- " + books.get(i).getTitle());
        }
    }

    private void printSearchResults(ArrayListADT<Book> books) {
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            System.out.println("- " + book.getTitle() + " by " + book.getAuthor());
        }
    }


    private void loadDefaultBooks() {
        availableBooks.add(new Book("Dune", "Frank Herbert"));
        availableBooks.add(new Book("A Game of Thrones", "George R.R. Martin"));
        availableBooks.add(new Book("The Lord of the Rings", "J.R.R. Tolkien"));
        availableBooks.add(new Book("To Kill a Mockingbird", "Harper Lee"));
    }

    public static void main(String[] args) {
        BookstoreSystem mySystem = new BookstoreSystem();
        mySystem.run();
    }
}
