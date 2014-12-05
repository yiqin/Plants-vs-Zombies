package edu.uchicago.cs.java.finalproject.game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;
import java.util.concurrent.CopyOnWriteArrayList;

import edu.uchicago.cs.java.finalproject.game.model.*;
import edu.uchicago.cs.java.finalproject.controller.Game;

public class GamePanel extends Panel {
	
	// ==============================================================
	// FIELDS 
	// ============================================================== 
	 
	// The following "off" vars are used for the off-screen double-bufferred image. 
	private Dimension dimOff;
	private Image imgOff;
	private Graphics grpOff;
	
	private GameFrame gmf;
	public static Font fnt = new Font("Times", Font.BOLD, 20);  // set font size.
    public static Font fntBig = new Font("Times", Font.BOLD + Font.ITALIC, 36);
	private FontMetrics fmt; 
	private int nFontWidth;
	private int nFontHeight;
	private String strDisplay = "";


	// ==============================================================
	// CONSTRUCTOR 
	// ==============================================================
	
	public GamePanel(Dimension dim){
        System.out.println("Create Game Panel.");

	    gmf = new GameFrame();
		gmf.getContentPane().add(this);
		gmf.pack();
		initView();
		
		gmf.setSize(dim);
		gmf.setTitle("Plants vs. Zombies");
		gmf.setResizable(false);
		gmf.setVisible(true);
		this.setFocusable(true);
	}
	
	
	// ==============================================================
	// METHODS 
	// ==============================================================
	
	private void drawScore(Graphics g) {
		g.setColor(Color.white);
		g.setFont(fnt);
		if (CommandCenter.getScore() != 0) {
			g.drawString("SCORE :  " + CommandCenter.getScore(), nFontWidth, nFontHeight);
		} else {
			g.drawString("NO SCORE", nFontWidth, nFontHeight);
		}
	}

     private void drawSunCredit(Graphics g){
         g.setColor(Color.white);
         g.setFont(fnt);
         if (CommandCenter.getSunCredit() != 0) {
             g.drawString("SUN CREDITS :  " + CommandCenter.getSunCredit(), nFontWidth, nFontHeight*2+5);
         } else {
             g.drawString("NO SUN CREDITS", nFontWidth, nFontHeight*2+5);
         }
     }

    private void regularPeashooterInstruction(Graphics g){
        g.setColor(Color.yellow);
        g.setFont(fnt);
        g.drawString("100 SUN CREDITS", 150-20, Game.DIM.height-50);
    }

    private void icePeashooterInstruction(Graphics g){
        g.setColor(Color.yellow);
        g.setFont(fnt);
        g.drawString("200 SUN CREDITS", 410-20-10, Game.DIM.height-50);
    }

	@SuppressWarnings("unchecked")
	public void update(Graphics g) {
		if (grpOff == null || Game.DIM.width != dimOff.width
				|| Game.DIM.height != dimOff.height) {
			dimOff = Game.DIM;
			imgOff = createImage(Game.DIM.width, Game.DIM.height);
			grpOff = imgOff.getGraphics();
		}
        // Change background here.
		// Fill in background with black.

        // System.out.println("updating...");
		grpOff.setColor(Color.black);
		grpOff.fillRect(0, 0, Game.DIM.width, Game.DIM.height);



		drawScore(grpOff);
        drawSunCredit(grpOff);



		if (!CommandCenter.isPlaying()) {

            if(CommandCenter.getTutorialPage()!=5 && CommandCenter.getCheckTutorialOnlyOneTime()){
                displayTutorialOnScreen();
            }
            else {
                displayTextOnScreen();
            }

		} else if (CommandCenter.isPaused()) {
			strDisplay = "Game Paused";
			grpOff.drawString(strDisplay,
					(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4);
		}
		
		//playing and not paused!
		else {
            grpOff.setColor(new Color(66, 72, 70));
            grpOff.fillRect(0, Game.DIM.height-200, Game.DIM.width, 200);

            regularPeashooterInstruction(grpOff);
            icePeashooterInstruction(grpOff);

            grpOff.setColor(Color.white);
            grpOff.setFont(fnt);
            grpOff.drawString(CommandCenter.gameGuide, 590, Game.DIM.height-150);


            grpOff.setColor(new Color(71, 86, 101));
            grpOff.fillRect(0, 60, 20, 500);


			//draw them in decreasing level of importance
			//friends will be on top layer and debris on the bottom
			iterateMovables(grpOff,
			           CommandCenter.movFloaters, 
			           CommandCenter.movFoes,
			           CommandCenter.movFriends,
                       CommandCenter.movSun,
                       CommandCenter.movCandidate,
                       CommandCenter.movTemp,
                       CommandCenter.movPlants,
                       CommandCenter.movDebris,
                       CommandCenter.movLevelInstruction);

			if (CommandCenter.isGameOver()) {
				CommandCenter.setPlaying(false);
				//bPlaying = false;
			}
		}
		//draw the double-Buffered Image to the graphics context of the panel
		g.drawImage(imgOff, 0, 0, this);
	}

	//for each movable array, process it.
	private void iterateMovables(Graphics g, CopyOnWriteArrayList<Movable>...movMovz){
		
		for (CopyOnWriteArrayList<Movable> movMovs : movMovz) {
			for (Movable mov : movMovs) {

				mov.move();
				mov.draw(g);
				mov.fadeInOut();
				mov.expire();   // call the special animation.... here.
			}
		}
		
	}
	
	private void initView() {
		Graphics g = getGraphics();			// get the graphics context for the panel
		g.setFont(fnt);						// take care of some simple font stuff
		fmt = g.getFontMetrics();
		nFontWidth = fmt.getMaxAdvance();
		nFontHeight = fmt.getHeight();
		g.setFont(fntBig);					// set font info
	}
	
	// This method draws some text to the middle of the screen before/after a game
    // Instruction.
	private void displayTextOnScreen() {
        CommandCenter.movCandidate.clear();

        grpOff.setColor(Color.red);
        strDisplay = "PLANTS VS ZOMBIES";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2-10, Game.DIM.height / 4);

        grpOff.setColor(Color.white);
		strDisplay = "INSTRUCTION";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 40);

        strDisplay = "a mob of fun-loving zombies is about to invade your home";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
                        + nFontHeight + 80);

		strDisplay = "use the mouse to collect suns";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 120);

		strDisplay = "use suns to plant peashooter to defense";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 160);

		strDisplay = "'S' to Start";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 200);

		strDisplay = "'P' to Pause";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 240);

		strDisplay = "'Q' to Quit";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 280);


        CommandCenter.movCandidate.add(new CandidateRegularPeashooter(300,650));
        CommandCenter.movCandidate.add(new CandidateIcePeashooter(450,650));
        CommandCenter.movCandidate.add(new Sun(600,650));

        Zombie tempZombie = new Zombie(650);
        tempZombie.setCenter(new Point(750, 650));
        CommandCenter.movCandidate.add(tempZombie);

        IronZombie tempIronZombie = new IronZombie(650);
        tempIronZombie.setCenter(new Point(900, 650));
        CommandCenter.movCandidate.add(tempIronZombie);

        CrazyZombie tempCrayZombie = new CrazyZombie(Game.DIM.height / 4);
        tempCrayZombie.setCenter(new Point(700+17, 180));
        CommandCenter.movCandidate.add(tempCrayZombie);

        iterateMovables(grpOff,
                CommandCenter.movCandidate);
	}

    private void displayTutorialOnScreen() {
        CommandCenter.movCandidate.clear();

        grpOff.setColor(Color.red);
        strDisplay = "PLANTS VS ZOMBIES";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2-10, Game.DIM.height / 4);

        grpOff.setColor(Color.white);


        strDisplay = "'S' to Continue";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
                        + nFontHeight + 80);

        CrazyZombie tempCrayZombie = new CrazyZombie(Game.DIM.height / 4);
        tempCrayZombie.setCenter(new Point(700+17, 180));
        CommandCenter.movCandidate.add(tempCrayZombie);

        switch (CommandCenter.getTutorialPage()) {
            case 4:
                tutorialPageTwo();
                break;
            case 3:
                tutorialPageThree();
                break;
            case 2:
                tutorialPageFour();
                break;
            case 1:
                tutorialPageFive();
                break;
            case 0:
                tutorialPageSix();
            default:
                break;
        }

        iterateMovables(grpOff,
                CommandCenter.movCandidate);
    }

    private void tutorialPageTwo(){
        strDisplay = "TUTORIAL - I";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 40);

        CommandCenter.movCandidate.add(new Sun(600,400));

        strDisplay = "SUN";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 260);

        strDisplay = "planting a new peashooter costs sun credits";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 320);
        strDisplay = "one sun has 50 sun credits";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 360);
        strDisplay = "use the mouse to click suns to collect them";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 400);
    }

    private void tutorialPageThree(){
        strDisplay = "TUTORIAL - II";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 40);
        CommandCenter.movCandidate.add(new CandidateRegularPeashooter(600,400));

        strDisplay = "PEASHOOTER";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 260);

        strDisplay = "use the mouse to drag and drop a new plant";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 320);
        strDisplay = "two types of plants: regular peashooter and ice peashooter";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 360);
        strDisplay = "peashooters issue bullets";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 400);
        strDisplay = "ice bullets from ice peashooters will frozen zombies";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 440);
    }

    private void tutorialPageFour(){
        strDisplay = "TUTORIAL - III";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 40);
        Zombie tempZombie = new Zombie(400);
        tempZombie.setCenter(new Point(600, 400));


        strDisplay = "ZOMBIE";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 260);

        CommandCenter.movCandidate.add(tempZombie);
        strDisplay = "three types of zombies: regular zombie, iron zombie and crazy zombie";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 320);
        strDisplay = "the iron zombie is coming with a shield (more bullets are needed to kill it)";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 360);

        strDisplay = "the crazy zombie is running, rather than walking";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 400);


    }

    private void tutorialPageFive(){
        strDisplay = "TUTORIAL - IV";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 40);
        strDisplay = "when any zombie reach your front door, game over";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 160);

        strDisplay = "do you see the area? (the left end of the screen)";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 200);

        grpOff.setColor(getRandomColor());
        grpOff.fillRect(0, 60, 20, 500);
    }

    public Color getRandomColor(){
        int nColorID = Game.R.nextInt(5);

        Color col = null;
        if(nColorID < 2){
            col = Color.white;
        }
        else if(nColorID == 2){
            col = Color.yellow;
        }
        else if(nColorID == 3){
            col = Color.red;
        }
        else if(nColorID == 4){
            col = new Color(0,255,255);			// 'aqua' color
        }
        return col;
    }

    private void tutorialPageSix(){
        strDisplay = "congratulations, you finish the tutorial";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 40);

        strDisplay = "the game is inspired by plants vs. zombies | PopCap Games";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 160);

        strDisplay = "http://www.popcap.com/plants-vs-zombies-1";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 200);

        strDisplay = "thanks to Adam Gerber, Shashank Sharma, Ali Alkhafaji, Varun Kaushal";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 260);

        strDisplay = "created by Yi Qin";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 320);
        strDisplay = "yi's page: http://www.yiqin.info/";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 360);
        strDisplay = "bitbucket: https://bitbucket.org/qin23/profinal/overview";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 400);
    }
	
	public GameFrame getFrm() {return this.gmf;}
	public void setFrm(GameFrame frm) {this.gmf = frm;}	
}