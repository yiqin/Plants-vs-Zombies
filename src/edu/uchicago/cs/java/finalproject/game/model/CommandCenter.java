package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

import edu.uchicago.cs.java.finalproject.sounds.Sound;

// I only want one Command Center and therefore this is a perfect candidate for static
// Able to get access to methods and my movMovables ArrayList from the static context.
public class CommandCenter {

	private static int nNumFalcon;
	private static int nLevel;
	private static long lScore;

    private static long lSunCredit;


    private static boolean bPlaying;
	private static boolean bPaused;


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

    public static Peashooter plant;

    public static int plantType = 0;
    public static Boolean isPlanting = false;


	// Constructor made private - static Utility class only
	private CommandCenter() {}
	
	public static void initGame(){
		setLevel(1);
		setScore(0);
        setSunCredit(300);
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
	
	public static boolean isGameOver() {		//if the number of falcons is zero, then game over
		/*
        if (getNumFalcons() == 0) {
			return true;
		}
		*/
		return false;
	}


    // Game setting
	public static int getLevel() {
		return nLevel;
	}

    public static void setLevel(int n) {
        nLevel = n;
    }

	public static long getScore() {
		return lScore;
	}

	public static void setScore(long lParam) {
		lScore = lParam;
	}

    public static long getSunCredit() {
        return lSunCredit;
    }

    public static void setSunCredit(long lParam) {
        lSunCredit = lParam;
    }




	public static int getNumFalcons() {
		return nNumFalcon;
	}

	public static void setNumFalcons(int nParam) {
		nNumFalcon = nParam;
	}

    public static void setPlant(Peashooter newPlant, int type){
        // Outside the screen
        // Be careful about this type...........
        plant = new Peashooter(-200,-200);
        if(type == 0){
            plantType = 0;
        }
        else if(type == 1){
            plantType = 1;
        }
        movTemp.clear();
        movTemp.add(plant);
        isPlanting = true;
    }

    public static void setPlantPosition(Point newPoint){
        plant.setCenter(newPoint);
        if(plantType == 0){
            plant.mainColor = Color.green;
        }
        else if(plantType == 1){
            plant.mainColor = Color.CYAN;
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
}
