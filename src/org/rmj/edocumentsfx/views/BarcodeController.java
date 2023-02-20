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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.CustomTextField;
import org.json.simple.JSONObject;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.edocumentsfx.utilities.qsParameter;
import org.rmj.parameter.agentfx.XMBarcode;

public class BarcodeController implements Initializable {
    //ui defined object
    @FXML private AnchorPane acMain;
    @FXML private Button btnExit;
    @FXML private FontAwesomeIconView glyphExit;
    @FXML private CustomTextField txtField00;
    @FXML private CustomTextField txtField01;
    @FXML private TextField txtField03;
    @FXML private TextArea txtField04;
    @FXML private CheckBox chk00;
    @FXML private TextField txtField02;
    @FXML private Button btnNew;
    @FXML private Button btnUpdate;
    @FXML private Button btnBrowse;
    @FXML private Button btnClose;
    @FXML private Button btnCancel;
    @FXML private Button btnActivate;
    @FXML private Button btnSearch;
    @FXML private Button btnSave;
    @FXML private FontAwesomeIconView faActivate;
    
     //user defined object/variable
    private static GRider poGRider;
    private String psBranchCD = "";
    private XMBarcode p_oTrans;
    private final String pxeModuleName = "Barcode";
    private static Image search = new Image("/org/rmj/edocumentsfx/images/search.png");
    private int pnEditMode;
    private int pnIndex = -1;
    private boolean pbLoaded = false;
    private String psOldRec;
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (poGRider == null){
            ShowMessageFX.Warning("GhostRider Application not set..", pxeModuleName, "Please inform MIS department.");
            System.exit(0);
        }
        
        if (psBranchCD.equals("")) psBranchCD = poGRider.getBranchCode();
        p_oTrans = new XMBarcode(poGRider, psBranchCD, false);
         /*Set action event handler for the buttons*/
        btnActivate.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnNew.setOnAction(this::cmdButton_Click);
        btnUpdate.setOnAction(this::cmdButton_Click);
        btnSearch.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
                
        /*Add listener to text fields*/
        txtField00.focusedProperty().addListener(txtField_Focus);
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtArea_Focus);
        
        txtField00.setOnKeyPressed(this::txtField_KeyPressed);
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtField02.setMouseTransparent(true);
        txtField00.setLeft(new ImageView(search));
        txtField01.setLeft(new ImageView(search));
        
        clearFields();
        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
        
        pbLoaded = true;
    }
    
    public void setGRider(GRider foGRider){
        this.poGRider= foGRider;
    }
    
    public void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId().toLowerCase();
        String lsValue = "";
        
        switch (lsButton){
            case "btnbrowse": //Browse
                JSONObject loResult = null;
                switch (pnIndex) {
                    case 0:
                        lsValue = txtField00.getText();
                        loResult  = qsParameter.getBarcode(poGRider, lsValue == null ? "" : lsValue, 0);
                        if (loResult != null && !loResult.isEmpty()){
                            openRecord(loResult.get("sFileCode").toString());
                            loadTransaction();
                        }
                        break;
                    case 1:
                        lsValue = txtField01.getText();
                        loResult = qsParameter.getBarcode(poGRider, lsValue == null ? "" : lsValue, 1);
                        if (loResult != null && !loResult.isEmpty()){
                            openRecord(loResult.get("sFileCode").toString());
                            loadTransaction();
                        }
                        break;
                    default:
                        loResult = qsParameter.getBarcode(poGRider, "%", 1);
                        if (loResult != null && !loResult.isEmpty()){
                            openRecord(loResult.get("sFileCode").toString());
                            loadTransaction();
                        }
                        break;
                }
                break;
                
            case "btncancel": //Cancel
                if(ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to disregard changes?") == true){
                    clearFields();
                    pnEditMode = EditMode.UNKNOWN;
               }
               break;
               
            case "btnclose"://Close
                unloadForm();
                break;
                
            case "btnexit"://exit
                unloadForm();
                break;
                
            case "btnnew": //New
                if (p_oTrans.newRecord()){
                    clearFields();
                    loadTransaction();
                    pnEditMode = p_oTrans.getEditMode();
                }
                break;
                
            case "btnsave": //Save
                if (p_oTrans.saveRecord()==true){
                    openRecord(psOldRec);
                    loadTransaction();
                    ShowMessageFX.Information(null, pxeModuleName, "Record Save Successfully.");
                }else{
                    ShowMessageFX.Error("Please contact MIS/SEG!", pxeModuleName, "Unable to save record!");
                }
                break;
                
            case "btnsearch": //Search
                break;
                
            case "btnupdate": //Update
                if (p_oTrans.getMaster("sFileCode") != null && !txtField02.getText().equals("")){
                    if (p_oTrans.updateRecord()){
                        pnEditMode = p_oTrans.getEditMode();
                    }
                }else
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to update!");
                break;
                
            case "btnactivate": //Acitvate
                if (!txtField02.getText().equals("") && p_oTrans.getMaster("sFileCode") != null){
                    if (btnActivate.getText().equals("Activate")){
                        if (p_oTrans.activateRecord(p_oTrans.getMaster(1).toString())){
                            openRecord(psOldRec);
                            loadTransaction();
                            ShowMessageFX.Information(null, pxeModuleName, "Record Activated Successfully.");
                        }
                    } else{
                        if (p_oTrans.deactivateRecord(p_oTrans.getMaster("sFileCode").toString())){
                            openRecord(psOldRec);
                            loadTransaction();
                            ShowMessageFX.Information(null, pxeModuleName, "Record Deactivated Successfully.");
                        }
                    }
                }else
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to activate!");
                break;
                
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
        initButton(pnEditMode);
    }
    
    private void openRecord(String fsRecordID){
        if (p_oTrans.openRecord(fsRecordID)){
            pnEditMode = p_oTrans.getEditMode();
        }
    }
    
    private void unloadForm(){
        acMain.getChildren().clear();
        acMain.setStyle("-fx-border-color: transparent");
    }
    

    public void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField)event.getSource();        
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (event.getCode() == F3 || event.getCode() == ENTER) {
            JSONObject loResult = null;
            switch (lnIndex){
                case 0: 
                    loResult = qsParameter.getBarcode(poGRider, lsValue == null ? "" : lsValue, 0);
                    if (loResult != null && !loResult.isEmpty()){
                        openRecord(loResult.get("sFileCode").toString());
                        loadTransaction();
                    }
                    break;
                
                case 1:
                    loResult = qsParameter.getBarcode(poGRider, lsValue == null ? "" : lsValue, 1);
                    if (loResult != null && !loResult.isEmpty()){
                        openRecord(loResult.get("sFileCode").toString());
                        loadTransaction();
                }break;
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
        txtField01.setText("");
        txtField02.setText("");
        txtField03.setText("");
        txtField04.setText("");
        
        chk00.selectedProperty().setValue(false);
        chk00.setText("Activate");
        btnActivate.setText("Activate");
        
        psOldRec = "";
    }

    public void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        
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
        txtField04.setMouseTransparent(!lbShow);
        
        if (!lbShow){
            txtField03.setFocusTraversable(false);
            txtField04.setFocusTraversable(false);}
        
        chk00.setMouseTransparent(true);
           
        if (lbShow)
            txtField03.requestFocus();
        else
            btnNew.requestFocus();
    }

    public void loadTransaction() {
        txtField02.setText((String) p_oTrans.getMaster("sFileCode"));
        txtField03.setText((String) p_oTrans.getMaster("sBarrcode"));
        txtField04.setText((String) p_oTrans.getMaster("sBriefDsc"));
                
        boolean lbCheck = p_oTrans.getMaster("cRecdStat").equals("1") ? true : false;
        chk00.selectedProperty().setValue(lbCheck);
        
        if (p_oTrans.getMaster("cRecdStat").toString().equals("1"))
            btnActivate.setText("Remove");
        else
            btnActivate.setText("Activate");
        
        psOldRec = txtField02.getText();
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
                case 1:
                    break;
                case 2:
                    p_oTrans.setMaster("sFileCode", CommonUtils.TitleCase(lsValue));
                        txtField.setText(CommonUtils.TitleCase((String)p_oTrans.getMaster("sFileCode")));
                    break;
                case 3:
                    if (lsValue.length() > 32) lsValue = lsValue.substring(0, 32);
                        p_oTrans.setMaster("sBarrcode", CommonUtils.TitleCase(lsValue));
                        txtField.setText(CommonUtils.TitleCase((String)p_oTrans.getMaster("sBarrcode")));
                        break;
            }
        } else {
            pnIndex = lnIndex;
        }
        txtField.selectAll();
    };
    
    final ChangeListener<? super Boolean> txtArea_Focus = (o,ov,nv)->{
        if (!pbLoaded) return;
        
        TextArea txtField = (TextArea)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
        if(!nv){ /*Lost Focus*/
            switch (lnIndex){
                case 4:
                    if (lsValue.length() > (int) 64) lsValue = lsValue.substring(0, 64);
                        p_oTrans.setMaster("sBriefDsc", CommonUtils.TitleCase(lsValue));
                        txtField.setText(CommonUtils.TitleCase((String) p_oTrans.getMaster("sBriefDsc")));
                        break;
            }
        } else {
            pnIndex = lnIndex;
        }
        txtField.selectAll();
    };
}
