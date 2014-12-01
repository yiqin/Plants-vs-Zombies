package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;

/**
 * Created by yiqin on 11/30/14.
 */
public class Zombie extends Sprite {

    private final static int ZOMBIE_RADIUS = 20;

    public Zombie(int y){
        super();

        // ArrayList<Point> pntCs = new ArrayList<Point>();

        setRadius(ZOMBIE_RADIUS);

        setCenter(new Point(1190, y));

        setDeltaX(-0.5);

        // plot some figure...
        // pntCs.add(new Point(0,3));
        // assignPolarPoints(pntCs);
    }

    public void move(){
        super.move();

        int x = getCenter().x;
        int y = getCenter().y;

        int xUpdate = x+(int)getDeltaX();
        setCenter(new Point(xUpdate, y));
    }

    @Override
    public void draw(Graphics g) {
        // super.draw(g);
        //fill this polygon (with whatever color it has)
        // g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
        //now draw a white border
        // g.setColor(Color.WHITE);
        // g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);

        g.setColor(Color.BLUE);
        g.fillRect(getCenter().x, getCenter().y, 80, 80);
    }

}
