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
public class LevelTwo extends LevelBuilder {

    @Override
    protected void createLevel() {
        registerLevel(2, "Learn the basics part 2", "Level_2");
       // ambientLight(0.8f);
        light(new Vector3f(-0.4197094f, -0.71377766f, -0.56068325f));
        setBasicScore(200);

        setCamera(new Vector3f(8.095426f, 6.9595184f, 11.176101f), new Vector3f(-0.18929777f, -0.64730644f, -0.7383503f));

        addCamera(new Vector3f(8.095426f, 6.9595184f, 11.176101f), new Vector3f(-0.18929777f, -0.64730644f, -0.7383503f));
        addCamera(new Vector3f(3.1917892f, 9.41792f, 5.0868196f), new Vector3f(0.18007101f, -0.90392005f, -0.38794708f));
        addCamera(new Vector3f(6.6873307f, 6.5666475f, -0.2823989f), new Vector3f(-0.10990174f, -0.7961061f, 0.59509385f));
        addCamera(new Vector3f(6.6577334f, 12.683389f, 4.2436047f), new Vector3f(-2.0788913E-4f, -0.99858135f, -0.05324793f));
        
        Material defaultMaterial = defaultWallMaterial();
        Material trasparentMaterial = transparentMaterial();
        floor(12, 9, defaultMaterial);

        wall(new Vector2f(0, 0), new Vector2f(8, 0), 3, defaultMaterial);
        wall(new Vector2f(0, 1), new Vector2f(0, 2), 3, defaultMaterial);
        wall(new Vector2f(0, 3), new Vector2f(0, 3), 1, trasparentMaterial);
        wall(new Vector2f(1, 4), new Vector2f(3, 4), 1, trasparentMaterial);
        wall(new Vector2f(4, 4), new Vector2f(4, 7), 1, trasparentMaterial);

        wall(new Vector2f(4, 8), new Vector2f(10, 8), 1, trasparentMaterial);
        wall(new Vector2f(8, 1), new Vector2f(8, 5), 3, defaultMaterial);
        wall(new Vector2f(9, 1), new Vector2f(10, 3), 3, defaultMaterial);
        wall(new Vector2f(11, 4), new Vector2f(11, 8), 3, defaultMaterial);

        //character(1, 1, Direction.RIGHT);
        treasure(2, 1);
        treasure(6, 6);

        floorButton(7, 1);
        floorButton(1, 3);
        rock(1,2);
        rock(7, 1);
        floorButton(5, 7);
        rock(5, 6);
        terminalGate(9, 5, Direction.UP);
    }
}
