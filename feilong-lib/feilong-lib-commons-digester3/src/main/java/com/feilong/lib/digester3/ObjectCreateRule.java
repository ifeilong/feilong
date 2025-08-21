package com.feilong.lib.digester3;

import static java.lang.String.format;

import org.xml.sax.Attributes;

/**
 * Rule implementation that creates a new object and pushes it onto the object stack. When the element is complete, the
 * object will be popped
 */
@lombok.extern.slf4j.Slf4j
public class ObjectCreateRule extends Rule{

    /**
     * Construct an object create rule with the specified class and an optional attribute name containing an override.
     *
     * @param attributeName
     *            Attribute name which, if present, contains an
     * @param clazz
     *            Java class name of the object to be created override of the class name to create
     */
    public ObjectCreateRule(String attributeName, Class<?> clazz){
        this.clazz = clazz;

        this.className = clazz.getName();
        this.attributeName = attributeName;
    }

    // ----------------------------------------------------- Instance Variables

    /**
     * The attribute containing an override class name if it is present.
     */
    protected String   attributeName = null;

    /**
     * The Java class of the object to be created.
     */
    protected Class<?> clazz         = null;

    /**
     * The Java class name of the object to be created.
     */
    protected String   className     = null;

    // --------------------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     */
    @Override
    public void begin(String namespace,String name,Attributes attributes) throws Exception{
        Class<?> clazz = this.clazz;

        if (clazz == null){
            // Identify the name of the class to instantiate
            String realClassName = className;
            if (attributeName != null){
                String value = attributes.getValue(attributeName);
                if (value != null){
                    realClassName = value;
                }
            }
            if (log.isDebugEnabled()){
                log.debug(format("[ObjectCreateRule]{%s} New '%s'", getDigester().getMatch(), realClassName));
            }

            // Instantiate the new object and push it on the context stack
            clazz = getDigester().getClassLoader().loadClass(realClassName);
        }
        Object instance = null;
        if (log.isDebugEnabled()){
            log.debug(format("[ObjectCreateRule]{%s} New '%s' using default empty constructor", getDigester().getMatch(), clazz.getName()));
        }

        instance = clazz.newInstance();
        getDigester().push(instance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void end(String namespace,String name) throws Exception{
        Object top = getDigester().pop();

        if (log.isDebugEnabled()){
            log.debug(format("[ObjectCreateRule]{%s} Pop '%s'", getDigester().getMatch(), top.getClass().getName()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(){
        return format("ObjectCreateRule[className=%s, attributeName=%s]", className, attributeName);
    }

}
