
package org.rmj.edocumentsfx.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.edocx.trans.agentFX.DocumentsMulti;

public class FileTyeSelectionController implements Initializable {

    @FXML private AnchorPane acMain;
    @FXML private Label lblTitle;
    @FXML private Button btnExit;
    @FXML private FontAwesomeIconView glyphExit;
    @FXML private AnchorPane anchorField;
    @FXML private TableView<ComboTableModel> table;
    @FXML private Button btnOkay;
    @FXML private TableColumn index01;
    @FXML private TableColumn index02;
    @FXML private Button btnBack;
   
    private String psDefaultPath = "";
    private int pnRowTable = -1;
    private ArrayList<String> psImageList = new ArrayList<>();
    private ObservableList<ComboTableModel> docsList = FXCollections.observableArrayList();
    private DocumentsMulti poTrans;
    private boolean isRegister;
    private int pnRow = -1;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnBack.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnOkay.setOnAction(this::cmdButton_Click);
        initGrid();
    }
    
    
    public void setDefaultPath(String fsPath){
        this.psDefaultPath = fsPath;
    }
    
    
    public void setClass(DocumentsMulti fsClass){
        this.poTrans = fsClass;
    }
    
    public void setComboTable(ObservableList<ComboTableModel> items){
        this.docsList = items;
        
    }
    
    public void initGrid(){
        index01.setSortable(false); index01.setResizable(false);index01.setStyle("-fx-alignment: CENTER");
        index02.setSortable(false); index02.setResizable(false);index02.setStyle("-fx-alignment: CENTER-LEFT");

        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.TableModel,String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.TableModel,String>("index02"));

        /*Set data source to table*/
        table.setItems(docsList);
    }
    
    private void cmdButton_Click(ActionEvent event){
        String lsButton = (String)((Button)event.getSource()).getId().toLowerCase();
        switch(lsButton){
            case "btnexit":
            case "btnback":
                CommonUtils.closeStage(btnBack);
                break;
            case "btnokay":
                generateImages();
                break;
        }
        
    }

    @FXML
    private void Table_Click(MouseEvent event) {
        if(table.getItems().isEmpty()) return; 
        if(poTrans.MasterFileCount() ==0) return;
        
        pnRow = Integer.valueOf(table.getSelectionModel().getSelectedItem().getIndex01());
        if(event.getClickCount() ==2){
            generateImages();
        }
    }
    
    public void generateImages(){
        psImageList.clear();
        for (int i = 0; i <=poTrans.MasterFileCount()-1; ++i) {
            try {
                if((int) poTrans.getMasterFile(i, "nClientNo") ==pnRowTable){
                    if((int) poTrans.getMasterFile(i, "nFileNoxx") ==pnRow){
                        if(isRegister == false){
                            psImageList.add(psDefaultPath+(String) poTrans.getMasterFile(i, "sFileName")+".jpg");
                        }else{
                            psImageList.add(psDefaultPath+(String) poTrans.getMasterFile(i, "sFileName"));
                        }
                    }
                }
            } catch (Exception e) {
                   e.printStackTrace();
            }
        }
        if(psImageList.isEmpty()) return;
        
        ImageGalleryController loImageGallery = new ImageGalleryController();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("ImageGallery.fxml"));

        fxmlLoader.setController(loImageGallery);
        loImageGallery.setFiles(getFiles());

        try {
            fxmlLoader.load();
            Parent parent = fxmlLoader.getRoot();
            Scene scene = new Scene(parent);
            scene.setFill(new Color(0, 0, 0, 0));

            Stage stage = new Stage();
            stage.setScene(scene);
            
            stage.initModality(Modality.APPLICATION_MODAL);
            
            stage.centerOnScreen();

            stage.initStyle(StageStyle.TRANSPARENT);
            stage.showAndWait();
        } catch (IOException | NullPointerException ex) {
            Logger.getLogger(FileTyeSelectionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList getFiles(){
        return psImageList;
    }
    
    public void setRowIndex(int fnRow){
        this.pnRowTable = fnRow;
    
    }
    
    public void setRegister(boolean fsRegister){
        this.isRegister = fsRegister;
    }
    
    
}
