package javaBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Reposite
{
	protected String name;
	protected ArrayList<Shelf> shelfs;

	public Reposite()
	{
		shelfs = new ArrayList<Shelf>();
		name = null;
	}
	
	public Reposite(String name, ArrayList<Shelf> shelfs)
	{
		if (null == name || null == shelfs)
			throw new RuntimeException();
		
		this.name = name;
		this.shelfs = shelfs;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public ArrayList<Shelf> getShelfs()
	{
		return shelfs;
	}
	
	public Shelf getShelf(String name)
	{
		if (null == name)
			throw new RuntimeException("invalid shelf name");
		
		for (Shelf shelf : shelfs)
		{
			if (shelf.getName().equals(name))
				return shelf;
		}
		
		throw new RuntimeException("can't find shelf named: " + name);
	}

	public void addShelf(Shelf shelf) throws Exception
	{
		if (null == shelf || null == shelf.getName())
			throw new RuntimeException();
		
		for (Shelf s : shelfs)
		{
			if (s.getName().equals(shelf.getName()))
				throw new Exception("shelf " + s.getName() + " already existed");
		}

		shelfs.add(shelf);
	}

	public void removeShelf(String name)
	{
		Iterator<Shelf> iterator = shelfs.iterator();
		while (iterator.hasNext())
		{
			if (iterator.next().getName().equals(name))
			{
				iterator.remove();
				
				break;
			}
		}

		throw new RuntimeException("don't have such shelf");
	}

	/*
	 * key : product.id or location 
	 */
	public Product search(Object key) 
	{
		if (null == key)
			throw new RuntimeException("invalid search key");

		Product result = null;
		for (Shelf shelf : shelfs)
		{
			if (null != (result = shelf.search(key)))
			{
				return result;
			}
		}

		return null;
	}
}
