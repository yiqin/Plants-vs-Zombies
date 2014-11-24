package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Created by yiqin on 11/24/14.
 */
public class Explosion extends MovableAdapter {

    private int mExpiry;
    private Point mCenter;
    private int mRadiux;


    public Explosion(Bullet bullet){
        mCenter = bullet.getCenter();
        mExpiry = 20;
        mRadiux = 10;
    }

    @Override
    public void draw(Graphics g){

    }


    // Control the animation........
    @Override
    public void expire(){

    }

}
