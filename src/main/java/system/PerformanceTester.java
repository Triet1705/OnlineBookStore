package system;

import dsa.ArrayList.ArrayListADT;
import dsa.Sorting;
import model.Book;
import model.OrderItem;
import java.util.Random;

public class PerformanceTester {

    private static ArrayListADT<OrderItem> generateRandomOrderItemList(int size) {
        ArrayListADT<OrderItem> list = new ArrayListADT<>();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            Book book = new Book("Book " + random.nextInt(size * 10), "Author " + i, 100);
            OrderItem item = new OrderItem(book, random.nextInt(5) + 1);
            list.add(item);
        }
        return list;
    }

    private static ArrayListADT<OrderItem> cloneList(ArrayListADT<OrderItem> original) {
        ArrayListADT<OrderItem> copy = new ArrayListADT<>();
        for (int i = 0; i < original.size(); i++) {
            copy.add(original.get(i));
        }
        return copy;
    }

    public static void main(String[] args) {
        int[] sizes = {10, 100, 1000, 5000, 10000};

        System.out.println("--- Sorting Algorithm Performance Test ---");
        System.out.printf("%-15s | %-25s | %-25s%n", "Size (n)", "Insertion Sort (ns)", "Merge Sort (ns)");
        System.out.println("---------------------------------------------------------------------");

        for (int size : sizes) {
            ArrayListADT<OrderItem> originalList = generateRandomOrderItemList(size);

            ArrayListADT<OrderItem> listForInsertionSort = cloneList(originalList);
            long startTimeInsertion = System.nanoTime();
            Sorting.insertionSort(listForInsertionSort);
            long endTimeInsertion = System.nanoTime();
            long durationInsertion = endTimeInsertion - startTimeInsertion;

            ArrayListADT<OrderItem> listForMergeSort = cloneList(originalList);
            long startTimeMerge = System.nanoTime();
            Sorting.mergeSort(listForMergeSort);
            long endTimeMerge = System.nanoTime();
            long durationMerge = endTimeMerge - startTimeMerge;

            System.out.printf("%-15d | %-25d | %-25d%n", size, durationInsertion, durationMerge);
        }

        System.out.println("\n\n");


    }
}