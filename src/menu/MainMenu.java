
package menu;

import loginsystem.User;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.ArrayList;
import levelManagement.Level;
import levelManagement.LevelCreator;
import loginsystem.Grade;
import mygame.Main;

/**
 *
 * @author Vaggos
 */
public class MainMenu extends AbstractAppState implements ScreenController{
    
    private Node guiNode;
    private ViewPort guiViewPort;
    private NiftyJmeDisplay niftyDisplay;
    private Main app;
    
    private Nifty nifty;
    private Screen screen;
    private User user;
    

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Main) app;
        guiNode = this.app.getGuiNode();
        guiViewPort = this.app.getGuiViewPort();
        niftyDisplay = this.app.getNiftyDisplay();
        user = this.app.getLoggedInUser();
        setScreen();
    }
    
    public void setScreen(){               
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/MenuGui.xml", "menu", this);
        guiViewPort.addProcessor(niftyDisplay);
        app.getFlyByCamera().setDragToRotate(true);
    }
    
   
    
    public void goToCurrentLevel(){
        int currentLevel = user.getCurrentLevel();
        int maxLevel = app.getLevelList().findMaxLevel();
        int levelNum = -1;
        if(currentLevel<maxLevel){
            levelNum = currentLevel;
        }
        else{
            levelNum = maxLevel;
        }
        Level level = app.getLevelList().getLevel(levelNum);
        if(level != null){
            app.getStateManager().detach(this);
        app.getStateManager().attach(new LevelCreator(level));
        }   
    }
    
    public void goToSelectLevel(){
        app.getStateManager().detach(this);
        app.getStateManager().attach(new LevelScreen());
    }
    
    public void goToViewScores(){
        app.getStateManager().detach(this);
        app.getStateManager().attach(new ScoreScreen());
            
    }
    
    public void back(){
        app.getStateManager().detach(this);
        app.getStateManager().attach(new LoadGameScreen());
    }
    
    public void quit(){
        app.stop();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        guiNode.detachAllChildren();
        guiViewPort.removeProcessor(niftyDisplay);
    }

    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
        
    }

    public void onStartScreen() {
        
    }

    public void onEndScreen() {
        
    }
    
    
}
