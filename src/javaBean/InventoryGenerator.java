package javaBean;

import java.util.ArrayList;

import util.XMLUtil;

public class InventoryGenerator
{
	private ArrayList<AccurateInventory> inventorys;
	
	public InventoryGenerator()
	{
		inventorys = new ArrayList<AccurateInventory>();
	}
	
	public AccurateInventory getInventory(String employeeName)
	{
		for (AccurateInventory i : inventorys) 
		{
			if (i.getEmployeeName().equals(employeeName))
			{
//				inventorys.remove(i);
				return i;
			}
		}
		
		throw new RuntimeException("û���̵������: " + employeeName);
	}
	
	public void deployInventory(Reposite repo, 
			String employeeName, String shelf, String time)
	{
		for (AccurateInventory i : inventorys)
		{
			if (i.getEmployeeName().equals(employeeName))
				throw new RuntimeException("��Ա���ѱ���������");
			if (i.getShelfName().equals(shelf))
				throw new RuntimeException("��Ա���ܱ���������");
		}
		
		Inventory inv = (Inventory) XMLUtil.getBean("Inventoryconfig", repo);
		AccurateInventory inventory = new AccurateInventory(inv);
		
		inventory.setEmployeeName(employeeName);
		inventory.setShelf(shelf);
		inventory.setTime(time);
		
		inventorys.add(inventory);
	}
	
	public double getProgress(String employeeName)
	{
		for (AccurateInventory i : inventorys)
		{
			if (i.getEmployeeName().equals(employeeName))
				return i.getProgress();
		}
		
		throw new RuntimeException("û���̵������: " + employeeName);
	}
	
	public void done(AccurateInventory inventory)
	{
		try
		{
			inventorys.remove(inventory);
		}
		catch (Exception e)
		{
			throw new RuntimeException("û�д��̵�����"); 
		}
		
	}
}
