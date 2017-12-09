package instructions;

import utils.*;

public class Execute{
    public void main(World world){
        //         position x, y, direction
        Robot karel = new Robot(1, 1, "RIGHT");
        karel.land(world);

    }
}

