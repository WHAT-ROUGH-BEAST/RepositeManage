package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ResourceBundle;

import ListView.ListItem;
import javaBean.Product;
import javaBean.Reposite;
import javaBean.Shelf;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import util.CheckoutHelper;

public class SearchController implements DataShare
{
	@FXML private Button searchBtn;
	@FXML private TextField textField;
	@FXML private ListView<String> productList;
	
	private Reposite reposite;
	
	private ArrayList<String> list = null;
	
	@FXML
	public void searchBtnPress()
	{
		String text = textField.getText();
		if (text.isEmpty())
			initList();
		
		try
		{
			Product product = reposite.search(text);
			if (null == product)
				throw new Exception("找不到产品 id: " + text);
			
			productList.getItems().clear();
			productList.getItems().add(
					"ID: " + product.getId() + 
					"  \t Amount: " + product.getAmount() + 
					"\t Location: " + product.getLocation());
		}
		catch (Exception e)
		{
			UiUtil.showAlert(e.getMessage());
			initList();
		}
	}
	
	@Override
	public void setReposite(Reposite reposite)
	{
		this.reposite = reposite;
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
	{	//nothing
	}
	
	@Override
	public void setName(String name)
	{	//nothing
	}
}
