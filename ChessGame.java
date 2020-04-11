/**
 * @author Akash Pulinthanathu
 * Chess Game with Advanced AI
 */
import java.io.*;
import java.util.*;
public class ChessGame {
    public static int recur = 0;
    public static int moveCounter = 0;
    public static TreeMap<String, Integer> boardFreq = new TreeMap<>(); //Checks for 3 fold rule (tie)
    public static TreeMap<String, MoveVal> bestMoveLog = new TreeMap<>(); //Overall
    public static TreeMap<String, MoveVal> bestMoveLog4 = new TreeMap<>(); //Log for Depth 4
    public static TreeMap<String, MoveVal> bestMoveLog2 = new TreeMap<>(); //Log for Depth 2
    public static String folder = "/Users/akash/software/Akash/Java Projects/Chess-Github/";
    public static int mainDepth = 4;
    public static void main(String args[]) { //Driver
      if (!System.getProperty("os.name").equals("Mac OS X")) {
        folder = "E:/Akash/Java Projects/Chess-Github/";
      }
      Board chessBoard = new Board();
      System.out.print("\033[H\033[2J"); //Clear Console Command
      clearMoveOutputFile();
      fillBestMoveLog("moveTB-D4.txt", bestMoveLog, bestMoveLog4, true);
      //fillBestMoveLog("moveTB-D2.txt", bestMoveLog, bestMoveLog2, false);//true for testing only
      playGame(chessBoard);
    }
    public static void playGame(Board board) {
        System.out.println("Welcome to Chess.  White goes first.");
        Scanner br = new Scanner(System.in);
        boolean currPlayerIsWhite = true;
        System.out.println("Initial Game Board:");
        System.out.println(board);
        while(true) {
            moveCounter++;
            board.printAllPossibleMoves(currPlayerIsWhite);
            String gamePhase = "";
            if (board.isEarlyGame(moveCounter)) {
                gamePhase = " (Early Game)";
            } else if (board.isMidGame(moveCounter)) {
                gamePhase = " (Mid Game)";
            } else {
                gamePhase = " (End Game)";
            }
            if (currPlayerIsWhite) {
                //parseAndMove("Make your move. (E.g. 6,0->4,0) : Move #" + moveCounter + gamePhase, board, currPlayerIsWhite, br);
                callAI(gamePhase, board, currPlayerIsWhite, "White");
            } else {
                //parseAndMove("Make your move. (E.g. 6,0->4,0) : Move #" + moveCounter + gamePhase, board, currPlayerIsWhite, br);
                callAI(gamePhase, board, currPlayerIsWhite, "Black");
            }
            if (board.isCheckMate(!currPlayerIsWhite)) {
                System.out.println((currPlayerIsWhite) ? "You Win!" : "Computer Wins!");
            } else if (board.isTie(!currPlayerIsWhite, boardFreq)) {
                System.out.println("Tie game!");
            }
            if (board.isCheckMate(!currPlayerIsWhite) || board.isTie(!currPlayerIsWhite, boardFreq)) {
                System.out.println("Finished Game Board:");
                System.out.println(board);
                break;
            }
            System.out.println("Current Game Board:");
            System.out.println(board);
            currPlayerIsWhite = !currPlayerIsWhite;
        }
    }
    public static double findBestMove(Board board, boolean isComputerTurn, int depth, int maxDepth, int tempMoveCounter, boolean isComputerWhite) {
        recur++;
        boolean isWhite = true;
        if ((isComputerTurn && !isComputerWhite) || (!isComputerTurn && isComputerWhite)) {
            isWhite = false;
        }
        if (board.isCheckMate(!isComputerWhite)) {
            return 1000 + (maxDepth - depth);
        } else if (board.isCheckMate(isComputerWhite)) {
            return -1000 - (maxDepth - depth);
        } else if (board.isTie(isWhite, boardFreq)) {
            if (Math.abs(board.getWhiteVal(tempMoveCounter) - board.getBlackVal(tempMoveCounter)) < 2.0) {
                return -1;
            }
            if (isComputerWhite) {
                return (board.getWhiteVal(tempMoveCounter) - board.getBlackVal(tempMoveCounter)) * -1.5;
            } else {
                return (board.getBlackVal(tempMoveCounter) - board.getWhiteVal(tempMoveCounter)) * -1.5;
            }
        }
        double optVal = (isComputerTurn) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        String optMove = "";
        ArrayList<String> allPossibleMoves = board.retAllPossibleMoves(isWhite);
        HashMap<String, Double> tiedOptMoves = new HashMap<>();
        double bestSumOver1 = 0;
        if (!(depth == 0 && (allPossibleMoves.size() == 1 || bestMoveLog.get(board.toString() + isComputerWhite + "\n") != null))) {
            if (depth < maxDepth) {
                for (int i = 0; i < allPossibleMoves.size(); i++) {
                    
                    int initI = Integer.parseInt(allPossibleMoves.get(i).substring(0,1));
                    int initJ = Integer.parseInt(allPossibleMoves.get(i).substring(2,3));
                    int finI = Integer.parseInt(allPossibleMoves.get(i).substring(5,6));
                    int finJ = Integer.parseInt(allPossibleMoves.get(i).substring(7,8));
                    if (moveCounter <= 2 && !board.board[initI][initJ].getName().equals("Pawn")) {
                        continue;
                    }
                    ChessPiece origPiece = board.board[finI][finJ];
                    double origValDiff = board.getBlackVal(tempMoveCounter) - board.getWhiteVal(tempMoveCounter);
                    if (isComputerWhite) {
                        origValDiff *= -1;
                    }
                    boolean didPawnPromo = board.makeMoveOverall(initI, initJ, finI, finJ, true); //Try Move
                    String triedBoard = board.toString();
                    if (boardFreq.get(triedBoard) == null) {
                        boardFreq.put(triedBoard, 1);
                    } else {
                        boardFreq.put(triedBoard, boardFreq.get(triedBoard) + 1);
                    }
                    double newValDiff = board.getBlackVal(tempMoveCounter) - board.getWhiteVal(tempMoveCounter);
                    if (isComputerWhite) {
                        newValDiff *= -1;
                    }
                    double diff = newValDiff - origValDiff;
                    double num = 2.0;
                    if (tempMoveCounter > 60) {
                        num = 6.0;
                    } else if (tempMoveCounter > 50) {
                        num = 5.0;
                    } else if (tempMoveCounter > 40) {
                        num = 4.0;
                    } else if (tempMoveCounter > 35) {
                        num = 3.3;
                    } else if (tempMoveCounter > 30) {
                        num = 3.0;
                    } else if (tempMoveCounter > 25) {
                        num = 2.6;
                    } else if (tempMoveCounter > 18) {
                        num = 2.4;
                    } else if (tempMoveCounter > 12) {
                        num = 2.2;
                    }
                    boolean shouldReturn = diff + num < optVal;
                    if (!isComputerTurn) {
                        shouldReturn = diff - num > optVal;
                    }
                    if (shouldReturn && tempMoveCounter < 75) {
                        //reset it
                        board.fullyResetMove(initI, initJ, finI, finJ, !isWhite, didPawnPromo, origPiece);
                        if (boardFreq.get(triedBoard) == 1) {
                            boardFreq.remove(triedBoard);
                        } else {
                            boardFreq.put(triedBoard, boardFreq.get(triedBoard) - 1);
                        }
                        continue;
                    }
                    double n = findBestMove(board, !isComputerTurn, depth + 1, maxDepth, tempMoveCounter + 1, isComputerWhite) + diff;
                    if (n > 800) {
                        n-=diff;
                    }
                    if (depth == 0) {
                        System.out.println(allPossibleMoves.get(i) + " : " + rounded(n) + "  |  " + (i+1) + "/" + allPossibleMoves.size());
                    }
                    if (isComputerTurn) {
                        double numOver1 = (isComputerWhite) ? board.getNumWhiteOver1() : board.getNumBlackOver1();
                        if (n > optVal || (n == optVal && numOver1 > bestSumOver1)) {
                            optVal = n;
                            optMove = allPossibleMoves.get(i);
                            tiedOptMoves.clear();
                            tiedOptMoves.put(allPossibleMoves.get(i), n);
                            bestSumOver1 = numOver1;
                        }
                    } else {
                        double numOver1 = (isComputerWhite) ? board.getNumBlackOver1() : board.getNumWhiteOver1();
                        if (n < optVal || (n == optVal && numOver1 > bestSumOver1)) {
                            optVal = n;
                            optMove = allPossibleMoves.get(i);
                            tiedOptMoves.clear();
                            tiedOptMoves.put(allPossibleMoves.get(i), n);
                            bestSumOver1 = numOver1;
                        }
                    }
                    if (n == optVal && ((isWhite) ? (board.getNumWhiteOver1() == bestSumOver1) : (board.getNumBlackOver1() == bestSumOver1))) {
                        tiedOptMoves.put(allPossibleMoves.get(i), n);
                    }
                    //reset it
                    board.fullyResetMove(initI, initJ, finI, finJ, !isWhite, didPawnPromo, origPiece);
                    if (boardFreq.get(triedBoard) == 1) {
                        boardFreq.remove(triedBoard);
                    } else {
                        boardFreq.put(triedBoard, boardFreq.get(triedBoard) - 1);
                    }
                }
            } else {
                return 0;
            }
        }
        boolean isAlreadyFound = false;
        if (bestMoveLog.get(board.toString() + isComputerWhite + "\n") != null) {
            optMove = bestMoveLog.get(board.toString() + isComputerWhite + "\n").move;
            optVal = bestMoveLog.get(board.toString() + isComputerWhite + "\n").val;
            isAlreadyFound = true;
        }
        if (!isAlreadyFound) {
            if (depth == 0 && allPossibleMoves.size() == 1) {
                optMove = allPossibleMoves.get(0);
            } else {
                for (String move : tiedOptMoves.keySet()) { //Eventually used to break ties
                    optMove = move;
                    optVal = tiedOptMoves.get(move);
                    break;
                }
            }
        }
        if (depth == 0) {
            String fileName = folder + "moveTB-D" + mainDepth + ".txt";
            boolean isAlreadyInIndivFile = true;
            if (mainDepth == 4) {
                if (bestMoveLog4.get(board.toString() + isComputerWhite + "\n") == null) {
                    bestMoveLog4.put(board.toString() + isComputerWhite + "\n", new MoveVal(optMove, optVal));
                    isAlreadyInIndivFile = false;
                }
            } else if (mainDepth == 2) {
                if (bestMoveLog2.get(board.toString() + isComputerWhite + "\n") == null) {
                    bestMoveLog2.put(board.toString() + isComputerWhite + "\n", new MoveVal(optMove, optVal));
                    isAlreadyInIndivFile = false;
                }
            }
            if (!isAlreadyFound) {
                bestMoveLog.put(board.toString() + isComputerWhite + "\n", new MoveVal(optMove, optVal));
            }
            if (!isAlreadyInIndivFile) {
                try{
                    File file = new File(fileName);
                    FileWriter writer = new FileWriter(file, true);
                    if (file.length() != 0) {
                        writer.write("\n");
                    }
                    writer.write(board.toString() + isComputerWhite + "\n" + optMove + "\n" + rounded(optVal));
                    writer.close();
                }catch(Exception e) {
                    System.out.println("FILE ERROR");
                }
            }

            int initI = Integer.parseInt(optMove.substring(0,1));
            int initJ = Integer.parseInt(optMove.substring(2,3));
            int finI = Integer.parseInt(optMove.substring(5,6));
            int finJ = Integer.parseInt(optMove.substring(7,8));
            int totalPieces = board.whitePieces.size() + board.blackPieces.size();
            boolean mightIncrNoCapture = false;
            if (!board.board[initI][initJ].getName().equals("Pawn")) {
                mightIncrNoCapture = true;
            }
            board.makeMoveOverall(initI, initJ, finI, finJ, false); //Make Final Move
            if (board.whitePieces.size() + board.blackPieces.size() == totalPieces && mightIncrNoCapture) {
                board.movesSinceNoCaptureOrPawn++;
            } else {
                board.movesSinceNoCaptureOrPawn = 0;
            }
            System.out.println("--------------");
            System.out.println("Num Same Values: " + tiedOptMoves.size());
            System.out.println("Optimal Move: " + optMove);
            System.out.println("Optimal Value: " + ((allPossibleMoves.size() == 1) ? "N/A" : rounded(optVal)));
            System.out.println("White Val: " + rounded(board.getWhiteVal(moveCounter)));
            System.out.println("Black Val: " + rounded(board.getBlackVal(moveCounter)));
            if (board.movesSinceNoCaptureOrPawn != 0) {
                System.out.println("Moves Since No Capture/Pawn Mvmt: " + board.movesSinceNoCaptureOrPawn);
            }
            //appendToFile(initI, initJ, finI, finJ);
            if (boardFreq.get(board.toString()) == null) {
                boardFreq.put(board.toString(), 1);
            } else {
                boardFreq.put(board.toString(), boardFreq.get(board.toString()) + 1);
            }
        }
        return optVal;
    }

    public static void parseAndMove(String message, Board board, boolean currPlayerIsWhite, Scanner br) {
        System.out.println(message);
        String input = br.nextLine();
        boolean isValid = false;
        if (board.retAllPossibleMoves(currPlayerIsWhite).contains(input)) {
            isValid = true;
        }
        while(!isValid) {
            System.out.println("Move is invalid. Try Again.");
            input = br.nextLine();
            if (board.retAllPossibleMoves(currPlayerIsWhite).contains(input)) {
                isValid = true;
            }
        }
        int initI = Integer.parseInt(input.substring(0,1));
        int initJ = Integer.parseInt(input.substring(2,3));
        int finI = Integer.parseInt(input.substring(5,6));
        int finJ = Integer.parseInt(input.substring(7,8));
        int totalPieces = board.whitePieces.size() + board.blackPieces.size();
        boolean mightIncrNoCapture = false;
        if (!board.board[initI][initJ].getName().equals("Pawn")) {
            mightIncrNoCapture = true;
        }
        board.makeMoveOverall(initI, initJ, finI, finJ, false); //Make Final Move
        if (board.whitePieces.size() + board.blackPieces.size() == totalPieces && mightIncrNoCapture) {
            board.movesSinceNoCaptureOrPawn++;
        } else {
            board.movesSinceNoCaptureOrPawn = 0;
        }
        if (boardFreq.get(board.toString()) == null) {
            boardFreq.put(board.toString(), 1);
        } else {
            boardFreq.put(board.toString(), boardFreq.get(board.toString()) + 1);
        }
        appendToFile(initI, initJ, finI, finJ);
    }
    public static void callAI(String gamePhase, Board board, boolean currPlayerIsWhite, String player) {
        System.out.println(player + "'s Turn : Move #" + moveCounter + gamePhase);
        int tempMoveCounter = moveCounter;
        int depth = mainDepth;
        // if (!board.isEarlyGame(moveCounter) && !board.isMidGame(moveCounter) && board.retAllPossibleMoves(currPlayerIsWhite).size() * board.retAllPossibleMoves(!currPlayerIsWhite).size() < 75) {
        //     depth = depth2;
        // }
        findBestMove(board, true, 0, depth, tempMoveCounter, currPlayerIsWhite);
        System.out.println("Num Recursions: " + recur);
        System.out.println("------------------------");
        recur = 0;
    }
    public static void clearMoveOutputFile() {
        try{
            File file = new File(folder + "MovesPerformed.txt");
            FileWriter writer = new FileWriter(file);
            writer.write("");
            writer.close();
        }catch(Exception e) {
            System.out.println("FILE ERROR");
        }
    }
    public static void appendToFile(int initI, int initJ, int finI, int finJ) {
        try{
            File file = new File(folder + "MovesPerformed.txt");
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
    public static void fillBestMoveLog(String filename, TreeMap<String, MoveVal> bestMoveLog, TreeMap<String, MoveVal> bestMoveLogIndiv, boolean checkToAdd) {
        try {
            File myObj = new File(folder + filename);
            Scanner reader = new Scanner(myObj);
            while (reader.hasNextLine()) {
                String boardStr = "";
                for (int i = 0; i < 18; i++) {
                    boardStr += reader.nextLine() + "\n";
                }
                String move = reader.nextLine();
                double val = Double.parseDouble(reader.nextLine());
                if (bestMoveLog.get(boardStr) == null && checkToAdd) {
                    bestMoveLog.put(boardStr, new MoveVal(move, val));
                }
                if (bestMoveLogIndiv.get(boardStr) == null) {
                    bestMoveLogIndiv.put(boardStr, new MoveVal(move, val));
                }
            }
            reader.close();
        } catch (FileNotFoundException e) { 
            System.out.println("File Error");
        }
    }
    public static double rounded(double num) {
        return Math.round(num*1000)/1000.0;
    }
}