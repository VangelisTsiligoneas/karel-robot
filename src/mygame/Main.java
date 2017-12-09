package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.font.BitmapFont;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;
import levelManagement.LevelList;
import loginsystem.User;
import menu.LoadGameScreen;

/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static final String GO_TO_MAIN_MENU = "go to main menu";
    private NiftyJmeDisplay niftyDisplay;
    private User loggedInUser;
    private LevelList levelList;
    Thread commandThread;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    public Main() {
        super();
        levelList = LevelList.loadList();
    }

    @Override
    public void simpleInitApp() {
        niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, getAudioRenderer(), guiViewPort);
        setDisplayStatView(false);
        setDisplayFps(false);
        getStateManager().attach(new LoadGameScreen());
        getStateManager().attach(new ScreenshotAppState(System.getProperty("user.dir") + "/assets/screenshots/"));

        mapKeys();
    }

    public void mapKeys() {
        //inputManager.addMapping("ScreenShot", new KeyTrigger(KeyInput.KEY_SYSRQ));
        //inputManager.deleteMapping("FLYCAM_Forward");

        //inputManager.deleteMapping("SIMPLEAPP_Exit");
        inputManager.addMapping("move", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("rotate", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addMapping("toggleGrid", new KeyTrigger(KeyInput.KEY_H));
        inputManager.addMapping("pickup", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("push", new KeyTrigger(KeyInput.KEY_M));
        inputManager.addMapping("unlock", new KeyTrigger(KeyInput.KEY_L));
        inputManager.addMapping("open", new KeyTrigger(KeyInput.KEY_O));
        inputManager.addMapping("check", new KeyTrigger(KeyInput.KEY_G));
        inputManager.addMapping(GO_TO_MAIN_MENU, new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addMapping("restart", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addMapping("write", new KeyTrigger(KeyInput.KEY_T));

        //for debugging
        inputManager.addMapping("createLight", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("deleteLight", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping("toggleTorch", new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));
        inputManager.addMapping("printLights", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("execute", new KeyTrigger(KeyInput.KEY_2));
        //
        inputManager.addMapping("toggleFullScreen", new KeyTrigger(KeyInput.KEY_X));
    }

    public void startCommandThread(Runnable r) {
        commandThread = new Thread(r);
        commandThread.start();
    }

    public Thread getCommandThread() {
        return commandThread;
    }

    public void interruptCommandThread() {
        if (commandThread != null) {

            commandThread.interrupt();

        }

    }

    public NiftyJmeDisplay getNiftyDisplay() {
        return niftyDisplay;
    }

    public LevelList getLevelList() {
        return levelList;
    }

    public BitmapFont getGuiFont() {
        return guiFont;
    }

    public AppSettings getSettings() {
        return settings;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}