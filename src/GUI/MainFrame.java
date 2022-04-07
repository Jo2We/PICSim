package GUI;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainFrame {

    private int rowsMemory = 32;
    private int columnsMemory = 8;
    private JLabel[][] labelsMemory = new JLabel[rowsMemory][columnsMemory + 1];
    private String[][] labelsMemonryCommands = new String[rowsMemory][columnsMemory + 1];
    private String[][] mainMemory = new String[rowsMemory][columnsMemory];

    private int rowsRARB = 4;
    private int columnsRARB = 8;
    private JLabel[][] labelsRARB = new JLabel[rowsRARB][columnsRARB + 1];

    private Controller controller;

    public MainFrame(Controller controller) {
        this.controller = controller;
        this.mainMemory = controller.getMainMemory();
        JFrame mainFrame = new JFrame("MainFrame");
        //mainFrame.add(buildGridLayoutMemory());
        //mainFrame.add(buildMemory());

        mainFrame.add(buildTopLineMemory());
        mainFrame.add(buildMemory());
        mainFrame.add(buildScrollView());
        mainFrame.add(buildRARB());

        mainFrame.setLayout(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1080, 720);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }

    private JPanel buildMemory() {
        JPanel panel = new JPanel();
        GridLayout layout = new GridLayout(rowsMemory, columnsMemory + 1);
        panel.setLayout(layout);
        for (int row = 0; row < rowsMemory; row++) {
            labelsMemory[row][0] = new JLabel(Integer.toHexString(row * 8), SwingConstants.CENTER);
        }
        for (int row = 0; row < rowsMemory; row++) {
            panel.add(labelsMemory[row][0]);
            for (int column = 1; column < columnsMemory + 1; column++) {
                labelsMemory[row][column] = new JLabel(this.mainMemory[row][column - 1], SwingConstants.CENTER);
                labelsMemonryCommands[row][column] = "clicked: " + row + " " + column;
                labelsMemory[row][column].setFont(new Font("Arial", Font.PLAIN, 10));
                labelsMemory[row][column].setBorder(BorderFactory.createLineBorder(Color.gray, 1));
                int rowCommand = row;
                int columnCommand = column;
                labelsMemory[row][column].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        mouseEventMemoryClick(new ActionEvent(MainFrame.this, ActionEvent.ACTION_PERFORMED, labelsMemonryCommands[rowCommand][columnCommand]));
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

                panel.add(labelsMemory[row][column]);
            }
        }
        return panel;
    }

    private ScrollPane buildScrollView () {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setBounds(10, 25, 240, 300);
        scrollPane.add(buildMemory());
        return scrollPane;
    }

    private JPanel buildTopLineMemory () {
        JPanel panel = new JPanel();
        GridLayout layout = new GridLayout(1, columnsMemory);
        panel.setLayout(layout);
        panel.setBounds(36, 10, 195, 15);
        for (int column = 0; column < columnsMemory; column++) {
            JLabel label = new JLabel("", SwingConstants.CENTER);
            label.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
            if (column == 0) {
                label.setText("00");
                panel.add(label);
            }
            if (column > 0) {
                label.setText("0" + column);
                panel.add(label);
            }
        }
        return panel;
    }

    private JPanel buildRARB () {
        JPanel panel = new JPanel();
        GridLayout layout = new GridLayout(rowsRARB, columnsRARB);
        panel.setBounds(250, 10, 180, 80);
        //panel.setBackground(Color.gray);
        panel.setLayout(layout);


        return panel;
    }

    private void mouseEventMemoryClick(ActionEvent ae) {
        System.out.println(ae.getActionCommand());
        String [] str = ae.getActionCommand().substring(9).split(" ");
        int row = Integer.parseInt(str[0]);
        int column = Integer.parseInt(str[1]);
        controller.setMainMemoryByIndex(row, column, "a5");
    }
}
