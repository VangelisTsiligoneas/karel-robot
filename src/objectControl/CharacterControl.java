/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objectControl;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.collision.CollisionResults;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import utils.Bridge;
import levelManagement.levelCreation.LevelBuilder;
import mygame.Direction;

/**
 *
 * @author Vaggos
 */
public class CharacterControl extends MoveableObject implements Savable {
    
    public static final String NO_DOOR_MESSAGE = "There's no DOOR! Your robot is crashed!";
    public static final String NO_KEY_MESSAGE = "your robot doesn't have the key for this DOOR!";
    public static final String NO_TAKEABLE_MESSAGE = "There's nothing to pick up! Your robot is crashed!";
    public static final String NO_CUBE_MESSAGE = "There's nothing to push! Your robot is crashed!";
    public static final String CRASH_MESSAGE = "Your robot hit an obstacle! The alarm turned On and the TERMINAL GATE is sealed!";
    public static final String CANT_FINISH_MESSAGE = "the TERMINAL GATE is sealed! you can't finish";

    private boolean rotatingLeft;
    private boolean rotatingRight;
    private int score;
    private ArrayList<Integer> keys;
    private UnlockableDoor frontDoor; //the door that is in front of the character
    private Rock frontRock;
    private Bridge bridge;
    private float halfExtend;
    private boolean finish;
    private boolean on;
    private String warningMessage;

    public CharacterControl(CollisionShape shape, Vector3f direction) {
        super(shape, direction);
        halfExtend = 0.3f;
        CylinderCollisionShape cylinderShape = (CylinderCollisionShape) shape;
        if (cylinderShape instanceof CylinderCollisionShape) {
            halfExtend = cylinderShape.getHalfExtents().x;
        }
        score = 0;
        keys = new ArrayList<Integer>();
        rotatingLeft = false;
        rotatingRight = false;
        bridge = new Bridge();
        bridge.setCharacter(this);
        finish = false;
        on = true;
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        CharacterControl control = new CharacterControl(collisionShape, direction);
        control.setCcdMotionThreshold(getCcdMotionThreshold());
        control.setCcdSweptSphereRadius(getCcdSweptSphereRadius());
        control.setCollideWithGroups(getCollideWithGroups());
        control.setCollisionGroup(getCollisionGroup());
        control.setPhysicsLocation(getPhysicsLocation());
        control.setPhysicsRotation(getPhysicsRotationMatrix());
        control.setApplyPhysicsLocal(isApplyPhysicsLocal());
        return control;
    }

    /*
     * serialization only
     */
    public CharacterControl() {
        finish = false;
        rotatingLeft = false;
        score = 100;
        keys = new ArrayList<Integer>();
    }

    public void addKey(int keyNumber) {
        keys.add(keyNumber);
    }

    @Override
    public void move() {
        if (isStill() && on) {
            moving = true;
        }
    }

    public void turnleft() {
        if (isStill() && on) {
            Quaternion rotateLeft = new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y);
            target = rotateLeft.mult(direction).normalize().add(spatial.getLocalTranslation());
            rotatingLeft = true;
        }
    }

    public void turnRight() {
        if (isStill() && on) {
            Quaternion rotateLeft = new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y);
            target = rotateLeft.mult(direction).normalize().add(spatial.getLocalTranslation());
            rotatingRight = true;
        }
    }

    public void pickup() {
        if(!on){
            return;
        }
        Node rootNode = findRootNode();
        Node takeableNode = (Node) rootNode.getChild(LevelBuilder.TAKEABLE_NODE);
        List<Spatial> takeableObjects = getTakeableObjects();
        boolean foundTakeble = false;
        for (Spatial takeableObject : takeableObjects) {
            Vector3f takeablePosition = takeableObject.getLocalTranslation();

            if (takeablePosition.equals(target)) {
                TakeableObject takeableControl = takeableObject.getControl(TakeableObject.class);
                takeableControl.pickedUp(this);
                takeableNode.detachChild(takeableObject);
                foundTakeble = true;
                break;
            }
        }
        if(!foundTakeble){
            warningMessage = NO_TAKEABLE_MESSAGE;
            on = false;
        }
    }

    public void push() {
        if (isStill() && on) {
            Node moveableNode = (Node) findRootNode().getChild(LevelBuilder.MOVEABLE_NODE);
            boolean foundRock = false;
            CollisionResults results = new CollisionResults();

            Ray ray = new Ray(spatial.getWorldTranslation(), direction);
            ray.setLimit(1f);
            moveableNode.collideWith(ray, results);

            for (int i = 0; i < results.size(); i++) {

                Spatial moveableSpatial = results.getCollision(i).getGeometry();
                String name = moveableSpatial.getName();

                if (name.equals("rock") && moveableSpatial.getLocalTranslation().equals(target)) {
                    frontRock = moveableSpatial.getControl(Rock.class);
                    frontRock.setTarget(direction);
                    frontRock.move();
                    foundRock = true;
                }
            }
            if(!foundRock){
                warningMessage = NO_CUBE_MESSAGE;
                on = false;
            }
        }
    }

    public void unlockDoor() {
        if (isStill() && on) {
            Spatial door = getDoor();

            if (door != null) {
                UnlockableDoor unlockableDoor = door.getControl(UnlockableDoor.class);

                if (unlockableDoor.getDirection().equals(direction)) {
                    int keyholeNumber = unlockableDoor.getKeyholeNumber();

                    if (keys.contains(keyholeNumber)) {
                        unlockableDoor.unlock();
                    }
                    else{
                        warningMessage = NO_KEY_MESSAGE;
                    }
                }
            }
            else{
                warningMessage = NO_DOOR_MESSAGE;
                on = false;
            }
        }
    }

    public void openDoor() {
        if (isStill() && on) {
            Spatial door = getDoor();

            if (door != null) {
                frontDoor = door.getControl(UnlockableDoor.class);

                if (frontDoor.getDirection().equals(direction)) {
                    

                    if (frontDoor.isUnlocked()) {
                        frontDoor.open();
                    }
                }
            }
            else{
                warningMessage = NO_DOOR_MESSAGE;
                on = false;
            }
        }
    }

    protected List<Spatial> getTakeableObjects() {
        List<Spatial> takeableObjects = new ArrayList<Spatial>();

        Node rootNode = findRootNode();
        Node takeableNode = (Node) rootNode.getChild(LevelBuilder.TAKEABLE_NODE);
        List<Spatial> children = takeableNode.getChildren();

        for (Spatial child : children) {
            if ((child.getName().equals(LevelBuilder.TREASURE) || child.getName().equals(LevelBuilder.KEY)) && child.getLocalTranslation().equals(target)) {
                takeableObjects.add(child);
            }
        }
        return takeableObjects;
    }

    /*returns the rock that is in front of the character*/
    private Spatial getRock() {
        Spatial rock = null;

        Node rootNode = findRootNode();
        Node objectNode = (Node) rootNode.getChild(LevelBuilder.OBJECT_NODE);
        List<Spatial> children = objectNode.getChildren();

        for (Spatial child : children) {
            if (child.getName().equals("rock") && child.getLocalTranslation().equals(target)) {
                rock = child;
            }
        }
        return rock;
    }

    private Spatial getDoor() {
        Spatial door = null;

        Node rootNode = findRootNode();
        Node objectNode = (Node) rootNode.getChild(LevelBuilder.OBJECT_NODE);
        List<Spatial> children = objectNode.getChildren();

        for (Spatial child : children) {
            if (child.getName().equals("doorNode") && child.getLocalTranslation().equals(target)) {
                door = child;
            }
        }
        return door;
    }

    public boolean inFrontOfADoor() {
        boolean flag = false;

        Node rootNode = findRootNode();
        Node objectNode = (Node) rootNode.getChild(LevelBuilder.OBJECT_NODE);
        List<Spatial> children = objectNode.getChildren();

        for (Spatial child : children) {
            if (child.getName().equals("doorNode") && child.getLocalTranslation().equals(target)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
    
    public boolean inFrontOfACube() {
        boolean flag = false;

        Node rootNode = findRootNode();
        Node objectNode = (Node) rootNode.getChild(LevelBuilder.MOVEABLE_NODE);
        List<Spatial> children = objectNode.getChildren();

        for (Spatial child : children) {
            if (child.getLocalTranslation().equals(target)) {
                System.out.println("target's location = true");
                if (child.getName().equals("rock")) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }
    
    public boolean inFrontOfAKey() {
        Node takeableNode = (Node) findRootNode().getChild(LevelBuilder.TAKEABLE_NODE);
        List<Spatial> children = takeableNode.getChildren();
        Vector3f worldTarget = spatial.getWorldTranslation().add(direction);

        for (Spatial child : children) {
            if (child.getName().equals(LevelBuilder.KEY)) {
                if (child.getWorldTranslation().equals(worldTarget)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean inFrontOfATreasure(){
        Node takeableNode = (Node) findRootNode().getChild(LevelBuilder.TAKEABLE_NODE);
        List<Spatial> children = takeableNode.getChildren();
        Vector3f worldTarget = spatial.getWorldTranslation().add(direction);

        for (Spatial child : children) {
            if (child.getName().equals(LevelBuilder.TREASURE)) {
                if (child.getWorldTranslation().equals(worldTarget)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean inFrontOfAGate() {
        Node rootNode = findRootNode();
        CollisionResults results = new CollisionResults();

        Ray ray = new Ray(spatial.getWorldTranslation().add(direction.mult(halfExtend)), direction);
        ray.setLimit(1f);
        int collideWith = rootNode.collideWith(ray, results);
        if (collideWith > 0) {
            for (int i = 0; i < results.size(); i++) {
                String name = results.getCollision(i).getGeometry().getName();
                if (name.equals("gate")) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean insideAGate() {
        boolean flag = false;
        Node rootNode = findRootNode();
        Spatial terminalGate = rootNode.getChild(LevelBuilder.TERMINAL_GATE);
        if (terminalGate.getLocalTranslation().equals(spatial.getLocalTranslation())) {
            flag = true;
        }
        return flag;
    }

    public boolean inFrontOfAnObstacle() {
        boolean flag = false;
        Node rootNode = findRootNode();
        CollisionResults results = new CollisionResults();
        Vector3f origin = spatial.getWorldTranslation().add(direction.mult(0.5f));
        Ray ray = new Ray(origin, direction);
        ray.setLimit(1f - halfExtend);
        int collideWith = rootNode.collideWith(ray, results);
        if (collideWith > 0) {
            for (int i = 0; i < results.size(); i++) {
                String name = results.getCollision(i).getGeometry().getName();
                if (name.equals("Cube.0101")) {
                    continue;
                }
                if (name.equals("gate")) {
                    return false;
                }
                if (!name.equals(LevelBuilder.TREASURE) && !name.equals(LevelBuilder.KEY)) {
                    return true;
                }
            }
        }
        return flag;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public int getScore() {
        return score;
    }

    public Bridge getBridge() {
        return bridge;
    }

    public void setRobot(Bridge robot) {
        this.bridge = robot;
        robot.setCharacter(this);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if (isStill()) {
            if (bridge.takeMessage() == (Bridge.IN_FRONT_OF_AN_OBSTACLE)) {
                bridge.returnResult(inFrontOfAnObstacle());
                endOfTask();
            } else if (bridge.takeMessage() == Bridge.IN_FRONT_OF_A_GATE) {
                bridge.returnResult(inFrontOfAGate());
                endOfTask();
            } else if (bridge.takeMessage() == Bridge.INSIDE_A_GATE) {
                bridge.returnResult(insideAGate());
                endOfTask();
            } else if (bridge.takeMessage() == (Bridge.PICK_UP)) {
                pickup();
                endOfTask();
            } else if (bridge.takeMessage() == (Bridge.IN_FRONT_OF_A_DOOR)) {
                bridge.returnResult(inFrontOfADoor());
                endOfTask();
            } 
            else if(bridge.takeMessage() == Bridge.IN_FRONT_OF_A_CUBE){
                bridge.returnResult(inFrontOfACube());
                endOfTask();
            }
            else if(bridge.takeMessage() == Bridge.IN_FRONT_OF_A_TREASURE){
                bridge.returnResult(inFrontOfATreasure());
                endOfTask();
            }
            else if(bridge.takeMessage() == Bridge.IN_FRONT_OF_A_KEY){
                bridge.returnResult(inFrontOfAKey());
                endOfTask();
            }
            else if (bridge.takeMessage() == (Bridge.OPEN)) {
                if(frontDoor == null)
                openDoor();
                if (!on || frontDoor.isOpened()) {
                    frontDoor = null;
                    endOfTask();
                }

            } else if (bridge.takeMessage() == (Bridge.PUSH)) {
                //System.out.println("push loop");
                if (frontRock == null) {
                    //System.out.println("push once");
                    push();
                } else {
                    if (!frontRock.isMoving()) {
                        frontRock = null;
                        endOfTask();
                    }
                }


            } else if (bridge.takeMessage() == (Bridge.UNLOCK)) {
                unlockDoor();
                endOfTask();
            } else if (bridge.takeMessage() == Bridge.FINISH) {
                finish();
                endOfTask();
            }

        } else {
            if (rotatingLeft) {
                Vector3f newDirection = target.subtract(spatial.getLocalTranslation());
                if (direction.distance(newDirection) < 0.1) {
                    spatial.lookAt(spatial.getWorldTranslation().add(newDirection), Vector3f.UNIT_Y);
                    direction = newDirection;
                    rotatingLeft = false;
                    endOfTask();
                } else {
                    Quaternion rotation = new Quaternion().fromAngleAxis(tpf * getSpeed(), Vector3f.UNIT_Y);
                    spatial.rotate(rotation);
                    direction = spatial.getLocalRotation().mult(new Vector3f(0, 0, 1));
                }
            } else if (rotatingRight) {
                Vector3f newDirection = target.subtract(spatial.getLocalTranslation());
                if (direction.distance(newDirection) < 0.1) {
                    spatial.lookAt(spatial.getWorldTranslation().add(newDirection), Vector3f.UNIT_Y);
                    direction = newDirection;
                    rotatingRight = false;
                    endOfTask();
                } else {
                    Quaternion rotation = new Quaternion().fromAngleAxis(-tpf * getSpeed(), Vector3f.UNIT_Y);
                    spatial.rotate(rotation);
                    direction = spatial.getLocalRotation().mult(new Vector3f(0, 0, 1));
                }
            }
        }
    }

    public boolean isFinished() {
        return finish;
    }

    public boolean isStill() {
        return !moving && !rotatingLeft && !rotatingRight;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(rotatingLeft, "rotating", false);
        capsule.write(score, "score", 100);
        capsule.write(finish, "finish", false);
        capsule.writeSavableArrayList(keys, "keys", new ArrayList<Integer>());
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        rotatingLeft = capsule.readBoolean("rotating", false);
        score = capsule.readInt("score", 100);
        finish = capsule.readBoolean("finish", false);
        keys = capsule.readSavableArrayList("keys", new ArrayList<Integer>());
    }

    protected void endOfTask() {
        bridge.giveCommand(0);
        bridge.setDone();
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);

    }

    public void finish() {
        TerminalGate gate = findRootNode().getChild(LevelBuilder.TERMINAL_GATE).getControl(TerminalGate.class);
        if (insideAGate()) {
            if(gate.isOpened()){
                finish = true;
            }
            else{
                warningMessage = CANT_FINISH_MESSAGE;
            }
        }
    }

    @Override
    public void reactionToCrush() {
        super.reactionToCrush();
        warningMessage = CRASH_MESSAGE;
        on = false;
    }
    

    public void setScore(int score) {
        this.score = score;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public boolean isOn() {
        return on;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }
}
