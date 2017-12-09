package instructions;

import utils.*;

public class Execute{
    public void main(World world){
        //         position x, y, direction
        MyRobot karel = new MyRobot(1, 1, "RIGHT");
        karel.land(world);
        karel.pickUp();
        karel.turnRight();
        karel.push();
        karel.turnLeft();
        karel.moveLong();
        karel.turnRight();
        karel.moveLong();
        karel.push();
        karel.move();
        karel.turnLeft();
        karel.pickUp();
        karel.moveLong();
        karel.turnLeft();
        karel.move();
       karel.finish();
        
    }
}