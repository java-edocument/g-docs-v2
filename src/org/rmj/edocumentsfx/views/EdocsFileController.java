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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

public class EdocsFileController implements Initializable {

    @FXML
    private AnchorPane acMain;
    @FXML
    private Button btnExit;
    @FXML
    private FontAwesomeIconView glyphExit;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnActivate;
    @FXML
    private FontAwesomeIconView faActivate;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnNew;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnSave;
    @FXML
    private TextField txtField00;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField03;
    @FXML
    private CheckBox chk00;
    @FXML
    private TextArea txtField02;
    @FXML
    private CustomTextField txtField50;
    @FXML
    private CustomTextField txtField51;
    @FXML
    private TableView table;
    @FXML
    private TextField txtField04;

    //user defined object/variable
    private static GRider poGRider;
    private String psBranchCD = "";
    private XMBarcode p_oTrans;
    private final String pxeModuleName = "Edocs File Docs";
    private static Image search = new Image("/org/rmj/edocumentsfx/images/search.png");
    private int pnEditMode;
    private int pnIndex = -1;
    private boolean pbLoaded = false;
    private String psOldRec;
    private ObservableList items = FXCollections.observableArrayList();
    private ObservableList b2bPage = FXCollections.observableArrayList();
    private String lsValueFinal;
    TableColumn index01 = new TableColumn("Page No");
    TableColumn index02 = new TableColumn("Is Back to Back?");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (poGRider == null) {
            ShowMessageFX.Warning("GhostRider Application not set..", pxeModuleName, "Please inform MIS department.");
            System.exit(0);
        }

        if (psBranchCD.equals("")) {
            psBranchCD = poGRider.getBranchCode();
        }

        p_oTrans = new XMBarcode(poGRider, psBranchCD, false);

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
        txtField50.focusedProperty().addListener(txtField_Focus);
        txtField51.focusedProperty().addListener(txtField_Focus);
        txtField00.focusedProperty().addListener(txtField_Focus);
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtArea_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);

        txtField50.setOnKeyPressed(this::txtField_KeyPressed);
        txtField51.setOnKeyPressed(this::txtField_KeyPressed);
        txtField00.setOnKeyPressed(this::txtField_KeyPressed);
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField02.setOnKeyPressed(this::txtArea_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);

        txtField00.setMouseTransparent(true);
        txtField50.setLeft(new ImageView(search));
        txtField51.setLeft(new ImageView(search));

        clearFields();
        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
        initGrid();

        pbLoaded = true;
    }

    private void initGrid() {
        index01.setSortable(false);
        index01.setResizable(false);
        index01.setStyle("-fx-alignment: CENTER");
        index02.setSortable(false);
        index02.setResizable(false);
        index02.setStyle("-fx-alignment: CENTER");

        table.getColumns().clear();
        table.getColumns().addAll(index01, index02);

        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.ParameterTableModel, String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.edocumentsfx.views.ParameterTableModel, CheckBox>("index02"));

        /*Set data source to table*/
        table.setItems(items);

        index01.prefWidthProperty().bind(table.widthProperty().divide(100 / 50));
        index02.prefWidthProperty().bind(table.widthProperty().divide(100 / 50));
    }

    public void loadDetail2Grid() {
        lsValueFinal = "";
        int lnCtr;
        items.clear();
        for (lnCtr = 0; lnCtr <= b2bPage.size() - 1; lnCtr++) {
            items.add(new ParameterTableModel("Page " + String.valueOf(lnCtr + 1), ""));
        }
        table.setItems(items);
    }

    public void loadDetail() {
        items.clear();

        String lsValue = (String) p_oTrans.getMaster("sB2BPagex");
        if (lsValue != null) {

            String[] arr = lsValue.split("»");
            int lnIterator = 1;
            for (int lnCtr = 0; lnCtr < arr.length; lnCtr++) {
                items.addAll(new ParameterTableModel("Page" + lnIterator, arr[lnCtr]));
                lnIterator = lnIterator + 1;
            }
            table.setItems(items);
        }
    }

    public void save() {
        lsValueFinal = "";
        ParameterTableModel instance;
        for (int lnCtr = 0; lnCtr <= items.size() - 1; lnCtr++) {
            instance = (ParameterTableModel) items.get(lnCtr);
            String lsValue = String.valueOf(instance.getIndex02().isSelected());
            if (lsValue.equals("true")) {
                lsValue = "1";
            } else {
                lsValue = "0";
            }
            lsValueFinal += lsValue + "»";

        }
        p_oTrans.setMaster("sB2BPagex", lsValueFinal.substring(0, lsValueFinal.length() - 1).replaceAll("\\s", ""));
    }

    public void setGRider(GRider fsGrider) {
        this.poGRider = fsGrider;
    }

    public void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId().toLowerCase();
        String lsValue = "";

        switch (lsButton) {
            case "btnbrowse": //Browse
                JSONObject loResult = null;
                switch (pnIndex) {
                    case 50:
                        lsValue = txtField50.getText();
                        loResult = qsParameter.getBarcode(poGRider, lsValue == null ? "" : lsValue, 0);
                        if (loResult != null && !loResult.isEmpty()) {
                            openRecord(loResult.get("sFileCode").toString());
                            loadTransaction();
                            loadDetail();
                        }
                        break;
                    case 51:
                        lsValue = txtField51.getText();
                        loResult = qsParameter.getBarcode(poGRider, lsValue == null ? "" : lsValue, 1);
                        if (loResult != null && !loResult.isEmpty()) {
                            openRecord(loResult.get("sFileCode").toString());
                            loadTransaction();
                            loadDetail();
                        }
                        break;
                    default:
                        loResult = qsParameter.getBarcode(poGRider, "%", 1);
                        if (loResult != null && !loResult.isEmpty()) {
                            openRecord(loResult.get("sFileCode").toString());
                            loadTransaction();
                            loadDetail();
                        }
                        break;
                }
                break;

            case "btncancel": //Cancel
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to disregard changes?") == true) {
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
                if (p_oTrans.newRecord()) {
                    clearFields();
                    loadTransaction();
                    pnEditMode = p_oTrans.getEditMode();
                }
                break;

            case "btnsave": //Save
                save();
                if (p_oTrans.saveRecord() == true) {
                    openRecord(psOldRec);
                    loadTransaction();
                    loadDetail();
                    ShowMessageFX.Information(null, pxeModuleName, "Record Save Successfully.");
                } else {
                    ShowMessageFX.Error("Please contact MIS/SEG!", pxeModuleName, "Unable to save record!");
                }
                break;

            case "btnsearch": //Search
                break;

            case "btnupdate": //Update
                if (p_oTrans.getMaster("sFileCode") != null && !txtField02.getText().equals("")) {
                    if (p_oTrans.updateRecord()) {
                        pnEditMode = p_oTrans.getEditMode();
                    }
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to update!");
                }
                break;

            case "btnactivate": //Acitvate
                if (!txtField02.getText().equals("") && p_oTrans.getMaster("sFileCode") != null) {
                    if (btnActivate.getText().equals("Activate")) {
                        if (p_oTrans.activateRecord(p_oTrans.getMaster(1).toString())) {
                            openRecord(psOldRec);
                            loadTransaction();
                            loadDetail();
                            ShowMessageFX.Information(null, pxeModuleName, "Record Activated Successfully.");
                        }
                    } else {
                        if (p_oTrans.deactivateRecord(p_oTrans.getMaster("sFileCode").toString())) {
                            openRecord(psOldRec);
                            loadTransaction();
                            loadDetail();
                            ShowMessageFX.Information(null, pxeModuleName, "Record Deactivated Successfully.");
                        }
                    }
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to activate!");
                }
                break;

            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
        initButton(pnEditMode);

    }

    private void unloadForm() {
        acMain.getChildren().clear();
        acMain.setStyle("-fx-border-color: transparent");
    }

    private void openRecord(String fsRecordID) {
        if (p_oTrans.openRecord(fsRecordID)) {
            pnEditMode = p_oTrans.getEditMode();
        }
    }

    public void txtArea_KeyPressed(KeyEvent event) {
        TextArea txtField = (TextArea) event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();

        if (event.getCode() == ENTER || event.getCode() == DOWN || event.getCode() == KeyCode.RIGHT) {
            CommonUtils.SetNextFocus((txtField));
        } else if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.LEFT) {
            CommonUtils.SetPreviousFocus(txtField);
        }

    }

    public void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();

        if (event.getCode() == F3 || event.getCode() == ENTER) {
            JSONObject loResult = null;
            switch (lnIndex) {
                case 50:
                    loResult = qsParameter.getBarcode(poGRider, lsValue == null ? "" : lsValue, 0);
                    if (loResult != null && !loResult.isEmpty()) {
                        openRecord(loResult.get("sFileCode").toString());
                        loadTransaction();
                        loadDetail();
                    }
                    break;

                case 51:
                    loResult = qsParameter.getBarcode(poGRider, lsValue == null ? "" : lsValue, 1);
                    if (loResult != null && !loResult.isEmpty()) {
                        openRecord(loResult.get("sFileCode").toString());
                        loadTransaction();
                        loadDetail();
                    }
                    break;
            }
        }
        if (event.getCode() == DOWN || event.getCode() == ENTER || event.getCode() == KeyCode.RIGHT) {
            CommonUtils.SetNextFocus(txtField);
        }
        if (event.getCode() == UP || event.getCode() == KeyCode.LEFT) {
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
        items.clear();
        lsValueFinal = "";

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

        txtField50.setMouseTransparent(lbShow);
        txtField51.setMouseTransparent(lbShow);

        txtField01.setMouseTransparent(!lbShow);
        txtField02.setMouseTransparent(!lbShow);
        txtField03.setMouseTransparent(!lbShow);

        if (!lbShow) {
            txtField01.setFocusTraversable(false);
        }

        chk00.setMouseTransparent(true);

        if (lbShow) {
            txtField01.requestFocus();
        } else {
            btnNew.requestFocus();
        }
    }

    public void loadTransaction() {
        txtField00.setText((String) p_oTrans.getMaster("sFileCode"));
        txtField01.setText((String) p_oTrans.getMaster("sBarrcode"));
        txtField02.setText((String) p_oTrans.getMaster("sBriefDsc"));
        txtField03.setText(String.valueOf(p_oTrans.getMaster("nNoPagesx")));
        txtField04.setText(String.valueOf(p_oTrans.getMaster("nNoCopies")));
        boolean lbCheck = p_oTrans.getMaster("cRecdStat").equals("1") ? true : false;
        chk00.selectedProperty().setValue(lbCheck);

        if (p_oTrans.getMaster("cRecdStat").toString().equals("1")) {
            btnActivate.setText("Remove");
        } else {
            btnActivate.setText("Activate");
        }

        psOldRec = txtField00.getText();
    }

    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        int lnCtr;
        if (!pbLoaded) {
            return;
        }

        TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();

        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 50:
                case 51:
                    break;
                case 0:
                    p_oTrans.setMaster("sFileCode", CommonUtils.TitleCase(lsValue));
                    txtField.setText(CommonUtils.TitleCase((String) p_oTrans.getMaster("sFileCode")));
                    break;
                case 1:
                    if (lsValue.length() > 32) {
                        lsValue = lsValue.substring(0, 32);
                    }
                    p_oTrans.setMaster("sBarrcode", CommonUtils.TitleCase(lsValue));
                    txtField.setText(CommonUtils.TitleCase((String) p_oTrans.getMaster("sBarrcode")));
                    break;
                case 2:
                    if (lsValue.length() > 64) {
                        lsValue = lsValue.substring(0, 64);
                    }
                    p_oTrans.setMaster("sBriefDsc", CommonUtils.TitleCase(lsValue));
                    txtField.setText(CommonUtils.TitleCase((String) p_oTrans.getMaster("sBriefDsc")));
                    break;
                case 3:
                    int lnPageNox = 0;
                    try {
                        lnPageNox = Integer.parseInt(lsValue);
                        p_oTrans.setMaster("nNoPagesx", lnPageNox);
                    } catch (NumberFormatException e) {
                        lnPageNox = 0;
                         p_oTrans.setMaster("nNoPagesx", lnPageNox);
                    }

                    txtField.setText(String.valueOf(p_oTrans.getMaster("nNoPagesx")));
                    b2bPage.clear();
                    for (lnCtr = 0; lnCtr <= (int) lnPageNox - 1; lnCtr++) {
                        b2bPage.add(lnCtr, "");
                    }
                    loadDetail2Grid();
                    break;
                case 4:
                    int lnNoCopies = 0;
                    try {
                        lnNoCopies = Integer.parseInt(lsValue);
                        p_oTrans.setMaster("nNoCopies", lnNoCopies);
                    } catch (NumberFormatException e) {
                        lnNoCopies = 1;
                        p_oTrans.setMaster("nNoCopies", lnNoCopies);
                    }
                    txtField.setText(String.valueOf(p_oTrans.getMaster("nNoCopies")));
            }
        } else {
            pnIndex = lnIndex;
            txtField.selectAll();
        }
    };

    final ChangeListener<? super Boolean> txtArea_Focus = (o, ov, nv) -> {
        if (!pbLoaded) {
            return;
        }

        TextArea txtField = (TextArea) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();

        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 2:
                    if (!lsValue.equals("")) {
                        if (lsValue.length() > 64) {
                            lsValue = lsValue.substring(0, 64);
                        }
                    }
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
