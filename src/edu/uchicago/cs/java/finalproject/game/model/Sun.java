package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by yiqin on 11/30/14.
 */
public class Sun extends Sprite {

    private final static int SUN_RADIUS = 40;
    private final static int SCALER = 2;

    public long credit = 100;

    public Sun(int x){
        super();

        setExpire(400);
        setRadius(SUN_RADIUS);

        setCenter(new Point(x, 0));
        setDeltaY(1);


        ArrayList<Point> pntCs = new ArrayList<Point>();
        // plot some figure...
        pntCs.add(new Point(0,18*SCALER));
        pntCs.add(new Point(10*SCALER, 18*SCALER));
        pntCs.add(new Point(12*SCALER, 20*SCALER));

        assignPolarPoints(pntCs);
        setOrientation(-90);
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
        super.draw(g);
        g.setColor(Color.WHITE);
        //fill this polygon (with whatever color it has)
        g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
        //now draw a white border

        g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);

        g.setColor(Color.YELLOW);
        g.fillOval(getCenter().x-SUN_RADIUS, getCenter().y-SUN_RADIUS, 2*SUN_RADIUS, 2*SUN_RADIUS);
    }
}
