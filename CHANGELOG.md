# Changelog

所有值得注意的变更都会记录在这里。
格式参考 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.1.0/)，版本号遵循 [Semantic Versioning](https://semver.org/lang/zh-CN/)。

## [Unreleased]

### Fixed

- 修正 `feilong-office-zip` 解压时的路径穿越风险（zip slip）：`AbstractUnzipHandler#write` 现在会校验解压条目不会逃出 `outputDirectory`，否则抛出 `IllegalArgumentException`

### Security（有意延后）

- ⏳ **`feilong-xml` 的 `XStreamBuilder#buildDefault` 仍保留 `xstream.addPermission(AnyTypePermission.ANY)`**：该调用会关闭 XStream 的安全框架，解析不可信 XML 时存在反序列化风险。为**保持已发布 jar 的行为兼容**，本版本刻意不改；计划在**下一个 major 版本**移除并改用 `xstream.allowTypes(...)` 显式白名单（属 breaking change）。跟踪 issue：`ifeilong/feilong`（待登记，标题建议 `[Security] 移除 XStreamBuilder 中的 AnyTypePermission.ANY`）

### Changed

- 根 `pom.xml` 新增 `strict-javadoc` profile（`mvn -Pstrict-javadoc javadoc:javadoc`），用于把 javadoc 的 `-Xdoclint` 告警当错误处理；默认构建行为不变
- 新增 GitHub Actions 工作流 `.github/workflows/maven.yml`：JDK 8/17 编译 + 无外网依赖模块的单元测试

### Docs

- 全项目 javadoc 审计与修正：机械类问题（`{@link "..."}`、`{@link StringUtils#EMPTY}`、`{@link <a href=...>}`）、示例中不存在的类/方法/常量、`@param`/`@return` 与签名不符、异常类型与实现不符、多余/缺失的 HTML 标签、指向 private 成员的断链、陈旧包名（`com.feilong.json.jsonlib.*`、`com.feilong.tools.*` 等）

## [4.5.6]

- 当前发布版本
