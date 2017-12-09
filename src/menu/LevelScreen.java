/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.ArrayList;
import levelManagement.Level;
import levelManagement.LevelCreator;
import levelManagement.LevelList;
import loginsystem.Grade;
import mygame.Main;

/**
 *
 * @author Vaggos
 */
public class LevelScreen extends AbstractAppState implements ScreenController {
    private Main app;
 private Nifty nifty;
    private Screen screen;    
    private ViewPort guiViewPort;
    private NiftyJmeDisplay niftyDisplay;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Main) app;
        guiViewPort = this.app.getGuiViewPort();
        niftyDisplay = this.app.getNiftyDisplay();
        setScreen();
    }
    
    public void setScreen() {
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/MenuGui.xml", "levels", this);
        guiViewPort.addProcessor(niftyDisplay);
        app.getFlyByCamera().setDragToRotate(true);
    }
    
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
        final int lineSize = 113;
        LevelList levelList = app.getLevelList();
        final int size = app.getLoggedInUser().getCurrentLevel();
        levelList.sort();
        ArrayList<Level> bLevels = levelList.getBuiltInLevels();
        
        Element parentElement = new PanelBuilder("levelPanel") {
            {
                int height = lineSize * size;                
                height(height + "px");
                width(percentage(99));
                childLayoutVertical();
                x("0px");
                y("0px");
            }
        }.build(nifty, nifty.getCurrentScreen(), nifty.getCurrentScreen().findElementByName("#nifty-scrollpanel-child-root"));
         
        int count=1;
        for(Level l : bLevels){            
            final int index = count;
            final int levelNumber = l.getNumber();
            final String title = l.getTitle();
            final String fileName = l.getFileName();
            
            new PanelBuilder("block" + index){{
                height(lineSize+"px");
                width(percentage(100));
                onHoverEffect(new HoverEffectBuilder("border"){{
                    effectParameter("color", "#e5d91bff");
                    effectParameter("border", "5px");
                    post(true);
                }});
                childLayoutHorizontal();
                margin("5px");
                backgroundColor("#a11f");
                image(new ImageBuilder("image" + index){{
                    filename("screenshots/Level_" + levelNumber + ".png");
                    width("200px");
                    height("113px");
                    valign(VAlign.Center);
                }});
                text(new TextBuilder(){{
                    align(Align.Left);
                    text("Level: " + levelNumber + " " + title);
                    font("Interface/Fonts/ComicSansMSplain24.fnt");
                    style("nifty-label");
                    color("#000f");                    
                    height(lineSize + "px");
                    width(percentage(60));
                }});
                control(new ButtonBuilder("enterButton" + index){{
                    interactOnClick("playLevel(" + fileName + ",play)");
                    height(percentage(90));
                    width(String.valueOf((int)(lineSize*0.9)) + "px");
                    align(Align.Right);
                    valign(VAlign.Center);
                    style("nifty-play-button");
                }});
            }}.build(nifty, screen, parentElement);
            if(count==size){
                break;
            }
            count++;
        }
    }
    
    public void playLevel(String fileName, String play){
        Level level = app.getLevelList().getLevel(fileName);
        app.getStateManager().detach(this);
        app.getStateManager().attach(new LevelCreator(level));
    }
    
    public void goBack(){
        app.getStateManager().detach(this);
        app.getStateManager().attach(new MainMenu());
    }

    public void onStartScreen() {
        
    }

    public void onEndScreen() {
        
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        guiViewPort.removeProcessor(niftyDisplay);
    }
    
    /*public void onClick(){
     * System.out.println("click!");
     * }*/
    
}
