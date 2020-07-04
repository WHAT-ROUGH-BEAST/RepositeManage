package util;

import java.util.List;

import javaBean.Order;
import javaBean.PickingTask;

public interface CheckoutHelper
{
	void addOrder(Order order);
	void checkout();
	List<Order> getOrders();
	void importOrder(String type, String path) throws Exception;
	PickingTask getTask();
}