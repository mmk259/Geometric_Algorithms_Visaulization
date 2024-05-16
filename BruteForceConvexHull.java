import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class BruteForceConvexHull extends JPanel implements ConvexHullAlgorithm {
    private List<Point2D> points = new ArrayList<>();

    public BruteForceConvexHull() {
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
        List<Point2D> convexHull = new ArrayList<>();

        int n = inputPoints.size();
        if (n < 3) {
            return convexHull;
        }

        int leftmostPointIndex = LeftmostPoint(inputPoints);
        int currentPointIndex = leftmostPointIndex;
        int nextPointIndex;

        do {
            convexHull.add(inputPoints.get(currentPointIndex));

            nextPointIndex = (currentPointIndex + 1) % n;
            for (int i = 0; i < n; i++) {
                if (orientation(inputPoints.get(currentPointIndex),
                                inputPoints.get(i),
                                inputPoints.get(nextPointIndex)) == 2) {
                    nextPointIndex = i;
                }
            }

            currentPointIndex = nextPointIndex;

        } while (currentPointIndex != leftmostPointIndex);

        writeConvexHullToFile(convexHull, "convex_hull_output.txt");

        return convexHull;
    }

    private int LeftmostPoint(List<Point2D> points) {
        int leftmostIndex = 0;
        int n = points.size();

        for (int i = 1; i < n; i++) {
            if (points.get(i).getX() < points.get(leftmostIndex).getX()) {
                leftmostIndex = i;
            }
        }

        return leftmostIndex;
    }

    private int orientation(Point2D p, Point2D q, Point2D r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());
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
        return "Brute Force";
    }

    @Override
    public String getTimeComplexity() {
        return "O(n^3)"; 
    }

    @Override
    public String getSpaceComplexity() {
        return "O(1)"; 
    }

}
