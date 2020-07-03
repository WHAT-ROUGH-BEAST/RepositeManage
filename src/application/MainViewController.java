package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

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
import util.DataBase;

/*
 * ÷–ΩÈ’ﬂ
 */
public class MainViewController
{
	@FXML private Button btn;
	@FXML private TabPane tabPane;
	
	private static Reposite reposite;
	private static CheckoutHelper checkoutHelper;
	static
	{
		DataBase db = DataBase.getInstance();
		reposite = db.getRepo("defaultReposite");
		db.killInstance();
		
		checkoutHelper = new CheckoutHelper(new ArrayList<Reposite>(Arrays.asList(reposite)));
	}

	public void setIdentity(String identity)
	{
		Tab searchTab = null, orderTab = null, generateTaskTab = null,
				doTaskTab = null, invenTab = null;
		
		switch (identity)
		{
		case "user":
			initTab(searchTab, "Search");
			initTab(orderTab, "Order");
			break;
		case "manager":
			initTab(searchTab, "Search");
			initTab(generateTaskTab, "GenerateTask");
			break;
		case "employee":
			initTab(searchTab, "Search");
			initTab(doTaskTab, "DoTask");
			initTab(invenTab, "Inventory");
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
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
