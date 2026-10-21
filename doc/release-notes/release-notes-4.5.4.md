# feilong 4.5.4 Release Notes（Milestone #53）

> **里程碑**：4.5.4 · Milestone #53（18 items, 100% completed）  
> **仓库**：https://github.com/ifeilong/feilong  
> **Milestone 视图**：https://github.com/ifeilong/feilong/milestone/53?closed=1

## 升级提示（先看这个）
- `MapUtil.get(Map<K,V>, int)` 由于 `int` 重载容易产生歧义（是按 key 还是按“第几个 entry”的语义），已被标记为 **`@Deprecated`**。  
  后续请改用新增的明确索引访问方法：`MapUtil.getByIndex(Map<K,V>, int)`。
- 若干历史 API 仍在渐进迁移到更现代的写法；如果你项目里有用到 `org.apache.commons.collections4.Predicate/Closure/Transformer` 相关桥接或内部回调，建议做一次 clean rebuild（一般无需改业务代码）。

---

## ✨ 新特性 / 增强（New Features & Enhancements）

### 1️⃣ 定时任务：`ScheduledExecutorServiceUtil.startSingleThreadScheduledAtFixedRate` (#836)

**背景**：在执行耗时较长的任务（如批处理、数据同步、持续1小时以上的计算）时，开发者常常需要在等待期间了解任务是否仍在正常运行，避免“假死”情况。通过定时心跳或进度播报，可以及时感知任务状态。本方法提供了一个轻量级的单线程定时调度工具，方便快速实现此类需求。

**返回值说明**：方法返回 `ScheduledExecutorService` 实例，**使用完毕后需自行调用 `shutdown()` 或 `shutdownNow()` 释放资源**，避免线程泄漏。

**示例**：
```java
// 启动一个定时任务：初始延迟1秒，之后每5秒执行一次，打印心跳
ScheduledExecutorService scheduler = ScheduledExecutorServiceUtil
    .startSingleThreadScheduledAtFixedRate(1, 5, TimeUnit.SECONDS, () -> {
        System.out.println("Heartbeat: " + System.currentTimeMillis());
    });

// ... 执行业务逻辑 ...

// 任务结束后，关闭调度器释放线程
scheduler.shutdown();
```

---

### 2️⃣ 分组增强：`GroupUtil.groupByToSet(…)` (#835, #845)

**背景**：以前只能用 `groupBy` 收集到 `List`，现在可以直接收集到 `Set`（自动去重），并且支持过滤。

**示例 1**：按城市分组，只保留活跃用户，每组结果放入 `Set`。
```java
List<User> users = Arrays.asList(
    new User(1, "Alice", "Beijing", true),
    new User(2, "Bob", "Shanghai", false),
    new User(3, "Charlie", "Beijing", true)
);

Map<String, Set<User>> cityActiveUsers =
    GroupUtil.groupByToSet(users, User::getCity, User::isActive);
// 结果：{Beijing=[User{id=1}, User{id=3}], Shanghai=[]}
```

**示例 2**：更强的重载 – 按城市分组，组内按 `userId` 去重，且只包含活跃用户。
```java
Map<String, Set<User>> result = GroupUtil.groupByToSet(
    users,
    User::getCity,              // 分组依据
    User::getId,                // 组内去重依据（相同id只保留一个）
    User::isActive              // 过滤条件
);
```

---

### 3️⃣ 明确的 Map 按序号访问：`MapUtil.getByIndex(…)` (#834, #837)

**背景**：`MapUtil.get(map, 0)` 曾经既可以解释为“获取 key=0 的值”，也可以解释为“获取第 0 个 entry 的值”。现在拆分为两个方法，消除歧义。旧方法 `MapUtil.get(Map<K,V>, int)` 已标记为 `@Deprecated`。

**示例**：
```java
Map<String, Integer> scores = new LinkedHashMap<>(); // 保证顺序
scores.put("Math", 95);
scores.put("English", 88);
scores.put("Physics", 92);

// ❌ 旧方法（已废弃）：
// Integer mathScore = MapUtil.get(scores, 0); // 歧义

// ✅ 新方法：明确表示获取第 0 个 entry 的 value
Integer firstScore = MapUtil.getByIndex(scores, 0); // 返回 95
```

---

### 4️⃣ 拒绝/排除选择器：`CollectionsUtil.selectRejected` 支持 Function 写法 (#838, #839)

**背景**：之前只能通过属性名字符串排除，现在可以用 Lambda 表达式，类型安全且支持 IDE 自动补全。

**示例**：排除状态为 `DISABLED` 或 `DELETED` 的用户。
```java
List<User> allUsers = ...;

// 使用 Function 提取 status 属性
List<User> activeUsers = CollectionsUtil.selectRejected(
    allUsers,
    User::getStatus,
    Status.DISABLED, Status.DELETED
);
```

另一个重载接受 `Collection<V>`，适合动态构造排除值列表：
```java
List<Status> excludedStatuses = Arrays.asList(Status.DISABLED, Status.DELETED);
List<User> activeUsers2 = CollectionsUtil.selectRejected(
    allUsers,
    User::getStatus,
    excludedStatuses
);
```

---

### 5️⃣ 轻量级重试：`SimpleRetryUtil` (#846)

**背景**：很多场景需要“失败后重试几次”，之前没有统一的工具类，现在新增 `com.feilong.context.SimpleRetryUtil`。

**⚠️ 轻量级定位**：本工具类仅提供最基本的重试能力（固定间隔、固定次数），适合简单场景。**如果你的场景需要更复杂的策略**（如指数退避、根据异常类型选择性重试、持久化重试状态等），建议使用 **Spring Retry** 或 **Failsafe** 等专业重试库。

**示例**：最多重试 3 次，每次间隔 500ms，执行一个可能失败的任务。
```java
boolean success = SimpleRetryUtil.retry(3, 500, () -> {
    boolean ok = callRemoteApi();
    if (!ok) throw new RuntimeException("API call failed");
    return true;
});

if (success) {
    System.out.println("远程调用成功");
}
```

---

### 6️⃣ 钉钉消息 at 指定人：`UserNameAndMobileEntity` (#833)

**背景**：钉钉机器人发送消息时需要指定被 @ 的人，通常需要姓名和手机号。现在有了专门的实体类。

**示例**：
```java
UserNameAndMobileEntity user = new UserNameAndMobileEntity("张三", "13800138000");
// 后续可传给钉钉消息构建器
```

---

### 7️⃣ Markdown 辅助工具增强：`appendMarkdownLine` (#850, #851)

**背景**：在拼接机器人消息或日志时，频繁需要追加一行 Markdown 文本，现在有了专用方法。

**示例**：使用 `appendMarkdownLine(StringBuilder, CharSequence)` 追加普通行。
```java
StringBuilder sb = new StringBuilder();
MarkdownHelper.appendMarkdownLine(sb, "# 用户报告");
MarkdownHelper.appendMarkdownLine(sb, "| 姓名 | 分数 |");
MarkdownHelper.appendMarkdownLine(sb, "|---|---|");
MarkdownHelper.appendMarkdownLine(sb, "| Alice | 95 |");
System.out.println(sb.toString());
```

**重载 `appendMarkdownLine(StringBuilder, CharSequence, int)`**：第三个参数表示**追加的换行符数量**，而非缩进。
```java
// 追加一行后额外增加 2 个换行符（空两行）
MarkdownHelper.appendMarkdownLine(sb, "---", 2);
```

---

## ♻️ 代码优化 / 底层对齐（Improvements & Cleanup）

- **commons-collections4 相关桥接代码跟进 4.5.0 风格**（#842/#843/#844）：  
  对仍在使用/桥接到 `Predicate<T>`、`Closure<T>`、`Transformer<T,R>` 的路径做了同步调整与清理。
- 部分方法入参中对 `Predicate` 的使用改为更明确的全限定名写法（#848），避免命名冲突。
- `ObjectUtil.defaultEmptyListIfNull(List<T>)` 改为更直白的三元实现（#840）——行为不变，可读性/静态分析更友好。
- Javadoc：完善 `GroupUtil` 类级文档（#849）。

---

## 🧪 测试
- 单测中逐步把 `org.junit.Assert.assertThat` 迁移到 `org.hamcrest.MatcherAssert.assertThat`（#847），减少对 JUnit 内建 assertThat 的依赖。

---
 