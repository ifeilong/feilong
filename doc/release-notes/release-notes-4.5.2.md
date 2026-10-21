feilong 4.5.2 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>4.5.2</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:4.5.2
```


本次升级共有 `30` 处变更, 具体参见  [4.5.2 milestone](https://github.com/ifeilong/feilong/milestone/52?closed=1)



## :bug: [bug](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Abug+milestone%3A4.5.2+is%3Aclosed)

#822  com.feilong.json.transformer.SeparatorToCamelCaseJavaIdentifierTransformer 如果传的是标准的java属性会变成小写  [[feilong-json](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-json+milestone%3A4.5.2+is%3Aclosed)]


## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A4.5.2+is%3Aclosed)

#812 :sparkles: com.feilong.core.util.CollectionsUtil.getPropertyValueSet(Iterable<O>, String) 支持 lambda function 写法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.5.2+is%3Aclosed)]
#813 :sparkles: com.feilong.core.util.CollectionsUtil.select(Iterable<O>, String, Collection<V>) 要支持 function lambda写法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.5.2+is%3Aclosed)]
#815 :sparkles: 新建 com.feilong.core.lang.StringUtil.toMap(String, String, String, Class<T>, StringToBeanConverter<V>) 要支持自定义& 分隔符  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.5.2+is%3Aclosed)]
#818 :sparkles: 新建 com.feilong.core.util.CollectionsUtil.select(Iterable<O>, Function<O, V>, Collection<V>)  值是 Collection 的方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.5.2+is%3Aclosed)]
#832 :sparkles: 新建 com.feilong.core.util.GroupUtil.groupBy(Iterable<O>, Function<O, T>)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.5.2+is%3Aclosed)]
#831 :sparkles: 新建 com.feilong.core.util.GroupUtil.groupBy(Iterable<O>, Function<O, T>, Predicate<O>)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.5.2+is%3Aclosed)]
#820 :sparkles: 新建 com.feilong.core.util.MapUtil.extractSubMap(Map<K, O>, Function<O, T>) MapUtil.extractSubMap 升级到lambda  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.5.2+is%3Aclosed)]
#816 :sparkles: 新建 com.feilong.core.util.MapUtil.getCaseInsensitive(Map<String, V>, String) 要有个支持 忽略大小写的map 获取数据  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.5.2+is%3Aclosed)]
#802 :sparkles: 新建 com.feilong.core.util.MapUtil.getSubMapExcludeValues(Map<K, T>, T...) 方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.5.2+is%3Aclosed)]
#819 :sparkles: 新建 com.feilong.core.util.MapUtil.putAllFromList(Map<K, V>, List<K>, List<V>)  新建一个 key 和value  list 塞道map 的方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.5.2+is%3Aclosed)]
#824 :sparkles: 新建 com.feilong.json.transformer.ToCamelCaseJavaIdentifierTransformer  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.5.2+is%3Aclosed)]
#817 :sparkles: 新建 com.feilong.net.bot.MarkdownHelper.createTable(List<String>, List<O>, List<Function<O, ?>>) 可以快速创建markdown  table  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.5.2+is%3Aclosed)]
#801 :sparkles: 新建com.feilong.core.util.CollectionsUtil.find(Iterable<O> beanIterable,Function<O, V> propertyValueExtractor,V propertyValue)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.5.2+is%3Aclosed)  [dependencies](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Adependencies+milestone%3A4.5.2+is%3Aclosed)]
#799 :sparkles: 新建com.feilong.core.util.CollectionsUtil.getPropertyValueMap(Iterable<O>, String, String) 对应的lambda方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.5.2+is%3Aclosed)]
~~#825 :wastebasket: 标识 com.feilong.core.util.CollectionsUtil.group(Iterable<O>, String)  @deprecated  [[deprecated](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Adeprecated+milestone%3A4.5.2+is%3Aclosed)]~~
~~#826 :wastebasket: 标识 com.feilong.core.util.CollectionsUtil.group(Iterable<O>, String, Predicate<O>) @deprecated  [[deprecated](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Adeprecated+milestone%3A4.5.2+is%3Aclosed)]~~
~~#827 :wastebasket: 标识 com.feilong.core.util.CollectionsUtil.group(Iterable<O>, Transformer<O, T>) @deprecated  [[deprecated](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Adeprecated+milestone%3A4.5.2+is%3Aclosed)]~~
~~#829 :wastebasket: 标识 com.feilong.core.util.CollectionsUtil.groupOne(Iterable<O>, Function<O, T>)  @deprecated  [[deprecated](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Adeprecated+milestone%3A4.5.2+is%3Aclosed)]~~
~~#830 :wastebasket: 标识 com.feilong.core.util.CollectionsUtil.groupOne(Iterable<O>, String)  @Deprecated  [[deprecated](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Adeprecated+milestone%3A4.5.2+is%3Aclosed)]~~
~~#828 :wastebasket: 标识com.feilong.core.util.CollectionsUtil.group(Iterable<O>, Predicate<O>, Transformer<O, T>)  @Deprecated  [[deprecated](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Adeprecated+milestone%3A4.5.2+is%3Aclosed)]~~


## :meat_on_bone: [feilong-json](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-json+milestone%3A4.5.2+is%3Aclosed)

#823 :sparkles: com.feilong.lib.json.JSONObjectToBeanUtil.toBeanUsePropertyDescriptor(Object, Triple<String, String, Object>, Class<?>, JsonConfig, PropertyDescriptor) 如果转的类型是 string  那么直接转过去  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.5.2+is%3Aclosed)]


## :arrow_up: [依赖升级](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖升级+milestone%3A4.5.2+is%3Aclosed)

#805  maven-compiler-plugin 升级到3.15.0
#804  maven-deploy-plugin 插件升级到3.1.4
#803  maven-install-plugin 插件升级到3.1.4
#806  maven-resources-plugin 插件升级到3.5.0
#807  maven-scm-plugin 插件升级到2.2.1
#808  maven-site-plugin 插件升级到 3.21.0


## :heavy_minus_sign: [依赖调整](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖调整+milestone%3A4.5.2+is%3Aclosed)

#810  maven-jar-plugin  3.5.0 回退到 3.3.0


## [其他](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A其他+milestone%3A4.5.2+is%3Aclosed)

#814 :sparkles: 封装 markdown font 部分语法 com.feilong.net.bot.MarkdownHelper.font(String, String)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.5.2+is%3Aclosed)]
