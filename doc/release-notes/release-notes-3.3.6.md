feilong 3.3.6 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>3.3.6</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:3.3.6
```


本次升级共有 `8` 处变更, 具体参见  [3.3.6 milestone](https://github.com/ifeilong/feilong/milestone/25?closed=1)



## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A3.3.6+is%3Aclosed)

#465 :sparkles: 修改 com.feilong.core.util.CollectionsUtil.removeAll(Collection<O>, Collection<O>) 支持第二个参数是null 直接返回第一个参数  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.6+is%3Aclosed)]
#468 :sparkles: 新建 ObjectUtil.defaultEmptyStringIfNull(String)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.6+is%3Aclosed)]
#469 :sparkles: 新建 ObjectUtil.defaultNewArrayListIfNull  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.6+is%3Aclosed)]
#471 :sparkles: 新建 ObjectUtil.defaultNewHashMapIfNull  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.6+is%3Aclosed)]
#470 :sparkles: 新建 ObjectUtil.defaultNewHashSetIfNull  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.6+is%3Aclosed)]
#472 :sparkles: 新建 ObjectUtil.defaultNewLinkedHashMapIfNull  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.6+is%3Aclosed)]


## :wine_glass: [feilong-net](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net+milestone%3A3.3.6+is%3Aclosed)

#464 :sparkles: 新建LogAndBotCombination  和日志一起的输出的bot 工具类   [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.6+is%3Aclosed)]


## :arrow_up: [依赖升级](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖升级+milestone%3A3.3.6+is%3Aclosed)

#473  maven-install-plugin 升级到3.1.0
