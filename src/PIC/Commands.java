package PIC;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class Commands {

    Memory memory;

    /**
     * constructor
     */
    public Commands(Memory m) {
        this.memory = m;
    }


    public void addwf(int opcode) /*00 0111 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called addwf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;

        char value = (char) (memory.getW() + memory.getMainMemory()[f]);

        if (d == 0) {
            memory.setW(value);
        } else {
            memory.setMainMemoryByIndex(f, value);
        }


    }

    public void andwf(int opcode) /*00 0101 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called andwf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;

    }

    public void clrf(int opcode) /*00 0001 1 -000 0000 -|-111 1111-*/ {
        System.out.println("called clrf with " + opcode);
        int f = opcode & 0x7f;

        memory.setMainMemoryByIndex(f, 0);
        memory.setMainMemoryBit(3, 1, 2);

    }

    public void clrw(int opcode) /*00 0001 0 -xxx xxxx-*/ {
        System.out.println("called clrw with " + opcode);

        memory.setW((char) 0);
    }

    public void comf(int opcode) /*00 1001 0 -000 0000 -|-111 1111-*/ {
        System.out.println("called comf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;
    }

    public void decf(int opcode) /*00 0011 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called decf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;
    }

    public void decfsz(int opcode) /*00 1011 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called decfsz with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;
    }

    public void incf(int opcode) /*00 1010 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called encf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;

        char value = memory.getMainMemory()[f];
        value++;

        if (d == 0) {
            memory.setW(value);
        } else {
            memory.setMainMemoryByIndex(f, value);
        }
    }

    public void incfsz(int opcode) /*00 1111 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called incfsz with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;
    }

    public void iorwf(int opcode) /*00 0100 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called iorwf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;
    }

    public void movf(int opcode) /*00 1000 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called movf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;

        char value = memory.getMainMemory()[f];

        if (d == 0) {
            memory.setW(value);
        } else {
            memory.setMainMemoryByIndex(f, value);
        }
    }

    public void movwf(int opcode) /*00 0000 1 -000 0000 -|-111 1111-*/ {
        System.out.println("called movwf with " + opcode);
        int f = opcode & 0x7f;

        memory.setMainMemoryByIndex(f, memory.getW());
    }

    public void nop() /*00 0000 0xx0 0000*/ {
        System.out.println("called nop");

    }

    public void rlf(int opcode) /*00 1101 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called rlf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;
    }

    public void rrf(int opcode) /*00 1100 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called rrf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;

    }

    public void subwf(int opcode) /*00 0010 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called subwf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;
    }

    public void swapf(int opcode) /*00 1110 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called swapf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;

        char value = memory.getMainMemory()[f];
        value = (char) (value - memory.getW());

        if (d == 0) {
            memory.setW(value);
        } else {
            memory.setMainMemoryByIndex(f, value);
        }
    }

    public void xorwf(int opcode) /*00 0110 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called xorwf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;
    }

    public void bcf(int opcode) /*01 00*/ {
        System.out.println("called bcf with " + opcode);
        int f = opcode & 0x7f;
        int b = opcode & 0x380;
    }

    public void bsf(int opcode) /*01 01*/ {
        System.out.println("called bsf with " + opcode);
        int f = opcode & 0x7f;
        int b = opcode & 0x380;
    }

    public void btfsc(int opcode) /*01 10*/ {
        System.out.println("called btfsc with " + opcode);
        int f = opcode & 0x7f;
        int b = opcode & 0x380;
    }

    public void btfss(int opcode) /*01 11*/ {
        System.out.println("called btfss with " + opcode);
        int f = opcode & 0x7f;
        int b = opcode & 0x380;
    }

    public void addlw(int opcode) /*11 111x*/ {
        System.out.println("called addlw with " + opcode);

        int k = opcode & 0xff;
        int value = k + memory.getW();
        saveValue(0, 0, value, memory.getW());
        //memory.setW((char) (k + memory.getW()));
    }

    public void andlw(int opcode) /*11 1001*/ {
        System.out.println("called andlw with " + opcode);
        int k = opcode & 0xff;

        memory.setW((char) (k & memory.getW()));


    }

    public void call(int opcode) /*10 0*/ {
        System.out.println("called call with " + opcode);

        int k = opcode & 0x7ff;

        memory.setPcl((char) (k - 1));

        memory.pushStack(k);

    }

    public void clrwdt() /*00 0000 0110 0100*/ {
        System.out.println("called clrwdt");

    }

    public void _goto(int opcode) /*10 0*/ {
        System.out.println("called _goto with " + opcode);
        int k = opcode & 0x7ff;

        memory.setPcl((char) (k - 1));

    }

    public void iorlw(int opcode) /*11 1000*/ {
        System.out.println("called iorlw with " + opcode);
        int k = opcode & 0xff;

        memory.setW((char) (k | memory.getW()));


    }

    public void movlw(int opcode) /*11 00xx*/ {
        System.out.println("called movlw with " + opcode);
        int k = opcode & 0xff;
        memory.setW((char) k);
    }

    public void retfie() /*00 0000 0000 1001*/ {
        System.out.println("called retfie");

    }

    public void retlw(int opcode) /*11 01xx*/ {
        System.out.println("called retlw with " + opcode);
        int k = opcode & 0xff;
    }

    public void _return() /*00 0000 0000 1000*/ {
        System.out.println("called _return");

    }

    public void sleep() /*00 0000 0110 0011*/ {
        System.out.println("called sleep");

    }

    public void sublw(int opcode) /*11 110x*/ {
        System.out.println("called sublw with " + opcode);
        int k = opcode & 0xff;
        int value = k - memory.getW();
        saveValue(0, 0, value, memory.getW());
        //memory.setW((char) (k - memory.getW()));
    }

    public void xorlw(int opcode) /*11 1010*/ {
        System.out.println("called xorlw with " + opcode);

        int k = opcode & 0xff;

        memory.setW((char) (k ^ memory.getW() & 0xff));


    }

    private void saveValue(int destination, int index, int value, int prevValue) {
        if (value == 0) {
            memory.setStatus(2);
        }


        if (value > 127 && prevValue <= 127 || value <= 127 && prevValue > 127) {
            //overflow
            memory.setStatus(1);
        }

        if (value > 255 || value < 0) {
            //carry
            value += 255;
            value = value % 255;
            memory.setStatus(0);
        }
        if (destination == 0) {
            memory.setW((char) value);
        } else {
            memory.setMainMemoryByIndex(index, value);
        }


    }
}
