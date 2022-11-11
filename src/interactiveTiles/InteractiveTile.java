package interactiveTiles;

import entity.Entity;
import main.GamePanel;

public class InteractiveTile extends Entity {

    public boolean desctructible = false;

    public InteractiveTile(GamePanel gp, int col, int row) {
        super(gp);
    }

    public void playSE(){

    }

    public InteractiveTile getDestroyedForm(){
        InteractiveTile tile = null;
        return tile;
    }

    public void update(){
        if(invincible){ //tree's invicibles
            invincibleCounter++;
            if(invincibleCounter > 20){
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    public boolean isCorrectItem (Entity entity){
        boolean isCorrectItem = false;
        return isCorrectItem;
    }
}
