import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class LineIntersectionPage extends JPanel {
    private List<Point2D> points = new ArrayList<>();
    private LineIntersectionAlgorithm lineIntersectionAlgorithm;
    private long executionTime;
    private JTextField timeComplexityTextField;
    private JTextField spaceComplexityTextField;
    private JTextField timeTextField;
    private JTextField answerTextField;
    private JTextField AlgoTextField;

    public LineIntersectionPage() {
        initUI();
        addListeners();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JButton algebricButton = new JButton("Algebric");
        algebricButton.addActionListener(e -> runAlgorithm(new AlgebraicLineIntersection()));
        algebricButton.setBackground(Color.WHITE);
        algebricButton.setForeground(Color.BLACK);

        JButton ccwButton = new JButton("CCW");
        ccwButton.addActionListener(e -> runAlgorithm(new CCWLineIntersection()));
        ccwButton.setBackground(Color.WHITE);
        ccwButton.setForeground(Color.BLACK);

        JButton vectorsButton = new JButton("Vectors");
        vectorsButton.addActionListener(e -> runAlgorithm(new VectorLineIntersection()));
        vectorsButton.setBackground(Color.WHITE);
        vectorsButton.setForeground(Color.BLACK);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clear());
        clearButton.setBackground(Color.BLACK);
        clearButton.setForeground(Color.WHITE);

        JButton backToMenuButton = new JButton("Back to Menu");
        backToMenuButton.addActionListener(e -> backToMenu());
        backToMenuButton.setBackground(Color.BLACK);
        backToMenuButton.setForeground(Color.WHITE);

        timeTextField = new JTextField(10);
        timeTextField.setEditable(false);

        answerTextField = new JTextField(15);
        answerTextField.setEditable(false);

        timeComplexityTextField = new JTextField(10);
        timeComplexityTextField.setEditable(false);
        spaceComplexityTextField = new JTextField(10);
        spaceComplexityTextField.setEditable(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(algebricButton);
        buttonPanel.add(ccwButton);
        buttonPanel.add(vectorsButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backToMenuButton);
        buttonPanel.add(new JLabel("Answer:"));
        buttonPanel.add(answerTextField);

        JPanel textBoxPanel = new JPanel();
        AlgoTextField = new JTextField(10);
        AlgoTextField.setEditable(false);
        textBoxPanel.add(new JLabel("Current Algorithm:"));
        textBoxPanel.add(AlgoTextField);

        JPanel complexityPanel = new JPanel();
        complexityPanel.add(new JLabel("Execution Time (ns):"));
        complexityPanel.add(timeTextField);
        complexityPanel.add(new JLabel("Time Complexity:"));
        complexityPanel.add(timeComplexityTextField);
        complexityPanel.add(new JLabel("Space Complexity:"));
        complexityPanel.add(spaceComplexityTextField);

        JPanel Panel = new JPanel(new GridLayout(2, 1));
        Panel.add(buttonPanel);
        Panel.add(textBoxPanel);

        add(Panel, BorderLayout.NORTH);
        add(complexityPanel, BorderLayout.SOUTH);
    }

    private void addListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                points.add(e.getPoint());
                repaint();

                if (points.size() == 4) {
                    runAlgorithm(lineIntersectionAlgorithm);
                }
            }
        });
    }

    private void runAlgorithm(LineIntersectionAlgorithm algorithm) {
        lineIntersectionAlgorithm = algorithm;
        AlgoTextField.setText(lineIntersectionAlgorithm.getAlgorithmName());
        long startTime = System.nanoTime();
        List<Line2D> intersections = calculateIntersections(points);
        long endTime = System.nanoTime();
        executionTime = endTime - startTime;
        timeTextField.setText(String.valueOf(executionTime));
        timeComplexityTextField.setText(lineIntersectionAlgorithm.getTimeComplexity());
        spaceComplexityTextField.setText(lineIntersectionAlgorithm.getSpaceComplexity());

        String answer = "No intersection";
        if (!intersections.isEmpty()) {
            answer = "Intersection found";
        }
        answerTextField.setText(answer);
    }

    private List<Line2D> calculateIntersections(List<Point2D> inputPoints) {
        if (lineIntersectionAlgorithm == null) {
            return new ArrayList<>();
        }
        List<Line2D> lines = new ArrayList<>();
        for (int i = 0; i < inputPoints.size() - 1; i += 2) {
            lines.add(new Line2D.Double(inputPoints.get(i), inputPoints.get(i + 1)));
        }
        return lineIntersectionAlgorithm.calculateIntersections(lines);
    }

    private void clear() {
        points.clear();
        executionTime = 0;
        timeTextField.setText("");
        answerTextField.setText("");
        timeComplexityTextField.setText("");
        spaceComplexityTextField.setText("");
        lineIntersectionAlgorithm = null;
        AlgoTextField.setText("");
        repaint();
    }

    private void backToMenu() {
        SwingUtilities.invokeLater(() -> {
            MenuPage menuPage = new MenuPage();
            menuPage.setVisible(true);
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.dispose();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        for (Point2D point : points) {
            g2d.drawString("(" + (int) point.getX() + ", " + (int) point.getY() + ")", (int) point.getX() + 8, (int) point.getY() - 8);
        }
    
        g2d.setColor(Color.RED);
        for (int i = 0; i < points.size() - 1; i += 2) {
            g2d.drawLine((int) points.get(i).getX(), (int) points.get(i).getY(),
                          (int) points.get(i + 1).getX(), (int) points.get(i + 1).getY());
        }
    
        g2d.setColor(Color.BLUE);
        for (Point2D point : points) {
            g2d.fillOval((int) point.getX() - 3, (int) point.getY() - 3, 6, 6);
        }
    }
    


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LineIntersectionPage lineIntersectionPage = new LineIntersectionPage();
            lineIntersectionPage.setVisible(true);
        });
    }
}