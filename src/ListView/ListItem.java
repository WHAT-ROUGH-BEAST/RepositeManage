package ListView;

import javaBean.Product;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class ListItem extends ListCell<Product>
{
	private ListView<Product> list;
	
	public void setList(ListView<Product> list) 
	{
		this.list = list;
	}
	
	@Override
    public void updateItem(Product product, boolean empty)
    {
        super.updateItem(product, empty);
        if (product != null && !empty)
        {
            ItemView data = new ItemView();
            data.setList(list);
            data.setInfo(product);
            setGraphic(data.getBox());
        }
        else
        {
        	setGraphic(null);
        }
    }
}
