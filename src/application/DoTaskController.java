package application;

import java.util.*;

import ListView.OrderItem;
import javaBean.Order;
import javaBean.PickingTask;
import javaBean.Product;
import javaBean.Reposite;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import util.CheckoutHelper;

public class DoTaskController implements DataShare
{
	@FXML private Button refreshBtn, pickBtn;
	@FXML private ListView<String> itemList;
	@FXML private TextField textField;
	
	private CheckoutHelper checkoutHelper;
	private PickingTask task;
 
	@FXML
	public void refreshBtnPress()
	{
		itemList.getItems().clear();
		try
		{
			task = checkoutHelper.getTask();
			
			for (Product p : task.getTaskItems())
			{
				itemList.getItems().add(
						"ID: " + p.getId() + 
						"  \t Amount: " + p.getAmount() + 
						"\t Location: " + p.getLocation());
			}
		}
		catch (Exception e)
		{
			UiUtil.showAlert(e.getMessage());
			task = null;
			return;
		}
	}
	
	@FXML 
	public void pickBtnPress()
	{
		if (null == task)
		{
			UiUtil.showAlert("Ŀǰû�б���������ˢ��");
			return;
		}
		
		try
		{
			String id = textField.getText();
			task.done(id);
			for (String s : itemList.getItems())
			{
				if (s.contains(id))
				{
					itemList.getItems().remove(s);
					break;
				}
			}
		}
		catch (Exception e)
		{
			UiUtil.showAlert(e.getMessage());
		}
	}
	
	@Override
	public void setReposite(Reposite reposite)
	{
	}

	@Override
	public void setCheckoutHelper(CheckoutHelper helper)
	{
		this.checkoutHelper = helper;
		initList();
	}
	
	private void initList()
	{
		List<String> list = new LinkedList<>();
		
		itemList.setItems(FXCollections.observableArrayList(list));
	}
}
