package com.universeview.utils.autotools;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import com.universeview.utils.CommonException;
import com.universeview.utils.autotools.config.TableVO;
import com.universeview.utils.autotools.config.XMLReader;

public class ActionBuilder extends VOBuilder
{
	public void buildAppMod() throws CommonException
	{
		try
		{
			if (XMLReader.tablelist == null)
			{
				XMLReader.readXMLFile();
			}

			// ---------operation------------------
			String[] operationarray = XMLReader.OPERATION_WEB_ARRAY;

			// 创建VO
			for (int i = 0; i < XMLReader.tablelist.size(); i++)
			{
				TableVO tvo = (TableVO) XMLReader.tablelist.get(i);
				List fieldlist = tvo.getFieldlist();

				System.out.println("........name:" + tvo.getName());

				tvo.buildName();

				// ------------folder------------
				File vofolder = new File(tvo.getAction_folder());
				if (!vofolder.exists())
				{
					vofolder.mkdirs();
					
				}else{
				    continue;
				}

				// 处理每个action
				String operation;
				

				for (int j = 0; j < operationarray.length; j++)
				{
					boolean showcommonexception = false;
					
					operation = operationarray[j];

					System.out.println("....operation:" + operation);

					tvo.setActionName(operation);

					System.out.println("....getAction_filepath...." + tvo.getAction_filepath());

					// ---------------------------------------------------
					// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
					FileWriter writer2 = new FileWriter(tvo.getAction_filepath());

					// ----------header----------------------------------
					// head com.universeview.controller.appuser
					writer2.write(" package " + XMLReader.action_package + "." + tvo.getName()+ ";\n");
					writer2.write(" import javax.servlet.http.HttpServletRequest;\n");
					writer2.write(" import javax.servlet.http.HttpServletResponse;\n");
					writer2.write(" import com.universeview.utils.DataVO;\n");
					writer2.write(" import com.universeview.utils.SplitPageVO;\n");
					writer2.write(" import org.springframework.beans.factory.annotation.Autowired;\n");
					writer2.write(" import org.springframework.stereotype.Controller;\n");
					writer2.write(" import org.springframework.web.bind.annotation.RequestMapping;\n");
					writer2.write(" import org.springframework.web.servlet.ModelAndView;\n");
					writer2.write(" import com.universeview.service." + tvo.getServer_classname()+ ";\n");
					writer2.write(" import  " + XMLReader.voadvpackage + "." + tvo.getVOClassname()+ ";\n");
					writer2.write(" import com.universeview.utils.CommonException;\n");
					writer2.write(" import com.universeview.service.*;\n");
					writer2.write(" import java.util.List; \n");
					writer2.write(" import java.text.SimpleDateFormat;\n");
					writer2.write("  \n");

					// class
					writer2.write("@Controller			 \n");
					writer2.write("public class " + tvo.getAction_classname() + "   \n");
					writer2.write("	{			 \n");

					// server
					writer2.write("		@Autowired		 \n");
					writer2.write("		private " + tvo.getServer_classname() + " "+ tvo.getServer_objectname() + ";\n");
					writer2.write("				 \n");

					// action
					writer2.write("		@RequestMapping(\"/" + operation + tvo.getTablename()+ "\")		 \n");
					
					if(operation.equals("query")){
					    
					    writer2.write("        public ModelAndView dealRegister( HttpServletRequest request , SplitPageVO quvo )\n ");
					    writer2.write("        {        \n");
	                    writer2.write("              \n");
	                    writer2.write("         ModelAndView mv = new ModelAndView();    \n");
	                    writer2.write("              \n");
	                    writer2.write("         try {     \n");
	                    writer2.write("                  \n");
	                    writer2.write( "                  "+tvo.getVOClassname()+" "+tvo.getVo_objectname()+" = new "+tvo.getVOClassname()+"(); \n\n");
	                    writer2.write("              "+tvo.getVo_objectname()+".getParameter(request); \n");
	                    writer2.write("                  \n");
	                    writer2.write("                  SplitPageVO spvo = new SplitPageVO(); \n");
	                    writer2.write("                  int currpagenumber = 1;  \n");
	                    writer2.write("                  if (quvo.getCurrpagenumber() != 0) \n");
	                    writer2.write("                  { \n");
	                    writer2.write("                  currpagenumber = quvo.getCurrpagenumber(); \n");
	                    writer2.write("                  } \n");
	                    writer2.write("                  spvo.setCurrpagenumber(currpagenumber); \n");
	                    writer2.write("                  DataVO datavo =" + tvo.getServer_objectname()+".querysp("+tvo.getVo_objectname()+", spvo); \n");
	                    writer2.write("                  List<"+tvo.getVOClassname()+"> "+tvo.getName()+"list = datavo.getDatalist(); \n");
	                    writer2.write("                  spvo = datavo.getSpvo(); \n");
	                    writer2.write("                  mv.addObject(\"spvo\", spvo); \n");
	                    writer2.write("                  mv.addObject(\""+tvo.getName()+"list\", "+tvo.getName()+"list);");
	                    showcommonexception = true;
	                    writer2.write("                 \n");
                        writer2.write("                  mv.setViewName(\""+tvo.getName()+"/"+tvo.getName()+"list\");\n");
	                    
					}else if(operation.equals("showquery")){
					    
	                        writer2.write("        public ModelAndView dealRegister( HttpServletRequest request , SplitPageVO quvo )\n ");
	                        writer2.write("        {        \n");
	                        writer2.write("              \n");
	                        writer2.write("         ModelAndView mv = new ModelAndView();    \n");
	                        writer2.write("              \n");
	                        writer2.write("         try {     \n");
	                        writer2.write("                  \n");
	                        writer2.write( "                  "+tvo.getVOClassname()+" "+tvo.getVo_objectname()+" = new "+tvo.getVOClassname()+"(); \n\n");

	                        writer2.write("                  \n");
	                        writer2.write("                  SplitPageVO spvo = new SplitPageVO(); \n");
	                        writer2.write("                  int currpagenumber = 1;  \n");
	                        writer2.write("                  if (quvo.getCurrpagenumber() != 0) \n");
	                        writer2.write("                  { \n");
	                        writer2.write("                  currpagenumber = quvo.getCurrpagenumber(); \n");
	                        writer2.write("                  } \n");
	                        writer2.write("                  spvo.setCurrpagenumber(currpagenumber); \n");
	                        writer2.write("                  DataVO datavo =" + tvo.getServer_objectname()+".querysp("+tvo.getVo_objectname()+", spvo); \n");
	                        writer2.write("                  List<"+tvo.getVOClassname()+"> "+tvo.getName()+"list = datavo.getDatalist(); \n");
	                        writer2.write("                  spvo = datavo.getSpvo(); \n");
	                        writer2.write("                  mv.addObject(\"spvo\", spvo); \n");
	                        writer2.write("                  mv.addObject(\""+tvo.getName()+"list\", "+tvo.getName()+"list);");
	                        showcommonexception = true;
	                        writer2.write("                 \n");
	                        writer2.write("                  mv.setViewName(\""+tvo.getName()+"/"+tvo.getName()+"list\");\n");
					}else{
					    
					    writer2.write("        public ModelAndView dealRegister( HttpServletRequest request )\n ");
					    writer2.write("        {        \n");
	                    writer2.write("              \n");
	                    writer2.write("         ModelAndView mv = new ModelAndView();    \n");
	                    writer2.write("              \n");
	                    writer2.write("         try {     \n");
	                    writer2.write("                  \n");
	                    writer2.write( "                  "+tvo.getVOClassname()+" "+tvo.getVo_objectname()+" = new "+tvo.getVOClassname()+"(); \n\n");
	                    
	                    // ======server======================================================
	                    // 添加server的方法
	                    if (operation.equals("delete"))
	                    {
	                        writer2.write("              "+tvo.getVo_objectname()+".getParameter(request);\n");
	                        writer2.write("                 " + tvo.getServer_objectname() + "."+ XMLReader.deletename + "(" + tvo.getVo_objectname() + ");\n");
	                        showcommonexception = true;
	                        writer2.write("                 \n");
	                        writer2.write("                  mv.setViewName(\"redirect:showquery"+tvo.getName()+"\");\n");

	                    } else if (operation.equals("update"))
	                    {
	                        writer2.write("                 "+tvo.getVo_objectname()+".getParameter(request);\n");
	                        writer2.write("                 " + tvo.getServer_objectname() + "."+ XMLReader.updatebypkname + "(" + tvo.getVo_objectname() + ");\n");
	                        writer2.write("                 " + tvo.getVo_objectname() + " = "+ tvo.getServer_objectname() + "." + XMLReader.querybypkname + "("+ tvo.getVo_objectname() + ");\n");
	                        writer2.write("                 mv.addObject(\"" + tvo.getVo_objectname()+ "\", " + tvo.getVo_objectname() + ");\n");
	                        showcommonexception = true;
	                        writer2.write("                 \n");
	                        writer2.write("                  mv.setViewName(\""+tvo.getName()+"/"+tvo.getName()+"detail\");\n");

	                    } else if (operation.equals("add"))
	                    {
	                        writer2.write("              "+tvo.getVo_objectname()+".getParameter(request);\n");
	                        writer2.write("                 " + tvo.getVo_objectname() + " =  "+ tvo.getServer_objectname() + "." + XMLReader.insertname + "("+ tvo.getVo_objectname() + ");\n");
	                        writer2.write("                 mv.addObject(\"" + tvo.getVo_objectname()+ "\", " + tvo.getVo_objectname() + ");\n");
	                        showcommonexception = true;
	                        writer2.write("                 \n");
	                        writer2.write("                  mv.setViewName(\""+tvo.getName()+"/"+tvo.getName()+"add\");\n");

	                    } else if (operation.equals("showupdate"))
	                    {
	                        writer2.write("              "+tvo.getVo_objectname()+".getParameter(request);\n");
	                        writer2.write("                 " + tvo.getVo_objectname() + " = "+ tvo.getServer_objectname() + "." + XMLReader.querybypkname + "("+ tvo.getVo_objectname() + ");\n");
	                        writer2.write("                 mv.addObject(\"" + tvo.getVo_objectname()+ "\", " + tvo.getVo_objectname() + ");\n");
	                        showcommonexception = true;
	                        writer2.write("                 \n");
	                        writer2.write("                  mv.setViewName(\""+tvo.getName()+"/"+tvo.getName()+"detail\");\n");
	                    } else if (operation.equals("showadd"))
                        {
                            
	                        writer2.write("              "+tvo.getVo_objectname()+".getParameter(request);\n");
                            showcommonexception = false;
                            writer2.write("                 \n");
                            writer2.write("                  mv.setViewName(\""+tvo.getName()+"/"+tvo.getName()+"add\");\n");
                        }
					}
					

					writer2.write("                 return mv   ;\n");
					
					if (showcommonexception)
					{
						writer2.write("	        	}  catch (CommonException e) {			 \n");
						writer2.write("               e.printStackTrace();           \n");
						writer2.write("	           		mv.setViewName(\"error\");			 \n");
						writer2.write("	            	return mv;			 \n");

					}

					writer2.write("				} catch (Exception ex){ \n");
					writer2.write("               ex.printStackTrace();           \n");
					writer2.write("	           	 	mv.setViewName(\"error\");			 \n");
					writer2.write("	            	return mv;			 \n");
					writer2.write("				}   \n");
					writer2.write("	 		}\n");
					writer2.write("	 		}\n");
					writer2.close();
				}
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
			System.out.println("main...");
			ActionBuilder vb = new ActionBuilder();
			vb.buildAppMod();
			System.out.println("VO builder exe complete...");
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
