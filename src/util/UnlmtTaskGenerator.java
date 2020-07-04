package util;

import java.util.*;

import javaBean.Order;
import javaBean.PickingTask;
import javaBean.Product;

public class UnlmtTaskGenerator implements TaskGenerator
{
	// LinkedList 方便员工得到 task 与 product
	private LinkedList<PickingTask> tasks = new LinkedList<>();
	private LinkedList<Product> taskItems = new LinkedList<>();
	
	private ProductListManager listManager = new ProductListManager(taskItems)
	{
		@Override
		public void addProduct(Product product) throws Exception
		{
			if (null == product || null == product.getId() 
					|| null == product.getLocation())
				throw new RuntimeException("invalid product");
			
			boolean flag = mergeProduct(product);
			
			if (false == flag)
				products.add(product);
		}
	};
	
	@Override
	public LinkedList<PickingTask> generateTask(List<Order> orders)
	{
		if (null == orders)
			throw new RuntimeException();
		
		for (Order o : orders)
		{
			for (Product p : o.getProducts())
			{
				try
				{
					listManager.addProduct(p);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
		Collections.sort(taskItems);
		
		tasks.add((PickingTask)XMLUtil.getBean("PickingTaskconfig", taskItems));
		
		return tasks;
	}
	
}
