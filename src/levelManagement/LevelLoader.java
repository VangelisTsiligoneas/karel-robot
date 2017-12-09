/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package levelManagement;

import menu.MainMenu;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.InputManager;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.SpotLightShadowRenderer;
import com.jme3.texture.Texture;
import com.jme3.util.SafeArrayList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
//import instructions.*;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import levelManagement.levelCreation.LevelBuilder;
import static levelManagement.levelCreation.LevelBuilder.LIGHTING_MATERIAL;
import static levelManagement.levelCreation.LevelBuilder.SWITCH;
import static levelManagement.levelCreation.LevelBuilder.TERMINAL_GATE;
import static levelManagement.levelCreation.LevelBuilder.UNSHADED_MATERIAL;
import minefield.InvisibleWall;
import minefield.Minefield;
import minefield.Scenario;
import mygame.Main;
import objectControl.CharacterControl;
import objectControl.PressurePlate;
import objectControl.TerminalGate;
import utils.Robot;

/**
 *
 * @author Vaggos this class loades a jm3 file from Scenes/Levels folder, addds
 * collision control and prepares the scene of the level each jm3 file is a
 * whole scnene
 */
public class LevelLoader extends AbstractAppState {

    private Node rootNode;
    private Main app;
    private InputManager inputManager;
    private AssetManager assetManager;
    private ViewPort viewPort;
    private BulletAppState bulletState;
    private String fileName;
    private boolean ready;
    
    private Robot newCharacter;
    

    public LevelLoader(String fileName) {
        super();
        this.fileName = fileName;
        ready = false;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Main) app;
        assetManager = this.app.getAssetManager();
        inputManager = this.app.getInputManager();
        viewPort = this.app.getViewPort();

        bulletState = new BulletAppState();
        this.app.getStateManager().attach(bulletState);

        //bulletState.setDebugEnabled(true);
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));

        this.app.getFlyByCamera().setMoveSpeed(10f);

        rootNode = this.app.getRootNode();
        loadLevel();

        this.app.getFlyByCamera().setDragToRotate(true);
        
       
        createGrid(20, 20);
        toggleGrid();
        arrow(0, 0, 0);
        attachSwitches();
        attachMinefields();
        setCollision();
        setShadows();
        ready = true;
    }
    
    private void setShadows(){
        LightList localLightList = rootNode.getChild("loadedNode").getLocalLightList();
        rootNode.getChild(LevelBuilder.BUILDING_NODE).setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        rootNode.getChild(LevelBuilder.FLOOR_NODE).setShadowMode(RenderQueue.ShadowMode.Receive);
        //rootNode.getChild(LevelBuilder.OBJECT_NODE).setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        
        for(Light l:localLightList){
            if(l instanceof DirectionalLight){
                DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 512, 3);
                dlsr.setLight((DirectionalLight) l);
                app.getViewPort().addProcessor(dlsr);
            }
            else if(l instanceof SpotLight){
                SpotLightShadowRenderer slsr = new SpotLightShadowRenderer(assetManager, 512);
                slsr.setLight((SpotLight) l);
                app.getViewPort().addProcessor(slsr);
            }
        }
    }

   private ArrayList<Minefield> getMineField(){
       FileInputStream fi = null;
       ArrayList<Minefield> minefields = null;
       
        try {
            fi = new FileInputStream(LevelBuilder.SAVE_DIRECTORY + fileName + ".mnf");
            ObjectInputStream ois = new ObjectInputStream(fi);
            minefields = (ArrayList<Minefield>) ois.readObject();            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LevelLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LevelLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LevelLoader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fi.close();
            } catch (IOException ex) {
                Logger.getLogger(LevelLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return minefields;
   }
   
   private ArrayList<Node> getMinefieldNodes(){
       Node objectNode = (Node) rootNode.getChild(LevelBuilder.OBJECT_NODE);
        List<Spatial> children = objectNode.getChildren();
        ArrayList<Node> minefieldNodes = new ArrayList<Node>();
        String minefieldName = LevelBuilder.MINEFIELD;
        
        for(Spatial s: children){
            if(s.getName().startsWith(minefieldName)){
                minefieldNodes.add((Node) s);
            }
        }
        return minefieldNodes;
   }
   
   public void attachMinefields(){
        ArrayList<Minefield> minefields = getMineField();
        ArrayList<Node> minefieldNodes = getMinefieldNodes();
        
        for(Node n : minefieldNodes){
            int startIndex = n.getUserData("count");
            
            Minefield minefield = minefields.get(startIndex);
            attachMinefieldToNode(n, minefield);
        }
   }
   
   private void attachMinefieldToNode(Node minefieldNode, Minefield minefield){       
       Spatial floor = minefieldNode.getChild("minefieldFloor");
       minefieldNode.detachAllChildren();
       if(floor != null)
       minefieldNode.attachChild(floor);
       
       Scenario scenario = minefield.selectScenario();
       
        for (InvisibleWall iw : scenario.getWalls()) {
            Vector2f from = iw.getFrom();
            int fromX = (int) from.x;
            int fromY = (int) from.y;
            Vector2f to = iw.getTo();
            int toX = (int) to.x;
            int toY = (int) to.y;
            //Vector3f center = new Vector3f((toX - fromX)/2, 0.49999f, (toY - fromY)/2);
            for (int x = fromX; x <= toX; x++) {
                for (int y = fromY; y <= toY; y++) {
                    CollisionShape boxCollisionShape = new BoxCollisionShape(new Vector3f(0.49999f, 0.49999f, 0.49999f));
                    GhostControl wallControl = new GhostControl(boxCollisionShape);
                    Material wallMat = new Material(assetManager, LIGHTING_MATERIAL);
                     Texture diffuseFloorTex = assetManager.loadTexture("Textures/sci_fi_metal_wall.jpg");
        
        wallMat.setTexture("DiffuseMap", diffuseFloorTex);
        
                    Box box = new Box(0.49999f, 0.49999f, 0.49999f);
                    Geometry wall = new Geometry("ghostWall",box);
                    wall.setMaterial(wallMat);
                    wall.setQueueBucket(RenderQueue.Bucket.Translucent);
                    wall.addControl(wallControl);
                    wall.setLocalTranslation(new Vector3f(x, 0, y));
                    //wall.setCullHint(CullHint.Always);
                    bulletState.getPhysicsSpace().addAll(wall);
                    minefieldNode.attachChild(wall);
                }
            }
        }
   }
   
   public void showMinefields(){
       ArrayList<Node> minefieldNodes = getMinefieldNodes();
       
       for(Node n : minefieldNodes){
           List<Spatial> children = n.getChildren();
           for(Spatial child:children){
               if(child.getName().startsWith("ghostWall")){
                   Geometry ghostWall = (Geometry) child;
                   ghostWall.setCullHint(CullHint.Never);
               }
           }
       }
   }
   
   private ArrayList<Ray> getCams(){
       FileInputStream fi = null;
       ArrayList<Ray> cams = new ArrayList<Ray>();
       try{
           fi = new FileInputStream(LevelBuilder.SAVE_DIRECTORY + fileName + ".cam");
            ObjectInputStream ois = new ObjectInputStream(fi);
            cams = (ArrayList<Ray>) ois.readObject(); 
       } catch (FileNotFoundException ex) {
            return cams;
        } catch (IOException ex) {
            Logger.getLogger(LevelLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LevelLoader.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                fi.close();
            } catch (IOException ex) {
                Logger.getLogger(LevelLoader.class.getName()).log(Level.SEVERE, null, ex);
            }catch(java.lang.NullPointerException ex){
                return cams;
            }
        }
       return cams;
   }
   
    private void setCollision() {
        Node objectNode = (Node) rootNode.getChild(LevelBuilder.OBJECT_NODE);
        Node wallNode = (Node) rootNode.getChild(LevelBuilder.WALL_NODE);
        
        for (Spatial s : objectNode.getChildren()) {
            if (s.getName().equals(LevelBuilder.DOOR_NODE) || s.getName().equals(LevelBuilder.MINEFIELD)) {
                bulletState.getPhysicsSpace().addAll(s);
            }
        }
        for (Spatial s : wallNode.getChildren()) {
            CollisionShape collisionShape = CollisionShapeFactory.createMeshShape((Node) s);

        RigidBodyControl wallControl = new RigidBodyControl(collisionShape, 0);
        s.addControl(wallControl);
        bulletState.getPhysicsSpace().add(s);
        }
        
        Spatial movableChild = rootNode.getChild(LevelBuilder.MOVEABLE_NODE);
        if(movableChild != null)
        bulletState.getPhysicsSpace().addAll(movableChild);
    }

    public void createGrid(int xLines, int yLines) {
        float lineDist = 1.0f;
        Node gridNode = (Node) rootNode.getChild(LevelBuilder.GRID_NODE);

        Grid gridMesh = new Grid(xLines + 1, yLines + 1, lineDist);
        Material gridMat = new Material(assetManager, UNSHADED_MATERIAL);
        gridMat.setColor("Color", ColorRGBA.Green);
        Geometry grid = new Geometry("grid", gridMesh);
        grid.setMaterial(gridMat);

        grid.setLocalTranslation(new Vector3f(0f, 0.0001f, 0f));
        if (gridNode != null) {
            gridNode.attachChild(grid);
        }
    }

    public void toggleGrid() {
        Spatial gridNode = rootNode.getChild(LevelBuilder.GRID_NODE);
        if (gridNode.getCullHint() == CullHint.Always) {
            gridNode.setCullHint(CullHint.Never);
        } else if (gridNode.getCullHint() == CullHint.Never) {
            gridNode.setCullHint(CullHint.Always);
        } else {
            gridNode.setCullHint(CullHint.Always);
        }
    }

    private void attachSwitches() {
        TerminalGate gate = rootNode.getChild(TERMINAL_GATE).getControl(TerminalGate.class);
        if (gate != null) {
            SafeArrayList<PressurePlate> switchControls = new SafeArrayList<PressurePlate>(PressurePlate.class);
            Node objectNode = (Node) rootNode.getChild(LevelBuilder.OBJECT_NODE);
            List<Spatial> switches = objectNode.getChildren();
            for (Spatial s : switches) {
                if (s.getName().equals(SWITCH)) {
                    PressurePlate switchControl = s.getControl(PressurePlate.class);
                    switchControls.add(switchControl);
                }
            }
            gate.setSwitches(switchControls);
        }
    }

    private void escape() {
        app.getStateManager().detach(this);
        app.getStateManager().attach(new MainMenu());
    }

    public void restart() {
        app.getStateManager().detach(this);
        app.getStateManager().attach(this);
    }
    
    public int getBasicScore() {
        int score;
        try{
            score = rootNode.getChild("loadedNode").getUserData("basic_score");
        }catch(NullPointerException e){
            return 0;
        }
        return score;
    }
    
    public ArrayList<CharacterControl> getCharacters(){        
        ArrayList<CharacterControl> characters = new ArrayList<CharacterControl>();
        if(rootNode == null){
            return characters;
        }
        Node characterNode = (Node) rootNode.getChild(LevelBuilder.CHARACTER_NODE);
        for(Spatial s:characterNode.getChildren()){
            characters.add(s.getControl(CharacterControl.class));
        }
        return characters;        
    }
    
    public ArrayList<Spatial> getCharacterSpatials(){
         ArrayList<Spatial> spatials = new ArrayList<Spatial>();
        if(rootNode == null){
            return spatials;
        }
        Node characterNode = (Node) rootNode.getChild(LevelBuilder.CHARACTER_NODE);
        for(Spatial s:characterNode.getChildren()){
            spatials.add(s);
        }
        return spatials;   
    }

    public void loadLevel() {
        assetManager.registerLocator(LevelBuilder.SAVE_DIRECTORY, FileLocator.class);
        try {
            Node loadedNode = (Node) assetManager.loadModel(fileName + ".j3o");
            loadedNode.setName("loadedNode");            
            rootNode.attachChild(loadedNode);
        } catch (AssetNotFoundException ex) {
            System.out.println("asset not found!abort to main menu");
        }
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if(newCharacter != null){
            Spatial spatial = newCharacter.getSpatial();
            Node characterNode = (Node) rootNode.getChild(LevelBuilder.CHARACTER_NODE);
            characterNode.attachChild(spatial);
        bulletState.getPhysicsSpace().addAll(spatial);
        newCharacter.setDone();
        newCharacter = null;
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
        app.getStateManager().detach(bulletState);

        rootNode.detachAllChildren();
        LightList localLightList = rootNode.getLocalLightList();
        for (Iterator<Light> it = localLightList.iterator(); it.hasNext();) {
            Light l = it.next();
            rootNode.removeLight(l);
        }
        viewPort.clearProcessors();
    }

    public BulletAppState getBulletState() {
        return bulletState;
    }

    public String getFileName() {
        return fileName;
    }

    public synchronized void setNewCharacter(Robot newCharacter) {
        this.newCharacter = newCharacter;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }
    
    private Geometry singleArrow(Vector3f direction, ColorRGBA color) {
        Material arrowMat = new Material(app.getAssetManager(), LevelBuilder.UNSHADED_MATERIAL);
        Arrow arrowMesh = new Arrow(direction);
        arrowMat.setColor("Color", color);
        //create Geometry
        Geometry arrowGeo = new Geometry("arrow", arrowMesh);
        arrowGeo.scale(2,1,1);
        arrowGeo.setMaterial(arrowMat);
        return arrowGeo;
    }
    
    public void arrow(int x, int y, int z) {
        //create geometry
        Geometry arrowGeoX = singleArrow(new Vector3f(1, 0, 0), ColorRGBA.Green);
        Geometry arrowGeoY = singleArrow(new Vector3f(0, 0, 1), ColorRGBA.Red);
        Geometry arrowGeoZ = singleArrow(new Vector3f(0, 1, 0), ColorRGBA.Blue);
        // create node
        Node arrow = new Node("arrowNode");
        arrow.attachChild(arrowGeoX);
        arrow.attachChild(arrowGeoY);
        arrow.attachChild(arrowGeoZ);

        arrow.setLocalTranslation(new Vector3f(x, y, z));
        app.getRootNode().attachChild(arrow);
        arrow.setCullHint(Spatial.CullHint.Always);
    }
}
