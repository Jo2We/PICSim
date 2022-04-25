package GUI;

import controller.Controller;

import java.util.ArrayList;

public class ReloadingMethods extends MainFrame {

    public ReloadingMethods(Controller controller) {
        super(controller);
    }

    public void reloadMainMemory() {
        mainMemory = controller.getMainMemory();
        for (int row = 0; row < rowsMemory; row++) {
            for (int column = 0; column < columnsMemory; column++) {
                labelsMemory[row][column + 1].setText(this.controller.getText(this.mainMemory[row * 8 + column]));
            }
        }
    }

    public void reloadCode(int index) {
        this.codeList.setSelectedIndex(index);
    }

    public void reloadSpecialFunctionsRegisterVisible() {
        labelsSpecialFunctionsRegisterVisible[0].setText(this.controller.getText(this.controller.getW()));
        labelsSpecialFunctionsRegisterVisible[1].setText(this.controller.getText(this.mainMemory[10]));
        labelsSpecialFunctionsRegisterVisible[2].setText(this.controller.getText(this.controller.getPcl()));
        labelsSpecialFunctionsRegisterVisible[3].setText("insert here");
        labelsSpecialFunctionsRegisterVisible[4].setText(this.controller.getText(this.controller.getStatus()));
    }
    public void reloadLabelsSpecialFunctionsRegisterHidden() {
        labelsSpecialFunctionsRegisterHidden[0].setText("insert here");
        labelsSpecialFunctionsRegisterHidden[1].setText(this.controller.getText(this.controller.getStack()));
    }
    public void reloadTimer (double timer) {
        this.timer = timer;
        this.timerLabel.setText("" + this.timer + "µs");
    }
}
