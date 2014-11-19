package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import edu.uchicago.cs.java.finalproject.controller.Game;

public abstract class Sprite implements Movable {
	//the center-point of this sprite
	private Point pntCenter;
	//this causes movement; change in x and change in y
	private double dDeltaX, dDeltaY;
	//every sprite needs to know about the size of the gaming environ
	private Dimension dim; //dim of the gaming environment

	//the radius of circumscibing circle
	private int nRadius;
	//is this DEBRIS, FRIEND, FOE, OR FLOATER 
	//private byte yFriend;
	//degrees (where the sprite is pointing out of 360)
	private int nOrientation;
	private int nExpiry; //natural mortality (short-living objects)
	//the color of this sprite
	private Color col;

	//radial coordinates
	//this game uses radial coordinates to render sprites
	public double[] dLengths;
	public double[] dDegrees;
	
	//for drawing alternative 
	//public double[] dLengthAlts;
	//public double[] dDegreeAlts;
	

	//fade value for fading in and out
	private int nFade;

	//these are used to draw the polygon. You don't usually need to interface with these
	private Point[] pntCoords; //an array of points used to draw polygon
	private int[] nXCoords;
	private int[] nYCoords;

	public void move() {

		Point pnt = getCenter();
		double dX = pnt.x + getDeltaX();
		double dY = pnt.y + getDeltaY();
		
		//this just keeps the sprite inside the bounds of the frame
		if (pnt.x > getDim().width) {
			setCenter(new Point(1, pnt.y));

		} else if (pnt.x < 0) {
			setCenter(new Point(getDim().width - 1, pnt.y));
		} else if (pnt.y > getDim().height) {
			setCenter(new Point(pnt.x, 1));

		} else if (pnt.y < 0) {
			setCenter(new Point(pnt.x, getDim().height - 1));
		} else {

			setCenter(new Point((int) dX, (int) dY));
		}

	}

	public Sprite() {

	//you can override this and many more in the subclasses
		setDim(Game.DIM);
		setColor(Color.white);
		setCenter(new Point(Game.R.nextInt(Game.DIM.width),
				Game.R.nextInt(Game.DIM.height)));


	}

	public void setExpire(int n) {
		nExpiry = n;

	}

	public double[] getLengths() {
		return this.dLengths;
	}

	public void setLengths(double[] dLengths) {
		this.dLengths = dLengths;
	}

	public double[] getDegrees() {
		return this.dDegrees;
	}

	public void setDegrees(double[] dDegrees) {
		this.dDegrees = dDegrees;
	}

	public Color getColor() {
		return col;
	}

	public void setColor(Color col) {
		this.col = col;

	}

	public int points() {
		//default is zero
		return 0;
	}

	public int getExpire() {
		return nExpiry;
	}

	public boolean isExploding() {
		return false;
	}

	public void fadeInOut() {
	};

	public int getOrientation() {
		return nOrientation;
	}

	public void setOrientation(int n) {
		nOrientation = n;
	}

	public void setDeltaX(double dSet) {
		dDeltaX = dSet;
	}

	public void setDeltaY(double dSet) {
		dDeltaY = dSet;
	}

	public double getDeltaY() {
		return dDeltaY;
	}

	public double getDeltaX() {
		return dDeltaX;
	}

	public int getRadius() {
		return nRadius;
	}

	public void setRadius(int n) {
		nRadius = n;

	}

	public Dimension getDim() {
		return dim;
	}

	public void setDim(Dimension dim) {
		this.dim = dim;
	}

	public Point getCenter() {
		return pntCenter;
	}

	public void setCenter(Point pntParam) {
		pntCenter = pntParam;
	}


	public void setYcoord(int nValue, int nIndex) {
		nYCoords[nIndex] = nValue;
	}

	public void setXcoord(int nValue, int nIndex) {
		nXCoords[nIndex] = nValue;
	}
	
	
	public int getYcoord( int nIndex) {
		return nYCoords[nIndex];// = nValue;
	}

	public int getXcoord( int nIndex) {
		return nXCoords[nIndex];// = nValue;
	}
	
	

	public int[] getXcoords() {
		return nXCoords;
	}

	public int[] getYcoords() {
		return nYCoords;
	}
	
	
	public void setXcoords( int[] nCoords) {
		 nXCoords = nCoords;
	}

	public void setYcoords(int[] nCoords) {
		 nYCoords =nCoords;
	}

	protected double hypot(double dX, double dY) {
		return Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
	}

	
	//utility function to convert from cartesian to polar
	//since it's much easier to describe a sprite as a list of cartesean points
	//sprites (except Asteroid) should use the cartesean technique to describe the coordinates
	//see Falcon or Bullet constructor for examples
	protected double[] convertToPolarDegs(ArrayList<Point> pntPoints) {

		//ArrayList<Tuple<Double,Double>> dblCoords = new ArrayList<Tuple<Double,Double>>();
		double[] dDegs = new double[pntPoints.size()];

		int nC = 0;
		for (Point pnt : pntPoints) {
			dDegs[nC++]=(Math.toDegrees(Math.atan2(pnt.y, pnt.x))) * Math.PI / 180 ;
		}
		return dDegs;
	}
	//utility function to convert to polar
	protected double[] convertToPolarLens(ArrayList<Point> pntPoints) {

		double[] dLens = new double[pntPoints.size()];

		//determine the largest hypotenuse
		double dL = 0;
		for (Point pnt : pntPoints)
			if (hypot(pnt.x, pnt.y) > dL)
				dL = hypot(pnt.x, pnt.y);

		int nC = 0;
		for (Point pnt : pntPoints) {
			if (pnt.x == 0 && pnt.y > 0) {
				dLens[nC] = hypot(pnt.x, pnt.y) / dL;
			} else if (pnt.x < 0 && pnt.y > 0) {
				dLens[nC] = hypot(pnt.x, pnt.y) / dL;
			} else {
				dLens[nC] = hypot(pnt.x, pnt.y) / dL;
			}
			nC++;
		}

		// holds <thetas, lens>
		return dLens;

	}

	protected void assignPolarPoints(ArrayList<Point> pntCs) {
		setDegrees(convertToPolarDegs(pntCs));
		setLengths(convertToPolarLens(pntCs));

	}
	




	
    public void draw(Graphics g) {
        nXCoords = new int[dDegrees.length];
        nYCoords = new int[dDegrees.length];
        //need this as well
        pntCoords = new Point[dDegrees.length];

        for (int nC = 0; nC < dDegrees.length; nC++) {
            nXCoords[nC] =    (int) (getCenter().x + getRadius() 
                            * dLengths[nC] 
                            * Math.sin(Math.toRadians(getOrientation()) + dDegrees[nC]));
            nYCoords[nC] =    (int) (getCenter().y - getRadius()
                            * dLengths[nC]
                            * Math.cos(Math.toRadians(getOrientation()) + dDegrees[nC]));
            
            
            //need this line of code to create the points which we will need for debris
            pntCoords[nC] = new Point(nXCoords[nC], nYCoords[nC]);
        }
        
        g.setColor(getColor());
        g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);
    }
    

	public Point[] getObjectPoints() {
		return pntCoords;
	}
	
	public void setObjectPoints(Point[] pntPs) {
		 pntCoords = pntPs;
	}
	
	public void setObjectPoint(Point pnt, int nIndex) {
		 pntCoords[nIndex] = pnt;
	}



	public void expire() {
	}

	public int getFadeValue() {
		return nFade;
	}

	public void setFadeValue(int n) {
		nFade = n;
	}

}
