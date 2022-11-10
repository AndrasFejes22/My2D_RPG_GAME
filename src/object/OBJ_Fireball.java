package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class OBJ_Fireball extends Projectile {

    //GamePanel gp;

    public OBJ_Fireball(GamePanel gp) {

        super(gp);

        name = "Fireball";
        speed = 5; // RPG-szerűen változtatható, pl ha valamely tárgy vagy képesség boostolja
        maxLife = 80; // RPG-szerűen változtatható, pl ha valamely tárgy vagy képesség boostolja, hogy mennyi idő után tűnik el
        life = maxLife;
        attack = 2;
        useCost = 1; //use 1 mana to cast spell
        alive = false;
        getImage();


        //attackArea.width = 30;
        //attackArea.height = 30;

        //description = "[" + name + "] \nA bit rusty but still can cut\n some trees.";


    }

    private void getImage() {
        up1 = setup("/projectile/fireball_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/projectile/fireball_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/projectile/fireball_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/projectile/fireball_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/projectile/fireball_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/projectile/fireball_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/projectile/fireball_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/projectile/fireball_right_2", gp.tileSize, gp.tileSize);
    }
    @Override
    public boolean haveResource(Entity user){

        boolean haveResource = false;
        if(user.mana >= useCost){
            haveResource = true;
        }
        return haveResource;

    }
    @Override
    public void subtractResource(Entity user){
        user.mana -= useCost;
    }
}
