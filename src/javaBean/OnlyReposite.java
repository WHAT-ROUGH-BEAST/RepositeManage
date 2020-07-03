package javaBean;

import java.util.ArrayList;
import java.util.List;

import util.Location;

public class OnlyReposite extends Reposite
{
	public OnlyReposite()
	{
		super();
	}
	
	public OnlyReposite(List<Object> attrs)
	{
		super((String) attrs.get(0), (ArrayList<Shelf>) attrs.get(1));
	}
	
	@Override
	public String getName()
	{
		return "defaultReposite";
	}
	
	@Override
	public void setName(String name)
	{
		throw new RuntimeException("OnlyReposite doesn't support this feature");
	}
}
