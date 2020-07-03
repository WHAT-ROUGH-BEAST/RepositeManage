package javaBean;

import java.util.*;

import util.ProductListManager;

public abstract class Order implements ProductListContainer
{
	private static int cnt = 1;
	protected final int id = cnt++;
	
	protected ArrayList<Product> products;
	protected String buyerName;
	protected ProductListManager ListManager;
	// 注册仓库以检查库存
	protected ArrayList<Reposite> registeredRepos;
	
	public Order()
	{
		products = new ArrayList<>();
		registeredRepos = new ArrayList<>();
	}
	
	public int getId()
	{
		return id;
	}
	
	@Override
	public ArrayList<Product> getProducts()
	{
		return products;
	}
	
	public Product getProduct(String id)
	{
		if (null == id)
			throw new RuntimeException("invalid id");
		
		for (Product p : products)
		{
			if (p.getId().equals(id))
				return p.clone();
		}
		
		throw new RuntimeException("can't find product id: " + id);
	}
	
	public String getBuyerName()
	{
		return buyerName;
	}
	
	public void setBuyerName(String name)
	{
		if (null == name)
			throw new RuntimeException();
		
		this.buyerName = name;
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
		for (Reposite r : registeredRepos)
		{
			if (r.getName().equals(repo.getName()))
				throw new RuntimeException("repo already registered");
		}
		
		registeredRepos.add(repo);
	}
	
	public void unregisterRepos(final Reposite repo)
	{
		try
		{
			registeredRepos.remove(repo);
		}
		catch (Exception e)
		{
			throw new RuntimeException("repo not registered");
		}
	}
	
	@Override
	public Map<String, Object> getAttrs()
	{
		HashMap<String, Object> attrs = new HashMap<>();
		attrs.put("registeredRepos", registeredRepos);
		
		return attrs;
	}
}
