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
public class Treasure extends TakeableObject implements Savable{
    private int points;

    public Treasure(int points) {
        super();
        this.points = points;
    }

    public Treasure(CollisionShape shape, int points) {
        super(shape);
        this.points = points;
    }    
    
    public Treasure() {
        super();
        points = 100;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public void pickedUp(CharacterControl fromCharacter) {
        fromCharacter.addScore(points);        
    }
    
    @Override
     public Control cloneForSpatial(Spatial spatial) {
     Treasure control = new Treasure(collisionShape, points);
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
        capsule.write(points, "points", 100);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        points = capsule.readInt("points", 100);
    }
    
    
    
    
    
}
