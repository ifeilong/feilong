feilong 3.4.0 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>3.4.0</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:3.4.0
```


本次升级共有 `22` 处变更, 具体参见  [3.4.0 milestone](https://github.com/ifeilong/feilong/milestone/29?closed=1)



## :alien: [不兼容](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A不兼容+milestone%3A3.4.0+is%3Aclosed)

#498 :fire: 删除 feilong-lib-xstream   [[依赖调整](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖调整+milestone%3A3.4.0+is%3Aclosed)  [删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.4.0+is%3Aclosed)]

如果你需要使用feilong  xstream 相关工具类或者组件,比如 `com.feilong.context.converter.XStreamStringToBeanConverter`, `com.feilong.xml.XmlUtil`, 你需要自行依赖 


```xml
 <dependency>
        <groupId>com.thoughtworks.xstream</groupId>
        <artifactId>xstream</artifactId>
        <version>1.4.20</version> 
      </dependency>
```


## :guitar: [feilong-context](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-context+milestone%3A3.4.0+is%3Aclosed)

#503  SimpleMapRequestHeaderBuilder 修改注释  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A3.4.0+is%3Aclosed)]
#502  SimpleMapRequestHeaderBuilder 新增map 的构造函数, 简化构造
#497  XStreamStringToBeanConverter 修改注释  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A3.4.0+is%3Aclosed)]
#496  com.feilong.context.converter.IfStringToBeanConverter<T> 新增注释  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A3.4.0+is%3Aclosed)]
#501  修改 XMLMapBuilderStringToBeanConverter 注释  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A3.4.0+is%3Aclosed)]
#500  修改JsonStringToBeanConverter 注释  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A3.4.0+is%3Aclosed)]


## :meat_on_bone: [feilong-json](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-json+milestone%3A3.4.0+is%3Aclosed)

#505  修改 CapitalizePropertyNameProcessor 注释  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A3.4.0+is%3Aclosed)]
#504 :sparkles: 新建 SimpleMapPropertyNameProcessor 简单的map 隐射实现  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.4.0+is%3Aclosed)]


## :lock: [feilong-security](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-security+milestone%3A3.4.0+is%3Aclosed)

#57 :sparkles: 建议在MD5Util里面新增一个根据byte数组或InputStream加密的方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.4.0+is%3Aclosed)]
#507 :sparkles: 新增 com.feilong.security.oneway.SHA1Util.encode(byte[]) 方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.4.0+is%3Aclosed)]
#509 :sparkles: 新增 com.feilong.security.oneway.SHA384Util.encode(byte[]) 方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.4.0+is%3Aclosed)]
#511 :sparkles: 新增 com.feilong.security.oneway.Sm3Util.encode(byte[]) 方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.4.0+is%3Aclosed)]
#508 :sparkles: 新增com.feilong.security.oneway.SHA256Util.encode(byte[]) 方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.4.0+is%3Aclosed)]
#510 :sparkles: 新增com.feilong.security.oneway.SHA512Util.encode(byte[]) 方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.4.0+is%3Aclosed)]
#506 :sparkles: 新建个base64Util  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.4.0+is%3Aclosed)]


## :fork_and_knife: [feilong-xml](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-xml+milestone%3A3.4.0+is%3Aclosed)

#40  Security framework of XStream not initialized, XStream is probably vulnerable
#320  XStream 漏洞事宜


## :arrow_up: [依赖升级](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖升级+milestone%3A3.4.0+is%3Aclosed)

#493  logback 升级到 	1.4.5
#494  maven-dependency-plugin 升级到3.4.0
#495  versions-maven-plugin 升级到 2.14.2


## :heavy_minus_sign: [依赖调整](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖调整+milestone%3A3.4.0+is%3Aclosed)

#499  添加 xstream  为可选依赖  [[feilong-xml](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-xml+milestone%3A3.4.0+is%3Aclosed)]

如果你需要使用feilong  xstream 相关工具类或者组件,比如 `com.feilong.context.converter.XStreamStringToBeanConverter`, `com.feilong.xml.XmlUtil`, 你需要自行依赖 

```xml
 <dependency>
        <groupId>com.thoughtworks.xstream</groupId>
        <artifactId>xstream</artifactId>
        <version>1.4.20</version> 
      </dependency>
```