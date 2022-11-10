package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Coin_Bronze extends Entity {



    public OBJ_Coin_Bronze(GamePanel gp) {
        super(gp);

        name = "Bronze Coin";
        down1 = setup("/objects/coin_bronze", gp.tileSize, gp.tileSize);
        //description = "[" + name + "] \nIt opens a door.";
        type = type_pickupOnly;
        value = 1;

    }
    @Override
    public void use (Entity entity){ //VAGY lehet @Override method is mit public void use (Entity entity){}
        gp.playSoundEffect(1);
        gp.ui.addMessage("Coin +" + value);
        //gp.ui.addMessage("COIN:" + gp.player.coin);
        gp.player.coin += value;
        //System.out.println("COIN:" + gp.player.coin);
    }
}
