package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by yiqin on 11/30/14.
 */
public class RegularBullet extends Sprite  {

    private final static int REGULAR_BULLET_RADIUS = 50;

    private final double FIRE_POWER = 10.0;



    public RegularBullet(Peashooter peashooter){

        super();


        //defined the points on a cartesean grid
        ArrayList<Point> pntCs = new ArrayList<Point>();

        pntCs.add(new Point(0,3)); //top point

        pntCs.add(new Point(1,-1));
        pntCs.add(new Point(0,-2));
        pntCs.add(new Point(-1,-1));

        assignPolarPoints(pntCs);

        //a bullet expires after 20 frames
        setRadius(6);


        //everything is relative to the falcon ship that fired the bullet
        setDeltaX( FIRE_POWER );
        setDeltaY( 0 );

        Point peashooterRelativePoint = peashooter.getCenter();

        // Control position.....
        setCenter(new Point((int)peashooterRelativePoint.getX()+20, (int)peashooterRelativePoint.getY()));

        //set the bullet orientation to the falcon (ship) orientation
        setOrientation(peashooter.getOrientation());

    }

    public void move() {
        int x = getCenter().x;

        super.move();
        if (x>1150){
            CommandCenter.movFriends.remove(this);
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.green);
        g.fillOval(getCenter().x, getCenter().y, REGULAR_BULLET_RADIUS, REGULAR_BULLET_RADIUS);
    }

}
