package instructions;

import utils.*;

public class Execute{
    public void main(World world){
        //         position x, y, direction
        MyRobot karel = new MyRobot(2, 2, "DOWN");
        karel.land(world);
        karel.move();
        karel.turnRight();
        karel.pickUp();
        karel.move();
        karel.turnRight();
        karel.push();
        karel.turnRight();
        karel.move();

        if(karel.inFrontOfAnObstacle()){
            karel.avoidObstacleAtLeft();
        }
        else{
            karel.moveTwice();
        }
        karel.pickUpAtLeft();
        karel.move();
        karel.unLock();
        karel.openDoor();
        karel.moveTwice();
        if(karel.inFrontOfAnObstacle()){
            karel.avoidObstacleAtLeft();
        }
        else{
            karel.moveTwice();
        }
        karel.pickUpAtLeft();
        karel.moveTwice();
        karel.finish();

    }
}