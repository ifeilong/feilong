package com.feilong.xml.dom;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class Main{

    /** The Constant log. */
    private static final Logger LOGGER        = LoggerFactory.getLogger(Main.class);

    //---------------------------------------------------------------

    private static final String XML_SCHEMA    = "http://www.w3.org/2001/XMLSchema";

    private static final String SCHEMA_LANG   = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    private static final String SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

    private final String        xml_path;

    private final String        schema_path;

    public Main(String xml_path, String schema_path){
        this.xml_path = xml_path;
        this.schema_path = schema_path;
    }

    public String getTextById(String id){
        // xml and schema file path
        File xmlFile = new File(this.xml_path);
        File schemaFile = new File(this.schema_path);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        // important
        //        documentBuilderFactory.setNamespaceAware(true);
        //        // you should add this to tell Java to validate the schema
        documentBuilderFactory.setValidating(true);
        try{
            //            // important
            documentBuilderFactory.setAttribute(SCHEMA_LANG, XML_SCHEMA);
            documentBuilderFactory.setAttribute(SCHEMA_SOURCE, schemaFile);
            DocumentBuilder parser = documentBuilderFactory.newDocumentBuilder();
            Document doc = parser.parse(xmlFile);
            return doc.getElementById(id).getTextContent();
        }catch (Exception e){
            if (LOGGER.isErrorEnabled()){
                LOGGER.error("", e);
            }
        }
        return null;
    }

    //---------------------------------------------------------------

    @Test
    public void testMain(){
        String xmlPath = "E:\\Workspaces\\feilong\\feilong-project\\feilong-novel\\src\\main\\resources\\feilong-novel.xml";
        String schemaPath = "E:\\Workspaces\\feilong\\feilong-project\\feilong-novel\\src\\main\\resources\\feilong-novel.xsd";

        Main m = new Main(xmlPath, schemaPath);
        String id = "catalog_suimeng_重生九二之商业大亨";
        String text = m.getTextById(id);
        System.out.println("The text with id \"" + id + "\" is " + text);
    }
}
