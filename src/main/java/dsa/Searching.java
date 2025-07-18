package dsa;

import model.Book;
import dsa.ArrayList.ArrayListADT;

public class Searching {

    public static ArrayListADT<Book> binarySearchByTitle(ArrayListADT<Book> sortedBooks, String targetTitle) {
        ArrayListADT<Book> results = new ArrayListADT<>();
        int left = 0;
        int right = sortedBooks.size() - 1;
        int initialMatchIndex = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            Book midBook = sortedBooks.get(mid);
            String midTitle = midBook.getTitle();
            int comparison = midTitle.compareTo(targetTitle);

            if (comparison == 0) {
                initialMatchIndex = mid;
                break;
            }
            if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        if (initialMatchIndex != -1) {
            results.add(sortedBooks.get(initialMatchIndex));
            int leftProbe = initialMatchIndex - 1;
            while (leftProbe >= 0 && sortedBooks.get(leftProbe).getTitle().equals(targetTitle)) {
                results.add(sortedBooks.get(leftProbe));
                leftProbe--;
            }

            int rightProbe = initialMatchIndex + 1;
            while (rightProbe < sortedBooks.size() && sortedBooks.get(rightProbe).getTitle().equals(targetTitle)) {
                results.add(sortedBooks.get(rightProbe));
                rightProbe++;
            }
        }
        return results;
    }

    public static ArrayListADT<Book> linearSearchFlexible(ArrayListADT<Book> books, String searchTerm) {
        ArrayListADT<Book> results = new ArrayListADT<>();
        String lowerCaseSearchTerm = searchTerm.toLowerCase();

        for (int i = 0; i < books.size(); i++) {
            Book currentBook = books.get(i);
            String lowerCaseTitle = currentBook.getTitle().toLowerCase();
            if (lowerCaseTitle.contains(lowerCaseSearchTerm)) {
                results.add(currentBook);
            }
        }
        return results;
    }
}