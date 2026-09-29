# feilong — a pragmatic Java utility library

[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
![JDK 1.8](https://img.shields.io/badge/JDK-1.8-green.svg "JDK 1.8")
[![Maven Central](https://img.shields.io/maven-central/v/com.github.ifeilong/feilong.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.ifeilong%22%20AND%20a:%22feilong%22)
[![CI](https://github.com/ifeilong/feilong/actions/workflows/maven.yml/badge.svg)](https://github.com/ifeilong/feilong/actions/workflows/maven.yml)
![tests](https://img.shields.io/badge/tests-SuiteTests-success.svg "each module exposes a SuiteTests entry point")

[简体中文](README.md) | **English**

> Reduce development, Release ideas.

## In 30 seconds

- **20+ modules covering everyday Java work**: dates, collections/maps, strings, IO, JSON, XML, HTTP, FTP/SFTP, mail, DingTalk / WeCom bots, crypto, CSV/Excel/ZIP, pagination tags, Spring integration.
- **Static utility classes with exhaustive javadoc**: every method documents what it returns and which exception it throws for which input (`@return` / `@throws`), with copy-pasteable examples.
- **One dependency or twenty**: use the all-in-one `feilong` jar, or pick individual modules (`feilong-core`, `feilong-json`, …).
- **Runtime exceptions + uniform validation semantics**: `Validate` always uses the same NPE / `IllegalArgumentException` rules, so you stop writing `try/catch` and null-guard boilerplate.
- **JDK 8+**, with CI compiling on both JDK 8 and JDK 17.

> Full documentation (Chinese): https://feilong.gitbook.io/feilong-docs

## Table of contents

- [1. Why feilong](#1-why-feilong)
- [2. Quick start (5 minutes)](#2-quick-start-5-minutes)
- [3. Capability map](#3-capability-map)
- [4. Design conventions](#4-design-conventions)
- [5. Quality & compatibility](#5-quality--compatibility)
- [6. Who it is for / when to be careful](#6-who-it-is-for--when-to-be-careful)
- [7. Module overview](#7-module-overview)
- [8. Installation](#8-installation)
- [9. Build from source](#9-build-from-source)
- [10. About the documentation language](#10-about-the-documentation-language)
- [License / feedback / community](#license--feedback--community)

## 1. Why feilong

| Advantage | What it means in practice |
|---|---|
| **Docs you can trust while coding** | Every method carries a Chinese javadoc block with an example, an explicit return-value description and the exact exception conditions (`if xxx is null, throws NullPointerException`). No need to read the implementation to guess behaviour. |
| **Broad coverage, one consistent style** | Dates, collections, IO, JSON, XML, HTTP, mail, bots, crypto and office files live in one project with the same API conventions, instead of gluing together several libraries. |
| **Predictable exception model** | Checked exceptions from the JDK are wrapped into runtime exceptions (`DefaultRuntimeException`, `ReflectException`, `URIParseException`, …), and `Validate` semantics are fixed (see [section 4](#4-design-conventions)). |
| **Constant classes instead of magic values** | `DatePattern`, `CharsetType`, `TimeInterval`, `Alphabet`, `HttpMethodType`, `HttpHeaders`, … |
| **Office scenarios out of the box** | CSV, config-driven Excel, ZIP, mail, DingTalk / WeCom bots; `feilong-component` wires "fetch data → Excel → ZIP → mail" declaratively. |
| **Friendly to Servlet / Spring projects** | `feilong-servlet` (request/response/header/cookie/session helpers), `feilong-accessor` (session & cookie access), `feilong-namespace` (Spring XML configuration). |
| **Low barrier to entry** | JDK 8+; core modules do not require Spring. |

### Side by side

**① Dates: format, parse, interval**

```java
// Plain JDK: a SimpleDateFormat per call plus ParseException handling
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String nowText = sdf.format(new Date());
Date begin;
try{
    begin = sdf.parse("2016-08-01 00:00:00");
}catch (ParseException e){
    throw new RuntimeException(e);
}
long days = Math.abs(System.currentTimeMillis() - begin.getTime()) / 1000 / 60 / 60 / 24;
```

```java
// feilong
String nowText = DateUtil.nowString(DatePattern.COMMON_DATE_AND_TIME);
Date begin = DateUtil.toDate("2016-08-01 00:00:00", DatePattern.COMMON_DATE_AND_TIME);
int days = DateUtil.getIntervalDay(begin, new Date());
String duration = DateUtil.formatDuration(begin, new Date());   // e.g. 6天13小时3分钟53秒259毫秒
```

**② Null-safety, validation and collection access**

```java
// Plain Java
if (name == null || name.trim().isEmpty()){
    throw new IllegalArgumentException("name can't be blank!");
}
if (list == null || list.isEmpty()){
    return null;
}
String first = list.get(0);
```

```java
// feilong: the exception type is documented and consistent
Validate.notBlank(name, "name can't be blank!");   // null -> NPE, blank -> IllegalArgumentException
String first = CollectionsUtil.first(list);
```

## 2. Quick start (5 minutes)

### Date & time

```java
String now = DateUtil.nowString(DatePattern.COMMON_DATE_AND_TIME);
Date date = DateUtil.toDate("2016-08-01", DatePattern.COMMON_DATE);
String text = DateUtil.toString(date, DatePattern.COMMON_DATE);

int months = DateUtil.getIntervalMonth(begin, end);   // absolute values
int days = DateUtil.getIntervalDay(begin, end);
long millis = DateUtil.getIntervalTime(begin, end);

String duration = DateUtil.formatDuration(begin, end);   // 6天13小时3分钟53秒259毫秒
```

### Collections & maps

```java
String first = CollectionsUtil.first(list);          // first element
String last = CollectionsUtil.last(list);            // last element
String third = CollectionsUtil.get(list, 2);         // element at index

Map<String, Integer> copy = MapUtil.newHashMap(source);            // new HashMap
Map.Entry<String, Integer> entry = MapUtil.getByIndex(source, 0);  // entry by index
Map<String, String[]> arrayValueMap = MapUtil.toArrayValueMap(source);
```

### Validation

```java
Validate.notNull(user, "user can't be null!");          // null -> NullPointerException
Validate.notBlank(name, "name can't be blank!");        // null -> NPE, blank -> IllegalArgumentException
Validate.notEmpty(list, "list can't be null/empty!");   // null -> NPE, empty -> IllegalArgumentException
Validate.isTrue(maxLength > 0, "maxLength:[%s] must > 0", maxLength);
```

### JSON / XML

```java
String json = JsonUtil.format(user, 0, 4);      // pretty print, indent 4
User parsed = JsonUtil.toBean(json, User.class);
String text = JsonUtil.toString(user);

Map<String, Object> map = XmlUtil.toMap(xml, "root");   // root element name
User user2 = XmlUtil.toBean(xml, User.class);
```

### HTTP

```java
Map<String, String> requestParamMap = MapUtil.newHashMap(8);
requestParamMap.put("q", "feilong");

String body = HttpClientUtil.getResponseBodyAsString("https://example.com/api", requestParamMap, "get");
```

### Files & IO

```java
byte[] bytes = FileUtil.toByteArray("/data/input/a.txt");   // file -> byte[]
IOWriteUtil.write(inputStream, "/data/out/", "a.txt");      // parent directories are created automatically

// Unzip: entries escaping the target directory are rejected (zip-slip protection)
new CompressUnzipHandler().unzip("/data/a.zip", "/data/out/");
```

### Office: CSV / Excel / ZIP / mail / bots

```java
String[] columnTitles = { "a", "b" };
List<Object[]> dataList = new ArrayList<>();
dataList.add(ConvertUtil.toArray("0金,鑫", "0jin'\"xin"));

new DefaultCsvWrite().write("/data/out/users.csv", columnTitles, dataList, new CsvConfig(CharsetType.GBK));
```

- **Excel** (`feilong-office-excel`): sheets, columns and styles are declared in XML; your code only fills in data.
- **ZIP** (`feilong-office-zip`): `CompressZipHandler` / `CompressUnzipHandler`.
- **Mail** (`feilong-net-mail`): attachments, inline images, ICS calendar mail.
- **Bots**: `feilong-net-bot-dingtalk` (DingTalk), `feilong-net-bot-wxwork` (WeCom).
- **Pipeline** (`feilong-component`): fetch data → Excel → ZIP → mail, configured declaratively.

## 3. Capability map

| Scenario | Entry class (module) |
|---|---|
| Date format / parse / interval / add | `DateUtil`, `DatePattern`, `TimeInterval` (feilong-core) |
| Collection, List, Set helpers | `CollectionsUtil` (feilong-core) |
| Map helpers, merge, sort | `MapUtil`, `SortUtil` (feilong-core) |
| String / regex / number / boolean | `StringUtil`, `RegexUtil`, `NumberUtil`, `BooleanUtil` (feilong-core) |
| Argument validation | `Validate` (feilong-core) |
| Type conversion | `ConvertUtil` (feilong-core) |
| URL / URI / query string | `URLUtil`, `URIUtil`, `ParamUtil` (feilong-core) |
| Reflection / bean / field | `BeanUtil`, `FieldUtil`, `MethodUtil`, `ClassUtil` (feilong-core) |
| Thread pools / batching | `ThreadUtil`, `PartitionThreadExecutor`, `BatchProcessorUtil` (feilong-core / feilong-context) |
| File, directory, stream IO | `FileUtil`, `FilenameUtil`, `IOReaderUtil`, `IOWriteUtil`, `InputStreamUtil` (feilong-io) |
| JSON | `JsonUtil`, `JsonConfigBuilder`, `*JsonValueProcessor` (feilong-json) |
| XML | `XmlUtil`, `XStreamBuilder` (feilong-xml) |
| HTTP client | `HttpClientUtil`, `HttpRequest`, `ConnectionConfig` (feilong-net-http) |
| HTML parsing | `JsoupUtil` (feilong-net-jsoup) |
| FTP / SFTP | `FileTransfer`, `FTPFileTransfer`, `SFTPFileTransfer` (feilong-net-filetransfer) |
| Mail | `MailSender`, `EmailCalendar` (feilong-net-mail) |
| DingTalk / WeCom bots | `DefaultDingTalkBot`, `DefaultWxworkBot` (feilong-net-bot-*) |
| Crypto / digest | `SymmetricEncryption`, `AesUtil`, `MD5Util`, `SHA256Util`, `Sm3Util`, `Base64Util` (feilong-security) |
| Mobile / e-mail / zip-code validation | `MobileUtil`, `EmailAddressUtil`, `ValidatorUtil` (feilong-tools / feilong-validator) |
| Random strings / numbers | `RandomUtil` (feilong-core) |
| Friendly printing (Map/Bean/List) | `FormatterUtil` (feilong-formatter) |
| Template rendering | `VelocityUtil` (feilong-template) |
| CSV / Excel / ZIP | `DefaultCsvWrite`, `ExcelWriteUtil`, `CompressZipHandler` (feilong-office-*) |
| Servlet request / response / cookie / session | `RequestUtil`, `ResponseUtil`, `HttpHeaders`, `CookieUtil`, `SessionUtil` (feilong-servlet / feilong-accessor) |
| Spring XML wiring | `feilong-namespace` |

## 4. Design conventions

### `Validate` exception semantics (fixed, per method)

| Call | Passed `null` | Passed blank / empty |
|---|---|---|
| `Validate.notNull(o)` | `NullPointerException` | — |
| `Validate.notBlank(s)` | `NullPointerException` | `IllegalArgumentException` |
| `Validate.notEmpty(c)` | `NullPointerException` | `IllegalArgumentException` |
| `Validate.isTrue(false, …)` | — | `IllegalArgumentException` |
| `Validate.validState(false, …)` | — | `IllegalStateException` |

### Constants over magic values

`DatePattern` (date formats), `CharsetType` (charsets), `TimeInterval`, `Alphabet` (random character sets), `HttpMethodType`, `HttpHeaders`, …

### Runtime exceptions by default

`IOException`, `ParseException`, `ReflectiveOperationException` and friends are wrapped into runtime exceptions (`DefaultRuntimeException`, `ReflectException`, `URIParseException`, `UncheckedIOException`, …), which keeps business code free of boilerplate `try/catch`.

## 5. Quality & compatibility

- **Tests**: each module exposes a `SuiteTests` entry point; CI runs the modules that do not need external services.
- **CI**: `.github/workflows/maven.yml` — builds on JDK 8 and JDK 17, plus unit tests for network-free modules.
- **Javadoc**: every method documents its return value and exception conditions. Run `mvn -Pstrict-javadoc javadoc:javadoc` to treat `-Xdoclint` warnings as errors (the profile is opt-in; default builds are unaffected).
- **JDK support**: JDK 8+ (Android is untested; for JDK 7 use feilong-core 2.1.0).
- **Security notes**:
  - `feilong-office-zip` unzip includes **zip-slip protection**: entries escaping the target directory raise `IllegalArgumentException`;
  - MD5/SHA1/DES in `feilong-security` exist **for legacy compatibility only**; prefer AES (or SM3) for new code;
  - `XStreamBuilder` still grants `AnyTypePermission.ANY` to XStream (kept for backward compatibility of published jars), so **only deserialize trusted XML**; this will be tightened in a future major release.

## 6. Who it is for / when to be careful

**Good fit**

- Teams that want rich documentation and less boilerplate;
- Projects that need one consistent style for dates, collections, IO, JSON, XML, HTTP, mail and office files;
- Existing Servlet / Spring applications that want a utility layer quickly;
- Declarative Excel / CSV / ZIP / mail / bot notification scenarios.

**Evaluate first**

- You already use `commons-lang3` / `hutool` / Guava for a single helper — avoid duplicating dependencies;
- You care about dependency size — depend on individual modules instead of the all-in-one `feilong` jar;
- You prefer functional/immutable APIs — feilong is a static-utility, runtime-exception style library;
- You need legacy algorithms (MD5/DES) for security, or must deserialize untrusted XML/Java objects.

## 7. Module overview

**Core utilities**

module | description
:----  | :---------
feilong-core | core utilities (recommended)
feilong-core-extension | extensions on top of feilong-core (awt, io, …)
feilong-tools | practical helpers (mobile number, e-mail address, …)
feilong-validator | configurable validation (mobile, zip code, …)
feilong-formatter | pretty printing for Map / bean / list
feilong-security | symmetric/asymmetric encryption, digests, Base64

**Data formats**

module | description
:----  | :---------
feilong-json | JSON format / toBean / toMap
feilong-xml | XML format / toBean / toMap
feilong-template | templating (Velocity)
feilong-io | file and stream helpers

**Network / communication**

module | description
:----  | :---------
feilong-net-http | HTTP client wrappers (recommended)
feilong-net-api | base HTTP request/response API
feilong-net-jsoup | jsoup helpers
feilong-net-filetransfer | FTP / SFTP (recommended)
feilong-net-mail | send and read mail
feilong-net-cxf | CXF helpers
feilong-net-bot-api | base API for chat bots
feilong-net-bot-dingtalk | DingTalk bot
feilong-net-bot-wxwork | WeCom (WeChat Work) bot

**Web / Spring**

module | description
:----  | :---------
feilong-servlet | Servlet request/response helpers (recommended)
feilong-accessor | session and cookie access
feilong-context | context / invoker abstractions (recommended)
feilong-namespace | Spring XML configuration support
feilong-taglib | JSP custom tags (to be deprecated)

**Office / composition**

module | description
:----  | :---------
feilong-office | aggregate for csv / excel / zip
feilong-office-csv | CSV writing
feilong-office-excel | read and write Excel via XML definition
feilong-office-zip | zip / unzip
feilong-component | configurable pipeline: data → Excel → ZIP → mail (recommended)

**All-in-one / other**

module | description
:----  | :---------
feilong | all-in-one jar with every module above
feilong-with-optional | all-in-one jar including every optional dependency
feilong-lib | forked third-party sources (commons-lang3, commons-io, commons-compress, …) — do not call directly

## 8. Installation

Published to Maven Central since 3.0.0: https://search.maven.org/artifact/com.github.ifeilong/feilong

**Maven — all-in-one**

```XML
<dependency>
	<groupId>com.github.ifeilong</groupId>
	<artifactId>feilong</artifactId>
	<version>4.5.6</version>
</dependency>
```

**Maven — single module (smaller footprint, recommended)**

```XML
<dependency>
	<groupId>com.github.ifeilong</groupId>
	<artifactId>feilong-core</artifactId>
	<version>4.5.6</version>
</dependency>
```

**Gradle**

```
com.github.ifeilong:feilong:4.5.6
```

**Without a build tool**: download the jar from https://repo1.maven.org/maven2/com/github/ifeilong/feilong/

## 9. Build from source

```bash
git clone https://github.com/ifeilong/feilong.git --depth 1
mvn install
```

Note: some modules contain tests that talk to external services (mail, FTP/SFTP, public HTTP). Use `-DskipTests`, or run the network-free modules only.

## 10. About the documentation language

Javadoc in this project is written in **Chinese**, with examples and explicit exception documentation on every method — that is the intended primary audience. This README is the English entry point; if you would like to contribute English translations or an English quick-start, pull requests are welcome.

## License / feedback / community

- Licensed under [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0). You may use it in closed-source projects; modified files must keep the [license notice](https://github.com/ifeilong/feilong/blob/master/LICENSE).
- History: since 2008 internally; [feilong-core](https://www.oschina.net/p/feilong-core) open-sourced in 2016, the current [feilong](https://www.oschina.net/p/feilong) platform in 2020.
- Bug reports and feature requests: [GitHub issues](https://github.com/ifeilong/feilong/issues)
- Wiki: [Reasons for using feilong-core](https://github.com/ifeilong/feilong/wiki/Reasons-for-use-feilong-core) · [feilong vs hutool](https://github.com/ifeilong/feilong/wiki/feilong%20VS%20hutool%E5%AF%B9%E6%AF%94)
- QQ group `243306798`; WeChat official account `feilong飞龙`

[![Star History Chart](https://api.star-history.com/svg?repos=ifeilong/feilong&type=Timeline)](https://star-history.com/#ifeilong/feilong&Timeline)
