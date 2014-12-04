package edu.uchicago.cs.java.finalproject.game.model;

import edu.uchicago.cs.java.finalproject.sounds.Sound;

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

    public int speed = 1;
    private int newSpeed = 1;

    public int speedRatio = 1;

    private boolean isFozen = false;

    public int nSize = 3;

    private int iceTime = 0;


    public Color mainColor = Color.red;


    public Point handPoint;


    public Zombie(int y){
        super();

        ArrayList<Point> pntCs = new ArrayList<Point>();

        setRadius(ZOMBIE_RADIUS);


        if (y<100){
            y = 300;
        }

        setCenter(new Point(1190, y));

        setDeltaX(-speed*0.5);

        pntCs.add(new Point(0*SCALER, 18*SCALER));

        //right points
        pntCs.add(new Point(10*SCALER, 18*SCALER));
        pntCs.add(new Point(12*SCALER, 20*SCALER));
        pntCs.add(new Point(12*SCALER, 18*SCALER));
        pntCs.add(new Point(12*SCALER, 0*SCALER));
        pntCs.add(new Point(11*SCALER, -2*SCALER));
        pntCs.add(new Point(4*SCALER, -3*SCALER));
        pntCs.add(new Point(2*SCALER, -10*SCALER));


        //left points
        pntCs.add(new Point(-2*SCALER, -10*SCALER));
        pntCs.add(new Point(-4*SCALER, -3*SCALER));
        pntCs.add(new Point(-11*SCALER, -2*SCALER));
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
        updateSpeed();
        if(speed==0){
            g.setColor(Color.cyan);
            g.fillRect(getCenter().x-35, getCenter().y-50, 60, 92);
            iceTime++;
        }

        if(iceTime==200){
            recover();
        }

        if (nSize >= 2){
            super.draw(g);
            // 1
            g.setColor(mainColor);
            g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
            // 2
            g.setColor(Color.lightGray);
            g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);
            // 3
            g.setColor(Color.black);
            g.fillArc(getCenter().x-45,getCenter().y-10, 35, 10,85,-175);
            // 4
            g.setColor(Color.darkGray);
            g.fillOval(getCenter().x-20, getCenter().y-30, 12, 10);
        }

        g.setColor(mainColor);
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
        handPoint = new Point(getCenter().x-15, getCenter().y+20);
        g2.drawLine(getCenter().x,getCenter().y+10, (int)handPoint.getX(), (int)handPoint.getY());
        g2.drawLine(getCenter().x,getCenter().y+10, getCenter().x+12, getCenter().y+18);

        g2.setStroke(new BasicStroke(1));
    }

    public int getSize(){
        return nSize;
    }

    public void isHit(int bulletType){
        nSize--;
        if (bulletType==1){
            frozen();

        }
        else {


        }
    }

    public void frozen(){
        speed = 0;
        iceTime = 0;
        setDeltaX(-speed*0.25);
        isFozen = true;
    }

    public void recover(){
        speed =checkSpeed();
        iceTime = 0;
        setDeltaX(-speed*0.5);
    }

    public int checkSpeed(){
        if(isFozen){
            newSpeed = 0;
            return newSpeed;
        }

        if(CommandCenter.getLevel()>2){
            newSpeed = 4;
            setDeltaX(-speed*0.5);
            stepLength = 14;
        }
        else {
            newSpeed = 1; // double size?
        }
        return newSpeed;
    }

    public void updateSpeed(){
        speed = checkSpeed()*speedRatio;
    }
}
