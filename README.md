feilong
================

[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
![JDK 1.8](https://img.shields.io/badge/JDK-1.8-green.svg "JDK 1.8")

> Reduce development, Release ideas (减少开发,释放思想)

让Java开发更简便的工具包,详细的帮助文档 http://feilong-core.mydoc.io/

1. 让你从`大量重复`的底层代码中脱身,`提高工作效率`;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

## feilong 优点:

1. 有常用的工具类 (如 处理日期的 `DateUtil`,处理 集合 的 `CollectionsUtil` 等)
1. 有常用的JAVA常量类 (如日期格式 `DatePattern`, 时间间隔 `TimeInterval` 等)
1. 不必要的`Exception` 转成了`RuntimeException`,减少不必要的代码
1. 国内`中文注释`最完善的API
1. 有完善的单元测试

## 子项目地址

child 				| Description 									
:---- 				| :---------									
feilong-core	| focus on J2SE,核心项目		
feilong-servlet| focus on J2EE,是 platform 重要组成部分,web相关项目的基础	
feilong-io			| 封装了io操作的常用类		
feilong-taglib	| 封装了常用的自定义标签		
feilong-net	| 封装了网络访问操作的常用类
feilong-json	| 封装了json操作的常用类

## 4.:dragon: Maven使用配置

如果你使用 `maven`, 您可以通过以下方式来配置 `pom.xml`:


```XML
<dependency>
	<groupId>com.github.ifeilong</groupId>
	<artifactId>feilong</artifactId>
	<version>3.0.0</version>
</dependency>
```

## How to install?

有些小伙伴想下载并 `自行install` 进行研究, 你需要执行以下 `2` 个步骤:

```bat
git clone https://github.com/ifeilong/feilong.git --depth 1
mvn install -f feilong/pom.xml
```

## 一图概述:

![one-feilong-platform](http://venusdrogon.github.io/feilong-platform/mysource/one-feilong-platform.png) 

## :memo: 说明

1. 基于 [Apache2](https://www.apache.org/licenses/LICENSE-2.0) 协议,您可以下载代码用于闭源项目,但每个修改的过的文件必须放置版权说明;

## :cyclone: feilong 即时交流

|QQ 群 `243306798`
|:---------
|![](http://i.imgur.com/cIfglCa.png)

## :panda_face: About

如果您对本项目有任何建议和批评,可以使用下面的联系方式：

* iteye博客:http://feitianbenyue.iteye.com/