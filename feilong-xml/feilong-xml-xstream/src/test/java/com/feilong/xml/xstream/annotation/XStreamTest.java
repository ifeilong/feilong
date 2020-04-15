package com.feilong.xml.xstream.annotation;

import org.junit.Test;

public class XStreamTest{

    /**
     * TestXStreamTest.
     */
    @Test
    public void testXStreamTest(){

        Request requestTest = new Request();
        requestTest.setReturnCode("200");
        requestTest.setReturnMessage("ok");
        requestTest.setReturn_Message("ok");
        requestTest.setStatus(200);
        requestTest.setReqTime("2013-09-22 00:00:00");

        String request = XstreamUtil.objectToXml(requestTest);
        System.out.println(request);

        Response responseTest = XstreamUtil.xmlToObject(request, Response.class);
        System.out.println(responseTest);

    }

}