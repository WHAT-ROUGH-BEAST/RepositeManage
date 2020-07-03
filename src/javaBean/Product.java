package javaBean;

import util.DataBase;
import util.Location;

public abstract class Product implements Cloneable, Comparable<Product>
{
	protected String id;
	protected int amount;
	protected Location location;
	
	public Product()
	{
	}
	
	public Product(String id, int amount, Location location)
	{
		if (null == id || null == location || amount < 0)
			throw new RuntimeException();
		
		this.id = id;
		this.amount = amount;
		this.location = location;
	}
	
	// getter
	public String getId()
	{
		return id;
	}
	
	public int getAmount()
	{
		return amount;
	}
	
	public Location getLocation()
	{
		return location;
	}
	
	// setter
	// id 为初始化时的必要属性，则不考虑数据库修改操作
	public void setId(String id)
	{
		this.id = id;
	}
	
	public void setAmount(int amount)
	{
		this.amount = amount;
	}
	
	public void setLocation(Location location)
	{
		this.location = location;
	}
	
	public static void updateDBProduct(Product product)
	{
		DataBase db = DataBase.getInstance();
		db.updateProduct(product);
		db.killInstance();
	}
	
	public abstract Product clone();
}