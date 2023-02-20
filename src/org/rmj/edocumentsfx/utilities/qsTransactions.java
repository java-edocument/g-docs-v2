package org.rmj.edocumentsfx.utilities;

import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ui.showFXDialog;
import org.json.simple.JSONObject;

public class qsTransactions {
    private final static String pxeModuleName = "qsTransactions";
    
    public static Object getSales(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "Trans No»Client Name»Date»Total»Inv. Type»Branch";
        String lsColName = "a.sTransNox»d.sClientNm»a.dTransact»a.nTranTotl»c.sDescript»b.sBranchNm";
        String lsColCrit = "a.sTransNox»d.sClientNm»a.dTransact»a.nTranTotl»c.sDescript»b.sBranchNm";
        String lsSQL = "SELECT " +
                            "  a.sTransNox" +
                            ", a.sBranchCd" + 
                            ", a.dTransact" +
                            ", a.sClientID" +
                            ", a.sReferNox" +
                            ", a.nTranTotl" + 
                            ", a.sInvTypCd" + 
                            ", b.sBranchNm" + 
                            ", c.sDescript" + 
                            ", d.sClientNm" + 
                            ", a.cTranStat" + 
                            ", CASE " +
                                " WHEN a.cTranStat = '0' THEN 'OPEN'" +
                                " WHEN a.cTranStat = '1' THEN 'CLOSED'" +
                                " WHEN a.cTranStat = '2' THEN 'POSTED'" +
                                " WHEN a.cTranStat = '3' THEN 'CANCELLED'" +
                                " WHEN a.cTranStat = '4' THEN 'VOID'" +
                                " END AS xTranStat" +
                        " FROM Sales_Master a" + 
                                " LEFT JOIN Branch b ON a.sBranchCd = b.sBranchCd" +
                                " LEFT JOIN Inv_Type c ON a.sInvTypCd = c.sInvTypCd" +
                            ", Client_Master d" + 
                        " WHERE a.sClientID = d.sClientID";
        
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