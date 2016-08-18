
package com.universeview.utils.autotools;

import java.io.File;
import java.io.FileWriter;
import com.universeview.utils.CommonException;
import com.universeview.utils.autotools.config.AutoTool;
import com.universeview.utils.autotools.config.TableVO;
import com.universeview.utils.autotools.config.XMLReader;


/**
 * @Title: WEBJSPBuilder.java
 * @Description: 生成JSP页面的工具类
 * @author: keven.c
 * @date: 2014-03-07 
 * @see
 * @Copyright: 2014 keven.c Inc. All rights reserved
 * @version: V1.0
 */
public class WEBJSPBuilder extends VOBuilder {


    // 默认目录： ../WebRoot/webpage/        最终表现形式：../WebRoot/webpage/表名文件夹/webjspfile_*
    protected String webjsp_folder_default = XMLReader.WEBJSP_folder_default;

    protected String webjspfile_add;

    protected String webjspfile_list;

    protected String webjspfile_detail;

    public void buildWebMod() throws CommonException {

        try {
            
            if (XMLReader.tablelist == null) {
                XMLReader.readXMLFile();
            }

            String pkname, bpkname;

            /*
             * @comment：创建VO对象
             */
            for (int i = 0; i < XMLReader.tablelist.size(); i++) {
                
                /*
                 * @comment：persistenceconfig.xml中每一个表解析且对应的对象即TableVO
                 */
                TableVO tvo = (TableVO) XMLReader.tablelist.get(i);
                
                // 获取表名
                tablename = tvo.getName();
                System.out.println("表名："+tablename);
                
                // 根据表名设置类名,即首字母大写
                classname = AutoTool.getUpperFirst(tablename);

                // 获取该表的主键名
                pkname = tvo.getPrimaryfield().getName();
                
                //生成首字母大写的主键名
                bpkname = AutoTool.getUpperFirst(pkname);

                /*
                 * @comment：根据表生成的类为  类名+VO ; 对象属性为 表名+vo ;
                 */
                voclassname = classname + "VO";
                voobjectname = tablename + "vo";
                
                /**
                 * @comment：生成每张表的增加页面,修改页面,列表页面
                 */
                webjspfile_add = tablename + "add.jsp";
                webjspfile_list = tablename + "list.jsp";
                webjspfile_detail = tablename + "detail.jsp";


                /*
                 * @comment：生成的JSP页面默认的放置路径
                 */
                String jspfolder_default_path = webjsp_folder_default + tablename;
                
                System.out.println("生成的JSP页面默认的放置路径:" + jspfolder_default_path);

                File jspfolder_default = new File(jspfolder_default_path);
                
                if (!jspfolder_default.exists()) {
                    jspfolder_default.mkdirs();
                }

                /**
                 * **************************************************************
                 * ************************webjspfile_add************************
                 * **************************************************************
                 */
                
                // add页面的放置路径：jspfolder_default_path表示： ../表名文件夹  ; webjspfile_add表示add页面名
                String addfilefilepath = jspfolder_default_path + "\\" + webjspfile_add;
                
                File addjspfile = new File(addfilefilepath);
                
                // 如果不存在,则开始生成
                if (!addjspfile.exists()) {

                    FileWriter writer = new FileWriter(addfilefilepath);

                    writer.write("<%@ page language=\"java\" import=\"java.util.*\" pageEncoding=\"UTF-8\"%>	\n ");
                    writer.write("<%@ page import=\"java.util.*\"%>	\n ");
                    writer.write("<%@ page import=\"com.universeview.bean." + voclassname + "\"%>	\n ");
                    writer.write("<%@ page import=\"com.universeview.utils.*\"%>	\n ");
                    writer.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">	\n ");
                    writer.write("<%	\n ");
                    writer.write("	//===================getresult==============\n ");
                    writer.write("	" + voclassname + " " + voobjectname + " =  new " + voclassname + "(); \n ");
                    writer.write("	if (request.getAttribute(\"" + voobjectname + "\") != null) \n ");
                    writer.write("	{\n ");
                    writer.write("		" + voobjectname + " =(" + voclassname + ") request.getAttribute(\"" + voobjectname + "\");\n ");
                    writer.write("	}\n ");
                    writer.write("%>	\n ");
                    writer.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">	\n ");
                    writer.write("	<head>\n ");
                    writer.write("		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n ");
                    writer.write("		<title></title>\n ");
                    writer.write("		<link href=\"css/index.css\" rel=\"stylesheet\" type=\"text/css\" />\n ");
                    writer.write("		<link href=\"../style/user.css\" rel=\"stylesheet\" type=\"text/css\" />\n ");
                    writer.write("		<script type=\"text/javascript\">\n ");
                    writer.write("  	function confirm()\n   ");
                    writer.write("		{						\n ");
                    writer.write("			df.action=\"/add" + tablename + ".do\";		\n ");
                    writer.write("			df.submit();\n ");
                    writer.write("		}\n ");
                    writer.write("		function showquery" + tablename + "()					\n");
                    writer.write("		{					\n");
                    writer.write("			window.location.href=\"/showquery" + tablename + ".do\";				\n");
                    writer.write("		}					\n");
                    writer.write("	 						\n");
                    writer.write("		</script>\n ");
                    writer.write("	</head>\n ");
                    writer.write("	<body>\n ");
                    writer.write("		<div id=\"mainContent\">\n ");
                    writer.write("			<div class=\"user-con\">\n ");
                    writer.write("				<div class=\"con-r\">\n ");
                    writer.write("					<form name=\"df\" action=\"\" method=\"post\">\n ");
                    writer.write("						<input name=\"" + pkname + "\" value=\"<%=" + voobjectname + ".get" + bpkname + "pstr() %>\"\n ");
                    writer.write("							type=\"hidden\" />\n ");
                    writer.write("						<table width=\"90%\" border=\"0\" cellpadding=\"6\" cellspacing=\"1\"\n ");
                    writer.write("							class=\"tab-con\">\n ");
                    writer.write("							<tr>\n ");
                    writer.write("								<td colspan=\"2\" align=\"right\" valign=\"bottom\"  >\n ");
                    writer.write("									<a href=\"javascript:showquery" + tablename + "()\"><strong>返回查询</strong></a>\n ");
                    writer.write("								</td>\n ");
                    writer.write("							</tr>\n ");
                    writer.write("							<tr>\n ");
                    writer.write("								<td colspan=\"2\" align=\"center\" valign=\"bottom\" class=\"tab-tit\">\n ");
                    writer.write("									<strong>增加</strong>\n ");
                    writer.write("								</td>\n ");
                    writer.write("							</tr>\n ");
                    writer.write("							<%=" + voobjectname + ".producePageDetailModTR() %>\n ");
                    writer.write("							<tr>\n ");
                    writer.write("								<td align=\"center\" colspan=\"2\" class=\"tab-inp\">\n ");
                    writer.write("									<input class=\"user_new_but\" type=\"button\" value=\"确定\"\n ");
                    writer.write("										onclick=\"confirm()\" />\n ");
                    writer.write("									<input class=\"user_new_but\" type=\"reset\" value=\"重置\" />\n ");
                    writer.write("								</td>\n ");
                    writer.write("							</tr>\n ");
                    writer.write("						</table>\n ");
                    writer.write("					</form>\n ");
                    writer.write("				</div>\n ");
                    writer.write("			</div>\n ");
                    writer.write("		</div>\n ");
                    writer.write("	</body>\n ");
                    writer.write("</html>	\n ");

                    writer.close();
                }

                /**
                 * **************************************************************
                 * ************************webjspfile_detail************************
                 * **************************************************************
                 */
                
                // 生成detail页面,同上
                String detailfilefilepath = jspfolder_default_path + "\\" + webjspfile_detail;
                
                File detailfilefile = new File(detailfilefilepath);
                
                if (!detailfilefile.exists()) {
                    
                    FileWriter writer = new FileWriter(detailfilefilepath);

                    writer.write("<%@ page language=\"java\" import=\"java.util.*\" pageEncoding=\"UTF-8\"%>	\n ");
                    writer.write("<%@ page import=\"java.util.*\"%>	\n ");

                    writer.write("<%@ page import=\"com.universeview.bean." + voclassname + "\"%>	\n ");
                    writer.write("<%@ page import=\"com.universeview.utils.*\"%>	\n ");

                    writer.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">	\n ");
                    writer.write("<%	\n ");
                    writer.write("	//===================getresult==============\n ");
                    writer.write("	" + voclassname + " " + voobjectname + " =  new " + voclassname + "(); \n ");
                    writer.write("	if (request.getAttribute(\"" + voobjectname + "\") != null) \n ");
                    writer.write("	{\n ");
                    writer.write("		" + voobjectname + " =(" + voclassname + ") request.getAttribute(\"" + voobjectname + "\");\n ");
                    writer.write("	}\n ");
                    writer.write("%>	\n ");
                    writer.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">	\n ");
                    writer.write("	<head>\n ");
                    writer.write("		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n ");
                    writer.write("		<title></title>\n ");
                    writer.write("		<link href=\"css/index.css\" rel=\"stylesheet\" type=\"text/css\" />\n ");
                    writer.write("		<link href=\"../style/user.css\" rel=\"stylesheet\" type=\"text/css\" />\n ");
                    writer.write("		<script type=\"text/javascript\">\n ");
                    writer.write("  	function confirm()\n ");
                    writer.write("		{						\n ");
                    writer.write("			df.action=\"/update" + tablename + ".do\";		\n ");
                    writer.write("			df.submit();\n ");
                    writer.write("		}\n ");
                    writer.write("		function showquery" + tablename + "()					\n");
                    writer.write("		{					\n");
                    writer.write("			window.location.href=\"/showquery" + tablename + ".do\";				\n");
                    writer.write("		}					\n");
                    writer.write("	 						\n");
                    writer.write("		</script>\n ");
                    writer.write("	</head>\n ");
                    writer.write("	<body>\n ");
                    writer.write("		<div id=\"mainContent\">\n ");
                    writer.write("			<div class=\"user-con\">\n ");
                    writer.write("				<div class=\"con-r\">\n ");
                    writer.write("					<form name=\"df\" action=\"\" method=\"post\">\n ");
                    writer.write("						<input name=\"" + pkname + "\" value=\"<%=" + voobjectname + ".get" + bpkname + "pstr() %>\"\n ");
                    writer.write("							type=\"hidden\" />\n ");
                    writer.write("						<table width=\"90%\" border=\"0\" cellpadding=\"6\" cellspacing=\"1\"\n ");
                    writer.write("							class=\"tab-con\">\n ");
                    writer.write("							<tr>\n ");
                    writer.write("								<td colspan=\"2\" align=\"right\" valign=\"bottom\"  >\n ");
                    writer.write("									<a href=\"javascript:showquery" + tablename + "()\"><strong>返回查询</strong></a>\n ");
                    writer.write("								</td>\n ");
                    writer.write("							</tr>\n ");
                    writer.write("							<tr>\n ");
                    writer.write("								<td colspan=\"2\" align=\"center\" valign=\"bottom\" class=\"tab-tit\">\n ");
                    writer.write("									<strong>修改</strong>\n ");
                    writer.write("								</td>\n ");
                    writer.write("							</tr>\n ");
                    writer.write("							<%=" + voobjectname + ".producePageDetailModTR() %>\n ");
                    writer.write("							<tr>\n ");
                    writer.write("								<td align=\"center\" colspan=\"2\" class=\"tab-inp\">\n ");
                    writer.write("									<input class=\"user_new_but\" type=\"button\" value=\"确定\"\n ");
                    writer.write("										onclick=\"confirm()\" />\n ");
                    writer.write("									<input class=\"user_new_but\" type=\"reset\" value=\"重置\" />\n ");
                    writer.write("								</td>\n ");
                    writer.write("							</tr>\n ");
                    writer.write("						</table>\n ");
                    writer.write("					</form>\n ");
                    writer.write("				</div>\n ");
                    writer.write("			</div>\n ");
                    writer.write("		</div>\n ");
                    writer.write("	</body>\n ");
                    writer.write("</html>	\n ");

                    writer.close();

                }
                
                /**
                 * **************************************************************
                 * ************************webjspfile_list***********************
                 * **************************************************************
                 */
                
                // 生成list页面,同上
                String listfilefilepath = jspfolder_default_path + "\\" + webjspfile_list;
                File listfilefile = new File(listfilefilepath);
                if (!listfilefile.exists()) {
                    FileWriter writer = new FileWriter(listfilefilepath);

                    writer = new FileWriter(listfilefilepath);
                    writer.write("	<%@ page language=\"java\" import=\"java.util.*\" pageEncoding=\"UTF-8\"%>						\n");
                    writer.write("	<%@ page import=\"java.util.List\"%>						\n");
                    writer.write("  <%@ page import=\"com.universeview.bean." + voclassname + "\"%>	\n ");
                    writer.write("  <%@ page import=\"com.universeview.utils.*\"%>	\n ");
                    writer.write("	<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">						\n");
                    writer.write("	<%						\n");
                    writer.write("							\n");
                    writer.write("	 	//===================getresult==============					\n");
                    writer.write("		List " + tablename + "list =null;					\n");
                    writer.write("		if (request.getAttribute(\"" + tablename + "list\") != null){					\n");
                    writer.write("	    						\n");
                    writer.write("		" + tablename + "list =  (List) request.getAttribute(\"" + tablename + "list\");					\n");
                    writer.write("		}					\n");
                    writer.write("	 	 					\n");

                    writer.write("							\n");
                    writer.write("		//============翻页================					\n");
                    writer.write("		SplitPageVO spvo = new SplitPageVO();					\n");
                    writer.write("		if (request.getAttribute(\"spvo\") != null) 					\n");
                    writer.write("		{					\n");
                    writer.write("			spvo = (SplitPageVO) request.getAttribute(\"spvo\");				\n");
                    writer.write("		}					\n");
                    writer.write("							\n");
                    writer.write("		//===========查询" + voobjectname + "=====					\n");
                    writer.write("		" + voclassname + " query" + voobjectname + " = new " + voclassname + "();					\n");
                    writer.write("		if(request.getAttribute(\"" + voobjectname + "\")!=null)	\n");
                    writer.write("		{				\n");
                    writer.write("		    query" + voobjectname + " = (" + voclassname + ") request.getAttribute(\"" + voobjectname + "\");					\n");
                    writer.write("		}	\n");
                    writer.write("		query" + voobjectname + ".getParameter(request);					\n");
                    writer.write("	 						\n");
                    writer.write("	 						\n");
                    writer.write("	%>						\n");
                    writer.write("	<html xmlns=\"http://www.w3.org/1999/xhtml\">						\n");
                    writer.write("		<head>					\n");
                    writer.write("			<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />				\n");
                    writer.write("			<title> </title>				\n");
                    writer.write("			<script type=\"text/javascript\" src=\"/js/common.js\">				\n");
                    writer.write("	</script>						\n");
                    writer.write("			<script type=\"text/javascript\">				\n");
                    writer.write("	 						\n");
                    writer.write("		//==========操作===============  					\n");
                    writer.write("      function query" + tablename + "()					\n");
                    writer.write("		{					\n");
                    writer.write("			initpagenum();				\n");
                    writer.write("			df.submit();	 			\n");
                    writer.write("		}					\n");
                    writer.write("	 						\n");
                    writer.write("	 	function mod" + tablename + "(id)					\n");
                    writer.write("		{					\n");
                    writer.write("			df.action=\"/showupdate" + tablename + ".do?" + pkname + "=\"+id;				\n");
                    writer.write("			df.submit();				\n");
                    writer.write("		}					\n");
                    writer.write("							\n");
                    writer.write("                         \n");
                    writer.write("      function del" + tablename + "(id)                   \n");
                    writer.write("      {                   \n");
                    writer.write("          df.action=\"/delete" + tablename + ".do?" + pkname + "=\"+id;                \n");
                    writer.write("          df.submit();                \n");
                    writer.write("      }                   \n");
                    writer.write("                          \n");
                    writer.write("		function showadd" + tablename + "()					\n");
                    writer.write("		{					\n");
                    writer.write("			df.action=\"/showadd" + tablename + ".do\";				\n");
                    writer.write("			df.submit();				\n");
                    writer.write("		}					\n");
                    writer.write("	 						\n");
                    writer.write("	</script>						\n");
                    writer.write("			<link href=\"/style/user.css\" rel=\"stylesheet\" type=\"text/css\" />				\n");
                    writer.write("		</head>					\n");
                    writer.write("		<body onload=\"loadalert()\">					\n");
                    writer.write("			<div id=\"mainContent\" style=\"height: auto\">				\n");
                    writer.write("				<div class=\"user-con\">			\n");
                    writer.write("					<div class=\"con-r\">		\n");
                    writer.write("						<form name=\"df\" action=\"/query" + tablename + ".do\" method=\"post\">	\n");
                    writer.write("										<input name=\"nextpagenumber\"						\n");
                    writer.write("											value=\"<%=spvo.getNextpagenumber()%>\" type=\"hidden\" />					\n");
                    writer.write("										<input name=\"prevpagenumber\"						\n");
                    writer.write("											value=\"<%=spvo.getPrevpagenumber()%>\" type=\"hidden\" />					\n");
                    writer.write("										<input name=\"currpagenumber\"						\n");
                    writer.write("											value=\"<%=spvo.getCurrpagenumber()%>\" type=\"hidden\" />					\n");
                    writer.write("							<table width=\"100%\" cellpadding=\"0\" cellspacing=\"1\" border=\"0\" class=\"tab-con\"\n");
                    writer.write("								class=\"mid_table_1\">			 					\n");
                    writer.write("											<tr>					\n");
                    writer.write("												<td   colspan=\"4\"   align=\"right\"  >				\n");
                    writer.write("														<a href=\"javascript:showadd" + tablename + "()\"><strong>新增</strong></a>				\n");
                    writer.write("												</td>				\n");
                    writer.write("											</tr>					\n");
                    writer.write("											<tr>					\n");
                    writer.write("												<td   colspan=\"4\"   align=\"right\" class=\"tab-tit\">				\n");
                    writer.write("													<div align=\"center\">			\n");
                    writer.write("														<strong>查询 </strong>		\n");
                    writer.write("													</div>			\n");
                    writer.write("												</td>				\n");
                    writer.write("											</tr>					\n");
                    writer.write("											<%=query" + tablename + "vo.producePAGEQueryTR()%>					\n");
                    writer.write("											<tr>					\n");
                    writer.write("												<td  colspan=\"4\"  height=\"25\"   align=\"center\" class=\"tab-inp\">				\n");
                    writer.write("													<input type=\"button\" value=\"查询\" onclick=\"query" + tablename + "()\"			\n");
                    writer.write("														class=\"user_new_but\" />		\n");
                    writer.write("													<input type=\"reset\" value=\"重置\"  			\n");
                    writer.write("														class=\"user_reset_but\" />		\n");
                    writer.write("												</td>				\n");
                    writer.write("											</tr>					\n");
                    writer.write("				 			\n");
                    writer.write("								<tr>								\n");
                    writer.write("									<td  colspan=\"4\"  align=\"center\">							\n");
                    writer.write("										<table width=\"100%\" cellpadding=\"0\" cellspacing=\"1\"						\n");
                    writer.write("											class=\"tab-con\">					\n");
                    writer.write("											<%=query" + tablename + "vo.producePageListCNameTR()%>					\n");
                    writer.write("											<% if(" + tablename + "list!=null && " + tablename + "list.size()>0){ %>					\n");
                    writer.write("											<%  					\n");
                    writer.write("											    " + voclassname + " listvo;					\n");
                    writer.write("												for(int i=0;i<" + tablename + "list.size();i++){				\n");
                    writer.write("												listvo = (" + voclassname + ")" + tablename + "list.get(i);				\n");
                    writer.write("											 %>					\n");
                    writer.write("											<%=listvo.producePageListShowTR()%>					\n");
                    writer.write("											<%} %>					\n");
                    writer.write("											<%} %>					\n");
                    writer.write("										</table>						\n");
                    writer.write("									</td>							\n");
                    writer.write("								<tr>								\n");
                    writer.write("									<td align=\"right\"    colspan=\"4\">							\n");
                    writer.write("										<div class=\"page\" style=\"padding-right: 15px;\">						\n");
                    writer.write("											<a href=\"javascript:prevpage()\">上一页</a>					\n");
                    writer.write("											<a>/</a>					\n");
                    writer.write("											<a href=\"javascript:nextpage()\">下一页</a> 					\n");
                    writer.write("											第<%=spvo.getCurrpagenumber()%>页/<%=spvo.getTotalpagenumber()%>共页					\n");
                    writer.write("										</div>						\n");
                    writer.write("									</td>							\n");
                    writer.write("								</tr>								\n");
                    writer.write("	 						\n");
                    writer.write("							</table>\n");
                    writer.write("						</form>	\n");
                    writer.write("					</div>		\n");
                    writer.write("				</div>			\n");
                    writer.write("			</div>				\n");
                    writer.write("	 						\n");
                    writer.write("		</body>					\n");
                    writer.write("	</html>						\n");
                    writer.close();
                }

            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new CommonException(" 配置文件解析错误");
        }
    }

    /**
     * @param args
     * @throws CommonException
     */
    public static void main(String[] args) throws CommonException {

        try {
            WEBJSPBuilder vb = new WEBJSPBuilder();
            vb.buildWebMod();
            System.out.println("JSP builder exe complete...");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
