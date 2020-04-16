public class Util {
    public static double rounded(double num) {
        return Math.round(num*1000)/1000.0;
    }
    public static int numSpaces(String boardStr) {
        int spaces = 0;
        for (int i = 0; i < boardStr.length(); i++) {
            if (boardStr.substring(i,i+1).equals(" ")) {
                spaces++;
            }
        }
        return spaces;
    }
}