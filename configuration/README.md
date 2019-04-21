Spring Boot使用一种非常特殊的PropertySource顺序，其设计目的是允许合理地覆盖值。属性按以下顺序考虑
从高到底
1. Devtools global settings properties on your home directory (`/.spring-bootdevtools.properties` when devtools is active).
2. @TestPropertySource annotations on your tests.
3. `properties` attribute on your tests. Available on @SpringBootTest and the test annotations for
testing a particular slice of your application.
4. Command line arguments.
5. Properties from `SPRING_APPLICATION_JSON` (inline JSON embedded in an environment variable
or system property).
6. `ServletConfig` init parameters.
7. `ServletContext` init parameters.
8. JNDI attributes from `java:comp/env`.
9. Java System properties (`System.getProperties()`).
10. OS environment variables.
11. A `RandomValuePropertySource` that has properties only in `random.*.`
12. Profile-specific application properties outside of your packaged jar (`application-
{profile}.properties` and YAML variants).
13. Profile-specific application properties packaged inside your jar (`application-
{profile}.properties` and YAML variants).
14. Application properties outside of your packaged jar (`application.properties` and YAML
variants).
15. Application properties packaged inside your jar (`application.properties` and YAML variants).
16. @PropertySource annotations on your `@Configuration` classes.
17. Default properties (specified by setting `SpringApplication.setDefaultProperties`).

# 命令行覆盖参数
`java -jar app.jar --test.name="Spring"`

```
SPRING_APPLICATION_JSON='{"acme":{"name":"test"}}' java -jar myapp.jar
```

您还可以将`spring.application.json`作为系统属性中的json
```
java -Dspring.application.json='{"acme":{"name":"test"}}' -jar myapp.jar
```
命令行参数
```
java -jar myapp.jar --spring.application.json='{"name":"test"}'
```
JNDI 变量
```
java:comp/env/spring.application.json
```

# 加载application.properties 或application.yml位置顺序
列表按优先级排序(在列表中较高位置定义的属性覆盖在较低位置定义的属性)。
1. A `/config` subdirectory of the current directory
2. The current directory
3. A classpath `/config` package
4. The classpath root

# 修改Spring boot 配置文件名称
## spring.config.name 
修改配置的文件名称
```
$ java -jar myproject.jar --spring.config.name=myproject
```

## spring.config.location
修改配置文件的位置，(它是由逗号分隔的目录位置或文件路径列表)。
```
java -jar myproject.jar --spring.config.location=classpath:/default.properties,classpath:/override.properties
```
### "/" 追加文件
如果是 `spring.config.location`是以“/”结束，那么配置文件的的路径则是
`spring.config.location`+`spring.config.name`<br>
or<br>
`spring.config.location`+`application.properties`


### 默认路径
当使用`spring.config.location`自定义配置位置时。它们替换默认位置
```
classpath:/,classpath:/config/,file:./,file:./config/
```
配置位置以相反的顺序搜索，如下
1. file:./config/
2. file:./
3. classpath:/config/
4. classpath:/

### spring.config.additional-location 增加配置路径
在默认位置之前搜索`additional-location`

```
classpath:/customconfig/,file:./custom-config/
```
搜寻次序如下
```
1. file:./custom-config/
2. classpath:custom-config/
3. file:./config/
4. file:./
5. classpath:/config/
6. classpath:/
```
这种搜索顺序允许您在一个配置文件中指定默认值，然后在另一个配置文件中选择性地覆盖这些值

>Note<br>
如果您的应用程序在容器中运行，则可以使用JNDI属性(在`java:comp/env`中)或servlet上下文初始化参数来代替环境变量或系统属性，或者同时使用环境变量或系统属性

>WARNING<br>
>spring.config.name,spring.config.location在很早以前就用于确定必须加载哪些文件，因此必须将它们定义为环境属性(通常是
操作系统环境变量、系统属性或命令行参数)

# Profile-specific Properties

默认的 Profile-specific 是"default"

**如果指定了多个配置文件，则应用“最后胜出(last-wins)”策略**。例如：
spring.profiles.active 属性指定的概要文件是在通过SpringApplication API配置的概要文件之后添加的，因此具有优先级。

Profile-specific 文件是从application.properties相同的位置加载。不管文件是打包jar还是外部jar,Profile-specific文件总是覆盖non-specific文件。

## spring.config.location

spring.config.location 只有指定目录的时候，才能使用此特性，否则不支持此特性
>Note<br>
 If you have specified any files in spring.config.location, profile-specific variants of those
 files are not considered. Use directories in spring.config.location if you want to also use
 profile-specific properties.

# Placeholders（占位符） in Properties

```
app.name=MyApp
app.description=${app.name} is a Spring Boot application
```
也可用来制作短命令行,--port=9000 而不是 --server.port=9000
```
server.port=${port:8080}
```

# Encrypting（加密） Properties

Spring Boot不提供任何内置的对属性值加密的支持，但是它提供了修改Spring环境中包含的值所必需的挂钩点

`EnvironmentPostProcessor`接口允许您在应用程序启动之前操作 `Environment`， See Section 76.3, “Customize the Environment or ApplicationContext Before It Starts”
for details.

如果您正在寻找存储凭证和密码的安全方法, [Spring Cloud Vault](https://cloud.spring.io/spring-cloud-vault/)项目支持在[HashiCorp Vault](https://www.vaultproject.io/)中存储外部配置。


# 使用YAML 而不是 Properties
YAML是JSON的一个超集，因此是指定分层配置数据的一种方便的格式

如果你classpath 中配置了[SnakeYAML](http://www.snakeyaml.org/)，SpringApplication自动支持YAML

>Note<br>
如果您使用“starter”，SnakeYAML将由spring-boot-starter自动提供。

spring 提供两个类方便处理YAML<br>
&nbsp;&nbsp;&nbsp;&nbsp;YamlPropertiesFactoryBean 加载 YAML 为 Properties<br>
&nbsp;&nbsp;&nbsp;&nbsp;YamlMapFactoryBean 加载YAML 为 Map

## 使用List or Set,必须需要初始化，
```yaml
my:
  servers:
    - dev.example.com
    - another.example.com
```

```java
@ConfigurationProperties(prefix="my")
public class Config {
    private List<String> servers = new ArrayList<String>();
    public List<String> getServers() {
        return this.servers;
    }
}
```

YamlPropertySourceLoader类可用于在Spring环境中将YAML公开为PropertySource。这样做可以使用@Value注释和占位符语法访问YAML属性

## YAML的缺点
无法使用@PropertySource注释加载YAML文件。因此，在需要以这种方式加载值的情况下，需要使用属性文件

# 

## @Value
@Value（“$ {property}”）

## @ConfigurationProperties

```java
package com.example;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties("acme")
public class AcmeProperties {
    private boolean enabled;
    private InetAddress remoteAddress;
    private final Security security = new Security();
    
    public boolean isEnabled() { ... }
    public void setEnabled(boolean enabled) { ... }
    public InetAddress getRemoteAddress() { ... }
    public void setRemoteAddress(InetAddress remoteAddress) { ... }
    public Security getSecurity() { ... }
   
    public static class Security {
        private String username;
        private String password;
        private List<String> roles = new ArrayList<>(Collections.singleton("USER"));
       
        public String getUsername() { ... }
        public void setUsername(String username) { ... }
        public String getPassword() { ... }
        public void setPassword(String password) { ... }
        public List<String> getRoles() { ... }
        public void setRoles(List<String> roles) { ... }
    }
}

```
如果初始化嵌套POJO属性(就像前面例子中的Security字段)，不需要setter,如果希望绑定器使用其默认值动态创建实例构造函数，你需要一个setter

如果您初始化集合，请确保它不是不可变的

有些人使用Project Lombok自动添加getter和setter。确保Lombok不会为这样的类型生成任何特定的构造函数，因为容器会自动使用它来实例化对象

只考虑标准Java Bean属性，不支持绑定静态属性

## @EnableConfigurationProperties
@ConfigurationProperties 以这样的方式注册
bean有一个常规名称：`<prefix>-<fqn>`，其中`<prefix>`是@ConfigurationProperties在注释中指定的环境键前缀，`<fqn>`是完全限定名称。 如果注释没有提供任何前缀，则只有bean的完全限定名称

上面示例中的bean名称是acme-com.example.AcmeProperties

### 推荐使用
要使用@ConfigurationProperties 创建的 bean，您可以使用与任何其他bean相同的方式注入它们
豆

```java
@Component
@ConfigurationProperties(prefix="acme")
public class AcmeProperties {
// ... see the preceding example
}
```

```java
@Configuration
@ConfigurationProperties(prefix="acme")
public class AcmeProperties {
// ... see the preceding example
}
```

## Third-party Configuration

除了使用@ConfigurationProperties注释类之外，您还可以在公共@Bean方法上使用它。 当您想要将属性绑定到控件之外的第三方组件时，这样做会特别有用。

要从Environment属性配置bean，请将@ConfigurationProperties添加到其bean注册中
```java
@ConfigurationProperties(prefix = "another")
@Bean
public AnotherComponent anotherComponent() {
    ...
}
```

## Relaxed Binding

| Property   |      Note      | 
|----------|:-------------|
| acme.my-project.person.first-name |  kebab-case（短横线命名）建议在.properties和.yml文件中使用。 |
| acme.myProject.person.firstName |    标准的camelCased (驼峰式) 语法   | 
| acme.my_project.person.first_name  | 下划线表示法，它是在.properties和.yml文件中使用的替代格式。 |
| ACME_MYPROJECT_PERSON_FIRSTNAME  | Upper case 格式，使用系统环境变量时建议使用。 |

`prefix` 必须是kebab-case（小写并以-分隔，例如acme.my-project.person）。

Relaxed Binding 每个属性源的绑定规则

| Property Source   |      Simple      |       List      | 
|----------|:-------------|:-------------| 
| Properties Files | Camel case, kebab case, or underscore notation | 使用`[]`或`,`分隔值的标准列表语法 |
| YAML Files | Camel case, kebab case, or underscore notation |  标准YAML列表语法或`.`分隔值 |
| Environment Variables| Upper case 格式以下划线作为分隔符。不应在属性名中使用`_` | 下划线包围的数值，比如：`MY_ACME_1_OTHER =my.acme[1].other` |
| System properties | Camel case, kebab case, or underscore notation | 使用`[]`或`,`分隔值的标准列表语法 |

>**Tip**<br>
我们推荐使用 lower-case kebab 格式，比如:`my.property-name=acme`

### Map
绑定到Map属性时，如果键包含除小写字母数字字符以外的任何内容或 - ，则需要使用括号表示法以保留原始值。 如果密钥未被[]包围，则删除任何非字母数字或字符的字符
```yaml
acme:
    map:
        "[/key1]": value1
        "[/key2]": value2
        /key3: value3
```

上面的属性将绑定到带有/key1，/key2和key3的Map作为Map中的键。

### 合并复杂类型 
当列表在多个位置配置时，通过替换整个列表覆盖工作
```java
@ConfigurationProperties("acme")
public class AcmeProperties {
    private final List<MyPojo> list = new ArrayList<>();
    public List<MyPojo> getList() {
        return this.list;
    }
}
```
```yaml
acme:
    list:
        - name: my name
        description: my description
---
spring:
    profiles: dev
acme:
    list:
      - name: my another name
```
上面的配置中，如果 dev profile is active，那么list中，只有一个，name是：my another name，description：是null

```java
@ConfigurationProperties("acme")
public class AcmeProperties {
    private final Map<String, MyPojo> map = new HashMap<>();
    public Map<String, MyPojo> getMap() {
        return this.map;
    }
}
```
```yaml
acme:
	map:
		key1:
			name: my name 1
			description: my description 1
---
spring:
	profiles: dev
acme:
	map:
		key1:
			name: dev name 1
		key2:
			name: dev name 2
			description: dev description 2
```
上面的配置中，如果 dev profile is active。map中key只有一个，key:key1。如果dev is not active,那么map中有一个key,分别是key1,key2


前面的合并规则适用于来自所有属性源的属性，而不仅仅是YAML文件。

## Properties Conversion

### Converting durations

- 一个常规的`long`表示(使用毫秒作为默认单位，除非@DurationUnit有)
被指定的)
- java.util.Duration使用的标准ISO-8601格式

```java
@ConfigurationProperties("app.system")
public class AppSystemProperties {
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration sessionTimeout = Duration.ofSeconds(30);
    private Duration readTimeout = Duration.ofMillis(1000);
    public Duration getSessionTimeout() {
    return this.sessionTimeout;
    }
    public void setSessionTimeout(Duration sessionTimeout) {
    this.sessionTimeout = sessionTimeout;
    }
    public Duration getReadTimeout() {
    return this.readTimeout;
    }
    public void setReadTimeout(Duration readTimeout) {
    this.readTimeout = readTimeout;
    }
}
```

您也可以使用任何支持的单位：
- ns for nanoseconds
- us for microseconds
- ms for milliseconds
- s for seconds
- m for minutes
- h for hours
- d for days

### Converting Data Sizes
- 一个常规的`long`表示形式(使用字节作为默认单位，除非@DataSizeUnit已经存在
  指定)
- 一种更易读的格式，其中值和单元耦合（例如10MB表示10兆字节）

```java
@ConfigurationProperties("app.io")
public class AppIoProperties {
    @DataSizeUnit(DataUnit.MEGABYTES)
    private DataSize bufferSize = DataSize.ofMegabytes(2);
    private DataSize sizeThreshold = DataSize.ofBytes(512);
    public DataSize getBufferSize() {
    return this.bufferSize;
    }
    public void setBufferSize(DataSize bufferSize) {
    this.bufferSize = bufferSize;
    }
    public DataSize getSizeThreshold() {
    return this.sizeThreshold;
    }
    public void setSizeThreshold(DataSize sizeThreshold) {
    this.sizeThreshold = sizeThreshold;
    }
}
```

要指定10兆字节的缓冲区大小，10和10MB是等效的。大小阈值256字节可以指定为256或256B

您也可以使用任何支持的单位:
- B for bytes
- KB for kilobytes
- MB for megabytes
- GB for gigabytes
- TB for terabytes

# @ConfigurationProperties vs. @Value

| Feature | @ConfigurationProperties | @Value | 
|-------|:-------------|:-------------|
| Relaxed binding | Yes | No |
| Meta-data support | Yes | No |
| SpEL evaluation | No | Yes |

   
   
   