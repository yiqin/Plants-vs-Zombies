package edu.uchicago.cs.java.finalproject.game.model;

import edu.uchicago.cs.java.finalproject.controller.Game;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by yiqin on 12/3/14.
 */
public class IronZombie extends Zombie {

    public IronZombie(int y){
        super(y);

        nSize = 5;

        mainColor = new Color(255, 140, 0);
    }

    public void draw(Graphics g) {
        super.draw(g);

        if(nSize >3){
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(getCenter().x-15, getCenter().y+20, getCenter().x-32, getCenter().y+10);


            g2.setColor(new Color(192,192,192));


            g2.setStroke(new BasicStroke(7));
            g2.drawLine(getCenter().x-32, getCenter().y-10, getCenter().x-32, getCenter().y+40);

            g2.setStroke(new BasicStroke(1));
        }

    }


}
