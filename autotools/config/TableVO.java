package com.universeview.utils.autotools.config;

import java.util.List;

public class TableVO
{
	private String name = null;
	private int newtable = 0;

	// 是否继承了enumitem接口
	private int enumitem = 0;

	private List<FieldVO> fieldlist = null;

	private FieldVO primaryfield = null;

	private int rebuildvo = 0;
	private int rebuilddao = 0;

	private String url = null;
	private String action = null;
	private String server = null;

	private String tablename;
	private String firstchar;
	private String classname;// 首字母大写

	// =====显示的name===========================
	private String vo_basic_classname;
	private String vo_classname;
	private String vo_objectname;

	private String dao_basic_classname;
	private String dao_classname;
	private String dao_objectname;

	private String action_classname;
	private String action_objectname;
	private String action_path;
	private String action_filepath;
	private String action_folder;

	private String server_basic_classname;
	private String server_basic_filepath;
	private String server_basic_objectname;

	private String server_classname;
	private String server_filepath;
	private String server_objectname;

	// server的方法名字
	private String server_methodname;

	// 构建名字
	public void buildName()
	{
		if (name != null && !name.equals(""))
		{
			tablename = name;
			firstchar = tablename.substring(0, 1);
			classname = firstchar.toUpperCase() + tablename.substring(1);

			// --------vo---------------
			vo_basic_classname = classname + "BasicVO";
			vo_classname = classname + "VO";
			vo_objectname = tablename + "vo";

			// --------dao---------------
			dao_basic_classname = classname + "BasicDAO";
			dao_classname = classname + "DAO";
			dao_objectname = tablename + "dao";

			// --------action---------------
			action_folder = XMLReader.action_folderpath + "\\" + tablename;

			// ---------server-----------
			server_basic_classname = classname + "BasicServer";
			server_basic_objectname = tablename + "basicserver";

			server_classname = classname + "Server";
			server_objectname = tablename + "server";

			server_basic_filepath = XMLReader.server_basic_folderpath + "\\"
					+ server_basic_classname + ".java";
			server_filepath = XMLReader.server_folderpath + "\\" + server_classname + ".java";

		}
	}

	// 设置action的名字
	public void setActionName(String operation)
	{
		action_classname = classname + AutoTool.getUpperFirst(operation) + "Ctr";
		action_objectname = tablename + operation + "Ctr";
		action_path = operation + tablename;
		action_filepath = action_folder + "\\" + action_classname + ".java";

		System.out.println("action_filepath:" + action_filepath);
		System.out.println("action_classname:" + action_classname);
	}

	public String getAction_objectname()
	{
		return action_objectname;
	}

	public FieldVO getPrimaryfield()
	{
		return primaryfield;
	}

	public void setPrimaryfield(FieldVO primaryfield)
	{
		this.primaryfield = primaryfield;
	}

	public String getAction_path()
	{
		return action_path;
	}

	public String getServer_basic_objectname()
	{
		return server_basic_objectname;
	}

	public void setServer_basic_objectname(String server_basic_objectname)
	{
		this.server_basic_objectname = server_basic_objectname;
	}

	public void setAction_path(String action_path)
	{
		this.action_path = action_path;
	}

	public void setAction_objectname(String action_objectname)
	{
		this.action_objectname = action_objectname;
	}

	public String getDao_basic_classname()
	{
		return dao_basic_classname;
	}

	public void setDao_basic_classname(String dao_basic_classname)
	{
		this.dao_basic_classname = dao_basic_classname;
	}

	public String getDao_classname()
	{
		return dao_classname;
	}

	public void setDao_classname(String dao_classname)
	{
		this.dao_classname = dao_classname;
	}

	public String getDao_objectname()
	{
		return dao_objectname;
	}

	public void setDao_objectname(String dao_objectname)
	{
		this.dao_objectname = dao_objectname;
	}

	public String getServer_methodname()
	{
		return server_methodname;
	}

	public void setServer_methodname(String server_methodname)
	{
		this.server_methodname = server_methodname;
	}

	public String getVo_basic_classname()
	{
		return vo_basic_classname;
	}

	public void setVo_basic_classname(String vo_basic_classname)
	{
		this.vo_basic_classname = vo_basic_classname;
	}

	public String getVo_objectname()
	{
		return vo_objectname;
	}

	public void setVo_objectname(String vo_objectname)
	{
		this.vo_objectname = vo_objectname;
	}

	public String getAction_folder()
	{
		return action_folder;
	}

	public void setAction_folder(String action_folder)
	{
		this.action_folder = action_folder;
	}

	public String getServer_basic_filepath()
	{
		return server_basic_filepath;
	}

	public void setServer_basic_filepath(String server_basic_filepath)
	{
		this.server_basic_filepath = server_basic_filepath;
	}

	public String getServer_filepath()
	{
		return server_filepath;
	}

	public void setServer_filepath(String server_filepath)
	{
		this.server_filepath = server_filepath;
	}

	public String getServer_objectname()
	{
		return server_objectname;
	}

	public void setServer_objectname(String server_objectname)
	{
		this.server_objectname = server_objectname;
	}

	public String getAction_filepath()
	{
		return action_filepath;
	}

	public void setAction_filepath(String action_filepath)
	{
		this.action_filepath = action_filepath;
	}

	public String getTablename()
	{
		return tablename;
	}

	public void setTablename(String tablename)
	{
		this.tablename = tablename;
	}

	public String getFirstchar()
	{
		return firstchar;
	}

	public void setFirstchar(String firstchar)
	{
		this.firstchar = firstchar;
	}

	public String getClassname()
	{
		return classname;
	}

	public String getVo_classname()
	{
		return vo_classname;
	}

	public void setVo_classname(String vo_classname)
	{
		this.vo_classname = vo_classname;
	}

	public void setClassname(String classname)
	{
		this.classname = classname;
	}

	public void setVo_adv_classname(String vo_adv_classname)
	{
		this.vo_classname = vo_adv_classname;
	}

	public String getAction_classname()
	{
		return action_classname;
	}

	public void setAction_classname(String action_classname)
	{
		this.action_classname = action_classname;
	}

	public String getServer_basic_classname()
	{
		return server_basic_classname;
	}

	public void setServer_basic_classname(String server_basic_classname)
	{
		this.server_basic_classname = server_basic_classname;
	}

	public String getServer_classname()
	{
		return server_classname;
	}

	public void setServer_classname(String server_classname)
	{
		this.server_classname = server_classname;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public String getServer()
	{
		return server;
	}

	public void setServer(String server)
	{
		this.server = server;
	}

	public String getBasicVOClassname()
	{
		return name.substring(0, 1).toUpperCase() + name.substring(1) + "BasicVO";
	}

	public String getVOClassname()
	{
		return name.substring(0, 1).toUpperCase() + name.substring(1) + "VO";
	}

	public String getVOObjectname()
	{
		return name.toLowerCase() + "_vo";
	}

	public String getResVOObjectname()
	{
		return name.toLowerCase() + "_res_vo";
	}

	public String getQueryVOObjectname()
	{
		return name.toLowerCase() + "_query_vo";
	}

	public String getBasicDAOClassname()
	{
		return name.substring(0, 1).toUpperCase() + name.substring(1) + "BasicDAO";
	}

	public String getDAOClassname()
	{
		return name.substring(0, 1).toUpperCase() + name.substring(1) + "DAO";
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<FieldVO> getFieldlist()
	{
		return fieldlist;
	}

	public void setFieldlist(List<FieldVO> fieldlist)
	{
		this.fieldlist = fieldlist;
	}

	public int getRebuildvo()
	{
		return rebuildvo;
	}

	public void setRebuildvo(int rebuildvo)
	{
		this.rebuildvo = rebuildvo;
	}

	public int getRebuilddao()
	{
		return rebuilddao;
	}

	public void setRebuilddao(int rebuilddao)
	{
		this.rebuilddao = rebuilddao;
	}

	public int getEnumitem()
	{
		return enumitem;
	}

	public void setEnumitem(int enumitem)
	{
		this.enumitem = enumitem;
	}

	public int getNewtable()
	{
		return newtable;
	}

	public void setNewtable(int newtable)
	{
		this.newtable = newtable;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}
