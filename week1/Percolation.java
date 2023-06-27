import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int siteCount;
    private boolean[][] grid;   // Stores open sites.
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF backwashPreventer;
    private int openCount;

    // Top and bottom dummy site indexes in the WeightedQuickUnionUF.
    private final int TOP_SITE_ELEMENT;
    private final int BOTTOM_SITE_ELEMENT;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be a positive number");
        }

        siteCount = n * n;
        grid = new boolean[n][n];

        // We have two extra dummy nodes (hence the +2 below) that are connected
        // to all of the top and all of the bottom respectively.  The final
        // percolation check is therefore that the top dummy node is connected
        // to the bottom one.
        //
        // This is the clever trick as described in the lectures -- better go
        // watch it there or you'll have NFI what's going on here.
        
        uf = new WeightedQuickUnionUF(siteCount + 2);
        backwashPreventer = new WeightedQuickUnionUF(siteCount + 2);

        TOP_SITE_ELEMENT = siteCount;
        BOTTOM_SITE_ELEMENT = siteCount + 1;

        openCount = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        
        int i = row - 1, j = col - 1;

        if (grid[i][j]) {
            return;
        }

        ++openCount;

        grid[i][j] = true;
        int ufPos = ufElement(i, j);

        // If it's on top or bottom row, connect up dummy nodes.
        if (i == 0) {
            uf.union(TOP_SITE_ELEMENT, ufPos);
            backwashPreventer.union(TOP_SITE_ELEMENT, ufPos);
        }

        if (i == grid.length - 1)
            uf.union(BOTTOM_SITE_ELEMENT, ufPos);
        // backwashPreventer is skipped!

        // connect to open neighbours
        connectNeighbours(i, j);
        
        return;
    }

    // Connect open sites surrounding pos: i, j.
    private void connectNeighbours(int i, int j) {
        int ufe = ufElement(i, j);
        
        int nexti = 0, nextj = 0;

        int[][] neighbours = {
            {i-1, j},           // above
            {i+1, j},           // below
            {i, j-1},           // left
            {i, j+1}};          // right

        for (int k = 0; k < neighbours.length; k++) {
            nexti = neighbours[k][0];
            nextj = neighbours[k][1];

            if (validGridPos(nexti, nextj) && grid[nexti][nextj]) {
                uf.union(ufe, ufElement(nexti, nextj));
                backwashPreventer.union(ufe, ufElement(nexti, nextj));
            }
        }
    }

    // Return true iff i and j are valid indices into the grid.  This
    // is zero-based, not 1-based like the public API.
    private boolean validGridPos(int i, int j) {
        if (i < 0 || i > grid.length - 1)
            return false;
        if (j < 0 || j > grid.length - 1)
            return false;

        return true;
    }

    // Convert row and column to sequential index for use with
    // WeightedQuickUnionUF.
    private int ufElement(int row, int col) {
        return row * grid.length + col;
    }

    private void validate(int row, int col) {
        // TODO - add a string error message in here?
        
        if (row <= 0 ||
            row >= grid.length + 1 ||
            col <= 0 ||
            col >= grid.length + 1) {
            throw new IllegalArgumentException();
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);

        int i = row - 1, j = col - 1;

        // This method is just asking whether or not we're connected to the top
        // dummy site.
        return backwashPreventer.find(TOP_SITE_ELEMENT) == backwashPreventer.find(ufElement(i, j));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(TOP_SITE_ELEMENT) == uf.find(BOTTOM_SITE_ELEMENT);
    }

    // test client (optional)
    public static void main(String[] args) {
        return;
    }
}
