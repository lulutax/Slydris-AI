package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	 public Grafica G;
    	public static SplitPane root;
    	public static Scene menuScene;
   
    	@Override
    	public void start(Stage primaryStage) throws Exception {    		
    		 root = (SplitPane)FXMLLoader.load(getClass().getResource("grafica.fxml"));
    		 menuScene = new Scene(root, 800, 900);
    		primaryStage.setScene(menuScene);
    		primaryStage.setResizable(false);

    		primaryStage.show();
    	
    	}
    	
	public static void main(String[] args) {
    		
    		launch();
    	
    		
    	}


    	

    }

