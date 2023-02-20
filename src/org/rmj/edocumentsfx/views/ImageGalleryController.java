
package org.rmj.edocumentsfx.views;

import com.jfoenix.controls.JFXButton;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.edocumentsfx.utilities.Utilities;

public class ImageGalleryController implements Initializable {
    @FXML private AnchorPane acWorkStation;
    @FXML private BorderPane borderPane;
    @FXML private Button btnTerminate;
    @FXML private AnchorPane centerPane;
    @FXML private ScrollPane scrollpane;
    @FXML private Label lblImageGallery;
    @FXML private Label lblImagePreview;
    @FXML private JFXButton btnImageNext;
    @FXML private JFXButton btnImagePrevious;
    
    
    private ArrayList<String> psImages = new ArrayList<>();
    private List<Image> images = new ArrayList<>();
    private int pos = 0;
    private double width = 0.0;
    private double height = 0.0;
    private String pxeMoudelName = "Image Gallery";

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        btnTerminate.setOnAction(this::cmdButton_Click);
        
        loadImages();
        ImageView mainImageView = new ImageView();
        mainImageView.setImage(images.get(0));
        mainImageView.setFitWidth(1100 - (1100 / 4));
        mainImageView.setFitHeight(700 - (700 / 4));
        mainImageView.setPreserveRatio(true);
        
        width = mainImageView.getImage().getWidth();
        height = mainImageView.getImage().getWidth();
        Utilities.ImageTransaction(mainImageView, width, height);
        
        BorderPane pane = new BorderPane();
        pane.setPrefSize(1200- (1100 / 4), 700 - (700 / 4));
        pane.setCenter(mainImageView);
        centerPane.getChildren().add(pane);
        
        HBox hbox = new HBox(4);
        for (int i = 0; i < images.size(); ++i) {
            ImageView imageview = new ImageView();
            
            imageview.setOnMouseClicked(event -> {
                mainImageView.setImage(imageview.getImage());
                pos = images.indexOf(imageview.getImage());
//                imageview.setEffect(setEffect(50,Color.BLUE));
            });
            
            btnImageNext.setOnMouseClicked(event -> {
                mainImageView.setImage(images.get(ImageNext()));
//                imageview.setEffect(setEffect(50,Color.BLUE));
                
            });
            
            btnImagePrevious.setOnMouseClicked(event -> {
                mainImageView.setImage(images.get(ImagePrevious()));
//                imageview.setEffect(setEffect(50,Color.BLUE));
            });
            
            imageview.setImage(images.get(i));
            imageview.setFitWidth(1100 / 4);
            imageview.setFitHeight(700 / 4 - 50);
            imageview.setPreserveRatio(true);
            
            hbox.getChildren().add(imageview);
            
        }
        scrollpane.setContent(hbox);
    }
    
    public void setFiles(ArrayList fsImages){
        this.psImages = fsImages;
    }
    
//    public DropShadow setEffect(int depth, Color fsColor){
// 
//        DropShadow borderGlow= new DropShadow();
//        borderGlow.setOffsetY(0f);
//        borderGlow.setOffsetX(0f);
//        borderGlow.setColor(fsColor);
//        borderGlow.setWidth(depth);
//        borderGlow.setHeight(depth);
//        
//        return borderGlow;
//    }
    
    
    private void loadImages() {
        System.gc();
        images.clear();
        for(int i = 0; i <=psImages.size() -1; i++){
            try {
                File file = new File(psImages.get(i));
                images.add(SwingFXUtils.toFXImage(ImageIO.read(file), null));
                pos = 0;
                } catch (Exception e) {
                    System.err.print(e);
                }
        }
    }
    
    private void cmdButton_Click(ActionEvent event){
        String lsButton = (String)((Button)event.getSource()).getId().toLowerCase();
        switch(lsButton){
            case "btnterminate":
                CommonUtils.closeStage(btnTerminate);
                break;
        }
        
    }
    
    public int ImagePrevious(){
        pos = pos - 1;
        if(pos < 0){
            ShowMessageFX.Warning(null, pxeMoudelName, "No previous image found!");
            pos = 0;
        }
    return pos;
    }
    
    public int ImageNext(){
        pos = pos + 1;
        if(pos >= images.size()){
            ShowMessageFX.Warning(null, pxeMoudelName, "No next image found!");
            pos  = images.size() - 1;
        }
        return pos;
    }
    
}
