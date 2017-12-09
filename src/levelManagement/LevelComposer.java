/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package levelManagement;

import com.jme3.app.state.AppState;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import static levelManagement.LevelEditor.javaFolder;
import utils.Body;

/**
 *
 * @author Vaggos
 */
public class LevelComposer extends LevelEditor{
    
    

    public LevelComposer() {
        super();
        classPackage = "levelCreation";
        javaFolder = "levelCreation";
    }

    @Override
    protected List<String> listContent() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("wall(int fromX, int fromY, int toX, int toY, int height)");
        list.add("floor(int xSize, int ySize)");
        list.add("treasure(int x, int y)");
        list.add("rock(int x, int y)");
        list.add("floorButton(int x, int y)");
        list.add("door(int x, int y, Direction direction, Key key)");
        list.add("terminalGate(int x, int y, Direction direction)");
        list.add("key(int x, int y, Key key)");
        list.add("setCamera(Vector3f position, Vector3f direction)");
        list.add("ambientLight(float volume)");
        list.add("light(Vector3f direction)");
        list.add("light(Vector3f position, Vector3f direction)");
        return list;
    }

    @Override
    public void setWindowContent() {
        
    }

    @Override
    public void restartEditors() {
        String path = System.getProperty("user.dir") + "/assets/misc/textEditor1.txt";
        String text;
        try {
            text = readTextFile(path);
        } catch (IOException ex) {
            text = "";
        }
        textEditor1.setText(text);
    }

    @Override
    public void restart() {
        
    }

    @Override
    public void ifCompile() {
        
    }

    @Override
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
        //className = file1.getName();
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
    
    
    
    Runnable runnable = new Runnable(){

        public void run() {
            String className = "MyLevel";

            try {
                String binaryName = "levelCreation" + "." + className;
                Class<?> classs;
                Object object;

                URL[] urls = null;

                File dir = new File(System.getProperty("user.dir") + "/assets/classes/");
                URL url = dir.toURI().toURL();
                urls = new URL[]{url};

                ClassLoader cl = new URLClassLoader(urls);

                classs = cl.loadClass(binaryName);

                object = classs.newInstance();
                
               // Class[] paramTypes = new Class[]{Body.class};
                
                app.getStateManager().attach((AppState) object);
                //System.out.println("start running!");
                //classs.getDeclaredMethod("installSoftware", paramTypes).invoke(object, params);
                //classs.getDeclaredMethod("createLevel", null).invoke(object, null);
                //System.out.println("end of running!");

                buttonElement.enable();

            } catch (InstantiationException ex) {
                Logger.getLogger(LevelCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);                
            } catch (IllegalAccessException ex) {
                Logger.getLogger(LevelCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }  catch (SecurityException ex) {
                Logger.getLogger(LevelCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(LevelCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(LevelCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(LevelCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                System.out.println("--thread NOT Closed!--");
            }
        
        }
        
    };

    @Override
    public void update(float tpf) {
        super.update(tpf);
        
        if(compile>0){
            if(compile>100){
                app.startCommandThread(runnable);
                compile = 0;
            }
            else{
                compile ++;
            }
        }
    }

    @Override
    public void onRelease() {
        super.onRelease(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
