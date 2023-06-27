import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

// Reads a list of lines from stdin (until EOF) and then outputs a random selection of args[0] of them to stdout.
public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            q.enqueue(s);
        }

        int k = Integer.parseInt(args[0]);

        while (k-- != 0) {
            StdOut.printf("%s\n", q.dequeue());

            k -= 1;
        }
    }
}
