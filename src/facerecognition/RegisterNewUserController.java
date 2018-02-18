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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sharathkashyap
 */
public class RegisterNewUserController implements Initializable {

    /*@FXML
    private Button cameraButton;*/
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField age;
    @FXML
    private TextField gender;
    @FXML
    private TextField track;
    @FXML
    private Button register;
    @FXML
    private Button back;
    @FXML
    private Label status;
    /*@FXML
    private ImageView originalFrame;
    @FXML
    private Button captureImage;
    @FXML
    private boolean cameraActive;
    private ScheduledExecutorService timer;// a timer for acquiring the video stream
    private VideoCapture capture;// the OpenCV object that performs the video capture
    /*protected void startCamera(ActionEvent event)
	{
	    originalFrame.setFitWidth(600);// set a fixed width for the frame
	    originalFrame.setPreserveRatio(true);  // preserve image ratio
	    if (!this.cameraActive)
	    {
		this.capture.open(0);// start the video capture
		if (this.capture.isOpened())// is the video stream available?
		{
		    this.cameraActive = true;
		    Runnable frameGrabber;// grab a frame every 33 ms (30 frames/sec)
                    frameGrabber = () -> {
                        Image imageToShow = grabFrame();
                        originalFrame.setImage(imageToShow);
                    };
		    this.timer = Executors.newSingleThreadScheduledExecutor();
		    this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
		    this.cameraButton.setText("Stop Camera");// update the button content
		}
		else
		{
		    System.err.println("Failed to open the camera connection...");// log the error
		}
	    }
	    else
	    {
		this.cameraActive = false;// the camera is not active at this point
		this.cameraButton.setText("Start Camera");// update again the button content
		try
		{
		    this.timer.shutdown();// stop the timer
		    this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
		}
		catch (InterruptedException e)
		{
		    System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e); // log the exception
		}
		this.capture.release();// release the camera
		this.originalFrame.setImage(null);// clean 
	    }
	}
    private Image grabFrame()
    {
	Image imageToShow = null;
	Mat frame = new Mat();
       	if (this.capture.isOpened())// check if the capture is open
            this.capture.read(frame); 
        imageToShow = mat2Image(frame);
	Imgcodecs.imwrite("resources/trainingset/combined/" +"skandima" +".png", frame);
        return imageToShow;
    }
    private Image mat2Image(Mat frame)
    {
	MatOfByte buffer = new MatOfByte();// create a temporary buffer
	Imgcodecs.imencode(".png", frame, buffer); // encode the frame in the buffer, according to the PNG format
	return new Image(new ByteArrayInputStream(buffer.toArray()));// build and return an Image created from the image encoded in the buffer
    }*/
    @FXML
    public void registerUser()
    {
        //access from webcam and get image location.
        String fn = firstName.getText();
        String ln = lastName.getText();
        String imgLoc = "resources/trainingset/combined/";
        String studAge = age.getText();
        String gen = gender.getText();
        String tr = track.getText();
        String addStatus = "Unable to register. Re-check input";
        if (fn.equals("") || ln.equals("") || studAge.equals("") || gen.equals("") || tr.equals("")) 
        {
            addStatus = "Empty fields not allowed.";
        } else 
        {
            try 
            {
                addStatus = JDBC.registerNewStudent(fn, ln, imgLoc, Integer.parseInt(studAge), gen, tr);
                
            } catch (Exception e) 
            { 
                addStatus = "Age must be a number.";  
            }
        }
        status.setText(addStatus);
    }

    @FXML
    public void getBackToFirst(ActionEvent event) throws IOException
    {
        //creates a new scene to use
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("RecogFX.fxml"));
        Parent firstPageParent = loader.load();
        FirstPageController controller = loader.getController();
        Scene firstScene = new Scene(firstPageParent);
        Stage firstStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        firstStage.setScene(firstScene);
        firstStage.show();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
