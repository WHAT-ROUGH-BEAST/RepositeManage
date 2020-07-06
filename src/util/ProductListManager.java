package util;

import java.util.Iterator;
import java.util.List;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import javaBean.Product;

public abstract class ProductListManager
{
	protected List<Product> products;
	
	public ProductListManager(final List<Product> products)
	{
		this.products = products;
	}
	
	public abstract void addProduct(Product product) throws Exception, SQLServerException;
	
	public void removeProduct(String id)
	{
		if (null == id)
			throw new RuntimeException("invalid id");
		
		Iterator<Product> iterator = products.iterator();
		
		while (iterator.hasNext())
		{
			if (iterator.next().getId().equals(id))
			{
				iterator.remove();
				
				return;
			}
		}
		
		throw new RuntimeException("can't find product id: " + id);
	}
	

	protected boolean mergeProduct(Product product)
	{
		for (Product p : products)
		{
			if (p.getId().equals(product.getId()))
			{
				p.setAmount(p.getAmount() + product.getAmount());
				product.setAmount(p.getAmount());
				product.setLocation(p.getLocation());
				return true;
			}
		}
		
		return false;
	}
}
