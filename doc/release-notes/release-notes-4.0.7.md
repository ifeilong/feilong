feilong 4.0.7 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>4.0.7</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:4.0.7
```


本次升级共有 `6` 处变更, 具体参见  [4.0.7 milestone](https://github.com/ifeilong/feilong/milestone/41?closed=1)



## :bug: [bug](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Abug+milestone%3A4.0.7+is%3Aclosed)

#662  RequestLogHelper 日志 paramString null显示成empty  [[feilong-servlet](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-servlet+milestone%3A4.0.7+is%3Aclosed)  [feilong-context](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-context+milestone%3A4.0.7+is%3Aclosed)]


## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A4.0.7+is%3Aclosed)

#666 :sparkles: com.feilong.core.util.CollectionsUtil 新建 `Pair<List<O>, List<O>> splitDuplicate(Collection<O> objectCollection,String...propertyNames)`方法,根据属性值,分隔新数据和重复的数据  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A4.0.7+is%3Aclosed)]

使用场景:

数据库中查询到以下数据,该数据来源都是MQ发过来的数据,原样保存到了数据库

现在在处理业务逻辑的时候 发现 code列有数据重复, 现在分开来逻辑处理,  比如 id `28958`和 `28142` 重复了, `28958`是新数据, `28142`是老数据, 现在需要把新数据`28958`更新到A表,而老数据`28142`做逻辑删除处理


id | create_time | code | type
-- | -- | -- | --
29144 | 2023-12-13T16:20:35 | 79598160 | 一般
29147 | 2023-12-13T16:20:35 | 79598155 | 独家
**`28958`** | 2023-12-13T16:20:35 | **`79584312`** | 一般
**`28142`** | 2023-12-13T10:07:16 | **`79584312`** | 一般
28225 | 2023-12-13T10:07:16 | 79584239 | 独家
**`28999`** | 2023-12-13T16:20:35 | **`79584197`** | 一般
**`28233`** | 2023-12-13T10:07:16 | **`79584197`** | 一般
28208 | 2023-12-13T10:07:16 | 79584172 | 独家
28139 | 2023-12-13T10:07:16 | 79583943 | 一般

此时你可以使用以下代码:

```JAVA
   //按照创建时间排过序了
List<Dto> newStatusData = dao.queryNewStatusData();
if (isNullOrEmpty(newStatusData)){
    return;
}

Pair<List<Dto>, List<Dto>> pair = CollectionsUtil.splitDuplicate(newStatusData,"code");

//查找重复的, 留下最新的, 把老的数据直接设置成delete

//对应上表格中,剔除 (28142 和 28233)的数据
List<Dto> newData = pair.getLeft();
//do logic

//对应上表格中的  28142 和 28233
List<Dto> duplicateData = pair.getRight();
//do logic

```









## :arrow_up: [依赖升级](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖升级+milestone%3A4.0.7+is%3Aclosed)

#664  logback 升级到1.3.13
#659  maven-project-info-reports-plugin 升级到3.5.0
#660  maven-surefire-plugin 升级到 3.2.2
#661  versions-maven-plugin 升级到 2.16.2
