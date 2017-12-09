/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package levelManagement.levelCreation;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.texture.Texture;
import static levelManagement.levelCreation.LevelBuilder.LIGHTING_MATERIAL;
import minefield.Minefield;
import minefield.Scenario;
import mygame.Direction;
import mygame.Key;

/**
 *
 * @author Vaggos
 */
public class LevelThree extends LevelBuilder {
    
    @Override
    protected void createLevel() {
        registerLevel(3, "if, else", "Level_3");
        ambientLight(0.8f);
        light(new Vector3f(-0.67389333f, -0.5610608f, -0.48070621f));
        setCamera(new Vector3f(4.5028768f, 7.99822f, 10.31189f), new Vector3f(0.074055515f, -0.5771159f, -0.81329775f));
        
        addCamera(new Vector3f(6.0134807f, 10.574681f, 8.235395f), new Vector3f(-0.011972052f, -0.85991645f, -0.51029456f));
        addCamera(new Vector3f(8.7186f, 5.671032f, 3.057044f), new Vector3f(-0.7388507f, -0.63002706f, -0.23909307f));
        addCamera(new Vector3f(13.442337f, 10.378248f, 4.851701f), new Vector3f(-0.5391292f, -0.8060509f, -0.2441752f));
        addCamera(new Vector3f(4.8230186f, 12.718873f, 4.0623107f), new Vector3f(0.007572595f, -0.96083355f, -0.2770226f));
        
        Material defaultMaterial = defaultWallMaterial();
        Material transparentMaterial = transparentMaterial();
        
        floor(13,6,defaultMaterial);
        
        //character(2, 2, Direction.DOWN);
        treasure(4, 2);
        treasure(9, 2);
        key(1, 3, Key.KEY_1);
        door(6, 3, Direction.RIGHT, Key.KEY_1);
        rock(1, 2);
        floorButton(1, 1);
        terminalGate(11, 3, Direction.RIGHT);
        
        
        Minefield m = new Minefield(1, 2);
        Scenario s1 = new Scenario();
        Scenario s2 = new Scenario();
        s1.addInvisibleWall(new Vector2f(0,0), new Vector2f(0,0));
        s2.addInvisibleWall(new Vector2f(0,1), new Vector2f(0,1));
        m.addScenario(s1);
        m.addScenario(s2);
        minefield(new Vector2f(3,2), m);
        minefield(new Vector2f(8,2),m);
        
       wall(0,0,6,0,3,defaultMaterial);
       wall(0,1,0,4,3,defaultMaterial);
       wall(2,1,6,1,2,transparentMaterial);
       wall(6,2,6,2,2,transparentMaterial);
       wall(1,4,6,4,2,transparentMaterial);
       wall(7,5,12,5,2,transparentMaterial);
       wall(7,0,13,0,2,defaultMaterial);
       wall(13,1,13,4,2,defaultMaterial);
    }
}
