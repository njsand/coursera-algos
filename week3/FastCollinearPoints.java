import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    // Apparently we're allowed to use ArrayLists for this (and later)
    // assignments.
    private ArrayList<LineSegment> segments = new ArrayList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        validatePoints(points);

        Point[] copy = Arrays.copyOf(points, points.length);
        double[] slopes = new double[points.length];

        int[][] runs = new int[points.length][2];

        for (Point p: points) {
            Comparator<Point> c = p.slopeOrder();

            Arrays.sort(copy, c);

            for (int i = 0; i < slopes.length; i++) {
                slopes[i] = p.slopeTo(copy[i]);
            }

            int dups = pickDups(slopes, runs);

            for (int i = 0; i < dups; i++) {

                if ((runs[i][1] - runs[i][0]) >= 2) {
                    Point[] check = Arrays.copyOfRange(copy, runs[i][0], runs[i][1]+1);

                    LineSegment seg = checkSmallest(p, check);
                    if (seg != null)
                        segments.add(seg);
                }
            }
        }
    }

    private LineSegment checkSmallest(Point p, Point[] points) {
        Arrays.sort(points);

        if (p.compareTo(points[0]) < 0) {
            return new LineSegment(p, points[points.length-1]);
        }
        return null;
    }

    // Pick out all runs of duplicates in a sorted array.
    // Return the count of dups found.
    //
    // The start and end of each sequence of duplicated elements is returned in
    // target[][0] and target[][1], respectively.
    // Target must be at least as long as source.length/2.
    // TODO: This is a generally re-usable method that can be extracted somewhere.
    private static int pickDups(double[] source, int[][] target) {
        int start = 0;
        int end = 0;
        boolean inRun = false;
        int foundRuns = 0;
        
        for (int i = 1; i < source.length; i++) {
            if (source[i-1] == source[i]) {
                if (!inRun) {
                    start = i-1;
                    inRun = true;
                }
            } else if (inRun) {
                end = i-1;
                target[foundRuns][0] = start;
                target[foundRuns][1] = end;
                inRun = false;
                ++foundRuns;
            }
        }

        if (inRun) {
            end = source.length-1;
            target[foundRuns][0] = start;
            target[foundRuns][1] = end;
            inRun = false;
            ++foundRuns;
        }

        return foundRuns;
    }
    
    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }

    // TODO: Remove dup with BruteCollinearPoints.
    private void validatePoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException("Points must not be null");

        Point[] copy = Arrays.copyOf(points, points.length);
        for (Point p: copy) {
            if (p == null)
                throw new IllegalArgumentException("Points must not be null");
        }

        Arrays.sort(copy);

        // Check for dups.
        for (int i = 1; i < points.length; i++)
            if (copy[i].equals(copy[i-1]))
                throw new IllegalArgumentException("Duplicate point!");
    }

    // public static void main(String[] args) {
    //     double[] dups = {Double.NEGATIVE_INFINITY, 0.35714285714285715, 0.7272727272727273, 1.0, 1.0, 1.0, 1.7142857142857142, 3.75};
    //     int[][] target = new int[10][10];
    // 
    //     System.out.println(pickDups(dups, target));
    //     System.out.println(1.0 == 1.0);
    // }
}
