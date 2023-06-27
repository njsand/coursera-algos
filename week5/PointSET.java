/**
 * Range and nearest-neighbour search for a set of points.
 *
 * This is a brute force implementation as a foil for the brilliance of ./KdTree.java.
 *
 * Part of the week 5 assignment for Introduction to Algorithms on Coursera.
 */

import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {

    private SET<Point2D> points;

    // construct an empty set of points 
    public PointSET() {
        points = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Point must not be null");

        points.add(p);
    }
    
    // does the set contain point p? 
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Point must not be null");

        return points.contains(p);
    }

    // draw all points to standard draw 
    public void draw() {
        for (Point2D p: points)
            p.draw();
    }

    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range (RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("Rectangle must not be null");

        ArrayList<Point2D> list = new ArrayList<>();
        
        // Brute force
        for (Point2D p: points) {
            if (rect.contains(p))
                list.add(p);                       
        }

        return list;
    }

    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Point must not be null");

        Point2D closest = null;
        double distance = Double.POSITIVE_INFINITY;
        
        for (Point2D other: points) {
            double d = p.distanceSquaredTo(other);

            if (d < distance) {
                closest = other;
                distance = d;
            }
        }

        return closest;
    }
    
    // unit testing of the methods (optional) 
    public static void main(String[] args) {
        
    }
}
