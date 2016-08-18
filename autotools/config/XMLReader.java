package com.universeview.utils.autotools.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.universeview.utils.CommonException;
import com.universeview.utils.ParsePropertiesFile;

public class XMLReader
{
	public static String configfolderpath = "src\\com\\universeview\\utils\\autotools\\";
	public static String configfilepath = configfolderpath + "persistenceconfig.xml";
	public static List tablelist = null;
	public static String PROJECT_folder = "";//ParsePropertiesFile.getSpecifiedFolderPath();
	
	// ========VO method=====================
	public static String produceVOQuerySQLname = "produceQuerySql";
	public static String produceVOPKQuerySQLname = "producePKQuerySQL";
	public static String produceVOUpdateSQLname = "produceUpdateSQL";
	public static String produceVOChangeUpdateSQLname = "produceChangeUpdateSQL";
	public static String produceVOInsertSQLname = "produceInsertSQL";
	public static String produceVOGetResultSetname = "getResultSet";
	public static String produceVOGetParametername = "getParameter";
	public static String produceVOGetXMLname = "getXML";
	public static String produceVOGetXMLListname = "getXMLList";
	
	public static String produceGetVOXMLPListname ="getXMLPList";
	public static String produceGetVOXMLPListArrayname = "getXMLPListArray";
	
	public static String produceVOPOSTMAPname = "getPostMap";
	
	public static String produceVOGetUpdateParametername = "getUpdateParameter";
	public static String produceVOGetParameterListname = "getParameterList";

	public static String produceVOXMLStrname = "getXMLStr";
	public static String produceVOXMLStrShortname = "getXMLStrShort";

	public static String vogetaipkname = "getAIPK";
	public static String produceVOQuerySQLApproname = "produceQuerySqlAppro";

	// ------------vo ----------------------------
	public static String vopackage = "com.universeview.bean.basic";
	public static String vofolderpath = "src\\com\\universeview\\bean\\basic\\";
	public static String voadvpackage = "com.universeview.bean";
	public static String voadvfolderpath = "src\\com\\universeview\\bean\\";

	// -------------dao-----------------------------
	public static String daopackage = "com.universeview.dao.basic";
	public static String daofolderpath = "src\\com\\universeview\\dao\\basic\\";
	public static String daoadvpackage = "com.universeview.dao";
	public static String daoadvfolderpath = "src\\com\\universeview\\dao\\";
	
	// -------------WEBJSP---------------------------
	public static String WEBJSP_folder_default = PROJECT_folder + "WebRoot\\webpage\\";
	public static String WEBJSP_folder_webpage = PROJECT_folder + "WebRoot\\webpage\\";
 
	// -------------DBSQL---------------------------
	public static String DBSQL_folder = PROJECT_folder + "db\\";

	// -------------ACTION---------------------------
	public static String action_package = "com.universeview.controller";
	public static String action_folderpath = "src\\com\\universeview\\controller";

	// -------------SERVER---------------------------
	public static String server_basic_package = "com.universeview.service.basic";
	public static String server_basic_folderpath = "src\\com\\universeview\\service\\basic\\";

	public static String server_package = "com.universeview.service";
	public static String server_folderpath = "src\\com\\universeview\\service\\";

	public static String basicserver = "BasicServer";
	public static String basicservername = "basicserver";

	//===============BAK==============================BEG
	// -------------APPXML---------------------------
	public static String APPXML_folder = PROJECT_folder + "WebRoot\\mobileweb\\";
	public static String APPCONTEXT_folder = PROJECT_folder + "WebRoot\\WEB-INF\\";
	
	// -------------android---------------------------
	public static String android_folder = "D:\\android-workspace\\wineandroid2\\";
	public static String android_vopackage = "com.universeview.wine.vo.basic";
	public static String android_vofolderpath = android_folder
			+ "src\\com\\universeview\\wine\\vo\\basic\\";

	public static String android_voadvpackage = "com.universeview.wine.vo";
	public static String android_voadvfolderpath = android_folder
			+ "src\\com\\universeview\\wine\\vo\\";

	// -------------IOS---------------------------
	public static String IOS_folder = "D:\\ShareDev\\IOSCODE\\awine\\";
	public static String IOS_vobasicfolderpath = IOS_folder + "vo\\basic\\";
	public static String IOS_voadvfolderpath = IOS_folder + "vo\\adv\\";
	
	//===============BAK==============================END
	
	
	//--------------OPEARTION----------------------
	public static String[] OPERATION_ARRAY =
	// { "query" };
	{ "delete", "query", "update", "add" };

	public static String[] OPERATION_WEB_ARRAY =
	{ "delete", "query", "update", "add", "showupdate", "showquery", "showadd" };

	// -------------method-----------------------------
	public static String queryname = "query";
	public static String querybypkname = "querybyid";
	public static String queryapproname = "queryappro";
	public static String querysplitname = "querysp";
	public static String querysplitapproname = "queryspappro";
	public static String updatename = "update";
	public static String updatebypkname = "updatebypk";
	public static String deletename = "delete";
	public static String insertname = "insert";

	public static void readXMLFile() throws CommonException
	{
		try
		{
			tablelist = new ArrayList<TableVO>();
			File f = new File(configfilepath);
			SAXReader reader = new SAXReader();
			Document doc = reader.read(f);

			Element data = doc.getRootElement();
			List tabl_elemente_list = data.elements();

			// 表循环
			for (int i = 0; i < tabl_elemente_list.size(); i++)
			{
				TableVO tvo = new TableVO();
				List<FieldVO> fieldlist = new ArrayList<FieldVO>();

				// 表
				Element table = (Element) tabl_elemente_list.get(i);
				List field_element_list = table.elements();

				// -------------必有属性------------------
				// name
				tvo.setName(table.attributeValue("name"));

				// 是否是新表
				if (table.attributeValue("newtable") != null)
				{
					tvo.setNewtable(Integer.parseInt(table.attributeValue("newtable")));
				}

				// 可选属性 enumitem---是否可以作为枚举值数据
				if (table.attributeValue("enumitem") != null)
				{
					tvo.setEnumitem(Integer.parseInt(table.attributeValue("enumitem")));
				}

				
//				
//				// url
//				tvo.setUrl(table.attributeValue("url"));
//
//				// action
//				tvo.setAction(table.attributeValue("action"));
//
//				// server
//				tvo.setServer(table.attributeValue("server"));
//
//			
//				// -------------可选属性-------------
//				if (table.attributeValue("rebuildvo") != null)
//				{
//					tvo.setRebuildvo(Integer.parseInt(table.attributeValue("rebuildvo")));
//				}
//
//				// ------------- 可选属性-------------
//				if (table.attributeValue("rebuilddao") != null)
//				{
//					tvo.setRebuilddao(Integer.parseInt(table.attributeValue("rebuilddao")));
//				}

				// -------------元素-------------
				for (int j = 0; j < field_element_list.size(); j++)
				{
					FieldVO fvo = new FieldVO();

					Element field = (Element) field_element_list.get(j);

					// 必有属性
					fvo.setName(field.attributeValue("name"));

					// bigname
					String firstchar = fvo.getName().substring(0, 1);
					String Bigname = firstchar.toUpperCase() + fvo.getName().substring(1);
					fvo.setBigname(Bigname);

					// 必有属性
					fvo.setType(field.attributeValue("type"));

					// 可选属性
					if (field.attributeValue("length") != null)
					{
						fvo.setLength(Integer.parseInt(field.attributeValue("length")));
					}

					// 可选属性
					if (field.attributeValue("allownull") != null)
					{
						fvo.setAllownull(Integer.parseInt(field.attributeValue("allownull")));
					}

					// 可选属性 primarykey
					if (field.attributeValue("primarykey") != null)
					{
						fvo.setPrimarykey(Integer.parseInt(field.attributeValue("primarykey")));

						// 设定table的主属性fieldvo
						tvo.setPrimaryfield(fvo);
					}

					// 可选属性 isvalue
					if (field.attributeValue("isvalue") != null)
					{
						fvo.setIsvalue(Integer.parseInt(field.attributeValue("isvalue")));
					}

					// 可选属性autoIncrement
					if (field.attributeValue("autoIncrement") != null)
					{
						fvo.setAutoIncrement(Integer
								.parseInt(field.attributeValue("autoIncrement")));
					}

					// 可选属性autoIncrement
					if (field.attributeValue("newfield") != null)
					{
						fvo.setNewfield(Integer.parseInt(field.attributeValue("newfield")));
					}

					// 可选属性defaultvalue
					if (field.attributeValue("defaultvalue") != null)
					{
						fvo.setDefaultvalue(field.attributeValue("defaultvalue"));
					}

					// 可选属性cname
					if (field.attributeValue("cname") != null)
					{
						fvo.setCname(field.attributeValue("cname"));
					}

					// 可选属性query
					if (field.attributeValue("query") != null)
					{
						fvo.setQuery(Integer.parseInt(field.attributeValue("query")));
					}

					// 可选属性mod
					if (field.attributeValue("mod") != null)
					{
						fvo.setMod(Integer.parseInt(field.attributeValue("mod")));
					}

					// 可选属性list
					if (field.attributeValue("list") != null)
					{
						fvo.setList(Integer.parseInt(field.attributeValue("list")));
					}

					// 可选属性 enum---是否是枚举值
					if (field.attributeValue("fieldtype") != null)
					{
						fvo.setFieldtype(Integer.parseInt(field.attributeValue("fieldtype")));

						// 固定的枚举
						if (fvo.getFieldtype() == FieldVO.FIELDTYPE_FIXEDENUM)
						{
							List itemlist = field.elements();

							// 元素列表
							List<ItemVO> enumlist = new ArrayList<ItemVO>();

							if (itemlist != null && itemlist.size() > 0)
							{
								for (int k = 0; k < itemlist.size(); k++)
								{
									ItemVO itemvo = new ItemVO();
									Element itemfield = (Element) itemlist.get(k);

									itemvo.setValue(Integer.parseInt(itemfield
											.attributeValue("value")));
									itemvo.setName(itemfield.attributeValue("name"));
									itemvo.setCname(itemfield.attributeValue("cname"));

									// 添加入enumlist
									enumlist.add(itemvo);
								}
							}

							// 添加入enumlist
							fvo.setEnumlist(enumlist);
						}

					}

					fieldlist.add(fvo);
				}

				tvo.setFieldlist(fieldlist);

				tablelist.add(tvo);
			}
			// System.out.println(
			// "================read xml success!================");
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new CommonException(" 配置文件解析错误");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}

}
