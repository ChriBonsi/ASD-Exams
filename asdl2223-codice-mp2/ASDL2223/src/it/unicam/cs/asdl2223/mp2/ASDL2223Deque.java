/**
 * 
 */
package it.unicam.cs.asdl2223.mp2;

import java.util.*;

/**
 * Implementation of the Java SE Double-ended Queue (Deque) interface
 * (<code>java.util.Deque</code>) based on a double linked list. This deque does
 * not have capacity restrictions, i.e., it is always possible to insert new
 * elements and the number of elements is unbound. Duplicated elements are
 * permitted while <code>null</code> elements are not permitted. Being
 * <code>Deque</code> a sub-interface of
 * <code>Queue<code>, this class can be used also as an implementation of a <code>Queue</code>
 * and of a <code>Stack</code>.
 * <p>
 * The following operations are not supported:
 * <ul>
 * <li><code>public <T> T[] toArray(T[] a)</code></li>
 * <li><code>public boolean removeAll(Collection<?> c)</code></li>
 * <li><code>public boolean retainAll(Collection<?> c)</code></li>
 * <li><code>public boolean removeFirstOccurrence(Object o)</code></li>
 * <li><code>public boolean removeLastOccurrence(Object o)</code></li>
 * </ul>
 *
 * @author Template: Luca Tesei, Implementation: Christian Bonsignore - christian.bonsignore@studenti.unicam.it
 */
public class ASDL2223Deque<E> implements Deque<E> {

    /*
     * Current number of elements in this deque
     */
    private int size;

    /*
     * Pointer to the first element of the double-linked list used to implement
     * this deque
     */
    private Node<E> first;

    /*
     * Pointer to the last element of the double-linked list used to implement
     * this deque
     */
    private Node<E> last;

    //aggiunto per tenere traccia del numero di modifiche
    private int modifications;

    /**
     * Constructs an empty deque.
     */
    public ASDL2223Deque() {
        size = 0;
        first = null;
        last = null;
        modifications = 0;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public Object[] toArray() {
        if (this == null) return null;
        if (this.size == 0) return new Object[0];

        int contPosition = 0;

        //scorro i vari elementi nel Deque e li aggiungo uno per uno a un array delle stesse dimensioni
        Object[] array = new Object[this.size];
        for (E e : this) {
            array[contPosition++] = e;
        }
        return array;
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("Null object passed.");
        }

        Iterator<?> cIterator = c.iterator();
        boolean flag;

        //uso un flag perché basta un elemento che non rispetti la
        //condizione e non ho bisogno di controllare tutti gli altri
        do {
            flag = this.contains(cIterator.next());
        } while (cIterator.hasNext() && flag);

        return flag;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null || c.contains(null)) {
            throw new NullPointerException("Null object passed.");
        }

        //uso toArray per non ricorrere all'iteratore, visto che usarlo
        //porterebbe a una ConcurrentModificationException
        E[] arr = (E[]) c.toArray();
        for (E e : arr) {
            offerLast(e);
        }
        return true;
    }

    @Override
    public void clear() {
        this.first = null;
        this.last = null;
        modifications++;
        this.size = 0;
    }

    @Override
    public boolean offerFirst(E e) {
        if (e == null) {
            throw new NullPointerException("The passed object is null.");
        }

        Node<E> toAdd = new Node<>(null, e, null);

        if (this.isEmpty()) {
            this.last = toAdd;
        } else {
            this.first.prev = toAdd;
            toAdd.next = this.first;
        }
        this.first = toAdd;
        this.size++;
        this.modifications++;

        return true;
    }

    @Override
    public boolean offerLast(E e) {
        if (e == null) {
            throw new NullPointerException("The passed object is null.");
        }

        Node<E> toAdd = new Node<>(null, e, null);

        if (this.isEmpty()) {
            this.first = toAdd;
        } else {
            this.last.next = toAdd;
            toAdd.prev = this.last;
        }

        this.last = toAdd;
        this.size++;
        this.modifications++;

        return true;
    }

    @Override
    public E pollFirst() {
        if (this.size == 0) return null;

        E toPoll = this.first.item;

        if (this.size == 1) {
            this.first = null;
            this.last = null;
        } else {
            Node<E> after = this.first.next;
            after.prev = null;
            first.next = null;
            first = after;
        }
        this.size--;
        this.modifications++;

        return toPoll;
    }

    @Override
    public E pollLast() {
        if (this.size == 0) return null;

        E toPoll = this.last.item;

        if (this.size == 1) {
            this.first = null;
            this.last = null;
        } else {
            Node<E> before = this.last.prev;
            before.next = null;
            last.prev = null;
            last = before;
        }
        this.size--;
        this.modifications++;

        return toPoll;
    }

    @Override
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException("The deque is empty.");
        return this.first.item;
    }

    @Override
    public E getLast() {
        if (size == 0) throw new NoSuchElementException("The deque is empty.");
        return this.last.item;
    }

    /*
     * Queue methods
     * Non commento i duplicati
     */
    @Override
    public boolean add(E e) {
        return offerLast(e);
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    /*
     * Stack methods
     */
    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException("The passed object is null.");
        }
        if (this.isEmpty()) return false;
        if (o.equals(this.first.item)) {
            removeFirst();
            return true;
        }
        if (o.equals(this.last.item)) {
            removeLast();
            return true;
        }

        Node<E> previous = this.first;
        Node<E> current = this.first.next;

        while (!(current.next == null)) {
            if (o.equals(current.item)) {
                //devo recidere entrambi i link all'elemento che sto rimuovendo
                previous.next = current.next;
                current.next.prev = previous;
                this.size--;
                this.modifications++;
                return true;
            }
            //continuo a scorrere in avanti
            previous = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) throw new NullPointerException("The passed object is null.");
        if (this.isEmpty()) return false;

        Iterator<E> iter = iterator();
        //anche qui uso un flag perché una volta trovato l'elemento non
        //ho bisogno di continuare a scorrere
        boolean flag = false;
        while (iter.hasNext() && !flag) {
            flag = iter.next().equals(o);
        }
        return flag;
    }

    @Override
    public int size() {
        return this.size;
    }

    /*
     * Duplicates
     */

    @Override
    public E removeFirst() {
        if (size == 0) throw new NoSuchElementException("The deque is empty.");
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (size == 0) throw new NoSuchElementException("The deque is empty.");
        return pollLast();
    }

    //Visto che, come da traccia, posso ignorare alcune eccezioni nell'implementazione, addFirst e addLast
    //sono uguali a offerFirst e offerLast

    @Override
    public void addFirst(E e) {
        offerFirst(e);
    }

    @Override
    public void addLast(E e) {
        offerLast(e);
    }

    @Override
    public E peekFirst() {
        if (size == 0) return null;
        return getFirst();
    }

    @Override
    public E peekLast() {
        if (size == 0) return null;
        return getLast();
    }


    /*
     * Unsupported operations;
     */

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("This class does not implement this service.");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("This class does not implement this service.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("This class does not implement this service.");
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException("This class does not implement this service.");
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException("This class does not implement this service.");
    }

    /**
     * Class for representing the nodes of the double-linked list used to
     * implement this deque. The class and its members/methods are protected
     * instead of private only for JUnit testing purposes.
     */
    protected static class Node<E> {
        protected E item;

        protected Node<E> next;

        protected Node<E> prev;

        protected Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    /**
     * Class for implementing an iterator for this deque. The iterator is
     * fail-fast: it detects if during the iteration a modification to the
     * original deque was done and, if so, it launches a
     * <code>ConcurrentModificationException</code> as soon as a call to the
     * method <code>next()</code> is done.
     */
    private class Itr implements Iterator<E> {
        private Node<E> lastReturned;
        private int expectedModifications;

        Itr() {
            lastReturned = null;
            this.expectedModifications = ASDL2223Deque.this.modifications;
        }

        public boolean hasNext() {
            //size == 0
            if (ASDL2223Deque.this.isEmpty()) return false;
            //mai fatto un .next
            if (this.lastReturned == null) return ASDL2223Deque.this.getFirst() != null;
            else
                //almeno un .next è stato fatto
                return lastReturned.next != null;
        }

        public E next() {
            //Controllo la concorrenza
            if (this.expectedModifications != ASDL2223Deque.this.modifications) {
                throw new ConcurrentModificationException("The deque was modified during the iteration.");
            }

            //Controllo che ci sia un elemento successivo
            if (!(this.hasNext())) {
                throw new NoSuchElementException("There is not a next element to retrieve.");
            }

            //Se sono all'inizio ritorno il primo, altrimenti il successivo dell'ultimo ritornato
            if (lastReturned == null) {
                this.lastReturned = ASDL2223Deque.this.first;
            } else {
                this.lastReturned = this.lastReturned.next;
            }
            return this.lastReturned.item;
        }
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new DescItr();
    }

    /**
     * Class for implementing a descending iterator for this deque. The iterator
     * is fail-fast: it detects if during the iteration a modification to the
     * original deque was done and, if so, it launches a
     * <code>ConcurrentModificationException</code> as soon as a call to the
     * method <code>next()</code> is done.
     */
    private class DescItr implements Iterator<E> {
        private Node<E> lastReturned;
        private int expectedModifications;

        DescItr() {
            lastReturned = null;
            this.expectedModifications = ASDL2223Deque.this.modifications;
        }

        public boolean hasNext() {
            //size == 0
            if (ASDL2223Deque.this.isEmpty()) return false;
            //mai fatto un .next
            if (this.lastReturned == null) return ASDL2223Deque.this.getLast() != null;
            else
                //almeno un .next è stato fatto
                return lastReturned.prev != null;
        }

        public E next() {
            //Controllo la concorrenza
            if (this.expectedModifications != ASDL2223Deque.this.modifications) {
                throw new ConcurrentModificationException("The deque was modified during the iteration.");
            }

            //Controllo che ci sia un elemento successivo
            if (!(this.hasNext())) {
                throw new NoSuchElementException("There is not a next element to retrieve.");
            }

            //Se sono all'inizio ritorno il primo, altrimenti il successivo dell'ultimo ritornato
            if (lastReturned == null) {
                this.lastReturned = ASDL2223Deque.this.last;
            } else {
                this.lastReturned = this.lastReturned.prev;
            }
            return this.lastReturned.item;
        }
    }

    /*
     * This method is only for JUnit testing purposes.
     */
    protected Node<E> getFirstNode() {
        return this.first;
    }

    /*
     * This method is only for JUnit testing purposes.
     */
    protected Node<E> getLastNode() {
        return this.last;
    }

}
