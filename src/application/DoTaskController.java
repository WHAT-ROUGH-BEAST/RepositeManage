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
	private Reposite reposite;
 
	@FXML
	public void refreshBtnPress()
	{
		if (!itemList.getItems().isEmpty())
		{
			UiUtil.showAlert("请先完成当前任务");
			return;
		}
		
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
			e.printStackTrace();
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
			UiUtil.showAlert("目前没有本地任务，请刷新");
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
		this.reposite = reposite;
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

	@Override
	public void setName(String name)
	{	//nothing
	}
}
