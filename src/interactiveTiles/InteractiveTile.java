package interactiveTiles;

import entity.Entity;
import main.GamePanel;

public class InteractiveTile extends Entity {

    public boolean desctructible = false;

    public InteractiveTile(GamePanel gp, int col, int row) {
        super(gp);
    }

    public void update(){

    }

    public boolean isCorrectItem (Entity entity){
        boolean isCorrectItem = false;
        return isCorrectItem;
    }
}
