import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JarvisMarchConvexHull extends JPanel implements ConvexHullAlgorithm {

    private List<Point2D> points = new ArrayList<>();

    public JarvisMarchConvexHull() {
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

        List<Point2D> hull = new ArrayList<>();

        int leftmostIndex = 0;
        for (int i = 1; i < n; i++) {
            if (inputPoints.get(i).getX() < inputPoints.get(leftmostIndex).getX()) {
                leftmostIndex = i;
            }
        }

        int currentPointIndex = leftmostIndex;
        do {
            hull.add(inputPoints.get(currentPointIndex));

            int nextPointIndex = (currentPointIndex + 1) % n;
            for (int i = 0; i < n; i++) {
                if (orientation(inputPoints.get(currentPointIndex), inputPoints.get(i), inputPoints.get(nextPointIndex)) == -1) {
                    nextPointIndex = i;
                }
            }

            currentPointIndex = nextPointIndex;

        } while (currentPointIndex != leftmostIndex);

        writeConvexHullToFile(hull, "convex_hull_output.txt");
        return hull;
    }

    private int orientation(Point2D p, Point2D q, Point2D r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());
        if (val == 0) {
            return 0; 
        }
        return (val > 0) ? 1 : -1;
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
        return "Jarvis March";
    }

    @Override
    public String getTimeComplexity() {
        return "O(nh)"; 
    }

    @Override
    public String getSpaceComplexity() {
        return "O(1)"; 
    }
}
