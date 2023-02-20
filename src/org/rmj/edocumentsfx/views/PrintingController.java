package org.rmj.edocumentsfx.views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;


public class PrintingController implements Initializable {

    @FXML private AnchorPane acMain;
    @FXML private Button btnExit;
    @FXML private TextField txtField00;
    @FXML private TableView table;
    @FXML private ScrollPane scrollpane;
    @FXML private ImageView imageview;
    @FXML private Button btnSearch;
    @FXML private Button btnPrint;
    @FXML private Button btnClose;
    @FXML private ComboBox cmb00;

    private GRider poGRider;
    private final static String pxeModuleName = "PrintingControlle";
    ObservableList<String> cmbCriteria = FXCollections.observableArrayList("Customer Name", "Batch No", "Engine No", "Chassis No");
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        btnSearch.setOnAction(this::cmdButton_Click);
        btnPrint.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        txtField00.setOnKeyPressed(this::txtField_KeyPressed);
        
        clearField();
    }    

    @FXML
    private void TableClick(MouseEvent event) {
    }
    
    public void setGRider(GRider fsGRider){
        this.poGRider = fsGRider;
    }
    
    private void clearField(){
        txtField00.setText("");
        cmb00.setItems(cmbCriteria);
        cmb00.getSelectionModel().select(0);    
    
    }
    
    public void cmdButton_Click(ActionEvent event) { 
        String lsButton  = ((Button)event.getSource()).getId().toLowerCase();
        
        switch(lsButton){   
            case "btnclose"://New 
                unloadForm();
                break;
            case "btnprint":
                ShowMessageFX.Information(null, pxeModuleName, "This feature is coming soon...");
                break;
            case "btnsearch":
                ShowMessageFX.Information(null, pxeModuleName, "This feature is coming soon...");
                break;
            case "btnexit":
                unloadForm();
                break;
                
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
    }
    
    private void unloadForm(){
        acMain.getChildren().clear();
        acMain.setStyle("-fx-border-color: transparent");
    }
    
    public void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField)event.getSource();        
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue =txtField.getText();
            
        if (event.getCode() == F3 || event.getCode() == ENTER){
            switch (lnIndex){                
                case 0: 
                    if(event.getCode() == ENTER)
                    ShowMessageFX.Information(null, pxeModuleName, "This feature is coming soon...");
                    break;
            }
        }
        if (event.getCode() == DOWN || event.getCode() == ENTER){
             CommonUtils.SetNextFocus(txtField);                  
        }
        if (event.getCode() == UP){
             CommonUtils.SetPreviousFocus(txtField);                 
        }
    }
    
}
