package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class OBJ_Fireball extends Projectile {

    //GamePanel gp;

    public OBJ_Fireball(GamePanel gp) {

        super(gp);

        name = "Fireball";
        speed = 5;
        maxLife = 80;
        life = maxLife;
        attack = 2;
        //down1 = setup("/objects/axe", gp.tileSize, gp.tileSize);

        //attackArea.width = 30;
        //attackArea.height = 30;

        //description = "[" + name + "] \nA bit rusty but still can cut\n some trees.";


    }
}
