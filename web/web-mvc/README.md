springboot-starter-web
spring-boot-starter-webflux


# The “Spring Web MVC Framework”
@Controller or @RestController bean来处理传入的HTTP请求

@RequestMapping 控制器中的方法通过使用映射到HTTP @RequestMapping注释

## Spring MVC Auto-configuration

Auto-configuration 默认的特性
- 包含`ContentNegotiatingViewResolve`r和`BeanNameViewResolver` bean
- 支持为静态资源提供服务，包括对webjar的支持(本文档后面将介绍)
- 自动注册`Converter`，`GenericConverter`和`Formatter` bean
- 支持`HttpMessageConverters`（本文后面会介绍）
- 自动注册`MessageCodesResolver`（本文后面会介绍）
- 静态`index.html`支持。
- 自定义Favicon支持（本文档稍后介绍）
- 自动使用`ConfigurableWebBindingInitializer` bean（本文稍后介绍）。

如果您想保留Spring Boot MVC功能，并且想要添加其他[MVC Configuation](https://docs.spring.io/spring/docs/5.1.5.RELEASE/spring-framework-reference/web.html#mvc)(`interceptors`, `formatters`, `view controllers`, and other features),您可以添加自己的@Configuration类，类型为`WebMvcConfigurer`，但不包含`@EnableWebMvc`。

[Spring MVC实现HTTP协议的LastModified缓存的两种方法与原理](https://blog.csdn.net/iwts_24/article/details/84575045) 

如果您想完全控制Spring MVC，您可以添加自己的@Configuration，并使用@EnableWebMvc进行注释。

## HttpMessageConverters

## Custom JSON Serializers and Deserializers

@JsonComponent 自定义 Serializers and Deserializers

Spring Boot还提供了JsonObjectSerializer和JsonObjectDeserializer基类
在序列化对象时，这为标准Jackson版本提供了有用的替代方案

## MessageCodesResolver
`spring.mvc.message-codes-resolver.format` 设置属性 `PREFIX_ERROR_CODE` or `POSTFIX_ERROR_CODE`

## Static Content
静态资源路径： /static (或者 /public 或者 / resources 或者 /META-INF/resources)。来自于：ResourceHttpRequestHandler

或者 使用 自己的`WebMvcConfigurer` 覆盖 `addResourceHandlers` 方法

默认的资源是 /**，则可以使用 `spring.mvc.staticpath-pattern` 将所有资源设置到 `/resources/**`
```properties
spring.mvc.static-path-pattern=/resources/**
```

spring.resources.staticlocations 添加多个静态资源的路径。root Servlet context path, "/"自动添加到静态资源路径前

```properties
spring.resources.staticlocations[0]=/stage/**
spring.resources.staticlocations[1]=test/**
```

### Webjars

访问路径：/webjars/**

添加`webjars-locator-core` 可不使用 version 版本的访问

>Note<br>
部署到jboss容器中，需要使用`webjars-locator-jboss-vfs`依赖 

#### 浏览器缓存解决方案
解决静态资源修改之后，浏览器缓存不刷新的问题，采用 content生成一个hash值

```properties
spring.resources.chain.strategy.content.enabled=true
spring.resources.chain.strategy.content.paths=/**
```
示例：
```html
<link href="/css/spring-2a2d595e6ed9a0b24f027f2b63b134d6.css"/>
```
>Note<br>
由于为`Thymeleaf`和`FreeMarker`自动配置了`ResourceUrlEncodingFilter`，因此在运行时可以在模板中重写资源链接。 您应该在使用JSP时手动声明此过滤器。 目前不支持其他模板引擎，但可以使用自定义 macros/helpers 以及ResourceUrlProvider的使用。


固定版本：
```properties
spring.resources.chain.strategy.content.enabled=true
spring.resources.chain.strategy.content.paths=/**
spring.resources.chain.strategy.fixed.enabled=true
spring.resources.chain.strategy.fixed.paths=/js/lib/
spring.resources.chain.strategy.fixed.version=v12
```

通过这种配置，位于“/js/lib/”下的JavaScript模块使用固定的版本控制策略(“`/v12/js/lib/mymodule.js`”)，而其他资源仍然使用内容hash策略(`<link href="/css/spring-2a2d595e6ed9a0b24f027f2b63b134d6.css"/>`)。

>更多介绍<br>
1、[Spring Framework 4.1 - handling static web resources](https://spring.io/blog/2014/07/24/spring-framework-4-1-handling-static-web-resources)<br/>
2、[mvc-config-static-resources](https://docs.spring.io/spring/docs/5.1.5.RELEASE/spring-framework-reference/web.html#mvc-config-static-resources)<br/>
3、[示例](https://www.cnblogs.com/iiot/p/9647653.html)

更多[ResourceProperties](https://github.com/spring-projects/spring-boot/blob/v2.1.3.RELEASE/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/web/ResourceProperties.java) 选项配置

#### 依赖资源
可添加依赖的资源：[Webjars content](https://www.webjars.org/)

#### 自定义资源

## Welcome Page

首先在 Static Content 查找“`index.html`”是否存在，其次，查看`index`模板是否存在

## Custom Favicon
在静态内容目录 favicon.ico

## Path Matching and Content Negotiation
默认的不支持后缀模式匹配

### 后缀模式匹配
```
GET /projects/spring-boot.json
```
无法匹配`@GetMapping("/projects/spring-boot")` 的mapping

开启后缀模式匹配
```properties
spring.mvc.contentnegotiation.favor-path-extension=true
spring.mvc.pathmatch.use-suffix-pattern=true
```

### 后缀模式优化
1、"`GET /projects/spring-boot?format=json`" 将请求修改为parameter

2、
```properties
#开启 内容呢协商 参数
spring.mvc.contentnegotiation.favor-parameter=true

# url 参数协商的方式，可以修改参数名称，默认是 format
# spring.mvc.contentnegotiation.parameter-name=myparam

# 新增一种 extensions/media 类型
spring.mvc.contentnegotiation.media-types.markdown=text/markdown
```

>[Content Negotiation using Spring MVC](https://blog.csdn.net/u012410733/article/details/78536656)

另外，与其打开所有后缀模式，只支持已注册的后缀模式更安全

```properties
spring.mvc.contentnegotiation.favor-path-extension=true
spring.mvc.pathmatch.use-registered-suffix-pattern=true

# 还可以注册其他文件"extensions/media" 类型 
# spring.mvc.contentnegotiation.media-types.adoc=text/asciidoc
```

## ConfigurableWebBindingInitializer

Spring MVC使用`WebBindingInitializer`为特定请求初始化`WebDataBinder`。如果您创建自己的`ConfigurableWebBindingInitializer` `@Bean`，Spring Boot会自动配置Spring MVC以使用它。

## Template Engines

除了 REST web 服务外，spring MVC 支持动态的html内容，支持的模板技术：Thymeleaf, FreeMarker, and JSPs等
spring boot 支持的自动配置的模板技术：
- [FreeMarker](https://freemarker.apache.org/docs/)
- [Groovy](http://docs.groovy-lang.org/docs/next/html/documentation/template-engines.html#_the_markuptemplateengine)
- [Thymeleaf](http://www.thymeleaf.org/)
- [Mustache](https://mustache.github.io/)

>Tip<br>
如果可能，应该避免jsp。在与嵌入式servlet容器一起使用时，有几个已知的[限制]()。

当您使用这些带有默认配置的模板引擎之一时，您的模板将自动从`src/main/resources/templates`中获取

>Tip<br>
根据您运行应用程序的方式，IntelliJ IDEA以不同方式对类路径进行排序。 从主方法在IDE中运行应用程序会导致与使用Maven或Gradle或其打包的jar运行应用程序时的顺序不同。 这可能导致Spring Boot无法在类路径中找到模板。 如果遇到此问题，可以在IDE中重新排序类路径，以便首先放置模块的类和资源。 或者，您可以配置模板前缀以搜索类路径上的每个`templates`目录，如下所示：
```yaml
spring:
  thymeleaf:
    prefix: classpath*:/templates/
```
> FreeMarker 模板文件的目录 路径不受 `spring.mvc.static-path-pattern=/resources/**` 配置的控制，



## Error Handling

默认情况下，Spring Boot提供`/error`映射，以合理的方式处理所有错误，并在servlet容器中注册为“global”错误页面。 对于计算机客户端，它会生成一个JSON响应，其中包含错误，HTTP状态和异常消息的详细信息。 对于浏览器客户端，有一个“whitelabel”错误视图，以HTML格式呈现相同的数据（要自定义它，添加`view`解析为`error`）。 要完全替换默认行为，可以实现`ErrorController`并注册该类型的`bean`定义，或者添加`ErrorAttributes`类型的`bean`以使用现有机制但替换内容。

>Tip<br>
`BasicErrorController`可以用作自定义`ErrorController`的基类。如果您想为新内容类型添加一个处理程序(缺省值是专门处理`text/html`，并为其他所有内容提供一个回退)，这尤其有用。为此，扩展`BasicErrorController`，添加一个带有`@RequestMapping`的公共方法，该方法具有`produces`属性，并创建一个新类型的`bean`。

您还可以定义一个带有`@ControllerAdvice`注释的类来定制JSON文档，以返回特定的控制器和/或异常类型，如下面的示例所示:
```java
@ControllerAdvice(basePackageClasses = AcmeController.class)
public class AcmeControllerAdvice extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(YourException.class)
    @ResponseBody
    ResponseEntity<?> handleControllerException(HttpServletRequest request, Throwable ex) {
        HttpStatus status = getStatus(request);
        return new ResponseEntity<>(new CustomErrorType(status.value(), ex.getMessage()), status);
    }
    
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
```

在前面的示例中，如果在与`AcmeController`相同的包中定义的控制器抛出`YourException`，则使用`CustomErrorType` POJO的JSON表示而不是`ErrorAttributes`表示

### Custom Error Pages

如果要显示给定状态代码的自定义HTML错误页面，可以将文件添加到`/error`文件夹。 错误页面可以是静态HTML（即，添加到任何静态资源文件夹下），也可以使用模板构建。 文件名应该是确切的状态代码或系列掩码

404 文件夹机构
```text
src/
    +- main/
        +- java/
        | + <source code>
        +- resources/
            +- public/
                +- error/
                | +- 404.html
                +- <other public assets>
```

5xx 状态码，使用 FreeMarker template
```text
src/
    +- main/
        +- java/
        | + <source code>
        +- resources/
            +- templates/
                +- error/
                | +- 5xx.ftl
                +- <other templates>
```
其他复杂的mapping ,可以定义beans实现了`ErrorViewResolver`接口

```java
public class MyErrorViewResolver implements ErrorViewResolver {
    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request,
    HttpStatus status, Map<String, Object> model) {
       // Use the request or status to optionally return a ModelAndView
       return new ModelAndView();
    }
}
```

先使用 `@ExceptionHandler`方法和`@ControllerAdvice`处理指定或者确定的异常。 然后，`ErrorController`将获取任何未处理的异常。



### Mapping Error Pages outside of Spring MVC

## Spring HATEOAS

如果您开发使用超媒体的RESTful AP,Spring Boot为Spring HATEOAS提供自动配置,适用于大多数应用程序。自动配置取代了使用`@EnableHypermediaSupport`的需要，并注册了许多bean以简化构建基于超媒体的应用程序，包括`LinkDiscoverers`（用于客户端支持）和一个`ObjectMapper`

ObjectMapper 同设置各种`spring.jackson.*` 属性 或者通过`Jackson2ObjectMapperBuilder`来定制

您可以使用`@EnableHypermediaSupport`控制Spring HATEOAS的配置。 请注意，这样做会禁用前面描述的`ObjectMapper`自定义。

## CORS Support
[Cross-origin resource sharing](https://en.wikipedia.org/wiki/Cross-origin_resource_sharing)（CORS）是大多数[浏览器](https://caniuse.com/#feat=cors)实现的[W3C specification](https://www.w3.org/TR/cors/)，允许您以灵活的方式指定授权何种跨域请求，而不是使用一些安全性较低且功能较弱的方法，如IFRAME或JSONP

从版本4.2开始，Spring MVC[支持CORS](https://docs.spring.io/spring/docs/5.1.5.RELEASE/spring-framework-reference/web.html#mvc-cors)。 

### method 配置
在Spring Boot应用程序中使用带有[@CrossOrigin](https://docs.spring.io/spring/docs/5.1.5.RELEASE/spring-framework-reference/web.html#mvc-cors-controller)注解的控制器方法CORS配置不需要任何特定配置。
 
 ### 全局配置
 可以通过使用自定义的`addCorsMappings（CorsRegistry）`方法注册[WebMvcConfigurer](https://docs.spring.io/spring/docs/5.1.5.RELEASE/spring-framework-reference/web.html#mvc-cors-global) bean来定义全局CORS配置，如以下示例所示：
```java
@Configuration
public class MyConfiguration {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**");
            }
        };
    }
}
```