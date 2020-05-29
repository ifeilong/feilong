package com.feilong.lib.digester3.xmlrules;

import static com.feilong.lib.digester3.binder.DigesterLoader.newLoader;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import static java.util.Collections.unmodifiableSet;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xml.sax.InputSource;

import com.feilong.lib.digester3.Digester;
import com.feilong.lib.digester3.binder.AbstractRulesModule;
import com.feilong.lib.digester3.binder.DigesterLoader;

/**
 * {@link com.feilong.lib.digester3.binder.RulesModule} implementation that allows loading rules from
 * XML files.
 *
 * @since 3.0
 */
public abstract class FromXmlRulesModule extends AbstractRulesModule{

    private static final String     DIGESTER_PUBLIC_ID = "-//Apache Commons //DTD digester-rules XML V1.0//EN";

    private static final String     DIGESTER_DTD_PATH  = "digester-rules.dtd";

    private final URL               xmlRulesDtdUrl     = FromXmlRulesModule.class.getResource(DIGESTER_DTD_PATH);

    private final List<InputSource> inputSource        = new ArrayList<InputSource>();

    private final Set<String>       systemIds          = new HashSet<String>();

    private String                  rootPath;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure(){
        if (null == xmlRulesDtdUrl){
            throw new NullPointerException("xmlRulesDtdUrl is null");
        }

        if (!inputSource.isEmpty()){
            throw new IllegalStateException("Re-entry is not allowed.");
        }

        try{
            loadRules();

            XmlRulesModule xmlRulesModule = new XmlRulesModule(new NameSpaceURIRulesBinder(rulesBinder()), getSystemIds(), rootPath);
            DigesterLoader newLoader = newLoader(xmlRulesModule);
            DigesterLoader register = newLoader.register(DIGESTER_PUBLIC_ID, xmlRulesDtdUrl.toString());
            DigesterLoader setXIncludeAware = register.setXIncludeAware(true);
            DigesterLoader setValidating = setXIncludeAware.setValidating(true);
            Digester digester = setValidating.newDigester();

            for (InputSource source : inputSource){
                try{
                    digester.parse(source);
                }catch (Exception e){
                    addError("Impossible to load XML defined in the InputSource '%s': %s", source.getSystemId(), e.getMessage());
                }
            }
        }finally{
            inputSource.clear();
        }
    }

    /**
     *
     */
    protected abstract void loadRules();

    /**
     * Reads the XML rules from the given {@code org.xml.sax.InputSource}.
     *
     * @param inputSource
     *            The {@code org.xml.sax.InputSource} where reading the XML rules from.
     */
    protected final void loadXMLRules(InputSource inputSource){
        if (inputSource == null){
            throw new IllegalArgumentException("Argument 'inputSource' must be not null");
        }

        this.inputSource.add(inputSource);

        String systemId = inputSource.getSystemId();
        if (systemId != null && !systemIds.add(systemId)){
            addError("XML rules file '%s' already bound", systemId);
        }
    }

    /**
     * Opens a new {@code org.xml.sax.InputSource} given a {@code java.io.InputStream}.
     *
     * @param input
     *            The {@code java.io.InputStream} where reading the XML rules from.
     */
    protected final void loadXMLRules(InputStream input){
        if (input == null){
            throw new IllegalArgumentException("Argument 'input' must be not null");
        }

        loadXMLRules(new InputSource(input));
    }

    /**
     * Opens a new {@code org.xml.sax.InputSource} given a {@code java.net.URL}.
     *
     * @param url
     *            The {@code java.net.URL} where reading the XML rules from.
     */
    protected final void loadXMLRules(URL url){
        if (url == null){
            throw new IllegalArgumentException("Argument 'url' must be not null");
        }

        try{
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            InputStream stream = connection.getInputStream();
            InputSource source = new InputSource(stream);
            source.setSystemId(url.toExternalForm());

            loadXMLRules(source);
        }catch (Exception e){
            rulesBinder().addError(e);
        }
    }

    /**
     * Set the root path (will be used when composing modules).
     *
     * @param rootPath
     *            The root path
     */
    protected final void useRootPath(String rootPath){
        this.rootPath = rootPath;
    }

    /**
     * Returns the XML source SystemIds load by this module.
     *
     * @return The XML source SystemIds load by this module
     */
    public final Set<String> getSystemIds(){
        return unmodifiableSet(systemIds);
    }

}
