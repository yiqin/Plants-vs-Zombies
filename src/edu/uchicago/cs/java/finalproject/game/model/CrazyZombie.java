package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;
import java.util.ArrayList;

import edu.uchicago.cs.java.finalproject.controller.Game;

/**
 * Created by yiqin on 12/3/14.
 */
public class CrazyZombie extends Zombie {

    public CrazyZombie(int y){

        super(y);

        stepLength = 6;

        nSize = 1;
        speedRatio = 5;

        // setCenter(new Point((int)getCenter().getX(), (int)getCenter().getY()));
    }

    public void move(){
        super.move();

    }

    public void draw(Graphics g) {
        mainColor = getRandomColor();
        super.draw(g);
    }

    public Color getRandomColor(){
        int nColorID = Game.R.nextInt(5);

        Color col = null;
        if(nColorID < 2){
            col = Color.white;
        }
        else if(nColorID == 2){
            col = Color.yellow;
        }
        else if(nColorID == 3){
            col = mainColor = new Color(255, 140, 0);;
        }
        else if(nColorID == 4){
            col = new Color(0,255,255);			// 'aqua' color
        }
        return col;
    }

}
