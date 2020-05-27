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

import java.util.List;

import javax.mail.search.SearchTerm;

import com.feilong.net.mail.entity.MailInfo;
import com.feilong.net.mail.entity.MailReaderConfig;

/**
 * 邮件读取器.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.0.9
 */
public interface MailReader{

    /**
     * 获得所有的邮件列表(不带筛选条件).
     *
     * @param mailReaderConfig
     *            the mail reader config
     * @return the mail info list
     */
    List<MailInfo> read(MailReaderConfig mailReaderConfig);

    /**
     * 获得所有的邮件列表.
     * 
     * <p>
     * 你可以通过 newstIndex来指定 最新的多少条,以及通过 searchTerm来设置筛选条件
     * </p>
     *
     * @param mailReaderConfig
     *            the mail reader config
     * @param newstIndex
     *            最新的多少条
     * @param searchTerm
     *            筛选条件
     * @return the mail info list
     */
    List<MailInfo> read(MailReaderConfig mailReaderConfig,Integer newstIndex,SearchTerm searchTerm);
}
