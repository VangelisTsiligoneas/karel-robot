/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package levelManagement.levelCreation;

import utils.Bridge;
import minefield.Scenario;
import minefield.Minefield;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.Texture;
import mygame.Direction;
import mygame.Key;
import objectControl.CharacterControl;

/**
 *
 * @author Vaggos
 */
public final class LevelTest  extends LevelBuilder{
    Bridge b;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
       b = getCharacter().getBridge();
    }
    
    /**
     *
     */
    @Override
    protected void createLevel(){
        registerLevel(1, "First Day at School...", "Level 1");
        
        buildingNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        objectNode.setShadowMode(RenderQueue.ShadowMode.Cast);
        
       
        Material wallMat = createWallMaterial();
        Material floorMat = createFloorMaterial();
        
        
     

        arrow(0, 0, 0);
        //createGrid(10, 10);

        floor(10, 10, floorMat);

        

        light(new Vector3f(-1, -1, -1));
        wall(new Vector2f(0, 0), new Vector2f(9, 0), 3, wallMat);
        wall(new Vector2f(0, 9), new Vector2f(9,9), 1, wallMat);
        wall(new Vector2f(0, 1), new Vector2f(0,8), 1, wallMat);
        wall(new Vector2f(9, 1), new Vector2f(9, 8), 1, wallMat);
        //wallBuilder(new Vector2f(1, 2), new Vector2f(7, 2), 1, wallMat);
        treasure(3, 1, 100);
        treasure(4, 3, 150);
        rock(2,5);
        rock(2,6);
        
        floorButton(3, 3);
        floorButton(3, 5);
        
        character(2,2, Direction.RIGHT);
        
        
        
        //unlockableDoorBuilder(5, 5, Vector3f.UNIT_Z,0);
        key(3,2,Key.KEY_1);
        door(5,3,Direction.RIGHT,Key.KEY_1);
        door(5,4,Direction.RIGHT,Key.KEY_4);
        terminalGate(8, 7, Direction.RIGHT);
        terminalGate(8, 5, Direction.RIGHT);
        
           Scenario scenario1 = new Scenario();
          scenario1.addInvisibleWall(new Vector2f(0,0), new Vector2f(0,1));
          scenario1.addInvisibleWall(new Vector2f(1,1), new Vector2f(3,1));
          
          Scenario scenario2 = new Scenario();
          scenario2.addInvisibleWall(new Vector2f(1,0), new Vector2f(2,0));
          scenario2.addInvisibleWall(new Vector2f(1,1), new Vector2f(1,3));
          
          Minefield minefield = new Minefield(4,4);
          minefield.addScenario(scenario1);
          minefield.addScenario(scenario2);
         
          minefield(new Vector2f(4, 5), minefield);
          
         
       
    }
    
    private Material createWallMaterial(){
        Material wallMat = new Material(assetManager, LIGHTING_MATERIAL);
        //wallMat.setColor("Diffuse", ColorRGBA.Blue);
        //wallMat.setColor("Ambient", ColorRGBA.Red);
        //wallMat.setBoolean("UseMaterialColors",true);
        Texture wallTex = assetManager.loadTexture("Textures/walltexture_portal2_80x80.jpg");
        
        wallMat.setTexture("DiffuseMap", wallTex);
        //wallMat.getAdditionalRenderState().setWireframe(true);
        //wallMat.setBoolean("UseMaterialColors",true);
        //wallMat.setColor("Diffuse",ColorRGBA.White);
        //wallMat.setColor("Specular",ColorRGBA.White);
        return wallMat;
    }
    
    private Material createFloorMaterial(){
        Material floorMat = new Material(assetManager, LIGHTING_MATERIAL);
        //floorMat.setColor("Color", ColorRGBA.Orange);
        Texture foorTex = assetManager.loadTexture("Textures/stone_floor_60x60.JPG");
        floorMat.setTexture("DiffuseMap", foorTex);
        return floorMat;
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
    }

    
}
