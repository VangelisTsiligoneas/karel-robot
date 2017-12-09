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
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 *
 * @author Vaggos
 */
public class KeyControl extends TakeableObject implements Savable{
    int keyNumber;

    public KeyControl(CollisionShape shape) {
        super(shape);
        keyNumber = 0;
    }
    
    

    public int getKeyNumber() {
        return keyNumber;
    }

    public void setKeyNumber(int keyNumber) {
        this.keyNumber = keyNumber;
    }

    public KeyControl() {
        super();
        keyNumber = 0;
    }
    

    @Override
    public void pickedUp(CharacterControl fromcharacter) {
        fromcharacter.addKey(keyNumber);
    }
    
    @Override
     public Control cloneForSpatial(Spatial spatial) {
     KeyControl control = new KeyControl(collisionShape);
     control.setKeyNumber(keyNumber);
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
        capsule.write(keyNumber, "keyNumber", 0);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        keyNumber = capsule.readInt("keyNumber", 0);
    }
}
