
package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class MyGdxGame extends ApplicationAdapter {
    int PLAYER_SPEED = 4;

    Matrix4 IDMATRIX;

    Animation koala;
    Texture Player;
    Texture sky;
    Texture youdead;
    Texture jumping;
    boolean movingForward = true;
    boolean jump;
    boolean win;
    Texture top;

    Texture tileset2, tileset3;
    TiledMapRenderer mapRenderer;
    TiledMap map;
    MapObjects objects, spikes, walls, vines;
    MapLayer GroundObject, SpikesObjects, WallObject, VineObject;
    int mapWidth, mapHeight, mapPixelWidth, mapPixelHeight, tilePixelWidth, tilePixelHeight;

    SpriteBatch batch;
    SpriteBatch absoluteBatch;
    Texture restartButton, exitButton;
    Rectangle restartbuttonbox, exitbuttonbox;

    int jumpCount;
    Sprite background1;
    Sprite background2;
    TextureRegion[] playerRun;
    TextureRegion playerFrame;

    Vector2 playerPosition;
    Vector2 gravity;
    Vector2 playerVelocity;
    Vector2 getX;
    Vector2 getY;
    Vector2 setLocation;
    float health;
    boolean dead;
    //Sound shot;
    Music song;
    boolean atmenu;

    OrthographicCamera cam;
    Rectangle playerBounds;

    Boolean isrunning = false;
    float timer;
    int width;
    int height;
    float dt;
    float stateTime;
    float lastStateTime;
    boolean inAir;
    int background1posx;
    int background2posx;

    MapProperties prop;

    @Override
    public void create() {
        IDMATRIX = new Matrix4();

        //shot = Gdx.audio.newSound(Gdx.files.internal("gunshot.wav"));
        //song = Gdx.audio.newMusic(Gdx.files.internal("song.mp3"));

        atmenu = false;
        batch = new SpriteBatch();
        absoluteBatch = new SpriteBatch();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        gravity = new Vector2();
        inAir = false;
        health = 3;
        dead = false;
        win = false;
        jump = false;
        playerVelocity = new Vector2();
        playerPosition = new Vector2();

        background1 = new Sprite(new Texture("skyline.png"));
        background1.setScale(50f);
        background1posx = 500;
        tileset2 = new Texture("spike2.png");
        map = new TmxMapLoader().load("lyleiscool.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        GroundObject = map.getLayers().get("GroundObject");
        objects = GroundObject.getObjects();


        VineObject = map.getLayers().get("VineObject");
        vines = VineObject.getObjects();

        SpikesObjects = map.getLayers().get("SpikesObject");
        spikes = SpikesObjects.getObjects();

        WallObject = map.getLayers().get("WallObject");
        walls = WallObject.getObjects();

        prop = new MapProperties();
        prop = map.getProperties();

        mapWidth = prop.get("width", Integer.class);
        mapHeight = prop.get("height", Integer.class);
        tilePixelWidth = prop.get("tilewidth", Integer.class);
        tilePixelHeight = prop.get("tileheight", Integer.class);

        int mapPixelWidth = mapWidth * tilePixelWidth;
        int mapPixelHeight = mapHeight * tilePixelHeight;

        getX = new Vector2();
        getY = new Vector2();
        setLocation = new Vector2();
        top = new Texture("ssbottom.png");
        youdead = new Texture("youdead.png");
        jumping = new Texture("koalajump.png");
        sky = new Texture("skyline.png");
        Player = new Texture("koalaidle.png");
        exitButton = new Texture("exitbutton.png");
        restartButton = new Texture("restartbutton.png");

        playerBounds = new Rectangle();
        exitbuttonbox = new Rectangle();
        exitbuttonbox.set(588, 125, exitButton.getWidth(), exitButton.getHeight());
        restartbuttonbox = new Rectangle();
        restartbuttonbox.set(170, 126, restartButton.getWidth(), restartButton.getHeight());
        cam = new OrthographicCamera();
        cam.setToOrtho(false, width, height);

        playerRun = new TextureRegion[4];
        playerRun[0] = new TextureRegion(new Texture("koalarunning5.png"));
        playerRun[1] = new TextureRegion(new Texture("koalarunning6.png"));
        playerRun[2] = new TextureRegion(new Texture("koalarunning3.png"));
        playerRun[3] = new TextureRegion(new Texture("koalarunning4.png"));
        //playerRun[4] = new TextureRegion(new Texture("koalarunning10.png"));
        //playerRun[5] = new TextureRegion(new Texture("koalarunning11.png"));
        //playerRun[6] = new TextureRegion(new Texture("koalarunning12.png"));


        koala = new Animation(.125f, new TextureRegion(playerRun[0]), new TextureRegion(playerRun[1]), new TextureRegion(playerRun[2]), new TextureRegion(playerRun[3]));
        koala.setPlayMode(Animation.PlayMode.LOOP);
        stateTime = 0f;
        dt = 0f;
        resetGame();
        StartScreen.create();
    }

    private void resetGame() {
        PLAYER_SPEED = 5;
        playerPosition.set(300, 1100);
        playerBounds.set(playerPosition.x, playerPosition.y, Player.getWidth(), Player.getHeight());
        playerVelocity.set(0, 0);
        gravity.set(0, -10);
        jumpCount = 0;
        health = 3;
        dead = false;
        win = false;
        jump = false;
    }

    public void updategame() {
        dt = Gdx.graphics.getDeltaTime();

        if (StartScreen.atmenu) {
            StartScreen.updategame();
            resetGame();
        }

        background1posx -= 2;

            /*
         else {
            song.setVolume(.1f);
            song.play();
            song.isLooping();
            */

        isrunning = false;
        float dt = Gdx.app.getGraphics().getDeltaTime();
        timer = timer - dt;

        isrunning = true;
        playerPosition.x = playerPosition.x + PLAYER_SPEED;
        movingForward = false;

        if (dead == true) {
            playerVelocity.x = 0;
        }

        for (int i = 0; i < 4; i++) {
            if (Gdx.input.isTouched(i) && jumpCount < 1 && playerPosition.x > 305) {
                float X = Gdx.input.getX(i);
                float Y = Gdx.input.getY(i);

                jumpCount = jumpCount + 1;
                inAir = true;
                jump = true;
                playerVelocity.y = 400;
                gravity.set(0, -10);
                playerVelocity.add(gravity);
                playerPosition.mulAdd(playerVelocity, dt);
            }
        }

        //checks collision with ground
        gravity.set(0, -10);
        jumpCount = 1;

//        checks PLAYER collision with ground

        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();
            if (Intersector.overlaps(playerBounds, rectangle)) {
                playerVelocity.y = 0;
                //playerPosition.y = rectangle.y + rectangle.getHeight() + 1;
                gravity.set(0, 0);
                jumpCount = 0;
                System.out.println("GRAVITY");
            }
        }

        for (RectangleMapObject rectangleObject : walls.getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();
            if (Intersector.overlaps(playerBounds, rectangle)) {
                dead = true;
            }
        }

        for (RectangleMapObject rectangleObject : vines.getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();
            if (Intersector.overlaps(playerBounds, rectangle)) {
                win = true;
            }
        }

        for (RectangleMapObject rectangleObject : spikes.getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();
            if (Intersector.overlaps(playerBounds, rectangle)) {
                dead = true;
            }
        }

        playerVelocity.add(gravity);
        playerPosition.mulAdd(playerVelocity, dt);
        playerBounds.setX(playerPosition.x);
        playerBounds.setY(playerPosition.y);

        if (playerPosition.y < 700) {
            dead = true;
        }
    }



    public void drawGame() {
        cam.position.set(playerPosition.x + 200, 1100, 0);
        cam.update();

        absoluteBatch.begin();
        absoluteBatch.draw(top, 0, 0);
        absoluteBatch.end();
        batch.begin();
        batch.draw(background1, background1posx - 500, 1000);
        batch.end();

        if (playerPosition.x > background1posx){
            batch.begin();
            batch.draw(background1, background1posx + 2700, 1000);
            batch.end();
        }

        if (playerPosition.x > background1posx){
            batch.begin();
            batch.draw(background1, background1posx + 5400, 1000);
            batch.end();
        }

        if (playerPosition.x > background1posx){
            batch.begin();
            batch.draw(background1, background1posx + 8100, 1000);
            batch.end();
        }

        if (playerPosition.x > background1posx){
            batch.begin();
            batch.draw(background1, background1posx + 10800, 1000);
            batch.end();
        }
        mapRenderer.setView(cam);
        mapRenderer.render();
        batch.setProjectionMatrix(cam.combined);

        if (StartScreen.atmenu == true) {
            batch.begin();
            StartScreen.render();
            batch.end();
        } else {
            batch.begin();
            if (isrunning) {
                playerFrame = koala.getKeyFrame(stateTime);
            } else {
                playerFrame = playerRun[4];
            }

            batch.draw(playerFrame, playerPosition.x, playerPosition.y);
            batch.end();

            if (win == true){
                playerVelocity.x = 0;
                playerVelocity.y = -400;
                isrunning = false;
                movingForward = false;
                PLAYER_SPEED = 0;
                jumpCount = 1;
                background1posx = 0;

                absoluteBatch.begin();
                absoluteBatch.end();
            }


            if (dead == true) {
                playerVelocity.x = 0;
                playerVelocity.y = -400;
                isrunning = false;
                movingForward = false;
                PLAYER_SPEED = 0;
                jumpCount = 1;
                background1posx = 0;


                absoluteBatch.begin();
                absoluteBatch.draw(youdead, 100, 100);
                absoluteBatch.draw(exitButton, 588, 125);
                absoluteBatch.draw(restartButton, 170, 126);
                absoluteBatch.end();

                StartScreen.updategame();

                if (Gdx.input.isTouched()) {
                    long x = Gdx.input.getX();
                    long y = Gdx.graphics.getHeight() - Gdx.input.getY();
                    if (restartbuttonbox.contains(x, y)) {
                        resetGame();
                    }

                    if (exitbuttonbox.contains(x, y)) {
                        StartScreen.atmenu = true;
                        dead = false;
                        System.exit(0);
                    }
                }
            }
        }
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        lastStateTime = stateTime;
        stateTime += Gdx.graphics.getDeltaTime();

        updategame();
        drawGame();

    }
}
