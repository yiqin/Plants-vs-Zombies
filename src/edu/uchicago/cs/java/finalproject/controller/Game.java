package edu.uchicago.cs.java.finalproject.controller;

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
			START = 83, // s key


	// for possible future use
	// HYPER = 68, 					// d key
	// SHIELD = 65, 				// a key arrow
	// NUM_ENTER = 10, 				// hyp
	 SPECIAL = 70; 					// fire special weapon;  F key

	private Clip clpThrust;
	private Clip clpMusicBackground;

    public static Clip clpLevel1;

	private static final int SPAWN_NEW_SHIP_FLOATER = 1200;



	// ===============================================
	// ==CONSTRUCTOR
	// ===============================================

	public Game() throws IOException {

        clpMusicBackground = Sound.clipForLoopFactory("plants_vs_zombies.wav");
        clpLevel1 = Sound.clipForLoopFactory("level1.wav");

        System.out.println("Start game");

		gmpPanel = new GamePanel(DIM);
		gmpPanel.addKeyListener(this);
        gmpPanel.addMouseListener(this);
        gmpPanel.addMouseMotionListener(this);

		// clpThrust = Sound.clipForLoopFactory("whitenoise.wav");

        clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
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
		tupMarkForRemovals = new ArrayList<Tuple>();
		tupMarkForAdds = new ArrayList<Tuple>();

		Point pntFriendCenter, pntFoeCenter;
		int nFriendRadiux, nFoeRadiux;

        for (Movable movFriend : CommandCenter.movFriends) {

            int offset = 0;
            for (Movable movFoe : CommandCenter.movFoes) {

                pntFriendCenter = movFriend.getCenter();
                pntFoeCenter = movFoe.getCenter();
                nFriendRadiux = movFriend.getRadius();
                nFoeRadiux = movFoe.getRadius();

                //detect collision
                if (pntFriendCenter.distance(pntFoeCenter) < (nFriendRadiux + nFoeRadiux-20+offset)) {

                    if ((movFriend instanceof RegularBullet) ){
                        offset = 15;

                        tupMarkForRemovals.add(new Tuple(CommandCenter.movFriends, movFriend));

                        int explodeX = (int)movFriend.getCenter().getX();
                        int explodeY = (int)movFriend.getCenter().getY();

                        RegularBullet.bulletSoundEffect(((RegularBullet) movFriend).bulletType);
                        CommandCenter.movDebris.add(new ExplodingRegularBullet(new Point(explodeX+30, explodeY+5)));

                        killFoe(movFriend, movFoe);
                    }



                    //explode/remove foe
                }//end if
            }//end inner for
        }//end outer for

        for (Tuple tup : tupMarkForRemovals)
            tup.removeMovable();


        if (nTick % 300 == 0) {
            System.gc();
        }
	}//end meth

	private void killFoe(Movable movFriend, Movable movFoe) {
		
		if (movFoe instanceof Zombie){
			Zombie astExploded = (Zombie)movFoe;
            RegularBullet bullet = (RegularBullet)movFriend;


			if(astExploded.getSize() == 1) {
                // Remove
                tupMarkForRemovals.add(new Tuple(CommandCenter.movFoes, movFoe));
                CommandCenter.addScore(100);
            }
            else {
                // Head down
                astExploded.isHit(bullet.bulletType);
                if(astExploded.getSize()==1){
                    CommandCenter.movDebris.add(new ExplodingHead(new Point(astExploded.getCenter()), astExploded.mainColor));
                }
            }
		}
        else if (movFoe instanceof CrazyZombie){
            CrazyZombie astExploded = (CrazyZombie)movFoe;
            RegularBullet bullet = (RegularBullet)movFriend;

            if(astExploded.getSize() == 1) {
                // Remove
                tupMarkForRemovals.add(new Tuple(CommandCenter.movFoes, movFoe));
                CommandCenter.addScore(100);
            }
            else {
                astExploded.isHit(bullet.bulletType);
            }
        }

	}

    // Remove sunflower
    private void checkMouseClicked(MouseEvent e) {
        tupMarkForRemovalsFromMouseSelect = new ArrayList<Tuple>();

        Point pntSumCenter, pntFoeCenter;
        int nFriendRadiux, nFoeRadiux;

        for (Movable movSun : CommandCenter.movSun) {

            pntSumCenter = movSun.getCenter();
            pntFoeCenter = new Point(e.getX(), e.getY());
            nFriendRadiux = movSun.getRadius();
            nFoeRadiux = 20;

            //detect collision
            if (pntSumCenter.distance(pntFoeCenter) < (nFriendRadiux + nFoeRadiux)) {
                if ((movSun instanceof Sun) ){
                    tupMarkForRemovalsFromMouseSelect.add(new Tuple(CommandCenter.movSun, movSun));
                    CommandCenter.addSunCredit(((Sun) movSun).credit);
                    Sound.playSound("select.wav");
                }
            }//end if
        }//end outer for


        //remove these objects from their appropriate ArrayLists
        //this happens after the above iterations are done
        for (Tuple tup : tupMarkForRemovalsFromMouseSelect)
            tup.removeMovable();
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

                }
            }//end if
        }//end outer for

    }



    // Remove sunflower
    private boolean checkNoDuplicatePeashoooter(Point position_) {
        Point pntSumCenter, pntFoeCenter;
        int nFriendRadiux, nFoeRadiux;

        RegularPeashooter tempP = new RegularPeashooter(position_);

        for (Movable movSun : CommandCenter.movPlants) {

            pntSumCenter = movSun.getCenter();
            pntFoeCenter = new Point((int)tempP.getCenter().getX(), (int)tempP.getCenter().getY());
            nFriendRadiux = movSun.getRadius();

            nFoeRadiux = tempP.getRadius();

            //detect collision
            if (pntSumCenter.distance(pntFoeCenter) < (nFriendRadiux + nFoeRadiux)) {


                System.out.println("DUPlicate.........");

                return false;
            }//end if
        }//end outer for

        return true;
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

        // System.out.println("Updating in Game.....");
        generateNewSun();
        generateNewZombie();
        generateNewIronZombie();
        generateNewCrazyZombie();
	}

	public static int getTick() {
		return nTick;
	}

	// Called when user presses 's'
	private void startGame() {
        System.out.println("user presses s");
        stopLoopingSounds(clpMusicBackground);
        clpLevel1.loop(Clip.LOOP_CONTINUOUSLY);


		CommandCenter.clearAll();

        CommandCenter.initGame();

		CommandCenter.setPlaying(true);
		CommandCenter.setPaused(false);
		//if (!bMuted)
		   // clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
	}

    private static void generateNewSun(){
        int tick = getTick();
        if (tick%10 == 0){
            int tempTick = (int)(Math.random()*10);
            if (tempTick%7 == 0){
                int randomNum = (int)(Math.random()*SCREEN_WIDTH);
                CommandCenter.movSun.add(new Sun(randomNum));
            }
        }
    }

    private static void generateNewZombie(){
        int tick = getTick();
        if (tick%20 == 0){
            int tempTick = (int)(Math.random()*10);
            if (tempTick%7 == 0){
                int randomNum = (Game.R.nextInt()%4)*100+200;
                CommandCenter.movFoes.add(new Zombie(randomNum));
            }
        }
    }


    private static void generateNewIronZombie(){
        int tick = getTick();
        if (tick%40 == 0){
            int tempTick = (int)(Math.random()*10);
            if (tempTick%7 == 0){
                int randomNum = (Game.R.nextInt()%4)*100+200;
                CommandCenter.movFoes.add(new IronZombie(randomNum));
            }
        }
    }


    private static void generateNewCrazyZombie(){
        int tick = getTick();
        if (tick%50 == 0){
            int tempTick = (int)(Math.random()*10);
            if (tempTick%7 == 0){
                int randomNum = (Game.R.nextInt()%4)*100+200;
                CommandCenter.movFoes.add(new CrazyZombie(randomNum));
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
		/*
        if (isLevelClear() ){
			CommandCenter.setLevel(CommandCenter.getLevel() + 1);
		}
		*/
	}


    public void sIsPressed(){
        int temp = CommandCenter.getTutorialPage();
        if(temp == 5){
            CommandCenter.setTutorialing(true);
        }

        if(!CommandCenter.isPlaying() && !CommandCenter.isTutorialing()){
            startGame();
            return;
        }

        if(temp>0){
            CommandCenter.setTutorialPage((temp-1));
        }

        System.out.println(temp);


    }

	// Varargs for stopping looping-music-clips
	public static void stopLoopingSounds(Clip... clpClips) {
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
		int nKey = e.getKeyCode();
		// System.out.println(nKey);

		if (nKey == START && !CommandCenter.isPlaying())
            sIsPressed();

			switch (nKey) {
			case PAUSE:
				CommandCenter.setPaused(!CommandCenter.isPaused());
                /*
				if (CommandCenter.isPaused())
					// stopLoopingSounds(clpMusicBackground, clpThrust);
				else
					// clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
					*/
				break;
			case QUIT:
				System.exit(0);
				break;
			default:
				break;
			}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int nKey = e.getKeyCode();
		 System.out.println(nKey);
	}

	@Override
	// Just need it b/c of KeyListener implementation
	public void keyTyped(KeyEvent e) {
        
	}

    // ===============================================
    // Mouse click METHODS
    // ===============================================

    // Two methods are needed to control the fal........
    @Override
    public void mouseClicked(MouseEvent e) {
        checkMouseClicked(e);

        // System.out.println("Mouse Clicked: "+e.getX()+", "+e.getY());

    }
    // Just need these because of MouseListener implementation

    public void mouseEntered(MouseEvent arg0) {}
    public void mouseExited(MouseEvent arg0) {}



    // Create a candidate
    public void mouseReleased(MouseEvent e) {
        if (CommandCenter.isPlanting && e.getY()<550 && e.getY()> 51){

            // No duplicate
            if(checkNoDuplicatePeashoooter(new Point(e.getX(),e.getY()))){
                generateNewPeashooter(new Point(e.getX(),e.getY()));
            }



        }
        CommandCenter.clearMovTemp();

    }

    // Select candidate
    public void mousePressed(MouseEvent e) {
        // System.out.println("Mouse Pressed: "+e.getX()+", "+e.getY());

        checkMousePress(e);

    }

    public void mouseDragged(MouseEvent e) {
        // System.out.println("Mouse Dragged: "+e.getX()+", "+e.getY());
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
