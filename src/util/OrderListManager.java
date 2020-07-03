package util;

import java.util.List;

import javaBean.Product;
import javaBean.ProductListContainer;
import javaBean.Reposite;
import javaBean.Shelf;

public class OrderListManager extends ProductListManager
{
	private List<Reposite> registeredRepos;
	
	@SuppressWarnings("unchecked")
	public OrderListManager(List<Object> attrs)
	{
		super(((ProductListContainer) attrs.get(0)).getProducts());
		registeredRepos = (List<Reposite>) 
				((ProductListContainer) attrs.get(0)).getAttrs().get("registeredRepos");
	}

	@Override
	public void addProduct(final Product product) throws Exception
	{
		if (null == product || null == product.getId() 
				|| null == product.getLocation())
			throw new RuntimeException("invalid product");
		
		// 如果订单中有同样的商品，则合计
		Product adder = mergeProduct(product.clone());
		
		// 检查是否超过库存
		checkReposite(adder); // throws Exception
		
		products.add(adder);
	}
	
	public void checkReposite(Product product) throws Exception
	{
		if (null == product)
			throw new RuntimeException();
		
		// 在库存中取得商品
		Product storage = null;
		for (Reposite r : registeredRepos)
		{
			if (null != (storage = r.search(product.getId())))
				break;
		}
		
		if (null == storage)
			throw new RuntimeException("can't find product id: " + product.getId());
		
		if (product.getAmount() > storage.getAmount())
			throw new Exception("order amount over storage");
	}
}
