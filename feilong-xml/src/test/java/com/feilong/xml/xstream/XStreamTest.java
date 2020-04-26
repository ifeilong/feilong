package com.feilong.xml.xstream;

import org.junit.Test;

import com.feilong.test.AbstractTest;
import com.feilong.xml.xstream.annotation.Request;
import com.feilong.xml.xstream.annotation.Response;

public class XStreamTest extends AbstractTest{

    @Test
    public void testXStreamTest(){
        Request requestTest = new Request();
        requestTest.setReturnCode("200");
        requestTest.setReturnMessage("ok");
        requestTest.setReturn_Message("ok");
        requestTest.setStatus(200);
        requestTest.setReqTime("2013-09-22 00:00:00");

        String request = XstreamTemp.objectToXml(requestTest);
        LOGGER.debug(request);

        Response responseTest = XstreamTemp.xmlToObject(request, Response.class);
        LOGGER.debug("" + responseTest);
    }

}