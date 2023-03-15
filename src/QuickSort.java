import java.util.ArrayList;

public class QuickSort {

    public interface Comparable {
        int compareTo(Comparable compare);
    }

    public static <T extends Comparable> void sort(ArrayList<T> sortArray) {
        sort(sortArray, 0, sortArray.size() - 1);
    }

    public static <T extends Comparable> void sort(ArrayList<T> sortArray, int left, int right) {
        if (left >= right) return;

        Comparable pivot = sortArray.get((left + right) / 2);
        int index = partition(sortArray, left, right, pivot);
        sort(sortArray, left, index - 1);
        sort(sortArray, index, right);
    }

    private static <T extends Comparable> int partition(ArrayList<T> sortArray, int left, int right, Comparable pivot) {
        while (left <= right) {
            while (sortArray.get(left).compareTo(pivot) < 0) {
                left++;
            }
            while (sortArray.get(right).compareTo(pivot) > 0) {
                right--;
            }

            if (left <= right) {
                swap(sortArray, left, right);
                left++;
                right--;
            }
        }
        return left;
    }

    private static <T extends Comparable> void swap(ArrayList<T> sortArray, int left, int right) {
        sortArray.set(left, sortArray.set(right, sortArray.get(left)));
    }
}
