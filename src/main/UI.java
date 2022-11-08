package main;

import entity.Entity;
import object.OBJ_Heart;

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

    //Fonts:
    Font arial_40;
    Font arial_80B;
    Font gabriola;
    Font lCallig;
    Font couree;
    Font terminal;
    Font bitmap;
    Font retro;
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
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Create HUD object:
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;


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
        }


        //old, printed the owned keys, boots, etc.
        /*
        if(gameFinished){

            g2.setFont(arial_40);
            g2.setColor(Color.orange);

            String text;
            int textLenght;
            int x;
            int y;

            text = "You found the treasure!";
            textLenght = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.screenWidth/2 - textLenght/2;
            y = gp.screenHeight/2 - (gp.tileSize * 3);
            g2.drawString(text, x, y);

            //time:

            text = "Your time is :" + dFormat.format(playTime) + " !";
            textLenght = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.screenWidth/2 - textLenght/2;
            y = gp.screenHeight/2 + (gp.tileSize * 4);
            g2.drawString(text, x, y);

            //Congratulation:
            g2.setFont(arial_80B);
            g2.setColor(Color.yellow);
            text = "Congratulations!";
            textLenght = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.screenWidth/2 - textLenght/2;
            y = gp.screenHeight/2 + (gp.tileSize * 2);
            g2.drawString(text, x, y);

            gp.gameThread = null;

        } else {
            g2.setFont(arial_40);
            g2.setColor(Color.orange);

            g2.drawImage(keyImage, gp.tileSize / 2, gp.tileSize / 2, gp.tileSize, gp.tileSize, null);
            g2.drawString("x " + gp.player.hasKey, 74, 65);

            // time:

            playTime += (double) 1/60;
            g2.drawString("Time: " + dFormat.format(playTime), gp.tileSize*11, 65);

            //message
            if(messageOn){
                g2.setFont(g2.getFont().deriveFont(30F));
                g2.drawString(message , gp.tileSize/2, gp.tileSize*2);

                messageCounter++;
                if(messageCounter > 150){
                    messageCounter = 0;
                    messageOn = false;
                }
            }


        }
        */



    }



    private void drawPlayerLife() {
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

    }

    private void drawTitleScreen() {

        if(titleScreenState == 0){
            // starting background color
            g2.setColor(Color.BLACK);
            // fill the whole screen:
            g2.fillRect(0,0, gp.screenWidth, gp.screenHeight);
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
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
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

        final int frameX = (int) (gp.tileSize);
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
        textY += lineHeight + 20;
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
