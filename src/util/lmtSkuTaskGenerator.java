package util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javaBean.Order;
import javaBean.PickingTask;
import javaBean.Product;
import javaBean.ReturnTask;

public class lmtSkuTaskGenerator implements TaskGenerator
{
	// LinkedList 方便员工得到 task 与 product
	private LinkedList<PickingTask> tasks = new LinkedList<>();
	private LinkedList<Product> taskItems = new LinkedList<>();
	
	private int skuLmt;
	 
	public lmtSkuTaskGenerator(List<Object> attr)
	{
		skuLmt = (int) attr.get(0);
	}

	@Override 
	public LinkedList<PickingTask> generateTask(List<Order> orders)
	{
		taskItems = SplitItems(orders);
		Collections.sort(taskItems);
		
		int cnt = 0;
		LinkedList<Product> temp = new LinkedList<Product>();
		for (Product p : taskItems)
		{ 
			if (cnt == skuLmt)
			{
				cnt = 0;
				PickingTask task = (PickingTask)XMLUtil.
						getBean("PickingTaskconfig", temp);
				tasks.add(task);
				temp = new LinkedList<Product>();
			}
			else
			{
				cnt++;
				mergeAdd(temp, p);
			}
		}
		
		PickingTask task = null;
		if (orders.get(0).getId() != 0)
			task = (PickingTask)XMLUtil.getBean("PickingTaskconfig", temp);
		else
			task = new ReturnTask(temp);
		
		tasks.add(task);
		
		return tasks;
	}
	
	private void mergeAdd(LinkedList<Product> temp, Product product)
	{
		for (Product p : temp)
		{
			if (p.getId().equals(product.getId()))
			{
				p.setAmount(p.getAmount() + product.getAmount());
				return;
			}
		}
		
		temp.add(product);
	}
	
	// 得到个数都为一的商品
	private LinkedList<Product> SplitItems(List<Order> orders)
	{
		LinkedList<Product> splitedItems = new LinkedList<Product>();
		
		for (Order o : orders)
		{
			for (Product p : o.getProducts())
			{
	
				if (p.getAmount() == 1)
					splitedItems.add(p);
				else
				{
					splitedItems.addAll(splitProduct(p));
				}
			}
		}
		
		return splitedItems;
	}
	
	private LinkedList<Product> splitProduct(Product product)
	{
		LinkedList<Product> items = new LinkedList<>();
		int amount = product.getAmount();
		while (amount > 0)
		{
			Product item = (Product) XMLUtil.getBean("Productconfig",
					product.getId(), 1, product.getLocation());
			items.add(item);
			amount --;
		}
		
		return items;
	}
}
