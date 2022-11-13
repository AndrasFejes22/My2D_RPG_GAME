package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Config {

    GamePanel gp;

    public Config(GamePanel gp) {
        this.gp = gp;
    }

    public void saveConfig(){

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"));

            // save Full screen settings:
            if(gp.fullScreenOn){
                bw.write("Full screen on");
            }
            if(!gp.fullScreenOn){
                bw.write("Full screen off");
            }
            bw.newLine();

            // save music volume settings:
            bw.write(String.valueOf(gp.music.volumeScale)); //van write(int c) is...
            bw.newLine();

            // save se volume settings:
            bw.write(String.valueOf(gp.soundEffect.volumeScale));
            bw.newLine();

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig(){

    }
}
