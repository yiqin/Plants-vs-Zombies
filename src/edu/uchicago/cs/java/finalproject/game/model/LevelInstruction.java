package edu.uchicago.cs.java.finalproject.game.model;

import edu.uchicago.cs.java.finalproject.controller.Game;
import edu.uchicago.cs.java.finalproject.game.view.GamePanel;

import java.awt.*;

/**
 * Created by yiqin on 12/4/14.
 */
public class LevelInstruction  extends Sprite  {

    public String strDisplay = "Level Instruction............";
    private FontMetrics fmt;
    private int nFontHeight;


    public LevelInstruction(int x, int y, String str_){
        super();


        strDisplay = str_;

        setExpire(150);

        setRadius(500);

        setDeltaY(1.5);

        setCenter(new Point(x, y));


    }

    public void move(){
        super.move();

        int x = getCenter().x;
        int y = getCenter().y;

        int yUpdate = 0;
        if (y>200){
            yUpdate = 200;
        }
        else {
            yUpdate = y+(int)getDeltaY();
        }

        setCenter(new Point(x, yUpdate));
    }

    //override the expire method - once an object expires, then remove it from the arrayList.
    public void expire(){
        if (getExpire() == 0)
            CommandCenter.movLevelInstruction.remove(this);
        else
            setExpire(getExpire() - 1);
    }

    @Override
    public void draw(Graphics g) {
        // super.draw(g);

        g.setFont(GamePanel.fntBig);

        fmt = g.getFontMetrics();
        nFontHeight = fmt.getHeight();

        g.setColor(new Color(118,210,182));
        g.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, (int)getCenter().getY());

        g.setFont(GamePanel.fnt);
    }



}
