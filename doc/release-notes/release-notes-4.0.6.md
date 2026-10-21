feilong 4.0.6 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>4.0.6</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:4.0.6
```


本次升级共有 `16` 处变更, 具体参见  [4.0.6 milestone](https://github.com/ifeilong/feilong/milestone/40?closed=1)



## :bug: [bug](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Abug+milestone%3A4.0.6+is%3Aclosed)

#76  post 请求 显示的full url不全  [[feilong-servlet](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-servlet+milestone%3A4.0.6+is%3Aclosed)]

**说明: 相关日志,从直接使用request queryString,改成解析请求参数拼接**

## :alien: [不兼容](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A不兼容+milestone%3A4.0.6+is%3Aclosed)

#643 :sparkles: PartitionThreadConfig 默认初始最大线程从100改成20,并且计算的时候 大于20时候 info 级别日志提醒  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.6+is%3Aclosed)  [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A4.0.6+is%3Aclosed)]


## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A4.0.6+is%3Aclosed)

#644 :sparkles: 多线程执行时,内部日志支持默认标识key,方便搜索  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.6+is%3Aclosed)]
#641 :sparkles: 新建 DefaultPartitionPerHandler  多线程处理单元素的方法, 并且内容try catch exception 和计算耗时时间  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.6+is%3Aclosed)]
#652 :sparkles: 新建 PartitionPerElementHandler  分区中的每个线程中的每个元素执行  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.6+is%3Aclosed)]
#649 :sparkles: 新建 com.feilong.core.lang.ThreadUtil.executeUsePerElement(List<T>, Map<String, ?>, PartitionPerElementHandler<T>)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.6+is%3Aclosed)]
#650 :sparkles: 新建 com.feilong.core.lang.ThreadUtil.executeUsePerElement(List<T>, PartitionPerElementHandler<T>)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.6+is%3Aclosed)]
#651 :sparkles: 新建 com.feilong.core.lang.ThreadUtil.executeUsePerElement(List<T>, PartitionThreadConfig, Map<String, ?>, PartitionPerElementHandler<T>)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.6+is%3Aclosed)]
#654 :sparkles: 新建 com.feilong.core.util.MapUtil.getDefaultEmptyStringIfNull(Map<K, V>, K)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.6+is%3Aclosed)]
~~#645 :wastebasket: 标识 com.feilong.core.lang.ThreadUtil.execute(List<T>, int, Map<String, ?>, PartitionPerHandler<T>)  Deprecated  [[deprecated](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Adeprecated+milestone%3A4.0.6+is%3Aclosed)]~~
~~#646 :wastebasket: 标识 com.feilong.core.lang.ThreadUtil.execute(List<T>, int, Map<String, ?>, PartitionRunnableBuilder<T>) 过时  [[deprecated](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Adeprecated+milestone%3A4.0.6+is%3Aclosed)]~~
~~#647 :wastebasket: 标识 com.feilong.core.lang.ThreadUtil.execute(List<T>, int, PartitionPerHandler<T>) 过时  [[deprecated](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Adeprecated+milestone%3A4.0.6+is%3Aclosed)]~~
~~#648 :wastebasket: 标识 com.feilong.core.lang.ThreadUtil.execute(List<T>, int, PartitionRunnableBuilder<T>) 过时  [[deprecated](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Adeprecated+milestone%3A4.0.6+is%3Aclosed)]~~


## :guitar: [feilong-context](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-context+milestone%3A4.0.6+is%3Aclosed)

#655 :sparkles: 新建 com.feilong.context.log.RequestLogHelper 个能快速输出请求url 请求类型, 请求参数的方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.6+is%3Aclosed)]


## :robot: [feilong-net-bot](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net-bot+milestone%3A4.0.6+is%3Aclosed)

#642 :sparkles: 机器人开放获取 key的方法,方便记录日志, 比如钉钉机器人需要可以返回token  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.6+is%3Aclosed)]


## :baby_bottle: [feilong-servlet](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-servlet+milestone%3A4.0.6+is%3Aclosed)

#656 :sparkles: 新建 com.feilong.servlet.http.RequestUtil.parseParamsToQueryString(HttpServletRequest)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.6+is%3Aclosed)]
