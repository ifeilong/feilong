feilong 3.1.1 发布了，让Java开发更简便的工具库。

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/

`单元测试数` 增加至 `2359` 个, `单元测试覆盖率` 增加至 `91%` ,`javadoc` 比率 `83%`

本次升级共有 `18` 处变更, 具体参见  [3.1.1 milestone](https://github.com/ifeilong/feilong/milestone/14?closed=1)



## :bug: [bug](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Abug+milestone%3A3.1.1+is%3Aclosed)

#375  修改 错误的javadoc DateExtensionUtil 换成 DateUtil  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A3.1.1+is%3Aclosed)]


## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A3.1.1+is%3Aclosed)

#366 :sparkles: 提供 默认 2位小数的 进度NumberUtil.getProgress(Number, Number, String)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.1.1+is%3Aclosed)]
#374 :sparkles: 新建 2个日期 间隔月数   [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.1.1+is%3Aclosed)]
#357 :sparkles: 新建 DateUtil.getFirstDateOfToday  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.1.1+is%3Aclosed)]
#368 :sparkles: 新建 StringUtil.trimAndEqualsIgnoreCase(String, String) string 去空格 忽视大小写的方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.1.1+is%3Aclosed)]
#358 :sparkles: 新建 ThreadUtil.sleepSeconds(int seconds)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.1.1+is%3Aclosed)]
#373 :sparkles: 新建 com.feilong.core.util.CollectionsUtil.newHashSet(Collection) 带参的方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.1.1+is%3Aclosed)]
#361 :sparkles: 新建类似于 CollectionsUtil.find(Iterable<O>, Map<String, ?>) select 方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.1.1+is%3Aclosed)]
#370 :sparkles: 简化这种 创建一个对象 再copy 属性的写法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.1.1+is%3Aclosed)]


## :meat_on_bone: [feilong-json](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-json+milestone%3A3.1.1+is%3Aclosed)

#367 :sparkles: 新建JsonUtil.toString(Object) 来简化  JsonUtil.format(xqlConfig, 0, 0)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.1.1+is%3Aclosed)]


## :wine_glass: [feilong-net](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net+milestone%3A3.1.1+is%3Aclosed)

#372 :sparkles: com.feilong.net.http.HttpClientUtil.get(String) 新增只有1个参数的方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.1.1+is%3Aclosed)]


## :baby_bottle: [feilong-servlet](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-servlet+milestone%3A3.1.1+is%3Aclosed)

#356  com.feilong.servlet.http.RequestLogBuilder.getHeaderMap() 迁移到 RequestUtil
#363 :sparkles: 新建 RequestUtil.getRequestFullURL(HttpServletRequest) 不带编码的方法, 默认utf8  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.1.1+is%3Aclosed)]


## [其他](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A其他+milestone%3A3.1.1+is%3Aclosed)

#376  maven-assembly-plugin 升级到 3.4.1  [[dependencies](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Adependencies+milestone%3A3.1.1+is%3Aclosed)]
#378  maven-deploy-plugin 升级到3.0.0  [[dependencies](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Adependencies+milestone%3A3.1.1+is%3Aclosed)]
#379  maven-install-plugin 升级到3.0.0  [[dependencies](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Adependencies+milestone%3A3.1.1+is%3Aclosed)]
#377  maven-project-info-reports-plugin 升级到 3.4.0  [[dependencies](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Adependencies+milestone%3A3.1.1+is%3Aclosed)]
#362 :sparkles: 处理优化json format Calendar 日志有告警  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.1.1+is%3Aclosed)]

**Full Changelog**: https://github.com/ifeilong/feilong/compare/3.1.0...3.1.1