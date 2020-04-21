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
package com.feilong.component;

import static com.feilong.core.date.DateUtil.nowTimestamp;

import java.util.ResourceBundle;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.junit.Test;

import com.feilong.context.DataListQuery;
import com.feilong.core.bean.BeanUtil;
import com.feilong.core.util.ResourceBundleUtil;
import com.feilong.io.FileUtil;
import com.feilong.net.mail.entity.MailSenderConfig;

public class DataFileZipEmailComponentTest{

    /** The data list query. */
    private final DataListQuery<DefaultTradeData>                       dataListQuery       = new TradeDataDataListQuery();

    /** The output stream builder. */
    private final LoxiaSimpleListLoopExcelFileCreator<DefaultTradeData> listDataFileCreator = new LoxiaSimpleListLoopExcelFileCreator<>();

    private final MailSenderConfig                                      mailSenderConfig    = loadMailSenderConfig();
    {
        listDataFileCreator.setExcelTemplateLocation(
                        "/Users/feilong/workspace/feilong/feilong-incubator/feilong-component/src/test/resources/loxia/TradeData/TradeData-list-export.xlsx");
        listDataFileCreator.setXmlSheetConfigurations("loxia/TradeData/feilong-sheets-TradeData.xml");
        listDataFileCreator.setSheetName("sheet");
        listDataFileCreator.setDataName("list");

        listDataFileCreator.setOutputFilePathExpression("/Users/feilong/Downloads/[1]" + nowTimestamp() + ".xlsx");
    }

    private final DataFileZipEmailComponent<DefaultTradeData> component = new DataFileZipEmailComponent<>();
    {
        component.setDataListQuery(dataListQuery);
        component.setListDataFileCreator(listDataFileCreator);
    }
    //---------------------------------------------------------------

    @Test
    public void run(){
        mailSenderConfig.setTos("venusdrogon@163.com", "xin.jin@baozun.com");
        mailSenderConfig.setSubject("hello report");
        mailSenderConfig.setContent("heelo");

        component.setMailSenderConfig(mailSenderConfig);

        component.run();
    }

    //---------------------------------------------------------------

    /** The Constant folder. */
    private static final String folder = "/Users/feilong/Development/DataCommon/Files/Java/config/";

    private MailSenderConfig loadMailSenderConfig(){
        ResourceBundle resourceBundle = ResourceBundleUtil.getResourceBundle(FileUtil.getFileInputStream(folder + getConfigFile()));

        ArrayConverter arrayConverter = new ArrayConverter(String[].class, new StringConverter(), 2);
        char[] allowedChars = { '@' };
        arrayConverter.setAllowedChars(allowedChars);
        ConvertUtils.register(arrayConverter, String[].class);

        //---------------------------------------------------------------

        MailSenderConfig mailSenderConfig = new MailSenderConfig();
        mailSenderConfig = BeanUtil.populate(mailSenderConfig, ResourceBundleUtil.toMap(resourceBundle));
        return mailSenderConfig;
    }

    /**
     * Gets the config file.
     *
     * @return the config file
     */
    private String getConfigFile(){
        return "mail-feilongtestemail.properties";
    }

}
