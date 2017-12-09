/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package levelManagement;

import java.io.Serializable;

/**
 *
 * @author Vaggos
 * each instance of this class represents a register of each level. Not the level itself.
 */
public class Level implements Serializable, Comparable<Level> {
    private int number;
    private String title;
    private String fileName;
    //private String screenShot;

    public Level(int no, String title, String fileName ) {
        this.number = no;
        this.title = title;
        this.fileName = fileName.replace(" ", "_");
    }

    public Level(String title) {
        number = 0;
        this.title = title;
        this.fileName = title.replace(" ", "_");
    }
    
    public void setLevel(Level level){
        if(this.number == level.number){
            this.title = level.title;
            this.fileName = level.fileName;
        }
    }
    

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getFileName() {
        return fileName;
    }
    
    public int compareTo(Level l) {
        if(number == 0 && l.getNumber() == 0){
            return title.compareTo(l.getTitle());
        }
        else{
            return Integer.compare(number, l.getNumber());
        }
    }
}
