package javaBean;

import java.util.HashMap;

import util.Location;
import util.LocationFactory;
import util.XMLUtil;

public class accurateInventory implements Inventory
{
	private Inventory invenComponent;
	private String employeeName;
	private String shelf;
	private String time;
	
	public accurateInventory(Inventory invenComponent)
	{
		this.invenComponent = invenComponent;
	}
	
	public void setEmployeeName(String employeeName)
	{
		this.employeeName = employeeName;
	}
	
	public void setShelf(String shelf)
	{
		this.shelf = shelf;
	}
	
	public void setTime(String time)
	{
		this.time = time;
	}
	
	public String getEmployeeName()
	{
		return employeeName;
	}
	
	public String getShelfName()
	{
		return shelf;
	}
	
	public String getTime()
	{
		return time;
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
	public Product getProduct(String repo, String shelf, int loc)
	{
		if (!shelf.equals(this.shelf))
			return null;
		
		return invenComponent.getProduct(repo, shelf, loc);
	}

	@Override
	public HashMap<String, Integer> printReport()
	{
		return invenComponent.printReport();
	}

	@Override
	public void modifyAmount(Product currProduct, int amount)
	{
		invenComponent.modifyAmount(currProduct, amount);
	}
	
	public double getProgress()
	{
		return (double)invenComponent.getCurrRecord().size() / 12;
	}

	@Override
	public HashMap<String, Integer> getCurrRecord()
	{
		return invenComponent.getCurrRecord();
	}
}
