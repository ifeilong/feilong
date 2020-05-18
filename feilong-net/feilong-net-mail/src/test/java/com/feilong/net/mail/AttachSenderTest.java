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
package com.feilong.net.mail;

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.date.DateUtil.getTime;
import static com.feilong.core.date.DateUtil.now;
import static com.feilong.core.util.MapUtil.newHashMap;
import static com.feilong.io.entity.FileType.FILE;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.feilong.io.entity.FileInfoEntity;
import com.feilong.lib.lang3.SystemUtils;
import com.feilong.net.mail.builder.setter.AttachmentSetter;
import com.feilong.template.TemplateUtil;

public class AttachSenderTest extends AbstractMailSenderTest{

    @Test
    public void sendMailWithAttach(){
        String templateInClassPath = "velocity/mailtest.vm";

        //---------------------------------------------------------------
        List<FileInfoEntity> fileInfoEntityList = buildList();

        //---------------------------------------------------------------
        //        另外，如果要做内嵌或发送图片，你应该使用信用较高的邮箱帐户，否则会报错：
        //        554 DT:SPM 发送的邮件内容包含了未被许可的信息，或被系统识别为垃圾邮件。请检查是否有用户发送病毒或者垃圾邮件

        Map<String, Object> contextKeyValues = newHashMap();
        contextKeyValues.put("PREFIX_CONTENTID", AttachmentSetter.PREFIX_CONTENTID);
        contextKeyValues.put("fileInfoEntityList", fileInfoEntityList);

        //---------------------------------------------------------------
        mailSendRequest.setContent(TemplateUtil.parseTemplate(templateInClassPath, contextKeyValues));

        //  String fileString = "E:\\DataFixed\\Material\\avatar\\飞龙.png";
        String fileString = SystemUtils.USER_HOME + "/DataFixed/Material/头像avatar/飞龙.png";
        // mailSenderConfig.setAttachFilePaths(fileString);
    }

    private List<FileInfoEntity> buildList(){
        return toList(
                        new FileInfoEntity("feilongstore_china_cancel20130910.csv", FILE, 25655L, getTime(now())),
                        new FileInfoEntity("feilongstore_china_revenue20131022.csv", FILE, 25655L, getTime(now())),
                        new FileInfoEntity("feilongstore_china_return20131022.csv", FILE, 25655L, getTime(now())),
                        new FileInfoEntity("feilongstore_china_demand20130910.csv", FILE, 25655L, getTime(now())));
    }
}