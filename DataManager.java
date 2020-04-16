import java.util.*;
import java.io.*;
public class DataManager {
    ArrayList<String> filenames = new ArrayList<>(); //for 0:3, 1:4, 2:5, 3:6
    ArrayList<Integer> fileLen = new ArrayList<>();
    public DataManager(String folder) {
        filenames.add(folder + "moveTB-D3.txt");
        filenames.add(folder + "moveTB-D4.txt");
        filenames.add(folder + "moveTB-D5.txt");
        filenames.add(folder + "moveTB-D6.txt");
    }
    public void reportNumPieces() {
        fileLen.clear();
        for (int a = 0; a < filenames.size(); a++) {
            try {
                File myObj = new File(filenames.get(a));
                Scanner reader = new Scanner(myObj);
                HashMap<Integer, Integer> numPiecesFreq = new HashMap<>();
                int numLines = 0;
                while (reader.hasNextLine()) {
                    numLines++;
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
                fileLen.add(numLines);
                System.out.println("--------------------------");
                System.out.println("Depth " + (a + 3) + ":");
                System.out.println(numPiecesFreq);
                System.out.println("--------------------------");
                reader.close();
            } catch (FileNotFoundException e) { 
                System.out.println("File Error");
            }
        }
        System.out.println("File Lengths:");
        System.out.println(fileLen);
        System.out.println("--------------------------");
    }
    public void cleanD3Duplicates() { // will remove all duplicates in D3 that are in D4 already
        ArrayList<String> uniqueMoves = new ArrayList<>(); //For D3 and D4
        try { //Go through D4 file
            File myObj = new File(filenames.get(1));
            Scanner reader = new Scanner(myObj);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String boardStr = line.split(":")[0];
                uniqueMoves.add(boardStr);
            }
            reader.close();
        } catch (Exception e) { 
            System.out.println("File Error");
        }
        String d3Str = "";
        try { //Go through D3 file
            File file = new File(filenames.get(0));
            Scanner reader = new Scanner(file);
            int count = 0;
            while (reader.hasNextLine()) {
                if (count %500 == 0) {
                    System.out.println(count);
                }
                
                count++;
                String line = reader.nextLine();
                String boardStr = line.split(":")[0];
                if (!uniqueMoves.contains(boardStr)) {
                    uniqueMoves.add(boardStr);
                    if (d3Str.length() != 0) {
                        d3Str += "\n";
                    }
                    d3Str += line;
                }
            }
            FileWriter writer = new FileWriter(file);
            writer.write(d3Str);
            writer.close();
            reader.close();
        } catch (Exception e) { 
            System.out.println("File Error");
        }
        System.out.println("D3 File has been cleaned of any duplicates.");
    }
    public void cleanD3RemoveBelow21() { //will remove anything with under 21 pieces because it will likely never happen

    }
}