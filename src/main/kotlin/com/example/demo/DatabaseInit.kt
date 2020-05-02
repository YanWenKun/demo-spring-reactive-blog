package com.example.demo

import com.example.demo.model.Article
import com.example.demo.model.User
import com.example.demo.repository.ArticleRepository
import com.example.demo.repository.UserRepository
import io.r2dbc.h2.H2ConnectionConfiguration
import io.r2dbc.h2.H2ConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.connectionfactory.init.CompositeDatabasePopulator
import org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import java.sql.SQLException


@Configuration
@EnableR2dbcRepositories
internal class R2DBCConfiguration : AbstractR2dbcConfiguration() {

    /**
     * 显式配置 H2 连接
     */
    @Bean
    override fun connectionFactory(): H2ConnectionFactory {
        return H2ConnectionFactory(
                H2ConnectionConfiguration.builder()
                        .url("mem:testdb;DB_CLOSE_DELAY=-1;")
                        .username("sa")
                        .build()
        )
    }
}

@Configuration
class H2WebConsole {

    private var webServer: org.h2.tools.Server? = null
    private var server: org.h2.tools.Server? = null

    /**
     * 在 8082 端口打开 H2 Web 控制台
     * 在 9092 端口打开 H2 TCP 访问
     */
    @EventListener(ContextRefreshedEvent::class)
    @Throws(SQLException::class)
    fun start() {
        webServer = org.h2.tools.Server.createWebServer("-webPort", "8082", "-tcpAllowOthers").start()
        server = org.h2.tools.Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start()
    }

    @EventListener(ContextClosedEvent::class)
    fun stop() {
        webServer!!.stop()
        server!!.stop()
    }
}

@Configuration
class DatabaseInit {

    private val log: Logger = LoggerFactory.getLogger(DatabaseInit::class.java)

    /**
     * 执行 SQL，以进行数据库的建表操作
     * R2DBC 这套自然是不会像 JPA 一样帮着建表的，所以还得自己来
     * 参考： https://docs.spring.io/spring-data/r2dbc/docs/current-SNAPSHOT/reference/html/#r2dbc.init
     */
    @Bean
    fun databaseInitializer(@Qualifier("connectionFactory") connectionFactory: ConnectionFactory?): ConnectionFactoryInitializer? {
        val initializer = ConnectionFactoryInitializer()
        initializer.setConnectionFactory(connectionFactory!!)
        val populator = CompositeDatabasePopulator()
        populator.addPopulators(ResourceDatabasePopulator(ClassPathResource("sql/schema.sql")))
        populator.addPopulators(ResourceDatabasePopulator(ClassPathResource("sql/data.sql")))
        initializer.setDatabasePopulator(populator)
        log.info("开始数据库初始化")
        return initializer
    }

    /**
     * 构造一些不疼不痒的数据作为数据库初始化内容
     * 注意这里没有、也不能指定 user id
     * 想要更细化的控制，可以直接写 SQL（利用初始化中用到的 SQL 文件）
     */
    @Bean
    fun databaseFiller(userRepository: UserRepository,
                       articleRepository: ArticleRepository) = ApplicationRunner {

        val userY = userRepository.save(User(
                userName = "user_y",
                displayName = "Y",
                description = "Hey! It's always good time!"
        )).block()
        log.debug("存入一个示例用户： " + userY?.userName)

        articleRepository.save(Article(
                title = "硅基生命被发现！",
                digest = "硅基生命被发现！但是不在太阳系",
                content = "也许周围还有黑洞",
                author = userY?.id!!
        )).subscribe { log.debug("存入一篇示例文章： " + it.slug) }

        articleRepository.save(Article(
                title = "铁基生命被发现！",
                digest = "铁基生命被发现！聚变性质十分稳定！",
                content = "也许是最熵生物",
                author = userY?.id!!
        )).subscribe { log.debug("存入另一篇示例文章： " + it.slug) }
    }
}
