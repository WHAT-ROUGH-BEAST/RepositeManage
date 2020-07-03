package util;

import java.util.*;

import javaBean.Order;
import javaBean.PickingTask;
import javaBean.Product;
import javaBean.ProductListContainer;
import javaBean.Reposite;;

interface CheckoutInterface
{
	void addOrder(Order order);
	void removeOrder(int id);
	void checkout();
	List<Order> getOrders();
}

public class CheckoutHelper implements CheckoutInterface
{
	private List<Reposite> repos;
	private List<Order> orders;
	private static LinkedList<PickingTask> tasks = new LinkedList<>();
	
	public CheckoutHelper(List<Reposite> repos)
	{
		orders = new ArrayList<>();
		setReposites(repos);
	}
	
	// 当前所有的仓库
	public void setReposites(List<Reposite> repos)
	{
		if (null == repos)
			throw new RuntimeException();
		
		this.repos = repos;
	}

	@Override
	public void addOrder(Order order)
	{
		if (null == order || null == order.getBuyerName()
				|| 0 == order.getProducts().size())
			throw new RuntimeException("invalid order");
		
		orders.add(order);
	}

	@Override
	public void removeOrder(int id)
	{
		for (Order o : orders)
		{
			if (id == o.getId())
			{
				orders.remove(o);
				return;
			}
		}
		
		throw new RuntimeException("can't find order id: " + id);
	}
	
	public List<Order> getOrders()
	{
		return orders;
	}
	
	/**
	 * 仓库中更新数量
	 * 分配拣货任务
	 */
	@Override
	public void checkout()
	{
		// 更新仓库
		ArrayList<Product> checkoutProducts = new ArrayList<>();
		for (Order o : orders)
		{
			checkoutProducts.addAll(o.getProducts());
		}
		
		for (Product p : checkoutProducts)
		{
			for (Reposite r : repos)
			{
				Product product = r.search(p.getId());
				
				if (product.getAmount() - p.getAmount() < 0)
					throw new RuntimeException("库存不足");
				
				product.setAmount(product.getAmount() - p.getAmount());
				
				// 操作数据库
				Product.updateDBProduct(product);
			}
		}
		
		// 得到task列表
		generateTask();
	}
	
	/*
	 * 管理员将整个orders转化为tasks
	 */
	private void generateTask()
	{
		TaskGenerator generator = (TaskGenerator) XMLUtil.getBean("TaskGeneratorconfig");
		tasks.addAll(generator.generateTask(orders));
		
		// database记录历史订单
		orders.clear();
	}

	/*
	 * 员工得到每次的任务
	 */
	public PickingTask getTask()
	{
		if (tasks.isEmpty())
			throw new RuntimeException("没有更多任务");
		
		return tasks.pop();
	}
}
