feilong 4.0.5 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>4.0.5</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:4.0.5
```

本次升级共有 `2` 处变更, 具体参见  [4.0.5 milestone](https://github.com/ifeilong/feilong/milestone/39?closed=1)


## :fishing_pole_and_fish: [feilong-io](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-io+milestone%3A4.0.5+is%3Aclosed)

#639 :sparkles: FilenameUtil 新建 `boolean isExtensionIgnoreCase(String filename,String...extensions)` 判断是否是指定后缀的文件(忽视大小写)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.5+is%3Aclosed)]

## :meat_on_bone: [feilong-json](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-json+milestone%3A4.0.5+is%3Aclosed)

#640  完善 json 转java  类型不匹配详细日志  java.lang.Integer"   but expected signature "java.util.Date"  [[log](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Alog+milestone%3A4.0.5+is%3Aclosed)]
