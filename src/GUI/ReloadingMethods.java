package GUI;

import controller.Controller;

public class ReloadingMethods extends MainFrame {

    public ReloadingMethods(Controller controller) {
        super(controller);
    }

    private void reloadMainMemory() {
        mainMemory = controller.getMainMemory();
        for (int row = 0; row < rowsMemory; row++) {
            for (int column = 0; column < columnsMemory; column++) {
                labelsMemory[row][column + 1].setText(this.controller.getText(this.mainMemory[row * 8 + column]));
            }
        }
    }

    private void reloadCode(int index) {
        this.codeList.setSelectedIndex(index);
       // this.codeList.ensureIndexIsVisible(index);
    }

    private void reloadSpecialFunctionsRegisterVisible() {
        labelsSpecialFunctionsRegisterVisible[0].setText(this.controller.getText(this.controller.getW()));
        labelsSpecialFunctionsRegisterVisible[1].setText(this.controller.getText(this.mainMemory[10]));
        labelsSpecialFunctionsRegisterVisible[2].setText(this.controller.getText(this.controller.getPcl()));
        labelsSpecialFunctionsRegisterVisible[3].setText("insert here");
        labelsSpecialFunctionsRegisterVisible[4].setText(this.controller.getText(this.controller.getStatus()));
    }

    private void reloadLabelsSpecialFunctionsRegisterHidden() {
        labelsSpecialFunctionsRegisterHidden[0].setText("insert here");
        labelsSpecialFunctionsRegisterHidden[1].setText(this.controller.getText(this.controller.getStackPointer()));
    }

    private void reloadTimer(double timer) {
        this.timer = timer;
        this.timerLabel.setText("" + this.timer + "µs");
    }

    private void reloadStatus() {
        for (int index = this.statusLabels.length - 1; index >= 0; index--) {
            this.statusLabels[index].setText(String.valueOf(this.controller.getStatusByIndex(index)));
        }
    }

    public void reloadAll(boolean reloadCode, double timer, int index) {
        this.reloadMainMemory();
        this.reloadSpecialFunctionsRegisterVisible();
        this.reloadLabelsSpecialFunctionsRegisterHidden();
        this.reloadTimer(timer);
        this.reloadStatus();
        this.reloadStackView();
        if (reloadCode) {
            this.reloadCode(index);
        }
    }

    public void reloadStackView () {
        for (int index = this.stackView.length - 1; index >= 0; index--) {
            if (this.stackView[index] != 0) {
                this.stackViewLabels[index].setText(this.controller.getText(this.stackView[index]));
            } else {
                this.stackViewLabels[index].setText("00");
            }
        }
    }
}
