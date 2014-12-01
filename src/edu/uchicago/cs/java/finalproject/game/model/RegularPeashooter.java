package edu.uchicago.cs.java.finalproject.game.model;

import edu.uchicago.cs.java.finalproject.controller.Game;

import java.awt.*;

/**
 * Created by yiqin on 11/30/14.
 */
public class RegularPeashooter extends Peashooter {

    private int initBullet;

    public RegularPeashooter(int x, int y){
        super(x, y);
        initBullet =  (int)(Math.random()*10);
    }

    public RegularPeashooter(Point newPoint){
        super(newPoint);
        initBullet =  (int)(Math.random()*130);


        int x = (int)newPoint.getX();
        int y = (int)newPoint.getY();

        int modX = x%100;
        int modY = y%100;

        if(modX<50){
            x = x-modX;
        }
        else {
            x = x+100-modX;
        }

        if(modY<50){
            y = y-modY;
        }
        else {
            y = y+100-modY;
        }
        /*
        if(modX<50){
            x = ((int)Math.ceil(newPoint.getX()/100))*100;
        }
        else {
            x = ((int)Math.floor(newPoint.getX()/100))*100;
        }

        if(modY<50){
            y = ((int)Math.ceil(newPoint.getY()/100))*100;
        }
        else {
            y = ((int)Math.floor(newPoint.getY()/100))*100;
        }
        */

        setCenter(new Point(x, y));

    }

    public void move() {

        RegularBullet bullet = new RegularBullet(this);

        int initTime = Game.getTick()+initBullet;
        if(initTime%130 == 0){
            CommandCenter.movFriends.add(bullet);
        }

    }




}
