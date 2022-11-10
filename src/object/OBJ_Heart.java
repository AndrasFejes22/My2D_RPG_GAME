package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Heart extends Entity {
    //GamePanel gp;

    public OBJ_Heart(GamePanel gp) {

        super(gp);
        name = "Heart";
        type = type_pickupOnly;
        value = 2;
        down1 = setup("/objects/heart_full", gp.tileSize, gp.tileSize);
        image = setup("/objects/heart_full", gp.tileSize, gp.tileSize);
        image2 = setup("/objects/heart_half", gp.tileSize, gp.tileSize);
        image3 = setup("/objects/heart_blank", gp.tileSize, gp.tileSize);

    }

    @Override
    public void use (Entity entity){ //VAGY lehet @Override method is mit public void use (Entity entity){}
        gp.playSoundEffect(2);
        gp.ui.addMessage("Heart +" + value);

        entity.life += value;

    }
}
