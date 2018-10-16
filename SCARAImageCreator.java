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
		//currently all classes and methods are preliminary, may need to be changed to fit actual files
		ImageLoader imageLoader = new ImageLoader();
		ShapeProcessor shapeProcessor = new ShapeProcessor();
		EdgeProcessor edgeProcessor = new EdgeProcessor();
		CoordinateProcessor coordinateProcessor = new CoordinateProcessor();
		FileCreator fileCreator = new FileCreator();
		
		System.out.println("Welcome to team baconCow();'s image creator for the SCARA robot arm");
		System.out.println("");
		System.out.println("Reading in file...");
		imageLoader.loadImage();
		System.out.println("Processing file...");
		shapeProcessor.processShape();
		System.out.println("Finding edges...");
		edgeProcessor.findEdges();
		System.out.println("Converting to coordinates...");
		coordinateProcessor.processCoordinates();
		System.out.println("Saving to file...");
		fileCreator.writeFile();
		System.out.println("================= ~ Done! ~ =================");
	}
	
	public static void main (String[] args)}{
		SCARAImageCreator imageCreator = new SCARAImageCreator();
	}
	
}
