package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import ListView.ListItem;
import javaBean.Order;
import javaBean.Product;
import javaBean.Reposite;
import javaBean.Shelf;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import util.CheckoutHelper;
import util.XMLUtil;

public class OrderController implements DataShare
{
	@FXML private Button addBtn, doneBtn;
	@FXML private TextField textField, nameText;
	@FXML private ListView<Product> productList;
	
	private Reposite reposite;
	private CheckoutHelper checkoutHelper;
	
	@FXML
	public void addBtnPress()
	{
		try
		{
			Product product = reposite.search(textField.getText());
			if (null == product)
				throw new Exception();
			
			// .clone()
			productList.getItems().add(product.clone());
		}
		catch (Exception e) {}
	}
	
	@FXML
	public void doneBtnPress()
	{
		Order order = (Order) XMLUtil.getBean("Orderconfig");
		order.registerRepos(reposite);
		
		try
		{
			String name = nameText.getText();
			order.setBuyerName(name);
			nameText.clear();
		}
		catch (Exception e)
		{
			UiUtil.showAlert("姓名不合法"); 
			return;
		}
		
		for (Product p : productList.getItems())
		{
			try
			{
				order.addProduct(p);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				UiUtil.showAlert(p.getId() + "  库存不足");
				return;
			}
		}
		productList.getItems().clear();
		textField.clear();
		
		// checkoutHelper;
		checkoutHelper.addOrder(order);
		
		UiUtil.showAlert("购物成功");
	}
	
	@Override
	public void setReposite(Reposite reposite)
	{
		this.reposite = reposite;
		initList();
	}
	
	private void initList()
	{	
		ArrayList<Product> list = new ArrayList<>();
		
		productList.setItems(FXCollections.observableArrayList(list));
		
		productList.setCellFactory(new Callback<ListView<Product>, ListCell<Product>>(){
			@Override
			public ListCell<Product> call(ListView<Product> list) {
				ListItem i = new ListItem();
				i.setList(productList);
				return i;
			}
		});	
	}

	@Override
	public void setCheckoutHelper(CheckoutHelper helper)
	{
		checkoutHelper = helper;
	}
}
