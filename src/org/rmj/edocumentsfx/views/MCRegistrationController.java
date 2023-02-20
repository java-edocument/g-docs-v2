package org.rmj.edocumentsfx.views;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ComboBox;
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
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.CustomTextField;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.edocumentsfx.utilities.Utilities;
import org.rmj.edocx.trans.agentFX.XMEDocumentsMulti;

/**
 * FXML Controller class
 *
 * @author user
 */
public class MCRegistrationController implements Initializable {

    @FXML private AnchorPane acMain;
    @FXML private Label lblTitle;
    @FXML private Button btnExit;
    @FXML private TextField txtField02;
    @FXML private TextField txtField03;
    @FXML private ComboBox cmb00;
    @FXML private CustomTextField txtField00;
    @FXML private CustomTextField txtField04;
    @FXML private TableView<TableWithCheckBoxModel> table;
    @FXML private TableColumn index01;
    @FXML private TableColumn index02;
    @FXML private TableColumn index03;
    @FXML private TableColumn index04;
    @FXML private TableColumn index05;
    @FXML private Button btnImagePrevious;
    @FXML private Button btnImageNext;
    @FXML private Button btnImageMoveDown;
    @FXML private Button btnImageMoveUP;
    @FXML private Button btnImageDelete;
    @FXML private ImageView imageview;
    @FXML private Label lblImagePreview;
    @FXML private Button btnSearch;
    @FXML private Button btnDelete;
    @FXML private Button btnBack1;
    @FXML private Button btnScan;
    @FXML private Button btnSave;
    @FXML private Button btnClose;
    @FXML private Button btnCancel;
    @FXML private Button btnBack;
    @FXML private ComboBox<ComboTableModel> cmbFilter;

    private String psCheckBox = "";
    private static GRider poGRider;
    private String psDefaultPath = "";
    private String psBranchNm = "";
    private String psBranchCd = "";
    private final String pxeDateFormat = "yyyy-MM-dd";  
    private final String pxeModuleName = "Documents Scanner";
    private String psDeptNm2 = "";
    private int pnDocSize = 0;
    private int pnEditMode;
    private int pnIndex = -1;
    private int pnRow = -1;
    private int pnComboRow = -1;
    private boolean pbLoaded = false;
    private String psOldRec;
    private int pnRowTable= -1;
    private ObservableList<TableWithCheckBoxModel> items = FXCollections.observableArrayList();
    private ObservableList<ComboTableModel> comboItem = FXCollections.observableArrayList();
    private XMEDocumentsMulti poTrans;
    private MCRegistrationSelectorController loCrDocs;
    private MCRegistrationSelectorRegisterController loMultipleDocsReg;
    
    private String psDefaultImage = "";
    private String psNoImage = "";
    private Image search;
    private ArrayList<String> psimages = new ArrayList<String>();
    private ArrayList<String> psList = new ArrayList<String>();
    private ArrayList<String> psClientList = new ArrayList<String>();
    private int pos = 0;
    private boolean isloaded;
    private int tempRow = 0;
    private int tempComboRow = 0;    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (poGRider == null){
                    ShowMessageFX.Warning("GhostRider Application not set..", pxeModuleName, "Please inform MIS department.");
                    System.exit(0);
            }

            if(isloaded == false){
                if (psDeptNm2.equals("")) psDeptNm2 = loCrDocs.getDepartment();
                if(pnDocSize== 0) pnDocSize = loCrDocs.getDocsSize();
            }
            
            poTrans.setSourceCD("MCRg");
            btnCancel.setOnAction(this::cmdButton_Click);
            btnScan.setOnAction(this::cmdButton_Click);
            btnSave.setOnAction(this::cmdButton_Click);
            btnExit.setOnAction(this::cmdButton_Click);
            btnBack.setOnAction(this::cmdButton_Click);
            btnSearch.setOnAction(this::cmdButton_Click);
            btnClose.setOnAction(this::cmdButton_Click);
            btnBack1.setOnAction(this::cmdButton_Click);
            btnDelete.setOnAction(this::cmdButton_Click);
            btnImageNext.setOnAction(this::cmdButton_Click);
            btnImagePrevious.setOnAction(this::cmdButton_Click);
            btnImageDelete.setOnAction(this::cmdButton_Click);
            btnImageMoveDown.setOnAction(this::cmdButton_Click);
            btnImageMoveUP.setOnAction(this::cmdButton_Click);
            
            /*Add listener to text fields*/
            txtField00.focusedProperty().addListener(txtField_Focus);
            txtField02.focusedProperty().addListener(txtField_Focus);
            txtField03.focusedProperty().addListener(txtField_Focus);            
            
            txtField00.setOnKeyPressed(this::txtField_KeyPressed);
            txtField02.setOnKeyPressed(this::txtField_KeyPressed);
            txtField03.setOnKeyPressed(this::txtField_KeyPressed);
            
            acMain.setOnKeyReleased(this::handleKeyCode);
            acMain.setOnKeyReleased(this::handleKeyCode);
            
            txtField00.setMouseTransparent(true);
            txtField00.setLeft(new ImageView(search));
            initGrid();
            setTextSearch();
            generateYear();
            clearFields();
            
            generateDocs();
            comboBox();
            initButton(pnEditMode);
            pbLoaded = true;
    }  
    
    public void generateYear(){
        Calendar currentYear = Calendar.getInstance();
        int curYear = currentYear.get(Calendar.YEAR);
        currentYear.add(Calendar.YEAR, -20);
        int lasYear =  currentYear.get(Calendar.YEAR);
        int i = 0;
        int[] year = new int[19];
        for (year[i] = curYear ; year[i] >= lasYear; year[i]--){
            cmb00.getItems().addAll(year[i]);
        }
    }

    @FXML
    private void TableClick(MouseEvent event) {
        if(table.getItems().isEmpty()) return; 
        pnRowTable =(int) table.getSelectionModel().getFocusedIndex();
        if(event.getClickCount() ==1){
                if(isloaded == false){
                    generateImages();
                }else{
                    loadImages();
                }
            }else{
                if(psimages.isEmpty()) return;
                
                FileTyeSelectionController loFileType = new FileTyeSelectionController();
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("FileTyeSelection.fxml"));

                fxmlLoader.setController(loFileType);
                
                loFileType.setDefaultPath(psDefaultPath);
                loFileType.setClass(poTrans);
                loFileType.setComboTable(getComboItem());
                if(isloaded ==false ){
                    loFileType.setRowIndex(pnRow);
                }else{
                    loFileType.setRowIndex(tempRow);
                }
                
                loFileType.setRegister(isloaded);

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
    
    public void cmbSetFocus(int fnRow){
        ComboTableModel instance;
        for(int lnCtr = 0; lnCtr <= comboItem.size() -1; lnCtr++){
            instance = (ComboTableModel) comboItem.get(lnCtr);
            if((int)poTrans.getMasterFile(fnRow,"nFileNoxx")== Integer.valueOf(instance.getIndex01())){
                cmbFilter.getSelectionModel().select(lnCtr);
            }
            
        }
    }
    
    public void setLoaded(Boolean fsloaded){
        this.isloaded = fsloaded;
    }

    public void generateDocs(){
        comboItem.clear();
        int dataSize =poTrans.SysFileCount();
        
        for (int lnCtr = 0; lnCtr <=dataSize -1; lnCtr++){
            comboItem.add(new ComboTableModel(String.valueOf(lnCtr+1),
                                              String.valueOf(poTrans.getSysFile(lnCtr, "sBriefDsc"))));
        }
        
        cmbFilter.setConverter(new StringConverter<ComboTableModel>() {
            @Override
            public String toString(ComboTableModel object) {
                return object.getIndex02();
                
            }

            @Override
            public ComboTableModel fromString(String string) {
                return null;
            }

           
        });
        
        cmbFilter.setItems(comboItem);
        cmbFilter.getSelectionModel().select(0);
    }
    
    
    public ObservableList<ComboTableModel> getComboItem(){
        return comboItem;
    }
    
    public void setClass(XMEDocumentsMulti foTrans){
        this.poTrans= foTrans;
    }
     
    public void setGRider(GRider fsGRider){
        this.poGRider = fsGRider;
    }
    
    public void setEditMode(int fnEditMode){
        this.pnEditMode = fnEditMode;
    }
    
    public void setFilePath(String fsFilePath){
        this.psDefaultPath = fsFilePath;
    }
    
    public void setImageDefault(String fsImageDefault){
        this.psDefaultImage = fsImageDefault;
    }
    
    public void setNoImage(String fsNoImage){
        this.psNoImage = fsNoImage;
    }
    
    public void setMultipleDocs(MCRegistrationSelectorController loController){
        this.loCrDocs = loController;
    }
    
    public void setMultipleDocsReg(MCRegistrationSelectorRegisterController loController){
        this.loMultipleDocsReg = loController;
    }
    
    private boolean loadImage(String fsImage){
        Image image = new Image(new File(fsImage).toURI().toString());
        double width = image.getWidth();
        double  height = image.getHeight();
        imageview.setImage(image);
        
        Utilities.ImageTransaction(imageview, width, height);
        lblImagePreview.setText(fsImage.replace(psDefaultPath, ""));
        boolean isError = image.isError();
        
        if (!isError) {
            return true;
        }else{
            return false;
        }
        
    }
    
    private String loadDefaultImage(String fsImage){
        Image image = new Image(fsImage);
        double width = image.getWidth();
        double  height = image.getHeight();
        imageview.setImage(image);
        Utilities.ImageTransaction(imageview, width, height);
        lblImagePreview.setText("No Image Preview");
        
        return fsImage;
    }
    
    public String deleteSelectedCheckBox(){
       psCheckBox = "";
        TableWithCheckBoxModel instance;
        for(int lnCtr = 0; lnCtr <=items.size()-1; lnCtr++){
            instance = (TableWithCheckBoxModel) items.get(lnCtr);
            String lsValue= String.valueOf(instance.getIndex05().isSelected());
            if(lsValue.equals("true")){
                lsValue = instance.getIndex01();
            }else{
                lsValue = "false";
            }
            psCheckBox +=lsValue+"»";           
            
        }
        
        psCheckBox= psCheckBox.replace("false»", "");
        
        try{
            return psCheckBox.substring(0, psCheckBox.length()-1).replaceAll("\\s", "");
        }catch(StringIndexOutOfBoundsException e){
            return "false";
        }
        
    }
    
    
    public void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId().toLowerCase();
        switch (lsButton){
            case "btndelete":
                    if(ShowMessageFX.OkayCancel("Deletion of detail cannot be undo, proceed now?", pxeModuleName, "Do you want to remove this information?") == true){
                        if(table.getItems().size() == 1){
                            ShowMessageFX.Warning(null, pxeModuleName, "Unable to delete last row!");
                            break;
                        }
                        
                        if(poTrans.MasterFileCount() !=0){
                            if(ShowMessageFX.OkayCancel("All scanned images will be deleted.. Wish to continue?", pxeModuleName, "Scanned Images detected!") == true){
                                if(poTrans.deleteAllImage()==true){
                                ShowMessageFX.Information(null, pxeModuleName, "All images successfully deleted!");
                                loadDefaultImage(psDefaultImage);
                                }
                             }else{
                                return;
                            }
                        }              
                       if (!deleteSelectedCheckBox().equals("false")) {
                            poTrans.deleteDetailS(deleteSelectedCheckBox());
                            loadDetail2Grid();
                        }else{
                            ShowMessageFX.Warning(null, pxeModuleName, "No record selected!");

                        }
                    }
                break;
                
            case "btnsearch":
                if(psBranchCd.equals("")){
                    ShowMessageFX.Error(null, pxeModuleName, "No Branch selected!\n Please select one and try again!");
                    return;
                }
                
                if (poTrans.getCRInventory(psBranchCd, txtField02.getText(), txtField03.getText(),(String) cmb00.getSelectionModel().getSelectedItem().toString()) == true){
                if(poTrans.MasterFileCount()!= 0){
                    if(ShowMessageFX.YesNo("All scan images will be deleted. Continue?", pxeModuleName, "Scanned Docs detected!")==true){
                         if(poTrans.deleteAllImage()==true){
                        ShowMessageFX.Information(null, pxeModuleName, "All images successfully deleted!");
                    }
                    }
                }
                loadDetail2Grid();
                loadDefaultImage(psDefaultImage);
            }else{
                ShowMessageFX.Warning(null, pxeModuleName, poTrans.getMessage());
                if(poTrans.MasterFileCount()!= 0){
                    if(ShowMessageFX.YesNo("All scan images will be deleted. Continue?", pxeModuleName, "Scanned Docs detected!")==true){
                         if(poTrans.deleteAllImage()==true){
                        ShowMessageFX.Information(null, pxeModuleName, "All images successfully deleted!");
                    }
                    }
                }
                items.clear();
                table.getItems().clear();
                loadDefaultImage(psDefaultImage);
            }

                break;
            case "btnscan": //scan
                    if(!table.getItems().isEmpty()){
                        if(ShowMessageFX.YesNo("Scanning of documents will not be cancelled. Continue?", pxeModuleName, "Please make sure to mount all necessary documents on printer...")== true){
//                            poTrans.sample_fileMultiple();
                            if(poTrans.start_scan()==true){
                                ShowMessageFX.Information("Please verify if all documents match with corresponding detail...", pxeModuleName, "All documents successfully scanned!");
                                table.requestFocus();
                                table.getSelectionModel().select(0);
                                table.getFocusModel().focus(0);
                                generateImages();
                            }else{
                                if(poTrans.getMessage()!= "") ShowMessageFX.Information("An error has occurred. Error code: "+poTrans.getMessage(), pxeModuleName, "Please contact MIS/SEG!");
                            }
                        }
                    }else{
                        ShowMessageFX.Error(null, pxeModuleName, "Please make sure to search for record first before scanning!!!");
                    }
                  break;
                  
            case "btncancel"://Exit
                if(ShowMessageFX.YesNo("All changes made including scanned images will be disregard?", pxeModuleName,"Do you want to close this form?" )==true){
                    if(poTrans.MasterFileCount()!= 0){
                        if(poTrans.deleteAllImage()==true){
                        ShowMessageFX.Information(null, pxeModuleName, "All images successfully deleted!");
                        }
                    }
                    clearFields();
                    loCrDocs.setStage();
                    unloadScene(event);
                }
                break;
                
            case "btnexit"://Close   
            case "btnclose":
                if(isloaded ==false){
                    loCrDocs.setStage();
                    loCrDocs.clearFields();
                    loCrDocs.initButton(EditMode.UNKNOWN);
                }else{
                    try{
                        loMultipleDocsReg.setStage();
                        loMultipleDocsReg.clearFields();
                        loMultipleDocsReg.initButton(EditMode.UNKNOWN);
                    }catch(NullPointerException e){
                        loCrDocs.setStage();
                        loCrDocs.clearFields();
                        loCrDocs.initButton(EditMode.UNKNOWN);
                    }
                }
                unloadScene(event);
                break;
            case "btnsave": //Save
                if(table.getItems().isEmpty()){
                    ShowMessageFX.Error(null, pxeModuleName, "Please retrieve record first before saving!!!" );
                    return;
                }
                
                if(poTrans.MasterFileCount()==0){
                    ShowMessageFX.Error(null, pxeModuleName, "Please make sure to scan documents first before saving!!!" );
                    return;
                }
                
//                if(poScan.getScanStat() == true){
                    if(ShowMessageFX.YesNo("All scanned documents will be save!!!", pxeModuleName, "Do you want to save this transaction?") == true){
                        poTrans.setSourceCD("MCRg");
                        if (poTrans.saveUpdate() == true) {
                            ShowMessageFX.Information(null, pxeModuleName, "Transaction successfully saved!");
                            pnEditMode = EditMode.UNKNOWN;
                            initButton(pnEditMode);
                            isloaded = true;
                            loadDetail2Grid();
                            table.requestFocus();
                            table.getSelectionModel().select(0);
                            table.getFocusModel().focus(0);
                        }else{
                            ShowMessageFX.Error(poTrans.getMessage(), pxeModuleName, "Please contact MIS!!!" );
                        }
                    }
//                }else{
//                    ShowMessageFX.Error(null, pxeModuleName, "Please make sure to scan documents first before saving!!!");
//                }
                break;
            case "btnupdate": //Update
//                if(!table.getItems().isEmpty()){
//                    if(poScan.getScanStat() == true){
////                        updateImage();
//                    }else{
//                        ShowMessageFX.Error(null, pxeModuleName, "Please make sure to scan documents before change!!!");
//                    }
//                }else{
//                    ShowMessageFX.Error(null, pxeModuleName, "Please make sure to search for record first before scanning!!!");
//                }
                break;
            
            case "btnback1":
                if(isloaded ==false){
                    loCrDocs.initButton(EditMode.UNKNOWN);
                }else{
                    try{
                        loMultipleDocsReg.initButton(EditMode.UNKNOWN);
                    }catch(NullPointerException e){
                        loCrDocs.initButton(EditMode.UNKNOWN);
                    }
                }
            case "btnback":
                if(isloaded ==false){
                    loCrDocs.setDeptName(psDeptNm2);
                    loCrDocs.setDocSize(pnDocSize);
                    btnBack.getScene().getWindow().hide();
                }else{
                    try{
                        btnBack1.getScene().getWindow().hide();
                    }catch(NullPointerException e){
                        loCrDocs.setDeptName(psDeptNm2);
                        loCrDocs.setDocSize(pnDocSize);
                        btnBack.getScene().getWindow().hide();
                    }
                }
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
            case "btnimagedelete":
                if(!table.getItems().isEmpty() && !psList.isEmpty()){
                    if(ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to remove this image?") == true){
                        if(poTrans.deleteImage(pos)==true){
                            poTrans.deleteMasterFile(pos);
                            ShowMessageFX.Information(null, pxeModuleName, "Successfully Deleted!");
                            reloadImage(pos-1);
                        }else{
                            ShowMessageFX.Warning(null, pxeModuleName, "Unable to delete image!");
                        }
                    }
                }
                break;
            case "btnimagemoveup":
                if(!table.getItems().isEmpty() && !psList.isEmpty()){
                    if(ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to move this image up?") == true){
                        try{
                            poTrans.moveMasterFile( pos, pos -1);
                            ShowMessageFX.Information(null, pxeModuleName, "Image Successfully moved!");
                            reloadImage(pos -1);
                        }catch(ArrayIndexOutOfBoundsException e){
                            ShowMessageFX.Warning(null, pxeModuleName, "Unable to move image!");
                        }
                    }
                }
                break;
            case "btnimagemovedown":
                if(!table.getItems().isEmpty()&& !psList.isEmpty()){
                    if(ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to move this image down?") == true){
                       try{
                           
//                           if(poTrans.MasterFileCount()  <= pos +1){
//                               poTrans.addMasterFile();
//                               
//                               int tempEnryNox = 0;
//                               String tempFileName = "";
//                               tempEnryNox = (int) poTrans.getMasterFile(pos, "nClientNo");
//                               tempFileName = (String) poTrans.getMasterFile(pos, "sFileName");
//                               
//                               poTrans.setMasterFile(pos+1, "nClientNo", tempEnryNox+1);
//                               poTrans.setMasterFile(pos+1, "sFileName", tempFileName);
//                               poTrans.deleteMasterFile(pos);
//                               
//                               ShowMessageFX.Information(null, pxeModuleName, "Image Successfully moved!");
//                               reloadImage(pos +1);
//                               return;
//                           }
                           
                           poTrans.moveMasterFile(pos, pos +1);
                           
                           ShowMessageFX.Information(null, pxeModuleName, "Image Successfully moved!");
                           reloadImage(pos +1);
                       }catch(IndexOutOfBoundsException e){
                           ShowMessageFX.Warning(null, pxeModuleName, "Unable to move image!");
                       }
                    }
                }
                break;
//            case "btnaddimage":
//                if(ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to remove this image?") == true){
//                poTrans.setMasterFile(getArrayIndex()+1, "nEtnryNox", poTrans.getMasterFile(getArrayIndex()+1, "nEtnryNox"));
//                poTrans.setMasterFile(getArrayIndex()+1, "sFileName", poTrans.getMasterFile(getArrayIndex()+1, "sFileName"));
//                break;
                
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
        
    }
    
    public void comboBox(){
        cmbFilter.valueProperty().addListener(new ChangeListener<ComboTableModel>() {
            @Override
            public void changed(ObservableValue<? extends ComboTableModel> observable, ComboTableModel oldValue, ComboTableModel newValue) {
                if(table.getItems().isEmpty()) return;
                    if(poTrans.MasterFileCount() ==0) return;
                        if(isloaded == false){
                            generateImages();
                        }else{
                            loadImages();
                        }
            }
    });
    }
    
    public void setTableFocus(int fsRow){
        TableWithCheckBoxModel instance;
        for(int lnCtr = 0; lnCtr <=items.size() -1; lnCtr++){
            instance = (TableWithCheckBoxModel) items.get(lnCtr);
            if((int)poTrans.getMasterFile(fsRow,"nClientNo")== Integer.valueOf(instance.getIndex01())){
                table.requestFocus();
                table.getSelectionModel().select(lnCtr);
                table.getFocusModel().focus(lnCtr);
            }
        }
        System.err.println(pos);
    }
    
    
//    public void deleteDetail(){
//        Path imagesPath = Paths.get(
//        poTrans.getDefaultPath() +
//        psImageName);
//        
//        try {
//            Files.delete(imagesPath);
//            System.out.println("File "
//                    + imagesPath.toAbsolutePath().toString()
//                    + " successfully removed");
//        } catch (IOException e) {
//            System.err.println("Unable to delete "
//                    + imagesPath.toAbsolutePath().toString()
//                    + " due to...");
//            e.printStackTrace();
//        }
//
//    }
    
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
                    if(event.getCode() == ENTER){
                        if(lsValue.equals("")){
                           ShowMessageFX.Warning(null, pxeModuleName, "Please input text to search!");
                           return;
                        }
                        if (poTrans.searchMaster("sBranchNm", lsValue) ==true){
                        txtField00.setText((String) poTrans.getOrigin().getMaster("sBranchNm"));
                        psBranchCd= (String) poTrans.getOrigin().getMaster("sBranchCd");
                        psBranchNm = txtField00.getText();
                    };
                    }else if(event.getCode() == F3) {
                        if (poTrans.searchMaster("sBranchNm", lsValue.equals("")? "%":lsValue) ==true){
                        txtField00.setText((String) poTrans.getOrigin().getMaster("sBranchNm"));
                        psBranchCd= (String) poTrans.getOrigin().getMaster("sBranchCd");
                        psBranchNm = txtField00.getText();
                        };
                    }
                    
                    break;
                case 2:
                    break;
                case 3:
                    if(psBranchCd.equals("")){
                        ShowMessageFX.Error(null, pxeModuleName, "No Branch selected!\n Please select one and try again!");
                        return;
                    }
                        if (poTrans.getCRInventory(psBranchCd, txtField02.getText(), txtField03.getText(),(String) cmb00.getSelectionModel().getSelectedItem().toString() ) == true){
                            if(poTrans.MasterFileCount()!= 0){
                                if(ShowMessageFX.YesNo("All scan images will be deleted. Continue?", pxeModuleName, "Scanned Docs detected!")==true){
                                    if(poTrans.deleteAllImage()==true){    
                                        ShowMessageFX.Information(null, pxeModuleName, "All images successfully deleted!");
                                    }
                                }
                            }
                            loadDetail2Grid();
                            loadDefaultImage(psDefaultImage);
                        }else{
                            ShowMessageFX.Warning(null, pxeModuleName, poTrans.getMessage());
                            if(poTrans.MasterFileCount()!= 0){
                                if(ShowMessageFX.YesNo("All scan images will be deleted. Continue?", pxeModuleName, "Scanned Docs detected!")==true){
                                     if(poTrans.deleteAllImage()==true){
                                    ShowMessageFX.Information(null, pxeModuleName, "All images successfully deleted!");
                                }
                                }
                            }
                            items.clear();
                            table.getItems().clear();
                            loadDefaultImage(psDefaultImage);
                        }
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
        txtField00.setText("");
        txtField02.setText("");
        txtField03.setText("");
        lblImagePreview.setText("No Image Preview");
        cmb00.getSelectionModel().select(0);
        cmbFilter.getSelectionModel().select(0);
        comboItem.clear();   
        items.clear();
        psClientList.clear();
        psOldRec = "";
        pnRow = -1;
        pnRowTable = -1;
    }
    
    public void loadTransaction() {
        txtField00.setText(psBranchNm);
    }
    
    public void initGrid(){
        index01.setSortable(false); index01.setResizable(false);index01.setStyle("-fx-alignment: CENTER");
        index02.setSortable(false); index02.setResizable(false);index02.setStyle("-fx-alignment: CENTER");
        index03.setSortable(false); index03.setResizable(false);index03.setStyle("-fx-alignment: CENTER");
        index04.setSortable(false); index04.setResizable(false);index04.setStyle("-fx-alignment: CENTER");
        index05.setSortable(false); index05.setResizable(false);index05.setStyle("-fx-alignment: CENTER");
       
//        table.getColumns().clear();
//        table.getColumns().addAll(index01,index02,index03,index04,index05);
       
        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.TableModel,String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.TableModel,String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.TableModel,String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.TableModel,String>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.TableModel,String>("index05"));

        /*Set data source to table*/
        table.setItems(items);
    }
    
    public void loadDetail2Grid() {
        int dataSize = poTrans.ItemCount();
        int lnCtr;
        
        items.clear();
        for(lnCtr = 0; lnCtr <= dataSize -1 ; lnCtr++){
            items.add(new TableWithCheckBoxModel(String.valueOf(lnCtr+1),
                    (String) poTrans.getDetail(lnCtr, "sCompnyNm"), 
                    (String) poTrans.getDetail(lnCtr, "sCRNoxxxx"), 
                    (String) poTrans.getDetail(lnCtr, "sEngineNo"),
                    ""
                    ));
        }
        
        if(pnEditMode == EditMode.ADDNEW){
            table.setItems(items);
            setTextSearch();
        }else{
            table.setItems(items);
            setTextSearch();
            table.requestFocus();
            table.getSelectionModel().select(0);
            table.getFocusModel().focus(0);
            loadImages();
        }
        
    }
    
    public void setTextSearch(){
        FilteredList<TableWithCheckBoxModel> filteredData = new FilteredList<>(items, b -> true);
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
		SortedList<TableWithCheckBoxModel> sortedData = new SortedList<>(filteredData);
		
		// 4. Bind the SortedList comparator to the TableView comparator.
		// 	  Otherwise, sorting the TableView would have no effect.
		sortedData.comparatorProperty().bind(table.comparatorProperty());
		
		// 5. Add sorted (and filtered) data to the table.
		table.setItems(sortedData);
                
    }
    
    private void initButton(int fnValue){
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        
        btnSearch.setVisible(lbShow);
        btnScan.setVisible(lbShow);
        btnBack.setVisible(lbShow);
//        btnUpdate.setVisible(lbShow);
        btnSave.setVisible(lbShow);
        btnCancel.setVisible(lbShow);
        btnDelete.setVisible(lbShow);
        
        btnImageDelete.setVisible(lbShow);
        btnImageMoveDown.setVisible(lbShow);
        btnImageMoveUP.setVisible(lbShow);
        
        txtField00.setMouseTransparent(lbShow);
        txtField00.setEditable(lbShow);
        txtField02.setMouseTransparent(lbShow);
        txtField03.setMouseTransparent(lbShow);
        cmb00.setMouseTransparent(lbShow);
        
        btnBack1.setVisible(!lbShow);
        btnClose.setVisible(!lbShow);
      
        txtField00.setMouseTransparent(!lbShow);
        txtField02.setMouseTransparent(!lbShow);
        txtField03.setMouseTransparent(!lbShow);
        cmb00.setMouseTransparent(!lbShow);
                        
        if (lbShow) loCrDocs.setFocus(txtField00);
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
                    if(lsValue.equals("") || lsValue.equals("%")){
                       txtField.setText("");
                       poTrans.setMaster(lnIndex, "");
                       psBranchNm = "";
                    }else{
                        txtField.setText(psBranchNm);
                    }return;
                case 1:
                    break;
                case 2:
                case 3:
                     break;
            }
        }
        else{
            pnIndex = lnIndex;
            txtField.selectAll();
        }
    };
    
    private void generateImages(){        
        psList.clear();
        psimages.clear();
        
        if(poTrans.MasterFileCount() == 0) return;
            pnComboRow = Integer.valueOf(cmbFilter.getSelectionModel().getSelectedItem().getIndex01());
            pnRow = Integer.valueOf(table.getSelectionModel().getSelectedItem().getIndex01());
        
                for (int i = 0; i <= poTrans.MasterFileCount()-1; ++i) {
                    try {
                        psList.add(psDefaultPath+(String) poTrans.getMasterFile(i, "sFileName")+".jpg");
                        if((int) poTrans.getMasterFile(i, "nClientNo") ==pnRow){
                            if((int) poTrans.getMasterFile(i, "nFileNoxx") == pnComboRow){
                                psimages.add(psDefaultPath+poTrans.getMasterFile(i, "sFileName").toString()+".jpg");
                            }
                        }
                        }
                     catch(Exception e){
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
    
    public void setBranchName(String fsBranchNm){
        this.psBranchNm = fsBranchNm;
    }
    
    private void loadImages(){
        psList.clear();
        psimages.clear();
        
        if(poTrans.MasterFileCount() == 0) return;
        tempRow = Integer.valueOf(table.getSelectionModel().getSelectedItem().getIndex01());  
        tempComboRow = Integer.valueOf(cmbFilter.getSelectionModel().getSelectedItem().getIndex01());
        
                for (int i = 0; i <= poTrans.MasterFileCount()-1; ++i) {
                    try {
                        psList.add(psDefaultPath+(String) poTrans.getMasterFile(i, "sFileName"));
                        if((int) poTrans.getMasterFile(i, "nClientNo") ==tempRow){
                            if((int) poTrans.getMasterFile(i, "nFileNoxx")==tempComboRow){
                                psimages.add(psDefaultPath+poTrans.getMasterFile(i, "sFileName"));
                            }
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
            if(loadImage(psList.get(fnRow))==true){
                setTableFocus(pos);
                cmbSetFocus(fnRow);
                generateImages();
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
        
        cmbSetFocus(pos);
    }
    
    public void ImagePrevious(){
        pos = pos - 1;
        if(pos < 0){
            ShowMessageFX.Warning(null, pxeModuleName, "No previous image found!");
            pos = 0;
        }
        loadImage(psList.get(pos));
        setTableFocus(pos);
        cmbSetFocus(pos);
        
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
                if(isloaded == false){
                    generateImages();
                }else{
                    loadImages();
                }
                break;
            case DOWN:
                if(isloaded == false){
                    generateImages();
                }else{
                    loadImages();
                }
                break;
        }
    }

    private void Combo_Click(MouseEvent event) {
        if(table.getItems().isEmpty()) return;
        if(poTrans.MasterFileCount() ==0) return;
            if(event.getClickCount() ==1){
            if(isloaded == false){
                generateImages();
            }else{
                loadImages();
            }
        }
    }
    
}
