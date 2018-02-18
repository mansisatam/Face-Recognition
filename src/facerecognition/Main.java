package facerecognition;

import org.opencv.core.Core;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * The main class for a JavaFX application. It creates and handle the main window with its resources (style, graphics, etc.)
 * This application handles a video stream and try to find any possible human face in a frame. 
 * It uses Haar classifier.
 * @author siddharth
 * @since (2017-11-20)
 */
public class Main extends Application
{
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
                        
			FXMLLoader loader = new FXMLLoader(getClass().getResource("RecogFX.fxml"));
			BorderPane root = (BorderPane) loader.load();
			root.setStyle("-fx-background-color: #B91729;");// set a whitesmoke background
			Scene scene = new Scene(root, 800, 600);// create and style a scene
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("CMU Entry Portal");// create the stage with the given title and the previously created scene
			primaryStage.setScene(scene);
			primaryStage.show();// show the GUI
			FXController controller = loader.getController();// init the controller
			controller.init();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		// load the native OpenCV library
		System.load("C:\\Users\\Siddharth\\Documents\\FaceRecognition\\opencv\\build\\java\\x64\\opencv_java310.dll");
		launch(args);
	}
}
