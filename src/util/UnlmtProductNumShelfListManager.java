package util;

import java.util.List;

import javaBean.Product;
import javaBean.ProductListContainer;

public class UnlmtProductNumShelfListManager extends ProductListManager
{
	private static int MAX;

	public UnlmtProductNumShelfListManager(List<Object> attrs)
	{
		super(((ProductListContainer) attrs.get(0)).getProducts());
		UnlmtProductNumShelfListManager.MAX = (int) 
				((ProductListContainer) attrs.get(0)).getAttrs().get("maxShelfNum");
	}

	@Override
	public void addProduct(Product product) throws Exception
	{
		if (null == product || null == product.getId() 
				|| null == product.getLocation())
			throw new RuntimeException("invalid product");
		
		if (true == mergeProduct(product))
		{
			Product.updateDBProduct(product);
		}
		else
		{ 
			// �������ƴ�С
			if (products.size() == MAX || product.getLocation().getPos() > MAX)
				throw new Exception("shelf oversize");
			
			// ���ݿ� -- ��ֹ����������shelf�ĵ���Ʒ�ظ�����
			DataBase db = DataBase.getInstance();
			try
			{
				db.addProduct(product);
			}
			catch (Exception e)
			{
				throw new Exception("already in other shelf");
			}
			finally
			{
				db.killInstance();
			}
			
			products.add(product);
		}
	}
	
	@Override
	public void removeProduct(String id)
	{
		try
		{
			// ���ݿ�
			DataBase db = DataBase.getInstance();
			db.removeProduct(id);
			db.killInstance();
			
			super.removeProduct(id);
		}
		catch (Exception e)
		{
			throw e;
		}
	}
}
