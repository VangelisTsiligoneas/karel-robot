/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Vaggos
 */
import utils.*;

public class Execute{ 
    

    public void main(World world){
        Robot karel = new Robot(1,1,0,"RIGHT");
		karel.land(world);
        karel.move();
    }
}

