package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;

/**
 * Created by yiqin on 11/30/14.
 */
public class Peashooter extends Sprite {

    public Peashooter(int x, int y){
        super();

        setRadius(100);
        setCenter(new Point(x, y));

    }

    @Override
    public void draw(Graphics g) {
        // super.draw(g);
        //fill this polygon (with whatever color it has)
        // g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
        //now draw a white border
        // g.setColor(Color.WHITE);
        // g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);

        g.setColor(Color.RED);
        g.fillRect(getCenter().x, getCenter().y, 100, 100);
    }

}
