package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class StartScreen {
    static Texture startScreen;
    static Texture Playbutton;
    static Texture start;
    static SpriteBatch batch;
    static Texture ssbackground;
    static Texture ssalien;
    static Texture bottom;
    static Texture top;
    static int width;
    static int height;
    static boolean atmenu;

    public static void create() {
        start = new Texture("start.png");
        startScreen = new Texture("startScreen.png");
        Playbutton = new Texture("play_button.png");
        bottom = new Texture("ssbottom.png");
        ssbackground = new Texture("skyline.png");
        ssalien = new Texture("ssalien.png");
        top = new Texture("topback.png");
        height = Gdx.graphics.getHeight() / 2;
        width = Gdx.graphics.getWidth() / 2;
        atmenu = true;
        batch = new SpriteBatch();
    }

    public static void updategame(){
        if (Gdx.input.isTouched()) {
            float X = Gdx.input.getX();
            float Y = Gdx.input.getY();
            System.out.println(X + " " + Y);

            for (int i = 0; i < 4; i++){
                if (Gdx.input.isTouched(i)) {
                    System.out.println("HIT ME");
                    atmenu = false;
                }
            }
        }
    }
    public static void render() {
        batch.begin();
        batch.draw(bottom, 0, 0);
        batch.draw(ssbackground, 0, 150);
        batch.draw(startScreen, width / 2 , 125);
        batch.draw(start, 275, 50);
        //batch.draw(ssalien, 400, 100);
        batch.end();
    }
}
