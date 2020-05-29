package com.feilong.lib.digester3.binder;

import static com.feilong.lib.digester3.binder.BinderClassLoader.createBinderClassLoader;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.feilong.lib.digester3.Digester;
import com.feilong.lib.digester3.RuleSet;
import com.feilong.lib.digester3.Rules;
import com.feilong.lib.digester3.RulesBase;
import com.feilong.lib.digester3.Substitutor;

/**
 * This class manages the creation of Digester instances from digester rules modules.
 */
public final class DigesterLoader{

    /**
     * The default head when reporting an errors list.
     */
    private static final String HEADING = "Digester creation errors:%n%n";

    /**
     * Creates a new {@link DigesterLoader} instance given one or more {@link RulesModule} instance.
     *
     * @param rulesModules
     *            The modules containing the {@code Rule} binding
     * @return A new {@link DigesterLoader} instance
     */
    public static DigesterLoader newLoader(RulesModule...rulesModules){
        if (rulesModules == null || rulesModules.length == 0){
            throw new DigesterLoadingException("At least one RulesModule has to be specified");
        }
        List<RulesModule> asList = Arrays.asList(rulesModules);
        return new DigesterLoader(asList);
    }

    /**
     * The concrete {@link RulesBinder} implementation.
     */
    private final DefaultRulesBinder    rulesBinder     = new DefaultRulesBinder();

    /**
     * The URLs of entityValidator that have been registered, keyed by the public
     * identifier that corresponds.
     */
    private final Map<String, URL>      entityValidator = new HashMap<String, URL>();

    /**
     * The SAXParserFactory to create new default {@link Digester} instances.
     */
    private final SAXParserFactory      factory         = SAXParserFactory.newInstance();

    private final Iterable<RulesModule> rulesModules;

    /**
     * The class loader to use for instantiating application objects.
     * If not specified, the context class loader, or the class loader
     * used to load Digester itself, is used, based on the value of the
     * <code>useContextClassLoader</code> variable.
     */
    private final BinderClassLoader     classLoader;

    /**
     * An optional class that substitutes values in attributes and body text. This may be null and so a null check is
     * always required before use.
     */
    private Substitutor                 substitutor;

    /**
     * Creates a new {@link DigesterLoader} instance given a collection of {@link RulesModule} instance.
     *
     * @param rulesModules
     *            The modules containing the {@code Rule} binding
     */
    private DigesterLoader(Iterable<RulesModule> rulesModules){
        this.rulesModules = rulesModules;
        this.classLoader = createBinderClassLoader(Thread.currentThread().getContextClassLoader());
    }

    /**
     * Set the "namespace aware" flag for parsers we create.
     *
     * @param namespaceAware
     *            The new "namespace aware" flag
     * @return This loader instance, useful to chain methods.
     */
    public DigesterLoader setNamespaceAware(boolean namespaceAware){
        factory.setNamespaceAware(namespaceAware);
        return this;
    }

    /**
     * Return the "namespace aware" flag for parsers we create.
     *
     * @return true, if the "namespace aware" flag for parsers we create, false otherwise.
     */
    private boolean isNamespaceAware(){
        return factory.isNamespaceAware();
    }

    /**
     * Set the XInclude-aware flag for parsers we create. This additionally
     * requires namespace-awareness.
     *
     * @param xIncludeAware
     *            The new XInclude-aware flag
     * @return This loader instance, useful to chain methods.
     * @see #setNamespaceAware(boolean)
     */
    public DigesterLoader setXIncludeAware(boolean xIncludeAware){
        factory.setXIncludeAware(xIncludeAware);
        return this;
    }

    /**
     * Set the validating parser flag.
     *
     * @param validating
     *            The new validating parser flag.
     * @return This loader instance, useful to chain methods.
     */
    public DigesterLoader setValidating(boolean validating){
        factory.setValidating(validating);
        return this;
    }

    /**
     * Return the validating parser flag.
     *
     * @return true, if the validating parser flag is set, false otherwise
     */
    public boolean isValidating(){
        return this.factory.isValidating();
    }

    private DigesterLoader register(String publicId,URL entityURL){
        entityValidator.put(publicId, entityURL);
        return this;
    }

    /**
     * <p>
     * Convenience method that registers the string version of an entity URL
     * instead of a URL version.
     * </p>
     *
     * @param publicId
     *            Public identifier of the entity to be resolved
     * @param entityURL
     *            The URL to use for reading this entity
     * @return This loader instance, useful to chain methods.
     */
    public DigesterLoader register(String publicId,String entityURL){
        try{
            return register(publicId, new URL(entityURL));
        }catch (MalformedURLException e){
            throw new IllegalArgumentException("Malformed URL '" + entityURL + "' : " + e.getMessage());
        }
    }

    /**
     * Creates a new {@link Digester} instance that relies on the default {@link Rules} implementation.
     *
     * @return a new {@link Digester} instance
     */
    public Digester newDigester(){
        try{
            SAXParser parser = this.factory.newSAXParser();
            RulesBase rules = new RulesBase();
            if (parser == null){
                throw new DigesterLoadingException("SAXParser must be not null");
            }

            try{
                XMLReader reader = parser.getXMLReader();
                if (reader == null){
                    throw new DigesterLoadingException("XMLReader must be not null");
                }

                Digester digester = new Digester(reader);
                // the ClassLoader adapter is no needed anymore
                digester.setClassLoader(classLoader.getAdaptedClassLoader());
                digester.setRules(rules);
                digester.setSubstitutor(substitutor);
                digester.registerAll(entityValidator);
                digester.setNamespaceAware(isNamespaceAware());

                addRules(digester);

                return digester;
            }catch (SAXException e){
                throw new DigesterLoadingException("An error occurred while creating the XML Reader", e);
            }
        }catch (ParserConfigurationException e){
            throw new DigesterLoadingException("SAX Parser misconfigured", e);
        }catch (SAXException e){
            throw new DigesterLoadingException("An error occurred while initializing the SAX Parser", e);
        }
    }

    /**
     * Add rules to an already created Digester instance, analyzing the digester annotations in the target class.
     *
     * @param digester
     *            the Digester instance reference.
     */
    public void addRules(final Digester digester){
        RuleSet ruleSet = createRuleSet();
        ruleSet.addRuleInstances(digester);
    }

    /**
     * Creates a new {@link RuleSet} instance based on the current configuration.
     *
     * @return A new {@link RuleSet} instance based on the current configuration.
     */
    public RuleSet createRuleSet(){
        if (classLoader != rulesBinder.getContextClassLoader()){
            rulesBinder.initialize(classLoader);
            for (RulesModule rulesModule : rulesModules){
                rulesModule.configure(rulesBinder);
            }
        }

        if (rulesBinder.hasError()){
            Formatter fmt = new Formatter().format(HEADING);
            int index = 1;

            for (ErrorMessage errorMessage : rulesBinder.getErrors()){
                fmt.format("%s) %s%n", index++, errorMessage.getMessage());

                Throwable cause = errorMessage.getCause();
                if (cause != null){
                    StringWriter writer = new StringWriter();
                    cause.printStackTrace(new PrintWriter(writer));
                    fmt.format("Caused by: %s", writer.getBuffer());
                }

                fmt.format("%n");
            }

            if (rulesBinder.errorsSize() == 1){
                fmt.format("1 error");
            }else{
                fmt.format("%s errors", rulesBinder.errorsSize());
            }

            throw new DigesterLoadingException(fmt.toString());
        }

        return rulesBinder.getFromBinderRuleSet();
    }

}
