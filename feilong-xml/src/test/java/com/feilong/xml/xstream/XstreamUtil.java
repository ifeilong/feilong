package com.feilong.xml.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XstreamUtil{

    /**
     * 
     * 将bean转换为xml
     * 
     * @param obj
     *            转换的bean
     * 
     * @return bean转换为xml
     * 
     */
    public static String objectToXml(Object obj){
        XStream xStream = new XStream();

        //xstream使用注解转换
        xStream.processAnnotations(obj.getClass());
        return xStream.toXML(obj);

    }

    /**
     * 
     * 将xml转换为bean
     * 
     * @param <T>
     *            泛型
     * 
     * @param xml
     *            要转换为bean的xml
     * 
     * @param cls
     *            bean对应的Class
     * 
     * @return xml转换为bean
     * 
     */
    public static <T> T xmlToObject(String xml,Class<T> cls){
        XStream xstream = new XStream(new DomDriver());//new DomDriver()

        //xstream使用注解转换
        xstream.processAnnotations(cls);
        return (T) xstream.fromXML(xml);

    }

}