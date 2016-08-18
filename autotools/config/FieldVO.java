package com.universeview.utils.autotools.config;

import java.util.List;

public class FieldVO
{
	private String name = null;
	private String type = null;
	private int length = -1;
	private int autoIncrement = -1;
	private int allownull = -1;
	private int primarykey = -1;
	private String cname = null;
	private int query = -1;
	private int mod = -1;
	private int list = -1;
	private int newfield=-1;

	// 是可以累加的值
	private int isvalue = -1;
	
	private String defaultvalue = null;
	
	
	


	// 是否是枚举
	private int fieldtype = 0;
	public static int FIELDTYPE_FIXEDENUM=1;//确定了范围的枚举
	public static int FIELDTYPE_REFENUM=2;//使用引用的枚举

	public static int beginYear = 2014 ; 
	
	public static int yearSpan = 10;

	
	private String bigname = null;
	// 枚举的list
	private List<ItemVO> enumlist = null;

	// ======对表的引用========
	private String reftable = null;
	private String refvalue = null;
	private String refname = null;
 

	
	
	
	
	public String getBigname()
	{
		return bigname;
	}

	public void setBigname(String bigname)
	{
		this.bigname = bigname;
	}

	public int getIsvalue()
	{
		return isvalue;
	}

	public void setIsvalue(int isvalue)
	{
		this.isvalue = isvalue;
	}

	public String getDefaultvalue()
	{
		return defaultvalue;
	}

	public void setDefaultvalue(String defaultvalue)
	{
		this.defaultvalue = defaultvalue;
	}

	public int getPrimarykey()
	{
		return primarykey;
	}

	public void setPrimarykey(int primarykey)
	{
		this.primarykey = primarykey;
	}

	public int getAutoIncrement()
	{
		return autoIncrement;
	}

	public void setAutoIncrement(int autoIncrement)
	{
		this.autoIncrement = autoIncrement;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}

	public int getAllownull()
	{
		return allownull;
	}

	public void setAllownull(int allownull)
	{
		this.allownull = allownull;
	}
 

	public int getFieldtype()
	{
		return fieldtype;
	}

	public void setFieldtype(int fieldtype)
	{
		this.fieldtype = fieldtype;
	}

	public List<ItemVO> getEnumlist()
	{
		return enumlist;
	}

	public void setEnumlist(List<ItemVO> enumlist)
	{
		this.enumlist = enumlist;
	}

	public String getReftable()
	{
		return reftable;
	}

	public void setReftable(String reftable)
	{
		this.reftable = reftable;
	}

	public String getRefvalue()
	{
		return refvalue;
	}

	public void setRefvalue(String refvalue)
	{
		this.refvalue = refvalue;
	}

	public String getRefname()
	{
		return refname;
	}

	public void setRefname(String refname)
	{
		this.refname = refname;
	}

	public String getCname()
	{
		return cname;
	}

	public void setCname(String cname)
	{
		this.cname = cname;
	}

	public int getQuery()
	{
		return query;
	}

	public void setQuery(int query)
	{
		this.query = query;
	}

	public int getMod()
	{
		return mod;
	}

	public void setMod(int mod)
	{
		this.mod = mod;
	}

	public int getList()
	{
		return list;
	}

	public void setList(int list)
	{
		this.list = list;
	}

	
	
	public int getNewfield()
	{
		return newfield;
	}

	public void setNewfield(int newfield)
	{
		this.newfield = newfield;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}
