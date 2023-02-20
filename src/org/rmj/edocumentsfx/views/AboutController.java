package org.rmj.edocumentsfx.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.rmj.appdriver.GRider;

public class AboutController implements Initializable {

    @FXML private AnchorPane acMain;
    @FXML private Button btnExit;
    @FXML private FontAwesomeIconView glyphExit;
    @FXML private Label lblProduct;
    @FXML private Label lblAddress;
    @FXML private Label lblTelephone;    
    @FXML private Label lblProgramName;
    @FXML private Label lblCopyright;
    
    private GRider poGRider;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblProduct.setText("");
        lblAddress.setText("");
        lblTelephone.setText("");
        lblProgramName.setText("");
        
        lblProgramName.setText(poGRider.getProductName());
        lblProduct.setText(poGRider.getBranchName());
        lblAddress.setText(poGRider.getAddress() + ", " + poGRider.getTownName() + " " + poGRider.getProvince());
        if (!poGRider.getTelNo().equals("")) lblTelephone.setText("Tel. No. " + poGRider.getTelNo() + " ");
        if (!poGRider.getFaxNo().equals("")) lblTelephone.setText(lblTelephone.getText() + "Fax No. " + poGRider.getFaxNo());
    }    

    @FXML
    private void btnExit(ActionEvent event) {
        unloadForm();
        acMain.setStyle("-fx-border-color: transparent");
    }
    
    public void setGRider(GRider foGRider){this.poGRider = foGRider;}
    
    private void unloadForm(){
        acMain.getChildren().clear();
    }
    
}
