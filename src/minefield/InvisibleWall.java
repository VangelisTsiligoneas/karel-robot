/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minefield;

import com.jme3.math.Vector2f;
import java.io.Serializable;

/**
 *
 * @author Vaggos
 */
public class InvisibleWall implements Serializable{
    private Vector2f from;
    private Vector2f to;

    public InvisibleWall(Vector2f from, Vector2f to) {
        this.from = from;
        this.to = to;
    }

    public Vector2f getFrom() {
        return from;
    }

    public Vector2f getTo() {
        return to;
    }
    
    
    
    
}
