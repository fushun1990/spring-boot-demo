# Profiles
1、对 @Component or @Configuration 有效

2、可以添加多个活动配置文件
```yml
spring.profiles.active=dev,hsqldb
```


YAML文档按其遇到的顺序合并。 以后的值会覆盖以前的值。

# 两种指定配置的方式
1、要对属性文件执行相同的操作，可以使用application-${profile}.yml 指定特定于概要文件的值

2、spring.profiles: ${profile} 的方式，操作配置
此种方式，因为spring boot 默认查找的只有2个文件application.yml。所有的配置，都在一个文件中不方便编辑。<br/>
  