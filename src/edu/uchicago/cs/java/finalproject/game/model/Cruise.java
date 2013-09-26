package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import edu.uchicago.cs.java.finalproject.controller.Game;

public class Cruise extends Sprite {

	private final double FIRE_POWER = 15.0;
	private final int MAX_EXPIRE = 25;
	
	//for drawing alternative shapes
	//you could have more than one of these sets so that your sprite morphs into various shapes
	//throughout its life
		public double[] dLengthsAlts;
		public double[] dDegreesAlts;

	public Cruise(Falcon fal) {

		super();

		//defined the points on a cartesean grid
		ArrayList<Point> pntCs = new ArrayList<Point>();


		pntCs.add(new Point(0, 5));
		pntCs.add(new Point(1, 3));
		pntCs.add(new Point(1, 0));
		pntCs.add(new Point(6, 0));
		pntCs.add(new Point(6, -1));
		pntCs.add(new Point(1, -1));
		pntCs.add(new Point(1, -2));

		pntCs.add(new Point(-1, -2));
		pntCs.add(new Point(-1, -1));
		pntCs.add(new Point(-6, -1));
		pntCs.add(new Point(-6, 0));
		pntCs.add(new Point(-1, 0));
		pntCs.add(new Point(-1, 3));
		assignPolarPoints(pntCs);
		
		
		//these are alt points
		ArrayList<Point> pntAs = new ArrayList<Point>();
		pntAs.add(new Point(0, 5));
		pntAs.add(new Point(1, 3));
		pntAs.add(new Point(1, -2));
		pntAs.add(new Point(-1, -2));
		pntAs.add(new Point(-1, 3));
		assignPolorPointsAlts(pntAs);

		//a cruis missile expires after 25 frames
		setExpire(MAX_EXPIRE);
		setRadius(20);

		//everything is relative to the falcon ship that fired the bullet
		setDeltaX(fal.getDeltaX()
				+ Math.cos(Math.toRadians(fal.getOrientation())) * FIRE_POWER);
		setDeltaY(fal.getDeltaY()
				+ Math.sin(Math.toRadians(fal.getOrientation())) * FIRE_POWER);
		setCenter(fal.getCenter());

		//set the bullet orientation to the falcon (ship) orientation
		setOrientation(fal.getOrientation());
		setColor(Color.RED);

	}

	
	//assign for alt imag
	protected void assignPolorPointsAlts(ArrayList<Point> pntCs) {
		 dDegreesAlts = convertToPolarDegs(pntCs);
		 dLengthsAlts = convertToPolarLens(pntCs);

	}
	
	@Override
	public void move() {

		super.move();
		
		if (getExpire() < MAX_EXPIRE -5){
			setDeltaX(getDeltaX() * 1.07);
			setDeltaY(getDeltaY() * 1.07);
		}
		


	}
	
	@Override
	public void draw(Graphics g){
		
		if (getExpire() < MAX_EXPIRE -5)
			super.draw(g);
		else{ 
			drawAlt(g);
		}
		
	}
	
	

    public void drawAlt(Graphics g) {
    	setXcoords( new int[dDegreesAlts.length]);
    	setYcoords( new int[dDegreesAlts.length]);
        setObjectPoints( new Point[dDegrees.length]);

        for (int nC = 0; nC < dDegreesAlts.length; nC++) {
        	
        	setXcoord((int) (getCenter().x + getRadius() 
                    * dLengthsAlts[nC] 
                    * Math.sin(Math.toRadians(getOrientation()) + dDegreesAlts[nC])), nC);
        	

        	setYcoord((int) (getCenter().y - getRadius()
                            * dLengthsAlts[nC]
                            * Math.cos(Math.toRadians(getOrientation()) + dDegreesAlts[nC])), nC);
            //need this line of code to create the points which we will need for debris
        	setObjectPoint( new Point(getXcoord(nC), getYcoord(nC)), nC);
        }
        
        g.setColor(Color.DARK_GRAY);
        g.drawPolygon(getXcoords(), getYcoords(), dDegreesAlts.length);
    }


	//override the expire method - once an object expires, then remove it from the arrayList.
	@Override
	public void expire() {
		if (getExpire() == 0)
			CommandCenter.movFriends.remove(this);
		else
			setExpire(getExpire() - 1);
	}

}
