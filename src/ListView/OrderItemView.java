package ListView;

import java.io.IOException;
import java.util.ArrayList;

import javaBean.Order;
import javaBean.Product;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class OrderItemView extends HBox
{
	@FXML private HBox hbox;
	@FXML private Label nameLabel, notationlabel;
	@FXML private ListView<String> productList;
	@FXML private VBox pane;
	
	public OrderItemView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListView/OrderView.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }
	
	public void setInfo(Order order)
	{		
		nameLabel.setText(order.getBuyerName());
		ArrayList<String> productStrs = new ArrayList<>();
		for (Product p : order.getProducts())
		{
			productStrs.add("\tID: " + p.getId() + "  \t\t amount: " + p.getAmount() + "\t");
		}
			
		productList.setItems(FXCollections.observableArrayList(productStrs));
	}
	
	public HBox getBox()
	{
		return hbox;
	}
}
