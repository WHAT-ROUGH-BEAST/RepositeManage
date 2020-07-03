package javaBean;

import java.util.*;

import util.ProductListManager;
import util.UnlmtProductNumShelfListManager;
import util.XMLUtil;

public abstract class Shelf implements ProductListContainer
{
	protected String name;
	protected ArrayList<Product> products;
	protected ProductListManager ListManager;
	
	public Shelf()
	{
		products = new ArrayList<>();
		ListManager = (ProductListManager)XMLUtil.
				getBean("ShelfListManagerconfig", this);
	}
	
	public Shelf(ArrayList<Product> products, String name)
	{
		if (null == products || null == name)
			throw new RuntimeException();
		
		this.products = products;
		this.name = name;
		
		ListManager = (ProductListManager)XMLUtil.
				getBean("ShelfListManagerconfig", this);
	}
	
	public String getName()
	{
		return name;
	}
	
	@Override
	public ArrayList<Product> getProducts()
	{
		return products;
	}
	
	public Product getProduct(String id)
	{
		if (null == id)
			throw new RuntimeException();
		
		for (Product p : products)
		{
			if (p.getId().equals(id))
				return p;
		}
		
		throw new RuntimeException("can't find product id: " + id);
	}
	
	public void setName(String name)
	{
		if (null == name)
			throw new RuntimeException();
		
		this.name = name;
	}
	
	public void addProduct(Product product) throws Exception
	{	
		if (null == product)
			throw new RuntimeException();
	
		ListManager.addProduct(product);
	}
	
	public void removeProduct(String id)
	{
		if (null == id)
			throw new RuntimeException();
		
		ListManager.removeProduct(id); 
	}

	public abstract Product search(Object key);
}
