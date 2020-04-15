public class MoveVal {
    public String move = "";
    public double val = 0;
    public MoveVal (String optMove, double optVal) {
        move = optMove;
        val = optVal;
    }
    //1,3->1,4 1.2
    public String toString() {
        return move + " " + rounded(val);
    }
    public static double rounded(double num) {
        return Math.round(num*1000)/1000.0;
    }
}