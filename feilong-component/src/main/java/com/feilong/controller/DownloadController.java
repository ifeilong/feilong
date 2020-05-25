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
package com.feilong.controller;

import static com.feilong.core.date.DateUtil.formatDuration;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.feilong.context.filecreator.RequestFileCreator;
import com.feilong.context.filecreator.RequestFileCreatorDetector;
import com.feilong.servlet.http.ResponseDownloadUtil;

/**
 * 下载.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.0 2012-3-18 下午11:32:05
 */
@Controller
public class DownloadController{

    /** The Constant LOGGER. */
    private static final Logger        LOGGER  = LoggerFactory.getLogger(DownloadController.class);

    //---------------------------------------------------------------

    private final AtomicInteger        counter = new AtomicInteger(0);

    @Autowired
    private RequestFileCreatorDetector requestFileCreatorDetector;

    //---------------------------------------------------------------

    @RequestMapping(value = "/download",method = { GET, POST })
    public void download(HttpServletRequest request,HttpServletResponse response){
        Date beginDate = new Date();

        RequestFileCreator requestFileCreator = requestFileCreatorDetector.detect(request);

        String filePath = requestFileCreator.create(request);
        Validate.notBlank(filePath, "filePath can't be blank!");

        //---------------------------------------------------------------
        ResponseDownloadUtil.download(filePath, request, response);
        int incrementAndGet = counter.incrementAndGet();

        if (LOGGER.isInfoEnabled()){
            String pattern = "download times:[{}],requestFileCreator:[{}],use time: [{}]";
            LOGGER.info(pattern, incrementAndGet, requestFileCreator.getClass().getSimpleName(), formatDuration(beginDate));
        }
    }
}