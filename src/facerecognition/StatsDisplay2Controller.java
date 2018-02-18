/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facerecognition;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

/**
 * FXML Controller class
 *
 * @author sharathkashyap
 */
public class StatsDisplay2Controller implements Initializable {

    
    //private TextArea visitStats2;
    
    @FXML
    private Button back2;
    @FXML
    private BarChart<?, ?> barChart2;
    @FXML
    private BarChart<?, ?> barChart3;
    private String fromDate;
    private String toDate;
    @FXML
    public void displayStats2(String ID)
    {
        VisitDetails vd = JDBC.getVisitDetails(ID);
        int i;
//        visitStats2.setText(vd.getsID());               
//        for(i=0;i<vd.getReasonForVisit().size();i++)
//        {
//            System.out.println(vd.getReasonForVisit().get(i)+"\n");
//            visitStats2.setText(visitStats2.getText()+vd.getReasonForVisit().get(i)+"\t");
//            visitStats2.setText(visitStats2.getText()+"\t"+vd.getVisitDate().get(i)+"\n");
//        }
        //write stats code here, text boxes to take input for date range and all that        
    }
    @FXML
    public void getBackToFirst(ActionEvent event)throws IOException
    {
        //creates a new scene to use
        FXMLLoader loader = new FXMLLoader();  
        loader.setLocation(getClass().getResource("StatsPage.fxml"));
        Parent firstPageParent = loader.load();
        Scene firstScene = new Scene(firstPageParent);
        Stage firstStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        firstStage.setScene(firstScene);
        firstStage.show();
        
    }
    @FXML
    public void setFromDate(int y,int m,int d)
    {   
       String s=""+y+"-"+m+"-"+d;
       fromDate=s;  
    }
    @FXML
    public void setToDate(int y,int m,int d)
    {
       String s=""+y+"-"+m+"-"+d;
       toDate=s; 
    } 
    private static java.sql.Date convertUtilToSql(java.util.Date uDate)
    {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    } 
    public void displayChart2()
    {
        
        XYChart.Series set1=new XYChart.Series();      
        set1.getData().add(new XYChart.Data("Health issue",JDBC.getStudentByDateMale("Health issue",fromDate,toDate)));       
        set1.getData().add(new XYChart.Data("Legal issue",JDBC.getStudentByDateMale("Legal issue",fromDate,toDate)));
        set1.getData().add(new XYChart.Data("Finance",JDBC.getStudentByDateMale("Finance",fromDate,toDate)));
        set1.getData().add(new XYChart.Data("Meet with staff",JDBC.getStudentByDateMale("Meet with staff",fromDate,toDate)));
        set1.getData().add(new XYChart.Data("Facility issue",JDBC.getStudentByDateMale("Facility issue",fromDate,toDate)));
        barChart2.getData().addAll(set1);
        XYChart.Series set2=new XYChart.Series();        
        set2.getData().add(new XYChart.Data("Health issue",JDBC.getStudentByDateFemale("Health issue",fromDate,toDate)));       
        set2.getData().add(new XYChart.Data("Legal issue",JDBC.getStudentByDateFemale("Legal issue",fromDate,toDate)));
        set2.getData().add(new XYChart.Data("Finance",JDBC.getStudentByDateFemale("Finance",fromDate,toDate)));
        set2.getData().add(new XYChart.Data("Meet with staff",JDBC.getStudentByDateFemale("Meet with staff",fromDate,toDate)));
        set2.getData().add(new XYChart.Data("Facility issue",JDBC.getStudentByDateFemale("Facility issue",fromDate,toDate)));
        barChart3.getData().addAll(set2);
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
