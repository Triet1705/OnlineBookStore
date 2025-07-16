package model;

import dsa.ArrayList.ArrayListADT;

public class Order {
    private String orderId;
    private Customer customer;
    private ArrayListADT<Book> books;

    public Order(String orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
        this.books = new ArrayListADT<>();
    }
    public void addBook(Book book) {
        this.books.add(book);
    }
    public ArrayListADT<Book> getBooks() {
        return books;
    }
    public Customer getCustomer() {
        return customer;
    }
    public String getOrderId() {return orderId;}
    @Override
    public String toString() {
        return "Order{orderId='" + orderId + "', customer=" + customer + ", books=" + books.size() + " items}";
    }
}
