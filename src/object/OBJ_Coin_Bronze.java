package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Coin_Bronze extends Entity {



    public OBJ_Coin_Bronze(GamePanel gp) {
        super(gp);

        name = "Bronze Coin";
        down1 = setup("/objects/coin_bronze", gp.tileSize, gp.tileSize);
        //description = "[" + name + "] \nIt opens a door.";
        type = type_consumable;
        value = 1;

    }

    public void useCoin (Entity entity){

    }
}
