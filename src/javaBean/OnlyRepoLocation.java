package javaBean;

import util.Location;

// Location
public class OnlyRepoLocation extends Location
{
	@Override
	public void setLocation(String r, String s, int p)
	{
		repo = "defaultReposite";
		shelf = s;
		pos = p;
	}
	
	@Override
	public String getRepo()
	{
		return "defaultReposite";
	}
}
