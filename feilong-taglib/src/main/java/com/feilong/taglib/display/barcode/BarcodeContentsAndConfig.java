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

import java.io.Serializable;

import com.feilong.tools.barcode.BarcodeConfig;

/**
 * The Class BarcodeContentsAndConfig.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.4
 * @deprecated since 1.10.5 推荐使用js来渲染二维码,
 *             <p>
 *             该标签会使用session机制,在页面静态化下以及高并发的场景下会有性能不高;<br>
 *             并且该标签还需要额外在web.xml中配置servlet
 *             </p>
 */
@Deprecated
public class BarcodeContentsAndConfig implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7754551783221278122L;

    /** 文字内容. */
    private String            contents;

    /** The barcode config. */
    private BarcodeConfig     barcodeConfig;

    //---------------------------------------------------------------

    /**
     * The Constructor.
     */
    public BarcodeContentsAndConfig(){
        super();
    }

    /**
     * The Constructor.
     *
     * @param contents
     *            the contents
     * @param barcodeConfig
     *            the barcode config
     */
    public BarcodeContentsAndConfig(String contents, BarcodeConfig barcodeConfig){
        super();
        this.contents = contents;
        this.barcodeConfig = barcodeConfig;
    }

    /**
     * 获得 contents.
     *
     * @return the contents
     */
    public String getContents(){
        return contents;
    }

    /**
     * 设置 contents.
     *
     * @param contents
     *            the contents to set
     */
    public void setContents(String contents){
        this.contents = contents;
    }

    /**
     * 获得 barcode config.
     *
     * @return the barcodeConfig
     */
    public BarcodeConfig getBarcodeConfig(){
        return barcodeConfig;
    }

    /**
     * 设置 barcode config.
     *
     * @param barcodeConfig
     *            the barcodeConfig to set
     */
    public void setBarcodeConfig(BarcodeConfig barcodeConfig){
        this.barcodeConfig = barcodeConfig;
    }
}
