/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import objectControl.CharacterControl;

/**
 *
 * @author Vaggos
 */
public final class Body {
    CharacterControl character;

    public Body(CharacterControl character) {
        this.character = character;
        install(new Bridge());
    }
    
    public void install(Bridge robot){
        character.setRobot(robot);
        
    }
}
