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
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.io.IOException;

/**
 *
 * @author Vaggos
 */
public abstract class TakeableObject extends GhostControl implements Savable{
    protected float speed;

    public TakeableObject() {
        super();
        speed = 1f;
    }

    public TakeableObject(CollisionShape shape) {
        super(shape);
        speed = 1f;
    }
    

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    public void spin(float tpf) {
        Quaternion rotation = new Quaternion().fromAngleAxis(tpf * speed, Vector3f.UNIT_Y);
        spatial.rotate(rotation);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf); //To change body of generated methods, choose Tools | Templates.
        spin(tpf);
        
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(speed, "speed", 1f);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        speed = capsule.readFloat("speed", 1f);
    }
    
    
    abstract public void pickedUp(CharacterControl fromcharacter);
}
