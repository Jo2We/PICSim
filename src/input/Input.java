package input;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner; // Import the Scanner class to read text files

public class Input {

    public Input() {
    }

    /**
     * reads the Assembler program file
     * saves the relevant lines cut to relevant length
     * saves all lines for usage in ui
     * creates a crosslist to reference both lists
     *
     * @param lines     Arraylist to save the hex values of commands
     * @param fullLines Arraylist to save all lines to later show in ui
     * @param crossList Arraylist to reference between lines and fulllines
     */
    public void read(ArrayList<Integer> lines, ArrayList<String> fullLines, ArrayList<String> crossList) {
        String encoding = "windows-1252";
        try {
            File source = new File("res/TPicSim7.LST");
            // System.out.println(source.isFile());
            Scanner myReader = new Scanner(source, Charset.forName(encoding));
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                fullLines.add(data);
                char character = data.charAt(0);
                if (character != ' ') {
                    lines.add(getHexStringAsInt(data.substring(5, 9)));
                    crossList.add(data.substring(20, 25));
                }
            }
            myReader.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * returns the base10 value as int from a hex string value
     *
     * @param str hex string
     * @return decimal int
     */
    private int getHexStringAsInt(String str) {
        return Integer.parseInt(str, 16);
    }
}

