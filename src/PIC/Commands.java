package PIC;


public class Commands {

    private final Memory memory;

    private double timer = 0.0;


    public Commands(Memory m) {
        this.memory = m;
    }

    /**
     * Add W and f | C, DC, Z
     *
     * @param opcode opcode
     */
    public void addwf(int opcode) /*00 0111 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called addwf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;
        int value = memory.getW() + memory.getMainMemory()[f];
        //checkDC
        int fTest = memory.getMainMemory()[f] & 0x0F;
        int wTest = memory.getW() & 0x0F;
        int overflowTest = fTest + wTest;
        if (overflowTest > 15) {
            memory.setStatus(1, 1);
        }
        value = checkCarry(value);
        checkZeroSave(d, f, value);
        this.addTimeToTimer(1);
    }

    /**
     * AND W with f | Z
     *
     * @param opcode opcode
     */
    public void andwf(int opcode) /*00 0101 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called andwf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;

        int value = memory.getW() & memory.getMainMemory()[f];

        checkZeroSave(d, f, value);

        this.addTimeToTimer(1);
    }

    /**
     * clear f | Z
     *
     * @param opcode opcode
     */
    public void clrf(int opcode) /*00 0001 1 -000 0000 -|-111 1111-*/ {
        System.out.println("called clrf with " + opcode);
        int f = opcode & 0x7f;
        memory.setMainMemoryByIndex(f, 0);
        memory.setStatus(2, 1);
        this.addTimeToTimer(1);
    }

    /**
     * clear W | Z
     *
     * @param opcode opcode
     */

    public void clrw(int opcode) /*00 0001 0 -xxx xxxx-*/ {
        System.out.println("called clrw with " + opcode);

        memory.setW(0);
        //memory.setMainMemoryBit(3, 1, 2);
        memory.setStatus(2, 1);

        this.addTimeToTimer(1);
    }

    /**
     * Complement f | Z
     *
     * @param opcode opcode
     */
    public void comf(int opcode) /*00 1001 0 -000 0000 -|-111 1111-*/ {
        System.out.println("called comf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;

        int value = ~memory.getMainMemory()[f] & 0xFF;

        checkZeroSave(d, f, value);
        this.addTimeToTimer(1);
    }

    /**
     * Decrement f | Z
     *
     * @param opcode opcode
     */
    public void decf(int opcode) /*00 0011 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called decf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;

        int value = memory.getMainMemory()[f];
        value--;
        value += 256;
        value %= 256;
        checkZeroSave(d, f, value);
        this.addTimeToTimer(1);
    }

    /**
     * Decrement f, skip if 0 | _
     *
     * @param opcode opcode
     */
    public void decfsz(int opcode) /*00 1011 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called decfsz with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;

        int value = memory.getMainMemory()[f];
        value--;
        value += 256;
        value %= 256;

        if (value == 0) {
            this.addTimeToTimer(1);
            memory.setPcl(memory.getPcl() + 1);
        }

        if (d == 0) {
            memory.setW(value);
        } else {
            memory.setMainMemoryByIndex(f, value);
        }

        this.addTimeToTimer(1);// checkTimer
    }

    /**
     * Increment f | Z
     *
     * @param opcode opcode
     */
    public void incf(int opcode) /*00 1010 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called encf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;
        int value = memory.getMainMemory()[f];
        value++;
        checkZeroSave(d, f, value);
        this.addTimeToTimer(1);
    }

    /**
     * Increment f, Skip if 0 | _
     *
     * @param opcode opcode
     */
    public void incfsz(int opcode) /*00 1111 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called incfsz with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;

        int value = memory.getMainMemory()[f];
        value++;
        value %= 256;
        if (value == 0) {
            this.addTimeToTimer(1);
            memory.setPcl(memory.getPcl() + 1);

        }
        if (d == 0) {
            memory.setW(value);
        } else {
            memory.setMainMemoryByIndex(f, value);
        }


        this.addTimeToTimer(1);// checkTimer
    }

    /**
     * Inclusive OR W with f | Z
     *
     * @param opcode opcode
     */
    public void iorwf(int opcode) /*00 0100 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called iorwf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;

        int value = memory.getW() | memory.getMainMemory()[f];

        checkZeroSave(d, f, value);
        this.addTimeToTimer(1);
    }

    /**
     * Move f | Z
     *
     * @param opcode opcode
     */
    public void movf(int opcode) /*00 1000 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called movf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;
        int value = memory.getMainMemory()[f];
        checkZeroSave(d, f, value);
        this.addTimeToTimer(1);
    }

    /**
     * Move w to f | _
     *
     * @param opcode opcode
     */
    public void movwf(int opcode) /*00 0000 1 -000 0000 -|-111 1111-*/ {
        System.out.println("called movwf with " + opcode);
        int f = opcode & 0x7f;
        memory.setMainMemoryByIndex(f, memory.getW());
        this.addTimeToTimer(1);
    }

    /**
     * No Operation | _
     */
    public void nop() /*00 0000 0xx0 0000*/ {
        System.out.println("called nop");
        this.addTimeToTimer(1);
    }

    /**
     * Rotate Left f through Carry | C
     *
     * @param opcode opcode
     */
    public void rlf(int opcode) /*00 1101 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called rlf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;

        int value = memory.getMainMemory()[f];
        int carry = memory.getMainMemoryBit(3, 0);
        value <<= 1;
        value += carry;
        value = checkCarry(value);
        if (d == 0) {
            memory.setW(value);
        } else {
            memory.setMainMemoryByIndex(f, value);
        }
        this.addTimeToTimer(1);
    }

    /**
     * Rotate Right f through Carry | C
     *
     * @param opcode opcode
     */
    public void rrf(int opcode) /*00 1100 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called rrf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;

        int value = memory.getMainMemory()[f];
        int carry = memory.getMainMemoryBit(3, 0);
        if (value % 2 == 1) {
            memory.setStatus(0, 1);

        }
        value >>= 1;
        value += 128 * carry;

        if (d == 0) {
            memory.setW(value);
        } else {
            memory.setMainMemoryByIndex(f, value);
        }

        this.addTimeToTimer(1);
    }

    /**
     * Subtract W from f (f-w) | C, DC, Z
     *
     * @param opcode opcode
     */
    public void subwf(int opcode) /*00 0010 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called subwf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;

        int fvalue = memory.getMainMemory()[f];
        int value = fvalue - memory.getW();

        int fTest = fvalue & 0xF;
        int wTest = ~memory.getW() & 0xF;
        int overflowTest = fTest + wTest + 1;
        if (overflowTest >= 16) {
            memory.setStatus(1, 1);
        } else {
            memory.setStatus(1, 0);
        }

        if (value >= 0) {
            memory.setStatus(0, 1);
        } else {
            memory.setStatus(0, 0);
        }
        value += 256;
        value %= 256;
        checkZeroSave(d, f, value);
        this.addTimeToTimer(1);
    }

    /**
     * Swap nibbles in f | _
     *
     * @param opcode opcode
     */
    public void swapf(int opcode) /*00 1110 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called swapf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;

        int value = memory.getMainMemory()[f];

        int upperbits = value & 0xF0;
        int lowerbits = value & 0x0F;
        value = upperbits / 16;
        value += lowerbits * 16;

        if (d == 0) {
            memory.setW(value);
        } else {
            memory.setMainMemoryByIndex(f, value);
        }

        this.addTimeToTimer(1);
    }

    /**
     * Exclusive OR W with f | Z
     *
     * @param opcode opcode
     */
    public void xorwf(int opcode) /*00 0110 -0000 0000 -|-1111 1111-*/ {
        System.out.println("called xorwf with " + opcode);
        int f = opcode & 0x7f;
        int d = opcode & 0x80;

        int value = memory.getMainMemory()[f] ^ memory.getW() & 0xFF;

        checkZeroSave(d, f, value);

        this.addTimeToTimer(1);
    }

    /**
     * Bit Clear f | _
     *
     * @param opcode opcode
     */
    public void bcf(int opcode) /*01 00*/ {
        System.out.println("called bcf with " + opcode);
        int f = opcode & 0x7f;
        int b = opcode & 0x380;
        b >>= 7;


        memory.setMainMemoryBit(f, 0, b);

        this.addTimeToTimer(1);
    }

    /**
     * Bit Set f | _
     *
     * @param opcode opcode
     */
    public void bsf(int opcode) /*01 01*/ {
        System.out.println("called bsf with " + opcode);
        int f = opcode & 0x7f;
        int b = opcode & 0x380;
        b >>= 7;

        memory.setMainMemoryBit(f, 1, b);


        this.addTimeToTimer(1);
    }

    /**
     * Bit Test f, Skip if Clear | _
     *
     * @param opcode opcode
     */
    public void btfsc(int opcode) /*01 10*/ {
        System.out.println("called btfsc with " + opcode);
        int f = opcode & 0x7f;
        int b = opcode & 0x380;
        b >>= 7;

        if (memory.getMainMemoryBit(f, b) == 0) {
            nop();
            memory.setPcl(memory.getPcl() + 1);
        }


        this.addTimeToTimer(1);// checkTimer
    }

    /**
     * Bit test f, Skip if Set | _
     *
     * @param opcode opcode
     */
    public void btfss(int opcode) /*01 11*/ {
        System.out.println("called btfss with " + opcode);
        int f = opcode & 0x7f;
        int b = opcode & 0x380;
        b >>= 7;

        if (memory.getMainMemoryBit(f, b) == 1) {
            nop();
            memory.setPcl(memory.getPcl() + 1);
        }

        this.addTimeToTimer(1);// checkTimer
    }

    /**
     * Add literal and W | C, DC, Z
     *
     * @param opcode opcode
     */
    public void addlw(int opcode) /*11 111x*/ {
        System.out.println("called addlw with " + opcode);
        int k = opcode & 0xff;
        int value = k + memory.getW();
        //checkDC
        int kTest = k & 0x0F;
        int wTest = memory.getW() & 0x0F;
        int overflowTest = kTest + wTest;
        if (overflowTest > 15) {
            memory.setStatus(1, 1);
        } else {
            memory.setStatus(1, 0);
        }
        value = checkCarry(value);
        checkZeroSave(0, 0, value);
        this.addTimeToTimer(1);
    }

    /**
     * AND literal with W | Z
     *
     * @param opcode opcode
     */
    public void andlw(int opcode) /*11 1001*/ {
        System.out.println("called andlw with " + opcode);
        int k = opcode & 0xff;
        int value = k & memory.getW();
        checkZeroSave(0, 0, value);
        this.addTimeToTimer(1);
    }

    /**
     * call Subroutine | _
     *
     * @param opcode opcode
     */
    public void call(int opcode) /*10 0*/ {
        System.out.println("called call with " + opcode);
        int k = opcode & 0x7ff;
        memory.pushStack(memory.getPcl());
        memory.setPcl(k - 1);
        this.addTimeToTimer(2);
    }

    /**
     * Clear Watchdig Timer | TO, PD
     */
    public void clrwdt() /*00 0000 0110 0100*/ {
        System.out.println("called clrwdt");
        this.addTimeToTimer(1);
    }

    /**
     * Go to Address | _
     *
     * @param opcode opcode
     */
    public void _goto(int opcode) /*10 0*/ {
        System.out.println("called _goto with " + opcode);
        int k = opcode & 0x7ff;
        memory.setPcl(k - 1);
        this.addTimeToTimer(2);
    }

    /**
     * Inclusive OR literal with W | Z
     *
     * @param opcode opcode
     */
    public void iorlw(int opcode) /*11 1000*/ {
        System.out.println("called iorlw with " + opcode);
        int k = opcode & 0xff;
        int value = k | memory.getW();
        checkZeroSave(0, 0, value);
        this.addTimeToTimer(1);
    }

    /**
     * Move literal to W | _
     *
     * @param opcode opcode
     */
    public void movlw(int opcode) /*11 00xx*/ {
        System.out.println("called movlw with " + opcode);
        int k = opcode & 0xff;
        memory.setW(k);
        this.addTimeToTimer(1);
    }

    /**
     * Return from Interrupt | _
     */
    public void retfie() /*00 0000 0000 1001*/ {
        System.out.println("called retfie");
        this.addTimeToTimer(2);
    }

    /**
     * Return with literal in W | _
     *
     * @param opcode opcode
     */
    public void retlw(int opcode) /*11 01xx*/ {
        System.out.println("called retlw with " + opcode);
        int k = opcode & 0xff;

        memory.setW(k);
        memory.setPcl(memory.popStack());

        this.addTimeToTimer(2);
    }

    /**
     * Return from Subroutine | _
     */
    public void _return() /*00 0000 0000 1000*/ {
        System.out.println("called _return");

        memory.setPcl(memory.popStack());

        this.addTimeToTimer(2);
    }

    /**
     * Go into standby mode | TO, PD
     */
    public void sleep() /*00 0000 0110 0011*/ {
        System.out.println("called sleep");
        this.addTimeToTimer(1);
    }

    /**
     * Subtract W from literal | C, DC, Z
     *
     * @param opcode opcode
     */
    public void sublw(int opcode) /*11 110x*/ {
        System.out.println("called sublw with " + opcode);
        int k = opcode & 0xff;
        int value = k - memory.getW();

        int kTest = k & 0x0F;
        int wTest = ~memory.getW() & 0xF;
        int overflowTest = kTest + wTest + 1;
        if (overflowTest >= 16) {
            memory.setStatus(1, 1);
        } else {

            memory.setStatus(1, 0);
        }

        if (value >= 0) {
            memory.setStatus(0, 1);
        } else {
            memory.setStatus(0, 0);
        }
        value += 256;
        value %= 256;
        checkZeroSave(0, 0, value);
        this.addTimeToTimer(1);
    }

    /**
     * Exclusive OR literal with W | Z
     *
     * @param opcode opcode
     */
    public void xorlw(int opcode) /*11 1010*/ {
        System.out.println("called xorlw with " + opcode);

        int k = opcode & 0xFF;

        int value = k ^ memory.getW() & 0xFF;

        checkZeroSave(0, 0, value);
        this.addTimeToTimer(1);
    }

    /**
     * check if Zero flag needs to be set, save value in w or f
     *
     * @param destination 0 or 1 -> w or f
     * @param index       index if saved into f
     * @param value       value to be checked and saved
     */
    private void checkZeroSave(int destination, int index, int value) {

        if (destination == 0) {
            memory.setW(value);
        } else {
            memory.setMainMemoryByIndex(index, value);
        }
        if (value == 0) {
            memory.setStatus(2, 1);
        } else {
            memory.setStatus(2, 0);
        }
    }

    /**
     * check if carry needs to be set
     * adjust values to be positive and lov´wer than 256
     *
     * @param value value to be checked
     * @return adjusted value
     */
    private int checkCarry(int value) {
        if (value >= 256 || value < 0) {
            //carry
            value += 256;
            value = value % 256;
            memory.setStatus(0, 1);
        } else {
            memory.setStatus(0, 0);
        }
        return value;
    }

    private void addTimeToTimer(int cycle) {
        this.timer += cycle;
    }

    public double getTimer() {
        return this.timer;
    }

    public void setTimer(double timer) {
        this.timer = timer;
    }
}
