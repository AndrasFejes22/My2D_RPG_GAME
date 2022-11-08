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
                        gp.ui.titleScreenState = 1;
                        //gp.playMusic(0);
                    }
                    if(gp.ui.commandNum == 1){ //load game
                        // add later
                    }
                    if(gp.ui.commandNum == 2){ //quit game
                        System.exit(0);
                    }
                }
            }
            else if(gp.ui.titleScreenState == 1){ //sub title screen
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


            //time-debug. KELL kirajzolás megmérése!
            if(code == KeyEvent.VK_T){
                if(!checkDrawTime){
                    checkDrawTime = true;
                } else if(checkDrawTime){
                    checkDrawTime = false;
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
            // WASD navigating in inventory screen
            if(code == KeyEvent.VK_W){
                if(gp.ui.slotRow != 0){
                    gp.ui.slotRow--;
                    gp.playSoundEffect(9);
                }
            }
            if(code == KeyEvent.VK_A){
                if(gp.ui.slotCol != 0) {
                    gp.ui.slotCol--;
                    gp.playSoundEffect(9);
                }
            }
            if(code == KeyEvent.VK_S){
                if(gp.ui.slotRow != 3) {
                    gp.ui.slotRow++;
                    gp.playSoundEffect(9);
                }
            }
            if(code == KeyEvent.VK_D){
                if(gp.ui.slotCol != 4) {
                    gp.ui.slotCol++;
                    gp.playSoundEffect(9);
                }
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
    }
}
