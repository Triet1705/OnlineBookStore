package model;

import dsa.ArrayList.ArrayListADT;

public class Order {
    private String orderId;
    private Customer customer;
    private ArrayListADT<OrderItem> items;

    public Order(String orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = new ArrayListADT<>();
    }

    public void addItem(OrderItem newItem) {
        for (int i = 0; i < items.size(); i++) {
            OrderItem currentItem = items.get(i);
            if (currentItem.getBook().getTitle().equals(newItem.getBook().getTitle())) {
                int newQuantity = currentItem.getQuantity() + newItem.getQuantity();
                currentItem.setQuantity(newQuantity);
                return;
            }
        }
        this.items.add(newItem);
    }

    public ArrayListADT<OrderItem> getItems() {
        return items;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getOrderId() {
        return orderId;
    }

    @Override
    public String toString() {
        return "Order{orderId='" + orderId + "', customer=" + customer.getName() + ", items=" + items.size() + " types}";
    }
}
