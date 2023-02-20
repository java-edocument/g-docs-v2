
package org.rmj.edocumentsfx.applications;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javafx.application.Application;
import org.rmj.appdriver.GRider;
import org.rmj.edocumentsfx.views.Edocumentsfx;

public class Login {    
    private static GRider poGRider = null;
    
    public void setGRider(GRider foGRider){poGRider = foGRider;}
    
    public static void main(String [] args){     
        String path;
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            path = "D:/GGC_Java_Systems";
        }
        else{
            path = "/srv/mac/GGC_Java_Systems";
        }
        
        System.setProperty("sys.default.path.config", path);
        
        if (!loadProperties()){
            System.err.println("Unable to load config.");
            System.exit(1);
        } else System.out.println("Config file loaded successfully.");
        
        String lsProdctID;
        String lsUserIDxx;
        
        if (System.getProperty("app.debug.mode").equals("1")){
            lsProdctID = System.getProperty("app.product.id");
            lsUserIDxx = System.getProperty("user.id");
        } else {
            lsProdctID = args[0];
            lsUserIDxx = args[1];
        }
        
        GRider poGRider = new GRider(lsProdctID);
        
        if (!poGRider.loadUser(lsProdctID, lsUserIDxx)){
            System.out.println(poGRider.getMessage() + poGRider.getErrMsg());
            System.exit(1);
        }
                
        Edocumentsfx edocs = new Edocumentsfx();
        edocs.setGRider(poGRider);
        
        Application.launch(edocs.getClass());
    }
    
    private static boolean loadProperties(){
        try {
            Properties po_props = new Properties();
            po_props.load(new FileInputStream(System.getProperty("sys.default.path.config") + "/config/edocs.properties"));
            
            //User and Application Info 
            System.setProperty("user.id", po_props.getProperty("user.id"));
            System.setProperty("app.product.id", po_props.getProperty("app.product.id"));
            System.setProperty("app.debug.mode", po_props.getProperty("app.debug.mode"));
            
            //PC Info
            System.setProperty("computer.terminal", po_props.getProperty("computer.terminal1"));
            
            return true;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
