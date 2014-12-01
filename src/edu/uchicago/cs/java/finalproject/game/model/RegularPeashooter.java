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
    }

    public void move() {

        RegularBullet bullet = new RegularBullet(this);

        int initTime = Game.getTick()+initBullet;
        if(initTime%130 == 0){
            CommandCenter.movFriends.add(bullet);
        }

    }




}
