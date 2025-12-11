/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.context.log;

import static com.feilong.context.log.AutoLog.autoLog;

import java.util.concurrent.Callable;

import com.feilong.core.DefaultRuntimeException;

import lombok.extern.slf4j.Slf4j;

/**
 * 日志上下文执行器.
 * 
 * <h3>Callable Supplier Runnable 区别</h3>
 * 
 * <blockquote>
 * <p>
 * Callable接口允许抛出受检异常，而Supplier不能 这是最核心的区别。<br>
 * 
 * Supplier属于Java 8引入的函数式接口，主要场景是延迟计算和数据供给。而Callable通常用于需要返回结果且可能抛出异常的并发任务<br>
 * 
 * Runnable适用于无返回值的场景，Callable适用于需要返回结果或抛出异常的场景。而用户的call方法明显需要返回值，所以排除了Runnable<br>
 * 
 * 虽然Supplier的语法更简洁（不需要处理受检异常），但在业务场景中反而成为限制。<br>
 * Callable虽然使用稍复杂（需要处理ExecutionException等），但提供更完整的错误处理机制
 * </p>
 * </blockquote>
 * 
 * <h3>核心对比：Supplier vs Callable​</h3>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">维度</th>
 * <th align="left">Supplier</th>
 * <th align="left">Callable</th>
 * <th align="left">适用性结论​</th>
 * </tr>
 * 
 * <tr valign="top">
 * <td>返回值</td>
 * <td>支持返回值 T get()</td>
 * <td>支持返回值 T call()</td>
 * <td>平手</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>异常处理</td>
 * <td>不支持抛出受检异常，只能内部处理或抛RuntimeException</td>
 * <td>支持抛出受检异常​（throws Exception）</td>
 * <td>Callable胜出，更贴合业务逻辑</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>使用场景</td>
 * <td>纯计算、无外部依赖（如缓存读取）</td>
 * <td>可能涉及IO、远程调用等易出错操作</td>
 * <td>Callable胜出，日志场景常含IO</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>线程安全性​</td>
 * <td>无状态时安全</td>
 * <td>无状态时安全</td>
 * <td>平手</td>
 * </tr>
 * 
 * 
 * </table>
 * </blockquote>
 * 
 * 
 * <p>
 * <h3>Supplier的短板​：</h3>
 * </p>
 * 若业务代码（如数据库操作、RPC调用）需抛出SQLException、IOException等受检异常，Supplier会强制在Lambda内部处理（try-catch），导致代码冗余且破坏简洁性
 * 
 * <pre>
{@code
// Supplier需手动处理异常，代码臃肿
call("key", () -> {
    try { return db.query(); } 
    catch (SQLException e) { throw new RuntimeException(e); }
});
}
 * </pre>
 * 
 * <p>
 * <h3>​Callable的优势​：</h3>
 * </p>
 * 直接声明throws Exception，异常可透传到调用层统一处理，符合Java异常规范
 * 
 * <pre>
{@code
// Callable天然支持异常传播
call("key", () -> db.query()); // 无需额外包装
}
 * </pre>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.4.0
 */
@Slf4j
public class LogContextExecutor{

    /**
     * 无返回值的场景
     * 
     * @param logKey
     * @param runnable
     * @since 4.4.0
     */
    public static void run(String logKey,Runnable runnable){
        call(logKey, () -> {
            runnable.run();
            return null;
        });
    }

    /**
     * 核心功能是日志上下文管理.
     *
     * @param <T>
     *            the generic type
     * @param logKey
     *            the log key
     * @param callable
     *            the supplier
     * @return the t
     */
    public static <T> T call(String logKey,Callable<T> callable){
        return call(logKey, callable, null);
    }

    //---------------------------------------------------------------

    /**
     * 核心功能是日志上下文管理.
     * 
     * <p>
     * 如果exceptionCallback 不是null, 那么会回调调用,比如可以发钉钉提醒
     * </p>
     *
     * @param <T>
     *            the generic type
     * @param logKey
     *            the log key
     * @param callable
     *            Callable接口允许抛出受检异常，而Supplier不能 这是最核心的区别。
     * 
     *            Supplier属于Java 8引入的函数式接口，主要场景是延迟计算和数据供给。而Callable通常用于需要返回结果且可能抛出异常的并发任务
     * 
     *            Runnable适用于无返回值的场景，Callable适用于需要返回结果或抛出异常的场景。而用户的call方法明显需要返回值，所以排除了Runnable
     * @param throwableCallback
     *            the exception callback
     * @return the t
     */
    public static <T> T call(String logKey,Callable<T> callable,ThrowableCallback throwableCallback){
        try{
            LogCounterThreadLocal.beginCounter();
            LogKeyThreadLocal.setLogKey(logKey);

            return callable.call();//可能有受检异常

        }catch (Throwable e){
            log.warn(autoLog(""), e);
            //支持异常回调, 比如发钉钉提醒,发短信等功能
            if (null != throwableCallback){
                throwableCallback.callback(e);
            }

            //如果是运行时异常, 那么直接返回,  有的exceptionHandler会解析 针对不同异常返回给客户端或者C端用户不同的错误内容
            if (e instanceof RuntimeException){
                throw (RuntimeException) e;
            }

            //如果是 受检异常, 那么包装一下返回
            throw new DefaultRuntimeException(e);
        }finally{
            LogCounterThreadLocal.removeCounter();
            LogKeyThreadLocal.removeLogKey();
        }
    }

}
