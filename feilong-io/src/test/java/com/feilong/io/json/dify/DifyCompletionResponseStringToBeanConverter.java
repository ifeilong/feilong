package com.feilong.io.json.dify;

import static com.feilong.core.bean.ConvertUtil.toMap;

import java.util.Map;

import com.feilong.context.converter.StringToBeanConverter;
import com.feilong.core.util.MapUtil;
import com.feilong.json.JsonToJavaConfig;
import com.feilong.json.JsonUtil;
import com.feilong.json.transformer.SeparatorToCamelCaseJavaIdentifierTransformer;
import com.feilong.lib.json.util.JavaIdentifierTransformer;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 */
public class DifyCompletionResponseStringToBeanConverter<T> implements StringToBeanConverter<DifyCompletionResponse<T>>{

    private final Class<T>                  outputClass;

    private JavaIdentifierTransformer javaIdentifierTransformer = SeparatorToCamelCaseJavaIdentifierTransformer.INSTANCE;

    private Map<String, Class<?>>     customClassMap;

    //---------------------------------------------------------------

    /**
     * @param outputClass
     */
    public DifyCompletionResponseStringToBeanConverter(Class<T> outputClass){
        super();
        this.outputClass = outputClass;
    }

    //---------------------------------------------------------------

    @Override
    public DifyCompletionResponse<T> convert(String responseBody){
        JsonToJavaConfig jsonToJavaConfig = new JsonToJavaConfig(DifyCompletionResponse.class, javaIdentifierTransformer);

        Map<String, Class<?>> map = toMap("outputs", outputClass);
        MapUtil.putAllIfNotNull(map, customClassMap);

        //---------------------------------------------------------------
        jsonToJavaConfig.setClassMap(map);

        return JsonUtil.toBean(responseBody, jsonToJavaConfig);
    }

    //---------------------------------------------------------------

    /**
     * @return the customClassMap
     */
    public Map<String, Class<?>> getCustomClassMap(){
        return customClassMap;
    }

    /**
     * @param customClassMap
     *            the customClassMap to set
     */
    public void setCustomClassMap(Map<String, Class<?>> customClassMap){
        this.customClassMap = customClassMap;
    }

    /**
     * @return the javaIdentifierTransformer
     */
    public JavaIdentifierTransformer getJavaIdentifierTransformer(){
        return javaIdentifierTransformer;
    }

    /**
     * @param javaIdentifierTransformer
     *            the javaIdentifierTransformer to set
     */
    public void setJavaIdentifierTransformer(JavaIdentifierTransformer javaIdentifierTransformer){
        this.javaIdentifierTransformer = javaIdentifierTransformer;
    }

}
