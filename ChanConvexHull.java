import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;


public class ChanConvexHull extends JPanel implements ConvexHullAlgorithm {

    private List<Point2D> points = new ArrayList<>();

    public ChanConvexHull() {
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

        inputPoints.sort(Comparator.comparing(Point2D::getX));

        int h = Math.min(2, n);
        List<List<Point2D>> pointSets = new ArrayList<>();
        for (int i = 0; i < n; i += h) {
            int end = Math.min(i + h, n);
            pointSets.add(inputPoints.subList(i, end));
        }

        for (List<Point2D> pointSet : pointSets) {
            List<Point2D> giftWrappingHull = GrahamScan(pointSet);
            convexHull.addAll(giftWrappingHull);
        }

        List<Point2D> grahamHull = GrahamScan(convexHull);

        writeConvexHullToFile(grahamHull, "convex_hull_output.txt");

        return grahamHull;
    }


    private static List<Point2D> GrahamScan(List<Point2D> points) {
        int n = points.size();
        if (n < 3) {
            return points;
        }

        Point2D pivot = points.stream()
                .min(Comparator.comparing(Point2D::getY).thenComparing(Point2D::getX))
                .orElseThrow(IllegalArgumentException::new);

        points.sort((p1, p2) -> {
            double angle1 = Math.atan2(p1.getY() - pivot.getY(), p1.getX() - pivot.getX());
            double angle2 = Math.atan2(p2.getY() - pivot.getY(), p2.getX() - pivot.getX());
            return Double.compare(angle1, angle2);
        });

        Stack<Point2D> stack = new Stack<>();
        stack.push(points.get(0));
        stack.push(points.get(1));

        for (int i = 2; i < n; i++) {
            while (orientation(nextToTop(stack), stack.peek(), points.get(i)) != 2) {
                stack.pop();
            }
            stack.push(points.get(i));
        }

        return new ArrayList<>(stack);
    }

    private static Point2D nextToTop(Stack<Point2D> stack) {
        Point2D top = stack.pop();
        Point2D nextToTop = stack.peek();
        stack.push(top);
        return nextToTop;
    }

    private static int orientation(Point2D p, Point2D q, Point2D r) {
        int val = (int) ((q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY()));
        if (val == 0) return 0;
        return (val > 0) ? 1 : 2;
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
        return "Chan's Algorithm";
    }

    @Override
    public String getTimeComplexity() {
        return "O(n log h)"; 
    }

    @Override
    public String getSpaceComplexity() {
        return "O(1)"; 
    }
}
