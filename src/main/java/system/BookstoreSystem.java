package system;

import model.Book;
import model.Customer;
import model.Order;
import model.OrderItem;
import dsa.ArrayList.ArrayListADT;
import dsa.Queue.LinkedQueueADT;
import dsa.Sorting;
import dsa.Searching;
import java.util.Scanner;

public class BookstoreSystem {
    private final Scanner scanner;
    private final ArrayListADT<Customer> customers;
    private final ArrayListADT<Book> availableBooks;
    private final LinkedQueueADT<Order> orderQueue;

    public BookstoreSystem() {
        this.scanner = new Scanner(System.in);
        this.customers = new ArrayListADT<>();
        this.availableBooks = new ArrayListADT<>();
        this.orderQueue = new LinkedQueueADT<>();
        loadDefaultBooks();
        loadDefaultCustomers();
    }

    public void run() {
        System.out.println("WELCOME TO THE ONLINE BOOKSTORE SYSTEM");

        while (true) {
            System.out.println("\n========= MAIN MENU =========");
            System.out.println("1. Create New Order");
            System.out.println("2. Process Next Order");
            System.out.println("3. View Orders in Queue");
            System.out.println("4. Edit an Order in Queue");
            System.out.println("5. Delete an Order from Queue");
            System.out.println("6. View Inventory");
            System.out.println("0. Exit Program");
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
                    viewOrdersInQueue();
                    break;
                case "4":
                    handleEditOrder();
                    break;
                case "5":
                    handleDeleteOrder();
                    break;
                case "6":
                    viewInventory();
                    break;
                case "0":
                    System.out.println("Thank you for using the system!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleOrderCreation() {
        Customer orderCustomer = selectOrCreateCustomer();
        if (orderCustomer == null) {
            System.out.println("\nOrder creation cancelled.");
            return;
        }

        Order newOrder = createNewOrder(orderCustomer);

        if (newOrder != null && !newOrder.getItems().isEmpty()) {
            deductStock(newOrder);
            orderQueue.offer(newOrder);
            System.out.println("\n==> Order " + newOrder.getOrderId() + " has been confirmed and placed in the processing queue.");
        } else {
            System.out.println("\n==> Order creation was cancelled or order is empty.");
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

        ArrayListADT<OrderItem> itemsInOrder = orderToProcess.getItems();
        Sorting.insertionSort(itemsInOrder);

        System.out.println("\n--- ITEMS IN ORDER (Sorted by Title) ---");
        printOrderItems(orderToProcess);

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
                    ArrayListADT<OrderItem> linearResults = Searching.linearSearchFlexible(itemsInOrder, linearTerm);
                    if (!linearResults.isEmpty()) {
                        System.out.println("=> Found " + linearResults.size() + " result(s):");
                        printOrderItemsFromList(linearResults);
                    } else {
                        System.out.println("=> No books found matching '" + linearTerm + "'.");
                    }
                    break;

                case "2":
                    System.out.print("Enter EXACT book title for BINARY search: ");
                    String binaryTerm = scanner.nextLine();
                    ArrayListADT<OrderItem> binaryResults = Searching.binarySearchByTitle(itemsInOrder, binaryTerm);
                    if (!binaryResults.isEmpty()) {
                        System.out.println("=> Found " + binaryResults.size() + " result(s):");
                        printOrderItemsFromList(binaryResults);
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

        System.out.println("\n--- FINISHED PROCESSING ORDER: " + orderToProcess.getOrderId() + " ---");
        System.out.println("Customer: " + orderToProcess.getCustomer().getName());
        System.out.println("Shipping address: " + orderToProcess.getCustomer().getAddress());
    }

    private void viewOrdersInQueue() {
        System.out.println("\n--- VIEWING ORDERS IN QUEUE ---");
        if (orderQueue.isEmpty()) {
            System.out.println("The order queue is currently empty.");
            return;
        }

        System.out.println("There are " + orderQueue.size() + " order(s) waiting to be processed:");
        LinkedQueueADT<Order> tempQueue = new LinkedQueueADT<>();
        int count = 1;

        while (!orderQueue.isEmpty()) {
            Order order = orderQueue.poll();
            System.out.println("\n" + count + ". Order ID: " + order.getOrderId() + " | Customer: " + order.getCustomer().getName());
            printOrderItems(order);
            tempQueue.offer(order);
            count++;
        }

        while (!tempQueue.isEmpty()) {
            orderQueue.offer(tempQueue.poll());
        }
    }

    private void handleEditOrder() {
        System.out.println("\n--- EDIT AN ORDER IN QUEUE ---");
        if (orderQueue.isEmpty()) {
            System.out.println("The order queue is empty. Nothing to edit.");
            return;
        }

        ArrayListADT<Order> orderList = queueToArrayList();

        System.out.println("Please select an order to edit:");
        for (int i = 0; i < orderList.size(); i++) {
            System.out.println((i + 1) + ". Order ID: " + orderList.get(i).getOrderId());
        }
        System.out.println("0. Cancel");
        System.out.print("Your choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) return;

            if (choice > 0 && choice <= orderList.size()) {
                Order orderToEdit = orderList.get(choice - 1);
                returnStock(orderToEdit);
                Order updatedOrder = editOrder(orderToEdit, false);
                deductStock(updatedOrder);
                orderList.set(choice - 1, updatedOrder);
                arrayListToQueue(orderList);
                System.out.println("==> Order " + updatedOrder.getOrderId() + " has been updated successfully.");

            } else {
                System.out.println("Invalid selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    private void handleDeleteOrder() {
        System.out.println("\n--- DELETE AN ORDER FROM QUEUE ---");
        if (orderQueue.isEmpty()) {
            System.out.println("The order queue is empty. Nothing to delete.");
            return;
        }

        ArrayListADT<Order> orderList = queueToArrayList();

        System.out.println("Please select an order to delete:");
        for (int i = 0; i < orderList.size(); i++) {
            System.out.println((i + 1) + ". Order ID: " + orderList.get(i).getOrderId());
        }
        System.out.println("0. Cancel");
        System.out.print("Your choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) return;

            if (choice > 0 && choice <= orderList.size()) {
                Order removedOrder = orderList.remove(choice - 1);
                returnStock(removedOrder);
                arrayListToQueue(orderList);
                System.out.println("==> Order " + removedOrder.getOrderId() + " has been successfully deleted.");
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }


    private Customer selectOrCreateCustomer() {
        System.out.println("\n--- SELECT OR CREATE A CUSTOMER FOR THE ORDER ---");
        if (!customers.isEmpty()) {
            System.out.println("Existing Customers:");
            for (int i = 0; i < customers.size(); i++) {
                System.out.println((i + 1) + ". " + customers.get(i).getName());
            }
        }
        System.out.println("---------------------------------");
        System.out.println("1. Add a New Customer");
        System.out.println("0. Cancel and return to Main Menu");
        System.out.print("Your choice: ");

        String choice = scanner.nextLine();

        if ("0".equalsIgnoreCase(choice)) return null;
        if ("1".equalsIgnoreCase(choice)) return createNewCustomer();

        try {
            int customerIndex = Integer.parseInt(choice) - 1;
            if (customerIndex >= 0 && customerIndex < customers.size()) {
                return customers.get(customerIndex);
            } else {
                System.out.println("Invalid number. Please try again.");
                return selectOrCreateCustomer();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please try again.");
            return selectOrCreateCustomer();
        }
    }

    private Customer createNewCustomer() {
        System.out.println("\n--- CREATING NEW CUSTOMER ---");
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter shipping address: ");
        String address = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();

        Customer newCustomer = new Customer(name, address, phone);
        customers.add(newCustomer);
        System.out.println("==> New customer '" + name + "' has been added.");
        return newCustomer;
    }

    private Order createNewOrder(Customer customer) {
        String orderId = "ORD" + (int)(Math.random() * 1000);
        Order newOrder = new Order(orderId, customer);
        System.out.println("\nCreating new order (" + orderId + ") for customer: " + customer.getName());
        return editOrder(newOrder, true);
    }

    private Order editOrder(Order orderToEdit, boolean isNewOrder) {
        while (true) {
            System.out.println("\n--- ORDER " + (isNewOrder ? "CREATION" : "EDITING") + " MENU ---");
            System.out.println("Editing Order: " + orderToEdit.getOrderId());
            System.out.println("1. Add a book");
            System.out.println("2. Remove a book");
            System.out.println("3. View current order");
            System.out.println("0. Finish");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addBookToOrder(orderToEdit);
                    break;
                case "2":
                    removeBookFromOrder(orderToEdit);
                    break;
                case "3":
                    viewCurrentOrder(orderToEdit);
                    break;
                case "0":
                    System.out.println("==> Finished editing order " + orderToEdit.getOrderId());
                    return orderToEdit;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addBookToOrder(Order order) {
        System.out.println("\n--- PLEASE SELECT A BOOK TO ADD ---");
        for (int i = 0; i < availableBooks.size(); i++) {
            Book book = availableBooks.get(i);
            System.out.println((i + 1) + ". " + book.getTitle() + " - by " + book.getAuthor() + " (In Stock: " + book.getQuantity() + ")");
        }
        System.out.println("0. Back to order menu");
        System.out.print("Enter book number: ");

        try {
            int bookChoice = Integer.parseInt(scanner.nextLine());
            if (bookChoice == 0) return;

            if (bookChoice > 0 && bookChoice <= availableBooks.size()) {
                Book selectedBook = availableBooks.get(bookChoice - 1);

                int quantityInCart = 0;
                for (int i = 0; i < order.getItems().size(); i++) {
                    if (order.getItems().get(i).getBook().getTitle().equalsIgnoreCase(selectedBook.getTitle())) {
                        quantityInCart = order.getItems().get(i).getQuantity();
                        break;
                    }
                }
                int effectiveStock = selectedBook.getQuantity() - quantityInCart;

                if (effectiveStock <= 0) {
                    System.out.println("Sorry, '" + selectedBook.getTitle() + "' is out of stock or you have added all available stock to your cart.");
                    return;
                }

                while (true) {
                    System.out.print("Enter quantity (available: " + effectiveStock + "): ");
                    try {
                        int quantityChoice = Integer.parseInt(scanner.nextLine());
                        if (quantityChoice > 0 && quantityChoice <= effectiveStock) {
                            OrderItem newItem = new OrderItem(selectedBook, quantityChoice);
                            order.addItem(newItem);
                            System.out.println("-> Added " + quantityChoice + " of '" + selectedBook.getTitle() + "' to the order.");
                            break;
                        } else {
                            System.out.println("Invalid quantity. Please enter a number between 1 and " + effectiveStock + ".");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                }
            } else {
                System.out.println("Invalid book number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    private void removeBookFromOrder(Order order) {
        if (order.getItems().isEmpty()) {
            System.out.println("Order is currently empty. Nothing to remove.");
            return;
        }
        System.out.println("\n--- PLEASE SELECT A BOOK TO REMOVE ---");
        ArrayListADT<OrderItem> itemsInOrder = order.getItems();
        for (int i = 0; i < itemsInOrder.size(); i++) {
            System.out.println((i + 1) + ". " + itemsInOrder.get(i).toString());
        }
        System.out.println("0. Back to order menu");
        System.out.print("Enter number of the item to remove: ");
        try {
            int removeChoice = Integer.parseInt(scanner.nextLine());
            if (removeChoice > 0 && removeChoice <= itemsInOrder.size()) {
                OrderItem removedItem = itemsInOrder.remove(removeChoice - 1);
                System.out.println("-> Removed '" + removedItem.getBook().getTitle() + "' from the order.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    private void viewCurrentOrder(Order order) {
        System.out.println("\n--- CURRENT ORDER DETAILS ---");
        if (order.getItems().isEmpty()) {
            System.out.println("The order is empty.");
        } else {
            printOrderItems(order);
        }
    }

    private void printOrderItems(Order order) {
        ArrayListADT<OrderItem> items = order.getItems();
        for (int i = 0; i < items.size(); i++) {
            System.out.println("- " + items.get(i).toString());
        }
    }

    private void printOrderItemsFromList(ArrayListADT<OrderItem> items) {
        for (int i = 0; i < items.size(); i++) {
            System.out.println("- " + items.get(i).toString());
        }
    }

    private Book findBookByTitle(String title) {
        for(int i = 0; i < availableBooks.size(); i++) {
            if(availableBooks.get(i).getTitle().equalsIgnoreCase(title)) {
                return availableBooks.get(i);
            }
        }
        return null;
    }

    private void returnStock(Order order) {
        for (int i = 0; i < order.getItems().size(); i++) {
            OrderItem item = order.getItems().get(i);
            Book bookInStore = findBookByTitle(item.getBook().getTitle());
            if (bookInStore != null) {
                bookInStore.setQuantity(bookInStore.getQuantity() + item.getQuantity());
            }
        }
    }

    private void deductStock(Order order) {
        for (int i = 0; i < order.getItems().size(); i++) {
            OrderItem item = order.getItems().get(i);
            Book bookInStore = findBookByTitle(item.getBook().getTitle());
            if (bookInStore != null) {
                bookInStore.setQuantity(bookInStore.getQuantity() - item.getQuantity());
            }
        }
    }

    private ArrayListADT<Order> queueToArrayList() {
        ArrayListADT<Order> list = new ArrayListADT<>();
        LinkedQueueADT<Order> tempQueue = new LinkedQueueADT<>();
        while (!orderQueue.isEmpty()) {
            Order order = orderQueue.poll();
            list.add(order);
            tempQueue.offer(order);
        }
        while(!tempQueue.isEmpty()){
            orderQueue.offer(tempQueue.poll());
        }
        return list;
    }

    private void arrayListToQueue(ArrayListADT<Order> list) {
        while(!orderQueue.isEmpty()) {
            orderQueue.poll();
        }
        for (int i = 0; i < list.size(); i++) {
            orderQueue.offer(list.get(i));
        }
    }

    private void viewInventory() {
        System.out.println("\n--- CURRENT INVENTORY STATUS ---");
        int totalStock = 0;
        for (int i = 0; i < availableBooks.size(); i++) {
            Book book = availableBooks.get(i);
            System.out.println("- " + book.getTitle() + " by " + book.getAuthor() + " (In Stock: " + book.getQuantity() + ")");
            totalStock += book.getQuantity();
        }
        System.out.println("-----------------------------------------------------");
        System.out.println("Total unique titles: " + availableBooks.size());
        System.out.println("Total number of books in stock: " + totalStock);
        System.out.println("-----------------------------------------------------");
    }


    private void loadDefaultCustomers() {
        customers.add(new Customer("John Smith", "123 Maple Street", "555-0101"));
        customers.add(new Customer("Jane Doe", "456 Oak Avenue", "555-0102"));
    }

    private void loadDefaultBooks() {
        availableBooks.add(new Book("To Kill a Mockingbird", "Harper Lee", 15));
        availableBooks.add(new Book("1984", "George Orwell", 22));
        availableBooks.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", 18));
        availableBooks.add(new Book("Dune", "Frank Herbert", 30));
        availableBooks.add(new Book("The Hobbit", "J.R.R. Tolkien", 40));
        availableBooks.add(new Book("Pride and Prejudice", "Jane Austen", 25));
        availableBooks.add(new Book("The Catcher in the Rye", "J.D. Salinger", 12));
        availableBooks.add(new Book("Fahrenheit 451", "Ray Bradbury", 20));
        availableBooks.add(new Book("Brave New World", "Aldous Huxley", 17));
        availableBooks.add(new Book("Moby Dick", "Herman Melville", 10));
        availableBooks.add(new Book("War and Peace", "Leo Tolstoy", 8));
        availableBooks.add(new Book("The Lord of the Rings", "J.R.R. Tolkien", 35));
        availableBooks.add(new Book("The Chronicles of Narnia", "C.S. Lewis", 28));
        availableBooks.add(new Book("Animal Farm", "George Orwell", 24));
        availableBooks.add(new Book("Jane Eyre", "Charlotte Brontë", 19));
        availableBooks.add(new Book("The Odyssey", "Homer", 14));
        availableBooks.add(new Book("The Divine Comedy", "Dante Alighieri", 11));
        availableBooks.add(new Book("Frankenstein", "Mary Shelley", 21));
        availableBooks.add(new Book("Dracula", "Bram Stoker", 23));
        availableBooks.add(new Book("The Adventures of Huckleberry Finn", "Mark Twain", 16));
        availableBooks.add(new Book("Don Quixote", "Miguel de Cervantes", 9));
        availableBooks.add(new Book("One Hundred Years of Solitude", "Gabriel Garcia Marquez", 26));
        availableBooks.add(new Book("The Brothers Karamazov", "Fyodor Dostoevsky", 13));
        availableBooks.add(new Book("Crime and Punishment", "Fyodor Dostoevsky", 15));
        availableBooks.add(new Book("Wuthering Heights", "Emily Brontë", 18));
        availableBooks.add(new Book("The Picture of Dorian Gray", "Oscar Wilde", 22));
        availableBooks.add(new Book("A Tale of Two Cities", "Charles Dickens", 27));
        availableBooks.add(new Book("Les Misérables", "Victor Hugo", 7));
        availableBooks.add(new Book("The Stranger", "Albert Camus", 19));
        availableBooks.add(new Book("The Sun Also Rises", "Ernest Hemingway", 14));
        availableBooks.add(new Book("Heart of Darkness", "Joseph Conrad", 12));
        availableBooks.add(new Book("The Sound and the Fury", "William Faulkner", 10));
        availableBooks.add(new Book("Ulysses", "James Joyce", 5));
        availableBooks.add(new Book("The Grapes of Wrath", "John Steinbeck", 20));
        availableBooks.add(new Book("Slaughterhouse-Five", "Kurt Vonnegut", 25));
        availableBooks.add(new Book("On the Road", "Jack Kerouac", 18));
        availableBooks.add(new Book("The Handmaid's Tale", "Margaret Atwood", 30));
        availableBooks.add(new Book("Beloved", "Toni Morrison", 17));
        availableBooks.add(new Book("Things Fall Apart", "Chinua Achebe", 21));
        availableBooks.add(new Book("The Color Purple", "Alice Walker", 16));
        availableBooks.add(new Book("Invisible Man", "Ralph Ellison", 13));
        availableBooks.add(new Book("The Old Man and the Sea", "Ernest Hemingway", 28));
        availableBooks.add(new Book("A Clockwork Orange", "Anthony Burgess", 24));
        availableBooks.add(new Book("Lord of the Flies", "William Golding", 29));
        availableBooks.add(new Book("The Bell Jar", "Sylvia Plath", 11));
        availableBooks.add(new Book("Catch-22", "Joseph Heller", 19));
        availableBooks.add(new Book("The Alchemist", "Paulo Coelho", 33));
        availableBooks.add(new Book("Sapiens: A Brief History of Humankind", "Yuval Noah Harari", 45));
        availableBooks.add(new Book("Educated", "Tara Westover", 38));
        availableBooks.add(new Book("The Road", "Cormac McCarthy", 26));

        System.out.println("-----------------------------------------------------");
        System.out.println("Loaded " + availableBooks.size() + " book titles.");
        System.out.println("-----------------------------------------------------");
    }

    public static void main(String[] args) {
        BookstoreSystem mySystem = new BookstoreSystem();
        mySystem.run();
    }
}
