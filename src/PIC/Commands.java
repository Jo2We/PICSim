package PIC;

import PIC.Memory;

public class  Commands {

    Memory m;

    /**
     * constructor
     */
    public Commands() {
        m = new Memory();
    }

    void movlw(int opcode){
        int k = opcode - 12288; //-11 0000 0000 0000
        m.setW((char) k);
    }

    void addlw(int opcode){
        int k = opcode - 15872; //-11 1110 0000 0000
        m.setW((char) (k+m.getW()));
    }

    void sublw(int opcode){
        int k = opcode - 15360; //-11 1100 0000 0000
        m.setW((char) (k-m.getW()));
    }
}
