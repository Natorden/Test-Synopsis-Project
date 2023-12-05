package MyTunes.dal.file;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileDAO {
    /**
     * Appends line to the end of a txt file.
     * @param source text file to append line to.
     * @param textOfLine Text to append.
     */
    protected static void appendLineToFile(String source, String textOfLine){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(source, true));
            writer.write(textOfLine+"\n");
            writer.close();
        }catch(Exception e){
            System.out.println("["+source+"]");
            System.out.println("Problem saving to persistent storage, only saved in memory.");
            e.printStackTrace();
        }
    }

    /**
     * Reads a text file and formats it into a string arraylist.
     * @param source text file to append line to.
     * @return List of string, each string representing a line in the text file.
     */
    protected static List<String> readFileToList(String source){
        List<String> array = new ArrayList<>();
        File file = new File(source);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) { array.add(line); }
        }catch(Exception e){
            System.out.println("["+source+"]");
            System.out.println("[FileDAO] Problem reading from persistent storage.");
            e.printStackTrace();
        }
        return array;
    }

    /**
     * Saves a List<String> to a file.
     * @param source text file to save list to.
     * @param list The list to save to the file.
     */
    protected static void saveListToFile(String source, List<String> list){
        try{
            File file = new File(source);
            Files.write(file.toPath(), list, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        }catch(Exception e){
            System.out.println("["+source+"]");
            System.out.println("[FileDAO] Problem saving to persistent storage.");
            e.printStackTrace();
        }
    }
}
