package com.feilong.namespace.http;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.feilong.context.invoker.http.HttpResponseStringBuilder;
import com.feilong.namespace.RuntimeBeanReferenceBuilder;

public class HttpResponseStringBuilderBeanDefinitionParser extends AbstractSingleBeanDefinitionParser{

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser#getBeanClass(org.w3c.dom.Element)
     */
    @Override
    protected Class<?> getBeanClass(Element element){
        return HttpResponseStringBuilder.class;
    }

    //---------------------------------------------------------------

    @Override
    protected void doParse(Element element,ParserContext parserContext,BeanDefinitionBuilder beanDefinitionBuilder){
        beanDefinitionBuilder.addPropertyValue(
                        "httpRequestBuilder",
                        RuntimeBeanReferenceBuilder.build(
                                        parserContext,
                                        HttpRequestBuilderBeanDefinitionBuilderBuilder.build(element, parserContext)));

        //beanDefinitionBuilder.addPropertyValue("connectionConfigBuilder", ConnectionConfigBuilderParserUtil.build(element));
    }

}