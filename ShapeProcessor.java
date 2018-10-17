import java.util.ArrayList;

/**
 * Processes a file of a picture of the edges into an ArrayList of
 * coordinates.
 *
 * Input: int[][] PPM  where the edges are white
 * Output: ArrayList<ArrayList<int[]>> Edges  where the list holds edges
 *         and the edges hold int[2] of coordinates.
 */
class ShapeProcessor {

    private int[][] image;
    private int startThreshold;
    private int continueThreshold;
    private ArrayList<ArrayList<int[]>> edges;

    private int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1},
                                  { 0, -1},          { 0, 1},
                                  { 1, -1}, { 1, 0}, { 1, 1}};

    public ShapeProcessor(int[][] edgesPPM,
                          int startThreshold,
                          int continueThreshold) {

        // copies the given image into a duplicate array, for safety
        // adapted from: https://stackoverflow.com/a/5785754
        image = new int[edgesPPM.length][edgesPPM[0].length];
        System.arraycopy(edgesPPM, 0, image, 0, edgesPPM.length);

        this.startThreshold = startThreshold;
        this.continueThreshold = continueThreshold;

        edges = new ArrayList<ArrayList<int[]>>();
        
        processEdges();
       
    }

    public ArrayList<ArrayList<int[]>> getEdges() {
        return edges;
    }

    /**
     * Produces the edges by adding them to the edges ArrayList.
     */
    private void processEdges() {
        for (int row = 0; row < image.length; row++) {
            for (int col = 0; col < image[0].length; col++) {
                if (image[row][col] > startThreshold) {
                    edges.add(followEdge(row, col));
                }
            }
        }
    }

    /**
     * Follows an individual edge and records it.
     * Erases pixels behind itself.
     * @param row  Row of the start pixel
     * @param col  Column of the start pixel
     * @return An edge
     */
    private ArrayList<int[]> followEdge(int row, int col) {

        ArrayList<int[]> edge = new ArrayList<int[]>();
        int newRow = row;
        int newCol = col;

        while (image[newRow][newCol] > continueThreshold) {
            edge.add(new int[] {newCol, newRow});
            erasePixel(newRow, newCol);

            int[] maximumNeighbour = findMaximumNeighbour(newRow, newCol);
            newRow = maximumNeighbour[0];
            newCol = maximumNeighbour[1];
        }
        return edge;
    }


    /**
     * Erases the given pixel in the image.
     * @param row  Row of the pixel to be erased.
     * @param col  Column of the pixel to be erased.
     */
    private void erasePixel(int row, int col) {
        image[row][col] = 0;
    }


    /**
     * Finds the neighbouring pixel with the maximum intensity.
     * @param row  Row of the current pixel
     * @param col  Column of the current pixel
     * @return Coordinates of the maximum neighbour
     */
    private int[] findMaximumNeighbour(int row, int col) {
        int[] max = {0, 0};
        for (int[] dir : directions) {
            int newRow = restrictValues(row + dir[0], 0, image.length - 1);
            int newCol = restrictValues(col + dir[1], 0, image[0].length - 1);
            if (image[newRow][newCol] > image[max[0]][max[1]]) {
                max = new int[] {newRow, newCol};
            }
        }
        return max;
    }

    private int restrictValues(int val, int min, int max) {
        if (val < min) return min;
        if (val > max) return max;
        return val;
    }

      /* uncomment for testing */
//    public static void main(String[] args) {
//
//        int[][] testData = new int[5][5];
//        int[][] testData = {{0  , 123, 255, 0  , 6  },
//                            {20 , 152, 160, 150, 0  },
//                            {151, 0  , 40 , 151, 255}};
//
//        for (int i = 0; i < testData.length; i ++) {
//            for (int j = 0; j < testData[i].length; j++) {
//                testData[i][j] = 0;
//                if (i == j || i == 4-j) {
//                    testData[i][j] = 255;
//                }
//            }
//        }
//
//        ShapeProcessor sp = new ShapeProcessor(testData, 200, 150);
//        sp.produceEdges();
//
//        for (ArrayList<int[]> edge : sp.getEdges()) {
//
//            for (int[] coord : edge) {
//                System.out.print("(" + coord[0] + ", " + coord[1] + "), ");
//            }
//            System.out.println();
//        }
//    }
}
