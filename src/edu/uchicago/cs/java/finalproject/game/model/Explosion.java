package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Created by yiqin on 11/24/14.
 */

/*
 Use MovableAdapter to update all
 */

public class Explosion extends MovableAdapter {

    private int mExpiry;
    private Point mCenter;
    private int mRadiux;


    public Explosion(RegularBullet bullet){
        mCenter = bullet.getCenter();
        mExpiry = 20; //  this is to control the time, I think.
        mRadiux = 10;
    }

    public Explosion(){
        mCenter = new Point(-200,-200);
        mExpiry = 20; //  this is to control the time, I think.
        mRadiux = 10;
    }

    @Override
    public void draw(Graphics g){

    }


    // Control the animation........
    // this is call GamePanel
    @Override
    public void expire(){
        if (mExpiry > 0){

        } else {
            CommandCenter.getMovDebris().remove(this);
        }


    }

}
