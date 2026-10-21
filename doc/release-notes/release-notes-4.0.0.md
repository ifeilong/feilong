feilong 4.0.0 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>4.0.0</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:4.0.0
```


本次升级共有 `13` 处变更, 具体参见  [4.0.0 milestone](https://github.com/ifeilong/feilong/milestone/33?closed=1)



## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A4.0.0+is%3Aclosed)

#597 :sparkles: 新建 CollectionsUtil List<T> getPropertyValueList(Collection<O> beanIterable,Function<O, T> mapper)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.0+is%3Aclosed)]
#599  标识 AnnotationToStringBuilder @FunctionalInterface
#590 :sparkles: 进度统计, 新增预计还需要多少时间 com.feilong.tools.log.LogHelper.getProcessLog(ProcessLogParamEntity)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.0+is%3Aclosed)]


## :guitar: [feilong-context](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-context+milestone%3A4.0.0+is%3Aclosed)

#585  com.feilong.spring.web.util.WebSpringUtil.getRequest() 加上异常处理, 如果出现异常返回null  [[逻辑调整](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A逻辑调整+milestone%3A4.0.0+is%3Aclosed)]


## :fishing_pole_and_fish: [feilong-io](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-io+milestone%3A4.0.0+is%3Aclosed)

#598  标识 LineNumberReaderResolver @FunctionalInterface


## :wine_glass: [feilong-net](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net+milestone%3A4.0.0+is%3Aclosed)

#584  com.feilong.net.http.callback.AbstractResultCallback.build(Date, HttpResponse) 当只需要statusCode 的时候, 只渲染statusCode  [[逻辑调整](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A逻辑调整+milestone%3A4.0.0+is%3Aclosed)]
#600  标识 ResultCallback FunctionalInterface


## [feilong-net-bot](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net-bot+milestone%3A4.0.0+is%3Aclosed)

#592 :sparkles: AbstractBot isCatchException 过期, 使用新的参数  isThrowException更加直接易懂  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.0+is%3Aclosed)]
#591 :sparkles: LogAndBotCombination 区分最后一个参数是不是e  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.0+is%3Aclosed)]


## :lock: [feilong-security](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-security+milestone%3A4.0.0+is%3Aclosed)

#593 :sparkles: 新建 字符串转Key 的方法 com.feilong.security.KeyUtil.generatePrivateKey(String)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.0+is%3Aclosed)]


## :baby_bottle: [feilong-servlet](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-servlet+milestone%3A4.0.0+is%3Aclosed)

#595 :sparkles: 新建 com.feilong.servlet.ServletContextUtil.getIgnoreKeys()  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.0+is%3Aclosed)]


## :heavy_minus_sign: [依赖调整](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖调整+milestone%3A4.0.0+is%3Aclosed)

#594  httpclient 并入到 feilong lib  [[重要](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A重要+milestone%3A4.0.0+is%3Aclosed)  [结构调整](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A结构调整+milestone%3A4.0.0+is%3Aclosed)]


## [其他](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A其他+milestone%3A4.0.0+is%3Aclosed)

#586  代码引用 com.feilong.core.date.DateUtil.formatDuration(Date)地方换成毫秒的方法
