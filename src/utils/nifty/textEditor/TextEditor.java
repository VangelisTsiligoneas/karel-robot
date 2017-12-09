/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.nifty.textEditor;

import de.lessvoid.nifty.controls.NiftyControl;

/**
 *
 * @author Vaggos
 */
public interface TextEditor extends NiftyControl {
   
  int UNLIMITED_LENGTH = -1;

 
 
  String getText();

  void setCursorPosition(int position);

 
  void setText( String text);
    
}
