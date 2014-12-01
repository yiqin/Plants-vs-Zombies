package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;

/**
 * Created by yiqin on 11/30/14.
 */
public class Peashooter extends Sprite {

    private final static int PEASHOOTER_RADIUS = 100;

    public Peashooter(int x, int y){
        super();

        setRadius(PEASHOOTER_RADIUS);



        setCenter(new Point(x, y));

    }

    public Peashooter(Point newPoint){
        super();

        setRadius(PEASHOOTER_RADIUS);
        setCenter(new Point((int)newPoint.getX(), (int)newPoint.getY()));
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
        g.fillRect(getCenter().x-50, getCenter().y-50, 100, 100);

        g.setColor(Color.green);
        g.fillOval(getCenter().x-40, getCenter().y-30, 50, 40);



    }

}
