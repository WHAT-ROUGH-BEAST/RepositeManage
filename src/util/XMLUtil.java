package util;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javaBean.OnlyRepoLocation;
import javaBean.Product;
import javaBean.SimpleProduct;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class XMLUtil
{
	private static ArrayList<Object> objects;
//该方法用于从XML配置文件中提取具体类类名，并返回一个实例对象
	
	public static Object getBean(String configName)
	{
		try
		{
			// 创建文档对象
			DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dFactory.newDocumentBuilder();
			Document doc;
			doc = builder.parse(new File("src\\"+ configName.trim() +".xml"));

			// 获取包含类名的文本节点
			NodeList nl = doc.getElementsByTagName("className");
			Node classNode = nl.item(0).getFirstChild();
			String cName = classNode.getNodeValue();

			// 通过类名生成实例对象并将其返回
			Class<?> c = Class.forName(cName);
			Object obj;
			if (null == objects)
				obj = c.newInstance();
			else
			{
				Constructor<?> constructor = c.getConstructor(List.class);
				obj = constructor.newInstance(objects);
			}			

			return obj;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			objects = null;
		}
	}
	
	public static Object getBean(String configName, Object... attrs)
	{
		setAttr(attrs);
		
		return getBean(configName);
	}
	
	private static void setAttr(Object... attrs)
	{
		objects = new ArrayList<>();
		for (Object attr : attrs)
		{
			objects.add(attr);
		}
	}
	
	public static void main(String[] args)
	{
		getBean("PickingTaskconfig", new LinkedList<Product>());
	}
}
