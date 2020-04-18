public class MoveVal {
    private String move = "";
    private double val = 0;
    public MoveVal (String optMove, double optVal) {
        move = optMove;
        val = optVal;
    }
    //1,3->1,4 1.2
    public String toString() {
        return move + " " + Util.rounded(val);
    }
    public String getMove() {
        return move;
    }
    public double getVal() {
        return val;
    }
}