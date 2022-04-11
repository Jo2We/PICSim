package PIC;

import java.nio.charset.StandardCharsets;

public class Memory {

    char w;
    char pcl;
    char ra = 0;
    char rb = 0;
    String[] trisa = { "i", "i", "i", "i", "i", "i", "i", "i"};
    String[] trisb = { "i", "i", "i", "i", "i", "i", "i", "i"};
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

    public void setRAPin (int value, int column) {
        if (value == 0) {
            switch (column) {
                case 0: this.ra -= 1; break;
                case 1: this.ra -= 2; break;
                case 2: this.ra -= 4; break;
                case 3: this.ra -= 8; break;
                case 4: this.ra -= 16; break;
                case 5: this.ra -= 32; break;
                case 6: this.ra -= 64; break;
                case 7: this.ra -= 128; break;
                default: break;
            }
        }
        if (value == 1) {
            switch (column) {
                case 0: this.ra += 1; break;
                case 1: this.ra += 2; break;
                case 2: this.ra += 4; break;
                case 3: this.ra += 8; break;
                case 4: this.ra += 16; break;
                case 5: this.ra += 32; break;
                case 6: this.ra += 64; break;
                case 7: this.ra += 128; break;
                default: break;
            }
        }
        System.out.println("RA: " + Integer.toBinaryString(this.ra));
    }

    public void setRBPin (int value, int column) {
        if (value == 0) {
            switch (column) {
                case 0: this.rb -= 1; break;
                case 1: this.rb -= 2; break;
                case 2: this.rb -= 4; break;
                case 3: this.rb -= 8; break;
                case 4: this.rb -= 16; break;
                case 5: this.rb -= 32; break;
                case 6: this.rb -= 64; break;
                case 7: this.rb -= 128; break;
                default: break;
            }
        }
        if (value == 1) {
            switch (column) {
                case 0: this.rb += 1; break;
                case 1: this.rb += 2; break;
                case 2: this.rb += 4; break;
                case 3: this.rb += 8; break;
                case 4: this.rb += 16; break;
                case 5: this.rb += 32; break;
                case 6: this.rb += 64; break;
                case 7: this.rb += 128; break;
                default: break;
            }
        }
        System.out.println("RB: " + Integer.toBinaryString(this.rb));
    }

    public void setRATris (String value, int column) {
        this.trisa[7 - column] = value;
        System.out.print("TRISA: ");
        for (String key : this.trisa) {
            System.out.print(key + " ");
        }
        System.out.println();
    }

    public void setRBTris (String value, int column) {
        this.trisb[7 - column] = value;
        System.out.print("TRISB: ");
        for (String key : this.trisb) {
            System.out.print(key + " ");
        }
        System.out.println();
    }
}
