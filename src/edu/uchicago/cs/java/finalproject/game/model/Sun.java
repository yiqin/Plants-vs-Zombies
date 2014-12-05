package edu.uchicago.cs.java.finalproject.game.model;

import edu.uchicago.cs.java.finalproject.controller.Game;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by yiqin on 11/30/14.
 */
public class Sun extends Sprite {

    private final static int SUN_RADIUS = 20;
    private final static int SCALER = 2;

    public long credit = 50;
    private int isLeftRotation = 1;
    private int stopRotationY = 0;

    private int isStatic = 1;

    public Sun(int x, int y){
        super();

        setRadius(SUN_RADIUS*2);

        setCenter(new Point(x, y));

        figure();
    }

    public Sun(int x){
        super();
        isStatic = 0;

        setExpire(400);
        setRadius(SUN_RADIUS*2);

        setCenter(new Point(x, 0));
        setDeltaY(1);


        figure();


    }

    public void figure(){

        ArrayList<Point> pntCs = new ArrayList<Point>();
        // plot some figure...
        pntCs.add(new Point(0,20*SCALER));
        pntCs.add(new Point(-5*SCALER, 9*SCALER));
        pntCs.add(new Point(-20*SCALER, 12*SCALER));

        pntCs.add(new Point(-9*SCALER, 0*SCALER));

        pntCs.add(new Point(-18*SCALER, -7*SCALER));

        pntCs.add(new Point(-5*SCALER, -12*SCALER));

        pntCs.add(new Point(1*SCALER,-20*SCALER));

        pntCs.add(new Point(5*SCALER, -7*SCALER));
        pntCs.add(new Point(18*SCALER, -12*SCALER));

        pntCs.add(new Point(10*SCALER, 0*SCALER));
        pntCs.add(new Point(20*SCALER, 5*SCALER));
        pntCs.add(new Point(6*SCALER, 8*SCALER));

        assignPolarPoints(pntCs);
        setOrientation(-90);

        if(Game.R.nextInt()%2 == 0){
            isLeftRotation=1;
        }
        else {
            isLeftRotation=-1;
        }

        stopRotationY = Game.R.nextInt()%100;
    }

    public void move(){
        super.move();

        int x = getCenter().x;
        int y = getCenter().y;

        int yUpdate = 0;
        if (y>540 && isStatic == 0){
            yUpdate = 540;
        }
        else {
            yUpdate = y+(int)getDeltaY();
        }

        // Stop rotation.
        if (y > 300+stopRotationY){
            isLeftRotation = 0;
        }

        setCenter(new Point(x, yUpdate));

        setOrientation(getOrientation()+1*isLeftRotation);
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

        g.setColor(Color.ORANGE);
        //fill this polygon (with whatever color it has)
        g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
        //now draw a white border

        g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);

        g.setColor(Color.YELLOW);
        g.fillOval(getCenter().x-SUN_RADIUS-3, getCenter().y-SUN_RADIUS-3, 2*SUN_RADIUS+6, 2*SUN_RADIUS+6);

    }
}
