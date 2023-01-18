package it.unicam.cs.asdl2223.mp2;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Class that provides an implementation of a "dynamic" min-priority queue based
 * on a ternary heap. "Dynamic" means that the priority of an element already
 * present in the queue may be decreased, so possibly this element may become
 * the new minimum element. The elements that can be inserted may be of any
 * class implementing the interface <code>PriorityQueueElement</code>. This
 * min-priority queue does not have capacity restrictions, i.e., it is always
 * possible to insert new elements and the number of elements is unbound.
 * Duplicated elements are permitted while <code>null</code> elements are not
 * permitted.
 *
 * @author Template: Luca Tesei, Implementation: Christian Bonsignore - christian.bonsignore@studenti.unicam.it
 */
public class TernaryHeapMinPriorityQueue {

    /*
     * ArrayList for representing the ternary heap. Use all positions, including
     * position 0 (the JUnit tests will assume so). You have to adapt
     * child/parent indexing formulas consequently.
     */
    private ArrayList<PriorityQueueElement> heap;

    /**
     * Create an empty queue.
     */
    public TernaryHeapMinPriorityQueue() {
        this.heap = new ArrayList<PriorityQueueElement>();
    }

    /**
     * Return the current size of this queue.
     *
     * @return the number of elements currently in this queue.
     */
    public int size() {
        return this.heap.size();
    }

    /**
     * Add an element to this min-priority queue. The current priority
     * associated with the element will be used to place it in the correct
     * position in the ternary heap. The handle of the element will also be set
     * accordingly.
     *
     * @param element the new element to add
     * @throws NullPointerException if the element passed is null
     */
    public void insert(PriorityQueueElement element) {
        if (element == null) {
            throw new NullPointerException("The element passed is null.");
        }
        int i = heap.size();
        element.setHandle(i);
        this.heap.add(element);

        while (i >= 0 && this.heap.get(i).getPriority() < this.heap.get(parentIndex(i)).getPriority()) {
            swap(i, parentIndex(i));
            i = parentIndex(i);
        }
    }

    /**
     * Returns the current minimum element of this min-priority queue without
     * extracting it. This operation does not affect the ternary heap.
     *
     * @return the current minimum element of this min-priority queue
     * @throws NoSuchElementException if this min-priority queue is empty
     */
    public PriorityQueueElement minimum() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("This min-priority queue is empty.");
        }
        //Il minimo è sempre la radice dell'albero, quindi 0
        return this.heap.get(0);
    }

    /**
     * Extract the current minimum element from this min-priority queue. The
     * ternary heap will be updated accordingly.
     *
     * @return the current minimum element
     * @throws NoSuchElementException if this min-priority queue is empty
     */
    public PriorityQueueElement extractMinimum() {
        if (this.isEmpty()) throw new NoSuchElementException("This min-priority queue is empty.");

        PriorityQueueElement min = this.minimum();

        this.swap(0, this.size() - 1);
        this.heap.get(this.size() - 1).setHandle(-1);
        this.heap.remove(this.size() - 1);

        //dopo l'estrazione di un elemento bisogna sempre far sì che la proprietà del min-heap sia di nuovo verificata
        this.minHeapify(0);

        return min;
    }

    /**
     * Decrease the priority associated to an element of this min-priority
     * queue. The position of the element in the ternary heap must be changed
     * accordingly. The changed element may become the minimum element. The
     * handle of the element will also be changed accordingly.
     *
     * @param element     the element whose priority will be decreased, it
     *                    must currently be inside this min-priority queue
     * @param newPriority the new priority to assign to the element
     * @throws NoSuchElementException   if the element is not currently
     *                                  present in this min-priority queue
     * @throws IllegalArgumentException if the specified newPriority is not
     *                                  strictly less than the current
     *                                  priority of the element
     */
    public void decreasePriority(PriorityQueueElement element, double newPriority) {
        if (element.getHandle() == -1 || element.getPriority() != heap.get(element.getHandle()).getPriority()) {
            throw new NoSuchElementException("The element is not currently present in this min-priority queue.");
        }

        int index = this.heap.indexOf(element);
        if (this.heap.get(index).getPriority() <= newPriority) {
            throw new IllegalArgumentException("The specified newPriority is not strictly less than the current priority of the element.");
        }

        element.setPriority(newPriority);

        //reinstaurare le proprietà del min-heap
        while (index >= 0 && this.heap.get(index).getPriority() < this.heap.get(parentIndex(index)).getPriority()) {
            swap(index, parentIndex(index));
            index = parentIndex(index);
        }
    }

    /**
     * Erase all the elements from this min-priority queue. After this operation
     * this min-priority queue is empty.
     */
    public void clear() {
        this.heap.clear();
    }

    private void minHeapify(int i) {
        int min = i;
        //se non c'è il figlio sinistro non ce ne sono altri e non ha senso continuare
        if (!hasLeft(i)) return;

        int l = leftIndex(i);
        int m = middleIndex(i);
        int r = rightIndex(i);

        //devo trovare il figlio minore con cui sostituire
        if (this.heap.get(min).getPriority() > this.heap.get(l).getPriority()) min = l;

        if (hasMiddle(i) && this.heap.get(min).getPriority() > this.heap.get(m).getPriority()) min = m;

        if (hasRight(i) && this.heap.get(min).getPriority() > this.heap.get(r).getPriority()) min = r;

        //scambio e riapplico per controllare i figli
        if (min != i) {
            swap(min, i);
            minHeapify(min);
        }
    }

    private void swap(int first, int second) {
        PriorityQueueElement firstElem = this.heap.get(first);
        PriorityQueueElement secElem = this.heap.get(second);
        this.heap.set(first, secElem);
        this.heap.set(second, firstElem);

        firstElem.setHandle(second);
        secElem.setHandle(first);
    }

    private int leftIndex(int i) {
        return 3 * i + 1;
    }

    private int middleIndex(int i) {
        return 3 * i + 2;
    }

    private int rightIndex(int i) {
        return 3 * i + 3;
    }

    private int parentIndex(int i) {
        return (i - 1) / 3;
    }

    private boolean hasLeft(int i) {
        return leftIndex(i) < this.size();
    }

    private boolean hasMiddle(int i) {
        return middleIndex(i) < this.size();
    }

    private boolean hasRight(int i) {
        return rightIndex(i) < this.size();
    }

    public boolean isEmpty() {
        return this.heap.isEmpty();
    }

    /*
     * This method is only for JUnit testing purposes.
     */
    protected ArrayList<PriorityQueueElement> getTernaryHeap() {
        return this.heap;
    }
}
