package controller;

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
        this.lines = I.read(this.lines);
        this.lines.forEach((key) -> System.out.println(key + ": " + getBinary(key)));

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

    /**
     * finds the command to call out of the binary
     */
    private void callCommands(){
        
    }

}
