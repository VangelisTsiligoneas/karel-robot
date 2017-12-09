/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objectControl;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.collision.CollisionResults;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.io.IOException;
import java.util.List;
import levelManagement.levelCreation.LevelBuilder;

/**
 *
 * @author Vaggos
 */
public class PressurePlate extends GhostControl implements Savable{
    private boolean pressed;    
    private CollisionResults results;

    public PressurePlate() {
        super();
        pressed = false;
        results = new CollisionResults();
    }

    
    public PressurePlate(CollisionShape shape) {
        super(shape);
        results = new CollisionResults();
        pressed = false;
    }
    
    
    public void setPressed(){
        pressed = false;
        Node objectNode = spatial.getParent();
        Node moveableNode = (Node) objectNode.getChild(LevelBuilder.MOVEABLE_NODE);
        Vector3f targetLocation = spatial.getWorldTranslation().mult(new Vector3f(1,0,1)).add(new Vector3f(0,0.5f,0));
        List<Spatial> children = moveableNode.getChildren();
        for(Spatial s : children){
            if(s.getWorldTranslation().equals(targetLocation)){
                pressed = true;
            }
        }
    }

    public boolean isPressed() {
        return pressed;
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf); //To change body of generated methods, choose Tools | Templates.
        boolean wasPressed = pressed;
        setPressed();
        if (pressed && !wasPressed) {
            Geometry geom = (Geometry) spatial;
            geom.getMaterial().setColor("Diffuse", ColorRGBA.Green);
        }
        if (!pressed && wasPressed) {
            Geometry geom = (Geometry) spatial;
            geom.getMaterial().setColor("Diffuse", ColorRGBA.Red);
        }
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial); //To change body of generated methods, choose Tools | Templates.
        //objectNode = (Node) spatial.getParent();
    }
    
    @Override
     public Control cloneForSpatial(Spatial spatial) {
     PressurePlate control = new PressurePlate(collisionShape);
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
        capsule.write(pressed, "pressed", false);
        
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        pressed = capsule.readBoolean("pressed", false);
    }
}
