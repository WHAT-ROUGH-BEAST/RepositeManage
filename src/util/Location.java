package util;

public abstract class Location
{
	protected String shelf;
	protected int pos;
	protected String repo;
	
	public void setLocation(String r, String s, int p)
	{
		shelf = s;
		pos = p;
		repo = r;
	}
	
	public String getShelf()
	{
		return shelf;
	}
	
	public int getPos()
	{
		return pos;
	}
	
	public String getRepo()
	{
		return repo;
	}
	
	@Override
	public String toString()
	{
		return repo.trim() + "--" + shelf.trim() + "--" + pos;
	}
	
	@Override 
	public boolean equals(Object obj)
	{
		if (obj instanceof Location)
		{
			Location loc = (Location)obj;
			if (loc.shelf.equals(shelf) && loc.repo.equals(repo) 
					&& loc.pos == pos)
				return true;
		}
		
		return false;
	}
}
