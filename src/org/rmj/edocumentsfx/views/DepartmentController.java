package org.rmj.edocumentsfx.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.edocx.trans.agentFX.XMDeptSysFile;

/**
 * FXML Controller class
 *
 * @author Jovan Ali
 */
public class DepartmentController implements Initializable {
    
    
    //FXML generated file
    @FXML private AnchorPane acMain;
    @FXML private Label lblTitle;
    @FXML private Button btnExit;
    @FXML private FontAwesomeIconView glyphExit;
    @FXML private AnchorPane anchorField;
    @FXML private CustomTextField txtField50;
    @FXML private CustomTextField txtField51;
    @FXML private TableView<TableModel> table;
    @FXML private Label lblHeader;
    @FXML private CustomTextField txtField01;
    @FXML private TextArea txtField04;
    @FXML private Button btnNew;
    @FXML private Button btnCancel;
    @FXML private Button btnClose;
    @FXML private Button btnSearch;
    @FXML private Button btnBrowse;
    @FXML private Button btnAdd;
    @FXML private Button btnDelete;
    @FXML private CustomTextField txtField00;
    @FXML private CustomTextField txtField02;
    @FXML private TextField txtField05;
    @FXML private TextField txtField06;
    @FXML private CustomTextField txtField03;
    @FXML private Button btnSave;

    //defined public variables
    private static GRider poGRider;
    private String psBranchCd = "";
    private XMDeptSysFile poTrans;
    private int pnRowTable = 0;
    private boolean pbLoaded = false;
    private int pnIndex = -1;
    private int pnEditMode = -1;
    private ObservableList items = FXCollections.observableArrayList();
    private final String pxeModuleName = "Department Entry";
    private Image search = new Image("/org/rmj/edocumentsfx/images/search.png");
    private String psDeptNme = "";
    private String psBarrcode = "";
    private String psDeptIDxx = "";
    private String psFileCode = "";
    
    private TableColumn index00;
    private TableColumn index01;
    private TableColumn index02;
    private TableColumn index03;
    private TableColumn index04;
    private TableColumn index05;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (poGRider == null){
                ShowMessageFX.Warning("GhostRider Application not set..",pxeModuleName, "Please Inform MIS/SEG" );
                System.exit(0);
            }
            
        if (psBranchCd.equals("")) psBranchCd = poGRider.getBranchCode();
        
            poTrans = new XMDeptSysFile(poGRider ,psBranchCd, false);
       
        
        btnAdd.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnDelete.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnNew.setOnAction(this::cmdButton_Click);
        btnSearch.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        
        txtField00.setOnKeyPressed(this::txtField_KeyPressed);
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtArea_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);
        txtField50.setOnKeyPressed(this::txtField_KeyPressed);
        txtField51.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtField00.focusedProperty().addListener(txtField_Focus);
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtArea_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField50.focusedProperty().addListener(txtField_Focus);
        txtField51.focusedProperty().addListener(txtField_Focus);
        
        txtField50.setLeft(new ImageView(search));
        txtField51.setLeft(new ImageView(search));
        txtField00.setLeft(new ImageView(search));
        txtField01.setLeft(new ImageView(search));
        txtField02.setLeft(new ImageView(search));
        txtField03.setLeft(new ImageView(search));
        clearFields();
        
        initGrid();
        initButton(pnEditMode);
        pbLoaded = true;
    }    

    @FXML
    private void Table_Click(MouseEvent event) {
        if(table.getItems().isEmpty()) return;
        pnRowTable = table.getSelectionModel().getSelectedIndex();
        
        if (event.getClickCount() == 2){              
            if (poTrans.getSysFile(pnRowTable, "sFileCode").equals("")) return;
            boolean lbShow = pnEditMode == EditMode.ADDNEW || 
                                pnEditMode == EditMode.UPDATE;
            
                EditMultipleDocsController loMultipleDocs = new EditMultipleDocsController();
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("EditMultipleDocs.fxml"));
            if (lbShow){
                loMultipleDocs.setEditMode(EditMode.UPDATE);
                try {
                    loMultipleDocs.setFileCode(String.valueOf(poTrans.getSysFile(pnRowTable, "sFileCode").toString()));
                    loMultipleDocs.setBarcode(String.valueOf(poTrans.getSysFile(pnRowTable, "sBarrcode")));
                    loMultipleDocs.setB2BPage(String.valueOf(poTrans.getSysFile(pnRowTable, "sB2BPagex").toString()));
                    loMultipleDocs.setNoOfPage(Integer.valueOf(poTrans.getSysFile(pnRowTable, "nNoPagesx").toString()));

                    fxmlLoader.setController(loMultipleDocs);
                    fxmlLoader.load();

                    Parent parent = fxmlLoader.getRoot();
                    Scene scene = new Scene(parent);
                    scene.setFill(new Color(0, 0, 0, 0));

                    Stage nStage = new Stage();
                    nStage.setScene(scene);
                    nStage.initModality(Modality.APPLICATION_MODAL);
                    nStage.initStyle(StageStyle.TRANSPARENT);
                    nStage.showAndWait();

                    if (!loMultipleDocs.isCancelled() && loMultipleDocs.getEditMode()== EditMode.UPDATE){
                        poTrans.setSysFile(pnRowTable, "nNoPagesx", loMultipleDocs.getNoOfPage());
                        poTrans.setSysFile(pnRowTable, "sB2bPagex", String.valueOf(loMultipleDocs.getB2bPage()));
                        loadDetail2Grid();
                    }
                } catch (IOException e) {
                    ShowMessageFX.Error(e.getMessage(), pxeModuleName, "Please inform MIS department.");
                    System.exit(1);
                }
            
            }else{
                loMultipleDocs.setEditMode(EditMode.UNKNOWN);
                try {

                    loMultipleDocs.setFileCode(String.valueOf(poTrans.getSysFile(pnRowTable, "sFileCode").toString()));
                    loMultipleDocs.setBarcode(String.valueOf(poTrans.getSysFile(pnRowTable, "sBarrcode")));
                    loMultipleDocs.setB2BPage(String.valueOf(poTrans.getSysFile(pnRowTable, "sB2BPagex").toString()));
                    loMultipleDocs.setNoOfPage(Integer.valueOf(poTrans.getSysFile(pnRowTable, "nNoPagesx").toString()));

                    fxmlLoader.setController(loMultipleDocs);
                    fxmlLoader.load();

                    Parent parent = fxmlLoader.getRoot();
                    Scene scene = new Scene(parent);
                    scene.setFill(new Color(0, 0, 0, 0));

                    Stage nStage = new Stage();
                    nStage.setScene(scene);
                    nStage.initModality(Modality.APPLICATION_MODAL);
                    nStage.initStyle(StageStyle.TRANSPARENT);
                    nStage.showAndWait();
            } catch (IOException e) {
                ShowMessageFX.Error(e.getMessage(), pxeModuleName, "Please inform MIS department.");
                System.exit(1);
            }
            }
            
        }else if(event.getClickCount() ==1){
            setFileInfo();
        }
    }
    
    public void clearFields(){
        pnEditMode = EditMode.UNKNOWN;
        txtField00.setText("");
        txtField01.setText("");
        txtField02.setText("");
        txtField03.setText("");
        txtField04.setText("");
        txtField05.setText("0");
        txtField06.setText("0");
        txtField50.setText("");
        txtField51.setText("");
        
        pnIndex = -1;
        pnRowTable = -1;
        psDeptNme = "";
        psBarrcode = "";
        psFileCode = "";
        psDeptIDxx = "";
        items.clear();
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId().toLowerCase();
        switch (lsButton){
            case "btnnew":
                if (poTrans.newTransaction() ==true){
                    poTrans.addDetail();
                    clearFields();
                    loadDetail2Grid();
                    txtField00.requestFocus();
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
                    case 0:
                        if (poTrans.searchMaster("sDeptIDxx", "%") ==true) loadParameter(4); break;
                    case 1: /*sDeptIDxx*/
                        if (poTrans.searchMaster("sDeptName", "%") ==true) loadParameter(4); break;
                    case 2:
                        if (poTrans.searchField(pnRowTable,"sFileCode", "%") ==true) loadParameter(3); break;
                    case 3:
                        if (poTrans.searchField(pnRowTable,"sBarrcode", "%") ==true) loadParameter(3);break;
                }
                break;
            case "btnbrowse":
                if(poTrans.searchWithCondition("sDeptName", "%", "") == true){
                    loadTransaction();
                }
                break;
            case "btndelete":
               if (ShowMessageFX.YesNo(null, "Confirm", "Do you want to delete this entry?") == true){
                if (items.size() == 1){
                    poTrans.deleteDetail(pnRowTable);
                    ShowMessageFX.Information(null, pxeModuleName, "Successfully deleted....");
                    poTrans.addDetail();
                    loadDetail2Grid();
                } else{
                    poTrans.deleteDetail(pnRowTable);
                    ShowMessageFX.Information(null, pxeModuleName, "Successfully deleted....");
                    loadDetail2Grid();
                }
               }
               
                break;
            case "btnadd":
                if(table.getItems().isEmpty())return;
                 String lsValue = (String) index01.getCellObservableValue(items.size() -1).getValue();
                 if(!lsValue.equals("")){
                    poTrans.addDetail();
                    loadDetail2Grid();
                 }else{
                     ShowMessageFX.Error(null, pxeModuleName, "Please populate empty row first before adding file!!");
                 }
                break;
            case "btnsave":
                if(psDeptIDxx.equals("")){
                    ShowMessageFX.Warning(null, pxeModuleName, "Empty Department detected!");
                    txtField00.requestFocus();
                    return;
                }
                
                if(poTrans.getSysFile(pnRowTable,"sFileCode").equals("")){
                    ShowMessageFX.Warning(null, pxeModuleName, "Empty File detected!");
                    txtField02.requestFocus();
                    return;
                }
                
                if(poTrans.saveUpdate() ==true){
                    ShowMessageFX.Information(null, pxeModuleName, "Successfully Saved!");
                    clearFields();
                    pnEditMode = poTrans.getEditMode();
                    initButton(pnEditMode);
                }else{
                    ShowMessageFX.Warning(null, pxeModuleName, poTrans.getMessage());
                }
                
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
    
    public void loadParameter(int fnValue){
        switch(fnValue){
            case 1:
                txtField00.setText((String) poTrans.getDepartment().getMaster("sDeptIDxx"));
                txtField01.setText((String) poTrans.getDepartment().getMaster("sDeptName"));
                psDeptIDxx = txtField00.getText();
                psDeptNme = txtField01.getText();
                break;
            case 3:
                txtField02.setText((String) poTrans.getSysFile(pnRowTable, "sFileCode"));
                psFileCode = txtField02.getText();
                txtField03.setText((String) poTrans.getSysFile(pnRowTable, "sBarrcode"));
                psBarrcode = txtField03.getText();
                txtField04.setText((String) poTrans.getSysFile(pnRowTable, "sBriefDsc"));
                txtField05.setText(Integer.toString((int) poTrans.getSysFile(pnRowTable, "nNoCopies")));
                txtField06.setText(Integer.toString((int) poTrans.getSysFile(pnRowTable, "nNoPagesx")));
                loadDetail2Grid();
        }
    }
    
    private void setFileInfo(){
        
        if(poTrans.getSysFile(pnRowTable, "sFileCode").equals("")){
            txtField02.setText("");
            txtField03.setText("");
            txtField04.setText("");
            txtField05.setText("0");
            txtField06.setText("0");
        }else{
            txtField02.setText((String) poTrans.getSysFile(pnRowTable, "sFileCode"));
            txtField03.setText((String) poTrans.getSysFile(pnRowTable, "sBarrcode"));
            txtField04.setText((String) poTrans.getSysFile(pnRowTable, "sBriefDsc"));
            txtField05.setText(Integer.toString((int) poTrans.getSysFile(pnRowTable, "nNoCopies")));
            txtField06.setText(Integer.toString((int) poTrans.getSysFile(pnRowTable, "nNoPagesx")));
        }
    }
    
    public void initButton(int fnValue){
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        
        btnCancel.setVisible(lbShow);
        btnSearch.setVisible(lbShow);
        btnAdd.setVisible(lbShow);
        btnDelete.setVisible(lbShow);
        lblHeader.setVisible(lbShow);
        btnSave.setVisible(lbShow);
        
        txtField50.setDisable(lbShow);
        txtField51.setDisable(lbShow);
                
        btnBrowse.setVisible(!lbShow);
        btnNew.setVisible(!lbShow);
        btnClose.setVisible(!lbShow);
        txtField00.setMouseTransparent(!lbShow);
        txtField01.setMouseTransparent(!lbShow);
        txtField02.setMouseTransparent(!lbShow);
        txtField03.setMouseTransparent(!lbShow);
        txtField04.setMouseTransparent(!lbShow);
        txtField05.setMouseTransparent(!lbShow);
        txtField06.setMouseTransparent(!lbShow);
      
        if (!lbShow) txtField50.requestFocus();
    }
    
    
    
    private void initGrid(){
        index00 = new TableColumn("No");
        index01 = new TableColumn("File Code");
        index02 = new TableColumn("Barcode");
        index03 = new TableColumn("Description");
        index04 = new TableColumn("Page No");
        index05 = new TableColumn("Total");
        
        index00.setSortable(false); index00.setResizable(false);index00.setStyle("-fx-alignment: CENTER");
        index01.setSortable(false); index01.setResizable(false);index01.setStyle("-fx-alignment: CENTER");
        index02.setSortable(false); index02.setResizable(false);index02.setStyle("-fx-alignment: CENTER");
        index03.setSortable(false); index03.setResizable(false);index03.setStyle("-fx-alignment: CENTER");
        index04.setSortable(false); index04.setResizable(false);index04.setStyle("-fx-alignment: CENTER");
        index05.setSortable(false); index05.setResizable(false);index05.setStyle("-fx-alignment: CENTER");
        
        table.getColumns().clear();
        table.getColumns().addAll(index00, index01, index02, index03, index04, index05);
        
        index00.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.ParameterTableModel,String>("index01"));
        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.ParameterTableModel,String>("index02"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.ParameterTableModel,String>("index03"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.ParameterTableModel,String>("index04"));
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.ParameterTableModel,String>("index05"));
        index05.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.ParameterTableModel,String>("index06"));
        

        /*Set data source to table*/
        table.setItems(items);
        
        index00.prefWidthProperty().bind(table.widthProperty().divide(100/10));
        index01.prefWidthProperty().bind(table.widthProperty().divide(100/20));
        index02.prefWidthProperty().bind(table.widthProperty().divide(100/20));
        index03.prefWidthProperty().bind(table.widthProperty().divide(100/20));
        index04.prefWidthProperty().bind(table.widthProperty().divide(100/20));
        index05.prefWidthProperty().bind(table.widthProperty().divide(100/10));
    }
    
    public void setGRider(GRider fsGRider){
        this.poGRider = fsGRider;
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
                    if(txtField.getText().equals("") || txtField.getText().equals("%")){
                       txtField.setText("");
                       poTrans.setMaster("sDeptName", "");
                       poTrans.setMaster("sDeptIDxx", "");
                       psDeptNme = "";
                       psDeptIDxx = "";
                    }else{
                         txtField.setText(psDeptIDxx);
                         txtField01.setText(psDeptNme);
                    }
                    break;
                case 1: 
                     if(txtField.getText().equals("") || txtField.getText().equals("%")){
                       txtField.setText("");
                       poTrans.setMaster("sDeptName", "");
                       poTrans.setMaster("sDeptIDxx", "");
                       psDeptNme = "";
                       psDeptIDxx = "";
                    }else{
                         txtField.setText(psDeptNme);
                         txtField00.setText(psDeptIDxx);
                     }
                     break;
                case 2:/*sDeptIDxx*/
                    if(txtField.getText().equals("") || txtField.getText().equals("%")){
                       txtField.setText("");
                       poTrans.setMaster("sFileCode", "");
                       psFileCode = "";
                    }else
                     txtField.setText(psFileCode); return;
                case 3:/*sDeptIDxx*/
                    if(txtField.getText().equals("") || txtField.getText().equals("%")){
                       txtField.setText("");
                       poTrans.setMaster("sBarrcode", "");
                       psBarrcode = "";
                    }else
                     txtField.setText(psBarrcode); return;
                case 50:
                   if(lsValue.equals("") || lsValue.equals("%"))
                       txtField.setText("");
                       break;
                case 51: 
                    if(lsValue.equals("") || lsValue.equals("%"))
                       txtField.setText("");
                       break;
                case 6:
//                    int nNoPagesx = 0;
//                    try{
//                        nNoPagesx = Integer.parseInt(lsValue);
//                        poTrans.setMaster("nNoPagesx", nNoPagesx);
//                    }catch(NumberFormatException | ClassCastException e){
//                        nNoPagesx = 0;
//                        poTrans.setMaster("nNoPagesx", nNoPagesx);
//                    }
//                    txtField.setText(String.valueOf(poTrans.getMaster("nNoPagesx")));
//                    b2bPage.clear();
//                    for(int lnCtr =0; lnCtr <=(int) nNoPagesx -1; lnCtr++){
//                        b2bPage.add(lnCtr, "");
//                    }
//                    generateB2b();
                    break;
                case 5:
//                    int nNoCopies = 0;
//                    try{
//                        nNoCopies = Integer.parseInt(lsValue);
//                        poTrans.setMaster("nNoCopies", nNoCopies);
//                    }catch(NumberFormatException |ClassCastException e){
//                        nNoCopies = 0;
//                        poTrans.setMaster("nNoCopies", nNoCopies);
//                    }
//                    txtField.setText(String.valueOf(poTrans.getMaster("nNoCopies")));
                    break;
                    
                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Text field with name " + txtField.getId() + " not registered.");
                   return;
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
                case 4: /*sRemarksx*/
                    break;
                    
                default:
            }
        }else{ 
            pnIndex = -1;
            txtField.selectAll();
        }
    };
    
    private void loadTransaction(){
        txtField50.setText((String) poTrans.getMaster("sDeptName"));
        txtField51.setText((String) poTrans.getMaster("sDeptIDxx"));
        txtField00.setText((String) poTrans.getMaster("sDeptIDxx"));
        txtField01.setText((String) poTrans.getMaster("sDeptName"));
        
        loadDetail2Grid();
        pnEditMode = EditMode.READY;
    }
    
    private void loadDetail2Grid(){
    int dataSize = poTrans.SysFileCount();
    int lnCtr;
    String lsValue;
    int fnSize = 0;

    items.clear();
    for(lnCtr = 0; lnCtr <= dataSize -1 ; lnCtr++){
        lsValue = (String) poTrans.getSysFile(lnCtr,"sB2BPagex");
                    String [] arr = lsValue.split("»");
        int lnB2BPagexx=0;
        for(int nCtr = 0; nCtr<= arr.length -1 ; nCtr++){
            lnB2BPagexx=lnB2BPagexx+Integer.parseInt(arr[nCtr]);
        }
        
        items.add(new TableModel(String.valueOf(lnCtr+1),
                String.valueOf(poTrans.getSysFile(lnCtr, "sFileCode")),
                String.valueOf(poTrans.getSysFile(lnCtr, "sBarrcode")),
                String.valueOf(poTrans.getSysFile(lnCtr, "sBriefDsc")),
                String.valueOf(poTrans.getSysFile(lnCtr, "nNoPagesx")),
                String.valueOf(((int) poTrans.getSysFile(lnCtr, "nNoPagesx") *(int) poTrans.getSysFile(lnCtr, "nNoCopies"))+lnB2BPagexx)
                ));
        
        fnSize = fnSize +((int) poTrans.getSysFile(lnCtr, "nNoPagesx") *(int) poTrans.getSysFile(lnCtr, "nNoCopies"))+lnB2BPagexx;
    }
   
    table.requestFocus();
    table.getSelectionModel().select(items.size() -1);
    table.getFocusModel().focus(items.size() -1);
    pnRowTable = table.getSelectionModel().getSelectedIndex();
    setFileInfo();
    }
    
    
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText() + "%";
        if (event.getCode() == ENTER || event.getCode() == F3){
            switch (lnIndex){
                case 0: /*sDeptIDxx*/
                    if(event.getCode() == F3){
                        if (poTrans.searchMaster("sDeptIDxx", lsValue) ==true) loadParameter(1);
                    }else if(event.getCode() == ENTER){
                        if (lsValue.equals("")){
                            ShowMessageFX.Warning(null, pxeModuleName, "Please input text to search!");
                            return;
                        }
                        if (poTrans.searchMaster("sDeptIDxx", lsValue) ==true) loadParameter(1);
                    }
                    break;
                case 1: /*sDeptIDxx*/
                    if(event.getCode() == F3){
                        if (poTrans.searchMaster("sDeptName", lsValue) ==true) loadParameter(1);
                    }else if(event.getCode() == ENTER){
                        if (lsValue.equals("")){
                            ShowMessageFX.Warning(null, pxeModuleName, "Please input text to search!");
                            return;
                        }
                        if (poTrans.searchMaster("sDeptName", lsValue) ==true) loadParameter(1);
                    }
                    break;
                case 2:
                    if(event.getCode() == F3){
                        if (poTrans.searchField(pnRowTable,"sFileCode", lsValue) ==true) loadParameter(3);
                    }else if(event.getCode() == ENTER){
                    if (lsValue.equals("")){
                        ShowMessageFX.Warning(null, pxeModuleName, "Please input text to search!");
                        return;
                    }
                        if (poTrans.searchField(pnRowTable,"sFileCode", lsValue) ==true) loadParameter(3);
                    }
                    break;
                case 3:
                     if(event.getCode() == F3){
                        if (poTrans.searchField(pnRowTable,"sBarrcode", lsValue) ==true) loadParameter(3);
                    }else if(event.getCode() == ENTER){
                    if (lsValue.equals("")){
                        ShowMessageFX.Warning(null, pxeModuleName, "Please input text to search!");
                        return;
                    }
                        if (poTrans.searchField(pnRowTable,"sBarrcode", lsValue) ==true) loadParameter(3);
                    }
                    break;
                case 50: /*sTransNox*/
                    if(event.getCode() == F3){
                        if (poTrans.searchWithCondition("sDeptName", lsValue, "")==true)
                        {
                            loadTransaction();
                        }else{
                            ShowMessageFX.Warning(null, pxeModuleName, poTrans.getMessage());
                            return;
                        }
                    }else if(event.getCode() == ENTER){
                        if (lsValue.equals("")){
                            ShowMessageFX.Warning(null, pxeModuleName, "Please input text to search!");
                        return;
                        }
                        
                        if (poTrans.searchWithCondition("sDeptName", lsValue, "")==true){
                            loadTransaction();
                        }else{
                            ShowMessageFX.Warning(null, pxeModuleName, poTrans.getMessage());
                            return;
                        }
                    }
                    break;
                case 51: /*dTransact*/
                    if(event.getCode() == F3){
                        if (poTrans.searchWithCondition("sDeptIDxx", lsValue, "")==true){
                            loadTransaction();
                        }else{
                            ShowMessageFX.Warning(null, pxeModuleName, poTrans.getMessage());
                            return;
                        }
                    }else if(event.getCode() == ENTER){
                        if (lsValue.equals("")){
                            ShowMessageFX.Warning(null, pxeModuleName, "Please input text to search!");
                            return;
                        }
                        if (poTrans.searchWithCondition("sDeptIDxx", lsValue, "")==true){
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
    
    private void txtArea_KeyPressed(KeyEvent event){
      if (event.getCode() == ENTER || event.getCode() == DOWN || event.getCode() == KeyCode.RIGHT){
            event.consume();
            CommonUtils.SetNextFocus((TextArea)event.getSource());
        }else if (event.getCode() ==KeyCode.UP|| event.getCode() == KeyCode.LEFT){
            event.consume();
            CommonUtils.SetPreviousFocus((TextArea)event.getSource());
        }
    }
    
}
