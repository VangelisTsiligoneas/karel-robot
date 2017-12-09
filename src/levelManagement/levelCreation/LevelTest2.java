/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package levelManagement.levelCreation;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import mygame.Direction;

/**
 *
 * @author Vaggos
 */
public class LevelTest2 extends LevelBuilder{

    @Override
    protected void createLevel() {
        ambientLight(0.6f);
        light(new Vector3f(-0.3788616f, -0.42010194f, -0.82460797f));
        light(new Vector3f(26.302763f, 8.493155f, 20.235155f), new Vector3f(-0.7973998f, -0.25776553f, -0.5456284f));
        app.getViewPort().setBackgroundColor(new ColorRGBA(0.93f, 0.9f, 0.05f, 1f));
        setCamera(new Vector3f(4.8319554f, 0.700786f, 6.1634946f), new Vector3f(-0.88425785f, -0.0077796653f, -0.46693444f));
        //floorButton(4, 6);
        //rock(4, 5);
        wall(0,0,0,9,4);
        wall(1,0,9,0,4,transparentMaterial());
        gridNode.detachAllChildren();
        floor(10, 10);
        //character(3,5, Direction.RIGHT);
        //key(6,5, Key.KEY_2);
       // treasure(6,4);
    }
    
}
