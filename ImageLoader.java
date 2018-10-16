
/**
 * Creates a ppm from an rgb image (optimised for jpeg)
 *
 * Felix (KrypticKain) Kain
 * 0.0.0.1
 */

import ecs100.*;
import java.util.*;
import java.awt.Color;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ImageLoader{
	
    private final int pixelSize = 1;
    
    private int selectedRow = 0;
    private int selectedCol = 0;
    
    private int[][] image = new int[][]{{80,80,80},{80, 200, 80},{80,80,80}}; 
    
    private Color[] greyColors = new Color[256];
    
    ImageLoader(){
		loadImage();
    }
    
    public int[][] loadImage(){
		this.computeGreyColours();
		this.loadImageFromFile();
		UI.print(image);
		return image;
	}
	//this method will just return the image(getter method)
    public int[][] getImage(){
		return this.image;    
       }
    
    private void computeGreyColours(){
        for (int i=0; i<256; i++){
            this.greyColors[i] = new Color(i, i, i);
        }
    }
	
	
	//This is the method that chooses the file.
    public void loadImageFromFile(){
        this.image = UIFileChooser.open();
        //this.redisplayImage();
    }

    public void redisplayImage(){
        UI.clearGraphics();
        UI.setImmediateRepaint(false);
        for(int row=0; row<this.image.length; row++){
            int y = row * this.pixelSize;
            for(int col=0; col<this.image[0].length; col++){
                int x = col * this.pixelSize;
                UI.setColor(this.greyColor(this.image[row][col]));
                UI.fillRect(x, y, this.pixelSize, this.pixelSize);
            }
        }
    }

	private Color greyColor(int grey){
        if (grey < 0){
            return Color.blue;
        }
        else if (grey > 255){
            return Color.red;
        }
        else {
            return this.greyColors[grey];
        }
    }

    public int[][] loadAnImage(String imageName) {
        int[][] ans = null;
        if (imageName==null) return null;
        try {
            BufferedImage img = ImageIO.read(new File(imageName));
            UI.printMessage("loaded image height(rows)= " + img.getHeight() + "  width(cols)= " + img.getWidth());
            ans = new int[img.getHeight()][img.getWidth()];
            for (int row = 0; row < img.getHeight(); row++){
                for (int col = 0; col < img.getWidth(); col++){
                    Color c = new Color(img.getRGB(col, row), true);
                    ans[row][col] = (int)Math.round((0.3 * c.getRed()) + (0.59 * c.getGreen())
                            + (0.11 * c.getBlue()));
                }
            }
        } catch(IOException e){UI.println("Image reading failed: "+e);}
        return ans;
    }
    
    /*public static void main(String[] arguments){
        imageLoader ob = new imageLoader();
    }  */

}


