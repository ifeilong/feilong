feilong 3.3.0 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/

maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>3.3.0</version>
</dependency>
```

Gradle 依赖配置:

```java
com.github.ifeilong:feilong:3.3.0
```

本次升级共有 `3` 处变更, 具体参见  [3.3.0 milestone](https://github.com/ifeilong/feilong/milestone/19?closed=1)

## :wine_glass: [feilong-net](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net+milestone%3A3.3.0+is%3Aclosed)

#371 :sparkles: httpclient 支持retry功能  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.0+is%3Aclosed)]

主要处理, httpclient默认 `java.net.SocketTimeoutException`,  `org.apache.http.conn.ConnectTimeoutException` 两个超时异常不重试的问题

## [feilong-net-bot](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net-bot+milestone%3A3.3.0+is%3Aclosed)

#417  新增钉钉机器人/企业微信机器人`自动捕获异常`  

消息机器人报错比如超时等不影响大局 支持参数设置

只需要设置 `isCatchException="true"` 即可

example:

```XML
<feilong:dingtalkBot id="dingtalkBot" accessToken="********" secret="********" defaultTitle="aaa" isCatchException="true" />
```

#365  钉钉机器人/企业微信机器人 `支持异步`

只需要设置 `isAsync="true"` 即可

example:

```XML
<feilong:dingtalkBot id="dingtalkBot" accessToken="********" secret="********" defaultTitle="aaa" isAsync="true" />
```
