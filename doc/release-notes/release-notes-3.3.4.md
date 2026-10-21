feilong 3.3.4 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>3.3.4</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:3.3.4
```


本次升级共有 `12` 处变更, 具体参见  [3.3.4 milestone](https://github.com/ifeilong/feilong/milestone/23?closed=1)



## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A3.3.4+is%3Aclosed)

#447 :sparkles: DateUtil.getTime 支持参数null,返回null  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.4+is%3Aclosed)]
#444 :sparkles: 新建 StringUtil.toMultiValueMap(String, Class<T>, Class<V>)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.4+is%3Aclosed)]
#450 :sparkles: 新建 StringUtil.toSingleValueMap(String, Class<T>, Class<V>)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.4+is%3Aclosed)]
#442 :sparkles: 新建 CollectionsUtil.containsTrimAndIgnoreCase(Iterable<String>, String), 忽视大小写包含  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.4+is%3Aclosed)]
#445 :sparkles: 新建 ConvertUtil.toLongList(Object) ConvertUtil.toIntegerList(Object)  支持字符串 直接转换成Long/Integer list 的方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.4+is%3Aclosed)  [重要](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A重要+milestone%3A3.3.4+is%3Aclosed)]
#443 :sparkles: 新建 Slf4jUtil.isEnabledDebug(String)   isEnabledInfo(String)  指定level 级别判断是否可以输出日志  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.4+is%3Aclosed)]



## :meat_on_bone: [feilong-json](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-json+milestone%3A3.3.4+is%3Aclosed)

#449 :sparkles: Jsonutil format相关方法, 如果出现异常返回null,而不要抛出异常  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.4+is%3Aclosed)]
#446 :sparkles: Jsonutil to bean 如果字符串是空, 返回null  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.4+is%3Aclosed)]


## :arrow_up: [依赖升级](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖升级+milestone%3A3.3.4+is%3Aclosed)

#451  logback 升级到  1.4.4
#456  maven-scm-plugin 升级到 1.13.0
#455  maven-site-plugin 升级到 3.12.1
#452  versions-maven-plugin 升级到 2.13.0
