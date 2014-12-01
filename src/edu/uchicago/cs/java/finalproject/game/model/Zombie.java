package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by yiqin on 11/30/14.
 */
public class Zombie extends Sprite {

    private final static int ZOMBIE_RADIUS = 50;

    private final static int SCALER = 3;

    private boolean isSwingToLeft = true;
    private int stepLength = 10;
    private int leftFootX = -stepLength;
    private int rightFootX = stepLength;


    public Zombie(int y){
        super();

        ArrayList<Point> pntCs = new ArrayList<Point>();

        setRadius(ZOMBIE_RADIUS);


        if (y==0){
            y = 300;
        }

        setCenter(new Point(1190, y));

        setDeltaX(-0.5);

        // plot some figure...
        // pntCs.add(new Point(0,18));
        /*
        pntCs.add(new Point(20,20));
        pntCs.add(new Point(-20,20));
        pntCs.add(new Point(20,-20));
        pntCs.add(new Point(-20,-20));
        */
        // top of ship
        pntCs.add(new Point(0*SCALER, 18*SCALER));

        //right points
        pntCs.add(new Point(12*SCALER, 0*SCALER));
        pntCs.add(new Point(13*SCALER, -2*SCALER));
        pntCs.add(new Point(13*SCALER, -4*SCALER));
        pntCs.add(new Point(11*SCALER, -2*SCALER));
        pntCs.add(new Point(4*SCALER, -3*SCALER));
        pntCs.add(new Point(2*SCALER, -10*SCALER));


        //left points
        pntCs.add(new Point(-2*SCALER, -10*SCALER));
        pntCs.add(new Point(-4*SCALER, -3*SCALER));
        pntCs.add(new Point(-11*SCALER, -2*SCALER));
        pntCs.add(new Point(-13*SCALER, -4*SCALER));
        pntCs.add(new Point(-13*SCALER, -2*SCALER));
        pntCs.add(new Point(-12*SCALER, 0*SCALER));



        assignPolarPoints(pntCs);


        setOrientation(-100);
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
        // g.setColor(Color.BLUE);
        // g.fillRect(getCenter().x-40, getCenter().y-40, 80, 80);

        super.draw(g);
        //fill this polygon (with whatever color it has)
        g.setColor(Color.red);
        g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
        //now draw a white border
        g.setColor(Color.red);
        g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);

        // System.out.println("DRAW ZOMBIE>>>>>>>>>>>>>>>");

        // g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);


        // g.drawLine(getCenter().x,getCenter().y,getCenter().x-30, getCenter().y+40);

        if(leftFootX < -stepLength){
            isSwingToLeft = false;
        }
        else if(leftFootX > stepLength){
            isSwingToLeft = true;
        }

        if (isSwingToLeft){
            leftFootX--;
            rightFootX++;
        }
        else {
            leftFootX++;
            rightFootX--;
        }




        g.drawLine(getCenter().x,getCenter().y,getCenter().x-leftFootX, getCenter().y+40);
        g.drawLine(getCenter().x,getCenter().y,getCenter().x-rightFootX, getCenter().y+40);
    }

}
