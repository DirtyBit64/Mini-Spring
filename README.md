# Mini-Spring
以学习Spring框架为初心，实现一个简洁版的Spring。从0开始实现IoC、MVC、JDBC模板、AOP和线程池。
IoC是Minis的核心。我们将使用beanFactory来管理所有需要的bean。
MVC与IoC集成。将请求URI映射到控制器方法，使用处理器适配器来调用方法并处理响应，以及使用视图解析器来渲染返回消息(JSP)。
使用JDBC访问数据库，并支持连接池。支持PooledConnection。
实现切点(Pointcut)和自动代理创建器(AutoProxyCreator)。
使用注解和AOP实现异步(@Async)。
支持方法拦截器、方法前置通知和方法返回后通知。
支持RESTful。
基于JDBC模板实现MyBatis。

## :label: TODO 
- [x] 实现mini-ioc
- [x] 实现mini-mvc
- [x] 实现jdbc-template
- [x] 实现mini-aop

## Reference
参考仓库[Minis](https://github.com/YaleGuo/Minis)

## Contributing
PRs accepted.

## License
**nuist** © **ailab-cv**
