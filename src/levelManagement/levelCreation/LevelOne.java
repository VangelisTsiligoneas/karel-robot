/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package levelManagement.levelCreation;

import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import mygame.Direction;

/**
 *
 * @author Vaggos
 */
public class LevelOne extends LevelBuilder{

    @Override
    protected void createLevel() {
        registerLevel(1, "Learn the basics", "Level_1");
        ambientLight(0.7f);
        light(new Vector3f(0.29124197f, -0.92959875f, -0.22588551f));
        setBasicScore(200);
        
        setCamera(new Vector3f(7.2690783f, 8.297029f, 8.613566f), new Vector3f(-0.1978269f, -0.81746304f, -0.5409423f));
        addCamera(new Vector3f(3.6010115f, 10.285935f, 6.662388f), new Vector3f(-0.003417155f, -0.9369598f, -0.3494202f));
        addCamera(new Vector3f(6.8000507f, 7.3738427f, 7.433185f), new Vector3f(-0.3663508f, -0.80082417f, -0.4737805f));
        
        terminalGate(4, 5, Direction.DOWN);
        
        Material defaultMaterial = defaultWallMaterial();
        Material transparentMaterial = transparentMaterial();
        
        floor(8,8,defaultMaterial);
        
        wall(0,0,7,0,2,defaultMaterial);
        wall(0,7,7,7,2,defaultMaterial);
        
        wall(0,1,0,6,2,defaultMaterial);
        wall(7,1,7,6,2,defaultMaterial);
        
        wall(3,1,5,3,1,transparentMaterial);
        wall(1,5,3,6,1,transparentMaterial);
    }
}
