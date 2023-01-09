package com.feilong.xml.temp.annotation;

import static com.feilong.core.CharsetType.UTF8;

import org.junit.Test;

import com.feilong.test.AbstractTest;
import com.feilong.xml.XmlUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;

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

        Response responseTest = XmlUtil.toBean(request, Response.class);
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

}