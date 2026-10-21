feilong 3.0.0 发布了，feilong 是一个让Java开发更简便的工具库。

1. 让你从大量重复的底层代码中脱身,提高工作效率;
1. 让你的代码更简炼，易写、易读、易于维护;

文档地址: http://feilong-core.mydoc.io/

单元测试数 增加至 `2259` 个, 单元测试覆盖率 增加至 `91%` ,javadoc 比率 `83%`

本次升级共有 `140` 处变更, 具体参见  [3.0.0 milestone](https://github.com/ifeilong/feilong/milestone/1?closed=1)



## :alien: [不兼容](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A不兼容+milestone%3A3.0.0+is%3Aclosed)

#106 :sparkles: DateExtensionUtil 代码 并入到 DateUtil  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)  [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A3.0.0+is%3Aclosed)]
#147  Json 工具包名进行修改  com.feilong.json.jsonlib.JsonUtil改成 com.feilong.json.JsonUtil  [[feilong-json](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-json+milestone%3A3.0.0+is%3Aclosed)]
#149  com.feilong.net.entity 改名为 com.feilong.net.http  [[feilong-net](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net+milestone%3A3.0.0+is%3Aclosed)]
#148  com.feilong.net.httpclient4 改名成 com.feilong.net.http 包  [[feilong-net](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net+milestone%3A3.0.0+is%3Aclosed)]
#184  feilong-velocity 改名为 feilong-template  [[结构调整](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A结构调整+milestone%3A3.0.0+is%3Aclosed)]
#152  修改 com.feilong.office.csv 包名为 com.feilong.csv  [[feilong-csv](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-csv+milestone%3A3.0.0+is%3Aclosed)]
#150 :fire: 删除 com.feilong.net.ssl.TrustAnyTrustManager  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#160 :fire: 删除 com.feilong.tools.jsonlib.JsonUtil  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#3  feilong-webservice-cxf 改名成 feilong-net-cxf
#34  修改 com.feilong.tools.office.csv package 为 com.feilong.office.csv  [[feilong-csv](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-csv+milestone%3A3.0.0+is%3Aclosed)]
#12 :fire: 删除 AbstractSymmetricEncryption ,代码合并到 SymmetricEncryption  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#25 :fire: 删除 DomParser  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#11 :fire: 删除 KeyBuilder  [[feilong-security](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-security+milestone%3A3.0.0+is%3Aclosed)  [删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#13 :fire: 删除 SymmetricEncryptionConfig 合并到SymmetricEncryption  [[feilong-security](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-security+milestone%3A3.0.0+is%3Aclosed)  [删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#10 :fire: 删除 com.feilong.security.symmetric.CipherUtil.opBytes(byte[], int, String, Key, Provider)  [[feilong-security](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-security+milestone%3A3.0.0+is%3Aclosed)  [删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#6 :fire: 删除 com.feilong.tools.MoneyUtil.fen2Yuan(long)  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#5 :fire: 删除 com.feilong.tools.MoneyUtil.yuan2Fen(double)  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#19 :fire: 删除 com.feilong.xml.XmlUtil.format()  [[feilong-xml](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-xml+milestone%3A3.0.0+is%3Aclosed)  [删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#21 :fire: 删除 com.feilong.xml.XmlUtil.getNodeByXPath(String)    [[feilong-xml](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-xml+milestone%3A3.0.0+is%3Aclosed)  [删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#20 :fire: 删除 com.feilong.xml.XmlUtil.getObjectByXPath(String, QName)   [[feilong-xml](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-xml+milestone%3A3.0.0+is%3Aclosed)  [删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#29 :fire: 删除 feilong-core-extension MathUtil  [[feilong-core-extension](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core-extension+milestone%3A3.0.0+is%3Aclosed)  [删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#33 :fire: 删除 feilong-tools-money  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)  [结构调整](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A结构调整+milestone%3A3.0.0+is%3Aclosed)]


## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A3.0.0+is%3Aclosed)

#207 :sparkles: StringUtil 添加 SPACE 和 EMPTY  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]
#206 :sparkles: SystemUtil  add USER_HOME  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]
#128 :wastebasket: com.feilong.core.UncheckedIOException deprecated  [[deprecated](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Adeprecated+milestone%3A3.0.0+is%3Aclosed)]
#126 :sparkles: 新增 com.feilong.core.util.ResourceBundleUtil.toMap(String...)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]
#209 :sparkles: 新建 com.feilong.core.Validate  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]
#197 :sparkles: 新建 com.feilong.core.bean.PropertyUtil.getPropertyDescriptors(Class<?>)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]
#210 :sparkles: 新建 com.feilong.core.lang.ArrayUtil.EMPTY_STRING_ARRAY  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]
#208 :sparkles: 新建 com.feilong.core.lang.ObjectUtil.defaultIfNull(T, T)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]


## :apple: [feilong-component](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-component+milestone%3A3.0.0+is%3Aclosed)

#215 :sparkles: 新建 DefaultRequestFileCreatorDetector  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]
#31 :sparkles: 新建 feilong-component  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)  [重要](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A重要+milestone%3A3.0.0+is%3Aclosed)]


## :guitar: [feilong-context](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-context+milestone%3A3.0.0+is%3Aclosed)

#220 :sparkles: 新建 FileParser  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]
#212 :sparkles: 新建 RequestDataBuilder  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]
#213 :sparkles: 新建 RequestFileCreator  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]
#214 :sparkles: 新建 RequestFileCreatorDetector  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]
#219 :sparkles: 新建 RequestFileParserDetector  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]


## :melon: [feilong-email](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-email+milestone%3A3.0.0+is%3Aclosed)

#216 :sparkles: email 支持 附件AttachFileLocations 模式  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]
#1 :sparkles: feilong-net-mail 打入到 all里面  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]


## :fishing_pole_and_fish: [feilong-io](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-io+milestone%3A3.0.0+is%3Aclosed)

#218 :sparkles: IOWriteUtil.write(InputStream, String, String) 修改 return type 从void 到 String  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]
#107  com.feilong.io.FilenameUtil.getFileName(String) 添加参数校验
#199 :sparkles: 新建 com.feilong.io.IOUtil.closeQuietly(Closeable...)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]
#100 :wastebasket: 标识 com.feilong.io.SpecialFolder.getDesktop() Deprecated  [[deprecated](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Adeprecated+milestone%3A3.0.0+is%3Aclosed)]
#102 :wastebasket: 标识 com.feilong.io.SpecialFolder.getMyDocuments() 过时  [[deprecated](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Adeprecated+milestone%3A3.0.0+is%3Aclosed)]
#161 :sparkles: 让 IOReaderUtil 支持 location 比如 classpath  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]


## :meat_on_bone: [feilong-json](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-json+milestone%3A3.0.0+is%3Aclosed)

#157 :fire: 删除 JsonEventListener  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#37 :sparkles: JsonUtil toMap 异常优化  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]
#26 :fire: 删除 feilong-json-api  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#27 :fire: 删除 feilong-json-jsonlib 合并到feilong-json  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]


## :lock: [feilong-security](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-security+milestone%3A3.0.0+is%3Aclosed)

#9  去掉 SymmetricEncryption finnal
#7 :sparkles: 新建 AesUtil  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]
#8 :wastebasket: 标识 com.feilong.security.HexUtil 过时  [[deprecated](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Adeprecated+milestone%3A3.0.0+is%3Aclosed)]


## :baby_bottle: [feilong-servlet](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-servlet+milestone%3A3.0.0+is%3Aclosed)

#105  RequestUtil STATIC_RESOURCE_SUFFIX 提取出去
#99 :sparkles: httpclientutil getClientIp 要支持允许定制化头  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]


## :fork_and_knife: [feilong-xml](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-xml+milestone%3A3.0.0+is%3Aclosed)

#155  XmlUtil.fromXML(String, String) 改名成 toMap
#15 :fire: 删除 feilong-xml-dom  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#16 :fire: 删除 feilong-xml-xstream  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#22 :sparkles: 新建 XmlUtil.getNodeNameAndStringValueMap(Object, String) static method  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]
#18 :sparkles: 新建 com.feilong.xml.XmlUtil.format(String)  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]
#23 :sparkles: 新建 com.feilong.xml.XmlUtil.getNodeAttributeValueAndStringValueMap(Object, String, String)   [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]
#17 :sparkles: 新建个通俗易懂的 xml util  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]


## [parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)

#2 :fire: git-commit-id-plugin 输出了两次  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#30 :fire: pom 中 删除 feilong-platform-repository  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#35  优化 maven-surefire-plugin 配置
#4  消除坏味道


## :arrow_up: [依赖升级](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖升级+milestone%3A3.0.0+is%3Aclosed)

#182  Commons Compress 升级到 1.20
#191  commons-codec 升级到 1.14
#167  commons-collection4 升级到4.4
#166  commons-lang3 升级到 3.10
#194  el-api 升级到3.0
#115  httpclient 升级到 4.5.12  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)]
#113  javax.mail  升级到 	1.6.2  [[feilong-email](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-email+milestone%3A3.0.0+is%3Aclosed)  [parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)]
#192  javax.servlet-api 升级到 3.1
#193  jsp-api 升级到 2.3  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)]
#130  maven-antrun-plugin 升级到 3.0.0  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)]
#131  maven-pmd-plugin 升级到 3.13.0  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)]
#195  ognl 升级到 3.2.14  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)]
#116  ooxml-schemas 升级到 1.4  [[feilong-excel](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-excel+milestone%3A3.0.0+is%3Aclosed)  [parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)]
#117  poi 升级到 4.1.2  [[jdk8](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajdk8+milestone%3A3.0.0+is%3Aclosed)  [parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)]
#132  spring的依赖升级到 4  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)]
#114  xstream 升级到 1.4.12  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)]
#187  使用 commons-digester3
#97  升级 com.github.ifeilong feilong common test 到4.0.0  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)]
#189  升级 maven-assembly-plugin 插件 到3.3.0
#104  升级到jdk8  [[jdk8](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Ajdk8+milestone%3A3.0.0+is%3Aclosed)  [parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)  [重要](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A重要+milestone%3A3.0.0+is%3Aclosed)]
#39  jsoup 升级到  1.13.1  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)]


## :heavy_minus_sign: [依赖调整](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖调整+milestone%3A3.0.0+is%3Aclosed)

#136  Xstream 依赖改成  optional
#145  commons-beanutils 比较老 又不升级, 看看能否废弃掉依赖  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)]
#143  commons-codec 设置为 optional
#141  commons-compress 做成 optional 依赖
#138  commons-disgester 依赖改成 optional 
#139  commons-net 做成optional 依赖
#140  commons-validator 做成可选依赖
#202  feilong excel 去除 spring-core 强依赖  [[feilong-excel](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-excel+milestone%3A3.0.0+is%3Aclosed)]
#98  feilong-office-excel-loxia poi 依赖改成 optional  [[feilong-excel](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-excel+milestone%3A3.0.0+is%3Aclosed)]
#137  httpclient 依赖改成 optional
#112 :fire: json-lib 打散进项目 删除 json-lib 依赖  [[feilong-json](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-json+milestone%3A3.0.0+is%3Aclosed)  [删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#124 :fire: xercesImpl 删除依赖  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)  [删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#109  依赖的jar 需要再精简  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)]
#123 :fire: 删除  artifactId>xml-apis依赖  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)  [删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#121 :fire: 删除  commons-httpclient 3 依赖  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)  [删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#120 :fire: 删除 com.github.github:site-maven-plugin  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)  [删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#181 :fire: 删除 commons-configuration 依赖  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#180 :fire: 删除 commons-configuration2 依赖  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)  [删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#200 :fire: 删除 commons-io 依赖  [[feilong-io](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-io+milestone%3A3.0.0+is%3Aclosed)  [删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#185 :fire: 删除 commons-lang  依赖  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)  [删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#204 :fire: 删除 commons-lang3 直接依赖  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#198 :fire: 删除 commons-validator 依赖  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#190 :fire: 删除 org.eclipse.m2e:lifecycle-mapping  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#165 :fire: 删除 spring-framework-bom 管理  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#203  打散 commons-collections4 到 feilong , 不直接依赖
#110 :sparkles: 提取 loxia excel完全打散 到feilong  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)  [feilong-excel](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-excel+milestone%3A3.0.0+is%3Aclosed)]
#122  添加 maven-gpg-plugin 插件  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.0+is%3Aclosed)]
#96  优化 feilong excel loxia 项目的jar 依赖  [[feilong-excel](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-excel+milestone%3A3.0.0+is%3Aclosed)]


## [其他](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A其他+milestone%3A3.0.0+is%3Aclosed)

#163 :fire: PinYinUtil 移动到solr 里面去  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#183  feilong-tools-parent 进行合并  [[结构调整](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A结构调整+milestone%3A3.0.0+is%3Aclosed)]
#164  feilong-velocity-extension 并入到 feilong-velocity
#142 :white_check_mark: 使用 org.hamcrest.MatcherAssert.assertThat 替代 org.junit.Assert.assertThat  [[Tests](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3ATests+milestone%3A3.0.0+is%3Aclosed)]
#169 :fire: 删除 ColorUtil  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#170 :fire: 删除 FontConstants  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#168 :fire: 删除 FontUtil  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#173 :fire: 删除 HtmlConstants  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#179 :fire: 删除 HttpHeader  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#171 :fire: 删除 ImageUtil  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#177 :fire: 删除 InetAddressUtil  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#174 :fire: 删除 ProcessUtil  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#175 :fire: 删除 RuntimeUtil  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#172 :fire: 删除 SpecialFolder  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#176 :fire: 删除 ThreadExtensionUtil  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#178 :fire: 删除 URLDownloadUtil  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#151 :fire: 删除 com.feilong.net.KeyStoreBuilder  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#119 :fire: 删除 distributionManagement github仓库 ,添加 nexus仓库  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)  [删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)]
#133 :fire: 删除 feilong-tools-iplookup  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)  [结构调整](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A结构调整+milestone%3A3.0.0+is%3Aclosed)]
#134 :fire: 删除 后端代码生成二维码功能  [[删除](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A删除+milestone%3A3.0.0+is%3Aclosed)  [结构调整](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A结构调整+milestone%3A3.0.0+is%3Aclosed)]
#92 :white_check_mark: feilong-component 添加maven 自动化测试  [[Tests](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3ATests+milestone%3A3.0.0+is%3Aclosed)]
#14 :sparkles: feilong-core-extension 打到 feilong-util-all  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]
#93 :white_check_mark: feilong-core-extension 添加maven 自动化测试  [[Tests](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3ATests+milestone%3A3.0.0+is%3Aclosed)]
#94 :white_check_mark: feilong-formatter 添加maven 自动化测试  [[Tests](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3ATests+milestone%3A3.0.0+is%3Aclosed)]
#28 :sparkles: feilong-office-excel-loxia 打入 all jar  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]
#32 :sparkles: feilong-tools-zip 择一 打进util all  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.0+is%3Aclosed)]
