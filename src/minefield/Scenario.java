/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minefield;

import com.jme3.math.Vector2f;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Vaggos
 * each senario is a collection of invisible obstacles (InvisibleWall class) in a minefield.
 * each time, the minefield selects a senario at random.
 */
public class Scenario implements Serializable{

    private ArrayList<InvisibleWall> walls;

    private Scenario(ArrayList<InvisibleWall> walls) {
        this.walls = walls;
    }

    public Scenario() {
        walls = new ArrayList<InvisibleWall>();
    }
    
    public void addInvisibleWall(Vector2f from, Vector2f to){
        InvisibleWall wall = new InvisibleWall(from, to);
            walls.add(wall);
    }

    public ArrayList<InvisibleWall> getWalls() {
        return walls;
    }
    
    
    public static class ScenarioBuilder {

        private ArrayList<InvisibleWall> nestedWalls;

        public ScenarioBuilder() {
            this.nestedWalls = new ArrayList<InvisibleWall>();
        }
        
        

        public ScenarioBuilder invisibleWall(Vector2f from, Vector2f to) {
            InvisibleWall wall = new InvisibleWall(from, to);
            nestedWalls.add(wall);
            return this;
        }

        public Scenario createSenario() {
            return new Scenario(nestedWalls);
        }
    }
}
