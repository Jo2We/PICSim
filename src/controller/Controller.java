package controller;

import GUI.MainFrame;
import GUI.ReloadingMethods;
import PIC.Commands;
import PIC.Memory;
import input.Input;

import java.util.ArrayList;
import java.util.Locale;

public class Controller {
    private ArrayList<Integer> lines = new ArrayList<>();
    private ArrayList<String> fullLines = new ArrayList<>();

    private ArrayList<String> crossList = new ArrayList<>();

    Memory memory;
    Commands command;
    private MainFrame mainFrame;
    private ReloadingMethods reloadingMethods;

    private boolean go = false;

    private boolean reset = false;

    private int breakpoint;
    private boolean contionueAfterBreakpoint = false;


    public Controller() {
        memory = new Memory();
        command = new Commands(memory);
    }

    /**
     * method to run the PICSimulator and manages all steps
     */
    public void runPICSimulator() {
        Input I = new Input();
        this.lines = I.read(this.lines, this.fullLines, this.crossList);
        this.reloadingMethods = new ReloadingMethods(this);
        //this.lines.forEach(key -> System.out.println(key));
        //System.out.println("-----------------------------------");
        //this.fullLines.forEach(key -> System.out.println(key));
        //System.out.println("-----------------------------------");
        //this.crossList.forEach(key -> System.out.println(key));
        int tempPcl = 0;
        do {
            while (!this.go) {
                reloadingMethods.reloadAll(false, command.getTimer(), -1);
            }
            do {
                if (this.memory.getPcl() > 0 && !this.reset) {
                    tempPcl = this.memory.getPcl();
                } else {
                    tempPcl = 0;
                }
                for (this.memory.setPcl(tempPcl); this.memory.getPcl() < this.lines.size(); this.memory.setPcl(this.memory.getPcl() + 1)) {
                    if (!this.go) {
                        break;
                    }
                    if (this.validBreakpoint(this.memory.getPcl()) && !this.contionueAfterBreakpoint) {
                        break;
                    }
                    this.setContionueAfterBreakpoint(false);
                    int index = Integer.parseInt(this.crossList.get(this.memory.getPcl()));
                    reloadingMethods.reloadAll(true, command.getTimer(), (index - 1));
                    callCommands(lines.get(memory.getPcl()));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                while (this.checkContionueOrReset()) {
                    reloadingMethods.reloadAll(false, command.getTimer(), -1);
                }
            } while (this.go);
        } while (!this.go);
    }

    /**
     * hex to bin calculation
     * value to return is String
     *
     * @param str
     * @return
     */
    private String getBinary(String str) {
        int num = (Integer.parseInt(str, 16));
        return Integer.toBinaryString(num);
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
                                        if (dec < 96) { // 00 0000 010 | 64 - 95

                                        }
                                        // 00 0000 011 | 96 - 127
                                        if (dec < 112) { // 00 0000 0110 | 96 - 111
                                            if (dec < 104) { // 00 0000 0110 0 | 96 - 103
                                                if (dec < 100) { // 00 0000 0110 00 | 96 - 99
                                                    if (dec < 98) { // 00 0000 0110 000 | 96 - 97 |

                                                    }
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

    /**
     * returns the Main Memory, calls the getMainMemory in Memory
     * @return
     */
    public int[] getMainMemory() {
        return this.memory.getMainMemory();
    }

    /**
     * set method to set a value in the Main Memory,
     * calls the setMainMemoryByIndex in the Memory with both parameters
     * @param index index where to set the value
     * @param value the value to set
     */
    public void setMainMemoryByIndex(int index, int value) {
        this.memory.setMainMemoryByIndex(index, value);
    }

    /**
     * set method to set a single bit in one value in the Main Memory
     * @param index index where to set the value
     * @param value the value contains 8 bits
     * @param position the position from 0 to 7 which bit to set
     */
    public void setBitInMemory(int index, int value, int position) {
        memory.setMainMemoryBit(index, value, position);
    }

    /**
     * set method to set TrisA to input ("i") or output ("o"),
     * TrisA is stored as array, the column stands for the index in the array
     * @param value contains a String with a "o" or "i"
     * @param column index to set in the array
     */
    public void setRATrisMemory(String value, int column) {
        memory.setRATris(value, column);
    }

    /**
     * set method to set TrisB to input ("i") or output ("o"),
     * TrisB is stored as array, the column stands for the index in the array
     * @param value contains a String with a "o" or "i"
     * @param column index to set in the array
     */
    public void setRBTrisMemory(String value, int column) {
        memory.setRBTris(value, column);
    }

    /**
     * method to return the ArrayList which contains the full lines from the .LST files
     * @return
     */
    public ArrayList<String> getFullLines() {
        return this.fullLines;
    }

    /**
     * get method for w register
     * @return
     */
    public int getW() {
        return this.memory.getW();
    }

    /**
     * get method for pcl
     * @return
     */
    public int getPcl() {
        return this.memory.getPcl();
    }

    /**
     * get method for status register
     * @return
     */
    public int getStatus() {
        return this.memory.getStatus();
    }

    /**
     * get method to return the text for every text on the gui
     * the intValue is the value as int which needs to be casted to a hex value,
     * if the value is lower than 16 a aditional 0 is put in front of the value
     * the value is casted from int to String by the toHexString() method
     * @param intValue value to cast to String
     * @return
     */
    public String getText(int intValue) {
        String str;
        int value = intValue;
        if (value < 16) {
            str = "0" + Integer.toHexString(value).toUpperCase();
        } else {
            str = Integer.toHexString(value).toUpperCase();
        }
        return str;
    }

    /**
     * get method to get the value where the stackpointer points at
     * @return
     */
    public int getStack() {
        return this.memory.getStack();
    }

    /**
     * get method to get the status by a specific bit selected by index
     * @param index the index of the bit
     * @return
     */
    public char getStatusByIndex(int index) {
        return this.memory.getStatusByIndex(index);
    }

    /**
     * set method set the timer
     * @param timer value for the timer to set
     */
    private void setTimer(double timer) {
        this.command.setTimer(timer);
    }

    /**
     * set method to set the value of the reset
     * @param value reset value
     */
    public void setReset (boolean value) {
        this.reset = value;
    }

    /**
     * method to perform a reset
     * resets the Main Memory, timer and selects the first line in the Code View,
     * @param timer value to set
     */
    public void reset(double timer) {
        this.memory.reset();
        this.setContionueAfterBreakpoint(true);
        this.setGo(false);
        this.setTimer(timer);
        int index = Integer.parseInt(this.crossList.get(this.memory.getPcl()));
        reloadingMethods.reloadAll(true, command.getTimer(), (index - 1));
    }

    /**
     * set method for go to start the Code in the simulator
     * @param value value to set
     */
    public void setGo (boolean value) {
        this.go = value;
        this.setContionueAfterBreakpoint(false);
    }

    /**
     * set method for the line of the breakpoint
     * @param line line of the click for the breakpoint
     */
    public void setBreakpoint (int line) {
        this.breakpoint = line;
        this.setContionueAfterBreakpoint(false);
    }

    /**
     * method to validate if the breakpoint is in a real Code line,
     * the crosslist contains every line of the full lines where real Code is,
     * if the line of the breakpoint matches a value of the crosslist, than it is a real breakpoint and is functional,
     * returns true if it is a real functional breakpoint, returns false if not
     * @param line line of the breakpoint
     * @return
     */
    private boolean validBreakpoint (int line) {
        for (int index = 0; index < this.crossList.size(); index++) {
            if (this.breakpoint == Integer.parseInt(this.crossList.get(index)) && index == line) {
                return true;
            }
        }
        return false;
    }

    /**
     * set method to set if the simulator should continue if there is a breakpoint,
     * when clicked on the continue button the value is set to true to interrupt the waiting at a breakpoint
     * @param value value to set
     */
    public void setContionueAfterBreakpoint (boolean value) {
        this.contionueAfterBreakpoint = value;
    }

    /**
     * used to check if one or both of the values are true
     * @return
     */
    private boolean checkContionueOrReset () {
        if (this.reset || this.contionueAfterBreakpoint) {
            return false;
        }
        return true;
    }

    /**
     * get method for getting the stackpointer,
     * calls the getStackPointer() in Memory
     * @return
     */
    public int getStackPointer () {
        return this.memory.getStackPointer();
    }

    /**
     * get method for getting the full stack with all values,
     * calls the getFullStack() in Memory
     * @return
     */
    public int [] getFullStack () {
        return this.memory.getFullStack();
    }
}
