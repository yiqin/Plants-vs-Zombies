package edu.uchicago.cs.java.finalproject.game.view;

import edu.uchicago.cs.java.finalproject.controller.Game;


import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Created with IntelliJ IDEA.
 * User: ag
 * Date: 11/21/13
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class OffScreenImage  {

	private Image imgOff;
	private Graphics grpOff;
    private Font fnt ;
    private Font fntBig;
    private FontMetrics fmt;
    private int nFontWidth;
    private int nFontHeight;

    public OffScreenImage() {
        reset();
        fnt = new Font("SansSerif", Font.BOLD, 12);
        fntBig = new Font("SansSerif", Font.BOLD + Font.ITALIC, 36);
        grpOff.setFont(fnt);						// take care of some simple font stuff
        fmt = grpOff.getFontMetrics();
        nFontWidth = fmt.getMaxAdvance();
        nFontHeight = fmt.getHeight();
        grpOff.setFont(fntBig);					// set font info
    }

    public void reset(){
        imgOff = new BufferedImage(Game.DIM.width, Game.DIM.height,BufferedImage.TYPE_INT_ARGB);
        grpOff = imgOff.getGraphics();
    }

    public Font getFnt() {
        return fnt;
    }

    public void setFnt(Font fnt) {
        this.fnt = fnt;
    }

    public int getFontHeight() {
        return nFontHeight;
    }

    public void setFontHeight(int nFontHeight) {
        this.nFontHeight = nFontHeight;
    }

    public int getFontWidth() {
        return nFontWidth;
    }

    public void setFontWidth(int nFontWidth) {
        this.nFontWidth = nFontWidth;
    }

    public FontMetrics getFmt() {
        return fmt;
    }

    public Graphics getGrpOff() {
        return grpOff;
    }

    public void setGrpOff(Graphics grpOff) {
        this.grpOff = grpOff;
    }

    public Image getImgOff() {
        return imgOff;
    }

    public void setImgOff(Image imgOff) {
        this.imgOff = imgOff;
    }
}
