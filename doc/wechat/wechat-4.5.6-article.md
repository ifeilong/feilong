# feilong 4.5.6 公众号文章（按 4.5.4 版式）

> **标题（主推）**
> 让Java开发更简便：feilong 4.5.6 版本发布，13 个反射式 API 退役、补齐金额兜底等 38 项更新
>
> **标题备选**
> 1. 让Java开发更简便：feilong 4.5.6 发布，写出更安全的集合代码
> 2. feilong 4.5.6：13 个 `"propertyName"` 方法集体退役，一个都没删
>
> **摘要（120 字内）**
> 4.5.6 完成 38 个 Issue。主线：`CollectionsUtil` / `GroupUtil` 里 13 个属性名(反射)方法标记 `@Deprecated`（不删）。同时补齐 `defaultZero(BigDecimal)`、懒创建 `defaultIfNull(x, Supplier)`、周日期边界、`splitByExistId`、`HttpRequest.builder()`。
>
> **封面**：纯色底 + 大字号 `4.5.6` + 一行小字「13 个反射式 API 退役」

---

各位开发者朋友，大家好！

feilong 4.5.6 现已正式发布。本次里程碑共完成 **38 个 Issue**，主线只有一条：**把 `String propertyName` 这种反射写法，从 `CollectionsUtil` 和 `GroupUtil` 里请出去**；同时补齐金额兜底、懒创建、周日期边界、批量拆分等几个日常高频的小工具。

🎯 始终坚持：让 Java 开发更简便，让代码更简练、易读、易维护。

---

## 🔧 如何获取

Maven 依赖：

```xml
<dependency>
    <groupId>com.github.ifeilong</groupId>
    <artifactId>feilong</artifactId>
    <version>4.5.6</version>
</dependency>
```

Gradle 依赖：

```
com.github.ifeilong:feilong:4.5.6
```

官方文档：https://feilong.gitbook.io/feilong-docs

---

## ⚠️ 升级前必看

- **13 个属性名(反射)方法标记 `@Deprecated`**（#874–#883、#891–#893）：**方法全部保留、不删除**，现有代码照常编译。建议逐步换成对应的 `Function` 版本，完整对照见下方「反射式 API 迁移对照」。
- **`ObjectUtil.defaultIfNull(user, null)` 现在会编译失败**：新增的 `(T, Supplier<T>)` 重载和原来的 `(T, Object)` 撞了，产生歧义。确实需要显式传 null 的，加一个转型即可：`defaultIfNull(user, (Supplier<User>) null)`。
- **`feilong-office-zip` 修复了 zip slip 路径穿越**（#869）：如果你继承过 `AbstractUnzipHandler` 并覆写过 `write`，请确认新加的路径校验没有被你的实现绕过。
- **新增 `javax.annotation-api` 可选依赖**（#872）：JDK 11+ 下编译更顺；因为是 `optional`，不会传递到你的工程，用到 `@PostConstruct` 等注解时请自行声明。

---

## ✨ 新特性详解与实战示例

### 1️⃣ 金额兜底：`ObjectUtil.defaultZero(BigDecimal)` / `defaultZero(Long)`（#896）

**背景**：金额、计数这类字段经常需要「null 当 0」的兜底，业务代码里到处是 `value != null ? value : BigDecimal.ZERO`。之前 `ObjectUtil` 只有 `defaultZero(Integer)`，而金额与 ID 场景用得最多的 `BigDecimal`、`Long` 反而没有。

**语义**：`null` 或**负数**返回 `0`，正数**原样返回**。大小判断用 `compareTo`（`new BigDecimal("0.00").equals(BigDecimal.ZERO)` 是 `false`，`compareTo` 才是 0）；**不做 `setScale`**，入参的 scale 原样保留。

```java
// 金额：null 和负数都当 0，正数保留原 scale
BigDecimal amount = ObjectUtil.defaultZero(new BigDecimal("-1.00"));  // 0
BigDecimal price  = ObjectUtil.defaultZero(new BigDecimal("12.30"));  // 12.30

// ID / 计数
Long id  = ObjectUtil.defaultZero((Long) null);  // 0L
Long seq = ObjectUtil.defaultZero(-5L);          // 0L
```

**说明**：如果只想兜 `null`、负数要保留，不要用本方法，用 `ObjectUtil.defaultIfNull(value, BigDecimal.ZERO)`。

---

### 2️⃣ 懒创建：`ObjectUtil.defaultIfNull(T, Supplier<T>)` / `defaultNewIfNull(T, Class<T>)`（#890, #628）

**背景**：`defaultIfNull(x, new Xxx())` 的问题是——当 `x` 不为 `null` 时，那个 `new Xxx()` 也白创建了（有开销或副作用时很难受）。现在可以传 `Supplier`，只有真的为 `null` 时才创建；`defaultNewIfNull` 则是「传类、为 null 才反射 new」。

```java
// Supplier 版本：只有 cachedUser 为 null 时才会去查库
User user = ObjectUtil.defaultIfNull(cachedUser, () -> userDao.load(userId));

// 反射版本：只有为 null 时才 new（目标类需要有无参构造方法）
PodcastFileContext context =
                ObjectUtil.defaultNewIfNull(assertContext.getPodcastFileContext(), PodcastFileContext.class);
```

---

### 3️⃣ 一周的边界：`DateUtil` 周相关方法明确命名（#68, #884–#888）

**背景**：「本周第一天」到底是周一还是周日，历来是日期工具类的重灾区；原来只有 `getFirstDateOfThisWeek(Date)` / `getLastDateOfThisWeek(Date)`，靠猜约定。本版本把约定写进方法名，并补了成对返回的 `Pair` 版本，省掉两次计算。

```java
Date date = now();

// 约定写进方法名，不用再猜
Date monday = DateUtil.getFirstDateOfThisWeekUseMonday(date);
Date sunday = DateUtil.getLastDateOfThisWeekUseSunday(date);

// 成对返回（Pair 来自 com.feilong.lib.lang3.tuple.Pair）
Pair<Date, Date> thisWeek = DateUtil.getWeekStartAndEndPairMondayFirst();
System.out.println(thisWeek.getLeft() + " ~ " + thisWeek.getRight());
```

| 方法 | 说明 |
|---|---|
| `getFirstDateOfThisWeekUseMonday(Date)` | 所在周的周一（#884） |
| `getLastDateOfThisWeekUseSunday(Date)` | 所在周的周日（#68） |
| `getWeekStartAndEndPairMondayFirst()` / `(Date)` | 周一起始的「开始 / 结束」对（#885 #886） |
| `getWeekStartAndEndPairSundayFirst()` / `(Date)` | 周日起始的「开始 / 结束」对（#887 #888） |

---

### 4️⃣ 按 ID 拆分批次：`CollectionsUtil.splitByExistId(...)`（#707）

**背景**：批量保存时典型写法是「已存在走 update、不存在走 insert」，之前要自己 `Map` 一次再分两遍。`splitByExistId` 一次拆好，返回 `Pair`。

```java
// 先把库里已存在的 id 查出来，再一次拆成两批
Pair<List<User>, List<User>> pair =
                CollectionsUtil.splitByExistId(users, User::getId, existIdCollection);

List<User> existList    = pair.getLeft();   // 已存在：走 update
List<User> notExistList = pair.getRight();  // 不存在：走 insert

userDao.batchUpdate(existList);
userDao.batchInsert(notExistList);
```

**语义**：`left` 是**已经存在**的元素，`right` 是**不存在**的元素；`existIdCollection` 为 `null` / `empty` 时所有元素都进 `right`（可以直接走 insert）；原集合不变，返回的两个 `List` 都是新的，顺序参照原集合。

---

### 5️⃣ 分批处理并汇总为 Map：`BatchProcessorUtil.executePartitionsToMap(...)`（#865）

**背景**：4.5.5 引入的 `BatchProcessorUtil.executePartitions(...)` 返回的是 `List`，汇总时还得再合并一次。新方法直接让每批返回一个 `Map`，最终合并成一个。

```java
Map<String, Integer> result = BatchProcessorUtil.executePartitionsToMap(
                allOrderList,
                100,   // perPartitionSize：每批 100 条
                0,     // perPartitionSleepMilliseconds：批间不休眠
                batch -> {
                    Map<String, Integer> part = new LinkedHashMap<>();
                    for (Order order : batch) {
                        part.put(order.getOrderNo(), order.getStatus());
                    }
                    return part;
                });
```

**注意**：同样是**同步逐批**执行（不是并行，要并行用 `ThreadUtil.execute`）；`perPartitionSize <= 0`、`perPartitionSleepMilliseconds < 0` 抛 `IllegalArgumentException`，`function` 为 `null` 抛 `NullPointerException`，空输入返回空 `Map`。

---

### 6️⃣ HTTP 请求构建：`HttpRequest.builder()`（#588）

**背景**：`HttpRequest` 字段不少（uri、请求方法、header、param、body、trimUri…），只能靠 setter 一行行堆。现在支持 builder，链式写完再 `build()`。

```java
HttpRequest request = HttpRequest.builder()
                .uri("https://www.feilong.com/whoami")
                .httpMethodType(HttpMethodType.GET)
                .headerMap(headerMap)
                .trimUri(true)
                .build();
```

---

## 🚧 反射式 API 迁移对照（13 个方法）

这条线从 4.0.0 的第一个 `Function` 重载起步，4.5.2 大面积铺开，4.5.6 收尾。**方法体全部保留、不删除**，换的时候把属性名字符串换成 `Function` 即可 —— 类型安全、IDE 能补全、重构能改名。

```java
// ❌ 旧写法：属性名字符串，反射 + 无类型检查
CollectionsUtil.select(users, "status", Status.ACTIVE);

// ✅ 新写法：Function，编译期检查
CollectionsUtil.select(users, User::getStatus, Status.ACTIVE);
```

| 旧方法（`String propertyName`） | 请改用 | issue |
|---|---|---|
| `CollectionsUtil.removeDuplicate(Collection, String)` | `removeDuplicate(Collection, Function)` | #874 |
| `CollectionsUtil.removeDuplicate(Collection, String...)` | 同上（多个属性可以先组合成一个 key） | #875 |
| `CollectionsUtil.getPropertyValueList(Iterable, String)` | `getPropertyValueList(Collection, Function)` | #876 |
| `CollectionsUtil.getPropertyValueSet(Iterable, String)` | `getPropertyValueSet(Collection, Function)` | #877 |
| `CollectionsUtil.getPropertyValueMap(Iterable, String, String)` | `getPropertyValueMap(Iterable, Function, Function)` | #878 |
| `CollectionsUtil.find(Iterable, String, V)` | `find(Iterable, Function, V)` | #879 |
| `CollectionsUtil.select(Iterable, String, V...)` | `select(Iterable, Function, V...)` | #880 |
| `CollectionsUtil.select(Iterable, String, Collection)` | `select(Iterable, Function, Collection)` | #881 |
| `CollectionsUtil.selectRejected(Iterable, String, V...)` | `selectRejected(Iterable, Function, V...)` | #882 |
| `CollectionsUtil.selectRejected(Iterable, String, Collection)` | `selectRejected(Iterable, Function, Collection)` | #883 |
| `GroupUtil.groupOne(Iterable, String)` | `groupOne(Iterable, Function)` | #891 |
| `GroupUtil.group(Iterable, String, Predicate)` | `groupBy(Iterable, Function, Predicate)` | #892 |
| `GroupUtil.group(Iterable, String)` | `groupBy(Iterable, Function)` | #893 |

---

## ♻️ 代码优化与清理

- `DateUtil.getFirstDateOfThisWeek` / `getLastDateOfThisWeek` 实现重构整理（#889），周起止约定请优先使用上面新增的 `UseMonday` / `UseSunday` 版本。
- `CollectionsUtil.removeDuplicate(Collection, String...)` 性能提升（#327）。
- `native2ascii-maven-plugin` 升级到 2.1.1，修复新版 m2e 环境下的报错（#867）。
- 新增 `javax.annotation-api` 可选依赖，JDK 11+ 可直接编译（#872）。

---

## 🐛 修复

- **jsoup 依赖缺失时不再抛 `NoClassDefFoundError`**：`JsoupUtil` 现在直接抛 `JsoupUtilException`，并提示所需依赖 `org.jsoup:jsoup`（#525）。这类「类找不到」的报错，排查起来最费时间。
- **（安全）`feilong-office-zip` 修复 zip slip 路径穿越**：`AbstractUnzipHandler#write` 会校验解压条目不会逃出 `outputDirectory`，越界直接抛 `IllegalArgumentException`（#869）。

---

## 📖 文档与测试改进

- 全项目 javadoc 审计与修正：失效的 `{@link}`、示例中不存在的类 / 方法、与签名不符的 `@param` / `@return`、陈旧包名（#868）。
- `CollectionsUtil.splitDuplicate` javadoc 修正（#873）；`ResponseDownloadUtil` javadoc 完善（#737）。
- `HttpClientUtil` javadoc 新增「httpclient debug 日志」说明：httpclient4 被 shade 后 logger 名是 `com.feilong.lib.org.apache.http.*`，配 `org.apache.http` 无效（#894）。
- 新增 `README.en.md`（#871）。
- `feilong-office-csv` 补充单元测试（#47）。
- `feilong-net-mail` 补齐 javadoc 与**不依赖网络**的单元测试，`mvn test` 可直接跑通；需要真实邮箱的用例移出 `SuiteTests`，改为在 IDE 里手工运行（#897）。

---

## 🎉 最后

从 4.0.0 第一个 `Function` 重载，到 4.5.6 批量退役，这条线走了 **4 个大版本、14 个发布**，方法一个没删 —— 这是 feilong 一直的规矩：✅ **可以让你写得更安全，但不逼你改代码。**

完整更新日志见仓库 `doc/release-notes/release-notes-4.5.6.md`
Milestone（38 items）：https://github.com/ifeilong/feilong/milestone/55?closed=1
源码仓库：https://github.com/ifeilong/feilong

如果这个工具包帮你省过时间，欢迎给个 Star 🌟

