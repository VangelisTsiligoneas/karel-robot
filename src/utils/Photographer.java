/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.post.SceneProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.system.JmeSystem;
import com.jme3.texture.FrameBuffer;

import com.jme3.util.BufferUtils;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import mygame.Main;

/**
 *
 * @author Vaggos
 * work in progress. it takes a screenshot of the level, converts ta image and saves it in project assets.
 */
public class Photographer extends AbstractAppState implements ActionListener, SceneProcessor {

    private static final Logger logger = Logger.getLogger(com.jme3.app.state.ScreenshotAppState.class.getName());
    private String filePath = null;
    private boolean capture = false;
    private Renderer renderer;
    private RenderManager rm;
    private ByteBuffer outBuf;
    private String appName;
    private int shotIndex = 0;
    private int width, height;
    private Main app;
    private ViewPort viewPort;
    /**
     * Using this constructor, the screenshot files will be written sequentially to the system
     * default storage folder.
     */
    public Photographer() {
        this(null);
    }

    /**
     * This constructor allows you to specify the output file path of the screenshot.
     * Include the seperator at the end of the path.
     * Use an emptry string to use the application folder. Use NULL to use the system
     * default storage folder.
     * @param file The screenshot file path to use. Include the seperator at the end of the path.
     */
    public Photographer(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Set the file path to store the screenshot.
     * Include the seperator at the end of the path.
     * Use an emptry string to use the application folder. Use NULL to use the system
     * default storage folder.
     * @param file File path to use to store the screenshot. Include the seperator at the end of the path.
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
            this.app = (Main) app;
            InputManager inputManager = app.getInputManager();
            
            inputManager.addListener(this, "ScreenShot");

            app.getViewPort().addProcessor(this);

            appName = app.getClass().getSimpleName();
        

        
    }

    public void onAction(String name, boolean value, float tpf) {
        if (value){
            capture = true;
        }
    }

    public void takeScreenshot() {
        capture = true;
    }
    
    public void takeScreenshot(String fileName){
        takeScreenshot();
        appName = fileName;
    }

    public void initialize(RenderManager rm, ViewPort vp) {
        renderer = rm.getRenderer();
        this.rm = rm;
        viewPort = vp;
        reshape(vp, vp.getCamera().getWidth(), vp.getCamera().getHeight());
    }

    @Override
    public boolean isInitialized() {
        return super.isInitialized() && renderer != null;
    }

    public void reshape(ViewPort vp, int w, int h) {
        outBuf = BufferUtils.createByteBuffer(w * h * 4);
        width = w;
        height = h;
    }

    public void preFrame(float tpf) {
    }

    public void postQueue(RenderQueue rq) {
    }

    public void postFrame(FrameBuffer out) {
        if (capture){
            capture = false;
            shotIndex++;

            
             /*
              Camera curCamera = rm.getCurrentCamera();
              int viewX = (int) (curCamera.getViewPortLeft() * curCamera.getWidth());
              int viewY = (int) (curCamera.getViewPortBottom() * curCamera.getHeight());
              int viewWidth = (int) ((curCamera.getViewPortRight() - curCamera.getViewPortLeft()) * curCamera.getWidth());
             int viewHeight = (int) ((curCamera.getViewPortTop() - curCamera.getViewPortBottom()) * curCamera.getHeight());
              */
              
            renderer.readFrameBuffer(out, outBuf);
            
              //app.getViewPort().getCamera().setViewPort(left, right, bottom, top);
              
            
           renderer.readFrameBuffer(out, outBuf);
            File file;
            if (filePath == null) {
                file = new File(JmeSystem.getStorageFolder() + File.separator + appName + shotIndex + ".png").getAbsoluteFile();
            } else {
                file = new File(filePath + appName + shotIndex + ".png").getAbsoluteFile();
            }
            logger.log(Level.FINE, "Saving ScreenShot to: {0}", file.getAbsolutePath());

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            try {
                JmeSystem.writeImageFile(outStream, "png", outBuf, width, height);
                convert(outStream, file);
               
            } catch (IOException ex) {
                Logger.getLogger(Photographer.class.getName()).log(Level.SEVERE, null, ex);
            }
                }
            }
        
    
    public void convert(ByteArrayOutputStream out, File file) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        BufferedImage bim = ImageIO.read(in);

        Image newImage = bim.getScaledInstance(192, 108, Image.SCALE_AREA_AVERAGING);
        
         BufferedImage bimage = new BufferedImage(newImage.getWidth(null), newImage.getHeight(null), BufferedImage.OPAQUE);

    // Draw the image on to the buffered image
    Graphics2D bGr = bimage.createGraphics();
    bGr.drawImage(newImage, 0, 0, null);
    bGr.dispose();


        ImageIO.write(bimage, "png", file);
    }
}