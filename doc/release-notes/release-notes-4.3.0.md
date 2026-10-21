feilong 4.3.0 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>4.3.0</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:4.3.0
```


本次升级共有 `10` 处变更, 具体参见  [4.3.0 milestone](https://github.com/ifeilong/feilong/milestone/47?closed=1)



## :bug: [bug](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Abug+milestone%3A4.3.0+is%3Aclosed)

#756 :sparkles: rename com.feilong.core.lang.NumberUtil.isGatherThan(Number, Number) to isGreaterThan  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.3.0+is%3Aclosed)  [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A4.3.0+is%3Aclosed)]
#757 :sparkles: rename com.feilong.core.lang.NumberUtil.isGatherThanOrEquals(Number, Number) to  isGreaterThanOrEquals  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.3.0+is%3Aclosed)  [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A4.3.0+is%3Aclosed)]


## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A4.3.0+is%3Aclosed)

#754 :sparkles: 新建 com.feilong.core.lang.ObjectUtil.defaultCountIfNullZeroOrGreaterThan(Integer, Integer) 当数字大于指定值时, 返回指定值 ,   [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.3.0+is%3Aclosed)]


## :wine_glass: [feilong-net](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net+milestone%3A4.3.0+is%3Aclosed)

#749  com.feilong.net.http.ConnectionConfig 要支持tostring方法
#755 :sparkles: com.feilong.net.http.HttpClientUtil.getHttpFullInfo(HttpRequest, ConnectionConfig) 要支持自定义返回值类型转换  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.3.0+is%3Aclosed)]
#750  com.feilong.net.http.HttpRequest 要支持 tostring方法
#751  com.feilong.net.http.HttpResponse 要支持tostring 方法


## :robot: [feilong-net-bot](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net-bot+milestone%3A4.3.0+is%3Aclosed)

#752 :sparkles: 新建 com.feilong.net.bot.MarkdownStringCreator.createListString(String, Map<String, String>) 机器人要支持这种自从换行markdown格式  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.3.0+is%3Aclosed)]


## :baby_bottle: [feilong-servlet](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-servlet+milestone%3A4.3.0+is%3Aclosed)

#753 :sparkles: 新建 com.feilong.context.log.RequestLogHelper.autoRequestInfo(HttpServletRequest, String, Object...) 解决 autoRequestInfo(String, Object...) 在filter 中取不到request 信息问题  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.3.0+is%3Aclosed)  [feilong-context](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-context+milestone%3A4.3.0+is%3Aclosed)]


## :arrow_up: [依赖升级](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖升级+milestone%3A4.3.0+is%3Aclosed)

#758  maven-project-info-reports-plugin 升级到3.8.0
