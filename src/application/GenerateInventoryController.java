package application;

import java.util.ArrayList;

import javaBean.InventoryGenerator;
import javaBean.Reposite;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import util.CheckoutHelper;

public class GenerateInventoryController implements InventoryShare
{
	@FXML private TextField employeeText, shelfText, timeText;
	@FXML private Button deployBtn, refreshBtn;
	@FXML private ListView<String> employeeList;
	
	private Reposite reposite;
	private InventoryGenerator inventoryGenerator;
	private ArrayList<String> employees = new ArrayList<>();

	@FXML
	public void deployBtnPress()
	{
		String employeeName = employeeText.getText();
		String shelf = shelfText.getText();
		String time = timeText.getText();
		
		if (employeeName.isBlank() || shelf.isBlank() || time.isBlank())
		{
			UiUtil.showAlert("不合法输入");
			return;
		}
		
		inventoryGenerator.deployInventory(reposite, employeeName, shelf, time);
		
		employees.add(employeeName);
		
		refreshBtnPress();
	}
	
	@FXML
	public void refreshBtnPress()
	{
		for (String employee : employees)
		{
			double progress = inventoryGenerator.getProgress(employee);
			
			for (String content : employeeList.getItems())
			{
				if (content.split(" ")[1].trim().equals(employee))
				{
					employeeList.getItems().remove(content);
					employeeList.getItems().add(
							"employee: " + employee
							+ " \tprogress: " + getProgressFig(progress) + " "
							+ progress);
					return;
				}
			}
			
			employeeList.getItems().add(
					"employee: " + employee
					+ " \tprogress: " + getProgressFig(progress) + " "
					+ progress * 100 + "%");
		}
	}
	
	private String getProgressFig(double progress)
	{
		int max = 50;
		StringBuilder s = new StringBuilder();
		s.append("|");
		for (int i = 0; i < max; i++)
		{
			if (i < (int) max * progress)
				s.append("\\\\");
			else
				s.append("  ");
		}
		
		s.append("|");
		
		return s.toString();
	}
	
	@Override
	public void setReposite(Reposite reposite)
	{
		this.reposite = reposite;
	}

	@Override
	public void setCheckoutHelper(CheckoutHelper helper)
	{	// nothing
	}

	@Override
	public void setName(String name)
	{	// nothing
	}

	@Override
	public void setInventoryGenerator(InventoryGenerator inventoryGenerator)
	{
		this.inventoryGenerator = inventoryGenerator;
	}

}
