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
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.util.SafeArrayList;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Vaggos
 */
public class TerminalGate extends Door implements Savable{
    private SafeArrayList<PressurePlate> switches;

    public TerminalGate() {
        super();
        switches = new SafeArrayList<PressurePlate>(PressurePlate.class);
        unlocked = true;
        opened = false;
    }

    
    public TerminalGate(CollisionShape shape, Vector3f direction) {
        super(shape, direction);
        switches = new SafeArrayList<PressurePlate>(PressurePlate.class);
        unlocked = true;
        opened = false;
    }
    

    @Override
    public void open() {
        super.open();
        Node node = (Node) spatial;
        Geometry led = (Geometry) node.getChild("led");
        led.getMaterial().setColor("Diffuse", ColorRGBA.Green);
    }
    
    public void close() {
        if (isOpened()) {
            opened = false;
            Node node = (Node) spatial;
            Geometry led = (Geometry) node.getChild("led");
            led.getMaterial().setColor("Diffuse", ColorRGBA.Red);
        }
    }

    public void setSwitches(SafeArrayList<PressurePlate> switches) {
        this.switches = switches;
    }
    
    public void seal(){
        unlocked = false;
        opened = false;
        
        Node node = (Node) spatial;
        Geometry body = (Geometry) node.getChild("gate");
        Geometry led = (Geometry) node.getChild("led");
        body.getMaterial().setColor("Diffuse", ColorRGBA.Black);
        led.getMaterial().setColor("Diffuse", ColorRGBA.Red);
    }
    
    private boolean areSwitchesOn() {
        if(isUnlocked()){
            for (PressurePlate s : switches) {
            if (!s.isPressed()) {
                return false;
            }
        }
        return true;
        }
        return false;
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if (areSwitchesOn()) {
            open();
        } else {
            close();
        }
    }
    
    @Override
     public Control cloneForSpatial(Spatial spatial) {
     TerminalGate control = new TerminalGate(collisionShape, direction);
     control.setSwitches(switches);
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
        capsule.writeSavableArrayList(new ArrayList(switches), "switces", new ArrayList<PressurePlate>());
        
        capsule.write(unlocked, "unlocked", true);
        capsule.write(opened, "opened", false);
        /*
         * unlocked = true;
        opened = false;
         */
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        //children = new SafeArrayList( Spatial.class, 
                                    //  e.getCapsule(this).readSavableArrayList("children", null) );
        switches = new SafeArrayList(GhostControl.class, capsule.readSavableArrayList("switches", new ArrayList<PressurePlate>()));
        
        unlocked = capsule.readBoolean("unlocked", true);
        opened = capsule.readBoolean("opened", false);
    }
    
    
}
