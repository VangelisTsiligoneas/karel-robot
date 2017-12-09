/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objectControl;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 *
 * @author Vaggos
 */
public class UnlockableDoor extends Door implements Savable{
    private int keyholeNumber;
    
    private boolean opening;

    public UnlockableDoor() {
        super();
        keyholeNumber = 1;
        opening = false;
    }
    
    
    public UnlockableDoor(CollisionShape shape, Vector3f direction) {
        super(shape, direction);
        this.keyholeNumber = 1;
        opening = false;
    }
    
     public int getKeyholeNumber() {
        return keyholeNumber;
    }

    public void setKeyholeNumber(int keyholeNumber) {
        this.keyholeNumber = keyholeNumber;
    }

    public boolean isOpening() {
        return opening;
    }
    

    @Override
    public void unlock() {
        super.unlock(); //To change body of generated methods, choose Tools | Templates.
       Node door = (Node) spatial;
       Geometry keyhole = (Geometry) door.getChild("keyhole");
       keyhole.getMaterial().setColor("Diffuse", ColorRGBA.Yellow);
    }

    @Override
    public void open() {        
        if(unlocked && !isOpened()){
            opening = true;
        }        
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        if (opening) {
            Vector3f position = spatial.getLocalTranslation().clone();
            Vector3f target = position.setY(1f);
            float distance = spatial.getLocalTranslation().distance(target);


            if (distance < 0.1) {
                spatial.setLocalTranslation(target);
                opening = false;
                opened = true;
            } else {
                spatial.setLocalTranslation(spatial.getLocalTranslation().interpolate(target, (1 * tpf) / distance));
            }
        }
    }

    @Override
     public Control cloneForSpatial(Spatial spatial) {
     UnlockableDoor control = new UnlockableDoor(collisionShape, direction);
     control.setKeyholeNumber(keyholeNumber);
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
        capsule.write(keyholeNumber, "keyholeNumber", 1);
        capsule.write(opening, "isOpening", false);        
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        keyholeNumber = capsule.readInt("keyholeNumber", 1);
        opening = capsule.readBoolean("isOpening", false);
    }

    

    
}
