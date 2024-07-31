
import org.rmj.appdriver.GRider;
import org.rmj.edocx.trans.EDocSysUtil;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Maynard
 */
public class testEDpcSysUtil {
     public static GRider oApp;
  public static void main(String [] args){
      String path;

        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            path = "D:/GGC_Java_Systems";
        } else {
            path = "/srv/GGC_Java_Systems";
        }

        System.setProperty("sys.default.path.config", path);
         oApp = new GRider("gRider");
      
      String lsTranno ="M0W124000003";
      
        if(!EDocSysUtil.RSyncFile(oApp,lsTranno)){
    }
}
}