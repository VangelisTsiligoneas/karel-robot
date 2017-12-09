
package instructions;

import utils.*;

public class MyRobot extends Robot{

    public MyRobot(int x, int y, String direction){
        super(x, y, direction);
    }

    public void moveTwice(){
        move();
        move();
    }

    public void avoidObstacleAtLeft(){
        turnLeft();
        move();
        turnRight();
        moveTwice();
        turnRight();
        move();
        turnLeft();
    }

    public void pickUpAtLeft(){
        turnLeft();
        pickUp();
        turnRight();
    }
}