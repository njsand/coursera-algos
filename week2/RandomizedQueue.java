import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] queue;
    private int end;  // This is *one past* the last item.

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[10];
        end = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return end == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return end;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Item must not be null");

        upsize();

        queue[end++] = item;
    }

    // Double the size of the array if need be.
    private void upsize() {
        if (end != queue.length)
            return;

        realloc(queue.length * 2);
    }

    private void downsize() {
        if (end > queue.length / 4)
            return;
        
        realloc(queue.length / 2);
    }

    // Resize the queue to the provided size.  Copies the existing one into a
    // new array and discards the old one.
    private void realloc(int newsize) {
        Item[] newq = (Item[]) new Object[newsize];

        // Could use System.arraycopy() here maybe?
        for (int i = 0; i < size(); i++) {
            newq[i] = queue[i];
        }

        queue = newq;
    }
    
    // remove and return a random item
    public Item dequeue() {
        if (size() == 0)
            throw new NoSuchElementException();
        
        int chosen = StdRandom.uniformInt(0, size());
        
        Item result = queue[chosen];
        queue[chosen] = queue[end-1];
        queue[end-1] = null;
        --end;

        downsize();

        return result;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size() == 0)
            throw new NoSuchElementException();

        return queue[StdRandom.uniformInt(0, size())];
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private RandomizedQueue<Integer> helperq;
        
        public RandomizedQueueIterator() {
            helperq = new RandomizedQueue<>();

            for (int i = 0; i < size(); i++) {
                helperq.enqueue(i);
            }
        }

        public boolean hasNext() {
            return helperq.size() != 0;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            return queue[helperq.dequeue()];
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> test = new RandomizedQueue<>();

        for (int i = 0; i < 42; i++) {
            test.enqueue(i);
        }

        for (Integer i: test) {
            StdOut.printf("Iterator returned: %d\n", i);
        }
    }

}
