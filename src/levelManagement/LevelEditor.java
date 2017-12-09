/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package levelManagement;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import menu.MainMenu;
import mygame.Main;
import utils.Body;
import utils.nifty.textEditor.TextEditorControl;

/**
 *
 * @author Vaggos
 */
public abstract class LevelEditor extends AbstractAppState implements ScreenController{
    protected static String classPackage;
    protected static String javaFolder;
    private String className;
    
    protected Main app;
    private Nifty nifty;
    protected TextEditorControl textEditor1;
    protected TextEditorControl textEditor2;
    protected TextEditorControl messageBox;
    protected Element buttonElement;
    private boolean fullScreen;
    private ListBox listBox;
    protected int compile;
    
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
    
    private Runnable runnable;

    public LevelEditor() {
        fullScreen = false;
        compile = 0;
        
        forward = false;
        backward = false;
        left = false;
        right = false;
        up = false;
        down = false;
        
        leftBoundary = 0.0f;
        rightBoundary = 0.65f;
        bottomBoundary = 0.28f;
        topBoundary = 0.93f;
        
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
        this.app.getFlyByCamera().setDragToRotate(true);
        this.app.getCamera().setViewPort(leftBoundary, rightBoundary, bottomBoundary, topBoundary);
        setGameScreen();
    }
    
    protected void setGameScreen() {
        nifty = app.getNiftyDisplay().getNifty();
        nifty.fromXml("Interface/GameGui.xml", "start", this);
        app.getGuiViewPort().addProcessor(app.getNiftyDisplay());
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        this.app.getCamera().setViewPort(0f, 1f, 0f, 1f);
    }

    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        buttonElement = screen.findElementByName("run");
        textEditor1 = screen.findNiftyControl("notepadOne", TextEditorControl.class);
        textEditor2 = screen.findNiftyControl("notepadTwo", TextEditorControl.class);
        messageBox = screen.findControl("messageBox", TextEditorControl.class);
    }

    public void onStartScreen() {
        restartEditors();
        setList();
    }

    public void onEndScreen() {
        
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);        
        moveCam(tpf);
        /*
        if(compile>0){
            if(compile>100){
                app.startCommandThread(runnable);
                compile = 0;
            }
            else{
                compile ++;
            }
        }
        */
    }
    
        private void moveCam(float tpf){
        float moveSpeed = 10f;
        if(forward){
            moveCamera(tpf, false, moveSpeed);        
        }
        if(backward){
            moveCamera(-tpf, false, moveSpeed);
        }
        if(left){
            moveCamera(tpf, true, moveSpeed);
        }
        if(right){
            moveCamera(-tpf, true, moveSpeed);
        }
        if(up){
            riseCamera(tpf,moveSpeed);
        }
        if(down){
            riseCamera(-tpf, moveSpeed);
        }
    }
    
    public void setList(){
        int itemHeight = 23;
        Element rightMenuTop = nifty.getCurrentScreen().findElementByName("rightMenuTop");
      int height =  rightMenuTop.getHeight();
      final int displayItems = height/itemHeight;
      int newHeight = displayItems * itemHeight;
      rightMenuTop.setHeight(newHeight);
      listBox = nifty.getCurrentScreen().findNiftyControl("myListBox", ListBox.class);
        List<String> listContent = listContent();
        
        for(String s:listContent){
            listBox.addItem(s);
        }
    }
    
    public String getdisplayCount(){
        int itemHeight = 23;
        int height = app.getSettings().getHeight();
        int panelHeight = (int) (height * 0.93 *0.2);        
      int displayItems =  panelHeight/itemHeight;
      return String.valueOf(displayItems);
    }
    
    public void copySelectedItem(){
        List<String> selection = listBox.getSelection();
        for(String s:selection){
            nifty.getClipboard().put(s);
            System.out.println(s);
        }
    }
    
    public void showWindow() {
        
    }
    
    public void goToMenu(){
        app.getStateManager().detach(this);
        app.getStateManager().attach(new MainMenu());
    }
    
    private void riseCamera(float tpf, float moveSpeed){
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
    
    public void moveCamForward(){
        forward = true;
    }
    
    public void moveCamBackward(){
        backward = true;
    }
    
    public void moveCamLeft(){
        left = true;
    }
    
    public void moveCamRight(){
        right = true;
    }
    
    public void moveCamUp(){
        up = true;
    }
    
    public void moveCamDown(){
        down = true;
    }
    
    public void onRelease(){
        forward = false;
        backward = false;
        up = false;
        down = false;
        right = false;
        left = false;
    }
    
    public String setNavigatorHeight(){
        int height = app.getSettings().getHeight();
        int pos = (int) (height * (1-topBoundary));
        return String.valueOf(pos);
    }
    
    public String setNavigatorWidth(){
        int width = app.getSettings().getWidth();
        int pos = (int) ((width * rightBoundary) - 120);
        return String.valueOf(pos);
    }
    
    public void compile() {
        buttonElement.disable();        
        String directory = System.getProperty("user.dir") + "/assets/scripts/" + javaFolder + "/";
        //String executeClassName = "Execute";
        //String robotClassName = "MyRobot";
        //String executeFileName = directory + executeClassName + ".java";
        //String robotFileName = directory + robotClassName + ".java";
        String outPutDirectory = System.getProperty("user.dir") + "/assets/classes/";

        File outPutPath = new File(outPutDirectory);
        ArrayList<File> path = new ArrayList<File>();
        path.add(outPutPath);

        deleteClassFiles();
        File file1 = createJavaFile(directory, textEditor1.getText());
        className = file1.getName();
        File file2 = createJavaFile(directory, textEditor2.getText());
        
        ArrayList<File> filesToCompile = new ArrayList<File>();
        if(file1 != null){filesToCompile.add(file1);}
        if(file2 != null){filesToCompile.add(file2);}

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        try {
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, path);
        } catch (IOException ex) {
            Logger.getLogger(LevelCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(filesToCompile);
        StringWriter stringWriter = new StringWriter();

        boolean call = compiler.getTask(stringWriter, fileManager, null, null, null, compilationUnits).call();
        String message = stringWriter.toString();
        messageBox.setText(message);
        try {
            fileManager.close();
        } catch (IOException e) {
            System.out.println("-----------fileManager not closed!---------");
        }
        if (call) {
            ifCompile();
            compile ++;
        } else {
            buttonElement.enable();
        }
    }
    
    public File createJavaFile(String path, String content) {
        File folder = new File(path);

        if (!folder.exists()) {
            if (folder.mkdirs()) {
                System.out.println("class folder created!");
            } else {
                System.out.println("failed to create the folder :(");
                return null;
            }
        }
        
        String sampleString = "public class ";
        int indexOf = content.indexOf(sampleString);
        int indexFrom = indexOf + sampleString.length();
        int lastIndex = content.indexOf(" ", indexFrom);
        
        String className = null;
        try{
           className = content.substring(indexFrom, lastIndex);
        }catch(StringIndexOutOfBoundsException e){
            return null;
        }
        
        if(className == null || className.contains(" ")){return null;}
        
        path+= className + ".java";
        
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
    
    protected void deleteClassFiles() {
        String outPutDirectory = System.getProperty("user.dir") + "/assets/classes/" + classPackage;
        File folder = new File(outPutDirectory);

        if (!folder.exists()) {
            if (folder.mkdirs()) {
                System.out.println("class folder created!");
            } else {
                System.out.println("failed to create the folder :(");
            }
        } else {
            for (File f : new File(outPutDirectory).listFiles()) {
                f.delete();
            }
        }
    }
    
    abstract protected List<String> listContent();
    
    abstract public void setWindowContent();
    
    abstract public void restartEditors();
    
    abstract public void restart();
    abstract public void ifCompile();
    
    protected String readTextFile(String path) throws IOException{
        Path paths = Paths.get(path);
        Charset encoding = StandardCharsets.UTF_8;
        Iterator<String> it = Files.readAllLines(paths, encoding).iterator();
        String text = "";
        while(it.hasNext()){
            text += it.next() + "\n";
        }
        return text;
    }
    
}