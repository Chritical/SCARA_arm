/** 
 * This is the class that ties it all together. 
 * Takes an image and converts it to code that can be submitted to the SCARA
 * robot arm for drawing
 * 
 * ENGR110, 2018, T2
 * Team baconCow();
 * 
 * Team members:
 * Christopher Meekin
 * Sam Kain
 * Riley Allen
 * Alexander Pace
 */

import ecs100.*;
import java.util.*;
import java.awt.Color;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SCARAImageCreator
{
	SCARAImageCreator(){
		//Make sure the image is 480p
		System.out.println("Welcome to team baconCow();'s image creator for the SCARA robot arm");
		System.out.println("");
		System.out.println("Reading in file...");
		ImageLoader imageLoader = new ImageLoader();
		int[][] image = imageLoader.loadImage();
		//imageLoader.printImage();
		
		System.out.println("Finding edges...");
		EdgeProcessor edgeProcessor = new EdgeProcessor(image);
		image = edgeProcessor.processEdges();
		
		System.out.println("Processing file...");
		ShapeProcessor shapeProcessor = new ShapeProcessor(image, 20, 5);
		ArrayList<ArrayList<int[]>> edges = shapeProcessor.getEdges();
		
		System.out.println("Converting to coordinates...");
	   	double motorLeftX = 300;
	   	double motorLeftY = 480;
	   	double motorRightX = 340;
	   	double motorRightY = 480;
	   	double radius = 290;
	   	
	   	// all of these are test coordinates for calibration
	   	double[] leftThetaValues = {160, 143, 130, 123};
	   	double[] rightThetaValues = {76.7, 62.4, 53.5, 45};
	   	double[] leftTValues = {1300, 1400, 1500, 1600};
		double[] rightTValues = {1300, 1400, 1500, 1600};
		
		CoordinateProcessor coordinateProcessor = new CoordinateProcessor(edges, motorLeftX, motorLeftY, motorRightX, motorRightY, 
										  radius, leftThetaValues, rightThetaValues, 
										  leftTValues, rightTValues);
		ArrayList<ArrayList<int[]>> coordinates = coordinateProcessor.getOutput();

		System.out.println("Saving to file...");
		FileCreator fileCreator = new FileCreator(coordinates);
		fileCreator.writeFile();
		
		System.out.println("================= ~ Done! ~ =================");
	}
	
	public static void main (String[] args) {
		SCARAImageCreator imageCreator = new SCARAImageCreator();
	}
	
}
