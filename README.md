feilong 让Java开发更简便的工具库
================

[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
![JDK 1.8](https://img.shields.io/badge/JDK-1.8-green.svg "JDK 1.8")
[![Maven Central](https://img.shields.io/maven-central/v/com.github.ifeilong/feilong.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.ifeilong%22%20AND%20a:%22feilong%22)

Reduce development, Release ideas (灵感从重复简单的代码中释放出来)

1. 让你从`大量重复`的底层代码中脱身,`提高工作效率`;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

## feilong 优点:

![](http://i.imgur.com/NCuo13D.png)

**对比1:**

![](http://i.imgur.com/rJnESSq.png)

**对比2:**

![](http://i.imgur.com/FG9ty3Q.png)

- [使用 feilong-core 的理由](https://github.com/ifeilong/feilong/wiki/Reasons-for-use-feilong-core)

1. 有常用的工具类 (如 处理日期的 `DateUtil`,处理 集合 的 `CollectionsUtil` 等)
1. 有常用的JAVA常量类 (如日期格式 `DatePattern`, 时间间隔 `TimeInterval` 等)
1. 不必要的`Exception` 转成了`RuntimeException`,减少不必要的代码
1. 国内`中文注释`最完善的API
1. 有完善的单元测试

详细的帮助文档 http://feilong-core.mydoc.io/


## :memo: 常用组件/功能

- [使用feilong发企业微信机器人](https://github.com/ifeilong/feilong/wiki/使用feilong发企业微信机器人)


## feilong 的历史

since 2008, 起初应对开发过程中不断重复的代码进行了封装,进而在公司内部推广

- `2016-09-22` 开源了 [feilong-core](https://www.oschina.net/p/feilong-core) (2020年停止更新维护)
- `2016-10-31` 开源了 [feilong-taglib](https://www.oschina.net/p/feilong-taglib) (2020年停止更新维护)
- `2020-05-26` 开源了 [feilong](https://www.oschina.net/p/feilong) 


## :dragon: Maven使用配置

feilong 自从3.0.0开始,发布中央仓库 https://search.maven.org/artifact/com.github.ifeilong/feilong

### `maven 配置` 

```XML
<dependency>
	<groupId>com.github.ifeilong</groupId>
	<artifactId>feilong</artifactId>
	<version>3.5.1</version>
</dependency>
```

### `Gradle 配置` 

```
com.github.ifeilong:feilong:3.5.1
```

### `非Maven项目`

点击 https://repo1.maven.org/maven2/com/github/ifeilong/feilong/ 链接，下载 feilong.jar即可：

**注意:**
- feilong 3 需要 JDK8+，对Android平台没有测试，不能保证所有工具类或工具方法可用。 
- 如果你的项目使用 JDK7，请使用 [feilong-core 2.1.0](https://github.com/ifeilong/feilong-core)  版本

## How to install?

有些小伙伴想下载并 `自行install` 进行研究, 你需要执行以下 `2` 个步骤:

```bat
git clone https://github.com/ifeilong/feilong.git --depth 1
mvn install
```
## 子模块介绍

module | 介绍
:----  | :---------
feilong-core |  核心包 (推荐)
feilong-validator |  常用的校验, 包含可配置式的手机号码, 邮编等等
feilong-json  | json format以及tobean toMap等常见操作
feilong-io | 文件常见操作 
feilong-servlet | 基于http servlet 的封装,含常见request,response操作快捷封装  (推荐)
feilong-accessor | 便捷式使用session ,cookie
feilong-formatter | 将Map,bean,list format成友好形式
feilong-net-http | http封装操作  (推荐)
feilong-net-jsoup | jsoup操作
feilong-net-filetransfer | ftp/sftp操作   (推荐)
feilong-template | 模板操作,如velocity
feilong-net-mail | 发送邮件,接收邮件操作
feilong-net-cxf | cxf操作
feilong-xml  | xml format以及tobean toMap等常见操作
feilong-security | 加密解密操作
feilong-context | 上下文操作  (推荐)
feilong-namespace | 可以spring xml 来配置的便捷操作
feilong-taglib | jsp 自定义标签 (将会废弃)
feilong-tools | 可用性操作
feilong-office-csv | csv生成操作
feilong-office-excel | excel操作,xml配置式来生成和读取excel文件
feilong-office-zip | 压缩解压缩操作
feilong-component  | 组件式操作,含配置式即可获取数据-->转成excel-->打成zip压缩包-->发送邮件   (推荐)
feilong | 一体化total jar包,包含上述所有功能
feilong-with-optional | 一体化total jar包,包含上述所有功能,且包含所有optional jar依赖

## :memo: 说明

1. 基于 [Apache2](https://www.apache.org/licenses/LICENSE-2.0) 协议,您可以下载代码用于闭源项目,但每个修改的过的文件必须放置 [版权说明](https://github.com/ifeilong/feilong/blob/master/LICENSE) ;

## :memo: 常见问题

- [使用 feilong 的理由](https://github.com/ifeilong/feilong/wiki/Reasons-for-use-feilong-core)
- [feilong3 VS feilong core2 对比](https://github.com/ifeilong/feilong/wiki/feilong3%20VS%20feilong%20core2%20%E5%AF%B9%E6%AF%94)
- [feilong VS hutool对比](https://github.com/ifeilong/feilong/wiki/feilong%20VS%20hutool%E5%AF%B9%E6%AF%94)

## :panda_face: 提bug反馈或建议

提交问题反馈 [Github issue](https://github.com/ifeilong/feilong/issues)

## :cyclone: feilong 即时交流

|QQ 群 `243306798` | 微信公众号 `feilong飞龙`
|:---------|:---------
|![](http://i.imgur.com/cIfglCa.png)  |![](https://ifeilong.oss-cn-beijing.aliyuncs.com/%E6%89%AB%E7%A0%81_%E6%90%9C%E7%B4%A2%E8%81%94%E5%90%88%E4%BC%A0%E6%92%AD%E6%A0%B7%E5%BC%8F-%E6%A0%87%E5%87%86%E8%89%B2%E7%89%88.png)
