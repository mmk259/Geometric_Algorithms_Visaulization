import java.awt.geom.Point2D;
import java.util.List;

public interface ConvexHullAlgorithm {
    List<Point2D> calculateConvexHull(List<Point2D> inputPoints);
    String getAlgorithmName();
    String getTimeComplexity();
    String getSpaceComplexity();
}
