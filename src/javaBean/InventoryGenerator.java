package javaBean;

import java.util.ArrayList;

import util.XMLUtil;

public class InventoryGenerator
{
	private ArrayList<accurateInventory> inventorys;
	
	public InventoryGenerator()
	{
		inventorys = new ArrayList<accurateInventory>();
	}
	
	public accurateInventory getInventory(String employeeName)
	{
		for (accurateInventory i : inventorys) 
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
		Inventory inv = (Inventory) XMLUtil.getBean("Inventoryconfig", repo);
		accurateInventory inventory = new accurateInventory(inv);
		
		inventory.setEmployeeName(employeeName);
		inventory.setShelf(shelf);
		inventory.setTime(time);
		
		inventorys.add(inventory);
	}
	
	public double getProgress(String employeeName)
	{
		for (accurateInventory i : inventorys)
		{
			if (i.getEmployeeName().equals(employeeName))
				return i.getProgress();
		}
		
		throw new RuntimeException("没有盘点任务给: " + employeeName);
	}
	
	public void done(accurateInventory inventory)
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
