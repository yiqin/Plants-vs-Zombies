package edu.uchicago.cs.java.finalproject.controller;

import sun.audio.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.sampled.Clip;

import edu.uchicago.cs.java.finalproject.game.model.*;
import edu.uchicago.cs.java.finalproject.game.view.*;
import edu.uchicago.cs.java.finalproject.sounds.Sound;

// ===============================================
// == This Game class is the CONTROLLER
// ===============================================

public class Game implements Runnable, KeyListener {

	// ===============================================
	// FIELDS
	// ===============================================

    private String strDisplay = "";

	public static final Dimension DIM = new Dimension(1100, 900); //the dimension of the game.
	private GamePanel gmpPanel;
	public static Random R = new Random();
	public final static int ANI_DELAY = 45; // milliseconds between screen
											// updates (animation)
	private Thread thrAnim;
	private int nLevel = 1;
	private int nTick = 0;
	private ArrayList<Tuple> tupMarkForRemovals;
	private ArrayList<Tuple> tupMarkForAdds;
	private boolean bMuted = true;
    private OffScreenImage offScreenImage;
	

	private final int PAUSE = 80, // p key
			QUIT = 81, // q key
			LEFT = 37, // rotate left; left arrow
			RIGHT = 39, // rotate right; right arrow
			UP = 38, // thrust; up arrow
			START = 83, // s key
			FIRE = 32, // space key
			MUTE = 77, // m-key mute

	// for possible future use
	// HYPER = 68, 					// d key
	// SHIELD = 65, 				// a key arrow
	// NUM_ENTER = 10, 				// hyp
	 SPECIAL = 70; 					// fire special weapon;  F key

	private Clip clpThrust;
	private Clip clpMusicBackground;

	private static final int SPAWN_NEW_SHIP_FLOATER = 1200;



	// ===============================================
	// ==CONSTRUCTOR
	// ===============================================

	public Game() {

        offScreenImage = new OffScreenImage();
		gmpPanel = new GamePanel(DIM,offScreenImage);
		gmpPanel.addKeyListener(this);

		clpThrust = Sound.clipForLoopFactory("whitenoise.wav");
		clpMusicBackground = Sound.clipForLoopFactory("music-background.wav");
	

	}

	// ===============================================
	// ==METHODS
	// ===============================================

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() { // uses the Event dispatch thread from Java 5 (refactored)
					public void run() {
						try {
							Game game = new Game(); // construct itself
							game.fireUpAnimThread();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	private void fireUpAnimThread() { // called initially
		if (thrAnim == null) {
			thrAnim = new Thread(this); // pass the thread a runnable object (this)
			thrAnim.start();
		}
	}

	// implements runnable - must have run method
	public void run() {

		// lower this thread's priority; let the "main" aka 'Event Dispatch'
		// thread do what it needs to do first
		thrAnim.setPriority(Thread.MIN_PRIORITY);

		// and get the current time
		long lStartTime = System.currentTimeMillis();

		// this thread animates the scene
		while (Thread.currentThread() == thrAnim) {
			tick();
			spawnNewShipFloater();

            drawOffScreen();

			gmpPanel.update(gmpPanel.getGraphics()); // update takes the graphics context we must 
														// surround the sleep() in a try/catch block
														// this simply controls delay time between 
														// the frames of the animation

			//this might be a good place to check for collisions
			checkCollisions();
			//this might be a god place to check if the level is clear (no more foes)
			//if the level is clear then spawn some big asteroids -- the number of asteroids 
			//should increase with the level. 
			checkNewLevel();

			try {
				// The total amount of time is guaranteed to be at least ANI_DELAY long.  If processing (update) 
				// between frames takes longer than ANI_DELAY, then the difference between lStartTime - 
				// System.currentTimeMillis() will be negative, then zero will be the sleep time
				lStartTime += ANI_DELAY;
				Thread.sleep(Math.max(0,
						lStartTime - System.currentTimeMillis()));
			} catch (InterruptedException e) {
				// just skip this frame -- no big deal
				continue;
			}
		} // end while
	} // end run

	private void checkCollisions() {

		
		//@formatter:off
		//for each friend in movFriends
			//for each foe in movFoes
				//if the distance between the two centers is less than the sum of their radii
					//mark it for removal
		
		//for each mark-for-removal
			//remove it
		//for each mark-for-add
			//add it
		//@formatter:on
		
		//we use this ArrayList to keep pairs of movMovables/movTarget for either
		//removal or insertion into our arrayLists later on
		tupMarkForRemovals = new ArrayList<Tuple>();
		tupMarkForAdds = new ArrayList<Tuple>();

		Point pntFriendCenter, pntFoeCenter;
		int nFriendRadiux, nFoeRadiux;

		for (Movable movFriend : CommandCenter.movFriends) {
			for (Movable movFoe : CommandCenter.movFoes) {

				pntFriendCenter = movFriend.getCenter();
				pntFoeCenter = movFoe.getCenter();
				nFriendRadiux = movFriend.getRadius();
				nFoeRadiux = movFoe.getRadius();

				//detect collision
				if (pntFriendCenter.distance(pntFoeCenter) < (nFriendRadiux + nFoeRadiux)) {

					//falcon
					if ((movFriend instanceof Falcon) ){
						if (!CommandCenter.getFalcon().getProtected()){
							tupMarkForRemovals.add(new Tuple(CommandCenter.movFriends, movFriend));
							CommandCenter.spawnFalcon(false);
                            createDebris((Sprite)movFoe, tupMarkForAdds);
                            createDebris((Sprite)movFriend, tupMarkForAdds);

							killFoe(movFoe);
						}
					}
					//not the falcon
					else {
						tupMarkForRemovals.add(new Tuple(CommandCenter.movFriends, movFriend));
                        createDebris((Sprite)movFoe, tupMarkForAdds);
						killFoe(movFoe);
					}//end else 

					//explode/remove foe
					
					
				
				}//end if 
			}//end inner for
		}//end outer for


		//check for collisions between falcon and floaters
		if (CommandCenter.getFalcon() != null){
			Point pntFalCenter = CommandCenter.getFalcon().getCenter();
			int nFalRadiux = CommandCenter.getFalcon().getRadius();
			Point pntFloaterCenter;
			int nFloaterRadiux;
			
			for (Movable movFloater : CommandCenter.movFloaters) {
				pntFloaterCenter = movFloater.getCenter();
				nFloaterRadiux = movFloater.getRadius();
	
				//detect collision
				if (pntFalCenter.distance(pntFloaterCenter) < (nFalRadiux + nFloaterRadiux)) {
	
					
					tupMarkForRemovals.add(new Tuple(CommandCenter.movFloaters, movFloater));
					Sound.playSound("pacman_eatghost.wav");
	
				}//end if 
			}//end inner for
		}//end if not null
		
		//remove these objects from their appropriate ArrayLists
		//this happens after the above iterations are done
		for (Tuple tup : tupMarkForRemovals) 
			tup.removeMovable();
		
		//add these objects to their appropriate ArrayLists
		//this happens after the above iterations are done
		for (Tuple tup : tupMarkForAdds) 
			tup.addMovable();

		//call garbage collection
		System.gc();
		
	}//end meth


    private void createDebris(Sprite spr, ArrayList tupTups){

        Point[] pntCs = spr.getObjectPoints();
        for (int nC = 0; nC < pntCs.length -1 ; nC++) {
            tupTups.add(new Tuple(CommandCenter.movDebris, new Debris(spr, pntCs[nC], pntCs[nC+1])));
        }
        tupTups.add(new Tuple(CommandCenter.movDebris, new Debris(spr, pntCs[0], pntCs[pntCs.length-1])));

    }

	private void killFoe(Movable movFoe) {
		
		if (movFoe instanceof Asteroid){

			//we know this is an Asteroid, so we can cast without threat of ClassCastException
			Asteroid astExploded = (Asteroid)movFoe;
			//big asteroid 
			if(astExploded.getSize() == 0){
				//spawn two medium Asteroids
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
				
			} 
			//medium size aseroid exploded
			else if(astExploded.getSize() == 1){
				//spawn three small Asteroids
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
			}
			//remove the original Foe	
			tupMarkForRemovals.add(new Tuple(CommandCenter.movFoes, movFoe));
		
			
		} 
		//not an asteroid
		else {
			//remove the original Foe
			tupMarkForRemovals.add(new Tuple(CommandCenter.movFoes, movFoe));
		}
		
		
		

		
		
		
		
	}

	//some methods for timing events in the game,
	//such as the appearance of UFOs, floaters (power-ups), etc. 
	public void tick() {
		if (nTick == Integer.MAX_VALUE)
			nTick = 0;
		else
			nTick++;
	}

	public int getTick() {
		return nTick;
	}

	private void spawnNewShipFloater() {
		//make the appearance of power-up dependent upon ticks and levels
		//the higher the level the more frequent the appearance
		if (nTick % (SPAWN_NEW_SHIP_FLOATER - nLevel * 7) == 0) {
			CommandCenter.movFloaters.add(new NewShipFloater());
		}
	}

	// Called when user presses 's'
	private void startGame() {
		CommandCenter.clearAll();
		CommandCenter.initGame();
		CommandCenter.setLevel(0);
		CommandCenter.setPlaying(true);
		CommandCenter.setPaused(false);
		//if (!bMuted)
		   // clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
	}

	//this method spawns new asteroids
	private void spawnAsteroids(int nNum) {
		for (int nC = 0; nC < nNum; nC++) {
			//Asteroids with size of zero are big
			CommandCenter.movFoes.add(new Asteroid(0));
		}
	}
	
	
	private boolean isLevelClear(){
		//if there are no more Asteroids on the screen
		
		boolean bAsteroidFree = true;
		for (Movable movFoe : CommandCenter.movFoes) {
			if (movFoe instanceof Asteroid){
				bAsteroidFree = false;
				break;
			}
		}
		
		return bAsteroidFree;

		
	}
	
	private void checkNewLevel(){
		
		if (isLevelClear() ){
			if (CommandCenter.getFalcon() !=null)
				CommandCenter.getFalcon().setProtected(true);
			
			spawnAsteroids(CommandCenter.getLevel() + 2);
			CommandCenter.setLevel(CommandCenter.getLevel() + 1);

		}
	}


    public void drawOffScreen() {
        if (offScreenImage.getGrpOff() == null) {
            offScreenImage.reset();
        }

        Graphics grpOff = offScreenImage.getGrpOff();
        // Fill in background with black.
        grpOff.setColor(Color.black);
        grpOff.fillRect(0, 0, Game.DIM.width, Game.DIM.height);

        drawScore(grpOff);

        if (!CommandCenter.isPlaying()) {
            displayTextOnScreen();
        } else if (CommandCenter.isPaused()) {
            strDisplay = "Game Paused";
            grpOff.drawString(strDisplay,
                    (Game.DIM.width - offScreenImage.getFmt().stringWidth(strDisplay)) / 2, Game.DIM.height / 4);
        }

        //playing and not paused!
        else {

            //draw them in decreasing level of importance
            //friends will be on top layer and debris on the bottom
            iterateMovables(grpOff,
                    CommandCenter.movDebris,
                    CommandCenter.movFloaters,
                    CommandCenter.movFoes,
                    CommandCenter.movFriends);


            drawNumberShipsLeft(grpOff);
            if (CommandCenter.isGameOver()) {
                CommandCenter.setPlaying(false);
                //bPlaying = false;
            }
        }

        //when we call repaint, repaint calls update(g)
        gmpPanel.repaint();
    }






    //for each movable array, process it.
    private void iterateMovables(Graphics g, CopyOnWriteArrayList<Movable>...movMovz){

        for (CopyOnWriteArrayList<Movable> movMovs : movMovz) {
            for (Movable mov : movMovs) {

                mov.move();
                mov.draw(g);
                mov.fadeInOut();
                mov.expire();
            }
        }

    }


    //offscreen
    // Draw the number of falcons left on the bottom-right of the screen.
    private void drawNumberShipsLeft(Graphics g) {
        Falcon fal = CommandCenter.getFalcon();
        double[] dLens = fal.getLengths();
        int nLen = fal.getDegrees().length;
        Point[] pntMs = new Point[nLen];
        int[] nXs = new int[nLen];
        int[] nYs = new int[nLen];

        //convert to cartesean points
        for (int nC = 0; nC < nLen; nC++) {
            pntMs[nC] = new Point((int) (10 * dLens[nC] * Math.sin(Math
                    .toRadians(90) + fal.getDegrees()[nC])),
                    (int) (10 * dLens[nC] * Math.cos(Math.toRadians(90)
                            + fal.getDegrees()[nC])));
        }

        //set the color to white
        g.setColor(Color.white);
        //for each falcon left (not including the one that is playing)
        for (int nD = 1; nD < CommandCenter.getNumFalcons(); nD++) {
            //create x and y values for the objects to the bottom right using cartesean points again
            for (int nC = 0; nC < fal.getDegrees().length; nC++) {
                nXs[nC] = pntMs[nC].x + Game.DIM.width - (20 * nD);
                nYs[nC] = pntMs[nC].y + Game.DIM.height - 40;
            }
            g.drawPolygon(nXs, nYs, nLen);
        }
    }



    //move to Game and pass in the offScreenImage
    // This method draws some text to the middle of the screen before/after a game
    private void displayTextOnScreen() {

        Graphics grpOff = offScreenImage.getGrpOff();
        strDisplay = "GAME OVER";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - offScreenImage.getFmt().stringWidth(strDisplay)) / 2, Game.DIM.height / 4);

        strDisplay = "use the arrow keys to turn and thrust";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - offScreenImage.getFmt().stringWidth(strDisplay)) / 2, Game.DIM.height / 4
                + offScreenImage.getFontHeight() + 40);

        strDisplay = "use the space bar to fire";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - offScreenImage.getFmt().stringWidth(strDisplay)) / 2, Game.DIM.height / 4
                + offScreenImage.getFontHeight() + 80);

        strDisplay = "'S' to Start";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - offScreenImage.getFmt().stringWidth(strDisplay)) / 2, Game.DIM.height / 4
                + offScreenImage.getFontHeight() + 120);

        strDisplay = "'P' to Pause";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - offScreenImage.getFmt().stringWidth(strDisplay)) / 2, Game.DIM.height / 4
                + offScreenImage.getFontHeight() + 160);

        strDisplay = "'Q' to Quit";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - offScreenImage.getFmt().stringWidth(strDisplay)) / 2, Game.DIM.height / 4
                + offScreenImage.getFontHeight() + 200);
        strDisplay = "left pinkie on 'A' for Shield";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - offScreenImage.getFmt().stringWidth(strDisplay)) / 2, Game.DIM.height / 4
                + offScreenImage.getFontHeight() + 240);

        strDisplay = "left index finger on 'F' for Guided Missile";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - offScreenImage.getFmt().stringWidth(strDisplay)) / 2, Game.DIM.height / 4
                + offScreenImage.getFontHeight() + 280);

        strDisplay = "'Numeric-Enter' for Hyperspace";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - offScreenImage.getFmt().stringWidth(strDisplay)) / 2, Game.DIM.height / 4
                + offScreenImage.getFontHeight() + 320);
    }




    private void drawScore(Graphics g) {
        g.setColor(Color.white);
        g.setFont(offScreenImage.getFnt());
        if (CommandCenter.getScore() != 0) {
            g.drawString("SCORE :  " + CommandCenter.getScore(), offScreenImage.getFontWidth(), offScreenImage.getFontHeight());
        } else {
            g.drawString("NO SCORE", offScreenImage.getFontWidth(), offScreenImage.getFontHeight());
        }
    }
	
	

	// Varargs for stopping looping-music-clips
	private static void stopLoopingSounds(Clip... clpClips) {
		for (Clip clp : clpClips) {
			clp.stop();
		}
	}

	// ===============================================
	// KEYLISTENER METHODS
	// ===============================================

	@Override
	public void keyPressed(KeyEvent e) {
		Falcon fal = CommandCenter.getFalcon();
		int nKey = e.getKeyCode();
		// System.out.println(nKey);

		if (nKey == START && !CommandCenter.isPlaying())
			startGame();

		if (fal != null) {

			switch (nKey) {
			case PAUSE:
				CommandCenter.setPaused(!CommandCenter.isPaused());
				if (CommandCenter.isPaused())
					stopLoopingSounds(clpMusicBackground, clpThrust);
				else
					clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
				break;
			case QUIT:
				System.exit(0);
				break;
			case UP:
				fal.thrustOn();
				if (!CommandCenter.isPaused())
					clpThrust.loop(Clip.LOOP_CONTINUOUSLY);
				break;
			case LEFT:
				fal.rotateLeft();
				break;
			case RIGHT:
				fal.rotateRight();
				break;

			// possible future use
			// case KILL:
			// case SHIELD:
			// case NUM_ENTER:

			default:
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Falcon fal = CommandCenter.getFalcon();
		int nKey = e.getKeyCode();
		 System.out.println(nKey);

		if (fal != null) {
			switch (nKey) {
			case FIRE:
				CommandCenter.movFriends.add(new Bullet(fal));
				Sound.playSound("laser.wav");
				break;
				
			//special is a special weapon, current it just fires the cruise missile. 
			case SPECIAL:
				CommandCenter.movFriends.add(new Cruise(fal));
				//Sound.playSound("laser.wav");
				break;
				
			case LEFT:
				fal.stopRotating();
				break;
			case RIGHT:
				fal.stopRotating();
				break;
			case UP:
				fal.thrustOff();
				clpThrust.stop();
				break;
				
			case MUTE:
				if (!bMuted){
					stopLoopingSounds(clpMusicBackground);
					bMuted = !bMuted;
				} 
				else {
					clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
					bMuted = !bMuted;
				}
				break;
				
				
			default:
				break;
			}
		}
	}

	@Override
	// Just need it b/c of KeyListener implementation
	public void keyTyped(KeyEvent e) {
	}
	

	
}

// ===============================================
// ==A tuple takes a reference to an ArrayList and a reference to a Movable
//This class is used in the collision detection method, to avoid mutating the array list while we are iterating
// it has two public methods that either remove or add the movable from the appropriate ArrayList 
// ===============================================

class Tuple{
	//this can be any one of several CopyOnWriteArrayList<Movable>
	private CopyOnWriteArrayList<Movable> movMovs;
	//this is the target movable object to remove
	private Movable movTarget;
	
	public Tuple(CopyOnWriteArrayList<Movable> movMovs, Movable movTarget) {
		this.movMovs = movMovs;
		this.movTarget = movTarget;
	}
	
	public void removeMovable(){
		movMovs.remove(movTarget);
	}
	
	public void addMovable(){
		movMovs.add(movTarget);
	}

}
