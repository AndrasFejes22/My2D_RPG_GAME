package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Axe extends Entity {

    GamePanel gp;

    public OBJ_Axe(GamePanel gp) {

        super(gp);

        name = "Woodcutter's Axe";
        price = 50;
        down1 = setup("/objects/axe", gp.tileSize, gp.tileSize);
        attackValue = 2;
        attackArea.width = 30;
        attackArea.height = 30;

        description = "[" + name + "] \nA bit rusty but still can cut\n some trees.";

        type = type_axe;
    }
}
