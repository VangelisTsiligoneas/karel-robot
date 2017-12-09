package instructions;

import utils.*;

public class Execute{
    public void main(World world){
        //         position x, y, direction
        MyRobot karel = new MyRobot(1, 1, "RIGHT");
        karel.land(world);

        karel.pickUp();
        karel.moveWhileYouCan();
        karel.push();
        karel.turnRight();
        karel.moveWhileYouCan();
        karel.turnRight();
        karel.moveWhileYouCan();
        karel.turnLeft();

        karel.harvestRowAndTurnLeft(4);
        karel.turnAround();
        karel.turnRight();
        karel.moveWhileYouCan();
        karel.turnRight();
        karel.moveWhileYouCan();
        karel.turnLeft();
        karel.unLock();
        karel.openDoor();
        karel.move(2);
        karel.pushAtLeft();
        karel.move();
        karel.finish();        
    }
}

