package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_ManaCrystal extends Entity {



    public OBJ_ManaCrystal(GamePanel gp) {
        super(gp);

        name = "Mana Crystal";
        image = setup("/objects/manacrystal_full", gp.tileSize, gp.tileSize);
        image2 = setup("/objects/manacrystal_blank", gp.tileSize, gp.tileSize);
        down1 = setup("/objects/manacrystal_full", gp.tileSize, gp.tileSize);
        type = type_pickupOnly;
        value = 1;

    }

    @Override
    public void use (Entity entity){ //VAGY lehet @Override method is mit public void use (Entity entity){}
        gp.playSoundEffect(2);
        gp.ui.addMessage("Mana +" + value);

        entity.mana += value;

    }
}
