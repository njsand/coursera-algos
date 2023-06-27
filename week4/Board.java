import java.util.Arrays;

import edu.princeton.cs.algs4.Queue;

public class Board {

    private int[][] board;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null)
            throw new IllegalArgumentException("Board must not be null");

        if (tiles.length < 2)
            throw new IllegalArgumentException("Board size must be two or greater");

        board = boardCopy(tiles);
    }

    private static int[][] boardCopy(int[][] board) {
        int[][] result = new int[board.length][];
        
        for (int i = 0; i < board.length; i++)
            result[i] = Arrays.copyOf(board[i], board[i].length);

        return result;
    }
    
    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append(dimension());
        s.append('\n');

        for (int i = 0; i < board.length; i++) {
            if (i != 0)
                s.append("\n");

            for (int j = 0; j < board[i].length; j++) {
                s.append(' ');  // Each board row is indented by one space.
                s.append(j == 0 ? board[i][j] : " " + board[i][j]);
            }
        }

        return s.toString();
    }

    // board dimension n
    public int dimension() {
        // The board is square, so just taking board.length is enough.
        return board.length;
    }

    // number of tiles out of place
    public int hamming() {
        int expected = 1;
        int errors = 0;
                 
        int numTiles = dimension() * dimension();

        for (int[] row: board)
            for (int c: row) {
                if (c != 0 && c != expected)
                    ++errors;
                expected = (expected + 1) % numTiles;
            }

        return errors;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int sum = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0)
                    continue;

                Position p = goalPosition(board[i][j]);

                sum += Math.abs(p.row - i);
                sum += Math.abs(p.col - j);
            }
        }

        return sum;
    }

    // Return the *goal* position of {@code tile}.  Eg., on a 3x3 board, tile 1 has goal position (0, 0) and tile 8 has
    // goal position (2, 1).  *NB* This is the position of the tile in the ideal (ie, goal) board, not it's current actual
    // position!
    private Position goalPosition(int tile) {
        if (tile <= 0)
            throw new IllegalArgumentException("Tile must be a positive number.");

        return new Position((tile-1) / dimension(), (tile-1) % dimension());
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || ! y.getClass().equals(Board.class))
            return false;

        Board that = (Board) y;

        if (dimension() != that.dimension())
            return false;

        for (int i = 0; i < board.length; i++)

            for (int j = 0; j < board[i].length; j++)
                if (board[i][j] != that.board[i][j])
                    return false;

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        // Empty square:
        // on corner -> 2 neighbours
        // on edge -> 3 neighbours
        // elsewhere -> 4 neighbours

        Queue<Board> result = new Queue<>();
        
        int[][] deltas = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        // I originally thought it would make sense to maintain the position of the empty tile (as a
        // member var) and avoid scanning for it here.  But it fails some memory usage tests in the
        // assignment if we do that, and doesn't seem much faster anyway.
        Position emptyPos = findTile(0);

        for (int[] d: deltas) {
            Position newPos = new Position(emptyPos.row + d[0], emptyPos.col + d[1]);
            
            if (!oob(newPos)) {
                Board neighbour = new Board(board);
                neighbour.swapTiles(emptyPos, newPos);
                result.enqueue(neighbour);
            }
        }
        
        return result;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin = new Board(board);

        // The empty tile (tile 0) is not a tile, so make sure not to swap that one!
        if (board[0][0] == 0 || board[0][1] == 0)
            twin.swapTiles(new Position(1, 0), new Position(1, 1));
        else
            twin.swapTiles(new Position(0, 0), new Position(0, 1));

        return twin;
    }

    private static class Position {
        int row, col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public String toString() {
            return String.format("(%d, %d)", row, col);
        }
    }

    // Return true iff the pos is off the grid!
    private boolean oob(Position p) {
        if (p.row < 0 || p.row >= dimension() ||
            p.col < 0 || p.col >= dimension())
            return true;
        return false;
    }

    // Swap any two tiles.
    private void swapTiles(Position tile1, Position tile2) {
        int tmp = board[tile1.row][tile1.col];
        board[tile1.row][tile1.col] = board[tile2.row][tile2.col];
        board[tile2.row][tile2.col] = tmp;
    }

    // Return the Position of a tile.
    // Scans the whole board to find it.
    private Position findTile(int tile) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == tile)
                    return new Position(i, j);        
            }
        }
        return null;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] goal = {{1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 0}};

        int[][] switchup = {{2, 1, 3},
                            {4, 5, 6},
                            {7, 8, 0}};

        int[][] shifted = {{0, 1, 2},
                           {3, 4, 5},
                           {6, 7, 8}};

        int[][] centerEmpty = {{4, 1, 2},
                               {3, 0, 5},
                               {6, 7, 8}};

        Board goalBoard = new Board(goal);
        Board ce = new Board(centerEmpty);

        System.out.println(goalBoard);
        System.out.println("isGoal() should be true: " + goalBoard.isGoal());

        Board equal = new Board(goal);
        System.out.println("goalBoard equals equals: " + goalBoard.equals(equal));

        Board twin = goalBoard.twin();
        System.out.println(twin);
        System.out.println("twin().isGoal() should be false: " + twin.isGoal());
        System.out.println("goalBoard equals twin should be false: " + goalBoard.equals(twin));
        System.out.println("goalBoard.isGoal() should still be true: " + goalBoard.isGoal());

        Board switchupBoard = new Board(switchup);
        Board shiftedBoard = new Board(shifted);
        System.out.println(".equals(): " + goalBoard.equals(switchupBoard));
        System.out.println(".equals(): " + goalBoard.equals(goalBoard));
        System.out.println("switchupBoard.hamming() should be 2: " + switchupBoard.hamming());
        System.out.println("switchupBoard.manhattan() should also be 2!: " + switchupBoard.hamming());
        System.out.println("shifted.manhattan() should be 12: " + shiftedBoard.manhattan());

        System.out.println("goalPosition(1):" + goalBoard.goalPosition(1));
        System.out.println("goalPosition(2):" + goalBoard.goalPosition(2));
        System.out.println("goalPosition(3):" + goalBoard.goalPosition(3));
        System.out.println("goalPosition(4):" + goalBoard.goalPosition(4));
        System.out.println("goalPosition(5):" + goalBoard.goalPosition(5));
        System.out.println("goalPosition(8):" + goalBoard.goalPosition(8));

        System.out.println("Goal board's neighbours:");
        for (Board n: goalBoard.neighbors())
            System.out.println(n);
        System.out.println("Center empty board's neighbours:");
        for (Board n: ce.neighbors())
            System.out.println(n);

        assert(false);
    }

}
