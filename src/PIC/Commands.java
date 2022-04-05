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
}
