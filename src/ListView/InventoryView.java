package ListView;

import java.io.IOException;

import application.UiUtil;
import javaBean.Inventory;
import javaBean.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class InventoryView
{
	@FXML private HBox hbox;
	@FXML private Label label;
	@FXML private TextField amountText;
	@FXML private Button updateBtn;
	
	private Product product;
	
	private Inventory inventory;
	
	public InventoryView()
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListView/InventoryView.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
	}
	
	public void setInfo(Product product)
	{
		this.product = product;
		
		label.setText("ID:" + product.getId() + 
				"   \t " + product.getLocation());
		amountText.setText(product.getAmount()+"");
	}
	
	@FXML
	public void updateBtnPress()
	{
		int amount = product.getAmount();
		try
		{
			amount = Integer.parseInt(amountText.getText());
			if (amount < 0)
				throw new Exception();
		}
		catch (Exception e)
		{
			UiUtil.showAlert("不合法数量");
			return;
		}
		
		inventory.modifyAmount(product, amount);
	}
	
	public void setInventory(Inventory inventory)
	{
		this.inventory = inventory;
	}
	
	public HBox getBox()
	{
		return hbox;
	}
}
