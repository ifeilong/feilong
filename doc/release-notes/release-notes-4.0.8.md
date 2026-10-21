feilong 4.0.8 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>4.0.8</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:4.0.8
```


本次升级共有 `28` 处变更, 具体参见  [4.0.8 milestone](https://github.com/ifeilong/feilong/milestone/42?closed=1)



## :bug: [bug](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Abug+milestone%3A4.0.8+is%3Aclosed)

#679  com.feilong.lib.json.ToStringUtil.OBJECT_START 是不是写反了 删除 com.feilong.lib.json.ToStringUtil.OBJECT_START  [[feilong-json](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-json+milestone%3A4.0.8+is%3Aclosed)]


## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A4.0.8+is%3Aclosed)

#701 :sparkles: com.feilong.core.lang.StringUtil 新增 NBSP常量  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.8+is%3Aclosed)]
#681  完善com.feilong.core.util.ResourceBundleUtil.toBean(ResourceBundle, Class<T>) 日志  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A4.0.8+is%3Aclosed)]


## :guitar: [feilong-context](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-context+milestone%3A4.0.8+is%3Aclosed)

#684  com.feilong.spring.web.util.WebSpringUtil.getRequest() 应该用 currentRequestAttributes  [[代码优化](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A代码优化+milestone%3A4.0.8+is%3Aclosed)]


## :meat_on_bone: [feilong-json](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-json+milestone%3A4.0.8+is%3Aclosed)

#668   JsonToJavaConfig.setJavaIdentifierTransformer(JavaIdentifierTransformer) javadoc补充下  SeparatorToCamelCaseJavaIdentifierTransformer  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A4.0.8+is%3Aclosed)]


## :wine_glass: [feilong-net](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net+milestone%3A4.0.8+is%3Aclosed)

#688  HttpRequest 新增一个 logTraceContext 表示, 让这个调用的所有log 都追加这个logkey 方便排查问题
#690  com.feilong.net.http.HttpLogHelper.createHttpRequestLog(HttpRequest) 增加javadoc  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A4.0.8+is%3Aclosed)]


## :robot: [feilong-net-bot](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net-bot+milestone%3A4.0.8+is%3Aclosed)

~~#683 :wastebasket: com.feilong.net.bot.dingtalk.DingTalkBot.sendMessage(String, String, String...)  sendMessage过期   [[deprecated](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Adeprecated+milestone%3A4.0.8+is%3Aclosed)]~~
#667 :sparkles: 新增 com.feilong.net.bot.Bot.sendMessage(String, MessageParams) 机器人log组件要支持at人  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.8+is%3Aclosed)]


## :lock: [feilong-security](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-security+milestone%3A4.0.8+is%3Aclosed)

#699  OnewayEncryption 改成public
#698  OnewayType 改成public 并且完善javadoc
#437 :sparkles: com.feilong.security.oneway.SHA256Util.encodeUpperCase(String) 新增大写的结果  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.8+is%3Aclosed)]
#692 :sparkles: 新建 com.feilong.security.oneway.SHA1Util.encodeUpperCase(String) 返回大写结果  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.8+is%3Aclosed)]
#694 :sparkles: 新建 com.feilong.security.oneway.SHA384Util.encodeUpperCase(String) 返回大写结果  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.8+is%3Aclosed)]
#695 :sparkles: 新建 com.feilong.security.oneway.SHA512Util.encodeUpperCase(String) 返回大写的结果  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.8+is%3Aclosed)]
#697 :sparkles: 新建 com.feilong.security.oneway.Sm3Util.encodeUpperCase(String) 返回大写结果  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.8+is%3Aclosed)]
#687 :sparkles: 新建com.feilong.security.oneway.MD5Util.encodeUpperCase(String) md5 encode 大写  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.8+is%3Aclosed)]
#693  更新 com.feilong.security.oneway.SHA384Util javadoc  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A4.0.8+is%3Aclosed)]
#696  更新 com.feilong.security.oneway.SHA512Util javadoc  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A4.0.8+is%3Aclosed)]
#691  更新com.feilong.security.oneway.SHA256Util  javadoc  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A4.0.8+is%3Aclosed)]


## :arrow_up: [依赖升级](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖升级+milestone%3A4.0.8+is%3Aclosed)

#670  (dependencyManagement)logback 升级到 1.3.14
#672  (only for test) log4j  升级到 2.22.0
#682  log4j 2 升级到 2.22.1
#673  maven-javadoc-plugin 升级到3.6.3
#674  maven-plugin-plugin 升级到	3.10.2
#676  maven-surefire-plugin 升级到3.2.3
#671  spring 升级到  5.3.31


## [其他](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A其他+milestone%3A4.0.8+is%3Aclosed)

#689  代码优化 com.feilong.core.lang.StringUtil.formatPattern(String, Object...) 换成static import  [[代码优化](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A代码优化+milestone%3A4.0.8+is%3Aclosed)]
