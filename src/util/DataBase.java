package util;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javaBean.OnlyReposite;
import javaBean.Order;
import javaBean.Product;
import javaBean.Reposite;
import javaBean.Shelf;
import javaBean.SimpleProduct;

/**
 *  facade pattern
 * @author 18069
 *
 */
interface RepositeDB
{
	Reposite getRepo(String repoName);
	Shelf getShelf(String repoName, String shelfName);
	Product getProduct(String repoName, String shelfName, int loc);
	
//	void addRepo(String name);
//	void addShelf(String name);
	void addProduct(Product product);
	
//	void removeRepo(String name);
//	void removeShelf(String name);
	void removeProduct(String id);
	
	void updateProduct(Product product);
}

interface OrderDB
{
	void addOrder(Order order);
	void removeOrder(int id);
}

/**
 * @author 18069 单例模式
 */
public class DataBase implements RepositeDB
{
	// 驱动路径
	private static final String DBDRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	// 数据库地址
	private static final String DBURL = "jdbc:sqlserver://localhost:1433; DataBaseName=RepositeDB";
	// 数据库登录用户名
	private static final String DBUSER = "sa";
	// 数据库用户密码
	private static final String DBPASSWORD = "transformers!13";
	// 数据库连接
	private static Connection conn = null;
	private static DataBase instance = null;
	private static Statement stmt = null;

	private DataBase()
	{
	}

	synchronized public static DataBase getInstance()
	{
		if (null == instance)
		{
			instance = new DataBase();
			
			// 保证不会重复连接
			try
			{
				conn = instance.getConnection();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			throw new RuntimeException("数据库正在使用");
		}
		return instance;
	}
	
	public void killInstance()
	{
		closeStmt(stmt);
		stmt = null;
		closeConnection(conn);
		conn = null;
		
		instance = null;
	}
	
	Connection getConnection() throws Exception
	{
		//加载驱动程序
        Class.forName(DBDRIVER);
        //连接数据库
        conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);
        
		return conn;
	}
	
	void closeConnection(Connection con)
	{
		try
		{
			if (con != null)
				con.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void closeStmt(Statement stmt)
	{
		try
		{
			if (stmt != null)
				stmt.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void closeResultSet(ResultSet rs)
	{
		try
		{
			if (rs != null)
				rs.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void executeUpdateStmt(String sql)
	{
		closeStmt(stmt); // 关闭上次stmt
		stmt = null;
		try
		{
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
//		finally
//		{
//			closeStmt(stmt);
//		}
	}
	
	private ResultSet executeQueryStmt(String sql)
	{
		closeStmt(stmt); // 关闭上次stmt
		stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			return rs;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
//		finally
//		{
//			closeStmt(stmt);
//		}
	}

	
	@Override
	public void addProduct(Product product)
	{
		String sql = "INSERT INTO Reposite " + "\n"
				+ "VALUES (" + toDBString(product.getId()) + ", " 
				+ product.getAmount() + ", "
				+ toDBString(product.getLocation().getRepo()) + ", "
				+ toDBString(product.getLocation().getShelf()) + ", "
				+ product.getLocation().getPos()
				+ ")";
		
		executeUpdateStmt(sql);
	}

	@Override
	public void removeProduct(String id)
	{
		String sql = "DELETE FROM Reposite" + "\n"
				+ "WHERE ID=" + toDBString(id);
		
		executeUpdateStmt(sql);
	}
	
	@Override
	public Reposite getRepo(String repoName)
	{
		String sql = "SELECT * FROM Reposite" + "\n"
				+ "WHERE LOC_R=" + toDBString(repoName);
		
		ResultSet rs = executeQueryStmt(sql);

		ArrayList<String> shelfNames = getShelfNames(rs);

		ArrayList<Shelf> shelfs = new ArrayList<>();
		for (String shelfName : shelfNames)
		{
			shelfs.add(getShelf(repoName, shelfName));
		}

		Reposite repo = (Reposite) XMLUtil.getBean("Repoconfig", repoName, shelfs);

		return repo;
	}
	
	private ArrayList<String> getShelfNames(ResultSet rs)
	{
		ArrayList<String> shelfNames = null;
		try
		{
			shelfNames = new ArrayList<>();
			while (rs.next())
			{
				String name = rs.getString("LOC_S");
				if (!shelfNames.contains(name))
				{
					shelfNames.add(name);
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeResultSet(rs);
		}
		
		return shelfNames;
	}
	

	@Override
	public Shelf getShelf(String repoName, String shelfName)
	{
		String sql = "SELECT * FROM Reposite" + "\n"
				+ "WHERE LOC_S=" + toDBString(shelfName) + "AND\n"
				+ "LOC_R=" + toDBString(repoName) + ";";
		
		ResultSet rs = executeQueryStmt(sql);
		
		try
		{
			ArrayList<Product> products = new ArrayList<>();
			while (rs.next())
			{
				Location l = ((LocationFactory) XMLUtil.
						getBean("LocationFactoryconfig")).getLocation();
				
				l.setLocation(rs.getString("LOC_R"),
						rs.getString("LOC_S"), 
						rs.getInt("LOC_L"));
				
				Product product = (Product) XMLUtil.getBean("Productconfig", 
						rs.getString("ID"), rs.getInt("AMOUNT"), l);
				
				products.add(product);
			}
			
			Shelf shelf = (Shelf) XMLUtil.getBean("Shelfconfig", products, shelfName);
			
			return shelf;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeResultSet(rs);
		}
		
		return null;
	}

	@Override
	public Product getProduct(String repoName, String shelfName, int loc)
	{
		String sql = "SELECT * FROM Reposite" + "\n"
				+ "WHERE LOC_R=" + toDBString(repoName) + " AND\n"
				+ "LOC_S=" + toDBString(shelfName) + "AND\n"
				+ "LOC_L=" + loc + ";";
		
		ResultSet rs = executeQueryStmt(sql);
		
		try
		{
			Location l = ((LocationFactory) XMLUtil.
					getBean("LocationFactoryconfig")).getLocation();
			
			rs.next();
			l.setLocation(rs.getString("LOC_R"),
					rs.getString("LOC_S"), 
					rs.getInt("LOC_L"));
			
			Product product = (Product) XMLUtil.getBean("Productconfig", 
					rs.getString("ID"), rs.getInt("AMOUNT"), l);
			
			return product;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeResultSet(rs);
		}
		
		return null;
	}
	
	@Override
	public void updateProduct(Product product)
	{
		String sql = "UPDATE Reposite" + "\n"
				+ "SET AMOUNT=" + product.getAmount() + ","
				+ "LOC_R=" + toDBString(product.getLocation().getRepo()) + ","
				+ "LOC_S=" + toDBString(product.getLocation().getShelf()) + ","
				+ "LOC_L=" + product.getLocation().getPos()
				+ "WHERE ID=" + toDBString(product.getId());
		
		executeUpdateStmt(sql);
	}
	
	private String toDBString(String str)
	{
		return "'" + str + "'";
	}
	
	public static void main(String[] args)
	{
		DataBase db = DataBase.getInstance();
		Reposite repo = db.getRepo("defaultReposite");
		System.out.println(repo.getName());
		db.killInstance();
	}
}
