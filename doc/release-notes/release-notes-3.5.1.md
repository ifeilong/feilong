feilong 3.5.1 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>3.5.1</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:3.5.1
```


本次升级共有 `13` 处变更, 具体参见  [3.5.1 milestone](https://github.com/ifeilong/feilong/milestone/32?closed=1)



## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A3.5.1+is%3Aclosed)

#572 :sparkles: DateUtil.formatDuration(Date) 自身耗时长  换成 System.currentTimeMillis()  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.5.1+is%3Aclosed)]
#573 :wastebasket: make DateUtil.formatDuration(Date) @Deprecated  [[deprecated](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Adeprecated+milestone%3A3.5.1+is%3Aclosed)]
#568 :sparkles: 新建 com.feilong.core.util.CollectionsUtil.setIfCondition(List<T>, int, T, boolean)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.5.1+is%3Aclosed)]


## :wine_glass: [feilong-net](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net+milestone%3A3.5.1+is%3Aclosed)

#578 :sparkles: 新建 HttpClientUtil String patch(String uri,String requestBody)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.5.1+is%3Aclosed)]
#579 :sparkles: 新建 HttpClientUtil String patch(String uri,String requestBody,Map<String, String> headerMap)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.5.1+is%3Aclosed)]
#575 :sparkles: 新建 com.feilong.net.http.HttpClientUtil.get(String, Map<String, String>,headerMap) 方法  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.5.1+is%3Aclosed)]
#577 :sparkles: 新建 com.feilong.net.http.HttpClientUtil.post(String, Map<String, String>, Map<String, String>)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.5.1+is%3Aclosed)]
#576 :sparkles: 新建 com.feilong.net.http.HttpClientUtil.put(String, Map<String, String>, Map<String, String>)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.5.1+is%3Aclosed)]


## :baby_bottle: [feilong-servlet](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-servlet+milestone%3A3.5.1+is%3Aclosed)

#581  com.feilong.servlet.http.RequestUtil.getRequestBody(HttpServletRequest) 修改如果有异常返回空字符串  [[逻辑调整](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A逻辑调整+milestone%3A3.5.1+is%3Aclosed)]
#583 :sparkles: 新增 com.feilong.servlet.http.RequestUtil.isPostMethod(HttpServletRequest) 判断请求是不是post请求  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.5.1+is%3Aclosed)]
#580 :sparkles: 新建 com.feilong.servlet.http.RequestUtil.isGetMethod(HttpServletRequest) 判断请求是不是get 请求   [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.5.1+is%3Aclosed)]


## :arrow_up: [依赖升级](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖升级+milestone%3A3.5.1+is%3Aclosed)

#570  maven-clean-plugin 升级到 3.3.1
#569  maven-war-plugin 升级到 3.4.0	
