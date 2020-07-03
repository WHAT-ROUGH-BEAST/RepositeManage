package application;

import java.util.ArrayList;
import java.util.List;

import ListView.ListItem;
import ListView.OrderItem;
import javaBean.Order;
import javaBean.Product;
import javaBean.Reposite;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import util.CheckoutHelper;
import util.XMLUtil;

public class GenerateTaskController implements DataShare
{
	@FXML private Button refreshBtn, checkoutBtn;
	@FXML private ListView<Order> orderList;
	
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
	public void checkoutBtnPress()
	{
		try
		{
			checkoutHelper.checkout();
		}
		catch (Exception e)
		{
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
}
