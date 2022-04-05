package input;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner; // Import the Scanner class to read text files

public class Input {
    public ArrayList<String> read(ArrayList<String> lines) {
        String encoding = "windows-1252";
        try {
            File source =  new File("res/TPicSim1.LST");
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
}

