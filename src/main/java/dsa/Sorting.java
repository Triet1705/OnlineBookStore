package dsa;

import model.OrderItem;
import dsa.ArrayList.ArrayListADT;
public class Sorting {
    public static void insertionSort(ArrayListADT<OrderItem> items) {
        int n = items.size();
        for (int i = 1; i < n; i++) {
            OrderItem key = items.get(i);
            int j = i - 1;

            while (j >= 0 && items.get(j).compareTo(key) > 0) {
                items.set(j + 1, items.get(j));
                j = j - 1;
            }
            items.set(j + 1, key);
        }
    }
    public static void mergeSort(ArrayListADT<OrderItem> items) {
        int n = items.size();
        if (n < 2) {
            return;
        }

        int mid = n / 2;
        ArrayListADT<OrderItem> leftHalf = new ArrayListADT<>();
        for (int i = 0; i < mid; i++) {
            leftHalf.add(items.get(i));
        }
        ArrayListADT<OrderItem> rightHalf = new ArrayListADT<>();
        for (int i = mid; i < n; i++) {
            rightHalf.add(items.get(i));
        }

        mergeSort(leftHalf);
        mergeSort(rightHalf);
        merge(items, leftHalf, rightHalf);
    }

    private static void merge(ArrayListADT<OrderItem> originalList, ArrayListADT<OrderItem> leftHalf, ArrayListADT<OrderItem> rightHalf) {
        int leftSize = leftHalf.size();
        int rightSize = rightHalf.size();
        int i = 0, j = 0, k = 0;

        while (i < leftSize && j < rightSize) {
            if (leftHalf.get(i).compareTo(rightHalf.get(j)) <= 0) {
                originalList.set(k++, leftHalf.get(i++));
            } else {
                originalList.set(k++, rightHalf.get(j++));
            }
        }

        while (i < leftSize) {
            originalList.set(k++, leftHalf.get(i++));
        }
        while (j < rightSize) {
            originalList.set(k++, rightHalf.get(j++));
        }
    }
}
