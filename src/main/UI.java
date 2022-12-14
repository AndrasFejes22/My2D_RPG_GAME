package main;

import entity.Entity;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class UI {

    GamePanel gp;
    Graphics2D g2;

    //images:
    BufferedImage heart_full;
    BufferedImage heart_half;
    BufferedImage heart_blank;
    BufferedImage crystal_full;
    BufferedImage crystal_blank;
    BufferedImage coin;

    //Fonts:
    Font arial_40;
    Font arial_80B;
    Font gabriola;
    Font lCallig;
    Font couree;
    Font terminal;
    Font bitmap;
    Font retro;
    Font nintendo;
    //BufferedImage keyImage;
    public boolean messageOn = false;
    public boolean gameFinished = false;
    //public String message = "";
    public String currentDialogue = "";
    //public int messageCounter = 0;
    public int commandNum = 0;
    public int titleScreenState = 0; // 0: the first screen, 1: second screen
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();

    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00");

    //slots:
    //player:
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;
    //npc:
    public int npcSlotCol = 0;
    public int npcSlotRow = 0;

    // game options:
    int subState = 0;

    // counter(s):
    int transitionCounter = 0;

    // npc:
    public Entity npc_merchant; // We can access npc' inventory and other



    public UI(GamePanel gp) {
        this.gp = gp;
        //arial_40 = new Font("Cambria", Font.PLAIN, 40);
        //arial_80B = new Font("Cambria", Font.BOLD, 70);

        // from C:\Windows\Fonts
        try {
            InputStream is = getClass().getResourceAsStream("/font/Gabriola.ttf");
            gabriola = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/font/retro.ttf");
            retro = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/font/PreschoolBits.ttf");
            bitmap = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/font/Nintendo-DS-BIOS.ttf");
            nintendo = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Create HUD object:
        //heart
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
        //mana
        Entity crystal = new OBJ_ManaCrystal(gp);
        Entity bronzeCoin = new OBJ_Coin_Bronze(gp);
        crystal_full = crystal.image;
        crystal_blank = crystal.image2;
        coin = bronzeCoin.down1;

    }

    public void addMessage(String text){
        message.add(text);
        messageCounter.add(0);
    }

    public void draw(Graphics2D g2){

        this.g2 = g2;

        // FONTS
        //g2.setFont(lCallig);
        g2.setFont(retro);
        //g2.setFont(couree);
        g2.setColor(Color.orange);
        //title state
        if(gp.gameState == gp.titleState){
            drawTitleScreen();
        }

        //play state
        if(gp.gameState == gp.playState){
            drawPlayerLife();
            drawMassage();
        }
        //pause state
        if(gp.gameState == gp.pauseState){
            drawPauseScreen();
            drawPlayerLife();
        }
        //dialogue state
        if(gp.gameState == gp.dialogueState){
            drawPlayerLife();
            drawDialogueScreen();

        }
        //character state
        if(gp.gameState == gp.characterState){
            drawCharacterScreen();
            drawInventory(gp.player, true);
        }
        //options state
        if(gp.gameState == gp.optionsState){
            drawOptionsScreen();
        }
        //gameOver state
        if(gp.gameState == gp.gameOverState){
            drawGameOverScreen();
        }
        //Transition between maps state
        if(gp.gameState == gp.transitionState){
            drawTransition();
        }
        //Trade state
        if(gp.gameState == gp.tradeState){
            drawTradeScreen();
        }
    }

    private void drawTradeScreen() {

        switch (subState){
            case 0: trade_select(); break;
            case 1: trade_buy(); break;
            case 2: trade_sell(); break;
        }
        gp.keyH.enterPressed = false; //reset the enter input

    }
    //trade substates:
    public void trade_select(){

        drawDialogueScreen();

        //create a frame:

        int frameX = gp.tileSize*15;
        int frameY = gp.tileSize*4;

        int frameWidth= gp.tileSize * 3;
        int frameHeight= (int) (gp.tileSize * 3.5);

        // draw a window
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // draw text:
        frameX += gp.tileSize;
        frameY += gp.tileSize;
        g2.drawString("Buy", frameX, frameY);
        if(commandNum == 0) {
            g2.drawString(">", frameX - 24, frameY);
            if(gp.keyH.enterPressed){
                subState = 1;
            }
        }
        frameY += gp.tileSize;
        g2.drawString("Sell", frameX, frameY);
        if(commandNum == 1) {
            g2.drawString(">", frameX - 24, frameY);
            if(gp.keyH.enterPressed){
                subState = 2;
            }
        }
        frameY += gp.tileSize;
        g2.drawString("Leave", frameX, frameY);
        if(commandNum == 2) {
            g2.drawString(">", frameX - 24, frameY);
            if(gp.keyH.enterPressed){
                gp.gameState = gp.dialogueState;
                currentDialogue = "Come again Adventurer!";
            }
        }
        frameY += gp.tileSize;


    }
    public void trade_buy(){

        // set text's features again
        g2.setFont(gabriola);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));

        // draw player inventory:
        drawInventory(gp.player, false); //eredeti
        //drawInventory(gp.player, true);

        // draw npc inventory:
        drawInventory(npc_merchant, true);

        //draw offer(hint) window:
        int frameX = gp.tileSize*2;
        int frameY = gp.tileSize*9;
        int frameWidth= gp.tileSize * 6;
        int frameHeight= (int) (gp.tileSize * 2);
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        g2.drawString("[ESC] Back", frameX+24, frameY+60);

        //draw player coin window:
        frameX = gp.tileSize*12;
        frameY = gp.tileSize*9;
        frameWidth= gp.tileSize * 6;
        frameHeight= (int) (gp.tileSize * 2);
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        g2.drawString("Your coin: " + gp.player.coin, frameX+24, frameY+60);

        // draw small price window:
        int itemIndex = getItemIndexOnSlot(npcSlotCol, npcSlotRow);
        if(itemIndex < npc_merchant.inventory.size()) {
            frameX = (int) (gp.tileSize * 5.5);
            frameY = (int) (gp.tileSize * 5.5);
            frameWidth = (int) (gp.tileSize * 2.5);
            frameHeight = gp.tileSize;
            drawSubWindow(frameX, frameY, frameWidth, frameHeight);
            g2.drawImage(coin, frameX+10, frameY+8, 32, 32, null);

            int price = npc_merchant.inventory.get(itemIndex).price;
            String text = "" + price;
            frameX = getXForAlignToRightText(text, gp.tileSize*8 - 20);
            g2.drawString(text, frameX, frameY+34);

            // buy items:
            if(gp.keyH.enterPressed){
                if(npc_merchant.inventory.get(itemIndex).price > gp.player.coin){
                    subState = 0;
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "You nee more coin to buy that\n my dear friend.";
                    drawDialogueScreen();
                }
                else if(gp.player.inventory.size() == gp.player.maxInventorySize){
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "You cannot carry more items!";
                    drawDialogueScreen();
                } else {
                    gp.player.coin -= npc_merchant.inventory.get(itemIndex).price;
                    gp.player.inventory.add(npc_merchant.inventory.get(itemIndex));
                }
            }


        }


    }
    public void trade_sell(){

        // set text's features again
        g2.setFont(gabriola);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));

        // draw player's inventory:
        drawInventory(gp.player, true);

        int frameX ;
        int frameY ;
        int frameWidth;
        int frameHeight;

        //draw offer(hint) window:
        frameX = gp.tileSize*2;
        frameY = gp.tileSize*9;
        frameWidth= gp.tileSize * 6;
        frameHeight= (int) (gp.tileSize * 2);
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        g2.drawString("[ESC] Back", frameX+24, frameY+60);

        //draw player coin window:
        frameX = gp.tileSize*12;
        frameY = gp.tileSize*9;
        frameWidth= gp.tileSize * 6;
        frameHeight= (int) (gp.tileSize * 2);
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        g2.drawString("Your coin: " + gp.player.coin, frameX+24, frameY+60);

        // draw small price window:
        int itemIndex = getItemIndexOnSlot(playerSlotCol, playerSlotRow);
        if(itemIndex < gp.player.inventory.size()) {
            frameX = (int) (gp.tileSize * 15.5);
            frameY = (int) (gp.tileSize * 5.5);
            frameWidth = (int) (gp.tileSize * 2.5);
            frameHeight = gp.tileSize;
            drawSubWindow(frameX, frameY, frameWidth, frameHeight);
            g2.drawImage(coin, frameX+10, frameY+8, 32, 32, null);

            int price = (int) (gp.player.inventory.get(itemIndex).price*0.75); //selling: reduce price
            String text = "" + price;
            frameX = getXForAlignToRightText(text, gp.tileSize*18 - 20);
            g2.drawString(text, frameX, frameY+34);

            // sell items:
            if(gp.keyH.enterPressed){
                //prevent selling current weapon/shield:
                if(gp.player.inventory.get(itemIndex) == gp.player.currentWeapon || gp.player.inventory.get(itemIndex) == gp.player.currentShield){
                    commandNum = 0;
                    subState = 0;
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "You cannot sell an equipped item!";
                    drawDialogueScreen();
                } else {
                    gp.player.inventory.remove(itemIndex);
                    gp.player.coin += price;
                }
            }


        }

    }

    private void drawTransition() {
        // The entire screen is getting darker:
        transitionCounter++;
        g2.setColor(new Color(0, 0, 0, transitionCounter * 5));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        // stop it at some point:
        if(transitionCounter == 50){
            transitionCounter = 0;
            gp.gameState = gp.playState;
            gp.currentMap = gp.eHandler.tempMap;
            gp.player.worldX = gp.tileSize * gp.eHandler.tempCol;
            gp.player.worldY = gp.tileSize * gp.eHandler.tempRow;
            gp.eHandler.previousEventX = gp.player.worldX;
            gp.eHandler.previousEventY = gp.player.worldY;
        }

    }

    private void drawGameOverScreen() {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int messageX;
        int messageY;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 100F));
        String text = "Game Over!";

        // shadow
        g2.setColor(Color.BLACK);
        messageX = getForCenteredText(text);
        messageY = gp.tileSize*4;
        g2.drawString(text, messageX , messageY);
        // text:
        g2.setColor(Color.WHITE);
        g2.drawString(text, messageX-4 , messageY-4);

        // Retry:
        g2.setFont(g2.getFont().deriveFont(35F));
        text = "Retry";
        messageX = getForCenteredText(text);
        messageY += gp.tileSize*4;
        g2.drawString(text, messageX , messageY);
        if(commandNum == 0) {
            g2.drawString(">", messageX - 40, messageY);
        }


        // Back to the title screen:
        text = "Quit";
        messageX = getForCenteredText(text);
        messageY += 70;
        g2.drawString(text, messageX , messageY);
        if(commandNum == 1) {
            g2.drawString(">", messageX - 40, messageY);
        }


    }

    private void drawOptionsScreen() {
        g2.setColor(Color.WHITE);
        g2.setFont(nintendo);
        g2.setFont(g2.getFont().deriveFont(42F));

        //create a frame:

        final int frameX = gp.tileSize*6;
        final int frameY = gp.tileSize;

        final int frameWidth= gp.tileSize * 8;
        final int frameHeight= gp.tileSize * 10;

        // draw a window
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        //substates:
        switch (subState){
            case 0: options_top (frameX, frameY); break;
            case 1: options_fullScreenNotification(frameX, frameY); break;
            case 2: options_control(frameX, frameY); break;
            case 3: options_endGameConfirmation(frameX, frameY); break;
        }

        gp.keyH.enterPressed = false;
    }

    private void options_endGameConfirmation(int frameX, int frameY) {
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize*3;
        currentDialogue = "Quit the game and\nreturn to the \ntitle screen?";

        for(String line : currentDialogue.split("\n")){
            g2.drawString(line, textX , textY);
            textY += 40;
        }

        // Yes:
        String text = "Yes";
        textX = getForCenteredText(text);
        textY += gp.tileSize*2;
        g2.drawString(text, textX, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) {
                subState = 0;
                gp.gameState = gp.titleState;
            }
        }

        // no:
        text = "No";
        textX = getForCenteredText(text);
        textY += gp.tileSize;
        g2.drawString(text, textX, textY);
        if(commandNum == 1) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) {
                subState = 0;
                commandNum = 4;
            }
        }

    }

    public void options_top (int frameX, int frameY){
        int textX;
        int textY;
        //title:
        String text = "Options";
        textX = getForCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        //full screen on/off:
        text = "Full screen";
        textX = frameX + gp.tileSize;
        textY += gp.tileSize*2;
        g2.drawString(text, textX, textY);
        if(commandNum == 0){
            g2.drawString(">" , textX-25, textY);
            if(gp.keyH.enterPressed){
                if(!gp.fullScreenOn){
                    gp.fullScreenOn = true;
                } else if (gp.fullScreenOn){
                    gp.fullScreenOn = false;
                }
                subState = 1;
            }
        }

        // music :
        textY += gp.tileSize;
        g2.drawString("Music", textX, textY);
        if(commandNum == 1){
            g2.drawString(">" , textX-25, textY);
        }

        // Sound effect :
        textY += gp.tileSize;
        g2.drawString("Sound effect", textX, textY);
        if(commandNum == 2){
            g2.drawString(">" , textX-25, textY);
        }

        // Control :
        textY += gp.tileSize;
        g2.drawString("Control", textX, textY);
        if(commandNum == 3){
            g2.drawString(">" , textX-25, textY);
            if(gp.keyH.enterPressed){
                subState = 2;
                commandNum = 0;
            }
        }

        // End Game :
        textY += gp.tileSize;
        g2.drawString("End Game", textX, textY);
        if(commandNum == 4){
            g2.drawString(">" , textX-25, textY);
            if(gp.keyH.enterPressed){
                subState = 3;
                commandNum = 0;
            }
        }

        // Back :
        textY += gp.tileSize*2;
        g2.drawString("Back", textX, textY);
        if(commandNum == 5){
            g2.drawString(">" , textX-25, textY);
            if(gp.keyH.enterPressed){
                gp.gameState = gp.playState;
                commandNum = 0;
            }
        }

        // Full screen check box:
        textX = (int) (frameX + gp.tileSize*4.8);
        textY = frameY + gp.tileSize*2 + 24;
        g2.setStroke(new BasicStroke(3)); // a jel??l??n??gyzet oldalainak vastags??ga
        g2.drawRect(textX, textY, 24, 24);
        if(gp.fullScreenOn){
            g2.fillRect(textX, textY, 24, 24);
        }

        // Music volume slider:
        textY += gp.tileSize;
        g2.drawRect(textX, textY, 120, 24); // 120/5 = 24
        int volumeWidth = 24 * gp.music.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, 24);

        // Sound effect volume slider:
        textY += gp.tileSize;
        g2.drawRect(textX, textY, 120, 24);
        volumeWidth = 24 * gp.soundEffect.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, 24);

        // SAVE CONFIG:
        gp.config.saveConfig();

    }

    public void options_fullScreenNotification(int frameX, int frameY){
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize*3;
        gp.setFullScreen();
        if(!gp.fullScreenOn){
            currentDialogue = "The change will take\nafter restarting \nthe game!";
        } else {
            currentDialogue = "Full screen ON!";
        }

        for(String line : currentDialogue.split("\n")){
            g2.drawString(line, textX , textY);
            textY += 40;
        }

        // back:
        textY = frameY + gp.tileSize*9;
        g2.drawString("Back", textX, textY);
        if(commandNum == 0){
            g2.drawString(">" , textX-25, textY);
            if(gp.keyH.enterPressed){
                subState = 0;
            }
        }
    }

    public void options_control(int frameX, int frameY){
        int textX;
        int textY;
        //title:
        String text = "Control";
        textX = getForCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        textX = frameX + gp.tileSize;
        textY += gp.tileSize;
        //submenu:
        g2.drawString("Move", textX, textY);  textY += gp.tileSize;
        g2.drawString("Confirm/Attack", textX, textY);  textY += gp.tileSize;
        g2.drawString("Shoot/Cast", textX, textY);  textY += gp.tileSize;
        g2.drawString("Character screen", textX, textY);  textY += gp.tileSize;
        g2.drawString("Pause", textX, textY);  textY += gp.tileSize;
        g2.drawString("Options", textX, textY);  textY += gp.tileSize;

        textX = frameX + gp.tileSize*6;
        textY = frameY + gp.tileSize*2;

        g2.drawString("WASD", textX, textY);  textY += gp.tileSize;
        g2.drawString("ENTER", textX, textY);  textY += gp.tileSize;
        g2.drawString("F", textX, textY);  textY += gp.tileSize;
        g2.drawString("C", textX, textY);  textY += gp.tileSize;
        g2.drawString("P", textX, textY);  textY += gp.tileSize;
        g2.drawString("ESC", textX, textY);  textY += gp.tileSize;

        // Back :
        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize*9;
        g2.drawString("Back", textX, textY);
        if(commandNum == 0){
            g2.drawString(">" , textX-25, textY);
            if(gp.keyH.enterPressed){
                subState = 0;
                commandNum = 3;
            }
        }



    }


    private void drawPlayerLife() {

        // DRAW MAX HEARTS
        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;

        while (i < gp.player.maxLife/2){
            g2.drawImage(heart_blank, x, y, null); //legyen full
            i++;
            x += gp.tileSize;
        }

        //reset
        x = gp.tileSize/2;
        y = gp.tileSize/2;
        i = 0;

        //draw current life:

        while (i < gp.player.life){
            g2.drawImage(heart_half, x, y, null);
            i++;
            if(i < gp.player.life){
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }

        // DRAW MAX MANA
        x = (gp.tileSize/2)-5;
        y = (int) (gp.tileSize*1.5);
        i = 0;

        while (i < gp.player.maxMana){
            g2.drawImage(crystal_blank, x, y, null);
            i++;
            x += 35;
        }

        // draw mana
        x = (gp.tileSize/2)-5;
        y = (int) (gp.tileSize*1.5);
        i = 0;

        while (i < gp.player.mana){
            g2.drawImage(crystal_full, x, y, null);
            i++;
            x += 35;
        }

    }

    private void drawMassage() {
        int messageX = gp.tileSize;
        int messageY = gp.tileSize*6;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18F));

        for(int i = 0; i < message.size(); i++){
            if(message.get(i) != null){
                //shadow:
                g2.setColor(Color.BLACK);
                g2.drawString(message.get(i), messageX+2, messageY+2);
                g2.setColor(Color.WHITE);
                g2.drawString(message.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1;
                messageCounter.set(i, counter);
                messageY += 50;

                if(messageCounter.get(i) > 180){
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }

    private void drawTitleScreen() {

        if(titleScreenState == 0){
            // starting background color
            g2.setColor(Color.BLACK);
            // fill the whole screen:
            g2.fillRect(0,0, gp.screenWidth, gp.screenHeight);
            //g2.fillRect(0,0, gp.screenWidth2, gp.screenHeight2);
            //title:
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 52F));
            String text = "Blue Boy Adventure";
            int x = getForCenteredText(text);
            int y = gp.tileSize * 3;

            //shadow:
            g2.setColor(Color.GRAY);
            g2.drawString(text, x+5, y+5);
            // main text
            g2.setColor(Color.WHITE);
            g2.drawString(text, x, y);

            //blue boy image:
            x = gp.screenWidth / 2 - (gp.tileSize*2)/2;
            y += gp.tileSize * 2;
            g2.drawImage(gp.player.down1, x, y, gp.tileSize*2, gp.tileSize*2, null);

            // menu:
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

            text = "NEW GAME";
            x = getForCenteredText(text);
            y += gp.tileSize * 3.5;
            g2.drawString(text, x, y);
            if(commandNum == 0){
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "LOAD GAME";
            x = getForCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if(commandNum == 1){
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "QUIT";
            x = getForCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if(commandNum == 2){
                g2.drawString(">", x-gp.tileSize, y);
            }

        } else if (titleScreenState == 1){
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(22F));
            String text = "Select your class!";
            int x = getForCenteredText(text);
            int y = gp.tileSize * 3;
            g2.drawString(text, x, y);

            text = "Fighter";
            x = getForCenteredText(text);
            y += gp.tileSize * 3;
            g2.drawString(text, x, y);
            if(commandNum == 0){
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "Thief";
            x = getForCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if(commandNum == 1){
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "Sorcerer";
            x = getForCenteredText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if(commandNum == 2){
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "Back";
            x = getForCenteredText(text);
            y += gp.tileSize * 2;
            g2.drawString(text, x, y);
            if(commandNum == 3){
                g2.drawString(">", x-gp.tileSize, y);
            }


        }



    }

    private void drawDialogueScreen() {
        //create a dialogue window
        int x = gp.tileSize * 3;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 6);
        int height = gp.tileSize * 4;
        drawSubWindow (x, y, width, height);

        //text in te dialogue window:
        g2.setFont(gabriola);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));
        x += gp.tileSize;
        y += gp.tileSize;

        for(String line : currentDialogue.split("\n")){
            g2.drawString(line, x, y);
            y += 40;
        }

    }

    private void drawCharacterScreen() {
        //create a frame:

        final int frameX = gp.tileSize*2;
        final int frameY = gp.tileSize;

        final int frameWidth= gp.tileSize * 5;
        final int frameHeight= gp.tileSize * 10;

        // draw a window
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // text
        g2.setColor(Color.WHITE);
        //g2.setFont(g2.getFont().deriveFont(20F));
        g2.setFont(gabriola);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));

        int textX = frameX +20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 32;

        //fields:
        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Life", textX, textY);
        textY += lineHeight;
        g2.drawString("Mana", textX, textY);
        textY += lineHeight;
        g2.drawString("Strength", textX, textY);
        textY += lineHeight;
        g2.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        g2.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2.drawString("Defense", textX, textY);
        textY += lineHeight;
        g2.drawString("XP", textX, textY); //Exp
        textY += lineHeight;
        g2.drawString("Next Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Coins", textX, textY);
        textY += lineHeight + 10;
        g2.drawString("Weapon", textX, textY);
        textY += lineHeight + 15;
        g2.drawString("Shield", textX, textY);
        textY += lineHeight;

        // Values:

        int tailX = (frameX + frameWidth) - 30;
        // Reset textY:
        textY = frameY + gp.tileSize;
        String value;

        value = String.valueOf(gp.player.level);
        textX = getXForAlignToRightText(value, tailX);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = getXForAlignToRightText(value, tailX);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.mana + "/" + gp.player.maxMana);
        textX = getXForAlignToRightText(value, tailX);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.strength);
        textX = getXForAlignToRightText(value, tailX);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.dexterity);
        textX = getXForAlignToRightText(value, tailX);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = getXForAlignToRightText(value, tailX);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.defense);
        textX = getXForAlignToRightText(value, tailX);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.exp);
        textX = getXForAlignToRightText(value, tailX);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXForAlignToRightText(value, tailX);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.coin);
        textX = getXForAlignToRightText(value, tailX);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // draw sword and shield images:

        g2.drawImage(gp.player.currentWeapon.down1, tailX -gp.tileSize + 8, textY-17, null);
        textY += gp.tileSize;
        g2.drawImage(gp.player.currentShield.down1, tailX -gp.tileSize + 8, textY-17, null);

    }

    private void drawInventory(Entity entity, boolean cursor) {

        int frameX = 0;
        int frameY = 0;
        int frameWidth = 0;
        int frameHeight = 0;
        int slotCol = 0;
        int slotRow = 0;

        // frame:
        if(entity == gp.player) {
            frameX = gp.tileSize * 12;
            frameY = gp.tileSize;
            frameWidth = gp.tileSize * 6;
            frameHeight = gp.tileSize * 5;
            slotCol = playerSlotCol;
            slotRow = playerSlotRow;
        } else {
            frameX = gp.tileSize * 2;
            frameY = gp.tileSize;
            frameWidth = gp.tileSize * 6;
            frameHeight = gp.tileSize * 5;
            slotCol = npcSlotCol;
            slotRow = npcSlotRow;
        }


        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // slot:
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gp.tileSize + 3;

        // draw player's items:
        for (int i = 0; i < entity.inventory.size(); i++) {

            // Highlight item
            if(entity.inventory.get(i) == entity.currentWeapon || entity.inventory.get(i) == entity.currentShield){
                g2.setColor(Color.lightGray);
                g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
            }
            g2.drawImage(entity.inventory.get(i).down1, slotX, slotY, null);
            slotX += slotSize;
            if(i == 4 || i == 9 || i == 14){ //oszlopok v??ge
                slotX = slotXStart;
                slotY += slotSize; //oszlopok v??g??n??l sort emel
            }

        }

        // cursor:
        if(cursor) {
            int cursorX = slotXStart + (slotSize * slotCol);
            int cursorY = slotYStart + (slotSize * slotRow);
            int cursorWidth = gp.tileSize;
            int cursorHeight = gp.tileSize;

            // draw cursor:
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

            // description window:
            int dFrameX = frameX;
            int dFrameY = frameY + frameHeight;
            int dFrameWidth = frameWidth;
            int dFrameHeight = gp.tileSize * 3;
            //drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);  //**

            // draw description texts:
            int textX = dFrameX + 20;
            int textY = dFrameY + gp.tileSize;
            g2.setFont(g2.getFont().deriveFont(28F));

            int itemIndex = getItemIndexOnSlot(slotCol, slotRow);

            if (itemIndex < entity.inventory.size()) {

                drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight); //** subwindow only appears when an item is selected

                for (String line : entity.inventory.get(itemIndex).description.split("\n")) {
                    g2.drawString(line, textX, textY);
                    textY += 32;
                }
            }
        }

    }

    public int getItemIndexOnSlot(int slotCol, int slotRow){
        return slotCol + (slotRow * 5);
    }

    public void drawSubWindow (int x, int y, int width, int height){

        Color color = new Color(0,0,0, 200); //rgb black, 200 = opacity
        g2.setColor(color);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        color = new Color(255, 255, 255); //rgb white
        g2.setColor(color);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);


    }

    public void drawPauseScreen(){

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 50F));
        String text = "Game paused";
        int x = getForCenteredText(text);
        int y = gp.screenHeight/2 - (gp.tileSize * 3);
        //int y = gp.screenHeight/2;
        g2.drawString(text, x, y);
    }

    public int getForCenteredText(String text){
        int textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x  = gp.screenWidth/2 - textLength/2;
        return x;
    }

    public int getXForAlignToRightText(String text, int tailX){
        int textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x  = tailX - textLength;
        return x;
    }


}
