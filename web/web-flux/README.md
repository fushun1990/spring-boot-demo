
# The “Spring WebFlux Framework”
&nbsp;&nbsp;&nbsp;&nbsp;Spring WebFlux是Spring framework 5.0中引入的新的反应性web框架。与Spring MVC不同，它不需要Servlet API，完全异步且非阻塞， and implements the Reactive Streams specification through the Reactor project.

&nbsp;&nbsp;&nbsp;&nbsp;Spring WebFlux有两种风格:功能性和基于注释的。基于注解的模式非常接近Spring MVC模式，如下例所示:

```java
public class MyRestController {
    public Mono<User> getUser( Long user) {
    // ...
    }
    public Flux<Customer> getUserCustomers( Long user) {
    // ...
    }
    public Mono<User> deleteUser( Long user) {
    // ...
    }
}
```

“WebFlux。功能变体fn”将路由配置与请求的实际处理分开，如下例所示:
```java
public class RoutingConfiguration {
    public RouterFunction<ServerResponse> monoRouterFunction(UserHandler userHandler) {
        return route(GET("/{user}").and(accept(APPLICATION_JSON)), userHandler::getUser)
        .andRoute(GET("/{user}/customers").and(accept(APPLICATION_JSON)), userHandler::getUserCustomers)
        .andRoute(DELETE("/{user}").and(accept(APPLICATION_JSON)), userHandler::deleteUser);
    }
}
public class UserHandler {
    public Mono<ServerResponse> getUser(ServerRequest request) {
    // ...
    }
    public Mono<ServerResponse> getUserCustomers(ServerRequest request) {
    // ...
    }
    public Mono<ServerResponse> deleteUser(ServerRequest request) {
    // ...
    }
}
```

WebFlux是Spring框架的一部分,其[参考文档](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux)中提供了详细的信息。

>Tip<br>
>您可以定义任意数量的RouterFunction bean来模块化路由器的定义。如果需要应用优先级，可以对bean进行排序。

首先，将`spring-boot-starter-webflux`模块添加到您的应用程序中。

>Note<br>
>在您的应用程序中同时添加`Spring-Boot-starter-web`和`Spring-Boot-starter-WebFlux`模块将导致Spring Boot自动配置Spring MVC，而不是WebFlux。之所以选择这种行为，是因为许多Spring开发人员将`Spring-bootstarter-webflux`添加到他们的Spring MVC应用程序中，以使用反应性web客户机。您仍然可以通过将所选的应用程序类型设置为`SpringApplication.setWebApplicationType(WebApplicationType.REACTIVE)`来强制执行您的选择。

## Spring WebFlux Auto-configuration

Spring Boot为Spring WebFlux提供了自动配置，这在大多数应用程序中都能很好地工作。自动配置在Spring的默认值之上添加了以下特性:
- 为HttpMessageReader和HttpMessageWriter实例配置编解码器(稍后将在本文档中描述)。
- 支持为静态资源提供服务，包括对webjar的支持(在本文后面进行了描述)。

如果您想保持Spring Boot WebFlux特性，并且想添加额外的WebFlux配置，您可以添加自己的`@Configuration`类，类型为`WebFluxConfigurer`，但是不需要`@EnableWebFlux`。

如果您想完全控制Spring WebFlux，您可以添加您自己的`@Configuration`，注释为`@EnableWebFlux`。

## HTTP Codecs with HttpMessageReaders and HttpMessageWriters

Spring WebFlux使用`HttpMessageReader`和`HttpMessageWriter`接口来转换HTTP请求和响应。它们配置了`CodecConfigurer`，通过查看类路径中可用的库，可以得到合理的默认值。

Spring Boot通过使用CodecCustomizer实例应用进一步的定制。例如,spring.jackson。配置密钥应用于Jackson编解码器

如果需要添加或自定义编解码器，可以创建自定义编解码器组件，如下例所示:
```java
import org.springframework.boot.web.codec.CodecCustomizer;
    public class MyConfiguration {
    public CodecCustomizer myCodecCustomizer() {
        return codecConfigurer -> {
        // ...
        };
    }
}
```
您还可以利用Boot的 custom JSON serializers and deserializers

## Static Content

默认情况下，Spring Boot从类路径中名为`/static`(或`/public`或`/resources`或`/META-INF/resources`)的目录中提供静态内容。它使用来自Spring WebFlux的`ResourceWebHandler`，因此您可以通过添加自己的`WebFluxConfigurer`并覆盖`addResourceHandlers`方法来修改该行为。

默认情况下，资源映射在`/**`上，但是您可以通过设置`spring.webflux.static-path-pattern`属性对其进行调优。例如，将所有资源重新分配到`/resources/**`可以实现以下目标:
```properties
spring.webflux.static-path-pattern=/resources/**
```

您还可以使用`spring.resources.staticlocations`定制静态资源位置。这样做将用目录位置列表替换默认值。如果您这样做，默认的欢迎页面检测将切换到您的自定义位置。因此，如果在启动时在任何位置都有index.html，那么它就是应用程序的主页。

除了前面列出的“标准”静态资源位置之外，webjar内容还有一个特殊的情况。如果以webjars格式打包，那么/webjars/**中有路径的任何资源都可以从jar文件中获得。

>Tip<br>
>Spring WebFlux应用程序并不严格依赖Servlet API，因此它们不能作为war文件部署，也不使用`src/main/webapp`目录。

## Template Engines
除了REST web服务，您还可以使用Spring WebFlux来提供动态HTML内容。Spring WebFlux支持多种模板技术，包括Thymeleaf、FreeMarker和Mustache。

Spring Boot包含对以下模板引擎的自动配置支持:
- [FreeMarker](https://freemarker.apache.org/docs/)
- [Thymeleaf](https://www.thymeleaf.org/)
- [Mustache](https://mustache.github.io/)

当您使用这些带有默认配置的模板引擎之一时，您的模板将自动从`src/main/resources/templates`中获取。

## Error Handling
Spring Boot提供了一个WebExceptionHandler，它以一种合理的方式处理所有错误。它在处理顺序中的位置紧接在WebFlux提供的处理程序之前，后者被认为是最后一个处理程序。对于机器客户机，它生成一个JSON响应，其中包含错误、HTTP状态和异常消息的详细信息。对于浏览器客户机，有一个“whitelabel”错误处理程序，它以HTML格式呈现相同的数据。您还可以提供自己的HTML模板来显示错误(参见下一节)。

定制此功能的第一步通常包括使用现有机制，但替换或增加错误内容。为此，您可以添加一个`ErrorAttributes`类型的bean。

要更改错误处理行为，可以实现ErrorWebExceptionHandler并注册该类型的bean定义。由于WebExceptionHandler是非常底层的，Spring Boot还提供了一个方便的AbstractErrorWebExceptionHandler，让您以WebFlux函数的方式处理错误，如下面的例子所示:
```java
public class CustomErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {
    // Define constructor here
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions
        .route(aPredicate, aHandler)
        .andRoute(anotherPredicate, anotherHandler);
    }
}
```
要获得更完整的信息，还可以直接子类化`DefaultErrorWebExceptionHandler`并覆盖特定的方法。

## Custom Error Pages

如果希望为给定状态代码显示自定义HTML错误页，可以将文件添加到/ error文件夹。错误页面可以是静态HTML(即添加在任何静态资源文件夹下)，也可以是用模板构建的。文件的名称应该是确切的状态码或系列掩码。

例如，要将404映射到静态HTML文件，您的文件夹结构如下:
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
要使用Mustache模板映射所有5xx个错误，您的文件夹结构如下:
```text
src/
	+- main/
		+- java/
			| + <source code>
		+- resources/
			+- templates/
				+- error/
					| +- 5xx.mustache
				+- <other templates>
```

## Web Filters

Spring WebFlux提供了一个`WebFilter`接口，可以实现该接口来过滤HTTP请求响应交换。在应用程序上下文中找到的`WebFilter` bean将自动用于过滤每个交换

在过滤器的顺序很重要的地方，它们可以实现Ordered或用@Order注释。Spring Boot auto-configuration可以为您配置web过滤器。当它这样做时，将使用下表所示的顺序:

| Web Filter | Order |
|-------|:-------------|
| MetricsWebFilter |  Ordered.HIGHEST_PRECEDENCE + 1 |
| WebFilterChainProxy (Spring Security) | -100 |
| HttpTraceWebFilter | Ordered.LOWEST_PRECEDENCE - 10 |


WebClient

https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-new-framework