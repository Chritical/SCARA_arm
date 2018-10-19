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
	                           double radius,
	                           double[] leftThetaValues,
	                           double[] rightThetaValues,
	                           double[] leftTValues,
	                           double[] rightTValues) {
		
		this.edges = edges;
		this.motorLeftX = motorLeftX;
		this.motorLeftY = motorLeftY;
		this.motorRightX = motorRightX;
		this.motorRightY = motorRightY;
		this.radius = radius;
		
		for (int i = 0; i < leftThetaValues.length; i++) {
			leftThetaValues[i] = leftThetaValues[i] * Math.PI/180;
			rightThetaValues[i] = rightThetaValues[i] * Math.PI/180;
		}
		
		calculateMotorEquation(leftThetaValues, leftTValues, "left");
		calculateMotorEquation(rightThetaValues, rightTValues, "right");
		
		processCoordinates();
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
		//theta = theta * Math.PI/180;
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
	public void calculateMotorEquation(double[] theta, double[] t,
	                                   String motor) {
		
		//for (int i = 0; i < theta.length; i++) {
		//	theta[i] = theta[i] * Math.PI/180;
		//}
		
		int chosenMotor = (motor.equals("left")) ? 0 : 1;
		
		assert theta.length == t.length : "There should be the same number of data points.";
		double numberOfDataPoints = theta.length;
		double sumOfXY = 0;
		double sumOfX = 0, sumOfY = 0;
		double sumOfXSquared = 0;
		
		for (int i = 0; i < theta.length; i++) {
			sumOfXY += theta[i] * t[i];
			sumOfX  += theta[i];
			sumOfY  += t[i];
			sumOfXSquared += Math.pow(theta[i], 2);
		}
		
		//System.out.println("sumOfXY: " + sumOfXY);
		//System.out.println("sumOfX: " + sumOfX);
		//System.out.println("sumOfY: " + sumOfY);
		//System.out.println("sumOfXSquared: " + sumOfXSquared);
		
		motorGradient[chosenMotor] = (numberOfDataPoints*sumOfXY - sumOfX*sumOfY) / 
		                             (numberOfDataPoints*sumOfXSquared - Math.pow(sumOfX, 2));
		                             
		motorConstant[chosenMotor] = (sumOfY*sumOfXSquared - sumOfX*sumOfXY) / 
		                             (numberOfDataPoints*sumOfXSquared - Math.pow(sumOfX, 2));
                          
		//System.out.println(motor + " motor gradient: " + motorGradient[chosenMotor] + "\t" + motor + " motor constant: " + motorConstant[chosenMotor]);
		
	}
    
    
    /**
     * Converts the given coordinate to the set of motor controls.
     * @param int[] coordinate The x-y coordinates of the target.
     * @return The set of motor controls.
     */
    private int[] convertCoordinateToMotorControls(int[] coordinate) {
		
		int[] newCoord = new int[2];
		
		newCoord[0] = coordinate[0];
		newCoord[1] = coordinate[1];
		
		//~ System.out.println("c: " + newCoord[0] + " " + newCoord[1]);
		
		double leftTheta, rightTheta;
		
		leftTheta = calculateTheta(newCoord[0], newCoord[1], "left");
		rightTheta = calculateTheta(newCoord[0], newCoord[1], "right");
		
		newCoord[0] = calculateMotorControl(leftTheta, "left");
		newCoord[1] = calculateMotorControl(rightTheta, "right");
		
		//~ System.out.println(newCoord[0] + " " + newCoord[1]);
		
		return newCoord;
	}
	
	
	/**
	 * Processes every coordinate in every edge and overwrites them
	 * with the new motor control values.
	 */
	private void processCoordinates() {	
		
		ArrayList<ArrayList<int[]>> edgesCopy = new ArrayList<ArrayList<int[]>>();
		
		//~ System.out.println(edges.size() + " " + edges.get(0).size());
		
		for (int i = 0; i < edges.size(); i++) {
			
			edgesCopy.add(new ArrayList<int[]>());
			
			for (int j = 0; j < edges.get(i).size(); j++) {
				edgesCopy.get(i).add(convertCoordinateToMotorControls(edges.get(i).get(j)));
			}
		}
		
		edges = edgesCopy;
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
	
	
	public void testMotorControls(double leftTheta, double rightTheta,
	                              double[] leftThetaTest,  double[] rightThetaTest, 
	                              double[] leftTTest,      double[] rightTTest) {
		
		leftTheta = leftTheta * Math.PI/180;
		rightTheta = rightTheta * Math.PI/180;
		
		for (int i = 0; i < leftThetaTest.length; i++) {
			leftThetaTest[i] = leftThetaTest[i] * Math.PI/180;
			rightThetaTest[i] = rightThetaTest[i] * Math.PI/180;
		}
		
		calculateMotorEquation(leftThetaTest, leftTTest, "left");
		calculateMotorEquation(rightThetaTest, rightTTest, "right");
		
		System.out.println("left motor control: " + 
						   calculateMotorControl(leftTheta, "left") +
						   "\tright motor control: " +
						   calculateMotorControl(rightTheta, "right"));
				
	}
	
	public void testCoordinateToMotorControl(int[] coordinate) {

		int[] c = convertCoordinateToMotorControls(coordinate);
		
		System.out.println(c[0] + " " + c[1]);
	}
    
 }
