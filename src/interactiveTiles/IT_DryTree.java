package interactiveTiles;

import entity.Entity;
import main.GamePanel;

public class IT_DryTree extends InteractiveTile{
    public IT_DryTree(GamePanel gp, int col, int row) {
        super(gp, col, row);

        this.worldX = gp.tileSize * col;
        this.worldY = gp.tileSize * row;

        down1 = setup("/tiles_interactive/drytree", gp.tileSize, gp.tileSize);
        desctructible = true;

    }

    public boolean isCorrectItem (Entity entity){
        boolean isCorrectItem = false;

        if(entity.currentWeapon.type == type_axe){
            isCorrectItem = true;
        }

        return isCorrectItem;
    }
}
