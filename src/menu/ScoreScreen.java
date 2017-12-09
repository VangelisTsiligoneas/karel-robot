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
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.ArrayList;
import loginsystem.Grade;
import mygame.Main;

/**
 *
 * @author Vaggos
 */
public class ScoreScreen extends AbstractAppState implements ScreenController {

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
        nifty.fromXml("Interface/MenuGui.xml", "scores", this);
        guiViewPort.addProcessor(niftyDisplay);
        app.getFlyByCamera().setDragToRotate(true);
    }

    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
        final int lineSize = app.getSettings().getHeight()/10;
        ArrayList<Grade> progress = app.getLoggedInUser().getProgress();
        final int size = progress.size();
                
         Element parentElement = new PanelBuilder("scorePanel") {
            {
                int height = lineSize * size;
                
                height(height + "px");
                width(percentage(100));
                backgroundColor("#550f");
                childLayoutVertical();
                x("0px");
                y("0px");
            }
        }.build(nifty, nifty.getCurrentScreen(), nifty.getCurrentScreen().findElementByName("#nifty-scrollpanel-child-root"));
         
        int count = 0;
        for (Grade g : progress) {
            final int color = count + 2;
            final int lv = g.getNumberOfLevel();
            final String title = g.getTitle();
            final int score = g.getScore();

            new TextBuilder() {
                {
                    text("Level: " + lv + " " + title + " Score: " + score);
                    font("Interface/Fonts/ComicSansMSplain24.fnt");
                    style("nifty-label");
                    color("#000f");
                    backgroundColor("#f" + color + color + "f");
                    height(lineSize + "px");
                    width(percentage(100));
                }
            }.build(nifty, screen, parentElement);
            
            count++;
        }
        
        System.out.println("done!");
    }

    public void onStartScreen() {
    }

    public void onEndScreen() {
    }
    public void goBack(){
        app.getStateManager().detach(this);
        app.getStateManager().attach(new MainMenu());
    }

    @Override
    public void cleanup() {
        super.cleanup();        
        guiViewPort.clearProcessors();
    }
    
}
