package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Entity {
    protected GamePanel gp;

    //state
    public int worldX, worldY;
    public boolean collision;
    public boolean invincible = false;
    public int spriteNum = 1;
    public String direction = "down";
    int dialogueIndex = 0;
    public boolean attacking = false;
    public boolean dying = false;
    public boolean alive = true;
    public boolean hpBarOn = false;
    public boolean onPath = false;

    // boy walking:
    public BufferedImage up1;
    public BufferedImage up2;
    public BufferedImage down1;
    public BufferedImage down2;
    public BufferedImage left1;
    public BufferedImage left2;
    public BufferedImage right1;
    public BufferedImage right2;

    //boy attacking:
    public BufferedImage attackUp1;
    public BufferedImage attackUp2;
    public BufferedImage attackDown1;
    public BufferedImage attackDown2;
    public BufferedImage attackLeft1;
    public BufferedImage attackLeft2;
    public BufferedImage attackRight1;
    public BufferedImage attackRight2;

    public BufferedImage image;
    public BufferedImage image2;
    public BufferedImage image3;

    //counters
    public int actionLockCounter = 0;
    public int invincibleCounter = 0;
    public int spriteCounter = 0;
    public int dyingCounter = 0;
    public int hpBarCounter = 0;
    public int shotAvailableCounter = 0;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int solidAreaDefaultX;
    public int solidAreaDefaultY;
    public boolean collisionOn = false;

    String [] dialogues = new String[20];


    // Character status:
    public String name;
    public int maxLife;
    public int life;
    public int maxMana;
    public int mana;
    public int ammo;

    public int speed;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public Entity currentWeapon;
    public Entity currentShield;
    public Projectile projectile;

    // Item attributes
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;
    public int attackValue;
    public int value;
    public int defenseValue;
    public int useCost;
    public int price;
    public String description = "";

    //Type
    public int type; // pl.: 0: player, 1: npc, 2.: monster
    public final int type_player = 0;
    public final int type_npc = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_axe = 4;
    public final int type_shield = 5;
    public final int type_consumable = 6;
    public final int type_pickupOnly = 7;




    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public BufferedImage setup(String imagePath, int width, int height){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try{

            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, width, height);


        } catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }

    // methods to override

    public void setAction(){
        //override in an another class
    }

    public void damageReaction(){
        //override in an another class
    }

    public void use (Entity entity){
        //override in an another class
    }

    public void checkDrop(){
        //override in an another class
    }

    public Color getParticleColor(){
        Color color = null;
        return color;
    }

    public int getParticleSize(){
        return 0; // 6 pixels
    }

    public int getParticleSpeed(){
        return 0;
    }

    public int getParticleMaxLife(){
        return 0;
    }

    /////////////override methods end//////////////

    public void generateParticle(Entity generator, Entity target){
        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLife = generator.getParticleMaxLife();

        Particle p1 = new Particle(gp, target, color, size, speed, maxLife, -2, -1); //top, left, 2: increasing the horizontal vectors
        Particle p2 = new Particle(gp, target, color, size, speed, maxLife, 2, -1);
        Particle p3 = new Particle(gp, target, color, size, speed, maxLife, -2, 1);
        Particle p4 = new Particle(gp, target, color, size, speed, maxLife, 2, 1);
        gp.projectileList.add(p1);
        gp.projectileList.add(p2);
        gp.projectileList.add(p3);
        gp.projectileList.add(p4);
    }

    public void dropItem(Entity droppedItem){
        for (int i = 0; i < gp.obj[1].length; i++) {
            if(gp.obj[gp.currentMap][i] == null){//??
                gp.obj[gp.currentMap][i] = droppedItem;
                gp.obj[gp.currentMap][i].worldX = worldX; // dead monster's coordinates
                gp.obj[gp.currentMap][i].worldY = worldY;
                break;
            }
        }
    }

    public void speak(){
        if(dialogues[dialogueIndex] == null){
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        //turning npc direction to player when dialogue
        switch (gp.player.direction){
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }
    }

    public void checkCollision(){
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        gp.cChecker.checkEntity(this, gp.iTile);
        boolean contactPlayer =  gp.cChecker.checkPlayer(this);

        //get damage from monster:

        if(this.type == type_monster && contactPlayer){
            damagePlayer(attack);
        }
    }

    public void update(){
        setAction();

        //collision check:
        checkCollision();

        //if collision is false, Entity can move
        if(collisionOn == false){
            switch (direction){
                case "up":
                    worldY = worldY - speed;
                    break;
                case "down":
                    worldY = worldY + speed;
                    break;
                case "left":
                    worldX = worldX - speed;
                    break;
                case "right":
                    worldX = worldX + speed;
                    break;
            }
        }

        spriteCounter++;
        if (spriteCounter > 12) { // moving speed (sprite)
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

        if(invincible){ //monster's invicibles
            invincibleCounter++;
            if(invincibleCounter > 40){
                invincible = false;
                invincibleCounter = 0;
            }
        }
        if(shotAvailableCounter < 30){ //met??dus helye?
            shotAvailableCounter ++;
        }

    }

    public void damagePlayer(int attack){
        if(!gp.player.invincible){
            // player can get damage:
            gp.playSoundEffect(6);

            int damage = attack - gp.player.defense;
            if(damage < 0){
                damage = 1;
            }
            gp.player.life =- damage;
            gp.player.invincible = true;
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        //if statement : only drawing the visible tiles, not the whole 50x50 world
        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize  > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            switch (direction){
                case "up":
                    if(spriteNum == 1){
                        image = up1;
                    }
                    if(spriteNum == 2){
                        image = up2;
                    }
                    break;
                case "down":
                    if(spriteNum == 1){
                        image = down1;
                    }
                    if(spriteNum == 2){
                        image = down2;
                    }
                    break;
                case "left":
                    if(spriteNum == 1){
                        image = left1;
                    }
                    if(spriteNum == 2){
                        image = left2;
                    }
                    break;
                case "right":
                    if(spriteNum == 1){
                        image = right1;
                    }
                    if(spriteNum == 2){
                        image = right2;
                    }
                    break;
            }

            // Monster HP bar:
            if(type == 2 && hpBarOn){ //2: monster

                double oneScale = (double)gp.tileSize/maxLife;
                double hpBarValue = oneScale*life;

                g2.setColor(Color.BLACK);
                g2.fillRect(screenX-1, screenY-16, gp.tileSize, 12);
                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY-15, (int) hpBarValue, 10);
                hpBarCounter++;

                if(hpBarCounter > 600){ //ten seconds
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }



            if(invincible){
                hpBarOn = true;
                hpBarCounter = 0;
                // set monster opacity
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            }
            //dying: (monster)
            if(dying){
                dyingAnimation(g2);
            }

            g2.drawImage(image, screenX, screenY, null);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }

    private void dyingAnimation(Graphics2D g2) {

        dyingCounter++;

        int i = 5;

        if(dyingCounter <= 5){
            changeAlpha(g2, 0f);
        }
        if(dyingCounter > i && dyingCounter <= i*2){
            changeAlpha(g2, 1f);
        }
        if(dyingCounter > i*2 && dyingCounter <= i*3){
            changeAlpha(g2, 0f);
        }
        if(dyingCounter > i*3 && dyingCounter <= i*4){
            changeAlpha(g2, 1f);
        }
        if(dyingCounter > i*4 && dyingCounter <= i*5){
            changeAlpha(g2, 0f);
        }
        if(dyingCounter > i*5 && dyingCounter <= i*6){
            changeAlpha(g2, 1f);
        }
        if(dyingCounter > i*6 && dyingCounter <= i*7){
            changeAlpha(g2, 0f);
        }
        if(dyingCounter > i*7 && dyingCounter <= i*8){
            changeAlpha(g2, 1f);
        }
        if(dyingCounter > i*8){
            alive = false;
        }

    }

    public void changeAlpha(Graphics2D g2, float alphaValue){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));

    }

    /////////////////////////// SEARCH ////////////////////////////

    public void searchPath(int goalCol, int goalRow){

        int startCol =(worldX +solidArea.x)/gp.tileSize;
        int startRow =(worldY +solidArea.y)/gp.tileSize;

        // call setNodes()
        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow);

        // call search()
        if(gp.pFinder.search()){ // it found a path

            // next worldX & worldY:
            int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
            int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;

            // Entity's solidArea position:
            int enLeftX = worldX + solidArea.x;
            int enRightX = worldX + solidArea.x + solidArea.width;
            int enTopY = worldY + solidArea.y;
            int enBottomY = worldY + solidArea.y + solidArea.height;

            if(enTopY > nextY && enLeftX >= nextX && enRightX < nextX +gp.tileSize){
                direction = "up";
            } else if(enTopY < nextY && enLeftX >= nextX && enRightX < nextX +gp.tileSize){
                direction = "down";
            } else if(enTopY >= nextY && enBottomY < nextY +gp.tileSize){
                //left or right:
                if(enLeftX > nextX){
                    direction = "left";
                }
                if(enLeftX < nextX){
                    direction = "right";
                }

            } else if(enTopY > nextY && enLeftX > nextX){
                // up or left:
                direction = "up";
                checkCollision();
                if(collisionOn){
                    direction = "left";
                }

            } else if(enTopY > nextY && enLeftX < nextX){
                // up or right:
                direction = "up";
                checkCollision();
                if(collisionOn){
                    direction = "right";
                }

            } else if(enTopY < nextY && enLeftX > nextX){
                // down or left:
                direction = "down";
                checkCollision();
                if(collisionOn){
                    direction = "left";
                }

            } else if(enTopY < nextY && enLeftX < nextX){
                // down or right:
                direction = "down";
                checkCollision();
                if(collisionOn){
                    direction = "right";
                }
            }

            // If reaches te goal, stop the search: **follow state: disable this!
            /*
            int nextCol = gp.pFinder.pathList.get(0).col;
            int nextRow = gp.pFinder.pathList.get(0).row;
            if(nextCol == goalCol && nextRow == goalRow){
                onPath = false;
            }
            */

        }


    }
}
