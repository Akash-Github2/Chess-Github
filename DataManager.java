import java.util.*;
import java.io.*;
public class DataManager {
    private ArrayList<String> filenames = new ArrayList<>(); //for 0:3, 1:4, 2:5, 3:6, 4: Moves Performed
    private ArrayList<Integer> fileLen = new ArrayList<>(); //doesn't include moves performed
    private ArrayList<String> fileSizes = new ArrayList<>(); //doesn't include moves performed
    public DataManager(String folder) {
        filenames.add(folder + "moveTB-D3.txt");
        filenames.add(folder + "moveTB-D4.txt");
        filenames.add(folder + "moveTB-D5.txt");
        filenames.add(folder + "moveTB-D6.txt");
        filenames.add(folder + "MovesPerformed.txt");
    }
    public void reportNumPieces() {
        fileLen.clear();
        fileSizes.clear();
        for (int a = 0; a < filenames.size() - 1; a++) {
            try {
                File myObj = new File(filenames.get(a));
                Scanner reader = new Scanner(myObj);
                HashMap<Integer, Integer> numPiecesFreq = new HashMap<>();
                int numLines = 0;
                while (reader.hasNextLine()) {
                    numLines++;
                    String line = reader.nextLine();
                    String boardStr = line.split(":")[0];
                    int count = Util.numPieces(boardStr);
                    if (numPiecesFreq.get(count) == null) {
                        numPiecesFreq.put(count, 1);
                    } else {
                        numPiecesFreq.put(count, numPiecesFreq.get(count) + 1);
                    }
                }
                fileLen.add(numLines);
                fileSizes.add(getFileSizeKB(myObj));
                System.out.println("--------------------------");
                System.out.println("Depth " + (a + 3) + ":");
                System.out.println(numPiecesFreq);
                System.out.println("--------------------------");
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace(); 
                System.out.println("File Err");
            }
        }
        System.out.println("File Lengths:");
        System.out.println(fileLen);
        System.out.println("File Sizes:");
        System.out.println(fileSizes);
        double totalFileSizes = 0;
        for (int i = 0; i < fileSizes.size(); i++) {
            totalFileSizes += Double.parseDouble(fileSizes.get(i).substring(0, fileSizes.get(i).length() - 3));
        }
        System.out.println("Total Size of Files: " + Util.rounded(totalFileSizes) + " KB");
        System.out.println("--------------------------");
    }
    public void fullClean() {
        cleanD3Duplicates();
        cleanD3RemoveBelow24();
        reportNumPieces();
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
                if (count % 2000 == 0) {
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
        reportNumPieces();
    }
    public void cleanD3RemoveBelow24() { //will remove anything with under 24 pieces because it will likely never happen
        String d3Str = "";
        try { //Go through D3 file
            File file = new File(filenames.get(0));
            Scanner reader = new Scanner(file);
            int count = 0;
            while (reader.hasNextLine()) {
                if (count % 2000 == 0) {
                    System.out.println(count);
                }
                count++;
                String line = reader.nextLine();
                String boardStr = line.split(":")[0];
                if (Util.numPieces(boardStr) >= 24) {
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
        System.out.println("D3 File has been cleaned of any boards with under 24 pieces.");
        reportNumPieces();
    }
    public void clearMoveOutputFile() {
        try{
            File file = new File(filenames.get(4));
            FileWriter writer = new FileWriter(file);
            writer.write("");
            writer.close();
        }catch(Exception e) {
            System.out.println("FILE ERROR");
        }
    }
    public void appendToFile(int initI, int initJ, int finI, int finJ) {
        try{
            File file = new File(filenames.get(4));
            FileWriter writer = new FileWriter(file, true);
            if (file.length() != 0) {
                writer.write("\n");
            }
            writer.write("chessBoard.makeMoveOverall(" + initI + ", " + initJ + ", " + finI + ", " + finJ + ", false);");
            writer.close();
        }catch(Exception e) {
            System.out.println("FILE ERROR");
        }
    }
    public String getFileSizeKB(File file) {
		return Util.rounded((double) file.length() / 1024) + " KB";
	}
}