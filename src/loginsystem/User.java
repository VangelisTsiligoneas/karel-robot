/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loginsystem;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Vaggos
 */
public class User implements Serializable {

    private final String userName;
    private ArrayList<Grade> progress;

    //private ArrayList<CustomizedLevel> customLevels;
    User(String userName) {
        this.userName = userName;
        progress = new ArrayList<Grade>();
    }
    
    public User(){
        userName = "unknownUser";
        progress = new ArrayList<Grade>();
    }
    
    public ArrayList<Grade> getProgress(){
        return progress;
    }

    public String getUserName() {
        return userName;
    }

    public void setScore(int numberOfLevel, int score) {
        if (numberOfLevel > 0) {
            boolean found = false;
            for (Grade s : progress) {
                if (s.getNumberOfLevel() == numberOfLevel) {
                    s.setScore(score);
                    found = true;
                    break;
                }
            }
            if (!found) {
                progress.add(new Grade(numberOfLevel, score));
            }
        }
    }
    
    public void setScore(String title, int score){
        boolean found = false;
        for (Grade g : progress){
            if(g.getTitle().equals(title)){
                g.setScore(score);
                found = true;
                break;
            }            
        }
        if(!found){
            progress.add(new Grade(title, score));
        }
        
    }
    
    public int getCurrentLevel(){
        int max = 0;
        for(Grade g : progress){
            int level = g.getNumberOfLevel();
            if(level > max){
                max = level;
            }
        }
        return max + 1;
    }

    @Override
    public String toString() {
        String progressText = "";
        for(Grade g : progress){
            int level = g.getNumberOfLevel();
            int score = g.getScore();
            
            progressText += "for level " + level + " score = " + score + "\n";
        }
        return "username: " + userName + "\n" + progressText;        
    }
    
    public void saveUser() {
        String directory = UserList.SAVE_DIRECTORY;
        try {
            FileOutputStream fileOut = new FileOutputStream(directory + "/" + userName + ".save");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}