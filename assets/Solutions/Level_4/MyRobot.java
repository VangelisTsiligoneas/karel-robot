
package instructions;

import utils.*;

public class MyRobot extends Robot{

    public MyRobot(int x, int y, String direction){
        super(x, y, direction);
    }

    public void pushAndMove(){
        if(inFrontOfACube()){
           push();
        }
        move();
    }

    public void turnAround(){
        turnRight();
        turnRight();
    }

    public void pickUpAndMove(){
        if(inFrontOfATreasure()){
            pickUp();
        }
        move();
    }

    public void pickUpAndMoveLong(){
        pickUpAndMove();
        pickUpAndMove();
        pickUpAndMove();
        pickUpAndMove();
    }

    public void moveTwice(){
        move();
        move();
    }

    public void pickUpAtRight(){
        turnRight();
        if(inFrontOfATreasure()){
            pickUp();
        }
        turnLeft();
    }
}