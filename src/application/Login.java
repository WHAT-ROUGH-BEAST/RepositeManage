package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Login extends Application
{
	@FXML
	private Button userBtn, managerBtn, employeeBtn;
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		try
		{
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/Login.fxml"));
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
			AnchorPane root = (AnchorPane) fxmlLoader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@FXML
	public void userBtnPress()
	{
		startMainView("user");
	}

	@FXML
	public void managerBtnPress()
	{
		startMainView("manager");
	}

	@FXML
	public void employeeBtnPress()
	{
		startMainView("employee");
	}

	private void startMainView(String identity)
	{		
		Platform.runLater(() ->
		{
			try
			{
				Main m = new Main();
				m.setIdentity(identity);
				m.start(new Stage());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
