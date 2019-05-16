# Spring WebFlux
## Overview

Why was Spring WebFlux created?

为什么要创建Spring WebFlux ?

Part of the answer is the need for a non-blocking web stack to handle concurrency with a small number of threads and scale with fewer hardware resources. Servlet 3.1 did provide an API for non-blocking I/O. However, using it leads away from the rest of the Servlet API, where contracts are synchronous (Filter, Servlet) or blocking (getParameter, getPart). This was the motivation for a new common API to serve as a foundation across any non-blocking runtime. That is important because of servers (such as Netty) that are well-established in the async, non-blocking space.

部分原因是需要一个非阻塞的web堆栈，以使用少量线程处理并发性，并使用更少的硬件资源进行伸缩。Servlet 3.1确实提供了一个用于非阻塞I/O的API。但是，使用它将远离Servlet API的其余部分，其中的契约是同步的(过滤器、Servlet)或阻塞的(getParameter、getPart)。这就是新的公共API作为跨任何非阻塞运行时的基础的动机。这一点很重要，因为服务器(如Netty)在异步、非阻塞空间中已经建立了良好的基础。

The other part of the answer is functional programming. Much as the addition of annotations in Java 5 created opportunities (such as annotated REST controllers or unit tests), the addition of lambda expressions in Java 8 created opportunities for functional APIs in Java. This is a boon for non-blocking applications and continuation-style APIs (as popularized by CompletableFuture and [ReactiveX](http://reactivex.io/)) that allow declarative composition of asynchronous logic. At the programming-model level, Java 8 enabled Spring WebFlux to offer functional web endpoints alongside annotated controllers.

答案的另一部分是函数式编程。正如在Java 5中添加注释创建了机会(如带注释的REST控制器或单元测试)一样，在Java 8中添加lambda表达式也为Java中的函数api创建了机会。这对于非阻塞应用程序和延续风格的api(如CompletableFuture和[ReactiveX](http://reactivex.io/)所推广的那样)来说是一个福音，它们允许异步逻辑的声明式组合。在编程模型级别，Java 8支持Spring WebFlux提供功能性web端点和带注释的控制器。

### Define “Reactive”

We touched on “non-blocking” and “functional” but what does reactive mean?

我们提到了“非阻塞”和“功能性”，但是反应性是什么意思呢?

The term, “reactive,” refers to programming models that are built around reacting to change — network components reacting to I/O events, UI controllers reacting to mouse events, and others. In that sense, non-blocking is reactive, because, instead of being blocked, we are now in the mode of reacting to notifications as operations complete or data becomes available.

术语“反应性”指的是围绕响应更改而构建的编程模型——网络组件响应I/O事件，UI控制器响应鼠标事件，以及其他。从这个意义上说，非阻塞是反应性的，因为我们现在不是被阻塞，而是在操作完成或数据可用时响应通知的模式。

There is also another important mechanism that we on the Spring team associate with “reactive” and that is non-blocking back pressure. In synchronous, imperative code, blocking calls serve as a natural form of back pressure that forces the caller to wait. In non-blocking code, it becomes important to control the rate of events so that a fast producer does not overwhelm its destination.

Spring团队中还有另一个重要的机制与“反应性”相关联，那就是非阻塞背压。在同步的命令式代码中，阻塞调用是一种迫使调用者等待的自然形式。在非阻塞代码中，控制事件的速率变得非常重要，这样快速的生成器就不会淹没它的目标。

Reactive Streams is a [small spec](https://github.com/reactive-streams/reactive-streams-jvm/blob/master/README.md#specification) (also [adopted](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Flow.html) in Java 9) that defines the interaction between asynchronous components with back pressure. For example a data repository (acting as [Publisher](https://www.reactive-streams.org/reactive-streams-1.0.1-javadoc/org/reactivestreams/Publisher.html)) can produce data that an HTTP server (acting as [Subscriber](https://www.reactive-streams.org/reactive-streams-1.0.1-javadoc/org/reactivestreams/Subscriber.html)) can then write to the response. The main purpose of Reactive Streams is to let the subscriber to control how quickly or how slowly the publisher produces data.

反应流是一个[小规范](https://github.com/reactive-streams/reactive-streams-jvm/blob/master/README.md#specification)(也在Java 9中[采用](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Flow.html))，它定义了具有背压的异步组件之间的交互。例如，数据存储库(充当[发布者](https://www.reactive-streams.org/reactive-streams-1.0.1-javadoc/org/reactivestreams/Publisher.html))可以生成HTTP服务器(充当[订阅者](https://www.reactive-streams.org/reactive-streams-1.0.1-javadoc/org/reactivestreams/Subscriber.html))可以写入响应的数据。反应性流的主要目的是让订阅者控制发布者生成数据的速度或速度。

>Common question: what if a publisher cannot slow down?<br>
>The purpose of Reactive Streams is only to establish the mechanism and a boundary. If a publisher cannot slow down, it has to decide whether to buffer, drop, or fail.
>
>常见问题:如果发布者不能慢下来怎么办?<br>
>反应流的目的只是建立机制和边界。如果发布者不能慢下来，它必须决定是缓冲、删除还是失败。

### Reactive API

Reactive Streams plays an important role for interoperability. It is of interest to libraries and infrastructure components but less useful as an application API, because it is too low-level. Applications need a higher-level and richer, functional API to compose async logic — similar to the Java 8 Stream API but not only for collections. This is the role that reactive libraries play.

反应性流对于互操作性起着重要的作用。它对库和基础设施组件很感兴趣，但作为应用程序API用处不大，因为它太低级。应用程序需要一个更高级、更丰富、功能更强的API来组合异步逻辑——类似于Java 8流API，但不只是用于集合。这就是反应性库所扮演的角色。

[Reactor](https://github.com/reactor/reactor) is the reactive library of choice for Spring WebFlux. It provides the Mono and Flux API types to work on data sequences of 0..1 (Mono) and 0..N (Flux) through a rich set of operators aligned with the ReactiveX [vocabulary of operators](http://reactivex.io/documentation/operators.html). Reactor is a Reactive Streams library and, therefore, all of its operators support non-blocking back pressure. Reactor has a strong focus on server-side Java. It is developed in close collaboration with Spring.

[反应器](https://github.com/reactor/reactor)是弹簧流量的反应库的选择。它提供了Mono和Flux API类型来处理0。1 (Mono)和0..N (Flux)通过与操作符的ReactiveX[词汇表对齐的一组丰富的操作符](http://reactivex.io/documentation/operators.html)。反应器是一个反应流库，因此，它的所有操作人员都支持非阻塞背压。Reactor非常关注服务器端Java。它是与Spring紧密合作开发的。

WebFlux requires Reactor as a core dependency but it is interoperable with other reactive libraries via Reactive Streams. As a general rule, a WebFlux API accepts a plain Publisher as input, adapts it to a Reactor type internally, uses that, and returns either a Flux or a Mono as output. So, you can pass any Publisher as input and you can apply operations on the output, but you need to adapt the output for use with another reactive library. Whenever feasible (for example, annotated controllers), WebFlux adapts transparently to the use of RxJava or another reactive library. See Reactive Libraries for more details.

WebFlux需要反应器作为核心依赖项，但是它可以通过反应流与其他反应库互操作。一般来说，WebFlux API接受一个普通的发布者作为输入，在内部调整它以适应反应器类型，使用它，并返回一个Flux或Mono作为输出。因此，您可以将任何发布者作为输入传递，并且可以对输出应用操作，但是您需要调整输出，以便与另一个反应性库一起使用。只要可行(例如，带注释的控制器)，WebFlux就透明地适应RxJava或其他反应性库的使用。有关详细信息，请参阅[反应性库](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-reactive-libraries)。

### Programming Models

The spring-web module contains the reactive foundation that underlies Spring WebFlux, including HTTP abstractions, Reactive Streams adapters for supported servers, codecs, and a core WebHandler API comparable to the Servlet API but with non-blocking contracts.

Spring-web模块包含Spring WebFlux的反应性基础，包括HTTP抽象、支持服务器的反应性流[适配器](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-httphandler)、[codecs](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-codecs)和一个核心的[WebHandler API](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-web-handler-api)，它与Servlet API类似，但是具有非阻塞契约。

On that foundation, Spring WebFlux provides a choice of two programming models:

在此基础上，Spring WebFlux提供了两种编程模型的选择:

- [Annotated Controllers](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-controller): Consistent with Spring MVC and based on the same annotations from the spring-web module. Both Spring MVC and WebFlux controllers support reactive (Reactor and RxJava) return types, and, as a result, it is not easy to tell them apart. One notable difference is that WebFlux also supports reactive @RequestBody arguments.
  
  [带注释的控制器](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-controller):与Spring MVC一致，基于Spring -web模块的相同注释。Spring MVC和WebFlux控制器都支持反应式(反应器和RxJava)返回类型，因此，很难将它们区分开来。一个显著的区别是WebFlux还支持响应性@RequestBody参数。
- [Functional Endpoints](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-fn): Lambda-based, lightweight, and functional programming model. You can think of this as a small library or a set of utilities that an application can use to route and handle requests. The big difference with annotated controllers is that the application is in charge of request handling from start to finish versus declaring intent through annotations and being called back.
  
  [函数端点](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-fn):基于lambda的、轻量级的和函数式编程模型。您可以将其视为一个小型库或一组实用程序，应用程序可以使用它们来路由和处理请求。与带注释的控制器的最大区别在于，应用程序从头到尾负责请求处理，而不是通过注释声明意图并被回调。
  
### Applicability

Spring MVC or WebFlux?

Spring MVC还是WebFlux?

A natural question to ask but one that sets up an unsound dichotomy. Actually, both work together to expand the range of available options. The two are designed for continuity and consistency with each other, they are available side by side, and feedback from each side benefits both sides. The following diagram shows how the two relate, what they have in common, and what each supports uniquely:

这是一个很自然的问题，但却产生了一个不合理的二分法。实际上，两者共同努力扩大了可用选项的范围。这两者的设计是为了彼此之间的连续性和一致性，它们可以并排使用，并且每一方的反馈对双方都有好处。下图显示了两者之间的关系、它们的共同点以及各自支持的独特之处:

[Spring MVC与WebFlux功能对比图](spring-mvc-and-webflux-venn.png)

We suggest that you consider the following specific points:

我们建议您考虑以下具体问题:

- If you have a Spring MVC application that works fine, there is no need to change. Imperative programming is the easiest way to write, understand, and debug code. You have maximum choice of libraries, since, historically, most are blocking.
  
  如果您有一个运行良好的Spring MVC应用程序，那么就不需要进行更改。命令式编程是编写、理解和调试代码的最简单方法。您有最多的库选择，因为从历史上看，大多数库都是阻塞的。
  
- If you are already shopping for a non-blocking web stack, Spring WebFlux offers the same execution model benefits as others in this space and also provides a choice of servers (Netty, Tomcat, Jetty, Undertow, and Servlet 3.1+ containers), a choice of programming models (annotated controllers and functional web endpoints), and a choice of reactive libraries (Reactor, RxJava, or other).
  
  如果你已经购买一个非阻塞的网络堆栈,Spring WebFlux提供了和其他人同样的执行模型的好处在这个空间,也提供了一个选择的服务器(Netty, Tomcat, Jetty, Undertow, and Servlet 3.1+ containers),选择编程模型(带注释的控制器和功能性web端点),和选择反应库(RxJava反应堆,或其他)。
  
- If you are interested in a lightweight, functional web framework for use with Java 8 lambdas or Kotlin, you can use the Spring WebFlux functional web endpoints. That can also be a good choice for smaller applications or microservices with less complex requirements that can benefit from greater transparency and control.

  如果您对与Java 8 lambdas或Kotlin一起使用的轻量级功能性web框架感兴趣，可以使用Spring WebFlux功能性web端点。对于要求不那么复杂的小型应用程序或微服务来说，这也是一个不错的选择，因为它们可以从更大的透明度和控制中获益。

- In a microservice architecture, you can have a mix of applications with either Spring MVC or Spring WebFlux controllers or with Spring WebFlux functional endpoints. Having support for the same annotation-based programming model in both frameworks makes it easier to re-use knowledge while also selecting the right tool for the right job.
  
  在微服务体系结构中，可以使用Spring MVC或Spring WebFlux控制器或Spring WebFlux功能端点组合应用程序。在这两个框架中都支持相同的基于注释的编程模型，这使得重用知识变得更容易，同时也为正确的工作选择了正确的工具。

- A simple way to evaluate an application is to check its dependencies. If you have blocking persistence APIs (JPA, JDBC) or networking APIs to use, Spring MVC is the best choice for common architectures at least. It is technically feasible with both Reactor and RxJava to perform blocking calls on a separate thread but you would not be making the most of a non-blocking web stack.

  评估应用程序的一个简单方法是检查它的依赖关系。如果您要使用阻塞持久性api (JPA、JDBC)或网络api，那么Spring MVC至少是公共体系结构的最佳选择。反应器和RxJava在技术上都可以在单独的线程上执行阻塞调用，但是您不会充分利用非阻塞web堆栈。
  
- If you have a Spring MVC application with calls to remote services, try the reactive WebClient. You can return reactive types (Reactor, RxJava, or other) directly from Spring MVC controller methods. The greater the latency per call or the interdependency among calls, the more dramatic the benefits. Spring MVC controllers can call other reactive components too.

  如果您有一个带有对远程服务调用的Spring MVC应用程序，请尝试反应性WebClient。您可以直接从Spring MVC控制器方法返回反应类型(反应器、RxJava或其他)。每次调用的延迟或调用之间的相互依赖性越大，好处就越大。Spring MVC控制器也可以调用其他反应性组件。
  
- If you have a large team, keep in mind the steep learning curve in the shift to non-blocking, functional, and declarative programming. A practical way to start without a full switch is to use the reactive WebClient. Beyond that, start small and measure the benefits. We expect that, for a wide range of applications, the shift is unnecessary. If you are unsure what benefits to look for, start by learning about how non-blocking I/O works (for example, concurrency on single-threaded Node.js) and its effects.
  
  如果您有一个大型的团队，请记住向非阻塞、函数式和声明式编程转变的陡峭学习曲线。在没有完全切换的情况下启动的一种实用方法是使用反应性web客户机。除此之外，从小处着手，衡量收益。我们预计，对于广泛的应用程序，这种转变是不必要的。如果您不确定应该寻找哪些好处，那么首先了解一下非阻塞I/O是如何工作的(例如，单线程Node.js上的并发性)及其效果。
  
### Servers  

Spring WebFlux is supported on Tomcat, Jetty, Servlet 3.1+ containers, as well as on non-Servlet runtimes such as Netty and Undertow. All servers are adapted to a low-level, common API so that higher-level programming models can be supported across servers.

Spring WebFlux支持Tomcat、Jetty、Servlet 3.1+容器，也支持非Servlet运行时，如Netty和Undertow。所有服务器都适用于低层的公共API，因此可以跨服务器支持更高级别的编程模型。

Spring WebFlux does not have built-in support to start or stop a server. However, it is easy to assemble an application from Spring configuration and WebFlux infrastructure and run it with a few lines of code.

Spring WebFlux不支持启动或停止服务器。然而，从Spring配置和WebFlux基础设施组装应用程序并使用几行代码运行它是很容易的。

Spring Boot has a WebFlux starter that automates these steps. By default, the starter uses Netty, but it is easy to switch to Tomcat, Jetty, or Undertow by changing your Maven or Gradle dependencies. Spring Boot defaults to Netty, because it is more widely used in the asynchronous, non-blocking space and lets a client and a server share resources.

Spring Boot有一个WebFlux启动器，可以自动执行这些步骤。默认情况下，启动器使用Netty，但是通过更改Maven或Gradle依赖项可以很容易地切换到Tomcat、Jetty或Undertow。Spring Boot默认为Netty，因为它更广泛地用于异步、非阻塞空间，并允许客户机和服务器共享资源。

Tomcat and Jetty can be used with both Spring MVC and WebFlux. Keep in mind, however, that the way they are used is very different. Spring MVC relies on Servlet blocking I/O and lets applications use the Servlet API directly if they need to. Spring WebFlux relies on Servlet 3.1 non-blocking I/O and uses the Servlet API behind a low-level adapter and not exposed for direct use.

Tomcat和Jetty可以同时用于Spring MVC和WebFlux。但是，请记住，它们的使用方法是非常不同的。Spring MVC依赖于Servlet阻塞I/O，并允许应用程序在需要时直接使用Servlet API。Spring WebFlux依赖于Servlet 3.1非阻塞I/O，使用底层适配器背后的Servlet API，并且没有公开供直接使用。

For Undertow, Spring WebFlux uses Undertow APIs directly without the Servlet API.

对于 Undertow，Spring WebFlux直接使用Undertow，而不使用Servlet API。

### Performance

Performance has many characteristics and meanings. Reactive and non-blocking generally do not make applications run faster. They can, in some cases, (for example, if using the WebClient to execute remote calls in parallel). On the whole, it requires more work to do things the non-blocking way and that can increase slightly the required processing time.

性能具有许多特性和意义。反应性和非阻塞通常不会使应用程序运行得更快。在某些情况下，它们可以(例如，如果使用WebClient并行执行远程调用)。总的来说，以非阻塞的方式做事情需要更多的工作，这可能会稍微增加所需的处理时间。

The key expected benefit of reactive and non-blocking is the ability to scale with a small, fixed number of threads and less memory. That makes applications more resilient under load, because they scale in a more predictable way. In order to observe those benefits, however, you need to have some latency (including a mix of slow and unpredictable network I/O). That is where the reactive stack begins to show its strengths, and the differences can be dramatic.

反应性和非阻塞的主要预期好处是能够使用少量固定数量的线程和更少的内存进行伸缩。这使得应用程序在负载下更有弹性，因为它们以更可预测的方式伸缩。然而，为了观察这些好处，您需要一些延迟(包括慢速和不可预测的网络I/O的混合)。这就是反应性堆栈开始显示其优势的地方，而且差异可能是巨大的。

### Concurrency Model

Both Spring MVC and Spring WebFlux support annotated controllers, but there is a key difference in the concurrency model and the default assumptions for blocking and threads.

Spring MVC和Spring WebFlux都支持带注释的控制器，但是在并发模型和阻塞和线程的默认假设方面有一个关键的区别。

In Spring MVC (and servlet applications in general), it is assumed that applications can block the current thread, (for example, for remote calls), and, for this reason, servlet containers use a large thread pool to absorb potential blocking during request handling.

在Spring MVC(以及一般的servlet应用程序)中，假定应用程序可以阻塞当前线程(例如，用于远程调用)，因此servlet容器使用一个大型线程池来吸收请求处理过程中可能出现的阻塞。

In Spring WebFlux (and non-blocking servers in general), it is assumed that applications do not block, and, therefore, non-blocking servers use a small, fixed-size thread pool (event loop workers) to handle requests.

在Spring WebFlux(以及一般的非阻塞服务器)中，假定应用程序不会阻塞，因此，非阻塞服务器使用一个小的固定大小的线程池(事件循环工作者)来处理请求。

> “To scale” and “small number of threads” may sound contradictory but to never block the current thread (and rely on callbacks instead) means that you do not need extra threads, as there are no blocking calls to absorb.
>
>“可伸缩”和“线程数量较少”听起来可能相互矛盾，但是永远不要阻塞当前线程(而是依赖回调)意味着您不需要额外的线程，因为没有需要吸收的阻塞调用。

#### Invoking a Blocking API
What if you do need to use a blocking library? Both Reactor and RxJava provide the publishOn operator to continue processing on a different thread. That means there is an easy escape hatch. Keep in mind, however, that blocking APIs are not a good fit for this concurrency model.

如果确实需要使用阻塞库怎么办?反应器和RxJava都提供了publishOn操作符来在不同的线程上继续处理。这意味着有一个容易的逃生口。但是请记住，阻塞api并不适合此并发模型。

#### Mutable State
In Reactor and RxJava, you declare logic through operators, and, at runtime, a reactive pipeline is formed where data is processed sequentially, in distinct stages. A key benefit of this is that it frees applications from having to protect mutable state because application code within that pipeline is never invoked concurrently.

在反应器和RxJava中，您可以通过操作符声明逻辑，并且在运行时，会形成一个反应性管道，其中数据按顺序在不同的阶段进行处理。这样做的一个关键好处是，它将应用程序从必须保护可变状态中解放出来，因为该管道中的应用程序代码永远不会被并发调用。

#### Threading Model
What threads should you expect to see on a server running with Spring WebFlux?

您希望在使用Spring WebFlux运行的服务器上看到哪些线程?

- On a “vanilla” Spring WebFlux server (for example, no data access nor other optional dependencies), you can expect one thread for the server and several others for request processing (typically as many as the number of CPU cores). Servlet containers, however, may start with more threads (for example, 10 on Tomcat), in support of both servlet (blocking) I/O and servlet 3.1 (non-blocking) I/O usage.
   
  在“普通的”Spring WebFlux服务器上(例如，没有数据访问或其他可选依赖项)，您可以期望一个线程用于服务器，几个线程用于请求处理(通常与CPU内核的数量一样多)。但是，Servlet容器可以从更多的线程开始(例如，Tomcat上的10个线程)，以支持Servlet(阻塞)I/O和Servlet 3.1(非阻塞)I/O使用。

- The reactive WebClient operates in event loop style. So you can see a small, fixed number of processing threads related to that (for example, reactor-http-nio- with the Reactor Netty connector). However, if Reactor Netty is used for both client and server, the two share event loop resources by default.
  
  反应性web客户机以事件循环的方式运行。因此，您可以看到与之相关的少量固定数量的处理线程(例如，使用反应器Netty连接器的反应器-http-nio)。但是，如果客户端和服务器都使用反应器Netty，则默认情况下这两个服务器共享事件循环资源。
 
- Reactor and RxJava provide thread pool abstractions, called Schedulers, to use with the publishOn operator that is used to switch processing to a different thread pool. The schedulers have names that suggest a specific concurrency strategy — for example, “parallel” (for CPU-bound work with a limited number of threads) or “elastic” (for I/O-bound work with a large number of threads). If you see such threads, it means some code is using a specific thread pool Scheduler strategy.

  反应器和RxJava提供线程池抽象，称为调度程序，用于与publishOn操作符一起使用，publishOn操作符用于将处理切换到不同的线程池。调度程序的名称暗示了一种特定的并发策略——例如，“parallel”(用于cpu绑定的工作，线程数量有限)或“elastic”(用于I/ o绑定的工作，线程数量较多)。如果您看到这样的线程，这意味着一些代码正在使用特定的线程池调度程序策略。
  
- Data access libraries and other third party dependencies can also create and use threads of their own.

  数据访问库和其他第三方依赖项也可以创建和使用它们自己的线程。
  
#### Configuring
The Spring Framework does not provide support for starting and stopping [servers](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-server-choice). To configure the threading model for a server, you need to use server-specific configuration APIs, or, if you use Spring Boot, check the Spring Boot configuration options for each server. You can [configure](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-client-builder) the WebClient directly. For all other libraries, see their respective documentation.

Spring框架不支持启动和停止[服务器](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-server-choice)。要为服务器配置线程模型，需要使用特定于服务器的配置api，或者，如果使用Spring Boot，请检查每个服务器的Spring Boot配置选项。您可以直接[配置](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-client-builder)WebClient。对于所有其他库，请参阅它们各自的文档。

## Reactive Core
The spring-web module contains the following foundational support for reactive web applications:

spring-web模块包含以下对反应性web应用程序的基本支持:

- For server request processing there are two levels of support.
  
  对于服务器请求处理，有两种级别的支持。
  
  - [HttpHandler](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-httphandler): Basic contract for HTTP request handling with non-blocking I/O and Reactive Streams back pressure, along with adapters for Reactor Netty, Undertow, Tomcat, Jetty, and any Servlet 3.1+ container.
  
    [HttpHandler](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-httphandler): HTTP请求处理的基本契约，具有非阻塞I/O和响应流回压，以及用于反应器Netty、Undertow、Tomcat、Jetty和任何Servlet 3.1+容器的适配器。
  
  - [WebHandler API](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-web-handler-api): Slightly higher level, general-purpose web API for request handling, on top of which concrete programming models such as annotated controllers and functional endpoints are built.
     
    [WebHandler API](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-web-handler-api): Slightly higher level, general-purpose web API for request handling, on top of which concrete programming models such as annotated controllers and functional endpoints are built.
  
- For the client side, there is a basic ClientHttpConnector contract to perform HTTP requests with non-blocking I/O and Reactive Streams back pressure, along with adapters for [Reactor Netty](https://github.com/reactor/reactor-netty) and for the reactive [Jetty HttpClient](https://github.com/jetty-project/jetty-reactive-httpclient). The higher level [WebClient](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-client) used in applications builds on this basic contract.

  对于客户端，有一个基本的ClientHttpConnector契约，用于执行具有非阻塞I/O和响应流回压的HTTP请求，以及[反应器Netty](https://github.com/reactor/reactor-netty)和响应[Jetty HttpClient](https://github.com/jetty-project/jetty-reactive-httpclient)的适配器。应用程序中使用的更高级别的[WebClient](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-client)基于这个基本契约。
  
- For client and server, [codecs](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-codecs) to use to serialize and deserialize HTTP request and response content.

  对于客户机和服务器，用于序列化和反序列化HTTP请求和响应内容的[编解码器](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-codecs)。
  
### HttpHandler

[HttpHandler](https://docs.spring.io/spring-framework/docs/5.1.6.RELEASE/javadoc-api/org/springframework/http/server/reactive/HttpHandler.html) is a simple contract with a single method to handle a request and response. It is intentionally minimal, and its main, and only purpose is to be a minimal abstraction over different HTTP server APIs.

[HttpHandler](https://docs.spring.io/spring-framework/docs/5.1.6.RELEASE/javadoc-api/org/springframework/http/server/reactive/HttpHandler.html)是一个简单的契约，只有一个方法来处理请求和响应。它故意最小化，它的主要也是唯一的目的是对不同HTTP服务器api进行最小的抽象。

The following table describes the supported server APIs:

下表描述了所支持的服务器api:

| Server name | Server API used | Reactive Streams support |
|-------|:-------------|:-------------|
| Netty |  Netty API | Reactor Netty |
| Undertow | Undertow API | spring-web: Undertow to Reactive Streams bridge |
| Tomcat | Servlet 3.1 non-blocking I/O; Tomcat API to read and write ByteBuffers vs byte[] | spring-web: Servlet 3.1 non-blocking I/O to Reactive Streams bridge |
| Jetty | Servlet 3.1 non-blocking I/O; Jetty API to write ByteBuffers vs byte[] | spring-web: Servlet 3.1 non-blocking I/O to Reactive Streams bridge |
| Servlet 3.1 container | Servlet 3.1 non-blocking I/O | spring-web: Servlet 3.1 non-blocking I/O to Reactive Streams bridge |

The following table describes server dependencies (also see [supported versions](https://github.com/spring-projects/spring-framework/wiki/What%27s-New-in-the-Spring-Framework))):

下表描述了服务器依赖关系(也参见[支持的版本](https://github.com/spring-projects/spring-framework/wiki/What%27s-New-in-the-Spring-Framework)):

| Server name | Group id | Artifact name |
|-------|:-------------|:-------------|
| Reactor Netty |  io.projectreactor.netty | reactor-netty |
| Undertow |  io.undertow | undertow-core |
| Tomcat |  org.apache.tomcat.embed | tomcat-embed-core |
| Jetty |  org.eclipse.jetty | jetty-server, jetty-servlet |

The code snippets below show using the HttpHandler adapters with each server API:

下面的代码片段显示了使用每个服务器API的HttpHandler适配器:

**Reactor Netty**
```java
HttpHandler handler = ...
ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(handler);
HttpServer.create(host, port).newHandler(adapter).block();
```

**Undertow**
```java
HttpHandler handler = ...
UndertowHttpHandlerAdapter adapter = new UndertowHttpHandlerAdapter(handler);
Undertow server = Undertow.builder().addHttpListener(port, host).setHandler(adapter).build();
server.start();
```

**Tomcat**
```java
HttpHandler handler = ...
Servlet servlet = new TomcatHttpHandlerAdapter(handler);

Tomcat server = new Tomcat();
File base = new File(System.getProperty("java.io.tmpdir"));
Context rootContext = server.addContext("", base.getAbsolutePath());
Tomcat.addServlet(rootContext, "main", servlet);
rootContext.addServletMappingDecoded("/", "main");
server.setHost(host);
server.setPort(port);
server.start();
```

**Jetty**
```java
HttpHandler handler = ...
Servlet servlet = new JettyHttpHandlerAdapter(handler);

Server server = new Server();
ServletContextHandler contextHandler = new ServletContextHandler(server, "");
contextHandler.addServlet(new ServletHolder(servlet), "/");
contextHandler.start();

ServerConnector connector = new ServerConnector(server);
connector.setHost(host);
connector.setPort(port);
server.addConnector(connector);
server.start();
```

**Servlet 3.1+ Container**
To deploy as a WAR to any Servlet 3.1+ container, you can extend and include AbstractReactiveWebInitializer in the WAR. That class wraps an HttpHandler with ServletHttpHandlerAdapter and registers that as a Servlet.

### WebHandler API

The org.springframework.web.server package builds on the [HttpHandler](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-httphandler) contract to provide a general-purpose web API for processing requests through a chain of multiple [WebExceptionHandler](https://docs.spring.io/spring-framework/docs/5.1.6.RELEASE/javadoc-api/org/springframework/web/server/WebExceptionHandler.html), multiple [WebFilter](https://docs.spring.io/spring-framework/docs/5.1.6.RELEASE/javadoc-api/org/springframework/web/server/WebFilter.html), and a single [WebHandler](https://docs.spring.io/spring-framework/docs/5.1.6.RELEASE/javadoc-api/org/springframework/web/server/WebHandler.html) component. The chain can be put together with WebHttpHandlerBuilder by simply pointing to a Spring ApplicationContext where components are [auto-detected](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-web-handler-api-special-beans), and/or by registering components with the builder.

org.springframework.web。服务器包构建在[HttpHandler](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-httphandler)契约的基础上，提供了一个通用的web API，用于通过多个[WebExceptionHandler](https://docs.spring.io/spring-framework/docs/5.1.6.RELEASE/javadoc-api/org/springframework/web/server/WebExceptionHandler.html)、多个[WebFilter](https://docs.spring.io/spring-framework/docs/5.1.6.RELEASE/javadoc-api/org/springframework/web/server/WebFilter.html)和一个[WebHandler](https://docs.spring.io/spring-framework/docs/5.1.6.RELEASE/javadoc-api/org/springframework/web/server/WebHandler.html)组件链处理请求。只需指向自动检测组件的Spring ApplicationContext，并/或向构建器[注册组件](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-web-handler-api-special-beans)，就可以将链与WebHttpHandlerBuilder放在一起。

While HttpHandler has a simple goal to abstract the use of different HTTP servers, the WebHandler API aims to provide a broader set of features commonly used in web applications such as:

HttpHandler有一个简单的目标，就是抽象不同HTTP服务器的使用，而WebHandler API的目标是提供web应用程序中常用的更广泛的特性，比如:

- User session with attributes.
  
  具有属性的用户会话。

- Request attributes.
  
  请求属性。
  
- Resolved Locale or Principal for the request.
  
  已解析请求的区域设置或主体。
  
- Access to parsed and cached form data.

  访问已解析和缓存的表单数据。
 
- Abstractions for multipart data.

  多部分数据的抽象。
  
- and more..

#### Special bean types

The table below lists the components that WebHttpHandlerBuilder can auto-detect in a Spring ApplicationContext, or that can be registered directly with it:

下表列出了WebHttpHandlerBuilder可以在Spring ApplicationContext中自动检测到的组件，或者可以直接用它注册的组件:

			
| Bean name | Bean type | Count | Description |
|-------|:-------------|:-------------|:-------------|
| <any> |  WebExceptionHandler | 0..N | Provide handling for exceptions from the chain of WebFilter instances and the target WebHandler. For more details, see [Exceptions](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-exception-handler). |
| <any> |  WebFilter | 0..N | Apply interception style logic to before and after the rest of the filter chain and the target WebHandler. For more details, see [Filters](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-filters). |
| webHandler |  WebHandler | 1 | The handler for the request. |
| webSessionManager |  WebSessionManager | 0..1 | The manager for WebSession instances exposed through a method on ServerWebExchange. DefaultWebSessionManager by default. |
| localeContextResolver |  LocaleContextResolver | 0..1 | The resolver for LocaleContext exposed through a method on ServerWebExchange. AcceptHeaderLocaleContextResolver by default. |
| forwardedHeaderTransformer |  ForwardedHeaderTransformer | 0..1 | For processing forwarded type headers, either by extracting and removing them or by removing them only. Not used by default. |

#### Form Data

ServerWebExchange exposes the following method for access to form data:

ServerWebExchange公开了访问表单数据的以下方法:

```java
Mono<MultiValueMap<String, String>> getFormData();
```

The DefaultServerWebExchange uses the configured HttpMessageReader to parse form data (application/x-www-form-urlencoded) into a MultiValueMap. By default, FormHttpMessageReader is configured for use by the ServerCodecConfigurer bean (see the Web Handler API).

DefaultServerWebExchange使用配置好的HttpMessageReader将表单数据(应用程序/x-www-form-urlencoded)解析为一个多值emap。默认情况下，FormHttpMessageReader被配置为供ServerCodecConfigurer bean使用(请参阅[Web处理程序API](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-web-handler-api))。

#### Multipart Data

[Same as in Spring MVC](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web.html#mvc-multipart)

ServerWebExchange exposes the following method for access to multipart data:

ServerWebExchange公开了访问多部分数据的以下方法:

```java
Mono<MultiValueMap<String, Part>> getMultipartData();
```

The DefaultServerWebExchange uses the configured HttpMessageReader<MultiValueMap<String, Part>> to parse multipart/form-data content into a MultiValueMap. At present, [Synchronoss NIO Multipart](https://github.com/synchronoss/nio-multipart) is the only third-party library supported and the only library we know for non-blocking parsing of multipart requests. It is enabled through the ServerCodecConfigurer bean (see the [Web Handler API](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-web-handler-api)).

DefaultServerWebExchange使用配置的HttpMessageReader<MultiValueMap<String, Part>>将多部分/表单数据内容解析为一个多值emap。目前，[Synchronoss NIO Multipart](https://github.com/synchronoss/nio-multipart)是唯一支持的第三方库，也是我们所知道的唯一一个可以对多部分请求进行非阻塞解析的库。它是通过ServerCodecConfigurer bean启用的(请参阅[Web处理程序API](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-web-handler-api))。

To parse multipart data in streaming fashion, you can use the Flux returned from an HttpMessageReader instead. For example, in an annotated controller, use of @RequestPart implies Map-like access to individual parts by name and, hence, requires parsing multipart data in full. By contrast, you can use @RequestBody to decode the content to Flux without collecting to a MultiValueMap.

要以流方式解析多部分数据，可以使用HttpMessageReader<Part>返回的Flux<Part>。例如，在带注释的控制器中，使用@RequestPart意味着按名称对单个部件进行类似映射的访问，因此需要完整地解析多部件数据。相反，您可以使用@RequestBody解码内容来通量<Part>，而无需收集到MultiValueMap。

#### Forwarded Headers

[Same as in Spring MVC](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web.html#filters-forwarded-headers)

As a request goes through proxies (such as load balancers), the host, port, and scheme may change, and that makes it a challenge, from a client perspective, to create links that point to the correct host, port, and scheme.

当请求通过代理(例如负载平衡器)时，主机、端口和模式可能会发生更改，从客户端角度来看，创建指向正确主机、端口和模式的链接是一个挑战。

[RFC 7239](https://tools.ietf.org/html/rfc7239) defines the Forwarded HTTP header that proxies can use to provide information about the original request. There are other non-standard headers, too, including X-Forwarded-Host, X-Forwarded-Port, X-Forwarded-Proto, X-Forwarded-Ssl, and X-Forwarded-Prefix.

[RFC 7239](https://tools.ietf.org/html/rfc7239)定义了转发的HTTP头，代理可以使用它来提供关于原始请求的信息。还有其他非标准头，包括x - forward - host、x - forward - port、x - forward - proto、x - forward - ssl和x - forward - prefix。

ForwardedHeaderTransformer is a component that modifies the host, port, and scheme of the request, based on forwarded headers, and then removes those headers. You can declare it as a bean with a name of forwardedHeaderTransformer, and it is detected and used.

forwarding headertransformer是一个组件，它根据转发的头修改请求的主机、端口和方案，然后删除这些头。您可以将它声明为一个bean，名称为forwarding headertransformer，并检测到它并使用它。

There are security considerations for forwarded headers, since an application cannot know if the headers were added by a proxy, as intended, or by a malicious client. This is why a proxy at the boundary of trust should be configured to remove untrusted forwarded traffic coming from the outside. You can also configure the ForwardedHeaderTransformer with removeOnly=true, in which case it removes but does not use the headers.

对于转发的报头有一些安全考虑，因为应用程序无法知道报头是由代理(如预期的那样)添加的，还是由恶意客户机添加的。这就是为什么应该配置位于信任边界的代理，以删除来自外部的不受信任的转发流量。您还可以使用removeOnly=true配置forwarding headertransformer，在这种情况下，它删除但不使用头文件。

> In 5.1 ForwardedHeaderFilter was deprecated and superceded by ForwardedHeaderTransformer so forwarded headers can be processed earlier, before the exchange is created. If the filter is configured anyway, it is taken out of the list of filters, and ForwardedHeaderTransformer is used instead.
>
> 在5.1中，forward headerfilter被摒弃，并由forward headertransformer代替，因此可以在创建交换之前更早地处理转发头。如果无论如何配置了过滤器，它将从过滤器列表中取出，并使用forward headertransformer。

### Filters

[Same as in Spring MVC](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web.html#filters)

In the WebHandler API, you can use a WebFilter to apply interception-style logic before and after the rest of the processing chain of filters and the target WebHandler. When using the WebFlux Config, registering a WebFilter is as simple as declaring it as a Spring bean and (optionally) expressing precedence by using @Order on the bean declaration or by implementing Ordered.

在[WebHandler API](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-web-handler-api)中，可以使用WebFilter在过滤器和目标WebHandler的处理链的其余部分之前和之后应用拦截式逻辑。当使用[WebFlux配置](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-config)时，注册一个WebFilter就像声明它为一个Spring bean一样简单，并且(可选地)通过在bean声明上使用@Order或实现Ordered来表示优先级。

#### CORS

[Same as in Spring MVC](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web.html#filters-cors)

Spring WebFlux provides fine-grained support for CORS configuration through annotations on controllers. However, when you use it with Spring Security, we advise relying on the built-in CorsFilter, which must be ordered ahead of Spring Security’s chain of filters.

Spring WebFlux通过对控制器的注释提供了对CORS配置的细粒度支持。但是，当您将其与Spring Security一起使用时，我们建议依赖内置的CorsFilter，它必须在Spring Security的过滤器链之前订购。

See the section on [CORS](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-cors) and the [CORS WebFilter](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-cors-webfilter) for more details.

有关[CORS](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-cors)和[CORS WebFilter](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-cors-webfilter)的详细信息，请参阅这一节。

### Exceptions

[Same as in Spring MVC](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web.html#mvc-ann-customer-servlet-container-error-page)

In the [WebHandler API](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-web-handler-api), you can use a WebExceptionHandler to handle exceptions from the chain of WebFilter instances and the target WebHandler. When using the [WebFlux Config](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-config), registering a WebExceptionHandler is as simple as declaring it as a Spring bean and (optionally) expressing precedence by using @Order on the bean declaration or by implementing Ordered.

在[WebHandler API](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-web-handler-api)中，可以使用WebExceptionHandler处理来自WebFilter实例链和目标WebHandler的异常。当使用[WebFlux配置](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-config)时，注册一个WebExceptionHandler就像声明它为一个Spring bean一样简单，并且(可选地)通过在bean声明上使用@Order或实现Ordered来表示优先级。

The following table describes the available WebExceptionHandler implementations:

下表描述了可用的WebExceptionHandler实现:

| Exception Handler | Description |
|-------|:-------------|
| ResponseStatusExceptionHandler| Provides handling for exceptions of type [ResponseStatusException](https://docs.spring.io/spring-framework/docs/5.1.6.RELEASE/javadoc-api/org/springframework/web/server/ResponseStatusException.html) by setting the response to the HTTP status code of the exception.|
| WebFluxResponseStatusExceptionHandler | Extension of ResponseStatusExceptionHandler that can also determine the HTTP status code of a @ResponseStatus annotation on any exception.<br><br>This handler is declared in the [WebFlux Config](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-config). |

### Codecs

[Same as in Spring MVC](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/integration.html#rest-message-conversion)

The spring-web and spring-core modules provide support for serializing and deserializing byte content to and from higher level objects through non-blocking I/O with Reactive Streams back pressure. The following describes this support:

spring-web和spring-core模块支持通过非阻塞I/O(带有响应流返回压力)在更高级别对象之间序列化和反序列化字节内容。以下描述了这种支持:

[https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-codecs](https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/web-reactive.html#webflux-codecs)