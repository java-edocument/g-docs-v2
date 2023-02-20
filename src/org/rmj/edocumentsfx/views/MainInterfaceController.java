package org.rmj.edocumentsfx.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.textfield.CustomTextField;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.edocumentsfx.utilities.Utilities;

public class MainInterfaceController implements Initializable {
    
    //interface created object
    @FXML private SplitPane splPane;
    @FXML private ToggleButton btnRestoreDown;
    @FXML private Button btnMinimize;
    @FXML private Button btnClose;
    @FXML private MenuBar mnuBar;
    @FXML private Label lblBranch;
    @FXML private Label lblUser;
    @FXML private FontAwesomeIconView faCompress;
    @FXML private Label lblDate;
    @FXML private TreeView treeView;
    @FXML private Tooltip tpRestore;
    @FXML private Label lblNavigation;
    @FXML private Button btnNavClose;
    @FXML private Button btnHome;
    @FXML private AnchorPane acWorkStation;
    @FXML private Button btnNavigation;
    @FXML private CustomTextField txtField00;
    @FXML private FontAwesomeIconView faNavigation;
    @FXML private Label lblfirstLevel;
    @FXML private Label lblSecondLevel;
    @FXML private Label lblTime;
    @FXML private Label lblDay;
    @FXML private Label lblSeconds;
    @FXML private MenuItem mnuBug;
    @FXML private MenuItem mnuAbout;
    
    //user defined object/variable
    Node vertiCalPane;
    public AnchorPane acTiltleBar;
    private GRider poGRider;
    private static final double xOffset = 0; 
    private static final double yOffset = 0;
    private final String pxeModuleName = "EDocuments Fx v2";
    private final Image search = new Image("org/rmj/edocumentsfx/images/search.png");
    TreeItem<String> MainTreeItem = new TreeItem<>("Home");
    ObservableList<TreeItem<String>> firstLevel = FXCollections.observableArrayList();
    public AnchorPane rootPane;
    private boolean pbLoaded = false;
    private boolean isRegistration = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(poGRider == null){
            ShowMessageFX.Warning("GhostRider Application not set..", pxeModuleName, "Please inform MIS department.");
            System.exit(0);
        }
       
        vertiCalPane = splPane.getItems().get(1);
        splPane.setDividerPosition(0, 0.2055);
        
        btnClose.setOnAction(this::cmdButton_Click);
        btnMinimize.setOnAction(this::cmdButton_Click);
        btnNavClose.setOnAction(this::cmdButton_Click);
        btnNavigation.setOnAction(this::cmdButton_Click);
        btnRestoreDown.setOnAction(this::toggleButton_Click);
        btnHome.setOnAction(this::cmdButton_Click);
        
        treeView.setOnMouseClicked(this::tree_ItemClick);
        mnuBug.setOnAction(this::menu_Click);
        mnuAbout.setOnAction(this::menu_Click);
        txtField00.setLeft(new ImageView(search));
//        txtField00.setOnKeyReleased(this::txtField_Search);
        
        loadTransaction();
        getTime();
        pbLoaded = true;
    }
    
    public void loadTreeView(){
        TreeItem<String> Files = new TreeItem<> ("Files");
        TreeItem<String> Module = new TreeItem<> ("Module");
        TreeItem<String> Barcode = new TreeItem<> ("Barcode");
        TreeItem<String> Branch = new TreeItem<> ("Branch");
        TreeItem<String> EdocsFile = new TreeItem<> ("File Documents");
        TreeItem<String> Department = new TreeItem<> ("Department");
        Files.getChildren().addAll(Module,Barcode,Branch, EdocsFile, Department);
        
        TreeItem<String> Transactions = new TreeItem<> ("Transactions");
//        TreeItem<String> Scan_documents = new TreeItem<> ("Scan Documents");
        TreeItem<String> MultipleDocs = new TreeItem<> ("MCAR Scanner");
//        TreeItem<String> MC_Registration = new TreeItem<> ("MC Registration Multi");
        TreeItem<String> Registration = new TreeItem<> ("MC Registration Scanner");
        TreeItem<String> RegistrationExpense = new TreeItem<> ("Registration Expense Scanner");
        Transactions.getChildren().addAll(MultipleDocs,Registration, RegistrationExpense);
        
        TreeItem<String> Reports = new TreeItem<> ("Reports");
        TreeItem<String> Utilites = new TreeItem<> ("Utilites");
        TreeItem<String> Register = new TreeItem<> ("Register");
//        TreeItem<String> ScanDocs = new TreeItem<> ("Scanned Documents");
        TreeItem<String> Multiple_Docs = new TreeItem<> ("MCAR Documents Register");
        TreeItem<String> RegistrationRegister = new TreeItem<> ("MC Registration Register");
//        TreeItem<String> RegistrationRegisterMulti = new TreeItem<> ("MC Registration Multi Reg");
        TreeItem<String> ExpenseRegister = new TreeItem<> ("Registration Expense Register");
        TreeItem<String> Printing = new TreeItem<> ("Printing");
        
        Utilites.getChildren().add(Printing);
        Register.getChildren().addAll(Multiple_Docs, RegistrationRegister, ExpenseRegister);
        
        MainTreeItem.getChildren().clear(); 
        MainTreeItem.getChildren().addAll(Files,Transactions,Reports,Register,Utilites);
        
        firstLevel.addAll(Files, Transactions, Reports, Register, Utilites);
        MainTreeItem.setExpanded(true);
        
        treeView.setRoot(MainTreeItem);
        treeView.setShowRoot(true);
    }
    
    private void tree_ItemClick(MouseEvent event) {
        String selectedItem = "";
        if (event.getClickCount()==2) {
                selectedItem = ((TreeItem)treeView.getSelectionModel().getSelectedItem()).getValue().toString();
                if(!selectedItem.equals("")){
                    try{
                        switch (selectedItem){
                            case "Barcode":
                                LoadScene("Barcode.fxml");
                                break;
                            case "Scanner":
                                LoadScene("Scanner.fxml");
                                break;
                            case "Module":
                                LoadScene("Module.fxml");
                                break;
                            case "Scan Documents":
                                LoadScene("CRDocs.fxml");
                                break;
                            case "Printing":
                                LoadScene("Printing.fxml");
                                break;
                            case "File Documents":
                                LoadScene("EdocsFile.fxml");
                                break;
                            case "Scanned Documents":
                                setIsRegistration(false);
                                LoadScene("BrowseMaster.fxml");
                                break;
                            case "MCAR Scanner":
                                LoadScene("MultipleDocsSelection.fxml");
                                break;
                            case "MC Registration Scanner":
                                LoadScene("MCRegistrationSelector.fxml");
                                break;
                            case "Department":
                                LoadScene("Department.fxml");
                                break;
                            case "MCAR Documents Register":
                                LoadScene("MultipleDocsSelectionRegister.fxml");
                                break;
//                            case "MC Registration":
//                                LoadScene("RegistrationSelector.fxml");
//                                break;
                            case "Registration Expense Scanner":
                                LoadScene("RegistrationExpenseSelector.fxml");
                                break;
//                            case "MC Registration Register":
//                                setIsRegistration(true);
//                                LoadScene("BrowseMaster.fxml");
//                                break;
                            case "MC Registration Register":
                                setIsRegistration(true);
                                LoadScene("MCRegistrationSelectorRegister.fxml");
                                break;
                            case "Registration Expense Register":
                                setIsRegistration(true);
                                LoadScene("RegistrationExpenseSelectorRegister.fxml");
                                break;
                             case "Branch":
                                LoadScene("Branch.fxml");
                                break;
                            }
                    }catch(IOException | NullPointerException ex){
                        ex.printStackTrace();
                    }
                }
        }
    }
    
    private void setIsRegistration(boolean fsRegistration){
        this.isRegistration = fsRegistration;
    }
    
    public void txtField_Search(KeyEvent event){
    FilteredList<TreeItem<String>> filteredList = new FilteredList<>(firstLevel, item -> true);
            filteredList.predicateProperty().bind(Bindings.createObjectBinding(() -> {
                String lsValue = txtField00.getText().toLowerCase();
                if (lsValue.isEmpty()) return item -> true ;
                return item -> item.getValue().toLowerCase().startsWith(lsValue);
            }, txtField00.textProperty()));
            
            Bindings.bindContent(MainTreeItem.getChildren(), filteredList);
    }
    
    public void setGRider(GRider foGRider){this.poGRider = foGRider;}
    
    private void toggleButton_Click(ActionEvent event){
        String toggleButton = ((ToggleButton)event.getSource()).getId().toLowerCase();
        switch (toggleButton){
            case "btnrestoredown":
            Stage stage=(Stage) rootPane.getScene().getWindow();
               if (btnRestoreDown.isSelected()) {
                   faCompress.setGlyphName("EXPAND");
                   tpRestore.setText("Maximize");
                   Screen screen = Screen.getPrimary();
                   Rectangle2D bounds = screen.getVisualBounds();
                   stage.setY(bounds.getMinY());
                   stage.setX(bounds.getMinX());
                   stage.setWidth(1024);
                   stage.setHeight(740);
                   stage.centerOnScreen();
               } else {
                   faCompress.setGlyphName("COMPRESS");
                   tpRestore.setText("Restore Down");
                   Screen screen = Screen.getPrimary();
                   Rectangle2D bounds = screen.getVisualBounds();
                   stage.setY(bounds.getMinY());
                   stage.setX(bounds.getMinX());
                   stage.setWidth(bounds.getWidth());
                   stage.setHeight(bounds.getHeight()); 
                   stage.centerOnScreen();
               }   
        }
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId().toLowerCase();
        switch (lsButton){
           case "btnclose":  //Close
                if(ShowMessageFX.OkayCancel("Do you want to exit?", pxeModuleName, "All changes made will not save!")== true){
                   System.exit(0);
                }
                break;
           case "btnminimize": //Minimize
                CommonUtils.minimizeStage(btnMinimize);
                break;
           case "btnclear": //Clear
                break;
           case "btnnavclose":
                splPane.setDividerPosition(0, 0);
                break;
           case "btnnavigation":
               splPane.setDividerPosition(0, 0.1500);
               break;
           case "btnhome":
               loadTreeView();
               break;
        }     
    }
    
    private void loadTransaction(){
        ResultSet name;
        lblBranch.setText(poGRider.getClientName());
        
        String lsQuery = "SELECT" +
                            "  IFNULL(b.sCompnyNm, 'CLIENT NOT REGISTERED')" +
                        " FROM xxxSysUser a," +
                            " Client_Master b" +
                        " WHERE sUserIDxx = " + SQLUtil.toSQL(poGRider.getUserID()) +
                                " AND a.sEmployNo = b.sClientID";
        
        name= poGRider.executeQuery(lsQuery);
        try {
            while(name.next())
            lblUser.setText(name.getString(1));
        } catch (SQLException ex) {
            Logger.getLogger(MainInterfaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getTime(){
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {            
        Calendar cal = Calendar.getInstance();
        int second = cal.get(Calendar.SECOND);
        
        Date date;
        date = poGRider.getServerDate();
        
        String fmtHourMin = "h:mm";
        String fmtSeconds = "ss";
        String fmtCurrentDay = "EEEE";
        
        DateFormat dftHourMin = new SimpleDateFormat(fmtHourMin);
        DateFormat dftSeconds = new SimpleDateFormat(fmtSeconds);
        DateFormat dftCurrentDay = new SimpleDateFormat(fmtCurrentDay);
        
        String formattedTime= dftHourMin.format(date);
        String formattedSec = dftSeconds.format(date);
        String formattedDay = dftCurrentDay.format(date);
        
        lblTime.setText(formattedTime);
        lblSeconds.setText(formattedSec);
        lblDay.setText(formattedDay);
        lblDate.setText(CommonUtils.xsDateLong(date));
        
    }),
         new KeyFrame(Duration.seconds(1))
    );
        
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
    
    public Parent LoadScene(String foURL)throws IOException{
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(foURL));
       
        Object fxObj = getController(foURL);
        fxmlLoader.setController(fxObj);
        
        Parent root = fxmlLoader.load();
        acWorkStation.getChildren().clear();

        StackPane stack = new StackPane();
        stack.getChildren().add(root);

            stack.translateXProperty()
                    .bind(acWorkStation.widthProperty().subtract(stack.widthProperty())
                            .divide(2));

            stack.translateYProperty()
                    .bind(acWorkStation.heightProperty().subtract(stack.heightProperty())
                            .divide(2));
        acWorkStation.getChildren().add(stack);
        dragNode(stack);
        
        return root;
    }
    
    class Delta { double x, y; }
        
    public void dragNode(Node node) {
        // Custom object to hold x and y positions
        final Delta dragDelta = new Delta();

        node.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                dragDelta.x = node.getLayoutX() - mouseEvent.getSceneX();
                dragDelta.y = node.getLayoutY() - mouseEvent.getSceneY();
            }
        });
        
        node.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                node.setCursor(Cursor.DEFAULT);
            }
        });

        node.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                node.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                node.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
            }
        });

        node.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                node.setCursor(Cursor.HAND);
            }
        });
    }
    
    private Object getController(String fsValue){   
        switch (fsValue){
            case "Branch.fxml":
                BranchController loBranch = new BranchController();
                loBranch.setGRider(poGRider);
                
                return loBranch;
            case "Barcode.fxml":
                BarcodeController loBarcode = new BarcodeController();
                loBarcode.setGRider(poGRider);
                
                return loBarcode;
            case "About.fxml":
                AboutController loAbout = new AboutController();
                loAbout.setGRider(poGRider);
                
                return loAbout;
             
             case "Module.fxml":
                ModuleController loModule = new ModuleController();
                loModule.setGRider(poGRider);
                
                return loModule;
                
             case "CRDocs.fxml":
                CRDocsController loCRDocs = new CRDocsController();
                loCRDocs.setGRider(poGRider);
                return loCRDocs;
                
             case "CRScanner.fxml":
                CRScannerController loCrScanner = new CRScannerController();
                loCrScanner.setGRider(poGRider);
                
                return loCrScanner;
                
             case "MCRegistrationSelector.fxml":
                MCRegistrationSelectorController loMCRegistration = new MCRegistrationSelectorController();
                loMCRegistration.setGRider(poGRider);
                
                return loMCRegistration;
                
             case "Printing.fxml":
                 PrintingController loPrinting = new PrintingController();
                 loPrinting.setGRider(poGRider);
                 
                 return loPrinting;
                 
             case "EdocsFile.fxml":
                EdocsFileController loedocsFile = new EdocsFileController();
                loedocsFile.setGRider(poGRider);
                
                return loedocsFile;
                
            case "BrowseMaster.fxml":
                BrowseMasterController loBrowse = new BrowseMasterController();
                if(isRegistration == false){
                    loBrowse.setisRegistration(false);
                }else{
                    loBrowse.setisRegistration(true);
                }
                loBrowse.setGRider(poGRider);
                return loBrowse;
                
            case "MultipleDocsSelection.fxml":
                MultipleDocsSelectionController loMultipleDocs = new MultipleDocsSelectionController();
                loMultipleDocs.setGRider(poGRider);
                
                return loMultipleDocs;
                
            case "Department.fxml":
                DepartmentController loDepartment = new DepartmentController();
                loDepartment.setGRider(poGRider);
                
                return loDepartment;
                
             case "MultipleDocsSelectionRegister.fxml":
                MultipleDocsSelectionRegisterController loMultipleDocsRegister = new MultipleDocsSelectionRegisterController();
                loMultipleDocsRegister.setGRider(poGRider);
                
                return loMultipleDocsRegister;
             
            case "RegistrationSelector.fxml":
                RegistrationSelectorController loRegistration = new RegistrationSelectorController();
                loRegistration.setGRider(poGRider);
                
                return loRegistration;
                
            case "RegistrationExpenseSelector.fxml":
                RegistrationExpenseSelectorController loRegExpense = new RegistrationExpenseSelectorController();
                loRegExpense.setGRider(poGRider);

                return loRegExpense;
                
            case "RegistrationExpenseSelectorRegister.fxml":
                RegistrationExpenseSelectorRegisterController loRegExpenseRegister = new RegistrationExpenseSelectorRegisterController();
                loRegExpenseRegister.setGRider(poGRider);
                
                return loRegExpenseRegister;
            
            case "MCRegistrationSelectorRegister.fxml":
                MCRegistrationSelectorRegisterController loRegister = new MCRegistrationSelectorRegisterController();
                loRegister.setGRider(poGRider);
                 
                return loRegister;
                
                
            default:
                return null;
        }
    }
    

    public void menu_Click(ActionEvent event) {
        String menuItem = ((MenuItem)event.getSource()).getId().toLowerCase();
        try{
           switch (menuItem){
            case "mnubug":
                Utilities.runDesktopBrowser();
                break;
            case "mnuabout":
                LoadScene("About.fxml");
                break;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
