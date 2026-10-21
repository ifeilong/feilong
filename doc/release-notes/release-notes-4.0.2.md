feilong 4.0.2 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>4.0.2</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:4.0.2
```


本次升级共有 `7` 处变更, 具体参见  [4.0.2 milestone](https://github.com/ifeilong/feilong/milestone/35?closed=1)



## :meat_on_bone: [feilong-json](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-json+milestone%3A4.0.2+is%3Aclosed)

#617  完善 com.feilong.json.JsonToJavaConfig.classMap javadoc  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A4.0.2+is%3Aclosed)]
#618 :sparkles: 新建 LongToDateMorpher,支持json转bean 的时候 自动将时间戳long类型转成Date类型 ,比如 `{'date':1609948800000} `可以自动转成Date类型   [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.2+is%3Aclosed)]


## :arrow_up: [依赖升级](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖升级+milestone%3A4.0.2+is%3Aclosed)

#620  log4j2 依赖升级到 2.21.1
#623  maven-clean-plugin 升级到3.3.2
#621  maven-dependency-plugin 升级到 3.6.1
#622  maven-javadoc-plugin 升级到3.6.2
#619  springframework 依赖升级到5.3.30
