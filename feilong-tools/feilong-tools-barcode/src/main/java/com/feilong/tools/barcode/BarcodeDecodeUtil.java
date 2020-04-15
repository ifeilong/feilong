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

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.EnumMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.coreextension.awt.ImageUtil;
import com.feilong.json.jsonlib.JsonUtil;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

/**
 * 专注于 bar code(条形码) decode的工具类.
 * 
 * <h3>本工具类依赖于以下jar:</h3>
 * 
 * <blockquote>
 * 
 * <pre class="code">
 * {@code
 *       <dependency>
 *           <groupId>com.google.zxing</groupId>
 *           <artifactId>javase</artifactId>
 *           <version>3.2.0</version>
 *       </dependency>
 *  }
 * </pre>
 * 
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.4
 */
public class BarcodeDecodeUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BarcodeDecodeUtil.class);

    /** Don't let anyone instantiate this class. */
    private BarcodeDecodeUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------
    /**
     * 解析二维码.
     *
     * @param filePath
     *            二维码图片地址
     * @return the string
     */
    public static String decode(String filePath){
        return decode(new File(filePath));
    }

    /**
     * 解析二维码.
     *
     * @param file
     *            二维码图片
     * @return the string
     */
    public static String decode(File file){
        BufferedImage bufferedImage = ImageUtil.getBufferedImage(file);
        return decode(bufferedImage);
    }

    /**
     * 解析二维码.
     *
     * @param bufferedImage
     *            the buffered image
     * @return the string
     */
    public static String decode(BufferedImage bufferedImage){
        BufferedImageLuminanceSource bufferedImageLuminanceSource = new BufferedImageLuminanceSource(bufferedImage);
        Binarizer binarizer = new HybridBinarizer(bufferedImageLuminanceSource);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.CHARACTER_SET, UTF8);

        MultiFormatReader multiFormatReader = new MultiFormatReader();
        try{
            Result result = multiFormatReader.decode(binaryBitmap, hints);

            if (LOGGER.isDebugEnabled()){
                LOGGER.debug(JsonUtil.format(result));
            }

            return result.getText();
        }catch (NotFoundException e){
            throw new DefaultRuntimeException(e);
        }
    }
}
