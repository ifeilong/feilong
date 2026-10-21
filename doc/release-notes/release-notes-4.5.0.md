feilong 4.5.0 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: https://feilong.gitbook.io/feilong-docs 

从该版本起, 使用gitbook文档, 代替原oschina托管的文档

<img width="2356" height="1442" alt="image" src="https://github.com/user-attachments/assets/1b899dfd-b324-4ac6-969e-38459a04d153" />


maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>4.5.0</version>
</dependency>
```

Gradle 依赖配置:

```java
com.github.ifeilong:feilong:4.5.0
```


本次升级共有 `11` 处变更, 具体参见  [4.5.0 milestone](https://github.com/ifeilong/feilong/milestone/50?closed=1)



## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A4.5.0+is%3Aclosed)

#551 :sparkles: CollectionsUtil.groupOne(Iterable<BillInvoiceRequestApplyResultDto>, String) 升级到lambda  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.5.0+is%3Aclosed)]
#772 :sparkles: 新建  com.feilong.core.util.CollectionsUtil.containsAny 方法  支持动态参数  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.5.0+is%3Aclosed)]


## :guitar: [feilong-context](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-context+milestone%3A4.5.0+is%3Aclosed)

#792 :sparkles: LogContextExecutor 将catch exception 改成 catch throwable  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.5.0+is%3Aclosed)]


## :arrow_up: [依赖升级](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖升级+milestone%3A4.5.0+is%3Aclosed)

#793  logback 升级到1.3.16
#794  maven clean plugin 升级到 3.5.0
#731  maven-assembly-plugin 3.8.0
#795  maven-compiler-plugin 升级到 3.14.1
#721  maven-jar-plugin 3.3.0升级到3.5.0
#798  maven-javadoc-plugin 升级到 3.12.0
#796  maven-project-info-reports-plugin 升级到  3.9.0
#797  maven-surefire-plugin 升级到3.5.4
