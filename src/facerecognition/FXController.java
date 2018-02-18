package facerecognition;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
//import org.opencv.face.Face;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.face.FaceRecognizer;
/**
 * The application logic is implemented here. It handles the button for
 * starting/stopping the camera, the acquired video stream, the relative
 * controls and the face detection and recognition.
 * @author siddharth
 * @since 2017-11-20		
 */
public class FXController
{
	@FXML
	private Button cameraButton;
	@FXML
	private ImageView originalFrame;// the FXML area for showing the current frame
	@FXML
	private TextField newUserName;
        @FXML
        public Button stats;
        @FXML
	private Button newUserNameSubmit;
	private ScheduledExecutorService timer;// a timer for acquiring the video stream
	private VideoCapture capture;// the OpenCV object that performs the video capture
	private boolean cameraActive;// a flag to change the button behavior
	private CascadeClassifier faceCascade;// face cascade classifier
	private int absoluteFaceSize;
	public int index = 0,counter = 0,flag;
	public int ind = 0;
        public boolean register;
	public String newname;// New user Name for a training data
	public HashMap<Integer, String> names = new HashMap<>();// Names of the people from the training set
	public int random = (int )(Math.random() * 20 + 3);// Random number of a training set
        FirstPageController fpc = new FirstPageController();
        ActionEvent event;
	/**
	 * Init the controller, at start time
	 */
	public void init()
	{
		this.capture = new VideoCapture();
		this.faceCascade = new CascadeClassifier();
		this.absoluteFaceSize = 0;
                this.register = false;
                flag = 0;
		this.loadHaar("resources/haarcascades/haarcascade_frontalface_alt.xml");
		trainModel(); // Takes some time thus use only when training set is updated 
	}
	/**
	 * The action triggered by pushing the button on the GUI
	 */
	@FXML
	protected void startCamera(ActionEvent event)
	{
            this.newUserName.setDisable(true);
            this.newUserNameSubmit.setDisable(true);
            this.event = event;
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
                this.newUserName.setDisable(false);
                this.newUserNameSubmit.setDisable(false);
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
		this.originalFrame.setImage(null);// clean the frame
		index = 0;// Clear the parameters for new user data collection
		newname = "";
	    }
	}
	/**
	 * Get a frame from the opened video stream (if any)
	 * @return the {@link Image} to show
	 */
	private Image grabFrame()
	{
	    Image imageToShow = null;
	    Mat frame = new Mat();
       	    if (this.capture.isOpened())// check if the capture is open
	    {
		try
		{
		    this.capture.read(frame);  // read the current frame
		    if (!frame.empty())// if the frame is not empty, process it
		    {
			this.detectAndDisplay(frame);// face detection
			imageToShow = mat2Image(frame);// convert the Mat object (OpenCV) to Image (JavaFX)
		    }	
		}
		catch (Exception e)
		{
		    System.err.println("ERROR: " + e);
		}
	    }	
	    return imageToShow;
	}
	private void trainModel () 
        {
	    File root = new File("resources/trainingset/combined/");// Read the data from the training set
	    FilenameFilter imgFilter = new FilenameFilter() 
            {
                @Override
		public boolean accept(File dir, String name) 
                {
		    name = name.toLowerCase();
		    return name.endsWith(".png");
		}
            };
            File[] imageFiles = root.listFiles(imgFilter);
            List<Mat> images = new ArrayList<>();
            System.out.println("THE NUMBER OF IMAGES READ IS: " + imageFiles.length);
	    List<Integer> trainingLabels = new ArrayList<>();
            Mat labels = new Mat(imageFiles.length,1,CvType.CV_32SC1);
            int counter = 0;
            for (File image : imageFiles) 
            {
		Mat img = Imgcodecs.imread(image.getAbsolutePath());// Parse the training set folder files
		Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);// Change to Grayscale and equalize the histogram
		Imgproc.equalizeHist(img, img);
		int label = Integer.parseInt(image.getName().split("\\-")[0]);// Extract label from the file name
		String labnname = image.getName().split("\\_")[0]; // Extract name from the file name and add it to names HashMap
		String name = labnname.split("\\-")[1];
		names.put(label, name);
		images.add(img);// Add training set images to images Mat
                labels.put(counter, 0, label);
		counter++;
            }
            FaceRecognizer faceRecognizer = Face.createLBPHFaceRecognizer();
            faceRecognizer.train(images, labels);
            faceRecognizer.save("traineddata");
	}
	/**
         * Method for face recognition grabs the detected face and matches it with the training set. 
         * If recognized the name of the person is printed below the face rectangle.
	 * @return 
	 */
	private double[] faceRecognition(Mat currentFace) 
        {
            int[] predLabel = new int[1]; // predict the label
            double[] confidence = new double[1];
            int result = -1; 
            FaceRecognizer faceRecognizer = Face.createLBPHFaceRecognizer();
            faceRecognizer.load("traineddata");
            faceRecognizer.predict(currentFace,predLabel,confidence);
            result = predLabel[0];
            return new double[] {result,confidence[0]};
	}
	/**
	 * Method for face detection and tracking
	 * @param frame it looks for faces in this frame
	 */
	private void detectAndDisplay(Mat frame) throws InterruptedException, IOException
	{
	    MatOfRect faces = new MatOfRect();
	    Mat grayFrame = new Mat();
	    Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY); // convert the frame in gray scale
	    Imgproc.equalizeHist(grayFrame, grayFrame); // equalize the frame histogram to improve the result
	    if (this.absoluteFaceSize == 0)// compute minimum face size (20% of the frame height, in our case)
	    {
		int height = grayFrame.rows();
		if (Math.round(height * 0.2f) > 0)
		{
		    this.absoluteFaceSize = Math.round(height * 0.2f);
		}
	    }
	    this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
	    new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());// detect faces	
	    Rect[] facesArray = faces.toArray(); // each rectangle in faces is a face: draw them!
	    for (int i = 0; i < facesArray.length; i++) 
            {
                Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);
                Rect rectCrop = new Rect(facesArray[i].tl(), facesArray[i].br()); // Crop the detected faces
                Mat croppedImage = new Mat(frame, rectCrop);
                Imgproc.cvtColor(croppedImage, croppedImage, Imgproc.COLOR_BGR2GRAY);// Change to gray scale
                Imgproc.equalizeHist(croppedImage, croppedImage);// Equalize histogram
                Mat resizeImage = new Mat();// Resize the image to a default size
                Size size = new Size(70,70);
                Imgproc.resize(croppedImage, resizeImage, size);
                // if yes start collecting training data (50 images is enough)
                if ((newname != null && !newname.isEmpty())) 
                {
                    if (index<50) 
                    {
                        flag = 0;
                        Imgcodecs.imwrite("resources/trainingset/combined/" + random + "-" + newname + "_" + (index++) + ".png", resizeImage);
                    }
                    else 
                        flag = 1;
                }
                if(register == false)
                {
                    double[] returnedResults = faceRecognition(resizeImage);
                    double prediction = returnedResults[0];
                    double confidence = returnedResults[1];
                    int label = (int) prediction;
                    String name = "";
                    if(confidence > 71 && confidence < 95)
                    {
                        counter++;
                        if (names.containsKey(label)) 
                        {
                            name = names.get(label);
                        }
                        else
                            name = "Unknown";
                    }
                    else 
                    {
                        name = "Unknown";
                        if(counter > 0)
                        counter--;
                    }
                    String box_text = name;// Create the text we will annotate the box with:
                    double pos_x = Math.max(facesArray[i].tl().x - 10, 0);// Calculate the position for annotated text (make sure we don't put illegal values in there):
                    double pos_y = Math.max(facesArray[i].tl().y - 10, 0);
                    Imgproc.putText(frame, box_text, new Point(pos_x, pos_y),Core.FONT_HERSHEY_PLAIN, 1.0, new Scalar(0, 255, 0, 2.0));// And now put it into the image:
                    if((confidence > 71 && confidence < 95)&& counter > 5 && !name.equals("Unknown"))
                    {
                        this.capture.release();// release or close the camera 
                        counter = 0; // set everything to zero for next time
                        Platform.runLater(() -> cameraButton.setText("Start Camera"));
                        final String n = name;
                        Platform.runLater(() -> {
                            try {
                                fpc.getAndrewID(event,n);
                            } catch (IOException ex) {
                                Logger.getLogger(FXController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        name = null;
                    }
                }
                else if(flag == 1 && register == true)
                {
                    this.capture.release();
                    counter = 0;
                    Platform.runLater(() -> {
                        try {
                            fpc.registerNewUser(event);
                        } catch (IOException ex) {
                            Logger.getLogger(FXController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });    
                }
                    
            }
        }
	@FXML
	protected void newUserNameSubmitted(ActionEvent event) 
        {
            this.event = event;
	    if ((newUserName.getText() != null && !newUserName.getText().isEmpty())) 
            {
		newname = newUserName.getText();
		System.out.println("BUTTON HAS BEEN PRESSED");
		newUserName.clear();
                register = true;
                startCamera(event);
	    }
	}
	/**
	 * Method for loading a classifier trained set from disk
	 * @param classifierPath
	 * the path on disk where a classifier trained set is located
	 */
	private void loadHaar(String classifierPath)
        {
	    this.faceCascade.load(classifierPath);// load the classifier(s)
	    this.cameraButton.setDisable(false); // now the video capture can start
	}
	/**
	 * Convert a Mat object (OpenCV) in the corresponding Image for JavaFX
	 * @param frame  the {@link Mat} representing the current frame
	 * @return the {@link Image} to show
	 */
	private Image mat2Image(Mat frame)
        {
	    MatOfByte buffer = new MatOfByte();// create a temporary buffer
	    Imgcodecs.imencode(".png", frame, buffer); // encode the frame in the buffer, according to the PNG format
	    return new Image(new ByteArrayInputStream(buffer.toArray()));// build and return an Image created from the image encoded in the buffer
        }
        
        @FXML
        public void getStats(ActionEvent event) throws IOException
        {
        //creates a new scene to use
        FXMLLoader loader = new FXMLLoader();  
        loader.setLocation(getClass().getResource("StatsPage.fxml")); 
        Parent statsViewParent = loader.load();
        Scene statsScene = new Scene(statsViewParent);
        Stage statsStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        statsStage.setScene(statsScene);
        statsStage.show();
        } 
}

