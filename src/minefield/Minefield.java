/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minefield;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Vaggos
 */
public class Minefield implements Serializable{
    private int sizeX;
    private int sizeY;
    private ArrayList<Scenario> scenarios;

    private Minefield(int sizeX, int sizeY, ArrayList<Scenario> scenarios) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.scenarios = scenarios;
    }
    
    public Minefield(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.scenarios = new ArrayList<Scenario>();
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public Scenario selectScenario(){
        Random rand = new Random();
        int size = scenarios.size();
        int index = rand.nextInt(size);
        return scenarios.get(index);
    }
    
    public void addScenario(Scenario scenario){
        scenarios.add(scenario);
    }
    
    public ArrayList<Scenario> getScenarios() {
        return scenarios;
    }

    
    
    
    
    public static class MinefieldBuilder{
        private int nestedSizeX;
        private int nestedSizeY;
        private ArrayList<Scenario> nestedScenarios;

        public MinefieldBuilder() {
            nestedSizeX = 0;
            nestedSizeY = 0;
            nestedScenarios = new ArrayList<Scenario>();
        }
        
        
        
        public MinefieldBuilder size(int x, int y){
            nestedSizeX = x;
            nestedSizeY = y;
            return this;
        }
        
        
        
        public MinefieldBuilder Scenario(Scenario scenario){            
            nestedScenarios.add(scenario);
            return this;
        }
        
        public Minefield createMinefield(){
            return new Minefield(nestedSizeX, nestedSizeY, nestedScenarios);
        }
    }
    
}
