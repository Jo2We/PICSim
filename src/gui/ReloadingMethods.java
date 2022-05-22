package gui;

import controller.Controller;

public class ReloadingMethods extends MainFrame {

    public ReloadingMethods(Controller controller) {
        super(controller);
    }

    /**
     * method to reload the Main Memory
     * gets the Main Memory and sets the text of each JLabel with the Main Memory
     */
    private void reloadMainMemory() {
        this.mainMemory = this.controller.getMainMemory();
        for (int row = 0; row < rowsMemory; row++) {
            for (int column = 1; column < columnsMemory + 1; column++) {
                labelsMemory[row][column].setText(this.controller.getText(this.mainMemory[row * 8 + (column - 1)]));
            }
        }
    }


    /**
     * method to select the current line in the Code View
     *
     * @param line selected line
     */
    private void reloadCode(int line) {
        this.codeList.setSelectedIndex(line);
        //this.codeList.ensureIndexIsVisible(line);
    }

    /**
     * method to reload the visible Special Functions Register
     * reloads w register, FSR, PCL, PCLATH and Status
     */
    private void reloadSpecialFunctionsRegisterVisible() {
        labelsSpecialFunctionsRegisterVisible[0].setText(this.controller.getText(this.controller.getW()));
        labelsSpecialFunctionsRegisterVisible[1].setText(this.controller.getText(this.mainMemory[4]));
        labelsSpecialFunctionsRegisterVisible[2].setText(this.controller.getText(this.controller.getPc()));
        labelsSpecialFunctionsRegisterVisible[3].setText("insert here");
        labelsSpecialFunctionsRegisterVisible[4].setText(this.controller.getText(this.controller.getMainMemory()[3]));
    }

    /**
     * method to reload the hidden Special Functions Register
     * reloads PC and Stackpointer
     */
    private void reloadLabelsSpecialFunctionsRegisterHidden() {
        labelsSpecialFunctionsRegisterHidden[0].setText("insert here");
        labelsSpecialFunctionsRegisterHidden[1].setText(this.controller.getText(this.controller.getStackPointer()));
    }

    /**
     * method to reload the timer
     *
     * @param timer value of the timer
     */
    private void reloadTimer(int timer) {
        this.timer = timer;
        this.timerLabel.setText("" + this.timer + "µs");
    }

    /**
     * method to reload the Status, INTCON and OPTION Register
     */
    private void reloadRegisters() {
        for (int index = this.statusLabels.length - 1; index >= 0; index--) {
            this.statusLabels[index].setText(String.valueOf(this.controller.getMainMemoryBit(3, index)));
            this.intconLabels[index].setText(String.valueOf(this.controller.getMainMemoryBit(0x0B, index)));
            this.optionLabels[index].setText(String.valueOf(this.controller.getMainMemoryBit(0x81, index)));
        }
    }

    /**
     * method for simple usage, calls every reloading method if selected
     * the Code needs to be reloaded while the code is running, if not the reloadCode is false and
     * the timer and index can be ignored, if not they are functional
     *
     * @param reloadCode selection if the code needs to be reloaded or not
     * @param timer      value of the timer
     * @param index      line of the current code
     */
    public void reloadAll(boolean reloadCode, int timer, int index) {
        this.reloadMainMemory();
        this.reloadSpecialFunctionsRegisterVisible();
        this.reloadLabelsSpecialFunctionsRegisterHidden();
        this.reloadRegisters();
        this.reloadStackView();
        if (reloadCode) {
            this.reloadTimer(timer);
            this.reloadCode(index);
            this.reloadTris();
            this.reloadPins();
        }
    }

    /**
     * method to reload the stack view
     */
    private void reloadStackView() {
        for (int index = this.stackView.length - 1; index >= 0; index--) {
            if (this.stackView[index] != 0) {
                this.stackViewLabels[index].setText(this.controller.getText(this.stackView[index]));
            } else {
                this.stackViewLabels[index].setText("00");
            }
        }
    }

    /**
     * method to reload the pins view
     */
    private void reloadPins () {
        if (!(this.ra == this.mainMemory[0x05]) || !(this.rb == this.mainMemory[0x06])) {
            this.ra = this.mainMemory[0x05];
            this.rb = this.mainMemory[0x06];
            for (int index = 0; index < 8; index++) {
                this.raLabels[index].setText("" + this.controller.getMainMemoryBit(0x05, index));
                this.rbLabels[index].setText("" + this.controller.getMainMemoryBit(0x06, index));
            }
        }
    }

    /**
     * method to reload the tris view
     */
    private void reloadTris () {
        if (!(this.trisa == this.mainMemory[0x85]) || !(this.trisb == this.mainMemory[0x86])) {
            this.trisa = this.mainMemory[0x85];
            this.trisb = this.mainMemory[0x86];
            for (int index = 0; index < 8; index++) {
                this.trisaLabels[index].setText("" + (this.controller.getMainMemoryBit(0x85, index) == 1 ? "i" : "o"));
                this.trisbLabels[index].setText("" + (this.controller.getMainMemoryBit(0x86, index) == 1 ? "i" : "o"));
            }
        }
    }
}
