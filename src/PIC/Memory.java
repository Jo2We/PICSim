package PIC;

import java.nio.charset.StandardCharsets;

public class Memory {

    char w;

    String[] trisa = {"i", "i", "i", "i", "i", "i", "i", "i"};
    String[] trisb = {"i", "i", "i", "i", "i", "i", "i", "i"};
    private int rowsMemory = 32;
    private int columnsMemory = 8;
    private char[] mainMemory = new char[rowsMemory * 8 + columnsMemory];
    private char[] stack = new char[8];
    private int stackpointer = 0;

    public Memory() {
        for (int row = 0; row < rowsMemory; row++) {
            for (int column = 0; column < columnsMemory; column++) {
                mainMemory[row * 8 + column] = 0;
            }
        }
    }

    public char[] getMainMemory() {
        return this.mainMemory;
    }

    public void setMainMemoryByIndex(int index, int value) {
        //int hex = Integer.parseInt(value, 16);
        this.mainMemory[index] = (char) value;
    }

    public char getW() {
        return w;
    }

    public void setW(char w) {
        this.w = w;
    }

    public char getPcl() {
        return mainMemory[2];
    }

    public void setPcl(char pcl) {
        mainMemory[2] = pcl;
    }

    public void pushStack(int value) {
        stack[stackpointer % 8] = (char) value;
        stackpointer++;
    }

    public int popStack(){
        if (stackpointer > 0){
            stackpointer--;
            return stack[stackpointer];
        }
        return 0;
    }

    public char getStack() {
        return this.stack[stackpointer];
    }

    public void setStatus(int position){
        setMainMemoryBit(3, 1, position);
    }

    public void resetStatus(){
        setMainMemoryByIndex(3, 0);
    }

    public char getStatus () {
        return this.mainMemory[3];
    }

    public void setMainMemoryBit(int index, int value, int position) {
        if (value == 0) {
            switch (position) {
                case 0:
                    mainMemory[index] -= 1;
                    break;
                case 1:
                    mainMemory[index] -= 2;
                    break;
                case 2:
                    mainMemory[index] -= 4;
                    break;
                case 3:
                    mainMemory[index] -= 8;
                    break;
                case 4:
                    mainMemory[index] -= 16;
                    break;
                case 5:
                    mainMemory[index] -= 32;
                    break;
                case 6:
                    mainMemory[index] -= 64;
                    break;
                case 7:
                    mainMemory[index] -= 128;
                    break;
                default:
                    break;
            }
        }
        if (value == 1) {
            switch (position) {
                case 0:
                    mainMemory[index] += 1;
                    break;
                case 1:
                    mainMemory[index] += 2;
                    break;
                case 2:
                    mainMemory[index] += 4;
                    break;
                case 3:
                    mainMemory[index] += 8;
                    break;
                case 4:
                    mainMemory[index] += 16;
                    break;
                case 5:
                    mainMemory[index] += 32;
                    break;
                case 6:
                    mainMemory[index] += 64;
                    break;
                case 7:
                    mainMemory[index] += 128;
                    break;
                default:
                    break;
            }
        }
    }

    public void setRATris(String value, int column) {
        this.trisa[7 - column] = value;
        System.out.print("TRISA: ");
        for (String key : this.trisa) {
            System.out.print(key + " ");
        }
        System.out.println();
    }

    public void setRBTris(String value, int column) {
        this.trisb[7 - column] = value;
        System.out.print("TRISB: ");
        for (String key : this.trisb) {
            System.out.print(key + " ");
        }
        System.out.println();
    }
}
