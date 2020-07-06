package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import application.UiUtil;
import javaBean.Order;
import javaBean.Product;
import javaBean.Reposite;

public class ReadExcelUtils
{
	private Workbook wb;
	private Sheet sheet;
	private Row row;

	public ReadExcelUtils(String filepath)
	{
		if (filepath == null)
		{
			throw new RuntimeException("�Ƿ�·��");
		}
		String ext = filepath.substring(filepath.lastIndexOf("."));
		try
		{
			InputStream is = new FileInputStream(filepath);
			if (".xls".equals(ext))
			{
				wb = new HSSFWorkbook(is);
			}
			else if (".xlsx".equals(ext))
			{
				wb = new XSSFWorkbook(is);
			}
			else
			{
				wb = null;
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡExcel����ͷ������
	 * 
	 * @param InputStream
	 * @return String ��ͷ���ݵ�����
	 */
	private String[] readExcelTitle() throws Exception
	{
		if (wb == null)
		{
			throw new Exception("Workbook����Ϊ�գ�");
		}
		sheet = wb.getSheetAt(0);
		row = sheet.getRow(0);
		// ����������
		int colNum = row.getPhysicalNumberOfCells();
		System.out.println("colNum:" + colNum);
		String[] title = new String[colNum];
		for (int i = 0; i < colNum; i++)
		{
			// title[i] = getStringCellValue(row.getCell((short) i));
			title[i] = row.getCell(i).getCellFormula();
		}
		return title;
	}

	/**
	 * ��ȡExcel��������
	 * 
	 * @param InputStream
	 * @return Map ������Ԫ���������ݵ�Map����
	 */
	public Map<Integer, Map<Integer, Object>> readExcelContent() throws Exception
	{
		if (wb == null)
		{
			throw new Exception("Workbook����Ϊ�գ�");
		}
		Map<Integer, Map<Integer, Object>> content = new HashMap<Integer, Map<Integer, Object>>();

		sheet = wb.getSheetAt(0);
		// �õ�������
		int rowNum = sheet.getLastRowNum();
		row = sheet.getRow(0);
		int colNum = row.getPhysicalNumberOfCells();
		// ��������Ӧ�ôӵڶ��п�ʼ,��һ��Ϊ��ͷ�ı���
		for (int i = 1; i <= rowNum; i++)
		{
			row = sheet.getRow(i);
			int j = 0;
			Map<Integer, Object> cellValue = new HashMap<Integer, Object>();
			while (j < colNum)
			{
				Object obj = getCellFormatValue(row.getCell(j));
				cellValue.put(j, obj);
				j++;
			}
			content.put(i, cellValue);
		}
		return content;
	}

	/**
	 * 
	 * ����Cell������������
	 * 
	 * @param cell
	 * @return
	 */
	private Object getCellFormatValue(Cell cell)
	{
		Object cellvalue = "";
		if (cell != null)
		{
			// �жϵ�ǰCell��Type
			switch (cell.getCellType())
			{
			case NUMERIC:// �����ǰCell��TypeΪNUMERIC
			case FORMULA:
			{
				// �жϵ�ǰ��cell�Ƿ�ΪDate
				if (DateUtil.isCellDateFormatted(cell))
				{
					// �����Date������ת��ΪData��ʽ
					// data��ʽ�Ǵ�ʱ����ģ�2013-7-10 0:00:00
					// cellvalue = cell.getDateCellValue().toLocaleString();
					// data��ʽ�ǲ�����ʱ����ģ�2013-7-10
					Date date = cell.getDateCellValue();
					cellvalue = date;
				}
				else
				{// ����Ǵ�����

					// ȡ�õ�ǰCell����ֵ
					cellvalue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			}
			case STRING:// �����ǰCell��TypeΪSTRING
				// ȡ�õ�ǰ��Cell�ַ���
				cellvalue = cell.getRichStringCellValue().getString();
				break;
			default:// Ĭ�ϵ�Cellֵ
				cellvalue = "";
			}
		}
		else
		{
			cellvalue = "";
		}
		return cellvalue;
	}
	
	public ArrayList<Order> getOrders(Reposite reposite) throws Exception
	{
		Map<Integer, Map<Integer, Object>> excel;
		try
		{
			excel = readExcelContent();
		}
		catch (Exception e)
		{
			throw new RuntimeException("·������");
		}
		
		ArrayList<Order> orders = new ArrayList<>();
		
		String buyer;
		Order temp = null;
		for (int i : excel.keySet())
		{
			Map<Integer, Object> row = excel.get(i);
			
			if (!((String)row.get(0)).isEmpty())
			{
				temp = (Order) XMLUtil.getBean("Orderconfig");
				temp.registerRepos(reposite);
				
				buyer = (String) row.get(0);
				temp.setBuyerName(buyer);
				
				orders.add(temp);
			}
			
			Product p = (Product) XMLUtil.getBean("Productconfig");
			p.setId((String) row.get(1));
			p.setAmount((int) Double.parseDouble((String) row.get(2)));
			p.setLocation(reposite.search(p.getId()).getLocation());

			temp.addProduct(p);
		}
		
		return orders;
	}
}
