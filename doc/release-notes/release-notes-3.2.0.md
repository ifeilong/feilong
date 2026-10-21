feilong 3.2.0 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>3.2.0</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:3.2.0
```


本次升级共有 `10` 处变更, 具体参见  [3.2.0 milestone](https://github.com/ifeilong/feilong/milestone/15?closed=1)



## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A3.2.0+is%3Aclosed)

#60 :sparkles: Validator 添加一个 类似于  StringUtils.isAnyBlank(CharSequence...)  的方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.2.0+is%3Aclosed)]


## :lock: [feilong-security](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-security+milestone%3A3.2.0+is%3Aclosed)

#382  修改 com.feilong.security.oneway.Sm3Util 注释  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A3.2.0+is%3Aclosed)]


## :baby_bottle: [feilong-servlet](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-servlet+milestone%3A3.2.0+is%3Aclosed)

#153  com.feilong.servlet.ServletContextUtil.EXCLUDE_KEYS 做成配置文件


## :arrow_up: [依赖升级](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖升级+milestone%3A3.2.0+is%3Aclosed)

#384  maven-assembly-plugin 升级到 3.4.2
#398  maven-install-plugin 升级到 3.0.1
#399  maven-resources-plugin 升级到 3.3.0	
#383  升级 bcprov-jdk15on>1.70
#381  升级 jsoup 1.15.2


## :heavy_minus_sign: [依赖调整](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖调整+milestone%3A3.2.0+is%3Aclosed)

#205  用 log4j2 替代 log4j


## [其他](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A其他+milestone%3A3.2.0+is%3Aclosed)

#380 :fire: 删除 maven-antrun-plugin 依赖管理  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.2.0+is%3Aclosed)]
