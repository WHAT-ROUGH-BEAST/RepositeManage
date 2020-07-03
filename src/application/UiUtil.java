package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class UiUtil
{
	public static void showAlert(String warning)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("��ʾ");
		alert.setHeaderText(null);
		alert.setContentText(warning);

		alert.showAndWait();
	}
}
