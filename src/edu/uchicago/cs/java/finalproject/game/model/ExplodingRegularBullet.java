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

        // playExplosionSounds();					// randomly plays an explosion sound
        // This is the reason that generate a null???????
    }

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
