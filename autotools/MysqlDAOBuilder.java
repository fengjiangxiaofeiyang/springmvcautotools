package com.universeview.utils.autotools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.universeview.utils.CommonException;
import com.universeview.utils.autotools.config.FieldVO;
import com.universeview.utils.autotools.config.TableVO;
import com.universeview.utils.autotools.config.XMLReader;

/**
 * @Title: MysqlDAOBuilder.java
 * @Description: DAO生成器
 * @author: keven.c
 * @date: 2014-2-17 上午11:30:57
 * @Copyright: 2014 keven.c . All rights reserved
 * @version: V1.0
 */

public class MysqlDAOBuilder
{

	// 方法
	public static String queryname = "query";
	public static String queryapproname = "queryappro";
	public static String querysplitname = "querysp";
	public static String querysplitapproname = "queryspappro";
	public static String updatename = "update";
	public static String updatebypkname = "updatebypk";
	public static String updatechangebypkname = "updatechangebypk";
	public static String deletename = "delete";
	public static String deletebypkname = "deletebypk";
	public static String insertname = "insert";

	// 目录
	private String daopackage = XMLReader.daopackage;
	private String daofolderpath = XMLReader.daofolderpath;
	private String daoadvpackage = XMLReader.daoadvpackage;
	private String daoadvfolderpath = XMLReader.daoadvfolderpath;

	public String produceVOQuerySQLname = XMLReader.produceVOQuerySQLname;
	public String produceVOQuerySQLApproname = XMLReader.produceVOQuerySQLApproname;
	public String produceVOPKQuerySQLname = XMLReader.produceVOPKQuerySQLname;
	public String produceVOInsertSQLname = XMLReader.produceVOInsertSQLname;
	public String produceVOUpdateSQLname = XMLReader.produceVOUpdateSQLname;
	public String produceVOChangeUpdateSQLname = XMLReader.produceVOChangeUpdateSQLname;
	public String produceVOGetResultSetname = XMLReader.produceVOGetResultSetname;
	public String produceVOGetParametername = XMLReader.produceVOGetParametername;
	private String VOgetAIPKname = XMLReader.vogetaipkname;
 

	/**
	 * 生成DAO接口与DAO实现类
	 * 
	 * @throws CommonException
	 */
	public void buildDAO() throws CommonException
	{

		try
		{

			if (XMLReader.tablelist == null)
			{
				XMLReader.readXMLFile();
			}

			// 关联DAO基类存放目录
			File daofolder = new File(daofolderpath);

			if (!daofolder.exists())
			{
				daofolder.mkdirs();
			}

			for (int i = 0; i < XMLReader.tablelist.size(); i++)
			{

				TableVO tvo = (TableVO) XMLReader.tablelist.get(i);
 
				// 获取约定的DAO基类的规范命名
				String BasicDAOFilename = tvo.getBasicDAOClassname() + ".java";
				FileWriter writer = new FileWriter(daofolderpath + BasicDAOFilename);

				System.out.println("dao:" + tvo.getName()+"   "+daofolderpath + BasicDAOFilename);
				
				writer.write("package " + daopackage + ";\n");
				writer.write("import java.sql.Connection;\n");
				writer.write("import java.sql.ResultSet;\n");
				writer.write("import java.sql.Statement;\n");
				writer.write("import java.text.SimpleDateFormat;\n");
				writer.write("import java.util.ArrayList;\n");
				writer.write("import java.util.List;\n");
				writer.write("import javax.sql.DataSource;\n");
				writer.write("import org.springframework.jdbc.datasource.DataSourceUtils;\n");
				writer.write("import com.universeview.bean." + tvo.getVOClassname() + ";\n");
				writer.write("import com.universeview.utils.DataVO;\n");
				writer.write("import com.universeview.utils.SplitPageVO;\n");
				writer.write("import com.universeview.utils.CommonException;\n");
				writer.write("import com.universeview.utils.Syslog;\n");
				writer.write("public class " + tvo.getBasicDAOClassname() + "{\n");
				writer
						.write("protected SimpleDateFormat DF = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\"); \n");
				writer.write("protected Connection	conn		= null; \n");
				writer.write("protected DataSource	dataSource	= null; \n");
				writer.write("public Connection getConn(){\n return conn; }\n");
				writer.write("public void setConn(Connection conn){ \n this.conn = conn; }\n");
				writer.write("public DataSource getDataSource() {\n return dataSource; }\n");
				writer
						.write("public void setDataSource(DataSource dataSource){\n this.dataSource = dataSource;}\n");

				producequerybyid(writer, tvo);
				producequery(writer, tvo);

				producequeryAppro(writer, tvo);
				producequeryorderby(writer, tvo);

				produceupdatebypk(writer, tvo);
				produceupdatechangebypk(writer, tvo);
				producedelbypk(writer, tvo);
				produceinsert(writer, tvo);
				producedelete(writer, tvo);
				produceupdate(writer, tvo);

				producequerySP(writer, tvo);
				producequerySPAppro(writer, tvo);

				//				
				// //生成查询方法
				// producequery(writer, tvo);
				// //生成带有模糊功能的查询方法
				// producequeryAppro(writer, tvo);
				// //生成带有指定排序功能的查询方法
				// producequeryorderby(writer, tvo);
				// //produceupdatebypk(writer, tvo);
				// //插入数据,该插入数据同时会获取插入的数据
				// produceinsert(writer, tvo);
				// //删除方法
				// producedelete(writer, tvo);
				// //更新方法
				// produceupdate(writer, tvo);
				// //分页查询
				// producequerySP(writer, tvo);

				writer.write("}");
				writer.close();

				// 获取DAO's name
				String advfileName = tvo.getDAOClassname();
				String fileName = tvo.getBasicDAOClassname();
				File advvo = new File(daoadvfolderpath + advfileName + ".java");
				if (!advvo.exists())
				{
					FileWriter advwriter = new FileWriter(daoadvfolderpath + advfileName + ".java");
					advwriter.write("package " + daoadvpackage + ";\n");
					advwriter.write("import " + daopackage + "." + fileName + ";\n");
					advwriter.write("public class " + advfileName + " extends " + fileName
							+ "\n {\n }\n");
					advwriter.close();
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new CommonException(" 配置文件解析错误");
		}
	}

	// 查询方法
	public void producequery(FileWriter writer, TableVO tvo) throws IOException
	{
		String VOClassname = tvo.getVOClassname();
		String VOObjectname = tvo.getVOObjectname();
		String ResVOObjectname = tvo.getResVOObjectname();

		writer.write("public List<" + VOClassname + "> " + queryname + "(" + VOClassname + " "
				+ VOObjectname + ") throws Exception {\n");
		writer.write("Statement statement = null;\n");
		writer.write("ResultSet rs = null;\n");
		writer.write("try{ \n");
		writer
				.write("List<" + VOClassname + "> reslist = new ArrayList<" + VOClassname
						+ ">(); \n");
		writer.write(" conn = DataSourceUtils.getConnection(dataSource);\n");
		writer.write(" statement = conn.createStatement();\n");
		writer.write("String sql =\" SELECT * FROM " + tvo.getName() + " \"+ " + VOObjectname + "."
				+ produceVOQuerySQLname + "(); \n");
		writer.write("Syslog.log(sql);\n");
		writer.write(" rs = statement.executeQuery(sql); \n");
		writer.write("while (rs.next()) { \n");
		writer.write(VOClassname + " " + ResVOObjectname + " = new " + VOClassname + "();\n");
		writer.write(ResVOObjectname + "." + produceVOGetResultSetname + "(rs); \n");
		writer.write("reslist.add(" + ResVOObjectname + "); }\n");
		writer.write(" return reslist; \n");
		writer.write("}catch (Exception e) { \n");
		writer.write("e.printStackTrace(); \n");
		writer.write(" throw new CommonException(\"数据库错误,查询数据失败！\"); \n");
		writer.write(" }\n");
		writer.write("finally{ \n");
		writer.write("if (null != statement){ \n");
		writer.write(" statement.close(); }\n");
		writer.write(" if (null != rs){\n");
		writer.write(" rs.close();}\n");
		// writer.write(" if (null != conn){\n");
		// writer.write(" conn.close();}\n");
		writer.write(" }\n");
		writer.write(" }\n");
		writer.write(" \n");
		writer.write(" \n");

	}

	// 查询方法
	public void producequerybyid(FileWriter writer, TableVO tvo) throws IOException
	{

		String VOClassname = tvo.getVOClassname();
		String VOObjectname = tvo.getVOObjectname();
		String ResVOObjectname = tvo.getResVOObjectname();

		System.out.println(">>>>>>>>>public  " + VOClassname + "  " + queryname + "byid");

		writer.write("public  " + VOClassname + "  " + queryname + "byid (" + VOClassname + " "
				+ VOObjectname + ") throws Exception {\n");
		writer.write("Statement statement = null;\n");
		writer.write("ResultSet rs = null;\n");
		writer.write("try{ \n");

		writer.write(" conn = DataSourceUtils.getConnection(dataSource);\n");
		writer.write(" statement = conn.createStatement();\n");

		writer.write("String sql =\" SELECT * FROM  " + tvo.getName() + "   \"+" + VOObjectname
				+ "." + produceVOPKQuerySQLname + "(); \n");

		writer.write("Syslog.log(sql);\n");
		writer.write(" rs = statement.executeQuery(sql); \n");

		writer.write("if (rs.next()) \n { \n");
		writer.write(VOClassname + " " + ResVOObjectname + " = new " + VOClassname + "();\n");
		writer.write(ResVOObjectname + "." + produceVOGetResultSetname + "(rs); \n");
		writer.write("return " + ResVOObjectname + " ;   \n");

		writer.write(" }else{ \n");
		writer.write("return null;   \n");
		writer.write(" } \n");
		writer.write("}catch (Exception e) { \n");
		writer.write("e.printStackTrace(); \n");
		writer.write(" throw new CommonException(\"数据库错误,查询数据失败！\"); \n");
		writer.write(" }\n");
		writer.write("finally{ \n");
		writer.write("if (null != statement){ \n");
		writer.write(" statement.close(); }\n");
		writer.write(" if (null != rs){\n");
		writer.write(" rs.close();}\n");
		// writer.write(" if (null != conn){\n");
		// writer.write(" conn.close();}\n");
		writer.write(" }\n");
		writer.write(" }\n");
		writer.write(" \n");
		writer.write(" \n");

	}

	// 查询方法
	public void producequeryorderby(FileWriter writer, TableVO tvo) throws IOException
	{
		String VOClassname = tvo.getVOClassname();
		String VOObjectname = tvo.getVOObjectname();
		String ResVOObjectname = tvo.getResVOObjectname();

		writer.write("public List<" + VOClassname + "> " + queryname + "orderby (" + VOClassname
				+ " " + VOObjectname + ", String orderbystr) throws Exception {\n");
		writer.write("Statement statement = null;\n");
		writer.write("ResultSet rs = null;\n");
		writer.write("try{ \n");
		writer
				.write("List<" + VOClassname + "> reslist = new ArrayList<" + VOClassname
						+ ">(); \n");
		writer.write(" conn = DataSourceUtils.getConnection(dataSource);\n");
		writer.write(" statement = conn.createStatement();\n");
		writer.write("String sql =\" SELECT * FROM " + tvo.getName() + " \"+ " + VOObjectname + "."
				+ produceVOQuerySQLname + "() +\" order by \"+ orderbystr; \n");
		writer.write("Syslog.log(sql);\n");
		writer.write(" rs = statement.executeQuery(sql); \n");
		writer.write("while (rs.next()) { \n");
		writer.write(VOClassname + " " + ResVOObjectname + " = new " + VOClassname + "();\n");
		writer.write(ResVOObjectname + "." + produceVOGetResultSetname + "(rs); \n");
		writer.write("reslist.add(" + ResVOObjectname + "); }\n");
		writer.write(" return reslist; \n");
		writer.write("}catch (Exception e) { \n");
		writer.write("e.printStackTrace(); \n");
		writer.write(" throw new CommonException(\"数据库错误,查询数据失败！\"); \n");
		writer.write(" }\n");
		writer.write("finally{ \n");
		writer.write("if (null != statement){ \n");
		writer.write(" statement.close(); }\n");
		writer.write(" if (null != rs){\n");
		writer.write(" rs.close();}\n");
		// writer.write(" if (null != conn){\n");
		// writer.write(" conn.close();}\n");
		writer.write(" }\n");
		writer.write(" }\n");
		writer.write(" \n");
		writer.write(" \n");

	}

	public void producequeryAppro(FileWriter writer, TableVO tvo) throws IOException
	{
		String VOClassname = tvo.getVOClassname();
		String VOObjectname = tvo.getVOObjectname();
		String ResVOObjectname = tvo.getResVOObjectname();

		writer.write("public List<" + VOClassname + "> " + queryapproname + "(" + VOClassname + " "
				+ VOObjectname + ") throws Exception {\n");
		writer.write("Statement statement = null;\n");
		writer.write("ResultSet rs = null;\n");
		writer.write("try{ \n");
		writer
				.write("List<" + VOClassname + "> reslist = new ArrayList<" + VOClassname
						+ ">(); \n");
		writer.write(" conn = DataSourceUtils.getConnection(dataSource);\n");
		writer.write(" statement = conn.createStatement();\n");
		writer.write("String sql =\" SELECT * FROM " + tvo.getName() + " \"+ " + VOObjectname + "."
				+ produceVOQuerySQLApproname + "(); \n");
		writer.write("Syslog.log(sql);\n");
		writer.write(" rs = statement.executeQuery(sql); \n");
		writer.write("while (rs.next()) { \n");
		writer.write(VOClassname + " " + ResVOObjectname + " = new " + VOClassname + "();\n");
		writer.write(ResVOObjectname + "." + produceVOGetResultSetname + "(rs); \n");
		writer.write("reslist.add(" + ResVOObjectname + "); }\n");
		writer.write(" return reslist; \n");
		writer.write("}catch (Exception e) { \n");
		writer.write("e.printStackTrace(); \n");
		writer.write(" throw new CommonException(\"数据库错误,查询数据失败！\"); \n");
		writer.write(" }\n");
		writer.write("finally{ \n");
		writer.write("if (null != statement){ \n");
		writer.write(" statement.close(); }\n");
		writer.write(" if (null != rs){\n");
		writer.write(" rs.close();}\n");
		// writer.write(" if (null != conn){\n");
		// writer.write(" conn.close();}\n");
		writer.write(" }\n");
		writer.write(" }\n");
		writer.write(" \n");
		writer.write(" \n");

	}

	// 查询方法
	public void producequerySPAppro(FileWriter writer, TableVO tvo) throws IOException
	{
		String VOClassname = tvo.getVOClassname();
		String VOObjectname = tvo.getVOObjectname();
		String ResVOObjectname = tvo.getResVOObjectname();

		writer.write("public DataVO " + querysplitapproname + "(" + VOClassname + " "
				+ VOObjectname + ", SplitPageVO spvo) throws Exception {\n");
		writer.write("Statement statement = null;\n");
		writer.write("ResultSet rs = null;\n");
		writer.write("try{ \n");

		writer.write("int beginnumber = spvo.getItemnumber() * (spvo.getCurrpagenumber() - 1);\n");
		writer.write("int itnumber = spvo.getItemnumber();\n");
		writer.write("DataVO dvo = new DataVO();\n");

		// PREPARE
		writer
				.write("List<" + VOClassname + "> reslist = new ArrayList<" + VOClassname
						+ ">(); \n");
		writer.write(" conn = DataSourceUtils.getConnection(dataSource);\n");
		writer.write(" statement = conn.createStatement();\n");

		// SQL

		writer.write("String where_sql =" + VOObjectname + "." + produceVOQuerySQLApproname
				+ "(); \n");
		writer.write("String totalsql = \" SELECT COUNT(*) TOTALNUM FROM " + tvo.getName()
				+ "\" +where_sql; \n");
		writer
				.write("String sql = \" SELECT A.*,TOTAL.TOTALNUM  FROM "
						+ tvo.getName()
						+ " A,(\" + totalsql + \") TOTAL \" + where_sql	+ \"  LIMIT \" + beginnumber + \",\" + itnumber; \n");

		writer.write("Syslog.log(sql);\n");
		writer.write("int totalnum = -1;\n");

		writer.write(" rs = statement.executeQuery(sql); \n");
		writer.write("while (rs.next()) { \n");

		writer.write("if (rs.getString(\"TOTALNUM\") != null){ \n");
		writer.write("	totalnum = rs.getInt(\"TOTALNUM\");} \n");

		writer.write(VOClassname + " " + ResVOObjectname + " = new " + VOClassname + "();\n");
		writer.write(ResVOObjectname + "." + produceVOGetResultSetname + "(rs); \n");
		writer.write("reslist.add(" + ResVOObjectname + "); }\n");

		// 分页
		// 总条数
		writer.write("spvo.setTotalnumber(totalnum);\n");

		// 总页面数
		writer.write("int totalpagenumber = (int) Math.ceil((double) totalnum / itnumber);\n");
		writer.write("spvo.setTotalpagenumber(totalpagenumber);\n");
		writer.write("spvo.processPageNumber();\n");

		// =========================
		writer.write("dvo.setDatalist(reslist);\n");
		writer.write("dvo.setSpvo(spvo);\n");
		writer.write(" return dvo; \n");
		writer.write("}catch (Exception e) { \n");
		writer.write("e.printStackTrace(); \n");
		writer.write(" throw new CommonException(\"数据库错误,查询数据失败！\"); \n");
		writer.write(" }\n");
		writer.write("finally{ \n");
		writer.write("if (null != statement){ \n");
		writer.write(" statement.close(); }\n");
		writer.write(" if (null != rs){\n");
		writer.write(" rs.close();}\n");
		writer.write(" }\n");
		writer.write(" }\n");
		writer.write(" \n");
		writer.write(" \n");

	}

	// produceVOQuerySQLApproname
	// 查询方法
	public void producequerySP(FileWriter writer, TableVO tvo) throws IOException
	{
		String VOClassname = tvo.getVOClassname();
		String VOObjectname = tvo.getVOObjectname();
		String ResVOObjectname = tvo.getResVOObjectname();

		writer.write("public DataVO " + querysplitname + "(" + VOClassname + " " + VOObjectname
				+ ", SplitPageVO spvo) throws Exception {\n");
		writer.write("Statement statement = null;\n");
		writer.write("ResultSet rs = null;\n");
		writer.write("try{ \n");

		writer.write("int beginnumber = spvo.getItemnumber() * (spvo.getCurrpagenumber() - 1);\n");
		writer.write("int itnumber = spvo.getItemnumber();\n");
		writer.write("DataVO dvo = new DataVO();\n");

		// PREPARE
		writer
				.write("List<" + VOClassname + "> reslist = new ArrayList<" + VOClassname
						+ ">(); \n");
		writer.write(" conn = DataSourceUtils.getConnection(dataSource);\n");
		writer.write(" statement = conn.createStatement();\n");

		// SQL

		writer.write("String where_sql =" + VOObjectname + "." + produceVOQuerySQLname + "(); \n");
		writer.write("String totalsql = \" SELECT COUNT(*) TOTALNUM FROM " + tvo.getName()
				+ "\" +where_sql; \n");
		writer
				.write("String sql = \" SELECT A.*,TOTAL.TOTALNUM  FROM "
						+ tvo.getName()
						+ " A,(\" + totalsql + \") TOTAL \" + where_sql	+ \"  LIMIT \" + beginnumber + \",\" + itnumber; \n");

		writer.write("Syslog.log(sql);\n");
		writer.write("int totalnum = -1;\n");

		writer.write(" rs = statement.executeQuery(sql); \n");
		writer.write("while (rs.next()) { \n");

		writer.write("if (rs.getString(\"TOTALNUM\") != null){ \n");
		writer.write("	totalnum = rs.getInt(\"TOTALNUM\");} \n");

		writer.write(VOClassname + " " + ResVOObjectname + " = new " + VOClassname + "();\n");
		writer.write(ResVOObjectname + "." + produceVOGetResultSetname + "(rs); \n");
		writer.write("reslist.add(" + ResVOObjectname + "); }\n");

		// 分页
		// 总条数
		writer.write("spvo.setTotalnumber(totalnum);\n");

		// 总页面数
		writer.write("int totalpagenumber = (int) Math.ceil((double) totalnum / itnumber);\n");
		writer.write("spvo.setTotalpagenumber(totalpagenumber);\n");
		writer.write("spvo.processPageNumber();\n");

		// =========================
		writer.write("dvo.setDatalist(reslist);\n");
		writer.write("dvo.setSpvo(spvo);\n");

		writer.write(" return dvo; \n");
		writer.write("}catch (Exception e) { \n");
		writer.write("e.printStackTrace(); \n");
		writer.write(" throw new CommonException(\"数据库错误,查询数据失败！\"); \n");
		writer.write(" }\n");
		writer.write("finally{ \n");
		writer.write("if (null != statement){ \n");
		writer.write(" statement.close(); }\n");
		writer.write(" if (null != rs){\n");
		writer.write(" rs.close();}\n");
		// writer.write(" if (null != conn){\n");
		// writer.write(" conn.close();}\n");
		writer.write(" }\n");
		writer.write(" }\n");
		writer.write(" \n");
		writer.write(" \n");

	}

	// 删除方法--主键
	public void producedelbypk(FileWriter writer, TableVO tvo) throws IOException
	{
		String VOClassname = tvo.getVOClassname();
		String VOObjectname = tvo.getVOObjectname();
		String ResVOObjectname = tvo.getResVOObjectname();

		writer.write("public int " + deletebypkname + "(" + VOClassname + " " + VOObjectname
				+ ") throws Exception {\n");
		writer.write("Statement statement = null;\n");
		writer.write("try{ \n");
		writer.write(" conn = DataSourceUtils.getConnection(dataSource);\n");
		writer.write(" statement = conn.createStatement();\n");
		writer.write("String sql =\" DELETE FROM  " + tvo.getName() + "  \"+" + VOObjectname + "."
				+ produceVOPKQuerySQLname + "(); \n");
		writer.write("Syslog.log(sql);\n");
		writer.write("  return statement.executeUpdate(sql); \n");
		// writer.write("  conn.commit(); \n");
		writer.write("}catch (Exception e) { \n");
		writer.write("e.printStackTrace(); \n");
		writer.write(" throw new CommonException(\"数据库错误,更新数据失败！\"); \n");
		writer.write(" }\n");
		writer.write("finally{ \n");
		writer.write("if (null != statement){ \n");
		writer.write(" statement.close(); }\n");
		// writer.write(" if (null != conn){\n");
		// writer.write(" conn.close();}\n");
		writer.write(" }\n");
		writer.write(" }\n");
		writer.write(" \n");
		writer.write(" \n");
	}

	// 更新方法--主键
	public void produceupdatebypk(FileWriter writer, TableVO tvo) throws IOException
	{
		String VOClassname = tvo.getVOClassname();
		String VOObjectname = tvo.getVOObjectname();
		String ResVOObjectname = tvo.getResVOObjectname();

		writer.write("public int " + updatebypkname + "(" + VOClassname + " " + VOObjectname
				+ ") throws Exception {\n");
		writer.write("Statement statement = null;\n");
		writer.write("try{ \n");

		// 添加key检查
		FieldVO primaryfvo = tvo.getPrimaryfield();

		String Bigname = primaryfvo.getBigname();

		writer.write(" if(" + VOObjectname + ".get" + Bigname + "()<=0)\n{\n");
		writer.write(" throw new CommonException(\"服务器程序错误：更新时无法获得主键！\");");
		writer.write(" }\n");

		writer.write(" conn = DataSourceUtils.getConnection(dataSource);\n");
		writer.write(" statement = conn.createStatement();\n");
		writer.write("String sql =\" UPDATE  " + tvo.getName() + "\" +" + VOObjectname + "."
				+ produceVOUpdateSQLname + "() +\"  \"+" + VOObjectname + "."
				+ produceVOPKQuerySQLname + "(); \n");
		writer.write("Syslog.log(sql);\n");
		writer.write("  return statement.executeUpdate(sql); \n");
		// writer.write("  conn.commit(); \n");
		writer.write("}catch (Exception e) { \n");
		writer.write("e.printStackTrace(); \n");
		writer.write(" throw new CommonException(\"数据库错误,更新数据失败！\"); \n");
		writer.write(" }\n");
		writer.write("finally{ \n");
		writer.write("if (null != statement){ \n");
		writer.write(" statement.close(); }\n");
		// writer.write(" if (null != conn){\n");
		// writer.write(" conn.close();}\n");
		writer.write(" }\n");
		writer.write(" }\n");
		writer.write(" \n");
		writer.write(" \n");
	}

	// 值的更新方法--主键,
	public void produceupdatechangebypk(FileWriter writer, TableVO tvo) throws IOException
	{
		String VOClassname = tvo.getVOClassname();
		String VOObjectname = tvo.getVOObjectname();
		String ResVOObjectname = tvo.getResVOObjectname();

		writer.write("public int " + updatechangebypkname + "(" + VOClassname + " " + VOObjectname
				+ ") throws Exception {\n");
		writer.write("Statement statement = null;\n");
		writer.write("try{ \n");
		writer.write(" conn = DataSourceUtils.getConnection(dataSource);\n");
		writer.write(" statement = conn.createStatement();\n");
		writer.write("String sql =\" UPDATE  " + tvo.getName() + "\" +" + VOObjectname + "."
				+ produceVOChangeUpdateSQLname + "() +\"  \"+" + VOObjectname + "."
				+ produceVOPKQuerySQLname + "(); \n");
		writer.write("Syslog.log(sql);\n");
		writer.write("  return statement.executeUpdate(sql); \n");
		// writer.write("  conn.commit(); \n");
		writer.write("}catch (Exception e) { \n");
		writer.write("e.printStackTrace(); \n");
		writer.write(" throw new CommonException(\"数据库错误,更新数据失败！\"); \n");
		writer.write(" }\n");
		writer.write("finally{ \n");
		writer.write("if (null != statement){ \n");
		writer.write(" statement.close(); }\n");
		// writer.write(" if (null != conn){\n");
		// writer.write(" conn.close();}\n");
		writer.write(" }\n");
		writer.write(" }\n");
		writer.write(" \n");
		writer.write(" \n");
	}

	// 更新方法
	public void produceupdate(FileWriter writer, TableVO tvo) throws IOException
	{
		String VOClassname = tvo.getVOClassname();
		String VOObjectname = tvo.getVOObjectname();
		String ResVOObjectname = tvo.getResVOObjectname();
		String QueryVOObjectname = tvo.getQueryVOObjectname();

		writer.write("public int " + updatename + "(" + VOClassname + " " + VOObjectname + ","
				+ VOClassname + " " + QueryVOObjectname + ") throws Exception {\n");
		writer.write("Statement statement = null;\n");
		writer.write("try{ \n");
		writer.write(" conn = DataSourceUtils.getConnection(dataSource);\n");
		writer.write(" statement = conn.createStatement();\n");
		writer.write("String sql =\" UPDATE  " + tvo.getName() + "\" +" + VOObjectname + "."
				+ produceVOUpdateSQLname + "() +\"  \"+" + QueryVOObjectname + "."
				+ produceVOQuerySQLname + "(); \n");
		writer.write("Syslog.log(sql);\n");
		writer.write(" return  statement.executeUpdate(sql); \n");
		// writer.write("  conn.commit(); \n");
		writer.write("}catch (Exception e) { \n");
		writer.write("e.printStackTrace(); \n");
		writer.write(" throw new CommonException(\"数据库错误,更新数据失败！\"); \n");
		writer.write(" }\n");
		writer.write("finally{ \n");
		writer.write("if (null != statement){ \n");
		writer.write(" statement.close(); }\n");
		// writer.write(" if (null != conn){\n");
		// writer.write(" conn.close();}\n");
		writer.write(" }\n");
		writer.write(" }\n");
		writer.write(" \n");
		writer.write(" \n");
	}

	// 插入
	public void produceinsert(FileWriter writer, TableVO tvo) throws IOException
	{
		String VOClassname = tvo.getVOClassname();
		String VOObjectname = tvo.getVOObjectname();
		String ResVOObjectname = tvo.getResVOObjectname();

		writer.write("public " + VOClassname + "  " + insertname + "(" + VOClassname + " "
				+ VOObjectname + ") throws Exception {\n");
		writer.write("Statement statement = null;\n");
		writer.write("ResultSet rs = null;\n");
		writer.write("try{ \n");
		writer.write(" conn = DataSourceUtils.getConnection(dataSource);\n");
		writer.write(" statement = conn.createStatement();\n");
		writer.write(" String sql = " + VOObjectname + "." + produceVOInsertSQLname + "() ; \n ");
		writer.write(" Syslog.log(sql);\n");
		writer.write(" statement.executeUpdate(sql); \n");
		// writer.write("  conn.commit(); \n");
		writer.write(" String query_sql = \"SELECT * FROM " + tvo.getName() + " WHERE \"+ "
				+ VOObjectname + "." + VOgetAIPKname + "() +\"= (SELECT LAST_INSERT_ID())\"; \n");
		writer.write(" rs = statement.executeQuery(query_sql);\n");
		writer.write(" if (rs.next()){\n " + VOObjectname + "." + produceVOGetResultSetname
				+ "(rs);\n }\n \n");
		writer.write(" return " + VOObjectname + ";");

		writer.write("}catch (Exception e) { \n");
		writer.write("e.printStackTrace(); \n");
		writer.write(" throw new CommonException(\"数据库错误,更新数据失败！\"); \n");
		writer.write(" }\n");
		writer.write("finally{ \n");
		writer.write("if (null != statement){ \n");
		writer.write(" statement.close(); }\n");
		// writer.write(" if (null != conn){\n");
		// writer.write(" conn.close();}\n");
		writer.write(" }\n");
		writer.write(" }\n");
		writer.write(" \n");
		writer.write(" \n");
	}

	// 插入
	public void producedelete(FileWriter writer, TableVO tvo) throws IOException
	{
		String VOClassname = tvo.getVOClassname();
		String VOObjectname = tvo.getVOObjectname();
		String ResVOObjectname = tvo.getResVOObjectname();

		writer.write("public int " + deletename + "(" + VOClassname + " " + VOObjectname
				+ ") throws Exception {\n");
		writer.write("Statement statement = null;\n");
		writer.write("try{ \n");
		writer.write(" conn = DataSourceUtils.getConnection(dataSource);\n");
		writer.write(" statement = conn.createStatement();\n");
		writer.write("String sql =\" DELETE FROM " + tvo.getName() + "\" +" + VOObjectname + "."
				+ produceVOQuerySQLname + "() ; \n ");
		writer.write("Syslog.log(sql);\n");
		writer.write("  return statement.executeUpdate(sql); \n");
		// writer.write("  conn.commit(); \n");
		writer.write("}catch (Exception e) { \n");
		writer.write("e.printStackTrace(); \n");
		writer.write(" throw new CommonException(\"数据库错误,更新数据失败！\"); \n");
		writer.write(" }\n");
		writer.write("finally{ \n");
		writer.write("if (null != statement){ \n");
		writer.write(" statement.close(); }\n");
		// writer.write(" if (null != conn){\n");
		// writer.write(" conn.close();}\n");
		writer.write(" }\n");
		writer.write(" }\n");
		writer.write(" \n");
		writer.write(" \n");
	}

	/**
	 * 运行用以生成持久层
	 * 
	 * @param args
	 * @throws CommonException
	 */
	public static void main(String[] args) throws CommonException
	{

		try
		{

			MysqlDAOBuilder mdb = new MysqlDAOBuilder();
			mdb.buildDAO();
			System.out.println("MysqlDAO Build complete!");

		} catch (Exception e)
		{

			e.printStackTrace();
		}
	}
}
