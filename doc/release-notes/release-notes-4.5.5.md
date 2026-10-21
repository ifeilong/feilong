# feilong 4.5.5 Release Notes（Milestone #54）

> **里程碑**：4.5.5 · Milestone #54（11 items, 100% completed）  
> **仓库**：https://github.com/ifeilong/feilong  
> **Milestone 视图**：https://github.com/ifeilong/feilong/milestone/54?closed=1

## 升级提示（先看这个）
- `CollectionsUtil.removeAll(...)` 与 `addAllIgnoreNull(...)` 的参数改成了**可变参数**，一次可以传多个集合 / `Iterable`。源码层面单参调用兼容、行为不变，但**方法描述符变了，已有的编译产物需要重新编译**。
- `com.feilong.context.converter.StringToBeanConverter<T>` 现在继承 `java.util.function.Function<String, T>`，可以直接当作 `Function` 传参。
- 本版本新增的 `CollectionsUtil.removeDuplicate(Collection, Function)` 建议优先使用；属性名(反射)版本此时仍然保留（4.5.6 起标记 `@Deprecated`）。

---

## ✨ 新特性 / 增强（New Features & Enhancements）

### 1️⃣ 集合截断：`CollectionsUtil.limit(List/Set, int)` (#854, #860)

**背景**：只需要"前 N 条"（取最新几条、首页展示、日志打样）时，手写 `subList` 要处理 `toIndex` 越界、`null`、`maxSize` 为 0 / 负数等边界。`limit` 把这些分支收成一次调用，`List` 和 `Set` 各有一个重载。

**边界说明**：`null` 或空集合原样返回；`maxSize = 0` 返回空集合；`maxSize < 0` 抛 `IllegalArgumentException`；元素不够 `maxSize` 时返回原样的全部元素。

**示例**：
```java
List<String> list = Arrays.asList("a", "b", "c", "d", "e");

List<String> top3 = CollectionsUtil.limit(list, 3);   // [a, b, c]
List<String> empty = CollectionsUtil.limit(list, 0);  // []
List<String> same = CollectionsUtil.limit(null, 5);   // null

Set<String> citySet = new LinkedHashSet<>(Arrays.asList("上海", "北京", "广州"));
Set<String> top2 = CollectionsUtil.limit(citySet, 2); // [上海, 北京]
```

---

### 2️⃣ 拍平：`CollectionsUtil.flattenToList` / `flattenToSet` (#857, #862)

**背景**：分批查询、并发聚合之后，结果往往是"一堆 List"或"一堆 Set"，要合成一个还经常踩空元素的坑。`flattenToList(Collection<List<T>>)` 与 `flattenToSet(Collection<Set<T>>)` 直接拍平，后者天然去重。

**示例**：
```java
List<List<String>> lists = Arrays.asList(
                Arrays.asList("a", "b"),
                Arrays.asList("c"),
                Arrays.asList("a", "d"));

List<String> all = CollectionsUtil.flattenToList(lists);   // [a, b, c, a, d]

Set<Set<String>> sets = new LinkedHashSet<>();
sets.add(new LinkedHashSet<>(Arrays.asList("a", "b")));
sets.add(new LinkedHashSet<>(Arrays.asList("b", "c")));

Set<String> union = CollectionsUtil.flattenToSet(sets);    // [a, b, c]
```

---

### 3️⃣ 按 lambda 去重：`CollectionsUtil.removeDuplicate(Collection, Function)` (#852)

**背景**：去重最常见的诉求是"按某个 key 去重"，之前只能传属性名字符串，走反射、没有类型检查。现在可以直接传 `Function`，IDE 能补全、重构能改名。

**示例**：按用户 id 去重，保留第一次出现的元素。
```java
List<User> users = ...;

List<User> distinctUsers = CollectionsUtil.removeDuplicate(users, User::getId);
```

---

### 4️⃣ 分批处理：`com.feilong.context.BatchProcessorUtil` (#853)

**背景**：调用第三方接口、批量入库时，一次全量提交容易超时或超限，通常要"分片 + 批间休眠 + 汇总结果"。新增的 `BatchProcessorUtil.executePartitions(...)` 把这套循环封装起来，并做了 `perPartitionSize <= 0`、`perPartitionSleepMilliseconds < 0`、`function` 为 `null` 的入参校验。

**⚠️ 定位说明**：它是**同步逐批**执行（不是并行），返回值顺序与分区顺序一致；需要并行请自行配线程池。空输入返回空 `List`。

**示例**：每 100 条一批，每批之间休眠 10ms。
```java
List<String> results = BatchProcessorUtil.executePartitions(
                allOrderList,
                100,   // perPartitionSize：每批 100 条
                10,    // perPartitionSleepMilliseconds：批间休眠 10ms
                batch -> {
                    // 处理这一批，返回这一批的结果
                    return remoteService.submit(batch);
                });
```

---

## 🐛 修复

- `com.feilong.context.invoker.SimpleHttpAndJsonResponseCommandBuilder`：`responseCommandRootClass` 传 `String.class` 时，现在直接输出响应结果字符串；之前会输出空值（#858）。

---

## ♻️ 代码优化 / 底层对齐（Improvements & Cleanup）

- `CollectionsUtil.removeAll(Collection<O>, Collection<O>...)` 改成可变参数，一次可以删除多个集合（#855）——单参调用行为不变。
- `CollectionsUtil.addAllIgnoreNull(Collection<O>, Iterable<? extends O>...)` 改成可变参数，一次可以合并多个 `Iterable`，仍然跳过 `null` 元素（#856）。
- `com.feilong.context.converter.StringToBeanConverter<T>` 继承 `java.util.function.Function<String, T>`（#859），与库内其它"函数式接口"的写法对齐。
- Javadoc：完善 `com.feilong.tools.log.LogHelper` 并补充示例（#861）。

---
