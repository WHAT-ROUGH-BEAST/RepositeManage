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
		
		throw new RuntimeException("没有盘点任务给: " + employeeName);
	}
	
	public void deployInventory(Reposite repo, 
			String employeeName, String shelf, String time)
	{
		for (AccurateInventory i : inventorys)
		{
			if (i.getEmployeeName().equals(employeeName))
				throw new RuntimeException("此员工已被分配任务");
			if (i.getShelfName().equals(shelf))
				throw new RuntimeException("此员货架被分配任务");
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
		
		throw new RuntimeException("没有盘点任务给: " + employeeName);
	}
	
	public void done(AccurateInventory inventory)
	{
		try
		{
			inventorys.remove(inventory);
		}
		catch (Exception e)
		{
			throw new RuntimeException("没有此盘点任务"); 
		}
		
	}
}
