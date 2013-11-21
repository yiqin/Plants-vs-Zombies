package edu.uchicago.cs.java.finalproject.game.model;

import edu.uchicago.cs.java.finalproject.controller.Game;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: ag
 * Date: 11/20/13
 * Time: 11:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class Debris extends Sprite {


    private Color col;
    private int nSpin;

    public Debris(Sprite spr, Point pntBegin, Point pntEnd) {
        super();
        col = spr.getColor();

        //get the radius using a^2 + b^2 = c2  and div by 2
        double dRad = Math.sqrt((Math.pow(pntBegin.getX() - pntEnd.getX(),2)) +
                Math.pow((pntBegin.getY() - pntEnd.getY()),2))/ 2;

        //get midpoint
        Point pntMid = new Point((int)(pntBegin.getX() + pntEnd.getX() )/ 2,
                (int)(pntBegin.getY() + pntEnd.getY() )/ 2);

        //outward movement - blow out
        setDeltaX(spr.getDeltaX() + (pntMid.x - spr.getCenter().x)/6);
        setDeltaY(spr.getDeltaY()+ (pntMid.y - spr.getCenter().y)/6);

        setCenter(pntMid);
        setRadius((int)dRad);

        //set orientation
        double dRadians = Math.atan2(pntBegin.y - pntMid.y, pntBegin.x - pntMid.x);
        setOrientation((int)Math.toDegrees(dRadians));

        //set polar coordinates
        double[] dLengths = { 1, 1};
        double[] dDegrees = {
                Math.PI / 2,
                180 * Math.PI / 360 + Math.PI};

        setLengths(dLengths);
        setDegrees(dDegrees);

        //set random spin
        if(Game.R.nextBoolean())
            setSpin(Game.R.nextInt(6));
        else
            setSpin(-Game.R.nextInt(6));


        setExpire(30);
        setFadeValue( 255 );
        this.setCol(col);
    }




    public void fadeInOut (){

        if (getFadeValue() > 20)
            setFadeValue(getFadeValue() - 10);

        Color colTemp = getCol();

        int nR = 255 - colTemp.getRed();
        int nG = 255 - colTemp.getGreen();
        int nB = 255 - colTemp.getBlue();

        setColor(new Color( Math.abs( getFadeValue() - nR ),
                Math.abs( getFadeValue() - nG ),
                Math.abs( getFadeValue() - nB ) ));



    }


    public void move(){
        super.move();
        setOrientation(getOrientation() + getSpin());

    }

    public int getSpin() {
        return nSpin;
    }

    public void setSpin(int nSpin) {
        this.nSpin = nSpin;
    }

    public Color getCol() {
        return this.col;
    }


    public void setCol(Color col) {
        this.col = col;
    }

    public void expire(){
        if (getExpire() == 0)
            CommandCenter.movDebris.remove(this);
        else
            setExpire(getExpire() - 1);
    }



}
