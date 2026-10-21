feilong 3.4.1 发布了，让Java开发更简便的工具库

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/



maven 依赖配置:

```xml
<dependency>
     <groupId>com.github.ifeilong</groupId>
     <artifactId>feilong</artifactId>
     <version>3.4.1</version>
</dependency>
```


Gradle 依赖配置:

```java
com.github.ifeilong:feilong:3.4.1
```


本次升级共有 `3` 处变更, 具体参见  [3.4.1 milestone](https://github.com/ifeilong/feilong/milestone/30?closed=1)



## :guitar: [feilong-context](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-context+milestone%3A3.4.1+is%3Aclosed)

#512  修改 com.feilong.context.invoker.DefaultResponseCommandBuilder.build(R) log 信息  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A3.4.1+is%3Aclosed)]
#513  修改 com.feilong.context.invoker.http.HttpRequestUriResolver.parse(String, T) 注释  [[javadoc](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajavadoc+milestone%3A3.4.1+is%3Aclosed)]
#514 :sparkles: 新建个简易版本的ResponseCommandBuilder  SimpleHttpAndJsonResponseCommandBuilder  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.4.1+is%3Aclosed)]

示例代码:

比如构造 一个post ,带requestbody, 设置制定的header 头, 并将请求响应的json字符串直接转成OpenApiResponse bean

```JAVA

  public ResponseCommandBuilder<R, OpenApiResponse> init(){
        SimpleHttpAndJsonResponseCommandBuilder<R, OpenApiResponse> responseCommandBuilder = new SimpleHttpAndJsonResponseCommandBuilder<>(
                        createUri(),
                        HttpMethodType.POST.getMethod(),
                        toMap(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8"));

        responseCommandBuilder.setResponseCommandRootClass(OpenApiResponse.class);
        responseCommandBuilder.setRequestBodyBuilder(createRequestBodyBuilder());

        return responseCommandBuilder;
    }

```
