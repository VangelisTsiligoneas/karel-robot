/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loginsystem;

import java.io.Serializable;

/**
 *
 * @author Vaggos
 * instance of this class represents the level that the user has managed to complete, including its score
 */
public class Grade implements Serializable {

    private int numberOfLevel;
    private String title;
    private int score;

    public Grade() {
        numberOfLevel = 0;
        score = 0;
    }

    public Grade(int numberOfLevel, int score) {
        this.numberOfLevel = numberOfLevel;
        title = "";
        this.score = score;
    }

    public Grade(String title, int score) {
        numberOfLevel = 0;
        this.title = title;
        this.score = score;
    }
    

    Grade(int numberOfLevel, String title, int score) {
        this.numberOfLevel = numberOfLevel;
        this.title = title;
        this.score = score;
    }

    public int getNumberOfLevel() {
        return numberOfLevel;
    }

    public String getTitle() {
        return title;
    }
    
    public int getScore() {
        return score;
    }

    void setScore(int score) {
        this.score = score;
    }
}
