package org.rmj.edocumentsfx.views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.CustomTextField;
import org.rmj.appdriver.GRider;


public class AddDocsController implements Initializable {

    @FXML private AnchorPane acMain;
    @FXML private Button btnExit;
    @FXML private CustomTextField txtField00;
    @FXML private TableView table;
    @FXML private ScrollPane scrollpane;
    @FXML private ImageView imageview;
    @FXML private Button btnSearch;
    @FXML private Button btnPrint;
    @FXML private Button btnClose;
    @FXML private Button btnBrowse;
    @FXML private Button btnSave;
    @FXML private Button btnImagePrevious;
    @FXML private Button btnImageNext;
    
    private static GRider poGRider;

 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
 
    }    

    @FXML
    private void TableClick(MouseEvent event) {
    }
    
    public void setGRider(GRider fsGrider){
        this.poGRider = fsGrider;
    }
    
}
