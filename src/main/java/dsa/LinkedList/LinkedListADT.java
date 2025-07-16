package dsa.LinkedList;


public class LinkedListADT<E> implements AbstractLinkedList<E> {
    private static class Node<E>{
        private E element;
        private Node<E> next;
        private Node<E> prev;

        public Node(E element) {
            this.element = element;
            this.next = null;
            this.prev = null;
        }
    }
    private Node<E> head;
    private Node<E> tail;
    private int size;

    public LinkedListADT() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }
    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element);
        if (isEmpty()) {
            this.head = newNode;
            this.tail = newNode;
        } else {
            newNode.next = this.head;
            this.head.prev = newNode;
            this.head = newNode;
        }
        this.size++;
    }

    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element);
        if (isEmpty()) {
            this.head = newNode;
            this.tail = newNode;
        } else {
            this.tail.next = newNode;
            newNode.prev = this.tail;
            this.tail = newNode;
        }
        this.size++;
    }

    @Override
    public E removeFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        E oldElement = this.head.element;
        if (this.head == this.tail) {
            this.head = null;
            this.tail = null;
        } else {
            this.head = this.head.next;
            this.head.prev = null;
        }
        this.size--;
        return oldElement;
    }

    @Override
    public E removeLast() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        E oldElement = this.tail.element;
        if (this.head == this.tail) {
            this.head = null;
            this.tail = null;
        } else {
            this.tail = this.tail.prev;
            this.tail.next = null;
        }
        this.size--;
        return oldElement;
    }

    @Override
    public E getFirst() {
        if (isEmpty()) return null;
        return this.head.element;
    }

    @Override
    public E getLast() {
        if (isEmpty()) return null;
        return this.tail.element;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
}
