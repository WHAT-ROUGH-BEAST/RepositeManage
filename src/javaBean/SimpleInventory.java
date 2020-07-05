package javaBean;

import util.Location;
import util.LocationFactory;
import util.XMLUtil;
import java.util.*;

public class SimpleInventory implements Inventory
{
	private HashMap<String, Integer> changeRecord;
	private Reposite registeredReposite;
	
	public SimpleInventory(List<Object> attr) 
	{
		Reposite registeredReposite = (Reposite) attr.get(0);
		
		if (null == registeredReposite)
			throw new RuntimeException();
		
		this.registeredReposite = registeredReposite;
		
		changeRecord = new HashMap<>();
	}
	
	@Override
	public Product getProduct(String repo, String shelf, int loc)
	{
		if (null == repo || null == shelf || loc < 0)
			throw new RuntimeException();
		
		Location l = ((LocationFactory) XMLUtil.
				getBean("LocationFactoryconfig")).getLocation();
		l.setLocation("defaultReposite", shelf, loc);
		
		Product product = registeredReposite.search(l);
		
		return product;
	}
	
	@Override
	public String getId(String repo, String shelf, int loc)
	{
		return getProduct(repo, shelf, loc).getId();
	}

	@Override
	public int getAmount(String repo, String shelf, int loc)
	{
		return getProduct(repo, shelf, loc).getAmount();
	}

	@Override
	public void modifyAmount(Product currProduct, int amount)
	{
		if (amount < 0)
			throw new RuntimeException();
		
		changeRecord.put(currProduct.getId(), amount - currProduct.getAmount());
		
		currProduct.setAmount(amount);
		
		// ����db
		Product.updateDBProduct(currProduct);
	}

	/*
	 * ��printreport��Ϊһ���̵�Ľ�������ռ�¼�б�
	 */
	@Override
	public HashMap<String, Integer> printReport()
	{
		HashMap<String, Integer> map = changeRecord;
		
		// �����̵����
		changeRecord = new HashMap<>();
		
		return map;
	}
	
	public HashMap<String, Integer> getCurrRecord()
	{
		return changeRecord;
	}
}
