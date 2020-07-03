package ListView;

import javaBean.Inventory;
import javaBean.Product;
import javafx.scene.control.ListCell;

public class InventoryItem  extends ListCell<Product>
{
	private Inventory inventory;
	
	@Override
    public void updateItem(Product product, boolean empty)
    {
        super.updateItem(product, empty);
        if (product != null && !empty)
        {
        	InventoryView data = new InventoryView();
            data.setInfo(product);
            data.setInventory(inventory);
            setGraphic(data.getBox());
        }
        else
        {
        	setGraphic(null);
        }
    }
	
	public void setInventory(Inventory inventory)
	{
		this.inventory = inventory;
	}
}
