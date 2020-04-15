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
import static com.feilong.core.Validator.isNullOrEmpty;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.OutputStream;
import java.util.EnumMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.coreextension.awt.ImageUtil;
import com.feilong.io.FileUtil;
import com.feilong.json.jsonlib.JsonUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 专注于 bar code(条形码)encode 工具类.
 * 
 * <p>
 * 注:原文见 <a href="http://bLOGGER.csdn.net/mmm333zzz/article/details/17259513">java二维码工具类,中间带LOGO的,很强大</a>,对此重新封装
 * </p>
 * 
 * <h3>关于bar code(条形码):</h3>
 * 
 * <blockquote>
 * 
 * <ul>
 * <li>二维码QR(Quick Response Code):{@link BarcodeFormat#QR_CODE}</li>
 * <li>UPC(Universal Product Code)统一产品代码,{@link BarcodeFormat#UPC_A}</li>
 * </ul>
 * </blockquote>
 * 
 * <h3>本工具类依赖于以下jar:</h3>
 * 
 * <blockquote>
 * 
 * <pre class="code">
 * {@code
 *      <dependency>
 *           <groupId>com.google.zxing</groupId>
 *           <artifactId>core</artifactId>
 *           <version>3.2.0</version>
 *       </dependency>
 *  }
 * </pre>
 * 
 * </blockquote>
 * 
 * <h3>关于二维码</h3>
 * 
 * <blockquote>
 * <p>
 * 二维码,是一种采用黑白相间的平面几何图形通过相应的编码算法来记录文字、图片、网址等信息的条码图片
 * </p>
 * 
 * <p>
 * 二维码的特点：
 * </p>
 * 
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * <tr valign="top">
 * <td>1.高密度编码,信息容量大</td>
 * <td>可容纳多达1850个大写字母或2710个数字或1108个字节,或500多个汉字,比普通条码信息容量约高几十倍。</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>2.编码范围广</td>
 * <td>该条码可以把图片、声音、文字、签字、指纹等可以数字化的信息进行编码,用条码表示出来;可以表示多种语言文字;可表示图像数据。</td>
 * </tr>
 * <tr valign="top">
 * <td>3.容错能力强,具有纠错功能</td>
 * <td>这使得二维条码因穿孔、污损等引起局部损坏时,照样可以正确得到识读,损毁面积达50%仍可恢复信息。</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>4.译码可靠性高</td>
 * <td>它比普通条码译码错误率百万分之二要低得多,误码率不超过千万分之一。</td>
 * </tr>
 * <tr valign="top">
 * <td>5.可引入加密措施</td>
 * <td>保密性、防伪性好。</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>6.成本低,易制作,持久耐用</td>
 * <td></td>
 * </tr>
 * </table>
 * </blockquote>
 * 
 * 
 * <h3>注意点:</h3>
 * <blockquote>
 * <ol>
 * <li>如果要采用在二维码中添加头像,那么生成的二维码最好采用最高等级{@link ErrorCorrectionLevel#H}级别的纠错能力,目的有两个:一是增加二维码的正确识别能力;二是扩大二维码数据内容的大小。</li>
 * <li>头像大小最好不要超过二维码本身大小的1/5,而且只能放在正中间部位。这是由于二维码本身结构造成的。所以说新浪微博的二维码只是合理的利用了规则而已。</li>
 * <li>如果要仿照腾讯微信,在二维码边上增加装饰框,记得一定要在装饰框和二维码之间留出白边,这是为了二维码可识别。</li>
 * </ol>
 * </blockquote>
 * 
 * <h3>关于容错率{@link ErrorCorrectionLevel}:</h3>
 * 
 * <blockquote>
 * <ul>
 * <li>Level L – up to 7% damage</li>
 * <li>Level M – up to 15% damage</li>
 * <li>Level Q – up to 25% damage</li>
 * <li>Level H – up to 30% damage</li>
 * </ul>
 * <img src="http://blog.qrstuff.com/wp-content/uploads/2011/11/error_correction_levels1.png"/>
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see <a href="http://bLOGGER.csdn.net/mmm333zzz/article/details/17259513">java二维码工具类,中间带LOGO的,很强大</a>
 * @see com.google.zxing.qrcode.QRCodeWriter
 * @see com.google.zxing.qrcode.QRCodeReader
 * @see com.google.zxing.client.j2se.MatrixToImageWriter
 * @see com.google.zxing.MultiFormatWriter
 * @since 1.2.1
 */
public class BarcodeEncodeUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BarcodeEncodeUtil.class);

    /** Don't let anyone instantiate this class. */
    private BarcodeEncodeUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Encode.
     *
     * @param contents
     *            the content
     * @param outputImageFile
     *            the output image file
     * @throws NullPointerException
     *             如果 <code>contents</code> 或者 <code>outputImageFile</code> 是null
     * @throws IllegalArgumentException
     *             如果 <code>contents</code> 或者 <code>outputImageFile</code> 是 blank
     * @see #encode(String, String, BarcodeConfig)
     */
    public static void encode(String contents,String outputImageFile){
        encode(contents, outputImageFile, new BarcodeConfig());
    }

    /**
     * 生成二维码.
     *
     * @param contents
     *            内容 The contents to encode in the barcode
     * @param outputImageFile
     *            the output image file
     * @param barcodeConfig
     *            the barcode config
     * @throws NullPointerException
     *             如果 <code>contents</code> 或者 <code>outputImageFile</code> 是null
     * @throws IllegalArgumentException
     *             如果 <code>contents</code> 或者 <code>outputImageFile</code> 是 blank
     * @see #encode(String, OutputStream, BarcodeConfig)
     */
    public static void encode(String contents,String outputImageFile,BarcodeConfig barcodeConfig){
        Validate.notBlank(contents, "contents can't be null/empty!");
        Validate.notBlank(outputImageFile, "outputImageFile can't be null/empty!");

        if (LOGGER.isDebugEnabled()){
            String pattern = "input params info,outputImageFile:[{}],contents:[{}],barcodeConfig:[{}]";
            LOGGER.debug(pattern, outputImageFile, contents, JsonUtil.format(barcodeConfig));
        }

        FileUtil.createDirectoryByFilePath(outputImageFile);

        encode(contents, FileUtil.getFileOutputStream(outputImageFile), barcodeConfig);
    }

    /**
     * 生成二维码(内嵌LOGO).
     *
     * @param contents
     *            内容 The contents to encode in the barcode
     * @param outputStream
     *            输出流
     * @param barcodeConfig
     *            the barcode config
     * @throws NullPointerException
     *             如果 <code>contents</code> 或者 <code>outputStream</code> 是null
     * @throws IllegalArgumentException
     *             如果 <code>contents</code> 是 blank
     * @see #createBarcodeBufferedImage(String, BarcodeConfig)
     * @see ImageUtil#write(RenderedImage, OutputStream, String)
     */
    public static void encode(String contents,OutputStream outputStream,BarcodeConfig barcodeConfig){
        Validate.notBlank(contents, "contents can't be null/empty!");
        Validate.notNull(outputStream, "outputStream can't be null!");

        BarcodeConfig useBarcodeConfig = defaultIfNull(barcodeConfig, new BarcodeConfig());

        BufferedImage bufferedImage = createBarcodeBufferedImage(contents, useBarcodeConfig);

        ImageUtil.write(bufferedImage, outputStream, useBarcodeConfig.getOutputImageFormatName());

        if (LOGGER.isDebugEnabled()){
            String pattern = "[create img success],contents:[{}],contents length:[{}],useBarcodeConfig:[{}],outputStream is:[{}]";
            LOGGER.debug(pattern, contents, contents.length(), JsonUtil.format(useBarcodeConfig), outputStream);
        }
    }

    //---------------------------------------------------------------

    /**
     * 创建 image.
     *
     * @param contents
     *            The contents to encode in the barcode
     * @param barcodeConfig
     *            the barcode config
     * @return the buffered image
     * @see #insertLogoImage(BufferedImage, BarcodeConfig)
     */
    private static BufferedImage createBarcodeBufferedImage(String contents,BarcodeConfig barcodeConfig){
        BufferedImage sourceBarcodeBufferedImage = getSourceBarcodeBufferedImage(contents, barcodeConfig);

        if (isNullOrEmpty(barcodeConfig.getLogoImagePath())){
            return sourceBarcodeBufferedImage;
        }

        // 插入logo图片  
        insertLogoImage(sourceBarcodeBufferedImage, barcodeConfig);
        return sourceBarcodeBufferedImage;
    }

    /**
     * 获得 source barcode buffered image.
     *
     * @param contents
     *            the contents
     * @param barcodeConfig
     *            the barcode config
     * @return the source barcode buffered image
     */
    private static BufferedImage getSourceBarcodeBufferedImage(String contents,BarcodeConfig barcodeConfig){
        int width = barcodeConfig.getWidth();
        int height = barcodeConfig.getHeight();

        BitMatrix bitMatrix = getBitMatrix(contents, barcodeConfig);

        BufferedImage sourceBarcodeBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; ++x){
            for (int y = 0; y < height; ++y){
                //Gets the requested bit, where true means black.
                boolean blackFlag = bitMatrix.get(x, y);
                sourceBarcodeBufferedImage.setRGB(x, y, blackFlag ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }
        return sourceBarcodeBufferedImage;
    }

    /**
     * 获得 bit matrix.
     *
     * @param contents
     *            the contents
     * @param barcodeConfig
     *            the barcode config
     * @return the bit matrix
     * @see com.google.zxing.MultiFormatWriter#encode(String, com.google.zxing.BarcodeFormat, int, int, Map)
     */
    private static BitMatrix getBitMatrix(String contents,BarcodeConfig barcodeConfig){
        Map<EncodeHintType, Object> hintsMap = new EnumMap<>(EncodeHintType.class);
        hintsMap.put(EncodeHintType.CHARACTER_SET, UTF8);
        //设置二维码排错率,可选L(7%)、M(15%)、Q(25%)、H(30%),排错率越高可存储的信息越少,但对二维码清晰度的要求越小
        hintsMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //二维码边框宽度
        hintsMap.put(EncodeHintType.MARGIN, barcodeConfig.getEncodeHintTypeMargin());//默认是4 com.google.zxing.qrcode.QRCodeWriter.QUIET_ZONE_SIZE

        //---------------------------------------------------------------
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BarcodeFormat barcodeFormat = barcodeConfig.getBarcodeFormat();
            return multiFormatWriter.encode(contents, barcodeFormat, barcodeConfig.getWidth(), barcodeConfig.getHeight(), hintsMap);
        }catch (WriterException e){
            throw new DefaultRuntimeException(e);
        }
    }

    /**
     * 插入LOGO.
     *
     * @param sourceBarcodeBufferedImage
     *            二维码图片
     * @param barcodeConfig
     *            the barcode config
     */
    private static void insertLogoImage(BufferedImage sourceBarcodeBufferedImage,BarcodeConfig barcodeConfig){
        Image srcImage = ImageUtil.getBufferedImage(barcodeConfig.getLogoImagePath());
        int width = srcImage.getWidth(null);
        int height = srcImage.getHeight(null);

        boolean needCompress = false;

        //TODO 注意逻辑
        Integer logoImageWidth = barcodeConfig.getLogoImageWidth();
        if (width > logoImageWidth){
            width = logoImageWidth;
            needCompress = true;
        }
        Integer logoImageHeight = barcodeConfig.getLogoImageHeight();
        if (height > logoImageHeight){
            height = logoImageHeight;
        }
        //---------------------------------------------------------------
        if (needCompress){ // 压缩LOGO  
            Image image = srcImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tagBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = tagBufferedImage.getGraphics();
            graphics.drawImage(image, 0, 0, null); // 绘制缩小后的图  
            graphics.dispose();
            srcImage = image;
        }
        //---------------------------------------------------------------
        // 插入LOGO  
        Graphics2D graphics2D = sourceBarcodeBufferedImage.createGraphics();
        int x = (barcodeConfig.getWidth() - width) / 2;
        int y = (barcodeConfig.getHeight() - height) / 2;

        graphics2D.drawImage(srcImage, x, y, width, height, null);

        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graphics2D.setStroke(new BasicStroke(3f));
        graphics2D.draw(shape);
        graphics2D.dispose();
    }
}
