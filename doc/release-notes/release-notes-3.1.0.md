feilong 3.1.0 发布了，feilong 是一个让Java开发更简便的工具库。

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/

`单元测试数` 增加至 `2259` 个, `单元测试覆盖率` 增加至 `91%` ,`javadoc` 比率 `83%`

本次升级共有 `40` 处变更, 具体参见  [3.1.0 milestone](https://github.com/ifeilong/feilong/milestone/13?closed=1)

## :bug: [bug](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Abug+milestone%3A3.1.0+is%3Aclosed)

#310  http client 中的post ,put 方法 要支持 body 和paramMap 并存的情况  [[feilong-net](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net+milestone%3A3.1.0+is%3Aclosed)]


## :alien: [不兼容](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A不兼容+milestone%3A3.1.0+is%3Aclosed)

#329  com.feilong.net.wxwork.bot 包名修改成 com.feilong.net.bot.wxwork (如果你用的是 feilong xml 标签形式, 不会受到影响)  [[feilong-net-bot](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net-bot+milestone%3A3.1.0+is%3Aclosed)]
#314  抽象类 AbstractSecretKeySpecBuilder 的 buildKeyBytes方法 新增algorithm参数  [[feilong-security](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-security+milestone%3A3.1.0+is%3Aclosed)]


## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A3.1.0+is%3Aclosed)

#350 :sparkles: Objectutil 新增 equalsAny 方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.1.0+is%3Aclosed)]
#353  新建 DefaultIfNullParameterizedTest


## :wine_glass: [feilong-net](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net+milestone%3A3.1.0+is%3Aclosed)

#308 :sparkles: ResponseUtil.write 优化代码, 将 PrintWriter 放在try-with-resources里  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.1.0+is%3Aclosed)]


## [feilong-net-bot](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net-bot+milestone%3A3.1.0+is%3Aclosed)

#319  新建 基于钉钉的机器人


## :lock: [feilong-security](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-security+milestone%3A3.1.0+is%3Aclosed)

#313 :sparkles: 新建 KeyGeneratorKeyBuilder  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.1.0+is%3Aclosed)]

## :baby_bottle: [feilong-servlet](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-servlet+milestone%3A3.1.0+is%3Aclosed)

#352 :sparkles: ResponseUtil writeText writeJson 当characterEncoding 参数是null或者empty的时候, 使用默认的utf-8  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.1.0+is%3Aclosed)  [javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A3.1.0+is%3Aclosed)]
#351 :sparkles: ResponseUtil.writeJson 注释把 默认utf-8 加上  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.1.0+is%3Aclosed)  [javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A3.1.0+is%3Aclosed)]
#354 :sparkles: 新增 RequestUtil.isNotWechatRequest(HttpServletRequest)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.1.0+is%3Aclosed)]
#349 :sparkles: 新增 判断请求是来自于微信小程序的方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.1.0+is%3Aclosed)]


## :arrow_up: [依赖升级](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖升级+milestone%3A3.1.0+is%3Aclosed)

#302  httpclient 升级到 4.5.13
#345  jacoco-maven-plugin 升级到 0.8.8
#316  junit 升级到 4.13.2  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.1.0+is%3Aclosed)]
#315  logback 升级到1.2.11  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.1.0+is%3Aclosed)]
#330  maven-antrun-plugin 升级到 3.1.0
#341  maven-compiler-plugin 升级到3.10.1	
#338  maven-dependency-plugin 升级到 3.3.0
#342  maven-jar-plugin 升级到 3.2.2
#339  maven-javadoc-plugin 升级到3.4.0
#340  maven-plugin-plugin 升级到 3.6.4
#343  maven-project-info-reports-plugin 升级到 3.3.0	
#305  maven-resources-plugin 插件升级到 3.2.0	
#346  maven-scm-plugin 升级到 1.12.2
#334  maven-war-plugin 升级到 3.3.2
#347  org.springframework test 依赖升级到 5.2.22.RELEASE
#317  slf4j 升级到 1.7.32  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.1.0+is%3Aclosed)]
#303  spring optional 依赖升级到 5.2.11.RELEASE
#344  versions-maven-plugin 升级到 2.11.0

## :heavy_minus_sign: [依赖调整](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖调整+milestone%3A3.1.0+is%3Aclosed)

#335 :fire: parent 移除 tiles 默认依赖  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.1.0+is%3Aclosed)]

## [其他](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A其他+milestone%3A3.1.0+is%3Aclosed)

#328  CollectionsUtil.removeDuplicate(Collection<Command>, String) 方法注释不够具体生动  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A3.1.0+is%3Aclosed)]
#355  日期转字符串, 格式 E 在不同jdk 版本显示结果不一样  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A3.1.0+is%3Aclosed)]
