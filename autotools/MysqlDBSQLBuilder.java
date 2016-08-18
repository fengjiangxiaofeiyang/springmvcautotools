package com.universeview.utils.autotools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.universeview.utils.CommonException;
import com.universeview.utils.autotools.config.FieldVO;
import com.universeview.utils.autotools.config.TableVO;
import com.universeview.utils.autotools.config.XMLReader;

public class MysqlDBSQLBuilder extends VOBuilder
{
	protected String voadvpackage = XMLReader.android_voadvpackage;

	protected String voadvfolderpath = XMLReader.android_voadvfolderpath;

	// protected String dbsql_folder = XMLReader.DBSQL_folder;
	protected String dbsql_folder = XMLReader.DBSQL_folder;

	protected String dbname = "basketball";

	protected String dbsqlxmlfile;
	protected String voname;

	public void buildAppMod() throws CommonException
	{
		try
		{
			if (XMLReader.tablelist == null)
			{
				XMLReader.readXMLFile();
			}

			// builder vo folder
			File vofolder = new File(dbsql_folder);
			if (!vofolder.exists())
			{
				vofolder.mkdirs();
			}

			// *************************************************
			// ******************新表SQL***********************
			// *************************************************

			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(dbsql_folder + "mysql.sql");

			writer.write("DROP DATABASE IF EXISTS `" + dbname + "`;\n");
			writer.write("CREATE DATABASE `" + dbname
					+ "` /*!40100 DEFAULT CHARACTER SET utf8 */;\n");
			writer.write("USE `" + dbname + "`;\n");

			// 创建VO
			for (int i = 0; i < XMLReader.tablelist.size(); i++)
			{
				TableVO tvo = (TableVO) XMLReader.tablelist.get(i);

				//
				writetable(tvo, writer);
			}

			writer.close();

			// *************************************************
			// ******************新字段SQL***********************
			// *************************************************

			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter(dbsql_folder + "mysqlnewfield.sql");
			System.out.println("new table:" + dbsql_folder + "mysqlnewfield.sql");

			writer.write("USE `" + dbname + "`;\n");

			// 创建VO
			for (int i = 0; i < XMLReader.tablelist.size(); i++)
			{
				TableVO tvo = (TableVO) XMLReader.tablelist.get(i);
				List fieldlist = tvo.getFieldlist();

				tablename = tvo.getName();

				System.out.println(">>> new field writing...table:" + tvo.getName()
						+ " field adv_filename:" + fieldlist.size());

				// 如果需要建立新的表
				if (tvo.getNewtable() == 1)
				{
					// 新表
					writetable(tvo, writer);

				} else
				{
					// ----------header---------------------------------
					// =======添加属性==============
					for (int j = 0; j < fieldlist.size(); j++)
					{
						FieldVO fvo = (FieldVO) fieldlist.get(j);

						// 新属性
						if (fvo.getNewfield() == 1)
						{
							String name = fvo.getName();
							String type = fvo.getType();

							System.out.println(">>>>>>name:" + name + "    type:" + type);

							// 通用式： alter table [表名] add [字段名] 字段属性 default 缺省值 default 是可选参数
							// alter table [表名] add 字段名 smallint default 0 增加数字字段，整型，缺省值为0
							// alter table [表名] add 字段名 int default 0 增加数字字段，长整型，缺省值为0
							// alter table [表名] add 字段名 single default 0 增加数字字段，单精度型，缺省值为0
							// alter table [表名] add 字段名 double default 0 增加数字字段，双精度型，缺省值为0
							// alter table [表名] add 字段名 Tinyint default 0 增加数字字段，字节型，缺省值为0

							if (type.equals("Date") || type.equals("Datetime"))
							{
								writer.write(" alter table " + tablename + " add " + name
										+ "  datetime ");

							} else if (type.equals("String"))
							{
								writer.write(" alter table " + tablename + " add   `" + name
										+ "` varchar(255)  ");

							} else
							{

								writer.write(" alter table " + tablename + " add " + name + "  "
										+ type);
							}

							if (fvo.getDefaultvalue() != null)
							{
								writer.write("  default " + fvo.getDefaultvalue() + "  ; \n");
							} else
							{
								writer.write(" ;\n");
							}

						}
					}
				}

			}

			writer.close();

		} catch (Exception e)
		{
			e.printStackTrace();
			throw new CommonException(" 配置文件解析错误");
		}
	}

	// 写表
	private void writetable(TableVO tvo, FileWriter writer) throws IOException
	{

		List fieldlist = tvo.getFieldlist();

		tablename = tvo.getName();

		System.out.println(">>>writing...table:" + tvo.getName() + " field adv_filename:"
				+ fieldlist.size());

		// ----------header----------------------------------

		writer.write("DROP TABLE IF EXISTS `" + tablename + "`;\n");
		writer.write("CREATE TABLE `" + tablename + "` ( \n");

		// =======添加属性==============
		for (int j = 0; j < fieldlist.size(); j++)
		{
			FieldVO fvo = (FieldVO) fieldlist.get(j);
			String name = fvo.getName();
			String type = fvo.getType();

			System.out.println(">>>>>>>>>>>>>name:" + name + "    type:" + type);

			if (j == 0)//
			{
				writer.write(" `" + name + "` int(11) NOT NULL AUTO_INCREMENT,\n");
				writer.write("  PRIMARY KEY (`" + name + "`)");

				if (j != fieldlist.size() - 1)
				{
					writer.write(",\n");
				}

			} else if (type.equals("int"))
			{
				writer.write(" `" + name + "` int(11) DEFAULT '0'");

				if (j != fieldlist.size() - 1)
				{
					writer.write(",\n");
				}

			} else if (type.equals("long"))
			{
				writer.write(" `" + name + "` bigint(20) DEFAULT '0'");

				if (j != fieldlist.size() - 1)
				{
					writer.write(",\n");
				}

			} else if (type.equals("String"))
			{
				writer.write("  `" + name + "` varchar(255) DEFAULT '0'");

				if (j != fieldlist.size() - 1)
				{
					writer.write(",\n");
				}

			} else if (type.equals("double"))
			{
				writer.write("  `" + name + "` double DEFAULT '0'");

				if (j != fieldlist.size() - 1)
				{
					writer.write(",\n");
				}

			} else if (type.equals("float"))
			{
				writer.write("  `" + name + "` float DEFAULT '0'");

				if (j != fieldlist.size() - 1)
				{
					writer.write(",\n");
				}

			} else if (type.equals("Date") || type.equals("Datetime"))
			{
				writer.write("  `" + name + "` datetime DEFAULT NULL  ");

				if (j != fieldlist.size() - 1)
				{
					writer.write(",\n");
				}
			}
		}

		// =====tableclass============
		writer.write("\n) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;\n\n");
	}

	/**
	 * @param args
	 * @throws CommonException
	 */
	public static void main(String[] args) throws CommonException
	{

		try
		{
			System.out.println("MysqlDBSQLBuilder builder exe complete...");
			MysqlDBSQLBuilder vb = new MysqlDBSQLBuilder();
			vb.buildAppMod();
			System.out.println("MysqlDBSQLBuilder builder exe complete...");
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
