package org.rmj.edocumentsfx.views;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
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
import org.controlsfx.control.textfield.CustomTextField;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.edocumentsfx.utilities.Utilities;
import org.rmj.edocx.trans.agentFX.XMEDocuments;

public class CRScannerController implements Initializable {
    
    @FXML private CustomTextField txtField00;
    @FXML private TextField txtField02;
    @FXML private TextField txtField03;
    @FXML private Button btnSearch;
    @FXML private ComboBox cmb00;
    @FXML private Button btnScan;
    @FXML private Button btnSave;
    @FXML private TableView<TableModel> table;
    @FXML private AnchorPane acMain;
    @FXML private ImageView imageview;
    @FXML private Button btnExit;
    @FXML private Button btnBack;
    @FXML private Button btnCancel;
    @FXML private Button btnClose;
    @FXML private Button btnBack1;
    @FXML private Button btnDelete;
    @FXML private TextField txtField04;
    @FXML private Label lblTitle;
    @FXML private Button btnImageNext;
    @FXML private Button btnImagePrevious;
    @FXML private Button btnUpdate;
    @FXML private Button btnImageDelete;
    @FXML private Button btnImageMoveDown;
    @FXML private Button btnImageMoveUP;
    @FXML private Label lblImagePreview;
    @FXML private TableColumn index01;
    @FXML private TableColumn index02;
    @FXML private TableColumn index03;
    @FXML private TableColumn index04;
    
    private int pnRowTable = -1;
    private static GRider poGRider;
    private String psDefaultPath = "";
    private final String pxeDefaultDte = java.time.LocalDate.now().minusYears(1).toString();
    private final String pxeDate1Month = java.time.LocalDate.now().minusYears(1).plusMonths(1).toString();
    private String psBranchNm = "";
    private String psBranchCd = "";
    private final String pxeDateFormat = "yyyy-MM-dd";  
    private final String pxeModuleName = "Documents Scanner";
    public String psFileName2 = "";
    private int pnEditMode;
    private int pnIndex = -1;
    private int pnRow = -1;
    private boolean pbLoaded = false;
    private String psOldRec;
    private ObservableList<TableModel> items = FXCollections.observableArrayList();
    private XMEDocuments poTrans;
    private CRDocsController loCrDocs;
    private String psDefaultImage = "";
    private String psNoImage = "";
    private Image search;
    public ArrayList<String> psimages = new ArrayList<String>();
    public ArrayList<String> psList = new ArrayList<String>();
    private String psDateFrom="";
    private String psDateThru="";
    private int pos = 0;
    private boolean isloaded = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
            if (poGRider == null){
                ShowMessageFX.Warning("GhostRider Application not set..", pxeModuleName, "Please inform MIS department.");
                System.exit(0);
            }
            
            if (psFileName2.equals("")) psFileName2 = loCrDocs.getFileName();
            
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
//            generateYear();
            clearFields();
            initButton(pnEditMode);
            pbLoaded = true;
    }
    
    private boolean loadImage(String fsImage){
        Image image = new Image(new File(fsImage).toURI().toString());
        imageview.setImage(image);
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
    
    public void setImageDefault(String fsImage){
        this.psDefaultImage = fsImage;
    
    }
    
    public void setLoaded(Boolean fsloaded){
        this.isloaded = fsloaded;
    }
    
    public void setNoImage(String fsImage){
        this.psNoImage = fsImage;
    }
    
    private String loadDefaultImage(String fsImage){
        Image image = new Image(fsImage);
        imageview.setImage(image);
        double width = image.getWidth();
        double height = image.getHeight();
        
        lblImagePreview.setText("No Image Preview");
        
        Utilities.ImageTransaction(imageview, width, height);
        
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
    
    public void setCrDocsController(CRDocsController loController){
        this.loCrDocs = loController;
    }
    
   
    public void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId().toLowerCase();
        switch (lsButton){
            case "btndelete":
                    if(ShowMessageFX.OkayCancel("Deletion of detail cannot be undo, proceed now?", pxeModuleName, "Do you want to remove this information?") == true){
                        if(table.getItems().size() == 1){
                            poTrans.deleteDetail(pnRowTable);
                            poTrans.addDetail();
                            loadDetail2Grid();
                            break;
                        }              
                        if ((pnRowTable) >= 0) {
                            poTrans.deleteDetail(pnRowTable);
                            loadDetail2Grid();
                            }
                        }
                break;
                
            case "btnsearch":
                if(psBranchCd.equals("")){
                    ShowMessageFX.Error(null, pxeModuleName, "No Branch selected!\n Please select one and try again!");
                    return;
                }
                
                    if (poTrans.getMCAccount(psBranchCd, psDateFrom, psDateThru) == true){
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
//                            poTrans.sample_fileSingle();
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
                loCrDocs.setStage();
                loCrDocs.clearFields();
                loCrDocs.initButton(EditMode.UNKNOWN);
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
                        if (poTrans.saveUpdate() == true) {
                            ShowMessageFX.Information(null, pxeModuleName, "Transaction successfully saved!");
                            initButton(EditMode.UNKNOWN);
                            isloaded = false;
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
                loCrDocs.initButton(EditMode.UNKNOWN);
            case "btnback":
                loCrDocs.setFileName(psFileName2);
                btnBack.getScene().getWindow().hide();
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
                           
//                           if(poTrans.MasterFileCount() <= pos +1){
//                               poTrans.addMasterFile();
//                               
//                               int tempEnryNox = 0;
//                               String tempFileName = "";
//                               tempEnryNox = (int) poTrans.getMasterFile(pos, "nEntryNox");
//                               tempFileName = (String) poTrans.getMasterFile(pos, "sFileName");
//                               
//                               
//                               poTrans.setMasterFile(pos+1, "nEntryNox", tempEnryNox+1);
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
    public ArrayList getFiles(){
        return psimages;
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
                    psDateFrom = lsValue;
                    break;
                case 3:
                    if(psBranchCd.equals("")){
                        ShowMessageFX.Error(null, pxeModuleName, "No Branch selected!\n Please select one and try again!");
                        return;
                    }
                        if (poTrans.getMCAccount(psBranchCd, psDateFrom, lsValue) == true){
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
        txtField02.setText(CommonUtils.xsDateLong(CommonUtils.toDate(pxeDefaultDte)));
        txtField03.setText(CommonUtils.xsDateLong(CommonUtils.toDate(pxeDate1Month)));
        lblImagePreview.setText("No Image Preview");
        psOldRec = "";
        pnRow = -1;
        psDateFrom ="";
        psDateThru = "";
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
       
        table.getColumns().clear();
        table.getColumns().addAll(index01,index02,index03,index04);
       
        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.TableModel,String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.TableModel,String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.TableModel,String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.TableModel,String>("index04"));
        
        /*Set data source to table*/
        table.setItems(items);
       
    }
    
//    public void generateYear(){
//        Calendar currentYear = Calendar.getInstance();
//        int curYear = currentYear.get(Calendar.YEAR);
//        currentYear.add(Calendar.YEAR, -20);
//        int lasYear =  currentYear.get(Calendar.YEAR);
//        int i = 0;
//        int[] year = new int[19];
//        for (year[i] = curYear ; year[i] >= lasYear; year[i]--){
//            cmb00.getItems().addAll(year[i]);
//        }
//    }
 
//    public void updateImage(){
//        File file = new File(poTrans.getDefaultPath());
//        JFileChooser jf = new JFileChooser();
//        TableModel fsImage =  table.getSelectionModel().getSelectedItem();
//        ImageViewPanel imgViewPane = new ImageViewPanel();
//        jf.setName("Choose Image");
//        
//        jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
//        jf.setCurrentDirectory(file);
//        jf.setFileFilter(new javax.swing.filechooser.FileFilter(){
//            @Override
//            public boolean accept(File f) {
//                return (f.getName().startsWith(psFileName.substring(0, psFileName.length() - 8)) && f.getName().endsWith(".jpg"));
//            }
//
//            @Override
//            public String getDescription() {
//                 return ("Recent files");
//            }
//        });
//        
//        jf.setAccessory(imgViewPane);
//        jf.setAcceptAllFileFilterUsed(false);
//        jf.addPropertyChangeListener(imgViewPane);
//        
//        int result = jf.showOpenDialog(null);
//        if(result == JFileChooser.APPROVE_OPTION){
//            filePath =  jf.getSelectedFile();
//        }
//        
//        if (filePath != null){
//            try{
//                BufferedImage bufferedImage = ImageIO.read(filePath);
//                    if(bufferedImage !=null){
//                        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
//                        imageview.setImage(image);
//                        fsImage.setIndex04(filePath.getName());
//                        poTrans.setDetail(pnRow, "sFileName", filePath.getName());
//                        System.out.println(filePath.getName());
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//    
//    }
    
    
    
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
    
    public void setTextFocust(CustomTextField txtField){
        this.txtField00 = txtField;
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
                    if (CommonUtils.isDate(lsValue, pxeDateFormat)){
                            txtField.setText(CommonUtils.xsDateLong(CommonUtils.toDate(lsValue)));
                        } else{
                            ShowMessageFX.Warning("Invalid date entry.", pxeModuleName, "Date format must be yyyy-MM-dd (e.g. 1991-07-07)");
                            txtField.setText(CommonUtils.xsDateLong(CommonUtils.toDate(pxeDefaultDte)));
                        }
                        /*get the value from the class*/
                        txtField.setText(CommonUtils.xsDateLong(CommonUtils.toDate(lsValue)));
                        psDateFrom =lsValue; 
                        return;
                case 3:
                     if (CommonUtils.isDate(lsValue, pxeDateFormat)){
                            txtField.setText(CommonUtils.xsDateLong(CommonUtils.toDate(lsValue)));
                        } else{
                            ShowMessageFX.Warning("Invalid date entry.", pxeModuleName, "Date format must be yyyy-MM-dd (e.g. 1991-07-07)");
                            txtField.setText(CommonUtils.xsDateLong(CommonUtils.toDate(pxeDate1Month)));
                        }
                        /*get the value from the class*/
                        txtField.setText(CommonUtils.xsDateLong(CommonUtils.toDate(lsValue)));
                        psDateThru =lsValue; 
                        return;
            }
            pnIndex = lnIndex;
            txtField.selectAll();
        } else{
            switch (lnIndex){
                case 2: /*dTransact*/
                    try{
                        txtField.setText(CommonUtils.xsDateShort(lsValue));
//                        psDateFrom =CommonUtils.xsDateShort(lsValue);
                    }catch(ParseException e){
                        ShowMessageFX.Error(e.getMessage(), pxeModuleName, null);
                    }
                    txtField.selectAll();
                    break;
                case 3: /*dTransact*/
                    try{
                        txtField.setText(CommonUtils.xsDateShort(lsValue));
//                        psDateThru = CommonUtils.xsDateShort(lsValue);
                    }catch(ParseException e){
                        ShowMessageFX.Error(e.getMessage(), pxeModuleName, null);
                    }
                    txtField.selectAll();
                    break;
                default:
            }
            pnIndex = lnIndex;
            txtField.selectAll();
        }
    };
    
    private void generateImages(){
        psList.clear();
        psimages.clear();
        
        if(poTrans.MasterFileCount() == 0) return;
        pnRow = Integer.valueOf(table.getSelectionModel().getSelectedItem().getIndex01());
                for (int i = 0; i <= poTrans.MasterFileCount()-1; ++i) {
                    try {
                        psList.add(psDefaultPath+(String) poTrans.getMasterFile(i, "sFileName")+".jpg");
                        if((int) poTrans.getMasterFile(i, "nEntryNox") ==pnRow){
                            psimages.add(psDefaultPath+poTrans.getMasterFile(i, "sFileName").toString()+".jpg");
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
    
    
    private void loadImages(){
        int tempRow = 0;
        psList.clear();
        psimages.clear();
        
        if(poTrans.ItemCount()== 0) return;
        
        tempRow = Integer.valueOf(table.getSelectionModel().getSelectedItem().getIndex01());  
        
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

    @FXML
    private void TableClick(MouseEvent event) {
        if(table.getItems().isEmpty()) return;
        pnRowTable = (int)table.getSelectionModel().getFocusedIndex();
            if(event.getClickCount() ==1){
                if(isloaded == true){
                    generateImages();
                }else{
                    loadImages();
                }
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
                if(isloaded == true){
                    generateImages();
                }else{
                    loadImages();
                }
                break;
            case DOWN:
                if(isloaded == true){
                    generateImages();
                }else{
                    loadImages();
                }
                break;
        }
    }
    

}
