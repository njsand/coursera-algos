import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {

    private int size;

    // head == tail when size == 1.
    // head == tail == null when size == 0.
    private Node head;
    private Node tail;

    private class Node {
        Node(Item i) {
            item = i;
        }
        Item item;

        Node prev;
        Node next;
    }

    // construct an empty deque
    public Deque() {
        
    }

    // is the deque empty?
    public boolean isEmpty() {
        return head == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Item cannot be null");
        
        Node add = new Node(item);
        
        if (size == 0) {
            head = tail = add;
        } else {
            head.prev = add;
            add.next = head;
            head = add;
        }

        ++size;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Item cannot be null");

        Node add = new Node(item);

        if (size == 0) {
            head = tail = add;
        } else {
            tail.next = add;
            add.prev = tail;
            tail = add;
        }

        ++size;
        return;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size == 0)
            throw new NoSuchElementException();
        
        Item result = head.item;
        
        if (size == 1) {
            head = tail = null;
        } else {
            head.next.prev = null;
            head = head.next;
        }

        --size;
        return result;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size == 0)
            throw new NoSuchElementException();

        Item result = tail.item;

        if (size == 1) {
            head = tail = null;
        } else {
            tail.prev.next = null;
            tail = tail.prev;
        }

        --size;
        return result;
    }

    // return an iterator over items in order from front to back
    private class DequeIterator implements Iterator<Item> {
        private Node current;

        public DequeIterator() {
            current = head;
        }

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            Item result = current.item;
            current = current.next;

            return result;
        }
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
    
    // unit testing (required)
    public static void main(String[] args)     {
        Deque<Integer> d = new Deque<>();
        
        for (int i = 0; i < 10; i++) {
            d.addFirst(i);
        }

        for (Integer j: d) {
            StdOut.printf("Hell yeah: %d\n", j);
        }

        for (Integer j: d) {
            StdOut.printf("Hell yeah: %d\n", j);
        }

        for (int i = 0; i < 5; i++) {
            d.removeLast();
        }

        for (Integer j: d) {
            StdOut.printf("Hell yeah: %d\n", j);
        }

    }

}
