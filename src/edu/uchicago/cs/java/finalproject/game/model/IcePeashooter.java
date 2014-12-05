package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;

/**
 * Created by yiqin on 12/1/14.
 */
public class IcePeashooter extends RegularPeashooter {

    public IcePeashooter(int x, int y){
        super(x, y);
        mainColor = Color.CYAN;
    }

    public IcePeashooter(Point newPoint){

        super(newPoint);
        mainColor = Color.CYAN;
        CommandCenter.minusSunCredit(100);
    }


}
