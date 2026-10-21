feilong 4.1.2 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>4.1.2</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:4.1.2
```


本次升级共有 `12` 处变更, 具体参见  [4.1.2 milestone](https://github.com/ifeilong/feilong/milestone/45?closed=1)



## :bug: [bug](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Abug+milestone%3A4.1.2+is%3Aclosed)

#736  ResponseDownloadUtil 没有设置文件大小, 空指针, 但这个文件大小只做日志用的  [[feilong-servlet](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-servlet+milestone%3A4.1.2+is%3Aclosed)]


## :fishing_pole_and_fish: [feilong-io](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-io+milestone%3A4.1.2+is%3Aclosed)

#739 :sparkles: 新建 com.feilong.io.IOReaderUtil.readToList  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.1.2+is%3Aclosed)]


## :arrow_up: [依赖升级](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖升级+milestone%3A4.1.2+is%3Aclosed)

#730  maven-dependency-plugin	3.6.1			3.8.0
#729  maven-javadoc-plugin	3.6.3			3.10.0
#722  maven-project-info-reports-plugin 3.5.0升级3.7.0
#723  maven-scm-plugin 2.0.1 升级2.1.0
#742  maven-site-plugin  3.12.1 升级 3.20.0
#725  maven-surefire-plugin 3.2.3 升级3.5.0	
#743  taglist-maven-plugin 升级到 3.1.0


## [其他](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A其他+milestone%3A4.1.2+is%3Aclosed)

#740  com.feilong.context.converter.StringToBeanConverter接口移动到 feilong-core包 路径保持不变
#738  com.feilong.core.lang.StringUtil.toSingleValueMap(String, Class<T>, Class<V>) 要支持定制转换value类
#741  标识 com.feilong.context.converter.StringToBeanConverter接口 @FunctionalInterface
