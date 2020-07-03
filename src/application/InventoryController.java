package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import ListView.InventoryItem;
import ListView.OrderItem;
import javaBean.Inventory;
import javaBean.Order;
import javaBean.Product;
import javaBean.Reposite;
import javaBean.Shelf;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import util.CheckoutHelper;
import util.XMLUtil;

public class InventoryController implements DataShare
{
	@FXML private Button searchBtn, reportBtn;
	@FXML private TextField locText, shelfText;
	@FXML private ListView<Product> productList;

	private Reposite reposite;
	private Inventory inventory;
	
	private ArrayList<Product> list;
	
	@Override
	public void setReposite(Reposite reposite)
	{
		this.reposite = reposite;
		initList();
		inventory = (Inventory) XMLUtil.getBean("Inventoryconfig", reposite);
	}
	
	private void initList()
	{
		list = new ArrayList<>();
		for (Shelf s : reposite.getShelfs())
		{
			for (Product p : s.getProducts())
			{
				list.add(p);
			}
		}
		
		Collections.sort(list);
		
		productList.setItems(FXCollections.observableArrayList(list));
		
		productList.setCellFactory(new Callback<ListView<Product>, ListCell<Product>>(){
			@Override
			public ListCell<Product> call(ListView<Product> list) {
				InventoryItem i = new InventoryItem();
				i.setInventory(inventory);
				return i;
			}
		});
	}
	
	@FXML
	public void searchBtnPress()
	{
		String shelf = shelfText.getText().trim();
		String loc = locText.getText();
		if (shelf.isEmpty() && loc.isEmpty())
		{
			productList.setItems(FXCollections.observableArrayList(list));
			return;
		}
		
		productList.getItems().clear();
		
		try
		{
			Product product = inventory.getProduct("defaultReposite",
					shelf, Integer.parseInt(loc));
			if (null == product)
				throw new Exception();
			
			productList.getItems().add(product);
		}
		catch (Exception e)
		{
			UiUtil.showAlert("找不对应产品");
		}
	}
	
	@FXML
	public void reportBtnPress()
	{
		 HashMap<String, Integer> record = inventory.printReport();
		 for (Product p : productList.getItems())
		 {
			 if (record.containsKey(p.getId()));
			 p.setAmount(record.get(p.getId()));
		 }
	}

	@Override
	public void setCheckoutHelper(CheckoutHelper helper)
	{	// nothing
	}

}
