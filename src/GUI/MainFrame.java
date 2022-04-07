package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainFrame {

    private int rows = 20;
    private int columns = 8;
    private JLabel[][] labels = new JLabel[rows][columns];
    private String[][] labelCommands = new String[rows][columns];

    public MainFrame() {
        JFrame mainFrame = new JFrame("MainFrame");
        //mainFrame.add(buildGridLayoutMemory());
        mainFrame.add(buildMemory());

        mainFrame.setLayout(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1080, 720);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }

    private JPanel buildMemory () {
        JPanel panel = new JPanel();
        GridLayout layout = new GridLayout(rows, columns);
        panel.setLayout(layout);
        //panel.setBackground(Color.gray);
        panel.setBounds(10, 10, 160, 400);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                labels[row][column] = new JLabel("AD", SwingConstants.CENTER);
                labelCommands[row][column] = "clicked: " + row + " "  + column;
                labels[row][column].setFont(new Font("Arial", Font.PLAIN, 10));
                labels[row][column].setBorder(BorderFactory.createLineBorder(Color.gray, 1));
                int rowCommand = row;
                int columnCommand = column;
                labels[row][column].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        mouseEventMemoryClick(new ActionEvent(MainFrame.this, ActionEvent.ACTION_PERFORMED, labelCommands[rowCommand][columnCommand]));
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });
                panel.add(labels[row][column]);
            }
        }
        return panel;
    }

    private void mouseEventMemoryClick (ActionEvent ae) {
        System.out.println(ae.getActionCommand());
    }
}
