/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.scene.Mesh;
import com.jme3.scene.Mesh.Mode;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
/**
 *
 * @author Vaggos
 */
public class WireGrid extends Mesh{
    /**
     * Creates a grid debug shape.
     * @param xLines
     * @param yLines
     * @param lineDist 
     */
    public WireGrid(int xLines, int yLines, int zLines, float lineDist){
        xLines -= 2;
        yLines -= 2;
        zLines -= 2;
        int lineCount = xLines + yLines + zLines + 6;

        FloatBuffer fpb = BufferUtils.createFloatBuffer(6 * lineCount * (zLines + 2) * 2);
        ShortBuffer sib = BufferUtils.createShortBuffer(2 * lineCount * (zLines + 2) * 2);

        float xLineLen = (yLines + 1) * lineDist;
        float yLineLen = (xLines + 1) * lineDist;
        float zLineLen = (xLines + 1) * lineDist;
        int curIndex = 0;
        
        for(int j = 0; j < zLines + 2; j++){
            float z = (j) * lineDist;
        // add lines along X
        for (int i = 0; i < xLines + 2; i++){
            float y = (i) * lineDist;

            // positions
            fpb.put(0)       .put(z).put(y);
            fpb.put(xLineLen).put(z).put(y);

            // indices
            sib.put( (short) (curIndex++) );
            sib.put( (short) (curIndex++) );
        }

        // add lines along Y
        for (int i = 0; i < yLines + 2; i++){
            float x = (i) * lineDist;

            // positions
            fpb.put(x).put(z).put(0);
            fpb.put(x).put(z).put(yLineLen);

            // indices
            sib.put( (short) (curIndex++) );
            sib.put( (short) (curIndex++) );
        }
        }
        
        for (int j = 0; j < yLines + 2; j++){
            float y = (j) * lineDist;
            for (int i = 0; i < zLines + 2; i++){
                float z = (i) * lineDist;
                 
                //positions
                fpb.put(0).put(z).put(y);
                fpb.put(xLineLen).put(z).put(y);
                
                sib.put( (short) (curIndex++) );
                sib.put( (short) (curIndex++) );
            }
            
            for (int i = 0; i < xLines + 2; i++){
                float x = (i) * lineDist;
                
                fpb.put(x).put(0).put(y);
                fpb.put(x).put(zLineLen).put(y);
                
                sib.put( (short) (curIndex++) );
                sib.put( (short) (curIndex++) );
            }
        }
        /*
        for (int i = 0; i < zLines + 2; i++){
            float x = (i) * lineDist;

            // positions
            fpb.put(0).put(x).put(0);
            fpb.put(zLineLen).put(x).put(0);

            // indices
            sib.put( (short) (curIndex++) );
            sib.put( (short) (curIndex++) );
        }
*/
        fpb.flip();
        sib.flip();

        setBuffer(Type.Position, 3, fpb);
        setBuffer(Type.Index, 2, sib);
        
        setMode(Mode.Lines);

        updateBound();
        updateCounts();
    }
    
}