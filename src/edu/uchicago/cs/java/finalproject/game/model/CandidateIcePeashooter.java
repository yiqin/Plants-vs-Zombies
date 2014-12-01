package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;

/**
 * Created by yiqin on 12/1/14.
 */
public class CandidateIcePeashooter extends Peashooter {

    public CandidateIcePeashooter(int x, int y){
        super(x,y);
        mainColor = Color.CYAN;
    }

    public CandidateIcePeashooter(Point newPoint){
        super(newPoint);
        mainColor = Color.CYAN;
    }

}