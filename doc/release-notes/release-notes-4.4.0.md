# feilong 4.4.0 Release Notes（Milestone #49）

> **里程碑**：4.4.0 · Milestone #49（12 items, 100% completed）  
> **仓库**：https://github.com/ifeilong/feilong  
> **Milestone 视图**：https://github.com/ifeilong/feilong/milestone/49?closed=1

> 说明：4.4.0 当时没有单独整理 release notes，本文件依据 Milestone #49 的 12 个已关闭 issue 补写。

## 升级提示（先看这个）
- `DateUtil.formatDurationUseBeginTimeMillis(long)` 名字太长，已由 `DateUtil.formatElapsedTime(long)` 取代并标记 **`@Deprecated`**（#779 #783）。方法体保留，换名字即可。
- 工具类的私有构造器统一改用 Lombok 的 **`@NoArgsConstructor(access = AccessLevel.PRIVATE)`**（#788），行为不变，只是写法统一。
- 日志统一换成 Lombok **`@Slf4j`**（#782），不再手写 `LoggerFactory.getLogger(...)`。
- 本版本引入**日志上下文体系**（#789）：如果需要往日志里自动带出"操作人 / ip / ua / 登录用户"这类业务上下文，实现 `CustomFixedContextLogCreator` 并通过 SPI 注册即可，不用改业务日志代码。

---

## ✨ 新特性 / 增强（New Features & Enhancements）

### 1️⃣ 日志上下文体系：`CustomFixedContextLogCreator` + SPI (#789)

**背景**：排查线上问题时，光有日志文本不够，还要知道"是哪个用户、哪个 ip、哪个 ua 触发的"。以前要么每个 `log.info` 手动拼参数，要么干脆不记。新增的日志上下文体系把这件事做成**一次配置、全局生效**：上下文由 SPI 提供，日志框架自动带上。

**用法**：

**第 1 步**：实现 `com.feilong.context.log.CustomFixedContextLogCreator`（必须有无参构造方法）。

```java
public class BackendCustomFixedContextLogCreator implements CustomFixedContextLogCreator{

    /**
     * 自动拼装操作人、操作 ip、操作路径、ua 等信息
     */
    @Override
    public String createContextLog(){
        HttpServletRequest request = WebSpringUtil.getRequest();
        return formatPattern(
                        "method:[{}] ip:[{}] ua:[{}],登录用户信息:[realName:[{}],userId:[{}]]", //
                        null == request ? null : request.getMethod(),
                        null == request ? null : RequestUtil.getClientIp(request),
                        null == request ? null : RequestUtil.getHeaderUserAgent(request),
                        UserUtils.getRealName(),
                        UserUtils.getUserId());
    }
}
```

**第 2 步**：在 `src/main/resources/META-INF/services/` 下创建 SPI 配置文件，**文件名是接口全限定名**：

```
src/main/resources/META-INF/services/com.feilong.context.log.CustomFixedContextLogCreator
```

**文件内容**：每行一个实现类的全限定名（多个实现就多行）。

```
com.xxx.log.BackendCustomFixedContextLogCreator
```

**第 3 步**：照常写业务日志，上下文会自动带上。

**⚠️ 注意**：SPI 实现类**必须有无参构造方法**；`createContextLog()` 里不要抛异常（它会在每条日志生成时被调用，抛异常等于把日志打挂）。

---

### 2️⃣ 请求参数带默认值：`RequestUtil.getAttribute` / `getParameterDefaultValue` (#780 #784 #785)

**背景**：`request.getParameter("page")` 拿到的是 `String`，为空取默认值、类型转换、异常兜底这三件事每次都要写一遍。现在有带类型和默认值的重载。

**示例**：

```java
// attribute：取不到（或类型不符）时返回默认值
String traceId = RequestUtil.getAttribute(request, "traceId", "unknown");
Integer tenantId = RequestUtil.getAttribute(request, "tenantId", Integer.class, 0);

// parameter：字符串自动转成目标类型，转不动就返回默认值
Integer timeout = RequestUtil.getParameterDefaultValue(request, "timeout", Integer.class, 30);
```

---

### 3️⃣ 分页参数开箱即用：`RequestUtil.getParameterPageNo` / `getParameterPageSize` (#786 #787)

**背景**：分页接口里 `page`、`count` 的取值 + 默认值 + 类型转换几乎是每个列表接口的复制粘贴。

**示例**：

```java
Integer pageNo   = RequestUtil.getParameterPageNo(request, "page");        // 默认 1
Integer pageSize = RequestUtil.getParameterPageSize(request, "count", 20); // 默认 20
```

---

### 4️⃣ SPI 加载工具：`com.feilong.core.util.ServiceLoaderUtil` (#790)

**背景**：JDK 的 `ServiceLoader` 用法略啰嗦（要处理 `Iterator#next`、空列表、多个实现取哪个），这里包一层最常用的「取第一个实现」。

**示例**：

```java
CustomFixedContextLogCreator creator = ServiceLoaderUtil.loadFirst(CustomFixedContextLogCreator.class);
```

---

## 🐛 修复 / 日志增强

- `SpecialHttpRequestRetryHandler` 的日志补上 `logTraceContext` 上下文（#781）——重试链路的日志之前不带 trace key，跟主流程的日志对不上。

---

## ♻️ 代码优化 / 底层对齐（Improvements & Cleanup）

- `DateUtil.formatDurationUseBeginTimeMillis(long)` 重命名/简化为 `DateUtil.formatElapsedTime(long)`，旧方法标记 `@Deprecated`（#779 #783）。
- 工具类的私有构造器统一改用 Lombok `@NoArgsConstructor(access = AccessLevel.PRIVATE)`（#788），与后续版本的写法保持一致。
- 日志声明统一换成 Lombok `@Slf4j`（#782），减少 `LoggerFactory.getLogger(Xxx.class)` 这类样板代码。

---
