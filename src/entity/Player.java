package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import object.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Entity{


    KeyHandler keyH;
    //player character screen position
    public final int screenX;
    public final int screenY;
    int standCounter = 0;
    public boolean attackCanceled = false;

    //public int hasKey = 0;
    //public int hasBoots = 0;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);

        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);
        //solid area/rectangle:
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        //attack:
        //attackArea.width = 36;
        //attackArea.height = 36;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }

    public void setDefaultValues(){
        //player position V3
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        //player position interior // a map a GamePanelben állitható
        //worldX = gp.tileSize * 11;
        //worldY = gp.tileSize * 11;
        speed = 4;
        direction = "down";

        //player status:
        maxLife = 10;
        life = maxLife;
        maxMana = 4;
        mana = maxMana;
        ammo = 10;
        level = 1;
        strength = 1;
        dexterity = 1;
        exp = 0;
        nextLevelExp = 5;//how much exp yo need to level up
        coin = 700;
        //currentWeapon = new OBJ_Sword_Normal(gp);
        currentWeapon = new OBJ_Axe(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        projectile = new OBJ_Fireball(gp);
        attack = getAttack();
        defense = getDefense();

    }

    //reset player's stuff after selected "Retry" (after Game Over)
    public void setDefaultPositions(){
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        direction = "down";
    }

    public void restoreLifeAndMana(){
        life = maxLife;
        mana = maxMana;
        invincible = false;
    }

    public void setItems(){
        // add items, de túllépheti a maxSize-ot!
        inventory.clear(); // delete extra items
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));

    }

    private int getDefense() {
        return defense = dexterity * currentShield.defenseValue;
    }

    private int getAttack() {
        attackArea = currentWeapon.attackArea;
        return attack = strength * currentWeapon.attackValue;
    }

    public void getPlayerImage(){

        up1 = setup("/player/boy_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/player/boy_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/player/boy_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/player/boy_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/player/boy_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/player/boy_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/player/boy_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/player/boy_right_2", gp.tileSize, gp.tileSize);
    }

    public void getPlayerAttackImage(){

        if(currentWeapon.type == type_sword) {
            attackUp1 = setup("/player/boy_attack_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("/player/boy_attack_up_2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setup("/player/boy_attack_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("/player/boy_attack_down_2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setup("/player/boy_attack_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("/player/boy_attack_left_2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setup("/player/boy_attack_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("/player/boy_attack_right_2", gp.tileSize * 2, gp.tileSize);
        }
        if(currentWeapon.type == type_axe) {
            attackUp1 = setup("/player/boy_axe_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("/player/boy_axe_up_2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setup("/player/boy_axe_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("/player/boy_axe_down_2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setup("/player/boy_axe_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("/player/boy_axe_left_2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setup("/player/boy_axe_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("/player/boy_axe_right_2", gp.tileSize * 2, gp.tileSize);
        }
    }



    public void update(){

        if(attacking){
            attacking();
        }

         else if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.enterPressed) {

            if (keyH.upPressed) {
                direction = "up";
                //worldY = worldY - speed;
            } else if (keyH.downPressed) {
                direction = "down";
                //worldY = worldY + speed;
            } else if (keyH.leftPressed) {
                direction = "left";
                //worldX = worldX - speed;
            } else if (keyH.rightPressed) { //keyH.rightPressed == true
                direction = "right";
                //worldX = worldX + speed;
            }

            //check tile collision:
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //check object collision:
            int objectIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objectIndex);

            //check npc collision:
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            //check monster collision:
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            // check interactive tile collision:
            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);

            // check event collision
            gp.eHandler.checkEvent();


            //if collision is false, player can move
            if(!collisionOn && !keyH.enterPressed){
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

            if(gp.keyH.enterPressed && !attackCanceled){
                gp.playSoundEffect(7);
                attacking = true;
                spriteCounter = 0;

            }

            attackCanceled = false;
            gp.keyH.enterPressed = false;

            spriteCounter++;
            if (spriteCounter > 12) { // moving speed (sprite)
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        } else {
             standCounter++;
             if(standCounter == 20){
                 spriteNum = 1;
                 standCounter = 0;
             }
        }

        // Shot:
        if (keyH.shotKeyPressed && !projectile.alive && shotAvailableCounter == 30 && projectile.haveResource(this)) { //alive when a fireball flies
            //set coordinates, direction, and user
            projectile.set(worldX, worldY, direction, true, this);
            // subtract the cost:
            projectile.subtractResource(this);
            // add to the list
            gp.projectileList.add(projectile);
            shotAvailableCounter = 0;
            //soundEffect:
            gp.playSoundEffect(10);
        }

        // timer
        if(shotAvailableCounter < 30){ //metódus helye? (videóban a if(invincible){} alatt van...)
            shotAvailableCounter ++;
        }

        if(life >maxLife){
            life = maxLife;
            gp.ui.addMessage("Your life is full!");
        }
        if(mana >maxMana){
            mana = maxMana;
            gp.ui.addMessage("Your mana is full!");
        }
        //game over:
        if(life <= 0){
            gp.gameState = gp.gameOverState;
            gp.ui.commandNum = -1;
            gp.stopMusic();
            gp.playSoundEffect(12);
        }


        if(invincible){
            invincibleCounter++;
            if(invincibleCounter > 60){
                invincible = false;
                invincibleCounter = 0;
            }
        }

    }

    private void attacking() {
        spriteCounter ++;
        if(spriteCounter <= 5){
            spriteNum = 1;
        }
        if(spriteCounter > 5 && spriteCounter <= 25){
            spriteNum = 2;
            // save the current x, y, solidarea
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // adjust player's x, y for the attackArea:

            switch (direction){
                case "up": worldY -= attackArea.height; break;
                case "down": worldY += attackArea.height; break;
                case "left": worldX -= attackArea.width; break;
                case "right": worldX += attackArea.width; break;
            }

            // attackArea becomes solidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            //check monster collision wit the updated x, y, area:

            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex, attack); //melee damage

            // check iTile collision:
            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
            damageInteractiveTile(iTileIndex); 

            //reset:
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;

        }
        if(spriteCounter > 25){
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }




    private void contactMonster(int monsterIndex) {
        if(monsterIndex != 999){
            if(!invincible && !gp.monster[gp.currentMap][monsterIndex].dying){
                gp.playSoundEffect(6);
                int damage = gp.monster[gp.currentMap][monsterIndex].attack - defense;
                if(damage < 0){
                    damage = 1;
                }
                life = life - damage;
                invincible = true;
            }

        }
    }

    void damageMonster(int monsterIndex, int attack) {
        if(monsterIndex != 999){
            if(!gp.monster[gp.currentMap][monsterIndex].invincible){
                gp.playSoundEffect(5);

                int damage = attack - gp.monster[gp.currentMap][monsterIndex].defense;
                if(damage < 0){
                    damage = 1;
                }
                gp.monster[gp.currentMap][monsterIndex].life -= damage;
                gp.ui.addMessage(damage + " damage!");
                gp.monster[gp.currentMap][monsterIndex].invincible = true;
                gp.monster[gp.currentMap][monsterIndex].damageReaction();

                if(gp.monster[gp.currentMap][monsterIndex].life <= 0){
                    // monster dies:
                    gp.monster[gp.currentMap][monsterIndex].dying = true;
                    gp.ui.addMessage("Killed the " + gp.monster[gp.currentMap][monsterIndex].name+ " !");
                    gp.ui.addMessage("+ " + gp.monster[gp.currentMap][monsterIndex].exp + " XP");
                    exp += gp.monster[gp.currentMap][monsterIndex].exp;
                    checkLevelUp();
                }
            }
        }

    }

    private void damageInteractiveTile(int iTileIndex) {
        if(iTileIndex != 999 && gp.iTile[gp.currentMap][iTileIndex].desctructible && gp.iTile[gp.currentMap][iTileIndex].isCorrectItem(this) && !gp.iTile[gp.currentMap][iTileIndex].invincible){
            gp.iTile[gp.currentMap][iTileIndex].playSE(); // cuttree sound
            gp.iTile[gp.currentMap][iTileIndex].life--; // (cuttable tree has 3 life!)
            gp.iTile[gp.currentMap][iTileIndex].invincible = true;

            // create particles:

            generateParticle(gp.iTile[gp.currentMap][iTileIndex], gp.iTile[gp.currentMap][iTileIndex]); //??

            if(gp.iTile[gp.currentMap][iTileIndex].life == 0){
                gp.iTile[gp.currentMap][iTileIndex] = gp.iTile[gp.currentMap][iTileIndex].getDestroyedForm(); // it will be a trunk when destroyed
            }

        }
    }

    private void checkLevelUp() {
        if(exp >= nextLevelExp){
            level++;
            nextLevelExp = nextLevelExp * 2;
            maxLife += 2; // 2 = 1 heart!
            strength ++;
            dexterity ++;
            // recalculate:
            attack = getAttack();
            defense = getDefense();
            //sound:
            gp.playSoundEffect(8);
            //textWindow:
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "LEVEL UP!\n" + "Your level is: " + level + "\n"
                    + "You feel stronger!";

        }
    }

    public void selectItem(){
        int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);

        if(itemIndex < inventory.size()){
            Entity selectedItem = inventory.get(itemIndex);
            if(selectedItem.type == type_sword || selectedItem.type == type_axe){
                currentWeapon = selectedItem;
                attack = getAttack();
                getPlayerAttackImage(); // weapon update
            }
            if(selectedItem.type == type_shield){
                currentShield = selectedItem;
                attack = getDefense();
            }
            if(selectedItem.type == type_consumable){
                selectedItem.use(this);
                inventory.remove(itemIndex);
            }
        }
    }


    private void interactNPC(int npcIndex) {
        if(gp.keyH.enterPressed){

            if(npcIndex != 999){
                attackCanceled = true;
                gp.gameState = gp.dialogueState;
                gp.npc[gp.currentMap][0].speak();
            }

        }

    }

    public void pickUpObject(int index) {
        if (index != 999) {
            // pickup_only items:
            if (gp.obj[gp.currentMap][index].type == type_pickupOnly) {

                gp.obj[gp.currentMap][index].use(this);
                gp.obj[gp.currentMap][index] = null;

            } else {
                // inventory items:
                String text = "";


                if (inventory.size() != maxInventorySize) {
                    inventory.add(gp.obj[gp.currentMap][index]);
                    gp.playSoundEffect(1);
                    text = "Got a " + gp.obj[gp.currentMap][index].name + "!";
                } else {
                    text = "Your inventory is full!";
                }
                gp.ui.addMessage(text);
                gp.obj[gp.currentMap][index] = null;

            }
        }

    }



    public void draw(Graphics2D g2){

        BufferedImage image = null;

        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch (direction){
            case "up":
                if(!attacking){
                    if(spriteNum == 1){
                        image = up1;
                    }
                    if(spriteNum == 2){
                        image = up2;
                    }
                }
                if(attacking){
                    tempScreenY = screenY - gp.tileSize;
                    if(spriteNum == 1){
                        image = attackUp1;
                    }
                    if(spriteNum == 2){
                        image = attackUp2;
                    }
                }
                break;
            case "down":
                if(!attacking){
                    if(spriteNum == 1){
                        image = down1;
                    }
                    if(spriteNum == 2){
                        image = down2;
                    }
                }
                if(attacking){
                    if(spriteNum == 1){
                        image = attackDown1;
                    }
                    if(spriteNum == 2){
                        image = attackDown2;
                    }
                }
                break;
            case "left":
                if(!attacking){
                    if(spriteNum == 1){
                        image = left1;
                    }
                    if(spriteNum == 2){
                        image = left2;
                    }
                }
                if(attacking){
                    tempScreenX = screenX - gp.tileSize;
                    if(spriteNum == 1){
                        image = attackLeft1;
                    }
                    if(spriteNum == 2){
                        image = attackLeft2;
                    }
                }
                break;
            case "right":
                if(!attacking){
                    if(spriteNum == 1){
                        image = right1;
                    }
                    if(spriteNum == 2){
                        image = right2;
                    }
                }
                if(attacking){
                    if(spriteNum == 1){
                        image = attackRight1;
                    }
                    if(spriteNum == 2){
                        image = attackRight2;
                    }
                }
                break;
        }
        if(invincible){
            // set player opacity
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
        g2.drawImage(image, tempScreenX, tempScreenY, null);

        //reset alpha: opacity is null
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));


        // debug invincible:
        //g2.setFont(new Font("Arial", Font.PLAIN, 26));
        //g2.setColor(Color.WHITE);
        //g2.drawString("Invincible = " + invincibleCounter, 10, 400);


    }


}
