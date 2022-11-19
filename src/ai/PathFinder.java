package ai;

import main.GamePanel;

import java.util.ArrayList;
import java.util.spi.AbstractResourceBundleProvider;

public class PathFinder {

    boolean goalReached = false;

    GamePanel gp;

    //NODE
    Node node[][];
    Node startNode, goalNode, currentNode;
    //lists:
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();

    int step = 0;

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes();
    }

    public void instantiateNodes() {
        node = new Node[gp.maxWorldCol][gp.maxWorldRow];

        //place nodes:
        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {

            node[col][row] = new Node(col, row);

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }

        }
    }

    public void resetNodes() {

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {

            // reset open, checked, and solid state
            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }

        }
        // reset other settings:

        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {

        resetNodes();

        // set start Node and goal Node:

        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {

            // set solid Node --> check tiles:
            int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
            if (gp.tileM.tile[tileNum].collision) {
                node[col][row].solid = true;
            }
            //check interactive tiles:
            for (int i = 0; i < gp.iTile[1].length; i++) {
                if (gp.iTile[gp.currentMap][i] != null && gp.iTile[gp.currentMap][i].desctructible) {
                    int itCol = gp.iTile[gp.currentMap][i].worldX / gp.tileSize;
                    int itRow = gp.iTile[gp.currentMap][i].worldY / gp.tileSize;
                    node[itCol][itRow].solid = true;
                }
            }
            // set cost
            getCost(node[col][row]);

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }


        }

    }

    private void getCost(Node node) {

        //G cost:(The distance from the start node):
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        //get hCost (The distance from the goal node):
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        //get F cost : F = G + H:
        node.fCost = node.gCost + node.hCost;
    }

    public boolean search() {

        while (goalReached == false && step < 500) {

            int col = currentNode.col;
            int row = currentNode.row;


            // check the currrent node:
            currentNode.checked = true;
            //checkedList.add(currentNode);
            openList.remove(currentNode);
            //Thread.sleep(1000);

            //open the up node:
            if (row - 1 >= 0) {
                openNode(node[col][row - 1]);
            }
            //open the left node:
            if (col - 1 >= 0) {
                openNode(node[col - 1][row]);
            }
            //open the down node:
            if (row + 1 < gp.maxWorldRow) {
                openNode(node[col][row + 1]);
            }
            //open the up node:
            if (col + 1 < gp.maxWorldCol) {
                openNode(node[col + 1][row]);
            }
            //find the best node:
            int bestNodeIndex = 0;
            int bestNodeFCost = 999;

            for(int i = 0; i < openList.size(); i++) {
                //Check if this node's F cost is better
                if (openList.get(i).fCost < bestNodeFCost) {
                    bestNodeIndex = i;
                    bestNodeFCost = openList.get(i).fCost;
                } else if (openList.get(i).fCost == bestNodeFCost) {
                    //check the G cost
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            // if there is no node in the openList, end the loop:
            if(openList.size() == 0){
                break;
            }

            //after the loop, we get the best node which is our next step:
            currentNode = openList.get(bestNodeIndex);

            if(currentNode == goalNode){
                goalReached = true;
                trackThePath();
            }

            step++;

        }
        return goalReached;
    }

    private void openNode(Node node){
        if(node.open == false && node.checked == false && node.solid ==false){
            //if the node not opened yet, add it to the open list
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }

    //track the path:

    private void trackThePath(){
        Node current = goalNode;
        while (current != startNode){

            pathList.add(0, current); //"Always adding the first slot, so the last added node is the [0]"
            current = current.parent;

        }

    }
}


