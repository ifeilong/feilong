package com.feilong.xml.temp.annotation;

import static com.feilong.core.CharsetType.UTF8;

import org.junit.Test;

import com.feilong.lib.xstream.XStream;
import com.feilong.lib.xstream.io.naming.NoNameCoder;
import com.feilong.lib.xstream.io.xml.DomDriver;
import com.feilong.test.AbstractTest;

public class XstreamTempTest extends AbstractTest{

    @Test
    public void test(){
        Request requestTest = new Request();
        requestTest.setReturnCode("200");
        requestTest.setReturnMessage("ok");
        requestTest.setReturn_Message("ok");
        requestTest.setStatus(200);
        requestTest.setReqTime("2013-09-22 00:00:00");

        String request = objectToXml(requestTest);
        LOGGER.debug(request);

        Response responseTest = xmlToObject(request, Response.class);
        LOGGER.debug("" + responseTest);
    }

    /**
     * 
     * 将bean转换为xml
     * 
     * @param obj
     *            转换的bean
     * 
     * @return bean转换为xml
     */
    public static String objectToXml(Object obj){
        XStream xStream = new XStream(new DomDriver(UTF8, new NoNameCoder()));

        //xstream使用注解转换
        xStream.processAnnotations(obj.getClass());
        return xStream.toXML(obj);

    }

    /**
     * 将xml转换为bean
     * 
     * @param <T>
     *            泛型
     * @param xml
     *            要转换为bean的xml
     * @param cls
     *            bean对应的Class
     * @return xml转换为bean
     */
    @SuppressWarnings("unchecked")
    public static <T> T xmlToObject(String xml,Class<T> cls){
        XStream xstream = new XStream(new DomDriver());//new DomDriver()

        //xstream使用注解转换
        xstream.processAnnotations(cls);
        return (T) xstream.fromXML(xml);
    }

}