package com.feilong.context.log;

import static com.feilong.core.lang.StringUtil.EMPTY;
import static com.feilong.core.lang.StringUtil.formatPattern;

import com.feilong.servlet.http.RequestUtil;
import com.feilong.spring.web.util.WebSpringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpSpringFixedContextLogCreator extends AbstractFixedContextLogCreator{

    private static Class<?>                              HTTP_SERVLET_REQUEST_CLASS = getClass("javax.servlet.http.HttpServletRequest");

    /** Static instance. */
    // the static instance works for all types
    public static final HttpSpringFixedContextLogCreator INSTANCE                   = new HttpSpringFixedContextLogCreator();

    //---------------------------------------------------------------
    @Override
    public String createContextLog(){
        //如果项目不存在javax.servlet.http.HttpServletRequest 类, 那么直接返回空
        if (null == HTTP_SERVLET_REQUEST_CLASS){
            return EMPTY;
        }

        //---------------------------------------------------------------
        try{
            //获取对应的request , 基于spring 框架
            javax.servlet.http.HttpServletRequest request = WebSpringUtil.getRequest();
            if (null == request){

                //如果没有的话 返回空
                return EMPTY;
            }

            //自动生成api地址以及请求参数的log 字符串
            return formatPattern(
                            "api:[{}],paramString:[{}]",
                            RequestUtil.getRequestURL(request),
                            RequestUtil.parseParamsToQueryString(request));
        }catch (Throwable e){
            return "HttpSpringFixedContextLogCreator exception:" + e.getMessage();
        }
    }

}
