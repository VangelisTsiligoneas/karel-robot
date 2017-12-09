package instructions;

import utils.*;

public class Execute{
    public void main(World world){
        //         position x, y, direction
        Robot karel = new Robot(2, 1, "DOWN");
        karel.land(world);
        karel.move();
        karel.move();
        karel.move();
        karel.turnLeft();
        karel.move();
        karel.move();
        karel.turnRight();
        karel.move();
        karel.finish();
    }
}