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
package com.feilong.taglib.display.barcode;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.accessor.KeyAccessor;
import com.feilong.accessor.session.SessionKeyAccessor;
import com.feilong.core.UncheckedIOException;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.tools.barcode.BarcodeEncodeUtil;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * 渲染验证码的servlet.
 * 
 * <h3>使用说明:</h3>
 * 
 * <blockquote>
 * <p>
 * 参见<a href="https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-barcode">feilongDisplay-barcode wiki</a>
 * </p>
 * </blockquote>
 * 
 * <h3>配置方式:</h3>
 * 
 * <blockquote>
 * 
 * <pre class="code">
{@code
    <!-- barcode -->
    <servlet>
        <servlet-name>feilong-barcode</servlet-name>
        <servlet-class>com.feilong.taglib.display.barcode.BarcodeServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>feilong-barcode</servlet-name>
        <url-pattern>/feilongbarcode</url-pattern>
    </servlet-mapping>
}
 * </pre>
 * 
 * </blockquote>
 * 
 * <h3>注意:</h3>
 * 
 * <blockquote>
 * <p>
 * 通常该类不会单独使用,要和 自定义标签 {@link BarcodeTag} 搭配使用
 * </p>
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see <a href="https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-barcode">feilongDisplay-barcode wiki</a>
 * @since 1.5.4
 * @deprecated since 1.10.5 推荐使用js来渲染二维码,
 *             <p>
 *             该标签会使用session机制,在页面静态化下以及高并发的场景下会有性能不高;<br>
 *             并且该标签还需要额外在web.xml中配置servlet
 *             </p>
 */
@Deprecated
public class BarcodeServlet extends HttpServlet{

    /** The Constant serialVersionUID. */
    private static final long   serialVersionUID = 231074760785325078L;

    /** The Constant log. */
    private static final Logger LOGGER           = LoggerFactory.getLogger(BarcodeServlet.class);

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    @Override
    public void service(ServletRequest request,ServletResponse response) throws ServletException{
        //① 取到 参数 里面的 barcodeId
        String barcodeId = request.getParameter(BarcodeRequestParams.BARCODE_ID);
        Validate.notEmpty(barcodeId, "barcodeId can't be null/empty!");

        //② 基于 barcodeId 取到session里面保存的 BarcodeContentsAndConfig
        KeyAccessor keyAccessor = new SessionKeyAccessor();

        BarcodeContentsAndConfig barcodeContentsAndConfig = keyAccessor.get(barcodeId, (HttpServletRequest) request);
        Validate.notNull(barcodeContentsAndConfig, "barcodeContentsAndConfig can't be null!");

        //③ render
        render(barcodeContentsAndConfig, response);
    }

    //---------------------------------------------------------------

    /**
     * Encode.
     *
     * @param barcodeContentsAndConfig
     *            the barcode contents and config
     * @param response
     *            the response
     */
    private static void render(BarcodeContentsAndConfig barcodeContentsAndConfig,ServletResponse response){
        try{
            ServletOutputStream outputStream = response.getOutputStream();
            BarcodeEncodeUtil.encode(barcodeContentsAndConfig.getContents(), outputStream, barcodeContentsAndConfig.getBarcodeConfig());
        }catch (IOException e){
            throw new UncheckedIOException(Slf4jUtil.format("barcodeContentsAndConfig:{}", JsonUtil.format(barcodeContentsAndConfig)), e);
        }
    }
}
