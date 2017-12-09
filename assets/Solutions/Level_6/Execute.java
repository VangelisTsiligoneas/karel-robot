package instructions;

import utils.*;

public class Execute{
    public void main(World world){
        //         position x, y, direction
        Robot karel = new Robot(4, 2, "LEFT");
        karel.land(world);
        MyRobot alex = new MyRobot(7,7,"UP");
        alex.land(world);

        karel.push();
        karel.turnRight();
        karel.pickUp();
        karel.turnRight();
        karel.pickUp();
        karel.move();
        karel.unLock();
        karel.openDoor();
        karel.move();
        karel.move();
        karel.turnRight();
        alex.move();
        alex.turnRight();
        alex.pickUp();
        alex.turnLeft();
        alex.unLock();
        alex.openDoor();
        alex.turnAround();
        alex.move();
        alex.turnRight();
        alex.pickUpAndMove(3);
        alex.finish();
        karel.move();   
        karel.move();
        karel.move();
        karel.move();
        karel.move();
        karel.turnRight();
        karel.move();
        karel.move();
        karel.move();
        karel.move();
        karel.move();
        karel.move();
        karel.finish();
    }
}