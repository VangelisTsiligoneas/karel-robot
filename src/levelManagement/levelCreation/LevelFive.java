/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package levelManagement.levelCreation;

import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import minefield.Minefield;
import minefield.Scenario;
import mygame.Direction;
import mygame.Key;

/**
 *
 * @author Vaggos
 */
public class LevelFive extends LevelBuilder{

    @Override
    protected void createLevel() {
        registerLevel(5, "while", "Level_5");
        setCamera(new Vector3f(6.7406244f, 13.145983f, 13.435343f), new Vector3f(0.012159588f, -0.8049035f, -0.59328127f));
        
        ambientLight(0.8f);
        light(new Vector3f(0.012159588f, -0.8049035f, -0.59328127f));
        
        
        addCamera(new Vector3f(6.7406244f, 13.145983f, 13.435343f), new Vector3f(0.012159588f, -0.8049035f, -0.59328127f));
        
        addCamera(new Vector3f(0.89947695f, 6.944717f, 4.026077f), new Vector3f(0.41954005f, -0.7973029f, -0.4339286f));        
        addCamera(new Vector3f(15.931771f, 6.5252304f, 1.0340253f), new Vector3f(-0.7545075f, -0.60234874f, 0.26056576f));
        addCamera(new Vector3f(1.81631f, 6.720568f, 8.578846f), new Vector3f(0.0987523f, -0.92371976f, -0.37012148f));
        //addCamera(new Vector3f(5.8745756f, 14.80647f, 5.7260714f), new Vector3f(3.2359012f, -1f, -0.047381043f));
        
        floor(15, 10, defaultWallMaterial());
        walls();
        
        key(2, 1, Key.KEY_1);
        door(5,8, Direction.RIGHT, Key.KEY_1);
        rock(6, 7);
        rock(14,1);
        floorButton(6, 6);
        floorButton(15,1);        
        treasure(1,5,4,5);
        treasure(1,6,1,7);
        treasure(4,6,4,7);
        treasure(1,8,4,8);
        terminalGate(7, 8, Direction.RIGHT);
        //character(1,1,Direction.RIGHT);
    }
    
    protected void treasure(int fromX, int fromY,int toX,int toY){
        for(int x = fromX;x<=toX;x++){
            for(int y=fromY;y<=toY;y++){
                treasure(x, y);
            }
        }
    }
    
    private void walls(){
        Material defaultMaterial = defaultWallMaterial();
        Material transparentMaterial = transparentMaterial();
        
        wall(new Vector2f(0,0), new Vector2f(15,0), 2,defaultMaterial);
        wall(16,1,16,1,1,defaultMaterial);
        wall(15,2,15,2,1,defaultMaterial);
        wall(new Vector2f(1,2), new Vector2f(12,2), 1,transparentMaterial);
        wall(new Vector2f(0,1), new Vector2f(0,9), 2,defaultMaterial);
        wall(new Vector2f(1,9), new Vector2f(9,9), 2,transparentMaterial);
        wall(new Vector2f(3,4), new Vector2f(14,4), 2,transparentMaterial);
        wall(new Vector2f(14,2), new Vector2f(14,3), 1,defaultMaterial);
        wall(new Vector2f(5,5), new Vector2f(5,7), 2,transparentMaterial);
        wall(new Vector2f(10,5), new Vector2f(10,9), 2,defaultMaterial);
    }
    
}
