package javaBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Location;
import util.ProductListManager;
import util.UnlmtProductNumShelfListManager;
import util.XMLUtil;

public class LmtCapacityShelf extends Shelf
{
	private static final int MAX = 12;
	
	public LmtCapacityShelf()
	{
		super();
	}
	
	public LmtCapacityShelf(List<Object> attrs)
	{
		super((ArrayList<Product>) attrs.get(0), (String) attrs.get(1));
	}
	
	@Override
	public Product search(Object key)
	{
		if (null == key)
			throw new RuntimeException("invalid search key");
		
		for (Product product : products)
		{
			if ((key instanceof String && product.getId().equals(key)))
				return product; 
			
			if ((key instanceof Location && 
					product.getLocation().toString().equals(key.toString())))
				return product;
		}
		
		return null;
	}

	@Override
	public Map<String, Object> getAttrs()
	{
		HashMap<String, Object> attrs = new HashMap<>();
		attrs.put("maxShelfNum", MAX);
		return attrs;
	}
}
