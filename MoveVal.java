public class MoveVal {
    private String move = "";
    private double val = 0;
    private Util util = new Util();
    public MoveVal (String optMove, double optVal) {
        move = optMove;
        val = optVal;
    }
    //1,3->1,4 1.2
    public String toString() {
        return move + " " + util.rounded(val);
    }
    public String getMove() {
        return move;
    }
    public double getVal() {
        return val;
    }
}