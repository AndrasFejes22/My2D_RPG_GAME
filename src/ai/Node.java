package ai;

import java.awt.*;

public class Node {

    Node parent;
    public int row;
    public int col;

    int gCost;
    int hCost;
    int fCost;

    boolean start;
    boolean goal;
    boolean solid;
    boolean open;
    boolean checked;

    public Node(int col, int row){
        this.row = row;
        this.col = col;
    }

}
