feilong 4.3.1 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>4.3.1</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:4.3.1
```


本次升级共有 `6` 处变更, 具体参见  [4.3.1 milestone](https://github.com/ifeilong/feilong/milestone/48?closed=1)



## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A4.3.1+is%3Aclosed)

#774 :sparkles: 新增 ObjectUtil.defaultFalseIfNull  方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.3.1+is%3Aclosed)]
#775 :sparkles: 新增 ObjectUtil.defaultTrueIfNull  方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.3.1+is%3Aclosed)]
#777 :sparkles: 新建 com.feilong.core.lang.StringUtil.toMultiValueMap(String)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.3.1+is%3Aclosed)]
#778 :sparkles: 新建 com.feilong.core.lang.StringUtil.toMultiValueMap(String, Class<V> valueElementClass) 方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.3.1+is%3Aclosed)]
#760 :sparkles: 新建 com.feilong.core.lang.StringUtil.tokenizeToList(String, Class<T>) 支持直接类型转换的字符串解析方法 , 比如可以转成list<Long>  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.3.1+is%3Aclosed)]


## :meat_on_bone: [feilong-json](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-json+milestone%3A4.3.1+is%3Aclosed)

#776 :sparkles: 新建 com.feilong.json.JsonUtil.toString(Object, String...excludes)方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.3.1+is%3Aclosed)]
