import java.util.*;
import java.io.*;
public class DataTester {
    String fullFilename;
    int depth;
    public DataTester(String folder, int depth) {
        fullFilename = folder + "moveTB-D" + depth + ".txt";
        this.depth = depth;
    }
    public void reportNumPieces() {
        try {
            File myObj = new File(fullFilename);
            Scanner reader = new Scanner(myObj);
            HashMap<Integer, Integer> numPiecesFreq = new HashMap<>();
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String boardStr = line.split(":")[0];
                int count = 0;
                for (int i = 0; i < boardStr.length() - 1; i++) {
                    if (!boardStr.substring(i, i+1).equals(" ")) {
                        count++;
                    }
                }
                if (numPiecesFreq.get(count) == null) {
                    numPiecesFreq.put(count, 1);
                } else {
                    numPiecesFreq.put(count, numPiecesFreq.get(count) + 1);
                }
            }
            System.out.println("--------------------------");
            System.out.println("Depth " + depth + ":");
            System.out.println(numPiecesFreq);
            System.out.println("--------------------------");
            reader.close();
        } catch (FileNotFoundException e) { 
            System.out.println("File Error");
        }
    }
}