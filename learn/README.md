# 📚 MyBatis 3 源码深度研读与架构实战路线大纲

> **研读原则**：符合“从宏观架构 -> 基础支撑与配置解析 -> SQL 执行核心链路 -> 高级特性（缓存/插件/延迟加载） -> 设计模式总结”的由浅入深认知规律。

```
宏观全景与基础支撑 ➔ 配置解析与初始化 ➔ 代理绑定与动态SQL ➔ 执行器核心链路 ➔ 高级特性与设计模式
```

---

## 📑 章节总览

| 章节 | 核心主题 | 核心关注点 |
| :--- | :--- | :--- |
| **第 01 章** | [宏观全景架构与调试环境搭建](#第-1-章mybatis-宏观全景架构与调试环境搭建) | 三层架构、`Configuration` 全局容器、Debug 环境打通 |
| **第 02 章** | [基础支撑层核心剖析](#第-2-章基础支撑层核心剖析反射类型系统与日志适配) | `Reflector` 反射缓存、`TypeHandler` 类型映射、统一日志适配 |
| **第 03 章** | [配置解析与启动初始化流程](#第-3-章配置解析与启动初始化流程xml--annotation) | `XPathParser`、`XMLConfigBuilder`、`MappedStatement` 注册 |
| **第 04 章** | [Mapper 接口绑定的底层奥秘](#第-4-章mapper-接口绑定的底层奥秘动态代理与注册中心) | `MapperRegistry`、JDK 动态代理、方法路由机制 |
| **第 05 章** | [动态 SQL 解析与 SQL 语法树构建](#第-5-章动态-sql-解析与-sql-语法树构建) | `SqlNode` 组合模式、AST 语法树、`#{}` 与 `${}` 占位符提取 |
| **第 06 章** | [SQL 执行核心链路与“四大核心金刚”](#第-6-章sql-执行核心链路与四大核心金刚) | `Executor`、`StatementHandler`、`ParameterHandler`、`ResultSetHandler` |
| **第 07 章** | [深度揭秘两级缓存架构](#第-7-章深度揭秘两级缓存架构一级缓存与二级缓存) | `PerpetualCache`、`CachingExecutor` 事务暂存区、装饰器链 |
| **第 08 章** | [插件（Interceptor）拦截器机制与底层实现](#第-8-章插件interceptor拦截器机制与底层实现) | 责任链模式、JDK 动态代理切入、插件实战开发规范 |
| **第 09 章** | [结果集深层映射与延迟加载机制](#第-9-章结果集深层映射与延迟加载机制) | 嵌套映射图谱、循环引用处理、CGLIB/Javassist 延迟加载 |
| **第 10 章** | [MyBatis 设计模式全景总结与架构思想升华](#第-10-章mybatis-设计模式全景总结与架构思想升华) | 7 大经典设计模式总结、轻量级 ORM 设计哲学 |

---

## 📖 详细大纲

### 第 1 章：MyBatis 宏观全景架构与调试环境搭建
* **核心类与接口**：
  * `org.apache.ibatis.session.SqlSessionFactoryBuilder`
  * `org.apache.ibatis.session.SqlSessionFactory`
  * `org.apache.ibatis.session.SqlSession`
  * `org.apache.ibatis.session.Configuration`
* **学习目标**：
  1. 建立 MyBatis 三层架构认知（接口 API 层、核心处理层、基础支撑层）。
  2. 梳理全局唯一配置中心 `Configuration` 的生命周期与重要地位。
  3. 基于 `learn` 模块跑通最简 Demo 并建立源码级断点调试路径。

---

### 第 2 章：基础支撑层核心剖析（反射、类型系统与日志适配）
* **核心类与接口**：
  * `org.apache.ibatis.reflection.Reflector` / `MetaClass` / `MetaObject`
  * `org.apache.ibatis.type.TypeHandler` / `TypeHandlerRegistry` / `BaseTypeHandler`
  * `org.apache.ibatis.logging.Log` / `LogFactory`
* **学习目标**：
  1. 深入 MyBatis 高性能反射元数据缓存机制（比普通 Java 原生反射快在哪）。
  2. 掌握 Java 类型与 JDBC `JdbcType` 之间的转换与类型处理器自动匹配规则。
  3. 理解 MyBatis 统一日志组件如何优雅适配各类第三方日志框架。

---

### 第 3 章：配置解析与启动初始化流程（XML / Annotation）
* **核心类与接口**：
  * `org.apache.ibatis.parsing.XPathParser`
  * `org.apache.ibatis.builder.xml.XMLConfigBuilder`
  * `org.apache.ibatis.builder.xml.XMLMapperBuilder`
  * `org.apache.ibatis.builder.xml.XMLStatementBuilder`
  * `org.apache.ibatis.mapping.MappedStatement`
* **学习目标**：
  1. 追踪从 `mybatis-config.xml` 到各类 `*Mapper.xml` 的加载与 DOM 节点解析全过程。
  2. 彻底掌握核心配置载体 `MappedStatement` 的属性构成及其在 `Configuration` 中的注册。

---

### 第 4 章：Mapper 接口绑定的底层奥秘（动态代理与注册中心）
* **核心类与接口**：
  * `org.apache.ibatis.binding.MapperRegistry`
  * `org.apache.ibatis.binding.MapperProxyFactory`
  * `org.apache.ibatis.binding.MapperProxy`
  * `org.apache.ibatis.binding.MapperMethod` (`SqlCommand` / `MethodSignature`)
* **学习目标**：
  1. 弄懂为何只有 Mapper 接口没有实现类却能执行 SQL（JDK 动态代理机制）。
  2. 理清方法调用如何桥接路由至 `SqlSession` 对应的底层增删改查 API。

---

### 第 5 章：动态 SQL 解析与 SQL 语法树构建
* **核心类与接口**：
  * `org.apache.ibatis.scripting.xmltags.XMLLanguageDriver` / `LanguageDriver`
  * `org.apache.ibatis.mapping.SqlSource` / `DynamicSqlSource` / `RawSqlSource`
  * `org.apache.ibatis.scripting.xmltags.SqlNode` (及其子类 `IfSqlNode`, `ForEachSqlNode`, `TrimSqlNode`, `MixedSqlNode` 等)
  * `org.apache.ibatis.scripting.xmltags.DynamicContext` / `BoundSql`
* **学习目标**：
  1. 深入理解组合模式解析 `<if>`、`<where>`、`<foreach>` 等标签生成 AST 树形结构。
  2. 掌握运行时参数解析、OGNL 表达式求值及 `#{}` 与 `${}` 占位符替换机制。

---

### 第 6 章：SQL 执行核心链路与“四大核心金刚”
* **核心类与接口**：
  * `org.apache.ibatis.executor.Executor` (`BaseExecutor`, `SimpleExecutor`, `ReuseExecutor`, `BatchExecutor`)
  * `org.apache.ibatis.executor.statement.StatementHandler` (`RoutingStatementHandler`, `PreparedStatementHandler`)
  * `org.apache.ibatis.executor.parameter.ParameterHandler` (`DefaultParameterHandler`)
  * `org.apache.ibatis.executor.resultset.ResultSetHandler` (`DefaultResultSetHandler`)
* **学习目标**：
  1. 全面打通 MyBatis 最关键的 SQL 执行流水线：
     `SqlSession ➔ Executor ➔ StatementHandler ➔ ParameterHandler ➔ ResultSetHandler`
  2. 掌握三种基本执行器（Simple / Reuse / Batch）的底层 JDBC Statement 差异与应用场景。

---

### 第 7 章：深度揭秘两级缓存架构（一级缓存与二级缓存）
* **核心类与接口**：
  * `org.apache.ibatis.cache.Cache` / `PerpetualCache`
  * `org.apache.ibatis.executor.CachingExecutor`
  * `org.apache.ibatis.cache.TransactionalCacheManager` / `TransactionalCache`
  * 缓存装饰器系列（`LruCache`, `SerializedCache`, `SynchronizedCache`, `LoggingCache` 等）
* **学习目标**：
  1. 掌握一级缓存（会话级/Local Cache）与二级缓存（Namespace 级）的存储位置与生命周期。
  2. 弄清脏读防范、事务性缓存（暂存区）在事务提交与回滚时的同步机制。
  3. 学习装饰器模式在缓存职责拆解中的优雅应用。

---

### 第 8 章：插件（Interceptor）拦截器机制与底层实现
* **核心类与接口**：
  * `org.apache.ibatis.plugin.Interceptor`
  * `org.apache.ibatis.plugin.Invocation`
  * `org.apache.ibatis.plugin.Plugin`
  * `org.apache.ibatis.plugin.InterceptorChain`
  * `@Intercepts` / `@Signature`
* **学习目标**：
  1. 理解基于“责任链模式 + JDK 动态代理”的拦截体系。
  2. 掌握插件如何针对“四大核心金刚”的方法调用链实现无侵入切入与增强。
  3. 掌握编写自定义插件（如：全链路 SQL 耗时监控、多租户行级数据隔离、通用分页插件）的最佳实践。

---

### 第 9 章：结果集深层映射与延迟加载机制
* **核心类与接口**：
  * `org.apache.ibatis.executor.resultset.DefaultResultSetHandler`
  * `org.apache.ibatis.mapping.ResultMap` / `ResultMapping`
  * `org.apache.ibatis.executor.loader.ResultLoader` / `ResultLoaderMap`
  * `org.apache.ibatis.executor.loader.ProxyFactory` (`JavassistProxyFactory`, `CglibProxyFactory`)
* **学习目标**：
  1. 拆解复杂嵌套查询（`association` / `collection`）的对象图组装与循环引用解决方案。
  2. 掌握基于字节码增强（CGLIB / Javassist）的延迟加载代理对象的生成与触发逻辑。

---

### 第 10 章：MyBatis 设计模式全景总结与架构思想升华
* **涉及设计模式**：
  * **建造者模式**：`SqlSessionFactoryBuilder`, `XMLConfigBuilder`, `MappedStatement.Builder`
  * **工厂模式**：`SqlSessionFactory`, `TransactionFactory`, `MapperProxyFactory`
  * **代理模式**：`MapperProxy`, `Plugin`, `ConnectionLogger`
  * **装饰器模式**：`CachingExecutor`, `Cache` 体系
  * **模板方法模式**：`BaseExecutor`, `BaseTypeHandler`
  * **组合模式**：`SqlNode` 及其子类
  * **责任链模式**：`InterceptorChain`
* **学习目标**：
  1. 提炼 MyBatis 核心设计模式与高内聚、低耦合架构设计哲学。
  2. 形成一套“如何设计一个轻量级 ORM 框架”的完整思维模型，全面提升架构素养与代码品味。
