package gui;

import javax.swing.*;


public class MemoryManipulationFrame {

    public MemoryManipulationFrame(String strRow, int row, int column, MainFrame mainPICFrame) {
        //zu int + row zu hex
        int hex = Integer.parseInt(strRow, 16);
        hex += column;
        JFrame mainFrame = new JFrame("Manipulate Memory at index " + Integer.toHexString(hex));
        mainFrame.add(buildInput(mainFrame, mainPICFrame, row, column));

        mainFrame.setSize(480, 240);
        mainFrame.setResizable(false);
        mainFrame.setLayout(null);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private JPanel buildInput(JFrame mainFrame, MainFrame mainPICFrame, int row, int column) {
        JPanel mainPanel = new JPanel();
        JPanel upperPanel = new JPanel();
        upperPanel.setBounds(10, 10, 430, 140);
        JLabel label = new JLabel("Neuen Wert eingeben:", SwingConstants.CENTER);
        label.setBounds(10, 60, 210, 20);
        JTextField textField = new JTextField();
        textField.setBounds(210, 60, 210, 20);
        upperPanel.setLayout(null);
        upperPanel.add(label);
        upperPanel.add(textField);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBounds(10, 150, 430, 30);
        JButton button = new JButton("OK");
        button.addActionListener(e -> {
            mainPICFrame.storeMemoryManipulation(row, column, textField.getText());
            mainFrame.dispose();
        });
        button.setSize(60, 20);
        button.setVisible(true);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(button);

        mainPanel.add(bottomPanel);
        mainPanel.add(upperPanel);
        mainPanel.setLayout(null);
        mainPanel.setBounds(10, 10, 460, 220);

        return mainPanel;
    }

}
