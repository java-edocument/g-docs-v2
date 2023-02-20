package org.rmj.edocumentsfx.views;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.textfield.CustomTextField;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.edocumentsfx.utilities.Utilities;
import org.rmj.edocx.trans.agentFX.XMEDocuments;

/**
 * FXML Controller class
 *
 * @author user
 */
public class IndividualViewController implements Initializable {

    @FXML private AnchorPane acMain;
    @FXML private Label lblTitle;
    @FXML private Button btnExit;
    @FXML private TableView<TableModel> table;
    @FXML private CustomTextField txtField04;
    @FXML private ImageView imageview;
    @FXML private Label lblImagePreview;
    @FXML private Button btnImagePrevious;
    @FXML private Button btnImageNext;
    @FXML private Button btnClose;
    @FXML private TableColumn index01;
    @FXML private TableColumn index02;
    @FXML private TableColumn index03;
    @FXML private TableColumn index04;
    
    private static GRider poGRider;
    private BrowseMasterController loBrowseMaster;
    private String psDefaultPath = "";
    private String psBranchNm = "";
    private String psBranchCd = "";
    private final String pxeModuleName = "Documents Scanned History";
    public String psFileName2 = "";
    private String name = "";
    private int pnEditMode;
    private int pnIndex = -1;
    private int pnRow = -1;
    private boolean pbLoaded = false;
    private String psOldRec;
    private ObservableList<TableModel> items = FXCollections.observableArrayList();
    private XMEDocuments poTrans;
    private String psNoImage = "";
    public ArrayList<String> psimages = new ArrayList<String>();
    public ArrayList<String> psList = new ArrayList<String>();
    private int pos = 0;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        if (poGRider == null){
                ShowMessageFX.Warning("GhostRider Application not set..", pxeModuleName, "Please inform MIS department.");
                System.exit(0);
            }
            
            btnExit.setOnAction(this::cmdButton_Click);
            btnClose.setOnAction(this::cmdButton_Click);
            btnImageNext.setOnAction(this::cmdButton_Click);
            btnImagePrevious.setOnAction(this::cmdButton_Click);
            
            acMain.setOnKeyReleased(this::handleKeyCode);
            acMain.setOnKeyReleased(this::handleKeyCode);
            
            initGrid();
            setTextSearch();

            clearFields();
            pbLoaded = true;
    }    

    @FXML
    private void TableClick(MouseEvent event) {
        if(event.getClickCount() ==1){
                loadImages();
            }else{
                if(psimages.isEmpty()) return;
                ImageGalleryController loController = new ImageGalleryController();
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("ImageGallery.fxml"));

                fxmlLoader.setController(loController);
                loController.setFiles(getFiles());

                try {
                    fxmlLoader.load();
                    Parent parent = fxmlLoader.getRoot();
                    Scene scene = new Scene(parent);
                    scene.setFill(new Color(0, 0, 0, 0));

                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    //height location
                    stage.centerOnScreen();
                    //width location

                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.showAndWait();
                } catch (IOException | NullPointerException ex) {
                    Logger.getLogger(CRDocsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }
    
    private boolean loadImage(String fsImage){
        Image image = new Image(new File(fsImage).toURI().toString());
        
        imageview.setImage(image);
        imageview.setPreserveRatio(true);
        double width = image.getWidth();
        double height = image.getHeight();
        
        lblImagePreview.setText(fsImage.replace(psDefaultPath, ""));
        Utilities.ImageTransaction(imageview, width, height);
            
        boolean isError = image.isError();
        if (!isError) {
            return true;
        }else{
            return false;
        }
        
    }
    
    


    
    public void setFilePath(String fsFilePath){
        this.psDefaultPath = fsFilePath;
    }
    
    
    public void setNoImage(String fsImage){
        this.psNoImage = fsImage;
    }
    
    private String loadDefaultImage(String fsImage){
        Image image = new Image(fsImage);
        imageview.setImage(image);
        lblImagePreview.setText("No Image Preview");
        
        return fsImage;
    }
    
    public void setGRider(GRider foGRider){
        this.poGRider= foGRider;
    }
    
    public void setClass(XMEDocuments foTrans){
        this.poTrans= foTrans;
    }
    
    public void setEditMode(int fnEditMode){
        this.pnEditMode = fnEditMode;
    }
    
    public void setBranchNm(String fsBranchNm){
        this.psBranchNm = fsBranchNm;
    }
    
    public void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId().toLowerCase();
        switch (lsButton){
            case "btnexit"://Close   
            case "btnclose":
                loBrowseMaster.setStage();
                loBrowseMaster.clearFields();
                unloadScene(event);
                break;
                
            case "btnimagenext":
                if(!table.getItems().isEmpty() && !psList.isEmpty()){
                    ImageNext();
                }
                break;
                
            case "btnimageprevious":
                if(!table.getItems().isEmpty()&& !psList.isEmpty()){
                  ImagePrevious();
                }
                break;
            
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
        
    }
    public ArrayList getFiles(){
        return psimages;
    }
    
    private int getArrayIndex(){    
        return psList.indexOf(name);
    }
    
    public void setTableFocus(int fsRow){
        TableModel instance;
        for(int lnCtr = 0; lnCtr <=items.size() -1; lnCtr++){
            instance = (TableModel) items.get(lnCtr);
            if((int)poTrans.getMasterFile(fsRow,"nEntryNox")== Integer.valueOf(instance.getIndex01())){
                table.requestFocus();
                table.getSelectionModel().select(lnCtr);
                table.getFocusModel().focus(lnCtr);
            }
        }
        System.err.println(pos);   
    }
    
    private void unloadScene(ActionEvent event){
        psBranchCd = "";
        psBranchNm = "";
        Node source = (Node)  event.getSource(); 
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    public void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField)event.getSource();        
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (event.getCode() == F3 || event.getCode() == ENTER) {
            switch (lnIndex){
                case 0: 
                     
                    
                case 2:
                    break;
                case 3:
                    
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
   
    public void clearFields() {
        
        lblImagePreview.setText("No Image Preview");
        psOldRec = "";
        pnRow = -1;
    }
    
    public void setController(BrowseMasterController loController){
        this.loBrowseMaster = loController;
    }
    
    public void initGrid(){
        
        index01.setSortable(false); index01.setResizable(false);index01.setStyle("-fx-alignment: CENTER");
        index02.setSortable(false); index02.setResizable(false);index02.setStyle("-fx-alignment: CENTER");
        index03.setSortable(false); index03.setResizable(false);index03.setStyle("-fx-alignment: CENTER");
        index04.setSortable(false); index04.setResizable(false);index04.setStyle("-fx-alignment: CENTER");
       
        table.getColumns().clear();
        table.getColumns().addAll(index01,index02,index03,index04);
       
        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.TableModel,String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.TableModel,String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.TableModel,String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.TableModel,String>("index04"));

        /*Set data source to table*/
        table.setItems(items);
       
    }
    
    public void loadDetail2Grid() {
        int dataSize = poTrans.ItemCount();
        int lnCtr;
        
        items.clear();
        for(lnCtr = 0; lnCtr <= dataSize -1 ; lnCtr++){
            items.add(new TableModel(String.valueOf(lnCtr+1),
                    (String) poTrans.getDetail(lnCtr, "sAcctNmbr"), 
                    (String) poTrans.getDetail(lnCtr, "sCompnyNm"), 
                     String.valueOf(poTrans.getDetail(lnCtr, "dPurchase")), 
                     "",
                    ""
                    ));
         }
            table.setItems(items);
            setTextSearch();
            table.requestFocus();
            table.getSelectionModel().select(0);
            table.getFocusModel().focus(0);
            loadImages();
            
        }
       
    public void setTextSearch(){
        FilteredList<TableModel> filteredData = new FilteredList<>(items, b -> true);
		// 2. Set the filter Predicate whenever the filter changes.
		txtField04.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(tableModel -> {
				// If filter text is empty, display all persons.
								
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				// Compare first name and last name of every person with filter text.
				String lowerCaseFilter = newValue.toLowerCase();
				
				if (tableModel.getIndex02().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
					return true; // Filter matches name name.
				} else if (tableModel.getIndex03().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches crnoxx.
				} else if (tableModel.getIndex04().toLowerCase().indexOf(lowerCaseFilter) != -1)
                                        return true;
				     else  
				    	 return false; // Does not match.
			});
		});
		
		// 3. Wrap the FilteredList in a SortedList. 
		SortedList<TableModel> sortedData = new SortedList<>(filteredData);
		
		// 4. Bind the SortedList comparator to the TableView comparator.
		// 	  Otherwise, sorting the TableView would have no effect.
		sortedData.comparatorProperty().bind(table.comparatorProperty());
		
		// 5. Add sorted (and filtered) data to the table.
		table.setItems(sortedData);
    }
    
    
    
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        if (!pbLoaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;      
        if(!nv){ /*Lost Focus*/
            switch (lnIndex){
                case 0:
            }
            pnIndex = lnIndex;
            txtField.selectAll();
        } else{
            switch (lnIndex){
                
                default:
            }
            pnIndex = lnIndex;
            txtField.selectAll();
        }
    };
    
    private void loadImages(){
        int tempRow = 0;
        psList.clear();
        psimages.clear();
        
        if(poTrans.ItemCount()==0) return;
        tempRow = Integer.valueOf(table.getSelectionModel().getSelectedItem().getIndex01());
            if(poTrans.MasterFileCount() == 0) return;
                for (int i = 0; i <= poTrans.MasterFileCount()-1; ++i) {
                    try {
                        psList.add(psDefaultPath+(String) poTrans.getMasterFile(i, "sFileName"));
                        if((int) poTrans.getMasterFile(i, "nEntryNox") ==tempRow){
                            psimages.add(psDefaultPath+poTrans.getMasterFile(i, "sFileName"));
                        }
                    } catch (Exception e) {
                           e.printStackTrace();
                    }
                }
        try{
            if(loadImage(psimages.get(0))==true){
                pos = psList.indexOf(psimages.get(0));
            }else{
                loadDefaultImage(psNoImage);
            }
        }catch(IndexOutOfBoundsException e){
            loadDefaultImage(psNoImage);
        }
                
    }
    
    public void reloadImage(int fnRow){
        try{
            if(loadImage(psimages.get(fnRow))==true){
            }else{
                loadDefaultImage(psNoImage);
            }
        }catch(IndexOutOfBoundsException e){
            loadDefaultImage(psNoImage);
        }

    }

    public void ImageNext(){
        pos = pos + 1;
        if(pos >= psList.size()){
            ShowMessageFX.Warning(null, pxeModuleName, "No next image found!");
            pos  = psList.size() - 1;
        }
        loadImage(psList.get(pos));
        setTableFocus(pos);
    }

    public void ImagePrevious(){
        pos = pos - 1;
        if(pos < 0){
            ShowMessageFX.Warning(null, pxeModuleName, "No previous image found!");
            pos = 0;
        }
        loadImage(psList.get(pos));
        setTableFocus(pos);

    }

    public void handleKeyCode(KeyEvent event) {
        switch(event.getCode()){
            case RIGHT:
                if(!table.getItems().isEmpty() && !psList.isEmpty()){
                    ImageNext();
                }
                break;
            case LEFT:
                if(!table.getItems().isEmpty()&& !psList.isEmpty()){
                    ImagePrevious();
                }
                break;
                
            case UP:
                loadImages();
                break;
            case DOWN:
                loadImages();
               break;
        }
    }
    
}
