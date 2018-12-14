package GUI.Animation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import GUI.GameSpirit;
import GUI.JBackground;
import GUI.MyFrame;
import Game.Game;
import GameObjects.GameObject;
import GameObjects.Packman;
import Geom.Point3D;
import Maps.Map;
import Path.Path;

/**
 * Class for simulating Path on Graphical user interface.
 * Moving objects along a certain path and speed.
 * @author Ofek Bader
 *
 */
public class SimulatePath extends Thread {

	public final static String version = "1.0";
	public final static String versionStage = "Beta";
	public final static int updateSpeed = 16; // milliseconds
	private boolean finished = false;
	private Game game;
	private JBackground gameUI;

	public SimulatePath(JBackground gameUI) {
		this.game = gameUI.getGame();
		this.gameUI = gameUI;

		setDaemon(true);
	}

	@Override
	public void run() {
		// setup
		GameSpirit gamespirit;
		Packman pacman;
		Path path;
		Map map = game.getMap();
		Component[] components = gameUI.getComponents();
		ArrayList<GameSpirit> gameSpirits = new ArrayList<>();

		for(int i = 0;i<components.length;i++) {
			gamespirit = (GameSpirit) components[i];
			if(gamespirit.getGameObj() instanceof Packman) {
				pacman = (Packman) gamespirit.getGameObj();
				path = pacman.getPath();
				// if path is empty then discard it.
				if(path == null || path.getPointAmount() < 2) {
					continue;
				}

				gameSpirits.add(gamespirit);
			}

		}
		// stopping condition setup
		int finish_amount = 0;
		boolean[] isFinished = new boolean[gameSpirits.size()];
		// rest of setup

		Iterator<Point3D>[] iterPath = new Iterator[gameSpirits.size()];
		Point3D[] p3d1 = new Point3D[gameSpirits.size()];
		Point3D[] p3d2 = new Point3D[gameSpirits.size()];;
		Point[] p2d1 = new Point[gameSpirits.size()],
				p2d2 = new Point[gameSpirits.size()],
				startP = new Point[gameSpirits.size()];
		double[] distance 	 = new double[gameSpirits.size()], // DISTANCE IN RAW PIXELS (RAW IS THE ORIGINAL PIXEL SIZE WITHOUT SCALE
				distMeters   = new double[gameSpirits.size()], // distance in meters
				pix2mtrRatio = new double[gameSpirits.size()], // the ratio between 1 meter and 1 pixel
				angle 		 = new double[gameSpirits.size()], // the angle / orientation between 2 points
				stepX 		 = new double[gameSpirits.size()], // next X position
				stepY 		 = new double[gameSpirits.size()]; // next Y position
		int[]   step 		 = new int[gameSpirits.size()], // current step counter in line
				steps 		 = new int[gameSpirits.size()]; // total steps for each line (2 points)

		for(int i =0 ; i<gameSpirits.size(); i++) {
			gamespirit = gameSpirits.get(i);
			pacman = (Packman) gamespirit.getGameObj();
			path = pacman.getPath();
			iterPath[i] = path.iterator();
			p3d1[i] = iterPath[i].next();
			p3d2[i] = iterPath[i].next();
			p2d1[i] = map.getLocationOnScreen(p3d1[i]);
			p2d2[i] = map.getLocationOnScreen(p3d2[i]);
			map.updateLocationOnScreen(gamespirit,(int)(p2d1[i].x/map.getScaleFactorX()), (int)(p2d1[i].y/map.getScaleFactorY()));
			startP[i] = gamespirit.getStartLocation();
			
			// get distance in pixels (RAW), distance in meters
			distance[i] = map.getDistanceByPixelRaw(p2d1[i], p2d2[i]);
			distMeters[i] = map.getDistance(p2d1[i], p2d2[i]);

			// calculate the ratio between the pixels and meters
			pix2mtrRatio[i] = distance[i]/distMeters[i];

			// calculate the orientation.
			angle[i] = Math.toRadians(map.getAngleRaw(p2d1[i], p2d2[i]));
		}

		double speed;

		System.out.println("[Simulation] Starting Path Simulation Version: "+version+", Stage: "+versionStage);

		try {
			while(!finished)  { 
				for(int i = 0 ; i < gameSpirits.size() ; i++) {
					if(isFinished[i])
						continue;
					// get gameUI component 
					gamespirit = gameSpirits.get(i);

					// get the pacman from it
					pacman = (Packman) gamespirit.getGameObj();

					// get speed
					speed = pacman.getSpeed();
					
					////////// MOVMENT OF A STEP ///////////////
					
					// set orientation of pacman
					// TODO: optimize, this is called on every step instead of every line.
					gamespirit.img = MyFrame.rotateImage(pacman.getSpirit(), (float)Math.toDegrees(angle[i])+180); //TODO: fix

					steps[i] = (int) (distMeters[i]/speed);

					// Simulate the movement between those two points p2d1 and p2d2[i] (which are p3d1[i] and p3d2[i] just in pixels)
					if(step[i] <= steps[i])
					{
						// calculate step sizes
						stepX[i] = Math.round(startP[i].x + Math.sin(angle[i])*(speed*step[i]*pix2mtrRatio[i]));
						stepY[i] = Math.round(startP[i].y + Math.cos(angle[i])*(speed*step[i]*pix2mtrRatio[i]));
						//System.out.println("[Simulation] stepX: " +stepX[i]+" stepY: "+stepY[i]);

						// animate movement. -> move the object.
						map.updateLocationOnScreen(gamespirit, (int)Math.round(stepX[i])+gamespirit.getStartWidth()/2, (int)Math.round(stepY[i])+gamespirit.getStartHeight()/2);
						//System.out.println("[Simulation] Pos: "+gamespirit.getStartLocation() +", step: "+step[i]+"/"+steps[i]);
						// sleep in milliseconds

					}
					else
					if(step[i] > steps[i]) {
						// final step
						p2d2[i] = map.getLocationOnScreen(p3d2[i]);
						map.updateLocationOnScreen(gamespirit,(int)(p2d2[i].x/map.getScaleFactorX()), (int)(p2d2[i].y/map.getScaleFactorY()));
						
						
						/////// NEXT LINE SETUP //////////
						if(!iterPath[i].hasNext()) {
							isFinished[i] = true;
							finish_amount++;
							if(finish_amount == isFinished.length) {
								System.out.println("[Simulation] Finished!");
								break;
							}
							continue;
						}
						
						p3d1[i] = p3d2[i];
						p3d2[i] = iterPath[i].next();
						
						p2d1[i] = map.getLocationOnScreen(p3d1[i]);
						p2d2[i] = map.getLocationOnScreen(p3d2[i]);
						
						startP[i] = gamespirit.getStartLocation();

						// get distance in pixels (RAW), distance in meters
						distance[i] = map.getDistanceByPixelRaw(p2d1[i], p2d2[i]);
						distMeters[i] = map.getDistance(p2d1[i], p2d2[i]);

						// calculate the ratio between the pixels and meters
						pix2mtrRatio[i] = distance[i]/distMeters[i];

						// calculate the orientation.
						angle[i] = Math.toRadians(map.getAngleRaw(p2d1[i], p2d2[i]));
						
						step[i] = 0;
					}
					
					step[i]++;

				}

			Thread.sleep(updateSpeed);
			}
		}	catch (InterruptedException ie) {
			// stop
			System.out.println("Stopping simulation of path...");
			return;
		}
		


	}



}
