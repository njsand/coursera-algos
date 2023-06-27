import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
// import edu.princeton.cs.algs4. StdDraw.Black;

public class KdTree {

    private Node root;

    // Incremented whenever we insert.
    private int size;
    
    private static class Node {
        Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        Node lb;        // the left/bottom subtree
        Node rt;        // the right/top subtree

        Node(Point2D p, RectHV rect, Node lb, Node rt) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
        }
    }

    // construct an empty set of points 
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set 
    public int size() {
        return size;
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Point must not be null.");
        
        boolean inserted = false;
        
        if (root == null) {
            root = new Node(p, new RectHV(0, 0, 1, 1), null, null);
            inserted = true;
        }
        else
            inserted = doInsert(p, root, true);

        if (inserted)
            ++size;
    }

    private enum Direction {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    };

    // Node n is never null.
    private boolean doInsert(Point2D p, Node n, boolean cmpX) {
        if (n.p.equals(p))
            return false;

        if ((cmpX && p.x() < n.p.x()) ||
            (!cmpX && p.y() < n.p.y())) {
            // We're going down the left mate.
            if (n.lb == null) {
                // Insert new node
                RectHV area = cropit(n.rect, n.p, cmpX ? Direction.RIGHT : Direction.TOP);
                n.lb = new Node(p, area, null, null);
                    
                return true;
            }
            else {
                return doInsert(p, n.lb, !cmpX);
            }
        } else {
            // We're going down the right
            if (n.rt == null) {
                // Insert new node
                RectHV area = cropit(n.rect, n.p, cmpX ? Direction.LEFT : Direction.BOTTOM);
                n.rt = new Node(p, area, null, null);
                    
                return true;
            } else {
                return doInsert(p, n.rt, !cmpX);
            }
        }
    }
     
    // Return the area given by cropping {@code a} with p.  We use either the x-
    // or y-coordinate of p as a boundary depending on direction {@code d}.
    // 
    // Direction {@code d} is the region to crop.  Eg., when d is TOP, the top
    // part of the rectangle defined by the y-coordinate of p is cropped.
    private RectHV cropit(RectHV a, Point2D p, Direction d) {
        double xmin = d == Direction.LEFT   ? p.x() : a.xmin();
        double ymin = d == Direction.BOTTOM ? p.y() : a.ymin();
        double xmax = d == Direction.RIGHT  ? p.x() : a.xmax();
        double ymax = d == Direction.TOP    ? p.y() : a.ymax();
        
        return new RectHV(xmin, ymin, xmax, ymax);
    }
        
    // does the set contain point p? 
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Point must not be null.");

        if (root == null)
            return false;

        return doContains(root, p, true);
    }

    // TODO: Share code with the insert here somehow.
    private boolean doContains(Node n, Point2D p, boolean useXcoord) {
        if (n.p.equals(p))
            return true;

        if ((useXcoord && p.x() < n.p.x()) ||
            (!useXcoord && p.y() < n.p.y())) {
            if (n.lb == null)
                return false;
            else
                return doContains(n.lb, p, !useXcoord);
        } else {
            if (n.rt == null)
                return false;
            else
                return doContains(n.rt, p, !useXcoord);
        }
            
    }
    
    // draw all points to standard draw 
    public void draw() {
        doDraw(root, true);
    }

    // FIXME: I'm just drawing rects here, not the lines correctly.  
    private void doDraw(Node n, boolean useX) {
        if (n != null) {
            // Draw point
            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(StdDraw.BLACK);
            n.p.draw();

            // Draw the line
            if (useX) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.setPenRadius();
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.setPenRadius();
            }
            n.rect.draw();

            doDraw(n.lb, !useX);
            doDraw(n.rt, !useX);
        }
    }

    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range (RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("Rectangle must not be null.");
        
        ArrayList<Point2D> result = new ArrayList<>();

        if (root != null)
            doRange(rect, root, result);

        return result;
    }

    // Helper for range.
    private void doRange(RectHV rect, Node n, ArrayList<Point2D> result) {
        if (rect.contains(n.p))
            result.add(n.p);

        // if (rect.intersects(n.rect)) {
            if (n.lb != null && rect.intersects(n.lb.rect))
                doRange(rect, n.lb, result);
            if (n.rt != null && rect.intersects(n.rt.rect))
                doRange(rect, n.rt, result);
        // }
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Point must not be null.");

        if (root == null)
            return null;
        
        return doNearest(p, root, root.p);
    }

    private Point2D doNearest(Point2D p, Node n, Point2D best) {
        // Prune the search if this node's rectangle can't include any closer points.
        if (n.rect.distanceSquaredTo(p) > p.distanceSquaredTo(best))
            return best;
        
        double newDist = p.distanceSquaredTo(n.p);
        double closestDist = p.distanceSquaredTo(best);
        
        if (newDist < closestDist) {
            best = n.p;
            closestDist = newDist;
        }

        Point2D rt = null;
        Point2D lb = null;

        // A lot of conditions here!  Probably mostly necessary.
        if (n.rt == null && n.lb == null)
            return best;

        if (n.rt == null)
            return doNearest(p, n.lb, best);

        if (n.lb == null)
            return doNearest(p, n.rt, best);

        // Choose the (left or right) path to follow, based on heuristic.
        if (n.rt.rect.contains(p)) {
            rt = doNearest(p, n.rt, best);
            if (p.distanceSquaredTo(rt) < closestDist)
                best = rt;

            return doNearest(p, n.lb, best);
        } else {
            lb = doNearest(p, n.lb, best);
            if (p.distanceSquaredTo(lb) < closestDist)
                best = lb;

            return doNearest(p, n.rt, best);
        }
    }
    
    // unit testing of the methods (optional) 
    public static void main(String[] args) {
        // No unit tests haha!
    }
}
