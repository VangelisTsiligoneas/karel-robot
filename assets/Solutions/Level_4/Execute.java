package instructions;

import utils.*;

public class Execute{
    public void main(World world){
        //         position x, y, direction
        MyRobot karel = new MyRobot(5, 7, "LEFT");
        karel.land(world);
        karel.pushAndMove();
        karel.pushAndMove();
        karel.turnAround();
        karel.move();
        karel.move();
        karel.turnLeft();
        karel.pickUpAndMoveLong();
        karel.turnRight();
        karel.pickUpAndMoveLong();
        
        if(karel.inFrontOfAnObstacle()){
            karel.turnLeft();
            karel.moveTwice();
            karel.turnRight();
            karel.moveTwice();
            if(karel.inFrontOfAnObstacle()){
                karel.turnRight();
                karel.move();
                karel.turnLeft();
                karel.moveTwice();
                karel.turnRight();
                karel.moveTwice();
                karel.turnLeft();
            }
            else{
                karel.moveTwice();
                karel.turnRight();
                karel.moveTwice();
                karel.move();
                karel.turnLeft();
            }
            
        }
        else{
            karel.move();
            karel.turnRight();
            karel.move();
            karel.turnLeft();
            karel.moveTwice();
            karel.move();
        }
        karel.moveTwice();
        karel.moveTwice();
        karel.turnRight();
        karel.pickUp();
        karel.turnAround();
        karel.moveTwice();
        karel.move();
        karel.finish();
    }
}

