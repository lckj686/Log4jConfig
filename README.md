## android log4j  多文件输出配置（配置多个输出文件）

### 1、概述
log4j 是 Apache 出的一个java 打log 到本地的框架。 其中对于android的支持需要使用：android-logging-log4j，对其进行支持。log4j 在使用时可以配置不同的输出源，进行保存log，java的配置网上可以找到很多是通过xml进行配置的。然android 的log4j 需要通过代码设置，之前一直没找到示例，抽时间看了下源码，和api，找到了配置方式。

### 2、导包
 implementation 'log4j:log4j:1.2.17'
 implementation 'de.mindpipe.android:android-logging-log4j:1.0.3'
    
### 3、关于配置log4j
#### 3.1、基础配置（共享配置）

```
val logConfigurator = LogConfigurator(logPath + fileName)
logConfigurator.setLevel(root, Level.ALL)//设置等级
logConfigurator.filePattern = "%d{yyyy:MM:dd HH:mm:ss} -%p, %m%n"//格式
logConfigurator.maxBackupSize = 3//备份数量
logConfigurator.maxFileSize = (1024 * 1024 * 2).toLong()//每个输出文件设置为 2m
logConfigurator.isImmediateFlush = true//追加
logConfigurator.configure()

```
LogConfigurator配置是来自android-logging-log4j:1.0.3 包，是android 提供的配置，点开源码可以看到，配置里都是对Logger.getRootLogger()进行配置的，所以之前对获取两个对象 Logger.getLogger(fileName) ，分别进行configure()的配置都配置到了，rootLogger 启用的后一个，若设置了setInternalDebugging(false)，就是两个输出源都生效内容一样

```
	public void configure() {
		final Logger root = Logger.getRootLogger();
		
		if(isResetConfiguration()) {
			LogManager.getLoggerRepository().resetConfiguration();
		}

		LogLog.setInternalDebugging(isInternalDebugging());
		
		if(isUseFileAppender()) {
			configureFileAppender();
		}
		
		if(isUseLogCatAppender()) {
			configureLogCatAppender();
		}
		
		root.setLevel(getRootLevel());
	}
```

### 3.2 配置多文件（差异性多文件配置）
 - 1、取不同对象
差异性多文件配置首先要取到不同的logger，用Logger.getLogger(fileName)，Logger getLogger(Class clazz) 都可以。
 
 - 2、添加输出源
 logger对象里是可以直接**添加输出源**的，注意是**添加** 不会影响基础配置里的输出源。添加方法：

```
  private fun appender(fileName: String): Appender {
        val rollingFileAppender: RollingFileAppender
        val fileLayout = PatternLayout("%m%n")

        try {
            rollingFileAppender = RollingFileAppender(fileLayout, PathUtil.getAppLogPath(application) + fileName)
        } catch (e: IOException) {
            throw RuntimeException("Exception configuring log system", e)
        }
        rollingFileAppender.maxBackupIndex = 3
        rollingFileAppender.maximumFileSize = (1024 * 60).toLong()
        rollingFileAppender.immediateFlush = true

        return rollingFileAppender
    }

fun(){
	logger2.addAppender(appender("logger2.txt"))
}
```

### 3、补充说明
到这里其实，多配置文件就已经配置完了。补充一下，一些点。

#### 3.1、PatternLayout 表示输出格式

```
 * log4j.appender.A1.layout.ConversionPattern=%-4r %-5p %d{yyyy-MM-dd HH:mm:ssS} %c %m%n
 * 这里需要说明的就是日志信息格式中几个符号所代表的含义：
 * 　　         －X号: X信息输出时左对齐；
 * <p>
 * %p: 输出日志信息优先级，即DEBUG，INFO，WARN，ERROR，FATAL,
 * %d: 输出日志时间点的日期或时间，默认格式为ISO8601，也可以在其后指定格式，比如：%d{yyy MMM dd HH:mm:ss,SSS}，输出类似：2002年10月18日 22：10：28，921
 * %r: 输出自应用启动到输出该log信息耗费的毫秒数
 * %c: 输出日志信息所属的类目，通常就是所在类的全名
 * %t: 输出产生该日志事件的线程名
 * %l: 输出日志事件的发生位置，相当于%C.%M(%F:%L)的组合,包括类目名、发生的线程，以及在代码中的行数。举例：Testlog4.main(TestLog4.java:10)
 * %x: 输出和当前线程相关联的NDC(嵌套诊断环境),尤其用到像java servlets这样的多客户多线程的应用中。
 * %%: 输出一个"%"字符
 * %F: 输出日志消息产生时所在的文件名称
 * %L: 输出代码中的行号
 * %m: 输出代码中指定的消息,产生的日志具体信息
 * %n: 输出一个回车换行符，Windows平台为"\r\n"，Unix平台为"\n"输出日志信息换行
 * 可以在%与模式字符之间加上修饰符来控制其最小宽度、最大宽度、和文本的对齐方式。如：
 * 1)%20c：指定输出category的名称，最小的宽度是20，如果category的名称小于20的话，默认的情况下右对齐。
 * 2)%-20c:指定输出category的名称，最小的宽度是20，如果category的名称小于20的话，"-"号指定左对齐。
 * 3)%.30c:指定输出category的名称，最大的宽度是30，如果category的名称大于30的话，就会将左边多出的字符截掉，但小于30的话也不会有空格。
 * 4)%20.30c:如果category的名称小于20就补空格，并且右对齐，如果其名称长于30字符，就从左边交远销出的字符截掉。
 */
```

#### 3.2、Appender 表示的输出源策略
其中代码中使用的 RollingFileAppender，是回滚策略，默认的共享配置里也是用这个。还有其他策略，不过觉得这个好用，也是为什么一直用log4j打log的原因

```
rollingFileAppender.maxBackupIndex = 3
rollingFileAppender.maximumFileSize = (1024 * 60).toLong()
```
比如上例，设置了 输出文件的大小，当fileName文件超过60k时，就会把该文件的内容copy到fileName-1中，清空fileName文件继续写。再满的话，fileName->fileName-1->fileName-2->fileName-3 这个路径超过的把fileName-3的删了，fileName-2的移动到3中

### 4、附带代码示例
简单了写了个demo：
https://github.com/lckj686/Log4jConfig