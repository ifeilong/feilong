feilong 3.0.8 发布了，feilong 是一个让Java开发更简便的工具库。

1. 让你从大量重复的底层代码中脱身,提高开发效率;
1. 让你的代码`更简炼`，`易写`、`易读`、`易于维护`;

文档地址: http://feilong-core.mydoc.io/

`单元测试数` 增加至 `2259` 个, `单元测试覆盖率` 增加至 `91%` ,`javadoc` 比率 `83%`

本次升级共有 `9` 处变更, 具体参见  [3.0.8 milestone](https://github.com/ifeilong/feilong/milestone/10?closed=1)



## :peach: [feilong-core](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-core+milestone%3A3.0.8+is%3Aclosed)

#292  CollectionsUtil 新增 exist 方法

`boolean exist(Iterable<O> beanIterable,String propertyName,V propertyValue)`

判断iterable中,是否存在 propertyName属性名称值是 propertyValue 的元素.

**示例:**
场景: 判断 list中是否存在name是 关羽 的User对象

```JAVA
 List<User> list = new ArrayList<>();
 list.add(new User("张飞", 23));
 list.add(new User("关羽", 24));
 list.add(new User("刘备", 25));
 list.add(new User("关羽", 50));

 LOGGER.info(CollectionsUtil.exist(list, "name", "关羽"));
```
 
**返回:**
 true


## [feilong-net-filetransfer](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-net-filetransfer+milestone%3A3.0.8+is%3Aclosed)

#287  sftp 设置默认的 sshConfig
#286  sftp 默认超时时间从0 (没有超时) 改成 3秒
#288  完善  FileTransfer.getFileEntityMap(String, String...) javadoc
#285  简化 sftp 配置

原先 使用sftp 需要配置大段的xml:

```XML

	<bean id="sftpFileTransfer" class="com.feilong.net.filetransfer.sftp.SFTPFileTransfer" scope="prototype">
		<property name="sftpFileTransferConfig">
			<bean class="com.feilong.net.filetransfer.sftp.SFTPFileTransferConfig">
				<property name="hostName" value="#{p_sftp['fileTransfer.sftp.hostName']}" />
				<property name="userName" value="#{p_sftp['fileTransfer.sftp.userName']}" />
				<property name="password">
					<value><![CDATA[#{p_sftp['fileTransfer.sftp.password']}]]></value>
				</property>
				<property name="port" value="#{p_sftp['fileTransfer.sftp.port']}" />

				<property name="sessionTimeout" value="#{p_sftp['fileTransfer.sftp.sessionTimeout']}" />
				<property name="sshConfig" ref="p_sftp-sshConfig" />
			</bean>
		</property>
	</bean>
```


现在你只需要:

```XML

    <feilong:sftpFileTransfer id="sftpFileTransfer" hostName="#{p_sftp['fileTransfer.sftp.hostName']}"
                              userName="#{p_sftp['fileTransfer.sftp.userName']}"
                              password="#{p_sftp['fileTransfer.sftp.password']}"
                              port="#{p_sftp['fileTransfer.sftp.port']}"
                              sessionTimeout="8000"
    />

```


## :lock: [feilong-security](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Afeilong-security+milestone%3A3.0.8+is%3Aclosed)

#294 :sparkles: OnewayEncryption.encodeFile(OnewayType, String) 支持 classpath  [[enhancement](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aenhancement+milestone%3A3.0.8+is%3Aclosed)]


## [parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.8+is%3Aclosed)

#290  修改 sonar-maven-plugin 插件 groupid 从org.codehaus.mojo 到 org.sonarsource.scanner.maven


## :arrow_up: [依赖升级](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3A依赖升级+milestone%3A3.0.8+is%3Aclosed)

#296  maven-site-plugin 升级到 3.9.1  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.8+is%3Aclosed)]
#295  maven-war-plugin 升级到 3.3.1  [[parent](https://github.com/ifeilong/feilong/issues?q=is%3Aissue+label%3Aparent+milestone%3A3.0.8+is%3Aclosed)]
