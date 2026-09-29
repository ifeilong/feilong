feilong 让Java开发更简便的工具库
================

[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
![JDK 1.8](https://img.shields.io/badge/JDK-1.8-green.svg "JDK 1.8")
[![Maven Central](https://img.shields.io/maven-central/v/com.github.ifeilong/feilong.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.ifeilong%22%20AND%20a:%22feilong%22)
[![CI](https://github.com/ifeilong/feilong/actions/workflows/maven.yml/badge.svg)](https://github.com/ifeilong/feilong/actions/workflows/maven.yml)
![tests](https://img.shields.io/badge/tests-SuiteTests-success.svg "各模块单元测试入口为 SuiteTests")
![javadoc](https://img.shields.io/badge/javadoc-中文-brightgreen.svg "中文 javadoc，每个方法带示例与异常说明")

[English](README.en.md) | **简体中文**

Reduce development, Release ideas (灵感从重复简单的代码中释放出来)

## 30 秒了解

- **20+ 模块、覆盖日常 Java 开发全场景**：日期、集合/Map、字符串、IO、JSON、XML、HTTP、FTP/SFTP、邮件、钉钉/企业微信机器人、加密、CSV/Excel/ZIP、分页标签、Spring 集成……
- **静态工具类 + 中文 javadoc**：每个方法都写清"什么情况返回什么、什么情况抛什么异常"，可直接照抄示例。
- **一处引依赖**：中央仓库只发布聚合包 `com.github.ifeilong:feilong`（由 `maven-shade-plugin` 打包的一体化 jar），引入一次即可用上全部模块功能；各子模块**不作为独立坐标发布**。
- **运行时异常化 + 统一校验语义**：`Validate` 的 NPE / IllegalArgumentException 语义固定，不再到处写 `try/catch` 和判空。
- **兼容 JDK 8 起**，CI 同时用 JDK 8 / 17 编译验证。

> 详细的帮助文档：https://feilong.gitbook.io/feilong-docs

## 目录

- [一、为什么选择 feilong](#一为什么选择-feilong)
- [二、5 分钟上手](#二5-分钟上手)
- [三、核心能力速览](#三核心能力速览)
- [四、设计约定](#四设计约定)
- [五、质量与兼容性](#五质量与兼容性)
- [六、适合谁 / 什么场景需要谨慎](#六适合谁--什么场景需要谨慎)
- [七、feilong 的历史](#七feilong-的历史)
- [八、Maven / Gradle / 非 Maven 配置](#八maven--gradle--非-maven-配置)
- [九、自行 install](#九自行-install)
- [十、子模块介绍](#十子模块介绍)
- [常用组件 / 功能](#常用组件--功能)
- [Star History](#star-history)
- [说明 / 常见问题 / 反馈](#说明--常见问题--反馈)

## 一、为什么选择 feilong

| 优点 | 具体表现 |
|---|---|
| **中文 API 文档最完善的工具库之一** | 全中文 javadoc；每个方法带**示例**、**返回值说明**、**异常条件**（`如果 xxx 是 null,抛出 {@link NullPointerException}`），不需要翻源码猜行为 |
| **覆盖面广，减少多库拼装** | 一个项目里常见的日期/集合/IO/JSON/XML/HTTP/邮件/机器人/加密/办公文件，feilong 都有对应模块，API 风格统一 |
| **异常语义统一且可预期** | 底层 checked 异常统一转成运行时异常（如 `DefaultRuntimeException`、`ReflectException`、`URIParseException`），`Validate` 的校验语义固定（见 [四、设计约定](#四设计约定)） |
| **常量类统一** | 日期格式 `DatePattern`、字符集 `CharsetType`、时间间隔 `TimeInterval`、随机字符集 `Alphabet`、HTTP 方法 `HttpMethodType`……避免魔法值散落 |
| **办公场景开箱即用** | CSV / Excel（xml 配置式）/ ZIP 压缩解压 / 邮件 / 钉钉、企业微信机器人；`feilong-component` 还提供"取数 → 生成 Excel → 打 ZIP → 发邮件"的配置式流水线 |
| **Web / Spring 项目友好** | `feilong-servlet`（request/response/header/cookie/session 快捷封装）、`feilong-accessor`（session/cookie 存取）、`feilong-namespace`（Spring XML 配置式装配） |
| **版本统一，不会错配** | 全部模块由 `feilong-parent` 统一管理版本、同版本发布；使用方只需要一个坐标 |
| **低门槛** | 兼容 JDK 8+（Android 未测试）；不依赖 Spring 也能用核心模块 |

### 对比：同样的功能，代码量差多少

**① 日期格式化 / 解析 / 计算间隔**

```java
// 原生写法：每个方法都要 new SimpleDateFormat、处理 ParseException
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
Date now = new Date();
String nowText = sdf.format(now);
Date begin;
try{
    begin = sdf.parse("2016-08-01 00:00:00");
}catch (ParseException e){
    throw new RuntimeException(e);
}
long diffMillis = Math.abs(now.getTime() - begin.getTime());
long days = diffMillis / 1000 / 60 / 60 / 24;
```

```java
// feilong 写法
String nowText = DateUtil.nowString(DatePattern.COMMON_DATE_AND_TIME);
Date begin = DateUtil.toDate("2016-08-01 00:00:00", DatePattern.COMMON_DATE_AND_TIME);
int days = DateUtil.getIntervalDay(begin, new Date());
String duration = DateUtil.formatDuration(begin, new Date());   // 例: 6天13小时3分钟53秒259毫秒
```

**② 判空 / 校验 / 取集合元素**

```java
// 原生写法
if (name == null || name.trim().isEmpty()){
    throw new IllegalArgumentException("name can't be blank!");
}
if (list.isEmpty()){                       // 还要先判 list != null
    return null;
}
String first = list.get(0);
```

```java
// feilong 写法：异常语义固定，null/blank 分别抛什么一目了然
Validate.notBlank(name, "name can't be blank!");
String first = CollectionsUtil.first(list);
```

### 更多对比

- [使用 feilong-core 的理由](https://github.com/ifeilong/feilong/wiki/Reasons-for-use-feilong-core)
- [feilong VS hutool 对比](https://github.com/ifeilong/feilong/wiki/feilong%20VS%20hutool%E5%AF%B9%E6%AF%94)
- [feilong3 VS feilong core2 对比](https://github.com/ifeilong/feilong/wiki/feilong3%20VS%20feilong%20core2%20%E5%AF%B9%E6%AF%94)

<details>
<summary>历史对比图（早期 wiki 截图）</summary>

![](http://i.imgur.com/NCuo13D.png)

**对比1:**

![](http://i.imgur.com/rJnESSq.png)

**对比2:**

![](http://i.imgur.com/FG9ty3Q.png)

</details>

## 二、5 分钟上手

### 日期与时间

```java
// 格式化 / 解析
String now = DateUtil.nowString(DatePattern.COMMON_DATE_AND_TIME);
Date date = DateUtil.toDate("2016-08-01", DatePattern.COMMON_DATE);
String text = DateUtil.toString(date, DatePattern.COMMON_DATE);

// 区间计算（返回绝对值）
int months = DateUtil.getIntervalMonth(begin, end);
int days = DateUtil.getIntervalDay(begin, end);
long millis = DateUtil.getIntervalTime(begin, end);

// 中文可读的耗时
String duration = DateUtil.formatDuration(begin, end);   // 6天13小时3分钟53秒259毫秒
```

### 集合与 Map

```java
String first = CollectionsUtil.first(list);          // 第一个元素
String last = CollectionsUtil.last(list);            // 最后一个元素
String third = CollectionsUtil.get(list, 2);         // 第 index 个元素

Map<String, Integer> copy = MapUtil.newHashMap(source);          // 新 HashMap
Map.Entry<String, Integer> entry = MapUtil.getByIndex(source, 0); // 按索引取 entry
Map<String, String[]> arrayValueMap = MapUtil.toArrayValueMap(source);
```

### 校验

```java
Validate.notNull(user, "user can't be null!");            // null → NullPointerException
Validate.notBlank(name, "name can't be blank!");          // null → NPE, blank → IllegalArgumentException
Validate.notEmpty(list, "list can't be null/empty!");     // null → NPE, empty → IllegalArgumentException
Validate.isTrue(maxLength > 0, "maxLength:[%s] must > 0", maxLength);
```

### JSON / XML

```java
String json = JsonUtil.format(user, 0, 4);           // 缩进 4 输出
User parsed = JsonUtil.toBean(json, User.class);
String text = JsonUtil.toString(user);

Map<String, Object> map = XmlUtil.toMap(xml, "root"); // 指定根元素名
User user2 = XmlUtil.toBean(xml, User.class);
```

### HTTP

```java
Map<String, String> requestParamMap = MapUtil.newHashMap(8);
requestParamMap.put("q", "feilong");

String body = HttpClientUtil.getResponseBodyAsString("https://example.com/api", requestParamMap, "get");
```

### 文件与 IO

```java
byte[] bytes = FileUtil.toByteArray("/data/input/a.txt");     // 文件 → byte[]
IOWriteUtil.write(inputStream, "/data/out/", "a.txt");        // 自动创建父目录

// 解压：内部会做 zip slip 校验，条目若逃出目标目录直接抛 IllegalArgumentException
new CompressUnzipHandler().unzip("/data/a.zip", "/data/out/");
```

### 办公：CSV / Excel / ZIP / 邮件 / 机器人

```java
String[] columnTitles = { "a", "b" };
List<Object[]> dataList = new ArrayList<>();
dataList.add(ConvertUtil.toArray("0金,鑫", "0jin'\"xin"));

new DefaultCsvWrite().write("/data/out/users.csv", columnTitles, dataList, new CsvConfig(CharsetType.GBK));
```

- **Excel**：`feilong-office-excel`，用 xml 定义 sheet/列/样式，代码只负责填数据
- **ZIP**：`feilong-office-zip`，`CompressZipHandler` / `CompressUnzipHandler`
- **邮件**：`feilong-net-mail`，支持附件、内联图片、ICS 日历邮件
- **机器人**：`feilong-net-bot-dingtalk`（钉钉 markdown/link）、`feilong-net-bot-wxwork`（企业微信 图文）
- **组合流水线**：`feilong-component` 配置式完成"取数 → Excel → ZIP → 邮件"

## 三、核心能力速览

| 场景 | 入口类（括号内为所在模块，便于浏览源码；子模块非独立发布坐标） |
|---|---|
| 日期格式化 / 解析 / 区间 / 加减 | `DateUtil`、`DatePattern`、`TimeInterval`（feilong-core） |
| 集合 / List / Set 操作 | `CollectionsUtil`（feilong-core） |
| Map 操作、键值合并、排序 | `MapUtil`、`SortUtil`（feilong-core） |
| 字符串 / 正则 / 数字 / 布尔 | `StringUtil`、`RegexUtil`、`NumberUtil`、`BooleanUtil`（feilong-core） |
| 参数校验 | `Validate`（feilong-core） |
| 参数类型转换 | `ConvertUtil`（feilong-core） |
| URL / URI / 查询串拼装 | `URLUtil`、`URIUtil`、`ParamUtil`（feilong-core） |
| 反射 / Bean / 字段 | `BeanUtil`、`FieldUtil`、`MethodUtil`、`ClassUtil`（feilong-core） |
| 线程池 / 分批执行 | `ThreadUtil`、`PartitionThreadExecutor`、`BatchProcessorUtil`（feilong-core / feilong-context） |
| 文件 / 目录 / IO 流 | `FileUtil`、`FilenameUtil`、`IOReaderUtil`、`IOWriteUtil`、`InputStreamUtil`（feilong-io） |
| JSON | `JsonUtil`、`JsonConfigBuilder`、各种 `*JsonValueProcessor`（feilong-json） |
| XML | `XmlUtil`、`XStreamBuilder`（feilong-xml） |
| HTTP 调用 | `HttpClientUtil`、`HttpRequest`、`ConnectionConfig`（feilong-net-http） |
| HTML 解析 | `JsoupUtil`（feilong-net-jsoup） |
| FTP / SFTP | `FileTransfer`、`FTPFileTransfer`、`SFTPFileTransfer`（feilong-net-filetransfer） |
| 邮件（含附件/日历） | `MailSender`、`EmailCalendar`（feilong-net-mail） |
| 钉钉 / 企业微信机器人 | `DefaultDingTalkBot`、`DefaultWxworkBot`（feilong-net-bot-*） |
| 加密 / 摘要 / 签名 | `SymmetricEncryption`、`AesUtil`、`MD5Util`、`SHA256Util`、`Sm3Util`、`Base64Util`（feilong-security） |
| 手机号 / 邮箱 / 邮编等校验 | `MobileUtil`、`EmailAddressUtil`、`ValidatorUtil`（feilong-tools / feilong-validator） |
| 随机串 / 随机数 | `RandomUtil`（feilong-core） |
| 友好打印（Map/Bean/List） | `FormatterUtil`（feilong-formatter） |
| 模板渲染 | `VelocityUtil`（feilong-template） |
| CSV / Excel / ZIP | `DefaultCsvWrite`、`ExcelWriteUtil`、`CompressZipHandler`（feilong-office-*） |
| request/response/header/cookie/session | `RequestUtil`、`ResponseUtil`、`HttpHeaders`、`CookieUtil`、`SessionUtil`（feilong-servlet / feilong-accessor） |
| 敏感信息脱敏（JSP 标签） | `SensitiveUtil` 及 taglib（feilong-taglib） |
| Spring XML 配置式装配 | `feilong-namespace` |

## 四、设计约定

### Validate 的异常语义（固定，不会随方法变）

| 调用 | 传入 null | 传入空/blank |
|---|---|---|
| `Validate.notNull(o)` | `NullPointerException` | — |
| `Validate.notBlank(s)` | `NullPointerException` | `IllegalArgumentException` |
| `Validate.notEmpty(c)` | `NullPointerException` | `IllegalArgumentException` |
| `Validate.isTrue(false, …)` | — | `IllegalArgumentException` |
| `Validate.validState(false, …)` | — | `IllegalStateException` |

> 这也是本项目 javadoc 统一采用的描述方式：先写**返回值**，再写**什么情况抛什么异常**。

### 常量类优先于魔法值

`DatePattern`（日期格式）、`CharsetType`（字符集）、`TimeInterval`（时间间隔）、`Alphabet`（随机字符集）、`HttpMethodType`（HTTP 方法）、`HttpHeaders`（请求头）等，避免字符串常量到处复制。

### 运行时异常优先

底层 `IOException`、`ParseException`、`ReflectiveOperationException` 等统一包装成运行时异常（`DefaultRuntimeException`、`ReflectException`、`URIParseException`、`UncheckedIOException` 等），让业务代码不必写大量 `try/catch`。

## 五、质量与兼容性

- **单元测试**：各模块以 `SuiteTests` 作为测试入口；CI 会运行与外部服务无关的模块。
- **CI**：`.github/workflows/maven.yml` —— JDK 8 / 17 双版本编译 + 无外网依赖模块的单元测试。
- **javadoc**：每个方法带示例与异常说明；可用 `mvn -Pstrict-javadoc javadoc:javadoc` 把 javadoc 的 `-Xdoclint` 告警当错误跑（`-Pstrict-javadoc` 为可选 profile，默认构建不受影响）。
- **JDK 兼容**：JDK 8 起（Android 未测试；JDK 7 请用 feilong-core 2.1.0）。
- **安全提示**：
  - `feilong-office-zip` 解压已加入 **zip slip 防护**（条目逃出目标目录会抛 `IllegalArgumentException`）；
  - `feilong-security` 中的 MD5/SHA1/DES 等旧算法**仅为兼容历史数据**保留，新项目建议使用 AES 等强算法；
  - `feilong-xml` 的 `XStreamBuilder` 目前仍放开 XStream 反序列化类型（为兼容已发布的 jar 行为），**请只用于可信 XML**；后续大版本会收紧。

## 六、适合谁 / 什么场景需要谨慎

**适合**

- 需要**中文文档**、希望少写样板代码的业务项目；
- 需要把"日期/集合/IO/JSON/HTTP/邮件/办公文件"统一风格的团队；
- 已有 Spring / Servlet 项目，想快速补齐工具层；
- 需要"配置式"完成 Excel、CSV、ZIP、邮件、机器人通知的场景。

**需要谨慎 / 评估**

- 已经在用 `commons-lang3` / `hutool` / Guava 且只需单个工具类：避免功能重复引入；
- 对依赖体积敏感：目前**只发布聚合包** `feilong`（包含各模块及其必需依赖），无法只取单个子模块；若只需要一两个工具类，可先评估直接用 `commons-lang3` / `hutool` 是否更合适；
- 偏好函数式/不可变风格：feilong 是**静态工具类 + 运行时异常**风格；
- 需要用旧算法（MD5/DES）承载安全场景，或需要直接反序列化不可信 XML/Java 对象。

## 七、feilong 的历史

since 2008, 起初应对开发过程中不断重复的代码进行了封装,进而在公司内部推广

- `2016-09-22` 开源了 [feilong-core](https://www.oschina.net/p/feilong-core) (2020年停止更新维护)
- `2016-10-31` 开源了 [feilong-taglib](https://www.oschina.net/p/feilong-taglib) (2020年停止更新维护)
- `2020-05-26` 开源了 [feilong](https://www.oschina.net/p/feilong)

## 八、Maven / Gradle / 非 Maven 配置

feilong 自从3.0.0开始,发布中央仓库 https://search.maven.org/artifact/com.github.ifeilong/feilong

### `maven 配置`

```XML
<dependency>
	<groupId>com.github.ifeilong</groupId>
	<artifactId>feilong</artifactId>
	<version>4.5.6</version>
</dependency>
```

> 说明：
> - 中央仓库**只发布聚合包 `feilong`**：`maven-shade-plugin` 把 `com.github.ifeilong:*` 各子模块的类打平到同一个 jar；子模块（`feilong-core`、`feilong-json`…）不作为独立坐标发布，无法单独引入。
> - **第三方依赖不会缺**：shade 只打包 feilong 自己的模块，并使用 dependency-reduced POM + `promoteTransitiveDependencies`，第三方依赖（velocity、poi、javax.mail、jsoup、xstream、cxf 等）仍由 `feilong` 的 POM 正常解析。
> - 同时会附带 sources jar 与 javadoc jar。

### `Gradle 配置`

```
com.github.ifeilong:feilong:4.5.6
```

### `非Maven项目`

点击 https://repo1.maven.org/maven2/com/github/ifeilong/feilong/ 链接，下载 feilong.jar即可：

**注意:**
- feilong 3 需要 JDK8+，对Android平台没有测试，不能保证所有工具类或工具方法可用。
- 如果你的项目使用 JDK7，请使用 [feilong-core 2.1.0](https://github.com/ifeilong/feilong-core)  版本

## 九、自行 install

有些小伙伴想下载并 `自行install` 进行研究, 你需要执行以下 `2` 个步骤:

```bat
git clone https://github.com/ifeilong/feilong.git --depth 1
mvn install
```

## 十、子模块介绍

**基础工具**

module | 介绍
:----  | :---------
feilong-core |  核心包 (推荐)
feilong-core-extension | 基于 feilong-core 的扩展 (awt, io 等)
feilong-tools | 可用性操作 (手机号/邮箱等)
feilong-validator |  常用的校验, 包含可配置式的手机号码, 邮编等等
feilong-formatter | 将Map,bean,list format成友好形式
feilong-security | 加密解密操作

**数据格式**

module | 介绍
:----  | :---------
feilong-json  | json format以及tobean toMap等常见操作
feilong-xml  | xml format以及tobean toMap等常见操作
feilong-template | 模板操作,如velocity
feilong-io | 文件常见操作

**网络 / 通信**

module | 介绍
:----  | :---------
feilong-net-http | http封装操作  (推荐)
feilong-net-api | http 请求/响应相关的基础 api
feilong-net-jsoup | jsoup操作
feilong-net-filetransfer | ftp/sftp操作   (推荐)
feilong-net-mail | 发送邮件,接收邮件操作
feilong-net-cxf | cxf操作
feilong-net-bot-api | 机器人消息发送的基础 api
feilong-net-bot-dingtalk | 钉钉机器人
feilong-net-bot-wxwork | 企业微信机器人

**Web / Spring 集成**

module | 介绍
:----  | :---------
feilong-servlet | 基于http servlet 的封装,含常见request,response操作快捷封装  (推荐)
feilong-accessor | 便捷式使用session ,cookie
feilong-context | 上下文操作  (推荐)
feilong-namespace | 可以spring xml 来配置的便捷操作
feilong-taglib | jsp 自定义标签 (将会废弃)

**办公 / 组合**

module | 介绍
:----  | :---------
feilong-office | csv/excel/zip 等办公相关操作的聚合
feilong-office-csv | csv生成操作
feilong-office-excel | excel操作,xml配置式来生成和读取excel文件
feilong-office-zip | 压缩解压缩操作
feilong-component  | 组件式操作,含配置式即可获取数据-->转成excel-->打成zip压缩包-->发送邮件   (推荐)

**一体化 / 其他**

module | 介绍
:----  | :---------
feilong | **发布到中央仓库的聚合包**（shade 成一体化 jar，包含上述所有模块）
feilong-with-optional | **未发布到中央仓库**：仅列出"使用全部功能"所需的完整依赖，可作依赖清单参考
feilong-lib | 从第三方库 fork 而来的源码 (commons-lang3, commons-io, commons-compress 等,请不要直接调用)

## Star History

[![Star History Chart](https://api.star-history.com/svg?repos=ifeilong/feilong&type=Timeline)](https://star-history.com/#ifeilong/feilong&Timeline)

## :memo: 常用组件/功能

- [使用feilong发企业微信机器人](https://github.com/ifeilong/feilong/wiki/使用feilong发企业微信机器人)

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
