package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;
import edu.uchicago.cs.java.finalproject.controller.Game;

/**
 * Created by yiqin on 12/3/14.
 */
public class CrazyZombie extends Zombie {


    public CrazyZombie(int y){

        super(y);


        setRadius(25);

        nSize = 5;
        speedRatio = 2;
        mainColor = new Color(255, 140, 0);
        setCenter(new Point((int)getCenter().getX(), (int)getCenter().getY()+20));
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
            col = Color.cyan;
        }
        else if(nColorID == 4){
            col = new Color(0,255,255);			// 'aqua' color
        }
        return col;
    }

    public void updateSpeed(){
        super.updateSpeed();
        speed = checkSpeed()*speedRatio;
    }

}
