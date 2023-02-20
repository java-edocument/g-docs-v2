package org.rmj.edocumentsfx.views;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ShowMessageFX;

public class Edocumentsfx extends Application {
    
    public static GRider poGRider;
    public final static String pxeMainFormTitle = "e-Documents Fx V1";
    public final static String pxeMainForm = "MainInterface.fxml";
    public final static String pxeStageIcon = "org/rmj/edocumentsfx/images/GFx.png";
    public final static String pxePackage = "org/rmj/edocumentsfx/";
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(pxeMainForm));
        
        //get the controller of the main interface
        MainInterfaceController loControl = new MainInterfaceController();
        
        //set the GRider Application Driver to the controller
        loControl.setGRider(poGRider);
        
        //the controller class to the main interface
        fxmlLoader.setController(loControl);
        
        //load the main interface
        Parent parent = fxmlLoader.load();
        //set the main interface as the scene
        Scene scene = new Scene(parent);
        
        //get the screen size
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image(pxeStageIcon));
        stage.setTitle(pxeMainFormTitle);
        
        // set stage as maximized but not full screen
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.centerOnScreen();
        stage.show();
        
        //making the stage draggable
        loControl.acTiltleBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        loControl.acTiltleBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
        
        loControl.rootPane.setOnKeyReleased(this::handleKeyCode);
        loControl.loadTreeView();
        
    }
    
    public void setGRider(GRider foGRider){
        this.poGRider = foGRider;
    }
    
    public void handleKeyCode(KeyEvent event) {
        switch(event.getCode()){
            case F1:
            case F2:
            case F3:
            case F4:
            case F5:
            case F6:
            case F7:
            case F8:
            case F9:
            case F10:
            case F11:
            case F12:
                break;
            case ESCAPE:
                if(ShowMessageFX.OkayCancel("Do you want to exit?", pxeMainFormTitle, "All changes made will not save!")== true){
                   System.exit(0);
                   break;
                }
        }
        
    }
    
    
}
