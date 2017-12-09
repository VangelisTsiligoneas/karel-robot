/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package levelManagement.levelCreation;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.LightList;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.util.ArrayList;
import java.util.Iterator;
import levelManagement.levelCreation.LevelBuilder;
import mygame.Main;

/**
 *
 * @author Vaggos
 * instance of this class goes "on top" of each concrete class that is subclass of LevelBuilder
 * It helps for the decoration of each level. it can be detached from the LevelBuilder class very easily
 */
public class LightAppState extends AbstractAppState {
    private Node rootNode;
    private Main app;
    protected ArrayList<SpotLight> spotLights;
    private InputManager inputManager;
     private SpotLight torch;
    protected Node lightNode;
    private AssetManager assetManager;
    private Camera cam;
    private boolean torchSwitch;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Main) app;
        rootNode = this.app.getRootNode();
        inputManager = this.app.getInputManager();
        spotLights = new ArrayList<SpotLight>();
        lightNode = new Node("lightNode");
        rootNode.attachChild(lightNode);
         torch = new SpotLight();
        rootNode.addLight(torch);
        assetManager = this.app.getAssetManager();
        cam = this.app.getCamera();
        torchSwitch = true;
        this.app.getViewPort().setBackgroundColor(ColorRGBA.Black);
        initKeys();
        initCrossHairs();
    }
    protected void initCrossHairs() {
    //setDisplayStatView(false);
    BitmapFont guiFont = app.getGuiFont();
         guiFont   = assetManager.loadFont("Interface/Fonts/Default.fnt");
    BitmapText ch = new BitmapText(guiFont, false);
    ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
    ch.setText("+"); // crosshairs
    ch.setLocalTranslation( // center
      app.getSettings().getWidth() / 2 - ch.getLineWidth()/2, app.getSettings().getHeight() / 2 + ch.getLineHeight()/2, 0);
    app.getGuiNode().attachChild(ch);
  }
    
    public void initKeys() {
        //inputManager.addMapping("Pause",  new KeyTrigger(KeyInput.KEY_P));
        
        
        inputManager.addListener(actionListener, "createLight");
        inputManager.addListener(actionListener, "deleteLight");
        inputManager.addListener(actionListener, "toggleTorch");
        inputManager.addListener(actionListener, "printLights");
    }
    
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            
            if(name.equals("createLight") && !keyPressed){
                
                Mesh box = new Box(1f, 1f, 1f);
                Geometry boxGeo = new Geometry("littleLight", box);
                Material mat = new Material(assetManager, LevelBuilder.UNSHADED_MATERIAL);
                mat.setColor("Color", ColorRGBA.Yellow);
                boxGeo.setMaterial(mat);
                
                SpotLight spotLight = new SpotLight();
                spotLight.setColor(ColorRGBA.White.multLocal(1f));
                boxGeo.setLocalTranslation(cam.getLocation());
                
                spotLight.setPosition(cam.getLocation());
                spotLight.setDirection(cam.getDirection());
                
                /*final int SHADOWMAP_SIZE=512;
                 * SpotLightShadowRenderer dlsr = new SpotLightShadowRenderer(assetManager, SHADOWMAP_SIZE);
                 * dlsr.setLight(spotLight);
                 * app.getViewPort().addProcessor(dlsr);
                 */
                rootNode.addLight(spotLight);
                spotLights.add(spotLight);
                lightNode.attachChild(boxGeo);
            }
            if (name.equals("deleteLight") && !keyPressed) {
                CollisionResults results = new CollisionResults();
                Ray ray = new Ray(cam.getLocation(), cam.getDirection());
                lightNode.collideWith(ray, results);
                //LightList localLightList = rootNode.getLocalLightList();
                for (int i = 0; i < results.size(); i++) {

                    Geometry light = results.getCollision(i).getGeometry();
                    lightNode.detachChild(light);
                    for (Iterator<SpotLight> it = spotLights.iterator(); it.hasNext();) {
                        SpotLight l = it.next();
                        if(l.getPosition().equals(light.getLocalTranslation())){
                            it.remove();
                            rootNode.removeLight(l);
                            
                        }
                    }                    
                }
            }
            if(name.equals("toggleTorch") && !keyPressed){
                torchSwitch = !torchSwitch;
            }
            if(name.equalsIgnoreCase("printLights") && !keyPressed){
                printLights();
            }
        }
    };

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if(torchSwitch){
            torch.setPosition(cam.getLocation());
        torch.setDirection(cam.getDirection());
        }
        /*Vector3f cPosition = rootNode.getChild("character1").getLocalTranslation().add(new Vector3f(0,0.5f,0));
         * Quaternion rotation = rootNode.getChild("character1").getLocalRotation();
         * 
         * cam.setLocation(cPosition);
         * cam.setRotation(rotation);
         */
    }
    
    public void printLights(){
        int count = 1;
        Vector3f position = null;
        Vector3f direction = null;
        for(SpotLight l : spotLights){
            
            System.out.println("SpotLight spotLight" + count + " = new SpotLight();");
            position = l.getPosition();
            System.out.println("spotLight" + count + ".setPotition(new Vector3f(" + position.x + "f, " + position.y + "f, " + position.z + "f));");
            direction = l.getDirection();
            System.out.println("spotLight" + count + ".setDirection(new Vector3f(" + direction.x + "f, " + direction.y + "f, " + direction.z + "f));");
            System.out.println("rootNode.addLight(spotLight" + count + ");");
            System.out.println();
            count++;
        }
        /*
         SpotLight spotLight1 = new SpotLight();
spotLight1.setPosition(new Vector3f(14.032181f, 15.062901f, 15.561783f));
spotLight1.setDirection(new Vector3f(-0.49437433f, -0.61705565f, -0.6122389f));
rootNode.addLight(spotLight1);
         */
    }

    @Override
    public void cleanup() {
        super.cleanup();
        inputManager.removeListener(actionListener);
        for(SpotLight l : spotLights){
            rootNode.removeLight(l);
        }
        rootNode.removeLight(torch);
        app.getGuiNode().detachAllChildren();
    }

    
    
}
