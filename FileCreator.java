/** INPUT: ArrayList<int[]> Edges int[0] L motor, int[1] R motor
/** OUTPUT: File of motor controls with format 'XXXX,XXXX,XXXX', one per line */

import ecs100.*;
import java.util.*;
import java.io.*;

public class FileCreator
{	
	/** Constructor */
	FileCreator(){
		//Test array
		/*ArrayList<int[]> testList = new ArrayList<int[]>();
		testList.add(new int[]{1000,2000});
		testList.add(new int[]{3000,4000});
		testList.add(new int[]{4000,5000});*/
		
		writeFile(testList);
	}
	
	/** Writes the edges to the file */
	/** Draws an edge, lifts the pen, moves to a new edge, lowers the pen, repeat */
	public void writeFile(ArrayList<int[]> edges){
		System.out.println("Writing file...");
		
		try{
		PrintStream out = new PrintStream(new File("SCARA_instructions.txt"));
			out.println("1500,1500,1200"); //default position, pen up
			for (int[] line : edges){
				out.println(line[0]+","+line[1]+",1200"); //pen up
				out.println(line[0]+","+line[1]+",1800"); //pen down
			}
		}
		catch (IOException e){ Trace.println("FAIL: "+e); }
		
		System.out.println("Success \nFile written to \"SCARA_instructions\"");
	}
	
	/*public static void main(String[] args){
		FileCreator fileCreator = new FileCreator();
	}*/
}
