package org.rmj.edocumentsfx.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.textfield.CustomTextField;
import org.json.simple.JSONObject;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.edocx.trans.agentFX.XMEDocuments;

public class CRDocsController implements Initializable {
    @FXML private Button btnExit;
    @FXML private FontAwesomeIconView glyphExit;
    @FXML private AnchorPane anchorField;
    @FXML private Label lblHeader;
    @FXML private TextField txtField01;
    @FXML private AnchorPane acMain;
    @FXML private TextField txtField02;
    @FXML private TextField txtField03;
    @FXML private CustomTextField txtField04;
    @FXML private TextArea txtField06;
    @FXML private CustomTextField txtField50;
    @FXML private CustomTextField txtField51;
    @FXML private Button btnNew;
    @FXML private Button btnCancel;
    @FXML private Button btnClose;
    @FXML private Button btnSearch;
    @FXML private Button btnBrowse;
    @FXML private CustomTextField txtField05;
    @FXML private Button btnNext;
    @FXML private Button btnDetail;
    @FXML private Label lblTitle;
    @FXML private TableView table;
    @FXML private CustomTextField txtField07;
    @FXML private CustomTextField txtField08;
    @FXML private TextField txtField09;
    @FXML private TextArea txtField10;
    @FXML private Button btnAdd;
    @FXML private Button btnDelete;
    @FXML private TableColumn index00;
    @FXML private TableColumn index01;
    @FXML private TableColumn index02;
    @FXML private TableView<ParameterTableModel> table1;
    @FXML private TextField txtField11;
    
    private Date pxeCurrentDate;
    private String psDefaultPath= "";
    private int pnEditMode = -1;
    private Integer pnIndex = -1;
    private XMEDocuments poTrans;
    private static GRider poGRider;
    private String psBranchCd = "";
    private String psFileName = "";
    private String psFileCode = "";
    private String psFileName2 = "";
    private Stage stage = null;
    private boolean pbLoaded = false;
    private String lsValueFinal;
    private String psDeptNme = "";
    private String psEmloyee = "";
    private String psBarcode = "";
    private int pnNoPagesx = 0;
    private int pnNoCopies = 0;
    private String psEmployNo = "";
    private String psDeptCode= "";
    private String psB2bPagex= "";
    private String psBriefDsc = "";
    private final String pxeDateFormat = "yyyy-MM-dd";
    private final String pxeModuleName = "Documents Scan";
    private Image search = new Image("/org/rmj/edocumentsfx/images/search.png");
    private String psDefaultImage = "org/rmj/edocumentsfx/images/default.png";
    private String psNoImage = "org/rmj/edocumentsfx/images/noimageavailable2.png";
    
    private ObservableList items = FXCollections.observableArrayList();
    private ObservableList b2bPage = FXCollections.observableArrayList();
    TableColumn index04 = new TableColumn("Pages No");
    TableColumn index05 = new TableColumn("Is Back 2 Back");
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
            if (poGRider == null){
                ShowMessageFX.Warning("GhostRider Application not set..",pxeModuleName, "Please Inform MIS/SEG" );
                System.exit(0);
            }
            
            if (psBranchCd.equals("")) psBranchCd = poGRider.getBranchCode();
            
          
                poTrans = new XMEDocuments(poGRider ,psBranchCd, false);
            
            pxeCurrentDate = poGRider.getServerDate();
            if (psDefaultPath.equals("")) psDefaultPath = poTrans.getDefaultPath();
            
            btnCancel.setOnAction(this::cmdButton_Click);
            btnSearch.setOnAction(this::cmdButton_Click);
            btnNext.setOnAction(this::cmdButton_Click);
            btnNew.setOnAction(this::cmdButton_Click);
            btnClose.setOnAction(this::cmdButton_Click);
            btnExit.setOnAction(this::cmdButton_Click);
            btnBrowse.setOnAction(this::cmdButton_Click);
            btnDetail.setOnAction(this::cmdButton_Click);
            btnAdd.setOnAction(this::cmdButton_Click);
            btnDelete.setOnAction(this::cmdButton_Click);
            
            txtField01.focusedProperty().addListener(txtField_Focus);
            txtField02.focusedProperty().addListener(txtField_Focus);
            txtField03.focusedProperty().addListener(txtField_Focus);
            txtField04.focusedProperty().addListener(txtField_Focus);
            txtField05.focusedProperty().addListener(txtField_Focus);
            txtField06.focusedProperty().addListener(txtArea_Focus);
            txtField07.focusedProperty().addListener(txtField_Focus);
            txtField08.focusedProperty().addListener(txtField_Focus);
            txtField09.focusedProperty().addListener(txtField_Focus);
            txtField11.focusedProperty().addListener(txtField_Focus);
            
            /*Add keypress event for field with search*/
            txtField01.setOnKeyPressed(this::txtField_KeyPressed);
            txtField02.setOnKeyPressed(this::txtField_KeyPressed);
            txtField03.setOnKeyPressed(this::txtField_KeyPressed);
            txtField04.setOnKeyPressed(this::txtField_KeyPressed);
            txtField05.setOnKeyPressed(this::txtField_KeyPressed);
            txtField06.setOnKeyPressed(this::txtArea_KeyPressed);
            txtField07.setOnKeyPressed(this::txtField_KeyPressed);
            txtField08.setOnKeyPressed(this::txtField_KeyPressed);
            txtField09.setOnKeyPressed(this::txtField_KeyPressed);
            txtField10.setOnKeyPressed(this::txtArea_KeyPressed);
            txtField11.setOnKeyPressed(this::txtField_KeyPressed);
            
            txtField50.setOnKeyPressed(this::txtField_KeyPressed);
            txtField51.setOnKeyPressed(this::txtField_KeyPressed);
            
            txtField08.setMouseTransparent(true);
            txtField09.setMouseTransparent(true);
            txtField10.setMouseTransparent(true);
            
            txtField50.setLeft(new ImageView(search));
            txtField51.setLeft(new ImageView(search));
            txtField04.setLeft(new ImageView(search));
            txtField05.setLeft(new ImageView(search));
            txtField07.setLeft(new ImageView(search));
            txtField08.setLeft(new ImageView(search));
            clearFields();
            
            initGrid();
            initButton(pnEditMode);
            txtField06.setMouseTransparent(true);
           
            pbLoaded = true;
    }
    
    public void setFocus(TextField fsTxtField){
         Platform.runLater(new Runnable() {
             
            @Override
            public void run() {
                fsTxtField.requestFocus();
            }
            });
    }

    public void initButton(int fnValue){
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        
        btnCancel.setVisible(lbShow);
        btnSearch.setVisible(lbShow);
        btnNext.setVisible(lbShow);
//        btnAdd.setVisible(lbShow);
//        btnDelete.setVisible(lbShow);
        lblHeader.setVisible(lbShow);
        
        
        txtField50.setDisable(lbShow);
        txtField51.setDisable(lbShow);
                
        btnBrowse.setVisible(!lbShow);
        btnDetail.setVisible(!lbShow);
        btnNew.setVisible(!lbShow);
        btnClose.setVisible(!lbShow);
        txtField01.setMouseTransparent(lbShow);
        txtField03.setMouseTransparent(lbShow);
        txtField02.setMouseTransparent(!lbShow);
        txtField04.setMouseTransparent(!lbShow);
        txtField05.setMouseTransparent(!lbShow);
        txtField06.setMouseTransparent(!lbShow);
        txtField07.setMouseTransparent(!lbShow);
        txtField09.setMouseTransparent(!lbShow);
        txtField10.setMouseTransparent(!lbShow);
        txtField11.setMouseTransparent(!lbShow);
      
        if (!lbShow) txtField50.requestFocus();
    }
    
    public void loadDetail(){
        items.clear();
        String lsValue = (String) poTrans.getFileCode().getMaster("sB2BPagex");
        String [] arr = lsValue.split("»");
        int lnIterator = 1;
        for(int lnCtr = 0; lnCtr< arr.length ; lnCtr++){
            items.addAll(new ParameterTableModel("Page "+ lnIterator,arr[lnCtr]));
            lnIterator= lnIterator +1;
        }
        table.setItems(items);
        
    }
    
     public void generateB2b(){
        int lnCtr;
        items.clear();
        for(lnCtr =0; lnCtr <=b2bPage.size() -1; lnCtr++){
            items.add(new ParameterTableModel("Page " + String.valueOf(lnCtr+1), ""));
        }
        table.setItems(items);
    }
    
    public void saveb2b2(){
        lsValueFinal = "";
        ParameterTableModel instance;
        for(int lnCtr = 0; lnCtr <=items.size() -1; lnCtr++){
            instance = (ParameterTableModel) items.get(lnCtr);
            String lsValue= String.valueOf(instance.getIndex02().isSelected());
            if(lsValue.equals("true")){
                lsValue = "1";
            }else{
                lsValue = "0";
            }
            lsValueFinal += lsValue+"»";
            
        }
        poTrans.setMaster("sB2BPagex", lsValueFinal.substring(0, lsValueFinal.length()-1).replaceAll("\\s", ""));
    }
    
    private void initGrid(){
        index04.setSortable(false); index04.setResizable(false);index04.setStyle("-fx-alignment: CENTER");
        index05.setSortable(false); index05.setResizable(false);index05.setStyle("-fx-alignment: CENTER");
       
        table1.getColumns().clear();
        table1.getColumns().addAll(index04, index05);
        
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.ParameterTableModel,String>("index01"));
        index05.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.ParameterTableModel,CheckBox>("index02"));

        /*Set data source to table*/
        table1.setItems(items);
        
        index04.prefWidthProperty().bind(table1.widthProperty().divide(100/50));
        index05.prefWidthProperty().bind(table1.widthProperty().divide(100/50));
    }
//    public int getSize(){
////        pnFileCount = (int) poTrans.getModuleCode().getMaster("nNoPagesx") * (int) poTrans.getModuleCode().getMaster("nNoCopies");;
//        pnFileCount = 1 * 1;
//        
//        return pnFileCount;
//    }
    
    public void clearFields(){
        pnEditMode = EditMode.UNKNOWN;
        txtField01.setText("");
        txtField02.setText(CommonUtils.xsDateLong(pxeCurrentDate));
        txtField03.setText("");
        txtField04.setText("");
        txtField05.setText("");
        txtField06.setText("");
        txtField07.setText("");
        txtField08.setText("");
        txtField09.setText("0");
        txtField10.setText("");
        txtField11.setText("0");
        txtField50.setText("");
        txtField51.setText("");
        
        lsValueFinal="";
        pnIndex = -1;
        
        psFileName2 = "";
        b2bPage.clear();
        items.clear();
        setStage();
    }
    
    private void txtArea_KeyPressed(KeyEvent event){
      if (event.getCode() == ENTER || event.getCode() == DOWN || event.getCode() == KeyCode.RIGHT){
            event.consume();
            CommonUtils.SetNextFocus((TextArea)event.getSource());
        }else if (event.getCode() ==KeyCode.UP|| event.getCode() == KeyCode.LEFT){
            event.consume();
            CommonUtils.SetPreviousFocus((TextArea)event.getSource());
        }
    }
   
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        if (event.getCode() == ENTER|| event.getCode() == F3){
            switch (lnIndex){
                case 4: /*sDeptIDxx*/
                    if(event.getCode() == F3){
                        if (poTrans.searchMaster("sDeptName", lsValue.equals("")? "%":lsValue) ==true) loadParameter(4);
                    }else if(event.getCode() == ENTER){
                        if (lsValue.equals("")){
                            ShowMessageFX.Warning(null, pxeModuleName, "Please input text to search!");
                            return;
                        }
                        if (poTrans.searchMaster("sDeptName", lsValue) ==true) loadParameter(4);
                    }
                    break;
                case 5: /*sEmployID*/
                    if(event.getCode() == F3){
                        if (poTrans.searchMaster("sCompnyNm", lsValue.equals("")? "%":lsValue) ==true) loadParameter(5);
                    }else if(event.getCode() == ENTER){
                        if (lsValue.equals("")){
                            ShowMessageFX.Warning(null, pxeModuleName, "Please input text to search!");
                            return;
                        }
                        if (poTrans.searchMaster("sCompnyNm", lsValue) ==true) loadParameter(5);
                    }
                    break;
                case 7:
                    if(event.getCode() == F3){
                        if(poTrans.searchMaster("sFileCode", lsValue.equals("")? "%":lsValue) == true) loadParameter(7);
                    }else if(event.getCode() == ENTER){
                        if (lsValue.equals("")){
                            ShowMessageFX.Warning(null, pxeModuleName, "Please input text to search!");
                            return;
                        }
                        if(poTrans.searchMaster("sFileCode", lsValue) == true) loadParameter(7);
                    }
                    break;
                case 50: /*sTransNox*/
                    if(event.getCode() == ENTER){
                        if (lsValue.equals("")){
                            ShowMessageFX.Warning(null, pxeModuleName, "Please input text to search!");
                            return;
                        }
                         if (poTrans.searchWithCondition("sTransNox", lsValue, "a.cTransTat = '0'")==true) loadTransaction();
                    }else if(event.getCode() == F3){
                        if (poTrans.searchWithCondition("sTransNox", lsValue.equals("")? "%":lsValue, "a.cTransTat = '0'")==true){
                            loadTransaction();
                        }else{
                            ShowMessageFX.Warning(null, pxeModuleName, poTrans.getMessage());
                            return;
                        }
                    }
                    break;
                case 51: /*dTransact*/
                    if(event.getCode() == ENTER){
                        if (lsValue.equals("")){
                            ShowMessageFX.Warning(null, pxeModuleName, "Please input text to search!");
                            return;
                        }
                         if (poTrans.searchWithCondition("dTransact", lsValue, "a.cTransTat = '0'")==true) loadTransaction();
                    }else if(event.getCode() == F3){
                        if (poTrans.searchWithCondition("dTransact", lsValue.equals("")? "%": lsValue, "a.cTransTat = '0'")==true){
                            loadTransaction();
                        }else{
                            ShowMessageFX.Warning(null, pxeModuleName, poTrans.getMessage());
                            return;
                        }
                    }
                    break;
                }
            }
        switch (event.getCode()){
        case ENTER:
        case DOWN:
        case RIGHT:
            CommonUtils.SetNextFocus(txtField);
            break;
        case UP:
        case LEFT:
            CommonUtils.SetPreviousFocus(txtField);
            break;
        }
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId().toLowerCase();
        JSONObject loResult = null;
        switch (lsButton){
            case "btnnew":
                if (poTrans.newTransaction() ==true){
                    clearFields();
                    loadParameter(1);
                    loadParameter(3);
                    txtField50.setText("");
                    txtField51.setText("");
                    txtField02.requestFocus();
                    if(!psFileCode.equals("")){
                      loadSavedVariable();
                    }
                    pnEditMode = poTrans.getEditMode();
                    initButton(pnEditMode);
                }else{
                    ShowMessageFX.Error(null, pxeModuleName, poTrans.getMessage());
                }
                break;
            case "btnclose":
            case "btnexit":
                unloadForm();
                return;
                
            case "btncancel": 
                if(ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to disregard changes?") == true){
                    clearFields();
                    initButton(pnEditMode);
                    break;
                } else
                    return;
                
            case "btnsearch":
                switch(pnIndex){
                    case 4: /*sDeptIDxx*/
                        if (poTrans.searchMaster("sDeptName", "%") ==true) loadParameter(4); break;
                    case 5: /*sEmployID*/
                        if (poTrans.searchMaster("sCompnyNm", "%") ==true) loadParameter(5); break;
                    case 6: /*sModuleCd*/
                        if (poTrans.searchMaster("sModuleDs", "%") ==true) loadParameter(6); break;
                    case 7: /*FileCode*/
                        if(poTrans.searchMaster("sFileCode", "%") == true) loadParameter(7); break;
                }
                break;
                
            case "btnnext":
                if(psDeptNme.equals("")){
                    ShowMessageFX.Error(null, pxeModuleName, "Please select Department Name to continue...");
                    txtField04.requestFocus();
                    return;
                }
                 
                  if(psEmloyee.equals("")){
                    ShowMessageFX.Error(null, pxeModuleName, "Please select Employee Name to continue...");
                    txtField05.requestFocus();
                    return;
                }
                 if(psFileName.equals("")){
                    ShowMessageFX.Error(null, pxeModuleName, "Please select documetns to scan to continue...");
                    txtField07.requestFocus();
                    return;
                 }else{
                    saveb2b2();
                }
                
                if(psFileName2 != ""){
                  if(!psFileName.equals(psFileName2)){
                       if(ShowMessageFX.YesNo("By Proceeding all changes made will be disregard!", pxeModuleName, "Change of documents detected!")== true){
                            if(poTrans.MasterFileCount()!= 0){
                            if(ShowMessageFX.YesNo("All scan images will be deleted. Continue?", pxeModuleName, "Scanned Docs detected!")==true){
                                if(poTrans.deleteAllImage()==true){    
                                    ShowMessageFX.Information(null, pxeModuleName, "All images successfully deleted!");
                                }
                            }
                            }   
                           loadCRScanner();
                           break;
                       }else{
                           return;
                       }
                   }
                }
                
                if (stage == null) {
                    if(poTrans.MasterFileCount()!= 0){
                        if(ShowMessageFX.YesNo("All scan images will be deleted. Continue?", pxeModuleName, "Scanned Docs detected!")==true){
                            if(poTrans.deleteAllImage()==true){    
                                ShowMessageFX.Information(null, pxeModuleName, "All images successfully deleted!");
                            }
                        }
                        } 
                    loadCRScanner();
                } else{
                  stage.showAndWait();
                }  
                break;
            case "btnbrowse":
                if(poTrans.searchWithCondition("sTransNox", "%", "") == true){
                    loadTransaction();
                    setStage();
                }
                break;
            case "btndetail":
                if (stage != null) {
                    stage.showAndWait();
                    break;
                }
                
                if (pnEditMode == EditMode.READY){
                    loadCRScanner();
                }else{
                    ShowMessageFX.Information(null, pxeModuleName, "Please browse a record first before proceeding...");
                }
                break;
//            case "btndelete":
//               if (ShowMessageFX.YesNo(null, "Confirm", "Do you want to delete this entry?") == true){
//                if (items.size() == 1){
//                    poTrans.deleteFile(pnRowTable);
//                    ShowMessageFX.Information(null, pxeModuleName, "Successfully deleted....");
//                    poTrans.addFile();
//                    loadDetail2Grid();
//                } else{
//                    poTrans.deleteFile(pnRowTable);
//                    ShowMessageFX.Information(null, pxeModuleName, "Successfully deleted....");
//                    loadDetail2Grid();
//                }
//               }
//               
//                break;
//            case "btnadd":
//                if(table.getItems().isEmpty()){return;}
//                 String lsValue = (String) index01.getCellObservableValue(items.size() -1).getValue();
//                 if(!lsValue.equals("")){
//                    poTrans.addFile();
//                 }else{
//                     ShowMessageFX.Error(null, pxeModuleName, "Please populate empty row first before adding file!!");
//                 }
//                 loadDetail2Grid();
//                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
    }
    
    private void loadCRScanner(){
        CRScannerController loController = new CRScannerController();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("CRScanner.fxml"));

        fxmlLoader.setController(loController);
        loController.setGRider(poGRider);
        loController.setClass(poTrans);
        loController.setEditMode(pnEditMode);
        loController.setFilePath(psDefaultPath);
        loController.setImageDefault(psDefaultImage);
        loController.setNoImage(psNoImage);
        loController.setCrDocsController(this);
        
        try {
            fxmlLoader.load();
            Parent parent = fxmlLoader.getRoot();
            Scene scene = new Scene(parent);
            scene.setFill(new Color(0, 0, 0, 0));
            
            if (pnEditMode != EditMode.ADDNEW){
                loController.loadDetail2Grid();
                loController.setLoaded(false);
                loController.loadTransaction();
            }
            
            stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            //height location
            stage.setY(110);
            //width location
            stage.setX(210);

            stage.initStyle(StageStyle.TRANSPARENT);
            stage.showAndWait();
            
        } catch (IOException ex) {
            Logger.getLogger(CRDocsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setEditMode(int fnEditmode){
        this.pnEditMode = fnEditmode;
    }
    
    public void setStage(){
        this.stage = null;
    }
    
    public void setFileName(String fsFileName){
        this.psFileName2 = fsFileName;
    }
    
    private void unloadForm(){
        psB2bPagex = "";
        psBarcode = "";
        psBranchCd = "";
        psBriefDsc = "";
        psDeptCode = "";
        psDeptNme = "";
        psEmloyee = "";
        psEmployNo = "";
        psFileCode = "";
        psFileName = "";
        psFileName2 = "";
        acMain.getChildren().clear();
        acMain.setStyle("-fx-border-color: transparent");
    }
    
    public String getFileName(){
        return psFileName;
    }
    
    
    public void loadParameter(int fnValue){
        switch(fnValue){
            case 1:
                txtField01.setText((String) poTrans.getMaster("sTransNox").toString().substring(1, 12).replaceFirst("(\\w{5})(\\w{6})", "$1-$2"));
                break;
            case 3:
                txtField03.setText((String) poGRider.getBranchName());
                break;
            case 4:
                txtField04.setText((String) poTrans.getDepartment().getMaster("sDeptName"));
                psDeptNme = txtField04.getText();
                psDeptCode = (String) poTrans.getDepartment().getMaster("sDeptCode");
                break;
            case 5:
                txtField05.setText((String) poTrans.getEmployee().getMaster("sCompnyNm"));
                psEmloyee = txtField05.getText();
                psEmployNo = (String) poTrans.getEmployee().getMaster("sEmployNo");
                break;
            case 7:
                txtField07.setText((String) poTrans.getFileCode().getMaster("sFileCode"));
                psFileName = txtField07.getText();
                psFileCode = (String) poTrans.getFileCode().getMaster("sFileCode");
                txtField08.setText((String) poTrans.getFileCode().getMaster("sBarrcode"));
                psBarcode = txtField08.getText();
                txtField09.setText(Integer.toString((int) poTrans.getFileCode().getMaster("nNoPagesx")));
                pnNoPagesx = Integer.valueOf(txtField09.getText());
                txtField10.setText((String) poTrans.getFileCode().getMaster("sBriefDsc"));
                psBriefDsc= txtField10.getText();
                txtField11.setText(Integer.toString((int) poTrans.getFileCode().getMaster("nNoCopies")) );
                pnNoCopies = Integer.valueOf(txtField11.getText());
                loadDetail();
                break;
        }
    }
    
    public void loadSavedVariable(){
        txtField07.setText(psFileName);
        poTrans.setEDocSysFile(psFileCode);
        txtField08.setText(psBarcode);
        txtField09.setText(String.valueOf(pnNoPagesx));
        txtField10.setText(psBriefDsc);
        txtField11.setText(String.valueOf(pnNoCopies));
        txtField04.setText(psDeptNme);
        txtField05.setText(psEmloyee);
        
        items.clear();
        String lsValue = String.valueOf(psB2bPagex);
        String [] arr = lsValue.split("»");
        int lnIterator = 1;
        for(int lnCtr = 0; lnCtr< arr.length ; lnCtr++){
            items.addAll(new ParameterTableModel("Page "+ lnIterator,arr[lnCtr]));
            lnIterator= lnIterator +1;
        }
        table.setItems(items);
        
    }
    
    private void loadTransaction(){
        txtField50.setText((String) poTrans.getMaster("sTransNox"));
        txtField51.setText(CommonUtils.xsDateMedium((Date) poTrans.getMaster("dTransact")));
        txtField01.setText((String) poTrans.getMaster("sTransNox"));
        txtField02.setText(CommonUtils.xsDateMedium((Date) poTrans.getMaster("dTransact")));
        txtField03.setText((String) poGRider.getBranchName());
        txtField04.setText((String) poTrans.getDepartment().getMaster("sDeptName"));
        txtField05.setText((String) poTrans.getEmployee().getMaster("sCompnyNm")); 
        txtField06.setText((String) poTrans.getMaster("sRemarksx"));
        
        txtField07.setText((String) poTrans.getFileCode().getMaster("sFileCode"));
        txtField08.setText((String) poTrans.getFileCode().getMaster("sBarrcode"));
        txtField09.setText(Integer.toString((int) poTrans.getFileCode().getMaster("nNoPagesx")));
        txtField10.setText((String) poTrans.getFileCode().getMaster("sBriefDsc"));
        txtField11.setText(Integer.toString((int) poTrans.getFileCode().getMaster("nNoCopies")) );
        
        loadDetail();
        pnEditMode = EditMode.READY;
    }
    
    public void setGRider(GRider foGRider){this.poGRider = foGRider;}
    
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        if (!pbLoaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
            
        if(!nv){ /*Lost Focus*/      
            switch (lnIndex){
                case 0:
                case 1: /*sTransNox*/
                case 7:
                case 8:
                case 3: /*sBranchCd*/
                    break;
                case 2: /*dTransact*/
                     if (CommonUtils.isDate(lsValue, pxeDateFormat)){
                        poTrans.setMaster("dTransact", CommonUtils.toDate(lsValue));
                    } else{
                        ShowMessageFX.Warning("Invalid date entry.", pxeModuleName, "Date format must be yyyy-MM-dd (e.g. 1991-07-07)");
                        poTrans.setMaster("dTransact", pxeCurrentDate);
                    }
                    /*get the value from the class*/
                    txtField.setText(CommonUtils.xsDateLong((Date)poTrans.getMaster("dTransact")));
                    return;
                case 4:/*sDeptIDxx*/
                    if(txtField.getText().equals("") || txtField.getText().equals("%")){
                       txtField.setText("");
                       poTrans.setMaster(lnIndex, "");
                       psDeptNme = "";
                    }else
                     txtField.setText(psDeptNme); return;
                case 5:/*sEmployID*/
                    if(txtField.getText().equals("") || txtField.getText().equals("%")){
                        txtField.setText("");
                        poTrans.setMaster(lnIndex, "");
                        psEmloyee = "";
                     }else
                      txtField.setText(psEmloyee); return;
                case 50:
                   if(lsValue.equals("") || lsValue.equals("%"))
                       txtField.setText("");
                       break;
                case 51: 
                    if(lsValue.equals("") || lsValue.equals("%"))
                       txtField.setText("");
                       break;
                case 9:
                    int nNoPagesx = 0;
                    try{
                        nNoPagesx = Integer.parseInt(lsValue);
                        poTrans.setMaster("nNoPagesx", nNoPagesx);
                    }catch(NumberFormatException | ClassCastException e){
                        nNoPagesx = 0;
                        poTrans.setMaster("nNoPagesx", nNoPagesx);
                    }
                    txtField.setText(String.valueOf(poTrans.getMaster("nNoPagesx")));
                    b2bPage.clear();
                    for(int lnCtr =0; lnCtr <=(int) nNoPagesx -1; lnCtr++){
                        b2bPage.add(lnCtr, "");
                    }
                    generateB2b();
                    break;
                case 11:
                    int nNoCopies = 0;
                    try{
                        nNoCopies = Integer.parseInt(lsValue);
                        poTrans.setMaster("nNoCopies", nNoCopies);
                    }catch(NumberFormatException |ClassCastException e){
                        nNoCopies = 0;
                        poTrans.setMaster("nNoCopies", nNoCopies);
                    }
                    txtField.setText(String.valueOf(poTrans.getMaster("nNoCopies")));
                    break;
                    
                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Text field with name " + txtField.getId() + " not registered.");
                   return;
            }
            pnIndex = lnIndex;
            txtField.selectAll();
        } else{
            switch (lnIndex){
                case 2: /*dTransact*/
                    try{
                        txtField.setText(CommonUtils.xsDateShort(lsValue));
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
    
    final ChangeListener<? super Boolean> txtArea_Focus = (o,ov,nv)->{
        if (!pbLoaded) return;
        
        TextArea txtField = (TextArea)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
        
        if(!nv){ /*Lost Focus*/            
            switch (lnIndex){
                case 6: /*sRemarksx*/
                    if(!lsValue.equals("")){
                        if (lsValue.length() > 256) lsValue = lsValue.substring(0, 256);
                    }   
                    poTrans.setMaster("sRemarksx", CommonUtils.TitleCase(lsValue));
                    txtField.setText((String)poTrans.getMaster("sRemarksx"));
                    break;
                case 10:
                    break;
                    
                default:
            }
        }else{ 
            pnIndex = -1;
            txtField.selectAll();
        }
    };

    @FXML
    private void Table_Click(MouseEvent event) {
    }
    
    
    
}
