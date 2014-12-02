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

    private int speed = 1;

    private int nSize = 3;

    private int iceTime = 0;

    public Zombie(int y){
        super();

        ArrayList<Point> pntCs = new ArrayList<Point>();

        setRadius(ZOMBIE_RADIUS);


        if (y==0){
            y = 300;
        }

        setCenter(new Point(1190, y));

        setDeltaX(-speed*0.5);

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
        pntCs.add(new Point(10*SCALER, 18*SCALER));
        pntCs.add(new Point(12*SCALER, 20*SCALER));
        pntCs.add(new Point(12*SCALER, 18*SCALER));
        pntCs.add(new Point(12*SCALER, 0*SCALER));
        // pntCs.add(new Point(13*SCALER, -2*SCALER));
        // pntCs.add(new Point(13*SCALER, -4*SCALER));
        pntCs.add(new Point(11*SCALER, -2*SCALER));
        pntCs.add(new Point(4*SCALER, -3*SCALER));
        pntCs.add(new Point(2*SCALER, -10*SCALER));


        //left points
        pntCs.add(new Point(-2*SCALER, -10*SCALER));
        pntCs.add(new Point(-4*SCALER, -3*SCALER));
        pntCs.add(new Point(-11*SCALER, -2*SCALER));
        // pntCs.add(new Point(-13*SCALER, -4*SCALER));
        // pntCs.add(new Point(-13*SCALER, -2*SCALER));
        pntCs.add(new Point(-12*SCALER, 0*SCALER));
        pntCs.add(new Point(-12*SCALER, 18*SCALER));



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
        if(speed==0){
            g.setColor(Color.cyan);
            g.fillRect(getCenter().x-33, getCenter().y-50, 60, 90);
            iceTime++;
        }

        if(iceTime==200){
            recover();
        }

        super.draw(g);
        //fill this polygon (with whatever color it has)
        g.setColor(Color.red);
        g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
        //now draw a white border

        g.setColor(Color.lightGray);
        g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);

        g.setColor(Color.red);
        if(leftFootX < -stepLength){
            isSwingToLeft = false;
        }
        else if(leftFootX > stepLength){
            isSwingToLeft = true;
        }

        if (isSwingToLeft){
            leftFootX=leftFootX-speed;
            rightFootX=rightFootX+speed;
        }
        else {
            leftFootX=leftFootX+speed;
            rightFootX=rightFootX-speed;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(4));
        g2.drawLine(getCenter().x+3,getCenter().y+20,getCenter().x-leftFootX, getCenter().y+40);
        g2.drawLine(getCenter().x+3,getCenter().y+20,getCenter().x-rightFootX, getCenter().y+40);

        g2.setStroke(new BasicStroke(2));
        g2.drawLine(getCenter().x,getCenter().y+10, getCenter().x-15, getCenter().y+20);
        g2.drawLine(getCenter().x,getCenter().y+10, getCenter().x+12, getCenter().y+18);
        // g2.drawLine(getCenter().x,getCenter().y+20, getCenter().x-15, getCenter().y+15);

        g2.setStroke(new BasicStroke(1));



        g.setColor(Color.black);
        // g.fillOval(getCenter().x-40,getCenter().y-10, 30, 10);
        g.fillArc(getCenter().x-45,getCenter().y-10, 35, 10,85,-180);

        // int xPoly[] = {getCenter().x-30,getCenter().x-2,getCenter().x-20};
        // int yPoly[] = {getCenter().y,getCenter().y,getCenter().y-10};

        // g.fillPolygon(xPoly, yPoly, xPoly.length);


        g.setColor(Color.darkGray);
        g.fillOval(getCenter().x-20, getCenter().y-30, 12, 10);



    }

    public int getSize(){
        return nSize;
    }

    public void isHit(int bulletType){
        nSize--;
        if (bulletType==1){
            frozen();
        }

    }

    public void frozen(){
        speed = 0;
        iceTime = 0;
        setDeltaX(-speed*0.25);
    }

    public void recover(){
        speed =1;
        iceTime = 0;
        setDeltaX(-speed*0.5);
    }

}
