package edu.uchicago.cs.java.finalproject.controller;

import sun.audio.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.sampled.Clip;

import edu.uchicago.cs.java.finalproject.game.model.*;
import edu.uchicago.cs.java.finalproject.game.view.*;
import edu.uchicago.cs.java.finalproject.sounds.Sound;

// ===============================================
// == This Game class is the CONTROLLER
// ===============================================

public class Game implements Runnable, KeyListener, MouseListener, MouseMotionListener {

	// ===============================================
	// FIELDS
	// ===============================================

    public final static int SCREEN_WIDTH = 1200;
    public final static int SCREEN_HEIGHT = 800;

	public static final Dimension DIM = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT); //the dimension of the game.
	private GamePanel gmpPanel;
	public static Random R = new Random();
	public final static int ANI_DELAY = 45; // milliseconds between screen
											// updates (animation)
	private Thread thrAnim;
	private int nLevel = 1;
	private static int nTick = 0;
	private ArrayList<Tuple> tupMarkForRemovals;
	private ArrayList<Tuple> tupMarkForAdds;
	private boolean bMuted = true;

    // Handle sunflowers
    private ArrayList<Tuple> tupMarkForRemovalsFromMouseSelect;


    // ASCII value
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

	public Game() throws IOException {

        System.out.println("Start game");

		gmpPanel = new GamePanel(DIM);
		gmpPanel.addKeyListener(this);
        gmpPanel.addMouseListener(this);
        gmpPanel.addMouseMotionListener(this);

		clpThrust = Sound.clipForLoopFactory("whitenoise.wav");
		clpMusicBackground = Sound.clipForLoopFactory("music-background.wav");

        // System.out.println(HTTPRequest.get("https://javafinalpro.firebaseio.com/.json"));
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

        System.out.println("Updating in Game.....");

		// lower this thread's priority; let the "main" aka 'Event Dispatch'
		// thread do what it needs to do first
		thrAnim.setPriority(Thread.MIN_PRIORITY);

		// and get the current time
		long lStartTime = System.currentTimeMillis();

		// this thread animates the scene
		while (Thread.currentThread() == thrAnim) {
			tick();
			spawnNewShipFloater();
            ///
            ///
            ///
            ///
            ///
            ///
            ///
            generateCandidatePlants();


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


    // Iterator<>  implement


	private void checkCollisions() {

        // System.out.println("Check Collisions..............");

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
                if (pntFriendCenter.distance(pntFoeCenter) < (nFriendRadiux + nFoeRadiux-20)) {

                    //falcon
                    // Check the flight. If the falcon is not protected. it will die....
                    if ((movFriend instanceof RegularBullet) ){

                        tupMarkForRemovals.add(new Tuple(CommandCenter.movFriends, movFriend));

                        int explodeX = (int)movFriend.getCenter().getX();
                        int explodeY = (int)movFriend.getCenter().getY();

                        CommandCenter.movDebris.add(new ExplodingRegularBullet(new Point(explodeX+30, explodeY)));
                            // CommandCenter.spawnFalcon(false);
                            // killFoe(movFoe);
                        // killlllll
                        tupMarkForRemovals.add(new Tuple(CommandCenter.movFoes, movFoe));

                    }
                    //not the falcon
                    //

                    //explode/remove foe
                }//end if
            }//end inner for
        }//end outer for

        for (Tuple tup : tupMarkForRemovals)
            tup.removeMovable();

        System.gc();

        /*
		for (Movable movFriend : CommandCenter.movFriends) {
			for (Movable movFoe : CommandCenter.movFoes) {

				pntFriendCenter = movFriend.getCenter();
				pntFoeCenter = movFoe.getCenter();
				nFriendRadiux = movFriend.getRadius();
				nFoeRadiux = movFoe.getRadius();

				//detect collision
				if (pntFriendCenter.distance(pntFoeCenter) < (nFriendRadiux + nFoeRadiux)) {

					//falcon
                    // Check the flight. If the falcon is not protected. it will die....
					if ((movFriend instanceof Falcon) ){
						if (!CommandCenter.getFalcon().getProtected()){
							tupMarkForRemovals.add(new Tuple(CommandCenter.movFriends, movFriend));
							CommandCenter.spawnFalcon(false);
							killFoe(movFoe);
						}
					}
					//not the falcon
                    //
					else {
						tupMarkForRemovals.add(new Tuple(CommandCenter.movFriends, movFriend));
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



        // Find collisions first.
        // We create an arrayList, and then we remove them.


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
		*/
	}//end meth


    // When the bullet hit the Asteroid, or any collision.
    // Control the animation
    /*
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
    */
    // Remove sunflower
    private void checkMouseClicked(MouseEvent e) {
        tupMarkForRemovalsFromMouseSelect = new ArrayList<Tuple>();

        Point pntSumCenter, pntFoeCenter;
        int nFriendRadiux, nFoeRadiux;

        for (Movable movSun : CommandCenter.movSun) {

            pntSumCenter = movSun.getCenter();
            pntFoeCenter = new Point(e.getX(), e.getY());
            nFriendRadiux = movSun.getRadius();
            nFoeRadiux = 10;

            //detect collision
            if (pntSumCenter.distance(pntFoeCenter) < (nFriendRadiux + nFoeRadiux)) {
                if ((movSun instanceof Sun) ){
                    tupMarkForRemovalsFromMouseSelect.add(new Tuple(CommandCenter.movSun, movSun));
                    CommandCenter.setSunCredit(CommandCenter.getSunCredit() + ((Sun) movSun).credit);
                    Sound.playSound("pacman_eatghost.wav");
                }
            }//end if
        }//end outer for


        //remove these objects from their appropriate ArrayLists
        //this happens after the above iterations are done
        for (Tuple tup : tupMarkForRemovalsFromMouseSelect)
            tup.removeMovable();

        //call garbage collection
        System.gc();
    }

    private void checkMousePress(MouseEvent e){

        Point pntSumCenter, pntFoeCenter;
        int nFriendRadiux, nFoeRadiux;

        for (Movable movSun : CommandCenter.movCandidate) {

            pntSumCenter = movSun.getCenter();
            pntFoeCenter = new Point(e.getX(), e.getY());
            nFriendRadiux = movSun.getRadius();
            nFoeRadiux = 10;

            //detect collision
            if (pntSumCenter.distance(pntFoeCenter) < (nFriendRadiux + nFoeRadiux)) {
                if ((movSun instanceof Peashooter) ){


                    CommandCenter.setPlant(new Peashooter(e.getX()-50,e.getY()-50),((Peashooter) movSun).typeIndicator);



                    // Sound.playSound("pacman_eatghost.wav");
                }
            }//end if
        }//end outer for

    }



    // ===============================================
    // Random METHODS
    // ===============================================
	//some methods for timing events in the game,
	//such as the appearance of UFOs, floaters (power-ups), etc. 
	public static void tick() {
		if (nTick == Integer.MAX_VALUE)
			nTick = 0;
		else
			nTick++;

        System.out.println("Updating in Game.....");
        generateNewSun();
        generateNewZombie();
	}

	public static int getTick() {
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
        System.out.println("user presses s");

		CommandCenter.clearAll();

        // CommandCenter.initGame();
        CommandCenter.initGame(5, 6);


		CommandCenter.setLevel(0);
		CommandCenter.setPlaying(true);
		CommandCenter.setPaused(false);
		//if (!bMuted)
		   // clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
	}

    // No use anymore.....
    // Generate new Asteroid....................
	//this method spawns new asteroids
	private void spawnAsteroids(int nNum) {
		for (int nC = 0; nC < nNum; nC++) {
			//Asteroids with size of zero are big
			// CommandCenter.movFoes.add(new Asteroid(0));
		}
	}

    private static void generateNewSun(){
        int tick = getTick();
        if (tick%30 == 0){
            int tempTick = (int)(Math.random()*10);
            if (tempTick%7 == 0){
                int randomNum = (int)(Math.random()*SCREEN_WIDTH);
                CommandCenter.movSun.add(new Sun(randomNum));
            }
        }
    }

    private static void generateNewZombie(){
        int tick = getTick();
        if (tick%5 == 0){
            int tempTick = (int)(Math.random()*10);
            if (tempTick%7 == 0){
                int randomNum = (Game.R.nextInt()%4)*100+200;
                CommandCenter.movFoes.add(new Zombie(randomNum));
            }
        }
    }


    private void generateNewPeashooter(Point newPoint){
        // Need to update later.......

        if(CommandCenter.plantType==0){
            CommandCenter.movPlants.add(new RegularPeashooter(newPoint));
        }
        else if (CommandCenter.plantType==1){
            CommandCenter.movPlants.add(new IcePeashooter(newPoint));
        }


    }

    ///////////////////////////////////////
    private void generateCandidatePlants(){
        CommandCenter.movCandidate.add(new CandidateRegularPeashooter(400,600+50));
        CommandCenter.movCandidate.add(new CandidateIcePeashooter(800,600+50));
    }

	private boolean isLevelClear(){
		//if there are no more Asteroids on the screen
		
		boolean bAsteroidFree = true;
		for (Movable movFoe : CommandCenter.movFoes) {
			/*
            if (movFoe instanceof Asteroid){
				bAsteroidFree = false;
				break;
			}
			*/
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
	
	
	

	// Varargs for stopping looping-music-clips
	private static void stopLoopingSounds(Clip... clpClips) {
		for (Clip clp : clpClips) {
			clp.stop();
		}
	}







	// ===============================================
	// KEYLISTENER METHODS
	// ===============================================

    // Two methods are needed to control the fal........

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
				// CommandCenter.movFriends.add(new Bullet(fal));
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


    // ===============================================
    // FireBase METHODS
    // ===============================================

    // Two methods are needed to control the fal........



    // ===============================================
    // Mouse click METHODS
    // ===============================================

    // Two methods are needed to control the fal........
    @Override
    public void mouseClicked(MouseEvent e) {
        checkMouseClicked(e);

        System.out.println("Mouse Clicked: "+e.getX()+", "+e.getY());

    }
    // Just need these because of MouseListener implementation

    public void mouseEntered(MouseEvent arg0) {}
    public void mouseExited(MouseEvent arg0) {}



    // Create a candidate
    public void mouseReleased(MouseEvent e) {
        if (CommandCenter.isPlanting && e.getY()<550){
            generateNewPeashooter(new Point(e.getX(),e.getY()));

        }
        CommandCenter.clearMovTemp();

    }

    // Select candidate
    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse Pressed: "+e.getX()+", "+e.getY());

        checkMousePress(e);

    }

    public void mouseDragged(MouseEvent e) {
        System.out.println("Mouse Dragged: "+e.getX()+", "+e.getY());
        CommandCenter.setPlantPosition(new Point(e.getX(),e.getY()));



    }


    public void mouseMoved(MouseEvent e){

    }

}

// ===============================================
// ==A tuple takes a reference to an ArrayList and a reference to a Movable
// This class is used in the collision detection method, to avoid mutating the array list while we are iterating
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
