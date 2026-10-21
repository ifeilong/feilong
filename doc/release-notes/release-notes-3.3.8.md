feilong 3.3.8 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>3.3.8</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:3.3.8
```


本次升级共有 `7` 处变更, 具体参见  [3.3.8 milestone](https://github.com/ifeilong/feilong/milestone/27?closed=1)



## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A3.3.8+is%3Aclosed)

#481 :wastebasket: Slf4jUtil.format Deprecated 过时  [[deprecated](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Adeprecated+milestone%3A3.3.8+is%3Aclosed)] 原Slf4jUtil.format 方法,使用StringUtil.formatPattern 代替
#56 :sparkles: Validator.isNotNullOrEmpty 无法校验 ascii 码为 160 Unicode 为 \u00A0 的不间断空格  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.8+is%3Aclosed)]
#55 :sparkles: 新建 com.feilong.core.lang.StringUtil.clean(CharSequence) 订单信息的address隐含’ backspace‘ '\b'信息导致HUB不能解析订单  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.8+is%3Aclosed)]
#480 :sparkles: 新建 com.feilong.core.lang.StringUtil.formatPattern(String, Object...) 替换 Slf4jUtil.format(String, Object...)
#404 :sparkles: 新建 com.feilong.core.net.URIUtil.decodeIfExceptionDefault(String, String)  如果报错返回指定值  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.8+is%3Aclosed)]
#482 :sparkles: 新建 com.feilong.core.net.URIUtil.decodeIfExceptionDefault(String, String, String)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.8+is%3Aclosed)]


## :wine_glass: [feilong-net](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net+milestone%3A3.3.8+is%3Aclosed)

#478 :sparkles: 新建个工具类 DingTalkBotBuilder,可以快速new 一个DingTalkBot  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.8+is%3Aclosed)]
