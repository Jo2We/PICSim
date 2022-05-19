package pic;

import java.util.Arrays;

public class Memory {

    int w;
    String[] trisa = {"i", "i", "i", "i", "i", "i", "i", "i"};
    String[] trisb = {"i", "i", "i", "i", "i", "i", "i", "i"};
    private final int rowsMemory = 32;
    private final int columnsMemory = 8;
    private final int[] mainMemory = new int[rowsMemory * columnsMemory];
    private final int[] stack = new int[8];
    private int stackpointer = 0;
    private int timer = 0;
    private int inhibitTimer = 0;
    int prescaler;
    private int prescalerCounter = 0;

    private boolean switched = false;

    /**
     * set a bit in the main memory 0 or 1
     * checks current value because its a toggle function
     *
     * @param index    byte in main memory
     * @param value    0 or 1
     * @param position bit in byte
     */
    public void setMainMemoryBit(int index, int value, int position) {

        if (index == 0) {
            index = getMainMemory()[4];
        }

        if (index == 1) {
            inhibitTimer += 2;
            prescaler = getMainMemory()[129] & 7;
            prescalerCounter = prescaler;
        }

        if (getMainMemoryBit(3, 5) == 1 && !switched) {
            index += 128;
        }

        if (!(index == 1 || index == 5 || index == 6 || index == 8 || index == 9 || index == 129 || index == 133 || index == 134 || index == 136 || index == 137) && !switched) {
            switched = true;
            setMainMemoryBit((index + 128) % 256, value, position);
        }

        switched = false;

        int currValue = this.getMainMemoryBit(index, position);
        if (currValue == 0 && value == 1) {
            switch (position) {
                case 0 -> mainMemory[index] += 1;
                case 1 -> mainMemory[index] += 2;
                case 2 -> mainMemory[index] += 4;
                case 3 -> mainMemory[index] += 8;
                case 4 -> mainMemory[index] += 16;
                case 5 -> mainMemory[index] += 32;
                case 6 -> mainMemory[index] += 64;
                case 7 -> mainMemory[index] += 128;
                default -> {
                }
            }
        } else if (currValue == 1 && value == 0) {
            switch (position) {
                case 0 -> mainMemory[index] -= 1;
                case 1 -> mainMemory[index] -= 2;
                case 2 -> mainMemory[index] -= 4;
                case 3 -> mainMemory[index] -= 8;
                case 4 -> mainMemory[index] -= 16;
                case 5 -> mainMemory[index] -= 32;
                case 6 -> mainMemory[index] -= 64;
                case 7 -> mainMemory[index] -= 128;
                default -> {
                }
            }
        }
    }

    public void setMainMemoryByIndex(int index, int value) {
        if (index == 0) {
            index = getMainMemory()[4];
        }

        if (getMainMemoryBit(3, 5) == 1 && !switched) {
            index += 128;
        }

        if (index == 1) {
            inhibitTimer += 2;
            int prescaler = getMainMemory()[129] & 7;
            prescalerCounter = (int) Math.pow(2, prescaler + 1);
        }

        if (!(index == 1 || index == 5 || index == 6 || index == 8 || index == 9 || index == 129 || index == 133 || index == 134 || index == 136 || index == 137) && !switched) {
            switched = true;
            setMainMemoryByIndex((index + 128) % 256, value);
        }

        switched = false;

        this.mainMemory[index] = value % 256;
    }

    /**
     * pushes the value to the stack and increases sp by 1 with mod8
     *
     * @param value return adress to be saved on stack
     */
    public void pushStack(int value) {
        stack[stackpointer % 8] = value;
        stackpointer++;
    }

    /**
     * return top of stack and ecrease sp, if not 0
     *
     * @return return adress from stack
     */
    public int popStack() {
        if (stackpointer > 0) {
            stackpointer--;
            return stack[stackpointer];
        }
        return 0;
    }

    /**
     * set method to set TrisA to input ("i") or output ("o"),
     * TrisA is stored as array, the column stands for the index in the array
     *
     * @param value  contains a string with o or i
     * @param column index to set in the array
     */
    public void setRATris(String value, int column) {
        this.trisa[7 - column] = value;
        if (value.equals("o")) {
            this.setMainMemoryBit(0x85, 0, column);
        } else {
            this.setMainMemoryBit(0x85, 1, column);
        }
        System.out.print("TRISA: ");
        for (String key : this.trisa) {
            System.out.print(key + " ");
        }
        System.out.println();
    }

    /**
     * set method to set TrisB to input ("i") or output ("o"),
     * TrisA is stored as array, the column stands for the index in the array
     *
     * @param value  contains a string with o or i
     * @param column index to set in the array
     */
    public void setRBTris(String value, int column) {
        this.trisb[7 - column] = value;
        if (value.equals("o")) {
            this.setMainMemoryBit(0x86, 0, column);
        } else {
            this.setMainMemoryBit(0x86, 1, column);
        }
        System.out.print("TRISB: ");
        for (String key : this.trisb) {
            System.out.print(key + " ");
        }
        System.out.println();
    }

    /**
     * returns the value(0 or 1) of the selcted bit of the selected main memory byte
     *
     * @param index    main memory byte
     * @param position bit in byte
     * @return 0 or 1, value of bit
     */
    public int getMainMemoryBit(int index, int position) {
        String str = String.format("%8s", Integer.toBinaryString(this.mainMemory[index] & 0xFF)).replace(' ', '0');
        return Character.getNumericValue(str.charAt(7 - position));
    }

    /**
     * sets w and main memory = 0
     */
    public void reset() {
        this.setW(0);
        Arrays.fill(this.mainMemory, 0);
        this.mainMemory[0x85] = 0xFF;
        this.mainMemory[0x86] = 0xFF;
    }

    public void increaseTimer(int cycle) {
        this.timer += cycle;
        if (getMainMemoryBit(131, 5) == 0 && inhibitTimer == 0 && prescalerCounter == 0) {
            int value = getMainMemoryByIndex(1) + cycle;
            if (value > 255) {
                value %= 256;
                if (getMainMemoryBit(11, 5) == 1) {
                    setMainMemoryBit(11, 1, 2);
                }
            }
            this.mainMemory[1] = value;
        }


        inhibitTimer = inhibitTimer > 0 ? inhibitTimer - cycle : 0;

        prescaler = getMainMemory()[129] & 7;

        prescalerCounter = prescalerCounter > 0 ? --prescalerCounter : (int) Math.pow(2, prescaler + 1);


    }


    // getter and setter

    public int[] getMainMemory() {
        return this.mainMemory;
    }

    public int getMainMemoryByIndex(int index) {
        if (index == 0) {
            index = getMainMemory()[4];
        }
        return getMainMemory()[index];
    }


    public void setStatus(int position, int value) {
        setMainMemoryBit(3, value, position);
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

    public int getStack() {
        return this.stack[stackpointer];
    }

    public int[] getFullStack() {
        return this.stack;
    }

    public int getStackPointer() {
        return this.stackpointer;
    }

    public int getTimer() {
        return this.timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }
}
