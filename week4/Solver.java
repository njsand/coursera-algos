import java.util.Comparator;
import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    private static class Node {

        int manhattanCache = -1;

        Node(Board b, Node prev, int moves) {
            this.b = b;
            this.prev = prev;
            this.moves = moves;
        }

        Board b;
        Node prev;
        int moves;

        private int getManhattan() {
            if (manhattanCache == -1) {
                manhattanCache = b.manhattan();
            }
            return manhattanCache;
        }
    }

    private static class NodeComp implements Comparator<Node> {
        public int compare(Node a, Node b) {
            int ap = a.getManhattan() + a.moves;
            int bp = b.getManhattan() + b.moves;

            return ap - bp;
        }
    }

    private ArrayList<Board> moves;
    private boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("Board must not be null.");

        MinPQ<Node> pq = new MinPQ<Node>(new NodeComp());

        // All this twin stuff will make sense if you read the spec (or you're
        // very smart and don't need that.)
        MinPQ<Node> pqtwin = new MinPQ<Node>(new NodeComp());

        pq.insert(new Node(initial, null, 0));
        pqtwin.insert(new Node(initial.twin(), null, 0));

        Node min = pq.delMin();
        Node mintwin = pqtwin.delMin();

        while (!min.b.isGoal() && !mintwin.b.isGoal()) {
            for (Board n: min.b.neighbors()) {
                if (min.prev == null)
                    pq.insert(new Node(n, min, min.moves+1));
                else if (!n.equals(min.prev.b)) {
                    pq.insert(new Node(n, min, min.moves+1));
                }
            }

            for (Board n: mintwin.b.neighbors()) {
                if (mintwin.prev == null)
                    pqtwin.insert(new Node(n, mintwin, mintwin.moves+1));
                else if (!n.equals(mintwin.prev.b)) {
                    pqtwin.insert(new Node(n, mintwin, mintwin.moves+1));
                }
            }

            min = pq.delMin();
            mintwin = pqtwin.delMin();
        }

        solvable = min.b.isGoal();
        
        if (solvable) {
            moves = new ArrayList<Board>();

            // Now build the list of successful moves to the goal.
            Stack<Board> s = new Stack<>();
            while (min != null) {
                s.push(min.b);
                min = min.prev;
            }

            for (Board b: s)
                moves.add(b);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves == null ? -1 : moves.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return moves;
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
