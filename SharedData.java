import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class SharedData {
    private static List<Point2D> points = new ArrayList<>();

    public static List<Point2D> getPoints() {
        return points;
    }

    public static void addPoint(Point2D point) {
        points.add(point);
    }
}