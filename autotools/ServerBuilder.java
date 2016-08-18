package com.universeview.utils.autotools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.universeview.utils.CommonException;
import com.universeview.utils.autotools.config.TableVO;
import com.universeview.utils.autotools.config.XMLReader;

public class ServerBuilder extends VOBuilder
{
	protected String actionpackage = XMLReader.action_package;
	protected String actionfolderpath = XMLReader.action_folderpath;

	public void buildAppMod() throws CommonException
	{
		try
		{
			if (XMLReader.tablelist == null)
			{
				XMLReader.readXMLFile();
			}

			// --------------builder vo folder-----------------
			File vofolder = new File(XMLReader.server_basic_folderpath);
			if (!vofolder.exists())
			{
				vofolder.mkdirs();
			}

			// -------------basic-------------------------
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			String basicservername = XMLReader.server_folderpath + "\\" + XMLReader.basicserver
					+ ".java";
			System.out.println("basicservername:" + basicservername);

			FileWriter writer = new FileWriter(basicservername);

			// -----header--------
			// head
			writer.write("package " + XMLReader.server_package + ";\n");
			writer.write("import java.io.File;\n");
			writer.write("import java.util.List;\n");
			writer.write("import org.apache.commons.fileupload.FileItem;\n");
			writer.write("import com.universeview.utils.*;\n");
			writer.write("import java.text.SimpleDateFormat;\n");
			writer.write("import " + XMLReader.voadvpackage + ".*;\n");
			writer.write("import " + XMLReader.daoadvpackage + ".*;\n");
			writer.write("import org.springframework.transaction.annotation.Propagation;\n");
			writer.write("import org.springframework.beans.factory.annotation.Autowired;\n");
			writer.write("import org.springframework.transaction.annotation.Transactional;\n");
			writer
					.write("@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)\n");
			// basic class
			writer.write("public class  " + XMLReader.basicserver + "  \n { \n");
			// writer.write("private SimpleDateFormat DF = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\"); \n");

			// 添加server
			writer
					.write("protected SimpleDateFormat DF = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\"); \n");

			// 生成所有dao
			for (int j = 0; j < XMLReader.tablelist.size(); j++)
			{
				TableVO daotvo = (TableVO) XMLReader.tablelist.get(j);
				System.out.println("server:" + daotvo.getName());

				daotvo.buildName();
				writer.write("@Autowired\n");
				writer.write(" protected " + daotvo.getDao_classname() + " "
						+ daotvo.getDao_objectname() + ";\n ");

				// ------setter------
				writer.write(" public void set" + daotvo.getClassname() + "dao("
						+ daotvo.getDao_classname() + " " + daotvo.getDao_objectname() + ") \n ");
				writer.write(" { \n this." + daotvo.getDao_objectname() + "="
						+ daotvo.getDao_objectname() + " ;\n}\n ");

				// ------getter------
				writer.write(" public " + daotvo.getDao_classname() + "  get"
						+ daotvo.getClassname() + "dao() \n ");
				writer.write(" { \n return " + daotvo.getDao_objectname() + " ;\n}\n ");

			}

			writer.write("} \n");
			writer.close();

			// ===================表 basic server===================
			// 循环创建server
			for (int i = 0; i < XMLReader.tablelist.size(); i++)
			{
				TableVO tvo = (TableVO) XMLReader.tablelist.get(i);

				tvo.buildName();

				System.out.println("........name:" + tvo.getName() + "   "
						+ tvo.getServer_basic_filepath());

				// ===================basic-server===================
				// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
				writer = new FileWriter(tvo.getServer_basic_filepath());

				// ----------header----------------------------------
				// head
				writer.write("package " + XMLReader.server_basic_package + ";\n");
				writer.write("import java.io.File;\n");
				writer.write("import java.util.List;\n");
				writer.write("import org.apache.commons.fileupload.FileItem;\n");
				writer.write("import com.universeview.utils.*;\n");
				writer.write("import java.text.SimpleDateFormat;\n");
				writer.write("import org.springframework.transaction.annotation.Transactional;\n");
				writer.write("import org.springframework.transaction.annotation.Propagation;\n");

				writer.write("import " + XMLReader.voadvpackage + ".*;\n");
				writer.write("import " + XMLReader.daoadvpackage + ".*;\n");
				writer.write("import " + XMLReader.server_package + "." + XMLReader.basicserver
						+ ";\n");
				writer
						.write("@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)\n");
				// basic class
				writer.write("public class " + tvo.getServer_basic_classname() + " extends "
						+ XMLReader.basicserver + "  \n { \n");

				producequery(writer, tvo);
				producequeryAppro(writer, tvo);
				producequerybypk(writer, tvo);
				producequeryorderby(writer, tvo);

				produceupdatebypk(writer, tvo);
				produceinsert(writer, tvo);
				producedelete(writer, tvo);
				produceupdate(writer, tvo);

				producequerySP(writer, tvo);
				producequerySPAppro(writer, tvo);

				writer.write("} \n");
				writer.close();

				// ================== server===================
				// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
				File serverfile = new File(tvo.getServer_filepath());

				// 存在就跳过
				if (serverfile.exists())
				{
					continue;
				}

				//
				FileWriter writer2 = new FileWriter(tvo.getServer_filepath());

				// ----------header----------------------------------
				// head
				writer2.write("package " + XMLReader.server_package + ";\n");
				writer2.write("import " + XMLReader.server_basic_package + "."
						+ tvo.getServer_basic_classname() + ";  \n");

				writer2.write("import org.springframework.transaction.annotation.Propagation;\n");
				writer2.write("import org.springframework.transaction.annotation.Transactional;\n");
				writer2.write("import org.springframework.stereotype.Service;\n");
				writer2.write("@Service\n");
				writer2
						.write("@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)\n");
				// class
				writer2.write("public class " + tvo.getServer_classname() + " extends "
						+ tvo.getServer_basic_classname() + "  \n { \n");

				writer2.write("} \n");
				writer2.close();

			}

		} catch (Exception e)
		{
			e.printStackTrace();
			throw new CommonException(" 配置文件解析错误");
		}
	}

	/**
	 * @param args
	 * @throws CommonException
	 */
	public static void main(String[] args) throws CommonException
	{
		try
		{
			ServerBuilder vb = new ServerBuilder();
			vb.buildAppMod();
			System.out.println("server builder exe complete...");
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	// 查询方法
	public void producequery(FileWriter writer, TableVO tvo) throws IOException
	{
		String VOClassname = tvo.getVOClassname();
		String VOObjectname = tvo.getVOObjectname();
		String ResVOObjectname = tvo.getResVOObjectname();

		writer.write("public List<" + VOClassname + "> " + XMLReader.queryname + "(" + VOClassname
				+ " " + VOObjectname + ") throws Exception {\n");
		writer.write("  return " + tvo.getDao_objectname() + "." + XMLReader.queryname + "("
				+ VOObjectname + ") ; \n");
		writer.write(" }\n");
		writer.write(" \n");
		writer.write(" \n");

	}

	// 查询方法bypk
	public void producequerybypk(FileWriter writer, TableVO tvo) throws IOException
	{
		String VOClassname = tvo.getVOClassname();
		String VOObjectname = tvo.getVOObjectname();

		writer.write("public  " + VOClassname + "  " + XMLReader.querybypkname + "(" + VOClassname
				+ " " + VOObjectname + ") throws Exception {\n");
		writer.write("  return " + tvo.getDao_objectname() + "." + XMLReader.querybypkname + "("
				+ VOObjectname + ") ; \n");
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

		writer.write("public List<" + VOClassname + "> " + XMLReader.queryname + "orderby ("
				+ VOClassname + " " + VOObjectname + ", String orderbystr) throws Exception {\n");

		writer.write("  return " + tvo.getDao_objectname() + "." + XMLReader.queryname
				+ "orderby ( " + VOObjectname + ",   orderbystr)   ; \n");

		writer.write(" }\n");
		writer.write(" \n");
		writer.write(" \n");

	}

	public void producequeryAppro(FileWriter writer, TableVO tvo) throws IOException
	{
		String VOClassname = tvo.getVOClassname();
		String VOObjectname = tvo.getVOObjectname();
		String ResVOObjectname = tvo.getResVOObjectname();

		writer.write("public List<" + VOClassname + "> " + XMLReader.queryapproname + "("
				+ VOClassname + " " + VOObjectname + ") throws Exception {\n");

		writer.write("  return " + tvo.getDao_objectname() + "." + XMLReader.queryapproname + "( "
				+ VOObjectname + ") ; \n");

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

		writer.write("public DataVO " + XMLReader.querysplitapproname + "(" + VOClassname + " "
				+ VOObjectname + ", SplitPageVO spvo) throws Exception {\n");

		writer.write("  return " + tvo.getDao_objectname() + "." + XMLReader.querysplitapproname
				+ "( " + VOObjectname + ",   spvo);\n");

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

		writer.write("public DataVO " + XMLReader.querysplitname + "(" + VOClassname + " "
				+ VOObjectname + ", SplitPageVO spvo) throws Exception {\n");

		writer.write("  return " + tvo.getDao_objectname() + "." + XMLReader.querysplitname + "(  "
				+ VOObjectname + ",   spvo);\n");

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

		writer.write("public int " + XMLReader.updatebypkname + "(" + VOClassname + " "
				+ VOObjectname + ") throws Exception {\n");

		writer.write("  return " + tvo.getDao_objectname() + "." + XMLReader.updatebypkname + "(  "
				+ VOObjectname + ") ; \n");

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

		writer.write("public int " + XMLReader.updatename + "(" + VOClassname + " " + VOObjectname
				+ "," + VOClassname + " " + QueryVOObjectname + ") throws Exception {\n");

		writer.write("  return " + tvo.getDao_objectname() + "." + XMLReader.updatename + "(  "
				+ VOObjectname + ",  " + QueryVOObjectname + ") ;\n");

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

		writer.write("public " + VOClassname + "  " + XMLReader.insertname + "(" + VOClassname
				+ " " + VOObjectname + ") throws Exception {\n");

		writer.write("  return " + tvo.getDao_objectname() + "." + XMLReader.insertname + "( "
				+ VOObjectname + ") ;  \n");

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

		writer.write("public int " + XMLReader.deletename + "(" + VOClassname + " " + VOObjectname
				+ ") throws Exception {\n");

		writer.write("  return " + tvo.getDao_objectname() + "." + XMLReader.deletename + "( "
				+ VOObjectname + ") ; ");

		writer.write(" }\n");
		writer.write(" \n");
		writer.write(" \n");
	}

}
