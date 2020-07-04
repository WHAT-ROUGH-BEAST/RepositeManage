package util;

import java.util.ArrayList;

import javaBean.Order;
import javaBean.Reposite;

public class ProductImporter
{
	private ReadExcelUtils excelReader;
	private ReadXmlUtils xmlReader;
	
	private Reposite reposite;
	
	public ProductImporter(Reposite reposite)
	{
		this.reposite = reposite;
	}
	
	public ArrayList<Order> importOrderByExl(String path) throws Exception
	{
		excelReader = new ReadExcelUtils(path);
		return excelReader.getOrders(reposite);
	}
	
	public ArrayList<Order> importOrderByXml(String path) throws Exception
	{
		xmlReader = new ReadXmlUtils(path);
		return xmlReader.getOrders(reposite);
	}
}
