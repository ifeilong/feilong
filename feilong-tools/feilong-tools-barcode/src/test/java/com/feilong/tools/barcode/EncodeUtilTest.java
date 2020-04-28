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
package com.feilong.tools.barcode;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.date.DateUtil.nowTimestamp;

import org.junit.After;
import org.junit.Test;

import com.feilong.coreextension.awt.DesktopUtil;
import com.feilong.io.IOReaderUtil;
import com.feilong.test.AbstractTest;
import com.google.zxing.BarcodeFormat;

public class EncodeUtilTest extends AbstractTest{

    private String        contents        = "";

    private String        outputImageFile = "";

    private BarcodeConfig barcodeConfig;

    @Test
    public void test(){
        contents = "http://www.baidu.com";
        outputImageFile = "d:/test.png";

        barcodeConfig = new BarcodeConfig();
        barcodeConfig.setEncodeHintTypeMargin(0);
    }

    /**
     * Test encode.
     */
    @Test
    public void testEncode(){
        barcodeConfig = new BarcodeConfig();
        barcodeConfig.setBarcodeFormat(BarcodeFormat.UPC_A);
        contents = "883419590795";
        outputImageFile = "d:/test" + barcodeConfig.getBarcodeFormat() + contents + ".png";
    }

    @Test
    public void testEncode11(){
        barcodeConfig = new BarcodeConfig();
        barcodeConfig.setBarcodeFormat(BarcodeFormat.UPC_A);
        //11或者12
        contents = "https://qr.95516.com/48020000/42171909246221001152996469";
        outputImageFile = "/Users/feilong/feilong/" + nowTimestamp() + ".pdf";
    }

    @Test
    public void test2(){
        barcodeConfig = new BarcodeConfig();
        //        barcodeConfig.setWidth(600);
        //        barcodeConfig.setHeight(600);
        //barcodeConfig.setLogoImagePath("E:\\DataFixed\\Material\\avatar\\飞龙_80x80.png");
        contents = "李景生日快乐~";
        contents = IOReaderUtil.readToString("/Users/feilong/Development/DataCommon/test/happybirthday-lijing.txt", UTF8);
        // contents = "mailto:it_training@feilong.cn?subject=%E5%AE%8C%E6%88%90%E4%BA%86%E3%80%8ANebula%E5%95%86%E5%93%81%E4%BD%93%E7%B3%BB%E3%80%8B%E5%9F%B9%E8%AE%ADhomework&body=";
        //        contents = "mailto:it_training@feilong.cn";

        //短信
        //        contents = "smsto:13800138000:你好,我来自志文工作室";

        LOGGER.debug("the param contents:{}", contents);
        outputImageFile = "/Users/feilong/feilong/test2.png";
    }

    /**
     * afterBarcodeUtilTest.
     */
    @After
    public void afterBarcodeUtilTest(){
        BarcodeEncodeUtil.encode(contents, outputImageFile, barcodeConfig);
        DesktopUtil.open(outputImageFile);
    }
}
