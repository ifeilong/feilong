feilong 4.0.1 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>4.0.1</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:4.0.1
```


本次升级共有 `11` 处变更, 具体参见  [4.0.1 milestone](https://github.com/ifeilong/feilong/milestone/34?closed=1)



## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A4.0.1+is%3Aclosed)

#611  修改 com.feilong.core.lang.SystemUtil.getSystemProperty(String) 为public
#610 :sparkles: 新建 com.feilong.core.lang.ObjectUtil.defaultZero(Integer)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.1+is%3Aclosed)]
#612 :sparkles: 新建 com.feilong.core.lang.SystemUtil.getEnv(String)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.1+is%3Aclosed)]
#601 :sparkles: 新建 com.feilong.core.util.CollectionsUtil.contains(Iterable<String>, String) 方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.1+is%3Aclosed)]
#606 :sparkles: 新建 com.feilong.core.util.ResourceBundleUtil.toBean(ResourceBundle, Class<T>)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.1+is%3Aclosed)]
#603 :sparkles: 新建直接的com.feilong.core.util.ResourceBundleUtil.toBean(String, Class<T>)方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.1+is%3Aclosed)]


## :fishing_pole_and_fish: [feilong-io](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-io+milestone%3A4.0.1+is%3Aclosed)

#608 :sparkles: 新建 com.feilong.io.IOReaderUtil.readToByteArray(String)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.1+is%3Aclosed)]


## :wine_glass: [feilong-net](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net+milestone%3A4.0.1+is%3Aclosed)

#596 :sparkles: http支持字节流请求体功能 HttpRequest 新增requestByteArrayBody  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.1+is%3Aclosed)]
#602 :sparkles: 支持http 根据不同环境 日志是format 还是tostring 输出  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.1+is%3Aclosed)]
#607 :sparkles: 新建 com.feilong.net.http.HttpClientUtil.getHttpResponse(HttpRequest)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.1+is%3Aclosed)]
#609 :sparkles: 新建 com.feilong.net.http.HttpLogHelper  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.1+is%3Aclosed)]
