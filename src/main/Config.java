package main;

import java.io.*;

public class Config {

    GamePanel gp;

    public Config(GamePanel gp) {
        this.gp = gp;
    }

    public void saveConfig(){

        try {
            //You have to fill in the config file the first time! // e.g.: Full screen off/3/1 (in new lines!)
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

        try {
            BufferedReader br = new BufferedReader(new FileReader("config.txt"));

                String s = br.readLine();
                //Full screen:
                if(s.equals("Full screen on")){
                    gp.fullScreenOn = true;
                }
                if(s.equals("Full screen off")){
                    gp.fullScreenOn = false;
                }

                // music:
                s = br.readLine();
                gp.music.volumeScale = Integer.parseInt(s);

                // soundEffect:
                s = br.readLine();
                gp.soundEffect.volumeScale = Integer.parseInt(s);

                br.close();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
