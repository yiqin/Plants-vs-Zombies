package edu.uchicago.cs.java.finalproject.game.model;

import edu.uchicago.cs.java.finalproject.sounds.Sound;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by yiqin on 12/1/14.
 */
public class ExplodingHead extends Sprite  {

    // =============================2=================================
    // FIELDS
    // ==============================================================

    private final int MAX_EXPIRE = 100;					// how long the explosion lasts
    private final static int SCALER = 3;
    private final static int ZOMBIE_RADIUS = 50;

    private final int originalY;
    private int headRotation=1;
    // ==============================================================
    // CONSTRUCTOR
    // ==============================================================

    // Sets up the basic settings
    public ExplodingHead(Point pnt) {
        super();

        originalY = pnt.y;

        setDeltaX(0);
        setDeltaY(1);
        setCenter(pnt);

        setExpire(MAX_EXPIRE);
        setRadius(ZOMBIE_RADIUS);

        // playExplosionSounds();					// randomly plays an explosion sound

        ArrayList<Point> pntCs = new ArrayList<Point>();
        pntCs.add(new Point(0*SCALER, 18*SCALER));

        //right points
        pntCs.add(new Point(10*SCALER, 18*SCALER));
        pntCs.add(new Point(12*SCALER, 20*SCALER));
        pntCs.add(new Point(12*SCALER, 18*SCALER));
        pntCs.add(new Point(12*SCALER, 0*SCALER));
        pntCs.add(new Point(11*SCALER, -2*SCALER));
        pntCs.add(new Point(4*SCALER, -3*SCALER));

        //left points
        pntCs.add(new Point(-4*SCALER, -3*SCALER));
        pntCs.add(new Point(-11*SCALER, -2*SCALER));
        pntCs.add(new Point(-12*SCALER, 0*SCALER));
        pntCs.add(new Point(-12*SCALER, 18*SCALER));

        assignPolarPoints(pntCs);

        setOrientation(-100);
    }


    // ==============================================================
    // METHODS
    // ==============================================================

    // Randomly plays one of 4 explosion sounds (each sound is actually a sword sound effect from Link's Awakening, a Zelda game)
    public void playExplosionSounds(){
        Random R = new Random();
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

    public void move(){
        super.move();

        int x = getCenter().x;
        int y = getCenter().y;

        int yUpdate = 0;
        if (y>originalY+40){
            yUpdate = originalY+40;

        }
        else {
            yUpdate = y+(int)getDeltaY();
        }

        int xUpdate = x+(int)getDeltaX();

        setCenter(new Point(xUpdate, yUpdate));
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        // 1
        g.setColor(Color.red);
        g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
        // 2
        g.setColor(Color.lightGray);
        g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);
        // 3
        g.setColor(Color.black);
        g.fillArc(getCenter().x-45,getCenter().y-10, 35, 10,85,-175);
        // 4
        g.setColor(Color.darkGray);
        g.fillOval(getCenter().x-20, getCenter().y-30, 12, 10);
    }

    @Override
    public void expire() {
        if (getExpire() == 0){
            CommandCenter.movDebris.remove(this);
        }
        else{
            setExpire(getExpire() - 1);
        }

        if (getExpire()>MAX_EXPIRE-10){
            setDeltaY(-1);
            setDeltaX(1);
        }
        else if(getExpire()>MAX_EXPIRE-30) {
            setDeltaY(1);
            setDeltaX(0);
        }

        if (getExpire()>MAX_EXPIRE-10){
            setOrientation(getOrientation()+1);
        }
        else if(getExpire()<MAX_EXPIRE-10 && getExpire()>MAX_EXPIRE-20) {

        }
        else{
            if (getExpire()%10==0){
                headRotation = headRotation*(-1);
            }
            setOrientation(getOrientation()-1*headRotation);
        }
    }
    
}
