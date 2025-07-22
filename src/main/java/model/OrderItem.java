package model;

public class OrderItem implements Comparable<OrderItem> {
    private Book book;
    private int quantity;

    public OrderItem(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Book getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return this.book.getTitle() + " by " + this.book.getAuthor() + " (Quantity: " + this.quantity + ")";
    }

    @Override
    public int compareTo(OrderItem other) {
        return this.getBook().getTitle().compareTo(other.getBook().getTitle());
    }
}
