 class CoordinateProcessor {
    
    double targetX, targetY;
    double motorLeftX, motorLeftY, motorRightX, motorRightY;
    double radius;
    double leftTheta, rightTheta;
    
    public CoordinateProcessor(double targetX, double targetY, double motorLeftX, double motorLeftY, double motorRightX, double motorRightY, double radius) {
        
        this.targetX = targetX;
        this.targetY = targetY;
        
        this.motorLeftX = motorLeftX;
        this.motorLeftY = motorLeftY;
        this.motorRightX = motorRightX;
        this.motorRightY = motorRightY;
        this.radius = radius;
        
        calculateLeftMotorAngle();
        calculateRightMotorAngle();
        
    }
    
    private void calculateLeftMotorAngle() {
    
		// calculate distance d
        // calculate h
        //
        // calculate A coordinates
        // calculate alpha
        //
        // calculate possible x and y
        
        double distance = Math.sqrt(  Math.pow((targetX - motorLeftX), 2) + Math.pow((targetY - motorLeftY), 2)  );
        
        System.out.println("distance: " + distance);
        
        System.out.println("radius^2: " + Math.pow(radius, 2));
        System.out.println("(distance/2)^2: " + Math.pow(distance/2, 2));
        
        double h = Math.sqrt( Math.pow(radius, 2) - Math.pow(distance/2, 2) );
        System.out.println("h: " + h);
        
        // coordinates in the middle of the pen and the motor
        double xA = (targetX + motorLeftX) / 2;
        double yA = (targetY + motorLeftY) / 2;
        
        System.out.println("xA: " + xA);
        System.out.println("yA: " + yA);
        
        double alpha = Math.acos((motorLeftX - targetX)/distance);
        
        System.out.println("alpha: " + alpha);
        
        System.out.println("sin alpha: " + Math.sin(alpha));
                //x1, y1, x2, y2
        double x1 = xA + h * Math.sin(alpha);
        double x2 = xA - h * Math.sin(alpha);

        double y1 = yA - h * Math.cos(alpha);
        double y2 = yA + h * Math.cos(alpha);
        
        System.out.println("x1: " + x1);
        System.out.println("y1: " + y1);
        System.out.println("x2: " + x2);
        System.out.println("y2: " + y2);

        double chosenX, chosenY;
        
        if (x1 > x2) {
			chosenX = x1;
			chosenY = y1;
		} else {
			chosenX = x2;
			chosenY = y2;
		}
    
		System.out.println("chosenX: " + chosenX);
		System.out.println("chosenY: " + chosenY);
    
        rightTheta = Math.atan2(chosenY - motorRightY, chosenX - motorRightX);
    }
    
    private void calculateRightMotorAngle() {
    
		// calculate distance d
        // calculate h
        //
        // calculate A coordinates
        // calculate alpha
        //
        // calculate possible x and y
        
        double distance = Math.sqrt(  Math.pow((targetX - motorRightX), 2) + Math.pow((targetY - motorRightY), 2)  );
        
        System.out.println("distance: " + distance);
        
        System.out.println("radius^2: " + Math.pow(radius, 2));
        System.out.println("(distance/2)^2: " + Math.pow(distance/2, 2));
        
        double h = Math.sqrt( Math.pow(radius, 2) - Math.pow(distance/2, 2) );
        System.out.println("h: " + h);
        
        // coordinates in the middle of the pen and the motor
        double xA = (targetX + motorRightX) / 2;
        double yA = (targetY + motorRightY) / 2;
        
        System.out.println("xA: " + xA);
        System.out.println("yA: " + yA);
        
        double alpha = Math.acos((motorRightX - targetX)/distance);
        
        System.out.println("alpha: " + alpha);
        
        System.out.println("sin alpha: " + Math.sin(alpha));
                //x1, y1, x2, y2
        double x1 = xA + h * Math.sin(alpha);
        double x2 = xA - h * Math.sin(alpha);

        double y1 = yA - h * Math.cos(alpha);
        double y2 = yA + h * Math.cos(alpha);
        
        System.out.println("x1: " + x1);
        System.out.println("y1: " + y1);
        System.out.println("x2: " + x2);
        System.out.println("y2: " + y2);

        double chosenX, chosenY;
        
        if (x1 > x2) {
			chosenX = x1;
			chosenY = y1;
		} else {
			chosenX = x2;
			chosenY = y2;
		}
    
		System.out.println("chosenX: " + chosenX);
		System.out.println("chosenY: " + chosenY);
    
        rightTheta = Math.atan2(chosenY - motorRightY, chosenX - motorRightX);
        
    }
    
    private double[] calculatePossibleCoordinates(double targetX, double targetY, double motorX, double motorY) {
        
        // calculate distance d
        // calculate h
        //
        // calculate A coordinates
        // calculate alpha
        //
        // calculate possible x and y
        
        double distance = Math.sqrt(  Math.pow((targetX - motorX), 2) + Math.pow((targetY - motorY), 2)  );
        
        System.out.println("radius^2: " + Math.pow(radius, 2));
        System.out.println("(distance/2)^2: " + Math.pow(distance/2, 2));
        
        double h = Math.sqrt( Math.pow(radius, 2) - Math.pow(distance/2, 2) );
        System.out.println(h);
        
        // coordinates in the middle of the pen and the motor
        double xA = (targetX + motorX) / 2;
        double yA = (targetY + motorY) / 2;
        
        double alpha = Math.acos((motorX - targetX)/distance);
        
        System.out.println("alpha: " + alpha);
        
        System.out.println("sin alpha: " + Math.sin(alpha));
        
        double x1 = xA + h * Math.sin(alpha);
        double x2 = yA - h * Math.cos(alpha);
        //x1, y1, x2, y2
        double y1 = xA - h * Math.sin(alpha);
        double y2 = yA + h * Math.cos(alpha);
        
        System.out.println("x1: " + x1);
        System.out.println("y1: " + y1);
        System.out.println("x2: " + x2);
        System.out.println("y2: " + y2);
        
        return new double[] {x1, y1, x2, y2};
    }
    
    public void output() {
		
		
		//System.out.println
				// calculate distance d
        // calculate h
        //
        // calculate A coordinates
        // calculate alpha
        //
        // calculate possible x and y
        
        double distance = Math.sqrt(  Math.pow((targetX - motorLeftX), 2) + Math.pow((targetY - motorLeftY), 2)  );
        
        System.out.println("distance: " + distance);
        
        System.out.println("radius^2: " + Math.pow(radius, 2));
        System.out.println("(distance/2)^2: " + Math.pow(distance/2, 2));
        
        double h = Math.sqrt( Math.pow(radius, 2) - Math.pow(distance/2, 2) );
        System.out.println("h: " + h);
        
        // coordinates in the middle of the pen and the motor
        double xA = (targetX + motorLeftX) / 2;
        double yA = (targetY + motorLeftY) / 2;
        
        double alpha = Math.acos((motorLeftX - targetX)/distance);
        
        System.out.println("alpha: " + alpha);
        
        System.out.println("sin alpha: " + Math.sin(alpha));
        
        double x1 = xA + h * Math.sin(alpha);
        double x2 = yA - h * Math.cos(alpha);
        
        double y1 = xA - h * Math.sin(alpha);
        double y2 = yA + h * Math.cos(alpha);
        
        System.out.println("x1: " + x1);
        System.out.println("y1: " + y1);
        System.out.println("x2: " + x2);
        System.out.println("y2: " + y2);
        
        double chosenX, chosenY;
        
        if (x1 < x2) {
			chosenX = x1;
			chosenY = y1;
		} else {
			chosenX = x2;
			chosenY = y2;
		}

        leftTheta = Math.atan2(motorLeftY - chosenY, chosenX - motorLeftX);
        System.out.println(leftTheta*180/Math.PI + " " + rightTheta*180/Math.PI);
    }
    
    
    public static void main(String[] args) {
        
        CoordinateProcessor cp = new CoordinateProcessor(255, 98, 300, 480, 340, 480, 290);
        cp.output();
        
    }
    
 }
