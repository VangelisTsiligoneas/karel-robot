/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.nifty.textEditor;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.ScrollPanel;
import de.lessvoid.nifty.controls.scrollpanel.ScrollPanelControl;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.elements.tools.FontHelper;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 * @author Vaggos
 */
public class TextEditorControl implements TextEditor, Controller, TextEditorView {

    private static final String TEXT_NAME = "textLine";
    private static final int CURSOR_Y = 0;
    private static final Logger log = Logger.getLogger(TextEditorControl.class.getName());
    private Nifty nifty;
    private Screen screen;
    private Element parentElement;
    private Element scrollStaff;
    private Element textAreaElement;
    private TextRenderer firstLine;
    private int lineCount = 0;
    private Element fieldElement;
    private Element cursorElement;
    private TextEditorLogic textLogic;
    private int initialWidth;
    private int initialHeight;
    private int fieldWidth;
    private int fromClickCursorPos;
    private FocusHandler focusHandler;
    private boolean bound;
    private boolean hadSelection = false;

    public void bind(Nifty niftyParam, Screen screenParam, Element parentElement, Properties parameter, Attributes controlDefinitionAttributes) {
        this.parentElement = parentElement;
        nifty = niftyParam;
        screen = screenParam;
        fromClickCursorPos = -1;

        final String initText = controlDefinitionAttributes.get("text");

        if ((initText == null) || initText.isEmpty()) {
            textLogic = new TextEditorLogic(nifty.getClipboard(), this);
            textLogic.toFirstPosition();
        } else {
            textLogic = new TextEditorLogic(initText, nifty.getClipboard(), this);
        }
        //textAreaElement = parentElement.findElementByName("#textArea");
        //cursorElement = parentElement.findElementByName("#cursor");
        fieldElement = parentElement.findElementByName("#field");
        cursorElement = parentElement.findElementByName("#cursor");        
        scrollStaff = screen.findElementByName("#scrollStaff");
        
        //scrollStaff = this.parentElement;

        if (fieldElement == null) {
            System.out.println("fieldElement is null");
        }
        if (cursorElement == null) {
            System.out.println("cursorElement is null");
        }
        if (scrollStaff == null) {
            System.out.println("scrollStaff is null");
        }
        
        initialHeight = scrollStaff.getHeight();
        initialWidth = scrollStaff.getWidth();        
    }

    public void init(Properties p, Attributes attributes) {
        this.focusHandler = screen.getFocusHandler();

        this.fieldWidth = this.fieldElement.getWidth() - this.cursorElement.getWidth();
        createTextElement(textLogic.getText());
        firstLine = screen.findElementByName(TEXT_NAME + 0).getRenderer(TextRenderer.class);

        updateCursor();
    }

    public void onStartScreen() {
    }

    public void onFocus(boolean getFocus) {
        if (cursorElement != null) {

            if (getFocus) {
                cursorElement.startEffect(EffectEventId.onCustom);
            } else {
                cursorElement.stopEffect(EffectEventId.onCustom);
            }
            updateCursor();
        }
    }

    public boolean inputEvent(NiftyInputEvent inputEvent) {

        if (inputEvent == NiftyInputEvent.MoveCursorLeft) {
            textLogic.cursorLeft();
            updateCursor();
            return true;
        } else if (inputEvent == NiftyInputEvent.MoveCursorRight) {
            textLogic.cursorRight();
            updateCursor();
            return true;
        } else if (inputEvent == NiftyInputEvent.MoveCursorUp) {
            textLogic.cursorUp();
            updateCursor();
        } else if (inputEvent == NiftyInputEvent.MoveCursorDown) {
            textLogic.cursorDown();
            updateCursor();
        } else if (inputEvent == NiftyInputEvent.Delete) {
            textLogic.delete();
            updateCursor();
            return true;
        } else if (inputEvent == NiftyInputEvent.Backspace) {
            textLogic.backspace();
            updateCursor();
            return true;
        } else if (inputEvent == NiftyInputEvent.MoveCursorToLastPosition) {
            textLogic.toLastPositionOfRow();
            updateCursor();
            return true;
        } else if (inputEvent == NiftyInputEvent.MoveCursorToFirstPosition) {
            textLogic.toFirstPositionOfRow();
            updateCursor();
            return true;
        } else if (inputEvent == NiftyInputEvent.SelectionStart) {
            textLogic.startSelecting();
            //updateCursor();
            return true;
        } else if (inputEvent == NiftyInputEvent.SelectionEnd) {
            textLogic.endSelecting();
            updateCursor();
            return true;
        } else if (inputEvent == NiftyInputEvent.Cut) {
            textLogic.cut();
            updateCursor();
            return true;
        } else if (inputEvent == NiftyInputEvent.Copy) {
            textLogic.copy();
            updateCursor();
            return true;
        } else if (inputEvent == NiftyInputEvent.Paste) {
            textLogic.paste();
            updateCursor();
            return true;
        } else if (inputEvent == NiftyInputEvent.SelectAll) {

            textLogic.selectAll();
            updateCursor();
        } else if (inputEvent == NiftyInputEvent.Character) {
            textLogic.insert(inputEvent.getCharacter());
            updateCursor();
            return true;
        } else if (inputEvent == NiftyInputEvent.NextInputElement) {
            textLogic.insert("    ");
            updateCursor();
            return true;
        } else if (inputEvent == NiftyInputEvent.PrevInputElement) {
            return true;
        } else if (inputEvent == NiftyInputEvent.SubmitText) {
            insertNewRow();
            updateCursor();
            return true;
        }
        return false;
    }

    private void insertNewRow() {
        textLogic.insert('\n');
        createTextElement("");
    }

    private void createTextElement(final String text) {
        new TextBuilder("textLine" + lineCount) {
            {
                text(text);
                align(ElementBuilder.Align.Left);
                wrap(false);
                textHAlignLeft();
                font("Interface/Fonts/Monospaced13.fnt");
                color("#000f");
                selectionColor("#00ff");
            }
        }.build(nifty, screen, fieldElement);
        lineCount++;
    }

    public void onClick(int mouseX, int mouseY) {
        String[] brokenText = getBrokenText();
        int row = (mouseY - fieldElement.getY()) / firstLine.getFont().getHeight();
        int x = FontHelper.getCharacterIndexFromPixelPosition(firstLine.getFont(), brokenText[row >= brokenText.length ? brokenText.length - 1 : row], (mouseX - fieldElement.getX()), 1.0f);
        int cursorPosition = calculateCursorPositionFromMouse(x, row);
        //if(textLogic.isSelecting())
        textLogic.setCursorPosition(cursorPosition);
        fromClickCursorPos = cursorPosition;
        updateCursor();
    }

    public void onClickMouseMove(final int mouseX, final int mouseY) {
        textLogic.startSelectingFrom(fromClickCursorPos);
        String[] brokenText = getBrokenText();
        int row = (mouseY - fieldElement.getY()) / firstLine.getFont().getHeight();
        int x = FontHelper.getCharacterIndexFromPixelPosition(firstLine.getFont(), brokenText[row >= brokenText.length ? brokenText.length - 1 : row], (mouseX - fieldElement.getX()), 1.0f);
        int cursorPosition = calculateCursorPositionFromMouse(x, row);
        textLogic.setCursorPosition(cursorPosition);
        textLogic.endSelecting();
        updateCursor();
    }

    private void updateCursor() {
        String[] brokenText = getBrokenText();
        Iterator<Element> it = fieldElement.getElements().iterator();
        int j = 0;
        while (it.hasNext() && j < brokenText.length) {
            it.next().getRenderer(TextRenderer.class).setText(brokenText[j]);
            j++;
        }
        while (it.hasNext()) {
            it.next().markForRemoval();
        }
        for (int i = j; i < brokenText.length; i++) {
            createTextElement(brokenText[i]);
        }

        int lineHeight = firstLine.getFont().getHeight() * j + 5;

        scrollStaff.setHeight(lineHeight > initialHeight ? lineHeight : initialHeight);

        int maxWidth = calculateWidth();

        scrollStaff.setWidth(maxWidth > initialWidth ? maxWidth + 5 : initialWidth + 5);

        setSelection();
        int row = calculateRowFromCurrentPosition();
        int currentPosition = textLogic.getCursorPosition();
        int textWidth = 0;
        int startIndex = textLogic.getText().lastIndexOf('\n', currentPosition);
        if (currentPosition == startIndex) {
            startIndex = textLogic.getText().lastIndexOf('\n', currentPosition - 1);
        }
        if (startIndex <= -1) {
            startIndex = 0;
        }
        if (currentPosition > 0) {
            textWidth = firstLine.getFont().getWidth(getAbsoluteSubstring(startIndex, currentPosition) + " ");
        }
        SizeValue constraintX = new SizeValue(textWidth + "px");
        SizeValue constraintY = new SizeValue((firstLine.getFont().getHeight() * row) + CURSOR_Y + "px");
        cursorElement.setConstraintX(constraintX);
        cursorElement.setConstraintY(constraintY);
        cursorElement.startEffect(EffectEventId.onActive, null);

ScrollPanel scrollPanel = screen.findNiftyControl("#scroll", ScrollPanel.class);
/*scrollPanel.setPageSizeX(textWidth);
 * scrollPanel.setPageSizeY(lineHeight);
 * scrollPanel.setHorizontalPos(textWidth);
 * scrollPanel.setVerticalPos(lineHeight);*/
        screen.layoutLayers();
    }

    private int calculateXIndexFromIndex(String[] brokenText, int index, int row) {
        return textLogic.calculateXIndexFromIndex(brokenText, index, row);
    }

    private void setSelection() {
        if (textLogic.hasNotSelection() && hadSelection) {
            Iterator<Element> it = fieldElement.getElements().iterator();
            while (it.hasNext()) {
                it.next().getRenderer(TextRenderer.class).setSelection(0, 0);
            }
            hadSelection = false;
        } else if (textLogic.hasSelection()) {
            int startIndex = textLogic.getSelectionStart();
            //System.out.println("startIndex = " + startIndex);
            int finishIndex = textLogic.getSelectionEnd();
            if (finishIndex < startIndex) {
                startIndex = textLogic.getSelectionEnd();
                finishIndex = textLogic.getSelectionStart();
            }
            int firstRow = 0;
            int lastRow = 0;
            int startX = 0;
            int finishX = 0;
            Iterator<Element> it = fieldElement.getElements().iterator();
            int countLines = 0;
            int countLetters = 0;
            boolean passedFirst = false;
            while (it.hasNext()) {
                int length = it.next().getRenderer(TextRenderer.class).getOriginalText().length();
                countLetters += length;

                if (countLetters + countLines >= startIndex && !passedFirst) {
                    firstRow = countLines;
                    startX = startIndex - (countLetters - length) - firstRow;
                    passedFirst = true;
                }
                if (countLetters + countLines >= finishIndex) {
                    lastRow = countLines;
                    finishX = finishIndex - (countLetters - length) - lastRow;
                    break;
                }
                countLines++;
            }

            ArrayList<Element> lines = new ArrayList<Element>(fieldElement.getElements());
            if (firstRow == lastRow) {
                lines.get(firstRow).getRenderer(TextRenderer.class).setSelection(startX, finishX);
            } else {
                TextRenderer textRenderer;
                textRenderer = lines.get(firstRow).getRenderer(TextRenderer.class);
                textRenderer.setSelection(startX, textRenderer.getOriginalText().length());
                for (int i = firstRow + 1; i < lastRow; i++) {
                    textRenderer = lines.get(i).getRenderer(TextRenderer.class);
                    textRenderer.setSelection(0, textRenderer.getOriginalText().length());
                }
                textRenderer = lines.get(lastRow).getRenderer(TextRenderer.class);
                textRenderer.setSelection(0, finishX);
            }
            hadSelection = true;
        }
    }

    /**
     * @param Xindex : the index int the given row starting from zero
     */
    private int calculateCursorPositionFromMouse(int xIndex, int row) {
        int count = 0;
        String[] brokenText = getBrokenText();
        int limit;
        if (row >= brokenText.length) {
            limit = brokenText.length - 1;
        } else {
            limit = row;
        }
        for (int i = 0; i < limit; i++) {
            count += brokenText[i].length() + 1;
        }
        return count + xIndex;
    }

    private int calculateRowFromCurrentPosition() {
        return textLogic.calculateRowFromCurrentPosition();
    }

    private String getAbsoluteSubstring(int fromIndex, int toIndex) {
        String text = textLogic.getText();
        int start = fromIndex;
        int finish = toIndex;
        if (fromIndex > toIndex) {
            start = toIndex;
            finish = fromIndex;
        }
        return text.substring(start, finish);
    }

    private int calculateRowFromGivenPosition(int index) {
        return textLogic.calculateRowFromGivenPosition(index);

    }

    private String[] getBrokenText() {
        return textLogic.getBrokenText();
    }

    private int calculateWidth() {
        int max = 0;
        String[] brokenText = getBrokenText();
        for (int i = 0; i < brokenText.length; i++) {
            int lineLength = brokenText[i].length();
            if (lineLength > max) {
                max = firstLine.getFont().getWidth(brokenText[i]);
            }
        }
        return max;
    }

    public String getText() {
        return textLogic.getText();
    }

    public void setCursorPosition(int position) {
        textLogic.setCursorPosition(position);
        updateCursor();
    }

    public void setText(String text) {
        textLogic.initWithText(text);
        //textLogic.initWithText(nifty.specialValuesReplace(text));
        updateCursor();
    }

    public Element getElement() {
        if (parentElement == null) {
            log.warning("Requested element from controller before binding was performed.");
        }
        return parentElement;
    }

    public String getId() {
        final Element element = getElement();
        return element != null ? element.getId() : null;
    }

    public void setId(String id) {
        final Element element = getElement();
        if (element != null) {
            element.setId(id);
        }
    }

    public int getWidth() {
        final Element element = getElement();
        return element != null ? element.getWidth() : 0;
    }

    public void setWidth(SizeValue width) {
        final Element element = getElement();
        if (element != null) {
            element.setConstraintWidth(width);
        }
    }

    public int getHeight() {
        final Element element = getElement();
        return element != null ? element.getHeight() : 0;
    }

    public void setHeight(SizeValue height) {

        final Element element = getElement();
        if (element != null) {
            element.setConstraintHeight(height);
        }
    }

    @Override
    public String getStyle() {
        final Element element = getElement();
        return element != null ? element.getStyle() : null;
    }

    @Override
    public void setStyle(final String style) {
        final Element element = getElement();
        if (element != null) {
            element.setStyle(element.getNifty().specialValuesReplace(style));
        }
    }

    public void enable() {
        setEnabled(true);
    }

    public void disable() {
        setEnabled(false);
    }

    public void setEnabled(final boolean enabled) {
        final Element element = getElement();
        if (element != null) {
            if (enabled) {
                element.enable();
            } else {
                element.disable();
            }
        }
    }

    public boolean isEnabled() {
        final Element element = getElement();
        return element != null && element.isEnabled();
    }

    public void setFocus() {
        final Element element = getElement();
        if (element != null) {
            element.setFocus();
        }
    }

    public void setFocusable(final boolean focusable) {
        final Element element = getElement();
        if (element != null) {
            element.setFocusable(focusable);
        }
    }

    public boolean hasFocus() {
        final Element element = getElement();
        if (element == null) {
            return false;
        }
        return element == element.getFocusHandler().getKeyboardFocusElement();
    }

    public void layoutCallback() {
        this.fieldWidth = this.fieldElement.getWidth() - this.cursorElement.getWidth();
    }

    public boolean isBound() {
        return bound;
    }

    public void textChangeEvent(String newText) {

        if (nifty == null) {
            log.warning("Binding not done yet. Can't publish events without reference to Nifty.");
        } else {

            if (parentElement != null) {
                String id = getElement().getId();
                if (id != null) {
                    nifty.publishEvent(id, new TextEditorChangedEvent(this, newText));
                }
            }
        }
    }
}
