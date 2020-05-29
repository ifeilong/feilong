package com.feilong.lib.digester3;

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

import static java.lang.String.format;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>
 * A <strong>Digester</strong> processes an XML input stream by matching a series of element nesting patterns to execute
 * Rules that have been added prior to the start of parsing.
 * </p>
 * <p>
 * See the <a href="package-summary.html#package_description">Digester Developer Guide</a> for more information.
 * </p>
 * <p>
 * <strong>IMPLEMENTATION NOTE</strong> - A single Digester instance may only be used within the context of a single
 * thread at a time, and a call to <code>parse()</code> must be completed before another can be initiated even from the
 * same thread.
 * </p>
 * <p>
 * A Digester instance should not be used for parsing more than one input document. The problem is that the Digester
 * class has quite a few member variables whose values "evolve" as SAX events are received during a parse. When reusing
 * the Digester instance, all these members must be reset back to their initial states before the second parse begins.
 * The "clear()" method makes a stab at resetting these, but it is actually rather a difficult problem. If you are
 * determined to reuse Digester instances, then at the least you should call the clear() method before each parse, and
 * must call it if the Digester parse terminates due to an exception during a parse.
 * </p>
 * <p>
 * <strong>LEGACY IMPLEMENTATION NOTE</strong> - When using the legacy XML schema support (instead of using the
 * {@link Schema} class), a bug in Xerces 2.0.2 prevents the support of XML schema. You need Xerces 2.1/2.3 and up to
 * make this class work with the legacy XML schema support.
 * </p>
 * <p>
 * This package was inspired by the <code>XmlMapper</code> class that was part of Tomcat 3.0 and 3.1, but is organized
 * somewhat differently.
 * </p>
 */
public class Digester extends DefaultHandler{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Digester.class);

    // --------------------------------------------------------- Constructors

    /**
     * Construct a new Digester, allowing an XMLReader to be passed in. This allows Digester to be used in environments
     * which are unfriendly to JAXP1.1 (such as WebLogic 6.0). Note that if you use this option you have to configure
     * namespace and validation support yourself, as these properties only affect the SAXParser and emtpy constructor.
     *
     * @param reader
     *            The XMLReader used to parse XML streams
     */
    public Digester(XMLReader reader){
        super();
        this.reader = reader;
    }

    // --------------------------------------------------- Instance Variables

    /**
     * The body text of the current element.
     */
    private StringBuilder                        bodyText              = new StringBuilder();

    /**
     * The stack of body text string buffers for surrounding elements.
     */
    private final Stack<StringBuilder>           bodyTexts             = new Stack<StringBuilder>();

    /**
     * Stack whose elements are List objects, each containing a list of Rule objects as returned from Rules.getMatch().
     * As each xml element in the input is entered, the matching rules are pushed onto this stack. After the end tag is
     * reached, the matches are popped again. The depth of is stack is therefore exactly the same as the current
     * "nesting" level of the input xml.
     *
     * @since 1.6
     */
    private final Stack<List<Rule>>              matches               = new Stack<List<Rule>>();

    /**
     * The class loader to use for instantiating application objects. If not specified, the context class loader, or the
     * class loader used to load Digester itself, is used, based on the value of the <code>useContextClassLoader</code>
     * variable.
     */
    private ClassLoader                          classLoader           = null;

    /**
     * Has this Digester been configured yet.
     */
    private boolean                              configured            = false;

    /**
     * The EntityResolver used by the SAX parser. By default it use this class
     */
    private EntityResolver                       entityResolver;

    /**
     * The URLs of entityValidator that have been registered, keyed by the public identifier that corresponds.
     */
    private final HashMap<String, URL>           entityValidator       = new HashMap<String, URL>();

    /**
     * The application-supplied error handler that is notified when parsing warnings, errors, or fatal errors occur.
     */
    private final ErrorHandler                   errorHandler          = null;

    /**
     * The SAXParserFactory that is created the first time we need it.
     */
    private SAXParserFactory                     factory               = null;

    /**
     * The Locator associated with our parser.
     */
    private Locator                              locator               = null;

    /**
     * The current match pattern for nested element processing.
     */
    private String                               match                 = "";

    /**
     * Do we want a "namespace aware" parser.
     */
    private boolean                              namespaceAware        = false;

    /**
     * The executor service to run asynchronous parse method.
     * 
     * @since 3.1
     */
    private ExecutorService                      executorService;

    /**
     * Registered namespaces we are currently processing. The key is the namespace prefix that was declared in the
     * document. The value is an Stack of the namespace URIs this prefix has been mapped to -- the top Stack element is
     * the most current one. (This architecture is required because documents can declare nested uses of the same prefix
     * for different Namespace URIs).
     */
    private final HashMap<String, Stack<String>> namespaces            = new HashMap<String, Stack<String>>();

    /**
     * Do we want a "XInclude aware" parser.
     */
    private boolean                              xincludeAware         = false;

    /**
     * The parameters stack being utilized by CallMethodRule and CallParamRule rules.
     *
     * @since 2.0
     */
    private final Stack<Object[]>                params                = new Stack<Object[]>();

    /**
     * The SAXParser we will use to parse the input stream.
     */
    private SAXParser                            parser                = null;

    /**
     * The public identifier of the DTD we are currently parsing under (if any).
     */
    private String                               publicId              = null;

    /**
     * The XMLReader used to parse digester rules.
     */
    private XMLReader                            reader                = null;

    /**
     * The "root" element of the stack (in other words, the last object that was popped.
     */
    private Object                               root                  = null;

    /**
     * The <code>Rules</code> implementation containing our collection of <code>Rule</code> instances and associated
     * matching policy. If not established before the first rule is added, a default implementation will be provided.
     */
    private Rules                                rules                 = null;

    /**
     * The XML schema to use for validating an XML instance.
     *
     * @since 2.0
     */
    private Schema                               schema                = null;

    /**
     * The object stack being constructed.
     */
    private final Stack<Object>                  stack                 = new Stack<Object>();

    /**
     * Do we want to use the Context ClassLoader when loading classes for instantiating new objects. Default is
     * <code>true</code>.
     */
    private boolean                              useContextClassLoader = true;

    /**
     * Do we want to use a validating parser.
     */
    private boolean                              validating            = false;

    /**
     * The schema language supported. By default, we use this one.
     */
    protected static final String                W3C_XML_SCHEMA        = "http://www.w3.org/2001/XMLSchema";

    /**
     * An optional class that substitutes values in attributes and body text. This may be null and so a null check is
     * always required before use.
     */
    private Substitutor                          substitutor;

    /** Stacks used for interrule communication, indexed by name String */
    private final HashMap<String, Stack<Object>> stacksByName          = new HashMap<String, Stack<Object>>();

    /**
     * If not null, then calls by the parser to this object's characters, startElement, endElement and
     * processingInstruction methods are forwarded to the specified object. This is intended to allow rules to
     * temporarily "take control" of the sax events. In particular, this is used by NodeCreateRule.
     * <p>
     * See setCustomContentHandler.
     */
    private ContentHandler                       customContentHandler  = null;

    // ------------------------------------------------------------- Properties

    /**
     * Return the class loader to be used for instantiating application objects when required. This is determined based
     * upon the following rules:
     * <ul>
     * <li>The class loader set by <code>setClassLoader()</code>, if any</li>
     * <li>The thread context class loader, if it exists and the <code>useContextClassLoader</code> property is set to
     * true</li>
     * <li>The class loader used to load the Digester class itself.
     * </ul>
     *
     * @return the class loader to be used for instantiating application objects.
     */
    public ClassLoader getClassLoader(){
        if (this.classLoader != null){
            return (this.classLoader);
        }
        if (this.useContextClassLoader){
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader != null){
                return (classLoader);
            }
        }
        return (this.getClass().getClassLoader());
    }

    /**
     * Set the class loader to be used for instantiating application objects when required.
     *
     * @param classLoader
     *            The new class loader to use, or <code>null</code> to revert to the standard rules
     */
    public void setClassLoader(ClassLoader classLoader){
        this.classLoader = classLoader;
    }

    /**
     * Return the current depth of the element stack.
     *
     * @return the current depth of the element stack.
     */
    public int getCount(){
        return (stack.size());
    }

    /**
     * Return the SAXParserFactory we will use, creating one if necessary.
     *
     * @return the SAXParserFactory we will use, creating one if necessary.
     */
    public SAXParserFactory getFactory(){
        if (factory == null){
            factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(namespaceAware);
            factory.setXIncludeAware(xincludeAware);
            factory.setValidating(validating);
            factory.setSchema(schema);
        }
        return (factory);
    }

    /**
     * Returns a flag indicating whether the requested feature is supported by the underlying implementation of
     * <code>org.xml.sax.XMLReader</code>. See <a href="http://www.saxproject.org">the saxproject website</a> for
     * information about the standard SAX2 feature flags.
     *
     * @param feature
     *            Name of the feature to inquire about
     * @return true, if the requested feature is supported by the underlying implementation of
     *         <code>org.xml.sax.XMLReader</code>, false otherwise
     * @exception ParserConfigurationException
     *                if a parser configuration error occurs
     * @exception SAXNotRecognizedException
     *                if the property name is not recognized
     * @exception SAXNotSupportedException
     *                if the property name is recognized but not supported
     */
    public boolean getFeature(String feature) throws ParserConfigurationException,SAXNotRecognizedException,SAXNotSupportedException{
        return (getFactory().getFeature(feature));
    }

    /**
     * Sets a flag indicating whether the requested feature is supported by the underlying implementation of
     * <code>org.xml.sax.XMLReader</code>. See <a href="http://www.saxproject.org">the saxproject website</a> for
     * information about the standard SAX2 feature flags. In order to be effective, this method must be called
     * <strong>before</strong> the <code>getParser()</code> method is called for the first time, either directly or
     * indirectly.
     *
     * @param feature
     *            Name of the feature to set the status for
     * @param value
     *            The new value for this feature
     * @exception ParserConfigurationException
     *                if a parser configuration error occurs
     * @exception SAXNotRecognizedException
     *                if the property name is not recognized
     * @exception SAXNotSupportedException
     *                if the property name is recognized but not supported
     */
    public void setFeature(String feature,boolean value)
                    throws ParserConfigurationException,SAXNotRecognizedException,SAXNotSupportedException{
        getFactory().setFeature(feature, value);
    }

    /**
     * Return the current rule match path
     *
     * @return the current rule match path
     */
    public String getMatch(){
        return match;
    }

    /**
     * Return a Stack whose elements are List objects, each containing a list of
     * Rule objects as returned from Rules.getMatch().
     *
     * @return a Stack whose elements are List objects, each containing a list of
     *         Rule objects as returned from Rules.getMatch().
     * @since 3.0
     */
    public Stack<List<Rule>> getMatches(){
        return matches;
    }

    /**
     * Return the "namespace aware" flag for parsers we create.
     *
     * @return the "namespace aware" flag for parsers we create.
     */
    public boolean getNamespaceAware(){
        return (this.namespaceAware);
    }

    /**
     * Set the "namespace aware" flag for parsers we create.
     *
     * @param namespaceAware
     *            The new "namespace aware" flag
     */
    public void setNamespaceAware(boolean namespaceAware){
        this.namespaceAware = namespaceAware;
    }

    /**
     * Return the XInclude-aware flag for parsers we create. XInclude functionality additionally requires
     * namespace-awareness.
     *
     * @return The XInclude-aware flag
     * @see #getNamespaceAware()
     * @since 2.0
     */
    public boolean getXIncludeAware(){
        return (this.xincludeAware);
    }

    /**
     * Set the XInclude-aware flag for parsers we create. This additionally requires namespace-awareness.
     *
     * @param xincludeAware
     *            The new XInclude-aware flag
     * @see #setNamespaceAware(boolean)
     * @since 2.0
     */
    public void setXIncludeAware(boolean xincludeAware){
        this.xincludeAware = xincludeAware;
    }

    /**
     * Set the public id of the current file being parse.
     *
     * @param publicId
     *            the DTD/Schema public's id.
     */
    public void setPublicId(String publicId){
        this.publicId = publicId;
    }

    /**
     * Return the public identifier of the DTD we are currently parsing under, if any.
     *
     * @return the public identifier of the DTD we are currently parsing under, if any.
     */
    public String getPublicId(){
        return (this.publicId);
    }

    /**
     * Return the namespace URI that will be applied to all subsequently added <code>Rule</code> objects.
     *
     * @return the namespace URI that will be applied to all subsequently added <code>Rule</code> objects.
     */
    public String getRuleNamespaceURI(){
        return (getRules().getNamespaceURI());
    }

    /**
     * Set the namespace URI that will be applied to all subsequently added <code>Rule</code> objects.
     *
     * @param ruleNamespaceURI
     *            Namespace URI that must match on all subsequently added rules, or <code>null</code> for
     *            matching regardless of the current namespace URI
     */
    public void setRuleNamespaceURI(String ruleNamespaceURI){
        getRules().setNamespaceURI(ruleNamespaceURI);
    }

    /**
     * Return the SAXParser we will use to parse the input stream.
     *
     * If there is a problem creating the parser, return <code>null</code>.
     *
     * @return the SAXParser we will use to parse the input stream
     */
    public SAXParser getParser(){
        // Return the parser we already created (if any)
        if (parser != null){
            return (parser);
        }

        // Create a new parser
        try{
            parser = getFactory().newSAXParser();
        }catch (Exception e){
            LOGGER.error("Digester.getParser: ", e);
            return (null);
        }

        return (parser);
    }

    /**
     * Return the current value of the specified property for the underlying <code>XMLReader</code> implementation.
     *
     * See <a href="http://www.saxproject.org">the saxproject website</a> for information about the standard SAX2
     * properties.
     *
     * @param property
     *            Property name to be retrieved
     * @return the current value of the specified property for the underlying <code>XMLReader</code> implementation.
     * @exception SAXNotRecognizedException
     *                if the property name is not recognized
     * @exception SAXNotSupportedException
     *                if the property name is recognized but not supported
     */
    public Object getProperty(String property) throws SAXNotRecognizedException,SAXNotSupportedException{
        return (getParser().getProperty(property));
    }

    /**
     * Set the current value of the specified property for the underlying <code>XMLReader</code> implementation. See <a
     * href="http://www.saxproject.org">the saxproject website</a> for information about the standard SAX2 properties.
     *
     * @param property
     *            Property name to be set
     * @param value
     *            Property value to be set
     * @exception SAXNotRecognizedException
     *                if the property name is not recognized
     * @exception SAXNotSupportedException
     *                if the property name is recognized but not supported
     */
    public void setProperty(String property,Object value) throws SAXNotRecognizedException,SAXNotSupportedException{
        getParser().setProperty(property, value);
    }

    /**
     * Return the <code>Rules</code> implementation object containing our rules collection and associated matching
     * policy. If none has been established, a default implementation will be created and returned.
     *
     * @return the <code>Rules</code> implementation object.
     */
    public Rules getRules(){
        if (this.rules == null){
            this.rules = new RulesBase();
            this.rules.setDigester(this);
        }
        return (this.rules);
    }

    /**
     * Set the <code>Rules</code> implementation object containing our rules collection and associated matching policy.
     *
     * @param rules
     *            New Rules implementation
     */
    public void setRules(Rules rules){
        this.rules = rules;
        this.rules.setDigester(this);
    }

    /**
     * Return the XML Schema used when parsing.
     *
     * @return The {@link Schema} instance in use.
     * @since 2.0
     */
    public Schema getXMLSchema(){
        return (this.schema);
    }

    /**
     * Set the XML Schema to be used when parsing.
     *
     * @param schema
     *            The {@link Schema} instance to use.
     * @since 2.0
     */
    public void setXMLSchema(Schema schema){
        this.schema = schema;
    }

    /**
     * Return the boolean as to whether the context ClassLoader should be used.
     *
     * @return true, if the context ClassLoader should be used, false otherwise.
     */
    public boolean getUseContextClassLoader(){
        return useContextClassLoader;
    }

    /**
     * Determine whether to use the Context ClassLoader (the one found by calling
     * <code>Thread.currentThread().getContextClassLoader()</code>) to resolve/load classes that are defined in various
     * rules. If not using Context ClassLoader, then the class-loading defaults to using the calling-class' ClassLoader.
     *
     * @param use
     *            determines whether to use Context ClassLoader.
     */
    public void setUseContextClassLoader(boolean use){
        useContextClassLoader = use;
    }

    /**
     * Return the validating parser flag.
     *
     * @return the validating parser flag.
     */
    public boolean getValidating(){
        return (this.validating);
    }

    /**
     * Set the validating parser flag. This must be called before <code>parse()</code> is called the first time.
     *
     * @param validating
     *            The new validating parser flag.
     */
    public void setValidating(boolean validating){
        this.validating = validating;
    }

    /**
     * Return the XMLReader to be used for parsing the input document.
     *
     * FIXME: there is a bug in JAXP/XERCES that prevent the use of a parser that contains a schema with a DTD.
     *
     * @return the XMLReader to be used for parsing the input document.
     * @exception SAXException
     *                if no XMLReader can be instantiated
     */
    public XMLReader getXMLReader() throws SAXException{
        if (reader == null){
            reader = getParser().getXMLReader();
        }

        reader.setDTDHandler(this);
        reader.setContentHandler(this);

        if (entityResolver == null){
            reader.setEntityResolver(this);
        }else{
            reader.setEntityResolver(entityResolver);
        }

        reader.setErrorHandler(this);
        return reader;
    }

    /**
     * Gets the <code>Substitutor</code> used to convert attributes and body text.
     *
     * @return the <code>Substitutor</code> used to convert attributes and body text,
     *         null if not substitutions are to be performed.
     */
    public Substitutor getSubstitutor(){
        return substitutor;
    }

    /**
     * Sets the <code>Substitutor</code> to be used to convert attributes and body text.
     *
     * @param substitutor
     *            the Substitutor to be used to convert attributes and body text or null if not substitution of
     *            these values is to be performed.
     */
    public void setSubstitutor(Substitutor substitutor){
        this.substitutor = substitutor;
    }

    /**
     * returns the custom SAX ContentHandler where events are redirected.
     *
     * @return the custom SAX ContentHandler where events are redirected.
     * @since 1.7
     */
    public ContentHandler getCustomContentHandler(){
        return customContentHandler;
    }

    /**
     * Redirects (or cancels redirecting) of SAX ContentHandler events to an external object.
     * <p>
     * When this object's customContentHandler is non-null, any SAX events received from the parser will simply be
     * passed on to the specified object instead of this object handling them. This allows Rule classes to take control
     * of the SAX event stream for a while in order to do custom processing. Such a rule should save the old value
     * before setting a new one, and restore the old value in order to resume normal digester processing.
     * <p>
     * An example of a Rule which needs this feature is NodeCreateRule.
     * <p>
     * Note that saving the old value is probably not needed as it should always be null; a custom rule that wants to
     * take control could only have been called when there was no custom content handler. But it seems cleaner to
     * properly save/restore the value and maybe some day this will come in useful.
     * <p>
     * Note also that this is not quite equivalent to
     *
     * <pre>
     * digester.getXMLReader().setContentHandler(handler)
     * </pre>
     *
     * for these reasons:
     * <ul>
     * <li>Some xml parsers don't like having setContentHandler called after parsing has started. The Aelfred parser is
     * one example.</li>
     * <li>Directing the events via the Digester object potentially allows us to log information about those SAX events
     * at the digester level.</li>
     * </ul>
     *
     * @param handler
     *            the custom SAX ContentHandler where events are redirected.
     * @since 1.7
     */
    public void setCustomContentHandler(ContentHandler handler){
        customContentHandler = handler;
    }

    /**
     * Get the most current namespaces for all prefixes.
     *
     * @return Map A map with namespace prefixes as keys and most current namespace URIs for the corresponding prefixes
     *         as values
     * @since 1.8
     */
    public Map<String, String> getCurrentNamespaces(){
        if (!namespaceAware){
            LOGGER.warn("Digester is not namespace aware");
        }
        Map<String, String> currentNamespaces = new HashMap<String, String>();
        for (Map.Entry<String, Stack<String>> nsEntry : namespaces.entrySet()){
            try{
                currentNamespaces.put(nsEntry.getKey(), nsEntry.getValue().peek());
            }catch (RuntimeException e){
                // rethrow, after logging
                LOGGER.error(e.getMessage(), e);
                throw e;
            }
        }
        return currentNamespaces;
    }

    /**
     * Sets the executor service to run asynchronous parse method.
     *
     * @param executorService
     *            the executor service to run asynchronous parse method
     * @since 3.1
     */
    public void setExecutorService(ExecutorService executorService){
        this.executorService = executorService;
    }

    // ------------------------------------------------- ContentHandler Methods

    /**
     * {@inheritDoc}
     */
    @Override
    public void characters(char buffer[],int start,int length) throws SAXException{
        if (customContentHandler != null){
            // forward calls instead of handling them here
            customContentHandler.characters(buffer, start, length);
            return;
        }

        LOGGER.trace("characters(" + new String(buffer, start, length) + ")");

        bodyText.append(buffer, start, length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endDocument() throws SAXException{
        if (LOGGER.isDebugEnabled()){
            if (getCount() > 1){
                LOGGER.debug("endDocument():  " + getCount() + " elements left");
            }else{
                LOGGER.debug("endDocument()");
            }
        }

        // Fire "finish" events for all defined rules
        for (Rule rule : getRules().rules()){
            try{
                rule.finish();
            }catch (Exception e){
                LOGGER.error("Finish event threw exception", e);
                throw createSAXException(e);
            }catch (Error e){
                LOGGER.error("Finish event threw error", e);
                throw e;
            }
        }

        // Perform final cleanup
        clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endElement(String namespaceURI,String localName,String qName) throws SAXException{
        if (customContentHandler != null){
            // forward calls instead of handling them here
            customContentHandler.endElement(namespaceURI, localName, qName);
            return;
        }

        LOGGER.trace("endElement({},{},{}),match:[{}],bodyText:[{}]", namespaceURI, localName, qName, match, bodyText);

        // the actual element name is either in localName or qName, depending
        // on whether the parser is namespace aware
        String name = localName;
        if ((name == null) || (name.length() < 1)){
            name = qName;
        }

        // Fire "body" events for all relevant rules
        List<Rule> rules = matches.pop();
        if ((rules != null) && (rules.size() > 0)){
            String bodyText = this.bodyText.toString();
            Substitutor substitutor = getSubstitutor();
            if (substitutor != null){
                bodyText = substitutor.substitute(bodyText);
            }
            for (int i = 0; i < rules.size(); i++){
                try{
                    Rule rule = rules.get(i);
                    LOGGER.debug("  Fire body() for " + rule);
                    rule.body(namespaceURI, name, bodyText);
                }catch (Exception e){
                    LOGGER.error("Body event threw exception", e);
                    throw createSAXException(e);
                }catch (Error e){
                    LOGGER.error("Body event threw error", e);
                    throw e;
                }
            }
        }else{
            LOGGER.debug("  No rules found matching '" + match + "'.");
        }

        // Recover the body text from the surrounding element
        bodyText = bodyTexts.pop();
        LOGGER.trace("  Popping body text '" + bodyText.toString() + "'");

        // Fire "end" events for all relevant rules in reverse order
        if (rules != null){
            for (int i = 0; i < rules.size(); i++){
                int j = (rules.size() - i) - 1;
                try{
                    Rule rule = rules.get(j);
                    LOGGER.debug("  Fire end() for " + rule);
                    rule.end(namespaceURI, name);
                }catch (Exception e){
                    LOGGER.error("End event threw exception", e);
                    throw createSAXException(e);
                }catch (Error e){
                    LOGGER.error("End event threw error", e);
                    throw e;
                }
            }
        }

        // Recover the previous match expression
        int slash = match.lastIndexOf('/');
        if (slash >= 0){
            match = match.substring(0, slash);
        }else{
            match = "";
        }
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException{
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("endPrefixMapping(" + prefix + ")");
        }

        // Deregister this prefix mapping
        Stack<String> stack = namespaces.get(prefix);
        if (stack == null){
            return;
        }
        try{
            stack.pop();
            if (stack.empty()){
                namespaces.remove(prefix);
            }
        }catch (EmptyStackException e){
            throw createSAXException("endPrefixMapping popped too many times");
        }
    }

    @Override
    public void ignorableWhitespace(char buffer[],int start,int len) throws SAXException{
        LOGGER.debug("ignorableWhitespace({})", new String(buffer, start, len));
        // No processing required
    }

    @Override
    public void processingInstruction(String target,String data) throws SAXException{
        if (customContentHandler != null){
            // forward calls instead of handling them here
            customContentHandler.processingInstruction(target, data);
            return;
        }

        LOGGER.debug("processingInstruction('" + target + "','" + data + "')");

        // No processing is required
    }

    @Override
    public void setDocumentLocator(Locator locator){
        LOGGER.debug("setDocumentLocator({})", locator);

        this.locator = locator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void skippedEntity(String name) throws SAXException{
        LOGGER.debug("skippedEntity(" + name + ")");

        // No processing required
    }

    @Override
    public void startDocument() throws SAXException{
        LOGGER.debug("startDocument()");

        // ensure that the digester is properly configured, as
        // the digester could be used as a SAX ContentHandler
        // rather than via the parse() methods.
        configure();
    }

    @Override
    public void startElement(String namespaceURI,String localName,String qName,Attributes list) throws SAXException{
        if (customContentHandler != null){
            // forward calls instead of handling them here
            customContentHandler.startElement(namespaceURI, localName, qName, list);
            return;
        }

        LOGGER.debug("startElement(" + namespaceURI + "," + localName + "," + qName + ")");

        // Save the body text accumulated for our surrounding element
        bodyTexts.push(bodyText);
        LOGGER.trace("  Pushing body text '" + bodyText.toString() + "'");
        bodyText = new StringBuilder();

        // the actual element name is either in localName or qName, depending
        // on whether the parser is namespace aware
        String name = localName;
        if ((name == null) || (name.length() < 1)){
            name = qName;
        }

        // Compute the current matching rule
        StringBuilder sb = new StringBuilder(match);
        if (match.length() > 0){
            sb.append('/');
        }
        sb.append(name);
        match = sb.toString();
        LOGGER.debug("  New match='" + match + "'");

        // Fire "begin" events for all relevant rules
        List<Rule> rules = getRules().match(namespaceURI, match, localName, list);
        matches.push(rules);
        if ((rules != null) && (rules.size() > 0)){
            Substitutor substitutor = getSubstitutor();
            if (substitutor != null){
                list = substitutor.substitute(list);
            }
            for (int i = 0; i < rules.size(); i++){
                try{
                    Rule rule = rules.get(i);
                    LOGGER.debug("  Fire begin() for " + rule);
                    rule.begin(namespaceURI, name, list);
                }catch (Exception e){
                    LOGGER.error("Begin event threw exception", e);
                    throw createSAXException(e);
                }catch (Error e){
                    LOGGER.error("Begin event threw error", e);
                    throw e;
                }
            }
        }else{
            LOGGER.debug("  No rules found matching '" + match + "'.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startPrefixMapping(String prefix,String namespaceURI) throws SAXException{
        LOGGER.debug("startPrefixMapping(" + prefix + "," + namespaceURI + ")");

        // Register this prefix mapping
        Stack<String> stack = namespaces.get(prefix);
        if (stack == null){
            stack = new Stack<String>();
            namespaces.put(prefix, stack);
        }
        stack.push(namespaceURI);
    }

    // ----------------------------------------------------- DTDHandler Methods

    /**
     * {@inheritDoc}
     */
    @Override
    public void notationDecl(String name,String publicId,String systemId){
        LOGGER.debug("notationDecl(" + name + "," + publicId + "," + systemId + ")");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unparsedEntityDecl(String name,String publicId,String systemId,String notation){
        LOGGER.debug("unparsedEntityDecl(" + name + "," + publicId + "," + systemId + "," + notation + ")");
    }

    // ----------------------------------------------- EntityResolver Methods

    /**
     * {@inheritDoc}
     */
    @Override
    public InputSource resolveEntity(String publicId,String systemId) throws SAXException{
        LOGGER.debug("resolveEntity('" + publicId + "', '" + systemId + "')");

        if (publicId != null){
            this.publicId = publicId;
        }

        // Has this system identifier been registered?
        URL entityURL = null;
        if (publicId != null){
            entityURL = entityValidator.get(publicId);
        }

        // Redirect the schema location to a local destination
        if (entityURL == null && systemId != null){
            entityURL = entityValidator.get(systemId);
        }

        if (entityURL == null){
            if (systemId == null){
                // cannot resolve
                if (LOGGER.isDebugEnabled()){
                    LOGGER.debug(" Cannot resolve null entity, returning null InputSource");
                }
                return (null);

            }
            // try to resolve using system ID
            if (LOGGER.isDebugEnabled()){
                LOGGER.debug(" Trying to resolve using system ID '" + systemId + "'");
            }
            try{
                entityURL = new URL(systemId);
            }catch (MalformedURLException e){
                throw new IllegalArgumentException("Malformed URL '" + systemId + "' : " + e.getMessage());
            }
        }

        // Return an input source to our alternative URL
        LOGGER.debug(" Resolving to alternate DTD '" + entityURL + "'");

        try{
            return createInputSourceFromURL(entityURL);
        }catch (Exception e){
            throw createSAXException(e);
        }
    }

    // ------------------------------------------------- ErrorHandler Methods

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(SAXParseException exception) throws SAXException{
        LOGGER.error(
                        "Parse Error at line " + exception.getLineNumber() + " column " + exception.getColumnNumber() + ": "
                                        + exception.getMessage(),
                        exception);
        if (errorHandler != null){
            errorHandler.error(exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fatalError(SAXParseException exception) throws SAXException{
        LOGGER.error(
                        "Parse Fatal Error at line " + exception.getLineNumber() + " column " + exception.getColumnNumber() + ": "
                                        + exception.getMessage(),
                        exception);
        if (errorHandler != null){
            errorHandler.fatalError(exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warning(SAXParseException exception) throws SAXException{
        if (errorHandler != null){
            LOGGER.warn(
                            "Parse Warning Error at line " + exception.getLineNumber() + " column " + exception.getColumnNumber() + ": "
                                            + exception.getMessage(),
                            exception);

            errorHandler.warning(exception);
        }
    }

    // ------------------------------------------------------- Public Methods

    /**
     * Parse the content of the specified input source using this Digester. Returns the root element from the object
     * stack (if any).
     *
     * @param <T>
     *            the type used to auto-cast the returned object to the assigned variable type
     * @param input
     *            Input source containing the XML data to be parsed
     * @return the root element from the object stack (if any)
     * @exception IOException
     *                if an input/output error occurs
     * @exception SAXException
     *                if a parsing exception occurs
     */
    public <T> T parse(InputSource input) throws IOException,SAXException{
        if (input == null){
            throw new IllegalArgumentException("InputSource to parse is null");
        }

        configure();

        String systemId = input.getSystemId();
        if (systemId == null){
            systemId = "(already loaded from stream)";
        }

        try{
            getXMLReader().parse(input);
        }catch (IOException e){
            LOGGER.error(format("An error occurred while reading stream from '%s', see nested exceptions", systemId), e);
            throw e;
        }catch (SAXException e){
            LOGGER.error(format("An error occurred while parsing XML from '%s', see nested exceptions", systemId), e);
            throw e;
        }
        cleanup();
        return this.<T> getRoot();
    }

    /**
     * Parse the content of the specified input stream using this Digester. Returns the root element from the object
     * stack (if any).
     *
     * @param <T>
     *            the type used to auto-cast the returned object to the assigned variable type
     * @param input
     *            Input stream containing the XML data to be parsed
     * @return the root element from the object stack (if any)
     * @exception IOException
     *                if an input/output error occurs
     * @exception SAXException
     *                if a parsing exception occurs
     */
    public <T> T parse(InputStream input) throws IOException,SAXException{
        if (input == null){
            throw new IllegalArgumentException("InputStream to parse is null");
        }

        return (this.<T> parse(new InputSource(input)));
    }

    /**
     * Creates a Callable instance that parse the content of the specified reader using this Digester.
     *
     * @param <T>
     *            The result type returned by the returned Future's {@code get} method
     * @param input
     *            Input stream containing the XML data to be parsed
     * @return a Future that can be used to track when the parse has been fully processed.
     * @see Digester#parse(InputStream)
     * @since 3.1
     */
    public <T> Future<T> asyncParse(final InputStream input){
        return asyncParse(new Callable<T>(){

            @Override
            public T call() throws Exception{
                return Digester.this.<T> parse(input);
            }

        });
    }

    /**
     * Execute the parse in async mode.
     *
     * @param <T>
     *            the type used to auto-cast the returned object to the assigned variable type
     * @param callable
     * @return a Future that can be used to track when the parse has been fully processed.
     * @since 3.1
     */
    private <T> Future<T> asyncParse(Callable<T> callable){
        if (executorService == null){
            throw new IllegalStateException("ExecutorService not set");
        }

        return executorService.submit(callable);
    }

    /**
     * Convenience method that registers DTD URLs for the specified public identifiers.
     *
     * @param entityValidator
     *            The URLs of entityValidator that have been registered, keyed by the public
     *            identifier that corresponds.
     * @since 3.0
     */
    public void registerAll(Map<String, URL> entityValidator){
        this.entityValidator.putAll(entityValidator);
    }

    /**
     * <p>
     * <code>List</code> of <code>InputSource</code> instances created by a <code>createInputSourceFromURL()</code>
     * method call. These represent open input streams that need to be closed to avoid resource leaks, as well as
     * potentially locked JAR files on Windows.
     * </p>
     */
    protected List<InputSource> inputSources = new ArrayList<InputSource>(5);

    /**
     * Given a URL, return an InputSource that reads from that URL.
     * <p>
     * Ideally this function would not be needed and code could just use <code>new InputSource(entityURL)</code>.
     * Unfortunately it appears that when the entityURL points to a file within a jar archive a caching mechanism inside
     * the InputSource implementation causes a file-handle to the jar file to remain open. On Windows systems this then
     * causes the jar archive file to be locked on disk ("in use") which makes it impossible to delete the jar file -
     * and that really stuffs up "undeploy" in webapps in particular.
     * <p>
     * In JDK1.4 and later, Apache XercesJ is used as the xml parser. The InputSource object provided is converted into
     * an XMLInputSource, and eventually passed to an instance of XMLDocumentScannerImpl to specify the source data to
     * be converted into tokens for the rest of the XMLReader code to handle. XMLDocumentScannerImpl calls
     * fEntityManager.startDocumentEntity(source), where fEntityManager is declared in ancestor class XMLScanner to be
     * an XMLEntityManager. In that class, if the input source stream is null, then:
     *
     * <pre>
     * URL location = new URL(expandedSystemId);
     * URLConnection connect = location.openConnection();
     * if (connect instanceof HttpURLConnection){
     *     setHttpProperties(connect, xmlInputSource);
     * }
     * stream = connect.getInputStream();
     * </pre>
     *
     * This method pretty much duplicates the standard behaviour, except that it calls URLConnection.setUseCaches(false)
     * before opening the connection.
     *
     * @param url
     *            The URL has to be read
     * @return The InputSource that reads from the input URL
     * @throws IOException
     *             if any error occurs while reading the input URL
     * @since 1.8
     */
    public InputSource createInputSourceFromURL(URL url) throws IOException{
        URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        InputStream stream = connection.getInputStream();
        InputSource source = new InputSource(stream);
        source.setSystemId(url.toExternalForm());
        inputSources.add(source);
        return source;
    }

    /**
     * <p>
     * Convenience method that creates an <code>InputSource</code> from the string version of a URL.
     * </p>
     *
     * @param url
     *            URL for which to create an <code>InputSource</code>
     * @return The InputSource that reads from the input URL
     * @throws IOException
     *             if any error occurs while reading the input URL
     * @since 1.8
     */
    public InputSource createInputSourceFromURL(String url) throws IOException{
        return createInputSourceFromURL(new URL(url));
    }

    // --------------------------------------------------------- Rule Methods

    /**
     * <p>
     * Register a new Rule matching the specified pattern. This method sets the <code>Digester</code> property on the
     * rule.
     * </p>
     *
     * @param pattern
     *            Element matching pattern
     * @param rule
     *            Rule to be registered
     */
    public void addRule(String pattern,Rule rule){
        rule.setDigester(this);
        getRules().add(pattern, rule);
    }

    /**
     * Add a "factory create" rule for the specified parameters.
     *
     * @param pattern
     *            Element matching pattern
     * @param clazz
     *            Java class of the object creation factory class
     * @param attributeName
     *            Attribute name which, if present, overrides the value specified by <code>className</code>
     * @param ignoreCreateExceptions
     *            when <code>true</code> any exceptions thrown during object creation will be
     *            ignored.
     * @see FactoryCreateRule
     */
    public void addFactoryCreate(
                    String pattern,
                    Class<? extends ObjectCreationFactory<?>> clazz,
                    String attributeName,
                    boolean ignoreCreateExceptions){
        addRule(pattern, new FactoryCreateRule(clazz, attributeName, ignoreCreateExceptions));
    }

    /**
     * Add a "set properties" rule for the specified parameters.
     *
     * @param pattern
     *            Element matching pattern
     * @see SetPropertiesRule
     */
    public void addSetProperties(String pattern){
        addRule(pattern, new SetPropertiesRule());
    }

    // --------------------------------------------------- Object Stack Methods

    /**
     * Clear the current contents of the default object stack, the param stack, all named stacks, and other internal
     * variables.
     * <p>
     * Calling this method <i>might</i> allow another document of the same type to be correctly parsed. However this
     * method was not intended for this purpose (just to tidy up memory usage). In general, a separate Digester object
     * should be created for each document to be parsed.
     * <p>
     * Note that this method is called automatically after a document has been successfully parsed by a Digester
     * instance. However it is not invoked automatically when a parse fails, so when reusing a Digester instance (which
     * is not recommended) this method <i>must</i> be called manually after a parse failure.
     */
    public void clear(){
        match = "";
        bodyTexts.clear();
        params.clear();
        publicId = null;
        stack.clear();
        stacksByName.clear();
        customContentHandler = null;
    }

    /**
     * Return the top object on the stack without removing it.
     *
     * If there are no objects on the stack, return <code>null</code>.
     *
     * @param <T>
     *            the type used to auto-cast the returned object to the assigned variable type
     * @return the top object on the stack without removing it.
     */
    public <T> T peek(){
        try{
            return this.<T> npeSafeCast(stack.peek());
        }catch (EmptyStackException e){
            LOGGER.warn("Empty stack (returning null)");
            return (null);
        }
    }

    /**
     * Return the n'th object down the stack, where 0 is the top element and [getCount()-1] is the bottom element. If
     * the specified index is out of range, return <code>null</code>.
     *
     * @param <T>
     *            the type used to auto-cast the returned object to the assigned variable type
     * @param n
     *            Index of the desired element, where 0 is the top of the stack, 1 is the next element down, and so on.
     * @return the n'th object down the stack
     */
    public <T> T peek(int n){
        int index = (stack.size() - 1) - n;
        if (index < 0){
            LOGGER.warn("Empty stack (returning null)");
            return (null);
        }
        try{
            return this.<T> npeSafeCast(stack.get(index));
        }catch (EmptyStackException e){
            LOGGER.warn("Empty stack (returning null)");
            return (null);
        }
    }

    /**
     * Pop the top object off of the stack, and return it. If there are no objects on the stack, return
     * <code>null</code>.
     *
     * @param <T>
     *            the type used to auto-cast the returned object to the assigned variable type
     * @return the top object popped off of the stack
     */
    public <T> T pop(){
        try{
            T popped = this.<T> npeSafeCast(stack.pop());
            return popped;
        }catch (EmptyStackException e){
            LOGGER.warn("Empty stack (returning null)");
            return (null);
        }
    }

    /**
     * Push a new object onto the top of the object stack.
     *
     * @param <T>
     *            any type of the pushed object
     * @param object
     *            The new object
     */
    public <T> void push(T object){
        if (stack.size() == 0){
            root = object;
        }
        stack.push(object);
    }

    /**
     * Pushes the given object onto the stack with the given name. If no stack already exists with the given name then
     * one will be created.
     *
     * @param <T>
     *            any type of the pushed object
     * @param stackName
     *            the name of the stack onto which the object should be pushed
     * @param value
     *            the Object to be pushed onto the named stack.
     * @since 1.6
     */
    public <T> void push(String stackName,T value){

        Stack<Object> namedStack = stacksByName.get(stackName);
        if (namedStack == null){
            namedStack = new Stack<Object>();
            stacksByName.put(stackName, namedStack);
        }
        namedStack.push(value);
    }

    /**
     * <p>
     * Pops (gets and removes) the top object from the stack with the given name.
     * </p>
     * <p>
     * <strong>Note:</strong> a stack is considered empty if no objects have been pushed onto it yet.
     * </p>
     *
     * @param <T>
     *            the type used to auto-cast the returned object to the assigned variable type
     * @param stackName
     *            the name of the stack from which the top value is to be popped.
     * @return the top <code>Object</code> on the stack or or null if the stack is either empty or has not been created
     *         yet
     * @since 1.6
     */
    public <T> T pop(String stackName){
        T result = null;
        Stack<Object> namedStack = stacksByName.get(stackName);
        if (namedStack == null){
            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("Stack '" + stackName + "' is empty");
            }
            throw new EmptyStackException();
        }

        result = this.<T> npeSafeCast(namedStack.pop());
        return result;
    }

    /**
     * <p>
     * Gets the top object from the stack with the given name. This method does not remove the object from the stack.
     * </p>
     * <p>
     * <strong>Note:</strong> a stack is considered empty if no objects have been pushed onto it yet.
     * </p>
     *
     * @param <T>
     *            the type used to auto-cast the returned object to the assigned variable type
     * @param stackName
     *            the name of the stack to be peeked
     * @return the top <code>Object</code> on the stack or null if the stack is either empty or has not been created yet
     * @since 1.6
     */
    public <T> T peek(String stackName){
        return this.<T> npeSafeCast(peek(stackName, 0));
    }

    /**
     * <p>
     * Gets the top object from the stack with the given name. This method does not remove the object from the stack.
     * </p>
     * <p>
     * <strong>Note:</strong> a stack is considered empty if no objects have been pushed onto it yet.
     * </p>
     *
     * @param <T>
     *            the type used to auto-cast the returned object to the assigned variable type
     * @param stackName
     *            the name of the stack to be peeked
     * @param n
     *            Index of the desired element, where 0 is the top of the stack, 1 is the next element down, and so on.
     * @return the specified <code>Object</code> on the stack.
     * @since 1.6
     */
    public <T> T peek(String stackName,int n){
        T result = null;
        Stack<Object> namedStack = stacksByName.get(stackName);
        if (namedStack == null){
            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("Stack '" + stackName + "' is empty");
            }
            throw new EmptyStackException();
        }

        int index = (namedStack.size() - 1) - n;
        if (index < 0){
            throw new EmptyStackException();
        }
        result = this.<T> npeSafeCast(namedStack.get(index));

        return result;
    }

    /**
     * <p>
     * Is the stack with the given name empty?
     * </p>
     * <p>
     * <strong>Note:</strong> a stack is considered empty if no objects have been pushed onto it yet.
     * </p>
     *
     * @param stackName
     *            the name of the stack whose emptiness should be evaluated
     * @return true if the given stack if empty
     * @since 1.6
     */
    public boolean isEmpty(String stackName){
        boolean result = true;
        Stack<Object> namedStack = stacksByName.get(stackName);
        if (namedStack != null){
            result = namedStack.isEmpty();
        }
        return result;
    }

    /**
     * Returns the root element of the tree of objects created as a result of applying the rule objects to the input
     * XML.
     * <p>
     * If the digester stack was "primed" by explicitly pushing a root object onto the stack before parsing started,
     * then that root object is returned here.
     * <p>
     * Alternatively, if a Rule which creates an object (eg ObjectCreateRule) matched the root element of the xml, then
     * the object created will be returned here.
     * <p>
     * In other cases, the object most recently pushed onto an empty digester stack is returned. This would be a most
     * unusual use of digester, however; one of the previous configurations is much more likely.
     * <p>
     * Note that when using one of the Digester.parse methods, the return value from the parse method is exactly the
     * same as the return value from this method. However when the Digester is being used as a SAXContentHandler, no
     * such return value is available; in this case, this method allows you to access the root object that has been
     * created after parsing has completed.
     *
     * @param <T>
     *            the type used to auto-cast the returned object to the assigned variable type
     * @return the root object that has been created after parsing or null if the digester has not parsed any XML yet.
     */
    public <T> T getRoot(){
        return this.<T> npeSafeCast(root);
    }

    /**
     * This method allows the "root" variable to be reset to null.
     * <p>
     * It is not considered safe for a digester instance to be reused to parse multiple xml documents. However if you
     * are determined to do so, then you should call both clear() and resetRoot() before each parse.
     *
     * @since 1.7
     */
    public void resetRoot(){
        root = null;
    }

    // ------------------------------------------------ Parameter Stack Methods

    // ------------------------------------------------------ Protected Methods

    /**
     * <p>
     * Clean up allocated resources after parsing is complete. The default method closes input streams that have been
     * created by Digester itself. If you override this method in a subclass, be sure to call
     * <code>super.cleanup()</code> to invoke this logic.
     * </p>
     *
     * @since 1.8
     */
    protected void cleanup(){
        // If we created any InputSource objects in this instance,
        // they each have an input stream that should be closed
        for (InputSource source : inputSources){
            try{
                source.getByteStream().close();
            }catch (IOException e){
                // Fall through so we get them all
                if (LOGGER.isWarnEnabled()){
                    LOGGER.warn(format("An error occurred while closing resource %s (%s)", source.getPublicId(), source.getSystemId()), e);
                }
            }
        }
        inputSources.clear();
    }

    /**
     * <p>
     * Provide a hook for lazy configuration of this <code>Digester</code> instance. The default implementation does
     * nothing, but subclasses can override as needed.
     * </p>
     * <p>
     * <strong>Note</strong> This method may be called more than once. Once only initialization code should be placed in
     * {@link #initialize} or the code should take responsibility by checking and setting the {@link #configured} flag.
     * </p>
     */
    protected void configure(){
        // Do not configure more than once
        if (configured){
            return;
        }

        // Perform lazy configuration as needed
        initialize(); // call hook method for subclasses that want to be initialized once only
        // Nothing else required by default

        // Set the configuration flag to avoid repeating
        configured = true;
    }

    /**
     * Checks the Digester instance has been configured.
     *
     * @return true, if the Digester instance has been configured, false otherwise
     * @since 3.0
     */
    public boolean isConfigured(){
        return configured;
    }

    /**
     * <p>
     * Provides a hook for lazy initialization of this <code>Digester</code> instance. The default implementation does
     * nothing, but subclasses can override as needed. Digester (by default) only calls this method once.
     * </p>
     * <p>
     * <strong>Note</strong> This method will be called by {@link #configure} only when the {@link #configured} flag is
     * false. Subclasses that override <code>configure</code> or who set <code>configured</code> may find that this
     * method may be called more than once.
     * </p>
     *
     * @since 1.6
     */
    protected void initialize(){
        // Perform lazy initialization as needed
        // Nothing required by default
    }

    // -------------------------------------------------------- Package Methods

    /**
     * Return the set of DTD URL registrations, keyed by public identifier. NOTE: the returned map is in read-only mode.
     *
     * @return the read-only Map of DTD URL registrations.
     */
    Map<String, URL> getRegistrations(){
        return Collections.unmodifiableMap(entityValidator);
    }

    /**
     * <p>
     * Return the top object on the parameters stack without removing it. If there are no objects on the stack, return
     * <code>null</code>.
     * </p>
     * <p>
     * The parameters stack is used to store <code>CallMethodRule</code> parameters. See {@link #params}.
     * </p>
     *
     * @return the top object on the parameters stack without removing it.
     */
    public Object[] peekParams(){
        try{
            return (params.peek());
        }catch (EmptyStackException e){
            LOGGER.warn("Empty stack (returning null)");
            return (null);
        }
    }

    /**
     * <p>
     * Return the n'th object down the parameters stack, where 0 is the top element and [getCount()-1] is the bottom
     * element. If the specified index is out of range, return <code>null</code>.
     * </p>
     * <p>
     * The parameters stack is used to store <code>CallMethodRule</code> parameters. See {@link #params}.
     * </p>
     *
     * @param n
     *            Index of the desired element, where 0 is the top of the stack, 1 is the next element down, and so on.
     * @return the n'th object down the parameters stack
     */
    public Object[] peekParams(int n){
        int index = (params.size() - 1) - n;
        if (index < 0){
            LOGGER.warn("Empty stack (returning null)");
            return (null);
        }
        try{
            return (params.get(index));
        }catch (EmptyStackException e){
            LOGGER.warn("Empty stack (returning null)");
            return (null);
        }
    }

    /**
     * <p>
     * Pop the top object off of the parameters stack, and return it. If there are no objects on the stack, return
     * <code>null</code>.
     * </p>
     * <p>
     * The parameters stack is used to store <code>CallMethodRule</code> parameters. See {@link #params}.
     * </p>
     *
     * @return the top object popped off of the parameters stack
     */
    public Object[] popParams(){
        try{
            if (LOGGER.isTraceEnabled()){
                LOGGER.trace("Popping params");
            }
            return (params.pop());
        }catch (EmptyStackException e){
            LOGGER.warn("Empty stack (returning null)");
            return (null);
        }
    }

    /**
     * <p>
     * Push a new object onto the top of the parameters stack.
     * </p>
     * <p>
     * The parameters stack is used to store <code>CallMethodRule</code> parameters. See {@link #params}.
     * </p>
     *
     * @param object
     *            The new object
     */
    public void pushParams(Object...object){
        if (LOGGER.isTraceEnabled()){
            LOGGER.trace("Pushing params");
        }
        params.push(object);
    }

    /**
     * Create a SAX exception which also understands about the location in the digester file where the exception occurs
     *
     * @param message
     *            the custom SAX exception message
     * @param e
     *            the exception cause
     * @return the new SAX exception
     */
    public SAXException createSAXException(String message,Exception e){
        if ((e != null) && (e instanceof InvocationTargetException)){
            Throwable t = ((InvocationTargetException) e).getTargetException();
            if ((t != null) && (t instanceof Exception)){
                e = (Exception) t;
            }
        }
        if (locator != null){
            String error = "Error at line " + locator.getLineNumber() + " char " + locator.getColumnNumber() + ": " + message;
            if (e != null){
                return new SAXParseException(error, locator, e);
            }
            return new SAXParseException(error, locator);
        }
        LOGGER.error("No Locator!");
        if (e != null){
            return new SAXException(message, e);
        }
        return new SAXException(message);
    }

    /**
     * Create a SAX exception which also understands about the location in the digester file where the exception occurs
     *
     * @param e
     *            the exception cause
     * @return the new SAX exception
     */
    public SAXException createSAXException(Exception e){
        if (e instanceof InvocationTargetException){
            Throwable t = ((InvocationTargetException) e).getTargetException();
            if ((t != null) && (t instanceof Exception)){
                e = (Exception) t;
            }
        }
        return createSAXException(e.getMessage(), e);
    }

    /**
     * Create a SAX exception which also understands about the location in the digester file where the exception occurs
     *
     * @param message
     *            the custom SAX exception message
     * @return the new SAX exception
     */
    public SAXException createSAXException(String message){
        return createSAXException(message, null);
    }

    /**
     * Helps casting the input object to given type, avoiding NPEs.
     *
     * @since 3.0
     * @param <T>
     *            the type the input object has to be cast.
     * @param obj
     *            the object has to be cast.
     * @return the casted object, if input object is not null, null otherwise.
     */
    private <T> T npeSafeCast(Object obj){
        if (obj == null){
            return null;
        }

        @SuppressWarnings("unchecked")
        T result = (T) obj;
        return result;
    }

}
