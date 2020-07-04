package util;

import java.sql.Connection;
import java.sql.Statement;
import java.util.*;

import application.UiUtil;
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
	void importOrder(String type, String path) throws Exception;
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
	
	// ��ǰ���еĲֿ�
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
	 * �ֿ��и�������
	 * ����������
	 */
	@Override
	public void checkout()
	{
		// ���²ֿ�
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
					throw new RuntimeException("��治��");
				
				product.setAmount(product.getAmount() - p.getAmount());
				
				// �������ݿ�
				Product.updateDBProduct(product);
			}
		}
		
		// �õ�task�б�
		generateTask();
	}
	
	/*
	 * ����Ա������ordersת��Ϊtasks
	 */
	private void generateTask()
	{
		TaskGenerator generator = (TaskGenerator) XMLUtil.getBean("TaskGeneratorconfig", 5);
		tasks.addAll(generator.generateTask(orders));
		
		// database��¼��ʷ����
		orders.clear();
	}

	/*
	 * Ա���õ�ÿ�ε�����
	 */
	public PickingTask getTask()
	{
		if (tasks.isEmpty())
			throw new RuntimeException("û�и�������");
		
		return tasks.pop();
	}

	@Override
	public void importOrder(String type, String path) throws Exception
	{
		// type : xml / excel
		ProductImporter importer = new ProductImporter(repos.get(0));
		List<Order> importOrders = null;
		
		switch (type)
		{
		case "Excel":
			importOrders = importer.importOrderByExl(path);
			break;
		case "Xml":
			importOrders = importer.importOrderByXml(path);
			break;
		default:
			throw new RuntimeException("��֧�ִ��ļ�����");
		}
		
		for (Order o : importOrders)
		{
			addOrder(o);
		}
	}
}
