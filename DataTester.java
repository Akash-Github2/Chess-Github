import java.util.*;
import java.io.*;
public class DataTester {
    ArrayList<String> filenames = new ArrayList<>(); //for 0:3, 1:4, 2:5, 3:6
    public DataTester(String folder) {
        filenames.add(folder + "moveTB-D3.txt");
        filenames.add(folder + "moveTB-D4.txt");
        filenames.add(folder + "moveTB-D5.txt");
        filenames.add(folder + "moveTB-D6.txt");
    }
    public void reportNumPieces() {
        for (int a = 0; a < filenames.size(); a++) {
            try {
                File myObj = new File(filenames.get(a));
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
                System.out.println("Depth " + (a + 3) + ":");
                System.out.println(numPiecesFreq);
                System.out.println("--------------------------");
                reader.close();
            } catch (FileNotFoundException e) { 
                System.out.println("File Error");
            }
        }
    }
    // public void cleanD3() {
    //     TreeMap<String, MoveVal> d34UniqueMoves = new TreeMap<>();
    //     try { //Go through D4 file
    //         File myObj = new File(filenames.get(1));
    //         Scanner reader = new Scanner(myObj);
    //         while (reader.hasNextLine()) {
    //             String line = reader.nextLine();
    //             String boardStr = line.split(":")[0];
    //             String nextLine = line.split(":")[1];
    //             String move = nextLine.split(" ")[0];
    //             double val = Double.parseDouble(nextLine.split(" ")[1]);
    //             if (d34UniqueMoves.get(boardStr) == null) {
    //                 d34UniqueMoves.put(boardStr, new MoveVal(move, val));
    //             }
    //             d34UniqueMoves.put(boardStr, new MoveVal(move, val));
    //         }
    //         reader.close();
    //     } catch (FileNotFoundException e) { 
    //         System.out.println("File Error");
    //     }
    // }
}