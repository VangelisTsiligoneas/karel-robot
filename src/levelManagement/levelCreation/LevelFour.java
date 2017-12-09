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
public class LevelFour extends LevelBuilder{
    
    

    @Override
    protected void createLevel() {
        registerLevel(4, "nested if", "Level_4");
        floor(19, 10);
        
        ambientLight(0.8f);
        light(new Vector3f(0.39293572f, -0.7279972f, -0.56180215f));
        
        setCamera(new Vector3f(14.708582f, 12.974026f, 12.135491f), new Vector3f(-0.35469395f, -0.78415555f, -0.5092076f));
        
        addCamera(new Vector3f(12.621088f, 11.607279f, 12.107439f), new Vector3f(-0.25971672f, -0.78294235f, -0.56528604f));
        addCamera(new Vector3f(-1.247572f, 9.098077f, 5.055967f), new Vector3f(0.5846455f, -0.81123406f, -0.009429097f));
        addCamera(new Vector3f(10.688227f, 7.4088554f, 5.1983204f), new Vector3f(-0.02126074f, -0.94033194f, -0.33959413f));
        addCamera(new Vector3f(12.4809f, 9.620594f, 6.943775f), new Vector3f(0.026176888f, -0.9047659f, -0.42510414f));
        addCamera(new Vector3f(9.258837f, 15.387651f, 7.4688063f), new Vector3f(-4.2454386E-4f, -0.97647995f, -0.21560812f));
        
        
        Material dm = defaultWallMaterial();
    Material tm = transparentMaterial();
        walls(dm, tm);
        
        minefield(9,1);
        
        floorButton(2, 7);
        rock(4, 7);
        
       // character(5, 7, Direction.LEFT);
        
        treasure(5, 3);
        treasure(5, 5);
        treasure(5,6);
        treasure(7,3);
        treasure(8,3);
        treasure(9,3);
        
        treasure(17,5);
        terminalGate(17, 1, Direction.UP);        
    }
    
    private void walls(Material dm, Material tm){
        wall(2,0,6,0,2,dm);
        wall(7,0,17,0,2,dm);
        wall(0,3,2,3,2,dm);
        wall(8,5,12,5,2,tm);
        wall(13,6,18,6,2,dm);
        
        wall(0,4,0,8,2,dm);
        wall(2,0,2,2,2,dm);
        wall(8,1,8,2,1,dm);
        wall(7,5,7,8,2,tm);
        wall(18,0,18,5,2,dm);
        
        wall(8,4,8,4,1,dm);
        wall(0,9,7,9,2,dm);
        wall(16,1,16,3,1,tm);
        wall(16,5,16,5,1,tm);
    }
    private void minefield(int x, int y){
        Minefield m = new Minefield(4, 4);
        Scenario s1 = new Scenario();
        Scenario s2 = new Scenario();
        Scenario s3 = new Scenario();
        
        s1.addInvisibleWall(new Vector2f(0,0), new Vector2f(3,0));
        s1.addInvisibleWall(new Vector2f(0,1), new Vector2f(0,1));
        s1.addInvisibleWall(new Vector2f(2,1), new Vector2f(3,1));
        
        s2.addInvisibleWall(new Vector2f(2,1),new Vector2f(3,1));
        s2.addInvisibleWall(new Vector2f(1,2),new Vector2f(1,2));
        s2.addInvisibleWall(new Vector2f(1,2),new Vector2f(3,2));
        
        s3.addInvisibleWall(new Vector2f(3,0),new Vector2f(3,0));
        s3.addInvisibleWall(new Vector2f(1,2),new Vector2f(1,2));
        s3.addInvisibleWall(new Vector2f(1,3),new Vector2f(1,3));
        
        m.addScenario(s1);
        m.addScenario(s2);
        m.addScenario(s3);
        
        minefield(new Vector2f(x,y), m);
        wall(x+1,y+1,x+1,y+1,1,defaultWallMaterial());
        wall(x+2,y+2,x+3,y+2,1,defaultWallMaterial());
        wall(x+0,y+3,x+0,y+3,1,defaultWallMaterial());
        
    }
}
