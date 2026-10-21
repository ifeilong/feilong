feilong 4.1.1 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>4.1.1</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:4.1.1
```


本次升级共有 `11` 处变更, 具体参见  [4.1.1 milestone](https://github.com/ifeilong/feilong/milestone/44?closed=1)



## [feilong-net-filetransfer](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net-filetransfer+milestone%3A4.1.1+is%3Aclosed)

#716 :sparkles: FTPFileTransfer 支持传入 logTraceContext   [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.1.1+is%3Aclosed)]
#715 :sparkles: SFTPFileTransfer 要支持传入 logTraceContext  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.1.1+is%3Aclosed)]
#714 :sparkles: 优化 com.feilong.net.filetransfer.AbstractFileTransfer disconnect方法 放到 finally体关闭  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.1.1+is%3Aclosed)]
#734  优化 com.feilong.net.filetransfer.AbstractFileTransfer.uploadFile(String, String, String) 使用 try-with-resources 关闭inputstream流写法
#735  修改 com.feilong.net.filetransfer.AbstractFileTransfer.upload(FileInputStream, String) javadoc  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A4.1.1+is%3Aclosed)]


## :arrow_up: [依赖升级](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖升级+milestone%3A4.1.1+is%3Aclosed)

#732  log4j2 升级到 2.23.1
#717  maven-clean-plugin 升级到 3.4.0
#718  maven-compiler-plugin 升级到3.13.0
#720  maven-install-plugin 3.1.1 升级到3.1.2
#726  nexus-staging-maven-plugin 1.6.13 1.6.14 1.7.0
#733  springframework升级到 5.3.37 optional=true 依赖
