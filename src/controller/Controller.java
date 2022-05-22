package controller;

import gui.ReloadingMethods;
import pic.Commands;
import pic.Memory;
import input.Input;

import java.util.ArrayList;

public class Controller {
    private final ArrayList<Integer> lines = new ArrayList<>();
    private final ArrayList<String> fullLines = new ArrayList<>();

    private final ArrayList<String> crossList = new ArrayList<>();

    Memory memory;
    Commands command;
    private ReloadingMethods reloadingMethods;

    private boolean go = false;

    private boolean reset = false;

    private int breakpoint;
    private boolean continueAfterBreakpoint = false;


    private int frequency = 8;
    private int pc;
    private int sleepBreak = -1;


    public Controller() {
        memory = new Memory(this);
        command = new Commands(memory, this);
    }

    /**
     * method to run the PICSimulator and manages all steps
     */
    public void runPICSimulator() {
        Input I = new Input();
        I.read(lines, fullLines, crossList);
        reloadingMethods = new ReloadingMethods(this);

        int tempPc;
        do {
            while (!go) {
                reloadingMethods.reloadAll(false, memory.getTimer(), -1);
            }
            do {
                tempPc = !reset ? memory.getPc() : 0;
                for (memory.setPc(tempPc); memory.getPc() < lines.size(); memory.increasePc()) {
                    if (!go) {
                        break;
                    }
                    if (validBreakpoint(memory.getPc()) && !continueAfterBreakpoint) {
                        break;
                    }
                    setContinueAfterBreakpoint(false);
                    int index = Integer.parseInt(crossList.get(memory.getPc()));
                    reloadingMethods.reloadAll(true, memory.getTimer(), (index - 1));
                    callCommands(lines.get(memory.getPc()));
                    if (this.sleepBreak == this.memory.getPc()) {
                        break;
                    }
                    try {
                        Thread.sleep((90 - (10 * frequency)));
                        //Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                while (checkContinueOrReset()) {
                    reloadingMethods.reloadAll(false, memory.getTimer(), -1);
                }
            } while (go);
        } while (!go);
    }

    /**
     * finds the command to call out of the binary
     */
    private void callCommands(int dec) {
        if (dec < 16384) {
            if (dec < 8192) { // 0
                if (dec < 4096) { // 00
                    if (dec < 2048) { // 00 0
                        if (dec < 1024) { // 00 00
                            if (dec < 512) { // 00 000
                                if (dec < 256) { // 00 0000
                                    if (dec < 128) { // 00 0000 0
                                        if (dec < 64) { // 00 0000 00 | 0 - 63
                                            if (dec == 32 || dec == 0) { // 32 | 0 | nop
                                                command.nop();
                                                return;
                                            }
                                            if (dec < 32) { // 00 0000 000
                                                if (dec < 16) { // 00 0000 0000
                                                    // 00 0000 0000 1 | 8 - 15
                                                    if (dec == 8) { // 00 0000 0000 1000 | 8 | return
                                                        command._return();
                                                        return;
                                                    }
                                                    // 00 0000 0000 1001 | 9 | refie
                                                    command.retfie();
                                                    return;
                                                }
                                                // 00 0000 0001
                                            }
                                            // 00 0000 001
                                        }
                                        // 00 0000 01
                                        if (dec == 96 || dec == 64) { // 96 | 64 | nop
                                            command.nop();
                                            return;
                                        }
                                        // 00 0000 011 | 96 - 127
                                        if (dec < 112) { // 00 0000 0110 | 96 - 111
                                            if (dec < 104) { // 00 0000 0110 0 | 96 - 103
                                                if (dec < 100) { // 00 0000 0110 00 | 96 - 99
                                                    // 00 0000 0110 001 | 98 - 99 | sleep
                                                    command.sleep();
                                                    return;
                                                }
                                                // 00 0000 0110 01 | 100 - 103 | clrwdt
                                                command.clrwdt();
                                                return;
                                            }
                                            // 00 0000 0110 1 | 104 - 111
                                        }
                                        // 00 0000 0111 | 112 - 127
                                    }
                                    // 00 0000 1 | 128 - 255 | movwf
                                    command.movwf(dec);
                                    return;
                                }
                                // 00 0001 | 256 - 511
                                if (dec < 384) { // 00 0001 0 | 256 - 383 | clrw
                                    command.clrw(dec);
                                    return;
                                }
                                // 00 0001 1 | 384 - 511 | clrf
                                command.clrf(dec);
                                return;
                            }
                            // 00 001
                            if (dec < 768) { // 00 0010 | 512 - 767 | subwf
                                command.subwf(dec);
                                return;
                            }
                            // 00 0011 | 768 - 1024 | decf
                            command.decf(dec);
                            return;
                        }
                        // 00 01 | 1024 - 2047
                        if (dec < 1536) { // 00 010 | 1024 - 1535
                            if (dec < 1280) { // 00 0100 | 1024 - 1279 | iorwf
                                command.iorwf(dec);
                                return;
                            }
                            // 00 0101 | 1280 - 1535 | andwf
                            command.andwf(dec);
                            return;
                        }
                        // 00 011 | 1536 - 2047
                        if (dec < 1792) { // 00 0110 | 1536 - 1791 | xorwf
                            command.xorwf(dec);
                            return;
                        }
                        // 00 0111 | 1792 - 2047 | addwf
                        command.addwf(dec);
                        return;
                    }
                    // 00 1 | 2048 - 4095
                    if (dec < 3072) { // 00 10 | 2048 - 3071
                        if (dec < 2560) { // 00 100 | 2048 - 2559
                            if (dec < 2304) { // 00 1000 | 2048 - 2303 | movf
                                command.movf(dec);
                                return;
                            }
                            // 00 1001 | 2304 - 2059 | comf
                            command.comf(dec);
                            return;
                        }
                        // 00 101 | 2560 - 3071
                        if (dec < 2816) { // 00 1010 | 2560 - 2815 | incf
                            command.incf(dec);
                            return;
                        }
                        // 00 1011 | 2816 - 3071 | decfsz
                        command.decfsz(dec);
                        return;
                    }
                    // 00 11 | 3072 - 4095
                    if (dec < 3584) { // 00 110 | 3072 - 3583
                        if (dec < 3328) { // 00 1100 | 3072 - 3327 | rrf
                            command.rrf(dec);
                            return;
                        }
                        // 00 1101 | 3328 - 3583 | rlf
                        command.rlf(dec);
                        return;
                    }
                    // 00 111 | 3584 - 4095
                    if (dec < 3840) { // 00 1110 | 3584 - 3839 | swapf
                        command.swapf(dec);
                        return;
                    }
                    // 00 1111 | 3840 - 4095 | incfsz
                    command.incfsz(dec);
                    return;
                }
                // 01 | 4096 - 8191
                if (dec < 6144) { // 01 0 4096 - 6143
                    if (dec < 5120) { // 01 00 | 4096 - 5119 | bcf
                        command.bcf(dec);
                        return;
                    }
                    // 01 01 | 5120 - 6143 | bsf
                    command.bsf(dec);
                    return;
                }
                // 01 1 | 6144 - 8191
                if (dec < 7168) { // 01 10 | 6144 - 7167 | btfsc
                    command.btfsc(dec);
                    return;
                }
                // 01 11 | 7168 - 8191 | btfss
                command.btfss(dec);
                return;
            }
            // --------------------------------------------------------------------------------
            // 1
            if (dec < 12288) { // 10
                if (dec < 10240) { // 10 0 | call
                    command.call(dec);
                    return;
                }
                //10 1 | goto
                command._goto(dec);
                return;
            }
            //11
            if (dec < 14336) { //110
                if (dec < 13312) { //1100 | movlw
                    command.movlw(dec);
                    return;
                }
                //1101 | retlw
                command.retlw(dec);
                return;
            }
            //111
            if (dec < 15360) { //1110
                if (dec < 14848) { //11100
                    if (dec < 14592) { //111000 | iorlw
                        command.iorlw(dec);
                        return;
                    }
                    //111001 | andlw
                    command.andlw(dec);
                    return;
                }
                //11101(0) | xorlw
                command.xorlw(dec);
                return;
            }
            //1111
            if (dec < 15872) { //11110 | sublw
                command.sublw(dec);
                return;
            }
            //11111 | addlw
            command.addlw(dec);
            return;
        }
        System.out.println("error");
    }


    // helper Methods

    /**
     * get method to return the text for every text on the gui
     * the intValue is the value as int which needs to be casted to a hex value,
     * if the value is lower than 16 a aditional 0 is put in front of the value
     * the value is casted from int to String by the toHexString() method
     *
     * @param intValue value to cast to String
     * @return casted String
     */
    public String getText(int intValue) {
        String str;
        if (intValue < 16) {
            str = "0" + Integer.toHexString(intValue).toUpperCase();
        } else {
            str = Integer.toHexString(intValue).toUpperCase();
        }
        return str;
    }

    /**
     * method to perform a reset
     * resets the Main Memory, timer and selects the first line in the Code View,
     *
     * @param timer value to set
     */
    public void reset(int timer) {
        memory.reset();
        pc = 0;
        setContinueAfterBreakpoint(true);
        setBreakpoint(-1);
        setGo(false);
        setTimer(timer);
        int index = Integer.parseInt(crossList.get(memory.getPc()));
        reloadingMethods.reloadAll(true, memory.getTimer(), (index - 1));
    }

    /**
     * method to validate if the breakpoint is in a real Code line,
     * the crosslist contains every line of the full lines where real Code is,
     * if the line of the breakpoint matches a value of the crosslist, than it is a real breakpoint and is functional,
     * returns true if it is a real functional breakpoint, returns false if not
     *
     * @param line line of the breakpoint
     * @return bool if breakpoint is valid
     */
    private boolean validBreakpoint(int line) {
        for (int index = 0; index < crossList.size(); index++) {
            if (this.breakpoint == Integer.parseInt(crossList.get(index)) && index == line) {
                return true;
            }
        }
        return false;
    }

    /**
     * used to check if one or both of the values are true
     *
     * @return bool if continue = true
     */

    private boolean checkContinueOrReset() {
        return !reset && !continueAfterBreakpoint;
    }


    //getter and setter methods, some forward to memory

    /**
     * set method to set if the simulator should continue if there is a breakpoint,
     * when clicked on the continue button the value is set to true to interrupt the waiting at a breakpoint
     *
     * @param value value to set
     */
    public void setContinueAfterBreakpoint(boolean value) {
        this.continueAfterBreakpoint = value;
        this.reset = false;
    }

    /**
     * set method for go to start the Code in the simulator
     *
     * @param value value to set
     */
    public void setGo(boolean value) {
        go = value;
        setContinueAfterBreakpoint(false);
    }

    /**
     * set method for the line of the breakpoint
     *
     * @param line line of the click for the breakpoint
     */
    public void setBreakpoint(int line) {
        breakpoint = line;
        setContinueAfterBreakpoint(false);
    }

    public ArrayList<String> getFullLines() {
        return fullLines;
    }

    public void setReset(boolean value) {
        reset = value;
    }

    public int[] getMainMemory() {
        return memory.getMainMemory();
    }

    public void setMainMemoryByIndex(int index, int value) {
        memory.setMainMemoryByIndex(index, value);
    }

    public void setBitInMemory(int index, int value, int position) {
        memory.setMainMemoryBit(index, value, position);
    }

    public void increaseTimer() {
        memory.increaseTimer();
    }

    public int getW() {
        return memory.getW();
    }

    public int getPc() {
        return memory.getPc();
    }

    public int getStackPointer() {
        return memory.getStackPointer();
    }

    public int[] getFullStack() {
        return memory.getFullStack();
    }

    public int getStack() {
        return memory.getStack();
    }



    public int getMainMemoryBit(int index, int position) {
        return memory.getMainMemoryBit(index, position);
    }

    private void setTimer(int timer) {
        memory.setTimer(timer);
    }

    public void setFrequency(String strFrequency) {
        switch (strFrequency) {
            case "1 MHz" -> frequency = 1;
            case "2 MHz" -> frequency = 2;
            case "3 MHz" -> frequency = 3;
            case "4 MHz" -> frequency = 4;
            case "5 MHz" -> frequency = 5;
            case "6 MHz" -> frequency = 6;
            case "7 MHz" -> frequency = 7;
            case "8 MHz" -> frequency = 8;
        }
        System.out.println("Frequency: " + frequency + " MHz");
    }

    public int getFrequency() {
        return frequency;
    }

    public void interrupt(int source) {
        memory.interrupt(source);
    }

    public void setSleepBreak () {
        this.sleepBreak = this.memory.getPc();
    }
}
