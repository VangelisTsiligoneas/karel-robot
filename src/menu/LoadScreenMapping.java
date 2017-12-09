/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

/**
 *
 * @author Vaggos
 */
public class LoadScreenMapping implements NiftyInputMapping {

    public NiftyInputEvent convert(KeyboardInputEvent inputEvent) {
        if(inputEvent.isKeyDown()){
            if(inputEvent.getKey() == KeyboardInputEvent.KEY_ESCAPE){
                return NiftyInputEvent.Escape;
            }
            else if(inputEvent.getKey() == KeyboardInputEvent.KEY_RETURN){
                return NiftyInputEvent.SubmitText;
            }
        }
        return null;
    }
    
}
