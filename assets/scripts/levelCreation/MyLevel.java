package levelCreation;

import levelManagement.levelCreation.LevelBuilder;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import mygame.Direction;
import mygame.Key;

public class MyLevel extends LevelBuilder{

    protected void createLevel() {
light(new Vector3f(0f,-1f,0f));
wall(0,0,0,1,2);
wall(0,1,0,2,2);
    }
}
