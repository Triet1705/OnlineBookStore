package dsa;

import model.Book;
import dsa.ArrayList.ArrayListADT;
public class Sorting {
    public static void insertionSort(ArrayListADT<Book> books) {
        int n = books.size();
        for (int i = 1; i < n; i++) {
            Book key = books.get(i);
            int j = i - 1;

            while (j >= 0 && books.get(j).compareTo(key) > 0) {
                books.set(j + 1, books.get(j));
                j = j -1;
            }
            books.set(j + 1, key);
        }
    }
}
