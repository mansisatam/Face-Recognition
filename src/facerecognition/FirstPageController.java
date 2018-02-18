/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facerecognition;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
/**
 *
 * @author sharathkashyap
 */
public class FirstPageController implements Initializable {
    
    ObservableList<String> monthList=FXCollections.observableArrayList("January","February","March","April","May","June","July","August","September","October","November","December");
    @FXML
    private Label label;
    @FXML
    private TextField andrewID;
    @FXML
    private Button button1; 
    @FXML
    private Button button2; 
    @FXML
    private ChoiceBox<String> monthChoice; 
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    public void registerNewUser(ActionEvent event) throws IOException {
        //creates a new scene to use
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("RegisterNewUser.fxml"));
        Parent registerUserParent = loader.load();
        //RegisterNewUserController controller = loader.getController();
        Scene detailsScene = new Scene(registerUserParent);
        Stage detailsStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        detailsStage.setScene(detailsScene);
        detailsStage.show();  
    }
    @FXML
    public void displayStats(ActionEvent event) throws IOException
    {
        //creates a new scene to use
        FXMLLoader loader = new FXMLLoader();  
        loader.setLocation(getClass().getResource("StatsDisplay.fxml")); 
        Parent statsViewParent = loader.load();
        StatsDisplayController controller = loader.getController();
        String month=monthChoice.getValue();
        controller.setMonth(month);    
        controller.displayChart();
        Scene statsScene = new Scene(statsViewParent);
        Stage statsStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        statsStage.setScene(statsScene);
        statsStage.show();
    }   
    @FXML
    public void displayStats2(ActionEvent event) throws IOException
    {
        //creates a new scene to use
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("StatsDisplay2.fxml"));
        Parent statsViewParent = loader.load();
        StatsDisplay2Controller controller = loader.getController();
        controller.setFromDate(fromDate.getValue().getYear(),fromDate.getValue().getMonthValue(),fromDate.getValue().getDayOfMonth());
        controller.setToDate(toDate.getValue().getYear(),toDate.getValue().getMonthValue(),toDate.getValue().getDayOfMonth());
        controller.displayChart2();
        Scene statsScene = new Scene(statsViewParent);
        Stage statsStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        statsStage.setScene(statsScene);
        statsStage.show();
    }
    @FXML
    public void getAndrewID(ActionEvent event,String name) throws IOException 
    {
            String text = name;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("DetailsDisplay.fxml"));
            Parent detailsViewParent = loader.load();
            DetailsDisplayController controller = loader.getController();
            controller.displayDetails(text);
            Scene detailsScene = new Scene(detailsViewParent);
            Stage detailsStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            detailsStage.setScene(detailsScene);
            detailsStage.show();
        } 
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        monthChoice.setValue("January");
        monthChoice.setItems(monthList);   
    }

}
