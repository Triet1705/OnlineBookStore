package model;

import dsa.ArrayList.ArrayListADT;

public class Order {
    private String orderId;
    private Customer customer;
    private ArrayListADT<OrderItem> items;
    private OrderStatus status;

    public Order(String orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = new ArrayListADT<>();
        this.status = OrderStatus.WAITING_FOR_PROCESSING;
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
    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{orderId='" + orderId + "', customer=" + customer.getName() + ", items=" + items.size() + " types, status=" + status + "}";
    }
}
