/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package levelManagement;

import menu.MainMenu;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.ElementBuilder.Align;
import de.lessvoid.nifty.builder.ElementBuilder.VAlign;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.PopupBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.tabs.TabGroupControl;
import de.lessvoid.nifty.controls.window.WindowControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
//import java.util.logging.Level;
import java.util.logging.Logger;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import levelManagement.levelCreation.LevelBuilder;
import mygame.Main;
import objectControl.CharacterControl;
import utils.World;
import utils.nifty.textEditor.TextEditorControl;

/**
 * @author Vaggos in this class a level is loaded (with levelLoader class) in
 * addition to the gui. Also the screen of the game is adjusted
 */
public class LevelCreator extends AbstractAppState implements ScreenController {

    public static final String MESSAGES_DIRECTORY = System.getProperty("user.dir") + "/assets/messages/";
    public static final String JAVA_DIRECTORY = System.getProperty("user.dir") + "/assets/scripts/";
    public static final String SCRIPTS_DIRECTORY = System.getProperty("user.dir") + "/assets/Solutions/";
    private Main app;
    private LevelLoader levelLoader;
    private int cameraIndex;
    private ArrayList<Ray> cams;
    private Nifty nifty;
    private TextEditorControl textEditor1;
    private TextEditorControl messageBox;
    private Element buttonElement;
    private CheckBox arrowCheckBox;
    private boolean fullScreen = false;
    private TextEditorControl textEditor2;
    private WindowControl window;
    private boolean finish;
    private int compile;
    private ListBox listBox;
    private float leftBoundary;
    private float rightBoundary;
    private float bottomBoundary;
    private float topBoundary;
    private boolean forward;
    private boolean backward;
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private Level level;
    private boolean run;

    public LevelCreator(Level level) {
        super();
        this.level = level;
        levelLoader = new LevelLoader(level.getFileName());
        forward = false;
        backward = false;
        left = false;
        right = false;
        up = false;
        down = false;

        leftBoundary = 0.0f;
        rightBoundary = 0.65f;
        bottomBoundary = 0.28f;
        topBoundary = 0.92175f;

        cameraIndex = 0;
        cams = new ArrayList<Ray>();
        run = false;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Main) app;
        InputManager inputManager = this.app.getInputManager();
        inputManager.deleteMapping("FLYCAM_Forward");
        inputManager.deleteMapping("FLYCAM_Backward");
        inputManager.deleteMapping("FLYCAM_StrafeLeft");
        inputManager.deleteMapping("FLYCAM_StrafeRight");
        inputManager.deleteMapping("FLYCAM_Rise");
        inputManager.deleteMapping("FLYCAM_Lower");
        inputManager.deleteMapping("FLYCAM_ZoomIn");
        inputManager.deleteMapping("FLYCAM_ZoomOut");

        this.app.getStateManager().attach(levelLoader);
        cams = getCams();
        setCamera(cameraIndex);
        compile = 0;
        setLevelPort(leftBoundary, rightBoundary, bottomBoundary, topBoundary);
        setGameScreen();
    }
    private Runnable runnable = new Runnable() {
        

        public void run() {
            String className = "Execute";
            //while (true) {
                try {
                    String binaryName = "instructions." + className;
                    Class<?> classs;
                    Object object;

                    URL[] urls = null;

                    File dir = new File(System.getProperty("user.dir") + "/assets/classes/");
                    URL url = dir.toURI().toURL();
                    urls = new URL[]{url};

                    ClassLoader cl = new URLClassLoader(urls);

                    classs = cl.loadClass(binaryName);

                    object = classs.newInstance();

                    Class[] paramTypes = new Class[]{World.class};
                    Object[] params = new Object[]{new World(app)};

                    //System.out.println("start running!");
                    //classs.getDeclaredMethod("installSoftware", paramTypes).invoke(object, params);
                    classs.getDeclaredMethod("main", paramTypes).invoke(object, params);
                    //System.out.println("end of running!");
                    
                    buttonElement.enable();
                    

                } catch (InstantiationException ex) {
                    Logger.getLogger(LevelCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    return;
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(LevelCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    return;
                } catch (NoSuchMethodException ex) {
                    Logger.getLogger(LevelCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    return;
                } catch (SecurityException ex) {
                    Logger.getLogger(LevelCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    return;
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(LevelCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(LevelCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    return;
                } catch (MalformedURLException ex) {
                    Logger.getLogger(LevelCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    return;
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(LevelCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    return;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    System.out.println("--thread NOT Closed!--");
                }
            //}
        }

        
    };

    private void deleteClassFiles() {
        String outPutDirectory = System.getProperty("user.dir") + "/assets/classes/instructions/";

        for (File f : new File(outPutDirectory).listFiles()) {
            f.delete();
        }
        //System.out.println("class files deleted!");
    }

    private void toggleFullScreen() {
        fullScreen = !fullScreen;
        if (fullScreen) {
            app.getFlyByCamera().setDragToRotate(false);
            setLevelPort(0, 1, 0, 1);
        } else {
            app.getFlyByCamera().setDragToRotate(true);
            setLevelPort(leftBoundary, rightBoundary, bottomBoundary, topBoundary);
        }
    }

    public void escape() {
        app.getStateManager().detach(levelLoader);
        app.getStateManager().detach(this);
        app.getStateManager().attach(new MainMenu());
    }

    private void setLevelPort(float left, float right, float bottom, float top) {
        this.app.getCamera().setViewPort(left, right, bottom, top);
    }

    protected void setGameScreen() {
        nifty = app.getNiftyDisplay().getNifty();
        nifty.fromXml("Interface/GameGui.xml", "start", this);
        app.getGuiViewPort().addProcessor(app.getNiftyDisplay());
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        moveCam(tpf);
        ArrayList<Spatial> characters = levelLoader.getCharacterSpatials();
        if (!characters.isEmpty()) {
            int characterScore = 0;
            boolean allCharacterFinished = true;

            for (Spatial s : characters) {
                CharacterControl c = s.getControl(CharacterControl.class);
                characterScore += c.getScore();
                if (!c.isFinished()) {
                    String warningMessage = c.getWarningMessage();
                    if (warningMessage != null) {
                        messageBox.setText(warningMessage);
                        c.setWarningMessage(null);
                    }
                    allCharacterFinished = false;
                } else {
                    s.setCullHint(Spatial.CullHint.Always);
                    s.move(s.getLocalTranslation().add(new Vector3f(0, -100, 0)));
                }
            }
            int basicScore = levelLoader.getBasicScore();
            int totalScrore = basicScore + characterScore;
            nifty.getCurrentScreen().findElementByName("score").getRenderer(TextRenderer.class).setText("Basic Score: " + String.valueOf(basicScore) + "   Extra points: "
                    + String.valueOf(characterScore) + "   Total Score: " + String.valueOf(totalScrore));
            if (!finish) {
                if (allCharacterFinished) {
                    if (level.getNumber() > 0) {
                        app.getLoggedInUser().setScore(level.getNumber(), totalScrore);
                    } else {
                        app.getLoggedInUser().setScore(level.getTitle(), totalScrore);
                    }
                    app.getLoggedInUser().saveUser();
                    try {
                        saveScripts();
                    } catch (IOException ex) {
                        Logger.getLogger(LevelCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                    createFinishPopup(totalScrore);
                    showFinishPopup();
                    finish = true;
                }
            }
        }
        if (run) {
            run = false;
            levelLoader.setReady(false);
        } else {
            if (levelLoader.isReady()) {
                simpleRun();
                levelLoader.setReady(false);
            }
        }



        if (compile > 0) {
            if (compile > 50) {
                app.startCommandThread(runnable);
               
                compile = 0;
            } else {
                compile++;
            }
        }
    }

    private void moveCam(float tpf) {
        float moveSpeed = 10f;
        if (forward) {
            moveCamera(tpf, false, moveSpeed);
        }
        if (backward) {
            moveCamera(-tpf, false, moveSpeed);
        }
        if (left) {
            moveCamera(tpf, true, moveSpeed);
        }
        if (right) {
            moveCamera(-tpf, true, moveSpeed);
        }
        if (up) {
            riseCamera(tpf, moveSpeed);
        }
        if (down) {
            riseCamera(-tpf, moveSpeed);
        }
    }

    private void riseCamera(float tpf, float moveSpeed) {
        Camera cam = app.getCamera();
        Vector3f vel = new Vector3f(0, tpf * moveSpeed, 0);
        Vector3f pos = cam.getLocation().clone();
        pos.addLocal(vel);
        cam.setLocation(pos);
    }

    private void moveCamera(float tpf, boolean sideways, float moveSpeed) {
        Camera cam = app.getCamera();
        Vector3f vel = new Vector3f();
        Vector3f pos = cam.getLocation().clone();

        if (sideways) {
            cam.getLeft(vel);
        } else {
            cam.getDirection(vel);
        }
        vel.multLocal(tpf * moveSpeed);

        pos.addLocal(vel);

        cam.setLocation(pos);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        app.getStateManager().detach(levelLoader);
        runnable = null;
        app.interruptCommandThread();
        setLevelPort(0f, 1f, 0f, 1f);
    }

    public void restart() {
        levelLoader.restart();
        buttonElement.enable();
    }

    private String getInitialText2() {
        String directory = System.getProperty("user.dir") + "/assets/Scenes/Levels/";
        String alternativeDirectory = System.getProperty("user.dir") + "/assets/misc/";
        String path = directory + levelLoader.getFileName() + "_MyRobot" + ".txt";
        String alternative = alternativeDirectory + "/textEditor2.txt";
        String text = "";

        try {
            text = readTextFile(path);
        } catch (IOException ex) {
            try {
                text = readTextFile(alternative);
            } catch (IOException ex2) {
                Logger.getLogger(LevelCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
        return text;
    }

    private String getInitialText1() {
        String path = System.getProperty("user.dir") + "/assets/misc/";
        String path1 = path + "/textEditor1.txt";
        String path3 = LevelBuilder.SAVE_DIRECTORY + levelLoader.getFileName() + ".txt";


        String text1 = "";

        String sandwitch = "Robot karel = new Robot(2, 1, \"UP\");\n" + "karel.land(world);";
        try {
            text1 = readTextFile(path1);
            sandwitch = readTextFile(path3);

        } catch (IOException ex) {
            Logger.getLogger(LevelCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        String content = "direction\n";
        int index = text1.indexOf(content) + content.length();
        StringBuffer sb = new StringBuffer(text1);
        sb.insert(index, sandwitch);

        return sb.toString();
    }

    public void restartEditors() {
        textEditor1.setText(getInitialText1());
        textEditor2.setText(getInitialText2());
        messageBox.setText("");
    }

    public boolean compile(File... files) {
        buttonElement.disable();
        //String directory = System.getProperty("user.dir") + "/assets/scripts/";
        // String executeClassName = "Execute";
        //String robotClassName = "MyRobot";
        //String executeFileName = directory + executeClassName + ".java";
        //String robotFileName = directory + robotClassName + ".java";
        String outPutDirectory = System.getProperty("user.dir") + "/assets/classes/";

        File outPutPath = new File(outPutDirectory);
        ArrayList<File> path = new ArrayList<File>();
        path.add(outPutPath);

        deleteClassFiles();

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

        try {
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, path);
        } catch (IOException ex) {
            Logger.getLogger(LevelCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(files);
        StringWriter stringWriter = new StringWriter();

        boolean call = compiler.getTask(stringWriter, fileManager, null, null, null, compilationUnits).call();
        String message = stringWriter.toString();
        messageBox.setText(message);
        try {
            fileManager.close();
        } catch (IOException e) {
            System.out.println("-----------fileManager not closed!---------");
        }
        return call;
    }

    public boolean compile() {
        String executeClassName = "Execute";
        String robotClassName = "MyRobot";

        File executeFile = createJavaFile(executeClassName, textEditor1.getText());
        File robotFile = createJavaFile(robotClassName, textEditor2.getText());
        return compile(executeFile, robotFile);
    }

    public void simpleRun() {

        String executeClassName = "Execute";
        String robotClassName = "MyRobot";

        File executeFile = createJavaFile(executeClassName, getInitialText1());
        File robotFile = createJavaFile(robotClassName, getInitialText2());
        boolean call = compile(executeFile, robotFile);
        if (call) {
            compile++;
        } else {
            buttonElement.enable();
        }
    }

    public void run() {
        run = true;
        restart();
        boolean call = compile();

        if (call) {
            levelLoader.showMinefields();
            compile++;
        } else {
            buttonElement.enable();
        }
    }

    public File createJavaFile(String className, String content) {
        String directory = JAVA_DIRECTORY;
        String path = directory + className + ".java";
        FileOutputStream fop = null;
        File file = null;

        try {
            file = new File(path);
            fop = new FileOutputStream(file, false); //3/11/14

            byte[] contentInBytes = content.getBytes();

            fop.write(contentInBytes);
            //System.out.println("file written");
            fop.flush();
            fop.close();
            // System.out.println("file closed");
            return file;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public void saveScripts() throws IOException {
        File levelsFolder = new File(JAVA_DIRECTORY);
        String userName = app.getLoggedInUser().getUserName();
        String destPath = SCRIPTS_DIRECTORY + "/" + userName + "/" + level.getFileName() + "/";
        File destFile = new File(destPath);
        if (!destFile.exists()) {
            destFile.mkdirs();
        }
        File[] listFiles = levelsFolder.listFiles();

        for (File f : listFiles) {
            Files.copy(f.toPath(), new File(destPath + f.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public void loadScripts() throws IOException {
        String userName = app.getLoggedInUser().getUserName();
        String sourcePath = SCRIPTS_DIRECTORY + "/" + userName +  "/" + level.getFileName() + "/";        

        File sourceFolder = new File(sourcePath);
        if (sourceFolder.exists()) {
            File[] listFiles = sourceFolder.listFiles();
            for (File f : listFiles) {
                if (f.getName().equals("Execute.java")) {
                    String text = readTextFile(f.toPath());
                    textEditor1.setText(text);
                } else if (f.getName().equals("MyRobot.java")) {
                    String text = readTextFile(f.toPath());
                    textEditor2.setText(text);
                }
            }
        }
    }

    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        buttonElement = screen.findElementByName("run");
        textEditor1 = screen.findNiftyControl("notepadOne", TextEditorControl.class);
        textEditor2 = screen.findNiftyControl("notepadTwo", TextEditorControl.class);
        messageBox = screen.findControl("messageBox", TextEditorControl.class);
    }

    public void onStartScreen() {
        if (level.getNumber() == 1) {
            nifty.getCurrentScreen().findNiftyControl("edit_tabs", TabGroupControl.class).removeTab(1);
        }
        restartEditors();
        window = nifty.getCurrentScreen().findNiftyControl("window", WindowControl.class);
        arrowCheckBox = nifty.getCurrentScreen().findNiftyControl("arrowCheckBox", CheckBox.class);
        String title = app.getLevelList().getLevel(levelLoader.getFileName()).getTitle();
        title = "Level: " + this.level.getNumber() + " " + title;
        window.setTitle(title);
        setWindowContent();
        setList();
        try {
            loadScripts();
        } catch (IOException ex) {
            Logger.getLogger(LevelCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public void setWindowContent() {
        String text = "";
        try {
            text = readTextFile(MESSAGES_DIRECTORY + levelLoader.getFileName() + ".txt");
        } catch (IOException ex) {
            //Logger.getLogger(LevelCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            text = "ERROR: CAN'T FIND FILE";
        }
        final String finalText = text;

        new TextBuilder("windowText") {
            {
                width(percentage(100));
                text(finalText);
                wrap(true);
                style("base-font");
                font("Interface/Fonts/cleanText24.fnt");
                color("#000f");
                backgroundColor("##fdfcf3ff");
                textVAlign(VAlign.Top);
                textHAlign(Align.Left);
                x("0px");
                y("0px");
                //control(new ScrollbarBuilder("verticalScrollbarId", true));
            }
        }.build(nifty, nifty.getCurrentScreen(), window.getContent().findElementByName("#nifty-scrollpanel-child-root"));
    }

    public String getdisplayCount() {
        int itemHeight = 23;
        int height = app.getSettings().getHeight();
        int panelHeight = (int) (height * 0.93 * 0.2);
        int displayItems = panelHeight / itemHeight;
        return String.valueOf(displayItems);
    }

    public void setList() {
        int itemHeight = 23;
        Element rightMenuTop = nifty.getCurrentScreen().findElementByName("rightMenuTop");
        int height = rightMenuTop.getHeight();
        final int displayItems = height / itemHeight;
        int newHeight = displayItems * itemHeight;
        rightMenuTop.setHeight(newHeight);
        listBox = nifty.getCurrentScreen().findNiftyControl("myListBox", ListBox.class);

        listBox.addItem("move()");
        listBox.addItem("turnRight()");
        listBox.addItem("turnLeft()");
        listBox.addItem("pickUp()");
        listBox.addItem("push()");
        listBox.addItem("unLock()");
        listBox.addItem("openDoor()");
        listBox.addItem("finish()");
        listBox.addItem("inFrontOfAnObstacle()");
        listBox.addItem("inFrontOfAGate()");
        listBox.addItem("inFrontOfADoor()");
        listBox.addItem("inFrontOfACube()");
        listBox.addItem("insideAGate()");
        listBox.addItem("inFrontOfATreasure()");
        listBox.addItem("inFrontOfAKey()");
    }

    public void copySelectedItem() {
        List<String> selection = listBox.getSelection();
        for (String s : selection) {
            nifty.getClipboard().put(s);
            System.out.println(s);
        }
    }

    public void createFinishPopup(final int score) {
        new PopupBuilder("finishPopup") {
            {
                childLayout(ElementBuilder.ChildLayoutType.Center);
                backgroundColor("#000a");

                panel(new PanelBuilder("mainWindow") {
                    {
                        backgroundColor("#e7e018f0");
                        width(percentage(60));
                        height(percentage(55));
                        childLayout(ElementBuilder.ChildLayoutType.Vertical);
                        text(new TextBuilder() {
                            {
                                text("CONGRATULATIONS!");//congratulations
                                color(Color.BLACK);
                                font("Interface/Fonts/ComicSansMSBold27.fnt");
                                style("nifty-label");
                                height(percentage(10));
                                marginBottom("10px");
                                valign(VAlign.Top);
                            }
                        });

                        text(new TextBuilder() {
                            {

                                text("Score: " + score);
                                color(Color.BLACK);
                                font("Interface/Fonts/ComicSansMSplain24.fnt");
                                style("nifty-label");
                                height(percentage(10));
                                marginBottom("10px");
                                valign(VAlign.Top);
                            }
                        });
                        control(new ControlBuilder("button") {
                            {
                                parameter("label", "Back to main menu");
                                margin("5px");
                                align(Align.Center);
                                valign(VAlign.Center);
                                width("200px");
                                height("46px");
                                interactOnClick("escape()");
                            }
                        });
                        control(new ControlBuilder("button") {
                            {
                                parameter("label", "Go to next Level");
                                margin("5px");
                                align(Align.Center);
                                valign(VAlign.Center);
                                width("200px");
                                height("46px");
                                interactOnClick("nextLevel()");
                            }
                        });
                    }
                });

            }
        }.registerPopup(nifty);
    }

    public void nextLevel() {
        int nextLevel = level.getNumber() + 1;
        int maxLevel = app.getLevelList().findMaxLevel();
        if (nextLevel > maxLevel) {
            nextLevel = maxLevel;
        }

        Level level = app.getLevelList().getLevel(nextLevel);
        if (level != null) {
            app.getStateManager().detach(this);
            app.getStateManager().attach(new LevelCreator(level));
        }
    }

    private void showFinishPopup() {
        Element popup = nifty.createPopup("finishPopup");
        nifty.showPopup(nifty.getCurrentScreen(), popup.getId(), null);
    }

    private String readTextFile(String path) throws IOException {
        Path paths = Paths.get(path);
        return readTextFile(paths);
    }

    private String readTextFile(Path path) throws IOException {

        Charset encoding = StandardCharsets.UTF_8;
        Iterator<String> it = Files.readAllLines(path, encoding).iterator();
        String text = "";
        while (it.hasNext()) {
            text += it.next() + "\n";
        }
        return text;
    }

    public void showWindow() {
        window.getElement().show();
    }

    public void goToMenu() {
        app.getStateManager().detach(this);
        app.getStateManager().attach(new MainMenu());
    }

    private ArrayList<Ray> getCams() {
        FileInputStream fi = null;
        ArrayList<Ray> cams = new ArrayList<Ray>();
        try {
            fi = new FileInputStream(LevelBuilder.SAVE_DIRECTORY + levelLoader.getFileName() + ".cam");
            ObjectInputStream ois = new ObjectInputStream(fi);
            cams = (ArrayList<Ray>) ois.readObject();
        } catch (FileNotFoundException ex) {
            return cams;
        } catch (IOException ex) {
            Logger.getLogger(LevelLoader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LevelLoader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } finally {
            try {
                fi.close();
            } catch (IOException ex) {
                Logger.getLogger(LevelLoader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (java.lang.NullPointerException ex) {
                return cams;
            }
        }
        return cams;
    }

    public void setCamera(int index) {
        Vector3f origin = new Vector3f(0, 0, 0);
        Vector3f direction = new Vector3f(0, 0, 0);
        Camera camera = app.getCamera();

        if (!cams.isEmpty()) {
            Ray ray = cams.get(index);
            origin = ray.getOrigin();
            direction = ray.getDirection();
        }

        camera.setLocation(origin);
        camera.lookAtDirection(direction, Vector3f.UNIT_Y);
    }

    public void nextCamera() {
        if (cameraIndex < cams.size() - 1) {
            cameraIndex++;
        } else {
            cameraIndex = 0;
        }
        setCamera(cameraIndex);
    }

    public void previousCamera() {
        if (cameraIndex > 0) {
            cameraIndex--;
        } else {
            cameraIndex = cams.size() - 1;
        }
        setCamera(cameraIndex);
    }

    public void moveCamForward() {
        forward = true;
    }

    public void moveCamBackward() {
        backward = true;
    }

    public void moveCamLeft() {
        left = true;
    }

    public void moveCamRight() {
        right = true;
    }

    public void moveCamUp() {
        up = true;
    }

    public void moveCamDown() {
        down = true;
    }

    public void toggleArrow() {
        Spatial arrow = app.getRootNode().getChild("arrowNode");
        if (arrow == null) {
            return;
        }
        if (arrow.getCullHint() == CullHint.Always) {
            arrow.setCullHint(CullHint.Inherit);
        } else {
            arrow.setCullHint(CullHint.Always);
        }
    }

    @NiftyEventSubscriber(id = "arrowCheckBox1")
    public void onClick(String id, NiftyMousePrimaryClickedEvent event) {
        toggleArrow();
    }

    public void increaseX() {
        Spatial arrow = app.getRootNode().getChild("arrowNode");
        if (arrow == null) {
            return;
        }
        arrow.move(1, 0, 0);
        int x = (int) arrow.getLocalTranslation().getX();
        nifty.getCurrentScreen().findElementByName("xValue").getRenderer(TextRenderer.class).setText(String.valueOf(x));
    }

    public void increaseY() {
        Spatial arrow = app.getRootNode().getChild("arrowNode");
        if (arrow == null) {
            return;
        }
        arrow.move(0, 0, 1);
        int y = (int) arrow.getLocalTranslation().getZ();
        nifty.getCurrentScreen().findElementByName("yValue").getRenderer(TextRenderer.class).setText(String.valueOf(y));
    }

    public void decreaseX() {
        Spatial arrow = app.getRootNode().getChild("arrowNode");
        if (arrow == null) {
            return;
        }
        arrow.move(-1, 0, 0);
        int x = (int) arrow.getLocalTranslation().getX();
        nifty.getCurrentScreen().findElementByName("xValue").getRenderer(TextRenderer.class).setText(String.valueOf(x));
    }

    public void decreaseY() {
        Spatial arrow = app.getRootNode().getChild("arrowNode");
        if (arrow == null) {
            return;
        }
        arrow.move(0, 0, -1);
        int y = (int) arrow.getLocalTranslation().getZ();
        nifty.getCurrentScreen().findElementByName("yValue").getRenderer(TextRenderer.class).setText(String.valueOf(y));
    }

    public void onRelease() {
        forward = false;
        backward = false;
        up = false;
        down = false;
        right = false;
        left = false;
    }

    public String setNavigatorHeight() {
        int height = app.getSettings().getHeight();
        int pos = (int) (height * (1 - topBoundary));
        return String.valueOf(pos);
    }

    public String setNavigatorWidth() {
        int width = app.getSettings().getWidth();
        int pos = (int) ((width * rightBoundary) - 120);
        return String.valueOf(pos);
    }

    public void onEndScreen() {
    }

    @NiftyEventSubscriber(id = "myListBox")
    public void onListBoxSelectionChanged(final String id, final ListBoxSelectionChangedEvent event) {
        List<String> selection = listBox.getSelection();
        for (String s : selection) {
            nifty.getClipboard().put(s);
        }
    }
}