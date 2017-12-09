
package instructions;

import utils.*;

public class MyRobot extends Robot{

    public MyRobot(int x, int y, String direction){
        super(x, y, direction);
    }

    public void moveWhileYouCan(){
        while(!inFrontOfAnObstacle()){
            move();
        }
    }

    public void harvestRow(){
        while(!inFrontOfAnObstacle()){
            if(inFrontOfATreasure()){
                pickUp();
            }
            move();
        }
    }

    public void harvestRowAndTurnLeft(int times){
        for(int i = 0;i < times; i++){
            harvestRow();
            turnLeft();
        }
    }

    public void move(int steps){
        for(int i=0;i<steps;i++){
            move();
        }
    } 

    public void turnAround(){
        turnRight();
        turnRight();
    }

    public void pushAtLeft(){
        turnLeft();
        if(inFrontOfACube()){
            push();
        }
        turnRight();
    }
}
