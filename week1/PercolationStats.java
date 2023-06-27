import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private double mean;
    private double stddev;
    private int trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <=0)
            throw new IllegalArgumentException("Invalid argument");
        
        this.trials = trials;
        
        double[] thresholds = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);

            while (!p.percolates()) {
                int row = StdRandom.uniformInt(1, n+1);
                int col = StdRandom.uniformInt(1, n+1);

                p.open(row, col);
            }

            thresholds[i] = ((double) p.numberOfOpenSites()) / (n * n);
        }

        mean = StdStats.mean(thresholds);
        stddev = StdStats.stddev(thresholds);

    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, trials);

        StdOut.printf("mean                     = %f\n", ps.mean());
        StdOut.printf("stddev                   = %f\n", ps.stddev());

        StdOut.printf("95%% confidence interval = [%f, %f]\n", ps.confidenceLo(), ps.confidenceHi());
    }

}
