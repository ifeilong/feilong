# feilong 4.5.6 Release Notes（Milestone #55）

> **里程碑**：4.5.6 · Milestone #55（38 items, 100% completed）  
> **仓库**：https://github.com/ifeilong/feilong  
> **Milestone 视图**：https://github.com/ifeilong/feilong/milestone/55?closed=1

## 升级提示（先看这个）
- 本版本的主线是**弱化、最终取消 `String propertyName` 反射式 API**：`CollectionsUtil`、`GroupUtil` 里 13 个属性名(反射)方法标记为 **`@Deprecated`**，**方法不删除**、现有代码可以继续编译，建议逐步迁移到对应的 `Function` 版本（完整对照见下方「废弃与迁移」）。
- `ObjectUtil.defaultIfNull(T, Object)` 有了新的孪生方法 `defaultIfNull(T, Supplier<T>)`。如果你原来写过 `defaultIfNull(user, null)` 这种**直接传 null 字面量**的调用，现在会产生重载歧义、编译失败，请显式转型：`defaultIfNull(user, (Supplier<User>) null)`。
- 新增 `javax.annotation-api` 可选依赖（#872）：JDK 11+ 下编译更顺；因为是 `optional`，不会传递到你的工程，用到 `@PostConstruct` 等注解时请自行声明。
- `feilong-office-zip` 修复了 zip slip 路径穿越问题（#869）：如果你自己继承了 `AbstractUnzipHandler` 并覆写过 `write`，请确认新加的路径校验没有被你的实现绕过。

---

## ✨ 新特性 / 增强（New Features & Enhancements）

### 1️⃣ 金额/计数的 0 兜底：`ObjectUtil.defaultZero(BigDecimal)` / `defaultZero(Long)` (#896)

**背景**：金额、计数这类字段经常需要"null 当 0"的兜底，业务代码里到处是 `value != null ? value : BigDecimal.ZERO`。之前 `ObjectUtil` 只有 `defaultZero(Integer)`，而金额与 ID 场景用得最多的 `BigDecimal`、`Long` 反而没有。

**语义**：`null` 或**负数**返回 `0`，正数**原样返回**。大小判断用 `compareTo`（`new BigDecimal("0.00").equals(BigDecimal.ZERO)` 是 `false`，`compareTo` 才是 0）；**不做 `setScale`**，入参的 scale 原样保留。

**示例**：
```java
// 金额：null 和负数都当 0，正数保留原 scale
BigDecimal amount = ObjectUtil.defaultZero(new BigDecimal("-1.00"));  // 0
BigDecimal price  = ObjectUtil.defaultZero(new BigDecimal("12.30"));  // 12.30

// ID / 计数
Long id  = ObjectUtil.defaultZero((Long) null);  // 0L
Long seq = ObjectUtil.defaultZero(-5L);          // 0L
```

**说明**：如果只想兜 `null`、负数保留，不要用本方法，用 `ObjectUtil.defaultIfNull(value, BigDecimal.ZERO)`。

---

### 2️⃣ 懒创建兜底：`ObjectUtil.defaultIfNull(T, Supplier<T>)` / `defaultNewIfNull(T, Class<T>)` (#890, #628)

**背景**：`defaultIfNull(x, new Xxx())` 的问题是——当 `x` 不为 `null` 时，那个 `new Xxx()` 也白创建了（有开销或副作用时很难受）。现在可以传 `Supplier`，只有真的为 `null` 时才创建；`defaultNewIfNull` 则是"传类、为 null 才反射 new"。

**示例**：
```java
// Supplier 版本：只有 cachedUser 为 null 时才会去查库
User user = ObjectUtil.defaultIfNull(cachedUser, () -> userDao.load(userId));

// 反射版本：只有为 null 时才 new（目标类需要有无参构造方法）
PodcastFileContext context =
                ObjectUtil.defaultNewIfNull(assertContext.getPodcastFileContext(), PodcastFileContext.class);
```

---

### 3️⃣ 一周的边界：`DateUtil` 新增周相关的明确命名方法 (#68, #884, #885, #886, #887, #888)

**背景**："本周第一天"到底是周一还是周日，历来是日期工具类的重灾区；原来只有 `getFirstDateOfThisWeek(Date)` / `getLastDateOfThisWeek(Date)`，靠猜约定。本版本把约定写进方法名，并补了成对返回的 `Pair` 版本，省掉两次计算。

**示例**：
```java
Date date = now();

// 约定写进方法名，不用再猜
Date monday = DateUtil.getFirstDateOfThisWeekUseMonday(date);
Date sunday = DateUtil.getLastDateOfThisWeekUseSunday(date);

// 成对返回（Pair 来自 com.feilong.lib.lang3.tuple.Pair）
Pair<Date, Date> thisWeek = DateUtil.getWeekStartAndEndPairMondayFirst();
System.out.println(thisWeek.getLeft() + " ~ " + thisWeek.getRight());

Pair<Date, Date> thatWeek = DateUtil.getWeekStartAndEndPairSundayFirst(date);
```

| 方法 | 说明 |
|---|---|
| `getFirstDateOfThisWeekUseMonday(Date)` | 所在周的周一（#884） |
| `getLastDateOfThisWeekUseSunday(Date)` | 所在周的周日（#68） |
| `getWeekStartAndEndPairMondayFirst()` / `(Date)` | 周一起始的「开始/结束」对（#885 #886） |
| `getWeekStartAndEndPairSundayFirst()` / `(Date)` | 周日起始的「开始/结束」对（#887 #888） |

---

### 4️⃣ 按 ID 拆分批次：`CollectionsUtil.splitByExistId(...)` (#707)

**背景**：批量保存时典型写法是"已存在走 update、不存在走 insert"，之前要自己 `Map` 一次再分两遍。`splitByExistId` 一次拆好，返回 `Pair`。

**语义**：`left` 是**已经存在**的元素，`right` 是**不存在**的元素；`existIdCollection` 为 `null`/`empty` 时所有元素都进 `right`（可以直接走 insert）；原集合不变，返回的两个 `List` 都是新的，且顺序参照原集合。

**示例**：
```java
// 先把库里已存在的 id 查出来，再一次拆成两批
Pair<List<User>, List<User>> pair =
                CollectionsUtil.splitByExistId(users, User::getId, existIdCollection);

List<User> existList    = pair.getLeft();   // 已存在：走 update
List<User> notExistList = pair.getRight();  // 不存在：走 insert

userDao.batchUpdate(existList);
userDao.batchInsert(notExistList);
```

---

### 5️⃣ 分批处理并汇总为 Map：`BatchProcessorUtil.executePartitionsToMap(...)` (#865)

**背景**：4.5.5 引入的 `BatchProcessorUtil.executePartitions(...)` 返回的是 `List`，汇总时还得再合并一次。新方法直接让每批返回一个 `Map`，最终合并成一个。

**示例**：每 100 条一批，逐批处理成 `Map` 后合并。
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

**说明**：同样是**同步逐批**执行（不是并行）；`perPartitionSize <= 0`、`perPartitionSleepMilliseconds < 0` 抛 `IllegalArgumentException`，`function` 为 `null` 抛 `NullPointerException`，空输入返回空 `Map`。

---

### 6️⃣ HTTP 请求构建：`HttpRequest.builder()` (#588)

**背景**：`HttpRequest` 字段不少（uri、请求方法、header、param、body、trimUri…），只能靠 setter 一行行堆。现在支持 builder，链式写完再 `build()`。

**示例**：
```java
HttpRequest request = HttpRequest.builder()
                .uri("https://www.feilong.com/whoami")
                .httpMethodType(HttpMethodType.GET)
                .headerMap(headerMap)
                .trimUri(true)
                .build();
```

---

## 🧹 废弃与迁移（Deprecations）

以下方法全部标记 `@Deprecated`，**方法体保留、不删除**，现有代码可以继续编译；迁移时把属性名字符串换成 `Function` 即可（类型安全、IDE 可补全、重构能改名）。

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

**示例**：
```java
// ❌ 旧写法：属性名字符串，反射 + 无类型检查
// CollectionsUtil.select(users, "status", Status.ACTIVE);

// ✅ 新写法：Function，编译期检查
List<User> activeUsers = CollectionsUtil.select(users, User::getStatus, Status.ACTIVE);
```

---

## 🐛 修复

- `feilong-net-jsoup`：`JsoupUtil` 在 classpath 缺少 jsoup 时抛出 `JsoupUtilException` 并直接提示所需依赖（`org.jsoup:jsoup`），不再抛难以定位的 `NoClassDefFoundError: org/jsoup/Jsoup`（#525）。
- **（安全）**`feilong-office-zip`：修正解压时的路径穿越风险（zip slip）——`AbstractUnzipHandler#write` 现在会校验解压条目不会逃出 `outputDirectory`，否则抛 `IllegalArgumentException`（#869）。

---

## ♻️ 代码优化 / 底层对齐（Improvements & Cleanup）

- `DateUtil.getFirstDateOfThisWeek` / `getLastDateOfThisWeek` 的实现做了重构整理（#889），周起止约定请优先使用上面新增的 `UseMonday` / `UseSunday` 版本。
- `CollectionsUtil.removeDuplicate(Collection, String...)` 性能提升（#327）。
- `native2ascii-maven-plugin` 升级到 2.1.1，修复新版 m2e 环境下的报错（#867）。
- 新增 `javax.annotation-api` **可选**依赖，JDK 11+ 可直接编译（#872）。

---

## 📖 文档

- 全项目 javadoc 审计与修正：失效的 `{@link}`、示例中不存在的类/方法、与签名不符的 `@param`/`@return`、陈旧包名等（#868）。
- `CollectionsUtil.splitDuplicate` javadoc 修正（#873）。
- `HttpClientUtil` javadoc 新增「httpclient debug 日志」说明：httpclient4 被 shade 后 logger 名是 `com.feilong.lib.org.apache.http.*`，配 `org.apache.http` 无效（#894）。
- 完善 `ResponseDownloadUtil` javadoc（#737）。
- 新增 `README.en.md`（#871）。

---

## 🧪 测试

- `feilong-office-csv` 补充单元测试（#47）。
- `feilong-net-mail` 补齐 javadoc 与**不依赖网络**的单元测试（会话属性构建、MessageBuilder、header/body setter、`SessionFactory`、`DefaultMailSender` 参数校验），`mvn test` 可直接跑通；需要真实邮箱的用例移出 `SuiteTests`，改为手工在 IDE 运行（#897）。

---
