feilong 4.0.4 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>4.0.4</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:4.0.4
```


本次升级共有 `2` 处变更, 具体参见  [4.0.4 milestone](https://github.com/ifeilong/feilong/milestone/38?closed=1)



## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A4.0.4+is%3Aclosed)

#637 :sparkles: loghelper 添加一个已经执行了多长时间  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.4+is%3Aclosed)]

效果
```
进度: [6/7] 85.71% ,perUseTimes: 2秒5毫秒 ,已经执行时间(elapsedTime): 13秒215毫秒 ,预估剩余时间(estimatedRemainingTime): 2秒5毫秒
```


## :meat_on_bone: [feilong-json](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-json+milestone%3A4.0.4+is%3Aclosed)

#638 :sparkles: 新建 com.feilong.json.JsonUtil.toString(Object obj,boolean isIgnoreNullValueElement,String...ifIgnoreNullValueElementIncludes) 忽略空和空白, 但是有的属性要支持白名单,强行输出null  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.4+is%3Aclosed)]
