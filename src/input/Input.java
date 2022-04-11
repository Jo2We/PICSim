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
    public Input() {}

    /**
     * reads the Assambler program file and filters the relevant lines out of the file
     * @param lines
     * @return
     */
    public ArrayList<String> read(ArrayList<String> lines) {
        String encoding = "windows-1252";
        try {
            File source =  new File("res/TPicSim2.LST");
            // System.out.println(source.isFile());
            Scanner myReader = new Scanner(source, Charset.forName(encoding));
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                char character = data.charAt(0);
                if (character != ' ') {
                    lines.add(data.substring(5,9));
                }
            }
            myReader.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * reads the Assambler program file and filters the relevant lines out of the file
     * @param lines
     * @return
     */
    public ArrayList<String> readTxt(ArrayList<String> lines) {
        // res/AllCommandsText.txt
        try {
            Scanner scanner = new Scanner(new File("res/AllCommandsText.txt"));
            String line;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                //System.out.println(line);
                if (!line.isBlank()) {
                    lines.add(line);
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // lines.forEach((key) -> System.out.println(key));
        ArrayList<String> linesCut = new ArrayList<String>();
        for (String key : lines) {
            linesCut.add(key.substring(5,9));
        }
        // linesCut.forEach((key) -> System.out.println(key));
        return linesCut;
    }
}

