package com.universeview.utils.autotools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.universeview.utils.CommonException;
import com.universeview.utils.autotools.config.AutoTool;
import com.universeview.utils.autotools.config.FieldVO;
import com.universeview.utils.autotools.config.ItemVO;
import com.universeview.utils.autotools.config.TableVO;
import com.universeview.utils.autotools.config.XMLReader;

public class VOBuilder
{

	protected String produceVOQuerySQLname = XMLReader.produceVOQuerySQLname;

	protected String produceVOPKQuerySQLname = XMLReader.produceVOPKQuerySQLname;

	protected String produceVOUpdateSQLname = XMLReader.produceVOUpdateSQLname;

	protected String produceVOChangeUpdateSQLname = XMLReader.produceVOChangeUpdateSQLname;

	protected String produceVOInsertSQLname = XMLReader.produceVOInsertSQLname;

	protected String produceVOGetResultSetname = XMLReader.produceVOGetResultSetname;
	protected String produceVOQuerySQLApproname = XMLReader.produceVOQuerySQLApproname;

	protected String produceVOGetParametername = XMLReader.produceVOGetParametername;
	protected String produceVOGetUpdateParametername = XMLReader.produceVOGetUpdateParametername;

	protected String produceVOGetParameterListname = XMLReader.produceVOGetParameterListname;

	// 创建显示用XMLStr
	protected String produceVOXMLStrname = XMLReader.produceVOXMLStrname;
	protected String produceVOXMLStrShortname = XMLReader.produceVOXMLStrShortname;

	// 读取xmlstr格式的的XML
	protected String produceGetVOXMLPListname = XMLReader.produceGetVOXMLPListname;
	protected String produceGetVOXMLPListArrayname = XMLReader.produceGetVOXMLPListArrayname;

	// 读取attribute格式的XML----暂时不使用
	protected String produceVOGetXMLname = XMLReader.produceVOGetXMLname;
	protected String produceVOGetXMLListname = XMLReader.produceVOGetXMLListname;

	// 内部通讯时，生成POST MAP
	protected String produceVOPOSTMAPname = XMLReader.produceVOPOSTMAPname;

	protected String VOgetAIPKname = XMLReader.vogetaipkname;

	protected String vopackage = XMLReader.vopackage;

	protected String vofolderpath = XMLReader.vofolderpath;

	protected String voadvpackage = XMLReader.voadvpackage;

	protected String voadvfolderpath = XMLReader.voadvfolderpath;

	protected String tablename = null;

	protected String firstchar = null;

	protected String classname = null;// 第一个大写

	protected String voclassname = null;

	protected String voobjectname = null;

	protected String basic_classname = null;

	protected String basic_h_filename = null;

	protected String basic_filename = null;

	protected String adv_classname = null;

	protected String adv_h_filename = null;

	protected String adv_filename = null;

	public static String BQ = "\"+BVO.Q+\"";

	public void buildWebMod() throws CommonException
	{

		try
		{
			if (XMLReader.tablelist == null)
			{
				XMLReader.readXMLFile();
			}

			// builder vo folder
			File vofolder = new File(vofolderpath);
			if (!vofolder.exists())
			{
				vofolder.mkdirs();
			}

			// 创建VO
			for (int i = 0; i < XMLReader.tablelist.size(); i++)
			{
				TableVO tvo = (TableVO) XMLReader.tablelist.get(i);
				List fieldlist = tvo.getFieldlist();

				tablename = tvo.getName();
				firstchar = tablename.substring(0, 1);
				classname = firstchar.toUpperCase() + tablename.substring(1);
				voclassname = classname + "VO";
				basic_filename = classname + "BasicVO.java";
				adv_filename = classname + "VO.java";

				System.out.println("building VO..." + basic_filename);
				System.out.println("writing...table:" + tvo.getName() + " field number:"
						+ fieldlist.size());

				System.out.println("folder ..." + vofolderpath + basic_filename);

				// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
				FileWriter writer = new FileWriter(vofolderpath + basic_filename);

				// head
				// writer.write("package " + vopackage + ";\n");
				// writer.write("import java.text.SimpleDateFormat;\n");
				// writer.write("import java.util.Date;\n");
				// writer.write("import java.util.List;\n");
				// writer.write("import java.sql.ResultSet;\n");
				// writer.write("import java.text.NumberFormat;\n");
				// writer.write("import javax.servlet.http.HttpServletRequest;\n");
				//

				// head
				writer.write("package " + vopackage + ";\n");
				writer.write("import java.text.SimpleDateFormat;\n");
				writer.write("import java.util.Date;\n");
				writer.write("import java.sql.ResultSet;\n");
				writer.write("import java.text.NumberFormat;\n");
				writer.write("import javax.servlet.http.HttpServletRequest;\n");
				writer.write("import java.sql.ResultSetMetaData;\n");

				writer.write("import java.util.ArrayList;\n");
				writer.write("import java.util.List;\n");
				writer.write("import java.util.HashMap;\n");
				writer.write("import java.util.Map;\n");

				writer.write("import  org.dom4j.Element;\n");

				writer.write("import " + voadvpackage + "." + voclassname + ";\n\n\n");

				writer.write("import com.universeview.utils.*;\n");

				// class
				writer.write("public class " + classname + "BasicVO extends BVO   ");

				// 如果是枚举数据类
				if (tvo.getEnumitem() == 1)
				{
					writer.write(" implements EnumItem ");
				}

				writer.write(" {\n");
				writer.write("public static SimpleDateFormat DF = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\"); \n");

				writer.write("private NumberFormat nf =  NumberFormat.getInstance(); \n  \n");

				// 如果是枚举数据类，添加VALUE属性的获取
				if (tvo.getEnumitem() == 1)
				{
					writer.write("public int getValue(){\n");
					writer.write(" return  " + tvo.getPrimaryfield().getName() + ";");
					writer.write("}\n");

					writer.write("public void setValue(int value){\n");
					writer.write(tvo.getPrimaryfield().getName() + "  = value;;");
					writer.write("}\n");

				}

				// 添加属性数组
				writer.write("final protected static String[] attributearray ={");
				for (int j = 0; j < fieldlist.size(); j++)
				{
					FieldVO fvo = (FieldVO) fieldlist.get(j);
					String name = fvo.getName();
					writer.write("\"" + name + "\"");
					if (j < fieldlist.size() - 1)
					{
						writer.write(",");
					}

				}
				writer.write("};\n  \n");

				// 添加属性名数组
				writer.write("final protected static String[] cnamearray ={");
				for (int j = 0; j < fieldlist.size(); j++)
				{
					FieldVO fvo = (FieldVO) fieldlist.get(j);
					String name = fvo.getCname();
					if (name == null)
					{
						name = "";
					}
					writer.write("\"" + name + "\"");
					if (j < fieldlist.size() - 1)
					{
						writer.write(",");
					}

				}
				writer.write("};\n  \n");

				// =======添加属性==============
				for (int j = 0; j < fieldlist.size(); j++)
				{
					FieldVO fvo = (FieldVO) fieldlist.get(j);
					String name = fvo.getName();
					String type = fvo.getType();

					if (type.equals("int"))
					{
						writer.write("protected " + type + " " + name + " = Integer.MIN_VALUE;\n");

					} else if (type.equals("long"))
					{
						writer.write("protected " + type + " " + name + " = Long.MIN_VALUE;\n");

					} else if (type.equals("String"))
					{
						writer.write("protected " + type + " " + name + " = null ;\n");
					} else if (type.equals("double"))
					{
						writer.write("protected " + type + " " + name
								+ " = -1* Double.MAX_VALUE;\n");

					} else if (type.equals("float"))
					{
						writer.write("protected " + type + " " + name + " = -1* Float.MAX_VALUE;\n");

					} else if (type.equals("Date"))
					{
						writer.write("protected " + type + " " + name + " = null ;\n");

						// 开始与结束时间
						writer.write("protected " + type + " " + name + "beg = null ;\n");
						writer.write("protected " + type + " " + name + "end = null ;\n");

						// 详细时间
						writer.write("protected Integer  year = Integer.MIN_VALUE;\n");
						writer.write("protected Integer  month = Integer.MIN_VALUE;\n");
						writer.write("protected Integer  day = Integer.MIN_VALUE;\n");
						writer.write("protected Integer  hour = Integer.MIN_VALUE;\n");
						writer.write("protected Integer  minute = Integer.MIN_VALUE;\n");
						writer.write("protected Integer  second = Integer.MIN_VALUE;\n");

						// 详细时间
						writer.write("protected Integer  year1 = Integer.MIN_VALUE;\n");
						writer.write("protected Integer  month1 = Integer.MIN_VALUE;\n");
						writer.write("protected Integer  day1 = Integer.MIN_VALUE;\n");
						writer.write("protected Integer  hour1 = Integer.MIN_VALUE;\n");
						writer.write("protected Integer  minute1 = Integer.MIN_VALUE;\n");
						writer.write("protected Integer  second1 = Integer.MIN_VALUE;\n");

					}

					// 如果是应用枚举，添加list
					if (fvo.getFieldtype() == FieldVO.FIELDTYPE_REFENUM)
					{
						writer.write("private List<EnumItem> " + name + "enumlist = null ;\n");
						writer.write("public void set" + name
								+ "enumlist( List<EnumItem> enumlist){ ;\n");
						writer.write(name + "enumlist = enumlist; \n");
						writer.write("} ;\n");
					}

				}

				// 后台方法
				// ########VO方法#########################
				produceGetterandSetter(writer, tvo);

				// #######DB#########################
				produceVOQuerySQL(writer, tvo);
				produceVOPKQuerySQL(writer, tvo);
				produceVOQueryAppro(writer, tvo);

				produceVOUpdateSQL(writer, tvo);
				produceVOChangeUpdateSQL(writer, tvo);

				produceVOInsertSQL(writer, tvo);
				produceVOGetResultSet(writer, tvo);

				// ########参数#########################
				produceVOGetParameter(writer, tvo);
				produceVOGetUpdateParameter(writer, tvo);
				produceVOGetParameterList(writer, tvo);

				// ########内部数据#########################
				// 生成XMLSTR
				produceXMLStr(writer, tvo);

				// 读取XMLSTR
				produceGetVOXMLPList(writer, tvo);
				produceGetVOXMLPListArray(writer, tvo);

				// 生成内部POST用map
				produceVOPostMap(writer, tvo);

				// ########页面#########################
				// 生成页面相关的方法
				producePageQueryTable(writer, tvo);
				producePageFieldQueryTable(writer, tvo);

				producePageListValueTR(writer, tvo);
				producePageListCNameTR(writer, tvo);

				producePageDetailModTR(writer, tvo);
				producePageFieldDetailTR(writer, tvo);

				// end=====tableclass============
				writer.write("}");
				writer.close();

				// adv==========================================
				File advvo = new File(voadvfolderpath + adv_filename);
				if (!advvo.exists())
				{
					System.out.println("adv path" + voadvfolderpath + adv_filename);
					System.out.println("adv vopackage" + vopackage);

					FileWriter advwriter = new FileWriter(voadvfolderpath + adv_filename);

					advwriter.write("package " + voadvpackage + ";\n");

					advwriter.write("import " + vopackage + "." + classname + "BasicVO;\n");

					// class
					advwriter.write("public class " + classname + "VO extends " + classname
							+ "BasicVO \n {\n }\n");

					advwriter.close();
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new CommonException(" 配置文件解析错误");
		}
	}

	// =====查询区域的制造函数=======================
	// 被config中query控制
	protected void producePageQueryTable(FileWriter writer, TableVO tvo) throws IOException
	{

		writer.write("public  String producePAGEQueryTR()\n");
		writer.write("{\n");

		writer.write("	StringBuffer queryTR = new StringBuffer();\n");

		List fieldlist = tvo.getFieldlist();
		int count = 0;
		for (int i = 0; i < fieldlist.size(); i++)
		{
			FieldVO fvo = (FieldVO) fieldlist.get(i);
			String name = fvo.getName();

			if (fvo.getQuery() == 1 && fvo.getCname() != null)
			{
				// 行开始
				if (count % 2 == 0)
				{
					writer.write("queryTR.append(\"<tr>\");\n");
				}

				// 显示属性名
				writer.write("queryTR.append(\"<td class=" + BQ + "tab-nam" + BQ + ">\");\n");
				writer.write("queryTR.append(\"" + fvo.getCname() + "\");\n");
				writer.write("queryTR.append(\" </td> \");\n");

				System.out.println(">>>>>>>>>>>>>>>>>Fieldtype()" + fvo.getFieldtype());

				// 一般input
				if (fvo.getFieldtype() <= 0)
				{
					if (fvo.getType().equals("Date"))
					{
						/************************************* 生成时间控件 ********************************************************/
						/*** 年 ***************************/
						writer.write("queryTR.append(\"<td class=" + BQ + "tab-inpste" + BQ
								+ ">\");\n");
						writer.write("queryTR.append(\"<select name=" + BQ + "year" + BQ + "id="
								+ BQ + fvo.getName() + BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");
						// 空白
						writer.write("queryTR.append(\"<option  selected>\");\n");
						writer.write("queryTR.append(\"\" );\n");
						writer.write("queryTR.append(\"</option>\"); \n");

						for (int j = FieldVO.beginYear; j <= FieldVO.beginYear + FieldVO.yearSpan; j++)
						{
							writer.write("queryTR.append(\"<option value=" + j + "\");\n");
							writer.write("queryTR.append(\">" + j + "年\");\n");
						}
						writer.write("queryTR.append(\"</select>\");\n");
						writer.write("queryTR.append(\"</td>\");\n");

						/************* 月 ******************/
						writer.write("queryTR.append(\"<td class=" + BQ + "tab-inpste" + BQ
								+ ">\");\n");
						writer.write("queryTR.append(\"<select name=" + BQ + "month" + BQ + "id="
								+ BQ + fvo.getName() + BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");
						// 空白
						writer.write("queryTR.append(\"<option  selected> \");\n");
						writer.write("queryTR.append(\"\" );\n");
						writer.write("queryTR.append(\"</option>\"); \n");

						for (int j = 1; j <= 12; j++)
						{
							writer.write("queryTR.append(\"<option value=" + j + "\");\n");
							writer.write("queryTR.append(\">" + j + "月\");\n");

						}
						writer.write("queryTR.append(\"</select>\");\n");
						writer.write("queryTR.append(\"</td>\");\n");

						/************* 日 ******************/
						writer.write("queryTR.append(\"<td class=" + BQ + "tab-inpste" + BQ
								+ ">\");\n");
						writer.write("queryTR.append(\"<select name=" + BQ + "day" + BQ + "id="
								+ BQ + fvo.getName() + BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");
						// 空白
						writer.write("queryTR.append(\"<option  selected> \");\n");
						writer.write("queryTR.append(\"\" );\n");
						writer.write("queryTR.append(\"</option>\"); \n");

						for (int j = 1; j <= 31; j++)
						{
							writer.write("queryTR.append(\"<option value=" + j + "\");\n");
							writer.write("queryTR.append(\">" + j + "日\");\n");

						}
						writer.write("queryTR.append(\"</select>\");\n");
						writer.write("queryTR.append(\"</td>\");\n");

						/************* 时 ******************/
						writer.write("queryTR.append(\"<td class=" + BQ + "tab-inpste" + BQ
								+ ">\");\n");
						writer.write("queryTR.append(\"<select name=" + BQ + "hour" + BQ + "id="
								+ BQ + fvo.getName() + BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");
						// 空白
						writer.write("queryTR.append(\"<option  selected> \");\n");
						writer.write("queryTR.append(\"\" );\n");
						writer.write("queryTR.append(\"</option>\"); \n");

						for (int j = 0; j <= 23; j++)
						{
							writer.write("queryTR.append(\"<option value=" + j + "\");\n");
							writer.write("queryTR.append(\">" + j + "时\");\n");

						}
						writer.write("queryTR.append(\"</select>\");\n");
						writer.write("queryTR.append(\"</td>\");\n");

						/************* 分 ******************/
						writer.write("queryTR.append(\"<td class=" + BQ + "tab-inpste" + BQ
								+ ">\");\n");
						writer.write("queryTR.append(\"<select name=" + BQ + "minute" + BQ + "id="
								+ BQ + fvo.getName() + BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");
						// 空白
						writer.write("queryTR.append(\"<option  selected> \");\n");
						writer.write("queryTR.append(\"\" );\n");
						writer.write("queryTR.append(\"</option>\"); \n");

						for (int j = 0; j <= 59; j++)
						{
							writer.write("queryTR.append(\"<option value=" + j + "\");\n");
							writer.write("queryTR.append(\">" + j + "分\");\n");

						}
						writer.write("queryTR.append(\"</select>\");\n");
						writer.write("queryTR.append(\"</td>\");\n");

						/************* 秒 ******************/
						writer.write("queryTR.append(\"<td class=" + BQ + "tab-inpste" + BQ
								+ ">\");\n");
						writer.write("queryTR.append(\"<select name=" + BQ + "second" + BQ + "id="
								+ BQ + fvo.getName() + BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");
						// 空白
						writer.write("queryTR.append(\"<option  selected> \");\n");
						writer.write("queryTR.append(\"\" );\n");
						writer.write("queryTR.append(\"</option>\"); \n");

						for (int j = 1; j <= 59; j++)
						{
							writer.write("queryTR.append(\"<option value=" + j + "\");\n");
							writer.write("queryTR.append(\">" + j + "秒\");\n");

						}
						writer.write("queryTR.append(\"</select>\");\n");
						writer.write("queryTR.append(\"</td>\");\n");

					} else
					{
						writer.write("queryTR.append(\"<td class=" + BQ + "tab-inpste" + BQ
								+ ">\");\n");
						writer.write("queryTR.append(\"  <input name=" + BQ + fvo.getName() + BQ
								+ " type=" + BQ + "text" + BQ + "" + " class=" + BQ + "srh" + BQ
								+ " value = " + BQ + "\"+get"
								+ AutoTool.getUpperFirst(fvo.getName()) + "pstr()+\"" + BQ
								+ "  />    \");\n");

						writer.write("queryTR.append(\"</td>\");\n");
					}
					// 固定范围的enum
				} else if (fvo.getFieldtype() == FieldVO.FIELDTYPE_FIXEDENUM)
				{

					writer.write("queryTR.append(\"<td class=" + BQ + "tab-inpste" + BQ + ">\");\n");
					writer.write("queryTR.append(\"<select name=" + BQ + fvo.getName() + BQ + "id="
							+ BQ + fvo.getName() + BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");

					// 空白
					writer.write("queryTR.append(\"<option > \");\n");
					writer.write("queryTR.append(\"\" );\n");
					writer.write("queryTR.append(\"</option>\"); \n");

					// 获得每个enumitem
					List<ItemVO> enumlist = fvo.getEnumlist();

					for (int j = 0; j < enumlist.size(); j++)
					{
						ItemVO itemvo = enumlist.get(j);

						writer.write("queryTR.append(\"<option value=" + itemvo.getValue()
								+ " \");\n");
						writer.write("if(get" + AutoTool.getUpperFirst(fvo.getName()) + "() ==  "
								+ itemvo.getValue() + "){\n");
						writer.write("queryTR.append(\" selected \");\n");
						writer.write("}\n");
						writer.write("queryTR.append(\"> \");\n");
						writer.write("queryTR.append(\"" + itemvo.getCname() + "\" );\n");

						writer.write("queryTR.append(\"</option>\"); \n");
					}

					writer.write("queryTR.append(\"</select>\");\n");
					writer.write("queryTR.append(\"</td>\");\n");

					// 引用范围的
				} else if (fvo.getFieldtype() == FieldVO.FIELDTYPE_REFENUM)
				{
					writer.write("queryTR.append(\"<td class=" + BQ + "tab-inpste" + BQ + ">\");\n");

					writer.write("queryTR.append(\"<select name=" + BQ + fvo.getName() + BQ + "id="
							+ BQ + fvo.getName() + BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");

					// 空白
					writer.write("queryTR.append(\"<option > \");\n");
					writer.write("queryTR.append(\"\" );\n");
					writer.write("queryTR.append(\"</option>\"); \n");

					// 注入数据
					writer.write(" if (" + name + "enumlist!=null && " + name
							+ "enumlist.size()>0){\n");
					writer.write(" for (int j=0;j<" + name + "enumlist.size();j++){\n");
					writer.write(" EnumItem enumitem =(EnumItem)" + name + "enumlist.get(j) ;\n");

					writer.write("queryTR.append(\"<option value= \"+enumitem.getValue()+\"\");\n");

					writer.write("if(get" + AutoTool.getUpperFirst(fvo.getName())
							+ "()==enumitem.getValue()){\n");
					writer.write("queryTR.append(\" selected \");\n");
					writer.write("}\n");
					writer.write("queryTR.append(\"> \");\n");
					writer.write("queryTR.append(enumitem.getCname() );\n");
					writer.write("queryTR.append(\"</option>\"); \n");

					writer.write(" }\n");
					writer.write(" }\n");

					writer.write("queryTR.append(\"</select>\");\n");
					writer.write("queryTR.append(\"</td>\");\n");
				}

				// 行结束
				if (count % 2 == 1)
				{
					writer.write("queryTR.append(\"</tr>\");\n");
				}

				count++;
			}

		}

		writer.write("return queryTR.toString();\n");
		writer.write("}\n\n");
	}

	// =====查询区域的制造函数=======================
	// 被config中query控制
	protected void producePageFieldQueryTable(FileWriter writer, TableVO tvo) throws IOException
	{

		List fieldlist = tvo.getFieldlist();
		int count = 0;
		for (int i = 0; i < fieldlist.size(); i++)
		{
			FieldVO fvo = (FieldVO) fieldlist.get(i);
			String name = fvo.getName();

			writer.write("public  String produce" + name + "QueryTR()\n");
			writer.write("{\n");

			writer.write("	StringBuffer queryTR = new StringBuffer();\n");

			if (fvo.getQuery() == 1 && fvo.getCname() != null)
			{
				// 行开始
				if (count % 2 == 0)
				{
					writer.write("queryTR.append(\"<tr>\");\n");
				}

				// 显示属性名
				writer.write("queryTR.append(\"<td class=" + BQ + "tab-nam" + BQ + ">\");\n");
				writer.write("queryTR.append(\"" + fvo.getCname() + "\");\n");
				writer.write("queryTR.append(\" </td> \");\n");

				System.out.println(">>>>>>>>>>>>>>>>>Fieldtype()" + fvo.getFieldtype());

				// 一般input
				if (fvo.getFieldtype() <= 0)
				{
					if (fvo.getType().equals("Date"))
					{
						/*************** 生成时间控件 ************************/
						/*** 年 ***************************/
						writer.write("queryTR.append(\"<td class=" + BQ + "tab-inpste" + BQ
								+ ">\");\n");
						writer.write("queryTR.append(\"<select name=" + BQ + "year" + BQ + "id="
								+ BQ + fvo.getName() + BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");
						// 空白
						writer.write("queryTR.append(\"<option  selected>\");\n");
						writer.write("queryTR.append(\"\" );\n");
						writer.write("queryTR.append(\"</option>\"); \n");

						for (int j = FieldVO.beginYear; j <= FieldVO.beginYear + FieldVO.yearSpan; j++)
						{
							writer.write("queryTR.append(\"<option value=" + j + "\");\n");
							writer.write("queryTR.append(\">" + j + "年\");\n");
						}
						writer.write("queryTR.append(\"</select>\");\n");
						writer.write("queryTR.append(\"</td>\");\n");

						/************* 月 ******************/
						writer.write("queryTR.append(\"<td class=" + BQ + "tab-inpste" + BQ
								+ ">\");\n");
						writer.write("queryTR.append(\"<select name=" + BQ + "month" + BQ + "id="
								+ BQ + fvo.getName() + BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");
						// 空白
						writer.write("queryTR.append(\"<option  selected> \");\n");
						writer.write("queryTR.append(\"\" );\n");
						writer.write("queryTR.append(\"</option>\"); \n");

						for (int j = 1; j <= 12; j++)
						{
							writer.write("queryTR.append(\"<option value=" + j + "\");\n");
							writer.write("queryTR.append(\">" + j + "月\");\n");

						}
						writer.write("queryTR.append(\"</select>\");\n");
						writer.write("queryTR.append(\"</td>\");\n");

						/************* 日 ******************/
						writer.write("queryTR.append(\"<td class=" + BQ + "tab-inpste" + BQ
								+ ">\");\n");
						writer.write("queryTR.append(\"<select name=" + BQ + "day" + BQ + "id="
								+ BQ + fvo.getName() + BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");
						// 空白
						writer.write("queryTR.append(\"<option  selected> \");\n");
						writer.write("queryTR.append(\"\" );\n");
						writer.write("queryTR.append(\"</option>\"); \n");

						for (int j = 1; j <= 31; j++)
						{
							writer.write("queryTR.append(\"<option value=" + j + "\");\n");
							writer.write("queryTR.append(\">" + j + "日\");\n");

						}
						writer.write("queryTR.append(\"</select>\");\n");
						writer.write("queryTR.append(\"</td>\");\n");

						/************* 时 ******************/
						writer.write("queryTR.append(\"<td class=" + BQ + "tab-inpste" + BQ
								+ ">\");\n");
						writer.write("queryTR.append(\"<select name=" + BQ + "hour" + BQ + "id="
								+ BQ + fvo.getName() + BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");
						// 空白
						writer.write("queryTR.append(\"<option  selected> \");\n");
						writer.write("queryTR.append(\"\" );\n");
						writer.write("queryTR.append(\"</option>\"); \n");

						for (int j = 0; j <= 23; j++)
						{
							writer.write("queryTR.append(\"<option value=" + j + "\");\n");
							writer.write("queryTR.append(\">" + j + "时\");\n");

						}
						writer.write("queryTR.append(\"</select>\");\n");
						writer.write("queryTR.append(\"</td>\");\n");

						/************* 分 ******************/
						writer.write("queryTR.append(\"<td class=" + BQ + "tab-inpste" + BQ
								+ ">\");\n");
						writer.write("queryTR.append(\"<select name=" + BQ + "minute" + BQ + "id="
								+ BQ + fvo.getName() + BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");
						// 空白
						writer.write("queryTR.append(\"<option  selected> \");\n");
						writer.write("queryTR.append(\"\" );\n");
						writer.write("queryTR.append(\"</option>\"); \n");

						for (int j = 0; j <= 59; j++)
						{
							writer.write("queryTR.append(\"<option value=" + j + "\");\n");
							writer.write("queryTR.append(\">" + j + "分\");\n");

						}
						writer.write("queryTR.append(\"</select>\");\n");
						writer.write("queryTR.append(\"</td>\");\n");

						/************* 秒 ******************/
						writer.write("queryTR.append(\"<td class=" + BQ + "tab-inpste" + BQ
								+ ">\");\n");
						writer.write("queryTR.append(\"<select name=" + BQ + "second" + BQ + "id="
								+ BQ + fvo.getName() + BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");
						// 空白
						writer.write("queryTR.append(\"<option  selected> \");\n");
						writer.write("queryTR.append(\"\" );\n");
						writer.write("queryTR.append(\"</option>\"); \n");

						for (int j = 1; j <= 59; j++)
						{
							writer.write("queryTR.append(\"<option value=" + j + "\");\n");
							writer.write("queryTR.append(\">" + j + "秒\");\n");

						}
						writer.write("queryTR.append(\"</select>\");\n");
						writer.write("queryTR.append(\"</td>\");\n");

					} else
					{
						writer.write("queryTR.append(\"<td class=" + BQ + "tab-inpste" + BQ
								+ ">\");\n");
						writer.write("queryTR.append(\"  <input name=" + BQ + fvo.getName() + BQ
								+ " type=" + BQ + "text" + BQ + "" + " class=" + BQ + "srh" + BQ
								+ " value = " + BQ + "\"+get"
								+ AutoTool.getUpperFirst(fvo.getName()) + "pstr()+\"" + BQ
								+ "  />    \");\n");

						writer.write("queryTR.append(\"</td>\");\n");
					}
					// 固定范围的enum
				} else if (fvo.getFieldtype() == FieldVO.FIELDTYPE_FIXEDENUM)
				{

					writer.write("queryTR.append(\"<td class=" + BQ + "tab-inpste" + BQ + ">\");\n");
					writer.write("queryTR.append(\"<select name=" + BQ + fvo.getName() + BQ + "id="
							+ BQ + fvo.getName() + BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");

					// 空白
					writer.write("queryTR.append(\"<option > \");\n");
					writer.write("queryTR.append(\"\" );\n");
					writer.write("queryTR.append(\"</option>\"); \n");

					// 获得每个enumitem
					List<ItemVO> enumlist = fvo.getEnumlist();

					for (int j = 0; j < enumlist.size(); j++)
					{
						ItemVO itemvo = enumlist.get(j);

						writer.write("queryTR.append(\"<option value=" + itemvo.getValue()
								+ " \");\n");
						writer.write("if(get" + AutoTool.getUpperFirst(fvo.getName()) + "() ==  "
								+ itemvo.getValue() + "){\n");
						writer.write("queryTR.append(\" selected \");\n");
						writer.write("}\n");
						writer.write("queryTR.append(\"> \");\n");
						writer.write("queryTR.append(\"" + itemvo.getCname() + "\" );\n");

						writer.write("queryTR.append(\"</option>\"); \n");
					}

					writer.write("queryTR.append(\"</select>\");\n");
					writer.write("queryTR.append(\"</td>\");\n");

					// 引用范围的
				} else if (fvo.getFieldtype() == FieldVO.FIELDTYPE_REFENUM)
				{
					writer.write("queryTR.append(\"<td class=" + BQ + "tab-inpste" + BQ + ">\");\n");

					writer.write("queryTR.append(\"<select name=" + BQ + fvo.getName() + BQ + "id="
							+ BQ + fvo.getName() + BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");

					// 空白
					writer.write("queryTR.append(\"<option > \");\n");
					writer.write("queryTR.append(\"\" );\n");
					writer.write("queryTR.append(\"</option>\"); \n");

					// 注入数据
					writer.write(" if (" + name + "enumlist!=null && " + name
							+ "enumlist.size()>0){\n");
					writer.write(" for (int j=0;j<" + name + "enumlist.size();j++){\n");
					writer.write(" EnumItem enumitem =(EnumItem)" + name + "enumlist.get(j) ;\n");

					writer.write("queryTR.append(\"<option value= \"+enumitem.getValue()+\"\");\n");

					writer.write("if(get" + AutoTool.getUpperFirst(fvo.getName())
							+ "()==enumitem.getValue()){\n");
					writer.write("queryTR.append(\" selected \");\n");
					writer.write("}\n");
					writer.write("queryTR.append(\"> \");\n");
					writer.write("queryTR.append(enumitem.getCname() );\n");
					writer.write("queryTR.append(\"</option>\"); \n");

					writer.write(" }\n");
					writer.write(" }\n");

					writer.write("queryTR.append(\"</select>\");\n");
					writer.write("queryTR.append(\"</td>\");\n");
				}

				// 行结束
				if (count % 2 == 1)
				{
					writer.write("queryTR.append(\"</tr>\");\n");
				}

				count++;
			}

			writer.write("return queryTR.toString();\n");
			writer.write("}\n\n");
		}

	}

	// 添加页面的时间控件
	public void produceTime(FileWriter writer, FieldVO fvo) throws IOException
	{

		/************************************* 生成时间控件 ********************************************************/
		/*** 年 ***************************/
		writer.write("modTR.append(\"<td class=" + BQ + "tab-inpste" + BQ + ">\");\n");
		writer.write("modTR.append(\"<select name=" + BQ + "year" + BQ + "id=" + BQ + fvo.getName()
				+ BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");
		// 空白
		writer.write("modTR.append(\"<option>\");\n");
		writer.write("modTR.append(\"\" );\n");
		writer.write("modTR.append(\"</option>\"); \n");

		for (int j = FieldVO.beginYear; j <= FieldVO.beginYear + FieldVO.yearSpan; j++)
		{
			writer.write("modTR.append(\"<option value=" + j + " \");\n");
			writer.write("if(get" + AutoTool.getUpperFirst(fvo.getName()) + "() !=null && get"
					+ AutoTool.getUpperFirst(fvo.getName()) + "().getYear() + 1900 ==  " + j
					+ "){\n");
			writer.write("modTR.append(\" selected \");\n");
			writer.write("}\n");
			writer.write("modTR.append(\"> \");\n");

			writer.write("modTR.append(\"" + j + "年\" );\n");
			writer.write("modTR.append(\"</option>\"); \n");

		}
		writer.write("modTR.append(\"</select>\");\n");
		writer.write("modTR.append(\"</td>\");\n");

		/************* 月 ******************/
		writer.write("modTR.append(\"<td class=" + BQ + "tab-inpste" + BQ + ">\");\n");
		writer.write("modTR.append(\"<select name=" + BQ + "month" + BQ + "id=" + BQ
				+ fvo.getName() + BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");
		// 空白
		writer.write("modTR.append(\"<option  selected> \");\n");
		writer.write("modTR.append(\"\" );\n");
		writer.write("modTR.append(\"</option>\"); \n");

		for (int j = 1; j <= 12; j++)
		{
			writer.write("modTR.append(\"<option value=" + j + " \");\n");
			writer.write("if(get" + AutoTool.getUpperFirst(fvo.getName()) + "() !=null &&get"
					+ AutoTool.getUpperFirst(fvo.getName()) + "().getMonth()+1   ==  " + j + "){\n");
			writer.write("modTR.append(\" selected \");\n");
			writer.write("}\n");
			writer.write("modTR.append(\"> \");\n");

			writer.write("modTR.append(\"" + j + "月\" );\n");
			writer.write("modTR.append(\"</option>\"); \n");

		}
		writer.write("modTR.append(\"</select>\");\n");
		writer.write("modTR.append(\"</td>\");\n");

		/************* 日 ******************/
		writer.write("modTR.append(\"<td class=" + BQ + "tab-inpste" + BQ + ">\");\n");
		writer.write("modTR.append(\"<select name=" + BQ + "day" + BQ + "id=" + BQ + fvo.getName()
				+ BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");
		// 空白
		writer.write("modTR.append(\"<option  selected> \");\n");
		writer.write("modTR.append(\"\" );\n");
		writer.write("modTR.append(\"</option>\"); \n");

		for (int j = 1; j <= 31; j++)
		{
			writer.write("modTR.append(\"<option value=" + j + " \");\n");
			writer.write("if(get" + AutoTool.getUpperFirst(fvo.getName()) + "() !=null &&get"
					+ AutoTool.getUpperFirst(fvo.getName()) + "().getDate()  ==  " + j + "){\n");
			writer.write("modTR.append(\" selected \");\n");
			writer.write("}\n");
			writer.write("modTR.append(\"> \");\n");

			writer.write("modTR.append(\"" + j + "日\" );\n");
			writer.write("modTR.append(\"</option>\"); \n");

		}
		writer.write("modTR.append(\"</select>\");\n");
		writer.write("modTR.append(\"</td>\");\n");

		/************* 时 ******************/
		writer.write("modTR.append(\"<td class=" + BQ + "tab-inpste" + BQ + ">\");\n");
		writer.write("modTR.append(\"<select name=" + BQ + "hour" + BQ + "id=" + BQ + fvo.getName()
				+ BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");
		// 空白
		writer.write("modTR.append(\"<option  selected> \");\n");
		writer.write("modTR.append(\"\" );\n");
		writer.write("modTR.append(\"</option>\"); \n");

		for (int j = 0; j <= 23; j++)
		{
			writer.write("modTR.append(\"<option value=" + j + " \");\n");
			writer.write("if(get" + AutoTool.getUpperFirst(fvo.getName()) + "() !=null &&get"
					+ AutoTool.getUpperFirst(fvo.getName()) + "().getHours()  ==  " + j + "){\n");
			writer.write("modTR.append(\" selected \");\n");
			writer.write("}\n");
			writer.write("modTR.append(\"> \");\n");

			writer.write("modTR.append(\"" + j + "时\" );\n");
			writer.write("modTR.append(\"</option>\"); \n");

		}
		writer.write("modTR.append(\"</select>\");\n");
		writer.write("modTR.append(\"</td>\");\n");

		/************* 分 ******************/
		writer.write("modTR.append(\"<td class=" + BQ + "tab-inpste" + BQ + ">\");\n");
		writer.write("modTR.append(\"<select name=" + BQ + "minute" + BQ + "id=" + BQ
				+ fvo.getName() + BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");
		// 空白
		writer.write("modTR.append(\"<option  selected> \");\n");
		writer.write("modTR.append(\"\" );\n");
		writer.write("modTR.append(\"</option>\"); \n");

		for (int j = 0; j <= 59; j++)
		{
			writer.write("modTR.append(\"<option value=" + j + " \");\n");
			writer.write("if(get" + AutoTool.getUpperFirst(fvo.getName()) + "() !=null &&get"
					+ AutoTool.getUpperFirst(fvo.getName()) + "().getMinutes()  ==  " + j + "){\n");
			writer.write("modTR.append(\" selected \");\n");
			writer.write("}\n");
			writer.write("modTR.append(\"> \");\n");

			writer.write("modTR.append(\"" + j + "分\" );\n");
			writer.write("modTR.append(\"</option>\"); \n");

		}
		writer.write("modTR.append(\"</select>\");\n");
		writer.write("modTR.append(\"</td>\");\n");

		/************* 秒 ******************/
		writer.write("modTR.append(\"<td class=" + BQ + "tab-inpste" + BQ + ">\");\n");
		writer.write("modTR.append(\"<select name=" + BQ + "second" + BQ + "id=" + BQ
				+ fvo.getName() + BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");
		// 空白
		writer.write("modTR.append(\"<option  selected> \");\n");
		writer.write("modTR.append(\"\" );\n");
		writer.write("modTR.append(\"</option>\"); \n");

		for (int j = 1; j <= 59; j++)
		{
			writer.write("modTR.append(\"<option value=" + j + " \");\n");
			writer.write("if(get" + AutoTool.getUpperFirst(fvo.getName()) + "() !=null &&get"
					+ AutoTool.getUpperFirst(fvo.getName()) + "().getSeconds()  ==  " + j + "){\n");
			writer.write("modTR.append(\" selected \");\n");
			writer.write("}\n");
			writer.write("modTR.append(\"> \");\n");

			writer.write("modTR.append(\"" + j + "秒\" );\n");
			writer.write("modTR.append(\"</option>\"); \n");

		}
		writer.write("modTR.append(\"</select>\");\n");
		writer.write("modTR.append(\"</td>\");\n");

	}

	// 添加页面固定的下拉菜单
	public void produceSelectedFiexd(FileWriter writer, FieldVO fvo) throws IOException
	{

		// 固定范围的列表
		writer.write("modTR.append(\"<select name=" + BQ + fvo.getName() + BQ + "id=" + BQ
				+ fvo.getName() + BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");

		// 空白
		writer.write("modTR.append(\"<option > \");\n");
		writer.write("modTR.append(\"\" );\n");
		writer.write("modTR.append(\"</option>\"); \n");

		// 获得每个enumitem
		List<ItemVO> enumlist = fvo.getEnumlist();

		for (int j = 0; j < enumlist.size(); j++)
		{
			ItemVO itemvo = enumlist.get(j);
			writer.write("modTR.append(\"<option value=" + itemvo.getValue() + " \");\n");
			writer.write("if(get" + AutoTool.getUpperFirst(fvo.getName()) + "() ==  "
					+ itemvo.getValue() + "){\n");
			writer.write("modTR.append(\" selected \");\n");
			writer.write("}\n");
			writer.write("modTR.append(\"> \");\n");

			writer.write("modTR.append(\"" + itemvo.getCname() + "\" );\n");
			writer.write("modTR.append(\"</option>\"); \n");
		}

		writer.write("modTR.append(\"</select>\");\n");
	}

	// 引用的下拉菜单
	public void produceSelectedRef(FileWriter writer, FieldVO fvo) throws IOException
	{

		String name = fvo.getName();
		// 引用范围的的列表
		writer.write("modTR.append(\"<select name=" + BQ + fvo.getName() + BQ + "id=" + BQ
				+ fvo.getName() + BQ + "class=" + BQ + "fxh" + BQ + ">\");\n");

		// 空白
		writer.write("modTR.append(\"<option > \");\n");
		writer.write("modTR.append(\"\" );\n");
		writer.write("modTR.append(\"</option>\"); \n");

		// 注入数据
		writer.write(" if (" + name + "enumlist!=null && " + name + "enumlist.size()>0){\n");
		writer.write(" for (int j=0;j<" + name + "enumlist.size();j++){\n");
		writer.write(" EnumItem enumitem =(EnumItem)" + name + "enumlist.get(j) ;\n");

		writer.write("modTR.append(\"<option value= \"+enumitem.getValue()+\"\");\n");

		writer.write("if(get" + AutoTool.getUpperFirst(fvo.getName())
				+ "()==enumitem.getValue()){\n");
		writer.write("modTR.append(\" selected \");\n");
		writer.write("}\n");
		writer.write("modTR.append(\"> \");\n");
		writer.write("modTR.append(enumitem.getCname() );\n");
		writer.write("modTR.append(\"</option>\"); \n");

		writer.write(" }\n");
		writer.write(" }\n");

		writer.write("modTR.append(\"</select>\");\n");

	}

	// =====制造每个属性的详细编辑区域的列表TR=======================
	protected void producePageFieldDetailTR(FileWriter writer, TableVO tvo) throws IOException
	{

		List fieldlist = tvo.getFieldlist();

		for (int i = 0; i < fieldlist.size(); i++)
		{
			FieldVO fvo = (FieldVO) fieldlist.get(i);
			String name = fvo.getName();

			writer.write("public   String produce" + name + "DetailTR()\n");
			writer.write("{\n");

			writer.write("	StringBuffer modTR = new StringBuffer();\n");

			if (fvo.getMod() == 1 && fvo.getCname() != null)
			{
				writer.write("modTR.append(\" <tr>\");\n");
				writer.write("modTR.append(\"<td class=" + BQ + "tab-nam" + BQ + ">\");\n");
				writer.write("modTR.append(\"" + fvo.getCname() + "\");\n");
				writer.write("modTR.append(\" </td> \");\n");

				writer.write("modTR.append(\"<td class=" + BQ + "tab-inpste" + BQ + ">\");\n");

				// 一般input
				if (fvo.getFieldtype() <= 0)
				{
					if (fvo.getType().equals("Date"))
					{
						// 创建时间控件
						produceTime(writer, fvo);
					} else
					{
						// 一般的属性
						writer.write("modTR.append(\"  <input name=" + BQ + fvo.getName() + BQ
								+ " type=" + BQ + "text" + BQ + "" + " class=" + BQ + "srh" + BQ
								+ " value = " + BQ + "\"+get"
								+ AutoTool.getUpperFirst(fvo.getName()) + "pstr()+\"" + BQ
								+ "  />    \");\n");

					}

				} else if (fvo.getFieldtype() == FieldVO.FIELDTYPE_FIXEDENUM)
				{
					// 固定的下拉
					produceSelectedFiexd(writer, fvo);
				} else if (fvo.getFieldtype() == FieldVO.FIELDTYPE_REFENUM)
				{
					// 引用下拉
					produceSelectedRef(writer, fvo);
				}

				writer.write("modTR.append(\"</td>\");\n");
				writer.write("modTR.append(\"</tr>\");\n\n");
			}

			writer.write("return modTR.toString();\n\n");
			writer.write("}\n\n");

		}

	}

	// =====制造详细编辑区域的列表TR=======================
	protected void producePageDetailModTR(FileWriter writer, TableVO tvo) throws IOException
	{

		writer.write("public   String producePageDetailModTR()\n");
		writer.write("{\n");

		writer.write("	StringBuffer modTR = new StringBuffer();\n");

		List fieldlist = tvo.getFieldlist();

		for (int i = 0; i < fieldlist.size(); i++)
		{
			FieldVO fvo = (FieldVO) fieldlist.get(i);
			String name = fvo.getName();

			if (fvo.getMod() == 1 && fvo.getCname() != null)
			{
				writer.write("modTR.append(\" <tr>\");\n");
				writer.write("modTR.append(\"<td class=" + BQ + "tab-nam" + BQ + ">\");\n");
				writer.write("modTR.append(\"" + fvo.getCname() + "\");\n");
				writer.write("modTR.append(\" </td> \");\n");

				writer.write("modTR.append(\"<td class=" + BQ + "tab-inpste" + BQ + ">\");\n");

				// 一般input
				if (fvo.getFieldtype() <= 0)
				{
					if (fvo.getType().equals("Date"))
					{
						// 创建时间控件
						produceTime(writer, fvo);
					} else
					{
						// 一般的属性
						writer.write("modTR.append(\"  <input name=" + BQ + fvo.getName() + BQ
								+ " type=" + BQ + "text" + BQ + "" + " class=" + BQ + "srh" + BQ
								+ " value = " + BQ + "\"+get"
								+ AutoTool.getUpperFirst(fvo.getName()) + "pstr()+\"" + BQ
								+ "  />    \");\n");

					}

				} else if (fvo.getFieldtype() == FieldVO.FIELDTYPE_FIXEDENUM)
				{
					// 固定的下拉
					produceSelectedFiexd(writer, fvo);
				} else if (fvo.getFieldtype() == FieldVO.FIELDTYPE_REFENUM)
				{
					// 引用下拉
					produceSelectedRef(writer, fvo);
				}

				writer.write("modTR.append(\"</td>\");\n");
				writer.write("modTR.append(\"</tr>\");\n\n");
			}
		}

		writer.write("return modTR.toString();\n\n");
		writer.write("}\n\n");
	}

	// =====制造显示列表TR=======================
	// <tr>
	// <td align="center" class="tab-inp">
	// <input type="checkbox" name="ids"
	// id="ids<%=goodsvo.getId()%>" value="<%=goodsvo.getId()%>"
	// size="1" />
	// </td>
	// <td align="center" class="tab-inp">
	// <%=goodsvo.getIdpstr()%>
	// </td>
	// <td align="center" class="tab-inp">
	// <input name="goodsnum<%=goodsvo.getId()%>" type="text"
	// value="<%=goodsvo.getGoodsnum()%>"
	// onchange="checkbox('<%=goodsvo.getId()%>')" size="4"/>
	// </td>
	// </tr>

	/**
	 * 
	 * @comment：列表中显示所有信息
	 * @exception
	 * @date 2014.03.12
	 */
	protected void producePageListValueTR(FileWriter writer, TableVO tvo) throws IOException
	{

		writer.write("public String producePageListShowTR()\n");
		writer.write("{\n");
		writer.write("	StringBuffer listTR = new StringBuffer();\n");

		List<FieldVO> fieldlist = tvo.getFieldlist();

		writer.write("listTR.append(\" <tr>\");\n");

		for (int i = 0; i < fieldlist.size(); i++)
		{

			FieldVO fvo = (FieldVO) fieldlist.get(i);

			String name = fvo.getName();

			String Bigname = AutoTool.getUpperFirst(name);

			if (fvo.getList() == 1 && fvo.getCname() != null)
			{

				writer.write("listTR.append(\"<td class=" + BQ + "tab-inp" + BQ + ">\");\n");

				/*
				 * @comment：对于一般的属性,取出属性值即可
				 */
				if (fvo.getFieldtype() <= 0)
				{

					writer.write("listTR.append(get" + Bigname + "pstr());\n");
				} else if (fvo.getFieldtype() == FieldVO.FIELDTYPE_FIXEDENUM)
				{

					/*
					 * @comment： 如果该属性是枚举类型,获取每一个枚举值
					 */
					List<ItemVO> enumlist = fvo.getEnumlist();

					for (int j = 0; j < enumlist.size(); j++)
					{

						ItemVO itemvo = enumlist.get(j);
						writer.write("if(get" + Bigname + "() ==  " + itemvo.getValue() + "){\n");
						writer.write("listTR.append(\"" + itemvo.getCname() + "\");\n");
						writer.write("}\n");
					}
				} else if (fvo.getFieldtype() == FieldVO.FIELDTYPE_REFENUM)
				{

					/*
					 * @comment：如果该属性是引用类型,则取出引用数据集
					 */
					writer.write(" if (" + name + "enumlist!=null && " + name
							+ "enumlist.size()>0){\n");
					writer.write(" for (int j=0;j<" + name + "enumlist.size();j++){\n");
					writer.write(" EnumItem enumitem =(EnumItem)" + name + "enumlist.get(j) ;\n");

					writer.write("if(get" + AutoTool.getUpperFirst(fvo.getName())
							+ "()==enumitem.getValue()){\n");
					writer.write("listTR.append(enumitem.getCname() );\n");
					writer.write("}\n");
					writer.write(" }\n");
					writer.write(" }\n");
				}
				writer.write("listTR.append(\" </td> \");\n");
			}
		}

		// <td align="center" class="tab-inp">
		// <input type="button" class="user_new_but" id="<%=goodsvo.getId()%>"
		// value="修改商品"
		// onclick="mod('<%=goodsvo.getId()%>')" size="3" />
		// </td>

		// 修改按钮
		writer.write("listTR.append(\"<td class=" + BQ + "tab-inp" + BQ + ">\");\n");

		writer.write("listTR.append(\"<input type=" + BQ + "button" + BQ + "  class=" + BQ
				+ "user_new_but" + BQ + "\");\n");
		writer.write("listTR.append(\"  id=" + BQ + "getId()" + BQ + " value=" + BQ + " 修改" + BQ
				+ "  \");\n");
		writer.write("listTR.append(\"onclick=" + BQ + "mod" + tablename + "(\"+getPK()+\")" + BQ
				+ " /> \");\n");
		writer.write("listTR.append(\" </td> \");\n");

		// 删除按钮
		writer.write("listTR.append(\"<td class=" + BQ + "tab-inp" + BQ + ">\");\n");

		writer.write("listTR.append(\"<input type=" + BQ + "button" + BQ + "  class=" + BQ
				+ "user_new_but" + BQ + "\");\n");
		writer.write("listTR.append(\"  id=" + BQ + "getId()" + BQ + " value=" + BQ + " 删除" + BQ
				+ "  \");\n");
		writer.write("listTR.append(\"onclick=" + BQ + "del" + tablename + "(\"+getPK()+\")" + BQ
				+ " /> \");\n");
		writer.write("listTR.append(\" </td> \");\n");

		writer.write("listTR.append(\"</tr>\");\n\n");

		writer.write("return listTR.toString();\n\n");
		writer.write("}\n\n");
	}

	// 显示name
	protected void producePageListCNameTR(FileWriter writer, TableVO tvo) throws IOException
	{

		writer.write("public   String producePageListCNameTR()\n");
		writer.write("{\n");

		writer.write("	StringBuffer listTR = new StringBuffer();\n");

		List fieldlist = tvo.getFieldlist();

		writer.write("listTR.append(\" <tr>\");\n");

		for (int i = 0; i < fieldlist.size(); i++)
		{
			FieldVO fvo = (FieldVO) fieldlist.get(i);
			String Bigname = AutoTool.getUpperFirst(fvo.getName());

			if (fvo.getList() == 1 && fvo.getCname() != null)
			{
				// <td align="center" class="tab-inp">
				// </td>

				writer.write("listTR.append(\"<td class=" + BQ + "tab-inp" + BQ + ">\");\n");
				writer.write("listTR.append(cnamearray[" + i + "]);\n");
				writer.write("listTR.append(\" </td> \");\n");
			}
		}

		writer.write("listTR.append(\"<td class=" + BQ + "tab-inp" + BQ + ">\");\n");
		writer.write("listTR.append(\"\" );\n");
		writer.write("listTR.append(\" </td> \");\n");

		writer.write("listTR.append(\"</tr>\");\n\n");

		writer.write("return listTR.toString();\n\n");
		writer.write("}\n\n");
	}

	// =====获取XML中数据的方法=======================
	protected void produceVOGetXML(FileWriter writer, TableVO tvo) throws IOException
	{

		List fieldlist = tvo.getFieldlist();

		writer.write("public void " + produceVOGetXMLname
				+ "(Element element) throws Exception \n { \n");

		writer.write("Element contentelement=null;\n");

		// 设置为不可以删掉一个数字
		for (int j = 0; j < fieldlist.size(); j++)
		{
			FieldVO fvo = (FieldVO) fieldlist.get(j);
			String name = fvo.getName();
			String type = fvo.getType();

			// ==================getAttribute=====================
			if (type.equals("int"))
			{
				writer.write("if (element.getAttribute(\"" + name + "\") != null )\n {\n");
				writer.write("if (!(element.getAttribute(\"" + name + "\").equals(\"\")))\n {\n");

				writer.write(name + " =  Integer.parseInt(element.getAttribute(\"" + name
						+ "\")); \n");

				writer.write("} \n");
				writer.write("} \n");

			} else if (type.equals("long"))
			{
				writer.write("if (element.getAttribute(\"" + name + "\") != null )\n {\n");
				writer.write("if (!(element.getAttribute(\"" + name + "\").equals(\"\")))\n {\n");

				writer.write(name + " =  Long.parseLong(element.getAttribute(\"" + name
						+ "\")); \n");

				writer.write("} \n");
				writer.write("} \n");

			} else if (type.equals("String"))
			{
				writer.write("if (element.getAttribute(\"" + name + "\") != null )\n {\n");
				writer.write("if (!(element.getAttribute(\"" + name + "\").equals(\"\")))\n {\n");
				writer.write(name + " =   element.getAttribute(\"" + name + "\" ); \n");
				writer.write("} \n");
				writer.write("} \n");
			} else if (type.equals("double"))
			{
				writer.write("if (element.getAttribute(\"" + name + "\") != null )\n {\n");
				writer.write("if (!(element.getAttribute(\"" + name + "\").equals(\"\")))\n {\n");

				writer.write(name + " =  Double.parseDouble(element.getAttribute(\"" + name
						+ "\")); \n");

				writer.write("} \n");
				writer.write("} \n");

			} else if (type.equals("float"))
			{
				writer.write("if (element.getAttribute(\"" + name + "\") != null )\n {\n");
				writer.write("if (!(element.getAttribute(\"" + name + "\").equals(\"\")))\n {\n");

				writer.write(name + " =  Float.parseFloat(element.getAttribute(\"" + name
						+ "\")); \n");

				writer.write("} \n");
				writer.write("} \n");
			}

			// 该语句有问题--方法过期
			else if (type.equals("Date"))
			{
				writer.write("if (element.getAttribute(\"" + name + "\") != null )\n {\n");
				writer.write("if (!(element.getAttribute(\"" + name + "\").equals(\"\")))\n {\n");

				writer.write(name + " =  DF.parse(element.getAttribute(\"" + name + "\")); \n");

				writer.write("} \n");
				writer.write("} \n");
			}

			// ========================content=====================
			writer.write(" contentelement= (Element) element.getElementsByTagName(\"" + name
					+ "\").item(0);\n");
			writer.write("if (contentelement != null )\n {\n");
			writer.write("if (contentelement.getTextContent() != null )\n {\n");
			writer.write("if (!(contentelement.getTextContent().equals(\"\")))\n {\n");

			if (type.equals("int"))
			{
				writer.write(name + " =  Integer.parseInt(contentelement.getTextContent()); \n");

			} else if (type.equals("long"))
			{
				writer.write(name + " =  Long.parseLong(contentelement.getTextContent()); \n");

			} else if (type.equals("String"))
			{

				writer.write(name + " = contentelement.getTextContent();\n");

			} else if (type.equals("double"))
			{
				writer.write(name + " =  Double.parseDouble(contentelement.getTextContent()); \n");

			} else if (type.equals("float"))
			{
				writer.write(name + " =  Float.parseFloat(contentelement.getTextContent()); \n");

			} else if (type.equals("Date"))// 该语句有问题--方法过期
			{
				writer.write(name + " =  DF.parse(contentelement.getTextContent()); \n");
			}

			writer.write("} \n");
			writer.write("} \n");
			writer.write("} \n");
		}

		writer.write("} \n");
	}

	// =====获取XML中数据列表的方法=======================
	protected void produceVOGetXMLList(FileWriter writer, TableVO tvo) throws IOException
	{

		writer.write("public List " + produceVOGetXMLListname
				+ "(Element elementlist) throws Exception \n { \n");

		writer.write("NodeList nodelist = elementlist.getElementsByTagName(\"" + tvo.getName()
				+ "\");\n");

		writer.write("Element element;\n");
		writer.write("List<" + voclassname + "> volist =   new ArrayList<" + voclassname + ">();\n");
		writer.write("for (int i = 0; i < nodelist.getLength(); i++)\n{\n");

		writer.write("element = (Element) nodelist.item(i);\n");

		writer.write(voclassname + " vo =new " + voclassname + "();\n");
		writer.write("vo." + produceVOGetXMLname + "(element);\n");
		writer.write("volist.add(vo);\n");
		writer.write("}\n");
		writer.write("return volist;\n");
		writer.write("}\n");

	}

	// 设置getter与setter
	protected void produceGetterandSetter(FileWriter writer, TableVO tvo) throws IOException
	{

		List fieldlist = tvo.getFieldlist();

		// 添加属性的查询语句
		// 属性
		for (int j = 0; j < fieldlist.size(); j++)
		{
			FieldVO fvo = (FieldVO) fieldlist.get(j);
			String type = fvo.getType();
			String name = fvo.getName();
			String firstchar = name.substring(0, 1);
			String Bigname = firstchar.toUpperCase() + name.substring(1);

			// get
			writer.write("public " + type + " get" + Bigname + "() \n { \n return " + name
					+ "; \n }\n");

			// 如果字段类型为Date,则year month day等set get方法
			if ("Date".equals(type) || "date".equals(type))
			{

				// get,beg
				writer.write("public " + type + " get" + Bigname + "beg() \n { \n return " + name
						+ "beg; \n }\n");
				// get,end
				writer.write("public " + type + " get" + Bigname + "end() \n { \n return " + name
						+ "end; \n }\n");

				// ====细分时间======================
				writer.write("public Integer getYear()\n{ \n return year ; \n }\n ");
				writer.write("public Integer getMonth()\n{ \n return month ; \n }\n ");
				writer.write("public Integer getDay()\n{ \n return day ; \n }\n ");
				writer.write("public Integer gethour()\n{ \n return hour ; \n }\n ");
				writer.write("public Integer getMinute()\n{ \n return minute ; \n }\n ");
				writer.write("public Integer getSecond()\n{ \n return second ; \n }\n ");

				// ====细分时间======================
				writer.write("public Integer getYear1()\n{ \n return year1 ; \n }\n ");
				writer.write("public Integer getMonth1()\n{ \n return month1 ; \n }\n ");
				writer.write("public Integer getDay1()\n{ \n return day1 ; \n }\n ");
				writer.write("public Integer gethour1()\n{ \n return hour1 ; \n }\n ");
				writer.write("public Integer getMinute1()\n{ \n return minute1 ; \n }\n ");
				writer.write("public Integer getSecond1()\n{ \n return second1 ; \n }\n ");

				writer.write("public void setYear(Integer year)\n{ \n this.year = year ; \n }\n ");
				writer.write("public void setMonth(Integer month)\n{ \n this.month = month ; \n }\n ");
				writer.write("public void setDay(Integer day)\n{ \n this.day = day ; \n }\n ");
				writer.write("public void sethour(Integer hour)\n{ \n this.hour = hour ; \n }\n ");
				writer.write("public void setMinute(Integer minute)\n{ \n this.minute = minute ; \n }\n ");
				writer.write("public void setSecond(Integer second)\n{ \n this.second = second ; \n }\n ");

				writer.write("public void setYear1(Integer year1)\n{ \n this.year1 = year1 ; \n }\n ");
				writer.write("public void setMonth1(Integer month1)\n{ \n this.month1 = month1 ; \n }\n ");
				writer.write("public void setDay1(Integer day1)\n{ \n this.day1 = day1 ; \n }\n ");
				writer.write("public void sethour1(Integer hour1)\n{ \n this.hour1 = hour1 ; \n }\n ");
				writer.write("public void setMinute1(Integer minute1)\n{ \n this.minute1 = minute1 ; \n }\n ");
				writer.write("public void setSecond1(Integer second1)\n{ \n this.second1 = second1 ; \n }\n ");

			}

			// =============prestr====================
			String prestr = "public String get" + Bigname + "pstr() \n { \n  ";

			if (type.equals("int"))
			{
				prestr = prestr + "if (this." + name + " > Integer.MIN_VALUE){ \n";
				prestr = prestr + "return String.valueOf(" + name + "); \n";
				writer.write(prestr + "}else{\n");
				writer.write("return \"\"; } \n } \n");
			} else if (type.equals("String"))
			{
				prestr = prestr + "if (this." + name + " !=null){ \n";
				prestr = prestr + "return " + name + "; \n";
				writer.write(prestr + "}else{\n");
				writer.write("return \"\";} \n } \n");

			} else if (type.equals("double"))
			{
				prestr = prestr + "  nf.setGroupingUsed(false);\n";
				prestr = prestr + " if (this." + name + " > -1*Double.MAX_VALUE){ \n";
				prestr = prestr + " return String.valueOf( nf.format( " + name + ")); \n";
				writer.write(prestr + "}else{\n");
				writer.write(" return \"\";} \n } \n");

			} else if (type.equals("float"))
			{
				prestr = prestr + "if (this." + name + " > -1*Float.MAX_VALUE){ \n";
				prestr = prestr + "return String.valueOf(" + name + "); \n";
				writer.write(prestr + "}else{\n");
				writer.write("return \"\"; } \n } \n");

			} else if (type.equals("Date"))
			{
				prestr = prestr + "if (this." + name + " !=null){ \n";
				prestr = prestr + "return DF.format(" + name + "); \n";
				writer.write(prestr + "}else{\n");
				writer.write("return \"\"; } \n } \n");
			}

			// =========================================
			// ================set======================
			writer.write("public void set" + Bigname + "( " + type + " " + name
					+ ") \n { \n this. " + name + "=" + name + " ; \n }\n");

			if (type.equals("Date"))
			{
				// set,beg
				writer.write("public void set" + Bigname + "beg( " + type + " " + name
						+ "beg) \n { \n this. " + name + "beg=" + name + "beg ; \n }\n");
				// set,end
				writer.write("public void set" + Bigname + "end( " + type + " " + name
						+ "end) \n { \n this. " + name + "end=" + name + "end ; \n }\n");
			}

			// 如果为自增主键
			if (fvo.getAutoIncrement() == 1 & fvo.getPrimarykey() == 1)
			{
				writer.write("public String getAIPK () \n { \n return \" " + name + " \"; \n }\n");
			}

			// 返回主键的值
			if (fvo.getAutoIncrement() == 1 & fvo.getPrimarykey() == 1)
			{
				writer.write("public int  getPK () \n { \n return " + name + "; \n }\n");
			}

		}

	}

	// 创建查询语句
	protected void produceVOQuerySQL(FileWriter writer, TableVO tvo) throws IOException
	{

		List fieldlist = tvo.getFieldlist();

		// =====查询语句produceQuerySql=======
		writer.write("public String  " + produceVOQuerySQLname + "(){\n");
		writer.write("StringBuffer sqlsb = new StringBuffer();\n");

		// 添加属性的查询语句
		// 属性
		for (int j = 0; j < fieldlist.size(); j++)
		{
			FieldVO fvo = (FieldVO) fieldlist.get(j);
			String name = fvo.getName();
			String type = fvo.getType();

			if (type.equals("int"))
			{
				writer.write("if (this." + name + " > Integer.MIN_VALUE){ \n");
				writer.write(" sqlsb.append(\" and " + name + " = \"); \n");
				writer.write(" sqlsb.append(this." + name + "); } \n");

			} else if (type.equals("long"))
			{
				writer.write("if (this." + name + " > Long.MIN_VALUE){ \n");
				writer.write(" sqlsb.append(\" and " + name + " = \"); \n");
				writer.write(" sqlsb.append(this." + name + "); } \n");

			} else if (type.equals("String"))
			{
				writer.write("if (this." + name + " !=null){ \n");
				writer.write(" sqlsb.append(\" and " + name + " = \'\"); \n");
				writer.write(" sqlsb.append(this." + name + ");  \n");
				writer.write(" sqlsb.append(\"\'\"); } \n");

			} else if (type.equals("double"))
			{
				writer.write("if (this." + name + " > -1*Double.MAX_VALUE){ \n");
				writer.write(" sqlsb.append(\" and " + name + " = \"); \n");
				writer.write(" sqlsb.append(this." + name + "); } \n");

			} else if (type.equals("float"))
			{
				writer.write("if (this." + name + " > -1*Float.MAX_VALUE){ \n");
				writer.write(" sqlsb.append(\" and " + name + " = \"); \n");
				writer.write(" sqlsb.append(this." + name + "); } \n");

			} else if (type.equals("Date"))
			{

				// 大于
				writer.write("if (this." + name + "beg !=null){ \n");
				writer.write(" sqlsb.append(\" and " + name + " >= \'\"); \n");
				writer.write(" sqlsb.append(DF.format(this." + name + "beg));  \n");
				writer.write(" sqlsb.append(\"\'\"); } \n");

				// 小于
				writer.write("if (this." + name + "end !=null){ \n");
				writer.write(" sqlsb.append(\" and " + name + " <= \'\"); \n");
				writer.write(" sqlsb.append(DF.format(this." + name + "end));  \n");
				writer.write(" sqlsb.append(\"\'\"); } \n");

			}

		}

		// 添加where的语句
		writer.write("if (sqlsb.length() > 0){  \n" + " sqlsb.delete(0, 5);  \n "
				+ " sqlsb.insert(0, \" where \"); \n" + " return sqlsb.toString(); \n"
				+ " } else { \n" + " return \"\";}");

		writer.write("}\n");
		// end=====produceQuerySql=======

	}

	// 生成XML的STRING---短标签
	protected void produceXMLStrShort(FileWriter writer, TableVO tvo) throws IOException
	{

		List fieldlist = tvo.getFieldlist();

		// =====查询语句produceQuerySql=======
		// writer.write("public String  produceQuerySql(){\n");
		writer.write("public String  " + produceVOXMLStrShortname + "(){\n");
		writer.write("StringBuffer sqlsb = new StringBuffer();\n");

		writer.write("sqlsb.append(\"<k>v</k>\"); \n");
		writer.write("sqlsb.append(\"<v>\"); \n");

		// 添加属性的查询语句
		// 属性
		for (int j = 0; j < fieldlist.size(); j++)
		{
			FieldVO fvo = (FieldVO) fieldlist.get(j);
			String name = fvo.getName();
			String type = fvo.getType();

			String firstchar = name.substring(0, 1);
			String Bigname = firstchar.toUpperCase() + name.substring(1);

			if (type.equals("int"))
			{
				writer.write("sqlsb.append(get" + Bigname + "pstr()); \n");
				writer.write("sqlsb.append(\"|\"); \n");

			} else if (type.equals("long"))
			{
				writer.write("sqlsb.append(get" + Bigname + "pstr()); \n");
				writer.write("sqlsb.append(\"|\"); \n");

			} else if (type.equals("String"))
			{
				writer.write("sqlsb.append(get" + Bigname + "pstr()); \n");
				writer.write("sqlsb.append(\"|\"); \n");
			} else if (type.equals("double"))
			{
				writer.write("sqlsb.append(get" + Bigname + "pstr()); \n");
				writer.write("sqlsb.append(\"|\"); \n");

			} else if (type.equals("float"))
			{
				writer.write("sqlsb.append(get" + Bigname + "pstr()); \n");
				writer.write("sqlsb.append(\"|\"); \n");

			} else if (type.equals("Date"))
			{
				writer.write("sqlsb.append(get" + Bigname + "pstr()); \n");
				writer.write("sqlsb.append(\"|\"); \n");
			}
		}

		writer.write("sqlsb.delete(sqlsb.length()- 1, sqlsb.length() );\n");

		writer.write("sqlsb.append(\"</v>\"); \n");

		// 添加where的语句
		writer.write("  return sqlsb.toString(); \n   ");

		writer.write("}\n");
		// end=====produceQuerySql=======

	}

	// 生成XML的STRING
	protected void produceXMLStr(FileWriter writer, TableVO tvo) throws IOException
	{

		List fieldlist = tvo.getFieldlist();

		// =====查询语句produceQuerySql=======
		// writer.write("public String  produceQuerySql(){\n");
		writer.write("public String  " + produceVOXMLStrname + "(){\n");
		writer.write("StringBuffer sqlsb = new StringBuffer();\n");

		writer.write("sqlsb.append(\"<k>v</k>\"); \n");
		writer.write("sqlsb.append(\"<v>\"); \n");

		// 添加属性的查询语句
		// 属性
		for (int j = 0; j < fieldlist.size(); j++)
		{
			FieldVO fvo = (FieldVO) fieldlist.get(j);
			String name = fvo.getName();
			String type = fvo.getType();

			String firstchar = name.substring(0, 1);
			String Bigname = firstchar.toUpperCase() + name.substring(1);

			if (type.equals("int"))
			{
				writer.write("sqlsb.append(get" + Bigname + "pstr()); \n");
				writer.write("sqlsb.append(\"|\"); \n");

			} else if (type.equals("long"))
			{
				writer.write("sqlsb.append(get" + Bigname + "pstr()); \n");
				writer.write("sqlsb.append(\"|\"); \n");

			} else if (type.equals("String"))
			{
				writer.write("sqlsb.append(get" + Bigname + "pstr()); \n");
				writer.write("sqlsb.append(\"|\"); \n");
			} else if (type.equals("double"))
			{
				writer.write("sqlsb.append(get" + Bigname + "pstr()); \n");
				writer.write("sqlsb.append(\"|\"); \n");

			} else if (type.equals("float"))
			{
				writer.write("sqlsb.append(get" + Bigname + "pstr()); \n");
				writer.write("sqlsb.append(\"|\"); \n");

			} else if (type.equals("Date"))
			{
				writer.write("sqlsb.append(get" + Bigname + "pstr()); \n");
				writer.write("sqlsb.append(\"|\"); \n");
			}
		}

		writer.write("sqlsb.delete(sqlsb.length()- 1, sqlsb.length() );\n");

		writer.write("sqlsb.append(\"</v>\"); \n");

		// 添加where的语句
		writer.write("  return sqlsb.toString(); \n   ");

		writer.write("}\n");
		// end=====produceQuerySql=======

	}

	// 生成XML的STRING---长标签
	protected void produceXMLStrlong(FileWriter writer, TableVO tvo) throws IOException
	{

		List fieldlist = tvo.getFieldlist();

		// =====查询语句produceQuerySql=======
		// writer.write("public String  produceQuerySql(){\n");
		writer.write("public String  " + produceVOXMLStrname + "(){\n");
		writer.write("StringBuffer sqlsb = new StringBuffer();\n");

		// 添加属性的查询语句
		// 属性
		for (int j = 0; j < fieldlist.size(); j++)
		{
			FieldVO fvo = (FieldVO) fieldlist.get(j);
			String name = fvo.getName();
			String type = fvo.getType();

			String firstchar = name.substring(0, 1);
			String Bigname = firstchar.toUpperCase() + name.substring(1);

			// prestr
			// String prestr = "public String get" + Bigname +
			// "pstr() \n { \n  ";

			// " <key>userid</key>   <integer>" + this.getUseridpstr() +
			// "</integer> "

			if (type.equals("int"))
			{
				writer.write("sqlsb.append(\"<key>" + name + "</key>\"); \n");
				writer.write("sqlsb.append(\"<integer>\"); \n");
				writer.write("sqlsb.append(get" + Bigname + "pstr()); \n");
				writer.write("sqlsb.append(\"</integer>\"); \n");

			} else if (type.equals("long"))
			{
				writer.write("sqlsb.append(\"<key>" + name + "</key>\"); \n");
				writer.write("sqlsb.append(\"<integer>\"); \n");
				writer.write("sqlsb.append(get" + Bigname + "pstr()); \n");
				writer.write("sqlsb.append(\"</integer>\"); \n");
			} else if (type.equals("String"))
			{
				writer.write("sqlsb.append(\"<key>" + name + "</key>\"); \n");
				writer.write("sqlsb.append(\"<string>\"); \n");
				writer.write("sqlsb.append(get" + Bigname + "pstr()); \n");
				writer.write("sqlsb.append(\"</string>\"); \n");
			} else if (type.equals("double"))
			{
				writer.write("sqlsb.append(\"<key>" + name + "</key>\"); \n");
				writer.write("sqlsb.append(\"<real>\"); \n");
				writer.write("sqlsb.append(get" + Bigname + "pstr()); \n");
				writer.write("sqlsb.append(\"</real>\"); \n");

			} else if (type.equals("float"))
			{
				writer.write("sqlsb.append(\"<key>" + name + "</key>\"); \n");
				writer.write("sqlsb.append(\"<real>\"); \n");
				writer.write("sqlsb.append(get" + Bigname + "pstr()); \n");
				writer.write("sqlsb.append(\"</real>\"); \n");

			} else if (type.equals("Date"))
			{
				writer.write("sqlsb.append(\"<key>" + name + "</key>\"); \n");
				writer.write("sqlsb.append(\"<string>\"); \n");
				writer.write("sqlsb.append(get" + Bigname + "pstr()); \n");
				writer.write("sqlsb.append(\"</string>\"); \n");
			}
		}

		// 添加where的语句
		writer.write("  return sqlsb.toString(); \n   ");

		writer.write("}\n");
		// end=====produceQuerySql=======

	}

	// 查询语句
	protected void produceVOQueryAppro(FileWriter writer, TableVO tvo) throws IOException
	{

		List fieldlist = tvo.getFieldlist();

		// =====查询语句produceQuerySql=======
		// writer.write("public String  produceQuerySql(){\n");
		writer.write("public String  " + produceVOQuerySQLApproname + "(){\n");
		writer.write("StringBuffer sqlsb = new StringBuffer();\n");

		// 添加属性的查询语句
		// 属性
		for (int j = 0; j < fieldlist.size(); j++)
		{
			FieldVO fvo = (FieldVO) fieldlist.get(j);
			String name = fvo.getName();
			String type = fvo.getType();

			if (type.equals("int"))
			{
				writer.write("if (this." + name + " > Integer.MIN_VALUE){ \n");
				writer.write(" sqlsb.append(\" and " + name + " = \"); \n");
				writer.write(" sqlsb.append(this." + name + "); } \n");

			} else if (type.equals("long"))
			{
				writer.write("if (this." + name + " > Long.MIN_VALUE){ \n");
				writer.write(" sqlsb.append(\" and " + name + " = \"); \n");
				writer.write(" sqlsb.append(this." + name + "); } \n");

			} else if (type.equals("String"))
			{
				// like "%ww%"
				writer.write("if (this." + name + " !=null){ \n");
				writer.write(" sqlsb.append(\" and " + name + " like \'%\"); \n");
				writer.write(" sqlsb.append(this." + name + ");  \n");
				writer.write(" sqlsb.append(\"%\'\"); } \n");

			} else if (type.equals("double"))
			{
				writer.write("if (this." + name + " > -1*Double.MAX_VALUE){ \n");
				writer.write(" sqlsb.append(\" and " + name + " = \"); \n");
				writer.write(" sqlsb.append(this." + name + "); } \n");

			} else if (type.equals("float"))
			{
				writer.write("if (this." + name + " > -1*Float.MAX_VALUE){ \n");
				writer.write(" sqlsb.append(\" and " + name + " = \"); \n");
				writer.write(" sqlsb.append(this." + name + "); } \n");

			} else if (type.equals("Date"))
			{

				// 大于
				writer.write("if (this." + name + "beg !=null){ \n");
				writer.write(" sqlsb.append(\" and " + name + " >= \'\"); \n");
				writer.write(" sqlsb.append(DF.format(this." + name + "beg));  \n");
				writer.write(" sqlsb.append(\"\'\"); } \n");

				// 小于
				writer.write("if (this." + name + "end !=null){ \n");
				writer.write(" sqlsb.append(\" and " + name + " <= \'\"); \n");
				writer.write(" sqlsb.append(DF.format(this." + name + "end));  \n");
				writer.write(" sqlsb.append(\"\'\"); } \n");

			}

		}

		// 添加where的语句
		writer.write("if (sqlsb.length() > 0){  \n" + " sqlsb.delete(0, 5);  \n "
				+ " sqlsb.insert(0, \" where \"); \n" + " return sqlsb.toString(); \n"
				+ " } else { \n" + " return \"\";}");

		writer.write("}\n");
		// end=====produceQuerySql=======

	}

	// 根据主键生成查询语句
	protected void produceVOPKQuerySQL(FileWriter writer, TableVO tvo) throws IOException
	{

		List fieldlist = tvo.getFieldlist();

		// =====查询语句produceQuerySql=======
		// writer.write("public String  produceQuerySql(){\n");
		writer.write("public String  " + produceVOPKQuerySQLname + "(){\n");
		writer.write("StringBuffer sqlsb = new StringBuffer();\n");

		// 添加属性的查询语句
		// 属性
		for (int j = 0; j < fieldlist.size(); j++)
		{
			FieldVO fvo = (FieldVO) fieldlist.get(j);

			// 只处理主键属性
			if (fvo.getPrimarykey() == 1)
			{
				String name = fvo.getName();
				String type = fvo.getType();

				if (type.equals("int"))
				{
					writer.write("if (this." + name + " > Integer.MIN_VALUE){ \n");
					writer.write(" sqlsb.append(\" and " + name + " = \"); \n");
					writer.write(" sqlsb.append(this." + name + "); } \n");

				} else if (type.equals("long"))
				{
					writer.write("if (this." + name + " > Long.MIN_VALUE){ \n");
					writer.write(" sqlsb.append(\" and " + name + " = \"); \n");
					writer.write(" sqlsb.append(this." + name + "); } \n");

				} else if (type.equals("String"))
				{
					writer.write("if (this." + name + " !=null){ \n");
					writer.write(" sqlsb.append(\" and " + name + " = \'\"); \n");
					writer.write(" sqlsb.append(this." + name + ");  \n");
					writer.write(" sqlsb.append(\"\'\"); } \n");

				} else if (type.equals("double"))
				{
					writer.write("if (this." + name + " > -1*Double.MAX_VALUE){ \n");
					writer.write(" sqlsb.append(\" and " + name + " = \"); \n");
					writer.write(" sqlsb.append(this." + name + "); } \n");

				} else if (type.equals("float"))
				{
					writer.write("if (this." + name + " > -1*Float.MAX_VALUE){ \n");
					writer.write(" sqlsb.append(\" and " + name + " = \"); \n");
					writer.write(" sqlsb.append(this." + name + "); } \n");

				} else if (type.equals("Date"))
				{
					writer.write("if (this." + name + " !=null){ \n");
					writer.write(" sqlsb.append(\" and " + name + " = \'\"); \n");
					writer.write(" sqlsb.append(DF.format(this." + name + "));  \n");
					writer.write(" sqlsb.append(\"\'\"); } \n");
				}
			}

		}

		// 添加where的语句
		writer.write("if (sqlsb.length() > 0){  \n" + " sqlsb.delete(0, 5);  \n "
				+ " sqlsb.insert(0, \" where \"); \n" + " return sqlsb.toString(); \n"
				+ " } else { \n" + " return \"\";}");

		writer.write("}\n");
		// end=====produceQuerySql=======

	}

	// 修改值
	protected void produceVOChangeUpdateSQL(FileWriter writer, TableVO tvo) throws IOException
	{

		List<FieldVO> fieldlist = tvo.getFieldlist();

		// =====更新语句produceUpdateSql=======
		writer.write("public String " + produceVOChangeUpdateSQLname + "(){ \n");
		writer.write("StringBuffer sqlsb = new StringBuffer(); \n");

		// 添加属性的更新语句=====================
		// 属性
		for (int j = 0; j < fieldlist.size(); j++)
		{
			FieldVO fvo = fieldlist.get(j);
			String name = fvo.getName();
			String type = fvo.getType();

			System.out.println("+++++++++++++fvo:" + fvo.getName() + "   " + fvo.getIsvalue());
			if (fvo.getIsvalue() > 0)
			{

				if (type.equals("int"))
				{
					writer.write("if (this." + name + " > Integer.MIN_VALUE){ \n");
					writer.write(" sqlsb.append(\" , " + name + " =  " + name + "+ (\" ); \n");
					writer.write(" sqlsb.append(this." + name + "+\")\" ); } \n");

				} else if (type.equals("long"))
				{
					writer.write("if (this." + name + " > Long.MIN_VALUE){ \n");
					writer.write(" sqlsb.append(\" , " + name + " =  " + name + "+ (\" ); \n");
					writer.write(" sqlsb.append(this." + name + "+\")\" ); } \n");

				} else if (type.equals("double"))
				{
					writer.write("if (this." + name + " > -1*Double.MAX_VALUE){ \n");
					writer.write(" sqlsb.append(\" , " + name + " =  " + name + "+ (\" ); \n");
					writer.write(" sqlsb.append(this." + name + "+\")\" ); } \n");

				} else if (type.equals("float"))
				{
					writer.write("if (this." + name + " > -1*Float.MAX_VALUE){ \n");
					writer.write(" sqlsb.append(\" , " + name + " =  " + name + "+ (\" ); \n");
					writer.write(" sqlsb.append(this." + name + "+\")\" ); } \n");

				}
			}

			// else if (type.equals("Date"))
			// {
			// writer.write("if (this." + name + " !=null){ \n");
			// writer.write(" sqlsb.append(\" , " + name + " = \'\"); \n");
			// writer.write(" sqlsb.append(DF.format(this." + name + "));  \n");
			// writer.write(" sqlsb.append(\"\'\"); } \n");
			// }
		}

		// 添加where的语句===== ================
		writer.write("if (sqlsb.length() > 0){  \n" + " sqlsb.delete(0, 2);  \n "
				+ " sqlsb.insert(0, \" set \"); \n" + " return sqlsb.toString(); \n"
				+ " } else { \n" + " return \"\";}");

		writer.write("}\n");

		// end=====更新语句produceUpdateSql=======
	}

	protected void produceVOUpdateSQL(FileWriter writer, TableVO tvo) throws IOException
	{

		// TableVO tvo = (TableVO) tablelist.get(i);
		List fieldlist = tvo.getFieldlist();
		// String tablename = tvo.getName();
		// =====更新语句produceUpdateSql=======
		// writer.write("public String produceUpdateSQL(){ \n");
		writer.write("public String " + produceVOUpdateSQLname + "(){ \n");
		writer.write("StringBuffer sqlsb = new StringBuffer(); \n");
		// writer.write(
		// "SimpleDateFormat DF = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\"); \n"
		// );

		// 添加属性的更新语句===== ================
		// 属性
		for (int j = 0; j < fieldlist.size(); j++)
		{
			FieldVO fvo = (FieldVO) fieldlist.get(j);
			String name = fvo.getName();
			String type = fvo.getType();

			if (type.equals("int"))
			{
				writer.write("if (this." + name + " > Integer.MIN_VALUE){ \n");
				writer.write(" sqlsb.append(\" , " + name + " = \"); \n");
				writer.write(" sqlsb.append(this." + name + "); } \n");

			} else if (type.equals("long"))
			{
				writer.write("if (this." + name + " > Long.MIN_VALUE){ \n");
				writer.write(" sqlsb.append(\" , " + name + " = \"); \n");
				writer.write(" sqlsb.append(this." + name + "); } \n");

			} else if (type.equals("String"))
			{
				writer.write("if (this." + name + " !=null){ \n");
				writer.write(" sqlsb.append(\" , " + name + " = \'\"); \n");
				writer.write(" sqlsb.append(this." + name + ");  \n");
				writer.write(" sqlsb.append(\"\'\"); } \n");

			} else if (type.equals("double"))
			{
				writer.write("if (this." + name + " > -1*Double.MAX_VALUE){ \n");
				writer.write(" sqlsb.append(\" , " + name + " = \"); \n");
				writer.write(" sqlsb.append(this." + name + "); } \n");

			} else if (type.equals("float"))
			{
				writer.write("if (this." + name + " > -1*Float.MAX_VALUE){ \n");
				writer.write(" sqlsb.append(\" , " + name + " = \"); \n");
				writer.write(" sqlsb.append(this." + name + "); } \n");

			} else if (type.equals("Date"))
			{
				writer.write("if (this." + name + " !=null){ \n");
				writer.write(" sqlsb.append(\" , " + name + " = \'\"); \n");
				writer.write(" sqlsb.append(DF.format(this." + name + "));  \n");
				writer.write(" sqlsb.append(\"\'\"); } \n");
			}

		}
		// updateSQL.append(" , buytime =  '");
		// updateSQL.append(DF.format(buytime));

		// 添加where的语句===== ================
		writer.write("if (sqlsb.length() > 0){  \n" + " sqlsb.delete(0, 2);  \n "
				+ " sqlsb.insert(0, \" set \"); \n" + " return sqlsb.toString(); \n"
				+ " } else { \n" + " return \"\";}");

		writer.write("}\n");

		// end=====更新语句produceUpdateSql=======
	}

	protected void produceVOInsertSQL(FileWriter writer, TableVO tvo) throws IOException
	{

		// TableVO tvo = (TableVO) tablelist.get(i);
		List fieldlist = tvo.getFieldlist();
		String tablename = tvo.getName();

		// begin=====更新语句produceInsertSql=======
		writer.write("public String " + produceVOInsertSQLname + "(){ \n");
		writer.write("StringBuffer insertsqlsb = new StringBuffer(); \n");
		writer.write("StringBuffer fieldsqlsb = new StringBuffer(); \n");
		writer.write("StringBuffer valuesqlsb = new StringBuffer(); \n");

		// 添加属性的查询语句===== ================
		// 属性名
		for (int j = 0; j < fieldlist.size(); j++)
		{
			FieldVO fvo = (FieldVO) fieldlist.get(j);
			String name = fvo.getName();
			String type = fvo.getType();

			if (type.equals("int"))
			{
				writer.write("if (this." + name + " > Integer.MIN_VALUE){ \n");
				writer.write(" fieldsqlsb.append(\" , " + name + " \"); \n  }\n");
			} else if (type.equals("long"))
			{
				writer.write("if (this." + name + " > Long.MIN_VALUE){ \n");
				writer.write(" fieldsqlsb.append(\" , " + name + " \"); \n  }\n");
			} else if (type.equals("String"))
			{
				writer.write("if (this." + name + " !=null){ \n");
				writer.write(" fieldsqlsb.append(\" , " + name + " \"); \n  }\n");
			} else if (type.equals("double"))
			{
				writer.write("if (this." + name + " > -1*Double.MAX_VALUE){ \n");
				writer.write(" fieldsqlsb.append(\" , " + name + " \"); \n  }\n");

			} else if (type.equals("float"))
			{
				writer.write("if (this." + name + " > -1*Float.MAX_VALUE){ \n");
				writer.write(" fieldsqlsb.append(\" , " + name + " \"); \n  }\n");

			} else if (type.equals("Date"))
			{
				writer.write("if (this." + name + " !=null){ \n");
				writer.write(" fieldsqlsb.append(\" , " + name + " \"); \n  }\n");
			}
		}

		writer.write("if (fieldsqlsb.length() > 0){  \n" + " fieldsqlsb.delete(0, 2);  \n  } \n");

		// 属性值
		for (int j = 0; j < fieldlist.size(); j++)
		{
			FieldVO fvo = (FieldVO) fieldlist.get(j);
			String name = fvo.getName();
			String type = fvo.getType();

			if (type.equals("int"))
			{
				writer.write("if (this." + name + " > Integer.MIN_VALUE){ \n");
				writer.write(" valuesqlsb.append(\" , \"+ this." + name + "); \n  }\n");
			} else if (type.equals("long"))
			{
				writer.write("if (this." + name + " > Long.MIN_VALUE){ \n");
				writer.write(" valuesqlsb.append(\" , \"+ this." + name + "); \n  }\n");
			} else if (type.equals("String"))
			{
				writer.write("if (this." + name + " !=null){ \n");
				writer.write(" valuesqlsb.append(\" , \'\"+ this." + name + "+\"\'\"); \n  }\n");
			} else if (type.equals("double"))
			{
				writer.write("if (this." + name + " > -1*Double.MAX_VALUE){ \n");
				writer.write(" valuesqlsb.append(\" , \"+ this." + name + "); \n  }\n");

			} else if (type.equals("float"))
			{
				writer.write("if (this." + name + " > -1*Float.MAX_VALUE){ \n");
				writer.write(" valuesqlsb.append(\" , \"+ this." + name + "); \n  }\n");

			} else if (type.equals("Date"))
			{
				writer.write("if (this." + name + " !=null){ \n");
				writer.write(" valuesqlsb.append(\" , \'\"+ DF.format(this." + name
						+ ")+\"\'\"); \n  }\n");
			}

		}

		writer.write("if (valuesqlsb.length() > 0){  \n" + " valuesqlsb.delete(0, 2);  \n  } \n");

		// 组装insertsqlsb
		writer.write(" insertsqlsb.append(\" insert into " + tablename + " ( \"); \n");
		writer.write("if (fieldsqlsb.length() > 0){  \n"
				+ " insertsqlsb.append(fieldsqlsb.toString());  \n  } \n");
		writer.write(" insertsqlsb.append(\" )values ( \"); \n");
		writer.write("if (valuesqlsb.length() > 0){  \n"
				+ " insertsqlsb.append(valuesqlsb.toString());  \n  } \n");
		writer.write(" insertsqlsb.append(\" ) \"); \n");

		writer.write(" return insertsqlsb.toString(); \n");

		writer.write("}\n");
		// end============
	}

	// =====获取ResultSet的方法=======================
	protected void produceVOGetResultSet(FileWriter writer, TableVO tvo) throws IOException
	{

		List fieldlist = tvo.getFieldlist();
		// String tablename = tvo.getName();

		// writer.write(
		// "public void getResultSet(ResultSet rs) throws Exception \n { \n");
		writer.write("public void " + produceVOGetResultSetname
				+ "(ResultSet rs) throws Exception \n { \n");

		for (int j = 0; j < fieldlist.size(); j++)
		{
			FieldVO fvo = (FieldVO) fieldlist.get(j);
			String name = fvo.getName();
			String type = fvo.getType();

			writer.write("if (rs.getString(\"" + name + "\") != null )\n {\n");

			if (type.equals("int"))
			{
				writer.write(name + " =  rs.getInt(\"" + name + "\"); \n");
			} else if (type.equals("long"))
			{
				writer.write(name + " =  rs.getLong(\"" + name + "\"); \n");

			} else if (type.equals("String"))
			{
				writer.write(name + " =  rs.getString(\"" + name + "\"); \n");
			} else if (type.equals("double"))
			{
				writer.write(name + " =  rs.getDouble(\"" + name + "\"); \n");

			} else if (type.equals("float"))
			{
				writer.write(name + " =  rs.getFloat(\"" + name + "\"); \n");

			} else if (type.equals("Date"))
			{
				// new Date(rs.getTimestamp("commtime").getTime())
				// writer.write(name + " =  rs.getDate(\"" + name + "\"); \n");
				writer.write(name + " =  new Date(rs.getTimestamp(\"" + name + "\").getTime()); \n");
			}
			writer.write("} \n");
		}

		writer.write("} \n");
	}

	// =====获取Parameter的方法=======================
	protected void produceVOGetParameter(FileWriter writer, TableVO tvo) throws IOException
	{

		List fieldlist = tvo.getFieldlist();

		writer.write("public void " + produceVOGetParametername
				+ "(HttpServletRequest request) throws Exception \n { \n");

		// 设置为不可以删掉一个数字
		for (int j = 0; j < fieldlist.size(); j++)
		{
			FieldVO fvo = (FieldVO) fieldlist.get(j);
			String name = fvo.getName();
			String type = fvo.getType();

			if (type.equals("int"))
			{
				writer.write("if (request.getParameter(\"" + name + "\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"" + name + "\").equals(\"\")))\n {\n");

				writer.write(name + " =  Integer.parseInt(request.getParameter(\"" + name
						+ "\")); \n");

				writer.write("} \n");
				writer.write("} \n");

			} else if (type.equals("long"))
			{
				writer.write("if (request.getParameter(\"" + name + "\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"" + name + "\").equals(\"\")))\n {\n");

				writer.write(name + " =  Long.parseLong(request.getParameter(\"" + name
						+ "\")); \n");

				writer.write("} \n");
				writer.write("} \n");

			} else if (type.equals("String"))
			{
				writer.write("if (request.getParameter(\"" + name + "\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"" + name + "\").equals(\"\")))\n {\n");
				writer.write(name + " =   request.getParameter(\"" + name + "\" ); \n");
				writer.write("} \n");
				writer.write("} \n");

			} else if (type.equals("double"))
			{
				writer.write("if (request.getParameter(\"" + name + "\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"" + name + "\").equals(\"\")))\n {\n");

				writer.write(name + " =  Double.parseDouble(request.getParameter(\"" + name
						+ "\")); \n");

				writer.write("} \n");
				writer.write("} \n");
			} else if (type.equals("float"))
			{
				writer.write("if (request.getParameter(\"" + name + "\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"" + name + "\").equals(\"\")))\n {\n");

				writer.write(name + " =  Float.parseFloat(request.getParameter(\"" + name
						+ "\")); \n");

				writer.write("} \n");
				writer.write("} \n");
			}

			// 该语句有问题--方法过期
			else if (type.equals("Date"))
			{
				// date
				writer.write("if (request.getParameter(\"" + name + "\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"" + name + "\").equals(\"\")))\n {\n");
				writer.write(name + " =  new Date(request.getParameter(\"" + name + "\")); \n");
				writer.write("} \n");
				writer.write("} \n");

				// datebeg
				writer.write("if (request.getParameter(\"" + name + "beg\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"" + name + "beg\").equals(\"\")))\n {\n");
				writer.write(name + "beg =  new Date(request.getParameter(\"" + name
						+ "beg\")); \n");
				writer.write("} \n");
				writer.write("} \n");

				// dateend
				writer.write("if (request.getParameter(\"" + name + "end\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"" + name + "end\").equals(\"\")))\n {\n");
				writer.write(name + "end =  new Date(request.getParameter(\"" + name
						+ "end\")); \n");
				writer.write("} \n");
				writer.write("} \n");

				// 表明存在date类型数据

				writer.write("if (request.getParameter(\"year\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"year\").equals(\"\")))\n {\n");
				writer.write("year = Integer.parseInt(request.getParameter(\"year\")); \n");
				writer.write("} \n");
				writer.write("} \n");

				writer.write("if (request.getParameter(\"month\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"month\").equals(\"\")))\n {\n");
				writer.write("month = Integer.parseInt(request.getParameter(\"month\")); \n");
				writer.write("} \n");
				writer.write("} \n");

				writer.write("if (request.getParameter(\"day\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"day\").equals(\"\")))\n {\n");
				writer.write("day = Integer.parseInt(request.getParameter(\"day\")); \n");
				writer.write("} \n");
				writer.write("} \n");

				writer.write("if (request.getParameter(\"hour\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"hour\").equals(\"\")))\n {\n");
				writer.write("hour = Integer.parseInt(request.getParameter(\"hour\")); \n");
				writer.write("} \n");
				writer.write("} \n");

				writer.write("if (request.getParameter(\"minute\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"minute\").equals(\"\")))\n {\n");
				writer.write("minute = Integer.parseInt(request.getParameter(\"minute\")); \n");
				writer.write("} \n");
				writer.write("} \n");

				writer.write("if (request.getParameter(\"second\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"second\").equals(\"\")))\n {\n");
				writer.write("second = Integer.parseInt(request.getParameter(\"second\")); \n");
				writer.write("} \n");
				writer.write("} \n");

				writer.write("if (year!=Integer.MIN_VALUE &&month!=Integer.MIN_VALUE  &&day!=Integer.MIN_VALUE)\n {\n");
				writer.write("Date date = new Date() ; \n");
				writer.write("date.setYear(year - 1900); \n");
				writer.write("date.setMonth(month - 1);\n");
				writer.write("date.setDate(day);\n");
				writer.write("if(hour!=Integer.MIN_VALUE && minute!=Integer.MIN_VALUE && second!=Integer.MIN_VALUE){\n");
				writer.write("date.setHours(hour);\n");
				writer.write("date.setMinutes(minute);\n");
				writer.write("date.setSeconds(second);\n");
				writer.write("}else{\n");
				writer.write("date.setHours(0);\n");
				writer.write("date.setMinutes(0);\n");
				writer.write("date.setSeconds(0);\n");
				writer.write("};\n");
				writer.write("" + name + " = date;\n");
				writer.write("} \n");

				// 表明存在date类型数据

				writer.write("if (request.getParameter(\"year1\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"year1\").equals(\"\")))\n {\n");
				writer.write("year1 = Integer.parseInt(request.getParameter(\"year1\")); \n");
				writer.write("} \n");
				writer.write("} \n");

				writer.write("if (request.getParameter(\"month1\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"month1\").equals(\"\")))\n {\n");
				writer.write("month1 = Integer.parseInt(request.getParameter(\"month1\")); \n");
				writer.write("} \n");
				writer.write("} \n");

				writer.write("if (request.getParameter(\"day1\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"day1\").equals(\"\")))\n {\n");
				writer.write("day1 = Integer.parseInt(request.getParameter(\"day1\")); \n");
				writer.write("} \n");
				writer.write("} \n");

				writer.write("if (request.getParameter(\"hour1\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"hour1\").equals(\"\")))\n {\n");
				writer.write("hour1 = Integer.parseInt(request.getParameter(\"hour1\")); \n");
				writer.write("} \n");
				writer.write("} \n");

				writer.write("if (request.getParameter(\"minute1\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"minute1\").equals(\"\")))\n {\n");
				writer.write("minute1 = Integer.parseInt(request.getParameter(\"minute1\")); \n");
				writer.write("} \n");
				writer.write("} \n");

				writer.write("if (request.getParameter(\"second1\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"second1\").equals(\"\")))\n {\n");
				writer.write("second1 = Integer.parseInt(request.getParameter(\"second1\")); \n");
				writer.write("} \n");
				writer.write("} \n");

				writer.write("if (year1!=Integer.MIN_VALUE &&month1!=Integer.MIN_VALUE  &&day1!=Integer.MIN_VALUE)\n {\n");
				writer.write("Date date = new Date() ; \n");
				writer.write("date.setYear(year1 - 1900); \n");
				writer.write("date.setMonth(month1 - 1);\n");
				writer.write("date.setDate(day1);\n");
				writer.write("if(hour1==Integer.MIN_VALUE || minute1==Integer.MIN_VALUE || second1==Integer.MIN_VALUE){\n");
				writer.write("date.setHours(0);\n");
				writer.write("date.setMinutes(0);\n");
				writer.write("date.setSeconds(0);\n");
				writer.write("};\n");
				writer.write("" + name + "end = date;\n");
				writer.write("} \n");

			}

		}

		writer.write("} \n");
	}

	// =====获取updateParameter的方法=======================
	protected void produceVOGetUpdateParameter(FileWriter writer, TableVO tvo) throws IOException
	{

		List fieldlist = tvo.getFieldlist();

		writer.write("public void " + produceVOGetUpdateParametername
				+ "(HttpServletRequest request) throws Exception \n { \n");

		// 设置为不可以删掉一个数字
		for (int j = 0; j < fieldlist.size(); j++)
		{
			FieldVO fvo = (FieldVO) fieldlist.get(j);
			String name = fvo.getName();
			String type = fvo.getType();

			// writer.write("if (request.getParameter(\"" + name +
			// "\") != null  && !(request.getParameter(\"" + name +
			// "\").equals(\"\")))\n {\n");
			// writer.write("if (request.getParameter(\"" + name +
			// "\") != null )\n {\n");

			if (type.equals("int"))
			{
				writer.write("if (request.getParameter(\"" + name + "\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"" + name + "\").equals(\"\")))\n {\n");

				writer.write(name + " =  Integer.parseInt(request.getParameter(\"" + name
						+ "\")); \n");

				// writer.write("}else{ \n");
				// writer.write(name + " =  Integer.MIN_VALUE; \n");
				// writer.write(name + " =  0; \n");
				writer.write("} \n");
				writer.write("} \n");
			} else if (type.equals("long"))
			{
				writer.write("if (request.getParameter(\"" + name + "\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"" + name + "\").equals(\"\")))\n {\n");

				writer.write(name + " =  Long.parseLong(request.getParameter(\"" + name
						+ "\")); \n");

				// writer.write("}else{ \n");
				// writer.write(name + " =  Integer.MIN_VALUE; \n");
				// writer.write(name + " =  0; \n");
				writer.write("} \n");
				writer.write("} \n");
			} else if (type.equals("String"))
			{
				writer.write("if (request.getParameter(\"" + name + "\") != null )\n {\n");
				writer.write(name + " =   request.getParameter(\"" + name + "\" ); \n");
				writer.write("} \n");
			} else if (type.equals("double"))
			{
				writer.write("if (request.getParameter(\"" + name + "\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"" + name + "\").equals(\"\")))\n {\n");

				writer.write(name + " =  Double.parseDouble(request.getParameter(\"" + name
						+ "\")); \n");

				// writer.write("}else{ \n");
				// writer.write(name + " =   -1*Double.MAX_VALUE; \n");
				// writer.write(name + " =   0; \n");
				writer.write("} \n");
				writer.write("} \n");
			} else if (type.equals("float"))
			{
				writer.write("if (request.getParameter(\"" + name + "\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"" + name + "\").equals(\"\")))\n {\n");

				writer.write(name + " =  Float.parseFloat(request.getParameter(\"" + name
						+ "\")); \n");

				// writer.write("}else{ \n");
				// writer.write(name + " =   -1*Float.MAX_VALUE; \n");
				// writer.write(name + " =  0; \n");
				writer.write("} \n");
				writer.write("} \n");
			}

			// 该语句有问题--方法过期
			else if (type.equals("Date"))
			{
				writer.write("if (request.getParameter(\"" + name + "\") != null )\n {\n");
				writer.write("if (!(request.getParameter(\"" + name + "\").equals(\"\")))\n {\n");
				writer.write(name + " =  new Date(request.getParameter(\"" + name + "\")); \n");
				writer.write("} \n");
				writer.write("} \n");
			}
			// writer.write("} \n");
		}

		writer.write("} \n");
	}

	protected void produceVOGetParameterList(FileWriter writer, TableVO tvo) throws IOException
	{

		List fieldlist = tvo.getFieldlist();

		writer.write("public void " + produceVOGetParameterListname
				+ "(HttpServletRequest request, int id) throws Exception \n { \n");
		writer.write("String idstr= String.valueOf(id);\n");
		for (int j = 0; j < fieldlist.size(); j++)
		{
			FieldVO fvo = (FieldVO) fieldlist.get(j);
			String name = fvo.getName();
			String type = fvo.getType();

			if (type.equals("int"))
			{
				writer.write("if (request.getParameter(\"" + name
						+ "\"+idstr) != null  && !(request.getParameter(\"" + name
						+ "\"+idstr).equals(\"\")))\n {\n");
				writer.write(name + " =  Integer.parseInt(request.getParameter(\"" + name
						+ "\"+idstr)); \n");
				writer.write("} \n");
			} else if (type.equals("long"))
			{
				writer.write("if (request.getParameter(\"" + name
						+ "\"+idstr) != null  && !(request.getParameter(\"" + name
						+ "\"+idstr).equals(\"\")))\n {\n");
				writer.write(name + " =  Long.parseLong(request.getParameter(\"" + name
						+ "\"+idstr)); \n");
				writer.write("} \n");
			} else if (type.equals("String"))
			{
				writer.write(name + " =   request.getParameter(\"" + name + "\"+idstr ); \n");
			} else if (type.equals("double"))
			{
				writer.write("if (request.getParameter(\"" + name
						+ "\"+idstr) != null  && !(request.getParameter(\"" + name
						+ "\"+idstr).equals(\"\")))\n {\n");
				writer.write(name + " =  Double.parseDouble(request.getParameter(\"" + name
						+ "\"+idstr)); \n");
				writer.write("} \n");

			} else if (type.equals("float"))
			{
				writer.write("if (request.getParameter(\"" + name
						+ "\"+idstr) != null  && !(request.getParameter(\"" + name
						+ "\"+idstr).equals(\"\")))\n {\n");
				writer.write(name + " =  Float.parseFloat(request.getParameter(\"" + name
						+ "\"+idstr)); \n");
				writer.write("} \n");

				// 该语句有问题--方法过期
			} else if (type.equals("Date"))
			{
				writer.write("if (request.getParameter(\"" + name
						+ "\"+idstr) != null  && !(request.getParameter(\"" + name
						+ "\"+idstr).equals(\"\")))\n {\n");
				writer.write(name + " =  new Date(request.getParameter(\"" + name
						+ "\"+idstr)); \n");
				writer.write("} \n");
			}

		}

		writer.write("} \n");
	}

	// =====获取XML中数据的方法=======================
	// 从plist格式的xml的数组中读取数据到VO
	protected void produceGetVOXMLPListArray(FileWriter writer, TableVO tvo) throws IOException
	{
		writer.write("public List< " + voclassname + ">" + produceGetVOXMLPListArrayname
				+ "( Element array_elementlist) throws Exception \n { \n");

		writer.write(" List nodelist  = array_elementlist.elements(\"" + tvo.getName() + "\");\n");

		writer.write(" Element vo_element;\n");
		writer.write("List<" + voclassname + "> volist =   new ArrayList<" + voclassname + ">();\n");

		writer.write("for (int i = 0; i < nodelist.size(); i++)\n{\n");

		writer.write("vo_element = (Element) nodelist.get(i);\n");

		writer.write(voclassname + " vo =new " + voclassname + "();\n");
		writer.write("vo." + produceGetVOXMLPListname + "(vo_element);\n");
		writer.write("volist.add(vo);\n");
		writer.write("}\n");
		writer.write("return volist;\n");
		writer.write("}\n");

	}

	// 读取plist至VO
	// 从plist格式的xml中读取数据到VO
	// <v>82559|9006|1035|2|2014-04-18 11:12:25|0|0</v>
	protected void produceGetVOXMLPList(FileWriter writer, TableVO tvo) throws IOException
	{

		List<FieldVO> fieldlist = tvo.getFieldlist();

		writer.write("public void " + produceGetVOXMLPListname
				+ "( Element vo_element) throws Exception \n { \n");

		writer.write(" Element value_element =  ( Element) vo_element.element(\"v\");\n");

		writer.write(" if(value_element ==null)\n{   \n");
		writer.write("	  value_element =(Element) vo_element.element(\"value\");\n");
		writer.write("} \n");

		writer.write(" if(value_element ==null)\n{   \n");
		writer.write("	throw new CommonException(\"数据格式错误，无法解析(value)\");  \n");
		writer.write("} \n");

		writer.write("String textcontent = value_element.getText();\n");
		writer.write("  textcontent = textcontent+\"|0\";\n");

		writer.write("String[] attributearray = textcontent.split(\"\\\\|\");\n");

		// 长度判断
		// writer.write("if (attributearray.length != " + (fieldlist.size() + 1)
		// + ")\n {\n");
		// writer.write("  throw new CommonException(\"数据格式错误，无法解析\");\n");
		// writer.write("}\n");

		writer.write("try{\n");
		FieldVO fieldvo = null;
		for (int i = 0; i < fieldlist.size(); i++)
		{
			fieldvo = fieldlist.get(i);
			String name = fieldvo.getName();
			String type = fieldvo.getType();

			writer.write("if (attributearray.length > " + i + ") { \n");

			writer.write(" if(!attributearray[" + i + "].equals(\"\")){  \n");

			// ==================getAttribute=====================
			if (type.equals("int"))
			{
				writer.write(name + " =  Integer.parseInt( attributearray[" + i + "]); \n");

			} else if (type.equals("long"))
			{
				writer.write(name + " =  Long.parseLong(attributearray[" + i + "]); \n");

			} else if (type.equals("String"))
			{
				writer.write(name + " =  attributearray[" + i + "]; \n");

			} else if (type.equals("double"))
			{

				writer.write(name + " =  Double.parseDouble(attributearray[" + i + "]); \n");

			} else if (type.equals("float"))
			{
				writer.write(name + " =  Float.parseFloat(attributearray[" + i + "]); \n");

			} else if (type.equals("Date"))
			{
				writer.write(name + " =  DF.parse(attributearray[" + i + "]); \n");

			}

			writer.write("  } \n");
			writer.write(" } \n");
		}

		writer.write("} catch (Exception e)\n");
		writer.write("{\n  e.printStackTrace(); \n throw new CommonException(\"数据格式错误，无法解析\");\n");
		writer.write("}\n");

		writer.write("}\n");

	}

	// 生成用于post发送的map
	protected void produceVOPostMap(FileWriter writer, TableVO tvo) throws IOException
	{
		List<FieldVO> fieldlist = tvo.getFieldlist();

		// =====查询语句produceQuerySql=======
		writer.write("public HashMap  " + produceVOPOSTMAPname + "(){\n");
		writer.write("HashMap<String,String> paramsmap= new HashMap<String,String>();\n");

		// 添加属性的查询语句
		// 属性
		for (int j = 0; j < fieldlist.size(); j++)
		{
			FieldVO fvo = fieldlist.get(j);
			String name = fvo.getName();
			String type = fvo.getType();

			if (type.equals("int"))
			{
				writer.write("if (this." + name + " > Integer.MIN_VALUE){ \n");
				writer.write(" paramsmap.put(\"" + name + "\",String.valueOf(this." + name
						+ "));  \n");
				writer.write("  } \n");

			} else if (type.equals("long"))
			{
				writer.write("if (this." + name + " > Long.MIN_VALUE){ \n");
				writer.write(" paramsmap.put(\"" + name + "\",String.valueOf(this." + name
						+ "));  \n");
				writer.write("   } \n");

			} else if (type.equals("String"))
			{
				writer.write("if (this." + name + " !=null){ \n");
				writer.write(" paramsmap.put(\"" + name + "\",this." + name + ");  \n");
				writer.write("  } \n");

			} else if (type.equals("double"))
			{
				writer.write("if (this." + name + " > -1*Double.MAX_VALUE){ \n");
				writer.write(" paramsmap.put(\"" + name + "\",String.valueOf(this." + name
						+ "));  \n");
				writer.write("  } \n");

			} else if (type.equals("float"))
			{
				writer.write("if (this." + name + " > -1*Float.MAX_VALUE){ \n");
				writer.write(" paramsmap.put(\"" + name + "\",String.valueOf(this." + name
						+ "));  \n");
				writer.write("  } \n");

			} else if (type.equals("Date"))
			{
				writer.write("if (this." + name + " !=null){ \n");
				writer.write(" paramsmap.put(\"" + name + "\",DF.format(this." + name + "));  \n");
				writer.write("   } \n");
			}
		}

		writer.write("return paramsmap;\n");

		writer.write("}\n");
		// end=====produceQuerySql=======

	}

	/**
	 * @param args
	 * @throws CommonException
	 */
	public static void main(String[] args) throws CommonException
	{

		try
		{
			VOBuilder vb = new VOBuilder();
			vb.buildWebMod();
			System.out.println("VO builder exe complete...");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
