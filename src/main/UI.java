package main;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font arial_40;
    Font arial_80B;
    Font gabriola;
    Font lCallig;
    Font couree;
    //BufferedImage keyImage;
    public boolean messageOn = false;
    public boolean gameFinished = false;
    public String message = "";
    public String currentDialogue = "";
    public int messageCounter = 0;

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
            is = getClass().getResourceAsStream("/font/LCALLIG.ttf");
            lCallig = Font.createFont(Font.TRUETYPE_FONT, is);
            //is = getClass().getResourceAsStream("/font/couree.fon");
            //couree = Font.createFont(Font.TYPE1_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void showMessage(String text){
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2){

        this.g2 = g2;

        // FONTS
        //g2.setFont(lCallig);
        g2.setFont(gabriola);
        //g2.setFont(couree);
        g2.setColor(Color.orange);
        //play state
        if(gp.gameState == gp.playState){
            //do playstate stuff later
        }
        //pause state
        if(gp.gameState == gp.pauseState){
            drawPauseScreen();
        }
        //dialogue state
        if(gp.gameState == gp.dialogueState){
            drawDialogueScreen();
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

    private void drawDialogueScreen() {
        //create a dialogue window
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;
        drawSubWindow (x, y, width, height);

        //text in te dialogue window:

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));
        x += gp.tileSize;
        y += gp.tileSize;

        for(String line : currentDialogue.split("\n")){
            g2.drawString(line, x, y);
            y += 40;
        }


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

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "Game paused";
        int x = getForCenteredTex(text);
        int y = gp.screenHeight/2 - (gp.tileSize * 3);
        //int y = gp.screenHeight/2;
        g2.drawString(text, x, y);
    }

    public int getForCenteredTex(String text){
        int textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x  = gp.screenWidth/2 - textLength/2;
        return x;
    }

}
