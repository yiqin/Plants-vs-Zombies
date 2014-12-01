package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by yiqin on 11/30/14.
 */
public class Sun extends Sprite {

    private final static int SUN_RADIUS = 100;

    public long credit = 100;

    public Sun(int x){
        super();

        // ArrayList<Point> pntCs = new ArrayList<Point>();

        setExpire(400);
        setRadius(SUN_RADIUS);

        setCenter(new Point(x, 0));


        setDeltaY(1);

        // plot some figure...
        // pntCs.add(new Point(0,3));
        // assignPolarPoints(pntCs);
    }

    public void move(){
        super.move();

        int x = getCenter().x;
        int y = getCenter().y;

        int yUpdate = 0;
        if (y>500){
            yUpdate = 500;
        }
        else {
            yUpdate = y+(int)getDeltaY();
        }

        setCenter(new Point(x, yUpdate));
    }

    //override the expire method - once an object expires, then remove it from the arrayList.
    public void expire(){
        if (getExpire() == 0)
            CommandCenter.movSun.remove(this);
        else
            setExpire(getExpire() - 1);
    }

    @Override
    public void draw(Graphics g) {
        // super.draw(g);
        //fill this polygon (with whatever color it has)
        // g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
        //now draw a white border
        // g.setColor(Color.WHITE);
        // g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);

        g.setColor(Color.YELLOW);
        g.fillOval(getCenter().x, getCenter().y, SUN_RADIUS, SUN_RADIUS);
    }
}
