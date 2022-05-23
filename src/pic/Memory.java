package pic;

import controller.Controller;

import java.util.Arrays;

public class Memory {
    private final Controller controller;
    int w;

    int pc = 0;

    private final int rowsMemory = 32;
    private final int columnsMemory = 8;
    private final int[] mainMemory = new int[rowsMemory * columnsMemory];
    private final int[] stack = new int[8];
    private int stackpointer = 0;
    private int timer = 0;
    private int inhibitTimer = 0;
    int prescaler;
    private int prescalerCounter = 0;

    private int watchdog = 0;
    private boolean switched = false;

    public Memory(Controller controller) {
        this.controller = controller;
    }

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
            index = mainMemory[4];
        }

        if (index == 1) {
            inhibitTimer += 2;
            prescaler = mainMemory[129] & 7;
            prescalerCounter = (int) Math.pow(2, prescaler + 1);
        }

        if (index == 2) {
            int pclath = mainMemory[10] << 7;
            pc = value + pclath;
        }

        if (getMainMemoryBit(3, 5) == 1 && !switched) {
            index += 128;
        }

        if (!(index == 1 || index == 5 || index == 6 || index == 8 || index == 9 || index == 129 || index == 133 || index == 134 || index == 136 || index == 137) && !switched) {
            switched = true;
            setMainMemoryBit((index + 128) % 256, value, position);
        }

        switched = false;

        int currValue = getMainMemoryBit(index, position);
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
            index = mainMemory[4];
        }

        if (index == 1) {
            inhibitTimer += 2;
            prescaler = mainMemory[129] & 7;
            prescalerCounter = (int) Math.pow(2, prescaler + 1);
        }

        if (index == 2) {
            int pclath = mainMemory[10] << 8;
            pc = value + pclath;
        }

        if (getMainMemoryBit(3, 5) == 1 && !switched) {
            index += 128;
        }

        if (!(index == 1 || index == 5 || index == 6 || index == 8 || index == 9 || index == 129 || index == 133 || index == 134 || index == 136 || index == 137) && !switched) {
            switched = true;
            setMainMemoryByIndex((index + 128) % 256, value);
        }

        switched = false;

        mainMemory[index] = value % 256;
    }

    /**
     * pushes the value to the stack and increases sp by 1 with mod8
     **/
    public void pushStack() {
        stack[stackpointer] = pc;
        stackpointer = (stackpointer + 1) % 8;
    }

    /**
     * return top of stack and ecrease sp, if not 0
     */
    public void popStack() {
        if (stackpointer > 0) {
            stackpointer--;
            mainMemory[2] = stack[stackpointer] & 0xFF;
            pc = stack[stackpointer];
        }
    }

    /**
     * returns the value(0 or 1) of the selected bit of the selected main memory byte
     *
     * @param index    main memory byte
     * @param position bit in byte
     * @return 0 or 1, value of bit
     */
    public int getMainMemoryBit(int index, int position) {
        String str = String.format("%8s", Integer.toBinaryString(mainMemory[index] & 0xFF)).replace(' ', '0');
        return Character.getNumericValue(str.charAt(7 - position));
    }

    /**
     * sets w and main memory = 0
     */
    public void reset() {
        w = 0;
        pc = 0;
        Arrays.fill(mainMemory, 0);
        Arrays.fill(stack, 0);
        stackpointer = 0;
        mainMemory[0x85] = 0xFF;
        mainMemory[0x86] = 0xFF;
    }


    public void operationTimer() {
        timer += (16 / controller.getFrequency());

        inhibitTimer = inhibitTimer > 0 ? inhibitTimer - 1 : 0;

        if (getMainMemoryBit(129, 5) == 0) {

            increaseTimer();
        }
    }

    public void increaseTimer() {

        prescaler = mainMemory[129] & 7;
        if (getMainMemoryBit(129, 3) == 0) {
            prescalerCounter = prescalerCounter > 0 ? prescalerCounter - 1 : (int) (Math.pow(2, prescaler + 1) - 1);
        } else {
            prescalerCounter = 0;
        }

        if (prescalerCounter == 0 && inhibitTimer == 0) {
            int value = getMainMemoryByIndex(1) + 1;
            if (value > 255) {
                value %= 256;
                interrupt(2);

            }
            mainMemory[1] = value;
        }
    }

    public void interrupt(int source) {
        if (getMainMemoryBit(11, source + 3) == 1 && getMainMemoryBit(11, 7) == 1) {
            setMainMemoryBit(11, 1, source);
            setMainMemoryBit(11, 0, 7);
            pushStack();
            setMainMemoryByIndex(2, 3);
            if(controller.getSleepBreak() != -1){
                pc = 0;
            }
        }
    }


    // getter and setter

    public int[] getMainMemory() {
        return mainMemory;
    }

    public int getMainMemoryByIndex(int index) {
        if (index == 0) {
            index = mainMemory[4];
        }
        return mainMemory[index];
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

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
        mainMemory[2] = pc & 0xFF;
    }

    public void loadPc(int value) {

        mainMemory[2] = value & 0xFF;
        //mainMemory[10] = mainMemory[10] & 0xF8;
        //mainMemory[10] += (value & 0x700) >> 8;

        int pclath = (mainMemory[10] & 0x18) << 8;
        pc = value + pclath;
    }

    public void increasePc() {
        mainMemory[2]++;
        pc++;
    }

    public int getStack() {
        return stack[stackpointer];
    }

    public int[] getFullStack() {
        return stack;
    }

    public int getStackPointer() {
        return stackpointer;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public int getWatchdog() {
        return watchdog;
    }

    public void setWatchdog(int watchdog) {
        this.watchdog = watchdog;
    }

    public int getPrescaler () {
        return this.prescaler;
    }

    public void setPrescaler (int prescaler) {
        this.prescaler = prescaler;
    }
}
