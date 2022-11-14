package main;

import entity.Entity;
import entity.Player;
import interactiveTiles.InteractiveTile;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable{

    //screen settings:
    final int originalTileSize = 16; //16x16 tile(size)
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 20; // 16 volt
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels, 20-> 960 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // world settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int maxMap = 10;
    public int currentMap = 0; //V3
    //public int currentMap = 1; //interior


    // for full screen:
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;
    public boolean fullScreenOn = false;

    // FPS

    int FPS = 60;

    //system:

    Thread gameThread;
    public KeyHandler keyH = new KeyHandler(this);

    //entity and object:
    public Player player = new Player(this, keyH);
    public Entity[][] obj = new Entity[maxMap][30];
    public Entity[][] npc = new Entity[maxMap][20];
    public Entity[][] monster = new Entity[maxMap][50];
    public InteractiveTile[][] iTile = new InteractiveTile[maxMap][50];
    public ArrayList<Entity> entityList = new ArrayList<>();
    public ArrayList<Entity> projectileList = new ArrayList<>();
    public ArrayList<Entity> particleList = new ArrayList<>();

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

    // Config

    Config config = new Config(this);

    //EventHandler
    public EventHandler eHandler = new EventHandler(this);

    // game state
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int optionsState = 5;
    public final int gameOverState = 6;




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
        aSetter.setMonster();
        aSetter.setInteractiveTile();
        gameState = titleState;
        //fullscreen prepare:
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();

        // call the method
        // config.txt:
        if(fullScreenOn){
            setFullScreen();
        }
    }

    public void retry(){
        player.setDefaultPositions();
        player.restoreLifeAndMana();
        aSetter.setNPC();
        aSetter.setMonster();
        // restore world
        aSetter.setObjects();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setInteractiveTile();
    }

    public void restart(){
        player.setDefaultPositions();
        player.setDefaultValues();
        player.restoreLifeAndMana();
        player.setItems();

    }

    // set full screen:

    public void setFullScreen(){

        // get local screen device:
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);

        // get full screen width and height
        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();

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
                //repaint();
                drawToTempScreen(); // draw everything to the buffered image
                drawToScreen(); // draw the buffered image to the screen
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
            // player
            player.update();
            //npc:
            for(int i = 0; i < npc[1].length; i++){
                if(npc[currentMap][i] != null){
                    npc[currentMap][i].update();
                }
            }
            // monsters:
            for(int i = 0; i < monster[1].length; i++){
                if(monster[currentMap][i] != null){
                    if(monster[currentMap][i].alive && !monster[currentMap][i].dying){
                        monster[currentMap][i].update();
                    }
                    if(!monster[currentMap][i].alive){
                        monster[currentMap][i].checkDrop();
                        monster[currentMap][i] = null;
                    }

                }
            }
            // projectile:
            for(int i = 0; i < projectileList.size(); i++){
                if(projectileList.get(i) != null){
                    if(projectileList.get(i).alive){
                        projectileList.get(i).update();
                    }
                    if(!projectileList.get(i).alive){
                        projectileList.remove(i);
                    }

                }
            }
            // particle:
            for(int i = 0; i < particleList.size(); i++){
                if(particleList.get(i) != null){
                    if(particleList.get(i).alive){
                        particleList.get(i).update();
                    }
                    if(!particleList.get(i).alive){
                        particleList.remove(i);
                    }

                }
            }

            // iTiles
            for(int i = 0; i < iTile[1].length; i++){//??
                if(iTile[currentMap][i] != null){
                    iTile[currentMap][i].update();
                }

            }

        }
        if(gameState == pauseState){
            //do nothing!
        }

    }

    public void drawToTempScreen(){

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
            //interactive tiles:
            for(int i = 0; i < iTile[1].length; i++){
                if(iTile[currentMap][i] != null){
                    iTile[currentMap][i].draw(g2);
                }

            }

            //Entities
            //player
            entityList.add(player);

            // npc
            for (int i = 0; i < npc[1].length; i++) {
                if(npc[currentMap][i] != null){
                    entityList.add(npc[currentMap][i]);
                }
            }

            //objects:
            for (int i = 0; i < obj[1].length; i++) {
                if(obj[currentMap][i] != null){
                    entityList.add(obj[currentMap][i]);
                }
            }

            //monsters:
            for (int i = 0; i < monster[1].length; i++) {
                if(monster[currentMap][i] != null){
                    entityList.add(monster[currentMap][i]);
                }
            }

            //projectile:
            for (int i = 0; i < projectileList.size(); i++) {
                if(projectileList.get(i) != null){
                    entityList.add(projectileList.get(i));
                }
            }

            //particle:
            for (int i = 0; i < particleList.size(); i++) {
                if(particleList.get(i) != null){
                    entityList.add(particleList.get(i));
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

            entityList.clear(); //ha nem az egészet kell törölni akkor meg listIterator()!

            //UI:
            ui.draw(g2);
        }

        //time-debug end:
        if(keyH.checkDrawTime){
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.WHITE);
            //g2.drawString("Draw time: " + passed, 10, 400);
            g2.drawString("Y: "+String.valueOf(player.worldY/maxWorldRow), 10, 400);
            g2.drawString("X: "+String.valueOf(player.worldX/maxWorldCol), 10, 450);
            System.out.println("Draw time: " + passed);
        }
    }

    public void drawToScreen(){

        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }

    /*
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
            //interactive tiles:
            for(int i = 0; i < iTile.length; i++){
                if(iTile[i] != null){
                    iTile[i].draw(g2);
                }

            }

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

            //monsters:
            for (int i = 0; i < monster.length; i++) {
                if(monster[i] != null){
                    entityList.add(monster[i]);
                }
            }

            //projectile:
            for (int i = 0; i < projectileList.size(); i++) {
                if(projectileList.get(i) != null){
                    entityList.add(projectileList.get(i));
                }
            }

            //particle:
            for (int i = 0; i < particleList.size(); i++) {
                if(particleList.get(i) != null){
                    entityList.add(particleList.get(i));
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

            entityList.clear(); //ha nem az egészet kell törölni akkor meg listIterator()!

            //UI:
            ui.draw(g2);
        }

        //time-debug end:
        if(keyH.checkDrawTime){
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.WHITE);
            //g2.drawString("Draw time: " + passed, 10, 400);
            g2.drawString("Y: "+String.valueOf(player.worldY/maxWorldRow), 10, 400);
            g2.drawString("X: "+String.valueOf(player.worldX/maxWorldCol), 10, 450);
            System.out.println("Draw time: " + passed);
        }


        g2.dispose();
    }
    */

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
