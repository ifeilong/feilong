package com.feilong.context.log;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static com.feilong.core.lang.StringUtil.formatPattern;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * auto工具(自动识别 rpc 还是request 还是mq).
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 4.4.0
 */
public class AutoLog{

    /**
     * auto输出日志
     * 
     * @param messagePattern
     * @param params
     * @return 计数器+上下文+业务日志
     */
    public static String autoLog(String messagePattern,Object...params){
        //业务日志
        String businessLog = formatPattern(messagePattern, params);

        return formatPattern(
                        "{}{}{}{}", //
                        getCounterLogString(),
                        getLogkeyString(),
                        ContextLogCreator.getContextLog(),

                        businessLog);
    }

    //---------------------------------------------------------------
    /**
     * 计步器
     */
    private static String getCounterLogString(){
        AtomicInteger counter = LogCounterThreadLocal.getCounter();
        if (null == counter || counter.get() < 0){
            return EMPTY;
        }
        return "Step:[" + counter.getAndIncrement() + "],";
    }

    /**
     * 日志上下文
     */
    private static String getLogkeyString(){
        String logkey = LogKeyThreadLocal.getLogkey();
        if (isNullOrEmpty(logkey)){
            return EMPTY;
        }
        return "logkey:[" + logkey + "],";
    }

}
