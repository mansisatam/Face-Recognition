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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sharathkashyap
 */
public class DetailsDisplayController implements Initializable
{

    ObservableList<String> reasonsList=FXCollections.observableArrayList("Health issue","Legal issue","Finance","Meet with staff","Facility issue");
    @FXML
    private Label name;  
    @FXML
    private Label andID;
    @FXML
    private Label age;
    @FXML
    private Label gender;
    @FXML
    private Label track;
    @FXML
    private Label lastVisited;
    @FXML
    private Label timesVisited;
    @FXML
    private ImageView image;
    @FXML
    private Label message;
    @FXML
    private ChoiceBox<String> visitReason;
    @FXML
    private Button back; 
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    public void displayDetails(String ID) 
    {
        Student currStudent = JDBC.getStudentDetails(ID);
        if (currStudent == null)
        {
            message.setText("This user does not exist. Go back and register.");
        } 
        else 
        { 
            andID.setText(ID);
            name.setText(currStudent.getFirstName() + " " + currStudent.getLastName());
            age.setText(currStudent.getAge() + "");
            gender.setText(currStudent.getGender());
            track.setText(currStudent.getTrack());
            lastVisited.setText(currStudent.getLastVisit() + "");
            timesVisited.setText(currStudent.getTimesVisited() + "");
            Image img = new Image("file:" + currStudent.getImagePath());
            image.setImage(img);             
        }
    }
    
    @FXML
    public void displayStats(ActionEvent event) throws IOException
    {
        //creates a new scene to use
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("StatsDisplay.fxml"));
        Parent statsViewParent = loader.load();
        StatsDisplayController controller = loader.getController();
        controller.displayStats(andID.getText());
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
        controller.displayStats2(andID.getText());
        Scene statsScene = new Scene(statsViewParent);
        Stage statsStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        statsStage.setScene(statsScene);
        statsStage.show();
    }
    
    @FXML
    public void updateVisitDetails()
    {
        String reason = visitReason.getValue();
        java.sql.Timestamp currentTime = new java.sql.Timestamp(new java.util.Date().getTime());
        JDBC.updateVisitNumberAndDate(andID.getText(), Integer.parseInt(timesVisited.getText()), currentTime);
        String addStatus = JDBC.addVisitDetails(reason, andID.getText(), currentTime);
        message.setText(addStatus);    
    }
    
    @FXML
    public void getBackToFirst(ActionEvent event)throws IOException
    {
        //creates a new scene to use
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("RecogFX.fxml"));
        Parent firstPageParent = loader.load();
        Scene firstScene = new Scene(firstPageParent);
        Stage firstStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        firstStage.setScene(firstScene);
        firstStage.show();
        //new FXController().init();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        visitReason.setValue("Health issue");
        visitReason.setItems(reasonsList);
    }
}
