package com.feilong.taglib.display;

//package com.feilong.common.configure.feilongConfig;
//
//import org.w3c.dom.Node;
//
//import com.feilong.core.xml.javaxXml.XmlConfigure;
//
///**
// * 面包屑配置
// * 
// */
//@Deprecated
//public class SiteMapConfigure{
//
//	private String				xmlPath;
//
//	private XmlConfigure	xmlConfigure;
//
//	/**
//	 * 带xml路径的构造函数
//	 * 
//	 * @param xmlPath
//	 */
//	public SiteMapConfigure(String xmlPath){
//		this.xmlPath = xmlPath;
//		xmlConfigure = new XmlConfigure(xmlPath);
//	}
//
//	/**
//	 * 根据当前的url 获得sitemap 里面的节点
//	 * 
//	 * @param currentUrl
//	 *            当前的url
//	 * @return 根据当前的url 获得sitemap 里面的节点
//	 */
//	public Node getCurrentNode(String currentUrl){
//		return xmlConfigure.getNodeByXPath("//siteMapNode[@url='" + currentUrl + "']");
//	}
//
//	/**
//	 * 生成siteMap内容
//	 * 
//	 * @return 生成siteMap内容
//	 */
//	public String getSiteMapContent(String currentUrl){
//		Node node = getCurrentNode(currentUrl);
//		if (null != node){
////			StringBuilder stringBuilder = new StringBuilder("");
////			HtmlSpanEntity htmlSpanEntity = new HtmlSpanEntity();
////			htmlSpanEntity.setContent(xmlConfigure.getAttributeValue(node, "title"));
////			htmlSpanEntity.setTitle(xmlConfigure.getAttributeValue(node, "description"));
////			stringBuilder.append(HTMLSpan.createSpan(htmlSpanEntity));
////			return stringBuilder.toString();
//		}
//		return EMPTY;
//	}
//}
