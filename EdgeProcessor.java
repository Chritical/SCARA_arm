/**
 * Takes an int[][] ppm image and outputs an int[][] with edges in white and everything else in black.
 **/

import ecs100.*;
import java.util.*;
import java.awt.Color;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.lang.Math;

class EdgeProcessor{

	int[][] brd;
	int[][] imgV;
	int[][] imgH;
	int[][] img;
	
	public EdgeProcessor(int input[][]){
		//Test case
        	/*int input[][] = {{250,250,250,250,50,50,50,50,50},
					{250,250,250,250,50,50,50,50,50},
					{250,250,250,250,50,50,50,50,50},
					{250,250,250,250,50,50,50,50,50},
					{50,50,50,50,50,50,50,50,50},
					{50,50,50,50,50,50,50,50,50}};*/
        img = input;
    }
    
	public int[][] processEdges(){
		
		brd = new int[img.length+2][img[0].length+2];
		int hPro[][] = {{-1,0,1},{-2,0,2},{-1,0,1}};
		int vPro[][] = {{1,2,1},{0,0,0},{-1,-2,-1}};
		imgV=new int[img.length][img[0].length];
		imgH=new int[img.length][img[0].length];
		
		
		//convolutes the image into its edges
		for(int i=1;i<img.length-1; i++){
			for(int j=1; j<img[0].length-1;j++){

				imgV[i][j] = img[i-1][j-1]*vPro[0][0]+img[i][j-1]*vPro[1][0]+img[i+1][j-1]*vPro[2][0]+
					img[i-1][j]  *vPro[0][1]+img[i][j]  *vPro[1][1]+img[i+1][j]  *vPro[2][1]+
					img[i-1][j+1]*vPro[0][2]+img[i][j+1]*vPro[1][2]+img[i+1][j+1]*vPro[2][2];

				imgH[i][j] = img[i-1][j-1]*hPro[0][0]+img[i][j-1]*hPro[1][0]+img[i+1][j-1]*hPro[2][0]+
				     	img[i-1][j]  *hPro[0][1]+img[i][j]  *hPro[1][1]+img[i+1][j]  *hPro[2][1]+
					img[i-1][j+1]*hPro[0][2]+img[i][j+1]*hPro[1][2]+img[i+1][j+1]*hPro[2][2];
			}
				
		}
		
		//creates a bordered version of the edged image
		for(int i=0; i<brd[0].length; i++){
			brd[0][i]=0;
			brd[brd.length-1][i]=0;
		}
		for(int i=0; i<brd.length; i++){
			brd[i][0]=0;
			brd[i][brd[0].length-1]=0;
		}
		for(int i=0;i<img.length; i++){
			for(int j=0; j<img[0].length;j++){
				
				brd[i+1][j+1]=(int)(Math.sqrt(imgH[i][j]*imgH[i][j] +
						      imgV[i][j]*imgV[i][j]) * 255 / Math.sqrt((Math.pow(1020, 2)*2)));
					
					//if (brd[i+1][j+1] > 0) {
					//	System.out.print(brd[i+1][j+1] + " ");
					//}
				}
				
							  
		}
		return brd;
		
	}
}
