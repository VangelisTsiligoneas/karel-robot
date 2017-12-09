/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objectControl;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.export.Savable;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

/**
 *
 * @author Vaggos
 */
public class Rock extends MoveableObject implements Savable{

    public Rock() {
    }
    

    public Rock(CollisionShape shape, Vector3f direction) {
        super(shape, direction);
        
    }
    
    public void setTarget(Vector3f direction){
        this.direction = direction;
        target = spatial.getLocalTranslation().add(direction);
    }
    
    @Override
     public Control cloneForSpatial(Spatial spatial) {
     Rock control = new Rock(collisionShape, direction);
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
    protected void endOfTask() {
    
    }
    
    
    
}
