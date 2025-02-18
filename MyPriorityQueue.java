import java.util.Comparator;
import java.util.ArrayList;

public class MyPriorityQueue<T> {
    private ArrayList<T> heap;
    private final Comparator<T> comparator;

    public MyPriorityQueue(Comparator<T> comparator) {
        this.heap = new ArrayList<>();
        this.comparator = comparator;
    }
    // These methods utilize the structural properties of a binary heap to calculate the indices of parent and child nodes.
    private int parent(int i) { return (i - 1) / 2; }
    private int leftChild(int i) { return 2 * i + 1; }
    private int rightChild(int i) { return 2 * i + 2; }

    // Add an element to the queue
    public void add(T element) {
        heap.add(element);
        heapifyUp(heap.size() - 1);
    }

    // Retrieve and remove the top element
    public T poll() {
        if (heap.isEmpty()) return null;
        T top = heap.get(0);
        T last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            heapifyDown(0);
        }
        return top;
    }


    // Heapify up to restore the heap property
    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = parent(index);
            if (comparator.compare(heap.get(index), heap.get(parentIndex)) < 0) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    // Heapify down to restore the heap property
    private void heapifyDown(int index) {
        int size = heap.size();
        while (true) {
            int left = leftChild(index);
            int right = rightChild(index);
            int smallest = index;

            if (left < size && comparator.compare(heap.get(left), heap.get(smallest)) < 0) {
                smallest = left;
            }
            if (right < size && comparator.compare(heap.get(right), heap.get(smallest)) < 0) {
                smallest = right;
            }
            if (smallest != index) {
                swap(index, smallest);
                index = smallest;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }
}
