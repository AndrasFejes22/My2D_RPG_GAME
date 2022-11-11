package main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {

    Clip clip;
    URL soundUrl[] = new URL[30];

    public Sound (){
        soundUrl[0] = getClass().getResource("/sound/BlueBoyAdventure.wav");
        soundUrl[1] = getClass().getResource("/sound/coin.wav");
        soundUrl[2] = getClass().getResource("/sound/powerup.wav");
        soundUrl[3] = getClass().getResource("/sound/unlock.wav");
        soundUrl[4] = getClass().getResource("/sound/fanfare.wav");
        soundUrl[5] = getClass().getResource("/sound/hitmonster.wav");
        soundUrl[6] = getClass().getResource("/sound/receivedamage.wav");
        soundUrl[7] = getClass().getResource("/sound/swing.wav");
        soundUrl[8] = getClass().getResource("/sound/levelup.wav");
        soundUrl[9] = getClass().getResource("/sound/cursor.wav");
        soundUrl[10] = getClass().getResource("/sound/burning.wav");
        soundUrl[11] = getClass().getResource("/sound/cuttree.wav");
    }

    public void setFile(int i){

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundUrl[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play(){
        clip.start();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(){
        clip.stop();
    }
}
