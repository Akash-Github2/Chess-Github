
/**
 * @author Akash Pulinthanathu
 * Chess Game with Advanced AI
 */
import java.io.*;
import java.util.*;
public class ChessGame {
    private static int recur = 0;
    private static int numSavedD3 = 0;
    private static int numSavedD4 = 0;
    private static int numSavedD5 = 0;
    private static int numSavedOverall = 0;
    public static int moveCounter = 0;
    private static TreeMap<String, Integer> boardFreq = new TreeMap<>(); // Checks for 3 fold rule (tie)
    private static TreeMap<String, MoveVal> bestMoveLog = new TreeMap<>(); // Overall
    private static ArrayList<TreeMap<String, MoveVal>> bestMoveLogList = new ArrayList<>(); // 0:D3; 1:D4; 2:D5;...
    private static String folder = "/Users/akash/software/Akash/Java Projects/Chess-Github/";
    private static ArrayList<Double[]> depthValToSkip = new ArrayList<>(); // 2,3,4,5...
    private static DataManager manager = new DataManager(folder);
    public static void main(String args[]) { // Driver
        if (!System.getProperty("os.name").equals("Mac OS X")) {
            folder = "E:/Akash/Java Projects/Chess-Github/";
        }
        Double[] depth2valToSkip = { 2.0, 2.2, 2.4, 2.7, 3.0, 3.4, 4.0, 4.5 };
        Double[] depth3valToSkip = { 2.0, 2.2, 2.4, 2.8, 3.3, 3.8, 4.8, 5.5 };
        Double[] depth4valToSkip = { 2.0, 2.2, 2.4, 2.8, 3.3, 3.8, 4.8, 6.0 };
        Double[] depth5valToSkip = { 2.5, 2.8, 3.0, 3.5, 3.9, 4.5, 5.4, 6.8 };
        Double[] depth6valToSkip = { 3.0, 3.8, 4.4, 5.1, 5.8, 6.3, 7.4, 8.5 };
        depthValToSkip.add(depth2valToSkip);
        depthValToSkip.add(depth3valToSkip);
        depthValToSkip.add(depth4valToSkip);
        depthValToSkip.add(depth5valToSkip);
        depthValToSkip.add(depth6valToSkip);
        // manager.reportNumPieces();
        // manager.convert();
        // manager.fullClean();
        Board chessBoard = new Board();
        playGame(chessBoard, 6, 6);
    }
    public static void playGame(Board board, int whiteDepth, int blackDepth) {
        System.out.print("\033[H\033[2J"); // Clear Console Command
        // manager.clearMoveOutputFile();
        fillBestMoveLog("moveTB-D6.txt", Math.max(whiteDepth, blackDepth) == 6); // true for real deal
        fillBestMoveLog("moveTB-D5.txt", Math.max(whiteDepth, blackDepth) == 5); // true for testing depth 5 only
        fillBestMoveLog("moveTB-D4.txt", Math.max(whiteDepth, blackDepth) == 4); // true for real deal
        fillBestMoveLog("moveTB-D3.txt", Math.max(whiteDepth, blackDepth) == 3); // true for testing depth 3 only
        System.out.println("Welcome to My Chess Game!");
        Scanner br = new Scanner(System.in);
        boolean currPlayerIsWhite = true;
        System.out.println("Initial Game Board:");
        System.out.println(board);
        while (true) {
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
                // parseAndMove("Make your move. (E.g. 6,0->4,0) : Move #" + moveCounter + gamePhase, board, currPlayerIsWhite, br);
                callAI(gamePhase, board, Math.max(whiteDepth, blackDepth) == whiteDepth, whiteDepth, "White");
            } else {
                // parseAndMove("Make your move. (E.g. 6,0->4,0) : Move #" + moveCounter + gamePhase, board, currPlayerIsWhite, br);
                callAI(gamePhase, board, Math.max(whiteDepth, blackDepth) == blackDepth, blackDepth, "Black");
            }
            if (board.isCheckMate(!currPlayerIsWhite)) {
                System.out.println((currPlayerIsWhite) ? "White Wins!" : "Black Wins!");
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
        moveCounter = 0;
        // numSavedOverall = 0;
        bestMoveLogList.clear();
        boardFreq.clear();
        bestMoveLog.clear();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static double findBestMove(Board board, boolean isComputerTurn, int depth, int maxDepth, int tempMoveCounter, boolean isComputerWhite, boolean isAllowedToAccessData) {
        recur++;
        if (depth > 0 && depth < maxDepth - 2 && bestMoveLogList.get(maxDepth-depth-3).get(board.formatBoardForFile(isComputerWhite, depth)) != null && moveCounter > 2 && (boardFreq.get(board.toString()) == null || boardFreq.get(board.toString()) < 2)) {
            if (maxDepth - depth == 3) {
                numSavedD3++;
            } else if (maxDepth - depth == 4) {
                numSavedD4++;
            } else {
                numSavedD5++;
            }
            numSavedOverall++;
            return bestMoveLogList.get(maxDepth-depth-3).get(board.formatBoardForFile(isComputerWhite, depth)).val;
        }
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
        if (!(depth == 0 && (allPossibleMoves.size() == 1 || (bestMoveLog.get(board.formatBoardForFile(isComputerWhite, depth)) != null && (boardFreq.get(board.toString()) == null || boardFreq.get(board.toString()) < 2)  && isAllowedToAccessData)))) {
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
                    if (maxDepth >= 2) {
                        double num = depthValToSkip.get(maxDepth - 2)[0];
                        if (tempMoveCounter > 50) {
                            num = depthValToSkip.get(maxDepth - 2)[7];
                        } else if (tempMoveCounter > 40) {
                            num = depthValToSkip.get(maxDepth - 2)[6];
                        } else if (tempMoveCounter > 35) {
                            num = depthValToSkip.get(maxDepth - 2)[5];
                        } else if (tempMoveCounter > 30) {
                            num = depthValToSkip.get(maxDepth - 2)[4];
                        } else if (tempMoveCounter > 25) {
                            num = depthValToSkip.get(maxDepth - 2)[3];
                        } else if (tempMoveCounter > 18) {
                            num = depthValToSkip.get(maxDepth - 2)[2];
                        } else if (tempMoveCounter > 12) {
                            num = depthValToSkip.get(maxDepth - 2)[1];
                        }
                        boolean shouldReturn = diff + num < optVal;
                        if (!isComputerTurn) {
                            shouldReturn = diff - num > optVal;
                        }
                        if (shouldReturn && tempMoveCounter < 60 && !board.whiteAlmostWins && !board.blackAlmostWins) {
                            //reset it
                            board.fullyResetMove(initI, initJ, finI, finJ, !isWhite, didPawnPromo, origPiece);
                            if (boardFreq.get(triedBoard) == 1) {
                                boardFreq.remove(triedBoard);
                            } else {
                                boardFreq.put(triedBoard, boardFreq.get(triedBoard) - 1);
                            }
                            continue;
                        }
                    }
                    double n = findBestMove(board, !isComputerTurn, depth + 1, maxDepth, tempMoveCounter + 1, isComputerWhite, isAllowedToAccessData) + diff;
                    if (n > 800) {
                        n-=diff;
                    }
                    if (depth == 0) {
                        System.out.println(allPossibleMoves.get(i) + " : " + Util.rounded(n) + "  |  " + (i+1) + "/" + allPossibleMoves.size());
                    }
                    double numOver1 = 0;
                    boolean shouldChange = false;
                    if (isComputerTurn) {
                        numOver1 = (isComputerWhite) ? board.getNumWhiteOver1() : board.getNumBlackOver1();
                        shouldChange = n > optVal || (n == optVal && numOver1 > bestSumOver1);
                    } else {
                        numOver1 = (isComputerWhite) ? board.getNumBlackOver1() : board.getNumWhiteOver1();
                        shouldChange = n < optVal || (n == optVal && numOver1 > bestSumOver1);
                    }
                    if (shouldChange) {
                        optVal = n;
                        optMove = allPossibleMoves.get(i);
                        tiedOptMoves.clear();
                        tiedOptMoves.put(allPossibleMoves.get(i), n);
                        bestSumOver1 = numOver1;
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
                if (maxDepth != 0) {
                    return 0;
                }
            }
        }
        boolean isAlreadyFound = false;
        if (depth == 0 && bestMoveLog.get(board.formatBoardForFile(isComputerWhite, depth)) != null && (boardFreq.get(board.toString()) == null || boardFreq.get(board.toString()) < 2) && isAllowedToAccessData) {
            optMove = bestMoveLog.get(board.formatBoardForFile(isComputerWhite, depth)).move;
            optVal = bestMoveLog.get(board.formatBoardForFile(isComputerWhite, depth)).val;
            isAlreadyFound = true;
        }
        if (maxDepth == 0) { //returns a random move for max depth = 0
            int randInd = (int)(Math.random() * allPossibleMoves.size());
            optMove = allPossibleMoves.get(randInd);
        }
        if (!isAlreadyFound && maxDepth != 0) {
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
        if (depth >= 0 && depth < maxDepth - 2 && (depth == 0 || moveCounter > 2)) {
            String fileName = folder + "moveTB-D" + (maxDepth - depth) + ".txt";
            boolean isAlreadyInIndivFile = true;
            if (bestMoveLogList.get(maxDepth-depth-3).get(board.formatBoardForFile(isComputerWhite, depth)) == null) {
                bestMoveLogList.get(maxDepth-depth-3).put(board.formatBoardForFile(isComputerWhite, depth), new MoveVal(optMove, optVal));
                isAlreadyInIndivFile = false;
            }
            if (!isAlreadyInIndivFile && ((maxDepth - depth != 3) ? true : (Util.numSpaces(board.formatBoardForFile(isComputerWhite, depth)) <= 41))) {
                try{
                    File file = new File(fileName);
                    FileWriter writer = new FileWriter(file, true);
                    if (file.length() != 0) {
                        writer.write("\n");
                    }
                    writer.write(board.formatBoardForFile(isComputerWhite, depth) + ":" + optMove + " " + Util.rounded(optVal));
                    writer.close();
                }catch(Exception e) {
                    System.out.println("FILE ERROR");
                }
            }
        }
        if (depth == 0) {
            if (!isAlreadyFound && maxDepth != 0) {
                bestMoveLog.put(board.formatBoardForFile(isComputerWhite, depth), new MoveVal(optMove, optVal));
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
            System.out.println("Optimal Value: " + ((allPossibleMoves.size() == 1 || maxDepth == 0) ? "N/A" : Util.rounded(optVal)));
            if ((optVal >= 1000 && isComputerWhite) || (optVal < -900 && !isComputerWhite)) {
                board.whiteAlmostWins = true;
            }
            if ((optVal >= 1000 && !isComputerWhite) || (optVal < -900 && isComputerWhite)) {
                board.blackAlmostWins = true;
            }
            System.out.println("White Val: " + Util.rounded(board.getWhiteVal(moveCounter)));
            System.out.println("Black Val: " + Util.rounded(board.getBlackVal(moveCounter)));
            if (board.movesSinceNoCaptureOrPawn != 0) {
                System.out.println("Moves Since No Capture/Pawn Mvmt: " + board.movesSinceNoCaptureOrPawn);
            }
            System.out.println("Size of Overall TB: " + bestMoveLog.size());
            System.out.println("Size of TB D-3: " + bestMoveLogList.get(0).size());
            System.out.println("Size of TB D-4: " + bestMoveLogList.get(1).size());
            if (maxDepth >= 5) {
                System.out.println("Size of TB D-5: " + bestMoveLogList.get(2).size());
                if (bestMoveLogList.get(3).size() > 5) {
                    System.out.println("Size of TB D-6: " + bestMoveLogList.get(3).size());
                }
            }
            System.out.println("Num Saved D3: " + numSavedD3);
            System.out.println("Num Saved D4: " + numSavedD4);
            System.out.println("Num Saved D5: " + numSavedD5);
            System.out.println("Num Saved Overall: " + numSavedOverall);
            // manager.appendToFile(initI, initJ, finI, finJ);
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
        // manager.appendToFile(initI, initJ, finI, finJ);
    }
    public static void callAI(String gamePhase, Board board, boolean isAllowedToAccessData, int fullDepth, String player) {
        System.out.println(player + "'s Turn : Move #" + moveCounter + gamePhase);
        int tempMoveCounter = moveCounter;
        int depth = fullDepth;
        // if (!board.isEarlyGame(moveCounter) && !board.isMidGame(moveCounter) && board.retAllPossibleMoves(currPlayerIsWhite).size() * board.retAllPossibleMoves(!currPlayerIsWhite).size() < 75) {
        //     depth = depth2;
        // }
        findBestMove(board, true, 0, depth, tempMoveCounter, player.equals("White"), isAllowedToAccessData);
        System.out.println("Num Recursions: " + recur);
        System.out.println("------------------------");
        recur = 0;
        numSavedD3 = 0;
        numSavedD4 = 0;
        numSavedD5 = 0;
    }
    public static void fillBestMoveLog(String filename, boolean checkToAdd) {
        TreeMap<String, MoveVal> bestMoveLogIndiv = new TreeMap<>();
        try {
            File myObj = new File(folder + filename);
            Scanner reader = new Scanner(myObj);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String boardStr = line.split(":")[0];
                String nextLine = line.split(":")[1];
                String move = nextLine.split(" ")[0];
                double val = Double.parseDouble(nextLine.split(" ")[1]);
                if (bestMoveLog.get(boardStr) == null && checkToAdd) {
                    bestMoveLog.put(boardStr, new MoveVal(move, val));
                }
                bestMoveLogIndiv.put(boardStr, new MoveVal(move, val));
            }
            bestMoveLogList.add(0, bestMoveLogIndiv);
            reader.close();
        } catch (FileNotFoundException e) { 
            System.out.println("File Error");
        }
    }
}