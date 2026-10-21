feilong 3.3.5 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>3.3.5</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:3.3.5
```


本次升级共有 `6` 处变更, 具体参见  [3.3.5 milestone](https://github.com/ifeilong/feilong/milestone/24?closed=1)



## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A3.3.5+is%3Aclosed)

#458 :sparkles: 新增 UrlUtil.getUnionUrl(String context,String spec)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.5+is%3Aclosed)]
#457 :sparkles: 新增 com.feilong.core.util.MapUtil.getSubSumValue(Map<K, T>, Iterable<K>, Class<C>) 封装统计 map value 累加并支持类型转换的方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.5+is%3Aclosed)]
#460 :sparkles: 新建 com.feilong.core.lang.ObjectUtil.defaultEmptyListIfNull(List<T>)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.5+is%3Aclosed)]
#462 :sparkles: 新建 com.feilong.core.lang.ObjectUtil.defaultEmptyMapIfNull(Map<K, V>)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.5+is%3Aclosed)]
#461 :sparkles: 新建 com.feilong.core.lang.ObjectUtil.defaultEmptySetIfNull(Set<T>)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.3.5+is%3Aclosed)]


## :arrow_up: [依赖升级](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖升级+milestone%3A3.3.5+is%3Aclosed)

#463  maven-plugin-plugin 升级到 3.7.0
