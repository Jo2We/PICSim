package PIC;

import PIC.Memory;

public class Commands {

    Memory m;

    /**
     * constructor
     */
    public Commands(Memory m) {
        this.m = m;
    }


    public void addwf(int opcode) /*00 0111 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called addwf with " + opcode);

    }

    public void andwf(int opcode) /*00 0101 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called andwf with " + opcode);

    }

    public void clrf(int opcode) /*00 0001 1 -000 0000 -|-111 1111-*/ {
        System.out.println("called clrf with " + opcode);

    }

    public void clrw(int opcode) /*00 0001 0 -xxx xxxx-*/ {
        System.out.println("called clrw with " + opcode);

    }

    public void comf(int opcode) /*00 1001 0 -000 0000 -|-111 1111-*/ {
        System.out.println("called comf with " + opcode);

    }

    public void decf(int opcode) /*00 0011 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called decf with " + opcode);

    }

    public void decfsz(int opcode) /*00 1011 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called decfsz with " + opcode);

    }

    public void incf(int opcode) /*00 1010 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called encf with " + opcode);

    }

    public void incfsz(int opcode) /*00 1111 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called incfsz with " + opcode);

    }

    public void iorwf(int opcode) /*00 0100 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called iorwf with " + opcode);

    }

    public void movf(int opcode) /*00 1000 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called movf with " + opcode);

    }

    public void movwf(int opcode) /*00 0000 1 -000 0000 -|-111 1111-*/ {
        System.out.println("called movwf with " + opcode);

    }

    public void nop() /*00 0000 0xx0 0000*/ {
        System.out.println("called nop");

    }

    public void rlf(int opcode) /*00 1101 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called rlf with " + opcode);

    }

    public void rrf(int opcode) /*00 1100 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called rrf with " + opcode);

    }

    public void subwf(int opcode) /*00 0010 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called subwf with " + opcode);

    }

    public void swapf(int opcode) /*00 1110 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called swapf with " + opcode);

    }

    public void xorwf(int opcode) /*00 0110 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called xorwf with " + opcode);

    }

    public void bcf(int opcode) /*01 00*/ {
        System.out.println("called bcf with " + opcode);

    }

    public void bsf(int opcode) /*01 01*/ {
        System.out.println("called bsf with " + opcode);

    }

    public void btfsc(int opcode) /*01 10*/ {
        System.out.println("called btfsc with " + opcode);

    }

    public void btfss(int opcode) /*01 11*/ {
        System.out.println("called btfss with " + opcode);

    }

    public void addlw(int opcode) /*11 111x*/ {
        System.out.println("called addlw with " + opcode);

        int k = opcode - 15872; //-11 1110 0000 0000
        m.setW((char) (k + m.getW()));
    }

    public void andlw(int opcode) /*11 1001*/ {
        System.out.println("called andlw with " + opcode);

    }

    public void call(int opcode) /*10 0*/ {
        System.out.println("called call with " + opcode);

    }

    public void clrwdt() /*00 0000 0110 0100*/ {
        System.out.println("called clrwdt");

    }

    public void _goto(int opcode) /*10 0*/ {
        System.out.println("called _goto with " + opcode);
        int k = opcode - 10240; //-10 1000 0000 0000

        m.setPcl((char) (k-1));

    }

    public void iorlw(int opcode) /*11 1000*/ {
        System.out.println("called iorlw with " + opcode);

    }

    public void movlw(int opcode) /*11 00xx*/ {
        System.out.println("called movlw with " + opcode);
        int k = opcode - 12288; //-11 0000 0000 0000
        m.setW((char) k);
    }

    public void retfie() /*00 0000 0000 1001*/ {
        System.out.println("called retfie");

    }

    public void retlw(int opcode) /*11 01xx*/ {
        System.out.println("called retlw with " + opcode);

    }

    public void _return() /*00 0000 0000 1000*/ {
        System.out.println("called _return");

    }

    public void sleep() /*00 0000 0110 0011*/ {
        System.out.println("called sleep");

    }

    public void sublw(int opcode) /*11 110x*/ {
        System.out.println("called sublw with " + opcode);
        int k = opcode - 15360; //-11 1100 0000 0000
        m.setW((char) (k - m.getW()));
    }

    public void xorlw(int opcode) /*11 1010*/ {
        System.out.println("called xorlw with " + opcode);

    }
}
