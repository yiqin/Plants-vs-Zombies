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
	private Font fnt = new Font("Times", Font.BOLD, 20);  // set font size.
	private Font fntBig = new Font("Times", Font.BOLD + Font.ITALIC, 36);
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
		gmf.setTitle("Game Base");
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
             g.drawString("SUN CREDITS :  " + CommandCenter.getSunCredit(), nFontWidth, nFontHeight*2);
         } else {
             g.drawString("NO SUN CREDITS", nFontWidth, nFontHeight*2);
         }
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









			displayTextOnScreen();
		} else if (CommandCenter.isPaused()) {
			strDisplay = "Game Paused";
			grpOff.drawString(strDisplay,
					(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4);
		}
		
		//playing and not paused!
		else {
            grpOff.setColor(Color.white);
            grpOff.fillRect(0, Game.DIM.height-200, Game.DIM.width, 200);

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
                       CommandCenter.movDebris);

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

        grpOff.setColor(Color.red);
        strDisplay = "PLANTS VS ZOMBIES";
        grpOff.drawString(strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2-10, Game.DIM.height / 4);

        grpOff.setColor(Color.white);
		strDisplay = "GAME OVER";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4+ nFontHeight + 40);

		strDisplay = "use the mouse to collect suns";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 80);

		strDisplay = "use suns to plant peashooter";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 120);

		strDisplay = "'S' to Start";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 160);

		strDisplay = "'P' to Pause";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 200);

		strDisplay = "'Q' to Quit";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 240);

        CommandCenter.movCandidate.clear();
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
	
	public GameFrame getFrm() {return this.gmf;}
	public void setFrm(GameFrame frm) {this.gmf = frm;}	
}