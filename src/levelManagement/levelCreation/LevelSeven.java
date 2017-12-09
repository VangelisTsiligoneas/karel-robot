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
public class LevelSeven extends LevelBuilder{

    @Override
    protected void createLevel() {
        registerLevel(6, "Overriding", "Level_6");
        
        
        
        ambientLight(0.7f);
        light(new Vector3f(-0.5f,-0.8f,0.3f));
        
        setCamera(new Vector3f(8.763581f, 13.987906f, 11.892691f), new Vector3f(-0.0027948301f, -0.87966734f, -0.47558105f));
        
        addCamera(new Vector3f(8.763581f, 13.987906f, 11.892691f), new Vector3f(-0.0027948301f, -0.87966734f, -0.47558105f));
        addCamera(new Vector3f(0.80496335f, 5.935927f, 8.968269f), new Vector3f(0.22703676f, -0.8673085f, -0.4429791f));
        addCamera(new Vector3f(10.886566f, 7.50428f, 6.124617f), new Vector3f(-0.39775342f, -0.79434675f, -0.4591354f));        
        addCamera(new Vector3f(6.378774f, 8.622911f, 3.0402212f), new Vector3f(0.61572516f, -0.78475964f, 0.07095587f));        
        addCamera(new Vector3f(9.293155f, 6.92329f, 2.8522031f), new Vector3f(-0.37896577f, -0.79612184f, 0.4717785f));
        addCamera(new Vector3f(6.6066713f, 13.392407f, 7.309381f), new Vector3f(0.0010587135f, -0.98345476f, -0.1811508f));        
        
        Material defaultMaterial = defaultWallMaterial();        
        
        floor(19,10,defaultMaterial);        
        
        minefield(new Vector2f(12,2), minefield());
        
        walls();
        treasures();
        key(1, 4, Key.KEY_1);
        key(12,5,Key.KEY_2);
        rock(2, 6);
        floorButton(1, 6);
        door(4, 4, Direction.RIGHT, Key.KEY_1);
        door(11,4, Direction.UP, Key.KEY_2);
        //character(3, 6, Direction.LEFT);
        terminalGate(5, 6, Direction.LEFT);
    }
    
    private void treasures(){
        for(int x=15;x<=17;x++){
            for(int y=3;y<=6;y++){
                treasure(x, y);
            }
        }
        treasure(2, 5);
        treasure(2, 7);
        treasure(10, 2);
    }
    
    private Minefield minefield() {
        Minefield m = new Minefield(1, 2);
        Scenario s1 = new Scenario();
        Scenario s2 = new Scenario();
        s1.addInvisibleWall(new Vector2f(0, 0), new Vector2f(0, 0));
        s2.addInvisibleWall(new Vector2f(0, 1), new Vector2f(0, 1));
        
        m.addScenario(s1);
        m.addScenario(s2);
        return m;
    }
    
    private void walls(){
        Material defaultMaterial = defaultWallMaterial();
        Material transparentMaterial = transparentMaterial();
        
        wall(new Vector2f(3,0), new Vector2f(13,0),2,defaultMaterial);
        wall(new Vector2f(3,1), new Vector2f(3,2),2,defaultMaterial);
        wall(new Vector2f(0,3), new Vector2f(4,3),2,defaultMaterial);
        wall(new Vector2f(0,4), new Vector2f(0,9),2,defaultMaterial);
        wall(new Vector2f(1,9), new Vector2f(7,9),2,defaultMaterial);
        wall(new Vector2f(4,5), new Vector2f(4,8),2,defaultMaterial);
        wall(new Vector2f(8,8), new Vector2f(8,9),2,defaultMaterial);
        wall(new Vector2f(5,5), new Vector2f(5,5),2,defaultMaterial);
        wall(new Vector2f(13,1), new Vector2f(13,1),2,defaultMaterial);
        wall(new Vector2f(6,4), new Vector2f(8,5),2,transparentMaterial);
        wall(new Vector2f(14,1), new Vector2f(18,1),2,defaultMaterial);
        wall(new Vector2f(13,7), new Vector2f(17,7),2,defaultMaterial);
        wall(new Vector2f(9,8), new Vector2f(12,8),2,transparentMaterial);
        wall(new Vector2f(18,2), new Vector2f(18,7),2,defaultMaterial);
        //wall(new Vector2f(14,2), new Vector2f(17,2),2,transparentMaterial);
        wall(8,4,8,4,2,defaultMaterial);
        wall(9,4,10,4,2,transparentMaterial);
        wall(12,4,13,4,2,transparentMaterial);
        wall(13,5,13,6,2,transparentMaterial);
    }
}
