package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

import java.awt.*;

public class OBJ_Rock extends Projectile {

    //GamePanel gp;

    public OBJ_Rock(GamePanel gp) {

        super(gp);

        name = "Rock";
        speed = 8; // RPG-szerűen változtatható, pl ha valamely tárgy vagy képesség boostolja
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
        up1 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        up2 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        down1 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        left1 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        left2 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        right1 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
    }

    @Override
    public boolean haveResource(Entity user){

        boolean haveResource = false;
        if(user.ammo >= useCost){
            haveResource = true;
        }
        return haveResource;

    }
    @Override
    public void subtractResource(Entity user){
        user.ammo -= useCost;
    }

    // 4 methods to generate particle
    public Color getParticleColor(){
        Color color = new Color(40, 50, 0);
        return color;
    }

    public int getParticleSize(){
        return 10; // 10 pixels
    }

    public int getParticleSpeed(){
        return 1;
    }

    public int getParticleMaxLife(){
        return 20;
    }
}
