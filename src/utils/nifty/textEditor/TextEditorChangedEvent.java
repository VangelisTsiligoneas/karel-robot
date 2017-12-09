/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.nifty.textEditor;

import de.lessvoid.nifty.NiftyEvent;



/**
 *
 * @author Vaggos
 */
public class TextEditorChangedEvent implements NiftyEvent{
     private final TextEditor textField;
  
  private final String currentText;

  public TextEditorChangedEvent( final TextEditor textFieldControl, final String currentText) {
    this.textField = textFieldControl;
    this.currentText = currentText;
  }

  
  public TextEditor getTextFieldControl() {
    return textField;
  }

  
  public String getText() {
    return currentText;
  }
}
