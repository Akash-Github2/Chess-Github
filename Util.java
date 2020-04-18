import java.util.*;
public class Util {
    public static double rounded(double num) {
        return Math.round(num*1000)/1000.0;
    }
    public static int numPieces(String boardStr) {
        String[] unicodeVals = {"\u265C", "\u265E", "\u265D", "\u265B", "\u265A", "\u265F", "\u2656", "\u2658", "\u2657", "\u2655", "\u2654", "\u2659"};
        ArrayList<String> chessPiecesUnicode = new ArrayList<>(Arrays.asList(unicodeVals));
        int nPieces = 0;
        for (int i = 0; i < boardStr.length(); i++) {
            if (chessPiecesUnicode.contains(boardStr.substring(i,i+1))) {
                nPieces++;
            }
        }
        return nPieces;
    }
}