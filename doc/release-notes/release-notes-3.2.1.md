feilong 3.2.1 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>3.2.1</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:3.2.1
```


本次升级共有 `9` 处变更, 具体参见  [3.2.1 milestone](https://github.com/ifeilong/feilong/milestone/16?closed=1)



## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A3.2.1+is%3Aclosed)

#406 :sparkles: Dateutil todate 新增模式的pattern 简化写法, 默认方法使用 yyyy-MM-dd 模式  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.2.1+is%3Aclosed)]

即 你可以使用下面的写法

```java
DateUtil.toDate("2022-09-08")
```

代替以前必须传 `yyyy-MM-dd`

```java
DateUtil.toDate("2022-09-08","yyyy-MM-dd")
```

```java
DateUtil.toDate("2022-09-08",DatePattern.COMMON_DATE)
```

#403 :sparkles: uriutil encode decode 增加默认utf-8的方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.2.1+is%3Aclosed)]
#387 :sparkles: 封装个方法来简化下面设置url 的代码  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.2.1+is%3Aclosed)]

新建 `com.feilong.core.net.URLUtil.ifNoHttpOrHttpsProtocolPrependPre(String, String)`

如以下10行代码, 作用是判断LogoPic 是不是空, 如果不是, 判断是不是以http://开头,  不是以这个开头会拼接前缀
```java
if (userInfo.getLogoPic() != null && !"".equals(userInfo.getLogoPic())) {
            // 微信登录头像地址是绝对路径
            // 公司登录头像地址是相对路径
            if (userInfo.getLogoPic().startsWith("http://")) {
                result.setUserLogoPic(userInfo.getLogoPic());
            } else {
                result.setUserLogoPic(coverPerfix + userInfo.getLogoPic());
            }
        }
```

可以使用以下1行代码代替
```java
result.setUserLogoPic(URLUtil.ifNoHttpOrHttpsProtocolPrependPre(userInfo.getLogoPic(), coverPerfix));
```


#402 :sparkles: 让  CollectionsUtil.addIgnoreNullOrEmpty(Collection<T>, T) 支持动态数组  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.2.1+is%3Aclosed)]


## :wine_glass: [feilong-net](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net+milestone%3A3.2.1+is%3Aclosed)

#405  http log jsonformat 转成tostring   [[help wanted](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ahelp wanted+milestone%3A3.2.1+is%3Aclosed)]


## :arrow_up: [依赖升级](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖升级+milestone%3A3.2.1+is%3Aclosed)

#409  jsoup 升级到 1.15.3
#408  logback 升级到1.4.0
#410  maven-javadoc-plugin 升级到 3.4.1
#412  maven-project-info-reports-plugin 升级到3.4.1
