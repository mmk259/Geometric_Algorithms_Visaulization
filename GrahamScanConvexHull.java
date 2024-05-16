import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

public class GrahamScanConvexHull extends JPanel implements ConvexHullAlgorithm{

    private List<Point2D> points = new ArrayList<>();

    public GrahamScanConvexHull() {
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

        Point2D startPoint = inputPoints.get(0);
        for (Point2D point : inputPoints) {
            if (point.getY() < startPoint.getY()
                    || (point.getY() == startPoint.getY() && point.getX() < startPoint.getX())) {
                startPoint = point;
            }
        }

        Collections.sort(inputPoints, new PolarAngleComparator(startPoint));

        Stack<Point2D> convexHull = new Stack<>();
        convexHull.push(inputPoints.get(0));
        convexHull.push(inputPoints.get(1));
        convexHull.push(inputPoints.get(2));

        for (int i = 3; i < inputPoints.size(); i++) {
            while (orientation(nextToTop(convexHull), convexHull.peek(), inputPoints.get(i)) != 2) {
                convexHull.pop();
            }
            convexHull.push(inputPoints.get(i));
        }

        writeConvexHullToFile(convexHull, "convex_hull_output.txt");
        
        return new ArrayList<>(convexHull);
    }

    private Point2D nextToTop(Stack<Point2D> stack) {
        Point2D top = stack.pop();
        Point2D nextToTop = stack.peek();
        stack.push(top);
        return nextToTop;
    }

    private int orientation(Point2D p, Point2D q, Point2D r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) -
                     (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (val == 0) return 0; 
        return (val > 0) ? 1 : 2; 
    }

    private static class PolarAngleComparator implements Comparator<Point2D> {
        private Point2D startPoint;

        public PolarAngleComparator(Point2D startPoint) {
            this.startPoint = startPoint;
        }

        @Override
        public int compare(Point2D p1, Point2D p2) {
            double angle1 = Math.atan2(p1.getY() - startPoint.getY(), p1.getX() - startPoint.getX());
            double angle2 = Math.atan2(p2.getY() - startPoint.getY(), p2.getX() - startPoint.getX());

            if (angle1 < angle2) return -1;
            if (angle1 > angle2) return 1;
            return Double.compare(p1.distance(startPoint), p2.distance(startPoint));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.RED);
        for (Point2D point : points) {
            g2d.fillOval((int) point.getX() - 3, (int) point.getY() - 3, 6, 6);
        }

        g2d.setColor(Color.BLUE);
        List<Point2D> convexHull = calculateConvexHull(points);
        for (int i = 0; i < convexHull.size() - 1; i++) {
            Point2D p1 = convexHull.get(i);
            Point2D p2 = convexHull.get(i + 1);
            g2d.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
        }

        if (!convexHull.isEmpty()) {
            Point2D firstPoint = convexHull.get(0);
            Point2D lastPoint = convexHull.get(convexHull.size() - 1);
            g2d.drawLine((int) firstPoint.getX(), (int) firstPoint.getY(), (int) lastPoint.getX(), (int) lastPoint.getY());
        }
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
        return "Graham Scan";
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