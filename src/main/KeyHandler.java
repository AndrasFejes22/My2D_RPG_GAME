package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;

    public boolean upPressed;
    public boolean downPressed;
    public boolean leftPressed;
    public boolean rightPressed;
    public boolean enterPressed;
    public boolean shotKeyPressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    // debug-time
    public boolean checkDrawTime = false;
    public boolean playMusic = true;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) { // lehet refaktoralni: How to Make a 2D Game in Java #25, 33:34-nél

        int code = e.getKeyCode();

        //title state
        if(gp.gameState == gp.titleState){

            if(gp.ui.titleScreenState == 0){
                if(code == KeyEvent.VK_W){
                    gp.ui.commandNum--; //az ui-ban átállítja, hogy hova rajzoljon
                    if(gp.ui.commandNum < 0){
                        gp.ui.commandNum = 2;
                    }
                }

                if(code == KeyEvent.VK_S){
                    gp.ui.commandNum++;
                    if(gp.ui.commandNum > 2){
                        gp.ui.commandNum = 0;
                    }
                }
                if(code == KeyEvent.VK_ENTER){
                    if(gp.ui.commandNum == 0){ //new game
                        gp.gameState = gp.playState; ////////////////// ITT VAN A VÁLTOZÁS, eredetileg itt a gp.ui.titleScreenState = 1; volt!!!!!!!!!!!!!!!!!!
                        //gp.ui.titleScreenState = 1;
                        gp.playMusic(0);
                    }
                    if(gp.ui.commandNum == 1){ //load game
                        // add later
                    }
                    if(gp.ui.commandNum == 2){ //quit game
                        System.exit(0);
                    }
                }
            }
            else if(gp.ui.titleScreenState == 1){ //sub title screen //else!

                if(code == KeyEvent.VK_W){
                    gp.ui.commandNum--; //az ui-ban átállítja, hogy hova rajzoljon
                    if(gp.ui.commandNum < 0){
                        gp.ui.commandNum = 3;
                    }
                }

                if(code == KeyEvent.VK_S){
                    gp.ui.commandNum++;
                    if(gp.ui.commandNum > 3){
                        gp.ui.commandNum = 0;
                    }
                }
                if(code == KeyEvent.VK_ENTER){
                    if(gp.ui.commandNum == 0){ //fighter
                        gp.gameState = gp.playState;
                        //gp.playMusic(0);
                    }
                    if(gp.ui.commandNum == 1){ //thief
                        gp.gameState = gp.playState;
                        //gp.playMusic(0);
                    }
                    if(gp.ui.commandNum == 2){ //sorcerer
                        gp.gameState = gp.playState;
                        //gp.playMusic(0);
                    }
                    if(gp.ui.commandNum == 3){ //back
                        gp.ui.titleScreenState = 0;
                    }

                }
            }


        }


        //play state
        else if(gp.gameState == gp.playState){
            if(code == KeyEvent.VK_W){
                upPressed = true;
            }

            if(code == KeyEvent.VK_S){
                downPressed = true;
            }

            if(code == KeyEvent.VK_A){
                leftPressed = true;
            }

            if(code == KeyEvent.VK_D){
                rightPressed = true;
            }

            if(code == KeyEvent.VK_P){
                gp.gameState = gp.pauseState;
            }

            if(code == KeyEvent.VK_C){
                gp.gameState = gp.characterState;
            }

            if(code == KeyEvent.VK_ENTER){
                enterPressed = true;
            }
            //projectile:
            if(code == KeyEvent.VK_F){
                shotKeyPressed = true;
            }
            //game options:
            if(code == KeyEvent.VK_ESCAPE){
                gp.gameState = gp.optionsState;
            }


            //time-debug. KELL kirajzolás megmérése!
            if(code == KeyEvent.VK_T){
                if(!checkDrawTime){
                    checkDrawTime = true;
                } else if(checkDrawTime){
                    checkDrawTime = false;
                }
            }
            if(code == KeyEvent.VK_R){//maps
                switch (gp.currentMap){
                    case 0: gp.tileM.loadMap("/maps/worldV3.txt", 0); break;
                    case 1: gp.tileM.loadMap("/maps/interior01.txt", 1); break;
                }

            }

        }

        //pause
        else if(gp.gameState == gp.pauseState){
            if(code == KeyEvent.VK_P){
                gp.gameState = gp.playState;
                //gp.stopMusic();
            }
        }
        //dialog:
        else if (gp.gameState == gp.dialogueState){
            if(code == KeyEvent.VK_ENTER){
                gp.gameState = gp.playState;
            }
        }
        // character state:
        else if (gp.gameState == gp.characterState){ //visszaváltás játékra
            if(code == KeyEvent.VK_C){
                gp.gameState = gp.playState;
            }

            if(code == KeyEvent.VK_ENTER){
                gp.player.selectItem();
            }
            // WASD navigating in inventory screen**
            playerInventory(code);
            //** lehet egy method
        }
        // game over:
        else if(gp.gameState == gp.gameOverState){
            if(code == KeyEvent.VK_W){
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0){
                    gp.ui.commandNum = 1;
                }
                gp.playSoundEffect(9);
            }
            if(code == KeyEvent.VK_S){
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 1){
                    gp.ui.commandNum = 0;
                }
                gp.playSoundEffect(9);
            }
            if(code == KeyEvent.VK_ENTER){
                if(gp.ui.commandNum == 0){
                    gp.gameState = gp.playState;
                    gp.retry();
                    gp.playMusic(0);
                } else if (gp.ui.commandNum == 1){
                    gp.gameState = gp.titleState;
                    gp.restart();
                }
            }
        }
        // game options:
        else if (gp.gameState == gp.optionsState){
            if(code == KeyEvent.VK_ESCAPE){
                gp.gameState = gp.playState;
            }
            if(code == KeyEvent.VK_ENTER){
                enterPressed = true;
            }
            // W-S navigation in options menu:
            int maxCommandNum = 0;
            switch (gp.ui.subState){
                case 0: maxCommandNum = 5; break;
                case 3: maxCommandNum = 1; break; //endgame yes/no section
            }
            if(code == KeyEvent.VK_W){
                gp.ui.commandNum--;
                gp.playSoundEffect(9);
                if(gp.ui.commandNum < 0){
                    gp.ui.commandNum = maxCommandNum;
                }
            }
            if(code == KeyEvent.VK_S){
                gp.ui.commandNum++;
                gp.playSoundEffect(9);
                if(gp.ui.commandNum > maxCommandNum){
                    gp.ui.commandNum = 0;
                }
            }
            // MUSIC AND SOUNDEFFECT volume
            if(code == KeyEvent.VK_A){
                if(gp.ui.subState == 0){
                   if(gp.ui.commandNum == 1 && gp.music.volumeScale > 0){
                       gp.music.volumeScale--;
                       gp.music.checkVolume();
                       gp.playSoundEffect(9);
                   }
                   //se:
                    if(gp.ui.commandNum == 2 && gp.soundEffect.volumeScale > 0){
                        gp.soundEffect.volumeScale--;
                        gp.playSoundEffect(9);
                    }

                }
            }
            if(code == KeyEvent.VK_D){
                if(gp.ui.subState == 0){
                    if(gp.ui.commandNum == 1 && gp.music.volumeScale < 5){
                        gp.music.volumeScale++;
                        gp.music.checkVolume();
                        gp.playSoundEffect(9);
                    }
                    //se:
                    if(gp.ui.commandNum == 2 && gp.soundEffect.volumeScale < 5){
                        gp.soundEffect.volumeScale++;
                        gp.playSoundEffect(9);
                    }
                }
            }
        }
        // trade:
        else if (gp.gameState == gp.tradeState){
            if(code == KeyEvent.VK_ENTER){
                enterPressed = true;
            }
            if(gp.ui.subState == 0){
                // lehetne metódus**
                if(code == KeyEvent.VK_W) {
                    gp.ui.commandNum--;
                    if(gp.ui.commandNum < 0){
                        gp.ui.commandNum = 2;
                    }
                    gp.playSoundEffect(9);
                }
                if(code == KeyEvent.VK_S) {
                    gp.ui.commandNum++;
                    if(gp.ui.commandNum > 2){
                        gp.ui.commandNum = 0;
                    }
                    gp.playSoundEffect(9);
                }
                //**idáig

            }
            if(gp.ui.subState == 1){//buy
                npcInventory(code);

                if(code == KeyEvent.VK_ESCAPE){
                    gp.ui.subState = 0;
                }
            }
            if(gp.ui.subState == 2){//sell
                playerInventory(code);

                if(code == KeyEvent.VK_ESCAPE){
                    gp.ui.subState = 0;
                }
            }

        }

    }

    public void playerInventory(int code){
        if(code == KeyEvent.VK_W){
            if(gp.ui.playerSlotRow != 0){
                gp.ui.playerSlotRow--;
                gp.playSoundEffect(9);
            }
        }
        if(code == KeyEvent.VK_A){
            if(gp.ui.playerSlotCol != 0) {
                gp.ui.playerSlotCol--;
                gp.playSoundEffect(9);
            }
        }
        if(code == KeyEvent.VK_S){
            if(gp.ui.playerSlotRow != 3) {
                gp.ui.playerSlotRow++;
                gp.playSoundEffect(9);
            }
        }
        if(code == KeyEvent.VK_D){
            if(gp.ui.playerSlotCol != 4) {
                gp.ui.playerSlotCol++;
                gp.playSoundEffect(9);
            }
        }
    }

    public void npcInventory(int code){
        if(code == KeyEvent.VK_W){
            if(gp.ui.npcSlotRow != 0){
                gp.ui.npcSlotRow--;
                gp.playSoundEffect(9);
            }
        }
        if(code == KeyEvent.VK_A){
            if(gp.ui.npcSlotCol != 0) {
                gp.ui.npcSlotCol--;
                gp.playSoundEffect(9);
            }
        }
        if(code == KeyEvent.VK_S){
            if(gp.ui.npcSlotRow != 3) {
                gp.ui.npcSlotRow++;
                gp.playSoundEffect(9);
            }
        }
        if(code == KeyEvent.VK_D){
            if(gp.ui.npcSlotCol != 4) {
                gp.ui.npcSlotCol++;
                gp.playSoundEffect(9);
            }
        }
    }



    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_W){
            upPressed = false;
        }

        if(code == KeyEvent.VK_S){
            downPressed = false;
        }

        if(code == KeyEvent.VK_A){
            leftPressed = false;
        }

        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }

        //projectile:
        if(code == KeyEvent.VK_F){
            shotKeyPressed = false;
        }
    }
}
