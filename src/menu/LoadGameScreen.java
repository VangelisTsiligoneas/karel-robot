/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.PopupBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.Properties;
import loginsystem.User;
import loginsystem.UserList;
import mygame.Main;

/**
 *
 * @author Vaggos
 */
public class LoadGameScreen extends AbstractAppState implements ScreenController, KeyInputHandler {

    private UserList userList;
    private Nifty nifty;
    private Screen screen;
    private Main app;
    private NiftyJmeDisplay niftyDisplay;
    private ViewPort guiViewPort;
    private static final int NUMBER_OF_SLOTS = 4;
    private int selectedSlot;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Main) app;
        userList = UserList.loadUsers();
        niftyDisplay = this.app.getNiftyDisplay();
        guiViewPort = this.app.getGuiViewPort();
        guiViewPort.addProcessor(niftyDisplay);
        this.app.getFlyByCamera().setDragToRotate(true);
        nifty = niftyDisplay.getNifty();
        setScreen();
    }

    public void setScreen() {
        nifty.fromXml("Interface/MenuGui.xml", "start", this);
    }

    public void refresh() {
        app.getStateManager().detach(this);
        app.getStateManager().attach(this);
    }

    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
        this.screen.addKeyboardInputHandler(new LoadScreenMapping(), this);
    }

    public void selectSlot(String slotString, String click) {
        int slot = Integer.parseInt(slotString);
        int size = userList.getSize();

        if (slot <= size) {
            if (click.equals("primary")) {
                loadUser(slot);
            } else {
                selectSlot(slot);
                Element popup = nifty.createPopup("deletePopup");
                nifty.showPopup(screen, popup.getId(), null);
            }
        } else {
            if (click.equals("primary")) {
                createNewUser();
            }
        }
    }

    private void selectSlot(int slot) {
        selectedSlot = slot;
    }

    public void deleteUser() {

        userList.deleteUser(userList.getUser(selectedSlot).getUserName());
        System.out.print("DELETE");
        refresh();
    }

    public void loadUser(int slot) {
        User user = userList.getUser(slot);
        app.setLoggedInUser(user);
        app.getStateManager().detach(this);
        app.getStateManager().attach(new MainMenu());
    }

    public void saveNewUser() {
        User newUser = userList.createUser(screen.findNiftyControl("textfield", TextField.class).getText());
        if (newUser != null) {
            nifty.closePopup("registerPopup");
            newUser.saveUser();
            System.out.print("user saved");
        } else {
            new TextBuilder() {
                {
                    text("invalid userName. Please try another one!");
                    font("Interface/Fonts/ComicSansMSplain24.fnt");
                    style("nifty-label");
                    color("#f00f");
                }
            }.build(nifty, screen, screen.findElementByName("mainWindow"));
            return;
        }
        refresh();
    }

    public void onStartScreen() {
        int count = 1;

int maxLevel = app.getLevelList().findMaxLevel();

        for (final User user : userList.getUsers()) {
            int currentLevel = user.getCurrentLevel();
            
            
            int num = -1;
            if (currentLevel < maxLevel) {
                num = currentLevel;
            } else {
                num = maxLevel;
            }
            final int levelNum = num;

            Element slot = screen.findElementByName("slot_" + count);
            final int height = slot.getHeight();
            final int width = height * 16 / 9;
            new ImageBuilder() {
                {
                    backgroundColor(Color.WHITE);
                    filename("screenshots/Level_" + levelNum + ".png");
                    height(pixels(height));
                    width(pixels(width));
                    align(ElementBuilder.Align.Left);
                }
            }.build(nifty, screen, slot);

            Element userInfo = screen.findElementByName("userInfo_" + count);
            new TextBuilder() {
                {
                    backgroundColor("#ff000055");
                    text(user.getUserName());
                    align(ElementBuilder.Align.Center);
                    valign(ElementBuilder.VAlign.Center);
                    width(percentage(100));
                    height(percentage(50));
                    font("Interface/Fonts/ComicSansMSplain24.fnt");
                    style("nifty-label");
                }
            }.build(nifty, screen, userInfo);

            new TextBuilder() {
                {
                    
                    backgroundColor("#0f0a");
                    text("Level " + String.valueOf(levelNum));
                    align(ElementBuilder.Align.Center);
                    valign(ElementBuilder.VAlign.Center);
                    width(percentage(100));
                    height(percentage(50));
                    font("Interface/Fonts/ComicSansMSplain24.fnt");
                    style("nifty-label");
                }
            }.build(nifty, screen, userInfo);

            count++;
        }
        while (count <= NUMBER_OF_SLOTS) {
            Element slot = screen.findElementByName("slot_" + count);
            final int height = slot.getHeight();
            final int width = height * 16 / 9;
            new ImageBuilder() {
                {
                    filename("Interface/no_image.png");
                    height(pixels(height));
                    width(pixels(width));
                    align(ElementBuilder.Align.Left);
                }
            }.build(nifty, screen, slot);

            Element userInfo = screen.findElementByName("userInfo_" + count);
            new TextBuilder() {
                {
                    backgroundColor("#00f9");
                    text("EMPTY SLOT");
                    align(ElementBuilder.Align.Center);
                    valign(ElementBuilder.VAlign.Center);
                    width(percentage(100));
                    height(percentage(100));
                    font("Interface/Fonts/ComicSansMSBold27.fnt");
                    style("nifty-label");
                }
            }.build(nifty, screen, userInfo);

            count++;
        }
        createRegisterPopup();
        createDeletePopup();
    }

    public void createRegisterPopup() {
        new PopupBuilder("registerPopup") {
            {
                childLayout(ChildLayoutType.Center);
                backgroundColor("#000a");

                panel(new PanelBuilder("mainWindow") {
                    {
                        backgroundColor("#db1717ff");
                        width(percentage(60));
                        height(percentage(55));
                        childLayout(ChildLayoutType.Vertical);

                        control(new ControlBuilder("button") {
                            {
                                align(Align.Right);
                                width("28px");
                                height("28px");
                                parameter("label", "X");
                                interactOnClick("refresh()");
                            }
                        });
                        text(new TextBuilder() {
                            {
                                text("WRITE YOUR USERNAME:");;;
                                color(Color.BLACK);
                                font("Interface/Fonts/ComicSansMSBold27.fnt");
                                style("nifty-label");
                                height(percentage(10));
                                marginBottom("10px");
                                align(Align.Center);
                            }
                        });
                        panel(new PanelBuilder() {
                            {
                                backgroundColor("#db1717ff");
                                width(percentage(70));
                                height(percentage(60));
                                align(Align.Center);
                                valign(VAlign.Bottom);
                                childLayout(ChildLayoutType.Horizontal);
                                text(new TextBuilder("label") {
                                    {
                                        text("Username: ");
                                        font("Interface/Fonts/ComicSansMSplain24.fnt");
                                        color(Color.BLACK);
                                        style("nifty-label");
                                        height(percentage(10));
                                        valign(VAlign.Center);

                                    }
                                });
                                control(new TextFieldBuilder("textfield") {
                                    {
                                        maxLength(10);
                                        width(percentage(60));
                                        valign(VAlign.Center);
                                        //focusable(true);


                                    }
                                });
                                control(new ControlBuilder("button") {
                                    {
                                        valign(VAlign.Center);
                                        parameter("label", "DONE");
                                        interactOnClick("saveNewUser()");
                                    }
                                });
                            }
                        });

                    }
                });

            }
        }.registerPopup(nifty);
    }

    public void createDeletePopup() {
        new PopupBuilder("deletePopup") {
            {
                childLayout(ElementBuilder.ChildLayoutType.Center);
                backgroundColor("#000a");

                panel(new PanelBuilder("deleteWindow") {
                    {
                        backgroundColor("#b13f3fff");
                        width(percentage(60));
                        height(percentage(55));
                        childLayout(ElementBuilder.ChildLayoutType.Vertical);
                        text(new TextBuilder() {
                            {
                                text("ARE YOU SURE YOU WANT TO DELETE THIS USER?");
                                color(Color.BLACK);
                                font("Interface/Fonts/ComicSansMSBold27.fnt");
                                style("nifty-label");
                                height(percentage(10));
                                marginBottom("10px");
                                valign(ElementBuilder.VAlign.Top);
                                align(Align.Center);
                            }
                        });
                        panel(new PanelBuilder() {
                            {
                                backgroundColor("#b13f3fff");
                                width(percentage(90));
                                height(percentage(80));
                                align(ElementBuilder.Align.Center);
                                valign(ElementBuilder.VAlign.Bottom);
                                childLayout(ElementBuilder.ChildLayoutType.AbsoluteInside);

                                control(new ControlBuilder("yesButton", "button") {
                                    {
                                        valign(ElementBuilder.VAlign.Center);
                                        width("200px");
                                        height("46px");
                                        x("0");
                                        y(percentage(50));
                                        marginLeft("25px");
                                        //align(Align.Center);
                                        parameter("label", "YES");
                                        interactOnClick("deleteUser()");

                                    }
                                });

                                control(new ControlBuilder("noButton", "button") {
                                    {
                                        valign(ElementBuilder.VAlign.Center);
                                        width("200px");
                                        height("46px");
                                        x(percentage(100));
                                        y(percentage(50));
                                        marginRight("25px");
                                        //align(Align.Center);
                                        parameter("label", "NO");
                                        interactOnClick("back()");
                                    }
                                });
                            }
                        });
                    }
                });

            }
        }.registerPopup(nifty);
    }

    public void back() {
        refresh();
    }

    public void exit() {
        app.stop();
    }

    public void onEndScreen() {
    }

    @Override
    public void cleanup() {
        super.cleanup();
        app.getGuiNode().detachAllChildren();
        guiViewPort.removeProcessor(niftyDisplay);
    }

    private void createNewUser() {
        Element popup = nifty.createPopup("registerPopup");
        nifty.showPopup(screen, popup.getId(), null);
    }

    public boolean keyEvent(NiftyInputEvent inputEvent) {
        if (inputEvent == NiftyInputEvent.SubmitText) {

            return true;
        } else if (inputEvent == NiftyInputEvent.Escape) {
            //System.out.println(inputEvent);
            refresh();
            return true;
        }
        return false;
    }
}
