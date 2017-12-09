
package instructions;

import utils.*;

public class MyRobot extends Robot{

    public MyRobot(int x, int y, String direction){
        super(x, y, direction);
    }

    public void move(){
        super.move();
        super.move();
    }
    
    public void turnAround(){
        turnRight();
        turnRight();
    }

    public void pickUpAndMove(int times){
        for(int i=0;i<times;i++){
            if(inFrontOfATreasure())
                pickUp();
            move();
        }
    }
}