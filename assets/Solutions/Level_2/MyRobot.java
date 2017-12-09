
package instructions;

import utils.*;

public class MyRobot extends Robot{

    public MyRobot(int x, int y, String direction){
        super(x, y, direction);
    }

    public void moveLong(){
        move();
        move();
        move();
        move();
    }
}