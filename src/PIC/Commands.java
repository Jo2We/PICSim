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



    public void addwf() /*00 0111 -0000 0000 -|-1111 1111-*/ {}
    public void andwf() /*00 0101 -0000 0000 -|-1111 1111-*/ {}
    public void clrf() /*00 0001 1 -000 0000 -|-111 1111-*/ {}
    public void clrw() /*00 0001 0 -xxx xxxx-*/ {}
    public void comf() /*00 1001 0 -000 0000 -|-111 1111-*/ {}
    public void decf() /*00 0011 -0000 0000 -|-1111 1111-*/ {}
    public void decfsz() /*00 1011 -0000 0000 -|-1111 1111-*/ {}
    public void incf() /*00 1010 -0000 0000 -|-1111 1111-*/ {}
    public void incfsz() /*00 1111 -0000 0000 -|-1111 1111-*/ {}
    public void iorwf() /*00 0100 -0000 0000 -|-1111 1111-*/ {}
    public void movf() /*00 1000 -0000 0000 -|-1111 1111-*/ {}
    public void movwf() /*00 0000 1 -000 0000 -|-111 1111-*/ {}
    public void nop() /*00 0000 0xx0 0000*/ {}
    public void rlf() /*00 1101 -0000 0000 -|-1111 1111-*/ {}
    public void rrf() /*00 1100 -0000 0000 -|-1111 1111-*/ {}
    public void subwf() /*00 0010 -0000 0000 -|-1111 1111-*/ {}
    public void swapf() /*00 1110 -0000 0000 -|-1111 1111-*/ {}
    public void xorwf() /*00 0110 -0000 0000 -|-1111 1111-*/ {}

    public void bcf() /*01 00*/ {}
    public void bsf() /*01 01*/ {}
    public void btfsc() /*01 10*/ {}
    public void btfss() /*01 11*/ {}

    public void addlw() /*11 111x*/ {}
    public void andlw() /*11 1001*/ {}
    public void call() /*10 0*/ {}
    public void clrwdt() /*00 0000 0110 0100*/ {}
    public void _goto() /*10 0*/ {}
    public void iorlw() /*11 1000*/ {}
    public void movlw(int opcode) /*11 00xx*/ {
        int k = opcode - 12288; //-11 0000 0000 0000
        m.setW((char) k);
    }
    public void retfie() /*00 0000 0000 1001*/ {}
    public void retlw() /*11 01xx*/ {}
    public void _return() /*00 0000 0000 1000*/ {}
    public void sleep() /*00 0000 0110 0011*/ {}
    public void sublw() /*11 110x*/ {}
    public void xorlw() /*11 1010*/ {}

}
