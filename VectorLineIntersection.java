import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VectorLineIntersection implements LineIntersectionAlgorithm {

    @Override
    public List<Line2D> calculateIntersections(List<Line2D> lines) {
        List<Line2D> intersections = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            for (int j = i + 1; j < lines.size(); j++) {
                Line2D line1 = lines.get(i);
                Line2D line2 = lines.get(j);

                if (doIntersect(line1.getP1(), line1.getP2(), line2.getP1(), line2.getP2())) {
                    intersections.add(new Line2D.Double(line1.getP1(), line1.getP2()));
                }
            }
        }

        writeIntersectionsToFile(intersections, "line_intersection_output.txt");

        return intersections;
    }

    private boolean doIntersect(Point2D p1, Point2D q1, Point2D p2, Point2D q2) {
        Point2D v1 = new Point2D.Double(q1.getX() - p1.getX(), q1.getY() - p1.getY());
        Point2D v2 = new Point2D.Double(q2.getX() - p2.getX(), q2.getY() - p2.getY());

        double crossProduct = v1.getX() * v2.getY() - v1.getY() * v2.getX();

        double dotProduct1 = (p2.getX() - p1.getX()) * v1.getY() - (p2.getY() - p1.getY()) * v1.getX();
        double dotProduct2 = (p2.getX() - p1.getX()) * v2.getY() - (p2.getY() - p1.getY()) * v2.getX();

        if (Math.abs(crossProduct) < 1e-9) {  
            if (Math.abs(dotProduct1) < 1e-9 && Math.abs(dotProduct2) < 1e-9) {
                return true;
            } else {
                return false;
            }
        } else { 
            double t1 = dotProduct1 / crossProduct;
            double t2 = dotProduct2 / crossProduct;

            if (t1 >= 0 && t1 <= 1 && t2 >= 0 && t2 <= 1) {
                return true;
            } else {
                return false;
            }
        }
    }

    private void writeIntersectionsToFile(List<Line2D> intersections, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Line2D line : intersections) {
                writer.write(line.getX1() + " " + line.getY1() + " " + line.getX2() + " " + line.getY2());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getAlgorithmName() {
        return "Vector";
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
