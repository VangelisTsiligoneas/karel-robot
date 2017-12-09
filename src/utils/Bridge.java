/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.math.Vector3f;
import objectControl.CharacterControl;

/**
 *
 * @author Vaggos
 * it connectes the command thread with the main thread
 */
public class Bridge {
    public static final int NULL_COMMAND = 0;
    public static final int IN_FRONT_OF_A_GATE = 7;
    public static final int INSIDE_A_GATE = 8;
    public static final int IN_FRONT_OF_AN_OBSTACLE = 6;
    public static final int IN_FRONT_OF_A_DOOR = 1;
    public static final int PICK_UP = 2;
    public static final int UNLOCK = 3;
    public static final int OPEN = 4;
    public static final int FINISH = 9;
    public static final int IN_FRONT_OF_A_CUBE = 10;
    public static final int IN_FRONT_OF_A_KEY = 11;
    public static final int IN_FRONT_OF_A_TREASURE = 12;
  
    public static final int PUSH = 5;
    
    private CharacterControl character;
    /*when is true then the command thread has the green light to send the next command*/
    private boolean done;
    private int message;
    private boolean result;

    public Bridge() {        
        done=true;
        message=0;
    }

    public void setCharacter(CharacterControl character) {
        this.character = character;
    }    
    
    /*
     * called by command thread
     */
    public synchronized void waitTillDone(){
        while(!done){
            
            try{
                wait();
            }catch(InterruptedException e){}
        }
        done=false;        
    }
    
    /*
     * called by main thread
     */
    public synchronized void setDone(){        
        done=true;
        
        notifyAll();
    }
    
    /*
     * called by main thread
     */
    public synchronized boolean getDone(){
        return done;
    }
    
    /*
     * called by command thread
     */
    public synchronized void setUndone(){
        done=false;
        
        notifyAll();
    }
    
    /*
     * called by command thread
     */
    public synchronized void move(){
        waitTillDone();
        character.move();
    }
    
    /*
     * called by command thread
     */
    public synchronized void turnLeft(){
        waitTillDone();
        character.turnleft();
    }
    
    public synchronized void turnRight(){
        waitTillDone();
        character.turnRight();
    }
    
    public synchronized void pickUp(){
        execute(PICK_UP);        
    }
    
    public synchronized void unLock(){
        execute(UNLOCK);
    }
    
    public synchronized void openDoor(){
        execute(OPEN);
    }
    
    public synchronized void push(){
        execute(PUSH);
    }
    
    public synchronized void finish(){
        execute(FINISH);
    }
    
    /*
     * called by commandThread
     */
    public synchronized boolean inFrontOfADoor(){
        return executeWithResult(IN_FRONT_OF_A_DOOR);
    }
    
    public synchronized boolean inFrontOfATresure(){
        return executeWithResult(IN_FRONT_OF_A_TREASURE);
    }
    
    public synchronized boolean inFrontOfAKey(){
        return executeWithResult(IN_FRONT_OF_A_KEY);
    }
    
    /*
     * called by commandThread & main thread
     */
    public synchronized void giveCommand(int message){
        this.message = message;       
    }
    
    
    /*
     * called by main thread
     */
    public synchronized int takeMessage(){
        return message;
    }
    
    /*
     * called by main thread
     */
    public synchronized void returnResult(boolean result){
        this.result = result;
        //notifyAll();
    }
    
    /*
     * called by command thread
     */
    public synchronized boolean inFrontOfAnObstacle(){
        return executeWithResult(IN_FRONT_OF_AN_OBSTACLE);
    }
    
    public synchronized boolean inFrontOfAGate() {
        return executeWithResult(IN_FRONT_OF_A_GATE);
    }
    
    public synchronized boolean inFrontOfACube(){
        return executeWithResult(IN_FRONT_OF_A_CUBE);
    }
    
    public synchronized boolean insideAGate(){
        return executeWithResult(INSIDE_A_GATE);
    }
    
    private synchronized void execute(int command){
        waitTillDone();
        giveCommand(command);
    }
    
    private synchronized boolean executeWithResult(int command){
        waitTillDone();
        giveCommand(command);
        waitTillDone();
        setDone();
        
        return result;
    }
}