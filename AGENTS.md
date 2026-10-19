# AGENTS.md

写给在本仓库里干活的 AI agent（以及三个月后的自己）。

目标只有一句话：**改出来的代码要像 feilong 自己长出来的**——同样的命名、同样的默认值、同样的兼容性尺度。

---

## 1. 项目是什么

Java 8 工具库，2008 年起由单人维护。定位是「作者自己顺手用」，不追求广度覆盖。

- 中央仓库**只发布聚合包** `com.github.ifeilong:feilong`（shade 成一体化 jar），子模块（`feilong-core`、`feilong-json`…）**没有独立坐标**。
- 不要引用 `com.github.ifeilong:feilong-core:4.5.6` 这种坐标，它不存在。
- `feilong-with-optional` 只在仓库里存在，**未发布**。

### 模块地图

| 目录 | 说明 |
|---|---|
| `feilong-core` | 核心工具：`StringUtil` / `CollectionsUtil` / `DateUtil` / `BeanUtil` / `ConvertUtil` / `Validate`… |
| `feilong-json` | `JsonUtil`（`toBean`/`toMap`/`toString`/`format`） |
| `feilong-net` | `http`（shaded httpclient4）、`jsoup`、`filetransfer`、`mail`、`bot`、`cxf` |
| `feilong-io` / `feilong-xml` / `feilong-security` / `feilong-context` | 各领域工具 |
| `feilong-lib` | **fork 进来并改包名的第三方库**（`com.feilong.lib.*`），不要直接调用内部实现 |
| `feilong` / `feilong-with-optional` | 聚合包 / 可选依赖清单 |

---

## 2. 构建与验证

```bash
# 单模块（含依赖模块）
mvn -pl feilong-core -am test

# 严格 javadoc 校验（doclint:all，告警即错误）
mvn -Pstrict-javadoc -pl feilong-core javadoc:javadoc
```

必须知道的几点：

1. **Surefire 只跑 `**/SuiteTests`**。新增的测试类如果不挂进所在包的 `SuiteTests`（`@SuiteClasses`），等于没写。
2. 没有 `-Werror`、没有 `-Xlint`。deprecation 警告不致命，但不要靠它兜底。
3. 默认 profile 的 javadoc 是 `-Xdoclint:none`；严格校验只在 `strict-javadoc` profile 里。
4. 跑不了测试时（比如受限环境），**必须在回复里明确写"未编译/未运行"**，并给出本地命令。

---

## 3. 语言与依赖

- **Java 8**。没有 `var`、`List.of`、`text block`、`record`。
- Lombok 是根 pom 里的 `provided` 依赖，所有模块都能用（`@Slf4j`、`@NoArgsConstructor` 很常见）。
- 工具类一律 `final` + `@NoArgsConstructor(access = AccessLevel.PRIVATE)`。
- **不要往 `feilong-core` 加新的第三方运行时依赖**。第三方库（jsoup、poi、javax.mail、jsch、velocity、xstream、cxf）在子模块里都是 `optional`，不传递。
- 要用 lang3 / commons-io / commons-collections4 / beanutils 的功能，用 `com.feilong.lib.*` 下 fork 的版本，不要引入原版坐标。

---

## 4. 公开 API 约定

### 4.1 反射 vs Function

**核心方向：弱化、最终取消 `String propertyName` 这种反射式 API。**

- 已有 `Function<O, V>` 孪生方法的 `String propertyName` 版本，标 `@Deprecated`，**不删**。
- **不要再新增** `String propertyName` 方法。
- 弃用注释模板（照抄）：

```java
 * @deprecated 从 4.5.6 版本开始, 属性名(反射)方式已不推荐, 请使用 {@link #xxx(Iterable, Function)} 代替
 */
@Deprecated
```

- 旧方法的 javadoc 里，凡是"还需要用的话可以调 X"的链接，**一律指向新方法**，不要把用户带到另一条废弃链上。

### 4.2 兼容性红线（补丁版本内）

- 改 `void` → 有返回值：**二进制不兼容**（方法描述符变了），别做。
- 改参数类型 / 改返回类型 / 删 public 方法：别做，加新方法 + 弃用旧方法。
- 加 `String` 重载、加 `Function` 重载是安全的，但**注意歧义**：`defaultIfNull(x, null)` 在有了 `(T, Supplier<T>)` 之后会 ambiguous。
- `ConnectionConfig` 是 `HttpClient` 缓存 key（`Map<ConnectionConfig, HttpClient>`），**不要随便加字段**，会分裂连接池。

### 4.3 命名与形态

- 工具方法：动词开头，`get` / `to` / `format` / `is`；`Iterable` 在前、`Function` 在后。
- 成对结果用 `Pair`（如 `getMonthStartAndEndPair`、`getWeekStartAndEndPairMondayFirst`）。
- `default*` 家族：`defaultIfNull`、`defaultNewIfNull`、`defaultCountIfNullZeroOrGreaterThan`——新增同类方法前先搜一遍有没有可复用的。
- 3 个以上参数、或大部分参数可选的构造，用 builder（参考 `HttpRequest.builder()`）。
- **不要炫技**。作者明确否决过 `Math.floorMod`、`+7 % 7` 这类写法；分支能写清楚就写清楚。

### 4.4 空值与异常

- 前置校验用 `com.feilong.core.Validate`：`notNull` → NPE；`notBlank(null)` → NPE、blank → IAE。
- 集合工具的惯例：入参 null/empty 返回 `Collections.emptyList()` / `emptyMap()`，不抛异常。
- 异常说明写在 `@return` 里（本仓库风格），必要时再补 `@throws`。

---

## 5. Javadoc 约定

中文，每个 public 方法都要有 `@param` / `@return` / `@since`（新增方法）。

```java
    /**
     * 一句话说明.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * Map{@code <String, List<User>>} map = GroupUtil.groupBy(list, User::getName);
     * 
     * </pre>
     * 
     * </blockquote>
     * 
     * @param beanIterable
     *            bean Iterable,诸如List{@code <User>},Set{@code <User>}等
     * @return 如果 <code>beanIterable</code> 是null或者empty,返回 {@link Collections#emptyMap()}<br>
     * @since 4.5.6
     */
```

易错点：

- 方法级 javadoc 缩进是 `     * `（5 空格 + `* `）；**空行也要带尾随空格** `     * `。
- 表格用 `<table border="1" cellspacing="0" cellpadding="4" summary="">`。
- `<pre class="code">` 里出现 `<`、`>`、`"` 要转义成 `&lt;` `&gt;` `\"`；**不要把一个 `{@code` 塞进 `<pre>` 里跨行**。
- 示例必须是能编译的代码。历史上出过 `GroupUtil.groupBy(list, "name")`（String 传给 Function）这种抄了就跑不过的示例。
- 面向用户解释"为什么"（比如 logger 名带 `com.feilong.lib.` 前缀、jsoup 是 optional），比多写一个方法有用。

---

## 6. 测试约定

- JUnit 4 + Hamcrest；类名 `XxxTest`，**必须注册进所在包的 `SuiteTests`**。
- 测试类命名跟被测方法走：`RemoveDuplicatePropertyNamesTest`、`SplitByExistIdTest`。
- 断言优先 `assertEquals` / `assertSame` / `assertNotSame`；行为约定（如 builder 返回自身）要显式断言。
- 不写联网测试（除了 `temp` 包里手工跑的），CI 环境没有外网。
- 新增 API 至少覆盖：正常路径、null/empty、边界（0、负数、空集合）。

---

## 7. 提交与发布

commit message（一个 issue 一行，靠 `fix #NNN` 自动关）：

```
GroupUtil 属性名(反射)方式的方法标记过期, 请使用 Function 版本代替
fix #893
fix #892
fix #891
```

- 每轮改动都要更新 `CHANGELOG.md` 的 `[Unreleased]`（`Fixed` / `Changed` / `Docs` / `Security`）。
- 版本号：根 `pom.xml` 的 `<version>`、`<version.feilong-platform>`、各子模块 `<parent><version>` 要一起改。
- 发布走 `-P release`（javadoc + gpg + central publishing），只有聚合包会发布。

---

## 8. 不要做的事

- 不要新增 `String propertyName` 反射式 API。
- 不要为了"整洁"重命名/重排已发布的 public API。
- 不要在补丁版本里做二进制不兼容的改动。
- 不要往 core 加第三方运行时依赖。
- 不要用 JDK8 之后的语法。
- 不要写测试类却不注册进 `SuiteTests`。
- 不要在 javadoc 里写跨行的 `{@code` 或未转义的 HTML。
- 不要把 `feilong-lib` 里的 `com.feilong.lib.*` 内部实现当公开 API 用。

---

## 9. 已知坑

| 坑 | 说明 |
|---|---|
| httpclient4 是 shade 的 | 包名和 logger 名都是 `com.feilong.lib.org.apache.http.*`；配 `org.apache.http` 的日志级别无效 |
| Lombok `@Builder.Default` | 字段初值会被搬进 `$default$xxx()`，手写构造器就丢失默认值（`HttpRequest.isTrimUri` 就踩过） |
| `ConvertUtil.toList(Collection)` | 入参已经是 `List` 时**返回同一个实例**，别拿它当"复制"用 |
| `ConvertUtil.toArray("")` | 返回长度为 1 的数组；判空要用 `isNullOrEmpty(String)` 而不是 `isNullOrEmpty(String[])` |
| `ConnectionConfig` | 是 `HttpClient` 缓存 key，加字段会分裂连接池 |
| jsoup 等 optional 依赖 | 聚合包的 POM 里只有 `slf4j-api`，用户必须自己声明 |

---

## 10. 新增一个方法前，先问自己

1. 有没有同名/近似的孪生方法？能不能直接加一个重载？
2. 默认值是什么？null 输入返回什么？会不会破坏现有调用方？
3. 有没有对应的 `Function` 版本需要一起维护？
4. 单测挂进 `SuiteTests` 了吗？`CHANGELOG` 写了吗？
5. 跑过 `mvn -pl <module> -am test` 了吗？
