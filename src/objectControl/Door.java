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
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 *
 * @author Vaggos
 */
public class Door extends GhostControl implements Savable{
    protected boolean unlocked;
    protected boolean opened;
    protected Vector3f direction;

    public Door(CollisionShape shape, Vector3f direction) {
        super(shape);
        unlocked = false;
        opened = false;
        
        this.direction = direction;
    }

    public Door() {
        unlocked = false;
        opened = false;
        direction = Vector3f.UNIT_X;
    }
    

    public Vector3f getDirection() {
        return direction;
    }
    
    

    public boolean isUnlocked() {
        return unlocked;
    }
    
    public boolean isOpened() {
        return opened;
    }

    public void unlock() {
        unlocked = true;
    }

    public void open(){
        if(unlocked){
            opened = true;
        }
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial); //To change body of generated methods, choose Tools | Templates.
        this.spatial.lookAt(spatial.getWorldTranslation().add(direction), Vector3f.UNIT_Y);
    }
    
    @Override
     public Control cloneForSpatial(Spatial spatial) {
     Door control = new Door(collisionShape, direction);
     control.setCcdMotionThreshold(getCcdMotionThreshold());
     control.setCcdSweptSphereRadius(getCcdSweptSphereRadius());
     control.setCollideWithGroups(getCollideWithGroups());
     control.setCollisionGroup(getCollisionGroup());
     control.setPhysicsLocation(getPhysicsLocation());
     control.setPhysicsRotation(getPhysicsRotationMatrix());
      control.setApplyPhysicsLocal(isApplyPhysicsLocal());
      return control;
      }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(unlocked, "unlocked", false);
        capsule.write(opened, "opened", false);
        capsule.write(direction, "direction", Vector3f.UNIT_X);
        /*
         *  protected boolean unlocked;
    protected boolean opened;
    private Vector3f direction;
         */
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        unlocked = capsule.readBoolean("unlocked", false);
        opened = capsule.readBoolean("opened", false);
        direction = (Vector3f) capsule.readSavable("direction", Vector3f.UNIT_X);
    }

    
    
    
}
