# feilong 4.5.6 朋友圈文案

> 朋友圈不渲染 Markdown：下面三段都是**纯文本**（已去掉反引号和星号，可直接复制粘贴）。
> 换行会被保留，但超过 5 行左右会被折叠成「全文」——第一行必须自带信息量。

---

## 版本 A（推荐，约 130 字）

🚀 feilong 4.5.6 发了。

主线只有一条：CollectionsUtil / GroupUtil 里 13 个 String propertyName 的反射方法，一次性标了 @Deprecated。方法一个没删，你的代码照常编译，什么时候迁自己定。

顺手补了我自己天天用的几个：

💰 defaultZero(BigDecimal)：金额 null / 负数都当 0，正数 scale 原样保留
⚡ defaultIfNull(x, Supplier)：不为 null 就不创建
📅 「本周第一天」是周一还是周日，终于写进方法名了
✂️ splitByExistId：批量保存一次拆 update / insert

https://github.com/ifeilong/feilong

⚠️ 升级注意：defaultIfNull(user, null) 现在有重载歧义，要显式转型。


## 版本 B（极简，3 行）

feilong 4.5.6：13 个反射式 API 全部 @Deprecated，方法一个没删 ✅

defaultZero 支持 BigDecimal 了，金额兜底不用再手写 != null ? : ZERO 💰

https://github.com/ifeilong/feilong

---

## 版本 C（痛点开头，适合工作群 / 同事）


写业务最烦的两句话：

😤 value != null ? value : BigDecimal.ZERO
😤 getFirstDateOfThisWeek() —— 到底是周一还是周日？

feilong 4.5.6 都给了答案：defaultZero(BigDecimal) + getFirstDateOfThisWeekUseMonday(date)。

同版本还把 CollectionsUtil / GroupUtil 里 13 个 "propertyName" 反射方法统一标了 @Deprecated（不删），换 Function 就行。

https://github.com/ifeilong/feilong

---

## 发布建议

| 项 | 建议 |
|---|---|
| 时间 | 工作日 09:00–10:00 或 20:00–22:00（技术号两个小高峰） |
| 配图 | 1 张：纯色底 + 大字号 `4.5.6` + 一行小字「13 个反射式 API 退役」；或直接截代码前后对比（`select(users, "status", ...)` → `select(users, User::getStatus, ...)`） |
| 评论 | 自评一条放公众号文章链接，正文只放 GitHub，避免朋友圈正文被链接吃掉 |
| emoji | 每处最多 1 个，别超过 6 个；开头 1 个 + 每条特性 1 个，节奏刚好 |
| 后续 | 同一篇可拆成「迁移指南」短内容，过几天再发一次，成本低 |

> 提示：朋友圈正文里如果放公众号链接，会被折叠成卡片；放 GitHub 链接一般能直接显示。
