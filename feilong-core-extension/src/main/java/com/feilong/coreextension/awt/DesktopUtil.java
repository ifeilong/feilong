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
package com.feilong.coreextension.awt;

import static com.feilong.core.CharsetType.UTF8;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;

import com.feilong.core.net.URIUtil;
import com.feilong.lib.lang3.Validate;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * {@link java.awt.Desktop}允许 Java应用程序启动已在本机桌面上注册的关联应用程序,以及处理 URI 或文件 .
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see java.awt.Desktop
 * @since 1.0.0
 * @since jdk 1.6
 */
public final class DesktopUtil{

    /** 判断当前系统是否支持Java AWT Desktop扩展. */
    private static final boolean DESKTOP_SUPPORTED = Desktop.isDesktopSupported();

    /** Don't let anyone instantiate this class. */
    private DesktopUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 使用系统默认浏览器,打开url.
     * 
     * <p>
     * 如果 <code>url</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>url</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * </p>
     *
     * @param url
     *            url地址
     * @param args
     *            the args
     * @see #desktopAction(String, Action)
     */
    public static void browse(String url,Object...args){
        desktopAction(Slf4jUtil.format(url, args), Action.BROWSE);
    }

    /**
     * 启动关联应用程序来打开文件..
     * <p>
     * 如果 <code>url</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>url</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * </p>
     *
     * @param url
     *            url地址
     * @param args
     *            the args
     * @see #desktopAction(String, Action)
     */
    public static void open(String url,Object...args){
        desktopAction(Slf4jUtil.format(url, args), Action.OPEN);
    }

    //---------------------------------------------------------------

    /**
     * 发送邮件.
     * <p>
     * 如果 <code>url</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>url</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * </p>
     *
     * @param mailtoURLPattern
     *            the mail
     * @param args
     *            the args
     * @see java.awt.Desktop#mail(URI)
     * @see #desktopAction(String, Action)
     */
    public static void mail(String mailtoURLPattern,Object...args){
        desktopAction(Slf4jUtil.format(mailtoURLPattern, args), Action.MAIL);
    }

    /**
     * 打印.
     * 
     * <p>
     * 如果 <code>url</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>url</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * </p>
     *
     * @param url
     *            the url
     * @param args
     *            the args
     * @since 1.2.0
     */
    public static void print(String url,Object...args){
        desktopAction(Slf4jUtil.format(url, args), Action.PRINT);
    }

    //---------------------------------------------------------------

    /**
     * Desktop action.
     * 
     * <p>
     * 如果 <code>url</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>url</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * </p>
     *
     * @param url
     *            the url
     * @param action
     *            the action
     * @since 1.2.0
     */
    private static void desktopAction(String url,Action action){
        Validate.notBlank(url, "url can't be blank!");
        Validate.notNull(action, "action can't be null!");

        //---------------------------------------------------------------
        Desktop desktop = getDesktop(action);

        try{
            switch (action) {
                case MAIL:
                    desktop.mail(URIUtil.create(url, UTF8));
                    break;
                case BROWSE: // 获取系统默认浏览器打开链接
                    desktop.browse(URIUtil.create(url, UTF8));
                    break;
                case OPEN: // 启动关联应用程序来打开文件
                    desktop.open(new File(url));
                    break;
                case EDIT:
                    desktop.edit(new File(url));
                    break;
                case PRINT:
                    desktop.print(new File(url));
                    break;
                default:
                    throw new UnsupportedOperationException(Slf4jUtil.format("[{}] not support!", action));
            }
        }catch (IOException e){
            throw new UncheckedIOException(Slf4jUtil.format("[{}],[{}]", url, action), e);
        }
    }

    /**
     * Gets the desktop.
     *
     * @param action
     *            the action
     * @return the desktop
     * @since 1.8.3
     */
    private static Desktop getDesktop(Action action){
        if (!DESKTOP_SUPPORTED){
            throw new UnsupportedOperationException("don't Support Desktop");
        }

        //---------------------------------------------------------------
        Desktop desktop = Desktop.getDesktop();// 获取当前系统桌面扩展
        boolean supported = desktop.isSupported(action);// 判断系统桌面是否支持要执行的功能
        if (!supported){
            throw new UnsupportedOperationException("don't Support action:" + action);
        }
        return desktop;
    }
}
