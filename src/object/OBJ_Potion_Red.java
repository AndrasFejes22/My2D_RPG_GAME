package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Red extends Entity {

    int healingValue = 5;

    public OBJ_Potion_Red(GamePanel gp) {
        super(gp);

        name = "Red Potion";
        down1 = setup("/objects/potion_red", gp.tileSize, gp.tileSize);
        description = "[" + name + "] \nHeals your life by " + healingValue + ".";
        type = type_consumable;
    }

    public void useRedPotion (Entity entity){
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You have drank the " + name + "!\n"
            + "Your life has been recovered by " + healingValue + "!";

        entity.life += healingValue;
        if(gp.player.life > gp.player.maxLife){
            gp.player.life = gp.player.maxLife;
        }
        gp.playSoundEffect(2);
    }
}
