package input;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner; // Import the Scanner class to read text files

public class Input {

    /**
     * constructor
     */
    public Input() {
    }

    /**
     * reads the Assembler program file and filters the relevant lines out of the file
     *
     * @param lines
     * @return
     */
    public ArrayList<Integer> read(ArrayList<Integer> lines, ArrayList<String> fullLines, ArrayList<String> crossList) {
        String encoding = "windows-1252";
        try {
            File source = new File("res/TPicSim1.LST");
            // System.out.println(source.isFile());
            Scanner myReader = new Scanner(source, Charset.forName(encoding));
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                fullLines.add(data);
                char character = data.charAt(0);
                if (character != ' ') {
                    lines.add(getBinaryAsInt(data.substring(5, 9)));
                    crossList.add(data.substring(20, 25));
                }
            }
            myReader.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return lines;
    }

    private int getBinaryAsInt(String str) {
        return Integer.parseInt(str, 16);
    }
}

