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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;

/**
 * FXML Controller class
 * @author sharathkashyap
 */
public class StatsDisplayController implements Initializable 
{
    @FXML
    private TextArea visitStats;
    @FXML
    private Button back;
    @FXML
    private BarChart<?, ?> barChart;
    @FXML
    private CategoryAxis x;
    private int month;
    @FXML
    public void displayStats(String ID)
    {
        VisitDetails vd = JDBC.getVisitDetails(ID);
        int i;
        visitStats.setText(vd.getsID()); 
        for(i=0;i<vd.getReasonForVisit().size();i++)
        {
            System.out.println(vd.getReasonForVisit().get(i)+"\n");
            visitStats.setText(visitStats.getText()+vd.getReasonForVisit().get(i)+"\t");
            visitStats.setText(visitStats.getText()+"\t"+vd.getVisitDate().get(i)+"\n");
        }       
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
    public void setMonth(String m)
    {
        
        switch(m)
        {
                case "January":
                month=1;
                break;
                case "February":
                month=2;
                break;
                case "March":
                month=3;
                break;
                case "April":
                month=4;
                break;
                case "May":
                month=5;
                break;
                case "June":
                month=6;
                break;
                case "July":
                month=7;
                break;
                case "August":
                month=8;
                break;
                case "September":
                month=9;
                break;
                case "October":
                month=10;
                break;
                case "November":
                month=11;
                break;
                case "December":
                month=12;
                break;
        }
    }
    public void displayChart()
    {
        XYChart.Series set1=new XYChart.Series();        
        set1.getData().add(new XYChart.Data("Health issue",JDBC.getStudentByCategoryandMonth("Health issue",month)));       
        set1.getData().add(new XYChart.Data("Legal issue",JDBC.getStudentByCategoryandMonth("Legal issue",month)));
        set1.getData().add(new XYChart.Data("Finance",JDBC.getStudentByCategoryandMonth("Finance",month)));
        set1.getData().add(new XYChart.Data("Meet with staff",JDBC.getStudentByCategoryandMonth("Meet with staff",month)));
        set1.getData().add(new XYChart.Data("Facility issue",JDBC.getStudentByCategoryandMonth("Facility issue",month)));
        barChart.getData().addAll(set1);
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
