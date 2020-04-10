import java.util.*;
public class BlackPiece extends ChessPiece {
    public BlackPiece(String name, String unicodeVal, int numVal, int locI, int locJ) {
        super(name, unicodeVal, numVal, locI, locJ);
    }
    public boolean isWhite() {
        return false;
    }
    protected ArrayList<Integer[]> getPossibleMoves4Pawn(ArrayList<Integer[]> possibleMoves, ChessPiece[][] board) {
        if (location[0] == 1 && board[location[0]+2][location[1]] == null && board[location[0]+1][location[1]] == null) {
            Integer[] temp = {location[0]+2, location[1]};
            possibleMoves.add(temp);
        }
        if (location[0] != 7 && board[location[0]+1][location[1]] == null) {
            Integer[] temp = {location[0]+1, location[1]};
            possibleMoves.add(temp);
        }
        if (location[0] != 7 && location[1] != 7 && board[location[0]+1][location[1]+1] != null && board[location[0]+1][location[1]+1].isWhite()) {
            Integer[] temp = {location[0]+1, location[1]+1};
            possibleMoves.add(temp);
        }
        if (location[0] != 7 && location[1] != 0 && board[location[0]+1][location[1]-1] != null && board[location[0]+1][location[1]-1].isWhite()) {
            Integer[] temp = {location[0]+1, location[1]-1};
            possibleMoves.add(temp);
        }
        return possibleMoves;
    } 
}