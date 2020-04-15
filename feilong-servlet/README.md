feilong-servlet
================

[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
![JDK 1.7](https://img.shields.io/badge/JDK-1.7-green.svg "JDK 1.7")
[![jar size 33K](https://img.shields.io/badge/size-33K-green.svg "size 33K")](https://github.com/venusdrogon/feilong-platform/tree/repository/com/feilong/platform/feilong-servlet/1.10.1)

> Reduce development, Release ideas (减少开发,释放思想)

## 简介

focus on J2EE,是 [feilong platform](https://github.com/venusdrogon/feilong-platform) 项目重要组成部分,是 web相关项目的基础

## Javadoc:
在此,我们提供在线的Javadoc,以便查阅,参见 [Javadoc](http://venusdrogon.github.io/feilong-platform/javadocs/feilong-servlet/)

## wiki:
更多内容,你可以参见 [WiKi](https://github.com/venusdrogon/feilong-servlet/wiki) 

## :dragon: Maven使用配置

feilong-servlet jar你可以直接在 [仓库](https://github.com/venusdrogon/feilong-platform/tree/repository/com/feilong/platform/feilong-servlet "仓库") 浏览 

如果你使用 `maven`, 您可以通过以下方式来配置 `pom.xml`:

```XML
<project>

	....
	<properties>
		<version.feilong-platform>2.1.0</version.feilong-platform>
		....
	</properties>
	
	....
	<repositories>
		<repository>
			<id>feilong-repository</id>
			<url>https://raw.github.com/venusdrogon/feilong-platform/repository</url>
		</repository>
	</repositories>
	
	....
	<dependencies>
		....
		<dependency>
			<groupId>com.feilong.platform</groupId>
			<artifactId>feilong-servlet</artifactId>
			<version>${version.feilong-platform}</version>
		</dependency>
		....
	</dependencies>
	
	....
	
</project>
```


此外强烈建议你使用 feilong 工具类全家桶(含IO操作,Net操作,Json,XML,自定义标签等等工具类)


```XML
<project>

	....
	<properties>
		<version.feilong-platform>2.1.0</version.feilong-platform>
		....
	</properties>

	....
	<repositories>
		<repository>
			<id>feilong-repository</id>
			<url>https://raw.github.com/venusdrogon/feilong-platform/repository</url>
		</repository>
	</repositories>

	....
	<dependencies>
		....
		<dependency>
			<groupId>com.feilong.platform</groupId>
			<artifactId>feilong-util-all</artifactId>
			<version>${version.feilong-platform}</version>
		</dependency>
		....
	</dependencies>
	....
</project>
```


## How to install?

有些小伙伴想下载并自行install 进行研究, 你需要执行以下4个步骤:

```bat
git clone https://github.com/venusdrogon/feilong-platform.git --depth 1
mvn install -f feilong-platform/pom.xml

git clone https://github.com/venusdrogon/feilong-servlet.git --depth 1
mvn install -f feilong-servlet/pom.xml
```

## :memo: 说明

1. 基于 [Apache2](https://www.apache.org/licenses/LICENSE-2.0) 协议,您可以下载代码用于闭源项目,但每个修改的过的文件必须放置版权说明;
1. 1.5.0及以上版本需要`jdk1.7`及以上环境(1.5.0以下版本需要`jdk1.6`及以上环境);

## :cyclone: feilong 即时交流

|QQ 群 `243306798`
|:---------
|![](http://i.imgur.com/cIfglCa.png)

## :panda_face: About

如果您对本项目有任何建议和批评,可以使用下面的联系方式：

* iteye博客:http://feitianbenyue.iteye.com/