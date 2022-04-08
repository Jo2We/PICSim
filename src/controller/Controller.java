package controller;

import GUI.MainFrame;
import PIC.Commands;
import PIC.Memory;
import input.Input;

import java.util.ArrayList;

public class Controller {

    /**
     * attributes
     */
    private ArrayList<String> lines = new ArrayList<String>();
    private MainFrame mainFrame;
    private Memory memory;

    /**
     * constructor
     */
    public Controller() {
    }

    /**
     * method to run the PICSumulator and manages all steps
     */
    public void runPICSimulator() {
        memory = new Memory();
        mainFrame = new MainFrame(this);
        Input I = new Input();
        //this.lines = I.read(this.lines);
        this.lines = I.read(this.lines);
        this.lines.forEach((key) -> System.out.println(key + ": " + getBinary(key)));
        this.lines.forEach((key) -> callCommands(getBinaryAsInt(key)));
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

    private int getBinaryAsInt(String str) {
        return Integer.parseInt(str, 16);
    }

    /**
     * finds the command to call out of the binary
     */
    private void callCommands(int dec) {
        //System.out.println(dec);
        Commands command = new Commands();

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

    public String[][] getMainMemory() {
        return memory.getMainMemory();
    }

    public void setMainMemoryByIndex(int row, int column, String value) {
        memory.setMainMemoryByIndex(row, column, value);
    }

    public void setRAPinMemory (int value, int column) {
        memory.setRAPin(value, column);
    }

    public void setRBPinMemory (int value, int column) {
        memory.setRBPin(value, column);
    }

    public void setRATrisMemory(String value, int column) {
        memory.setRATris(value, column);
    }

    public void setRBTrisMemory(String value, int column) {
        memory.setRBTris(value, column);
    }
}
