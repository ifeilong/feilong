package com.feilong.lib.digester3.xmlrules;

import org.xml.sax.Attributes;

import com.feilong.lib.digester3.Rule;

/**
 * 
 */
class PatternRule extends Rule{

    private final String       attributeName;

    private final PatternStack patternStack;

    private String             pattern;

    public PatternRule(PatternStack patternStack){
        this("value", patternStack);
    }

    public PatternRule(String attributeName, PatternStack patternStack){
        this.attributeName = attributeName;
        this.patternStack = patternStack;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void begin(String namespace,String name,Attributes attributes) throws Exception{
        this.pattern = attributes.getValue(this.attributeName);
        if (this.pattern != null){
            this.patternStack.push(pattern);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void end(String namespace,String name) throws Exception{
        if (this.pattern != null){
            this.patternStack.pop();
        }
    }

    /**
     * @return
     */
    protected String getMatchingPattern(){
        return this.patternStack.toString();
    }

}
