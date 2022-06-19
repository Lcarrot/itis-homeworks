import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

public class Main {

    private Graphics2D graphics;
    private JFrame frame;
    private JPanel panel;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem menuItem;
    private JCheckBoxMenuItem cbMenuItem;
    private JRadioButtonMenuItem rbMenuItem;
    private JMenu submenu;
    private Shape shape;
    private ButtonGroup group;
    private boolean isActiveTriangle = false;
    private boolean isActiveForm = false;
    private JTextField prompt;
    private java.util.List<Component> componentList;

    private final Dimension dim = new Dimension(300, 300);

    public static void main(String[] args) {
        Main main = new Main();
        main.initGUI();
    }

    public void initGUI() {
        frame = new JFrame("Rotating square");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the first menu.
        menu = new JMenu("Menu");
        menuBar.add(menu);

        //a group of JMenuItems
        menuItem = new JMenuItem("Text-only menu item");
        menu.add(menuItem);
        //a group of radio button menu items
        menu.addSeparator();
        group = new ButtonGroup();
        rbMenuItem = new JRadioButtonMenuItem("Radio button");
        rbMenuItem.setSelected(true);
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        //group of check box menu items
        menu.addSeparator();
        cbMenuItem = new JCheckBoxMenuItem("Check box menu item");
        menu.add(cbMenuItem);
        cbMenuItem = new JCheckBoxMenuItem("Another one");
        menu.add(cbMenuItem);

        //submenu
        menu.addSeparator();
        submenu = new JMenu("Submenu");
        menuItem = new JMenuItem("Item in the submenu");
        submenu.add(menuItem);

        //Build second menu in the menu bar.
        menu = new JMenu("About");
        menuBar.add(menu);
        menu.addMenuListener(new MenuListener() {
            private JFrame aboutFrame;

            public void menuSelected(MenuEvent e) {
                aboutFrame = new JFrame("About");
                aboutFrame.setBounds(600, 300, 500, 500);
                aboutFrame.setVisible(true);
                aboutFrame.setAlwaysOnTop(true);
            }

            public void menuDeselected(MenuEvent e) {
                aboutFrame.dispose();
            }

            @Override
            public void menuCanceled(MenuEvent e) {
                throw new UnsupportedOperationException();
            }
        });
        frame.setJMenuBar(menuBar);

        //Build rightPanel
        JPanel rightPanel = new JPanel();
        GridLayout gridLayout = new GridLayout(10, 0, 20, 20);
        rightPanel.setLayout(gridLayout);
        JButton drawButton = new JButton("Draw square");
        rightPanel.add(drawButton);
        drawButton.addActionListener(e -> {
            removeComponentsIfFlagTrue(isActiveForm);
            panel.repaint();
            int width = panel.getSize().width;
            int height = panel.getSize().height;
            shape = new Rectangle((width / 3), (height / 3), width / 3, height / 3);
            SwingUtilities.invokeLater(() -> drawShape(shape, Color.black));
        });
        drawButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                prompt.setText("draw your figure");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                prompt.setText("");
            }
        });
        JButton spinButton = new JButton("Spin Right Round");
        spinButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                prompt.setText("spin your figure");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                prompt.setText("");
            }
        });
        componentList = new LinkedList<>();
        rightPanel.add(spinButton);
        spinButton.addActionListener(e -> {
            isActiveTriangle = !isActiveTriangle;
            new Thread(() -> {
                while (isActiveTriangle) {
                    removeComponentsIfFlagTrue(isActiveForm);
                    panel.repaint();
                    SwingUtilities.invokeLater(() -> {
                        graphics.rotate(Math.toRadians(20), panel.getSize().width / 2.0, panel.getSize().height / 2.0);
                        drawShape(shape, Color.BLACK);
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }).start();
        });
        JButton printButton = new JButton("Print Form");
        //getForm
        printButton.addActionListener(e -> {
            isActiveTriangle = isActiveTriangle ? false : false;
            if (isActiveForm) return;
            isActiveForm = true;
            panel.repaint();
            JLabel lblEmailId = new JLabel("Email Id");
            lblEmailId.setBounds(65, 115, 46, 14);
            panel.add(lblEmailId);
            componentList.add(lblEmailId);

            JTextField textField_2 = new JTextField();
            textField_2.setBounds(128, 112, 247, 17);
            panel.add(textField_2);
            textField_2.setColumns(10);
            componentList.add(textField_2);

            JLabel lblAddress = new JLabel("Address");
            lblAddress.setBounds(65, 162, 46, 14);
            panel.add(lblAddress);
            componentList.add(lblAddress);

            JTextArea textArea_1 = new JTextArea();
            textArea_1.setBounds(126, 157, 212, 40);
            panel.add(textArea_1);
            textField_2.setColumns(10);
            componentList.add(textArea_1);

            JButton btnClear = new JButton("Clear");
            btnClear.addActionListener(s -> {
                textArea_1.setText("");
                textField_2.setText("");
            });
            componentList.add(btnClear);

            btnClear.setBounds(312, 387, 89, 23);
            panel.add(btnClear);

            JLabel lblSex = new JLabel("Sex");
            lblSex.setBounds(65, 228, 46, 14);
            panel.add(lblSex);
            componentList.add(lblSex);

            JLabel lblMale = new JLabel("Male");
            lblMale.setBounds(128, 228, 46, 14);
            panel.add(lblMale);
            componentList.add(lblMale);

            JLabel lblFemale = new JLabel("Female");
            lblFemale.setBounds(292, 228, 46, 14);
            panel.add(lblFemale);
            componentList.add(lblFemale);

            JRadioButton radioButton = new JRadioButton("");
            radioButton.setBounds(337, 224, 109, 23);
            panel.add(radioButton);
            componentList.add(radioButton);

            JRadioButton radioButton_1 = new JRadioButton("");
            radioButton_1.setBounds(162, 224, 109, 23);
            panel.add(radioButton_1);
            componentList.add(radioButton_1);
            JLabel lblOccupation = new JLabel("Occupation");
            lblOccupation.setBounds(65, 288, 67, 14);
            panel.add(lblOccupation);
            componentList.add(lblOccupation);

            JComboBox<String> comboBox = new JComboBox<>();
            comboBox.addItem("Select");
            comboBox.addItem("Business");
            comboBox.addItem("Engineer");
            comboBox.addItem("Doctor");
            comboBox.addItem("Student");
            comboBox.addItem("Others");
            comboBox.addActionListener(event -> {
            });
            comboBox.setBounds(180, 285, 91, 20);
            panel.add(comboBox);
            componentList.add(comboBox);

            JButton btnSubmit = new JButton("submit");
            componentList.add(btnSubmit);
            btnSubmit.addActionListener(p -> {
                removeComponentsIfFlagTrue(isActiveForm);
                isActiveForm = false;
                panel.repaint();
            });

            btnSubmit.setBackground(Color.BLUE);
            btnSubmit.setForeground(Color.MAGENTA);
            btnSubmit.setBounds(65, 387, 89, 23);
            panel.add(btnSubmit);
        });
        printButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                prompt.setText("OH MY");
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                prompt.setText("you can enter your data");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                prompt.setText("");
            }
        });
        rightPanel.add(printButton);
        rightPanel.setBackground(Color.LIGHT_GRAY);
        rightPanel.setPreferredSize(dim);
        frame.getContentPane().add(rightPanel, BorderLayout.EAST);

        //Build mainPanel
        panel = new JPanel();
        panel.setBackground(Color.WHITE);
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        //Build bottomPanel and status
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        prompt = new JTextField();
        prompt.setEditable(false);
        bottomPanel.add(prompt);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.setPreferredSize(new Dimension(frame.getWidth(), 25));
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        frame.setBounds(10, 10, 1900, 1000);
        frame.setVisible(true);
        graphics = (Graphics2D) frame.getGraphics();
    }


    private void removeComponentsIfFlagTrue(boolean flag) {
        if (flag) {
            for (Component component: componentList) {
                panel.remove(component);
            }
        }
    }

    private void drawShape(Shape shape, Color color) {
        graphics.draw(shape);
        graphics.setColor(color);
        graphics.fill(shape);
    }
}
