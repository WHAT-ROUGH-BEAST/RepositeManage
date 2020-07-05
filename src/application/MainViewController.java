package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javaBean.InventoryGenerator;
import javaBean.Reposite;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import util.CheckoutHelper;
import util.CheckoutNReturnHelper;
import util.DataBase;
import util.SimpleCheckoutHelper;

/*
 * ÷–ΩÈ’ﬂ
 */
public class MainViewController
{
	@FXML private Button btn;
	@FXML private TabPane tabPane;
	
	private static Reposite reposite;
	private static CheckoutHelper checkoutHelper;
	private String identity; 
	private static InventoryGenerator inventoryGenerator;
	static
	{
		if (null == reposite)
		{
			DataBase db = DataBase.getInstance();
			reposite = db.getRepo("defaultReposite");
			db.killInstance();
		}
		
		if (null == checkoutHelper)
		{
			CheckoutHelper helperComponent = new SimpleCheckoutHelper(
					new ArrayList<Reposite>(Arrays.asList(reposite)));
			checkoutHelper = new CheckoutNReturnHelper((SimpleCheckoutHelper) helperComponent);
		}
		
		if (null == inventoryGenerator)
			inventoryGenerator = new InventoryGenerator();
	}

	public void setIdentity(String identity)
	{
		this.identity = identity;
		
		Tab searchTab = null, orderTab = null, generateTaskTab = null,
				doTaskTab = null, invenTab = null, addProductTab = null, 
				generateInventoryTab = null;
		
		switch (identity.split(":")[0])
		{
		case "user":
//			initTab(searchTab, "Search");
			initTab(orderTab, "Order");
			break;
		case "manager":
			initTab(searchTab, "Search");
			initTab(generateTaskTab, "GenerateTask");
			initTab(generateInventoryTab, "GenerateInventory");
			break;
		case "employee":
			initTab(searchTab, "Search");
			initTab(doTaskTab, "DoTask");
			initTab(invenTab, "Inventory");
			initTab(addProductTab, "AddProduct");
			break;
		}
	}
	
	private void initTab(Tab tab, String name)
	{
		tab = new Tab();
		tab.setText(name);
		tab.setId(name);
		tab.setClosable(false);
		
		try
		{
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().
					getResource("/layout/" + name + ".fxml"));
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
			AnchorPane root  = (AnchorPane) fxmlLoader.load();
			
			tab.setContent(root);
			tabPane.getTabs().add(tab);
			
			((DataShare) fxmlLoader.getController()).setReposite(reposite);
			((DataShare) fxmlLoader.getController()).setCheckoutHelper(checkoutHelper);
			((DataShare) fxmlLoader.getController()).setName(identity.split(":")[1].trim());
			
			if (name.equals("GenerateInventory") || name.equals("Inventory"))
				((InventoryShare) fxmlLoader.getController()).setInventoryGenerator(inventoryGenerator);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
