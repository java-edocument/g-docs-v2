
package org.rmj.edocumentsfx.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.textfield.CustomTextField;
import org.json.simple.JSONObject;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.edocumentsfx.utilities.qsParameter;
import org.rmj.parameters.agent.XMModule;

public class ModuleController implements Initializable {

    @FXML private StackPane acMain;
    @FXML private Button btnExit;
    @FXML private FontAwesomeIconView glyphExit;
    @FXML private Button btnNew;
    @FXML private Button btnUpdate;
    @FXML private Button btnBrowse;
    @FXML private Button btnClose;
    @FXML private Button btnActivate;
    @FXML private Button btnCancel;
    @FXML private Button btnSave;
    @FXML private Button btnSearch;
    @FXML private CustomTextField txtField00;
    @FXML private CustomTextField txtField01;
    @FXML private CheckBox chk00;
    @FXML private TextField txtField02;
    @FXML private TextField txtField03;
    
    private final String pxeModuleName = "Module";
    private GRider poGRider;
    private XMModule poRecord;
    private int pnIndex = -1;
    private static Image search = new Image("org/rmj/edocumentsfx/images/search.png");
    private int pnEditMode;
    private boolean pbLoaded = false;
    private String psOldRec;
    private String psBranchCD="";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (poGRider == null){
            ShowMessageFX.Warning("GhostRider Application not set..", pxeModuleName, "Please inform MIS department.");
            System.exit(0);
        }
        if (psBranchCD.equals("")) psBranchCD = poGRider.getBranchCode();
        
        poRecord = new XMModule(poGRider, psBranchCD, false);
        
        btnActivate.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnNew.setOnAction(this::cmdButton_Click);
        btnSearch.setOnAction(this::cmdButton_Click);        
        btnSave.setOnAction(this::cmdButton_Click);
        btnUpdate.setOnAction(this::cmdButton_Click);
        
        txtField00.focusedProperty().addListener(txtField_Focus);
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        
        txtField00.setOnKeyPressed(this::txtField_KeyPressed);
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtField00.setLeft(new ImageView(search));
        txtField01.setLeft(new ImageView(search));
        
        txtField02.setMouseTransparent(true);
        clearFields();
        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
        
        pbLoaded = true;
    }

    public void cmdButton_Click(ActionEvent event) { 
        String lsButton  = ((Button)event.getSource()).getId().toLowerCase();
        String lsValue = "";
       
        switch(lsButton){   
            case "btnnew"://New 
                if (poRecord.newRecord()){
                    clearFields();
                    loadTransaction();
                    pnEditMode = poRecord.getEditMode();
                }
                break;
            case "btnupdate"://Update
                if (poRecord.getMaster(1) != null && !txtField02.getText().equals("")){
                    if (poRecord.updateRecord()){
                        pnEditMode = poRecord.getEditMode();
                    }
                }else 
                        ShowMessageFX.Warning(null ,pxeModuleName, "Please select a record to update!");
               break;
               
            case "btnactivate"://Activate 
                if (poRecord.getMaster(1) != null && !txtField02.getText().equals("")){
                    if (btnActivate.getText().equals("Activate")){
                        if (poRecord.activateRecord(poRecord.getMaster(1).toString())){
                            openRecord(psOldRec);
                            loadTransaction();
                            ShowMessageFX.Information(null, pxeModuleName, "Record Activated Successfully.");
                        }
                    } else{
                        if (poRecord.deactivateRecord(poRecord.getMaster(1).toString())){
                            openRecord(psOldRec);
                            loadTransaction();
                            ShowMessageFX.Information(null, pxeModuleName, "Record Deactivated Successfully.");
                        }    
                    }
                }else
                ShowMessageFX.Warning(null ,pxeModuleName, "Please select a record to activate!");
                break;

            case "btnbrowse"://Browse 
                JSONObject loResult = null;
                    switch (pnIndex) {
                        case 0:
                            lsValue = txtField00.getText();
                            loResult = qsParameter.getModule(poGRider, lsValue == null ? "" : lsValue, 0);
                            if (loResult != null && !loResult.isEmpty()){
                                openRecord(loResult.get("sModuleCd").toString());
                                loadTransaction();
                            }
                            break;
                        case 1:
                            lsValue = txtField01.getText();
                            loResult = qsParameter.getModule(poGRider, lsValue == null ? "" : lsValue, 1);
                            if (loResult != null && !loResult.isEmpty()){
                                openRecord(loResult.get("sModuleCd").toString());
                                loadTransaction();
                            }
                            break;
                        default:
                            loResult = qsParameter.getModule(poGRider, "%", 1);
                            if (loResult != null && !loResult.isEmpty()){
                                openRecord(loResult.get("sModuleCd").toString());
                                loadTransaction();
                            }
                            break;
                    }
                    break;

            case "btnclose"://Close
            case "btnexit":
                unloadForm();
                return;
                
            case "btnsave"://Save 
                if (poRecord.saveRecord()==true){
                    openRecord(psOldRec);
                    ShowMessageFX.Information(null, pxeModuleName, "Record Save Successfully.");
                }else{
                    ShowMessageFX.Error("Please contact MIS/SEG!", pxeModuleName, "Unable to save record!.");
                }
                break;
            
            case "btnsearch"://Search
                break;
                
            case "btncancel"://Cancel 
                if (ShowMessageFX.OkayCancel(null, "Confirm", "Do you want to disregard changes?") == true){
                    clearFields();
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
                
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
            initButton(pnEditMode);
    }
    
    public void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField)event.getSource();        
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue =txtField.getText();
            
        if (event.getCode() == F3 || event.getCode() == ENTER){
            JSONObject loResult = null;
            switch (lnIndex){                
                case 0: 
                    if(event.getCode() == ENTER)
                    loResult = qsParameter.getModule(poGRider, lsValue, 0); 
                        if (loResult != null && !loResult.isEmpty()){
                            openRecord(loResult.get("sModuleCd").toString());
                            loadTransaction();
                        }
                    break;
                case 1:
                    loResult = qsParameter.getModule(poGRider, lsValue, 1);                     
                    if (loResult != null && !loResult.isEmpty()){
                        openRecord(loResult.get("sModuleCd").toString());
                        loadTransaction();
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
        txtField01.setText("");
        txtField02.setText("");
        txtField00.setText("");
        txtField03.setText("");
        chk00.selectedProperty().setValue(false);
        
        btnActivate.setText("Activate");
        
        psOldRec = "";
    }

    public void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE );
        
        btnBrowse.setVisible(!lbShow);
        btnClose.setVisible(!lbShow);
        btnNew.setVisible(!lbShow);
        btnUpdate.setVisible(!lbShow);
        btnActivate.setVisible(!lbShow);
        
        btnSave.setVisible(lbShow);
        btnSearch.setVisible(lbShow);
        btnCancel.setVisible(lbShow);
        
        txtField00.setMouseTransparent(lbShow);
        txtField01.setMouseTransparent(lbShow);
        
        txtField03.setMouseTransparent(!lbShow);
        if (!lbShow) txtField03.setFocusTraversable(false);
            
        chk00.setMouseTransparent(true);
        
        if (lbShow)
            txtField03.requestFocus();
        else
            btnNew.requestFocus();
        
    }
    
    private void openRecord(String fsRecordID){
        if (poRecord.openRecord(fsRecordID)){
            pnEditMode = poRecord.getEditMode();
        }
    }
    
    public void loadTransaction() {
        txtField02.setText((String) poRecord.getMaster("sModuleCd"));
        txtField03.setText((String) poRecord.getMaster("sModuleDs"));
                
        boolean lbCheck = poRecord.getMaster("cRecdStat").equals("1") ? true : false;
        chk00.selectedProperty().setValue(lbCheck);
        
       if (poRecord.getMaster("cRecdStat").toString().equals("1"))
            btnActivate.setText("Remove");
       else
            btnActivate.setText("Activate");
        
         psOldRec = (String) poRecord.getMaster("sModuleCd");
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
                case 1:
                case 2:
                    break;
                case 3:
                    if (lsValue.length() > 512) lsValue = lsValue.substring(0, 512);                     
                    poRecord.setMaster("sModuleDs", lsValue);
                    txtField.setText((String)poRecord.getMaster("sModuleDs"));
                    break;
                    
                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Text field with name " + txtField.getId() + " not registered.");
                    return;
            }            
        }else{
            pnIndex = lnIndex; 
        }
            txtField.selectAll();  
    };
     
     private void unloadForm(){
        acMain.getChildren().clear();
        acMain.setStyle("-fx-border-color: transparent");
    }
    
}
