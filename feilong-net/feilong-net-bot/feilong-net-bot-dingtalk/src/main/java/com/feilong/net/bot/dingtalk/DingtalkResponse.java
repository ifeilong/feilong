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
package com.feilong.net.bot.dingtalk;

/**
 * 钉钉机器人响应.
 * 
 * <h3>示例:</h3>
 * 
 * <blockquote>
 * 
 * <pre class="code">
 *  {
        "errmsg": "ok",
        "errcode": "0"
    }
 * </pre>
 * 
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.9
 */
// 不能改成default 修饰符,  json 字符串转bean 会失败
public class DingtalkResponse{

    /**
     * The errcode.
     */
    private String errcode;

    /** The errmsg. */
    private String errmsg;

    //---------------------------------------------------------------

    /**
     * Gets the checks if is success.
     *
     * @return the checks if is success
     */
    public boolean getIsSuccess(){
        return "ok".equals(errmsg) && "0".equals(errcode);
    }

    //---------------------------------------------------------------

    /**
     * Gets the errcode.
     *
     * @return the errcode
     */
    public String getErrcode(){
        return errcode;
    }

    /**
     * Sets the errcode.
     *
     * @param errcode
     *            the errcode to set
     */
    public void setErrcode(String errcode){
        this.errcode = errcode;
    }

    /**
     * Gets the errmsg.
     *
     * @return the errmsg
     */
    public String getErrmsg(){
        return errmsg;
    }

    /**
     * Sets the errmsg.
     *
     * @param errmsg
     *            the errmsg to set
     */
    public void setErrmsg(String errmsg){
        this.errmsg = errmsg;
    }

}
