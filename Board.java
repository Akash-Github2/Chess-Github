import java.util.*;
public class Board {
    public ChessPiece[][] board = new ChessPiece[8][8];
    public ArrayList<ChessPiece> whitePieces = new ArrayList<>();
    public ArrayList<ChessPiece> blackPieces = new ArrayList<>();
    public boolean wKingHasMoved = false;
    public boolean bKingHasMoved = false;
    public boolean wRookLHasMoved = false;
    public boolean bRookLHasMoved = false;
    public boolean wRookRHasMoved = false;
    public boolean bRookRHasMoved = false;
    public boolean isAlmostCheckMate = false;
    public int movesSinceNoCaptureOrPawn = 0;
    public Board() {
        board = initializeBoard(board);
        for (int i = 0; i < board[0].length; i++) {
            if (board[1][i] != null) {
                blackPieces.add(board[1][i]);
            }
        }
        for (int i = 0; i < board[0].length; i++) {
            if (i == 4 || board[0][i] == null) {
                continue;
            }
            blackPieces.add(board[0][i]);
        }
        blackPieces.add(board[0][4]); //Adds king at the end
        for (int i = 0; i < board[0].length; i++) {
            if (board[6][i] != null) {
                whitePieces.add(board[6][i]);
            }
        }
        for (int i = 0; i < board[0].length; i++) {
            if (i == 4 || board[7][i] == null) {
                continue;
            }
            whitePieces.add(board[7][i]);
        }
        whitePieces.add(board[7][4]);
    }
    public boolean makeMoveOverall(int initI, int initJ, int finI, int finJ, boolean temp) {
        boolean didPawnPromo = false;
        boolean blackTurn = !board[initI][initJ].isWhite();
        if (blackTurn && board[initI][initJ].getName().equals("Pawn") && finI == 7) { //Black Pawn Promotion
            ChessPiece promotionPiece = new BlackPiece("Queen", "\u265B", 10, finI, finJ);
            pawnPromotion(initI, initJ, finI, finJ, promotionPiece);
            didPawnPromo = true;
        } else if (!blackTurn && board[initI][initJ].getName().equals("Pawn") && finI == 0) { //White Pawn Promotion
            ChessPiece promotionPiece = new WhitePiece("Queen", "\u2655", 10, finI, finJ);
            pawnPromotion(initI, initJ, finI, finJ, promotionPiece);
            didPawnPromo = true;
        } else {
            if (!temp) {
                if (blackTurn) {
                    if (board[initI][initJ].getName().equals("King")) {
                        bKingHasMoved = true;
                    }
                    if (board[initI][initJ].getName().equals("Rook") && initI == 0 && initJ == 0) {
                        bRookLHasMoved = true;
                    }
                    if (board[initI][initJ].getName().equals("Rook") && initI == 0 && initJ == 7) {
                        bRookRHasMoved = true;
                    }
                } else {
                    if (board[initI][initJ].getName().equals("King")) {
                        wKingHasMoved = true;
                    }
                    if (board[initI][initJ].getName().equals("Rook") && initI == 7 && initJ == 0) {
                        wRookLHasMoved = true;
                    }
                    if (board[initI][initJ].getName().equals("Rook") && initI == 7 && initJ == 7) {
                        wRookRHasMoved = true;
                    }
                }
            }
            if (initI == 7 && initJ == 4 && finI == 7 && (finJ == 2 || finJ == 6) && board[initI][initJ].getName().equals("King")) { //Castling White
                movePieceCastling(initI, initJ, finI, finJ);
            } else if (initI == 0 && initJ == 4 && finI == 0 && (finJ == 2 || finJ == 6) && board[initI][initJ].getName().equals("King")) { //Castling Black
                movePieceCastling(initI, initJ, finI, finJ);
            } else {
                movePiece(initI, initJ, finI, finJ);
            }
        }
        return didPawnPromo;
    }
    public void movePiece(int initI, int initJ, int finI, int finJ) { //Assumes move is possible
        if (board[finI][finJ] != null) {
            ArrayList<ChessPiece> toCheck = blackPieces;
            if (!board[initI][initJ].isWhite()) {
                toCheck = whitePieces;
            }
            for (int i = 0; i < toCheck.size(); i++) {
                int[] tempLoc = toCheck.get(i).getLocation();
                if (tempLoc[0] == finI && tempLoc[1] == finJ) {
                    toCheck.remove(i);
                    board[finI][finJ] = null;
                    break;
                }
            }
        }
        board[finI][finJ] = board[initI][initJ];
        board[finI][finJ].setLocation(finI, finJ);
        board[initI][initJ] = null;
    }
    public void movePieceCastling(int initI, int initJ, int finI, int finJ) { //Assumes move is possible
        if (finI == 7 && finJ == 6) {
            board[7][5] = board[7][7];
            board[7][5].setLocation(7, 5);
            board[7][7] = null;
        }
        if (finI == 7 && finJ == 2) {
            board[7][3] = board[7][0];
            board[7][3].setLocation(7, 3);
            board[7][0] = null;
        }
        if (finI == 0 && finJ == 6) {
            board[0][5] = board[0][7];
            board[0][5].setLocation(0, 5);
            board[0][7] = null;
        }
        if (finI == 0 && finJ == 2) {
            board[0][3] = board[0][0];
            board[0][3].setLocation(0, 3);
            board[0][0] = null;
        }
        //For King Movement
        board[finI][finJ] = board[initI][initJ];
        board[finI][finJ].setLocation(finI, finJ);
        board[initI][initJ] = null;
    }
    public void pawnPromotion(int initI, int initJ, int finI, int finJ, ChessPiece promotionPiece) {
        movePiece(initI, initJ, finI, finJ);
        ArrayList<ChessPiece> teamPieces = (board[finI][finJ].isWhite()) ? whitePieces : blackPieces;
        for (int a = 0; a < teamPieces.size(); a++) {
            int[] tempLoc = teamPieces.get(a).getLocation();
            if (tempLoc[0] == finI && tempLoc[1] == finJ) {
                teamPieces.remove(a);
                board[finI][finJ] = null;
                break;
            }
        }
        board[finI][finJ] = promotionPiece;
        teamPieces.add(board[finI][finJ]);
    }
    public void fullyResetMove(int initI, int initJ, int finI, int finJ, boolean isBlackTurn, boolean didPawnPromo, ChessPiece origPiece) {
        if (initI == 7 && initJ == 4 && finI == 7 && (finJ == 2 || finJ == 6) && board[finI][finJ].getName().equals("King")) { //Reset Castling White
            resetCastling(initI, initJ, finI, finJ);
        } else if (initI == 0 && initJ == 4 && finI == 0 && (finJ == 2 || finJ == 6) && board[finI][finJ].getName().equals("King")) { //Reset Castling Black
            resetCastling(initI, initJ, finI, finJ);
        } else {
            resetMove(initI, initJ, finI, finJ, isBlackTurn, didPawnPromo, origPiece);
        }
    }
    public void resetMove(int initI, int initJ, int finI, int finJ, boolean isBlackTurn, boolean didPawnPromo, ChessPiece origPiece) {
        movePiece(finI, finJ, initI, initJ);
        if (origPiece != null) {
            if (isBlackTurn) {
                whitePieces.add(origPiece);
            } else {
                blackPieces.add(origPiece);
            }
        }
        board[finI][finJ] = origPiece;
        if (didPawnPromo) {
            if (isBlackTurn) {
                ChessPiece bPawn = new BlackPiece("Pawn", "\u265F", 1, initI, initJ);
                for (int a = 0; a < blackPieces.size(); a++) {
                    int[] tempLoc = blackPieces.get(a).getLocation();
                    if (tempLoc[0] == initI && tempLoc[1] == initJ) {
                        blackPieces.remove(a);
                        break;
                    }
                }
                board[initI][initJ] = bPawn;
                blackPieces.add(board[initI][initJ]);
            } else {
                ChessPiece wPawn = new WhitePiece("Pawn", "\u2659", 1, initI, initJ);
                for (int a = 0; a < whitePieces.size(); a++) {
                    int[] tempLoc = whitePieces.get(a).getLocation();
                    if (tempLoc[0] == initI && tempLoc[1] == initJ) {
                        whitePieces.remove(a);
                        break;
                    }
                }
                board[initI][initJ] = wPawn;
                whitePieces.add(board[initI][initJ]);
            }
        }
    }
    public void resetCastling(int initI, int initJ, int finI, int finJ) { //Assumes move is possible
        if (finI == 7 && finJ == 6) {
            board[7][7] = board[7][5];
            board[7][7].setLocation(7, 7);
            board[7][5] = null;
        }
        if (finI == 7 && finJ == 2) {
            board[7][0] = board[7][3];
            board[7][0].setLocation(7, 0);
            board[7][3] = null;
        }
        if (finI == 0 && finJ == 6) {
            board[0][7] = board[0][5];
            board[0][7].setLocation(0, 7);
            board[0][5] = null;
        }
        if (finI == 0 && finJ == 2) {
            board[0][0] = board[0][3];
            board[0][0].setLocation(0, 0);
            board[0][3] = null;
        }
        //For King Movement
        board[initI][initJ] = board[finI][finJ];
        board[initI][initJ].setLocation(initI, initJ);
        board[finI][finJ] = null;
    }
    public boolean canCastleKingSide(boolean isWhite) {
        int rowNum = (isWhite) ? 7 : 0;
        if (board[rowNum][4] == null || board[rowNum][7] == null || board[rowNum][7].isWhite() != isWhite) {
            return false;
        }
        if (isWhite) {
            if (wKingHasMoved || wRookRHasMoved || isInCheck(true)) {
                return false;
            }
        } else {
            if (bKingHasMoved || bRookRHasMoved || isInCheck(false)) {
                return false;
            }
        }
        if (board[rowNum][5] != null || board[rowNum][6] != null) {
            return false;
        }
        if (!board[rowNum][4].isMovePossible(rowNum, 4, rowNum, 5, board, whitePieces, blackPieces)) {
            return false;
        }
        if (!board[rowNum][4].isMovePossible(rowNum, 4, rowNum, 6, board, whitePieces, blackPieces)) {
            return false;
        }
        return true;
    }
    public boolean canCastleQueenSide(boolean isWhite) {
        int rowNum = (isWhite) ? 7 : 0;
        if (board[rowNum][4] == null || board[rowNum][0] == null || board[rowNum][0].isWhite() != isWhite) {
            return false;
        }
        if (isWhite) {
            if (wKingHasMoved || wRookLHasMoved || isInCheck(true)) {
                return false;
            }
        } else {
            if (bKingHasMoved || bRookLHasMoved || isInCheck(false)) {
                return false;
            }
        }
        if (board[rowNum][1] != null || board[rowNum][2] != null || board[rowNum][3] != null) {
            return false;
        }
        if (!board[rowNum][4].isMovePossible(rowNum, 4, rowNum, 3, board, whitePieces, blackPieces)) {
            return false;
        }
        if (!board[rowNum][4].isMovePossible(rowNum, 4, rowNum, 2, board, whitePieces, blackPieces)) {
            return false;
        }
        return true;
    }
    public boolean isInCheck(boolean isWhite) {
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
            tempPossibleMoves = attackingPieces.get(i).getPossibleMovesExceptKing(board); //King can't check other king so won't include King
            for (int j = 0; j < tempPossibleMoves.size(); j++) {
                if (tempPossibleMoves.get(j)[0] == locKing[0] && tempPossibleMoves.get(j)[1] == locKing[1]) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean isCheckMate(boolean isWhite) {
        if (!isInCheck(isWhite)) {
            return false;
        }
        boolean shouldReturn = false;
        int[] locKing = new int[2];
        ArrayList<ChessPiece> defendingPieces = (isWhite) ? whitePieces : blackPieces;//The one that might be in check
        //Locates King
        for (int i = 0; i < defendingPieces.size(); i++) {
            if (defendingPieces.get(i).getName().equals("King")) {
                locKing = defendingPieces.get(i).getLocation();
            }
        }
        //Other pieces move to protect king
        for (int i = 0; i < defendingPieces.size(); i++) {
            ArrayList<Integer[]> tempPossibleMoves = new ArrayList<>();
            tempPossibleMoves = defendingPieces.get(i).getPossibleMovesExceptKing(board);
            int locI = defendingPieces.get(i).getLocation()[0];
            int locJ = defendingPieces.get(i).getLocation()[1];
            for (int j = 0; j < tempPossibleMoves.size(); j++) {
                ChessPiece origPiece = board[tempPossibleMoves.get(j)[0]][tempPossibleMoves.get(j)[1]];
                movePiece(locI, locJ, tempPossibleMoves.get(j)[0], tempPossibleMoves.get(j)[1]);
                if (!shouldReturn) {
                    shouldReturn = !isInCheck(isWhite);
                }
                movePiece(tempPossibleMoves.get(j)[0], tempPossibleMoves.get(j)[1], locI, locJ);
                if (origPiece != null) {
                  if (isWhite) {
                    blackPieces.add(origPiece);
                  } else {
                    whitePieces.add(origPiece);
                  }
                }
                board[tempPossibleMoves.get(j)[0]][tempPossibleMoves.get(j)[1]] = origPiece;
                tempPossibleMoves.remove(j);
                j--;
            }
        }
        if (shouldReturn) {
            return false;
        }
        //Can King moves to safety
        ArrayList<Integer[]> kingPossibleMoves = new ArrayList<>();
        kingPossibleMoves = board[locKing[0]][locKing[1]].getPossibleMoves(this, whitePieces, blackPieces);
        return kingPossibleMoves.size() == 0;
    }
    public boolean isTie(boolean isWhite, TreeMap<String, Integer> boardFreq) {
        ArrayList<String> allPossibleMoves = retAllPossibleMoves(isWhite);
        if (allPossibleMoves.size() == 0 || (whitePieces.size() == 1 && blackPieces.size() == 1)) {
            return true;
        }
        if (whitePieces.size() == 2 && blackPieces.size() == 1) {
            for (int i = 0; i < whitePieces.size(); i++) {
                if (whitePieces.get(i).getName().equals("Bishop") || whitePieces.get(i).getName().equals("Knight")){
                    return true;
                }
            }
        } else if (whitePieces.size() == 1 && blackPieces.size() == 2) {
            for (int i = 0; i < blackPieces.size(); i++) {
                if (blackPieces.get(i).getName().equals("Bishop") || blackPieces.get(i).getName().equals("Knight")){
                    return true;
                }
            }
        } else if (whitePieces.size() == 2 && blackPieces.size() == 2) {
            int wBishopIndex = -1;
            int bBishopIndex = -1;
            for (int i = 0; i < whitePieces.size(); i++) {
                if (whitePieces.get(i).getName().equals("Bishop")){
                    wBishopIndex = i;
                    break;
                }
            }
            for (int i = 0; i < blackPieces.size(); i++) {
                if (blackPieces.get(i).getName().equals("Bishop")){
                    bBishopIndex = i;
                    break;
                }
            }
            if (wBishopIndex != -1 && bBishopIndex != -1) {
                int[] wBishopLoc = whitePieces.get(wBishopIndex).getLocation();
                int[] bBishopLoc = blackPieces.get(bBishopIndex).getLocation();
                if ((wBishopLoc[0] + wBishopLoc[1]) % 2 == (bBishopLoc[0] + bBishopLoc[1]) % 2){
                    return true;
                }
            }
        }
        if (boardFreq.containsValue(3)) { //Threefold Rule
            return true;
        }
        if (movesSinceNoCaptureOrPawn == 100) { //50 moves with no capture or pawn movement
            return true;
        }
        return false;
    }
    public ArrayList<String> retAllPossibleMoves(boolean isWhite) {
        ArrayList<String> allPossibleMoves = new ArrayList<>();
        ArrayList<ChessPiece> currSide = (isWhite) ? whitePieces : blackPieces;
        for (int i = 0; i < currSide.size(); i++) {
            ArrayList<Integer[]> tempPossibleMoves = new ArrayList<>();
            tempPossibleMoves = currSide.get(i).getPossibleMoves(this, whitePieces, blackPieces);
            int locI = currSide.get(i).getLocation()[0];
            int locJ = currSide.get(i).getLocation()[1];
            for (int j = 0; j < tempPossibleMoves.size(); j++) {
                boolean works = false;
                ChessPiece origPiece = board[tempPossibleMoves.get(j)[0]][tempPossibleMoves.get(j)[1]];
                movePiece(locI, locJ, tempPossibleMoves.get(j)[0], tempPossibleMoves.get(j)[1]);
                works = !isInCheck(isWhite);
                movePiece(tempPossibleMoves.get(j)[0], tempPossibleMoves.get(j)[1], locI, locJ);
                if (origPiece != null) {
                  if (isWhite) {
                    blackPieces.add(origPiece);
                  } else {
                    whitePieces.add(origPiece);
                  }
                }
                board[tempPossibleMoves.get(j)[0]][tempPossibleMoves.get(j)[1]] = origPiece;
                if (works) {
                    allPossibleMoves.add(locI + "," + locJ + "->" + tempPossibleMoves.get(j)[0] + "," + tempPossibleMoves.get(j)[1]);
                }
            }
        }
        return allPossibleMoves;
    }
    public void printAllPossibleMoves(boolean isWhite) {
        ArrayList<String> allPossibleMoves = retAllPossibleMoves(isWhite);
        System.out.println("Possible Moves: (" + allPossibleMoves.size() + ")");
        if (allPossibleMoves.size() == 0) {
            System.out.println("N/A");
            return;
        }
        for (int i = 0; i < allPossibleMoves.size(); i++) {
            System.out.println(allPossibleMoves.get(i));
        }
    }
    public double getWhiteVal(int moveCounter) {
        double val = 0;
        for (ChessPiece cp: whitePieces) {
            if (isEarlyGame(moveCounter)) { //Early Game
                val += cp.getNumValEarly(board);
            } else if (isMidGame(moveCounter)) { //Mid Game
                val += cp.getNumValMid(board);
            } else { //End Game
                val += cp.getNumValEnd(board);
            }
        }
        return val;
    }
    public double getBlackVal(int moveCounter) {
        double val = 0;
        for (ChessPiece cp: blackPieces) {
            if (isEarlyGame(moveCounter)) { //Early Game
                val += cp.getNumValEarly(board);
            } else if (isMidGame(moveCounter)) { //Mid Game
                val += cp.getNumValMid(board);
            } else { //End Game
                val += cp.getNumValEnd(board);
            }
        }
        return val;
    }
    //For early game; adjust for mid and end game
    public double getNumWhiteOver1() {
        double total = 0;
        for (ChessPiece cp: whitePieces) {
            if (cp.getNumValEarly(board) > 1) {
                total+=cp.getNumValEarly(board);
            }
        }
        return total;
    }
    public double getNumBlackOver1() {
        double total = 0;
        for (ChessPiece cp: blackPieces) {
            if (cp.getNumValEarly(board) > 1) {
                total+=cp.getNumValEarly(board);
            }
        }
        return total;
    }
    public ChessPiece[][] initializeBoard(ChessPiece[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = null;
            }
        }
        //Black Pieces
        board[0][0] = new BlackPiece("Rook", "\u265C", 5, 0, 0);
        board[0][1] = new BlackPiece("Knight", "\u265E", 3, 0, 1);
        board[0][2] = new BlackPiece("Bishop", "\u265D", 3, 0, 2);
        board[0][3] = new BlackPiece("Queen", "\u265B", 10, 0, 3);
        board[0][4] = new BlackPiece("King", "\u265A", 100, 0, 4);
        board[0][5] = new BlackPiece("Bishop", "\u265D", 3, 0, 5);
        board[0][6] = new BlackPiece("Knight", "\u265E", 3, 0, 6);
        board[0][7] = new BlackPiece("Rook", "\u265C", 5, 0, 7);
        for (int i = 0; i < board.length; i++) {
            board[1][i] = new BlackPiece("Pawn", "\u265F", 1, 1, i);
        }
        //White Pieces
        board[7][0] = new WhitePiece("Rook", "\u2656", 5, 7, 0);
        board[7][1] = new WhitePiece("Knight", "\u2658", 3, 7, 1);
        board[7][2] = new WhitePiece("Bishop", "\u2657", 3, 7, 2);
        board[7][3] = new WhitePiece("Queen", "\u2655", 10, 7, 3);
        board[7][4] = new WhitePiece("King", "\u2654", 100, 7, 4);
        board[7][5] = new WhitePiece("Bishop", "\u2657", 3, 7, 5);
        board[7][6] = new WhitePiece("Knight", "\u2658", 3, 7, 6);
        board[7][7] = new WhitePiece("Rook", "\u2656", 5, 7, 7);
        for (int i = 0; i < board.length; i++) {
            board[6][i] = new WhitePiece("Pawn", "\u2659", 1, 6, i);
        }
        return board;
    }
    //Only for testing purposes
    public void testPossibleMoves(int i, int j) {
        ArrayList<Integer[]> test = new ArrayList<>();
        test = board[i][j].getPossibleMoves(this, whitePieces, blackPieces);
        for (int a = 0; a < test.size(); a++) {
            System.out.println(test.get(a)[0] + "," + test.get(a)[1]);
        }
    }
    public String toString() {
        String retStr = "";
        for (int i = 0; i < board.length; i++) {
            retStr += "---------------------------------\n";
            for (int j = 0; j < board[i].length; j++) {
                retStr += "| " + ((board[i][j] != null) ? board[i][j] : " ") + " ";
            }
            retStr += "|\n";
        }
        retStr += "---------------------------------\n";
        return retStr;
    }
    public String formatBoardForFile(boolean isComputerWhite, int depth) {
        String retStr = "";
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                retStr += ((board[i][j] != null) ? board[i][j] : " ");
            }
        }
        boolean isWhite = isComputerWhite;
        if (depth % 2 == 1) {
            isWhite = !isWhite;
        }
        return retStr + ((isWhite) ? "1" : "0");
    }
    public boolean isEarlyGame(int moveCounter) {
        return moveCounter < 18 || (whitePieces.size() > 14 && blackPieces.size() > 14);
    }
    public boolean isMidGame(int moveCounter) {
        return moveCounter < 55 || (whitePieces.size() > 6 && blackPieces.size() > 6);
    }
}