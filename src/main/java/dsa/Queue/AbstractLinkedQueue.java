package dsa.Queue;

public interface AbstractLinkedQueue<E> {
    void offer(E element);
    E poll();
    E peek();
    int size();
    boolean isEmpty();
}
