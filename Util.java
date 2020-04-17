import java.util.*;
public class Util {
    private ArrayList<String> chessPiecesUnicode = new ArrayList<>();
    public Util() {
        chessPiecesUnicode.add("\u265C");
        chessPiecesUnicode.add("\u265E");
        chessPiecesUnicode.add("\u265D");
        chessPiecesUnicode.add("\u265B");
        chessPiecesUnicode.add("\u265A");
        chessPiecesUnicode.add("\u265F");
        chessPiecesUnicode.add("\u2656");
        chessPiecesUnicode.add("\u2658");
        chessPiecesUnicode.add("\u2657");
        chessPiecesUnicode.add("\u2655");
        chessPiecesUnicode.add("\u2654");
        chessPiecesUnicode.add("\u2659");
    }
    public double rounded(double num) {
        return Math.round(num*1000)/1000.0;
    }
    public int numPieces(String boardStr) {
        int nPieces = 0;
        for (int i = 0; i < boardStr.length(); i++) {
            if (chessPiecesUnicode.contains(boardStr.substring(i,i+1))) {
                nPieces++;
            }
        }
        return nPieces;
    }
}