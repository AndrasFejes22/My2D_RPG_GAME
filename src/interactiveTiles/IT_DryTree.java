package interactiveTiles;

import entity.Entity;
import main.GamePanel;

import java.awt.*;

public class IT_DryTree extends InteractiveTile{
    public IT_DryTree(GamePanel gp, int col, int row) {
        super(gp, col, row);

        this.worldX = gp.tileSize * col;
        this.worldY = gp.tileSize * row;

        down1 = setup("/tiles_interactive/drytree", gp.tileSize, gp.tileSize);
        desctructible = true;
        life = 3;

    }

    public boolean isCorrectItem (Entity entity){
        boolean isCorrectItem = false;

        if(entity.currentWeapon.type == type_axe){
            isCorrectItem = true;
        }

        return isCorrectItem;
    }

    public void playSE(){
        gp.playSoundEffect(11);
    }

    public InteractiveTile getDestroyedForm(){
        InteractiveTile tile = new IT_Trunk(gp, worldX/ gp.tileSize, worldY/gp.tileSize);
        return tile;
    }

    public Color getParticleColor(){
        Color color = new Color(65, 50, 30);
        return color;
    }

    public int getParticleSize(){
        return 6; // 6 pixels
    }

    public int getParticleSpeed(){
        return 1;
    }

    public int getParticleMaxSize(){
        return 20;
    }

}
