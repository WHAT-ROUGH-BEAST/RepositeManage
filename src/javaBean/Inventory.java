package javaBean;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import util.DataBase;
import util.ReadExcelUtils;

public interface Inventory
{
	String getId(String repo, String shelf, int loc);
	int getAmount(String repo, String shelf, int loc);
	public Product getProduct(String repo, String shelf, int loc);
	HashMap<String, Integer> printReport(); // елс╞ ел©В
	void modifyAmount(Product currProduct, int amount);
}
