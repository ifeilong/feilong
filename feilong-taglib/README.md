feilong-taglib JSP常用自定义标签集
================

[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
![JDK 1.7](https://img.shields.io/badge/JDK-1.7-green.svg "JDK 1.7")

> Reduce development, Release ideas (减少开发,释放思想)

## 简介

`封装了常用的自定义标签`

主要由两部分组成

## :rat: Common

包含所有`自定义标签的base类`,以及`常用的自定义标签` 和`el function`

taglib	|说明	
:---- | :---------
[isContains](https://github.com/venusdrogon/feilong-taglib/wiki/feilong-isContains "isContains") 	|判断一个值,是否在一个集合(或者可以被转成Iterator)当中
[isInTime](https://github.com/venusdrogon/feilong-taglib/wiki/feilong-isInTime "isInTime") 	|判断一个日期(date),是否在一个时间区间内(beginDate,endDate)


## :dromedary_camel: Display

taglib	|说明	
:---- | :---------
[pager](https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-pager "J2EE分页解决方案") 	|J2EE分页解决方案
[concat](https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-concat "feilongDisplay concat")  	| jsp版本的 "css/js合并以及版本控制"的标签 
[option](https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-option "feilongDisplay-option")  	|用来基于 i18n配置文件,渲染select option选项,实现国际化功能,简化开发
[loadBundle](https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-loadBundle "feilongDisplay-loadBundle")  	|将i18n配置文件,转成map,加载到request 作用域, 实现国际化功能,简化开发
[sensitive](https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-sensitive "feilongDisplay sensitive")  	|敏感数据mask标签
[barcode](https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-barcode "feilongDisplay barcode") `@Deprecated` 	|用来在页面生成二维码

## :dragon: Maven使用配置

taglib jar你可以直接在 [仓库]( https://github.com/venusdrogon/feilong-platform/tree/repository/com/feilong/platform/feilong-taglib "仓库") 浏览 

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
		
		<!--feilong-taglib -->
		<dependency>
			<groupId>com.feilong.platform</groupId>
			<artifactId>feilong-taglib</artifactId>
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


## 使用示例:

两行代码搞定分页显示

```XML

	<%@ taglib prefix="feilongDisplay" uri="http://java.feilong.com/tags-display"%>
	....
	<feilongDisplay:pager count="${count}"/>
	....
```

更多说明,参见 [pager](https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-pager "J2EE分页解决方案")

## How to install?

有些小伙伴想下载并自行install 进行研究, 你需要执行以下4个步骤:

```bat
git clone https://github.com/venusdrogon/feilong-platform.git --depth 1
mvn install -f feilong-platform/pom.xml

git clone https://github.com/venusdrogon/feilong-taglib.git --depth 1
mvn install -f feilong-taglib/pom.xml
```

**详细参考** https://github.com/venusdrogon/feilong-taglib/wiki/install

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