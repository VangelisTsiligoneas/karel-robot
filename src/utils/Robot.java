/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.logging.Level;
import java.util.logging.Logger;
import levelManagement.LevelLoader;
import levelManagement.levelCreation.LevelBuilder;
import mygame.Direction;
import objectControl.CharacterControl;

/**
 *
 * @author Vaggos
 */
public class Robot {

    private int x;
    private int y;
    private int score;
    private String direction;
    private Spatial spatial;
    private Bridge bridge;

    public Robot(int x, int y, int score, String direction) {
        this.x = x;
        this.y = y;
        this.score = score;
        this.direction = direction;
    }

    public Robot(int x, int y, String direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public synchronized void land(World world) {
        if (spatial == null) {
            spatial = world.getApp().getAssetManager().loadModel("Models/My karel.j3o");
            spatial.setLocalTranslation(new Vector3f(x, 0, y));
            CollisionShape cc = new CylinderCollisionShape(new Vector3f(0.3f, 0.3f, 0.3f), 1);
            CharacterControl character = new CharacterControl(cc, getDirection(direction).getDirection());
            character.setScore(score);
            bridge = character.getBridge();
            spatial.addControl(character);

            world.getApp().getStateManager().getState(LevelLoader.class).setNewCharacter(this);
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Robot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public synchronized void setDone(){
        notifyAll();
    }

    public Spatial getSpatial() {
        return spatial;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getDirection() {
        return direction;
    }

    public static Direction getDirection(String direction) {
        if (direction.equals("UP")) {
            return Direction.UP;
        } else if (direction.equals("RIGHT")) {
            return Direction.RIGHT;
        } else if (direction.equals("DOWN")) {
            return Direction.DOWN;
        } else if (direction.equals("LEFT")) {
            return Direction.LEFT;
        } else {
            return Direction.RIGHT;
        }
    }

    public synchronized void move() {
        bridge.move();
    }

    public synchronized void turnRight() {
        bridge.turnRight();
    }

    public synchronized void turnLeft() {
        bridge.turnLeft();
    }

    public synchronized void pickUp() {
        bridge.pickUp();
    }

    public synchronized void unLock() {
        bridge.unLock();
    }

    public synchronized void openDoor() {
        bridge.openDoor();
    }

    public synchronized void push() {
        bridge.push();
    }

    public synchronized void finish() {
        bridge.finish();
    }

    public synchronized boolean inFrontOfAnObstacle() {
       return bridge.inFrontOfAnObstacle();
    }

    public synchronized boolean inFrontOfADoor() {
        return bridge.inFrontOfADoor();
    }

    public synchronized boolean inFrontOfAGate() {
        return bridge.inFrontOfAGate();
    }

    public synchronized boolean inFrontOfACube() {
        return bridge.inFrontOfACube();
    }
    
    public synchronized boolean inFrontOfAKey(){
        return bridge.inFrontOfAKey();
    }
    
    public synchronized boolean inFrontOfATreasure(){
        return bridge.inFrontOfATresure();
    }

    public synchronized boolean insideAGate() {
        return bridge.insideAGate();
    }
}