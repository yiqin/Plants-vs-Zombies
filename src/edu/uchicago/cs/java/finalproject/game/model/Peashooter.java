package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;

/**
 * Created by yiqin on 11/30/14.
 */
public class Peashooter extends Sprite {

    private final static int PEASHOOTER_RADIUS = 100;

    public Color mainColor = Color.green;

    public int typeIndicator = 0;

    public Peashooter(int x, int y){
        super();

        setRadius(PEASHOOTER_RADIUS);
        setCenter(new Point(x, y));
    }

    public Peashooter(Point newPoint){
        super();

        setRadius(PEASHOOTER_RADIUS);
        setCenter(new Point((int)newPoint.getX(), (int)newPoint.getY()));
    }


    @Override
    public void draw(Graphics g) {
        // super.draw(g);
        //fill this polygon (with whatever color it has)
        // g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
        //now draw a white border
        // g.setColor(Color.WHITE);
        // g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);

        g.setColor(Color.RED);
        // g.fillRect(getCenter().x-50, getCenter().y-50, 100, 100);



        g.setColor(mainColor);
        g.fillOval(getCenter().x-40, getCenter().y-30, 55, 45);

        g.fillOval(getCenter().x-20+2, getCenter().y+10, 15, 10);

        g.fillArc(getCenter().x-40-10, getCenter().y+30, 80, 20, 240, 340);

        g.fillRect(getCenter().x-10, getCenter().y-10, 50, 20);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(8));
        g2.drawLine(getCenter().x-14,getCenter().y+10,getCenter().x, getCenter().y+40);

        g2.setStroke(new BasicStroke(1));



        g.setColor(Color.white);
        g.fillOval(getCenter().x, getCenter().y-15, 8, 12);

        g.setColor(Color.black);
        g.fillOval(getCenter().x+4, getCenter().y-12, 4, 6);



        g.setColor(Color.darkGray);
        g.fillOval(getCenter().x+20, getCenter().y-18, 30, 36);

        g2.setStroke(new BasicStroke(2));
        g2.setColor(mainColor);
        g2.drawOval(getCenter().x+20, getCenter().y-18, 30, 36);
        g2.setStroke(new BasicStroke(1));
    }

}
