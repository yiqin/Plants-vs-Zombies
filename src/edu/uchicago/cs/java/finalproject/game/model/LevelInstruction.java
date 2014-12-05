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

        setExpire(100);

        setRadius(500);

        setCenter(new Point(x, y));


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

        g.setColor(Color.yellow);
        g.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4 + nFontHeight + 0);

        g.setFont(GamePanel.fnt);
    }



}
