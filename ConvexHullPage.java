import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class ConvexHullPage extends JPanel {
    private ConvexHullAlgorithm convexHullAlgorithm;
    private long executionTime;
    private List<Point2D> points = SharedData.getPoints();
    private JTextField timeTextField;
    private JTextField timeComplexityTextField;
    private JTextField spaceComplexityTextField;
    private JTextField AlgoTextField;

    public ConvexHullPage() {
        initUI();
        addListeners();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JButton bruteForceButton = new JButton("Brute Force");
        bruteForceButton.addActionListener(e -> runAlgorithm(new BruteForceConvexHull()));
        bruteForceButton.setBackground(Color.WHITE);  
        bruteForceButton.setForeground(Color.BLACK);

        JButton grahamScanButton = new JButton("Graham Scan");
        grahamScanButton.addActionListener(e -> runAlgorithm(new GrahamScanConvexHull()));
        grahamScanButton.setBackground(Color.WHITE);  
        grahamScanButton.setForeground(Color.BLACK);

        JButton jarvisButton = new JButton("Jarvis March");
        jarvisButton.addActionListener(e -> runAlgorithm(new JarvisMarchConvexHull()));
        jarvisButton.setBackground(Color.WHITE);  
        jarvisButton.setForeground(Color.BLACK);

        JButton quickHullButton = new JButton("QuickHull");
        quickHullButton.addActionListener(e -> runAlgorithm(new QuickHullConvexHull()));
        quickHullButton.setBackground(Color.WHITE);  
        quickHullButton.setForeground(Color.BLACK);

        JButton divideConquerButton = new JButton("Chan's");
        divideConquerButton.addActionListener(e -> runAlgorithm(new ChanConvexHull()));
        divideConquerButton.setBackground(Color.WHITE);  
        divideConquerButton.setForeground(Color.BLACK);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clear());
        clearButton.setBackground(Color.BLACK);  
        clearButton.setForeground(Color.WHITE);

        JButton backToMenuButton = new JButton("Back to Menu");
        backToMenuButton.addActionListener(e -> backToMenu());
        backToMenuButton.setBackground(Color.BLACK);  
        backToMenuButton.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1)); 
        JPanel buttons = new JPanel();
        buttons.add(bruteForceButton);
        buttons.add(grahamScanButton);
        buttons.add(jarvisButton);
        buttons.add(quickHullButton);
        buttons.add(divideConquerButton);
        buttons.add(clearButton);
        buttons.add(backToMenuButton);

        JPanel textBoxPanel = new JPanel();
        AlgoTextField = new JTextField(10);
        AlgoTextField.setEditable(false);
        textBoxPanel.add(new JLabel("Current Algorithm:"));
        textBoxPanel.add(AlgoTextField);

        buttonPanel.add(buttons);
        buttonPanel.add(textBoxPanel);

        timeTextField = new JTextField(10);
        timeTextField.setEditable(false);
        timeComplexityTextField = new JTextField(10);
        timeComplexityTextField.setEditable(false);
        spaceComplexityTextField = new JTextField(10);
        spaceComplexityTextField.setEditable(false);

        JPanel complexityPanel = new JPanel();
        complexityPanel.add(new JLabel("Execution Time (ns):"));
        complexityPanel.add(timeTextField);
        complexityPanel.add(new JLabel("Time Complexity:"));
        complexityPanel.add(timeComplexityTextField);
        complexityPanel.add(new JLabel("Space Complexity:"));
        complexityPanel.add(spaceComplexityTextField);

        add(buttonPanel, BorderLayout.NORTH);
        add(complexityPanel, BorderLayout.SOUTH);
    }

    private void addListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                convexHullAlgorithm = null;
                SharedData.addPoint(e.getPoint());
                repaint();
            }
        });
    }

    private void runAlgorithm(ConvexHullAlgorithm algorithm) {
        convexHullAlgorithm = algorithm;
        AlgoTextField.setText(convexHullAlgorithm.getAlgorithmName());
        long startTime = System.nanoTime();
        repaint();
        long endTime = System.nanoTime();
        executionTime = endTime - startTime;
        timeTextField.setText(String.valueOf(executionTime));
        timeComplexityTextField.setText(convexHullAlgorithm.getTimeComplexity());
        spaceComplexityTextField.setText(convexHullAlgorithm.getSpaceComplexity());
    }

    private void clear() {
        points.clear();
        executionTime = 0;
        timeTextField.setText("");
        timeComplexityTextField.setText("");
        spaceComplexityTextField.setText("");
        convexHullAlgorithm = null;
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

    private List<Point2D> calculateConvexHull(List<Point2D> inputPoints) {
        if (convexHullAlgorithm == null) {
            return new ArrayList<>();
        }
        return convexHullAlgorithm.calculateConvexHull(inputPoints);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.RED);
        for (Point2D point : points) {
            int x = (int) point.getX();
            int y = (int) point.getY();

            g2d.fillOval(x - 3, y - 3, 6, 6);

            String coordinates = "(" + x + ", " + y + ")";
            g2d.drawString(coordinates, x + 7, y - 7);    
        }

        g2d.setColor(Color.BLUE);
        List<Point2D> convexHull = calculateConvexHull(points);
        for (int i = 0; i < convexHull.size() - 1; i++) {
            Point2D p1 = convexHull.get(i);
            Point2D p2 = convexHull.get(i + 1);
            g2d.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
        }

        if (!convexHull.isEmpty()) {
            Point2D firstPoint = convexHull.get(0);
            Point2D lastPoint = convexHull.get(convexHull.size() - 1);
            g2d.drawLine((int) firstPoint.getX(), (int) firstPoint.getY(), (int) lastPoint.getX(), (int) lastPoint.getY());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConvexHullPage convexHullPage = new ConvexHullPage();
            convexHullPage.setVisible(true);
        });
    }
}
