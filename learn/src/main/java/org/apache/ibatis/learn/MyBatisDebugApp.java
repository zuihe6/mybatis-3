/*
 *    Copyright 2009-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.learn;

import java.io.InputStream;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MyBatis 源码极简原生 Debug 调试入口
 * <p>
 * 核心调试链路与断点打法推荐：
 * <ul>
 *   <li><b>Step 1 (配置解析与容器构建)</b>：进入 {@link SqlSessionFactoryBuilder#build(InputStream)} ->
 *       {@code XMLConfigBuilder#parse()}，观察 {@code Configuration} 容器如何解析 settings/environments/mappers 并生成 {@code MappedStatement}。</li>
 *   <li><b>Step 2 (会话开启与执行器初始化)</b>：进入 {@link SqlSessionFactory#openSession()} ->
 *       {@code DefaultSqlSessionFactory#openSessionFromDataSource}，观察事务管理器 {@code Transaction} 与 {@code Executor}（SimpleExecutor / CachingExecutor）的包装与创建。</li>
 *   <li><b>Step 3 (动态代理生成)</b>：进入 {@link SqlSession#getMapper(Class)} ->
 *       {@code MapperRegistry#getMapper} -> {@code MapperProxyFactory#newInstance}，观察 JDK 动态代理类 {@code MapperProxy} 的诞生。</li>
 *   <li><b>Step 4 (SQL 执行与结果集映射全链路)</b>：进入 {@link UserMapper#selectById(Long)} ->
 *       {@code MapperProxy#invoke} -> {@code MapperMethod#execute} -> {@code DefaultSqlSession#selectOne} ->
 *       {@code Executor#query} -> 四大核心对象（{@code StatementHandler} -> {@code ParameterHandler} -> {@code ResultSetHandler}）的协同工作。</li>
 * </ul>
 */
public class MyBatisDebugApp {

    private static final Logger log = LoggerFactory.getLogger(MyBatisDebugApp.class);

    public static void main(String[] args) throws Exception {
        String resource = "mybatis-config.xml";

        // ==========================================
        // 1. 读取配置文件并构建 SqlSessionFactory
        // 【断点建议 1】：进入 build()，跟踪 XMLConfigBuilder 解析 XML 并填充 Configuration 过程
        // ==========================================
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        // ==========================================
        // 2. 获取 SqlSession 会话对象
        // 【断点建议 2】：进入 openSession()，跟踪 Executor 与 Transaction 的创建
        // ==========================================
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            // ==========================================
            // 3. 获取 Mapper 接口的动态代理实例
            // 【断点建议 3】：进入 getMapper()，跟踪 MapperProxy JDK 动态代理的创建
            // ==========================================
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // ==========================================
            // 4. 执行业务查询方法
            // 【断点建议 4】：进入 selectById()，跟踪 MapperProxy.invoke() 到底层 JDBC Statement 的执行与结果集封装
            // ==========================================
            Long targetId = 1L;
            log.info("========== [DEBUG] 开始执行 MyBatis 原生查询 (id={}) ==========", targetId);

            User user = userMapper.selectById(targetId);

            log.info("========== [DEBUG] 查询成功，映射结果如下 ==========");
            log.info("{}", user);
        }
    }
}
