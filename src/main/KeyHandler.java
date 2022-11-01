package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;

    public boolean upPressed;
    public boolean downPressed;
    public boolean leftPressed;
    public boolean rightPressed;

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
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
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

        //time-debug
        if(code == KeyEvent.VK_T){
            if(!checkDrawTime){
                checkDrawTime = true;
            } else if(checkDrawTime){
                checkDrawTime = false;
            }
        }

        //pause
        if(code == KeyEvent.VK_P){
            if(gp.gameState == gp.playState){
                gp.gameState =gp.pauseState;
                gp.stopMusic();
            }
            else if(gp.gameState == gp.pauseState){
                gp.gameState = gp.playState;
                gp.playMusic(0);
            }
        }


        /*
        if(code == KeyEvent.VK_O){
            Sound sound = new Sound();
            sound.stop();
        }
        */

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
