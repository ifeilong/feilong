package com.feilong.lib.digester3.binder;

import com.feilong.lib.digester3.BeanPropertySetterRule;

/**
 * Builder chained when invoking {@link LinkedRuleBuilder#setBeanProperty()}.
 */
public final class BeanPropertySetterBuilder extends AbstractBackToLinkedRuleBuilder<BeanPropertySetterRule>{

    private String propertyName;

    private String attribute;

    BeanPropertySetterBuilder(String keyPattern, String namespaceURI, RulesBinder mainBinder, LinkedRuleBuilder mainBuilder){
        super(keyPattern, namespaceURI, mainBinder, mainBuilder);
    }

    /**
     * Sets the name of property to set.
     *
     * @param propertyName
     *            The name of property to set
     * @return this builder instance
     */
    public BeanPropertySetterBuilder withName( /* @Nullable */String propertyName){
        this.propertyName = propertyName;
        return this;
    }

    /**
     * Sets the attribute name from which the property name has to be extracted.
     *
     * @param attribute
     *            The attribute name from which extracting the name of property to set
     * @return this builder instance
     */
    public BeanPropertySetterBuilder extractPropertyNameFromAttribute(String attribute){
        if (attribute == null){
            reportError("setBeanProperty().extractPropertyNameFromAttribute( String )", "Attribute name can not be null");
        }
        this.attribute = attribute;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BeanPropertySetterRule createRule(){
        BeanPropertySetterRule rule = new BeanPropertySetterRule(propertyName);
        rule.setPropertyNameFromAttribute(attribute);
        return rule;
    }

}
