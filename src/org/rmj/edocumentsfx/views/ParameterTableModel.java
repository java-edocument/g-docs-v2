package org.rmj.edocumentsfx.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public class ParameterTableModel {  
    private SimpleStringProperty index01;
    private CheckBox index02;
 
    
    public ParameterTableModel(String index01,String value){
        this.index01 = new SimpleStringProperty(index01);
        this.index02 = new CheckBox();
        if(value.equals("1")){
        this.index02.setSelected(true);
        }else{
        this.index02.setSelected(false);
        }
    }
    
    public String getIndex01(){return index01.get();}
    public void setIndex01(String index01){this.index01.set(index01);}
    
    public CheckBox getIndex02(){return index02;}
    public void setIndex02(CheckBox index02){this.index02 = index02;}
    
}