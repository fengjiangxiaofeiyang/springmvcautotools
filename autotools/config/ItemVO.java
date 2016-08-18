package com.universeview.utils.autotools.config;

import com.universeview.utils.EnumItem;


public class ItemVO implements EnumItem
{
	private int value = -1;
	private String name = null;
	private String cname = null;
	
	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCname()
	{
		return cname;
	}

	public void setCname(String cname)
	{
		this.cname = cname;
	}

}
