package javaBean;

import java.util.List;

import util.Location;

public class SimpleProduct extends Product
{
	public SimpleProduct()
	{
		super();
	}
	
	public SimpleProduct(List<Object> attrs)
	{
		super((String) attrs.get(0), 
				(int) attrs.get(1), 
				(Location) attrs.get(2));
	}
	
	@Override
	public SimpleProduct clone()
	{
		SimpleProduct clone = new SimpleProduct();
		clone.amount = amount;
		clone.id = id;
		clone.location = location;
		return clone;
	}

	@Override
	public int compareTo(Product product)
	{
		int compare = 0;
		compare = this.location.getRepo().compareTo(product.getLocation().getRepo()) * 100
				+ (this.location.getShelf().charAt(0) - product.getLocation().getShelf().charAt(0)) * 10
				+ this.location.getPos() - product.getLocation().getPos();
		return compare;
	}
}
