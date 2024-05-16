import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPage extends JFrame {
    private JPanel mainPanel;

    public MenuPage() {
        setTitle("Geometric Algorithms Project");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        setLayout();
    }

    private void initComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        JLabel titleLabel = createTitleLabel();
        JLabel participantsLabel = createParticipantsLabel();

        JPanel controlPanel = createControlPanel();

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(participantsLabel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("<html><div style='text-align: center;'>Geometric Algorithms<br>DAA - Project 2023</div></html>");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        return titleLabel;
    }

    private JLabel createParticipantsLabel() {
        JLabel participantsLabel = new JLabel("<html><div style='text-align: center;'>Group Members:<br>21K-3069 Rija Anwar<br>21K-3078 Midhat Masood</div></html>");
        participantsLabel.setForeground(Color.LIGHT_GRAY);
        participantsLabel.setHorizontalAlignment(JLabel.CENTER);
        participantsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        return participantsLabel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBackground(Color.BLACK);

        JButton lineIntersectionButton = createButton("Line Intersection", this::openLineIntersectionPage);
        lineIntersectionButton.setBackground(Color.DARK_GRAY);  
        lineIntersectionButton.setForeground(Color.WHITE);
        JButton convexHullButton = createButton("Convex Hull", this::openConvexHullPage);
        convexHullButton.setBackground(Color.DARK_GRAY);  
        convexHullButton.setForeground(Color.WHITE);

        controlPanel.add(lineIntersectionButton);
        controlPanel.add(convexHullButton);

        return controlPanel;
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        Dimension buttonSize = new Dimension(150, 40);
        button.setPreferredSize(buttonSize);
        button.setBackground(Color.lightGray);
        button.setForeground(Color.BLACK);
        button.addActionListener(actionListener);
        return button;
    }

    private void setLayout() {
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }

    private void openLineIntersectionPage(ActionEvent e) {
        SwingUtilities.invokeLater(() -> {
            LineIntersectionPage lineIntersectionPage = new LineIntersectionPage();
            JFrame frame = new JFrame("Line Intersection Page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);
            frame.add(lineIntersectionPage);
            frame.setVisible(true);
            this.dispose(); 
        });
    }
    
    private void openConvexHullPage(ActionEvent e) {
        SwingUtilities.invokeLater(() -> {
            ConvexHullPage convexHullPage = new ConvexHullPage();
            JFrame frame = new JFrame("Convex Hull Page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);
            frame.add(convexHullPage);
            frame.setVisible(true);
            this.setVisible(false); 
        });
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuPage menuPage = new MenuPage();
            menuPage.setVisible(true);
        });
    }
}
