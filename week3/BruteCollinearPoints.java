import java.util.Comparator;
import java.util.ArrayList;
import java.util.Arrays;

// TODO: Just check for duplicates in the validate method.

public class BruteCollinearPoints {
    // Apparently we're allowed to use ArrayLists for this (and later) assignments.
    private ArrayList<LineSegment> segments = new ArrayList<>();
    
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        points = validatePoints(points);

        for (int i = 0; i < points.length; i++) {
            for (int j = i+1; j < points.length; j++)
                for (int k = j+1; k < points.length; k++)
                    for (int l = k+1; l < points.length; l++) {
                        Point ipoint = points[i];
                        Point jpoint = points[j];
                        Point kpoint = points[k];
                        Point lpoint = points[l];

                        double jslope = ipoint.slopeTo(jpoint);
                        double kslope = ipoint.slopeTo(kpoint);
                        double lslope = ipoint.slopeTo(lpoint);

                        if (jslope == kslope && jslope == lslope)
                            segments.add(new LineSegment(ipoint, lpoint));
                    }
        }
    }
    
    private Point[] validatePoints(Point[] points) {
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

        return copy;
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }
}
