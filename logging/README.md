# 日志等级
默认日志等级是 --info

全部等级：
Log Level: ERROR, WARN, INFO, DEBUG, or TRACE

# 开启方式
1、 运行参数
```yaml
java -jar myApp.jar --trace
java -jar myApp.jar --debug
```

# 三种日志等级设置的优先级别（从小到大）
3、logging.level.root=info
1、--info
2、info=ture


# logging.file 或 logging.path
1、两个同时设置，只有logging.file 生效

## logging.file
设置的可以是相对位置，也可以是绝对路径。

相对位置：
```yaml
logging.file=var.log
# or
logging.file=log/var.log
```

绝对路径：
```yaml
logging.file=/var.log  #window 会在C盘生成文件，如：C:/var.log
# or
logging.file=/log/var.log
# or window
logging.file=C:/var.log
```


## logging.path
logging.path 设置的可以是相对位置，也可以是绝对路径。

绝对路径路径示例：
````yaml
logging.path=/var/log
#or window
logging.path=C:/var/log
````
window: C:/var/log/spring.log<br>
mac or linux: /var/log/spring.log

相对路径示例：
```yaml
logging.path=var/log
```
项目根目录生成文件，如：${project_root}/var/log/spring.log



# logging.file.max-size 设置分卷大小
会自动压缩为：*.gz格式的压缩文件。<br>
默认单位是KB, 默认分卷大小：10MB。<br>
全部单位：KB,MB,GB,TB

# logging.file.max-history 翻滚保留的数量
默认是无限分卷。只能输入number类型的
同 logging.file.max-size设置没有任何关系 

spring boot logging 默认采用翻滚策略是天：`logFile + ".%d{yyyy-MM-dd}.%i.gz` 

代码：
```java
// org.springframework.boot.logging.logback.DefaultLogbackConfiguration.setRollingPolicy
class DefaultLogbackConfiguration{
  // ...
  private void setRollingPolicy(RollingFileAppender<ILoggingEvent> appender,
  			LogbackConfigurator config, String logFile) {
  		SizeAndTimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new SizeAndTimeBasedRollingPolicy<>();
  		rollingPolicy.setFileNamePattern(logFile + ".%d{yyyy-MM-dd}.%i.gz");
  		setMaxFileSize(rollingPolicy,
  				this.patterns.getProperty("logging.file.max-size", MAX_FILE_SIZE));
  		rollingPolicy.setMaxHistory(this.patterns.getProperty("logging.file.max-history",
  				Integer.class, CoreConstants.UNBOUND_HISTORY));
  		appender.setRollingPolicy(rollingPolicy);
  		rollingPolicy.setParent(appender);
  		config.start(rollingPolicy);
  	}
}

```

# Log Levels

logging.level.<logger-name>=<level>
level:TRACE, DEBUG, INFO, WARN, ERROR, FATAL, or OFF. 
logger-name:类路径，可以使用package设置整个包下的类使用同一个level

可以使用以下命令`logging.level.root.`配置 root logger
```yaml
logging: 
  level: 
    root: WARN # 设置之后，warn:true将失效
    org:
      springframework: 
        web: OFF
      hibernate: OFF
```

# Log Groups

能够将相关的日志记录程序分组在一起，以便能够同时对它们进行配置，这通常很有用。例如，您可能会更改所有与Tomcat相关的日志记录程序的日志级别，但是您无法轻松记住顶级包
```yaml
logging: 
  group: 
    tomcat: org.apache.catalina, org.apache.coyote, org.apache.tomcat
  level: 
    tomcat: TRACE
```
spring boot 预定义group

| Name | Loggers |
|-------|:-------------|
| web| org.springframework.core.codec, org.springframework.http,org.springframework.web|
| sql |  org.springframework.jdbc.core, org.hibernate.SQL |

 # Custom Log Configuration
 
 ##强制使用某个日志框架
 更改日志系统或完全禁用它的唯一方法是通过系统属性
 
 org.springframework.boot.logging.LoggingSystem=日志框架的完整类路径
 ```properties
 org.springframework.boot.logging.LoggingSystem=org.springframework.boot.logging.logback.LogbackLoggingSystem
```
如果不需要使用 spring boot looging configuration
```properties
 org.springframework.boot.logging.LoggingSystem=none
```
## 日志框架加载的文件
| Logging System | Customization |
|-------|:-------------|
| Logback |  logback-spring.xml, logbackspring.groovy, logback.xml, or logback.groovy|
| Log4j2 | log4j2-spring.xml or log4j2.xml |
| JDK (Java Util Logging) | logging.properties |
推荐使用 {name}-spring 的配置文件,这样spring boot 可控制log的初始化

## *-spring.xml 占位符
占位符使用Spring Boot的语法:${}
```yaml
app.name=MyApp
app.description=${app.name} is a Spring Boot application
```

## 默认值
你应该使用`:`作为属性名称和它的默认值之间的分隔符，而不是使用`:-`。

## MDC
通过仅覆盖`LOG_LEVEL_PATTERN`将`MDC`和其他临时内容添加到日志行
（或`Logback`的`logging.pattern.level`）。
例如，如果你使用`logging.pattern.level = user：％X {user}％5p`，则默认日志格式包含
`“user”`的`MDC`条目（如果存在）

```
2015-09-30 12:30:04.031 user:someone INFO 22174 --- [ nio-8080-exec-0] demo.Controller Handling authenticated request
```
 
 | Spring Environment | System Property | Comments |
 |-------|:-------------|:-------------|
 | logging.exceptionconversion-word | LOG_EXCEPTION_CONVERSION_WORD| The conversion word used when logging excep|
 | logging.file | LOG_FILE | If defined, it is used in the default log configuration |
 | logging.file.max-size | LOG_FILE_MAX_SIZE | Maximum log file size (if LOG_FILE enabled). (Only supported with the default Logback setup.)|
 | logging.path | LOG_PATH | If defined, it is used in the default log configuration.|
 | logging.pattern.console | CONSOLE_LOG_PATTERN | The log pattern to use on the console (stdout). (Only supported with the default Logback setup.) |
 | logging.pattern.dateformat | LOG_DATEFORMAT_PATTERN| Appender pattern for log date format. (Only supported with the default Logback setup.)|
 | logging.pattern.file | FILE_LOG_PATTERN | The log pattern to use in a file (if LOG_FILE is enabled). (Only supported with the default Logback setup.)|
 | logging.pattern.level | LOG_LEVEL_PATTERN | The format to use when rendering the log level (default %5p). (Only supported with the default Logback setup.)|
 | PID | PID | The current process ID (discovered if possible and  when not already defined as an OS environment variable).|
 
 
 # Logback Extensions
 
`logback-spring.xml` 可以使用高级扩展配置
 
 因为标准的`logback.xml`配置文件加载得太早，所以不能在其中使用扩展名。您需要使用`logback-spring.xml`或定义`logging.config`属性
 
 >Warning<br/>
 不能同 configuration Scanning 一起只用，然修改配置文件会出现错误
[configuration scanning](https://logback.qos.ch/manual/configuration.html#autoScan)
 ```
 ERROR in ch.qos.logback.core.joran.spi.Interpreter@4:71 - no applicable action for [springProperty], current ElementPath is [[configuration][springProperty]]
 ERROR in ch.qos.logback.core.joran.spi.Interpreter@4:71 - no applicable action for [springProfile], current ElementPath is [[configuration][springProfile]]
 ```
 
 ## Profile-specific Configuration
 
 <springProfile> 可以决定是加载此配置。需要与`spring.profiles.activ`的值匹配
 
 ```xml
<springProfile name="staging">
<!-- 配置开启  "staging"  -->
</springProfile>
<springProfile name="dev | staging">
<!-- 配置开启 "dev" or "staging"  -->
</springProfile>
<springProfile name="!production">
<!-- 配置开启不是 "production"  -->
</springProfile>
```
 
 ## Environment Properties
 
` <springProperty>` 获取`Environment`的值。使用方式同`property`
属性：
- `scope` 如果您需要将属性存储在`locak`范围之外的某个位置
- `source` `Environment` 名称。`source`必须使用`kebab case`格式指定(例如`my.property-name`)。
- `name` 定义属性的名称 使用的时候引用。占位符的概念
- `defaultValue` 如果环境不存在 `source`的默认值

```xml
<springProperty scope="context" name="fluentHost" source="myapp.fluentd.host"
defaultValue="localhost"/>
<appender name="FLUENT" class="ch.qos.logback.more.appenders.DataFluentAppender">
<remoteHost>${fluentHost}</remoteHost>
...
</appender>
```
