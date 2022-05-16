package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class MainFrame {

    protected Controller controller;
    protected int rowsMemory = 32;
    protected int columnsMemory = 8;
    protected JLabel[][] labelsMemory = new JLabel[rowsMemory][columnsMemory + 1];
    private final String[][] labelsMemoryCommands = new String[rowsMemory][columnsMemory + 1];
    protected int[] mainMemory;


    private ArrayList<String> fullLines;

    protected JList<String> rowList;
    protected JList<String> codeList;
    protected JLabel[] labelsSpecialFunctionsRegisterVisible = new JLabel[5];
    protected JLabel[] labelsSpecialFunctionsRegisterHidden = new JLabel[2];

    private final String[] statusStrings = {"C", "DC", "Z", "PD", "TO", "RP0", "RP1", "IRP"};

    protected JLabel[] statusLabels = new JLabel[8];

    protected JLabel timerLabel = new JLabel("", SwingConstants.RIGHT);
    protected int timer = 0;

    protected int[] stackView;
    protected JLabel[] stackViewLabels = new JLabel[8];

    public MainFrame(Controller controller) {
        this.controller = controller;
        this.mainMemory = controller.getMainMemory();
        JFrame mainFrame = new JFrame("PIC Simulator");

        mainFrame.add(buildTopLineMemory());
        mainFrame.add(buildMemory());
        mainFrame.add(buildRARB());
        mainFrame.add(buildCodeScrollPane());
        mainFrame.add(buildSpecialFunctionsRegisterVisible());
        mainFrame.add(buildSpecialFunctionRegisterHidden());
        mainFrame.add(buildStatusRegister());
        mainFrame.add(buildButtonControls());
        mainFrame.add(buildTimerView());
        mainFrame.add(buildStackView());

        mainFrame.setLayout(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(990, 900);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }

    /**
     * method to build the visual component of the Main Memory,
     * build with a 2 dimensional array contains an additional column for the left index,
     * the main element is a JPanel with an GridLayout, which contains JLabels with the value
     * of the memory block,
     * every JLabels has an MouseListener to detect if the field is clicked to manipulate the memory block,
     * the values for the JLabels are saved in a global attribute
     * <p>
     * this method is called once in the program to build the view element
     *
     * @return JPanel
     */
    private JPanel buildMemory() {
        JPanel panel = new JPanel();
        GridLayout layout = new GridLayout(rowsMemory, columnsMemory + 1);
        panel.setLayout(layout);
        panel.setBounds(10, 25, 210, 550);
        panel.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        for (int row = 0; row < rowsMemory; row++) {
            labelsMemory[row][0] = new JLabel(Integer.toHexString((int) Math.floor(row/2)).toUpperCase(), SwingConstants.CENTER);
            if (row % 2 == 1) {
                labelsMemory[row][0].setOpaque(true);
                labelsMemory[row][0].setBackground(Color.lightGray);
            }
        }
        for (int row = 0; row < rowsMemory; row++) {
            panel.add(labelsMemory[row][0]);
            for (int column = 1; column < columnsMemory + 1; column++) {
                String content = String.format("%02d", this.mainMemory[row * 8 + (column - 1)]);
                //String.valueOf((int)this.mainMemory[row * 8 + (column - 1)])
                labelsMemory[row][column] = new JLabel(content, SwingConstants.CENTER);
                labelsMemory[row][column].setFont(new Font("Arial", Font.PLAIN, 10));
                labelsMemoryCommands[row][column] = "clicked: " + row + " " + (column - 1);
                if (row % 2 == 0 && (column - 1) % 2 == 1) {
                    labelsMemory[row][column].setOpaque(true);
                    labelsMemory[row][column].setBackground(Color.lightGray);
                }
                else if (row % 2 == 1 && (column - 1) % 2 == 0) {
                    labelsMemory[row][column].setOpaque(true);
                    labelsMemory[row][column].setBackground(Color.lightGray);
                }
                else if (row % 2 == 1 && (column - 1) % 2 == 1) {
                    labelsMemory[row][column].setOpaque(true);
                    labelsMemory[row][column].setBackground(Color.gray);
                }
                int rowCommand = row;
                int columnCommand = (column - 1);
                labelsMemory[row][column].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        mouseEventMemoryClick(new ActionEvent(MainFrame.this, ActionEvent.ACTION_PERFORMED, labelsMemoryCommands[rowCommand][columnCommand]));
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

    /**
     * method to build the headline of the Main Memory for the index of the columns
     *
     * @return JPanel
     */
    private JPanel buildTopLineMemory() {
        JPanel panel = new JPanel();
        GridLayout layout = new GridLayout(1, columnsMemory);
        panel.setLayout(layout);
        panel.setBounds(35, 10, 185, 15);
        for (int column = 0; column < columnsMemory; column++) {
            JLabel label = new JLabel("", SwingConstants.CENTER);
            label.setBorder(BorderFactory.createLineBorder(Color.gray, 1));

            label.setText(column + "/" + Integer.toHexString(column + 8).toUpperCase());
            panel.add(label);

        }
        return panel;
    }

    /**
     * methode to build the view for RA and RB,
     * the main element is a JPanel with a GridLayout,
     * in row 0 and 3 are for the index of RA and RB,
     * in row 1 and 4 are the TRISA and TRISB,
     * in row 2 and 5 are the bits of PINA and PINB,
     * both TRISA/B and PINA/B are using JLabels with MouseListener to detect if they are clicked
     *
     * @return JPanel
     */
    private JPanel buildRARB() {
        JPanel panel = new JPanel();
        GridLayout layout = new GridLayout(6, 8 + 1);
        for (int row = 0; row < 6; row++) {
            for (int column = 8; column >= 0; column--) {
                JLabel label = new JLabel("", SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
                if (row == 0 && column == 8) {
                    label.setText("RA");
                }
                if ((row == 1 || row == 4) && column == 8) {
                    label.setText("Tris");
                    label.setFont(new Font("Arial", Font.PLAIN, 12));
                }
                if ((row == 2 || row == 5) && column == 8) {
                    label.setText("Pin");
                }
                if (row == 3 && column == 8) {
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
        panel.setLayout(layout);
        return panel;
    }

    /**
     * method is call if one block in the Main Memory was clicked,
     * extracts the row and column of the ActionEvent and parses them to int,
     * a new JFrame is created with the row and column as parameter to manipulate the memory block
     *
     * @param ae ActionEvent with payload
     */
    private void mouseEventMemoryClick(ActionEvent ae) {
        System.out.println(ae.getActionCommand());
        String[] str = ae.getActionCommand().substring(9).split(" ");
        int row = Integer.parseInt(str[0]);
        int column = Integer.parseInt(str[1]);
        new MemoryManipulationFrame(Integer.toHexString(row * 8), row, column, this);
    }

    /**
     * method is called to store the manipulation of a memory block,
     * calls the setMainMemoryByIndex with the index of the memory block and the manipulated value
     *
     * @param row    row of the memory block
     * @param column column of the memory block
     * @param value  manipulated value
     */
    public void storeMemoryManipulation(int row, int column, String value) {
        int num = Integer.parseInt(value, 16);
        this.controller.setMainMemoryByIndex(row * 8 + column, num);
        this.labelsMemory[row][column].setText(this.controller.getText((char) num));
    }

    /**
     * method to toggle the value of the TRISA and TRISB,
     * is toggled by click on it, uses the ActionEvent, JLabel, row and column to toggle te value,
     * toggled values are saved in the Memory, calls setRATrisMemory od setRBTrisMemory
     *
     * @param ae     contains the current value, must be toggled from i to o or o to i
     * @param label  the JLabel to toggle
     * @param row    row of the clicked field
     * @param column column of the clicked field
     */
    private void toggleRARBTris(ActionEvent ae, JLabel label, int row, int column) {
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

    /**
     * method to toggle the value of the PINA and PINB,
     * is toggled by click on it, uses the ActionEvent, JLabel, row and column to toggle te value,
     * toggled values are saved in the Memory, calls setBitInMemory depends on RA or RB,
     * RA is saved in block 5 in the Main Memory, RB in block 6
     *
     * @param ae     contains the current value, must be toggled from 1 to 0 or 0 to 1
     * @param label  the JLabel to toggle
     * @param row    row of the clicked field
     * @param column column of the clicked field
     */
    private void toggleRARBPin(ActionEvent ae, JLabel label, int row, int column) {
        if (ae.getActionCommand().equals("1")) {
            label.setText("0");
            if (row == 2) { // RA
                controller.setBitInMemory(5, 0, column);
            }
            if (row == 5) { // RB
                controller.setBitInMemory(6, 0, column);
            }
        } else {
            label.setText("1");
            if (row == 2) { // RA
                controller.setBitInMemory(5, 1, column);
            }
            if (row == 5) { // RB
                controller.setBitInMemory(6, 1, column);
            }
        }
    }

    /**
     * method to build the scrollable Code view,
     * using two JLists, one for the line of the code and the other for the acual code
     *
     * @return JScrollPane
     */
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
        scrollPane.setBounds(260, 200, 650, 600);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return scrollPane;
    }

    /**
     * method to build the JList for the number of lines of the code,
     * with MouseListener for usage with breakpoints
     *
     * @return JList
     */
    private JList<String> buildCodeRowPanel() {
        String[] lines = fullLines.toArray(new String[0]);
        for (int index = 0; index < lines.length; index++) {
            lines[index] = lines[index].substring(20, 25);
        }
        this.rowList = new JList<>(lines);
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

    /**
     * method to build the JList for the code,
     * is disabled
     *
     * @return JList
     */
    private JList<String> buildCodePanel() {
        String[] lines = fullLines.toArray(new String[0]);
        for (int index = 0; index < lines.length; index++) {
            lines[index] = lines[index].substring(26);
        }
        this.codeList = new JList<>(lines);
        this.codeList.setEnabled(false);
        return this.codeList;
    }

    /**
     * method to handle a click on a number of a line in the Code View,
     * selects the line and creates breakpoint
     *
     * @param ae  ActionEvent
     * @param row index of the clicked line
     */
    private void mouseEventCodeRowClick(ActionEvent ae, int row) {
        System.out.println(ae.getActionCommand());
        this.createBreakpoint(row);
    }

    /**
     * method th build the visible special functions register,
     * contains w register, FSR, PCL, PCLATH, Status
     *
     * @return JPanel
     */
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
        labelStatus.setText(this.controller.getText(this.controller.getMainMemory()[3]));
        labelsSpecialFunctionsRegisterVisible[4] = labelStatus;
        panel.add(labelStatus);
        return panel;
    }

    /**
     * method to build the hidden special function register,
     * contains PC and Stackpointer
     *
     * @return JPanel
     */
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

    /**
     * method to build the status register,
     * contains all flags
     *
     * @return JPanel
     */
    private JPanel buildStatusRegister() {
        JPanel panel = new JPanel();
        //panel.setBackground(Color.cyan);
        panel.setBounds(10, 600, 240, 40);
        //panel.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        GridLayout layout = new GridLayout(2, 8);
        panel.setLayout(layout);
        for (int row = 0; row < 2; row++) {
            for (int column = 8 - 1; column >= 0; column--) {
                JLabel label = new JLabel("", SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
                if (row == 0) {
                    label.setText(statusStrings[column]);
                    panel.add(label);
                }
                if (row == 1) {
                    label.setText("" + this.controller.getMainMemoryBit(3, column));
                    this.statusLabels[column] = label;
                    panel.add(label);
                }
            }
        }
        return panel;
    }

    /**
     * method to build the buttons to control the running code,
     * buttons go, reset and continue with ActionListener
     *
     * @return JPanel
     */
    private JPanel buildButtonControls() {
        JPanel panel = new JPanel();
        panel.setBounds(10, 650, 75, 75);
        panel.setBackground(Color.cyan);
        GridLayout layout = new GridLayout(3, 1);
        panel.setLayout(layout);
        JButton goButton = new JButton("Go");
        goButton.setBackground(Color.lightGray);
        goButton.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        goButton.addActionListener(e -> clickedGoButton());
        JButton resetButton = new JButton("Reset");
        resetButton.setBackground(Color.lightGray);
        resetButton.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        resetButton.addActionListener(e -> clickedResetButton());
        JButton continueButton = new JButton("Continue");
        continueButton.setBackground(Color.lightGray);
        continueButton.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        continueButton.addActionListener(e -> clickedContinueButton());
        panel.add(goButton);
        panel.add(resetButton);
        panel.add(continueButton);
        return panel;
    }

    /**
     * method is called if the go button was clicked,
     * sets the go in Controller to true
     */
    private void clickedGoButton() {
        System.out.println("Clicked: Go");
        this.controller.setGo(true);
    }

    /**
     * method is called if the reset button was clicked,
     * sets the timer to 0, triggers the reset of the code and sets the reset in Controller
     */
    private void clickedResetButton() {
        System.out.println("Clicked: Reset");
        this.timer = 0;
        this.controller.reset(this.timer);
        this.controller.setReset(true);
    }


    /**
     * method is called if the continue button was clicked,
     * sets continue in Controller
     */
    private void clickedContinueButton() {
        System.out.println("Clicked: Continue");
        this.controller.setContionueAfterBreakpoint(true);
    }

    /**
     * method to build the view of the runtime timer
     *
     * @return JPanel
     */
    private JPanel buildTimerView() {
        JPanel panel = new JPanel();
        panel.setBounds(100, 650, 150, 50);
        panel.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        GridLayout layout = new GridLayout(2, 1);
        panel.setLayout(layout);
        panel.add(new JLabel("Laufzeit"));
        this.timerLabel.setText("" + this.timer + "µs");
        panel.add(this.timerLabel);
        return panel;
    }

    /**
     * method is called after a click on a row in the code view to create a breakpoint,
     * sets the row of the breakpoint in Controller
     *
     * @param row clicked row
     */
    private void createBreakpoint(int row) {
        System.out.println("Breakpoint at row: " + (row + 1));
        this.controller.setBreakpoint((row + 1));
    }


    /**
     * method to build the visual component of the stack,
     * contains 8 JLabels
     *
     * @return JPanel
     */
    private JPanel buildStackView() {
        JPanel panel = new JPanel();
        panel.setBounds(850, 10, 50, 150);
        //panel.setBackground(Color.cyan);
        GridLayout layout = new GridLayout(8, 1);
        panel.setLayout(layout);
        this.stackView = this.controller.getFullStack();

        for (int index = this.stackView.length - 1; index >= 0; index--) {
            JLabel label = new JLabel("", SwingConstants.RIGHT);
            label.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
            if (this.stackView[index] != 0) {
                label.setText(this.controller.getText(this.stackView[index]));
            } else {
                label.setText("00");
            }
            this.stackViewLabels[index] = label;
            panel.add(label);
        }
        return panel;
    }
}
