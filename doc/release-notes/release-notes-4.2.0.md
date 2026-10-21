feilong 4.2.0 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>4.2.0</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:4.2.0
```


本次升级共有 `7` 处变更, 具体参见  [4.2.0 milestone](https://github.com/ifeilong/feilong/milestone/46?closed=1)



## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A4.2.0+is%3Aclosed)

#744 :sparkles: 新建com.feilong.core.util.MapUtil.newLinkedHashMap(Iterable<K>, V) 支持传入keys 返回指定值的map  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.2.0+is%3Aclosed)]


## :wine_glass: [feilong-net](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net+milestone%3A4.2.0+is%3Aclosed)

#745 :sparkles: 新建 com.feilong.net.http.HttpClientUtil.getHttpFullInfo(HttpRequest, ConnectionConfig) http工具类要支持 返回请求参数和请求链接和请求响应结果 方便保存到数据库  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.2.0+is%3Aclosed)]


## :arrow_up: [依赖升级](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖升级+milestone%3A4.2.0+is%3Aclosed)

#719  maven-deploy-plugin 升级到 3.1.3
#748  maven-javadoc-plugin 升级到3.10.1
#728  maven-plugin-plugin	3.10.2		3.15.0
#727  maven-release-plugin	3.0.1			3.1.1
#746  spring 相关依赖 dependencyManagement 依赖升级到	5.3.39
