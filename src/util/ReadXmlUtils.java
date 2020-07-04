package util;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javaBean.Order;
import javaBean.Product;
import javaBean.Reposite;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadXmlUtils
{
    private Document doc;
    
    public ReadXmlUtils(String path)
    {
		try
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
	        doc = db.parse(path);
		}
		catch (Exception e)
		{
			throw new RuntimeException("非法路径");
		}
    }
	
	public ArrayList<Order> getOrders(Reposite reposite) throws Exception
	{
		ArrayList<Order> orders = new ArrayList<>();
		// 获取order节点
		ArrayList<Node> orderList = getOrderNodes();

		for (Node node : orderList)
		{
			NodeList childNodes = node.getChildNodes();
			
			orders.add(readOrder(childNodes, reposite));
		}
        
		return orders;
	}
	
	private Order readOrder(NodeList childNodes, Reposite reposite) throws Exception
	{
		String buyer;
		Order temp = null;
		for (int i = 0; i < childNodes.getLength(); i++) 
		{
            Node childNode = childNodes.item(i);
            
            if (childNode.getNodeName().equals("buyer"))
            {
            	buyer = childNode.getFirstChild().getNodeValue();
            	temp = (Order) XMLUtil.getBean("Orderconfig");
				temp.registerRepos(reposite);
				temp.setBuyerName(buyer);
            }
            else if (childNode.getNodeName().equals("product"))
            {
            	int amount = Integer.parseInt(
            			(childNode.getAttributes().item(0).getNodeValue()));
            	String id = childNode.getFirstChild().getNodeValue();
            	Location l = ((LocationFactory) XMLUtil.
    					getBean("LocationFactoryconfig")).getLocation();
            	Product p = (Product) XMLUtil.getBean("Productconfig", id, amount, l);
				temp.addProduct(p);
            }
        }
		
		return temp;
	}
	
	private ArrayList<Node> getOrderNodes()
	{
		Element root = doc.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        ArrayList<Node> orderList = new ArrayList<>();
        
        for (int i = 0; i < nodeList.getLength(); i ++)
        	if ("order".equals(nodeList.item(i).getNodeName()))
        		orderList.add(nodeList.item(i));
        
        return orderList;
	}
}
