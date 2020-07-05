package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import ListView.InventoryItem;
import ListView.OrderItem;
import javaBean.Inventory;
import javaBean.InventoryGenerator;
import javaBean.Order;
import javaBean.Product;
import javaBean.Reposite;
import javaBean.Shelf;
import javaBean.accurateInventory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import util.CheckoutHelper;
import util.XMLUtil;

public class InventoryController implements InventoryShare
{
	@FXML private Button searchBtn, reportBtn, refreshBtn;
	@FXML private TextField locText, shelfText;
	@FXML private ListView<Product> productList;

	private Reposite reposite;
	private String name;
	private InventoryGenerator inventoryGenerator;
	private accurateInventory inventory;
	
	private ArrayList<Product> list;
	
	@Override
	public void setReposite(Reposite reposite)
	{
		this.reposite = reposite;
	}
	
	@Override
	public void setInventoryGenerator(InventoryGenerator inventoryGenerator)
	{
		this.inventoryGenerator = inventoryGenerator;
	}
	
	private void initList()
	{
		try
		{
			inventory = inventoryGenerator.getInventory(name);
			list = reposite.getShelf(inventory.getShelfName()).getProducts();
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			UiUtil.showAlert(e.getMessage());
			return;
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
	public void refreshBtnPress()
	{
		initList();
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
			UiUtil.showAlert("�Ҳ���Ӧ��Ʒ");
		}
	}
	
	@FXML
	public void reportBtnPress()
	{
		ArrayList<Product> pros = new ArrayList<>();
		HashMap<String, Integer> record = inventory.printReport();
		for (Product p : productList.getItems())
		{
			String id = p.getId();
			if (record.containsKey(id))
			{
				Product pro = p.clone();
				pro.setAmount(record.get(id));
				pros.add(pro);
			}
		}
		
		productList.getItems().clear();
		productList.getItems().addAll(pros);
		
		inventoryGenerator.done(inventory);
	}

	@Override
	public void setCheckoutHelper(CheckoutHelper helper)
	{	// nothing
	}

	@Override
	public void setName(String name)
	{	
		this.name = name;
	}
}
