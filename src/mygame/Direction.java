/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;

/**
 *
 * @author Vaggos
 */
public enum Direction {
    UP (Vector3f.UNIT_Z.mult(-1f)),
    RIGHT (Vector3f.UNIT_X),
    DOWN (Vector3f.UNIT_Z),
    LEFT (Vector3f.UNIT_X.mult(-1f));
    
    private final Vector3f direction;
    
    Direction(Vector3f direction){
        this.direction = direction;
    }
    
    public Vector3f getDirection(){
        return direction;
    }
    
}
