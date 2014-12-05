package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

import edu.uchicago.cs.java.finalproject.controller.Game;
import edu.uchicago.cs.java.finalproject.sounds.Sound;

import javax.sound.sampled.Clip;

// I only want one Command Center and therefore this is a perfect candidate for static
// Able to get access to methods and my movMovables ArrayList from the static context.
public class CommandCenter {

    private static int nTutorialPage = 5;

	private static int nLevel;
	private static long lScore;

    private static long lSunCredit;


    private static boolean bPlaying;
    private static boolean bTutorial = false;
    private static boolean checkTutorialOnlyOneTime = true;
	private static boolean bPaused;

    private static boolean bisGameOver;


    public static String gameGuide = "Enjoy the game.";
    //
	// These ArrayLists are thread-safe
	public static CopyOnWriteArrayList<Movable> movDebris = new CopyOnWriteArrayList<Movable>();
	public static CopyOnWriteArrayList<Movable> movFriends = new CopyOnWriteArrayList<Movable>();
	public static CopyOnWriteArrayList<Movable> movFoes = new CopyOnWriteArrayList<Movable>();
	public static CopyOnWriteArrayList<Movable> movFloaters = new CopyOnWriteArrayList<Movable>();

    // Suns
    // We use ArrayList to hold all movSun...
    public static CopyOnWriteArrayList<Movable> movSun = new CopyOnWriteArrayList<Movable>();
    public static CopyOnWriteArrayList<Movable> movPlants = new CopyOnWriteArrayList<Movable>();
    public static CopyOnWriteArrayList<Movable> movCandidate = new CopyOnWriteArrayList<Movable>();
    public static CopyOnWriteArrayList<Movable> movTemp = new CopyOnWriteArrayList<Movable>();

    public static CopyOnWriteArrayList<Movable> movLevelInstruction = new CopyOnWriteArrayList<Movable>();

    public static Peashooter plant;

    public static int plantType = 0;
    public static Boolean isPlanting = false;

	// Constructor made private - static Utility class only
	private CommandCenter() {}
	
	public static void initGame(){
		setLevel(1);
		setScore(0);
        setSunCredit(0);
        setIsGameOver(false);
        CommandCenter.movCandidate.clear();
	}

	public static void clearAll(){
        System.out.println("Clean....");
		movDebris.clear();
		movFriends.clear();
		movFoes.clear();
		movFloaters.clear();

        movSun.clear();
        movCandidate.clear();
        movTemp.clear();
        movPlants.clear();
        movLevelInstruction.clear();
	}


    // Controller the process of the game.
	public static boolean isPlaying() {
		return bPlaying;
	}

	public static void setPlaying(boolean bPlaying) {
		CommandCenter.bPlaying = bPlaying;
	}

	public static boolean isPaused() {
		return bPaused;
	}

	public static void setPaused(boolean bPaused) {
		CommandCenter.bPaused = bPaused;
	}

    public static void setIsGameOver(boolean b){
        bisGameOver = b;
    }

	public static boolean isGameOver() {
		return bisGameOver;
	}

    public static void zombieCrossYard() {

        Game.stopLoopingSounds(Game.clpLevel1);
        // Game.clpLevel1 = Sound.clipForLoopFactory("level1.wav");  // I need a new sound effect here.

        // Game.clpLevel1.loop(Clip.LOOP_CONTINUOUSLY);

        Sound.playSound("gameover.wav");

        movLevelInstruction.clear();

        LevelInstruction tempLevleInstruction = new LevelInstruction(500,200, "Zombies Reach Your Front Door. Game Over.");
        tempLevleInstruction.setDeltaY(0);
        movLevelInstruction.add(tempLevleInstruction);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // your code here
                        setIsGameOver(true);

                        // Game.stopLoopingSounds(Game.clpMusicBackground);


                        Game.clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
                    }
                },
                4000  // One second.....................
        );
    }

    public static void setCheckTutorialOnlyOneTime(){
        checkTutorialOnlyOneTime = false;
    }

    public static boolean getCheckTutorialOnlyOneTime(){
        return checkTutorialOnlyOneTime;
    }



    // Game setting
	public static int getLevel() {
		return nLevel;
	}

    public static void addLevel() {
        setLevel(nLevel+1);
    }

    public static void setLevel(int n) {
        if (nLevel != n && n < 4){
            System.out.println("Change playing music.................");
            changePlayingMusic(n);
        }
        nLevel = n;
    }

    private static void changePlayingMusic(int level_){
        Game.stopLoopingSounds(Game.clpLevel1);
        if (level_ == 1){
            Game.clpLevel1 = Sound.clipForLoopFactory("level1.wav");
            playMusic();
            movLevelInstruction.add(new LevelInstruction(500,50, "Zombies are Coming!"));
        }
        else if(level_ == 2){
            Game.clpLevel1 = Sound.clipForLoopFactory("level2.wav");
            playMusic();
            movLevelInstruction.add(new LevelInstruction(500,50, "Level Two"));
        }
        else if(level_==3){
            Game.clpLevel1 = Sound.clipForLoopFactory("level3.wav");
            playMusic();
            movLevelInstruction.add(new LevelInstruction(500,50, "Level Three"));
        }
    }

    public static void playMusic(){
        // When the background music is changed, always wait for 50000
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // your code here
                        Game.clpLevel1.loop(Clip.LOOP_CONTINUOUSLY);
                    }
                },
                3000
        );
    }

	public static long getScore() {
		return lScore;
	}

	public static void setScore(long lParam) {
		lScore = lParam;
	}

    public static void addScore(long lParam) {
        lScore = lScore+lParam;

        if(lScore%300==0){
            addLevel();
        }
    }


    public static long getSunCredit() {
        return lSunCredit;
    }

    public static void setSunCredit(long lParam) {
        lSunCredit = lParam;
    }

    public static void addSunCredit(long lParam) {
        lSunCredit = lSunCredit + lParam;
    }

    public static void minusSunCredit(long lParam) {
        lSunCredit = lSunCredit-lParam;
        if(lSunCredit < 0){
            lSunCredit = 0;
        }
        System.out.println(lSunCredit);
    }

    public static void setPlant(Peashooter newPlant, int type){
        // Outside the screen
        // Be careful about this type...........
        long currentScore = getSunCredit();

        if(type == 0){
            plantType = 0;
            if(currentScore < 100){
                // System.out.println("No enough credits");
                gameGuide = "No enough credits. Collect more suns.";
                return;
            }
        }
        else if(type == 1){
            plantType = 1;
            if(currentScore<200){
                gameGuide = "No enough credits. Collect more suns.";
                return;
            }
        }
        gameGuide = "Drop the peashooter in one slot.";

        plant = new Peashooter(-200,-200);
        movTemp.clear();
        movTemp.add(plant);
        isPlanting = true;
    }

    public static void setPlantPosition(Point newPoint){
        if(isPlanting){
            plant.setCenter(newPoint);
            if(plantType == 0){
                plant.mainColor = Color.green;
            }
            else if(plantType == 1){
                plant.mainColor = Color.CYAN;
            }
        }
    }

    // no called
	public static CopyOnWriteArrayList<Movable> getMovDebris() {
		return movDebris;
	}

	public static CopyOnWriteArrayList<Movable> getMovFriends() {
		return movFriends;
	}

	public static CopyOnWriteArrayList<Movable> getMovFoes() {
		return movFoes;
	}

	public static CopyOnWriteArrayList<Movable> getMovFloaters() {
		return movFloaters;
	}

    public static CopyOnWriteArrayList<Movable> getMovSun() {
        return movSun;
    }
	public static CopyOnWriteArrayList<Movable> getMovCandidate() { return  movCandidate; }

    public static void clearMovTemp() {
        movTemp.clear();
        isPlanting = false;
    }

    public static int getTutorialPage(){
        return nTutorialPage;
    }

    public static void setTutorialPage(int n){
        nTutorialPage = n;
        if(nTutorialPage == 0){
            setTutorialing(false);
        }
    }

    public static boolean isTutorialing(){
        return bTutorial;
    }

    public static void setTutorialing(boolean n){
        bTutorial = n;
    }

}
