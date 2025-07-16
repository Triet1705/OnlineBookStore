package dsa;

import model.Book;
import dsa.ArrayList.ArrayListADT;

public class Searching {

    public static Book binarySearchByTitle(ArrayListADT<Book> sortedBooks, String targetTitle) {
        int left = 0;
        int right = sortedBooks.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            Book midBook = sortedBooks.get(mid);
            String midTitle = midBook.getTitle();
            int comparison = midTitle.compareTo(targetTitle);

            if (comparison == 0) {
                return midBook;
            }

            if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return null;
    }
}