package org.rmj.edocumentsfx.utilities;

import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ui.showFXDialog;
import org.json.simple.JSONObject;

public class qsParameter {
    private final static String pxeModuleName = "qsParameter";
    
    public static JSONObject getBarcode(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "Code»Name»Description";
        String lsColName = "sFileCode»sBarrCode»sBriefDsc";
        String lsColCrit = "sFileCode»sBarrCode»sBriefDsc";
        String lsSQL = "SELECT " +
                            "  sFileCode" +
                            ", sBarrCode" + 
                            ", sBriefDsc" + 
                            ", cRecdStat" + 
                        " FROM EDocSys_File";
        JSONObject loValue = showFXDialog.jsonSearch(foGRider, 
                lsSQL,
                fsValue,
                lsHeader,
                lsColName,
                lsColCrit,
                fnSort);
        return loValue;
    }
    
    public static JSONObject getBranch(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "Branch Code»Branch Name";
        String lsColName = "sBranchCd»sBranchNm";
        String lsColCrit = "sBranchCd»sBranchNm";
        String lsSQL = "SELECT " +
                            "  sBranchCd" +
                            ", sBranchNm" +   
                        " FROM Branch" +
                        " WHERE sBranchCd LIKE 'M%'" +
                        " OR sBranchCD LIKE 'G%'";
        JSONObject loValue = showFXDialog.jsonSearch(foGRider, 
                lsSQL,
                fsValue,
                lsHeader,
                lsColName,
                lsColCrit,
                fnSort);
        return loValue;
    }
    
    public static JSONObject getModule(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "Code»Name";
        String lsColName = "sModuleCd»sModuleDs";
        String lsColCrit = "sModuleCd»sModuleDs";
        String lsSQL = "SELECT " +
                            "  sModuleCd" +
                            ", sModuleDs" + 
                            ", cRecdStat" + 
                        " FROM EDocSys_Module";
        
        JSONObject loValue = showFXDialog.jsonSearch(foGRider, 
                lsSQL,
                fsValue,
                lsHeader,
                lsColName,
                lsColCrit,
                fnSort);
        return loValue;
    }
}
