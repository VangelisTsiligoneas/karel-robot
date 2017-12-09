/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.ColorRGBA;

/**
 *
 * @author Vaggos
 */
public enum Key {
    KEY_1 (1, ColorRGBA.Blue),
    KEY_2 (2, ColorRGBA.Green),
    KEY_3 (3, ColorRGBA.Orange),
    KEY_4 (4, ColorRGBA.Magenta),
    KEY_5 (5, ColorRGBA.Brown);
    
    private int keyNumber;
    private ColorRGBA color;
    
    Key(int value, ColorRGBA color){
        this.keyNumber = value;
        this.color = color;
    }

    public int getKeyNumber() {
        return keyNumber;
    }    
    
    public ColorRGBA getColor(){
        return color;
    }
}
