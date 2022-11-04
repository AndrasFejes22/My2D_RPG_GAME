package main;

import entity.Entity;
import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable{

    //screen settings:
    final int originalTileSize = 16; //16x16 tile(size)
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // world settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWith = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    // FPS

    int FPS = 60;

    //system:

    Thread gameThread;
    public KeyHandler keyH = new KeyHandler(this);

    //entity and object:
    public Player player = new Player(this, keyH);
    public Entity[] obj = new Entity[10];
    public Entity[] npc = new Entity[10];
    ArrayList<Entity> entityList = new ArrayList<>();
    //tiles
    TileManager tileM = new TileManager(this);
    //collision
    public CollisionChecker cChecker = new CollisionChecker(this);
    //assets
    public AssetSetter aSetter = new AssetSetter(this);
    //sound:
    Sound music = new Sound();
    Sound soundEffect = new Sound();
    // UI
    public UI ui = new UI(this);
    //EventHandler
    public EventHandler eHandler = new EventHandler(this);

    // game state
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int titleState = 0;




    public GamePanel(){

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame(){
        aSetter.setObjects();
        aSetter.setNPC();
        //playMusic(0);
        //stopMusic();
        gameState = titleState;
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS; //1 mp osztva FPS = 0.01666666 sec
        double nextDrawTime = System.nanoTime() + drawInterval;
        double delta = 0;
        long lastTime = System.nanoTime();
        long timer = 0;
        long drawCount = 0;

        while (gameThread != null){
            //System.out.println("The game loop is running!");
            long currentTime = System.nanoTime();
            /*
            long currentTime2 = System.currentTimeMillis();
            update();
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000;
                if(remainingTime < 0){
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            */
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1){
                update();
                repaint();
                delta--;
                drawCount ++;
            }

            if (timer >= 1000000000){
                System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update(){
        if(gameState == playState){
            player.update();
            //npc:
            for(int i = 0; i < npc.length; i++){
                if(npc[i] != null){
                    npc[i].update();
                }
            }
        }
        if(gameState == pauseState){
            //do nothing!
        }

    }

    public void paintComponent (Graphics graphics){
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics;

        //time-debug end:
        long drawStart = System.nanoTime();
        if(keyH.checkDrawTime){
            drawStart = System.nanoTime();
        }

        // TITLE SCREEN

        if(gameState == titleState){

            ui.draw(g2);

        } else {
            // Others:
            //TILE
            tileM.draw(g2);

            //Entities
            //player
            entityList.add(player);

            // npc
            for (int i = 0; i < npc.length; i++) {
                if(npc[i] != null){
                    entityList.add(npc[i]);
                }
            }

            //objects:
            for (int i = 0; i < obj.length; i++) {
                if(obj[i] != null){
                    entityList.add(obj[i]);
                }
            }

            // sorting

            Collections.sort(entityList, new Comparator<>() {
                @Override
                public int compare(Entity entity1, Entity entity2) {
                    return Integer.compare(entity1.worldY, entity2.worldY);
                }
            });

            // draw entities

            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }

            //reset the list:
            for (int i = 0; i < entityList.size(); i++) {
                entityList.remove(i);
            }


            //UI:
            ui.draw(g2);
        }



        //time-debug end:
        if(keyH.checkDrawTime){
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.WHITE);
            g2.drawString("Draw time: " + passed, 10, 400);
            System.out.println("Draw time: " + passed);
        }


        g2.dispose();
    }

    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic(){
        music.stop();
    }

    public void playSoundEffect(int i){
        soundEffect.setFile(i);
        soundEffect.play();
    }
}
