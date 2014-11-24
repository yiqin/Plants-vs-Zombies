package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;

/**
 * Created by yiqin on 11/24/14.
 */
public abstract class MovableAdapter implements Movable {

    @Override
    public void move() {

    }

    @Override
    public void draw(Graphics g) {

    }

    @Override
    public int points() {
        return 0;
    }

    @Override
    public Point getCenter() {
        return null;
    }

    @Override
    public int getRadius() {
        return 0;
    }

    @Override
    public void expire() {

    }

    @Override
    public void fadeInOut() {

    }
}
