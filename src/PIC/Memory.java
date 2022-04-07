package PIC;

import java.nio.charset.StandardCharsets;

public class Memory {

    char w;
    char pcl;
    char ra;
    char rb;
    private int rowsMemory = 32;
    private int columnsMemory = 8;
    private String[][] mainMemory = new String[rowsMemory][columnsMemory];

    public Memory () {
        for (int row = 0; row < rowsMemory; row++) {
            for (int column = 0; column < columnsMemory; column++) {
                mainMemory[row][column] = Integer.toHexString(0);
            }
        }
    }

    public String[][] getMainMemory() {
        return this.mainMemory;
    }

    public void setMainMemoryByIndex (int row, int column, String value) {
        int hex = Integer.parseInt(value, 16);
        this.mainMemory[row][column] = String.valueOf(hex);
        System.out.println("assigned " + value + " as dec " + hex + " to: mainMemory[" + row + "][" + column + "]");
    }

    char getW(){
        return w;
    }

    void setW(char w){
        this.w = w;
    }

}
