# feilong 4.5.6 公众号文章（草稿）

> **标题候选**
> 1. feilong 4.5.6：13 个反射式 API 正式退役，`defaultZero` 补上金额那一格
> 2. 写了十年的 `"propertyName"`，从这个版本开始收摊
> 3. feilong 4.5.6 发布：把「本周第一天是周几」写进方法名
>
> **摘要（120 字内）**
> 4.5.6 共 38 项改动。主线是把 `CollectionsUtil` / `GroupUtil` 里 13 个 `String propertyName` 的反射方法标为 `@Deprecated`；同时补上金额兜底 `defaultZero(BigDecimal)`、懒创建 `defaultIfNull(x, Supplier)`、周起止的明确命名，以及 `splitByExistId`、`HttpRequest.builder()`。
>
> **封面**：纯色底 + 大字号版本号 `4.5.6`，下方一行小字「反射式 API 退役」。

---

## 正文

🎯 4.5.6 一共 38 项改动，但真正的主线只有一条：

**把 `String propertyName` 这种反射写法，从 `CollectionsUtil` 和 `GroupUtil` 里请出去。**

这件事从 4.0.0 埋下第一颗种子（`getPropertyValueList(Collection, Function)`），4.5.2 大面积铺开，到 4.5.6 收尾：13 个方法一次性标上 `@Deprecated`。

顺带补了几个平时写业务天天要用、但一直缺的小东西。

---

### ⚠️ 一、先说三件会影响你的事

**1. 13 个方法标了 `@Deprecated`，但一个都没删。**

现有代码可以原样编译、原样运行，只是编译时会看到一行提示。要不要现在迁移，你自己定。

**2. `ObjectUtil.defaultIfNull(user, null)` 这种写法，现在编译不过了。**

新增的孪生方法 `defaultIfNull(T, Supplier<T>)` 和原来的 `defaultIfNull(T, Object)` 撞了重载。如果你确实想显式传 null，加一个转型即可：

```java
// ❌ 编译报错：reference to defaultIfNull is ambiguous
ObjectUtil.defaultIfNull(user, null);

// ✅ 显式转型，告诉编译器你要哪个重载
ObjectUtil.defaultIfNull(user, (Supplier<User>) null);
```

**3. 两个新依赖 / 行为变化。**

- 新增 `javax.annotation-api`（可选依赖）：JDK 11+ 下编译更顺，用到 `@PostConstruct` 请自行声明，它不会传递到你工程里。
- `feilong-office-zip` 修了 zip slip 路径穿越：如果你继承过 `AbstractUnzipHandler` 并覆写过 `write`，确认一下你的实现没有绕过新加的校验。

---

### 💰 二、金额兜底：`defaultZero` 终于支持 `BigDecimal`

业务代码里到处都是这句：

```java
BigDecimal amount = value != null ? value : BigDecimal.ZERO;
```

`ObjectUtil` 之前只有 `defaultZero(Integer)`，偏偏金额和 ID 用得最多的 `BigDecimal`、`Long` 没有。现在补上了：

```java
// 金额：null 和负数都当 0，正数原样返回
BigDecimal amount = ObjectUtil.defaultZero(new BigDecimal("-1.00"));  // 0
BigDecimal price  = ObjectUtil.defaultZero(new BigDecimal("12.30"));  // 12.30

// ID / 计数
Long id  = ObjectUtil.defaultZero((Long) null);  // 0L
Long seq = ObjectUtil.defaultZero(-5L);          // 0L
```

两个实现细节：

- 判断正负用的是 `compareTo`，不是 `equals` —— `new BigDecimal("0.00").equals(BigDecimal.ZERO)` 是 `false`，只有 `compareTo` 才认得出「等于 0」。
- **不改 scale**。入参是 `12.30` 就还是 `12.30`，不会被悄悄改成 `12.3` 或 `12.3000`。

如果你只想兜 null、负数要保留，别用这个，用 `defaultIfNull(value, BigDecimal.ZERO)`。

---

### 🦥 三、懒创建：`defaultIfNull(x, Supplier)` / `defaultNewIfNull`

`defaultIfNull(x, new Xxx())` 一直有个隐痛：**当 `x` 不为 null 时，那个 `new Xxx()` 也白创建了**。有开销、有副作用（比如构造函数里去查库）时尤其难受。

现在可以传 `Supplier`，只有真的为 null 才执行：

```java
// 只有 cachedUser 为 null 时，才会真的去查库
User user = ObjectUtil.defaultIfNull(cachedUser, () -> userDao.load(userId));
```

还有一个「传类、为 null 才反射 new」的版本，适合代码里那些 `new XxxContext()` 的样板：

```java
PodcastFileContext context =
        ObjectUtil.defaultNewIfNull(assertContext.getPodcastFileContext(), PodcastFileContext.class);
```

---

### 📅 四、「本周第一天」到底是周一还是周日？现在写在方法名里了

日期工具类的经典争议。以前只有 `getFirstDateOfThisWeek(Date)`，你得去看一眼实现或者猜。

这个版本把约定直接写进方法名：

```java
Date date = now();

Date monday = DateUtil.getFirstDateOfThisWeekUseMonday(date);
Date sunday = DateUtil.getLastDateOfThisWeekUseSunday(date);
```

还补了成对返回的版本，省掉算两次：

```java
Pair<Date, Date> thisWeek = DateUtil.getWeekStartAndEndPairMondayFirst();
Pair<Date, Date> thatWeek = DateUtil.getWeekStartAndEndPairSundayFirst(date);

System.out.println(thisWeek.getLeft() + " ~ " + thisWeek.getRight());
```

| 方法 | 说明 |
|---|---|
| `getFirstDateOfThisWeekUseMonday(Date)` | 所在周的周一 |
| `getLastDateOfThisWeekUseSunday(Date)` | 所在周的周日 |
| `getWeekStartAndEndPairMondayFirst()` / `(Date)` | 周一起始的「开始 / 结束」对 |
| `getWeekStartAndEndPairSundayFirst()` / `(Date)` | 周日起始的「开始 / 结束」对 |

---

### ✂️ 五、批量保存：一次拆成 update 和 insert

批量保存的典型写法是「已存在走 update、不存在走 insert」，以前要自己 `Map` 一次再分两遍。现在一次拆好：

```java
Pair<List<User>, List<User>> pair =
        CollectionsUtil.splitByExistId(users, User::getId, existIdCollection);

List<User> existList    = pair.getLeft();   // 已存在：update
List<User> notExistList = pair.getRight();  // 不存在：insert

userDao.batchUpdate(existList);
userDao.batchInsert(notExistList);
```

`left` 是**已存在**，`right` 是**不存在**；传 `null` / 空集合时全部进 `right`，可以直接整批 insert。原集合不动，返回的两个 List 都是新的。

---

### 📦 六、分批处理直接汇总成 Map

4.5.5 引入的 `BatchProcessorUtil.executePartitions(...)` 返回 `List`，汇总时还得再合并一次。现在每批可以直接返回一个 `Map`，最后自动合并：

```java
Map<String, Integer> result = BatchProcessorUtil.executePartitionsToMap(
        allOrderList,
        100,   // 每批 100 条
        0,     // 批间不休眠
        batch -> {
            Map<String, Integer> part = new LinkedHashMap<>();
            for (Order order : batch) {
                part.put(order.getOrderNo(), order.getStatus());
            }
            return part;
        });
```

注意它仍然是**同步逐批**执行，不是并行；要并行用 `ThreadUtil.execute` 那一套。

---

### 🌐 七、`HttpRequest` 支持 builder

字段多（uri、请求方法、header、param、body、trimUri…），以前只能一行行 setter。现在：

```java
HttpRequest request = HttpRequest.builder()
        .uri("https://www.feilong.com/whoami")
        .httpMethodType(HttpMethodType.GET)
        .headerMap(headerMap)
        .trimUri(true)
        .build();
```

---

### 🔄 八、迁移对照表（13 个方法）

方法体全部保留，换的时候把属性名字符串换成 `Function` 就行 —— 类型安全、IDE 能补全、重构能改名。

| 旧写法 | 请改用 |
|---|---|
| `CollectionsUtil.removeDuplicate(Collection, String)` | `removeDuplicate(Collection, Function)` |
| `CollectionsUtil.removeDuplicate(Collection, String...)` | 同上（多属性先组合成一个 key） |
| `CollectionsUtil.getPropertyValueList(Iterable, String)` | `getPropertyValueList(Collection, Function)` |
| `CollectionsUtil.getPropertyValueSet(Iterable, String)` | `getPropertyValueSet(Collection, Function)` |
| `CollectionsUtil.getPropertyValueMap(Iterable, String, String)` | `getPropertyValueMap(Iterable, Function, Function)` |
| `CollectionsUtil.find(Iterable, String, V)` | `find(Iterable, Function, V)` |
| `CollectionsUtil.select(Iterable, String, V...)` | `select(Iterable, Function, V...)` |
| `CollectionsUtil.select(Iterable, String, Collection)` | `select(Iterable, Function, Collection)` |
| `CollectionsUtil.selectRejected(Iterable, String, V...)` | `selectRejected(Iterable, Function, V...)` |
| `CollectionsUtil.selectRejected(Iterable, String, Collection)` | `selectRejected(Iterable, Function, Collection)` |
| `GroupUtil.groupOne(Iterable, String)` | `groupOne(Iterable, Function)` |
| `GroupUtil.group(Iterable, String)` | `groupBy(Iterable, Function)` |
| `GroupUtil.group(Iterable, String, Predicate)` | `groupBy(Iterable, Function, Predicate)` |

改起来就一行的事：

```java
// before：字符串，写错了只有运行时知道
CollectionsUtil.select(users, "status", Status.ACTIVE);

// after：编译期检查
CollectionsUtil.select(users, User::getStatus, Status.ACTIVE);
```

---

### 🐛 九、修复

- 🧩 **jsoup 依赖缺失时不再抛 `NoClassDefFoundError`。** `JsoupUtil` 现在直接抛 `JsoupUtilException`，并告诉你需要 `org.jsoup:jsoup`。这类「类找不到」的报错，排查起来最费时间。
- 🔒 **（安全）`feilong-office-zip` 修复 zip slip 路径穿越。** `AbstractUnzipHandler#write` 会校验解压条目不会逃出 `outputDirectory`，越界直接抛 `IllegalArgumentException`。

---

### 🛠️ 十、工程侧

- 全项目 javadoc 审计：失效的 `{@link}`、示例里不存在的类和方法、与签名不符的 `@param`、陈旧包名，一次性修掉。
- `feilong-net-mail` 补齐 javadoc 和**不依赖网络**的单元测试，`mvn test` 直接能跑；需要真实邮箱的用例移出 `SuiteTests`，改在 IDE 里手工跑。
- 新增 `README.en.md`。
- `CollectionsUtil.removeDuplicate(Collection, String...)` 性能提升。

---

### 🎉 最后

从 4.0.0 第一个 `Function` 重载，到 4.5.6 批量退役，这条线走了 **4 个大版本、14 个发布**，方法一个没删 —— 这是 feilong 一直的规矩：✅ **可以让你写得更安全，但不逼你改代码。**

完整更新日志：仓库里的 `doc/release-notes/release-notes-4.5.6.md`
Milestone（38 items）：https://github.com/ifeilong/feilong/milestone/55?closed=1
源码仓库：https://github.com/ifeilong/feilong

如果这个工具包帮你省过时间，欢迎给个 Star。
