package ai;

import main.GamePanel;

import java.util.ArrayList;

public class PathFinder {

    boolean goalReached = false;

    GamePanel gp;

    //NODE
    Node node [][] ;
    Node startNode, goalNode, currentNode;
    //lists:
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();

    int step = 0;

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes();
    }

    public void instantiateNodes(){
        node = new Node[gp.maxWorldCol][gp.maxWorldRow];
    }




}
