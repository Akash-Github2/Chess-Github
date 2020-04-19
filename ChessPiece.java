import java.util.*;
public abstract class ChessPiece {
    protected String name; //Pawn, Rook, Knight, Bishop, Queen, King
    protected String unicodeVal;
    protected double numVal; //1, 5, 3, 3, 10, 100
    protected int[] location = new int[2]; //{i,j}
    //Early Game Piece Values
    private double[][] pawnWeighting4WhiteEarly = {{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                                                   {1.5, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.5},
                                                   {0.9, 1.0, 1.2, 1.5, 1.5, 1.2, 1.0, 0.9},
                                                   {0.6, 1.2, 1.2, 1.6, 1.6, 1.2, 1.2, 0.6},
                                                   {0.5, 0.6, 1.2, 1.4, 1.6, 1.2, 0.6, 0.5},
                                                   {0.1, 0.7, 1.0, 1.1, 1.3, 0.7, 0.7, 0.1},
                                                   {0.7, 0.7, 0.9, 1.0, 1.0, 1.0, 0.7, 0.7},
                                                   {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}};

    private double[][] knightWeighting4WhiteEarly = {{2.8, 2.5, 2.5, 2.6, 2.6, 2.5, 2.5, 2.8},
                                                    {2.6, 2.7, 3.6, 3.0, 3.0, 3.6, 2.7, 2.6},
                                                    {2.7, 3.0, 3.4, 3.4, 3.4, 3.4, 3.0, 2.7},
                                                    {2.7, 4.1, 3.7, 4.4, 4.4, 3.7, 4.1, 2.7},
                                                    {2.7, 3.0, 3.7, 4.4, 4.4, 3.7, 3.0, 2.7},
                                                    {2.8, 2.8, 3.5, 3.0, 3.0, 3.5, 2.8, 2.8},
                                                    {1.0, 2.1, 2.4, 2.4, 2.4, 2.4, 2.1, 1.0},
                                                    {0.3, 2.0, 1.4, 1.4, 1.4, 1.4, 2.0, 0.3}};
    
    private double[][] kingWeighting4WhiteEarly = {{90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0},
                                                   {90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0},
                                                   {90.0, 93.0, 93.0, 93.0, 93.0, 93.0, 93.0, 90.0},
                                                   {93.0, 94.0, 94.0, 94.5, 94.5, 94.0, 94.0, 93.0},
                                                   {94.5, 95.0, 95.0, 95.0, 95.0, 95.0, 95.0, 94.5},
                                                   {96.5, 96.5, 96.5, 96.5, 96.5, 96.5, 96.5, 96.5},
                                                   {98.0, 98.0, 98.0, 98.0, 98.0, 98.0, 98.0, 98.0},
                                                   {100.0, 100.0, 100.7, 99.0, 100.0, 98.0, 101.8, 101.0}};

    //Mid Game Piece Values
    private double[][] pawnWeighting4WhiteMid = {{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                                                 {1.6, 1.6, 1.8, 1.8, 1.8, 1.8, 1.6, 1.6},
                                                 {0.9, 1.0, 1.2, 1.5, 1.5, 1.2, 1.0, 0.9},
                                                 {0.8, 1.1, 1.3, 1.6, 1.6, 1.3, 1.1, 0.8},
                                                 {0.9, 1.0, 1.0, 1.6, 1.6, 1.0, 1.0, 0.9},
                                                 {0.9, 1.0, 1.1, 1.3, 1.3, 1.1, 1.0, 0.9},
                                                 {0.9, 0.9, 0.9, 0.7, 0.7, 1.0, 1.0, 1.0},
                                                 {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}};
  
    private double[][] knightWeighting4WhiteMid = {{2.8, 2.1, 2.1, 2.1, 2.1, 2.1, 2.1, 2.8},
                                                   {2.4, 3.0, 3.5, 2.7, 2.7, 3.5, 3.0, 2.4},
                                                   {2.5, 3.1, 3.4, 3.4, 3.4, 3.4, 3.1, 2.5},
                                                   {2.6, 3.1, 3.5, 4.0, 4.0, 3.5, 3.1, 2.6},
                                                   {2.6, 3.1, 3.5, 4.0, 4.0, 3.5, 3.1, 2.6},
                                                   {2.5, 3.1, 3.4, 3.2, 3.2, 3.4, 3.1, 2.5},
                                                   {2.1, 2.4, 2.6, 2.7, 2.7, 2.6, 2.4, 2.1},
                                                   {1.6, 2.0, 1.7, 1.7, 1.7, 1.7, 2.0, 1.6}};
    
    private double[][] kingWeighting4WhiteMid = {{90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0},
                                                 {90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0, 90.0},
                                                 {90.0, 93.0, 93.0, 93.0, 93.0, 93.0, 93.0, 90.0},
                                                 {93.0, 94.0, 94.0, 94.5, 94.5, 94.0, 94.0, 93.0},
                                                 {94.5, 95.0, 95.0, 95.0, 95.0, 95.0, 95.0, 94.5},
                                                 {96.5, 96.5, 96.5, 96.5, 96.5, 96.5, 96.5, 96.5},
                                                 {98.0, 98.0, 98.0, 98.0, 98.0, 98.0, 98.0, 98.0},
                                                 {100.0, 100.0, 100.7, 99.0, 100.0, 98.0, 101.8, 101.0}};

    //Endgame Piece Values
    private double[][] pawnWeighting4WhiteEnd = {{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                                                 {4.0, 3.8, 3.6, 3.6, 3.6, 3.6, 3.8, 4.0},
                                                 {2.8, 2.7, 2.5, 2.4, 2.4, 2.5, 2.7, 2.8},
                                                 {2.4, 2.2, 2.0, 1.8, 1.8, 2.0, 2.2, 2.4},
                                                 {2.0, 1.8, 1.6, 1.4, 1.4, 1.6, 1.8, 2.0},
                                                 {1.2, 1.1, 1.0, 0.9, 0.9, 1.0, 1.1, 1.2},
                                                 {1.2, 1.1, 1.0, 0.9, 0.9, 1.0, 1.1, 1.2},
                                                 {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}};

    private double[][] knightWeighting4WhiteEnd = {{2.1, 2.2, 2.2, 2.2, 2.2, 2.2, 2.2, 2.1},
                                                   {2.2, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.2},
                                                   {2.2, 2.5, 3.0, 3.0, 3.0, 3.0, 2.5, 2.2},
                                                   {2.2, 2.5, 3.0, 3.0, 3.0, 3.0, 2.5, 2.2},
                                                   {2.2, 2.5, 3.0, 3.0, 3.0, 3.0, 2.5, 2.2},
                                                   {2.2, 2.5, 3.0, 3.0, 3.0, 3.0, 2.5, 2.2},
                                                   {2.2, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.2},
                                                   {2.1, 2.2, 2.2, 2.2, 2.2, 2.2, 2.2, 2.1}};
    //Max point val for king will be 98.5 for convenience
    private double[][] kingWeighting4WhiteEnd = {{97.5, 97.5, 97.5, 97.5, 97.5, 97.5, 97.5, 97.5},
                                                 {97.5, 98.5, 98.5, 98.5, 98.5, 98.5, 98.5, 97.5},
                                                 {97.5, 98.5, 98.5, 98.5, 98.5, 98.5, 98.5, 97.5},
                                                 {97.5, 98.5, 98.5, 98.1, 98.1, 98.5, 98.5, 97.5},
                                                 {97.5, 98.5, 98.5, 98.1, 98.1, 98.5, 98.5, 97.5},
                                                 {97.5, 98.5, 98.5, 98.5, 98.5, 98.5, 98.5, 97.5},
                                                 {97.5, 98.5, 98.5, 98.5, 98.5, 98.5, 98.5, 97.5},
                                                 {97.5, 97.5, 97.5, 97.5, 97.5, 97.5, 97.5, 97.5}};
    public ChessPiece(String name, String unicodeVal, int numVal, int locI, int locJ) {
        this.name = name;
        this.unicodeVal = unicodeVal;
        this.numVal = (double)numVal;
        location[0] = locI;
        location[1] = locJ;
    }
    public int[] getLocation() {
        return location;
    }
    public String getName() {
        return name;
    }
    public String getUnicode() {
        return unicodeVal;
    }
    public double getNumValEarly(ChessPiece[][] board) {
        int loc0 = (isWhite()) ? location[0] : (7-location[0]);
        if (name.equals("Pawn")) {
            return pawnWeighting4WhiteEarly[loc0][location[1]] + getIncr(board); //If defended, value increases
        } else if (name.equals("Knight")) {
            return knightWeighting4WhiteEarly[loc0][location[1]];
        } else if (name.equals("King")) {
            return kingWeighting4WhiteEarly[loc0][location[1]];
        }
        return numVal;
    }
    public double getNumValMid(ChessPiece[][] board) {
        int loc0 = (isWhite()) ? location[0] : (7-location[0]);
        if (name.equals("Pawn")) {
            return pawnWeighting4WhiteMid[loc0][location[1]] + getIncr(board);
        } else if (name.equals("Knight")) {
            return knightWeighting4WhiteMid[loc0][location[1]];
        } else if (name.equals("King")) {
            return kingWeighting4WhiteMid[loc0][location[1]];
        }
        return numVal;
    }
    public double getNumValEnd(ChessPiece[][] board) {
        int loc0 = (isWhite()) ? location[0] : (7-location[0]);
        if (name.equals("Pawn")) {
            double incr = getIncr(board);
            if (incr == 0.2) {
                incr = 0.3;
            }
            return pawnWeighting4WhiteEnd[loc0][location[1]] + incr;
        } else if (name.equals("Knight")) {
            return knightWeighting4WhiteEnd[loc0][location[1]];
        } else if (name.equals("King")) {
            return kingWeighting4WhiteEnd[loc0][location[1]];
        }
        return numVal;
    }
    private double getIncr(ChessPiece[][] board) {
        double incr = 0.0;
        int incrloc0 = (isWhite()) ? 1 : -1;
        if (location[1] != 0 && board[location[0] + incrloc0][location[1] - 1] != null && board[location[0] + incrloc0][location[1] - 1].getUnicode().equals(unicodeVal)) {
            incr = 0.2;
        }
        if (location[1] != 7 && board[location[0] + incrloc0][location[1] + 1] != null && board[location[0] + incrloc0][location[1] + 1].getUnicode().equals(unicodeVal)) {
            incr = 0.2;
        }
        return incr;
    }
    public void setLocation(int i, int j) {
        location[0] = i;
        location[1] = j;
    }
    public abstract boolean isWhite();
    public ArrayList<Integer[]> getPossibleMovesExceptKing(ChessPiece[][] board) {
        ArrayList<Integer[]> possibleMoves = new ArrayList<>();//contains only moves that are ACTUALLY possible (doesn't account for king moving to checked position)
        switch(name) {
            case "Pawn":
                return getPossibleMoves4Pawn(possibleMoves, board);
            case "Rook":
                return getPossibleMoves4Rook(possibleMoves, board);
            case "Knight":
                return getPossibleMoves4Knight(possibleMoves, board);
            case "Bishop":
                return getPossibleMoves4Bishop(possibleMoves, board);
            case "Queen":
                possibleMoves = getPossibleMoves4Rook(possibleMoves, board);
                return getPossibleMoves4Bishop(possibleMoves, board);
        }
        return possibleMoves;
    } 
    public ArrayList<Integer[]> getPossibleMoves(Board board, ArrayList<ChessPiece> whitePieces, ArrayList<ChessPiece> blackPieces) {
        if (!name.equals("King")) {
            return getPossibleMovesExceptKing(board.board);
        }
        ArrayList<Integer[]> possibleMoves = new ArrayList<>();
        return getPossibleMoves4King(possibleMoves, board, whitePieces, blackPieces);
    } 
    protected abstract ArrayList<Integer[]> getPossibleMoves4Pawn(ArrayList<Integer[]> possibleMoves, ChessPiece[][] board);
    protected ArrayList<Integer[]> getPossibleMoves4Rook(ArrayList<Integer[]> possibleMoves, ChessPiece[][] board) {
        //Go L
        int tempI = location[0];
        int tempJ = location[1] - 1;
        while(tempJ >= 0) {
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() == isWhite()) {
                break;
            }
            Integer[] tempArr = {tempI, tempJ};
            possibleMoves.add(tempArr);
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() != isWhite()) {
                break;
            }
            tempJ--;
        }
        //Go Up
        tempI = location[0] - 1;
        tempJ = location[1];
        while(tempI >= 0) {
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() == isWhite()) {
                break;
            }
            Integer[] tempArr = {tempI, tempJ};
            possibleMoves.add(tempArr);
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() != isWhite()) {
                break;
            }
            tempI--;
        }
        //Go R
        tempI = location[0];
        tempJ = location[1] + 1;
        while(tempJ < 8) {
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() == isWhite()) {
                break;
            }
            Integer[] tempArr = {tempI, tempJ};
            possibleMoves.add(tempArr);
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() != isWhite()) {
                break;
            }
            tempJ++;
        }
        //Go D
        tempI = location[0] + 1;
        tempJ = location[1];
        while(tempI < 8) {
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() == isWhite()) {
                break;
            }
            Integer[] tempArr = {tempI, tempJ};
            possibleMoves.add(tempArr);
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() != isWhite()) {
                break;
            }
            tempI++;
        }
        return possibleMoves;
    } 
    protected ArrayList<Integer[]> getPossibleMoves4Knight(ArrayList<Integer[]> possibleMoves, ChessPiece[][] board) {
        Integer[] iChanges = {1, 2, 2, 1, -1, -2, -2, -1};
        Integer[] jChanges = {2, 1, -1, -2, -2, -1, 1, 2};
        for (int i = 0; i < iChanges.length; i++) {
            int newI = location[0] + iChanges[i];
            int newJ = location[1] + jChanges[i];
            if (newI >= 0 && newI < 8 && newJ >= 0 && newJ < 8 && (board[newI][newJ] == null || board[newI][newJ].isWhite() != isWhite())) {
                Integer[] temp = {newI, newJ}; 
                possibleMoves.add(temp);
            }
        }
        return possibleMoves;
    } 
    protected ArrayList<Integer[]> getPossibleMoves4Bishop(ArrayList<Integer[]> possibleMoves, ChessPiece[][] board) {
        //Go TL
        int tempI = location[0] - 1;
        int tempJ = location[1] - 1;
        while(tempI >= 0 && tempJ >= 0) {
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() == isWhite()) {
                break;
            }
            Integer[] tempArr = {tempI, tempJ};
            possibleMoves.add(tempArr);
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() != isWhite()) {
                break;
            }
            tempI--;
            tempJ--;
        }
        //Go BL
        tempI = location[0] + 1;
        tempJ = location[1] - 1;
        while(tempI < 8 && tempJ >= 0) {
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() == isWhite()) {
                break;
            }
            Integer[] tempArr = {tempI, tempJ};
            possibleMoves.add(tempArr);
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() != isWhite()) {
                break;
            }
            tempI++;
            tempJ--;
        }
        //Go TR
        tempI = location[0] - 1;
        tempJ = location[1] + 1;
        while(tempI >= 0 && tempJ < 8) {
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() == isWhite()) {
                break;
            }
            Integer[] tempArr = {tempI, tempJ};
            possibleMoves.add(tempArr);
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() != isWhite()) {
                break;
            }
            tempI--;
            tempJ++;
        }
        //Go BR
        tempI = location[0] + 1;
        tempJ = location[1] + 1;
        while(tempI < 8 && tempJ < 8) {
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() == isWhite()) {
                break;
            }
            Integer[] tempArr = {tempI, tempJ};
            possibleMoves.add(tempArr);
            if (board[tempI][tempJ] != null && board[tempI][tempJ].isWhite() != isWhite()) {
                break;
            }
            tempI++;
            tempJ++;
        }
        return possibleMoves;
    } 
    protected ArrayList<Integer[]> getPossibleMoves4King(ArrayList<Integer[]> possibleMoves, Board board, ArrayList<ChessPiece> whitePieces, ArrayList<ChessPiece> blackPieces) {
        int locI = location[0];
        int locJ = location[1];
        int sPointI = (locI == 0) ? 0 : locI - 1;
        int sPointJ = (locJ == 0) ? 0 : locJ - 1;
        for (int i = sPointI; i < ((locI + 2 > 8) ? 8 : locI + 2); i++) {
            for (int j = sPointJ; j < ((locJ + 2 > 8) ? 8 : locJ + 2); j++) {
                if (!(i == locI && j == locJ) && (board.board[i][j] == null || board.board[i][j].isWhite() != isWhite())) {
                    if (isMovePossible(locI, locJ, i, j, board.board, whitePieces, blackPieces) && !isNextToOpposingKing(whitePieces, blackPieces, board.board, i, j)) {
                        Integer[] temp = {i, j}; 
                        possibleMoves.add(temp);
                    }
                }
            }
        }
        if (board.canCastleKingSide(isWhite())) {
            Integer[] temp = {((isWhite()) ? 7 : 0),6};
            possibleMoves.add(temp);
        }
        if (board.canCastleQueenSide(isWhite())) {
            Integer[] temp = {((isWhite()) ? 7 : 0),2};
            possibleMoves.add(temp);
        }
        return possibleMoves;
    }
    public boolean isMovePossible(int locI, int locJ, int newI, int newJ, ChessPiece[][] board, ArrayList<ChessPiece> whitePieces, ArrayList<ChessPiece> blackPieces) {
        ChessPiece origPiece = board[newI][newJ];
        tempMovePiece(locI, locJ, newI, newJ, board, null);
        boolean isMovePossible = !isInCheck(board[newI][newJ].isWhite(), whitePieces, blackPieces, board);
        tempMovePiece(newI, newJ, locI, locJ, board, origPiece);
        return isMovePossible;
    }
    protected void tempMovePiece(int initI, int initJ, int finI, int finJ, ChessPiece[][] board, ChessPiece toAddInPlace) { //Assumes move is possible
        board[finI][finJ] = board[initI][initJ];
        board[finI][finJ].setLocation(finI, finJ);
        board[initI][initJ] = toAddInPlace;
    }
    protected boolean isInCheck(boolean isWhite, ArrayList<ChessPiece> whitePieces, ArrayList<ChessPiece> blackPieces, ChessPiece[][] board) {
        int[] locKing = new int[2];
        ArrayList<ChessPiece> defendingPieces = whitePieces;//The one that might be in check
        ArrayList<ChessPiece> attackingPieces = blackPieces;
        if (!isWhite) {
            defendingPieces = blackPieces;
            attackingPieces = whitePieces;
        }
        //Locates King
        for (int i = 0; i < defendingPieces.size(); i++) {
            if (defendingPieces.get(i).getName().equals("King")) {
                locKing = defendingPieces.get(i).getLocation();
            }
        }
        //Sees if possible moves land on Opposing King
        for (int i = 0; i < attackingPieces.size(); i++) {
            ArrayList<Integer[]> tempPossibleMoves = new ArrayList<>();
            tempPossibleMoves = attackingPieces.get(i).getPossibleMovesExceptKing(board);
            for (int j = 0; j < tempPossibleMoves.size(); j++) {
                if (tempPossibleMoves.get(j)[0] == locKing[0] && tempPossibleMoves.get(j)[1] == locKing[1]) {
                    return true;
                }
            }
        }
        return false;
    }
    protected boolean isNextToOpposingKing(ArrayList<ChessPiece> whitePieces, ArrayList<ChessPiece> blackPieces, ChessPiece[][] board, int newI, int newJ) {
        int locI = newI;
        int locJ = newJ;
        int sPointI = (locI == 0) ? 0 : locI - 1;
        int sPointJ = (locJ == 0) ? 0 : locJ - 1;
        for (int i = sPointI; i < ((locI + 2 > 8) ? 8 : locI + 2); i++) {
            for (int j = sPointJ; j < ((locJ + 2 > 8) ? 8 : locJ + 2); j++) {
                if (board[i][j] != null && board[i][j].isWhite() != isWhite() && board[i][j].getName().equals("King")) {
                    return true;
                }
            }
        }
        return false;
    }
    public String toString() {
        return unicodeVal;
    }
}