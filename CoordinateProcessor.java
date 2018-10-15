 import java.util.ArrayList;
 
 class CoordinateProcessor {
    private ArrayList<ArrayList<int[]>> edges;
    private double motorLeftX, motorLeftY;
    private double motorRightX, motorRightY;
    private double[] motorGradient = new double[2];
    private double[] motorConstant = new double[2];
    private double radius;
    
    
    /**
     * An object that processes a given list of edges with coordinates
     * measured in the x-y plane and returns the same set of edges with
     * coordinates replaces with motor controls.
     * @param ArrayList<ArrayList<int[]>> edges List of edges to process.
     * @param motorLeftX X-coordinate of the left motor.
     * @param motorLeftY Y-coordinate of the left motor.
     * @param motorRightX X-coordinate of the right motor.
     * @param motorRightY Y-coordinate of the right motor.
     * @param radius Dinstance of the SCARA's motor to its joint.
     */
	public CoordinateProcessor(ArrayList<ArrayList<int[]>> edges,
	                           double motorLeftX,
	                           double motorLeftY,
	                           double motorRightX,
	                           double motorRightY,
	                           double radius) {
		
		this.edges = edges;
		this.motorLeftX = motorLeftX;
		this.motorLeftY = motorLeftY;
		this.motorRightX = motorRightX;
		this.motorRightY = motorRightY;
		this.radius = radius;
	}
	
	
	/**
	 * Calculates the required angle of the motor (theta) for the given
	 * target and motor.
	 * @param double targetX X-coordinate of the target.
	 * @param double targetY Y-coordinate of the target.
	 * @param String motor Either "left" or "right" motor.
	 * @return The value for theta.
	 */
	private double calculateTheta(double targetX, double targetY,
	                              String motor) {
		double motorX, motorY;
		double distance, h;
		double middleX, middleY;
		double cosAlpha, sinAlpha;
		double x1, y1, x2, y2;
		double chosenX, chosenY;
		
		if (motor.equals("left")) {
			motorX = motorLeftX;
			motorY = motorLeftY;
		} else {
			motorX = motorRightX;
			motorY = motorRightY;
		}
		
		// distance from target to motor
		distance = Math.sqrt(Math.pow((targetX - motorX), 2) + Math.pow((targetY - motorY), 2));
		
		// distance from middle coordinates to arm junction
		h = Math.sqrt(Math.pow(radius, 2) - Math.pow(distance/2, 2));
		
		// coordinates of the middle point between the target and the motor
		middleX = (targetX + motorX) / 2;
		middleY = (targetY + motorY) / 2;
		
		// angles needed to calculate theta
		cosAlpha = (motorX - targetX) / distance;
		sinAlpha = (motorY - targetY) / distance;
		
		// possible coordinates 1
		x1 = middleX + h * sinAlpha;
		y1 = middleY - h * cosAlpha;
		
		// possible coordinates 2
		x2 = middleX - h * sinAlpha;
		y2 = middleY + h * cosAlpha;
		
		if (motor.equals("left")) {
			// choose smallest x
			if (x1 < x2) {
				chosenX = x1;
				chosenY = y1;
			} else {
				chosenX = x2;
				chosenY = y2;
			}
		} else {  // right motor
			// choose largest x
			if (x1 > x2) {
				chosenX = x1;
				chosenY = y1;
			} else {
				chosenX = x2;
				chosenY = y2;
			}
		}
		
		// is negative because coordinate space has y increasing downards
		return -Math.atan2(chosenY - motorY, chosenX - motorX);
	}
    
    
    /**
     * Calculates the corresponding motor control to the given angle.
     * @param double theta The required angle.
     * @return The corresponding motor control.
     */
    private int calculateMotorControl(double theta, String motor) {
		int chosenMotor = motor.equals("left") ? 0 : 1;	
		return (int) (motorGradient[chosenMotor] * theta + motorConstant[chosenMotor]);  // y = mx + c
	}
	
	
	/**
	 * The motor control is linearly proportional to the given theta for
	 * the unique motor. This method calculates the linear equation for
	 * both motors so it can be used later.
	 * @param theta1  One tested theta value.
	 * @param theta2  A second tested theta value.
	 * @param t1  One tested motor value corresponding to the theta.
	 * @param t2  A second tested motor value corresponding to theta2.
	 * @param motor  Either the left or right motor.
	 */
	public void calculateMotorEquation(double theta1, double theta2,
	                                   double t1,     double t2,
	                                   String motor) {
		
		int chosenMotor = (motor.equals("left")) ? 0 : 1;
		
		motorGradient[chosenMotor] = (t2 - t1) / (theta2 - theta1);
		motorConstant[chosenMotor] = ((t1 * theta1 - t2 * theta2) / 
		                              (theta2 - theta1)) + t1;
	}
    
    
    /**
     * Converts the given coordinate to the set of motor controls.
     * @param int[] coordinate The x-y coordinates of the target.
     * @return The set of motor controls.
     */
    private int[] convertCoordinateToMotorControls(int[] coordinate) {
		double leftTheta, rightTheta;
		
		leftTheta = calculateTheta(coordinate[0], coordinate[1], "left");
		rightTheta = calculateTheta(coordinate[0], coordinate[1], "right");
		
		coordinate[0] = calculateMotorControl(leftTheta, "left");
		coordinate[1] = calculateMotorControl(rightTheta, "right");
		
		return coordinate;
	}
	
	
	/**
	 * Processes every coordinate in every edge and overwrites them
	 * with the new motor control values.
	 */
	private void processCoordinates() {		
		for (ArrayList<int[]> edge : edges) {
			for (int[] coordinate : edge) {		
				convertCoordinateToMotorControls(coordinate);	
			}
		}
	}
	
	
	/**
	 * Gets the output from the object, which is an ArrayList containing
	 * all edges with the coordinates being given in motor controls.
	 * @return The edges as motor controls.
	 */
	public ArrayList<ArrayList<int[]>> getOutput() {
		return edges;
	}
	
	
	/**
	 * Tests that the angles are calculated correctly given the target
	 * coordinates.
	 * @param targetX Target x-coordinate.
	 * @param targetY Target y-coordinate.
	 */
	public void testTheta(double targetX, double targetY) {	
		double leftTheta = calculateTheta(targetX, targetY, "left");
		double rightTheta = calculateTheta(targetX, targetY, "right");
		
		System.out.println("left theta:  " + leftTheta*180/Math.PI
		               + "\tright theta: " + rightTheta*180/Math.PI);
	}
	
	
	public void testMotorControls(double leftTheta,
	                              double leftTheta1,  double leftTheta2, 
	                              double leftT1,      double leftT2,
	                              double rightTheta,
	                              double rightTheta1, double rightTheta2,
	                              double rightT1,     double rightT2) {
		
		calculateMotorEquation(leftTheta1, leftTheta2, leftT1, leftT2, "left");
		calculateMotorEquation(rightTheta1, rightTheta2, rightT1, rightT2, "right");
		
		System.out.println("left motor control: " + 
		                   calculateMotorControl(leftTheta, "left") +
		                   "\tright motor control: " +
		                   calculateMotorControl(rightTheta, "right"));
		
	}
    
    public static void main(String[] args) {
		// some simple test cases which are correct according to
		// Arthur's python program.
        CoordinateProcessor cp = new CoordinateProcessor(null, 300, 480, 340, 480, 290);
        cp.testTheta(255, 98);
        cp.testTheta(230, 12);
        
        //cp.testMotorControls(
    }
    
 }
