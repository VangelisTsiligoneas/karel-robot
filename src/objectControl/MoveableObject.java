/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objectControl;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.IOException;
import levelManagement.levelCreation.LevelBuilder;

/**
 *
 * @author Vaggos
 */
public abstract class MoveableObject extends GhostControl implements Savable{
    protected Vector3f direction;
    protected Vector3f target;
    protected boolean moving;
    
    protected float speed;

    public MoveableObject(CollisionShape shape, Vector3f direction) {
        super(shape);
        this.direction = direction;
        moving = false;
        speed = 1f;
        
    }

    public boolean isMoving() {
        return moving;
    }

    
    
    
/*
 * serialization only. Do not use
 */
    public MoveableObject() {
        direction = new Vector3f();
        target = new Vector3f();
        moving = false;
        speed = 1f;
    }
    

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial); //To change body of generated methods, choose Tools | Templates.
        this.spatial.lookAt(spatial.getWorldTranslation().add(direction), Vector3f.UNIT_Y);
        target = spatial.getLocalTranslation().add(direction);
    }
    
    public void move(){
        if(!moving){
            moving = true;
        }        
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if (moving) {
            float distance = spatial.getLocalTranslation().distance(target);
            boolean passed = false;
            Vector3f origin = target.subtract(direction);
            if(spatial.getLocalTranslation().distance(origin)> 1){passed = true;System.out.println("passed!");}

            if (distance < 0.1 || passed) {
                spatial.setLocalTranslation(target);
                target = spatial.getLocalTranslation().add(direction);
                moving = false;
                endOfTask();
            } else {
                spatial.setLocalTranslation(spatial.getLocalTranslation().interpolate(target, (speed * tpf) / distance));
            }
            if (getOverlappingCount() > 0) {
                reactionToCrush();
                moving = false;
            }
        }
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    protected Node findRootNode(){
        Node parent = spatial.getParent();
        if(parent == null){return (Node) spatial;}
        while(parent.getParent() != null){
            parent = parent.getParent();
        }
        return parent;
    }

    public void reactionToCrush(){
        Vector3f destination = target.subtract(direction);
        spatial.setLocalTranslation(destination);
        target = spatial.getLocalTranslation().add(direction);
        Node rootNode = findRootNode();
        TerminalGate gate = rootNode.getChild(LevelBuilder.TERMINAL_GATE).getControl(TerminalGate.class);
        gate.seal();
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(direction, "direction", Vector3f.UNIT_X);
        capsule.write(target, "target", new Vector3f());
        capsule.write(moving, "moving", false);
        capsule.write(speed, "speed", 1f);
        
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        direction = (Vector3f) capsule.readSavable("direction", Vector3f.UNIT_X);
        target = (Vector3f) capsule.readSavable("target", Vector3f.UNIT_X);
        moving = capsule.readBoolean("moving", false);
        speed = capsule.readFloat("speed", 1f);
       
    }
    
    abstract protected void endOfTask();
        
}
