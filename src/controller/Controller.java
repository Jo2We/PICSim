package controller;

import PIC.Commands;
import input.Input;

import java.util.ArrayList;

public class Controller {

    /**
     * attributes
     */
    private ArrayList<String> lines = new ArrayList<String>();

    /**
     * constructor
     */
    public Controller() {}

    /**
     * method to run the PICSumulator and manages all steps
     */
    public void runPICSimulator() {
        Input I = new Input();
        //this.lines = I.read(this.lines);
        this.lines = I.readTxt(this.lines);
        this.lines.forEach((key) -> System.out.println(key + ": " + getBinary(key)));
        this.lines.forEach((key) -> callCommands(getBinaryAsInt(key)));
    }

    /**
     * hex to bin calculation
     * value to return is String
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
    private void callCommands(int dec){
        System.out.println(dec);
        if (dec < 8192) { // 0
            if (dec < 4096) { // 00
                if (dec < 2048) { // 00 0
                    if (dec < 1024) { // 00 00
                        if (dec < 512) { // 00 000
                            if (dec < 256) { // 00 0000
                                if (dec < 128) { // 00 0000 0

                                }
                                // 00 0000 1
                            }
                            // 00 0001
                        }
                        // 00 001
                    }
                    // 00 01
                }
                // 00 1
            }
            // 01
        }
        if (dec < 16384) { // 1
            if (dec < 12288) { // 10
                if (dec < 10240) { // 10 0
                    if (dec < 9216) { // 10 00
                        if (dec < 8704) { // 10 000
                            if (dec < 8448) { // 10 0000
                                if (dec < 8320) { // 10 0000 0
                                    if (dec < 8256) { // 10 0000 00
                                        if (dec < 8224) { // 10 0000 000
                                            if (dec < 8208) { // 10 0000 0000
                                                if (dec < 8200) { // 10 0000 0000 0
                                                    if (dec < 8196) { // 10 0000 0000 00
                                                        if (dec < 8194) { // 10 0000 0000 000

                                                        }
                                                        // 11 1111 1111 111
                                                    }
                                                    // 11 1111 1111 11
                                                }
                                                // 11 1111 1111 1
                                            }
                                            // 11 1111 1111
                                        }
                                        // 11 1111 111
                                    }
                                    // 11 1111 11
                                }
                                // 11 1111 1
                            }
                            // 11 1111
                        }
                        // 11 111
                    }
                    // 11 11
                }
                // 11 1
            }
            // 11
        }
        // error
    }

}
