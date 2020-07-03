package javaBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.OrderListManager;
import util.ProductListManager;
import util.XMLUtil;

public class SimpleOrder extends Order
{
	public SimpleOrder()
	{
		super();
		ListManager = (ProductListManager) XMLUtil.getBean("OrderListManagerconfig", this);
	}

	@Override
	public Map<String, Object> getAttrs()
	{
		return super.getAttrs();
	}
}
