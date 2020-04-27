package com.feilong.office.excel;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoxiaSupportSettings{

    /** The Constant log. */
    private static final Logger         LOGGER  = LoggerFactory.getLogger(LoxiaSupportSettings.class);

    //---------------------------------------------------------------

    private static LoxiaSupportSettings instance;

    private static final String[]       CONFIGS = new String[] { "loxiasupport", "loxia/support-default" };

    private final List<Properties>      props   = new ArrayList<>();

    private LoxiaSupportSettings(){
        for (String config : CONFIGS){
            InputStream is = getResourceAsStream(config + ".properties", LoxiaSupportSettings.class);
            if (is != null){
                Properties prop = new Properties();
                try{
                    prop.load(is);
                    props.add(prop);
                }catch (IOException e){
                    LOGGER.warn("Error occurs when loading " + config + ".properties");
                }
            }else{
                LOGGER.warn("Could not find {}.properties", config);
            }
        }
    }

    public static LoxiaSupportSettings getInstance(){
        if (instance == null){
            instance = new LoxiaSupportSettings();
        }
        return instance;
    }

    public String get(String name){
        String result = null;
        for (Properties prop : props){
            result = prop.getProperty(name);
            if (result != null){
                break;
            }
        }
        return result;
    }

    private InputStream getResourceAsStream(String resourceName,Class<?> callingClass){
        URL url = getResource(resourceName, callingClass);
        try{
            return (url != null) ? url.openStream() : null;
        }catch (IOException e){
            return null;
        }
    }

    private URL getResource(String resourceName,Class<?> callingClass){
        URL url = null;
        url = Thread.currentThread().getContextClassLoader().getResource(resourceName);
        if (url == null){
            url = LoxiaSupportSettings.class.getClassLoader().getResource(resourceName);
        }
        if (url == null){
            url = callingClass.getClassLoader().getResource(resourceName);
        }
        return url;
    }
}
