package ListView;

import javaBean.Order;
import javafx.scene.control.ListCell;

public class OrderItem extends ListCell<Order>
{
	@Override
    public void updateItem(Order order, boolean empty)
    {
        super.updateItem(order, empty);
        if (order != null && !empty)
        {
            OrderItemView data = new OrderItemView();
            data.setInfo(order);
            setGraphic(data.getBox());
        }
        else
        {
        	setGraphic(null);
        }
    }
}
