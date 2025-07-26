package dsa;

import model.Book;
import model.OrderItem;
import dsa.ArrayList.ArrayListADT;

public class Searching {

    public static ArrayListADT<OrderItem> binarySearchByTitle(ArrayListADT<OrderItem> sortedItems, String targetTitle) {
        ArrayListADT<OrderItem> results = new ArrayListADT<>();
        int left = 0;
        int right = sortedItems.size() - 1;
        int initialMatchIndex = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            OrderItem midItem = sortedItems.get(mid);
            Book midBook = midItem.getBook();
            int comparison = midBook.getTitle().compareToIgnoreCase(targetTitle);

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
            results.add(sortedItems.get(initialMatchIndex));
            int leftProbe = initialMatchIndex - 1;
            while (leftProbe >= 0 && sortedItems.get(leftProbe).getBook().getTitle().equalsIgnoreCase(targetTitle)) {
                results.add(sortedItems.get(leftProbe));
                leftProbe--;
            }
            int rightProbe = initialMatchIndex + 1;
            while (rightProbe < sortedItems.size() && sortedItems.get(rightProbe).getBook().getTitle().equalsIgnoreCase(targetTitle)) {
                results.add(sortedItems.get(rightProbe));
                rightProbe++;
            }
        }
        return results;
    }

    public static ArrayListADT<OrderItem> linearSearchFlexible(ArrayListADT<OrderItem> items, String searchTerm) {
        ArrayListADT<OrderItem> results = new ArrayListADT<>();
        String lowerCaseSearchTerm = searchTerm.toLowerCase();

        for (int i = 0; i < items.size(); i++) {
            OrderItem currentItem = items.get(i);
            Book currentBook = currentItem.getBook();
            String lowerCaseTitle = currentBook.getTitle().toLowerCase();
            if (lowerCaseTitle.contains(lowerCaseSearchTerm)) {
                results.add(currentItem);
            }
        }
        return results;
    }
}
