/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package levelManagement;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import levelManagement.levelCreation.LevelBuilder;

/**
 *
 * @author Vaggos
 * it's a list of levels that is saved in LevelList folder
 */
public class LevelList implements Serializable {

    private static final String SAVE_DIRECTORY = System.getProperty("user.dir") + "/assets/LevelList/";
    private static final String NAME_OF_FILE = "levelList";
    private ArrayList<Level> levels;

    private LevelList() {
        levels = new ArrayList<Level>();
    }

    public void saveList() {
        try {
            FileOutputStream fileOut = new FileOutputStream(SAVE_DIRECTORY + NAME_OF_FILE + ".lev");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.println("levelList saved!");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static LevelList loadList() {
        LevelList lvl = new LevelList();
        try {
            FileInputStream fileIn = new FileInputStream(SAVE_DIRECTORY + NAME_OF_FILE + ".lev");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            lvl = (LevelList) in.readObject();
            in.close();
            fileIn.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found. I ll create a new one");
            lvl.saveList();
            return lvl;
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("LevelList class not found");
            c.printStackTrace();
            return null;
        }
        return lvl;
    }
    
    public void addLevel(Level level){
        Level existingLevel = getLevel(level.getNumber());
        if(existingLevel == null){
            levels.add(level);
        }
        else{
            existingLevel.setLevel(level);
        }
        saveList();
    }
    
    public Level getLevel(int number){
        for(Level l:levels){
            if(l.getNumber()==number){
                return l;
            }
        }
        return null;
    }
    
    public Level getLevel(String fileName){
        for(Level l:levels){
            if(l.getFileName().equals(fileName)){
                return l;
            }
        }
        return null;
    }
    
    public int size(){
        return levels.size();
    }
    
    public void sort(){
        Collections.sort(levels);
    }
    
    public ArrayList<Level> getBuiltInLevels(){
        ArrayList<Level> bLevels = new ArrayList<Level>();
        for(Level l : levels){
            if(l.getNumber() != 0){
                bLevels.add(l);
            }
        }
        return bLevels;
    }

    public boolean isTitleUnique(String title) {
        for (Level l : levels) {
            if (l.getTitle().equals(title)) {
                return false;
            }
        }
        return true;
    }

    public boolean isNumberOfLevelUnique(int number) {
        for (Level l : levels) {
            if (l.getNumber() == number) {
                return false;
            }
        }
        return true;
    }

    public boolean isFileNameUnique(String fileName) {
        for (Level l : levels) {
            if (l.getFileName().equals(fileName)) {
                return false;
            }
        }
        return true;
    }
   
    public String getFileName(int numLevel){        
        for(Level l : levels){
            if(l.getNumber() == numLevel){
                return l.getFileName();
            }
        }        
        System.out.println("the Filename for this level does not exist");
        return null;
    }
    
    
    public int findMaxLevel() {
        int max = 0;
        for (Level l : levels) {
            int number = l.getNumber();
            if (number > max) {
                max = number;
            }
        }
        return max;
    }
    
    public void print(){
        sort();
        for(Level l: levels){
            System.out.println("no: " + l.getNumber() + ", title: " + l.getTitle() + ", file name: " + l.getFileName());
        }
    }
}