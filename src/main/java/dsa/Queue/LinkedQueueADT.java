package dsa.Queue;
public class LinkedQueueADT<E> implements AbstractLinkedQueue<E> {
    private static class Node<E> {
        private E element;
        private Node<E> next;

        public Node(E element) {
            this.element = element;
            this.next = null;
        }
    }

    // Thuộc tính của Queue
    private Node<E> head;
    private Node<E> tail;
    private int size;

    public LinkedQueueADT() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public void offer(E element) {
        Node<E> newNode = new Node<>(element);

        if (isEmpty()) {
            this.head = newNode;
            this.tail = newNode;
        } else {
            this.tail.next = newNode;
            this.tail = newNode;
        }

        this.size++;
    }

    @Override
    public E poll() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }

        E oldElement = this.head.element;

        if (this.head == this.tail) {
            this.head = null;
            this.tail = null;
        } else {
            this.head = this.head.next;
        }

        this.size--;
        return oldElement;
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return this.head.element;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Queue[");
        Node<E> tempNode = this.head;
        while (tempNode != null) {
            result.append(tempNode.element);
            if (tempNode.next != null) {
                result.append(" <- ");
            }
            tempNode = tempNode.next;
        }
        result.append("]");
        return result.toString();
    }
}