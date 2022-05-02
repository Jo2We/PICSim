package PIC;

public class Memory {

    int w;

    String[] trisa = {"i", "i", "i", "i", "i", "i", "i", "i"};
    String[] trisb = {"i", "i", "i", "i", "i", "i", "i", "i"};
    private int rowsMemory = 32;
    private int columnsMemory = 8;
    private int[] mainMemory = new int[rowsMemory * 8 + columnsMemory];
    private int[] stack = new int[8];
    private int stackpointer = 0;

    public Memory() {
        for (int row = 0; row < rowsMemory; row++) {
            for (int column = 0; column < columnsMemory; column++) {
                mainMemory[row * 8 + column] = 0;
            }
        }
    }

    public int[] getMainMemory() {
        return this.mainMemory;
    }

    public void setMainMemoryByIndex(int index, int value) {
        //int hex = Integer.parseInt(value, 16);
        this.mainMemory[index] = value;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getPcl() {
        return mainMemory[2];
    }

    public void setPcl(int pcl) {
        mainMemory[2] = pcl;
    }

    public void pushStack(int value) {
        stack[stackpointer % 8] = value;
        stackpointer++;
    }

    public int popStack() {
        if (stackpointer > 0) {
            stackpointer--;
            return stack[stackpointer];
        }
        return 0;
    }

    public int getStack() {
        return this.stack[stackpointer];
    }

    public void setStatus(int position, int value) {
        int currValue = this.checkStatusIndex(position);
        if (currValue == 48 && value == 1) {
            setMainMemoryBit(3, 1, position);
        } else if (currValue == 49 && value == 0) {
            setMainMemoryBit(3, 0, position);
        }
    }

    public char checkStatusIndex(int index) {
        String str = String.format("%8s", Integer.toBinaryString(this.mainMemory[3] & 0xFF)).replace(' ', '0');
        return str.charAt(7-index);
    }

    public void resetStatus() {
        setMainMemoryByIndex(3, 0);
    }

    public int getStatus() {
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

    public char getStatusByIndex(int index) {
        String str = String.format("%8s", Integer.toBinaryString(this.mainMemory[3] & 0xFF)).replace(' ', '0');
        return str.charAt(7-index);
    }

    public void reset() {
        this.setW(0);
        for (int index = 0; index < this.mainMemory.length; index++) {
            this.mainMemory[index] = 0;
        }
    }
}
