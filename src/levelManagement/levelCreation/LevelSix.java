/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package levelManagement.levelCreation;

import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import minefield.Minefield;
import minefield.Scenario;
import mygame.Direction;
import mygame.Key;

/**
 *
 * @author Vaggos
 */
public class LevelSix extends LevelBuilder{

    @Override
    protected void createLevel() {
        registerLevel(6, "Overriding", "Level_6");
        
        setCamera(new Vector3f(3.9773757f, 11.827892f, 11.864965f), new Vector3f(0.03086904f, -0.84408104f, -0.53532624f));
        
        addCamera(new Vector3f(3.9773757f, 11.827892f, 11.864965f), new Vector3f(0.018567707f, -0.8393356f, -0.54329634f));
        addCamera(new Vector3f(8.258046f, 8.312626f, 9.104587f), new Vector3f(-0.3647508f, -0.8492764f, -0.3816886f));
        addCamera(new Vector3f(4.0883255f, 12.761375f, 9.025849f), new Vector3f(6.392469E-4f, -0.94392926f, -0.3301469f));
        
        ambientLight(0.8f);
        light(new Vector3f(0.42848825f, -0.756137f, 0.49462563f));
        floor(10,10);
        door(6,2, Direction.RIGHT, Key.KEY_1);
        door(7,4,Direction.UP, Key.KEY_2);
        
        key(4,1,Key.KEY_1);
        key(8, 5,Key.KEY_2);
        floorButton(2,2);
        rock(3,2);
        treasure(6,7);
        treasure(4,7);
        treasure(5,2);
        terminalGate(1, 7, Direction.LEFT);
        
        walls();
    }
    
    
    
    private void walls(){
        Material defaultMaterial = defaultWallMaterial();
        Material transparentMaterial = transparentMaterial();
        
        
       wall(1,0,5,0,2,defaultMaterial);
       wall(6,1,9,1,2,defaultMaterial);       
       wall(1,5,5,5,1,transparentMaterial);
       wall(4,8,9,8,1,transparentMaterial);
       wall(0,9,3,9,1,transparentMaterial);
       
       wall(0,5,0,8,2,defaultMaterial);
       wall(1,1,1,4,2,defaultMaterial);
       wall(9,2,9,8,2,defaultMaterial);
        
       wall(6,3,6,3,1,transparentMaterial);
       wall(6,4,6,4,1,transparentMaterial);
       wall(8,4,8,4,1,transparentMaterial);
    }
}
