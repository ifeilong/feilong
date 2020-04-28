/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.office.excel;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class LoxiaSupportSettings.
 */
public class LoxiaSupportSettings{

    /** The Constant log. */
    private static final Logger         LOGGER  = LoggerFactory.getLogger(LoxiaSupportSettings.class);

    //---------------------------------------------------------------

    /** The instance. */
    private static LoxiaSupportSettings instance;

    //---------------------------------------------------------------

    /** The Constant CONFIGS. */
    private static final String[]       CONFIGS = new String[] { "loxiasupport", "loxia/support-default" };

    /** The props. */
    private final List<Properties>      props   = new ArrayList<>();

    //---------------------------------------------------------------

    /**
     * Instantiates a new loxia support settings.
     */
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

    //---------------------------------------------------------------

    /**
     * Gets the single instance of LoxiaSupportSettings.
     *
     * @return single instance of LoxiaSupportSettings
     */
    public static LoxiaSupportSettings getInstance(){
        if (instance == null){
            instance = new LoxiaSupportSettings();
        }
        return instance;
    }

    //---------------------------------------------------------------

    /**
     * 获得.
     *
     * @param name
     *            the name
     * @return the string
     */
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

    /**
     * Gets the resource as stream.
     *
     * @param resourceName
     *            the resource name
     * @param callingClass
     *            the calling class
     * @return the resource as stream
     */
    @Deprecated
    private InputStream getResourceAsStream(String resourceName,Class<?> callingClass){
        URL url = getResource(resourceName, callingClass);
        try{
            return (url != null) ? url.openStream() : null;
        }catch (IOException e){
            return null;
        }
    }

    //---------------------------------------------------------------

    /**
     * Gets the resource.
     *
     * @param resourceName
     *            the resource name
     * @param callingClass
     *            the calling class
     * @return the resource
     */
    @Deprecated
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
