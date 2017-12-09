/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loginsystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 *
 * @author Vaggos
 */
public class UserList {

    static final String SAVE_DIRECTORY = System.getProperty("user.dir") + "/assets/Saves/";
    private ArrayList<User> users;
    
    /*
     * private constructor. Use static loadUsers Method to instantiate this class.
     * this assures that only one object will be created
     */
    private UserList() {
        this.users = new ArrayList<User>();
    }

    public ArrayList<User> getUsers() {
        return users;
    }
    
    public int getSize(){
        return users.size();
    }
    
    public User getUser(int slot){       
        return users.get(slot - 1);        
    }
    
    
    
     public User createUser(String userName) {
        for (User user : users) {
            if (user.getUserName().toLowerCase().equals(userName.toLowerCase()) || userName.equals("")) {
                return null;
            }
        }
        return new User(userName);
    }

    private void addUser(User user){
        users.add(user);
    }
    
    public void deleteUser(String userName){
        File file = new File(SAVE_DIRECTORY + userName + ".save");
        file.delete();
        
        
    }
    /*
     * called in the loadGame screen for instantiation of this class 
     */
    public static UserList loadUsers() {
        final File folder = new File(SAVE_DIRECTORY);
        UserList userList = new UserList();
        User user = null;
        for (File file : folder.listFiles()) {

            try {
                FileInputStream fin = new FileInputStream(file.getAbsoluteFile());
                ObjectInputStream in = new ObjectInputStream(fin);
                user = (User) in.readObject();
                fin.close();
                in.close();
            } catch (IOException i) {
                i.printStackTrace();

            } catch (ClassNotFoundException c) {
                System.out.println("User class not found");
                c.printStackTrace();
                return null;
            }
            userList.addUser(user);            
        }
        return userList;
    }
    
    public void printUsers(){
        for(User user : users){
            System.out.println(user.toString());
        }
    }
}