feilong 3.5.0 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>3.5.0</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:3.5.0
```


本次升级共有 `41` 处变更, 具体参见  [3.5.0 milestone](https://github.com/ifeilong/feilong/milestone/31?closed=1)



## :bug: [bug](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Abug+milestone%3A3.5.0+is%3Aclosed)

#527  com.feilong.io.FileUtil.formatSize(long) 第一行校验,少输入了size 参数  [[feilong-io](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-io+milestone%3A3.5.0+is%3Aclosed)  [log](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Alog+milestone%3A3.5.0+is%3Aclosed)]
#528  com.feilong.io.IOWriteUtil.write(InputStream, OutputStream) 2.57G的文件  日志 FileUtil.formatSize(long) 会报错  [[feilong-io](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-io+milestone%3A3.5.0+is%3Aclosed)  [log](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Alog+milestone%3A3.5.0+is%3Aclosed)]


## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A3.5.0+is%3Aclosed)

#548 :sparkles: CollectionsUtil 新增获取list 最后一条数据  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.5.0+is%3Aclosed)]
#554 :sparkles: com.feilong.core.util.AggregateUtil.avg(Iterable<O>, String, int) 值支持自动转换, 比如string 转成数字  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.5.0+is%3Aclosed)]
#553 :sparkles: com.feilong.core.util.AggregateUtil.groupSum(Iterable<O>, String, String, Predicate<O>) 值支持类型自动转换  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.5.0+is%3Aclosed)]
#549 :sparkles: com.feilong.core.util.AggregateUtil.sum(Iterable<O>, String[], Predicate<O>) 支持类型自动转换  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.5.0+is%3Aclosed)]
#523 :sparkles: java 自带 repleaceAll 性能比较差  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.5.0+is%3Aclosed)]
#550 :sparkles: 新增 com.feilong.core.Validator.isAllNullOrEmpty(Object...)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.5.0+is%3Aclosed)]
#557 :sparkles: 新建 com.feilong.core.lang.NumberUtil.getSubtractValueWithScale(Number, Number, int) 的方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.5.0+is%3Aclosed)]
#560 :sparkles: 新建 com.feilong.core.lang.StringUtil.tokenizeToStringArray(String)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.5.0+is%3Aclosed)]
#559 :sparkles: 新建 map 方法 computeIfAbsent   [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.5.0+is%3Aclosed)]
#561 :sparkles: 新建com.feilong.core.lang.StringUtil.tokenizeToList(String)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.5.0+is%3Aclosed)]
#558 :sparkles: 新建个 StringUtil.tokenizeToSet方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.5.0+is%3Aclosed)]
#556  更新   NumberUtil.getSubtractValue(amount, finishSubtotal) javadoc 关于 精度的  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A3.5.0+is%3Aclosed)]
#552  更新com.feilong.core.lang.NumberUtil.setScale(Number, int) javadoc  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A3.5.0+is%3Aclosed)]
#555  补充 com.feilong.core.util.CollectionsUtil.first(Iterable<T>) javadoc  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A3.5.0+is%3Aclosed)]


## :meat_on_bone: [feilong-json](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-json+milestone%3A3.5.0+is%3Aclosed)

#563 :sparkles: jsonutil 如果截取, 加上换行  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.5.0+is%3Aclosed)]


## :wine_glass: [feilong-net](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net+milestone%3A3.5.0+is%3Aclosed)

#522  http retry 失败的时候, 把URL 参数输出出来  [[log](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Alog+milestone%3A3.5.0+is%3Aclosed)]
#543 :sparkles: http 请求日志需要输出 response status code 和response header 信息  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.5.0+is%3Aclosed)  [log](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Alog+milestone%3A3.5.0+is%3Aclosed)]


## :arrow_up: [依赖升级](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖升级+milestone%3A3.5.0+is%3Aclosed)

#529  Apache Commons FileUpload denial of service vulnerability 升级到 1.5
#532  dependencyManagement springframework版本  依赖升级到 5.3.26
#531  jsoup 升级到 	1.15.4
#533  log4j2  (only for test)升级到 2.20.0
#530  logback manager 依赖改回 1.3系列  1.3.8
#539  maven-compiler-plugin 升级到3.11.0
#516  maven-dependency-plugin 升级到 3.6.0
#538  maven-deploy-plugin 插件升级到  3.1.1
#565  maven-install-plugin 升级到3.1.1
#517  maven-plugin-plugin 升级到	3.7.1
#537  maven-plugin-plugin 插件升级到 3.9.0
#567  maven-project-info-reports-plugin 升级3.4.5
#518  maven-project-info-reports-plugin 升级到 3.4.2	
#536  maven-release-plugin 插件升级到 3.0.1
#566  maven-resources-plugin 升级到3.3.1
#540  maven-scm-plugin 升级到2.0.1
#541  maven-surefire-plugin 升级到 3.1.2
#564  native2ascii-maven-plugin 升级到2.1.0
#542  versions-maven-plugin 升级到2.16.0
#534  插件 maven-assembly-plugin 升级到 3.6.0
#535  插件 maven-javadoc-plugin 升级到  3.5.0


## :heavy_minus_sign: [依赖调整](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖调整+milestone%3A3.5.0+is%3Aclosed)

#515  slf4j 变成直接依赖, 而不是打入到shade jar  [[重要](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A重要+milestone%3A3.5.0+is%3Aclosed)]
