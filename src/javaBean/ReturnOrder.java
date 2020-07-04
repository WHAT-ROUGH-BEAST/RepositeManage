package javaBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import util.ProductListManager;

/*
 * 装饰
 */
public class ReturnOrder extends Order
{
	private Order componentOrder;
	
	public ReturnOrder(Order componentOrder)
	{
		this.componentOrder = componentOrder;
		this.products = componentOrder.getProducts();
		
		ListManager = new ProductListManager(products) {
			public void addProduct(Product product) throws Exception
			{
				if (null == product || null == product.getId())
					throw new RuntimeException("invalid product");
				
				products.add(product);
			}
		};
	}
	
	public int getId()
	{
		return 0;
	}
	
	public String getBuyerName()
	{
		return "RETURN--" + componentOrder.getBuyerName();
	}
	
	public void setBuyerName(String name)
	{
		if (null == name)
			throw new RuntimeException();
		
		componentOrder.setBuyerName(name);
	}
	
	public void addProduct(Product product) throws Exception 
	{
		ListManager.addProduct(product);
	}
	
	public void removeProduct(String id)
	{
		ListManager.removeProduct(id);
	}
	
	// 根据用户的信息决定哪些仓库适用
	public void registerRepos(final Reposite repo)
	{
		throw new RuntimeException("unsupported feature");
	}
	
	public void unregisterRepos(final Reposite repo)
	{
		throw new RuntimeException("unsupported feature");
	}
}
