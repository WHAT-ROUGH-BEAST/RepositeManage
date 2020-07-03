package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Main extends Application
{
	private String identity;
	
	@Override
	public void start(Stage primaryStage)
	{ 
		try
		{
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/Main.fxml"));
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
			AnchorPane root = (AnchorPane) fxmlLoader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Reposite Manage : " + identity);
			primaryStage.show();
			
			((MainViewController) fxmlLoader.getController()).setIdentity(identity);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setIdentity(String identity)
	{
		if (null == identity)
			throw new RuntimeException();
		
		this.identity = identity;
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
