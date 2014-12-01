package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;
import java.util.Random;

import edu.uchicago.cs.java.finalproject.sounds.Sound;


/**
 * Created by yiqin on 12/1/14.
 */
public class ExplodingRegularBullet extends Sprite {

    // ==============================================================
    // FIELDS
    // ==============================================================

    private final int MAX_EXPIRE = 15;					// how long the explosion lasts
    private final int MIN_EXPLOSION_RADIUS = 30;		// size of the explosion immediately after the click
    private final int MAX_EXPLOSION_RADIUS = 75;		// maximum size of the explosion before becoming small again

    private static Random R;


    // ==============================================================
    // CONSTRUCTOR
    // ==============================================================

    // Sets up the basic settings
    public ExplodingRegularBullet(Point pnt) {

        super();
        R = new Random();

        setDeltaX(0);
        setDeltaY(0);
        setCenter(pnt);

        setExpire(MAX_EXPIRE);
        setRadius(MIN_EXPLOSION_RADIUS);

        playExplosionSounds();					// randomly plays an explosion sound
    }


    // ==============================================================
    // METHODS
    // ==============================================================

    // Randomly plays one of 4 explosion sounds (each sound is actually a sword sound effect from Link's Awakening, a Zelda game)
    public void playExplosionSounds(){
        int nSound = R.nextInt(4);
        if(nSound == 0){
            Sound.playSound("LA_Sword_Slash1.wav");
        }
        else if(nSound == 1){
            Sound.playSound("LA_Sword_Slash2.wav");
        }
        else if(nSound == 2){
            Sound.playSound("LA_Sword_Slash3.wav");
        }
        else if(nSound == 3){
            Sound.playSound("LA_Sword_Slash4.wav");
        }
    }

    // The draw method is overriden to create the explosion effect.  Colored circles animate the explosion, with each circle
    // gradually growing larger, then smaller.
    @Override
    public void draw(Graphics g) {
        g.setColor(getRandomColor());

        // Controls the size of the circles being drawn depending on how long the counter missile has been alive
        double nIncrement = (MAX_EXPLOSION_RADIUS - MIN_EXPLOSION_RADIUS)/(MAX_EXPIRE/2.0);

        double nRadius = 0;
        if((double)getExpire()<MAX_EXPIRE/2.0){
            nRadius = MAX_EXPLOSION_RADIUS - nIncrement*(MAX_EXPIRE/2.0-(getExpire()));
        }
        else{
            nRadius = MIN_EXPLOSION_RADIUS + nIncrement*(MAX_EXPIRE-(getExpire()));
        }
        g.fillOval((int)getCenter().getX()-(int)nRadius/2, (int)getCenter().getY()-(int)nRadius/2, (int)nRadius, (int)nRadius);

        setRadius((int)nRadius);
    }

    // Randomly returns a color - white has a 40% chance of being selected, while yellow, cyan, and aqua have a 20% chance each
    public Color getRandomColor(){
        int nColorID = R.nextInt(5);

        Color col = null;
        if(nColorID < 2){
            col = Color.white;
        }
        else if(nColorID == 2){
            col = Color.yellow;
        }
        else if(nColorID == 3){
            col = Color.cyan;
        }
        else if(nColorID == 4){
            col = new Color(0,255,255);			// 'aqua' color
        }
        return col;
    }

    // Overrides the expire method - once an object expires remove it from the movFriends array list
    @Override
    public void expire() {
        if (getExpire() == 0){
            CommandCenter.movDebris.remove(this);
        }
        else{
            setExpire(getExpire() - 1);
        }
    }
}
