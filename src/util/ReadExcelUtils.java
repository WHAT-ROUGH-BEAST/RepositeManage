package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
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

public class ReadExcelUtils
{
	private Workbook wb;
	private Sheet sheet;
	private Row row;

	private ReadExcelUtils(String filepath)
	{
		if (filepath == null)
		{
			return;
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
	private Map<Integer, Map<Integer, Object>> readExcelContent() throws Exception
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
	
	public static Map<Integer, Map<Integer, Object>> getExcelContent(String filepath)
	{
		ReadExcelUtils excelReader = new ReadExcelUtils(filepath);
		
		// �Զ�ȡExcel������ݲ���
		// <row <column value>>
		// {0=A, 1=1.0, 2=4a507db7-7c52-2c22-d6a6-77ade48625bd, 3=8.0}
		Map<Integer, Map<Integer, Object>> map;
		try
		{
			map = excelReader.readExcelContent();
			return map;
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
//	// �������ݿ�
//	public static void main(String[] args)
//	{
//		DataBase db = DataBase.getInstance();
//		try
//		{
//			Connection con = db.getConnection();
//			
//			Map<Integer, Map<Integer, Object>> map = ReadExcelUtils.getExcelContent("C:\\Users\\18069\\Desktop\\reposite_table.xlsx");
//			
//			Statement stmt = con.createStatement();
//			
//			String loc_s = null;
//			String sql = null;
//			for (int i = 1; i < map.size(); i++)
//			{
//				if (null == (String) map.get(i).get(2) || 0 == ((String) map.get(i).get(2)).length())
//					continue;
//				
//				String id = (String) map.get(i).get(2);
//				String amount = (String) map.get(i).get(3);
//				
//				if (null != (String) map.get(i).get(0) && 0 != ((String) map.get(i).get(0)).length())
//					loc_s = (String) map.get(i).get(0);
//				
//				String loc_r = "defaultReposite";
//				
//				String loc_l = (String) map.get(i).get(1);
//				
//				sql	= "INSERT INTO Reposite " + "\n"
//						+ "VALUES (" + "'" + id  + "'" + ", " 
//						+ amount + ", "
//						+ "'" + loc_r + "'" + ", "
//						+ "'" + loc_s + "'" + ", "
//						+ loc_l
//						+ ")";
//				System.out.println(sql);
//				stmt.executeUpdate(sql);
//			}
//
//			db.closeConnection(con);
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		finally
//		{
//			db.killInstance();
//		}
//	}
}
