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
1、--info
2、info=ture
3、logging.level.root=info


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
|-------|:-------------:|
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
|-------|:-------------:|
| Logback |  logback-spring.xml, logbackspring.groovy, logback.xml, or logback.groovy|
| Log4j2 | log4j2-spring.xml or log4j2.xml |
| JDK (Java Util Logging) | logging.properties |
推荐使用 {name}-spring 的配置文件,这样spring boot 可控制log的初始化


 
 