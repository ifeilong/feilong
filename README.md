feilong
================

[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
![JDK 1.8](https://img.shields.io/badge/JDK-1.8-green.svg "JDK 1.8")
[![Maven Central](https://img.shields.io/maven-central/v/com.github.ifeilong/feilong.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.ifeilong%22%20AND%20a:%22feilong%22)

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


## 4.:dragon: Maven使用配置

feilong 自从3.0.0开始,发布中央仓库 https://search.maven.org/search?q=feilong

### `maven`, 您可以通过以下方式来配置 `pom.xml`:

```XML
<dependency>
	<groupId>com.github.ifeilong</groupId>
	<artifactId>feilong</artifactId>
	<version>3.0.0</version>
</dependency>
```

### `Gradle`,

```XML
'com.github.ifeilong:feilong:3.0.0'
```

### 非Maven项目

点击 https://repo1.maven.org/maven2/com/github/ifeilong/feilong/ 链接，下载 feilong.jar即可：

**注意:**
- feilong 3 需要 JDK8+，对Android平台没有测试，不能保证所有工具类或工具方法可用。 
- 如果你的项目使用 JDK7，请使用 feilong core 2.1.0 版本

## How to install?

有些小伙伴想下载并 `自行install` 进行研究, 你需要执行以下 `2` 个步骤:

```bat
git clone https://github.com/ifeilong/feilong.git --depth 1
mvn install
```
## 子项目地址

module | 描述
:---- 				| :---------		

feilong-core |  核心包
feilong-validator |  常用的校验, 包含可配置式的手机号码, 邮编等等
feilong-json  | json format以及tobean toMap等常见操作
feilong-io | 文件常见操作
feilong-servlet | 基于http servlet 的封装,含常见request,response操作快捷封装
feilong-accessor | 便捷式使用session ,cookie
feilong-formatter | 将Map,bean,list format成友好形式
feilong-net-http | http封装操作
feilong-net-jsoup | jsoup操作
feilong-net-filetransfer | ftp/sftp操作
feilong-template | 模板操作,如velocity
feilong-net-mail | 发送邮件,接收邮件操作
feilong-net-cxf | cxf操作
feilong-xml  | xml format以及tobean toMap等常见操作
feilong-security | 加密解密操作
feilong-context | 上下文操作
feilong-namespace | 可以spring xml 来配置的便捷操作
feilong-taglib | jsp 自定义标签
feilong-tools | 可用性操作
feilong-office-csv | csv生成操作
feilong-office-excel | excel操作,xml配置式来生成和读取excel文件
feilong-office-zip | 压缩解压缩操作
feilong-component  | 组件式操作,含配置式即可获取数据-->转成excel-->打成zip压缩包-->发送邮件
feilong | 一体化total jar包,包含上述所有功能
feilong-with-optional | 一体化total jar包,包含上述所有功能,且包含所有optional jar依赖


## 一图概述:

![one-feilong-platform](http://venusdrogon.github.io/feilong-platform/mysource/one-feilong-platform.png) 

## :memo: 说明

1. 基于 [Apache2](https://www.apache.org/licenses/LICENSE-2.0) 协议,您可以下载代码用于闭源项目,但每个修改的过的文件必须放置版权说明;

## :panda_face: 提bug反馈或建议

提交问题反馈

- Github issue https://github.com/ifeilong/feilong/issues
- 码云Gitee issue https://gitee.com/ifeilong/feilong/issues

## :cyclone: feilong 即时交流

|QQ 群 `243306798`
|:---------
|![](http://i.imgur.com/cIfglCa.png)
