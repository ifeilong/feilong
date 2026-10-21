feilong 3.3.2 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>3.3.2</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:3.3.2
```


本次升级共有 `12` 处变更, 具体参见  [3.3.2 milestone](https://github.com/ifeilong/feilong/milestone/21?closed=1)



## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A3.3.2+is%3Aclosed)

#424 :sparkles: convertutil tolong 也新增个默认值的参数  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.2+is%3Aclosed)]
#426 :sparkles: 新增  ConvertUtil.toBoolean(Object, Boolean) 显性默认值的方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.2+is%3Aclosed)]
#427 :sparkles: 新增  StringUtil.newString(byte[]) 默认使用utf-8  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.2+is%3Aclosed)]
#425 :sparkles: 新增 ConvertUtil.toBigDecimal(Object, BigDecimal) 带显性默认值的方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.2+is%3Aclosed)]
#433 :sparkles: 新建 ObjectUtil.defaultIfNullOrLessThanOne 的方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.2+is%3Aclosed)]
#434 :sparkles: 新建 com.feilong.core.lang.ObjectUtil.defaultPageNo(Integer)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.2+is%3Aclosed)]


## :baby_bottle: [feilong-servlet](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-servlet+milestone%3A3.3.2+is%3Aclosed)

#423 :sparkles: cookieutil 新增自动转int  转long 等方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.2+is%3Aclosed)]
#432 :sparkles: 新增 RequestUtil.getRequestInfoFullMapForLog(HttpServletRequest) 用来简化  RequestUtil.getRequestInfoMapForLog(HttpServletRequest, RequestLogSwitch.Full)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.2+is%3Aclosed)]


## :arrow_up: [依赖升级](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖升级+milestone%3A3.3.2+is%3Aclosed)

#429  log4j 升级到2.19.0
#428  logback 升级到 1.4.1
#430  maven-jar-plugin 升级到3.3.0	
#431  versions-maven-plugin 升级到 2.12.0	
