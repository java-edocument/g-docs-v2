package org.rmj.edocumentsfx.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.constants.EditMode;


public class EditMultipleDocsController implements Initializable {

    @FXML private AnchorPane acMain;
    @FXML private Label lblTitle;
    @FXML private Button btnExit;
    @FXML private FontAwesomeIconView glyphExit;
    @FXML private AnchorPane anchorField;
    @FXML private TableView table;
    @FXML private Button btnCancel;
    @FXML private CustomTextField txtField00;
    @FXML private CustomTextField txtField01;
    @FXML private TextField txtField02;
    @FXML private Button btnOkay;
    
    private String psBranchCd = "";
    private ObservableList items = FXCollections.observableArrayList();
    private ObservableList b2bPage = FXCollections.observableArrayList();
    private final String pxeModuleName = "Update Multiple Documents";
    private String psValueFinal = "";
    private int pnEditMode = -1;
    private static String psFileCode = "";
    private static String psBarcode = "";
    private static String psB2BPage = "";
    private static int pnNoOfPagesx = 0;
    TableColumn index01 = new TableColumn("Page No");
    TableColumn index02 = new TableColumn("Is Back to Back?");
    
    private boolean bCancelled = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        btnOkay.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        txtField02.focusedProperty().addListener(masterFocus);
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
       
        txtField00.setText(psFileCode);
        txtField01.setText(psBarcode);
        txtField02.setText(String.valueOf(pnNoOfPagesx));
        initButton(pnEditMode);
        initGrid();
        loadDetail();
    
    }    

    public void setEditMode(int fnEditMode){
        this.pnEditMode = fnEditMode;
    }
    
    public void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId();
        
        switch (lsButton){
            case "btnOkay":
                if(pnEditMode  != -1){   
                    saveb2b2();
                }
                bCancelled = false;
                break;
            case "btnCancel":
            case "btnExit":
                bCancelled = true;
        }
        unloadScene(event);
    }
    
    public void txtField_KeyPressed(KeyEvent event) {
       TextField txtField = (TextField)event.getSource();        
       int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
       String lsValue = txtField.getText();

       if (event.getCode() == F3 || event.getCode() == ENTER) {
           switch (lnIndex){
               case 2:
                   b2bPage.clear(); 
                   for(int lnCtr =0; lnCtr <=(int) pnNoOfPagesx -1; lnCtr++){
                        b2bPage.add(lnCtr, "");
                    }
                    generateB2b();
                   break;
           }
       }        
       if (event.getCode() == DOWN || event.getCode() == ENTER || event.getCode() == KeyCode.RIGHT){
          CommonUtils.SetNextFocus(txtField); 
       }
       if (event.getCode() == UP || event.getCode() == KeyCode.LEFT){
          CommonUtils.SetPreviousFocus(txtField);                 
       }
    }
    
    private void unloadScene(ActionEvent event){
        Node source = (Node)  event.getSource(); 
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    final ChangeListener<? super Boolean> masterFocus = (o,ov,nv)->{
        if(!nv){
            TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
            String lsValue = txtField.getText();
            switch (lnIndex){
                case 2: //on hand
                    int x = 0;
                    try {
                        x = Integer.parseInt(lsValue);
                        pnNoOfPagesx = x;
                    } catch (NumberFormatException e) {
                        pnNoOfPagesx = 1;
                    }
                    txtField02.setText(String.valueOf(pnNoOfPagesx));
                    
                    b2bPage.clear();
                    for(int lnCtr =0; lnCtr <=(int) x -1; lnCtr++){
                        b2bPage.add(lnCtr, "");
                    }
                    generateB2b();
                    
                    break;
                    
                default:
            }
        }
    };
    
    //todo: textfield validations
    
    public void setFileCode(String fsFileCode){
        psFileCode = fsFileCode;
    }
    public void setBarcode(String fsBarcode){
        psBarcode = fsBarcode;
    }
    public void setB2BPage(String fsB2BPage){
        psB2BPage = fsB2BPage;
    }
    public void setNoOfPage(int fnNoOfPage){
        pnNoOfPagesx = fnNoOfPage;
    }
    
    public int getEditMode(){
    return pnEditMode;
    }
    
    public String getFileCode(){
        return psFileCode;
    }
    public String getBarcode(){
        return psBarcode;
    }
    public String getB2bPage(){
        return psB2BPage;
    }
    
    public int getNoOfPage(){
        return pnNoOfPagesx;
    }
     
    public boolean isCancelled(){return bCancelled;}
    
    public void initGrid(){
        
        index01.setSortable(false); index01.setResizable(false);index01.setStyle("-fx-alignment: CENTER");
        index02.setSortable(false); index02.setResizable(false);index02.setStyle("-fx-alignment: CENTER");
       
        table.getColumns().clear();
        table.getColumns().addAll(index01, index02);
        
        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.ParameterTableModel,String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.ParameterTableModel,CheckBox>("index02"));

         index01.prefWidthProperty().bind(table.widthProperty().divide(100/50));
        index02.prefWidthProperty().bind(table.widthProperty().divide(100/50));
        
        /*Set data source to table*/
        table.setItems(items);
    }
    
    public void loadDetail(){
        items.clear();
        String [] arr = psB2BPage.split("»");
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
        psValueFinal = "";
        ParameterTableModel instance;
        for(int lnCtr = 0; lnCtr <=items.size() -1; lnCtr++){
            instance = (ParameterTableModel) items.get(lnCtr);
            String lsValue= String.valueOf(instance.getIndex02().isSelected());
            if(lsValue.equals("true")){
                lsValue = "1";
            }else{
                lsValue = "0";
            }
            psValueFinal += lsValue+"»";
            
        }
        setB2BPage(psValueFinal.substring(0, psValueFinal.length()-1).replaceAll("\\s", ""));
//        poTrans.setMaster("sB2BPagex", psValueFinal.substring(0, psValueFinal.length()-1).replaceAll("\\s", ""));
    }
    
    public void initButton(int fnValue){
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        
     
            txtField02.setMouseTransparent(!lbShow);
            table.setMouseTransparent(!lbShow);
            
            
       
    }
    
}
