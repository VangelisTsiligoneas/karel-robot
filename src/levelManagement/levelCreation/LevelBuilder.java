/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package levelManagement.levelCreation;

import minefield.Scenario;
import minefield.InvisibleWall;
import minefield.Minefield;
import objectControl.CharacterControl;
import objectControl.Treasure;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Torus;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.texture.Texture;
import com.jme3.util.SafeArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.util.logging.Logger;
import jme3tools.optimize.GeometryBatchFactory;
import levelManagement.Level;
import menu.MainMenu;
import mygame.Direction;
import mygame.Key;
import mygame.Main;
import objectControl.*;

/**
 *
 * @author Vaggos
 * abstract class for creating each Level
 * Very important class for this project!
 */
public abstract class LevelBuilder extends AbstractAppState {

    public static final String BUILDING_NODE = "buildingNode";
    public static final String OBJECT_NODE = "objectNode";
    public static final String DOOR_NODE = "doorNode";
    public static final String GRID_NODE = "gridNode";
    public static final String WALL_NODE = "wallNode";
    public static final String FLOOR_NODE = "floorNode";
    public static final String TAKEABLE_NODE = "takeableNode";
    public static final String MOVEABLE_NODE = "moveableNode";
    public static final String CHARACTER_NODE = "characterNode";
    public static final String TREASURE = "treasure";
    public static final String KEY = "key";
    public static final String TERMINAL_GATE = "terminalGate";
    public static final String SWITCH = "switch";
    public static final String MINEFIELD = "minefield";
    public static final String DEFAULT_FILENAME = "default_level";
    public static final String SAVE_DIRECTORY = System.getProperty("user.dir") + "/assets/Scenes/Levels/";
    public static final String UNSHADED_MATERIAL = "Common/MatDefs/Misc/Unshaded.j3md";
    public static final String LIGHTING_MATERIAL = "Common/MatDefs/Light/Lighting.j3md";
    
    protected AssetManager assetManager;
    protected FlyByCamera flyCam;
    protected Main app;
    protected Camera cam;
    protected Node rootNode;
    protected ViewPort viewPort;
    private InputManager inputManager;
    protected Node buildingNode;
    protected Node objectNode;
    protected Node gridNode;
    protected BulletAppState bulletAppState;
    private LightAppState lightAppState;
    private CharacterControl character;
    protected ArrayList<CharacterControl> characters;
    protected ArrayList<Minefield> minefields;
    protected ArrayList<Ray> cams;
    private Level level;
    
    

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        level = new Level(0, "", DEFAULT_FILENAME);
        this.app = (Main) app;
        this.assetManager = app.getAssetManager();
        this.viewPort = this.app.getViewPort();
        this.flyCam = this.app.getFlyByCamera();
        this.cam = this.app.getCamera();
        //this.inputManager = this.app.getInputManager();
        this.inputManager = this.app.getInputManager();
        flyCam.setMoveSpeed(10);
        cam.setLocation(new Vector3f(0.39384592f, 11.828153f, 23.181461f));
        cam.setRotation(new Quaternion(0.062156238f, 0.936923f, -0.26374283f, 0.22079766f));
        rootNode = this.app.getRootNode();
        
        buildingNode = new Node(BUILDING_NODE);
        objectNode = new Node(OBJECT_NODE);
        gridNode = new Node(GRID_NODE);
        
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        characters = new ArrayList<CharacterControl>();
        minefields = new ArrayList<Minefield>();
        cams = new ArrayList<Ray>();

        Node wallNode = new Node(WALL_NODE);
        Node floorNode = new Node(FLOOR_NODE);
        Node takeableNode = new Node(TAKEABLE_NODE);
        Node moveableNode = new Node(MOVEABLE_NODE);
        Node characterNode = new Node(CHARACTER_NODE);

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        //bulletAppState.setDebugEnabled(true);
        //createAmbientLight(1.5f);

        //characterNode = new Node("characterNode");
        buildingNode.setLocalTranslation(new Vector3f(0, 0, 0));
        objectNode.setLocalTranslation(new Vector3f(0, 0.5f, 0));
        gridNode.setLocalTranslation(new Vector3f(-0.5f, 0f, -0.5f));

        wallNode.setLocalTranslation(new Vector3f(0, 0f, 0));
        floorNode.setLocalTranslation(new Vector3f(0, -0.00001f, 0));
        takeableNode.setLocalTranslation(new Vector3f());
        moveableNode.setLocalTranslation(new Vector3f());

        rootNode.attachChild(gridNode);
        rootNode.attachChild(buildingNode);
        rootNode.attachChild(objectNode);

        buildingNode.attachChild(wallNode);
        buildingNode.attachChild(floorNode);
        objectNode.attachChild(moveableNode);
        objectNode.attachChild(takeableNode);
        moveableNode.attachChild(characterNode);

        createLevel();
        attachSwitches();
        initKeys();
        lightAppState = new LightAppState();
        //put this line in commands if you like to disable the lightAppState
        //
        //this.app.getStateManager().attach(lightAppState);
    }
    
    
    public void setBasicScore(int score){
        rootNode.setUserData("basic_score", score);
    }
    
    public void setCamera(Vector3f position, Vector3f direction){
        
        app.getCamera().setLocation(position);
        app.getCamera().lookAtDirection(direction, Vector3f.UNIT_Y);
    }
    
    public void addCamera(Vector3f position, Vector3f direction){
        Ray ray = new Ray(position, direction);
        cams.add(ray);
    }
    
    public void ambientLight(float volume){
        AmbientLight light = new AmbientLight();
        light.setColor(ColorRGBA.White.multLocal(volume));
        buildingNode.addLight(light);
    }

    public void character(int x, int y, Direction direction) {
        float size = 0.3f;
        int count = characters.size();
        Node moveableNode = (Node) objectNode.getChild("moveableNode");
        Spatial characterSpatial = assetManager.loadModel("Models/My karel.j3o");
        characterSpatial.setName("character" + count);

        characterSpatial.setLocalTranslation(x, 0f, y);

        CylinderCollisionShape shape = new CylinderCollisionShape(new Vector3f(size, size, size), 1);
        
        CharacterControl character = new CharacterControl(shape, direction.getDirection());
        characterSpatial.addControl(character);
        character.setSpeed(2.5f);
        if(count == 0)this.character = character;
        characters.add(character);
        bulletAppState.getPhysicsSpace().add(characterSpatial);
        moveableNode.attachChild(characterSpatial);
    }

    public void initKeys() {
        inputManager.addListener(actionListener, "move");
        inputManager.addListener(actionListener, "rotate");
        inputManager.addListener(actionListener, "toggleGrid");
        inputManager.addListener(actionListener, "pickup");
        inputManager.addListener(actionListener, "push");
        inputManager.addListener(actionListener, "unlock");
        inputManager.addListener(actionListener, "open");
        inputManager.addListener(actionListener, "check");
        inputManager.addListener(actionListener, "go to main menu");
        inputManager.addListener(actionListener, "restart");
        inputManager.addListener(actionListener, "write");
        inputManager.addListener(actionListener, "execute");
    }
    
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            Node moveableNode = (Node) objectNode.getChild("moveableNode");

            if (character != null) {
                if (name.equals("move") && !keyPressed) {
                    character.move();
                }
                if (name.equals("rotate") && !keyPressed) {
                    character.turnleft();
                }

                if (name.equals("pickup") && !keyPressed) {
                    character.pickup();
                }
                if (name.equals("push") && !keyPressed) {
                    character.push();
                }
                if (name.equals("unlock") && !keyPressed) {
                    character.unlockDoor();
                }
                if (name.equals("open") && !keyPressed) {
                    character.openDoor();
                }
                if (name.equals("check") && !keyPressed) {
                    character.inFrontOfAnObstacle();
                }
            }
            if (name.equals("go to main menu") && !keyPressed) {
                escape();
            }
            if (name.equals("restart") && !keyPressed) {
                restart();
            }
            if (name.equals("write") && !keyPressed) {
                writeSceneGraph();
            }
            if(name.equals("execute") && !keyPressed){
               // app.startCommandThread(runnable);
            }
            /*if (name.equals("toggleGrid") && !keyPressed) {
             * toggleGrid();
             * }*/
        }
    };

    private void escape() {
        app.getStateManager().detach(this);
        app.getStateManager().attach(new MainMenu());
    }

    private void restart() {
        app.getStateManager().detach(this);
        app.getStateManager().attach(this);
    }

    public void wall(Vector2f from, Vector2f to, int height, Material material) {
        float size = 0.49999f;
        Node wallNode = (Node) buildingNode.getChild(WALL_NODE);
        //Node gridNode = (Node) rootNode.getChild(GRID_NODE);

        Material wireframeMat = new Material(assetManager, UNSHADED_MATERIAL);
        wireframeMat.setColor("Color", ColorRGBA.Green);

        Node wall = new Node("wall");

        Node wireframe = new Node("wireframe");

        Box boxMesh = new Box(size, size, size);
        WireBox gridMesh = new WireBox(.5f, .5f, .5f);

        for (int x = (int) from.x; x <= to.x; x++) {
            for (int y = (int) from.y; y <= to.y; y++) {
                for (int h = 0; h < height; h++) {

                    Geometry wallGeo = new Geometry("box", boxMesh);

                    Vector3f position = new Vector3f(x, h + 0.5f, y);
                    Geometry wireframeGeo = new Geometry("wirebox", gridMesh);

                    wallGeo.setLocalTranslation(position);

                    /*CollisionShape collisionShape = CollisionShapeFactory.createBoxShape(wallGeo);
                     * GhostControl wallControl = new GhostControl(collisionShape);
                     * wallGeo.addControl(wallControl);*/

                    wall.attachChild(wallGeo);
//                    bulletAppState.getPhysicsSpace().add(wallGeo);

                    wireframeGeo.setLocalTranslation(position.add(new Vector3f(0.5f, 0, 0.5f)));
                    wireframe.attachChild(wireframeGeo);
                }
            }
        }
        
        if(material.getAdditionalRenderState().getBlendMode() == BlendMode.Alpha){
            wall.setQueueBucket(RenderQueue.Bucket.Translucent);
        }
        
        wall.setMaterial(material);
        
        Spatial wallSpatial = GeometryBatchFactory.optimize(wall);
        wallNode.attachChild(wallSpatial);

        CollisionShape collisionShape = CollisionShapeFactory.createMeshShape((Node) wallSpatial);

        RigidBodyControl wallControl = new RigidBodyControl(collisionShape, 0);
        wallSpatial.addControl(wallControl);

        bulletAppState.getPhysicsSpace().add(wallSpatial);
        Spatial wireframeSpatial = GeometryBatchFactory.optimize(wireframe);
        wireframeSpatial.setMaterial(wireframeMat);
        gridNode.attachChild(wireframeSpatial);
    }

    public void wall(int fromX, int fromY, int toX, int toY, int height, Material material) {
        wall(new Vector2f(fromX,fromY), new Vector2f(toX, toY), height, material);
    }
    
    public void wall(int fromX, int fromY, int toX, int toY, int height){
        wall(fromX, fromY, toX, toY, height, defaultWallMaterial());
    }

    public void minefield(Vector2f location, Minefield m) {
        int count = minefields.size();
        Node minefield = new Node(MINEFIELD);
        minefield.setUserData("count", count);
        minefields.add(m);
        minefield.setLocalTranslation(location.x, 0, location.y);
        int sizeX = m.getSizeX();
        int sizeY = m.getSizeY();
        Scenario scenario = m.selectScenario();
        Material floorMat = new Material(assetManager, LIGHTING_MATERIAL);
        //Texture diffuseFloorTex = assetManager.loadTexture("Textures/textures_iron_floor_80x80.jpg");
        Texture diffuseFloorTex = assetManager.loadTexture("Textures/floor/diffuseFloor.jpg");
        Texture normalFloorTex = assetManager.loadTexture("Textures/floor/normalFloor.jpg");
        Texture ambientFloorTex = assetManager.loadTexture("Textures/floor/ambientFloor.jpg");
        floorMat.setTexture("DiffuseMap", diffuseFloorTex);
        floorMat.setTexture("NormalMap", normalFloorTex);
        floorMat.setTexture("SpecularMap", ambientFloorTex);
        //floorMat.setColor("Color", ColorRGBA.Gray);

        Spatial floor = floorSpatial(sizeX, sizeY, floorMat);
        floor.setName("minefieldFloor");
        floor.setLocalTranslation(new Vector3f(0, -0.49999f, 0));
        minefield.attachChild(floor);

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
                    Node wall = new Node("ghostWall");
                    wall.addControl(wallControl);
                    wall.setLocalTranslation(new Vector3f(x, 0, y));
                    bulletAppState.getPhysicsSpace().addAll(wall);
                    minefield.attachChild(wall);
                }
            }
        }
        objectNode.attachChild(minefield);
    }

    public Spatial floorSpatial(int xSize, int ySize, Material material) {
        float size = 0.49999f;

        Box floorMesh = new Box(size, 0, size);
        Node floorNode = new Node();
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                Geometry floorGeo = new Geometry("floor", floorMesh);
                floorGeo.setMaterial(material);

                floorGeo.setLocalTranslation(x, 0, y);

                floorNode.attachChild(floorGeo);
            }
        }
        Spatial floorSpatial = GeometryBatchFactory.optimize(floorNode);
        return floorSpatial;
    }

    public void floor(int xSize, int ySize, Material material) {
        Spatial floorGeo = floorSpatial(xSize, ySize, material);
        Node floorNode = (Node) buildingNode.getChild(FLOOR_NODE);
        floorNode.setUserData("xSize", xSize);
        floorNode.setUserData("ySize", ySize);
        floorNode.attachChild(floorGeo);
    }
    
    public void floor(int xSize, int ySize){
        floor(xSize, ySize, defaultWallMaterial());
    }

    public void light(Vector3f direction) {
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, cam.getWidth(), 3);
        DirectionalLight light = new DirectionalLight();
        light.setDirection(direction);
        light.setColor(ColorRGBA.White.mult(1.8f));

        rootNode.addLight(light);

        dlsr.setLight(light);
        viewPort.addProcessor(dlsr);
    }
    
    protected void light(Vector3f position, Vector3f direction){
        SpotLight spotLight = new SpotLight();        
        spotLight.setPosition(position);
        spotLight.setDirection(direction);
        rootNode.addLight(spotLight);
    }

    public void arrow(int x, int y, int z) {

        //create geometry
        Geometry arrowGeoX = createArrow("x", ColorRGBA.Green);
        Geometry arrowGeoY = createArrow("y", ColorRGBA.Red);
        Geometry arrowGeoZ = createArrow("z", ColorRGBA.Blue);
        // create node
        Node arrow = new Node("arrowNode");
        arrow.attachChild(arrowGeoX);
        arrow.attachChild(arrowGeoY);
        arrow.attachChild(arrowGeoZ);

        arrow.setLocalTranslation(new Vector3f(x, y, z));
        rootNode.attachChild(arrow);
    }

    private Geometry createArrow(String direction, ColorRGBA color) {
        //create Mesh
        String geoName = "";
        Arrow arrowMesh;
        if (direction.equals("x")) {
            arrowMesh = new Arrow(new Vector3f(1, 0, 0));
            geoName = "arrowX";
        } else if (direction.equals("y")) {
            arrowMesh = new Arrow(new Vector3f(0, 1, 0));
            geoName = "arrowY";
        } else if (direction.equals("z")) {
            arrowMesh = new Arrow(new Vector3f(0, 0, 1));
            geoName = "arrowZ";
        } else {
            arrowMesh = new Arrow(new Vector3f(0, 0, 0));
        }

        //create Material
        Material arrowMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        arrowMat.setColor("Color", color);
        //create Geometry
        Geometry arrowGeo = new Geometry(geoName, arrowMesh);
        arrowGeo.setMaterial(arrowMat);
        return arrowGeo;
    }
    
    public void treasure(int x, int y){
        treasure(x, y, 100);
    }

    public void treasure(int x, int y, int points) {
        Sphere sphereMesh = new Sphere(8, 8, 0.2f);
        //String name = "treasure";
        Node takeable = (Node) objectNode.getChild("takeableNode");


        Material sphereMat = new Material(assetManager, LIGHTING_MATERIAL);
        sphereMat.setBoolean("UseMaterialColors", true);
        sphereMat.setColor("Diffuse", ColorRGBA.Yellow);
        sphereMat.setColor("Specular", ColorRGBA.White); // for shininess
        sphereMat.setFloat("Shininess", 128f);

        Geometry sphereGeo = new Geometry(TREASURE, sphereMesh);
        sphereGeo.setMaterial(sphereMat);
        sphereGeo.setLocalTranslation(new Vector3f(x, 0, y));

        SphereCollisionShape sphereShape = new SphereCollisionShape(0.2f);
        Treasure treasure = new Treasure(sphereShape, points);
        treasure.setSpeed(1f);

        sphereGeo.addControl(treasure);
        takeable.attachChild(sphereGeo);
    }

    public void rock(int x, int y) {
        float boxSize = 0.4f;


        Node moveableNode = (Node) objectNode.getChild(MOVEABLE_NODE);
        Box box = new Box(boxSize, boxSize, boxSize);
        Material rockMat = new Material(assetManager, LIGHTING_MATERIAL);
        Texture rockTex = assetManager.loadTexture("Textures/cube.jpg");
        rockMat.setTexture("DiffuseMap", rockTex);



        Geometry rockGeo = new Geometry("rock", box);

        CollisionShape boxShape = CollisionShapeFactory.createBoxShape(rockGeo);

        Rock rockControl = new Rock(boxShape, new Vector3f(1, 0, 0));
        rockGeo.setMaterial(rockMat);
        rockGeo.addControl(rockControl);

        bulletAppState.getPhysicsSpace().addAll(rockGeo);

        rockGeo.setLocalTranslation(x, 0, y);
        moveableNode.attachChild(rockGeo);
    }

    public void pressurePlate(int x, int y) {
        String name = "pressurePlate";
        float size = 0.49999f;
        Box box = new Box(size, 0, size);

        Material pressurePlateMat = new Material(assetManager, LIGHTING_MATERIAL);
        pressurePlateMat.setColor("Diffuse", ColorRGBA.Yellow);

        BoxCollisionShape boxShape = new BoxCollisionShape(new Vector3f(size, 0, size));

        PressurePlate pressurePlate = new PressurePlate(boxShape);

        Geometry pressurePlateGeo = new Geometry(name, box);
        pressurePlateGeo.setMaterial(pressurePlateMat);
        pressurePlateGeo.addControl(pressurePlate);

        pressurePlateGeo.setLocalTranslation(new Vector3f(x, -0.49999f, y));
        objectNode.attachChild(pressurePlateGeo);
    }

    public void floorButton(int x, int y) {
        float size = 0.49999f;

        Box box = new Box(size, 0, size);

        Material pressurePlateMat = new Material(assetManager, LIGHTING_MATERIAL);
        Texture switchTex = assetManager.loadTexture("Textures/woodHD.png");
        pressurePlateMat.setTexture("DiffuseMap", switchTex);
        pressurePlateMat.setColor("Diffuse", ColorRGBA.Red);
        pressurePlateMat.setBoolean("UseMaterialColors", true);

        BoxCollisionShape boxShape = new BoxCollisionShape(new Vector3f(size, 0, size));

        PressurePlate pressurePlate = new PressurePlate(boxShape);

        Geometry pressurePlateGeo = new Geometry(SWITCH, box);
        pressurePlateGeo.setMaterial(pressurePlateMat);
        pressurePlateGeo.addControl(pressurePlate);

        pressurePlateGeo.setLocalTranslation(new Vector3f(x, -0.49999f, y));
        objectNode.attachChild(pressurePlateGeo);
    }

    public void door(int x, int y, Direction direction, Key key) {
        float sizeX = 0.49999f;
        float sizeY = 0.49999f;
        float sizeZ = 0.29999f;

        Box boxMesh = new Box(sizeX, sizeY, sizeZ);
        //Cylinder cylinderMesh = new Cylinder(4, 10, 0.2f, 0.2f, true);
        Box littleBoxMesh = new Box(0.1f, 0.1f, 0.1f);

        Material boxMaterial = new Material(assetManager, LIGHTING_MATERIAL);
        boxMaterial.setColor("Diffuse", key.getColor());
        boxMaterial.setBoolean("UseMaterialColors", true);

        Material keyholeMaterial = new Material(assetManager, LIGHTING_MATERIAL);
        keyholeMaterial.setColor("Diffuse", ColorRGBA.Red);
        keyholeMaterial.setBoolean("UseMaterialColors", true);

        Geometry boxGeo = new Geometry("unlockableDoor", boxMesh);
        Geometry keyholeGeo = new Geometry("keyhole", littleBoxMesh);
        keyholeGeo.setLocalTranslation(new Vector3f(0f, 0f, -sizeZ));

        boxGeo.setMaterial(boxMaterial);
        keyholeGeo.setMaterial(keyholeMaterial);

        Node doorNode = new Node("doorNode");
        doorNode.setLocalTranslation(new Vector3f(x, 0, y));

        doorNode.attachChild(boxGeo);
        doorNode.attachChild(keyholeGeo);
        objectNode.attachChild(doorNode);

        CollisionShape boxShape = CollisionShapeFactory.createBoxShape(boxGeo);
        UnlockableDoor unlockableDoor = new UnlockableDoor(boxShape, direction.getDirection());
        doorNode.addControl(unlockableDoor);
        //doorNode.setMaterial(boxMaterial);
        unlockableDoor.setKeyholeNumber(key.getKeyNumber());

        bulletAppState.getPhysicsSpace().addAll(doorNode);
    }

    public void terminalGate(int x, int y, Direction direction) {
        objectNode.detachChildNamed(TERMINAL_GATE);

        float sizeX = 0.49999f;
        float height = 0.99999f;
        Vector3f ledSize = new Vector3f(0.15f, 0.15f, 0.15f);
        Vector3f ledLocation = new Vector3f(0f, height / 2 + ledSize.getY() + 0.1f, -sizeX + ledSize.getZ());

        Node gateNode = new Node(TERMINAL_GATE);

        Mesh bodyMesh = new Cylinder(8, 8, sizeX, height, true);
        Quaternion rotation = new Quaternion();
        rotation.fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Z);


        Mesh ledMesh = new Box(ledSize.getX(), ledSize.getY(), ledSize.getZ());

        Material bodyMat = new Material(assetManager, LIGHTING_MATERIAL);
        bodyMat.setColor("Diffuse", new ColorRGBA(0, 1, 1, 0.5f));
        bodyMat.setBoolean("UseMaterialColors", true);
        bodyMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

        Material ledMat = new Material(assetManager, LIGHTING_MATERIAL);
        ledMat.setColor("Diffuse", new ColorRGBA(1, 0, 0, 1f));
        ledMat.setBoolean("UseMaterialColors", true);

        Geometry bodyGeo = new Geometry("gate", bodyMesh);
        bodyGeo.setQueueBucket(RenderQueue.Bucket.Translucent);
        Geometry ledGeo = new Geometry("led", ledMesh);
        ledGeo.setLocalTranslation(ledLocation);

        bodyGeo.setMaterial(bodyMat);
        ledGeo.setMaterial(ledMat);


        bodyGeo.rotate(FastMath.HALF_PI, 0, 0);


        CollisionShape collisionShape = new BoxCollisionShape(new Vector3f(0.49999f, 0.49999f, 0.49999f));

        TerminalGate gateControl = new TerminalGate(collisionShape, direction.getDirection());
        gateNode.setLocalTranslation(x, 0, y);
        gateNode.addControl(gateControl);

        gateNode.attachChild(bodyGeo);
        gateNode.attachChild(ledGeo);

        objectNode.attachChild(gateNode);
    }

    public void key(int x, int y, Key key) {
        //Torus keyMesh = new Torus(15, 15, 0.1f, 0.3f);
        Spatial keyGeo = assetManager.loadModel("Models/key.j3o");
        keyGeo.setName(KEY);
        Material keyMat = new Material(assetManager, LIGHTING_MATERIAL);
        keyMat.setColor("Ambient", key.getColor());
        keyMat.setColor("Diffuse", key.getColor());
        keyMat.setBoolean("UseMaterialColors", true);

        //Geometry keyGeo = new Geometry(KEY, keyMesh);
        keyGeo.setMaterial(keyMat);
        keyGeo.setLocalTranslation(new Vector3f(x, 0, y));

        BoxCollisionShape keyShape = new BoxCollisionShape(new Vector3f(0.3f, 0.3f, 0.3f));
        KeyControl keyController = new KeyControl(keyShape);
        keyController.setKeyNumber(key.getKeyNumber());

        keyGeo.addControl(keyController);

        Node takeableNode = (Node) objectNode.getChild("takeableNode");
        takeableNode.attachChild(keyGeo);
    }

    private void attachSwitches() {
        Spatial gateSpatial = objectNode.getChild(TERMINAL_GATE);
        if(gateSpatial == null)return;
        TerminalGate gate = gateSpatial.getControl(TerminalGate.class);
        if (gate != null) {
            SafeArrayList<PressurePlate> switchControls = new SafeArrayList<PressurePlate>(PressurePlate.class);

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

    @Override
    public void update(float tpf) {
        super.update(tpf);
    }

    public void registerLevel(int numberOfLevel, String title, String fileName) {
        //LevelList lvList = app.getLevelList();
        level = new Level(numberOfLevel, title, fileName);
    }
    
    /*
     * called by writeSceneGraph
     */
    /*
    public void registerLevel(String title) {
        LevelList lvList = app.getLevelList();
        if (lvList.isTitleUnique(title) && lvList.isFileNameUnique(title)) {
            level = new Level(title);
        } else {
            System.out.println("this title is already in use. Try an other one");
        }
    }
    */
    private void writeMineField(){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(SAVE_DIRECTORY + level.getFileName() + ".mnf");
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(this.minefields);
            out.close();
            fileOutputStream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LevelBuilder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LevelBuilder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    private void writeCameras(){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(SAVE_DIRECTORY + level.getFileName() + ".cam");
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(this.cams);
            out.close();
            fileOutputStream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LevelBuilder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LevelBuilder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    protected final void writeSceneGraph() {
        BinaryExporter exporter = BinaryExporter.getInstance();
        File file = new File(SAVE_DIRECTORY + level.getFileName() + ".j3o");
        try {
            exporter.save(rootNode, file);
            writeMineField();
            writeCameras();
            if (level.getFileName().equals(DEFAULT_FILENAME)) {
                System.out.println("you haven't registered your level!");
            }
            System.out.println("finish writting");
            app.getLevelList().addLevel(level);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, "Error: Failed to save game!", ex);
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
        inputManager.removeListener(actionListener);
        app.getStateManager().detach(bulletAppState);
        app.getStateManager().detach(lightAppState);
        rootNode.detachAllChildren();
        LightList localLightList = rootNode.getLocalLightList();
        for (Iterator<Light> it = localLightList.iterator(); it.hasNext();) {
            Light l = it.next();
            rootNode.removeLight(l);
        }
        viewPort.clearProcessors();
        app.interruptCommandThread();
    }
    
    /*
     * returns the first character that is created on the level
     */
    public CharacterControl getCharacter() {
        return character;
    }
    
    protected Material defaultWallMaterial(){
        Material wallMat = new Material(assetManager, LIGHTING_MATERIAL);
        //wallMat.setColor("Diffuse", ColorRGBA.Blue);
        //wallMat.setColor("Ambient", ColorRGBA.Red);
        //wallMat.setBoolean("UseMaterialColors",true);
        Texture wallTex = assetManager.loadTexture("Textures/walltexture_portal2_80x80.png");
        
        wallMat.setTexture("DiffuseMap", wallTex);
        //wallMat.getAdditionalRenderState().setWireframe(true);
        //wallMat.setBoolean("UseMaterialColors",true);
        //wallMat.setColor("Diffuse",ColorRGBA.White);
        //wallMat.setColor("Specular",ColorRGBA.White);
        return wallMat;
    }
    
    protected Material transparentMaterial(){
        Material wallMat = new Material(assetManager, LIGHTING_MATERIAL);
        wallMat.setColor("Diffuse", new ColorRGBA(0.8f, 0.05f, 0.1f, 0.5f));
        wallMat.setBoolean("UseMaterialColors", true);
        wallMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        return wallMat;
        /*
         * Material bodyMat = new Material(assetManager, LIGHTING_MATERIAL);
        bodyMat.setColor("Diffuse", new ColorRGBA(0, 1, 1, 0.5f));
        bodyMat.setBoolean("UseMaterialColors", true);
        bodyMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
         */
    }
    
    
    protected Material defaultFloorMaterial(){
        Material floorMat = new Material(assetManager, LIGHTING_MATERIAL);
        //floorMat.setColor("Color", ColorRGBA.Orange);
        Texture foorTex = assetManager.loadTexture("Textures/stone_floor_60x60.JPG");
        floorMat.setTexture("DiffuseMap", foorTex);
        return floorMat;
    }
    /*
     * methods for adding objects in the scene, are called inside the method
     */
    abstract protected void createLevel();    
}