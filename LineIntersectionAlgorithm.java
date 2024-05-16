import java.awt.geom.Line2D;
import java.util.List;

public interface LineIntersectionAlgorithm {
    List<Line2D> calculateIntersections(List<Line2D> lines);
    String getTimeComplexity();
    String getSpaceComplexity();
    String getAlgorithmName();
}
