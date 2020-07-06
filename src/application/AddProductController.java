package application;

import java.util.ArrayList;
import java.util.Collections;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import javaBean.Product;
import javaBean.Reposite;
import javaBean.Shelf;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import util.CheckoutHelper;

public class AddProductController implements DataShare
{
	@FXML private Button addBtn;
	@FXML private TextField idText, amountText;
	@FXML private ListView<String> productList;
	
	private Reposite reposite;
	
	private ArrayList<String> list = null;
	
	@Override
	public void setReposite(Reposite reposite)
	{
		this.reposite = reposite;
		initList();
	}

	@FXML
	public void addBtnPress()
	{
		String id = idText.getText();
		int amount = 0;
		try
		{
			amount = Integer.parseInt(amountText.getText());
		}
		catch (Exception e)
		{
			UiUtil.showAlert("数量不合法");
			return;
		}
		
		try
		{
			reposite.addProduct(id, amount);
		}
		catch (Exception e)
		{
			UiUtil.showAlert(e.getMessage());
		}
		
		initList(); 
	}
	
	private void initList()
	{
		list = new ArrayList<>();
		
		for (Shelf s : reposite.getShelfs())
		{
			for (Product p : s.getProducts())
			{
				list.add("ID: " + p.getId() + 
						"  \t Amount: " + p.getAmount() + 
						"\t Location: " + p.getLocation());
			}
		}
		
		Collections.reverse(list);
		
		productList.setItems(FXCollections.observableArrayList(list));	
	}
	
	@Override
	public void setCheckoutHelper(CheckoutHelper helper)
	{	// nothing
	}

	@Override
	public void setName(String name)
	{	// nothing
	}

}
