package ListView;

import java.io.IOException;
import java.util.ArrayList;

import application.UiUtil;
import javaBean.Product;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ItemView
{
	@FXML private Button removeBtn;
	@FXML private Label label;
	@FXML private TextField amountText;
	@FXML private HBox hbox;
	
	private ListView<Product> list;
	
	private Product product;
	
	public ItemView()
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListView/ItemView.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
	}
	
	public void setList(ListView<Product> list) 
	{
		this.list = list;
	}
	
	public void setInfo(Product product)
	{
		this.product = product;
		
		label.setText("ID: " + product.getId());
	}
	
	@FXML
	public void removeBtnPress(ActionEvent e)
	{
		list.getItems().remove(product);
	}
	
	@FXML 
	public void onAmountTextType()
	{
		try
		{
			int amount = Integer.parseInt(amountText.getText());
			if (amount < 0)
				throw new Exception();
			
			product.setAmount(amount);
		}
		catch (Exception e)
		{
			UiUtil.showAlert("不合法数量");
		}
	}
	
	public HBox getBox()
	{
		return hbox;
	}
}
