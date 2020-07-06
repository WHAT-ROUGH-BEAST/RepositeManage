package util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javaBean.Order;
import javaBean.PickingTask;
import javaBean.Product;
import javaBean.Reposite;

public class CheckoutNReturnHelper implements CheckoutHelper
{
	private List<Reposite> repos;
	private SimpleCheckoutHelper helperComponent;
	private List<Order> returnOrders;

	public CheckoutNReturnHelper(SimpleCheckoutHelper helperComponent)
	{ 
		this.helperComponent = helperComponent;
		repos = helperComponent.getReposites();
		returnOrders = new LinkedList<>();
	}
	
	@Override
	public void addOrder(Order order)
	{
		if (null == order || null == order.getBuyerName()
				|| 0 == order.getProducts().size())
			throw new RuntimeException("invalid order");
		
		if (0 == order.getId())
		{
			returnOrders.add(order);
			return;
		}
		
		helperComponent.addOrder(order);
	}

	@Override
	public void checkout()
	{
		if (!returnOrders.isEmpty())
		{
			ArrayList<Product> returnProducts = new ArrayList<>();
			for (Order o : returnOrders)
			{
				returnProducts.addAll(o.getProducts());
			}
			
			for (Product p : returnProducts)
			{
				for (Reposite r : repos)
				{
					Location l = r.addProduct(p.getId(), p.getAmount());
					p.setLocation(l);
				}
			}
			
			TaskGenerator generator = (TaskGenerator) XMLUtil.getBean("TaskGeneratorconfig", 5);
			helperComponent.addTasks(generator.generateTask(returnOrders));
			returnOrders.clear();
		}
		
		helperComponent.checkout();
	}

	@Override
	public List<Order> getOrders()
	{
		LinkedList<Order> allOrders = new LinkedList<>();
		allOrders.addAll(returnOrders);
		allOrders.addAll(helperComponent.getOrders());
		
		return allOrders;
	}

	@Override
	public void importOrder(String type, String path) throws Exception
	{
		helperComponent.importOrder(type, path);
	}

	@Override
	public PickingTask getTask()
	{
		return helperComponent.getTask();
	}

}
