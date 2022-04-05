package controller;

import input.Input;

import java.util.ArrayList;

public class Controller {

    private ArrayList<String> lines = new ArrayList<String>();

    public Controller() {}

    public void runPICSimulator() {
        Input I = new Input();
        this.lines = I.read(this.lines);
        this.lines.forEach((key) -> System.out.println(key + ": " + getBinary(key)));

    }

    private String getBinary(String str) {
        int num = (Integer.parseInt(str, 16));
        return Integer.toBinaryString(num);
    }

    void callCommands(){
        
    }

}
