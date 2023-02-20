package org.rmj.edocumentsfx.views;

import com.fujitsu.pfu.fiscn.sdk.FiscnException;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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


public class BrowseMasterController implements Initializable {

    @FXML private AnchorPane acMain;
    @FXML private Label lblTitle;
    @FXML private Button btnExit;
    @FXML private FontAwesomeIconView glyphExit;
    @FXML private AnchorPane anchorField;
    @FXML private CustomTextField txtField50;
    @FXML private CustomTextField txtField51;
    @FXML private TableView<ParameterTableModel> table1;
    @FXML private Label lblHeader;
    @FXML private TextField txtField01;
    @FXML private TextField txtField02;
    @FXML private TextField txtField03;
    @FXML private CustomTextField txtField04;
    @FXML private CustomTextField txtField05;
    @FXML private TextArea txtField06;
    @FXML private CustomTextField txtField07;
    @FXML private CustomTextField txtField08;
    @FXML private TextArea txtField10;
    @FXML private TextField txtField11;
    @FXML private TextField txtField09;
    @FXML private Button btnClose;
    @FXML private Button btnDetail;
    @FXML private Button btnBrowse;
    @FXML private TextField txtField12;
    
    private Date pxeCurrentDate;
    private String psDefaultPath= "";
    private int pnEditMode = -1;
    private Integer pnIndex = -1;
    private XMEDocuments poTrans;
    private static GRider poGRider;
    private String psBranchCd = "";
    private Stage stage = null;
    private boolean pbLoaded = false;
    private final String pxeDateFormat = "yyyy-MM-dd";
    private final String pxeModuleName = "File Documents";
    private Image search = new Image("/org/rmj/edocumentsfx/images/search.png");
    private String psDefaultImage = "org/rmj/edocumentsfx/images/default.png";
    private String psNoImage = "org/rmj/edocumentsfx/images/noimageavailable2.png";
    private boolean isRegistration = false;
    
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
            
            try {
                poTrans = new XMEDocuments(poGRider ,psBranchCd, false);
            } catch (FiscnException ex) {
                Logger.getLogger(CRDocsController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            pxeCurrentDate = poGRider.getServerDate();
            if (psDefaultPath.equals("")) psDefaultPath = poTrans.getDefaultPath();
            
            btnClose.setOnAction(this::cmdButton_Click);
            btnExit.setOnAction(this::cmdButton_Click);
            btnBrowse.setOnAction(this::cmdButton_Click);
            btnDetail.setOnAction(this::cmdButton_Click);
            
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
            txtField12.focusedProperty().addListener(txtField_Focus);
            
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
            txtField12.setOnKeyPressed(this::txtField_KeyPressed);
            
            txtField50.setOnKeyPressed(this::txtField_KeyPressed);
            txtField51.setOnKeyPressed(this::txtField_KeyPressed);
            
            txtField01.setMouseTransparent(true);
            txtField02.setMouseTransparent(true);
            txtField03.setMouseTransparent(true);
            txtField04.setMouseTransparent(true);
            txtField05.setMouseTransparent(true);
            txtField06.setMouseTransparent(true);
            txtField07.setMouseTransparent(true);
            txtField08.setMouseTransparent(true);
            txtField09.setMouseTransparent(true);
            txtField10.setMouseTransparent(true);
            txtField11.setMouseTransparent(true);
            txtField12.setMouseTransparent(true);
            
            txtField50.setLeft(new ImageView(search));
            txtField51.setLeft(new ImageView(search));
            txtField04.setLeft(new ImageView(search));
            txtField05.setLeft(new ImageView(search));
            txtField07.setLeft(new ImageView(search));
            txtField08.setLeft(new ImageView(search));
            clearFields();
            
            initGrid();
            txtField06.setMouseTransparent(true);
           
            pbLoaded = true;
    }

    public void setisRegistration(boolean fsRegistration){
        this.isRegistration = fsRegistration;
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
        table1.setItems(items);
        
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
        txtField12.setText("");
        txtField50.setText("");
        txtField51.setText("");
        
        pnIndex = -1;
        
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
        if (event.getCode() == ENTER || event.getCode() == F3){
            switch (lnIndex){
                case 4: /*sDeptIDxx*/
                    break;
                case 5: /*sEmployID*/
                    break;
                case 7:
                    break;
                case 50: /*sTransNox*/
                    poTrans.setSourceCD("MCRg");
                    if (lsValue.equals("")){
                        ShowMessageFX.Warning(null, pxeModuleName, "Please input text to search!");
                    return;
                    }
                    
                    if (poTrans.searchWithCondition("sTransNox", lsValue, "a.cTransTat = '0'")==true){
                        loadTransaction();
                    }else{
                        ShowMessageFX.Warning(null, pxeModuleName, poTrans.getMessage());
                        return;
                    }
                    break;
                case 51: /*dTransact*/
                    poTrans.setSourceCD("MCRg");
                    if (lsValue.equals("")){
                        ShowMessageFX.Warning(null, pxeModuleName, "Please input text to search!");
                        return;
                    }

                    if (poTrans.searchWithCondition("dTransact", lsValue, "a.cTransTat = '0'")==true){
                        loadTransaction();
                    }else{
                        ShowMessageFX.Warning(null, pxeModuleName, poTrans.getMessage());
                        return;
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
            case "btnclose":
            case "btnexit":
                unloadForm();
                return;
                
            case "btnbrowse":
                poTrans.setSourceCD("MCRg");
                if(poTrans.searchWithCondition("sTransNox", "%", "") == true){
                    loadTransaction();
                    setStage();
                }
                break;
            case "btndetail":
                if (pnEditMode == EditMode.READY){
                    if(isRegistration == false){
                        loadIndividualView();
                    }else{
                        loadRegistrationView();
                    }
                }else{
                    ShowMessageFX.Information(null, pxeModuleName, "Please browse a record first before proceeding...");
                }
                break;

            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
    }
    
    private void loadIndividualView(){
        IndividualViewController loController = new IndividualViewController();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("IndividualView.fxml"));

        fxmlLoader.setController(loController);
        loController.setGRider(poGRider);
        loController.setClass(poTrans);
        loController.setEditMode(pnEditMode);
        loController.setFilePath(psDefaultPath);
        loController.setNoImage(psNoImage);
        loController.setController(this);
        
        try {
            fxmlLoader.load();
           
            Parent parent = fxmlLoader.getRoot();
            Scene scene = new Scene(parent);
            scene.setFill(new Color(0, 0, 0, 0));
            
            loController.loadDetail2Grid();
             
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
    
    private void loadRegistrationView(){
        RegistrationRegisterController loController = new RegistrationRegisterController();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("RegistrationRegister.fxml"));

        fxmlLoader.setController(loController);
        loController.setGRider(poGRider);
        loController.setClass(poTrans);
        loController.setEditMode(pnEditMode);
        loController.setFilePath(psDefaultPath);
        loController.setNoImage(psNoImage);
        loController.setController(this);
        
        try {
            fxmlLoader.load();
           
            Parent parent = fxmlLoader.getRoot();
            Scene scene = new Scene(parent);
            scene.setFill(new Color(0, 0, 0, 0));
            
            loController.loadDetail2Grid();
             
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
    
    
    private void unloadForm(){
        psBranchCd = "";
        acMain.getChildren().clear();
        acMain.setStyle("-fx-border-color: transparent");
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
        txtField11.setText(Integer.toString((int) poTrans.getFileCode().getMaster("nNoCopies")));
        txtField12.setText(String.valueOf(poTrans.getBranch().getMaster("sBranchNm")));
        
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
                case 2: /*dTransact*/
                case 4:/*sDeptIDxx*/
                case 5:/*sEmployID*/
                case 9:
                case 11:
                case 12:
                    break;
                    
                case 50:
                   if(lsValue.equals("") || lsValue.equals("%"))
                       txtField.setText("");
                       break;
                case 51: 
                    if(lsValue.equals("") || lsValue.equals("%"))
                       txtField.setText("");
                       break;
                    
                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Text field with name " + txtField.getId() + " not registered.");
                   return;
            }
            pnIndex = lnIndex;
            txtField.selectAll();
        } else{
            switch (lnIndex){
                case 51: /*dTransact*/
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
