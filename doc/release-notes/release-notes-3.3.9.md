feilong 3.3.9 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>3.3.9</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:3.3.9
```


本次升级共有 `8` 处变更, 具体参见  [3.3.9 milestone](https://github.com/ifeilong/feilong/milestone/28?closed=1)



## :bug: [bug](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Abug+milestone%3A3.3.9+is%3Aclosed)

#492  DateUtil.isInTime(Date, Date, Date) 判定在之间用的是date.after(beginDate) && date.before(endDate); 这种是在区间内,不包含开始和结束  [[feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A3.3.9+is%3Aclosed)]
#487  TypeUtil  getGenericSuperclassParameterizedRawTypes  泛型取值,如果类型参数是同时也是泛型 会是空  [[feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A3.3.9+is%3Aclosed)]


## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A3.3.9+is%3Aclosed)

#491 :sparkles: 新建 com.feilong.core.date.DateUtil.isNotInTime(Date, Date, Date)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.9+is%3Aclosed)]
#490 :sparkles: 新建 com.feilong.core.date.DateUtil.isNotInTime(Date, String, String, String)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.9+is%3Aclosed)]
#486 :sparkles: 新建 com.feilong.core.date.DateUtil.isNotInTime(String, String, String)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.9+is%3Aclosed)]
#484 :sparkles: 新建 com.feilong.core.util.CollectionsUtil.addIgnoreNullOrEmpty(Collection<T>, List)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.9+is%3Aclosed)]


## :baby_bottle: [feilong-servlet](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-servlet+milestone%3A3.3.9+is%3Aclosed)

#489 :sparkles: 新增  RequestUtil.getHeaderIntegerValue(HttpServletRequest, String)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.9+is%3Aclosed)]
#488 :sparkles: 新增 RequestUtil.getHeaderLongValue(HttpServletRequest, String)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.9+is%3Aclosed)]
