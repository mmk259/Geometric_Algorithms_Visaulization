import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlgebraicLineIntersection implements LineIntersectionAlgorithm {

    @Override
    public List<Line2D> calculateIntersections(List<Line2D> lines) {
        List<Line2D> intersections = new ArrayList<>();

        if (lines.size() < 2) {
            return intersections;
        }

        Point2D p1 = lines.get(0).getP1();
        Point2D p2 = lines.get(0).getP2();

        Point2D p3 = lines.get(1).getP1();
        Point2D p4 = lines.get(1).getP2();

        double slope1 = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
        double slope2 = (p4.getY() - p3.getY()) / (p4.getX() - p3.getX());

        if (slope1 == slope2) {
            return intersections;
        }

        double xNumerator = (p1.getX() * p2.getY() - p1.getY() * p2.getX()) * (p3.getX() - p4.getX()) - (p1.getX() - p2.getX()) * (p3.getX() * p4.getY() - p3.getY() * p4.getX());
        double yNumerator = (p1.getX() * p2.getY() - p1.getY() * p2.getX()) * (p3.getY() - p4.getY()) - (p1.getY() - p2.getY()) * (p3.getX() * p4.getY() - p3.getY() * p4.getX());
        double denominator = (p1.getX() - p2.getX()) * (p3.getY() - p4.getY()) - (p1.getY() - p2.getY()) * (p3.getX() - p4.getX());

        if (denominator == 0) {
            return intersections;
        }

        double intersectionX = xNumerator / denominator;
        double intersectionY = yNumerator / denominator;

        if (isPointWithinLineSegment(intersectionX, intersectionY, p1, p2) &&
            isPointWithinLineSegment(intersectionX, intersectionY, p3, p4)) {

            Point2D intersectionPoint = new Point2D.Double(intersectionX, intersectionY);
            intersections.add(new Line2D.Double(intersectionPoint, intersectionPoint));

            writeIntersectionsToFile(intersections, "line_intersection_output.txt");
        }

        return intersections;
    }

    private boolean isPointWithinLineSegment(double x, double y, Point2D start, Point2D end) {
        double minX = Math.min(start.getX(), end.getX());
        double maxX = Math.max(start.getX(), end.getX());
        double minY = Math.min(start.getY(), end.getY());
        double maxY = Math.max(start.getY(), end.getY());

        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }

    private void writeIntersectionsToFile(List<Line2D> intersections, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Line2D intersection : intersections) {
                Point2D point = intersection.getP1();
                writer.write(point.getX() + " " + point.getY());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getAlgorithmName() {
        return "Algebraic";
    }

    @Override
    public String getTimeComplexity() {
        return "O(1)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(1)";
    }
}
