package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by yiqin on 11/30/14.
 */
public class RegularBullet extends Sprite  {

    private final static int REGULAR_BULLET_RADIUS = 40;

    private final double FIRE_POWER = 10.0;

    private Color bulletColor;

    public RegularBullet(Peashooter peashooter){

        super();

        bulletColor = peashooter.mainColor;

        setRadius(REGULAR_BULLET_RADIUS);

        setDeltaX( FIRE_POWER );
        setDeltaY( 0 );

        Point peashooterRelativePoint = peashooter.getCenter();

        // Control position.....
        setCenter(new Point((int)peashooterRelativePoint.getX()+20, (int)peashooterRelativePoint.getY()-20));

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
        g.setColor(bulletColor);
        g.fillOval(getCenter().x, getCenter().y, REGULAR_BULLET_RADIUS, REGULAR_BULLET_RADIUS);
    }

}
