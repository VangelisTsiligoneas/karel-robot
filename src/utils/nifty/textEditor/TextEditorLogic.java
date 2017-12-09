/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.nifty.textEditor;

import de.lessvoid.nifty.Clipboard;

/**
 *
 * @author Vaggos
 */
public class TextEditorLogic {

    private StringBuffer text;
    private int cursorPosition=0;
    private int selectionStart = -1;
    private int selectionEnd = -1;
    private boolean selecting=false;
    
    //private int selectionStartIndex;
    private final Clipboard clipboard;
    private final TextEditorView view;

    public TextEditorLogic(final Clipboard newClipboard, final TextEditorView textFieldView) {
        view = textFieldView;
        clipboard = newClipboard;


        text = new StringBuffer(100);
    }

    public TextEditorLogic(final String newText, final Clipboard newClipboard, final TextEditorView textFieldView) {

        this.view = textFieldView;
        initText(newText);
        clipboard = newClipboard;
    }

    public void setText(final CharSequence newText) {
        text.setLength(0);
        if (newText != null) {
            text.append(newText);
        }
        cursorPosition = 0;
        resetSelection();
    }
    
    private void initText(final String newText) {
        this.text = new StringBuffer();
        if (newText != null) {
            this.text.append(newText);
        }
        this.cursorPosition = 0;
        resetSelection();        
    }
  
    public void resetSelection() {
        if (hasSelection()&& !selecting) {
            selectionStart = -1;
            selectionEnd = -1;
        }
    }
    
    /**
     * Backspace.
     */
    public void backspace() {
        String old = text.toString();
        if (hasSelection()) {
            deleteSelectedText();
        } else {
            if (cursorPosition > 0) {
// delete character left of cursor
                text.delete(cursorPosition - 1, cursorPosition);
                cursorPosition--;
            }
        }
        notifyTextChange(old);
    }

    /**
     * Delete the character at the cursor position.
     */
    public void delete() {
        String old = text.toString();
        if (hasSelection()) {
            deleteSelectedText();
        } else {
            text.delete(cursorPosition, cursorPosition + 1);
        }
        notifyTextChange(old);
    }
    
    
    
    /**
     * Checks if we currently have a selection.
     *
     * @return true or false
     */
    public boolean hasSelection() {
        return (selectionStart != -1) && (selectionEnd != -1);
    }
    
    public boolean hasNotSelection(){
        return (selectionStart==-1) && (selectionEnd ==-1);
    }
    
    private void deleteSelectedText() {
        if (hasSelection()&& selectionStart!=selectionEnd) {
            int start = selectionStart;
            int finish = selectionEnd;
            if(start>finish){
                start = selectionEnd;
                finish = selectionStart;
            }
            text.delete(start, finish);
            cursorPosition = start;
            resetSelection();
        }

    }

    /**
     * Copy currently selected text to clipboard.
     */
    public void copy() {
        final CharSequence selectedText = getSelectedText();
        if (selectedText != null) {
            clipboard.put(selectedText.toString());
        }
    }
    
     public void cut() {
        final CharSequence selectedText = getSelectedText();
        if (selectedText == null) {
            return;
        }

        clipboard.put(selectedText.toString());
        delete();
    }
      /**
     * Put data from clipboard into text field.
     */
    public void paste() {        
        final String clipboardText = clipboard.get();
        if (clipboardText != null) {
            insert(clipboardText);
        }
        selectionStart=-1;
        selectionEnd=-1;
    }
     
     /**
     * Move cursor left.
     */
    public void cursorLeft() {
        moveCursor(-1);

    }
    
    private void moveCursor(int i){
        setCursorPosition(cursorPosition+i);
    }

    /**
     * Move cursor right.
     */
    public void cursorRight() {
        moveCursor(1);
    }
    
    /**
     * move cursor up
     */
    public void cursorUp(){
        moveCursorVertical(-1);
    }
    
    private void moveCursorVertical(int steps){
        resetSelection();
        
        String[] brokenText = getBrokenText();
        int latestRow = brokenText.length-1;
        int currentRow = calculateRowFromCurrentPosition();
        int currentX = calculateXIndexFromIndex(brokenText, this.cursorPosition, currentRow);
        
        int targetRow = currentRow + steps;
        if(targetRow<0){
            targetRow = 0;
        }
        else if(targetRow>latestRow){
            targetRow = latestRow;
        }
        //int currentLength = brokenText[currentRow].length();
        int targetLength = brokenText[targetRow].length();
        
        int x = currentX;
        if(currentX > targetLength){
            x = targetLength;
        }
        cursorPosition = calculateCursorPositionFromXY(x, targetRow, brokenText);
    }
    
    /**
     * move cursor down
     */
    public void cursorDown(){
        moveCursorVertical(1);
    }
    
    private int calculateCursorPositionFromXY(int xIndex, int row, String[] brokenText ){
        int count = 0;
        //String[] brokenText = getBrokenText();
        int limit;
        if(row>=brokenText.length){limit = brokenText.length-1;}
        else{limit = row;}
        for(int i=0;i<limit;i++){
            count += brokenText[i].length() + 1;           
        }
        return count+xIndex;
    }

    public String getSelectedText() {
        
        if(selectionStart > selectionEnd){
            return text.substring(selectionEnd, selectionStart);
        }
        else{
            return text.substring(selectionStart, selectionEnd);
        }
        
    }
    
    public int getSelectionLength() {
       return Math.abs(selectionEnd - selectionStart) ;
    }
    
    public void setTextAndNotify(final CharSequence newText) {
        setText(newText);
        if ((newText != null) && (newText.length() > 0)) {
            view.textChangeEvent(newText.toString());
        }
    }

    public void insert(final char c) {
        endSelecting();
        deleteSelectedText();
        resetSelection();
        String newText = text.insert(cursorPosition, c).toString();
        cursorPosition++;
        
        notifyTextChange(newText);
    }

    /**
     * Insert a sequence of characters into the current text.
     *
     * @param chars the character sequence to insert
     */
    public void insert(final CharSequence chars) {
        deleteSelectedText();
        text.insert(cursorPosition, chars);
        setCursorPosition(cursorPosition + chars.length());
    }

    /**
     * Expand the active selection over the entire text field.
     */
    public void selectAll() {        
        selectionStart = 0;
        selectionEnd = text.length();
        cursorPosition = text.length();
    }
    
    /**
     * Set new cursor position.
     *
     * @param newIndex index.
     *
     */
    public void setCursorPosition(final int newIndex) {
        //resetSelection();
        if (newIndex < 0) {
            cursorPosition = 0;
        } else if (newIndex > text.length()) {
            cursorPosition = text.length();
        } else {
            cursorPosition = newIndex;
        }
        if(selecting)
        selectionEnd = cursorPosition;
        resetSelection();
    }

    /**
     * Start a selecting operation at the current cursor position. Be sure to
     * set the location of the cursor to the proper stop before calling this
     * function.
     */
    public void startSelecting() {
        if(!selecting){
            selectionStart = cursorPosition;
        selecting = true;
        }
        
    }
    
    public void startSelectingFrom(int fromIndex){
        selectionStart = fromIndex;
        selecting=true;
    }
    
    public void endSelecting(){
        if(selectionStart>-1)
        selectionEnd = cursorPosition;
        selecting = false;
    }

    /**
     * Move the location of the cursor to the first position in the text.
     */
    public void toFirstPosition() {
        resetSelection();
        cursorPosition = 0;        
    }
    
    public void toFirstPositionOfRow(){
        resetSelection();
        String[] brokenText = getBrokenText();
        int currentRow = calculateRowFromCurrentPosition();
        cursorPosition = calculateCursorPositionFromXY(0, currentRow, brokenText);
    }
    
    public void toLastPosition(){
        resetSelection();
        cursorPosition = text.length();        
    }
    
    public void toLastPositionOfRow(){
        resetSelection();
        
        String[] brokenText = getBrokenText();
        int currentRow = calculateRowFromCurrentPosition();
        int length = brokenText[currentRow].length();
        cursorPosition = calculateCursorPositionFromXY(length, currentRow, brokenText);
    }

    
    
    public void initWithText(final String newText) {
        changeText(newText);

        if (newText != null && newText.length() > 0) {
            view.textChangeEvent(newText);
        }
    }

    private void changeText(final String newText) {
        this.text = new StringBuffer();
        if (newText != null) {
            this.text.append(newText);
        }
// only reset the cursorposition if the old one is not valid anymore
        if (this.cursorPosition > this.text.length()) {
            this.cursorPosition = 0;
        }
        resetSelection();
        
    }

    

    private void notifyTextChange(final String old) {
        String current = text.toString();
        if (old.equals(current)) {
            return;
        }
        view.textChangeEvent(current);
    }
    
    
    public int getCursorPosition() {
        return cursorPosition;
    }

    public int getSelectionEnd() {
        return selectionEnd;
    }

   
    public int getSelectionStart() {
        return selectionStart;
    }
    public String getText() {
        return text.toString();
    }

    public boolean isSelecting() {
        return selecting;
    }
    
    public String[] getBrokenText(){
        return getText().split("\n", -1);        
    }
    
    public int calculateRowFromCurrentPosition(){
        int currentPosition = getCursorPosition();
        return calculateRowFromGivenPosition(currentPosition);
    }
    
    public int calculateRowFromGivenPosition(int index){        
        if(index<=-1){
            index = 0;
        }
        else if(index>text.length()){
            index=text.length();
        }        
        return text.substring(0, index).split("\n", -1).length-1;
    }
    
    public int calculateXIndexFromIndex(String[] brokenText, int index, int row){
        int count=0;
        for(int i=0;i<row;i++){
            count+=brokenText[i].length()+1;            
        }
        return index-count;
    }
}
