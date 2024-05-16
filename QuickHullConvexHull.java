import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuickHullConvexHull extends JPanel implements ConvexHullAlgorithm{

    private List<Point2D> points = new ArrayList<>();

    public QuickHullConvexHull() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                points.add(e.getPoint());
                repaint();
            }
        });
    }

    @Override
    public List<Point2D> calculateConvexHull(List<Point2D> inputPoints) {
        int n = inputPoints.size();
        if (n < 3) {
            return inputPoints;
        }

        List<Point2D> convexHull = new ArrayList<>();

        Point2D leftmost = inputPoints.get(0);
        Point2D rightmost = inputPoints.get(0);

        for (Point2D point : inputPoints) {
            if (point.getX() < leftmost.getX()) {
                leftmost = point;
            }
            if (point.getX() > rightmost.getX()) {
                rightmost = point;
            }
        }

        convexHull.add(leftmost);
        convexHull.add(rightmost);

        List<Point2D> upperSet = new ArrayList<>();
        List<Point2D> lowerSet = new ArrayList<>();

        for (Point2D point : inputPoints) {
            if (point.equals(leftmost) || point.equals(rightmost)) {
                continue;
            }

            if (orientation(leftmost, rightmost, point) > 0) {
                upperSet.add(point);
            } else if (orientation(leftmost, rightmost, point) < 0) {
                lowerSet.add(point);
            }
        }

        quickHull(upperSet, leftmost, rightmost, convexHull);
        quickHull(lowerSet, rightmost, leftmost, convexHull);

        writeConvexHullToFile(convexHull, "convex_hull_output.txt");
        
        return convexHull;
    }

    private void quickHull(List<Point2D> points, Point2D p1, Point2D p2, List<Point2D> convexHull) {
        int insertPosition = convexHull.indexOf(p2);

        if (points.isEmpty()) {
            return;
        }

        if (points.size() == 1) {
            convexHull.add(insertPosition, points.get(0));
            return;
        }

        double maxDistance = -1;
        Point2D farthestPoint = null;

        for (Point2D point : points) {
            double distance = distance(p1, p2, point);
            if (distance > maxDistance) {
                maxDistance = distance;
                farthestPoint = point;
            }
        }

        convexHull.add(insertPosition, farthestPoint);

        List<Point2D> leftSetAP = new ArrayList<>();
        List<Point2D> leftSetPB = new ArrayList<>();

        for (Point2D point : points) {
            if (point.equals(farthestPoint)) {
                continue;
            }

            if (orientation(p1, farthestPoint, point) > 0) {
                leftSetAP.add(point);
            }

            if (orientation(farthestPoint, p2, point) > 0) {
                leftSetPB.add(point);
            }
        }

        quickHull(leftSetAP, p1, farthestPoint, convexHull);
        quickHull(leftSetPB, farthestPoint, p2, convexHull);
    }

    private double orientation(Point2D a, Point2D b, Point2D c) {
        return (b.getY() - a.getY()) * (c.getX() - b.getX())
                - (b.getX() - a.getX()) * (c.getY() - b.getY());
    }

    private double distance(Point2D a, Point2D b, Point2D c) {
        return Math.abs((b.getY() - a.getY()) * c.getX() + (a.getX() - b.getX()) * c.getY() + (b.getX() * a.getY() - a.getX() * b.getY()))
                / Math.sqrt(Math.pow(b.getY() - a.getY(), 2) + Math.pow(a.getX() - b.getX(), 2));
    }

    private void writeConvexHullToFile(List<Point2D> convexHull, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Point2D point : convexHull) {
                writer.write(point.getX() + " , " + point.getY());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getAlgorithmName() {
        return "Quick Hull";
    }

    @Override
    public String getTimeComplexity() {
        return "O(n log n)"; 
    }

    @Override
    public String getSpaceComplexity() {
        return "O(n)"; 
    }
}