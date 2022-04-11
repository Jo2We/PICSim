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

    private int rowsRARB = 6;
    private int columnsRARB = 8;
    private JLabel[][] labelsRARB = new JLabel[rowsRARB][columnsRARB + 1];

    private String memoryManipulation = "0";

    private Controller controller;

    public MainFrame(Controller controller) {
        this.controller = controller;
        this.mainMemory = controller.getMainMemory();
        JFrame mainFrame = new JFrame("PIC Simulator");
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
        panel.setBounds(34, 10, 178, 15);
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
        GridLayout layout = new GridLayout(rowsRARB, columnsRARB + 1);
        for (int row = 0; row < rowsRARB; row++) {
            for (int column = columnsRARB; column >= 0; column--) {
                JLabel label = new JLabel("", SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
                if (row == 0 && column == columnsRARB) {
                    label.setText("RA");
                }
                if ((row == 1 || row == 4) && column == columnsRARB) {
                    label.setText("Tris");
                    label.setFont(new Font("Arial", Font.PLAIN, 12));
                }
                if ((row == 2 || row == 5) && column == columnsRARB) {
                    label.setText("Pin");
                }
                if (row == 3 && column == columnsRARB) {
                    label.setText("RB");
                }
                if ((row == 0 || row == 3) && column < 8) {
                    label.setText("0" + column);
                }
                int ro = row;
                int col = column;
                if ((row == 1 || row == 4) && column < 8) {
                    label.setText("i");
                    label.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            toggleRARBTris(new ActionEvent(MainFrame.this, ActionEvent.ACTION_PERFORMED, label.getText()), label, ro, col);
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
                }
                if ((row == 2 || row == 5) && column < 8) {
                    label.setText("0");
                    label.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            toggleRARBPin(new ActionEvent(MainFrame.this, ActionEvent.ACTION_PERFORMED, label.getText()), label, ro, col);
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
                }
                panel.add(label);
            }
        }
        panel.setBounds(260, 10, 200, 120);
        //panel.setBackground(Color.gray);
        panel.setLayout(layout);


        return panel;
    }

    private void mouseEventMemoryClick(ActionEvent ae) {
        System.out.println(ae.getActionCommand());
        String [] str = ae.getActionCommand().substring(9).split(" ");
        int row = Integer.parseInt(str[0]);
        int column = Integer.parseInt(str[1]);
        MemoryManipulationFrame memoryManipulationFrame = new MemoryManipulationFrame(Integer.toHexString(row * 8), row, column, this);
    }

    public void storeMemoryManipulation (int row, int column, String value) {
        controller.setMainMemoryByIndex(row, column, value);
        this.labelsMemory[row][column].setText(value);
    }

    private void reloadMainMemory() {
        mainMemory = controller.getMainMemory();
        for (int row = 0; row < rowsMemory; row++) {
            for (int column = 0; column < columnsMemory; column++) {
                labelsMemory[row][column + 1].setText(mainMemory[row][column]);
            }
        }
    }

    private void toggleRARBTris (ActionEvent ae, JLabel label, int row, int column) {
        //System.out.println(ae.getActionCommand());
        if (ae.getActionCommand().equals("i")) {
            label.setText("o");
            if (row == 1) {
                controller.setRATrisMemory("o", column);
            }
            if (row == 4) {
                controller.setRBTrisMemory("o", column);
            }
        } else {
            label.setText("i");
            if (row == 1) {
                controller.setRATrisMemory("i", column);
            }
            if (row == 4) {
                controller.setRBTrisMemory("i", column);
            }
        }
    }

    private void toggleRARBPin (ActionEvent ae, JLabel label, int row, int column) {
        //System.out.println(ae.getActionCommand() + " " + column);
        if (ae.getActionCommand().equals("1")) {
            label.setText("0");
            if (row == 2) { // RA
                controller.setRAPinMemory(0, column);
            }
            if (row == 5) { // RB
                controller.setRBPinMemory(0, column);
            }
        } else {
            label.setText("1");
            if (row == 2) { // RA
                controller.setRAPinMemory(1, column);
            }
            if (row == 5) { // RB
                controller.setRBPinMemory(1, column);
            }
        }
    }
}