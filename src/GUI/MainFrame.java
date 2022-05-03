package GUI;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class MainFrame {

    protected Controller controller;
    protected int rowsMemory = 32;
    protected int columnsMemory = 8;
    protected JLabel[][] labelsMemory = new JLabel[rowsMemory][columnsMemory + 1];
    private String[][] labelsMemonryCommands = new String[rowsMemory][columnsMemory + 1];
    protected int[] mainMemory;

    private int rowsRARB = 6;
    private int columnsRARB = 8;
    private JLabel[][] labelsRARB = new JLabel[rowsRARB][columnsRARB + 1];

    private String memoryManipulation = "0";

    private ArrayList<String> fullLines;

    protected JList rowList;
    protected JList codeList;
    protected JLabel[] labelsSpecialFunctionsRegisterVisible = new JLabel[5];
    protected JLabel[] labelsSpecialFunctionsRegisterHidden = new JLabel[2];

    private int statusRows = 2;
    private int statusColumns = 8;
    private String[] statusStrings = {"C", "DC", "Z", "PD", "TO", "RP0", "RP1", "IRP"};

    protected JLabel[] statusLabels = new JLabel[8];

    protected JLabel timerLabel = new JLabel("", SwingConstants.RIGHT);
    protected double timer = 0.0;

    public MainFrame(Controller controller) {
        this.controller = controller;
        this.mainMemory = controller.getMainMemory();
        JFrame mainFrame = new JFrame("PIC Simulator");

        mainFrame.add(buildTopLineMemory());
        mainFrame.add(buildScrollView());
        mainFrame.add(buildRARB());
        mainFrame.add(buildCodeScrollPane());
        mainFrame.add(buildSpecialFunctionsRegisterVisible());
        mainFrame.add(buildSpecialFunctionRegisterHidden());
        mainFrame.add(buildStatusRegister());
        mainFrame.add(buildButtonControls());
        mainFrame.add(buildTimerView());

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
                String content = String.format("%02d", (int) this.mainMemory[row * 8 + (column - 1)]);
                //String.valueOf((int)this.mainMemory[row * 8 + (column - 1)])
                labelsMemory[row][column] = new JLabel(content, SwingConstants.CENTER);
                labelsMemory[row][column].setFont(new Font("Arial", Font.PLAIN, 10));
                labelsMemory[row][column].setBorder(BorderFactory.createLineBorder(Color.gray, 1));
                labelsMemonryCommands[row][column] = "clicked: " + row + " " + column;
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

    private ScrollPane buildScrollView() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setBounds(10, 25, 240, 300);
        scrollPane.add(buildMemory());
        return scrollPane;
    }

    private JPanel buildTopLineMemory() {
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

    private JPanel buildRARB() {
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
        String[] str = ae.getActionCommand().substring(9).split(" ");
        int row = Integer.parseInt(str[0]);
        int column = Integer.parseInt(str[1]);
        MemoryManipulationFrame memoryManipulationFrame = new MemoryManipulationFrame(Integer.toHexString(row * 8), row, column, this);
    }

    public void storeMemoryManipulation(int row, int column, String value) {
        int num = Integer.parseInt(value, 16);
        controller.setMainMemoryByIndex(row * 8 + column, num);
        this.labelsMemory[row][column].setText(this.controller.getText((char) num));
    }

    private void toggleRARBTris(ActionEvent ae, JLabel label, int row, int column) {
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

    private void toggleRARBPin(ActionEvent ae, JLabel label, int row, int position) {
        if (ae.getActionCommand().equals("1")) {
            label.setText("0");
            if (row == 2) { // RA
                controller.setBitInMemory(5, 0, position);
            }
            if (row == 5) { // RB
                controller.setBitInMemory(6, 0, position);
            }
        } else {
            label.setText("1");
            if (row == 2) { // RA
                controller.setBitInMemory(5, 1, position);
            }
            if (row == 5) { // RB
                controller.setBitInMemory(6, 1, position);
            }
        }
    }

    private JScrollPane buildCodeScrollPane() {
        this.fullLines = this.controller.getFullLines();
        JSplitPane panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JPanel panelRows = new JPanel();
        panelRows.setBackground(Color.gray);
        panelRows.add(new JLabel("Rows"));
        JPanel panelCode = new JPanel();
        panelCode.setBackground(Color.gray);
        panelCode.add(new JLabel("Code"));

        panel.setDividerLocation(0.1);
        panel.setLeftComponent(buildCodeRowPanel());
        panel.setRightComponent(buildCodePanel());
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBounds(260, 200, 650, 450);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return scrollPane;
    }

    private JList buildCodeRowPanel() {
        String[] lines = fullLines.toArray(new String[0]);
        for (int index = 0; index < lines.length; index++) {
            lines[index] = lines[index].substring(20, 25);
            //System.out.println(lines[index]);
        }
        this.rowList = new JList(lines);
        this.rowList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mouseEventCodeRowClick(new ActionEvent(MainFrame.this, ActionEvent.ACTION_PERFORMED, "clicked on row: " + (rowList.getSelectedIndex() + 1)), rowList.getSelectedIndex());
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
        return this.rowList;
    }

    private JList buildCodePanel() {
        String[] lines = fullLines.toArray(new String[0]);
        for (int index = 0; index < lines.length; index++) {
            lines[index] = lines[index].substring(26);
            //System.out.println(lines[index]);
        }
        this.codeList = new JList(lines);
        this.codeList.setEnabled(false);
        return this.codeList;
    }

    private void mouseEventCodeRowClick(ActionEvent ae, int row) {
        System.out.println(ae.getActionCommand());
        //this.codeList.setSelectedIndex(row);
        this.createBreakpoint(row);
    }

    private JPanel buildSpecialFunctionsRegisterVisible() {
        JPanel panel = new JPanel();
        panel.setBounds(500, 10, 150, 150);
        panel.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        GridLayout layout = new GridLayout(6, 2);
        panel.setLayout(layout);
        panel.add(new JLabel("sichtbar"));
        panel.add(new JLabel(""));
        panel.add(new JLabel("W-Register"));
        JLabel labelWRegister = new JLabel("", SwingConstants.CENTER);
        labelWRegister.setText(this.controller.getText(this.controller.getW()));
        labelsSpecialFunctionsRegisterVisible[0] = labelWRegister;
        panel.add(labelWRegister);
        panel.add(new JLabel("FSR"));
        JLabel labelFsr = new JLabel("", SwingConstants.CENTER);
        labelFsr.setText("insert here");
        labelsSpecialFunctionsRegisterVisible[1] = labelFsr;
        panel.add(labelFsr);
        panel.add(new JLabel("PCL"));
        JLabel labelPcl = new JLabel("", SwingConstants.CENTER);
        labelPcl.setText(this.controller.getText(this.controller.getPcl()));
        labelsSpecialFunctionsRegisterVisible[2] = labelPcl;
        panel.add(labelPcl);
        panel.add(new JLabel("PCLATH"));
        JLabel labelPclath = new JLabel("", SwingConstants.CENTER);
        labelPclath.setText(this.controller.getText(this.mainMemory[10]));
        labelsSpecialFunctionsRegisterVisible[3] = labelPclath;
        panel.add(labelPclath);
        panel.add(new JLabel("Status"));
        JLabel labelStatus = new JLabel("", SwingConstants.CENTER);
        labelStatus.setText(this.controller.getText(this.controller.getStatus()));
        labelsSpecialFunctionsRegisterVisible[4] = labelStatus;
        panel.add(labelStatus);
        return panel;
    }

    private JPanel buildSpecialFunctionRegisterHidden() {
        JPanel panel = new JPanel();
        panel.setBounds(675, 10, 150, 150);
        panel.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        GridLayout layout = new GridLayout(5, 2);
        panel.setLayout(layout);
        panel.add(new JLabel("versteckt"));
        panel.add(new JLabel(""));
        panel.add(new JLabel("PC"));
        JLabel labelPc = new JLabel("", SwingConstants.CENTER);
        labelPc.setText("insert here");
        labelsSpecialFunctionsRegisterHidden[0] = labelPc;
        panel.add(labelPc);
        panel.add(new JLabel("Stackpointer"));
        JLabel labelStackpointer = new JLabel("", SwingConstants.CENTER);
        labelStackpointer.setText(this.controller.getText(this.controller.getStack()));
        labelsSpecialFunctionsRegisterHidden[1] = labelStackpointer;
        panel.add(labelStackpointer);
        return panel;
    }

    private JPanel buildStatusRegister() {
        JPanel panel = new JPanel();
        //panel.setBackground(Color.cyan);
        panel.setBounds(10, 350, 240, 40);
        panel.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        GridLayout layout = new GridLayout(this.statusRows, this.statusColumns);
        panel.setLayout(layout);
        for (int row = 0; row < this.statusRows; row++) {
            for (int column = this.statusColumns - 1; column >= 0; column--) {
                JLabel label = new JLabel("", SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
                if (row == 0) {
                    label.setText(statusStrings[column]);
                    panel.add(label);
                }
                if (row == 1) {
                    label.setText("" + this.controller.getStatusByIndex(column));
                    this.statusLabels[column] = label;
                    panel.add(label);
                }
            }
        }
        return panel;
    }

    private JPanel buildButtonControls() {
        JPanel panel = new JPanel();
        panel.setBounds(10, 400, 75, 75);
        panel.setBackground(Color.cyan);
        GridLayout layout = new GridLayout(3, 1);
        panel.setLayout(layout);
        JButton goButton = new JButton("Go");
        goButton.setBackground(Color.lightGray);
        goButton.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickedGoButton();
            }
        });
        JButton resetButton = new JButton("Reset");
        resetButton.setBackground(Color.lightGray);
        resetButton.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickedResetButton();
            }
        });
        JButton continueButton = new JButton("Continue");
        continueButton.setBackground(Color.lightGray);
        continueButton.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickedContinueButton();
            }
        });
        panel.add(goButton);
        panel.add(resetButton);
        panel.add(continueButton);
        return panel;
    }

    private void clickedGoButton() {
        System.out.println("Clicked: Go");
        this.controller.setGo(true);
    }

    private void clickedResetButton() {
        System.out.println("Clicked: Reset");
        this.timer = 0.0;
        this.controller.reset(this.timer);
        this.controller.setReset(true);
    }

    private void clickedContinueButton () {
        System.out.println("Clicked: Continue");
        this.controller.setContionueAfterBreakpoint(true);
    }

    private JPanel buildTimerView() {
        JPanel panel = new JPanel();
        panel.setBounds(100, 400, 150, 50);
        panel.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        GridLayout layout = new GridLayout(2, 1);
        panel.setLayout(layout);
        panel.add(new JLabel("Laufzeit"));
        this.timerLabel.setText("" + this.timer + "µs");
        panel.add(this.timerLabel);
        return panel;
    }

    private void createBreakpoint(int row) {
        System.out.println("Breakpoint at row: " + (row + 1));
        this.controller.setBreakpoint((row + 1));
    }
}
