feilong 4.1.0 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>4.1.0</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:4.1.0
```


本次升级共有 `11` 处变更, 具体参见  [4.1.0 milestone](https://github.com/ifeilong/feilong/milestone/43?closed=1)



## :bug: [bug](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Abug+milestone%3A4.1.0+is%3Aclosed)

#706  com.feilong.net.http.HttpLogHelper.autoLog(HttpRequest, ConnectionConfig, String, Object...) 死循环了  [[feilong-net](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net+milestone%3A4.1.0+is%3Aclosed)]


## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A4.1.0+is%3Aclosed)

#708 :sparkles: 新建 ConvertUtil.toStringList(Object) 将list 转成 list<String> 的转换方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.1.0+is%3Aclosed)]


## :guitar: [feilong-context](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-context+milestone%3A4.1.0+is%3Aclosed)

#704 :sparkles: com.feilong.context.invoker.AbstractResponseCommandBuilder.createStringToBeanConverter() 新增responseString 参数,方便实现类基于responseString 来决策转换器  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.1.0+is%3Aclosed)]
#703 :sparkles: com.feilong.context.invoker.ResponseCommandBuilder<R, T> 泛型T extends ResponseCommand 去掉, 要支持list 等  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.1.0+is%3Aclosed)]


## :melon: [feilong-email](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-email+milestone%3A4.1.0+is%3Aclosed)

#702  丰富 com.feilong.net.mail.util.MessageSendUtil.send(Message) 异常日志  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A4.1.0+is%3Aclosed)  [log](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Alog+milestone%3A4.1.0+is%3Aclosed)]


## :meat_on_bone: [feilong-json](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-json+milestone%3A4.1.0+is%3Aclosed)

#705  com.feilong.json.JsonHelper.isNeedConvertToJSONArray(Object) 改成 public


## :robot: [feilong-net-bot](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net-bot+milestone%3A4.1.0+is%3Aclosed)

#709 :sparkles: 新建 com.feilong.net.bot.LogAndBotCombination.debug(CombinationConfig, String, Object...) 机器人支持是否发机器人的开关  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.1.0+is%3Aclosed)]
#712 :sparkles: 新建com.feilong.net.bot.LogAndBotCombination.error(CombinationConfig, String, Object...)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.1.0+is%3Aclosed)]
#710 :sparkles: 新建com.feilong.net.bot.LogAndBotCombination.info(CombinationConfig, String, Object...)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.1.0+is%3Aclosed)]
#711 :sparkles: 新建com.feilong.net.bot.LogAndBotCombination.warn(CombinationConfig, String, Object...)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.1.0+is%3Aclosed)]


## [feilong-net-filetransfer](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net-filetransfer+milestone%3A4.1.0+is%3Aclosed)

#713  丰富 com.feilong.net.filetransfer.sftp.SFTPFileTransfer javadoc  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A4.1.0+is%3Aclosed)]
