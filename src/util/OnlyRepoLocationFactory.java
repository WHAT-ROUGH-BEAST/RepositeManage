package util;

import javaBean.OnlyRepoLocation;

public class OnlyRepoLocationFactory implements LocationFactory
{
	@Override
	public OnlyRepoLocation getLocation()
	{
		return new OnlyRepoLocation();
	}
}
