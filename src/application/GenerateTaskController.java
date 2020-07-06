package application;

import java.util.ArrayList;
import java.util.List;

import ListView.ListItem;
import ListView.OrderItem;
import javaBean.OnlyRepoLocation;
import javaBean.Order;
import javaBean.Product;
import javaBean.Reposite;
import javaBean.ReturnOrder;
import javaBean.SimpleOrder;
import javaBean.SimpleProduct;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import util.CheckoutHelper;
import util.Location;
import util.XMLUtil;

public class GenerateTaskController implements DataShare
{
	@FXML private Button refreshBtn, checkoutBtn, importBtn, returnBtn;
	@FXML private ListView<Order> orderList;
	@FXML private TextField importText; 
	
	private CheckoutHelper checkoutHelper;
	
	@FXML
	public void refreshBtnPress()
	{
		orderList.getItems().clear();
		List<Order> orders = checkoutHelper.getOrders();
		
		for (Order o : orders)
		{
			orderList.getItems().add(o);
		} 
	}
	
	@FXML 
	public void returnBtnPress()
	{
		Order order = new SimpleOrder();
		order = new ReturnOrder(order);
		order.setBuyerName("XNOMD2");
		try
		{
			Location l = new OnlyRepoLocation();
			l.setLocation(l.getRepo(), "A", 1);
			order.addProduct((Product) XMLUtil.getBean("Productconfig", 
					"33783757-e89e-3262-f0a4-e9db956ca1c3", 2, l));
//			order.addProduct(product);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		orderList.getItems().add(order);
		checkoutHelper.addOrder(order);
	}
	
	@FXML
	public void importBtnPress()
	{
		String text = importText.getText();
		String type = null;
		try
		{
			String end = text.split("x")[text.split("x").length -1];
			if (end.equals("ls") || end.equals("lsx"))
				type = "Excel";
			else
				type = "Xml";
			// "C:\Users\18069\Desktop\orderList.xml"
			
			checkoutHelper.importOrder(type, text);
			refreshBtnPress();
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			UiUtil.showAlert(e.getMessage());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			UiUtil.showAlert(e.getMessage());
			return;
		}
	}
	
	@FXML
	public void checkoutBtnPress()
	{
		try
		{
			checkoutHelper.checkout();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			UiUtil.showAlert(e.getMessage());
			return;
		}
		
		orderList.getItems().clear();
	}
	
	@Override
	public void setReposite(Reposite reposite)
	{	// nothing
	}
	
	@Override
	public void setCheckoutHelper(CheckoutHelper helper)
	{
		this.checkoutHelper = helper;
		initList();
	}
	
	private void initList()
	{	
		ArrayList<Order> list = new ArrayList<>();
		
		orderList.setItems(FXCollections.observableArrayList(list));
		
		orderList.setCellFactory(new Callback<ListView<Order>, ListCell<Order>>(){
			@Override
			public ListCell<Order> call(ListView<Order> list) {
				OrderItem i = new OrderItem();
				return i;
			}
		});	
	}
	
	@Override
	public void setName(String name)
	{	//nothing
	}
}
