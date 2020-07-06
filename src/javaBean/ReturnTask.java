package javaBean;

import java.util.LinkedList;

public class ReturnTask extends PickingTask
{
	public ReturnTask(LinkedList<Product> products)
	{
		super(products);
		for (Product p : products)
		{
			p.setId("RETURN: " + p.getId());
		}
	}

	@Override
	public void done(String id)
	{
		if (null == id)
			throw new RuntimeException("null id");
		
		for (Product p : taskItems)
		{
			if (p.getId().contains(id))
			{
				taskItems.remove(p);
				return;
			}
		}
		
		throw new RuntimeException("找不到以下任务项: " + id);
	}

}
