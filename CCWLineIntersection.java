import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CCWLineIntersection implements LineIntersectionAlgorithm {

    @Override
    public List<Line2D> calculateIntersections(List<Line2D> lines) {
        List<Line2D> intersections = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            for (int j = i + 1; j < lines.size(); j++) {
                Line2D line1 = lines.get(i);
                Line2D line2 = lines.get(j);

                if (ccw(line1.getP1(), line1.getP2(), line2.getP1()) !=
                        ccw(line1.getP1(), line1.getP2(), line2.getP2()) &&
                        ccw(line2.getP1(), line2.getP2(), line1.getP1()) !=
                                ccw(line2.getP1(), line2.getP2(), line1.getP2())) {
                    Point2D intersectionPoint = calculateIntersectionPoint(line1, line2);
                    intersections.add(new Line2D.Double(intersectionPoint, intersectionPoint));
                }
            }
        }

        writeIntersectionsToFile(intersections, "line_intersection_output.txt");

        return intersections;
    }

    private int ccw(Point2D a, Point2D b, Point2D c) {
        double crossProduct = (b.getX() - a.getX()) * (c.getY() - a.getY()) -
                (b.getY() - a.getY()) * (c.getX() - a.getX());

        if (crossProduct > 0) return 1;
        if (crossProduct < 0) return -1;
        return 0;
    }

    private Point2D calculateIntersectionPoint(Line2D line1, Line2D line2) {
        double x1 = line1.getX1(), y1 = line1.getY1();
        double x2 = line1.getX2(), y2 = line1.getY2();
        double x3 = line2.getX1(), y3 = line2.getY1();
        double x4 = line2.getX2(), y4 = line2.getY2();

        double det = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        double x = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / det;
        double y = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / det;

        return new Point2D.Double(x, y);
    }

    private void writeIntersectionsToFile(List<Line2D> intersections, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Line2D line : intersections) {
                Point2D point = line.getP1();
                writer.write(point.getX() + " " + point.getY());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getAlgorithmName() {
        return "CCW";
    }

    @Override
    public String getTimeComplexity() {
        return "O(n^2)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(1)";
    }
}
