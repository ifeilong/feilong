# Changelog

所有值得注意的变更都会记录在这里。
格式参考 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.1.0/)，版本号遵循 [Semantic Versioning](https://semver.org/lang/zh-CN/)。

## [Unreleased]

### Added

- `ObjectUtil` 新增 `defaultZero(BigDecimal)` 与 `defaultZero(Long)`：null 或负数返回 `0`，正数原样返回（不改 scale）。大小判断用 `BigDecimal#compareTo`，避开 `new BigDecimal("0.00").equals(BigDecimal.ZERO)` 为 `false` 的坑（#896）

### Fixed

- 修正 `feilong-office-zip` 解压时的路径穿越风险（zip slip）：`AbstractUnzipHandler#write` 现在会校验解压条目不会逃出 `outputDirectory`，否则抛出 `IllegalArgumentException`
- `feilong-net-jsoup`：`JsoupUtil` 在 classpath 缺少 jsoup 时抛出 `JsoupUtilException` 并直接提示所需依赖（`org.jsoup:jsoup`），不再抛难以定位的 `NoClassDefFoundError: org/jsoup/Jsoup`（#525）
- `feilong-net-mail`：`DefaultMailSender#send` 在 `mailSendConnectionConfig` 为 null 时先抛带说明的 `NullPointerException`，不再在解引用时抛无上下文的 NPE

### Security（有意延后）

- ⏳ **`feilong-xml` 的 `XStreamBuilder#buildDefault` 仍保留 `xstream.addPermission(AnyTypePermission.ANY)`**：该调用会关闭 XStream 的安全框架，解析不可信 XML 时存在反序列化风险。为**保持已发布 jar 的行为兼容**，本版本刻意不改；计划在**下一个 major 版本**移除并改用 `xstream.allowTypes(...)` 显式白名单（属 breaking change）。跟踪 issue：`ifeilong/feilong`（待登记，标题建议 `[Security] 移除 XStreamBuilder 中的 AnyTypePermission.ANY`）

### Changed

- 根 `pom.xml` 新增 `strict-javadoc` profile（`mvn -Pstrict-javadoc javadoc:javadoc`），用于把 javadoc 的 `-Xdoclint` 告警当错误处理；默认构建行为不变
- 新增 GitHub Actions 工作流 `.github/workflows/maven.yml`：JDK 8/17 编译 + 无外网依赖模块的单元测试
- `feilong-net-mail` 不再全局跳过 surefire（`maven-surefire-plugin.skip=false`）：`SuiteTests` 只保留不依赖网络的用例，新增 `SessionPropertiesBuilderTest`、`MessageBuilderTest`、`AuthenticatorBuilderTest`、`HeaderSetterTest`、`BodySetterTest`、`SessionFactoryTest`、`DefaultMailSenderTest`，并给 `InternetAddressUtilTest` 补了断言；需要真实邮箱的用例（`HelloWorldTest`、`MailSenderTest`、`AttachSenderTest`、`EmojiSubjectTest`、`SSLTest`、`ics` 包等）移出 suite，改为手工在 IDE 里运行

### Docs

- 全项目 javadoc 审计与修正：机械类问题（`{@link "..."}`、`{@link StringUtils#EMPTY}`、`{@link <a href=...>}`）、示例中不存在的类/方法/常量、`@param`/`@return` 与签名不符、异常类型与实现不符、多余/缺失的 HTML 标签、指向 private 成员的断链、陈旧包名（`com.feilong.json.jsonlib.*`、`com.feilong.tools.*` 等）
- `JsoupUtil` 的 javadoc 补充「依赖」说明（jsoup 是 `optional` 依赖不会传递、需自行声明 `org.jsoup:jsoup`、聚合包 POM 中只有 `slf4j-api`）、maven 依赖片段、四类用法示例，并修正 `parse` 中损坏的 HTML 示例
- `README.md` / `README.en.md` 修正「第三方依赖不会缺」的错误描述：聚合包 `feilong` 发布出来的 POM 只有 `slf4j-api`，velocity、poi、javax.mail、jsch、jsoup、xstream、cxf 等需按需自行声明
- `HttpClientUtil` javadoc 与 `README.md` / `README.en.md` 新增「httpclient debug 日志」说明：httpclient4 被 shade 后 logger 名是 `com.feilong.lib.org.apache.http.*`，配置 `org.apache.http` 无效，并给出 wire/执行链/连接池等 logger 与 logback、log4j2 示例（#88）
- `feilong-net-mail` javadoc 修正：`HeaderSetter` 里 `</ol>` 前多出来的 `*`、`SessionFactory#createSession` 过期的 `@param` 名字（`mailSenderInfo` → `sessionConfig`）、`SessionPropertiesBuilder#build` 未文档化的 `IllegalArgumentException`、`MessageSendUtil#send` 未文档化的 `MailException`、`AuthenticatorBuilder#build` 需要说明的 null 返回、`MailSendConnectionConfig` 里把 STARTTLS 写成「is tls」的措辞，以及 `DefaultMailSender` 空构造函数、`MessageUtil` / `InternetAddressUtil` / `SessionPropertiesBuilder` 的类级说明

## [4.5.6]

- 当前发布版本
