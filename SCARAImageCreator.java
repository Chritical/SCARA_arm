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

public class SCARAImageCreator
{
	SCARAImageCreator(){
		//Make sure the image is 480p
		System.out.println("Welcome to team baconCow();'s image creator for the SCARA robot arm");
		System.out.println("");
		System.out.println("Reading in file...");
		ImageLoader imageLoader = new ImageLoader();
		System.out.println("Processing file...");
		ShapeProcessor shapeProcessor = new ShapeProcessor();
		System.out.println("Finding edges...");
		EdgeProcessor edgeProcessor = new EdgeProcessor();
		System.out.println("Converting to coordinates...");
		CoordinateProcessor coordinateProcessor = new CoordinateProcessor();
		System.out.println("Saving to file...");
		FileCreator fileCreator = new FileCreator();
		System.out.println("================= ~ Done! ~ =================");
	}
	
	public static void main (String[] args)}{
		SCARAImageCreator imageCreator = new SCARAImageCreator();
	}
	
}
