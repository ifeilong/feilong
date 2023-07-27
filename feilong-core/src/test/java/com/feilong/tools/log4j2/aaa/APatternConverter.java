package com.feilong.tools.log4j2.aaa;

import static com.feilong.core.date.DateUtil.nowTime;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;

//Plugin 表示的是这是一个插件，
//name是名称，
//category为PatternConverter.CATEGORY（目前插件只有这个选择）
@Plugin(name = "TIDPatternConverter",category = PatternConverter.CATEGORY)

//ConverterKeys表示的就是自定义的参数,可以多个
@ConverterKeys({ "a", "aaa" })

//定义一个类继承LogEventPatternConverter
public class APatternConverter extends LogEventPatternConverter{

    private static final APatternConverter INSTANCE = new APatternConverter();

    //定义的这个类必须提供一个newInstance方法，
    //参数是final String[] options，
    //返回值为定义的类（对于是否是单例没有明确的要求）
    public static APatternConverter newInstance(final String[] options){
        return INSTANCE;
    }

    //提供一个私有的构造函数，调用父类的构造函数，函数需要提供两个参数 
    //第一个参数是转换器的名称，第二个是css样式
    private APatternConverter(){
        super("aaa", "aaa");
    }

    //还有主要的工作format，这里有两个参数，LogEvent是系统已经存在的一些可选数据，StringBuilder 表示的是最终的输出字符流。
    //一般都是将自定义的append进去
    @Override
    public void format(LogEvent event,StringBuilder toAppendTo){
        toAppendTo.append(nowTime());
    }

}
